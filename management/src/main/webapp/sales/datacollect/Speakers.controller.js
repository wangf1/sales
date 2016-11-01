sap.ui.define([
    "sales/basicData/CRUDTableController", "sap/ui/model/json/JSONModel", "sap/ui/model/Filter", "sap/ui/model/FilterOperator", "sales/common/AjaxUtils", "sales/common/i18nUtils",
    "sales/common/DateTimeUtils", "sales/common/ValidateUtils", "sales/common/UIUtils", "sales/common/ArrayUtils", "sap/m/MessageBox"
], function(CRUDTableController, JSONModel, Filter, FilterOperator, AjaxUtils, i18nUtils, DateTimeUtils, ValidateUtils, UIUtils, ArrayUtils, MessageBox) {
    "use strict";

    var oViewModel = CRUDTableController.prototype.oViewModel;

    var resBundle = i18nUtils.initAndGetResourceBundle();

    function init() {
        CRUDTableController.prototype.onInit.call(this);
        var startAt = DateTimeUtils.firstDayOfPreviousMonth();
        var endAt = DateTimeUtils.today();
        oViewModel.setProperty("/startAt", startAt);
        oViewModel.setProperty("/endAt", endAt);
    }

    function filterProvinceByRegion(region) {
        var filteredProvinces = [];
        oViewModel.getProperty("/allProvinces").forEach(function(province) {
            if (province.region === region) {
                filteredProvinces.push(province);
            }
        });
        return filteredProvinces;
    }

    function buildSearchCriteria() {
        var startAt = oViewModel.getProperty("/startAt");
        var endAt = oViewModel.getProperty("/endAt");
        var endAt = DateTimeUtils.nextDay(endAt);
        var searchCriteria = {
            startAt: startAt,
            endAt: endAt
        };
        return searchCriteria;
    }

    function setTableModel() {
        var searchCriteria = buildSearchCriteria();
        var promise = AjaxUtils.ajaxCallAsPromise({
            method: "POST",
            url: this.urlForListAll,
            data: JSON.stringify(searchCriteria),
            dataType: "json",
            contentType: "application/json"
        });
        var promiseAfterSetTableModel = promise.then(function(result) {
            oViewModel.setProperty("/tableData", result.data);
        });
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
            oViewModel.setProperty("/allHospitals", result.data);
        });
        return promiseAfterGetAllHospitals;
    }
    function refreshSpeakerTypes() {
        var promise = AjaxUtils.ajaxCallAsPromise({
            method: "GET",
            url: "getSpeakerTypes",
            dataType: "json",
            contentType: "application/json"
        });
        promise.then(function(result) {
            var types = result.data;
            var fixedTypes = [
                "临床", "检验"
            ];
            fixedTypes.forEach(function(type) {
                if (types.indexOf(type) < 0) {
                    types.push(type);
                }
            });
            oViewModel.setProperty("/speakerTypes", types);
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
        refreshSpeakerTypes();
    }

    function onAdd() {
        var newAdded = CRUDTableController.prototype.onAdd.call(this);
        newAdded["product"] = oViewModel.getProperty("/allProducts")[0].name;
        newAdded["region"] = oViewModel.getProperty("/regions")[0];
        newAdded["filteredProvinces"] = filterProvinceByRegion(newAdded.region);
        newAdded["province"] = newAdded["filteredProvinces"][0].name;
        newAdded["filteredHospitals"] = filterHospitalByProvince(newAdded.province);
        if (newAdded["filteredHospitals"][0]) {
            newAdded["hospital"] = newAdded["filteredHospitals"][0].name;
        } else {
            newAdded["hospital"] = undefined;
        }
        newAdded["type"] = oViewModel.getProperty("/speakerTypes")[0];
        // Purpose of set a date is the cell enabled status depends on date
        newAdded["date"] = DateTimeUtils.today();
        oViewModel.refresh();
        return newAdded;
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

    function validateRequiredFieldNotNull(object, thisController) {
        var isHospitalValid = thisController.validateHospital(object);
        if (!isHospitalValid) {
            return false;
        }
        for ( var key in object) {
            if (!object.hasOwnProperty(key)) {
                continue;
            }
            if (key === "date" || key === "salesPersonFullName") {
                continue;
            }
            var value = object[key];
            if (!value) {
                var message = resBundle.getText("before_save_validate_fail");
                UIUtils.showMessageToast(message);
                return false;
            }
            if (value.trim) {
                if (value.trim() === "") {
                    var message = resBundle.getText("before_save_validate_fail");
                    UIUtils.showMessageToast(message);
                    return false;
                }
            }
        }
        return true;
    }

    function validateBeforeSaveShowMessageToast(object) {
        var isValid = validateRequiredFieldNotNull(object, this);
        return isValid;
    }

    function onExport() {
        var searchCriteria = buildSearchCriteria();
        var promise = AjaxUtils.ajaxCallAsPromise({
            method: "POST",
            url: this.urlForExport,
            data: JSON.stringify(searchCriteria),
            dataType: "text",
            contentType: "application/json"
        });
        promise.then(function(result) {
            var iframe = document.createElement("iframe");
            iframe.setAttribute("src", result.data);
            iframe.setAttribute("style", "display: none");
            document.body.appendChild(iframe);
        });
    }

    function filterHospitalByProvince(province) {
        var filteredHospitals = [];
        oViewModel.getProperty("/allHospitals").forEach(function(hospital) {
            if (hospital.province === province) {
                filteredHospitals.push(hospital);
            }
        });
        return filteredHospitals;
    }

    var controller = CRUDTableController.extend("sales.datacollect.Speakers", {
        columnNames: [
            "date", "region", "province", "salesPersonFullName", "hospital", "type", "speakerName"
        ],
        onInit: init,
        urlForListAll: "getSpeakersByCurrentUser",
        urlForSaveAll: "saveSpeakers",
        urlForDeleteAll: "deleteSpeakers",
        urlForExport: "exportSpeakers",
        onRefresh: onRefresh,
        onAdd: onAdd,
        setTableModel: setTableModel,
        onRegionChanged: onRegionChanged,
        onExport: onExport,
        onProvinceChanged: onProvinceChanged,
        validateBeforeSaveShowMessageToast: validateBeforeSaveShowMessageToast
    });
    return controller;
});
