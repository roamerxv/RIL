package com.alcor.ril.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.AntPathMatcher;

import java.util.Collection;
import java.util.Iterator;

/**
 * Created with admin.
 * User: duduba - 邓良玉
 * Date: 2017/12/8
 * Time: 22:14
 */
@Slf4j
public class MyAccessDecisionManager implements AccessDecisionManager {

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
//        HttpServletRequest request = ((FilterInvocation) object).getHttpRequest();
//
//        for (GrantedAuthority ga : authentication.getAuthorities()) {
//            if (ga instanceof MyGrantedAuthority) {
//                MyGrantedAuthority urlGrantedAuthority = (MyGrantedAuthority) ga;
//                if(antPathMatcher.match(urlGrantedAuthority.getUrl(), request.getServletPath())) {
//                    //当权限表权限的method为ALL时表示拥有此路径的所有请求方式权利。
//                    String method = urlGrantedAuthority.getMethod();
//                    if(method.equals(request.getMethod()) || method.equals("ALL")) {
//                        return;
//                    }
//                }
//            } else if (ga.getAuthority().equals("ROLE_ANONYMOUS")) {
//                //未登录只允许访问 login 页面
//                if (antPathMatcher.match("/login", request.getServletPath())) {
//                    return;
//                }
//            }
//        }
        
        Iterator<ConfigAttribute> ite = configAttributes.iterator();
        while (ite.hasNext()) {
            ConfigAttribute ca = ite.next();
            String needPermission = ((SecurityConfig) ca).getAttribute().trim();
            for (GrantedAuthority ga : authentication.getAuthorities()) {
                log.debug("{} ?-> {}", needPermission, ga.getAuthority());
                if(ga.getAuthority().equals(needPermission)){
                    log.info("{} !-> {}", needPermission, ga.getAuthority());
                    //匹配到有对应权限,则允许通过
                    return;
                }
            }
        }

        //该url有配置权限,但是当然登录用户没有匹配到对应权限,则禁止访问
        throw new AccessDeniedException("not allow");
    }

    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
