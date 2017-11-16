var fullCalendar;
$().ready(function () {

    fullCalendar = $('#m_calendar').fullCalendar({
        locale: 'zh-cn',
        header: {
            left: 'prev,next today',
            center: 'title',
            right: 'month,agendaWeek,agendaDay,listWeek'
        },
        selectable: false, //Allows a user to highlight multiple days or timeslots by clicking and dragging.
        selectHelper: false, //Whether to draw a "placeholder" event while the user is dragging.
        editable: false,  // 不能拖拉
        eventLimit: true, // allow "more" link when too many events
        events: {
            url: contextPath + "fullcalendar/events",
            type: 'GET',
            beforeSend: function () {

            },
            error: function (jqXHR) {
                Logger.debug(jqXHR);
                showMessage("danger", "错误", jQuery.parseJSON(jqXHR.responseJSON.message).message);
            },
        },
        eventRender: function (e, t) {
            Logger.debug(t);
            t.attr("data-html","true");  //这个标签必须放在下面语句的前面，否则无法产生提示是 html 的效果
            t.hasClass("fc-day-grid-event") ? ( t.data("content", e.description), t.data("placement", "top"), mApp.initPopover(t)) : t.hasClass("fc-time-grid-event") ? t.find(".fc-title").append('<div class="fc-description">' + e.description + "</div>") : 0 !== t.find(".fc-list-item-title").lenght && t.find(".fc-list-item-title").append('<div class="fc-description">' + e.description + "</div>")
        }

    });
    // var beginDate = fullCalendar.fullCalendar('getView').start ;
    // var endDate = fullCalendar.fullCalendar('getView').end ;
    // Logger.debug(beginDate.format());
    // Logger.debug(endDate.format());

})
