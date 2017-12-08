package com.alcor.ril.service;

import com.alcor.ril.entity.SystemConfigureEntity;
import com.alcor.ril.persistence.repository.ISystemConfigureRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * @author roamer - 徐泽宇
 * @create 2017-08-2017/8/8  下午5:57
 */
@Slf4j
@Data
@Service("com.alcor.ril.service.SystemConfigureService")
public class SystemConfigureService {
    @Qualifier("com.alcor.ril.repository.ISystemConfigureRepository")
    @Autowired
    ISystemConfigureRepository iSystemConfigureRepository;

    public List<SystemConfigureEntity> findAll() throws ServiceException {
        return iSystemConfigureRepository.findAll(new Sort(Sort.Direction.ASC, "SortNo"));
    }

    public SystemConfigureEntity findByName(String name) throws  ServiceException{
        return iSystemConfigureRepository.findOne(name);
    }

    @Transactional()
    public SystemConfigureEntity update( SystemConfigureEntity systemConfigureEntity) throws  ServiceException{
        return iSystemConfigureRepository.save(systemConfigureEntity);
    }

    @Transactional()
    public void delete( String id) throws  ServiceException{
        iSystemConfigureRepository.delete(id);
    }

}
