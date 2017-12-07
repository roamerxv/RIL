package com.alcor.ril.security;

import com.alcor.ril.service.ServiceException;
import com.alcor.ril.service.SystemConfigureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import pers.roamer.boracay.configer.ConfigHelper;
import pers.roamer.boracay.helper.HttpResponseHelper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created with RIL.
 * User: duduba - 邓良玉
 * Date: 2017/12/6
 * Time: 15:59
 */
@Slf4j
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    SystemConfigureService systemConfigureService;

    public CustomAuthenticationSuccessHandler(String defaultUrl) {
        setDefaultTargetUrl(defaultUrl);
    }
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        //do some logic here if you want something to be done whenever
        //the user successfully logs in.

        HttpSession session = httpServletRequest.getSession();
        User authUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        session.setAttribute(ConfigHelper.getConfig().getString("System.SessionUserKeyword"), authUser.getUsername());
        try {
            String systemBanner = systemConfigureService.findByName("banner_message").getValue();
            session.setAttribute("SystemBanner", systemBanner);
        } catch (ServiceException e) {
            log.error(e.getMessage());
            throw new ServletException(e.getMessage());
        }

        //set our response to OK status
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);

        if(RequestUtil.isAjaxRequest(httpServletRequest)) {
            RequestUtil.sendJsonResponse(httpServletResponse, HttpResponseHelper.successInfoInbox("成功登录"));
        } else {
            super.onAuthenticationSuccess(httpServletRequest, httpServletResponse, authentication);
        }
        
        //since we have created our custom success handler, its up to us to where
        //we will redirect the user after successfully login
//        httpServletResponse.sendRedirect("home");
    }
}
