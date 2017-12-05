//treeview 节点处理需要获取的对象
var draged_item;   //被拖拽的节点
var droped_item;   //被放入的节点
var item_children; //拖拽后产生的新子节点数组
//-------

var system_menu_tree = $('#jstree_system_menus_div').jstree({
    "check_callback": true,
    "core": {
        "themes": {
            "responsive": false
        },
        // so that create works
        "check_callback": function (operation, node, node_parent, node_position, more) {
            draged_item = node;
            droped_item = node_parent;

            if (node_parent.parent == null) {
                return false;
            }

            // if (node_parent.type == "#" && node_parent.id == "#") {
            //     Logger.debug(" 拖拽到了 root 节点");
            //     return true;
            // } else if (node_parent.type == "default") {
            //     Logger.debug("拖拽到了菜单节点");
            //     if (node_parent.original.parent == "0") {
            //         Logger.debug("说明是一级菜单，可以把菜单放放入");
            //         // 判断被加入的菜单项下是否有子菜单，如果有，则不许加入.
            //         if (node.children == null || node.children.length <= 0) {
            //             //Logger.debug("是被拖拉的菜单没有字菜单了。可以被增加为子菜单")
            //             return true;
            //         } else {
            //             return false;
            //         }
            //     } else {
            //         Logger.debug("是二级菜单，不能再增加子菜单了");
            //         return false;
            //     }
            // } else {
            //     //Logger.debug("没有拖拽到有效节点");
            //     return true;
            // }

            return true;
        },
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
        //根据 id 获取 菜单项的具体内容
        fun_get_menu_item_info(selected_node.original);
    }
}).on('move_node.jstree', function (e, data) {
    Logger.debug("拖拽的节点：")
    Logger.debug(draged_item);
    Logger.debug("被拽入的节点是：")
    Logger.debug(droped_item);
    Logger.debug("产生新的子节点数组是：")
    item_children = droped_item.children;
    Logger.debug(item_children)
    var result = {};
    result.id = draged_item.id;
    result.parentId = droped_item.id === 'j1_1' ? "0" : droped_item.id;
    result.children = item_children;
    Logger.debug(result);

    $.ajax({
        type: "put",
        url: contextPath + "systemMenu/resort.json",
        async: true,//默认为true
        contentType: "application/json",
        dataType: 'json',//默认为预期服务器返回的数据类型
        data: JSON.stringify(result),
        beforeSend: function () {
            mApp.blockPage({
                overlayColor: "#000000",
                type: "loader",
                state: "success",
                message: "正在保存..."
            });
        },
        success: function (data, textStatus, jqXHR) {
            fun_render_jstree();
        },
        error: function (jqXHR, textStatus, errorThrown) {
            Logger.debug(jqXHR);
            showMessage("danger", "错误", jqXHR.responseJSON.data[0].errorMessage);
        },
    }).done(function (data) {
        mApp.unblockPage();
    });
});

var operator;

$().ready(function () {
    fun_render_jstree();
    $("button[name=btn_update]").click(function () {
        operator = "put";
    });
    $("button[name=btn_create]").click(function () {
        operator = "post";
    });

    $("#submit_form").submit(function () {
        var systemMenu = {};
        systemMenu.parentId = $("#menu_parent_id").val();
        systemMenu.id = $("#menu_id").val();
        systemMenu.name = $("#menu_name").val();
        systemMenu.url = $("#menu_url").val();
        systemMenu.clazz = $("#menu_clazz").val();
        systemMenu.orderNum = $("#menu_order_num").val();
        $.ajax({
            type: operator,
            url: contextPath + "systemMenu",
            async: true,//默认为true
            contentType: "application/json",
            dataType: 'json',//默认为预期服务器返回的数据类型
            data: JSON.stringify(systemMenu),
            beforeSend: function () {
                mApp.blockPage({
                    overlayColor: "#000000",
                    type: "loader",
                    state: "success",
                    message: "正在更新信息..."
                });
            },
            success: function (data, textStatus, jqXHR) {
                fun_render_jstree();
            },
            error: function (jqXHR, textStatus, errorThrown) {
                showMessage("danger", "错误", jqXHR.responseJSON.data[0].errorMessage);
            },
        }).done(function (data) {
            mApp.unblockPage();
        });
        return false;
    })
})


function fun_render_jstree() {

    $.ajax({
        type: 'get',
        url: contextPath + "systemMenus4Treeviewer",
        async: true,//默认为true
        contentType: "application/json",
        dataType: 'json',//默认为预期服务器返回的数据类型
        beforeSend: function () {
            mApp.blockPage({
                overlayColor: "#000000",
                type: "loader",
                state: "success",
                message: "正在刷新菜单..."
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
    }).done(function (data) {
        mApp.unblockPage();
    });
};

function fun_get_menu_item_info(menu_info) {
    if (menu_info.id === undefined) {
        $("button[name=btn_update]").attr('disabled', 'disabled');
        $("#menu_id").val("0");
    } else {
        $("#menu_id").val(menu_info.id);
        $("button[name=btn_update]").removeAttr('disabled');
    }
    $("#menu_name").val(menu_info.text);
    $("#menu_url").val(menu_info.url);
    $("#menu_clazz").val(menu_info.icon);
    $("#menu_parent_id").val(menu_info.parent);
    $("#menu_order_num").val(menu_info.orderNum);
    if (menu_info.parent === "0" || menu_info.parent === "#") {
        $("button[name=btn_create]").removeAttr('disabled');
    } else {
        $("button[name=btn_create]").attr('disabled', 'disabled');
    }
}

