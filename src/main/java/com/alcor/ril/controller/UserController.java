package com.alcor.ril.controller;

import com.alcor.ril.entity.UserEntity;
import com.alcor.ril.service.SystemConfigureService;
import com.alcor.ril.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import pers.roamer.boracay.aspect.businesslogger.BusinessMethod;
import pers.roamer.boracay.aspect.httprequest.SessionCheckKeyword;
import pers.roamer.boracay.configer.ConfigHelper;
import pers.roamer.boracay.helper.HttpResponseHelper;

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


    /**
     * 访问 首页的 跳转功能
     *
     * @return
     */
    @RequestMapping("/")
    @SessionCheckKeyword(checkIt = false)
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
     * @return
     * @throws ControllerException
     */
    @RequestMapping("/user/profile")
    public ModelAndView profile() throws ControllerException {
        ModelAndView modelAndView;
        UserEntity userEntity = userService.findByID(super.getUserID());
        log.debug(userEntity.toString());
        modelAndView = new ModelAndView("/user/profile");
        modelAndView.addObject("UserEntity",userEntity);
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
        while (eume.hasMoreElements()) {
            String name = eume.nextElement();
            httpSession.removeAttribute(name);
        }
        log.debug("登出完成");
        return new ModelAndView("/user/login");
    }

    @BusinessMethod(value = "用户登录")
    @PostMapping("/signIn")
    @SessionCheckKeyword(checkIt = false)
    @ResponseBody
    public String login(@RequestBody UserEntity userEntity) throws ControllerException {
        log.debug("用户登录!");
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

    @BusinessMethod(value = "更新用户资料")
    @PostMapping("/user/{id}")
    @SessionCheckKeyword
    @ResponseBody
    public String updateUser(@RequestBody UserEntity userEntity) throws ControllerException{
        log.debug("用户资料更新");
        UserEntity userInDb = userService.findByID(userEntity.getName());
        userEntity.setPasswd(userInDb.getPasswd());
        userService.update(userEntity);
        log.debug("用户资料更新完成");
        return HttpResponseHelper.successInfoInbox("成功登录");
    }
}
