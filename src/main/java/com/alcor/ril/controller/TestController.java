package com.alcor.ril.controller;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import pers.roamer.boracay.aspect.httprequest.SessionCheckKeyword;

import javax.servlet.http.HttpServletRequest;

/**
 * 提醒事件的 controller 类
 *
 * @author roamer - 徐泽宇
 * @create 2017-10-2017/10/11  下午12:13
 */
@Log4j2
@RestController("com.alcor.cli.controller.TestController")

public class TestController extends BaseController {



    @PostMapping("/test/500Error")
    @ResponseBody
    public String testError() throws ControllerException{
        throw new ControllerException("一个测试用的错误！");
    }


    @SessionCheckKeyword
    @GetMapping("serverInfo")
    public ModelAndView serverInfo (HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("/test/server-info");
        ServerInfo serverInfo = new ServerInfo();
        serverInfo.setServerIP(request.getLocalAddr());
        serverInfo.setSessionID(httpSession.getId());
        serverInfo.setLocalPort(request.getLocalPort());
        serverInfo.setContextPath(request.getContextPath());
        modelAndView.addObject("serverInfo", serverInfo);
        return modelAndView;
    }

}

@Data
class ServerInfo{
    String serverIP ;
    String sessionID;
    int localPort ;
    String contextPath;
}

