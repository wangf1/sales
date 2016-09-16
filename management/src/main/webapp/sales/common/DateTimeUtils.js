jQuery.sap.declare("sales.common.DateTimeUtils");

sales.common.DateTimeUtils = (function() {
    "use strict";
    /**
     * Return the string of last month first day, format is yyyy-MM-dd
     * For example, now is October, then the return value is "2016-09-01"
     */
    function firstDayOfPreviousMonth() {
        var current = new Date();
        var lastMonth = current.getMonth();// js month is 0 based, so we get 1 based lastMonth
        var lastMonthString = (lastMonth < 10 ? "0" : "") + lastMonth;
        var dateString = current.getFullYear() + "-" + lastMonthString + "-" + "01";
        return dateString;
    }

    function yyyyMMdd(date) {
        var isoString = date.toISOString();
        var dateString = isoString.substr(0, 10);
        return dateString;
    }

    function firstDayOfCurrentMonth() {
        var current = new Date();
        current.setDate(1);
        var isoString = yyyyMMdd(current);
        return isoString;
    }

    function today() {
        var current = new Date();
        var isoString = current.toISOString();
        var dateString = isoString.substr(0, 10);
        return dateString;
    }

    var toExpose = {
        firstDayOfPreviousMonth: firstDayOfPreviousMonth,
        firstDayOfCurrentMonth: firstDayOfCurrentMonth,
        today: today,
        yyyyMMdd: yyyyMMdd
    };
    return toExpose;
})();
