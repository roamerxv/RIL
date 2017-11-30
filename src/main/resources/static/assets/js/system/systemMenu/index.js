$().ready(function () {

    fun_render_jsTree();

    $("#jstree_system_menus_div1").jstree({
        "core": {
            "themes": {
                "responsive": false
            },
            // so that create works
            "check_callback": true,
            'data': [{
                "text": "系统菜单",
                "children": [{
                    "text": "Initially selected",
                    "state": {
                        "selected": true
                    }
                }, {
                    "text": "Custom Icon",
                    "icon": "fa fa-warning m--font-danger"
                }, {
                    "text": "Initially open",
                    "icon": "fa fa-folder m--font-success",
                    "state": {
                        "opened": true
                    },
                    "children": [
                        {"text": "Another node", "icon": "fa fa-file m--font-waring"}
                    ]
                }, {
                    "text": "Another Custom Icon",
                    "icon": "fa fa-warning m--font-waring"
                }, {
                    "text": "Disabled Node",
                    "icon": "fa fa-check m--font-success",
                    "state": {
                        "disabled": true
                    }
                }, {
                    "text": "Sub Nodes",
                    "icon": "fa fa-folder m--font-danger",
                    "children": [
                        {"text": "Item 1111", "icon": "flaticon-line-graph"},
                        {"text": "Item 2", "icon": "fa fa-file m--font-success"},
                        {"text": "Item 3", "icon": "fa fa-file m--font-default"},
                        {"text": "Item 4", "icon": "fa fa-file m--font-danger"},
                        {"text": "Item 5", "icon": "fa fa-file m--font-info"}
                    ]
                }]
            },
                "Another Node"
            ]
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
    });
})

function fun_render_jsTree() {

    $.ajax({
        type: 'get',
        url: contextPath + "systemMenu4Treeviewer",
        async: true,//默认为true
        contentType: "application/json",
        dataType: 'json',//默认为预期服务器返回的数据类型
        success: function (data, textStatus, jqXHR) {
            //加上一个 jsTree 的 root 节点
            var new_data = {
                "parent" : "#",
                "text" : "系统全局菜单",
                "children": null,
                "state":{
                    "opened": "true"
                }
            };
            new_data.children = data ;
            jsTree_type = $('#jstree_system_menus_div').jstree({
                "core": {
                    "themes": {
                        "responsive": false
                    },
                    // so that create works
                    "check_callback": true,
                    'data': new_data
                },
            })
        },
        error: function (jqXHR, textStatus, errorThrown) {
            showMessage("danger", "错误", jqXHR.responseJSON.data[0].errorMessage);
        },
    }).done(function (data) {

    });
}
