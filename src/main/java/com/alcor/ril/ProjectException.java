/*
 * Boracay - Web 项目实用组件框架
 *
 * @author 徐泽宇 roamerxv@gmail.com
 * @version 1.0.0
 * Copyright (c) 2017. 徐泽宇
 *
 */

package com.alcor.ril;

import org.springframework.validation.BindingResult;
import pers.roamer.boracay.BoracayException;

/**
 * 项目的 Exception
 *
 * @author roamer - 徐泽宇
 * @create 2017-06-2017/6/27  下午5:50
 */
public class ProjectException extends BoracayException {

    public ProjectException(String message){
        super(message);
    }
    public ProjectException(BindingResult bindingResult) {
        super(bindingResult);
    }
}
