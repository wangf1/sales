sap.ui.define([
    "sales/basicData/CRUDTableController", "sap/ui/model/json/JSONModel", "sap/ui/model/Filter", "sap/ui/model/FilterOperator", "sales/common/AjaxUtils", "sales/common/i18nUtils",
    "sales/common/DateTimeUtils", "sales/common/ValidateUtils", "sales/common/UIUtils", "sales/common/ArrayUtils", "sap/m/MessageBox"
], function(CRUDTableController, JSONModel, Filter, FilterOperator, AjaxUtils, i18nUtils, DateTimeUtils, ValidateUtils, UIUtils, ArrayUtils, MessageBox) {
    "use strict";

    var oViewModel = CRUDTableController.prototype.oViewModel;

    var STATUS_PLAN = "预申请";
    var STATUS_FINISH = "已完成";
    var STATUS_ADDITONAL = "无预申请增补";
    var firstDayOfCurrentMonth = oViewModel.getProperty("/firstDayOfCurrentMonth");
    var firstDayOfPreviousMonth = Date.parse(DateTimeUtils.firstDayOfPreviousMonth());
    oViewModel.setProperty("/firstDayOfPreviousMonth", firstDayOfPreviousMonth);

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

    function cloneAndRemoveOneFromArray(array, element) {
        var cloned = array.slice(0);
        var index = array.indexOf(element);
        if (index > -1) {
            cloned.splice(index, 1);
        }
        return cloned;
    }

    function setAvailableStatusForMeeting(meeting) {
        // 本月不能填“已完成”，
        // 上月不能填“无预申请增补”，
        // 上月是“无预申请增补”的不能变成别的
        // 大上月及以前不能有任何修改
        var allStatuses = oViewModel.getProperty("/statuses");
        if (!meeting.date) {
            // If date is undefined, it is new added, "finish" status should removed
            var cloned = cloneAndRemoveOneFromArray(allStatuses, STATUS_FINISH);
            meeting["availableStatuses"] = cloned;
        } else if (meeting.date) {
            var meetingDate = Date.parse(meeting.date);
            var isInCurrentMonth = meetingDate >= firstDayOfCurrentMonth;
            var isInLastMonth = meetingDate < firstDayOfCurrentMonth && meetingDate >= firstDayOfPreviousMonth;
            if (isInCurrentMonth) {
                // 本月不能填“已完成”，
                var cloned = cloneAndRemoveOneFromArray(allStatuses, STATUS_FINISH);
                meeting["availableStatuses"] = cloned;
            } else if (isInLastMonth) {
                // 上月不能填“无预申请增补”
                var cloned = cloneAndRemoveOneFromArray(allStatuses, STATUS_ADDITONAL);
                meeting["availableStatuses"] = cloned;
                if (meeting.status === STATUS_ADDITONAL) {
                    // 上月是“无预申请增补”的不能变成别的
                    meeting["availableStatuses"] = [
                        STATUS_ADDITONAL
                    ];
                }
            } else {
                // 大上月及以前不能有任何修改, will disable the select on UI
                meeting["availableStatuses"] = allStatuses;
            }
        }
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
                setAvailableStatusForMeeting(dataItem);
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
    function refreshDepartmentNames() {
        var promise = AjaxUtils.ajaxCallAsPromise({
            method: "GET",
            url: "listAllDepartments",
            dataType: "json",
            contentType: "application/json"
        });
        var promiseAfterGetAllHospitals = promise.then(function(result) {
            oViewModel.setProperty("/departmentNames", result.data);
        });
        return promiseAfterGetAllHospitals;
    }
    function refreshDepartmentMeetingStatuses() {
        var promise = AjaxUtils.ajaxCallAsPromise({
            method: "GET",
            url: "getDepartmentMeetingStatuses",
            dataType: "json",
            contentType: "application/json"
        });
        var promiseAfterGetStatuses = promise.then(function(result) {
            var types = result.data;
            var fixedTypes = [
                STATUS_PLAN, STATUS_FINISH, STATUS_ADDITONAL
            ];
            fixedTypes.forEach(function(type) {
                if (types.indexOf(type) < 0) {
                    types.push(type);
                }
            });
            oViewModel.setProperty("/statuses", types);
        });
        return promiseAfterGetStatuses;
    }

    function onRefresh() {
        var that = this;
        CRUDTableController.prototype.clearSelectAndChangedData.call(this);
        var promiseAfterGetAllHospitals = refreshHospitals();
        var promiseAfterGetAllProvinces = refreshProvinces();
        var promiseAfterGetStatuses = refreshDepartmentMeetingStatuses();
        Promise.all([
            promiseAfterGetAllHospitals, promiseAfterGetAllProvinces, promiseAfterGetStatuses
        ]).then(function() {
            that.setTableModel();
        });
        refreshAvailableRegions();
        refreshProducts();
        refreshDepartmentNames();
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
        newAdded["department"] = oViewModel.getProperty("/departmentNames")[0];
        setAvailableStatusForMeeting(newAdded);
        newAdded["status"] = newAdded["availableStatuses"][0];
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

    var controller = CRUDTableController.extend("sales.datacollect.DepartmentMeetings", {
        columnNames: [
            "date", "region", "province", "salesPerson", "hospital", "department", "product", "purpose", "subject", "planCost", "status", "actualCost"
        ],
        onInit: init,
        urlForListAll: "getDepartmentMeetingsByCurrentUser",
        urlForSaveAll: "saveDepartmentMeetings",
        urlForDeleteAll: "deleteDepartmentMeetings",
        urlForExport: "exportDepartmentMeetings",
        onRefresh: onRefresh,
        onAdd: onAdd,
        setTableModel: setTableModel,
        onRegionChanged: onRegionChanged,
        validateEachItemBeforeSave: validateEachItemBeforeSave,
        onExport: onExport,
        onProvinceChanged: onProvinceChanged,
    });
    return controller;
});
