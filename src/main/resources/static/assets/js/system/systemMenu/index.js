var system_menu_tree = $('#jstree_system_menus_div').jstree({
    "check_callback": true,
    "core": {
        "themes": {
            "responsive": false
        },
        // so that create works
        "check_callback": true,
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
}).on('changed.jstree', function (e, data) {
    if (typeof(data.node) == "undefined") {
        return false;
    } else {
        var selected_node = data.node;
        //根据 id 获取 菜单项的具体内容
        fun_get_menu_item_info(selected_node.original);
    }
});

var jsTree_data;
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

    });
};

function fun_get_menu_item_info(menu_info) {
    if (menu_info.id === undefined) {
        $("button[name=btn_update]").attr('disabled','disabled');
        $("#menu_id").val("0");
    } else {
        $("#menu_id").val(menu_info.id);
        $("button[name=btn_update]").removeAttr('disabled');
    }
    $("#menu_name").val(menu_info.text);
    $("#menu_url").val(menu_info.url);
    $("#menu_clazz").val(menu_info.icon);
    $("#menu_parent_id").val(menu_info.parent);
    if(menu_info.parent === "0" || menu_info.parent === "#"){
        $("button[name=btn_create]").removeAttr('disabled');
    }else{
        $("button[name=btn_create]").attr('disabled','disabled');
    }
}

function fun_submit_create() {
    alert("create");
}

