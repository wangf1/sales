sap.ui.define([
    "sap/ui/core/mvc/Controller", "sap/ui/model/json/JSONModel", "sap/ui/model/Filter", "sap/ui/model/FilterOperator", "sales/common/AjaxUtils", "sales/common/i18nUtils", "sales/common/DateTimeUtils"
], function(Controller, JSONModel, Filter, FilterOperator, AjaxUtils, i18nUtils, DateTimeUtils) {
    "use strict";

    var viewModelData = {
        salesRecords: [],
        regions: [],
        provinces: [],
        hospitals: [],
        departments: [],
        products: [],
    };

    var oViewModel = new JSONModel(viewModelData);

    function getSalesRecordsPromise() {
        var lastMonthFirstDayString = DateTimeUtils.firstDayOfPreviousMonth()
        var promise = AjaxUtils.ajaxCallAsPromise({
            method: "GET",
            url: "salesRecordsAdvanceSearch?startFrom=" + lastMonthFirstDayString,
            dataType: "json",
            contentType: "application/json",
        });
        return promise;
    }

    function setSalesRecordsModel() {
        var promise = getSalesRecordsPromise();
        promise.then(function(result) {
            oViewModel.setProperty("/salesRecords", result.data);
        });
    }

    function setRegionsModel() {
        var promise = AjaxUtils.ajaxCallAsPromise({
            method: "GET",
            url: "getRegionsByCurrentUser",
            dataType: "json",
            contentType: "application/json",
        });
        promise.then(function(result) {
            oViewModel.setProperty("/regions", result.data);
        });
    }
    function setProvincesModel() {
        var promise = AjaxUtils.ajaxCallAsPromise({
            method: "GET",
            url: "getProvincesByCurrentUser",
            dataType: "json",
            contentType: "application/json",
        });
        promise.then(function(result) {
            oViewModel.setProperty("/provinces", result.data);
        });
    }
    function setHospitalsModel() {
        var promise = AjaxUtils.ajaxCallAsPromise({
            method: "GET",
            url: "getHospitalsByCurrentUser",
            dataType: "json",
            contentType: "application/json",
        });
        promise.then(function(result) {
            oViewModel.setProperty("/hospitals", result.data);
        });
    }
    function setDepartmentsModel() {
        var promise = AjaxUtils.ajaxCallAsPromise({
            method: "GET",
            url: "listAllDepartments",
            dataType: "json",
            contentType: "application/json",
        });
        promise.then(function(result) {
            oViewModel.setProperty("/departments", result.data);
        });
    }
    function setProductsModel() {
        var promise = AjaxUtils.ajaxCallAsPromise({
            method: "GET",
            url: "listAllProducts",
            dataType: "json",
            contentType: "application/json",
        });
        promise.then(function(result) {
            oViewModel.setProperty("/products", result.data);
        });
    }

    var init = function() {
        // set i18n model
        i18nUtils.initAndGetResourceBundle();
        // populate view model date
        setSalesRecordsModel(this);
        setRegionsModel();
        setProvincesModel();
        setHospitalsModel();
        setDepartmentsModel();
        setProductsModel();
        // set model into view
        this.getView().setModel(oViewModel)
    };

    function onFilterRecords() {
    }

    var controller = Controller.extend("sales.records.ListInTable", {
        onInit: init,
        onFilterRecords: onFilterRecords
    });
    return controller;
});
