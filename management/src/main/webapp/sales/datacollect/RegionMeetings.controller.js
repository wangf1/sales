sap.ui
    .define([
        "sales/basicData/CRUDTableController", "sap/ui/model/json/JSONModel", "sap/ui/model/Filter", "sap/ui/model/FilterOperator", "sales/common/AjaxUtils",
        "sales/common/i18nUtils", "sales/common/DateTimeUtils", "sales/common/ValidateUtils", "sales/common/UIUtils", "sales/common/ArrayUtils", "sap/m/MessageBox"
    ],
        function(CRUDTableController, JSONModel, Filter, FilterOperator, AjaxUtils, i18nUtils, DateTimeUtils, ValidateUtils, UIUtils, ArrayUtils, MessageBox) {
            "use strict";

            var oViewModel = CRUDTableController.prototype.oViewModel;
            var columnNames = [
                "date", "name", "region", "province", "salesPersonFullName", "type", "form", "status", "allKindsOfInputs", "lastModifyAt", "lastModifyBy"
            ]

            function initColumnVisiableModel() {
                CRUDTableController.prototype.initColumnVisiableModel.call(this);
            }
            function onSelectColumnDialogConfirm(selectConfirmEvent) {
                CRUDTableController.prototype.onSelectColumnDialogConfirm.call(this, selectConfirmEvent);
            }
            function onCustomizeTable() {
                CRUDTableController.prototype.onCustomizeTable.call(this);
            }

            var resBundle = i18nUtils.initAndGetResourceBundle();

            var STATUS_PLAN = resBundle.getText("department_meeting_STATUS_PLAN");
            var STATUS_FINISH = resBundle.getText("department_meeting_STATUS_FINISH");
            var STATUS_ADDITONAL = resBundle.getText("department_meeting_STATUS_ADDITONAL");

            var region_meeting_types =
                                       [
                                           resBundle.getText("region_meeting_type_1"), resBundle.getText("region_meeting_type_2"), resBundle.getText("region_meeting_type_3"),
                                           resBundle.getText("region_meeting_type_4"), resBundle.getText("region_meeting_type_5")
                                       ];

            var region_meeting_forms = [
                {
                    name: resBundle.getText("region_meeting_form_1")
                }, {
                    name: resBundle.getText("region_meeting_form_2")
                }, {
                    name: resBundle.getText("region_meeting_form_3")
                }, {
                    name: resBundle.getText("region_meeting_form_4")
                }, {
                    name: resBundle.getText("region_meeting_form_5")
                }
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
// oViewModel.setProperty("/region_meeting_forms", region_meeting_forms);
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
            function refreshDepartmentNames() {
                var promise = AjaxUtils.ajaxCallAsPromise({
                    method: "GET",
                    url: "listAllDepartments",
                    dataType: "json",
                    contentType: "application/json"
                });
                var promiseAfterGetAllDepartNames = promise.then(function(result) {
                    var departNames = result.data;
                    // For now make department name is must input
// departNames.unshift({
// name: ""
// });
                    oViewModel.setProperty("/departmentNames", result.data);
                });
                return promiseAfterGetAllDepartNames;
            }
            function refreshRegionMeetingTypes() {
                var promise = AjaxUtils.ajaxCallAsPromise({
                    method: "GET",
                    url: "getRegionMeetingTypes",
                    dataType: "json",
                    contentType: "application/json"
                });
                var promiseAfterGetStatuses = promise.then(function(result) {
                    var types = result.data;
                    types.forEach(function(type) {
                        if (region_meeting_types.indexOf(type) < 0) {
                            region_meeting_types.push(type);
                        }
                    });
                    // 1. Since all controller extend CRUDTableController, so all views share same oViewModel object, and same viewModelData object.
                    // 2. I find when two Select controls in different view try to bind to same model property(in this case "type"), but the "items"
                    // of the Select bind to different model path, then a serious problem will happen: The Select cannot drop down..
                    // 3. To fix the issue, the "items" bind path must be same, so I must use same property name to store the region meeting types and
                    // speaker types.
                    // 4. This is a very tricky problem, so next time I will not try to create a supper controller for sub controller to extend, never
                    // try to use the "module pattern" to create super controller, it cause lots of potential problems.
                    oViewModel.setProperty("/allTypes", region_meeting_types);
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
                refreshDepartmentNames();
                refreshRegionMeetingTypes();
            }

            function onAdd() {
                // When Add new item, must set all required column visible
                this.initColumnVisiableModel();

                var newAdded = CRUDTableController.prototype.onAdd.call(this);
                newAdded["region"] = oViewModel.getProperty("/regions")[0];
                newAdded["filteredProvinces"] = filterProvinceByRegion(newAdded.region);
                newAdded["province"] = newAdded["filteredProvinces"][0].name;
                setAvailableStatusForMeeting(newAdded);
                newAdded["status"] = newAdded["availableStatuses"][0];
                // Purpose of set a date is the cell enabled status depends on date
                newAdded["date"] = DateTimeUtils.today();
// newAdded["form"] = region_meeting_forms[0];
                newAdded["type"] = region_meeting_types[0];
                // After move all input into one cell, the two-day binding cannot initialize the property, so must explicitly set numberOfPeople to
                // undefined in order to do validate before save
                newAdded["numberOfPeople"] = undefined;
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

            function hasNegativeCost(object) {
                var haveNegativeNumber = false;
                var costs = [
                    "satelliteMeetingCost", "exhibitionCost", "speakerCost", "otherCost", "otherTAndE"
                ];
                costs.forEach(function(cost) {
                    var value = object[cost];
                    if (!ValidateUtils.isEmptyString(value) && !ValidateUtils.isGreaterOrEqualThan0(value)) {
                        haveNegativeNumber = true;
                    }
                });
                if (haveNegativeNumber) {
                    var message = resBundle.getText("cost_invalid");
                    UIUtils.showMessageToast(message);
                }
                return haveNegativeNumber;
            }

            function validatePeopleNumberAndCost(object) {
                if (!ValidateUtils.validateIntegerGreaterThan0(object.numberOfPeople)) {
                    var message = resBundle.getText("number_of_people_invalid");
                    UIUtils.showMessageToast(message);
                    return false;
                }
                if (hasNegativeCost(object)) {
                    return false;
                }
                return true;
            }

            function validateRequiredFieldNotNull(object) {
                var isNumbersValid = validatePeopleNumberAndCost(object);
                if (!isNumbersValid) {
                    return false;
                }
                for ( var key in object) {
                    if (!object.hasOwnProperty(key)) {
                        continue;
                    }
                    if (key === "date" || key === "salesPersonFullName" || key === "allKindsOfInputs" || key === "satelliteMeetingCost" || key === "exhibitionCost" || key === "speakerCost" || key === "otherCost" || key === "otherTAndE" || key === "lastModifyBy" || key === "lastModifyAt") {
                        continue;
                    }
                    var value = object[key];
                    if (!value) {
                        var message = resBundle.getText("before_save_validate_region_meeting_fail");
                        UIUtils.showMessageToast(message);
                        return false;
                    }
                    if (value.trim) {
                        if (value.trim() === "") {
                            var message = resBundle.getText("before_save_validate_region_meeting_fail");
                            UIUtils.showMessageToast(message);
                            return false;
                        }
                    }
                }
                return true;
            }
            function validateBeforeSaveShowMessageToast(object) {
                var isValid = validateRequiredFieldNotNull(object);
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
                    var url = result.data;
                    window.open(url, '_blank');
                });
            }

            function buildReadableDetailMessage(dataItem) {
                var readableMessage = "";
                for ( var key in dataItem) {
                    if (key === "availableStatuses") {
                        continue;
                    }
                    if (!dataItem.hasOwnProperty(key)) {
                        continue;
                    }
                    var propertyReadableName = resBundle.getText(key);
                    var propertyValue = dataItem[key];
                    if (readableMessage === "") {
                        readableMessage = propertyReadableName + "=" + propertyValue;
                    } else {
                        readableMessage = readableMessage + "\n" + propertyReadableName + "=" + propertyValue;
                    }
                }
                return readableMessage;
            }

            var meetingToEditForms;

            function onEditMettingForms(e) {
                meetingToEditForms = e.getSource().getBindingContext().getObject();
                var formSelectDialog = sap.ui.view({
                    type: sap.ui.core.mvc.ViewType.JS,
                    viewName: "sales.datacollect.ItemSelect"
                });
                // Should attach confirm event listener ONLY one time
                formSelectDialog.dialog.attachConfirm(function(selectConfirmEvent) {
                    onEditFormsDialogConfirm(selectConfirmEvent);
                });
                var initialFormNames = ArrayUtils.commaStringToArray(meetingToEditForms.form);
                formSelectDialog.getController().setModelAndInitialSelection(region_meeting_forms, initialFormNames);
                // Must call addDependent otherwise the dialog will cannot access the i18n model
                this.getView().addDependent(formSelectDialog);
                formSelectDialog.dialog.open();
            }

            function onEditFormsDialogConfirm(oEvent) {
                var selectedForms = [];
                var aContexts = oEvent.getParameter("selectedContexts");
                aContexts.forEach(function(oContext) {
                    var form = oContext.getObject();
                    selectedForms.push(form.name);
                });
                var forms = ArrayUtils.stringArrayToCommaString(selectedForms);
                meetingToEditForms.form = forms;
                CRUDTableController.prototype.tableItemDataChanged(meetingToEditForms);
            }

            var controller = CRUDTableController.extend("sales.datacollect.RegionMeetings", {
                columnNames: columnNames,
                onInit: init,
                urlForListAll: "getRegionMeetingsByCurrentUser",
                urlForSaveAll: "saveRegionMeetings",
                urlForDeleteAll: "deleteRegionMeetings",
                urlForExport: "exportRegionMeetings",
                onRefresh: onRefresh,
                onAdd: onAdd,
                setTableModel: setTableModel,
                onRegionChanged: onRegionChanged,
                validateBeforeSaveShowMessageToast: validateBeforeSaveShowMessageToast,
                onExport: onExport,
                initColumnVisiableModel: initColumnVisiableModel,
                onCustomizeTable: onCustomizeTable,
                onSelectColumnDialogConfirm: onSelectColumnDialogConfirm,
                onEditMettingForms: onEditMettingForms
            });
            return controller;
        });
