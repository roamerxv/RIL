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
public class RoleService {

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
    @Transactional
    public void assignMenu(String menuId, String roleId) throws ServiceException {
        //查看菜单项下是否有子菜单
        List<SysPermissionEntity> sysPermissionEntityList = iSysPermissionRepository.findAllByParentIdOrderByOrderNum(menuId);
        if (sysPermissionEntityList == null || sysPermissionEntityList.size() == 0) {
            log.debug("没有子菜单了");
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

    public List<String> findMenusByRole(String roleId)  {
        List<RilRolePermissionEntity> rilRolePermissionEntityList = iRolePermissionRepository.findAllByRoleId(roleId);
        List<String> roleMenus = new ArrayList<String>();
        rilRolePermissionEntityList.forEach(item -> {
            roleMenus.add(item.getPermissionId());
        });
        return roleMenus;
    }
}
