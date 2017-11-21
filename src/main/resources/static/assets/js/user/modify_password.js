$().ready(function () {
  
});

function fun_modify_password() {
    var old_password = $("#oldpassword").val();
    if ( $("#newpassword").val() == $("#newpassword-v").val() ){
        mApp.blockPage({
            overlayColor: "#000000",
            type: "loader",
            state: "success",
            message: "正在更新密码，请稍等..."
        });
        $.ajax({
            type: 'post',
            url: contextPath + 'user/modify_password.json?oldPassword=' + old_password + "&newPassword="+ $("#newpassword").val(),
            async: true,//默认为true
            contentType: "application/json",
            dataType: 'json',//默认为预期服务器返回的数据类型
            success: function ( data,  textStatus, jqXHR ) {
                Logger.debug(jqXHR);
                showMessage("success", "成功", jqXHR.responseJSON.data.localMessage);
            },
            error: function ( jqXHR, textStatus, errorThrown ){
                Logger.debug(jqXHR);
                mApp.unblock();
                showMessage("danger", "错误", jqXHR.responseJSON.data[0].errorMessage);
            },
            complete: function () {
                mApp.unblockPage();
                return true;
            }
        });
    }else{
        showMessage("danger", "错误",  "新输入的密码不符合");
        return false;
    }
}
