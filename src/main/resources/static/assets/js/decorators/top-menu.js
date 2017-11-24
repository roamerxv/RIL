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
    //刷新头像的图片
    $.ajax({
        type: 'get',
        url: contextPath + 'avatar.json',
        async: true,//默认为true
        contentType: "application/text",
        dataType: 'text',//默认为预期服务器返回的数据类型
        success: function ( data,  textStatus, jqXHR ) {
            Logger.debug(jqXHR);
            $("img[name='avatar']").attr("src", contextPath+jqXHR.responseText);
        },
        error: function ( jqXHR, textStatus, errorThrown ){
            Logger.debug(jqXHR);
        },
        complete: function () {
        }
    });
})
