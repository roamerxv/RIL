package com.alcor.ril.controller;

import lombok.extern.log4j.Log4j2;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import pers.roamer.boracay.aspect.businesslogger.BusinessMethod;
import pers.roamer.boracay.aspect.httprequest.SessionCheckKeyword;
import pers.roamer.boracay.helper.HttpResponseHelper;

import java.util.Enumeration;

/**
 * 用户操作的 Controller
 *
 * @author roamer - 徐泽宇
 * @create 2017-09-2017/9/28  上午9:53
 */
@Log4j2
@Controller("pers.roamer.boracay.websample.controller.UserController")
public class UserController extends  BaseController{

    /**
     * 管理员登出功能
     *
     * @return
     *
     * @throws ServiceException
     */
    @BusinessMethod(value = "管理员登出", isLogged = true)
    @SessionCheckKeyword(checkIt = false)
    @RequestMapping(value = "/admin/logout")
    public ModelAndView adminLogout() throws ControllerException {
        log.debug("开始登出");
        Enumeration<String> eume = httpSession.getAttributeNames();
        while (eume.hasMoreElements()) {
            String name = eume.nextElement();
            httpSession.removeAttribute(name);
        }
        log.debug("admin登出完成");
        return new ModelAndView("/admin/login");
    }

    /**
     * 测试需要记录日志的业务方法
     *
     * @return
     *
     * @throws ControllerException
     */
    @GetMapping(value = "/businessMethodLog")
    @BusinessMethod(value = "测试需要记录日志的业务方法")
    @ResponseBody
    public String businessMethodLog() throws ControllerException {
        return HttpResponseHelper.successInfoInbox("记录业务日志的方法被成功调用！现在可以到日志结果查看功能里面去查看日志是否被成功记录！");
    }





}
