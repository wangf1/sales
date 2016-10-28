sap.ui
    .define([
        "sales/basicData/CRUDTableController", "sap/ui/model/json/JSONModel", "sap/ui/model/Filter", "sap/ui/model/FilterOperator", "sales/common/AjaxUtils",
        "sales/common/i18nUtils", "sales/common/DateTimeUtils", "sales/common/ValidateUtils", "sales/common/UIUtils", "sales/common/ArrayUtils", "sap/m/MessageBox"
    ],
        function(CRUDTableController, JSONModel, Filter, FilterOperator, AjaxUtils, i18nUtils, DateTimeUtils, ValidateUtils, UIUtils, ArrayUtils, MessageBox) {
            "use strict";

            var oViewModel = CRUDTableController.prototype.oViewModel;
            var resBundle = i18nUtils.initAndGetResourceBundle();

            var STATUS_PLAN = resBundle.getText("department_meeting_STATUS_PLAN");
            var STATUS_FINISH = resBundle.getText("department_meeting_STATUS_FINISH");
            var STATUS_ADDITONAL = resBundle.getText("department_meeting_STATUS_ADDITONAL");

            var region_meeting_types =
                                       [
                                           resBundle.getText("region_meeting_type_1"), resBundle.getText("region_meeting_type_2"), resBundle.getText("region_meeting_type_3"),
                                           resBundle.getText("region_meeting_type_4")
                                       ];

            var region_meeting_forms =
                                       [
                                           resBundle.getText("region_meeting_form_1"), resBundle.getText("region_meeting_form_2"), resBundle.getText("region_meeting_form_3"),
                                           resBundle.getText("region_meeting_form_4"), resBundle.getText("region_meeting_form_5")
                                       ];

            var firstDayOfCurrentMonth = oViewModel.getProperty("/firstDayOfCurrentMonth");
            var firstDayOfPreviousMonth = Date.parse(DateTimeUtils.firstDayOfPreviousMonth());
            oViewModel.setProperty("/firstDayOfPreviousMonth", firstDayOfPreviousMonth);

            function init() {
                CRUDTableController.prototype.onInit.call(this);
                var startAt = DateTimeUtils.firstDayOfPreviousMonth();
                var endAt = DateTimeUtils.today();
                oViewModel.setProperty("/startAt", startAt);
                oViewModel.setProperty("/endAt", endAt);
                oViewModel.setProperty("/region_meeting_types", region_meeting_types);
                oViewModel.setProperty("/region_meeting_forms", region_meeting_forms);
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
                var isAdminRole = sap.ui.getCore().getModel("permissionModel").getProperty("/user/create");
                var availableStatuses;
                if (isAdminRole) {
                    availableStatuses = allStatuses;
                } else if (!meeting.date) {
                    // If date is undefined, it is new added, "finish" status should removed
                    availableStatuses = cloneAndRemoveOneFromArray(allStatuses, STATUS_FINISH);
                } else if (meeting.date) {
                    var meetingDate = Date.parse(meeting.date);
                    var isInCurrentMonth = meetingDate >= firstDayOfCurrentMonth;
                    var isInLastMonth = meetingDate < firstDayOfCurrentMonth && meetingDate >= firstDayOfPreviousMonth;
                    if (isInCurrentMonth) {
                        // 本月不能填“已完成”，
                        availableStatuses = cloneAndRemoveOneFromArray(allStatuses, STATUS_FINISH);
                    } else if (isInLastMonth) {
                        // 上月不能填“无预申请增补”
                        var cloned = cloneAndRemoveOneFromArray(allStatuses, STATUS_ADDITONAL);
                        availableStatuses = cloned;
                        if (meeting.status === STATUS_ADDITONAL) {
                            // 上月是“无预申请增补”的不能变成别的
                            availableStatuses = [
                                STATUS_ADDITONAL
                            ];
                        }
                    } else {
                        // 大上月及以前不能有任何修改, will disable the select on UI
                        availableStatuses = allStatuses;
                    }
                }
                meeting["availableStatuses"] = availableStatuses;
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
            function refreshRegionMeetingStatuses() {
                var promise = AjaxUtils.ajaxCallAsPromise({
                    method: "GET",
                    url: "getRegionMeetingStatuses",
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
                var promiseAfterGetAllProvinces = refreshProvinces();
                var promiseAfterGetStatuses = refreshRegionMeetingStatuses();
                Promise.all([
                    promiseAfterGetAllProvinces, promiseAfterGetStatuses
                ]).then(function() {
                    that.setTableModel();
                });
                refreshAvailableRegions();
            }

            function onAdd() {
                var newAdded = CRUDTableController.prototype.onAdd.call(this);
                newAdded["region"] = oViewModel.getProperty("/regions")[0];
                newAdded["filteredProvinces"] = filterProvinceByRegion(newAdded.region);
                newAdded["province"] = newAdded["filteredProvinces"][0].name;
                setAvailableStatusForMeeting(newAdded);
                newAdded["status"] = newAdded["availableStatuses"][0];
                // Purpose of set a date is the cell enabled status depends on date
                newAdded["date"] = DateTimeUtils.today();
                newAdded["form"] = region_meeting_forms[0];
                newAdded["type"] = region_meeting_types[0];
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
                CRUDTableController.prototype.onCellLiveChange.call(this, e);
            }

            function validateRequiredFieldNotNull(object) {
                for ( var key in object) {
                    if (!object.hasOwnProperty(key)) {
                        continue;
                    }
                    if (key === "date" || key === "salesPerson" || key === "satelliteMeetingCost" || key === "exhibitionCost" || key === "speakerCost" || key === "otherCost" || key === "otherTAndE") {
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
            function validateEachItemBeforeSave(object) {
                var isValid = validateRequiredFieldNotNull(object);
                if (!isValid) {
                    var message = resBundle.getText("before_save_validate_region_meeting_fail");
                    UIUtils.showMessageToast(message + "\n\n\n" + JSON.stringify(object));
                }
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

            var controller =
                             CRUDTableController.extend("sales.datacollect.RegionMeetings", {
                                 columnNames: [
                                     "date", "name", "region", "province", "salesPerson", "type", "form", "numberOfPeople", "status", "satelliteMeetingCost", "exhibitionCost",
                                     "speakerCost", "otherCost", "otherTAndE"
                                 ],
                                 onInit: init,
                                 urlForListAll: "getRegionMeetingsByCurrentUser",
                                 urlForSaveAll: "saveRegionMeetings",
                                 urlForDeleteAll: "deleteRegionMeetings",
                                 urlForExport: "exportRegionMeetings",
                                 onRefresh: onRefresh,
                                 onAdd: onAdd,
                                 setTableModel: setTableModel,
                                 onRegionChanged: onRegionChanged,
                                 validateEachItemBeforeSave: validateEachItemBeforeSave,
                                 onExport: onExport,
                             });
            return controller;
        });
