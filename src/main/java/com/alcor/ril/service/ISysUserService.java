package com.alcor.ril.service;

import com.alcor.ril.persistence.entity.SysUser;

import java.util.Optional;

/**
 * Created with RIL.
 * User: duduba - 邓良玉
 * Date: 2017/12/8
 * Time: 00:36
 */
public interface ISysUserService {
    Optional<SysUser> findByUsername(String username);
}
