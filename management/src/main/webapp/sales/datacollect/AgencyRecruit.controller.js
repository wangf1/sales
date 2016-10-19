sap.ui.define([
    "sales/basicData/CRUDTableController", "sap/ui/model/json/JSONModel", "sap/ui/model/Filter", "sap/ui/model/FilterOperator", "sales/common/AjaxUtils", "sales/common/i18nUtils",
    "sales/common/DateTimeUtils", "sales/common/ValidateUtils", "sales/common/UIUtils", "sales/common/ArrayUtils", "sap/m/MessageBox"
], function(CRUDTableController, JSONModel, Filter, FilterOperator, AjaxUtils, i18nUtils, DateTimeUtils, ValidateUtils, UIUtils, ArrayUtils, MessageBox) {
    "use strict";

    var oViewModel = CRUDTableController.prototype.oViewModel;

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
            url: "listAgencyRecruitsByCurrentUser",
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
            });
            // Must refresh model for each dataItem, otherwise UI will not update
            oViewModel.refresh();
        });
    }

    function refreshProvinces() {
        var promise = AjaxUtils.ajaxCallAsPromise({
            method: "GET",
            url: "listAllProvinces",
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
    function refreshAvailableAgencies() {
        var promise = AjaxUtils.ajaxCallAsPromise({
            method: "GET",
            url: "getAgenciesByCurrentUser",
            dataType: "json",
            contentType: "application/json"
        });
        promise.then(function(result) {
            oViewModel.setProperty("/agencies", result.data);
        });
    }
    function refreshAgencyLevels() {
        var promise = AjaxUtils.ajaxCallAsPromise({
            method: "GET",
            url: "getAgencyLevels",
            dataType: "json",
            contentType: "application/json"
        });
        promise.then(function(result) {
            oViewModel.setProperty("/agencyLevels", result.data);
        });
    }

    function onRefresh() {
        var that = this;
        CRUDTableController.prototype.clearSelectAndChangedData.call(this);
        var promiseAfterGetAllProvinces = refreshProvinces();
        promiseAfterGetAllProvinces.then(function() {
            that.setTableModel();
        });
        refreshAvailableRegions();
        refreshProducts();
        refreshAvailableAgencies();
        refreshAgencyLevels();
    }

    function onAdd() {
        var newAdded = CRUDTableController.prototype.onAdd.call(this);
        newAdded["product"] = oViewModel.getProperty("/allProducts")[0].name;
        newAdded["region"] = oViewModel.getProperty("/regions")[0];
        newAdded["filteredProvinces"] = filterProvinceByRegion(newAdded.region);
        newAdded["province"] = newAdded["filteredProvinces"][0];
        oViewModel.refresh();
        return newAdded;
    }

    function onRegionChanged(e) {
        var dataItem = e.getSource().getBindingContext().getObject()
        dataItem["filteredProvinces"] = filterProvinceByRegion(dataItem.region);
        CRUDTableController.prototype.onCellLiveChange.call(this, e);
    }

    function getLevelForAgency(agencyName) {
        var agencies = oViewModel.getProperty("/agencies");
        var level;
        for (var i = 0; i < agencies.length; i++) {
            var agency = agencies[i];
            if (agency.name === agencyName) {
                level = agency.level;
                break;
            }
        }
        return level;
    }

    function onAgencyChanged(e) {
        var dataItem = e.getSource().getBindingContext().getObject()
        var agencyName = dataItem["agency"];
        var level = getLevelForAgency(agencyName);
        dataItem["level"] = level;
        CRUDTableController.prototype.onCellLiveChange.call(this, e);
    }

    function validateEachItemBeforeSave(object) {
        for ( var key in object) {
            if (!object.hasOwnProperty(key)) {
                continue;
            }
            if (key === "date" || key === "salesPerson") {
                continue;
            }
            var value = object[key];
            if (value.trim) {
                if (value.trim() === "") {
                    return false;
                }
            }
        }
        return true;
    }

    function onExport() {
        var searchCriteria = buildSearchCriteria();
        var promise = AjaxUtils.ajaxCallAsPromise({
            method: "POST",
            url: "exportAgencyRecruits",
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

    var controller = CRUDTableController.extend("sales.datacollect.AgencyRecruit", {
        columnNames: [
            "date", "region", "province", "salesPerson", "agency", "product", "level"
        ],
        onInit: init,
        urlForSaveAll: "saveAgencyRecruits",
        urlForDeleteAll: "deleteAgencyRecruits",
        onRefresh: onRefresh,
        onAdd: onAdd,
        setTableModel: setTableModel,
        onRegionChanged: onRegionChanged,
        validateEachItemBeforeSave: validateEachItemBeforeSave,
        onExport: onExport,
        onAgencyChanged: onAgencyChanged
    });
    return controller;
});