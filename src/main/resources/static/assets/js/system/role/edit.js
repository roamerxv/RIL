$().ready(function () {
    $("#m_modal_role_edit").on('hidden.bs.modal', function () {
        $("#role-name").val("");
        $("#role-description").val("");
    });

    // 模式对话框被打开
    $("#m_modal_role_edit").on('shown.bs.modal', function () {
        if (operator === "edit"){
            Logger.debug("开始显示 id 是" + role_id + "的角色信息！");
            $.ajax({
                type: 'get',
                url: contextPath + 'role/' + role_id + '.json',
                async: true,//默认为true
                contentType: "application/json",
                dataType: 'json',//默认为预期服务器返回的数据类型
                beforeSend: function () {
                    mApp.block('#m_modal_role_edit .modal-content', {
                        overlayColor: '#000000',
                        state: 'primary',
                        message: "正在获取数据..."
                    });
                },
                success: function (data, textStatus, jqXHR) {
                    $("#role-id").val(data.id);
                    $("#role-name").val(data.name);
                    $("#role-description").val(data.description);
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    showMessage("danger", "错误", jqXHR.responseJSON.data[0].errorMessage);
                },
                complete: function () {
                    mApp.unblock('#m_modal_role_edit .modal-content');
                }
            });
        }else if (operator === "create"){
            
        }
    });


    $("#role-form").submit(function () {
        var ajax_type = "";
        if (operator === "edit"){
            ajax_type = "put";
        }else{
            ajax_type = "post"
        }
        var role = {};
        role.id = role_id;
        role.name = $("#role-name").val();
        role.description = $("#role-description").val();
        $.ajax({
            type: ajax_type,
            url: contextPath + 'role.json',
            async: true,//默认为true
            data: JSON.stringify(role),
            contentType: "application/json",
            dataType: 'json',//默认为预期服务器返回的数据类型
            beforeSend: function () {
                mApp.block('#m_modal_role_edit .modal-content', {
                    overlayColor: '#000000',
                    state: 'primary',
                    message: "正在更新数据..."
                });
            },
            success: function (data, textStatus, jqXHR) {
                role_table.ajax.reload();
                $("#m_modal_role_edit").modal("hide");
            },
            error: function (jqXHR, textStatus, errorThrown) {
                showMessage("danger", "错误", jqXHR.responseJSON.data[0].errorMessage);
            },
            complete: function () {
                mApp.unblock('#m_modal_role_edit .modal-content');
            }
        });
        return false;
    })
});
