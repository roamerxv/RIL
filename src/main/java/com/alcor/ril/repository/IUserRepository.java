package com.alcor.ril.repository;

import com.alcor.ril.entity.UserEntity;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository("com.alcor.ril.repository.IUserRepository")
@CacheConfig(cacheNames = "spring:cache:UserEntity")
public interface IUserRepository extends JpaRepository<UserEntity, String>, PagingAndSortingRepository<UserEntity, String> {

    @Cacheable(key = "'user_'.concat(#a0)")
    public UserEntity findOne(String name);

    @CachePut(key = "'user_'.concat(#a0.name)")
    public UserEntity save(UserEntity userEntity);
}
