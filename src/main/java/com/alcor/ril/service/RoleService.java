package com.alcor.ril.service;

import com.alcor.ril.persistence.entity.SysRoleEntity;
import com.alcor.ril.persistence.repository.IRoleRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * @author roamer - 徐泽宇
 * @create 2017-08-2017/8/8  下午5:57
 */
@Slf4j
@Data
@Service("com.alcor.ril.service.RoleService")
@Transactional
public class RoleService {
    @Qualifier("com.alcor.ril.repository.IRoleRepository")
    @Autowired
    IRoleRepository iRoleRepository;

    @Transactional(readOnly = true)
    public List<SysRoleEntity> findAll() throws ServiceException {
        return iRoleRepository.findAll();
    }

    public SysRoleEntity findById(String id) throws ServiceException {
        return iRoleRepository.findOne(id);
    }


    public SysRoleEntity update(SysRoleEntity roleEntity) throws ServiceException {
        return iRoleRepository.save(roleEntity);
    }

    public void delete(String id) throws ServiceException {
        iRoleRepository.delete(id);
    }
}
