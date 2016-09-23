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
        newAdded["level"] = oViewModel.getProperty("/levels")[0];
        return newAdded;
    }

    function init() {
        CRUDTableController.prototype.onInit.call(this);
// refreshAvailableProvinces();
// refreshAvailableLevels();
    }

    function refreshAvailableLevels() {
        var promise = AjaxUtils.ajaxCallAsPromise({
            method: "GET",
            url: "listAllHospitalLevels",
            dataType: "json",
            contentType: "application/json"
        });
        promise.then(function(result) {
            CRUDTableController.prototype.oViewModel.setProperty("/levels", result.data);
        });
    }

    function onRefresh() {
        CRUDTableController.prototype.onRefresh.call(this);
        refreshAvailableProvinces();
        refreshAvailableLevels();
    }

    var controller = CRUDTableController.extend("sales.basicData.Hospital", {
        columnNames: [
            "name", "level", "province"
        ],
        urlForListAll: "getHospitalsByCurrentUser",
        urlForSaveAll: "saveHospitals",
        urlForDeleteAll: "deleteUserHospitalRelationship",
        onInit: init,
        onAdd: onAdd,
        onRefresh: onRefresh
    });
    return controller;
});
