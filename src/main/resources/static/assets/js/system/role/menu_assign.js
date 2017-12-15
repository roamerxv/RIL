var system_menu_tree = $('#jstree_system_menus_div').jstree({
    "check_callback": false,
    "core": {
        "themes": {
            "responsive": false
        },
        // so that create works
        "strings": {
            'Loading ...': '正在加载...'
        }
    },
    "types": {
        "default": {
            "icon": "fa fa-folder m--font-success"
        },
        "file": {
            "icon": "fa fa-file  m--font-success"
        }
    },
    "state": {"key": "demo2"},
    "plugins": ["dnd", "state", "types"],
}).on('select_node.jstree', function (e, data) {
    if (typeof(data.node) == "undefined") {
        return false;
    } else {
        var selected_node = data.node;
    }
}).on('move_node.jstree', function (e, data) {

});

//角色菜单
var role_menu_tree = $('#jstree_role_menus_div').jstree({
    "check_callback": false,
    "core": {
        "themes": {
            "responsive": false
        },
        // so that create works
        "strings": {
            'Loading ...': '正在加载...'
        }
    },
    "types": {
        "default": {
            "icon": "fa fa-folder m--font-success"
        },
        "file": {
            "icon": "fa fa-file  m--font-success"
        }
    },
    "state": {"key": "demo2"},
    "plugins": ["dnd", "state", "types"],
}).on('select_node.jstree', function (e, data) {
    if (typeof(data.node) == "undefined") {
        return false;
    } else {
        var selected_node = data.node;
    }
}).on('move_node.jstree', function (e, data) {

});

//监听系统菜单的双击事件，进行分配
system_menu_tree.bind("dblclick.jstree", function (event) {
    var node = $(event.target).closest("li");
    var data = node.data("jstree");
    Logger.debug("选择的菜单 ID 是:" + node[0].id);
    Logger.debug("准备分配给 role ID 是" + role_id);
    //菜单分配
    fun_assign_menu_to_role(node[0].id, role_id);
});

//监听系统菜单的双击事件，进行删除
role_menu_tree.bind("dblclick.jstree", function (event) {
    var node = $(event.target).closest("li");
    var data = node.data("jstree");
    Logger.debug("选择的菜单 ID 是:" + node[0].id);
    Logger.debug("准备分配给 role ID 是" + role_id);
    //角色菜单删除
    fun_delete_role_menu(node[0].id, role_id);

});


$().ready(function () {
    fun_render_system_menu();
    $("#m_modal_assign_menu").on('show.bs.modal', function () {
        fun_render_role_menu();
    });
})

//渲染系统菜单 tree view
function fun_render_system_menu() {
    $.ajax({
        type: 'get',
        url: contextPath + "systemMenus4Treeviewer",
        async: true,//默认为true
        contentType: "application/json",
        dataType: 'json',//默认为预期服务器返回的数据类型
        beforeSend: function () {
            mApp.block("#system_menu_div", {
                overlayColor: "#000000",
                type: "loader",
                state: "success",
                message: "正在获取系统菜单..."
            });
        },
        success: function (data, textStatus, jqXHR) {
            //加上一个 jsTree 的 root 节点
            var new_data = {
                "parent": "#",
                "text": "系统全局菜单",
                "children": null,
                "state": {
                    "opened": true,
                    "disabled": false
                }
            };
            new_data.children = data;
            system_menu_tree.jstree(true).settings.core.data = new_data;
            system_menu_tree.jstree("refresh");
        },
        error: function (jqXHR, textStatus, errorThrown) {
            showMessage("danger", "错误", jqXHR.responseJSON.data[0].errorMessage);
        },
        complete: function () {
            mApp.unblock("#system_menu_div");
        }
    })
};


function fun_render_role_menu() {
    $.ajax({
        type: 'get',
        url: contextPath + "role/showMenus/" + role_id + ".json",
        async: true,//默认为true
        contentType: "application/json",
        dataType: 'json',//默认为预期服务器返回的数据类型
        beforeSend: function () {
            mApp.block("#role_menu_div",{
                overlayColor: "#000000",
                type: "loader",
                state: "success",
                message: "正在刷新角色菜单..."
            });
        },
        success: function (data, textStatus, jqXHR) {
            //加上一个 jsTree 的 root 节点
            var new_data = {
                "parent": "#",
                "text": "角色的菜单",
                "children": null,
                "state": {
                    "opened": true,
                    "disabled": false
                }
            };
            new_data.children = data;
            role_menu_tree.jstree(true).settings.core.data = new_data;
            role_menu_tree.jstree("refresh");
        },
        error: function (jqXHR, textStatus, errorThrown) {
            showMessage("danger", "错误", jqXHR.responseJSON.data[0].errorMessage);
        },
        complete: function () {
            mApp.unblock("#role_menu_div");
        }
    })
}

//分配一个菜单项给一个 role
function fun_assign_menu_to_role(menu_id, role_id) {
    if (menu_id == "j1_1") {
        menu_id = "0"
    };
    $.ajax({
        type: 'post',
        url: contextPath + "/assign-menu-to-role/" + menu_id + "/" + role_id + ".json",
        async: true,//默认为true
        contentType: "application/json",
        dataType: 'json',//默认为预期服务器返回的数据类型
        beforeSend: function () {
            mApp.block("#m_modal_assign_menu .modal-content", {
                overlayColor: "#000000",
                type: "loader",
                state: "success",
                message: "正在分配菜单..."
            });
        },
        success: function (data, textStatus, jqXHR) {
            //刷新角色菜单
            fun_render_role_menu();
        },
        error: function (jqXHR, textStatus, errorThrown) {
            showMessage("danger", "错误", jqXHR.responseJSON.data[0].errorMessage);
        },
        complete: function () {
            mApp.unblock("#m_modal_assign_menu .modal-content");
        }
    })
}


//删除一个角色的菜单
function fun_delete_role_menu(menu_id,role_id){
    if (menu_id == "j2_1") {
        menu_id = "0"
    };
    $.ajax({
        type: 'delete',
        url: contextPath + "/assign-menu-to-role/" + menu_id + "/" + role_id + ".json",
        async: true,//默认为true
        contentType: "application/json",
        dataType: 'json',//默认为预期服务器返回的数据类型
        beforeSend: function () {
            mApp.block("#m_modal_assign_menu .modal-content", {
                overlayColor: "#000000",
                type: "loader",
                state: "success",
                message: "正在删除菜单..."
            });
        },
        success: function (data, textStatus, jqXHR) {
            //刷新角色菜单
            fun_render_role_menu();
        },
        error: function (jqXHR, textStatus, errorThrown) {
            showMessage("danger", "错误", jqXHR.responseJSON.data[0].errorMessage);
        },
        complete: function () {
            mApp.unblock("#m_modal_assign_menu .modal-content");
        }
    })
}
