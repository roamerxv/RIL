package com.alcor.ril.repository;

import com.alcor.ril.entity.RoleEntity;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository("com.alcor.ril.repository.IRoleRepository")
@CacheConfig(cacheNames = "spring:cache:UserEntity")
public interface IRoleRepository extends JpaRepository<RoleEntity, String>, PagingAndSortingRepository<RoleEntity, String> {

}
