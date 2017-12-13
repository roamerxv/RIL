package com.alcor.ril.persistence.repository;

import com.alcor.ril.persistence.entity.RilRolePermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository()
public interface IRolePermissionRepository extends JpaRepository<RilRolePermissionEntity, String>, PagingAndSortingRepository<RilRolePermissionEntity, String> {
    public List<RilRolePermissionEntity> findAllByRoleId(String roleId);
}
