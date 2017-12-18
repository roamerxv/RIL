package com.alcor.ril.web.controller;

import com.alcor.ril.persistence.entity.SysUser;
import com.alcor.ril.security.SecurityUtil;
import com.alcor.ril.service.ServiceException;
import com.alcor.ril.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import pers.roamer.boracay.BoracayException;
import pers.roamer.boracay.aspect.businesslogger.BusinessMethod;
import pers.roamer.boracay.aspect.httprequest.SessionCheckKeyword;
import pers.roamer.boracay.configer.ConfigHelper;
import pers.roamer.boracay.helper.HttpResponseHelper;
import pers.roamer.boracay.util.web.FileUploadResult;
import pers.roamer.boracay.util.web.UploadFileUtil;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Created with RIL.
 * User: duduba - 邓良玉
 * Date: 2017/12/8
 * Time: 00:39
 */
@Slf4j
@Controller
public class SysUserController extends  BaseController{

    static private String AVATAR_FILE_FOLDER_PATH = "/static/assets/img/avatar";

    @Autowired
    private SysUserService sysUserService;

    /**
     * 访问 首页的 跳转功能
     *
     * @return
     */
    @RequestMapping("/")
    public ModelAndView index() {
        ModelAndView modelAndView;
        log.debug("开始跳转到主页！");
        try {
            if (super.getUserID() != null) {
                modelAndView = new ModelAndView("/dashboard/index");
            } else {
                modelAndView = new ModelAndView("/user/login");
            }
        } catch (ControllerException e) {
            modelAndView = new ModelAndView("/user/login");
        }

        return modelAndView;
    }


    /**
     * 登出功能
     *
     * @return
     *
     * @throws ServiceException
     */
    @BusinessMethod(value = "登出", isLogged = true)
    @SessionCheckKeyword(checkIt = false)
    @RequestMapping(value = "/user/logout")
    public ModelAndView adminLogout() throws ControllerException {
        log.debug("开始登出");
//        Enumeration<String> eume = httpSession.getAttributeNames();
//        while (eume.hasMoreElements()) {
//            String name = eume.nextElement();
//            log.debug("开始从 session 中移除 {}", name);
//
//            httpSession.removeAttribute(name);
//        }

        httpSession.removeAttribute("SystemBanner");
        httpSession.removeAttribute(ConfigHelper.getConfig().getString("System.SessionUserKeyword"));

        log.debug("登出完成");
        return new ModelAndView("/user/login");
    }


    /**
     * 跳转到显示用户 profile 的界面
     *
     * @return
     *
     * @throws ControllerException
     */
    @RequestMapping("/user/profile")
    public ModelAndView profile() throws ControllerException {
        ModelAndView modelAndView;
        SysUser sysUser = sysUserService.findByUsername(super.getUserID()).get();
        log.debug(sysUser.getName());
        modelAndView = new ModelAndView("/user/profile");
        modelAndView.addObject("SysUser", sysUser);
        return modelAndView;
    }


    /**
     * 更新用户信息
     *
     * @param sysUser
     *
     * @return
     *
     * @throws ControllerException
     */
    @BusinessMethod(value = "更新用户资料")
    @PostMapping("/user/{id}")
    @ResponseBody
    public String updateUser(@RequestBody SysUser sysUser) throws ControllerException {
        log.debug("用户资料更新");
        SysUser userInDb = sysUserService.findByUsername(sysUser.getUsername()).get();
        userInDb.setEmail(sysUser.getEmail());
        userInDb.setName(sysUser.getName());
        try {
            sysUserService.update(userInDb);
            log.debug("用户资料更新完成");
            return HttpResponseHelper.successInfoInbox("成功登录");
        } catch (ServiceException e) {
            throw new ControllerException(e.getMessage());
        }
    }

    @BusinessMethod(value = "更新用户头像")
    @ResponseBody
    @PostMapping(value = "/updateAvatar", consumes = {"multipart/form-data;charset=utf-8"})
    public String updateAvatar(@RequestPart("avatar") MultipartFile[] avatar) throws ControllerException {
        log.debug("开始处理更新头像");
        if (avatar.length <= 0) {
            throw new ControllerException("没有传入有效的头像文件！");
        }
        try {
            Resource resource = new ClassPathResource(AVATAR_FILE_FOLDER_PATH);
            //如果保存 avatar 的目录不存在，则建立
            ArrayList<FileUploadResult> avatarFileList = new UploadFileUtil().saveFile(avatar, true, resource.getFile().getPath());
            FileUploadResult avatarFile = avatarFileList.get(0);
            log.debug("保存的文件信息是：{}", avatarFile.toString());
            SysUser sysUser = sysUserService.findByUsername(super.getUserID()).get();
            sysUser.setAvatar(avatarFile.getId());
            sysUserService.update(sysUser);
            log.debug("头像更新完成！");
            return HttpResponseHelper.successInfoInbox("成功更新");
        } catch (IOException | NoSuchAlgorithmException | BoracayException e) {
            e.printStackTrace();
            throw new ControllerException(e.getMessage());
        }
    }

    /**
     * 显示用户头像
     *
     * @throws ControllerException
     */
    @GetMapping(value = "/avatar")
    @ResponseBody
    public String showAvatar() throws ControllerException {
        // 从性能角度考虑，不再使用文件下载的方式显示用户头像
        Resource resource = new ClassPathResource(AVATAR_FILE_FOLDER_PATH);
        String avatarUrl = ConfigHelper.getConfig().getString("System.user.avatar.defaultFile");

        String userAvatarID = null;
        Optional<SysUser> user = sysUserService.findByUsername(super.getUserID());
        if(user.isPresent()) {
            userAvatarID = user.get().getAvatar();
        } else {
            return avatarUrl;
        }
        
        StringBuilder avatarPath = null;
        try {
            if (StringUtils.isEmpty(userAvatarID)) {
                log.debug("头像没有设置，使用缺省的头像文件");
                return avatarUrl;
            } else {
                avatarPath = new StringBuilder(resource.getFile().getPath()).append(File.separator).append(userAvatarID);
                File[] listFiles = new File(avatarPath.toString()).listFiles();
                if (listFiles == null || listFiles.length <= 0) {
                    log.warn("目录{}下没有头像文件！使用缺省的头像:{}", avatarPath, avatarUrl);
                    return avatarUrl;
                } else {
                    File file = listFiles[0];
                    //log.debug("开始显示用户头像，头像路径是:{}", file.getPath());
                    avatarUrl = new StringBuilder(AVATAR_FILE_FOLDER_PATH).append(File.separator).append(userAvatarID).append(File.separator).append(file.getName()).toString();
                    avatarUrl = avatarUrl.replace("/static/", "");
                    //log.debug("头像 url 调用路径是：{}", avatarUrl);
                    return avatarUrl;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new ControllerException(e.getMessage());
        }
    }

    @GetMapping(value = "/sidebar")
    public ModelAndView showSidebar() throws ControllerException{
        ModelAndView modelAndView = new ModelAndView("decorators/sidebar2");
        String menuHtml= "<div id=\"metronic_sidebar_menu\">\n" +
                "        <div\n" +
                "            id=\"m_ver_menu\"\n" +
                "            class=\"m-aside-menu  m-aside-menu--skin-dark m-aside-menu--submenu-skin-dark m-aside-menu--dropdown \"\n" +
                "            data-menu-vertical=\"false\"\n" +
                "            data-menu-dropdown=\"true\" data-menu-scrollable=\"true\" data-menu-dropdown-timeout=\"0\"\n" +
                "    >\n" +
                "        <ul class=\"m-menu__nav  m-menu__nav--dropdown-submenu-arrow \">\n" +
                "            <li class=\"m-menu__item \" aria-haspopup=\"true\" name=\"menu_system_dashboard\">\n" +
                "                <a th:href=\"@{/}\" class=\"m-menu__link \">\n" +
                "                    <i class=\"m-menu__link-icon flaticon-line-graph\"></i>\n" +
                "                    <span class=\"m-menu__link-title\">\n" +
                "                        <span class=\"m-menu__link-wrap\">\n" +
                "                            <span class=\"m-menu__link-text\">\n" +
                "                                Dashboard\n" +
                "                            </span>\n" +
                "                        </span>\n" +
                "                    </span>\n" +
                "                </a>\n" +
                "            </li>\n" +
                "            <li class=\"m-menu__item  m-menu__item--submenu\" aria-haspopup=\"true\" data-menu-submenu-toggle=\"hover\"\n" +
                "                name=\"menu_system\">\n" +
                "                <a href=\"#\" class=\"m-menu__link m-menu__toggle\">\n" +
                "                    <span class=\"m-menu__item-here\"></span>\n" +
                "                    <i class=\"m-menu__link-icon flaticon-open-box\"></i>\n" +
                "                    <span class=\"m-menu__link-text\">\n" +
                "                        系统设置\n" +
                "                    </span>\n" +
                "                    <i class=\"m-menu__ver-arrow la la-angle-right\"></i>\n" +
                "                </a>\n" +
                "                <div class=\"m-menu__submenu\">\n" +
                "                    <span class=\"m-menu__arrow\"></span>\n" +
                "                    <ul class=\"m-menu__subnav\">\n" +
                "                        <li class=\"m-menu__item  m-menu__item--submenu\" aria-haspopup=\"true\"\n" +
                "                            data-menu-submenu-toggle=\"hover\">\n" +
                "                            <a href=\"#\" class=\"m-menu__link m-menu__toggle\">\n" +
                "                                <span class=\"m-menu__item-here\"></span>\n" +
                "                                <i class=\"m-menu__link-icon flaticon-open-box\"></i>\n" +
                "                                <span class=\"m-menu__link-text\">\n" +
                "                                    授权维护\n" +
                "                                </span>\n" +
                "                                <i class=\"m-menu__ver-arrow la la-angle-right\"></i>\n" +
                "                            </a>\n" +
                "                            <div class=\"m-menu__submenu\">\n" +
                "                                <span class=\"m-menu__arrow\"></span>\n" +
                "                                <ul class=\"m-menu__subnav\">\n" +
                "                                    <li class=\"m-menu__item \" aria-haspopup=\"true\" data-redirect=\"true\">\n" +
                "                                        <a th:href=\"@{/systemMenuMaintain}\" class=\"m-menu__link \">\n" +
                "                                            <i class=\"m-menu__link-bullet m-menu__link-bullet--dot\">\n" +
                "                                                <span></span>\n" +
                "                                            </i>\n" +
                "                                            <span class=\"m-menu__link-text\">系统菜单维护</span>\n" +
                "                                        </a>\n" +
                "                                    </li>\n" +
                "                                    <li class=\"m-menu__item \" aria-haspopup=\"true\" data-redirect=\"true\">\n" +
                "                                        <a th:href=\"@{/roleMaintain}\" class=\"m-menu__link \">\n" +
                "                                            <i class=\"m-menu__link-bullet m-menu__link-bullet--dot\">\n" +
                "                                                <span></span>\n" +
                "                                            </i>\n" +
                "                                            <span class=\"m-menu__link-text\">角色维护</span>\n" +
                "                                        </a>\n" +
                "                                    </li>\n" +
                "                                </ul>\n" +
                "                            </div>\n" +
                "                        </li>\n" +
                "                        <li class=\"m-menu__item \" aria-haspopup=\"true\" data-redirect=\"true\">\n" +
                "                            <a th:href=\"@{/serverInfo}\" class=\"m-menu__link \">\n" +
                "                                <i class=\"m-menu__link-bullet m-menu__link-bullet--dot\">\n" +
                "                                    <span></span>\n" +
                "                                </i>\n" +
                "                                <span class=\"m-menu__link-text\">查看服务器信息</span>\n" +
                "                            </a>\n" +
                "                        </li>\n" +
                "                        <li class=\"m-menu__item\" aria-haspopup=\"true\" name=\"menu_system_configer\">\n" +
                "                            <a th:href=\"@{/system_config}\" class=\"m-menu__link \">\n" +
                "                                <span class=\"m-menu__item-here\"></span>\n" +
                "                                <i class=\"m-menu__link-icon flaticon-list-3\"></i>\n" +
                "                                <span class=\"m-menu__link-text\">参数设置</span>\n" +
                "                            </a>\n" +
                "                        </li>\n" +
                "                        <li class=\"m-menu__item\" aria-haspopup=\"true\" name=\"menu_system_log\">\n" +
                "                            <a th:href=\"@{/system_logs_index}\" class=\"m-menu__link \">\n" +
                "                                <span class=\"m-menu__item-here\"></span>\n" +
                "                                <i class=\"m-menu__link-icon flaticon-line-graph\"></i>\n" +
                "                                <span class=\"m-menu__link-text\">系统日志</span>\n" +
                "                            </a>\n" +
                "                        </li>\n" +
                "                    </ul>\n" +
                "                </div>\n" +
                "            </li>\n" +
                "            <li class=\"m-menu__item  m-menu__item--submenu\" aria-haspopup=\"true\" data-menu-submenu-toggle=\"hover\">\n" +
                "                <a href=\"#\" class=\"m-menu__link m-menu__toggle\">\n" +
                "                    <span class=\"m-menu__item-here\"></span>\n" +
                "                    <i class=\"m-menu__link-icon flaticon-open-box\"></i>\n" +
                "                    <span class=\"m-menu__link-text\">\n" +
                "                                        测试功能\n" +
                "                                    </span>\n" +
                "                    <i class=\"m-menu__ver-arrow la la-angle-right\"></i>\n" +
                "                </a>\n" +
                "                <div class=\"m-menu__submenu\">\n" +
                "                    <span class=\"m-menu__arrow\"></span>\n" +
                "                    <ul class=\"m-menu__subnav\">\n" +
                "                        <li class=\"m-menu__item  m-menu__item--submenu\" aria-haspopup=\"true\"\n" +
                "                            data-menu-submenu-toggle=\"hover\">\n" +
                "                            <a href=\"#\" class=\"m-menu__link m-menu__toggle\">\n" +
                "                                <span class=\"m-menu__item-here\"></span>\n" +
                "                                <i class=\"m-menu__link-icon flaticon-open-box\"></i>\n" +
                "                                <span class=\"m-menu__link-text\">\n" +
                "                                    三级菜单\n" +
                "                                </span>\n" +
                "                                <i class=\"m-menu__ver-arrow la la-angle-right\"></i>\n" +
                "                            </a>\n" +
                "                            <div class=\"m-menu__submenu\">\n" +
                "                                <span class=\"m-menu__arrow\"></span>\n" +
                "                                <ul class=\"m-menu__subnav\">\n" +
                "                                    <li class=\"m-menu__item \" aria-haspopup=\"true\" data-redirect=\"true\">\n" +
                "                                        <a th:href=\"@{/cleanCache}\" class=\"m-menu__link \">\n" +
                "                                            <i class=\"m-menu__link-bullet m-menu__link-bullet--dot\">\n" +
                "                                                <span></span>\n" +
                "                                            </i>\n" +
                "                                            <span class=\"m-menu__link-text\">三级菜单1</span>\n" +
                "                                        </a>\n" +
                "                                    </li>\n" +
                "                                </ul>\n" +
                "                            </div>\n" +
                "                        </li>\n" +
                "                        <li class=\"m-menu__item \" aria-haspopup=\"true\" data-redirect=\"true\">\n" +
                "                            <a th:href=\"@{/cleanCache}\" class=\"m-menu__link \">\n" +
                "                                <i class=\"m-menu__link-bullet m-menu__link-bullet--dot\">\n" +
                "                                    <span></span>\n" +
                "                                </i>\n" +
                "                                <span class=\"m-menu__link-text\">清除所有 cache</span>\n" +
                "                            </a>\n" +
                "                        </li>\n" +
                "                        <li class=\"m-menu__item \" aria-haspopup=\"true\" data-redirect=\"true\">\n" +
                "                            <a th:href=\"@{/systemMenus4Treeviewer}\" class=\"m-menu__link \" target=\"_test\">\n" +
                "                                <i class=\"m-menu__link-bullet m-menu__link-bullet--dot\">\n" +
                "                                    <span></span>\n" +
                "                                </i>\n" +
                "                                <span class=\"m-menu__link-text\">显示系统菜单的 json</span>\n" +
                "                            </a>\n" +
                "                        </li>\n" +
                "                        <li class=\"m-menu__item \" aria-haspopup=\"true\" data-redirect=\"true\">\n" +
                "                            <a th:href=\"@{/serverInfo}\" class=\"m-menu__link \">\n" +
                "                                <i class=\"m-menu__link-bullet m-menu__link-bullet--dot\">\n" +
                "                                    <span></span>\n" +
                "                                </i>\n" +
                "                                <span class=\"m-menu__link-text\">查看服务器信息</span>\n" +
                "                            </a>\n" +
                "                        </li>\n" +
                "                        <li class=\"m-menu__item \" aria-haspopup=\"true\">\n" +
                "                            <a th:href=\"@{/businessMethodLog.json}\" class=\"m-menu__link \" target=\"_test\">\n" +
                "                                <i class=\"m-menu__link-bullet m-menu__link-bullet--dot\">\n" +
                "                                    <span></span>\n" +
                "                                </i>\n" +
                "                                <span class=\"m-menu__link-text\">调用一个记录日志的方法</span>\n" +
                "                            </a>\n" +
                "                        </li>\n" +
                "                        <li class=\"m-menu__item \" aria-haspopup=\"true\" data-redirect=\"true\">\n" +
                "                            <a th:href=\"@{/builder.html}\" class=\"m-menu__link \">\n" +
                "                                <i class=\"m-menu__link-bullet m-menu__link-bullet--dot\">\n" +
                "                                    <span></span>\n" +
                "                                </i>\n" +
                "                                <span class=\"m-menu__link-text\">调用一个静态页面</span>\n" +
                "                            </a>\n" +
                "                        </li>\n" +
                "                        <li class=\"m-menu__item \" aria-haspopup=\"true\" data-redirect=\"true\">\n" +
                "                            <a th:href=\"@{/testPage}\" class=\"m-menu__link \">\n" +
                "                                <i class=\"m-menu__link-bullet m-menu__link-bullet--dot\">\n" +
                "                                    <span></span>\n" +
                "                                </i>\n" +
                "                                <span class=\"m-menu__link-text\">访问一个动态界面</span>\n" +
                "                            </a>\n" +
                "                        </li>\n" +
                "                        <li class=\"m-menu__item \" aria-haspopup=\"true\" data-redirect=\"true\">\n" +
                "                            <a th:href=\"@{/test/ajax_500}\" class=\"m-menu__link \">\n" +
                "                                <i class=\"m-menu__link-bullet m-menu__link-bullet--dot\">\n" +
                "                                    <span></span>\n" +
                "                                </i>\n" +
                "                                <span class=\"m-menu__link-text\"> 通过 ajax 捕获500错误</span>\n" +
                "                            </a>\n" +
                "                        </li>\n" +
                "                    </ul>\n" +
                "                </div>\n" +
                "            </li>\n" +
                "\n" +
                "        </ul>\n" +
                "    </div>\n" +
                "    </div>";
        log.debug("获取动态菜单");
        modelAndView.addObject("sidebar",menuHtml);
        return modelAndView;
    }

    @RequestMapping(value = "/user/modify_password")
    @ResponseBody
    public String modifyPassword(@RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword) throws ControllerException {
        log.debug("密码修改，老密码：{},新密码{}", oldPassword, newPassword);
        try {
            sysUserService.modifyPassword(super.getUserID(), oldPassword, newPassword);
            return HttpResponseHelper.successInfoInbox("密码修改成功");
        } catch (ServiceException e) {
            throw new ControllerException(e.getMessage());
        }
    }



    /**
     * 获取当前登录用户的信息
     *
     * @return
     *
     * @throws ControllerException
     */
    @ResponseBody
    @GetMapping("/user")
    public String getUserInfo() throws ControllerException {
        Optional<SysUser> user = sysUserService.findByUsername(SecurityUtil.getCurrentUsername());
        log.debug("current user: {}", SecurityUtil.getCurrentUsername());
        if(user.isPresent()) {
            return String.format("{\"fullName\":\"%s\",\"name\":\"%s\"}", user.get().getName(), user.get().getUsername());
        } else {
            return "{}";
        }
//        try {
//            Optional<SysUser> user = sysUserService.findByUsername(SecurityUtil.getCurrentUsername());
//            return JsonUtilsHelper.objectToJsonString(user.get());
//        } catch (JsonProcessingException e) {
//            log.error(e.getMessage());
//            throw new ControllerException(e.getMessage());
//        }
    }

}
