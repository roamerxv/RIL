/*
 * Boracay - Web 项目实用组件框架
 *
 * @author 徐泽宇 roamerxv@gmail.com
 * @version 1.0.0
 * Copyright (c) 2017. 徐泽宇
 *
 */

package com.alcor.ril.web.controller;

import com.alcor.ril.ProjectException;
import org.springframework.validation.BindingResult;

/**
 * @ClassName: ControllerException
 * @Description: 系统的Controller下的Exception基类
 * @author 徐泽宇
 * @date 2016年10月13日 下午4:03:04
 *
 */
public class ControllerException extends ProjectException {

    /**
     * 
     * 创建一个新的实例 ControllerException.
     * 
     * @Auther 徐泽宇
     * @Date 2016年11月2日 下午3:36:53
     * @param message
     */
     public ControllerException(String message){
         super(message);
     }
    /**
     * 
     * 创建一个新的实例 ControllerException.
     * 
     * @Auther 徐泽宇
     * @Date 2016年11月4日 下午12:50:57
     * @param bindingResult
     */
    public ControllerException(BindingResult bindingResult) {
        super(bindingResult);
    }
}
