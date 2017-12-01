package com.alcor.ril.controller.bean.treeview;

import lombok.Data;

import java.util.List;

/**
 * 配件 bootstrap treeview 的结构对象
 *
 * @author roamer - 徐泽宇
 * @create 2017-11-2017/11/30  下午12:11
 */
@Data
public class TreeViewItem {
    String text;
    String id;
    String icon;
    String parent;
    String url;
    List<TreeViewItem> children;
    ItemState state ;
}
