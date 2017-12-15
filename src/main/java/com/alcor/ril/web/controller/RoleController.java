package com.alcor.ril.web.controller;

import com.alcor.ril.persistence.entity.SysRoleEntity;
import com.alcor.ril.service.RoleService;
import com.alcor.ril.service.ServiceException;
import com.alcor.ril.service.SystemMenuService;
import com.alcor.ril.web.controller.bean.treeview.Item;
import com.alcor.ril.web.controller.bean.treeview.Tools;
import com.fasterxml.jackson.databind.ser.Serializers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import pers.roamer.boracay.aspect.businesslogger.BusinessMethod;
import pers.roamer.boracay.aspect.httprequest.SessionCheckKeyword;
import pers.roamer.boracay.helper.HttpResponseHelper;

import java.util.List;
import java.util.UUID;

/**
 * 角色维护的 Controller 类
 *
 * @author roamer - 徐泽宇
 * @create 2017-12-2017/12/6  下午6:19
 */
@Slf4j
@RestController("com.alcor.ril.controller.RoleController")
@SessionCheckKeyword(checkIt = true)
public class RoleController extends Serializers.Base {

    @Autowired
    private RoleService roleService;

    @Autowired
    private SystemMenuService systemMenuService;


    /**
     * 跳转到角色维护的 index 界面
     *
     * @return
     *
     * @throws ControllerException
     */
    @GetMapping(value = "/roleMaintain")
    public ModelAndView showIndex() throws ControllerException {
        ModelAndView modelAndView = new ModelAndView("/system/role/index");
        return modelAndView;
    }

    /**
     * 获取所有 role 信息，以 jquery Datatables 需要的 json 结构返回
     *
     * @return
     *
     * @throws ControllerException
     */
    @GetMapping(value = "/role/getData4Datatables")
    @ResponseBody
    public List<SysRoleEntity> getAllRoleInfo4Datatables() throws ControllerException {
        log.debug("获取jquery datatales 用的所有的角色信息");
        try {
            List<SysRoleEntity> roleEntityList = roleService.findAll();
            return roleEntityList;
        } catch (ServiceException e) {
            log.error(e.getMessage(), e);
            throw new ControllerException(e.getMessage());
        }
    }

    /**
     * 获取一个角色的详细信息
     *
     * @param id
     *
     * @return
     *
     * @throws ControllerException
     */
    @GetMapping(value = "/role/{id}")
    public SysRoleEntity getById(@PathVariable String id) throws ControllerException {
        log.debug("获取 id 是{}的角色信息", id);
        try {
            return roleService.findById(id);
        } catch (ServiceException e) {
            log.error(e.getMessage(), e);
            throw new ControllerException(e.getMessage());
        }
    }

    /**
     * 更新一个角色的信息
     *
     * @param roleEntity
     *
     * @return
     *
     * @throws ControllerException
     */
    @BusinessMethod(value = "更新一个角色的信息")
    @PutMapping(value = "/role")
    @ResponseBody
    public String udpate(@RequestBody SysRoleEntity roleEntity) throws ControllerException {
        log.debug("要更新的角色 id 是{},名称是{}", roleEntity.getId(), roleEntity.getRole());
        SysRoleEntity roleEntityInDb = null;
        try {
            roleEntityInDb = roleService.findById(roleEntity.getId());
            roleEntityInDb.setRole(roleEntity.getRole());
            roleEntityInDb.setDescription(roleEntity.getDescription());
            roleService.update(roleEntityInDb);
            return HttpResponseHelper.successInfoInbox("更新成功");
        } catch (ServiceException e) {
            log.error(e.getMessage(), e);
            throw new ControllerException(e.getMessage());
        }
    }

    /**
     * 新增一个角色记录
     *
     * @param roleEntity
     *
     * @return
     *
     * @throws ControllerException
     */
    @BusinessMethod(value = "新增一个角色记录")
    @PostMapping(value = "/role")
    public SysRoleEntity create(@RequestBody SysRoleEntity roleEntity) throws ControllerException {
        roleEntity.setId(UUID.randomUUID().toString());
        try {
            return roleService.update(roleEntity);
        } catch (ServiceException e) {
            log.error(e.getMessage(), e);
            throw new ControllerException(e.getMessage());
        }
    }

    /**
     * 删除一个角色信息
     *
     * @param id
     *
     * @return
     *
     * @throws ControllerException
     */
    @BusinessMethod(value = "删除一个角色信息")
    @DeleteMapping(value = "/role/{id}")
    @ResponseBody
    public String deleteById(@PathVariable String id) throws ControllerException {
        log.debug("要删除的角色 id 是{}", id);
        try {
            roleService.delete(id);
            return HttpResponseHelper.successInfoInbox("删除成功");
        } catch (ServiceException e) {
            log.error(e.getMessage(), e);
            throw new ControllerException(e.getMessage());
        }
    }


    /**
     * 获取一个角色对应的菜单链表结构
     *
     * @param id
     *
     * @return
     *
     * @throws ControllerException
     */
    @SessionCheckKeyword(checkIt = false)
    @GetMapping(value = "/role/showMenus/{id}")
    @ResponseBody
    public List<Item> showMenus(@PathVariable String id) throws ControllerException {
        log.debug("开始获取角色下的菜单项");
        List<String> menu_ids = roleService.findMenusByRole(id);
        try {
            Tools tools = new Tools();
            List<Item> items = tools.parseSystemMenu(systemMenuService.getSystemMenusWithRoot());
            tools.filtIncludeIds(items, menu_ids);
            log.debug("角色菜单生成完成");
            return items;
        } catch (ServiceException e) {
            throw new ControllerException(e.getMessage());
        }
    }

    /**
     * 给角色分配一个菜单
     * 包括此菜单下面的所有子孙菜单
     *
     * @param menuId
     * @param roleId
     *
     * @return
     *
     * @throws ControllerException
     */
    @BusinessMethod(value = "给角色分配菜单")
    @PostMapping(value = "/assign-menu-to-role/{menuId}/{roleId}")
    @ResponseBody
    public String assignMenu2Role(@PathVariable String menuId, @PathVariable String roleId) throws ControllerException {
        log.debug("分配一个菜单{}给角色{}", menuId, roleId);
        try {
            roleService.assignMenu(menuId, roleId);
            return HttpResponseHelper.successInfoInbox("分配成功");
        } catch (ServiceException e) {
            log.error(e.getMessage(), e);
            throw new ControllerException(e.getMessage());
        }

    }


    @BusinessMethod(value = "删除一个角色的菜单项")
    @DeleteMapping(value = "/assign-menu-to-role/{menuId}/{roleId}")
    @ResponseBody
    public String deleteMenu4Role(@PathVariable String menuId, @PathVariable String roleId) throws ControllerException {
        log.debug("分配一个菜单{}给角色{}", menuId, roleId);
        try {
            roleService.delete(menuId, roleId);
            return HttpResponseHelper.successInfoInbox("删除成功");
        } catch (ServiceException e) {
            log.error(e.getMessage(), e);
            throw new ControllerException(e.getMessage());
        }

    }
}
