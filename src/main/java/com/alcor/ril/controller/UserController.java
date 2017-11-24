package com.alcor.ril.controller;

import com.alcor.ril.entity.UserEntity;
import com.alcor.ril.service.ServiceException;
import com.alcor.ril.service.SystemConfigureService;
import com.alcor.ril.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.log4j.Log4j2;
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
import pers.roamer.boracay.helper.JsonUtilsHelper;
import pers.roamer.boracay.util.web.FileUploadResult;
import pers.roamer.boracay.util.web.UploadFileUtil;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Enumeration;

/**
 * 用户操作的 Controller
 *
 * @author roamer - 徐泽宇
 * @create 2017-09-2017/9/28  上午9:53
 */
@Log4j2
@Controller("com.alcor.ril.controller.UserController")
@SessionCheckKeyword
public class UserController extends BaseController {

    @Autowired
    UserService userService;

    @Autowired
    SystemConfigureService systemConfigureService;

    static private String AVATAR_FILE_FOLDER_PATH = "/static/assets/img/avatar";

    /**
     * 访问 首页的 跳转功能
     *
     * @return
     */
    @RequestMapping("/")
    public ModelAndView index() {
        ModelAndView modelAndView;
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
     * 跳转到显示用户 profile 的界面
     *
     * @return
     *
     * @throws ControllerException
     */
    @RequestMapping("/user/profile")
    public ModelAndView profile() throws ControllerException {
        ModelAndView modelAndView;
        UserEntity userEntity = userService.findByName(super.getUserID());
        modelAndView = new ModelAndView("/user/profile");
        modelAndView.addObject("UserEntity", userEntity);
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
        Enumeration<String> eume = httpSession.getAttributeNames();
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

    @BusinessMethod(value = "用户登录")
    @PostMapping("/signIn")
    @SessionCheckKeyword(checkIt = false)
    @ResponseBody
    public String login(@RequestBody UserEntity userEntity) throws ControllerException {
        log.debug("用户登录!{}", userEntity.toString());
        try {
            if (userService.login(userEntity)) {
                httpSession.setAttribute(ConfigHelper.getConfig().getString("System.SessionUserKeyword"), userEntity.getName());
                String systemBanner = systemConfigureService.findByName("banner_message").getValue();
                httpSession.setAttribute("SystemBanner", systemBanner);
            }
        } catch (ServiceException e) {
            log.error(e.getMessage());
            throw new ControllerException(e.getMessage());
        }
        log.debug("用户登录成功");
        return HttpResponseHelper.successInfoInbox("成功登录");
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
        String m_rtn = "";
        try {
            m_rtn = JsonUtilsHelper.objectToJsonString(userService.findByName(super.getUserID()));
            return m_rtn;
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            throw new ControllerException(e.getMessage());
        }
    }

    /**
     * 更新用户信息
     *
     * @param userEntity
     *
     * @return
     *
     * @throws ControllerException
     */
    @BusinessMethod(value = "更新用户资料")
    @PostMapping("/user/{id}")
    @ResponseBody
    public String updateUser(@RequestBody UserEntity userEntity) throws ControllerException {
        log.debug("用户资料更新");
        UserEntity userInDb = userService.findByName(userEntity.getName());
        userEntity.setPasswd(userInDb.getPasswd());
        userEntity.setAvatar(userInDb.getAvatar());
        try {
            userService.update(userEntity);
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
            ArrayList<FileUploadResult> avatarFileList = new UploadFileUtil().saveFile(avatar, true, resource.getFile().getPath());
            FileUploadResult avatarFile = avatarFileList.get(0);
            log.debug("保存的文件信息是：{}", avatarFile.toString());
            UserEntity userEntity = userService.findByName(super.getUserID());
            userEntity.setAvatar(avatarFile.getId());
            userService.update(userEntity);
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
        String userAvatarID = userService.findByName(super.getUserID()).getAvatar();
        StringBuilder avatarPath = null;
        try {
            String avatarUrl = ConfigHelper.getConfig().getString("System.user.avatar.defaultFile");
            if (StringUtils.isEmpty(userAvatarID)) {
                log.debug("头像没有设置，使用缺省的头像文件");
                return avatarUrl;
            } else {
                avatarPath = new StringBuilder(resource.getFile().getPath()).append(File.separator).append(userAvatarID);
                File[] listFiles = new File(avatarPath.toString()).listFiles();
                if (listFiles == null || listFiles.length <= 0) {
                    log.error("目录{}下没有头像文件！使用缺省的头像:{}", avatarPath, avatarUrl);
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


//        String userID = super.getUserID();
//        UserEntity userEntity = userService.findByName(userID);
//        String avatarId = userEntity.getAvatar();
//        log.debug("头像的ID 是{}", avatarId);
//        String saveFilePath = ConfigHelper.getConfig().getString("System.UploadFile.saveFilePath") + File.separator + avatarId;
//        File[] listFiles = new File(saveFilePath).listFiles();
//        try {
//            File avatarFile = null;
//            if (listFiles == null) {
//                log.debug("头像没有设置，使用缺省的头像文件");
//                avatarFile = new ClassPathResource("/static/assets/img/logo/logo.png").getFile();
//            } else if (listFiles.length <= 0) {
//                log.debug("头像文件不存，使用缺省的头像文件");
//                avatarFile = new ClassPathResource("/static/assets/img/logo/logo.png").getFile();
//            } else {
//                log.debug("在 id 的目录下发现多个文件，取第一个文件");
//                avatarFile = listFiles[0];
//            }
//            log.debug(avatarFile);
//            FileInputStream inputStream = new FileInputStream(avatarFile);
//            byte[] data = new byte[(int) avatarFile.length()];
//            inputStream.read(data);
//            inputStream.close();
//            response.setContentType("image/png");
//            OutputStream stream = response.getOutputStream();
//            stream.write(data);
//            stream.flush();
//            stream.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//            throw new ControllerException(e.getMessage());
//        }
    }

    @RequestMapping(value = "/user/modify_password")
    @ResponseBody
    public String modifyPassword(@RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword) throws ControllerException {
        log.debug("密码修改，老密码：{},新密码{}", oldPassword, newPassword);
        try {
            userService.modifyPassword(super.getUserID(), oldPassword, newPassword);
            return HttpResponseHelper.successInfoInbox("密码修改成功");
        } catch (ServiceException e) {
            throw new ControllerException(e.getMessage());
        }
    }

}
