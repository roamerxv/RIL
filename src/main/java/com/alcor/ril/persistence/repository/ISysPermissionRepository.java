package com.alcor.ril.persistence.repository;

import com.alcor.ril.persistence.entity.SysPermissionEntity;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
@CacheConfig(cacheNames = "spring:cache:SystemPermission")
public interface ISysPermissionRepository extends JpaRepository<SysPermissionEntity, Long> {
    @Cacheable(key = "'ListByParentId_'.concat(#a0)")
    public List<SysPermissionEntity> findAllByParentIdOrderByOrderNum(Long parentId);

    //    @CacheEvict(key = "'ListByParentId_'.concat(#a0.parentId)")
    @CacheEvict(allEntries = true) //为了避免麻烦。把所有spring:cache:SystemMenuEntity开头的 cache 都清除
    public SysPermissionEntity save(SysPermissionEntity sysPermissionEntity);


    @Modifying
    @Query("update SysPermissionEntity  a set a.orderNum = :orderNum WHERE  a.id = :id ")
    public int changeOrderNumWithId(@Param("id") Long id, @Param("orderNum") int orderNum);
}
