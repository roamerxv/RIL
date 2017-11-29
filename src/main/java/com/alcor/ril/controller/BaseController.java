/*
 * Boracay - Web 项目实用组件框架
 *
 * @author 徐泽宇 roamerxv@gmail.com
 * @version 1.0.0
 * Copyright (c) 2017. 徐泽宇
 *
 */

package com.alcor.ril.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import pers.roamer.boracay.configer.ConfigHelper;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

/**
 * 基础控制类
 *
 * @author roamer - 徐泽宇
 * @create 2017-06-2017/6/2  下午4:24
 */
@Controller("com.alcor.ril.controller.BaseController")
public class BaseController {

    @Autowired
    ServletContext servletContext;

    @Autowired
    protected HttpSession httpSession;

    public String getUserID() throws ControllerException {
        String user_id = (String) httpSession.getAttribute(ConfigHelper.getConfig().getString("System.SessionUserKeyword"));
        if (StringUtils.isEmpty(user_id)) {
            throw new ControllerException("exception.system.need_login");
        }
        return user_id;
    }
}
