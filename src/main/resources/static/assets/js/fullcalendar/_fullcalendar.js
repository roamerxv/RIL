var fullCalendar;
$().ready(function () {

    fullCalendar = $('#m_calendar').fullCalendar({
        locale: 'zh-cn',
        events: {
            url: contextPath + "fullcalendar/events",
            type: 'GET',
            beforeSend: function () {

            },
            error: function () {
                alert('there was an error while fetching events!');
            },
        },
        eventRender: function (e, t) {
            t.attr("data-html","true");  //这个标签必须放在下面语句的前面，否则无法产生提示是 html 的效果
            t.hasClass("fc-day-grid-event") ? ( t.data("content", e.description), t.data("placement", "top"), mApp.initPopover(t)) : t.hasClass("fc-time-grid-event") ? t.find(".fc-title").append('<div class="fc-description">' + e.description + "</div>") : 0 !== t.find(".fc-list-item-title").lenght && t.find(".fc-list-item-title").append('<div class="fc-description">' + e.description + "</div>")
        }
        // eventRender 函数可以修改 element 元素，返回一个新的DOM元素插入到日程表中。或者返回false，阻止该日程的插入。
        // eventRender: function (event, element) {
        //     // t.hasClass("fc-day-grid-event") ? (t.data("content", e.description), t.data("placement", "top"), mApp.initPopover(t)) : t.hasClass("fc-time-grid-event") ? t.find(".fc-title").append('<div class="fc-description">' + e.description + "</div>") : 0 !== t.find(".fc-list-item-title").lenght && t.find(".fc-list-item-title").append('<div class="fc-description">' + e.description + "</div>")
        //     $(element).attr("data-toggle","m-tooltip");
        //     $(element).attr("data-placement","left");
        //     $(element).attr("data-original-title",event.description);
        // }
    });
    // var beginDate = fullCalendar.fullCalendar('getView').start ;
    // var endDate = fullCalendar.fullCalendar('getView').end ;
    // Logger.debug(beginDate.format());
    // Logger.debug(endDate.format());

})
