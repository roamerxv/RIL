var role_table;

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
    });
});

