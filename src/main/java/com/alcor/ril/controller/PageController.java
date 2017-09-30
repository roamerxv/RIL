package com.alcor.ril.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 项目所有的页面跳转控制类
 *
 * @author roamer - 徐泽宇
 * @create 2017-09-2017/9/29  下午1:29
 */

@Controller("com.alcor.ril.controller.PageController")
@Log4j2
public class PageController {

    @RequestMapping("/")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("/index");
        return modelAndView;
    }

    @RequestMapping("/system_logs_index")
    public ModelAndView systemLogIndex() {
        ModelAndView modelAndView = new ModelAndView("/systemLogs/systemLoggerIndex");
        return modelAndView;
    }

    @RequestMapping("/testPage")
    public ModelAndView showTestPage() {
        ModelAndView modelAndView = new ModelAndView("/test/test-page");
        return modelAndView;
    }
}
