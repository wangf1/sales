sap.ui.define([
    "sales/basicData/CRUDTableController", "sap/ui/model/json/JSONModel", "sap/ui/model/Filter", "sap/ui/model/FilterOperator", "sales/common/AjaxUtils", "sales/common/i18nUtils",
    "sales/common/DateTimeUtils", "sales/common/ValidateUtils", "sales/common/UIUtils", "sales/common/ArrayUtils", "sap/m/MessageBox"
], function(CRUDTableController, JSONModel, Filter, FilterOperator, AjaxUtils, i18nUtils, DateTimeUtils, ValidateUtils, UIUtils, ArrayUtils, MessageBox) {
    "use strict";

    var oViewModel = CRUDTableController.prototype.oViewModel;

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

    function setTableModel() {
        var promiseAfterSetTableModel = CRUDTableController.prototype.setTableModel.call(this);
        promiseAfterSetTableModel.then(function() {
            var hospitals = oViewModel.getProperty("/tableData");
            hospitals.forEach(function(hospital) {
                hospital["filteredProvinces"] = filterProvinceByRegion(hospital.region);
            });
            // Must refresh model for each hospital, otherwise UI will not update
            oViewModel.refresh();
        });
    }

    function onRefresh() {
        var that = this;
        var promiseAfterGetAllProvinces = refreshAvailableProvinces();
        promiseAfterGetAllProvinces.then(function() {
            CRUDTableController.prototype.onRefresh.call(that);
        });

        refreshAvailableRegions();
        refreshAvailableLevels();
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
        urlForListAll: "getHospitalsByCurrentUser",
        urlForSaveAll: "saveHospitals",
        urlForDeleteAll: "deleteHospitals",
        onInit: init,
        onAdd: onAdd,
        onRefresh: onRefresh,
        onRegionChanged: onRegionChanged,
        setTableModel: setTableModel
    });
    return controller;
});
