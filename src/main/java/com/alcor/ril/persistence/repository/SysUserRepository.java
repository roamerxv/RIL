package com.alcor.ril.persistence.repository;

import com.alcor.ril.persistence.entity.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("com.alcor.ril.persistence.repository.SysUserRepository")
public interface SysUserRepository extends JpaRepository<SysUser, Long> {
    /**通过username查找用户信息;*/
    Optional<SysUser> findByUsername(String username);

    Optional<SysUser> findByEmail(String email);

    Optional<SysUser> findById(Long id);

    @Modifying
    @Query("update SysUser ui set ui.status = 1 where ui.id = :id")
    int activeUser(@Param("id") Long id);

//    @CachePut(key = "'user_'.concat(#a0.name)")
    public SysUser save(SysUser userEntity);
}
