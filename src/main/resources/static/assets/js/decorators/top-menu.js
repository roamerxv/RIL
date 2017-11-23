$().ready(function(){
    $.ajax({
        type: 'get',
        url: contextPath + 'user.json',
        async: true,//默认为true
        contentType: "application/json",
        dataType: 'json',//默认为预期服务器返回的数据类型
        success: function ( data,  textStatus, jqXHR ) {
            $("#top_menu_user_fullname").html(jqXHR.responseJSON.fullName);
            $("#top_menu_user_name").html(jqXHR.responseJSON.name);
        },
        error: function ( jqXHR, textStatus, errorThrown ){
            Logger.debug(jqXHR);

        },
        complete: function () {

        }
    });
})
