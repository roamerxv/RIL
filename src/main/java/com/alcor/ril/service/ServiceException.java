/**
 * @Title: ControllerException.java
 * @Package com.ninelephas.terrier.controller
 * @Description: 系统的Controller下的Exception基类
 *               Copyright: Copyright (c) 2016
 *               Company:九象网络科技（上海）有限公司
 * 
 * @author 徐泽宇
 * @date 2016年10月13日 下午4:03:04
 * @version V1.0.0
 */

package com.alcor.ril.service;

import com.alcor.ril.ProjectException;
import org.springframework.validation.BindingResult;


/**
 * @ClassName: ServiceException
 * @Description: 系统的Service下的Exception基类
 * @author 邹伟
 * @date 2017年05月04日 下午4:03:04
 *
 */

public class ServiceException extends ProjectException {

    /**
     * @Fields serialVersionUID : 序列号随机数
     */

    private static final long serialVersionUID = 694905108298102070L;
    /**
     *
     * 创建一个新的实例 ServiceException.
     *
     * @Auther 徐泽宇
     * @Date 2016年11月2日 下午3:36:53
     * @param message
     */
     public ServiceException(String message){
         super(message);
     }
    /**
     *
     * 创建一个新的实例 ServiceException.
     *
     * @Auther 徐泽宇
     * @Date 2016年11月4日 下午12:50:57
     * @param bindingResult
     */
    public ServiceException(BindingResult bindingResult) {
        super(bindingResult);
    }
}
