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


$().ready(function () {
    fun_render_system_menu();
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
        complete: function () {
            mApp.unblockPage();
        }
    })
};
