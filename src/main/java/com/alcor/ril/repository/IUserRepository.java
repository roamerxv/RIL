package com.alcor.ril.repository;

import com.alcor.ril.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository("com.alcor.ril.repository.IUserRepository")
public interface IUserRepository extends JpaRepository<UserEntity, String>, PagingAndSortingRepository<UserEntity, String> {
}
