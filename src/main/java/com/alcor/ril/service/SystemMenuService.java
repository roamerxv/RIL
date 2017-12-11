package com.alcor.ril.service;

import com.alcor.ril.persistence.entity.SysPermissionEntity;
import com.alcor.ril.persistence.repository.ISysPermissionRepository;
import com.alcor.ril.web.controller.bean.SystemMenu;
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
    public List<SystemMenu> getSystemMenusWithRoot() throws ServiceException {
        return getSystemMenusWithParent(new Long(0));
    }

    public List<SystemMenu> getSystemMenusWithParent(Long parentId) throws ServiceException {
        List<SysPermissionEntity> sysPermissionEntityList = iSysPermissionRepository.findAllByParentIdOrderByOrderNum(parentId);
        if (sysPermissionEntityList == null) {
            return null;
        }
//        log.debug("发现{}条，父id 是{}的菜单记录", systemMenuEntityList.size(),parentId);
        List<SystemMenu> sysPermissionChilden = new ArrayList<>(sysPermissionEntityList.size());
        for (SysPermissionEntity item : sysPermissionEntityList) {
            SystemMenu systemMenu = new SystemMenu();
            systemMenu.setMenuItem(item);
            systemMenu.setChildrenMenu(this.getSystemMenusWithParent(item.getId()));
            sysPermissionChilden.add(systemMenu);
        }
        return sysPermissionChilden;
    }


    public SysPermissionEntity getMenuItemById(Long id) throws ServiceException {
        return iSysPermissionRepository.findOne(id);
    }

    public SysPermissionEntity update(SysPermissionEntity sysPermissionEntity) throws ServiceException {
        return iSysPermissionRepository.save(sysPermissionEntity);
    }

    /**
     * 修改当前系统菜单项的父ID
     * @param id
     * @param pid
     * @return
     * @throws ServiceException
     */
    @Transactional
    public SysPermissionEntity updateParent(Long id, Long pid) throws ServiceException {
        SysPermissionEntity sysPermissionEntity = iSysPermissionRepository.findOne(id);
        sysPermissionEntity.setParentId(pid);
        iSysPermissionRepository.save(sysPermissionEntity);
        return sysPermissionEntity;
    }

    @Transactional
    public void resort(List<Long> children) throws  ServiceException{
        for (int i=0 ; i < children.size() ;i++){
            log.debug("开始把id 是{}的菜单项下的 sortNum 改成{}",children.get(i),i);
            iSysPermissionRepository.changeOrderNumWithId(children.get(i),i);
        }
    }

}
