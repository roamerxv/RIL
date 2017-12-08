package com.alcor.ril.web.controller;

import com.alcor.ril.entity.SystemConfigureEntity;
import com.alcor.ril.service.ServiceException;
import com.alcor.ril.service.SystemConfigureService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import pers.roamer.boracay.aspect.businesslogger.BusinessMethod;
import pers.roamer.boracay.aspect.httprequest.SessionCheckKeyword;
import pers.roamer.boracay.helper.HttpResponseHelper;
import pers.roamer.boracay.helper.JsonUtilsHelper;

import java.util.List;

/**
 * 系统参数Controller 类
 *
 * @author roamer - 徐泽宇
 * @create 2017-10-2017/10/16  上午10:56
 */
@Slf4j
@RestController("com.alcor.ril.controller.SystemConfigureController")
@SessionCheckKeyword(checkIt = true)
public class SystemConfigureController extends BaseController {

    @Autowired
    SystemConfigureService systemConfigureService;


    @GetMapping(value = "/system_config")
    public ModelAndView showSystemConfigure() throws ControllerException {
        log.debug("开始显示系统参数页面");
        ModelAndView modelAndView = new ModelAndView("/systemConfigure/edit");
        try {
            List<SystemConfigureEntity> systemConfigureEntityList = systemConfigureService.findAll();
            log.debug("总共发现{}条系统参数记录", systemConfigureEntityList.size());
            modelAndView.addObject("systemConfigures", systemConfigureEntityList);
        } catch (ServiceException e) {

            throw new ControllerException(e.getMessage());
        }
        log.debug("显示完成");

        return modelAndView;
    }

    @GetMapping(value = "/systemConfigs/{name}")
    public String getValueByName(@PathVariable String name) throws ControllerException {
        log.debug("开始获取{}的值", name);
        try {
            SystemConfigureEntity systemConfigureEntity = systemConfigureService.findByName(name);
            return JsonUtilsHelper.objectToJsonString(systemConfigureEntity);
        } catch (ServiceException | JsonProcessingException e) {
            throw new ControllerException(e.getMessage());
        }
    }

    @PostMapping(value = "/systemConfigs/{name}")
    @BusinessMethod(value = "更新一个配置项")
    public String update(@RequestBody SystemConfigureEntity systemConfigureEntity) throws ControllerException {
        log.debug("更新一个配置项目,名字是:{},内容是:{}", systemConfigureEntity.getName(), systemConfigureEntity.getValue());
        try {
            SystemConfigureEntity systemConfigureEntity2Update = systemConfigureService.findByName(systemConfigureEntity.getName());
            systemConfigureEntity2Update.setValue(systemConfigureEntity.getValue());
            systemConfigureService.update(systemConfigureEntity2Update);
            if (systemConfigureEntity2Update.getName().equalsIgnoreCase("banner_message")) {
                httpSession.setAttribute("SystemBanner", systemConfigureEntity2Update.getValue());
            }
        } catch (ServiceException e) {
            throw new ControllerException(e.getMessage());
        }
        log.debug("更新完成");
        return HttpResponseHelper.successInfoInbox("更新成功");
    }
}
