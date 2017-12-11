package com.alcor.ril.web.controller.bean.treeview;

import lombok.Data;

import java.util.List;

/**
 * 配件 bootstrap treeview 的结构对象
 *
 * @author roamer - 徐泽宇
 * @create 2017-11-2017/11/30  下午12:11
 */
@Data
public class Item {
    String text;
    Long id;
    String icon;
    Long parent;
    String url;
    int orderNum;
    List<Item> children;
    ItemState state;
}
