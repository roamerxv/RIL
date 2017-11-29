package com.alcor.ril.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 提醒事件的 controller 类
 *
 * @author roamer - 徐泽宇
 * @create 2017-10-2017/10/11  下午12:13
 */
@Slf4j
@RestController("com.alcor.cli.controller.TestController")
@RequestMapping("/test")
public class TestController extends BaseController {

    @PostMapping("/500Error")
    @ResponseBody
    public String testError() throws ControllerException{
        throw new ControllerException("一个测试用的错误！");
    }

    @GetMapping("/systemMenu")
    @ResponseBody
    public String showSystemMenu() throws  Exception{
        return "asdf";
    }
}

