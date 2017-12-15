package com.alcor.ril.service;

import com.alcor.ril.persistence.entity.RilRolePermissionEntity;
import com.alcor.ril.persistence.entity.SysPermissionEntity;
import com.alcor.ril.persistence.entity.SysRoleEntity;
import com.alcor.ril.persistence.repository.IRolePermissionRepository;
import com.alcor.ril.persistence.repository.IRoleRepository;
import com.alcor.ril.persistence.repository.ISysPermissionRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


/**
 * @author roamer - 徐泽宇
 * @create 2017-08-2017/8/8  下午5:57
 */
@Slf4j
@Data
@Service()
@Transactional
@CacheConfig(cacheNames = "spring:cache:SystemPermission:Role")
public class RoleService {

    @Autowired
    SystemMenuService systemMenuService;

    @Autowired
    IRoleRepository iRoleRepository;

    @Autowired
    IRolePermissionRepository iRolePermissionRepository;

    @Autowired
    ISysPermissionRepository iSysPermissionRepository;


    @Transactional(readOnly = true)
    public List<SysRoleEntity> findAll() throws ServiceException {
        return iRoleRepository.findAll();
    }

    @Transactional(readOnly = true)
    public SysRoleEntity findById(String id) throws ServiceException {
        return iRoleRepository.findOne(id);
    }

    public SysRoleEntity update(SysRoleEntity roleEntity) throws ServiceException {
        return iRoleRepository.save(roleEntity);
    }

    public void delete(String id) throws ServiceException {
        iRoleRepository.delete(id);
    }

    /**
     * 给一个角色分配一个菜单
     * 在数据库里面只保存叶菜单！！！ 不保存枝干菜单
     *
     * @param menuId
     * @param roleId
     *
     * @throws ServiceException
     */
    @CacheEvict(key = "'Permission_By_RoleID_'.concat(#a0)", allEntries = true)
    public void assignMenu(String menuId, String roleId) throws ServiceException {
        //查看菜单项下是否有子菜单
        List<SysPermissionEntity> sysPermissionEntityList = iSysPermissionRepository.findAllByParentIdOrderByOrderNum(menuId);
        if (sysPermissionEntityList == null || sysPermissionEntityList.size() == 0) {
            if (!menuId.equals("0")) {
                RilRolePermissionEntity rilRolePermissionEntity = new RilRolePermissionEntity();
                rilRolePermissionEntity.setPermissionId(menuId);
                rilRolePermissionEntity.setRoleId(roleId);
                iRolePermissionRepository.save(rilRolePermissionEntity);
            }
        } else {
            log.debug("发现{}个子菜单", sysPermissionEntityList.size());
            for (int i = 0; i < sysPermissionEntityList.size(); i++) {
                assignMenu(sysPermissionEntityList.get(i).getId(), roleId);
            }
        }

    }


    /**
     * 获取一个角色的菜单的 id 集合
     *
     * @param roleId
     *
     * @return
     */
    @Transactional(readOnly = true)
    @Cacheable(key = "'Permission_By_RoleID_'.concat(#a0)")
    public List<String> findMenusByRole(String roleId) {
        log.debug("spring cache 没有命中，开始做数据库查询和处理！");
        List<RilRolePermissionEntity> rilRolePermissionEntityList = iRolePermissionRepository.findAllByRoleId(roleId);
        List<String> roleMenus = new ArrayList<String>();
        rilRolePermissionEntityList.forEach(item -> {
            roleMenus.add(item.getPermissionId());
        });
        return roleMenus;
    }

    /**
     * 按照传入的菜单 id，删除角色菜单
     * 如果传入的是 支菜单 id，则要删除角色权限表里面的所有包含在这个支菜单下面的所有子孙菜单项
     *
     * @param menuId
     * @param roleId
     */
    @CacheEvict(key = "'Permission_By_RoleID_'.concat(#a1)", allEntries = true)
    public void delete(String menuId, String roleId) throws ServiceException {
        // 先从系统菜单表里面，获取菜单 id 和下面所有的子孙菜单 id
        List<String> childenMenuIdList = new ArrayList<>();
        childenMenuIdList.add(menuId);
        //从系统权限表中获取此菜单项下的所有子孙项的 id 列表
        systemMenuService.getSystemMenuIDsWithParent(childenMenuIdList, menuId);
        log.debug("总共发现菜单{}下有{}条子孙菜单", menuId, childenMenuIdList.size());
        //在角色权限表里面删除这些记录
        iRolePermissionRepository.deleteAllByRoleIdAndPermissionIdIn(roleId, childenMenuIdList);
    }
}
