var businesslog_table;

$().ready(function () {
    activeMenu("menu_system_log");

    if ($('#begin_time') == undefined || $('#begin_time') == undefined) {
        Logger.error("没有定义开始时间和结束时间的 UI 组件");
    } else {
        var datetimepickerformat = "Y-m-d H:i"
        jQuery.datetimepicker.setLocale('zh');
        $('#begin_time').datetimepicker({
            format: datetimepickerformat
        });
        $('#end_time').datetimepicker({
            format: datetimepickerformat
        });
    }


    // 设置 datatables 的错误，不做抛出。以便接管错误信息
    $.fn.dataTable.ext.errMode = 'none';

    businesslog_table = $("#businesslog_table").DataTable({
        // "scrollX": true,  //如果使用这个选项，会导致表格的内容向右移动。
        "autoWidth": true,
        "processing": true,
        "serverSide": true,
        "stateSave": true,
        "fixedHeader": true, //需要映入响应的 js 文件
        "ajax": {
            url: contextPath + "/system/businesslog/getDataWithPaged.json",
            type: 'post',
            data: function (data) {
                data.beginTime = $("#begin_time").val();
                data.endTime = $("#end_time").val();
                return JSON.stringify(data);
            },
            error: function (jqXHR, textStatus, errorThrown) {
                var responseText = JSON.parse(jqXHR.responseText);
                showMessage("error", responseText.data[0].errorMessage);
            },
            dataType: "json",
            processData: true,
            contentType: 'application/json;charset=UTF-8',
            dataSrc: 'logs'
        },
        "language": {
            "url": contextPath + "/assets/js/lib//DataTables-1.10.16/chinese.lang.json"
        },
        "columns": [{
            "data": "createdAt" // 发生时间
        }, {
            "data": "operator" // 操作员
        }, {
            "data": "methodDescription" // 事件描述
        }, {
            "data": "remoteIp" // IP地址
        }, {
            "data": "clientOs" // 操作系统
        }, {
            "data": "clientBrowser" // 浏览器
        }, {
            "data": "browserVersion" // 浏览器版本
        }, {
            "data": "clientDeviceType" // 访问设备
        }, {
            "data": "timeConsuming" // 耗时
        }, {
            "data": "success" // 调用是否成功
        }],
        "order": [[0, "desc"]],
        "columnDefs": [{
            "orderable": false,
            "targets": [1, 2, 3, 4, 5, 6, 7, 8]
        }, {
            "render": function (data, type, row) {
                var prompt = "";
                if (data) {
                    prompt = "成功";
                } else {
                    prompt = "失败";
                }
                return prompt;
            },
            "targets": 9
        }],
        "createdRow": function (row, data, index) {
            if (data.success) {
                $('td', row).eq(9).addClass('success_type');
            } else {
                $('td', row).eq(9).addClass('fail_type');
            }
        },
        "dom": 'flpitrp', //定制表格各个部件的位置 . 查看文档 https://datatables.net/reference/option/dom
        "fnDrawCallback": function (oSettings) {
            $("div.toolbar").html('');
        }
    });
});

function fun_filterByDateTime() {
    businesslog_table.ajax.reload();
};

function fun_cleanDateTimeCondition() {
    $('#begin_time').val("");
    $('#end_time').val("");
    return false;
}
