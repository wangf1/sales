sap.ui.define([
    "sales/basicData/CRUDTableController", "sap/ui/model/json/JSONModel", "sap/ui/model/Filter", "sap/ui/model/FilterOperator", "sales/common/AjaxUtils", "sales/common/i18nUtils",
    "sales/common/DateTimeUtils", "sales/common/ValidateUtils", "sales/common/UIUtils", "sales/common/ArrayUtils", "sap/m/MessageBox"
], function(CRUDTableController, JSONModel, Filter, FilterOperator, AjaxUtils, i18nUtils, DateTimeUtils, ValidateUtils, UIUtils, ArrayUtils, MessageBox) {
    "use strict";

    var oViewModel = CRUDTableController.prototype.oViewModel;

    function filterProvinceByRegion(region) {
        var filteredProvinces = [];
        oViewModel.getProperty("/allProvinces").forEach(function(province) {
            if (province.region === region) {
                filteredProvinces.push(province);
            }
        });
        return filteredProvinces;
    }

    function setTableModel() {
        var promiseAfterSetTableModel = CRUDTableController.prototype.setTableModel.call(this);
        promiseAfterSetTableModel.then(function() {
            var tableData = oViewModel.getProperty("/tableData");
            tableData.forEach(function(dataItem) {
                dataItem["filteredProvinces"] = filterProvinceByRegion(dataItem.region);
                dataItem["filteredHospitals"] = filterHospitalByProvince(dataItem.province);
            });
            // Must refresh model for each dataItem, otherwise UI will not update
            oViewModel.refresh();
        });
    }

    function refreshProvinces() {
        var promise = AjaxUtils.ajaxCallAsPromise({
            method: "GET",
            url: "getProvincesByCurrentUser",
            dataType: "json",
            contentType: "application/json"
        });
        var promiseAfterGetAllProvinces = promise.then(function(result) {
            oViewModel.setProperty("/allProvinces", result.data);
        });
        return promiseAfterGetAllProvinces;
    }
    function refreshAvailableRegions() {
        var promise = AjaxUtils.ajaxCallAsPromise({
            method: "GET",
            url: "getRegionsByCurrentUser",
            dataType: "json",
            contentType: "application/json"
        });
        promise.then(function(result) {
            oViewModel.setProperty("/regions", result.data);
        });
    }
    function refreshHospitals() {
        var promise = AjaxUtils.ajaxCallAsPromise({
            method: "GET",
            url: "getHospitalsByCurrentUser",
            dataType: "json",
            contentType: "application/json"
        });
        var promiseAfterGetAllHospitals = promise.then(function(result) {
            oViewModel.setProperty("/allHospitalsBelongToCurrentUser", result.data);
        });
        return promiseAfterGetAllHospitals;
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
        var that = this;
        CRUDTableController.prototype.clearSelectAndChangedData.call(this);

        var promiseAfterGetAllHospitals = refreshHospitals();
        promiseAfterGetAllHospitals.then(function() {
            var promiseAfterGetAllProvinces = refreshProvinces();
            promiseAfterGetAllProvinces.then(function() {
                that.setTableModel();
            });
        });
        refreshAvailableRegions();
        refreshProducts();
    }

    function onAdd() {
        var newAdded = CRUDTableController.prototype.onAdd.call(this);
        newAdded["product"] = oViewModel.getProperty("/allProducts")[0].name;
        newAdded["region"] = oViewModel.getProperty("/regions")[0];
        newAdded["filteredProvinces"] = filterProvinceByRegion(newAdded.region);
        newAdded["province"] = newAdded["filteredProvinces"][0];
        newAdded["filteredHospitals"] = filterHospitalByProvince(newAdded.province);
        if (newAdded["filteredHospitals"][0]) {
            newAdded["hospital"] = newAdded["filteredHospitals"][0].name;
        } else {
            newAdded["hospital"] = undefined;
        }
        return newAdded;
    }

    function filterHospitalByProvince(province) {
        var filteredHospitals = [];
        oViewModel.getProperty("/allHospitalsBelongToCurrentUser").forEach(function(hospital) {
            if (hospital.province === province) {
                filteredHospitals.push(hospital);
            }
        });
        return filteredHospitals;
    }

    function onProvinceChanged(e) {
        var dataItem = e.getSource().getBindingContext().getObject()
        dataItem["filteredHospitals"] = filterHospitalByProvince(dataItem.province);
        if (dataItem["filteredHospitals"][0]) {
            dataItem["hospital"] = dataItem["filteredHospitals"][0].name;
        } else {
            dataItem["hospital"] = undefined;
        }
        CRUDTableController.prototype.onCellLiveChange.call(this, e);
    }

    function onRegionChanged(e) {
        var dataItem = e.getSource().getBindingContext().getObject()
        dataItem["filteredProvinces"] = filterProvinceByRegion(dataItem.region);
        if (dataItem["filteredProvinces"][0]) {
            dataItem["province"] = dataItem["filteredProvinces"][0].name;
        } else {
            dataItem["province"] = undefined;
        }
        onProvinceChanged(e);
        CRUDTableController.prototype.onCellLiveChange.call(this, e);
    }

    var controller = CRUDTableController.extend("sales.basicData.ProductPrice", {
        columnNames: [
            "product", "region", "province", "hospital", "price"
        ],
        urlForListAll: "listProductPricesByCurrentUser",
        urlForSaveAll: "saveProductPrices",
        urlForDeleteAll: "deleteProductPrices",
        onRefresh: onRefresh,
        onAdd: onAdd,
        setTableModel: setTableModel,
        onRegionChanged: onRegionChanged,
        onProvinceChanged: onProvinceChanged
    });
    return controller;
});
