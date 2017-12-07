package com.alcor.ril.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import pers.roamer.boracay.configer.ConfigHelper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created with RIL.
 * User: duduba - 邓良玉
 * Date: 2017/12/7
 * Time: 00:30
 */

@Slf4j
public class CustomLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

    public CustomLogoutSuccessHandler(String defaultUrl) {
        setDefaultTargetUrl(defaultUrl);
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                Authentication authentication) throws IOException, ServletException {
        if (authentication != null && authentication.getDetails() != null) {
            try {
                log.info("User Successfully Logout");
                HttpSession session = httpServletRequest.getSession();
                session.removeAttribute("SystemBanner");
                session.removeAttribute(ConfigHelper.getConfig().getString("System.SessionUserKeyword"));
//                session.invalidate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        httpServletResponse.setStatus(HttpServletResponse.SC_OK);

        super.onLogoutSuccess(httpServletRequest, httpServletResponse, authentication);

        //redirect to login
//        httpServletResponse.sendRedirect(getDefaultTargetUrl());
    }
}
