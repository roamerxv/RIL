package com.alcor.ril.persistence.repository;

import com.alcor.ril.persistence.entity.SysPermission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SysPermissionRepository extends JpaRepository<SysPermission, Long> {
}