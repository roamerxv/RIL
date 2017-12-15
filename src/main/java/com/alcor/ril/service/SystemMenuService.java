package com.alcor.ril.service;

import com.alcor.ril.persistence.entity.SysPermissionEntity;
import com.alcor.ril.persistence.repository.ISysPermissionRepository;
import com.alcor.ril.web.controller.bean.MenuNode;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * @author roamer - 徐泽宇
 * @create 2017-08-2017/8/8  下午5:57
 */
@Slf4j
@Data
@Service("com.alcor.ril.service.SystemMenuService")
public class SystemMenuService {
    @Autowired
    private ISysPermissionRepository iSysPermissionRepository;

    /**
     * 获取所有的 菜单项的链表结构
     *
     * @return
     *
     * @throws ServiceException
     */
    public List<MenuNode> getSystemMenusWithRoot() throws ServiceException {
        return getSystemMenusWithParent("0");
    }

    /**
     * 以 MenuNode 对象的方式，返回所有的子孙链表
     *
     * @param parentId
     *
     * @return
     *
     * @throws ServiceException
     */
    public List<MenuNode> getSystemMenusWithParent(String parentId) throws ServiceException {
        List<SysPermissionEntity> sysPermissionEntityList = iSysPermissionRepository.findAllByParentIdOrderByOrderNum(parentId);
        if (sysPermissionEntityList == null) {
            return null;
        }
//        log.debug("发现{}条，父id 是{}的菜单记录", systemMenuEntityList.size(),parentId);
        List<MenuNode> sysPermissionChilden = new ArrayList<>(sysPermissionEntityList.size());
        for (SysPermissionEntity item : sysPermissionEntityList) {
            MenuNode systemMenu = new MenuNode();
            systemMenu.setMenuItem(item);
            systemMenu.setChildrenMenu(this.getSystemMenusWithParent(item.getId()));
            sysPermissionChilden.add(systemMenu);
        }
        return sysPermissionChilden;
    }


    /**
     * 获取一个 菜单 id 对应的项目的所有子孙菜单项的 id 数组
     * @param results
     * @param parentId
     * @throws ServiceException
     */
    public void getSystemMenuIDsWithParent(List<String> results, String parentId) throws ServiceException {
        List<SysPermissionEntity> sysPermissionEntityList = iSysPermissionRepository.findAllByParentIdOrderByOrderNum(parentId);
        if (sysPermissionEntityList == null) {
        }else{
            for (SysPermissionEntity item : sysPermissionEntityList) {
                results.add(item.getId());
                this.getSystemMenuIDsWithParent(results, item.getId());
            }
        }
    }


    public SysPermissionEntity getMenuItemById(String id) throws ServiceException {
        return iSysPermissionRepository.findOne(id);
    }

    public SysPermissionEntity update(SysPermissionEntity sysPermissionEntity) throws ServiceException {
        return iSysPermissionRepository.save(sysPermissionEntity);
    }

    public List<SysPermissionEntity> findByPsermission(String permission) throws ServiceException {
        return iSysPermissionRepository.findByPermission(permission);
    }

    /**
     * 修改当前系统菜单项的父ID
     *
     * @param id
     * @param pid
     *
     * @return
     *
     * @throws ServiceException
     */
    @Transactional
    public SysPermissionEntity updateParent(String id, String pid) throws ServiceException {
        SysPermissionEntity sysPermissionEntity = iSysPermissionRepository.findOne(id);
        sysPermissionEntity.setParentId(pid);
        iSysPermissionRepository.save(sysPermissionEntity);
        return sysPermissionEntity;
    }

    @Transactional
    public void resort(List<String> children) throws ServiceException {
        for (int i = 0; i < children.size(); i++) {
            log.debug("开始把id 是{}的菜单项下的 sortNum 改成{}", children.get(i), i);
            iSysPermissionRepository.changeOrderNumWithId(children.get(i), i);
        }
    }

    /**
     * 删除id 对应的菜单和其下所有的子孙菜单
     *
     * @param id
     *
     * @throws ServiceException
     */
    @Transactional
    public void deleteByIdIncludedChilden(String id) throws ServiceException {
        log.debug("开始把 id 为{}的菜单，以及下面所有的子孙菜单都删除", id);
        //查看是否下面有子菜单
        List<SysPermissionEntity> sysPermissionEntityList = iSysPermissionRepository.findAllByParentIdOrderByOrderNum(id);
        if (sysPermissionEntityList == null || sysPermissionEntityList.size() == 0) {
            //没有子菜单了。删除自身
            log.debug("没有子菜单了。删除自身");
            iSysPermissionRepository.delete(id);
        } else {
            //递归调用，进行查询
            for (int i = 0; i < sysPermissionEntityList.size(); i++) {
                deleteByIdIncludedChilden(sysPermissionEntityList.get(i).getId());
            }
        }
    }
}
