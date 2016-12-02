sap.ui.define([
    "sales/basicData/CRUDTableController", "sap/ui/model/json/JSONModel", "sap/ui/model/Filter", "sap/ui/model/FilterOperator", "sales/common/AjaxUtils", "sales/common/i18nUtils",
    "sales/common/DateTimeUtils", "sales/common/ValidateUtils", "sales/common/UIUtils", "sales/common/ArrayUtils", "sap/m/MessageBox", "sales/common/Constants"
], function(CRUDTableController, JSONModel, Filter, FilterOperator, AjaxUtils, i18nUtils, DateTimeUtils, ValidateUtils, UIUtils, ArrayUtils, MessageBox, Constants) {
    "use strict";

    var oViewModel = CRUDTableController.prototype.oViewModel;
    function refreshAvailableCompanies() {
        var promise = AjaxUtils.ajaxCallAsPromise({
            method: "GET",
            url: "listAllConpanies",
            dataType: "json",
            contentType: "application/json"
        });
        promise.then(function(result) {
            oViewModel.setProperty("/companies", result.data);
        });
    }
    function refreshUsageType() {
        var promise = AjaxUtils.ajaxCallAsPromise({
            method: "GET",
            url: "listAllProductUsageTypes",
            dataType: "json",
            contentType: "application/json"
        });
        promise.then(function(result) {
            var levels = result.data;
            var fixedLevels = [
                Constants.PRODUCT_USAGE_TYPE_NORMAL, Constants.PRODUCT_USAGE_TYPE_NORMAL_AND_ForDepartmentMeeting, Constants.PRODUCT_USAGE_TYPE_ONLY_ForDepartmentMeeting
            ];
            levels.forEach(function(level) {
                if (fixedLevels.indexOf(level) < 0) {
                    fixedLevels.push(level);
                }
            });
            oViewModel.setProperty("/usageTypes", fixedLevels);
        });
    }

    function onRefresh() {
        CRUDTableController.prototype.onRefresh.call(this);
        refreshAvailableCompanies();
        refreshUsageType();
    }

    var controller = CRUDTableController.extend("sales.basicData.Product", {
        columnNames: [
            "name", "company", "usageType"
        ],
        urlForListAll: "listAllProducts",
        urlForSaveAll: "saveProducts",
        urlForDeleteAll: "deleteProducts",
        onRefresh: onRefresh
    });
    return controller;
});
