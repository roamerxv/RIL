package com.alcor.ril.repository;

import com.alcor.ril.entity.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository("com.alcor.ril.repository.IEventRepository")
public interface IEventRepository extends JpaRepository<EventEntity, String>, PagingAndSortingRepository<EventEntity, String> {

    /**
     * 列出所有需要在 start 和 end 时间段内显示的事件
     * 分成3种情况：(长竖线表示选择的时间段，短竖线表示事件的时间段)
     *  1: 事件事件段在选择的时间段内
     *               |                            |
     *               |------|-----------|---------|
     *  2： 事件时间段在选择时间段的左侧
     *               |                    |
     *       |-------|------|-------------|
     *  3： 事件时间段在选择时间段的右侧
     *               |                    |
     *               |------|-------------|-------|
     *  4： 事件时间段在选择时间段的外面
     *                      |             |
     *               |------|-------------|-------|
     *
     *  反过来说，只要排除以下2种情况，剩下的都是满足的记录
     *
     *                     |                |
     *  1.   |------------||----------------|
     *
     *  2.   |            |
     *       |------------||----------------|
     *
     **/
    @Query("select u from EventEntity u WHERE u.id NOT IN ( SELECT O FROM EventEntity O WHERE O.start > ?2 OR  O.end < ?1)")

    public List<EventEntity> findAlInDays(Date start, Date end);
}
