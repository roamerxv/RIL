$().ready(function () {

    fun_render_jsTree();
})

function fun_render_jsTree() {

    $.ajax({
        type: 'get',
        url: contextPath + "systemMenus4Treeviewer",
        async: true,//默认为true
        contentType: "application/json",
        dataType: 'json',//默认为预期服务器返回的数据类型
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
            jsTree_type = $('#jstree_system_menus_div').jstree({
                "check_callback": true,
                "core": {
                    "themes": {
                        "responsive": false
                    },
                    // so that create works
                    "check_callback": true,
                    'data': new_data
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
                "plugins": ["dnd", "state", "types"]
            }).on('changed.jstree', function (e, data) {
                Logger.debug(data);
                if (typeof(data.node) == "undefined") {
                    Logger.debug("undefined");
                    return false;
                } else {
                    var selected_node = data.node;
                    var menu_id = selected_node.id;
                    //根据 id 获取 菜单项的具体内容
                    fun_get_menu_item_info(selected_node.original);
                }
            });
        },
        error: function (jqXHR, textStatus, errorThrown) {
            showMessage("danger", "错误", jqXHR.responseJSON.data[0].errorMessage);
        },
    }).done(function (data) {

    });
};

function fun_get_menu_item_info(menu_info) {
    $("#menu_id").val(menu_info.id);
    $("#menu_name").val(menu_info.text);
    $("#menu_url").val(menu_info.url);
    $("#menu_clazz").val(menu_info.icon);
    $("#menu_parent_id").val(menu_info.parent);
}


function fun_submit() {
    var systemMenu = {};
    systemMenu.id = $("#menu_id").val();
    systemMenu.name = $("#menu_name").val();
    systemMenu.url = $("#menu_url").val();
    systemMenu.clazz = $("#menu_clazz").val();

    Logger.debug(systemMenu);
    $.ajax({
        type: 'post',
        url: contextPath + "systemMenu",
        async: true,//默认为true
        contentType: "application/json",
        dataType: 'json',//默认为预期服务器返回的数据类型
        data: JSON.stringify(systemMenu),
        success: function (data, textStatus, jqXHR) {
            window.location =  contextPath + "systemMenuMaintain";
        },
        error: function (jqXHR, textStatus, errorThrown) {
            showMessage("danger", "错误", jqXHR.responseJSON.data[0].errorMessage);
        },
    }).done(function (data) {

    });
}
