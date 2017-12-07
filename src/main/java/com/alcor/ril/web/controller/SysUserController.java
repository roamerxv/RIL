package com.alcor.ril.web.controller;

import com.alcor.ril.controller.ControllerException;
import com.alcor.ril.persistence.entity.SysUser;
import com.alcor.ril.security.SecurityUtil;
import com.alcor.ril.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pers.roamer.boracay.configer.ConfigHelper;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

/**
 * Created with RIL.
 * User: duduba - 邓良玉
 * Date: 2017/12/8
 * Time: 00:39
 */
@Slf4j
@Controller
//@RequestMapping("/user")
public class SysUserController {

    static private String AVATAR_FILE_FOLDER_PATH = "/static/assets/img/avatar";

    @Autowired
    private SysUserService sysUserService;


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
        Optional<SysUser> user = sysUserService.findByUsername(SecurityUtil.getCurrentUsername());
        String avatarUrl = ConfigHelper.getConfig().getString("System.user.avatar.defaultFile");
        if(user.isPresent()) {
            String userAvatarID = user.get().getAvatar();
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
        } else {
            return avatarUrl;
        }
    }

}
