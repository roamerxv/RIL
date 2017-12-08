package com.alcor.ril.web.controller.bean.treeview;

import lombok.Data;

/**
 * TreeViewer Item 的 state
 *
 * @author roamer - 徐泽宇
 * @create 2017-11-2017/11/30  下午2:24
 */
@Data
public class ItemState {
    public boolean opened = true;
    public boolean disabled = false;
}
