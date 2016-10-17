sap.ui.define([
    "sales/analysis/ReadOnlyTableController", "sap/ui/model/json/JSONModel", "sap/ui/model/Filter", "sap/ui/model/FilterOperator", "sales/common/AjaxUtils",
    "sales/common/i18nUtils", "sales/common/DateTimeUtils", "sales/common/ValidateUtils", "sales/common/UIUtils", "sales/common/ArrayUtils", "sap/m/MessageBox"
], function(ReadOnlyTableController, JSONModel, Filter, FilterOperator, AjaxUtils, i18nUtils, DateTimeUtils, ValidateUtils, UIUtils, ArrayUtils, MessageBox) {
    "use strict";

    var oViewModel = ReadOnlyTableController.prototype.oViewModel;

    function setTableModel() {
        var oldMonth = oViewModel.getProperty("/oldMonth");
        var newMonth = oViewModel.getProperty("/newMonth");
        var searchCriteria = {
            startAt: oldMonth,
            endAt: newMonth
        };

        var promise = AjaxUtils.ajaxCallAsPromise({
            method: "POST",
            url: "findLostCustomer",
            data: JSON.stringify(searchCriteria),
            dataType: "json",
            contentType: "application/json"
        });
        promise.then(function(result) {
            oViewModel.setProperty("/tableData", result.data);
        });
    }

    function init() {
        ReadOnlyTableController.prototype.onInit.call(this);

        var oldMonth = DateTimeUtils.firstDayOfPreviousMonth();
        var newMonth = DateTimeUtils.firstDayOfCurrentMonth();
        oViewModel.setProperty("/oldMonth", oldMonth);
        oViewModel.setProperty("/newMonth", newMonth);
    }

    var controller = ReadOnlyTableController.extend("sales.analysis.LostCustomer", {
        columnNames: [
            "hospital", "product"
        ],
        onInit: init,
        setTableModel: setTableModel
    });
    return controller;
});
