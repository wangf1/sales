sap.ui.define([
    "sales/basicData/CRUDTableController", "sap/ui/model/json/JSONModel", "sap/ui/model/Filter", "sap/ui/model/FilterOperator", "sales/common/AjaxUtils", "sales/common/i18nUtils",
    "sales/common/DateTimeUtils", "sales/common/ValidateUtils", "sales/common/UIUtils", "sales/common/ArrayUtils", "sap/m/MessageBox"
], function(CRUDTableController, JSONModel, Filter, FilterOperator, AjaxUtils, i18nUtils, DateTimeUtils, ValidateUtils, UIUtils, ArrayUtils, MessageBox) {
    "use strict";

    var oViewModel = CRUDTableController.prototype.oViewModel;

    function initAccordingToViewUsage(thisController) {
        var viewData = thisController.getView().getViewData();
        if (viewData.usedForAgencyTraining) {
            thisController.urlForListAll = "listAgencyTrainingsByCurrentUser";
            thisController.urlForSaveAll = "saveAgencyTrainings";
            thisController.urlForDeleteAll = "deleteAgencyTrainings";
            thisController.urlForExport = "exportAgencyTrainings";

        } else {
            thisController.urlForListAll = "listAgencyRecruitsByCurrentUser";
            thisController.urlForSaveAll = "saveAgencyRecruits";
            thisController.urlForDeleteAll = "deleteAgencyRecruits";
            thisController.urlForExport = "exportAgencyRecruits";
        }
    }

    function initColumnVisiableModel() {
        CRUDTableController.prototype.initColumnVisiableModel.call(this);
    }
    function onSelectColumnDialogConfirm(selectConfirmEvent) {
        CRUDTableController.prototype.onSelectColumnDialogConfirm.call(this, selectConfirmEvent);
    }
    function onCustomizeTable() {
        CRUDTableController.prototype.onCustomizeTable.call(this);
    }

    function init() {
        CRUDTableController.prototype.onInit.call(this);

        initAccordingToViewUsage(this);

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
            var levels = result.data;
            var fixedLevels = [
                "省级代理", "分销商", "指定医院/区域授权"
            ];
            levels.forEach(function(level) {
                if (fixedLevels.indexOf(level) < 0) {
                    fixedLevels.push(level);
                }
            });
            oViewModel.setProperty("/agencyLevels", fixedLevels);
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
        refreshAvailableAgencies();
        refreshAgencyLevels();
    }

    function onAdd() {
        // When Add new item, must set all required column visible
        this.initColumnVisiableModel();

        var newAdded = CRUDTableController.prototype.onAdd.call(this);
        newAdded["region"] = oViewModel.getProperty("/regions")[0];
        newAdded["filteredProvinces"] = filterProvinceByRegion(newAdded.region);
        newAdded["province"] = newAdded["filteredProvinces"][0];
        newAdded["level"] = oViewModel.getProperty("/agencyLevels")[0];
        var viewData = this.getView().getViewData();
        if (viewData.usedForAgencyTraining) {
            if (oViewModel.getProperty("/agencies")[0]) {
                newAdded["agency"] = oViewModel.getProperty("/agencies")[0].name;
            }
        }
        // Purpose of set a date is the cell enabled status depends on date
        newAdded["date"] = DateTimeUtils.today();
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
        CRUDTableController.prototype.onCellLiveChange.call(this, e);
        var dataItem = e.getSource().getBindingContext().getObject();
        var agencyName = dataItem["agency"];
        var level = getLevelForAgency(agencyName);
        if (level) {
            dataItem["level"] = level;
        }
        oViewModel.refresh();
    }

    function validateEachItemBeforeSave(object) {
        for ( var key in object) {
            if (!object.hasOwnProperty(key)) {
                continue;
            }
            if (key === "date" || key === "salesPersonFullName" || key === "lastModifyBy" || key === "lastModifyAt") {
                continue;
            }
            var value = object[key];
            if (!value) {
                return false;
            }
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

    var agencyToEditProducts;

    function onEditProducts(e) {
        agencyToEditProducts = e.getSource().getBindingContext().getObject();
        var productSelectDialog = sap.ui.view({
            type: sap.ui.core.mvc.ViewType.JS,
            viewName: "sales.datacollect.ProductSelect"
        });
        // Should attach confirm event listener ONLY one time
        productSelectDialog.dialog.attachConfirm(function(selectConfirmEvent) {
            onEditProductsDialogConfirm(selectConfirmEvent);
        });
        productSelectDialog.getController().initSelection(agencyToEditProducts.products);
        // Must call addDependent otherwise the dialog will cannot access the i18n model
        this.getView().addDependent(productSelectDialog);
        productSelectDialog.dialog.open();
    }

    function onEditProductsDialogConfirm(oEvent) {
        var selectedProducts = [];
        var aContexts = oEvent.getParameter("selectedContexts");
        aContexts.forEach(function(oContext) {
            var product = oContext.getObject();
            selectedProducts.push(product.name);
        });
        agencyToEditProducts.products = selectedProducts;
        CRUDTableController.prototype.tableItemDataChanged(agencyToEditProducts);
    }

    var controller = CRUDTableController.extend("sales.datacollect.AgencyRecruit", {
        columnNames: [],
        onInit: init,
        urlForListAll: "",
        urlForSaveAll: "",
        urlForDeleteAll: "",
        urlForExport: "",
        onRefresh: onRefresh,
        onAdd: onAdd,
        setTableModel: setTableModel,
        onRegionChanged: onRegionChanged,
        validateEachItemBeforeSave: validateEachItemBeforeSave,
        onExport: onExport,
        onAgencyChanged: onAgencyChanged,
        onEditProducts: onEditProducts,
        initColumnVisiableModel: initColumnVisiableModel,
        onCustomizeTable: onCustomizeTable,
        onSelectColumnDialogConfirm: onSelectColumnDialogConfirm
    });
    return controller;
});
