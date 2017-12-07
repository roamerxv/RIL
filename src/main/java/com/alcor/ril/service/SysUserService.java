package com.alcor.ril.service;

import com.alcor.ril.persistence.entity.SysUser;
import com.alcor.ril.persistence.repository.SysUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

/**
 * Created with RIL.
 * User: duduba - 邓良玉
 * Date: 2017/12/8
 * Time: 00:37
 */
@Service
@Transactional
public class SysUserService implements ISysUserService {
    @Autowired
    private SysUserRepository sysUserRepository;

    @Override
    public Optional<SysUser> findByUsername(String username) {
        return sysUserRepository.findByUsername(username);
    }
}
