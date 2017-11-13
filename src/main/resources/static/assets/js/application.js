var contextPath = $("meta[name='ctx']").attr("content");

Logger.useDefaults();



var BaseObj = {
    createNew: function () {
        var baseObj = {};
        baseObj.toString = function () {
            return JSON.stringify(baseObj);
        }
        return baseObj;
    }
}

function activeMenu(menuName) {
    //显示左侧菜单的对应菜单项激活效果
    // $("li.m-menu__item").removeClass("m-menu__item--active");
    $("li[name='" + menuName + "']").addClass("m-menu__item--active");
    //end
}

//获取url中的参数
function getUrlParam(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
    var r = window.location.search.substr(1).match(reg); //匹配目标参数
    if (r != null) return decodeURI(r[2]);
    return null; //返回参数值
}

$().ready(function () {

})
