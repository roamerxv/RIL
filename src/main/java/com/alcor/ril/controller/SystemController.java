package com.alcor.ril.controller;

import com.alcor.ril.controller.bean.ServerInfo;
import com.alcor.ril.controller.bean.SystemMenu;
import com.alcor.ril.controller.bean.treeview.Item;
import com.alcor.ril.controller.bean.treeview.ItemState;
import com.alcor.ril.entity.SystemMenuEntity;
import com.alcor.ril.service.ServiceException;
import com.alcor.ril.service.SystemMenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import pers.roamer.boracay.aspect.httprequest.SessionCheckKeyword;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 系统级别的控制操作
 *
 * @author roamer - 徐泽宇
 * @create 2017-11-2017/11/29  下午6:36
 */
@Slf4j
@RestController("com.alcor.cli.controller.SystemController")
@SessionCheckKeyword
public class SystemController extends BaseController {

    @Autowired
    private SystemMenuService systemMenuService;

    /**
     * 查看服务器信息，主要是用于查看集群状态
     *
     * @param request
     *
     * @return
     */
    @GetMapping("/serverInfo")
    public ModelAndView serverInfo(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("/test/server-info");
        ServerInfo serverInfo = new ServerInfo();
        serverInfo.setServerIP(request.getLocalAddr());
        serverInfo.setSessionID(httpSession.getId());
        serverInfo.setLocalPort(request.getLocalPort());
        serverInfo.setContextPath(request.getContextPath());
        modelAndView.addObject("serverInfo", serverInfo);
        return modelAndView;
    }

    @GetMapping("/cleanCache")
    @CacheEvict(cacheNames = {"spring:cache:UserEntity", "spring:cache:SystemMenuEntity" }, allEntries = true)
    public ModelAndView cleanCache() {
        log.debug("清除spring cache 中的缓存！");
        ModelAndView modelAndView = new ModelAndView("/index");
        return modelAndView;
    }

    @GetMapping("/systemMenuMaintain")
    public ModelAndView systemMenuMaintain() {
        ModelAndView modelAndView = new ModelAndView("/system/systemMenu/index");
        return modelAndView;
    }

    /**
     * 获取系统菜单信息
     *
     * @return
     *
     * @throws ControllerException
     */
    @GetMapping("/systemMenus4Treeviewer")
    @ResponseBody
    public List<Item> getystemMenu4Treeviewer() throws ControllerException {
        log.debug("开始获取系统菜单,用于显示在 TreeViewer");
        try {
            return this.parseSystemMenu(systemMenuService.getSystemMenusWithRoot());
        } catch (ServiceException e) {
            throw new ControllerException(e.getMessage());
        }
    }

    /**
     * 递归把系统菜单的对象链表结构转换成用于 treeviewer 显示的链表结构
     * @param systemMenuList
     * @return
     * @throws ControllerException
     */
    private List<Item> parseSystemMenu(List<SystemMenu> systemMenuList) throws ControllerException {
        if (systemMenuList == null) {
            return null;
        }
        List<Item> treeViewItemList = new ArrayList<>(systemMenuList.size());
        for (SystemMenu item : systemMenuList) {
            Item treeViewItem = new Item();
            treeViewItem.setId(item.getMenuItem().getId());
            treeViewItem.setParent(item.getMenuItem().getParentId());
            treeViewItem.setText(item.getMenuItem().getName());
            treeViewItem.setIcon(item.getMenuItem().getClazz());
            treeViewItem.setUrl(item.getMenuItem().getUrl());
            ItemState itemState = new ItemState();
            itemState.setOpened(true);
            treeViewItem.setState(itemState);

            treeViewItemList.add(treeViewItem);
            if (!item.getChildrenMenu().isEmpty()) {
                treeViewItem.setChildren(this.parseSystemMenu(item.getChildrenMenu()));
            }
        }
        return treeViewItemList;
    }

    /**
     * 根据一个 systemMenuEntity ，保存更新到数据库
     * @return
     * @throws ControllerException
     */
    @PutMapping("/systemMenu")
    public SystemMenuEntity updateSystemMenuItem(@RequestBody SystemMenuEntity systemMenuEntity) throws  ControllerException{
        log.debug("开始 更新 id 是：{}的菜单项信息",systemMenuEntity.getId());
        try {
            SystemMenuEntity systemMenuEntityInDB = systemMenuService.getMenuItemById(systemMenuEntity.getId());
            if ( systemMenuEntityInDB == null){
                throw new ControllerException("要更新的菜单项不存在!");
            }
            systemMenuEntityInDB.setUrl(systemMenuEntity.getUrl());
            systemMenuEntityInDB.setName(systemMenuEntity.getName());
            systemMenuEntityInDB.setClazz(systemMenuEntity.getClazz());
            return  systemMenuService.update(systemMenuEntityInDB);
        } catch (ServiceException e) {
            log.error(e.getMessage(),e);
            throw new ControllerException(e.getMessage());
        }
    }

    /**
     * 根据一个 systemMenuEntity ，新增到数据库
     * @return
     * @throws ControllerException
     */
    @PostMapping("/systemMenu")
    public SystemMenuEntity getSystemMenuItem(@RequestBody SystemMenuEntity systemMenuEntity) throws  ControllerException{
        log.debug("开始 增加一个菜单项信息");
        try {
            systemMenuEntity.setParentId(systemMenuEntity.getId());
            systemMenuEntity.setId(UUID.randomUUID().toString());
            systemMenuEntity.setLabelClazz("");
            return systemMenuService.update(systemMenuEntity);
        } catch (ServiceException e) {
            log.error(e.getMessage(),e);
            throw new ControllerException(e.getMessage());
        }
    }
}


