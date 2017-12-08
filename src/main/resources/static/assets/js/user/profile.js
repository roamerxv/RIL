$().ready(function () {

});


function fun_submit() {
    // 收集界面录入元素 begin
    var userEntity = {};
    var inputs = $("input[data-name]");
    $("input[data-name]").each(function () {
        userEntity[$(this).attr("data-name")] = $(this).val();
    })
    // 收集界面录入元素 end
    $.ajax({
        type: 'post',
        data: JSON.stringify(userEntity),
        url: contextPath + 'user/' + userEntity.username + ".json",
        async: true,//默认为true
        contentType: "application/json",
        dataType: 'json',//默认为预期服务器返回的数据类型
        beforeSend: function () {
            mApp.blockPage({
                overlayColor: "#000000",
                type: "loader",
                state: "success",
                message: "正在保存，请稍等..."
            });
        },
        success: function (data, textStatus) {
            showMessage("success", "成功", "更新完成！");
            window.location = contextPath + "user/profile";
        },
        error: function (jqXHR, textStatus, errorThrown) {
            showMessage("danger", "错误", jqXHR.responseJSON.data[0].errorMessage);
        },
        complete: function () {
            mApp.unblockPage();
        }
    });

}
