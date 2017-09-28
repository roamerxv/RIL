package com.alcor.ril.controller;

import lombok.extern.log4j.Log4j2;
import org.hibernate.service.spi.ServiceException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import pers.roamer.boracay.aspect.httprequest.SessionCheckKeyword;

import java.util.Enumeration;

/**
 * 用户操作的 Controller
 *
 * @author roamer - 徐泽宇
 * @create 2017-09-2017/9/28  上午9:53
 */
@Log4j2
public class UserController extends  BaseController{

    /**
     * 登出功能
     *
     * @return
     *
     * @throws ServiceException
     */
    // @BusinessMethod(value = "登出", isLogged = true)
    @SessionCheckKeyword(checkIt = false)
    @RequestMapping(value = "/logout")
    public ModelAndView logout() throws ServiceException {
        log.debug("开始登出");
        Enumeration<String> eume = httpSession.getAttributeNames();
        while (eume.hasMoreElements()) {
            String name = eume.nextElement();
            httpSession.removeAttribute(name);
        }
        log.debug("登出完成");
        return new ModelAndView("/");
    }
}
