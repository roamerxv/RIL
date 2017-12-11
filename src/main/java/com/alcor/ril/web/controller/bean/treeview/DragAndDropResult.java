package com.alcor.ril.web.controller.bean.treeview;

import lombok.Data;

import java.util.List;

/**
 * 拖拉节点后产生的结构类
 *
 * @author roamer - 徐泽宇
 * @create 2017-12-2017/12/5  上午10:55
 */
@Data
public class DragAndDropResult {
    public String id;
    public String parentId;
    public List<String> children;
}
