package com.alcor.ril.controller;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import pers.roamer.boracay.aspect.httprequest.SessionCheckKeyword;

import javax.servlet.http.HttpServletRequest;

/**
 * 系统级别的控制操作
 *
 * @author roamer - 徐泽宇
 * @create 2017-11-2017/11/29  下午6:36
 */
@Slf4j
@RestController("com.alcor.cli.controller.SystemController")
@SessionCheckKeyword
public class SystemController extends BaseController {


    /**
     * 查看服务器信息，主要是用于查看集群状态
     *
     * @param request
     *
     * @return
     */
    @GetMapping("/serverInfo")
    public ModelAndView serverInfo(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("/test/server-info");
        ServerInfo serverInfo = new ServerInfo();
        serverInfo.setServerIP(request.getLocalAddr());
        serverInfo.setSessionID(httpSession.getId());
        serverInfo.setLocalPort(request.getLocalPort());
        serverInfo.setContextPath(request.getContextPath());
        modelAndView.addObject("serverInfo", serverInfo);
        return modelAndView;
    }

    @GetMapping("/cleanCache")
    @CacheEvict(cacheNames = {"spring:cache:UserEntity"}, allEntries=true)
    public ModelAndView cleanCache(){
        log.debug("清除spring cache 中的缓存！");
        ModelAndView modelAndView = new ModelAndView("/index");
        return  modelAndView;
    }
}


@Data
class ServerInfo {
    String serverIP;
    String sessionID;
    int localPort;
    String contextPath;
}

