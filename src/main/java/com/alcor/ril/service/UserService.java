package com.alcor.ril.service;

import com.alcor.ril.entity.UserEntity;
import com.alcor.ril.repository.IUserRepository;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author roamer - 徐泽宇
 * @create 2017-08-2017/8/8  下午5:57
 */
@Log4j2
@Data
@Service("com.alcor.ril.service.UserService")
@CacheConfig(cacheNames = "UserEntity")
public class UserService {
    @Qualifier("com.alcor.ril.repository.IUserRepository")
    @Autowired
    IUserRepository iUserRepository;


    @Cacheable(key = "#name")
    public UserEntity findByName(String name) {
        log.debug("cache 没有命中，进行数据库查询！");
        return iUserRepository.findOne(name);
    }


    @Transactional(readOnly = true)
    public boolean login(UserEntity userEntity) throws ServiceException {
        UserEntity userEntityInDB = iUserRepository.findOne(userEntity.getName());
        if (userEntityInDB == null) {
            throw new ServiceException("exception.user.login.user_not_exit");
        }
        if (userEntityInDB.getPasswd().equalsIgnoreCase(userEntity.getPasswd())) {
            return true;
        } else {
            throw new ServiceException("exception.user.login.password_not_match");
        }
    }

    public boolean modifyPassword(String userName, String oldPassword, String newPassword) throws ServiceException {
        UserEntity userEntity = iUserRepository.findOne(userName);
        if (userEntity == null) {
            throw new ServiceException("exception.user.login.user_not_exit");
        }
        if (!userEntity.getPasswd().equals(oldPassword)) {
            throw new ServiceException("exception.user.login.password_not_match");
        }
        userEntity.setPasswd(newPassword);
        iUserRepository.save(userEntity);
        return true;
    }

    @CachePut(key = "#userEntity.name")
    public UserEntity update(UserEntity userEntity) throws ServiceException {
        return iUserRepository.save(userEntity);
    }
}
