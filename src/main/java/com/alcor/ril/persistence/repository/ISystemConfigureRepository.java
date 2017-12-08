package com.alcor.ril.persistence.repository;

import com.alcor.ril.entity.SystemConfigureEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository("com.alcor.ril.repository.ISystemConfigureRepository")
public interface ISystemConfigureRepository extends JpaRepository<SystemConfigureEntity, String>, PagingAndSortingRepository<SystemConfigureEntity, String> {
}
