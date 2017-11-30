package com.alcor.ril.repository;

import com.alcor.ril.entity.SystemMenuEntity;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("com.alcor.ril.repository.ISystemMenuRepository")
@CacheConfig(cacheNames="spring:cache:SystemMenus")
public interface ISystemMenuRepository extends JpaRepository<SystemMenuEntity, String>, PagingAndSortingRepository<SystemMenuEntity, String> {

    public List<SystemMenuEntity> findAll();

    public List<SystemMenuEntity> findAllByParentId(String parentId);
}
