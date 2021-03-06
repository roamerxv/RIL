package com.alcor.ril.persistence.repository;

import com.alcor.ril.persistence.entity.SysRoleEntity;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository()
@CacheConfig(cacheNames = "spring:cache:UserEntity")
public interface IRoleRepository extends JpaRepository<SysRoleEntity, String>, PagingAndSortingRepository<SysRoleEntity, String> {

}
