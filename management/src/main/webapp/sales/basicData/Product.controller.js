sap.ui.define([
    "sales/basicData/CRUDTableController", "sap/ui/model/json/JSONModel", "sap/ui/model/Filter", "sap/ui/model/FilterOperator", "sales/common/AjaxUtils", "sales/common/i18nUtils",
    "sales/common/DateTimeUtils", "sales/common/ValidateUtils", "sales/common/UIUtils", "sales/common/ArrayUtils", "sap/m/MessageBox"
], function(CRUDTableController, JSONModel, Filter, FilterOperator, AjaxUtils, i18nUtils, DateTimeUtils, ValidateUtils, UIUtils, ArrayUtils, MessageBox) {
    "use strict";

    function refreshAvailableCompanies() {
        var promise = AjaxUtils.ajaxCallAsPromise({
            method: "GET",
            url: "listAllConpanies",
            dataType: "json",
            contentType: "application/json"
        });
        promise.then(function(result) {
            CRUDTableController.prototype.oViewModel.setProperty("/companies", result.data);
        });
    }

    function onRefresh() {
        CRUDTableController.prototype.onRefresh.call(this);
        refreshAvailableCompanies();
    }

    var controller = CRUDTableController.extend("sales.basicData.Product", {
        columnNames: [
            "name", "company"
        ],
        urlForListAll: "listAllProducts",
        urlForSaveAll: "saveProducts",
        urlForDeleteAll: "deleteProducts",
        onRefresh: onRefresh
    });
    return controller;
});
