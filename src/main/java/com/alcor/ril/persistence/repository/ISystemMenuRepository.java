package com.alcor.ril.persistence.repository;

import com.alcor.ril.entity.SystemMenuEntity;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository("com.alcor.ril.repository.ISystemMenuRepository")
@CacheConfig(cacheNames = "spring:cache:SystemMenuEntity")
public interface ISystemMenuRepository extends JpaRepository<SystemMenuEntity, String>, PagingAndSortingRepository<SystemMenuEntity, String> {

    public List<SystemMenuEntity> findAll();

    @Cacheable(key = "'ListByParentId_'.concat(#a0)")
    public List<SystemMenuEntity> findAllByParentIdOrderByOrderNum(String parentId);

    //    @CacheEvict(key = "'ListByParentId_'.concat(#a0.parentId)")
    @CacheEvict(allEntries = true) //为了避免麻烦。把所有spring:cache:SystemMenuEntity开头的 cache 都清除
    public SystemMenuEntity save(SystemMenuEntity systemMenuEntity);


    @Modifying
    @Query("update SystemMenuEntity  a set a.orderNum = :orderNum WHERE  a.id = :id ")
    public int changeOrderNumWithId(@Param("id") String id, @Param("orderNum") int orderNum);
}
