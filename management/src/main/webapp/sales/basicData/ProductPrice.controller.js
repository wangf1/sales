sap.ui.define([
    "sales/basicData/CRUDTableController", "sap/ui/model/json/JSONModel", "sap/ui/model/Filter", "sap/ui/model/FilterOperator", "sales/common/AjaxUtils", "sales/common/i18nUtils",
    "sales/common/DateTimeUtils", "sales/common/ValidateUtils", "sales/common/UIUtils", "sales/common/ArrayUtils", "sap/m/MessageBox"
], function(CRUDTableController, JSONModel, Filter, FilterOperator, AjaxUtils, i18nUtils, DateTimeUtils, ValidateUtils, UIUtils, ArrayUtils, MessageBox) {
    "use strict";

    var oViewModel = CRUDTableController.prototype.oViewModel;

    function refreshHospitals() {
        var promise = AjaxUtils.ajaxCallAsPromise({
            method: "GET",
            url: "getHospitalsByCurrentUser",
            dataType: "json",
            contentType: "application/json"
        });
        promise.then(function(result) {
            oViewModel.setProperty("/allHospitalsBelongToCurrentUser", result.data);
        });
    }
    function refreshProducts() {
        var promise = AjaxUtils.ajaxCallAsPromise({
            method: "GET",
            url: "listAllProducts",
            dataType: "json",
            contentType: "application/json"
        });
        promise.then(function(result) {
            oViewModel.setProperty("/allProducts", result.data);
        });
    }

    function onRefresh() {
        CRUDTableController.prototype.onRefresh.call(this);
        refreshHospitals();
        refreshProducts();
    }

    function onAdd() {
        var newAdded = CRUDTableController.prototype.onAdd.call(this);
        newAdded["product"] = oViewModel.getProperty("/allProducts")[0].name;
        newAdded["hospital"] = oViewModel.getProperty("/allHospitalsBelongToCurrentUser")[0].name;
        return newAdded;
    }

    var controller = CRUDTableController.extend("sales.basicData.ProductPrice", {
        columnNames: [
            "product", "hospital", "price"
        ],
        urlForListAll: "listProductPricesByCurrentUser",
        urlForSaveAll: "saveProductPrices",
        urlForDeleteAll: "deleteProductPrices",
        onRefresh: onRefresh,
        onAdd: onAdd
    });
    return controller;
});
