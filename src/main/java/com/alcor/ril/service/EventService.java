package com.alcor.ril.service;

import com.alcor.ril.persistence.entity.EventEntity;
import com.alcor.ril.persistence.repository.IEventRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Data
@Service("com.alcor.ril.service.EventService")
@Transactional()
public class EventService {
    @Qualifier("com.alcor.ril.repository.IEventRepository")
    @Autowired
    IEventRepository iEventRepository;


    /**
     * 列出所有需要在 start 和 end 时间段内显示的事件
     * 可以不管日期条件，返回所有记录。是否显示在日历里面。交给 fullcalendar 进行处理
     *
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
            eventEntityList = iEventRepository.findAlInDays(start, end);
        } catch (Exception e) {
            log.debug("asfd");
            throw new ServiceException(e.getMessage());
        }
        return eventEntityList;
    }

}
