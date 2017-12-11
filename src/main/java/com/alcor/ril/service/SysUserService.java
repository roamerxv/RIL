package com.alcor.ril.service;

import com.alcor.ril.persistence.entity.SysUser;
import com.alcor.ril.persistence.repository.SysUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

/**
 * Created with RIL.
 * User: duduba - 邓良玉
 * Date: 2017/12/8
 * Time: 00:37
 */
@Slf4j
@Service
@Transactional
public class SysUserService implements ISysUserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SysUserRepository sysUserRepository;

    @Override
    public Optional<SysUser> findByUsername(String username) {
        return sysUserRepository.findByUsername(username);
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public boolean login(SysUser sysUser) throws ServiceException {
        Optional<SysUser> sysUserInDB = sysUserRepository.findByUsername(sysUser.getName());
        if (sysUserInDB == null) {
            throw new ServiceException("exception.user.login.user_not_exit");
        }
        if (sysUserInDB.get().getPassword().equalsIgnoreCase(sysUser.getPassword())) {
            return true;
        } else {
            throw new ServiceException("exception.user.login.password_not_match");
        }
    }

    public boolean modifyPassword(String userName, String oldPassword, String newPassword) throws ServiceException {
        Optional<SysUser> sysUser = sysUserRepository.findByUsername(userName);
        if (sysUser == null) {
            throw new ServiceException("exception.user.login.user_not_exit");
        }
        log.debug("表单中的密码是：[{}],输入的密码是:[{}]",sysUser.get().getPassword(),passwordEncoder.encode(oldPassword));
        if (!sysUser.get().getPassword().equals(passwordEncoder.encode(oldPassword))) {
            throw new ServiceException("exception.user.login.password_not_match");
        }
        sysUser.get().setPassword(passwordEncoder.encode(newPassword));
        sysUserRepository.save(sysUser.get());
        return true;
    }

    // 在 repository 中使用 cache
    //    @CachePut(key = "#a0.name")
    public SysUser update(SysUser sysUser) throws ServiceException {
        return sysUserRepository.save(sysUser);
    }
}
