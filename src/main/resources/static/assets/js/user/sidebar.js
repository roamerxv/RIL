$().ready(function () {
    var admin_role_id = "1"
    $.ajax({
        type: 'get',
        url: contextPath + "role/showMenus/" + admin_role_id + ".json",
        async: true,//默认为true
        contentType: "application/json",
        dataType: 'json',//默认为预期服务器返回的数据类型
        beforeSend: function () {
            mApp.block("#metronic_sidebar_menu", {
                overlayColor: "#000000",
                type: "loader",
                state: "success",
                size: 'ls'
            });
        },
        success: function (data, textStatus, jqXHR) {
            //生成菜单
            MetronicSidebarMenu($("#metronic_sidebar_menu"), data);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            showMessage("danger", "错误", jqXHR.responseJSON.data[0].errorMessage);
        },
        complete: function () {
            mApp.unblock("#metronic_sidebar_menu");
        }
    })
});

