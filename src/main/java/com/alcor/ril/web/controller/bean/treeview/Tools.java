package com.alcor.ril.web.controller.bean.treeview;

import com.alcor.ril.web.controller.bean.MenuNode;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * TreeView 的工具类
 *
 * @author roamer - 徐泽宇
 * @create 2017-12-2017/12/13  下午12:13
 */
@Slf4j
public class Tools {

    /**
     * 递归把系统菜单的对象链表结构转换成用于 treeviewer 显示的链表结构
     *
     * @param systemMenuList
     *
     * @return
     */
    public List<Item> parseSystemMenu(List<MenuNode> systemMenuList) {
        if (systemMenuList == null) {
            return null;
        }
        List<Item> treeViewItemList = new ArrayList<>(systemMenuList.size());
        for (MenuNode item : systemMenuList) {
            Item treeViewItem = new Item();
            treeViewItem.setId(item.getMenuItem().getId());
            treeViewItem.setParent(item.getMenuItem().getParentId());
            treeViewItem.setText(item.getMenuItem().getName());
            treeViewItem.setIcon(item.getMenuItem().getClazz());
            treeViewItem.setUrl(item.getMenuItem().getUrl());
            treeViewItem.setOrderNum(item.getMenuItem().getOrderNum());
            treeViewItem.setPermission(item.getMenuItem().getPermission());
            ItemState itemState = new ItemState();
            itemState.setOpened(true);
            treeViewItem.setState(itemState);

            treeViewItemList.add(treeViewItem);
            if (!item.getChildrenMenu().isEmpty()) {
                treeViewItem.setChildren(this.parseSystemMenu(item.getChildrenMenu()));
            }
        }
        return treeViewItemList;
    }

    /**
     * 对链表对象进行过滤，只显示叶子节点包含在 ids 数组里面的链表
     *
     * @param items
     * @param ids
     */
    public void filtIncludeIds(List<Item> items, List<String> ids) {
        if(items == null){
            return ;
        }
        try{
            for (Iterator<Item> itemIterator = items.iterator(); itemIterator.hasNext(); ) {
                Item item = itemIterator.next();
                boolean usedIncludeChildren = this.contains(item, ids);
                if (usedIncludeChildren){
                    filtIncludeIds(item.getChildren(),ids);
                }else{
                    itemIterator.remove();
                }
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }

    }


    private boolean contains(Item menu, List<String> ids) {
        boolean m_rtn = false;
        if (menu.getChildren() == null || menu.getChildren().size() <= 0) {
            if (ids.contains(menu.getId())) {
                return true;
            } else {
                return false;
            }
        } else {
            for (int i = 0; i < menu.getChildren().size(); i++) {
                if (m_rtn) {
                    return true;
                } else {
                    m_rtn = contains(menu.getChildren().get(i), ids);
                }
            }
        }
        return m_rtn;
    }
}
