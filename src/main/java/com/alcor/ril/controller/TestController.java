package com.alcor.ril.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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


}

