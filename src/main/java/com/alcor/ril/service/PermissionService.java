package com.alcor.ril.service;

import com.alcor.ril.persistence.entity.SysPermission;
import com.alcor.ril.persistence.repository.SysPermissionRepository;
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
    private SysPermissionRepository sysPermissionRepository;

    public List<SysPermission> findAll() {
        return sysPermissionRepository.findAll();
    }
    
}
