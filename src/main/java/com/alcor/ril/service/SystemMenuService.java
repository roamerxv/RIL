package com.alcor.ril.service;

import com.alcor.ril.controller.bean.SystemMenu;
import com.alcor.ril.entity.SystemMenuEntity;
import com.alcor.ril.repository.ISystemMenuRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

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
    @Qualifier("com.alcor.ril.repository.ISystemMenuRepository")
    @Autowired
    private ISystemMenuRepository iSystemMenuRepository;

    /**
     * 获取所有的 菜单项的链表结构
     * @return
     * @throws ServiceException
     */
    public List<SystemMenu> getSystemMenusWithRoot() throws ServiceException{
        return getSystemMenusWithParent("0");
    }

    public List<SystemMenu>  getSystemMenusWithParent (String parentId) throws  ServiceException{
        List<SystemMenuEntity> systemMenuEntityList  = iSystemMenuRepository.findAllByParentId(parentId);
        if (systemMenuEntityList == null){
            return null;
        }
//        log.debug("发现{}条，父id 是{}的菜单记录", systemMenuEntityList.size(),parentId);
        List<SystemMenu> systemMenuList = new ArrayList<>(systemMenuEntityList.size());
        for (SystemMenuEntity item : systemMenuEntityList){
            SystemMenu systemMenu = new SystemMenu();
            systemMenu.setMenuItem(item);
            systemMenu.setChildrenMenu(this.getSystemMenusWithParent(item.getId()));
            systemMenuList.add(systemMenu);
        }
        return systemMenuList;
    }


    public SystemMenuEntity getMenuItemById(String id) throws ServiceException{
        return iSystemMenuRepository.findOne(id);
    }

    public SystemMenuEntity update(SystemMenuEntity systemMenuEntity) throws  ServiceException{
        return iSystemMenuRepository.save(systemMenuEntity);
    }
    
}
