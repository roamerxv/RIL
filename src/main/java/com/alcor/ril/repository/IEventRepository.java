package com.alcor.ril.repository;

import com.alcor.ril.entity.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository("com.alcor.ril.repository.IEventRepository")
public interface IEventRepository extends JpaRepository<EventEntity, String>, PagingAndSortingRepository<EventEntity, String> {
//
//    /**
//     * 列出所有需要在 start 和 end 时间段内显示的事件
//     * 分成3种情况：(长竖线表示选择的时间段，短竖线表示事件的时间段)
//     *  1: 事件事件段在选择的时间段内
//     *               |                            |
//     *               |------|-----------|---------|
//     *  2： 事件时间段在选择时间段的左侧
//     *               |                    |
//     *       |-------|------|-------------|
//     *  3： 事件时间段在选择时间段的右侧
//     *               |                    |
//     *               |------|-------------|-------|
//     *
//     *
//     **/
//    @Query("select u from EventEntity u WHERE ( u.start >= ?1 AND u.end <= ?2 ) OR ( u.start < ?1 AND u.end >= ?2 AND u.end <= ?2) OR ( u.start >= ?1 AND u.start <= ?2  AND u.end > ?2 )")
//    public List<EventEntity> findAlInDays(Date start, Date end);
}
