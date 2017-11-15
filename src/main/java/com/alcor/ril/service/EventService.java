package com.alcor.ril.service;

import com.alcor.ril.entity.EventEntity;
import com.alcor.ril.repository.IEventRepository;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author roamer - 徐泽宇
 * @create 2017-08-2017/8/8  下午5:57
 */
@Log4j2
@Data
@Service("com.alcor.ril.service.EventService")
@Transactional()
public class EventService {
    @Qualifier("com.alcor.ril.repository.IEventRepository")
    @Autowired
    IEventRepository iEventRepository;


    /**
     * 列出所有需要在 start 和 end 时间段内显示的事件
     * 不管日期条件，返回所有记录。是否显示在日历里面。交给 fullcalendar 进行处理
     * @param start
     * @param end
     *
     * @return
     *
     * @throws ServiceException
     */
    @Transactional(readOnly = true)
    public List<EventEntity> findAlInDays(Date start, Date end) throws ServiceException {
        List<EventEntity> eventEntityList;
        try {
            eventEntityList = iEventRepository.findAll();
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
        return eventEntityList;
    }

}
