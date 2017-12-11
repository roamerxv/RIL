package com.alcor.ril.web.controller.bean;

import com.alcor.ril.persistence.entity.SysPermissionEntity;
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
    public SysPermissionEntity menuItem;
    public List<SystemMenu> childrenMenu;
}
