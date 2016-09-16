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
        startAt: DateTimeUtils.firstDayOfCurrentMonth(),
        endAt: DateTimeUtils.today()
    };

    var oViewModel = new JSONModel(viewModelData);

    function getSalesRecordsPromise() {
        var lastMonthFirstDayString = DateTimeUtils.firstDayOfPreviousMonth();
        var promise = AjaxUtils.ajaxCallAsPromise({
            method: "GET",
            url: "salesRecordsAdvanceSearch?startFrom=" + lastMonthFirstDayString,
            dataType: "json",
            contentType: "application/json"
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
            contentType: "application/json"
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
            contentType: "application/json"
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
            contentType: "application/json"
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
            contentType: "application/json"
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
            contentType: "application/json"
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
        this.getView().setModel(oViewModel);
    };

    function onFilterRecords() {
    }

    function getAllOwnPropertyAsArray(object) {
        var props = [];
        for ( var key in object) {
            if (!object.hasOwnProperty(key)) {
                continue;
            }
            props.push(object[key]);
        }
        return props;
    }
    function doAdvanceSearchSalesRecord(hospitals, installDepartments, orderDepartments, products) {
        /*
        searchCriteriaFormatExample:
        {
            "productNames": [
                "PCT-Q"
            ],
            "hospitalNames": [
                "长征", "长海"
            ],
            "locationDepartmentNames": [
                "ICU"
            ],
            "orderDepartNames": [
                "ICU"
            ],
            "startAt": "2016-08-16",
            "endAt": "2016-09-17"
        }
        */
        var productNames = getAllOwnPropertyAsArray(products);
        var hospitalNames = getAllOwnPropertyAsArray(hospitals);
        var locationDepartmentNames = getAllOwnPropertyAsArray(installDepartments);
        var orderDepartNames = getAllOwnPropertyAsArray(orderDepartments);

        var searchCriteria = {
            "productNames": productNames,
            "hospitalNames": hospitalNames,
            "locationDepartmentNames": locationDepartmentNames,
            "orderDepartNames": orderDepartNames,
            "startAt": viewModelData.startAt,
            "endAt": viewModelData.endAt
        };
        var promise = AjaxUtils.ajaxCallAsPromise({
            method: "POST",
            url: "salesRecordsAdvanceSearch",
            data: JSON.stringify(searchCriteria),
            dataType: "json",
            contentType: "application/json"
        });
        promise.then(function(result) {
            oViewModel.setProperty("/salesRecords", result.data);
        });
    }

    function onAdvanceSearchSalesRecord() {
// var selectedRegins = this.byId("filterRegion").getSelectedKeys();
// var selectedProvinces = this.byId("filterProvince").getSelectedKeys();
        var selectedHospitals = this.byId("filterHospital").getSelectedKeys();
        var selectedInstallDepartments = this.byId("filterInstallDepartment").getSelectedKeys();
        var selectedOrderDepartments = this.byId("filterOrderDepartment").getSelectedKeys();
        var selectedProducts = this.byId("filterProduct").getSelectedKeys();
        doAdvanceSearchSalesRecord(selectedHospitals, selectedInstallDepartments, selectedOrderDepartments, selectedProducts);
    }

    var controller = Controller.extend("sales.records.ListInTable", {
        onInit: init,
        onFilterRecords: onFilterRecords,
        onAdvanceSearchSalesRecord: onAdvanceSearchSalesRecord
    });
    return controller;
});
