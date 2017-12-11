package com.alcor.ril.web.controller;

import com.alcor.ril.persistence.entity.EventEntity;
import com.alcor.ril.service.EventService;
import com.alcor.ril.service.ServiceException;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import pers.roamer.boracay.aspect.httprequest.SessionCheckKeyword;
import pers.roamer.boracay.helper.JsonUtilsHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 提醒事件的 controller 类
 *
 * @author roamer - 徐泽宇
 * @create 2017-10-2017/10/11  下午12:13
 */
@Slf4j
@Controller("com.alcor.cns.controller.FullCalendarController")
@SessionCheckKeyword(checkIt = true)
public class FullCalendarController extends BaseController {

    @Autowired
    EventService eventService;


    /**
     * 获取日期时间内的事件列表
     *
     * @param start 这个参数由 fullCalendar 组件自动传入，缺省的 key 就是是 start ，代表用户选择视图的开始日期
     * @param end   这个参数由 fullCalendar 组件自动传入，缺省的 key 就是是 end，代表用户选择视图的结束日期
     *
     * @return
     *
     * @throws ControllerException
     */
    @GetMapping("/fullcalendar/events")
    @ResponseBody
    public String listEvents(@RequestParam String start, @RequestParam String end) throws ControllerException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        log.debug("传入的日期是从{}到{}", start, end);
        List<FullCalendarEvent> fullCalendarEventList = new ArrayList<>();
        EventsDateCondition eventsDateCondition = new EventsDateCondition();
        try {
            eventsDateCondition.setBeginDate(dateFormat.parse(start));
            eventsDateCondition.setEndDate(dateFormat.parse(end));
            log.debug("开始的日期{}，结束的日期{}", eventsDateCondition.getBeginDate(), eventsDateCondition.getEndDate());

            List<EventEntity> eventEntityList = eventService.findAlInDays(eventsDateCondition.getBeginDate(), eventsDateCondition.getEndDate());
            eventEntityList.forEach(item -> {
                FullCalendarEvent fullCalendarEvent = new FullCalendarEvent();
                fullCalendarEvent.id = item.getId();
                fullCalendarEvent.title = item.getName();
                fullCalendarEvent.start = dateTimeFormat.format(item.getStart());
                fullCalendarEvent.end = dateTimeFormat.format(item.getEnd());
                fullCalendarEvent.url = servletContext.getContextPath() + "/gatherInfos/" + item.getId();
                //fullCalendarEvent.description = fullCalendarEvent.start + "<br/>" + fullCalendarEvent.end +"<br/><font color='red'>" + item.getName()+"</font>";
                fullCalendarEvent.description = item.getName();
                fullCalendarEvent.className = "m-fc-event--danger m-fc-event--solid-focus";
                fullCalendarEventList.add(fullCalendarEvent);
            });
            String m_rtn;
            m_rtn = JsonUtilsHelper.objectToJsonString(fullCalendarEventList);
            log.debug("返回的fullCalendar 的 events 数据是:{}", m_rtn);
            return m_rtn;
        } catch (JsonProcessingException | ParseException | ServiceException e) {
            e.printStackTrace();
            throw new ControllerException(e.getMessage());
        }
    }
}

@Data
class EventsDateCondition {
    Date beginDate;
    Date endDate;
}

@Data
class FullCalendarEvent {
    String id;
    String title;
    boolean allDay;
    String start;
    String end;
    String url;
    String description;
    String className;
}
