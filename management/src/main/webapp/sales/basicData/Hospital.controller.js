sap.ui.define([
    "sales/basicData/CRUDTableController", "sap/ui/model/json/JSONModel", "sap/ui/model/Filter", "sap/ui/model/FilterOperator", "sales/common/AjaxUtils", "sales/common/i18nUtils",
    "sales/common/DateTimeUtils", "sales/common/ValidateUtils", "sales/common/UIUtils", "sales/common/ArrayUtils", "sap/m/MessageBox", "sales/common/ObjectUtils"
], function(CRUDTableController, JSONModel, Filter, FilterOperator, AjaxUtils, i18nUtils, DateTimeUtils, ValidateUtils, UIUtils, ArrayUtils, MessageBox, ObjectUtils) {
    "use strict";

    var oViewModel = CRUDTableController.prototype.oViewModel;

    var resBundle = i18nUtils.initAndGetResourceBundle();

    function doSearchHospitals(thisController) {
        var searchCriteria = buildSearchCriteria(thisController);
        if (searchCriteria.length === 0) {
            var message = resBundle.getText("search_hospital_no_province");
            UIUtils.showMessageToast(message);
            return;
        }
        var promise = AjaxUtils.ajaxCallAsPromise({
            method: "POST",
            url: thisController.urlForListAll,
            data: JSON.stringify(searchCriteria),
            dataType: "json",
            contentType: "application/json"
        });
        var promiseAfterSetTableModel = promise.then(function(result) {
            oViewModel.setProperty("/tableData", result.data);
        });
        promiseAfterSetTableModel.then(function() {
            var hospitals = oViewModel.getProperty("/tableData");
            hospitals.forEach(function(hospital) {
                hospital["filteredProvinces"] = filterProvinceByRegion(hospital.region);
            });
            // Must refresh model for each hospital, otherwise UI will not update
            oViewModel.refresh();
        });
    }

    function onSearchHospitals() {
        var that = this;
        // Must get all provinces before search hospitals, since we should set "filteredProvinces" for each hospital
        var promiseAfterGetAllProvinces = refreshAvailableProvinces();
        promiseAfterGetAllProvinces.then(function() {
            doSearchHospitals(that);
        });
        // Refresh other data
        this.clearSelectAndChangedData();
        refreshAvailableRegions();
        refreshAvailableLevels();

    }

    function buildSearchCriteria(thisController) {
        var selectedProvinces = thisController.byId("filterProvince").getSelectedKeys();
        var provinces = ObjectUtils.getAllOwnPropertyAsArray(selectedProvinces);
        return provinces;
    }

    function refreshAvailableProvinces() {
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

    function onAdd() {
        var newAdded = CRUDTableController.prototype.onAdd.call(this);
        newAdded["region"] = oViewModel.getProperty("/regions")[0];
        newAdded["filteredProvinces"] = filterProvinceByRegion(newAdded.region);
        newAdded["province"] = newAdded["filteredProvinces"][0];
        newAdded["level"] = oViewModel.getProperty("/levels")[0].name;
        oViewModel.refresh();
        return newAdded;
    }

    function init() {
        CRUDTableController.prototype.onInit.call(this);
    }

    function refreshAvailableLevels() {
        var promise = AjaxUtils.ajaxCallAsPromise({
            method: "GET",
            url: "listAllHospitalLevels",
            dataType: "json",
            contentType: "application/json"
        });
        promise.then(function(result) {
            var levels = result.data;
            var fixedLevels = [
                {
                    name: "三级"
                }, {
                    name: "二级"
                }, {
                    name: "一级"
                }
            ];
            levels.forEach(function(level) {
                var alreadyContains = false;
                for (var i = 0; i < fixedLevels.length; i++) {
                    if (fixedLevels[i].name === level.name) {
                        alreadyContains = true;
                    }
                }
                if (!alreadyContains) {
                    fixedLevels.push(level);
                }
            });
            oViewModel.setProperty("/levels", fixedLevels);
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

    function onRefresh() {
        // this.onSearchHospitals() may not fetch data if filter value not set. So must clear table data, the data is other pages data when jump from
        // other page to this page. And
        oViewModel.setProperty("/tableData", []);
        this.onSearchHospitals();
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

    function onRegionChanged(e) {
        var hospital = e.getSource().getBindingContext().getObject()
        hospital["filteredProvinces"] = filterProvinceByRegion(hospital.region);
        CRUDTableController.prototype.onCellLiveChange.call(this, e);
    }

    var controller = CRUDTableController.extend("sales.basicData.Hospital", {
        columnNames: [
            "name", "level", "region", "province"
        ],
        urlForListAll: "searchHospitalsByProvinces",
        urlForSaveAll: "saveHospitals",
        urlForDeleteAll: "deleteHospitals",
        onInit: init,
        onAdd: onAdd,
        onRefresh: onRefresh,
        onRegionChanged: onRegionChanged,
        onSearchHospitals: onSearchHospitals
    });
    return controller;
});
