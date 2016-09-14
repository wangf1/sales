sap.ui.define([
    "sap/ui/core/mvc/Controller", "sap/ui/model/json/JSONModel", "sap/ui/model/Filter", "sap/ui/model/FilterOperator", "sales/common/AjaxUtils", "sales/common/i18nUtils"
], function(Controller, JSONModel, Filter, FilterOperator, AjaxUtils, i18nUtils) {
    "use strict";

    var viewModelData = {
        salesRecords: {}
    };

    function getSalesRecordsPromise() {
        var promise = AjaxUtils.ajaxCallAsPromise({
            method: "GET",
            url: "getSalesRecordsByCurrentUser",
            dataType: "json",
            contentType: "application/json",
        });
        return promise;
    }

    function setSalesRecordsModel(thisController) {
        var promise = getSalesRecordsPromise();
        promise.then(function(result) {
            viewModelData["salesRecords"] = result.data;
            var oViewModel = new JSONModel(viewModelData);
            thisController.getView().setModel(oViewModel)
        });
    }

    var init = function() {
        i18nUtils.initAndGetResourceBundle();
        setSalesRecordsModel(this);
        var oViewModel = new JSONModel({
            currency: "EUR"
        });
        this.getView().setModel(oViewModel, "view");
    };

    function onFilterRecords() {
    }

    var controller = Controller.extend("sales.records.ListInTable", {
        onInit: init,
        onFilterRecords: onFilterRecords
    });
    return controller;
});
