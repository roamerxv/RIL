var role_table;
var role_id ;
var operator ;

$().ready(function () {

    // 设置 datatables 的错误，不做抛出。以便接管错误信息
    $.fn.dataTable.ext.errMode = 'none';

    role_table = $("#role_table").DataTable({
        "autoWidth": true,
        "processing": true,
        "serverSide": false,
        "stateSave": true,
        "language": {
            "url": contextPath + "/assets/js/lib//DataTables-1.10.16/chinese.lang.json"
        },
        "dom": 'lpfitrp', //定制表格各个部件的位置 . 查看文档 https://datatables.net/reference/option/dom
        "select": false,
        "ajax": {
            url: contextPath + "role/getData4Datatables.json",
            type: 'get',
            error: function (jqXHR, textStatus, errorThrown) {
                var responseText = JSON.parse(jqXHR.responseText);
                showMessage("error", responseText.data[0].errorMessage);
            },
            dataType: "json",
            processData: true,
            contentType: 'application/json;charset=UTF-8',
            dataSrc: ''
        },
        "columns": [{
            "data": "name"
        }, {
            "data": "description"
        }],
        "columnDefs": [
            // {
            //     "targets": [0],
            //     "visible": false,
            // },
            {
                "targets": [2],
                "orderable": false,
                "render": function (data, type, row, meta) {
                    return '<a href="javascript:fun_role_edit(\'' + row.id + '\')" class="m-portlet__nav-link btn m-btn m-btn--hover-accent m-btn--icon m-btn--icon-only m-btn--pill" title="编辑"><i class="la la-edit"></i></a>' +
                        '<a href="javascript:fun_role_delete(\'' + row.id + '\')" class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" title=" 删除"><i class="la la-trash"></i></a>'
                }
            }],
    }).on('select', function (e, dt, type, indexes) {

    });
    ;


    $('#role_table tbody').on('dblclick', 'tr', function () {

    });
});


function fun_role_edit (id) {
    operator = "edit";
    role_id = id ;
    $('#m_modal_role_edit').modal('show');
}

function fun_create(){
    operator = "create";
    $('#m_modal_role_edit').modal('show');
}

function fun_role_delete(id){
    bootbox.confirm({
        message: "确认要删除这条记录吗?一经删除，就无法恢复！",
        buttons: {
            confirm: {
                label: '删除',
                className: 'btn-success'
            },
            cancel: {
                label: '不删了',
                className: 'btn-danger'
            }
        },
        callback: function (result) {
            if (result) {
                mApp.blockPage({
                    overlayColor: "#000000",
                    type: "loader",
                    state: "success",
                    message: "正在删除，请稍等..."
                });
                $.ajax({
                    type: 'delete',
                    url: contextPath + 'role/' + id + ".json",
                    async: false,//默认为true
                    contentType: "application/json",
                    dataType: 'json',//默认为预期服务器返回的数据类型
                    success: function (data, textStatus, jqXHR) {
                        if (typeof role_table == 'undefined') {
                            window.location = contextPath;
                        } else {
                            role_table.ajax.reload();
                            mApp.unblock();
                        }
                    },
                    error: function ( jqXHR, textStatus, errorThrown ) {
                        showMessage("danger", "错误", jqXHR.responseJSON.data[0].errorMessage);
                        mApp.unblock();
                    }
                });
            }
        }
    });
}
