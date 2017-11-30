package com.alcor.ril.controller.bean;

import com.alcor.ril.entity.SystemMenuEntity;
import lombok.Data;

import java.util.List;

/**
 * 系统菜单的 bean。用于由 controller 传递给前台转换成的 json
 *
 * @author roamer - 徐泽宇
 * @create 2017-11-2017/11/30  上午9:53
 */
@Data
public class SystemMenu {
    public SystemMenuEntity menuItem;
    public List<SystemMenu> childrenMenu;
}
