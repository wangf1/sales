jQuery.sap.declare("sales.common.DateTimeUtils");

sales.common.DateTimeUtils = (function() {
    "use strict";
    /**
     * Return the string of last month first day, format is yyyy-MM-dd
     * For example, now is October, then the return value is "2016-09-01"
     */
    function firstDayOfPreviousMonth() {
        var current = new Date();
        // Must firstly set date to 1, then set month to last month.
        // Otherwise when 30 March, the current month -1 will still in March.
        // It should be JavaScript Date behavior.
        current.setDate(1);
        current.setMonth(current.getMonth() - 1);
        var isoString = yyyyMMdd(current);
        return isoString;
    }

    function yyyyMMdd(date) {
        var yyyy_MM_dd = date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate();
        return yyyy_MM_dd;
    }

    function firstDayOfCurrentMonth() {
        var current = new Date();
        current.setDate(1);
        var isoString = yyyyMMdd(current);
        return isoString;
    }

    function today() {
        var current = new Date();
        var dateString = yyyyMMdd(current);
        return dateString;
    }

    function nextDay(dateString) {
        var date = new Date(dateString);
        date.setDate(date.getDate() + 1);
        var nextDay = yyyyMMdd(date);
        return nextDay;
    }

    var toExpose = {
        firstDayOfPreviousMonth: firstDayOfPreviousMonth,
        firstDayOfCurrentMonth: firstDayOfCurrentMonth,
        today: today,
        yyyyMMdd: yyyyMMdd,
        nextDay: nextDay
    };
    return toExpose;
})();
