/**
 * Created by roamer on 2017/5/18.
 */


var tipsAPI = new Array();

/**
 * 清除当前页面所有的 dom 的错误 tips
 */
function cleanAllExceptionTip() {
    for (var index in  tipsAPI) {
        tipsAPI[index].toggle(false);
    }
    //清空上次显示 tip 的 dom 对象数组
    tipsAPI = [];
}

/**
 * 根据返回的内容，使用qtip2进行错误提示。
 * 错误内容显示在相应的 dom 上
 * @param exception
 */
function showExceptionTip(exception) {
    var error_information = JSON.parse(exception.responseText);
    for (var index in error_information.data) {
        var exceptionMessage = error_information.data[index].errorMessage;
        var nodes = error_information.data[index].nodesName;
        tipsAPI = [];
        for (var nodeIndex in nodes) {
            var domNeedTip = $("#" + nodes[nodeIndex]);
            var node_name = nodes[nodeIndex]
            if (domNeedTip.exist()) {
                var dom = $("[name='" + node_name + "']");
                var error_len = dom.parent().find('label.error').length > 0;
                if (error_len) {
                    dom.parent().find('label.error').detach();
                }

                domNeedTip.focus();
                var api = domNeedTip.qtip({
                    content: exceptionMessage,
                    style: {
                        classes: 'qtip-red qtip-shadow'
                    },
                    position: {
                        my: 'center left',
                        at: 'center right'
                    },
                    show: {
                        target: $(this)
                    },
                });
                api.toggle(true);
                tipsAPI.push(api);
            } else {
                $.notify({
                    title: "<strong>错误</strong> ",
                    message: exceptionMessage
                }, {
                    type: "danger",
                    placement: {
                        from: 'bottom',
                        align: 'right'
                    }
                });
            }
        }
    }
}

/**
 * 使用 BootstrapNotify 组件进行消息提示
 * @param type
 * @param message
 */
function showMessage(type, title, message) {
    $.notify({
        title: "<strong>" + title + "</strong> ",
        message: message
    }, {
        type: type,
        placement: {
            from: 'bottom',
            align: 'right'
        }
    });
}

// 使用 jquery 判断 dom 是否存在
(function ($) {
    $.fn.exist = function () {
        if ($(this).length >= 1) {
            return true;
        }
        return false;
    };
})(jQuery);
