//treeview 节点处理需要获取的对象
var draged_item;   //被拖拽的节点
var droped_item;   //被放入的节点
var item_children; //拖拽后产生的新子节点数组
//-------

$(document).on('dnd_stop.vakata', function (e, data) {
    Logger.debug(e);
});

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
            Logger.debug(node_parent);
            if (node_parent.parent == null) {
                return false;
            }
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
    "plugins": ["dnd", "state", "types", "contextmenu"],
    "contextmenu": {
        "items": {
            "create": null,
            "rename": null,
            "remove": null,
            "ccp": null,
            "delete": {
                "label": "删除",
                "action": function (obj) {
                    var inst = jQuery.jstree.reference(obj.reference);
                    var clickedNode = inst.get_node(obj.reference);
                    if (clickedNode.parent === "#") {
                        showMessage("warning", "警告", "不能删除根菜单");
                    } else {
                        //删除菜单项
                        fun_delete_menu(clickedNode.id);
                    }
                }
            }
        }
    }
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
        complete: function () {
            mApp.unblockPage();
        }
    })
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
        systemMenu.permission = $("#menu_permission").val();
        systemMenu.url = $("#menu_url").val();
        systemMenu.clazz = $("#menu_clazz").val();
        systemMenu.orderNum = $("#menu_order_num").val();
        $.ajax({
            type: operator,
            url: contextPath + "systemMenu.json",
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
            complete: function (data) {
                mApp.unblockPage();
            }
        });
        return false;
    })
})


function fun_render_jstree() {

    $.ajax({
        type: 'get',
        url: contextPath + "systemMenus4Treeviewer.json",
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
    Logger.debug(menu_info);
    if (menu_info.id === undefined) {
        $("button[name=btn_update]").attr('disabled', 'disabled');
        $("#menu_id").val("0");
    } else {
        $("#menu_id").val(menu_info.id);
        $("button[name=btn_update]").removeAttr('disabled');
    }
    $("#menu_name").val(menu_info.text);
    $("#menu_permission").val(menu_info.permission);
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


function fun_delete_menu(id) {
    bootbox.confirm({
        message: "确认要删除这条记录吗?一经删除，就无法恢复！",
        buttons: {
            confirm: {
                label: '删除',
                className: 'btn-success'
            },
            cancel: {
                label: '不删了',
                className: 'btn-danger'
            }
        },
        callback: function (result) {
            if (result) {
                mApp.blockPage({
                    overlayColor: "#000000",
                    type: "loader",
                    state: "success",
                    message: "正在删除，请稍等..."
                });
                $.ajax({
                    type: "delete",
                    url: contextPath + "systemMenu/" + id + ".json",
                    async: true,//默认为true
                    contentType: "application/json",
                    dataType: 'json',//默认为预期服务器返回的数据类型
                    beforeSend: function () {
                        mApp.blockPage({
                            overlayColor: "#000000",
                            type: "loader",
                            state: "success",
                            message: "正在进行删除..."
                        });
                    },
                    success: function (data, textStatus, jqXHR) {
                        fun_render_jstree();
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                        Logger.debug(jqXHR);
                        showMessage("danger", "错误", jqXHR.responseJSON.data[0].errorMessage);
                    },
                    complete: function () {
                        mApp.unblockPage();
                    }
                })
            }
        }
    });
}
