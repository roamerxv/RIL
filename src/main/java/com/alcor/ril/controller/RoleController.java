package com.alcor.ril.controller;

import com.fasterxml.jackson.databind.ser.Serializers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import pers.roamer.boracay.aspect.httprequest.SessionCheckKeyword;

/**
 * 角色维护的 Controller 类
 *
 * @author roamer - 徐泽宇
 * @create 2017-12-2017/12/6  下午6:19
 */
@Slf4j
@RestController("com.alcor.ril.controller.RoleController")
@SessionCheckKeyword(checkIt = true)
public class RoleController extends Serializers.Base {

    /**
     * 跳转到角色维护的 index 界面
     * @return
     * @throws ControllerException
     */
    @GetMapping(value = "/roleMaintain")
    public ModelAndView showIndex() throws ControllerException{
        ModelAndView modelAndView = new ModelAndView("/system/role/index");
        return modelAndView;
    }
}
