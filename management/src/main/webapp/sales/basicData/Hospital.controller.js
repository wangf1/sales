sap.ui.define([
    "sales/basicData/CRUDTableController", "sap/ui/model/json/JSONModel", "sap/ui/model/Filter", "sap/ui/model/FilterOperator", "sales/common/AjaxUtils", "sales/common/i18nUtils",
    "sales/common/DateTimeUtils", "sales/common/ValidateUtils", "sales/common/UIUtils", "sales/common/ArrayUtils", "sap/m/MessageBox"
], function(CRUDTableController, JSONModel, Filter, FilterOperator, AjaxUtils, i18nUtils, DateTimeUtils, ValidateUtils, UIUtils, ArrayUtils, MessageBox) {
    "use strict";

    var oViewModel = CRUDTableController.prototype.oViewModel;

    function refreshAvailableProvinces() {
        var promise = AjaxUtils.ajaxCallAsPromise({
            method: "GET",
            url: "listAllProvinces",
            dataType: "json",
            contentType: "application/json"
        });
        promise.then(function(result) {
            CRUDTableController.prototype.oViewModel.setProperty("/provinces", result.data);
        });
    }

    function onAdd() {
        var newAdded = CRUDTableController.prototype.onAdd.call(this);
        newAdded["province"] = oViewModel.getProperty("/provinces")[0];
        return newAdded;
    }

    function init() {
        CRUDTableController.prototype.onInit.call(this);
        refreshAvailableProvinces();
    }

    var controller = CRUDTableController.extend("sales.basicData.Hospital", {
        onInit: init,
        onAdd: onAdd
    });
    return controller;
});