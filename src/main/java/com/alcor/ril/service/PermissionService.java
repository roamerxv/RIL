package com.alcor.ril.service;

import com.alcor.ril.persistence.entity.SysPermissionEntity;
import com.alcor.ril.persistence.repository.ISysPermissionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created with admin.
 * User: duduba - 邓良玉
 * Date: 2017/12/8
 * Time: 21:52
 */
@Service
@Slf4j
@Transactional
public class PermissionService {

    @Autowired
    private ISysPermissionRepository iSysPermissionRepository;

    public List<SysPermissionEntity> findAll() {
        return iSysPermissionRepository.findAll();
    }
    
}
