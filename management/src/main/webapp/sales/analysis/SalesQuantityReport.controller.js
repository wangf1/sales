sap.ui.define([
    "sap/ui/core/mvc/Controller", "sales/common/i18nUtils", "sales/common/DateTimeUtils", "sap/ui/model/json/JSONModel", "sales/common/AjaxUtils", "sales/common/ObjectUtils",
    "sales/common/UIUtils"
], function(Controller, i18nUtils, DateTimeUtils, JSONModel, AjaxUtils, ObjectUtils, UIUtils) {
    "use strict";

    var resBundle = i18nUtils.initAndGetResourceBundle();

    var viewModelData = {
        salesQuantityReport: [],
        regions: [],
        provinces: [],
        allProvinces: [],
        hospitals: [],
        allHospitals: [],
        departments: [],
        products: [],
        startAt: DateTimeUtils.firstDayOfPreviousMonth(),
        endAt: DateTimeUtils.today(),
    };

    var oViewModel = new JSONModel(viewModelData);

    function buildSearchCriteria(thisController) {
        var selectedHospitals = thisController.byId("filterHospital").getSelectedKeys();
        var selectedInstallDepartments = thisController.byId("filterInstallDepartment").getSelectedKeys();
        var selectedOrderDepartments = thisController.byId("filterOrderDepartment").getSelectedKeys();
        var selectedProducts = thisController.byId("filterProduct").getSelectedKeys();

        var productNames = ObjectUtils.getAllOwnPropertyAsArray(selectedProducts);
        var hospitalNames = ObjectUtils.getAllOwnPropertyAsArray(selectedHospitals);
        var locationDepartmentNames = ObjectUtils.getAllOwnPropertyAsArray(selectedInstallDepartments);
        var orderDepartNames = ObjectUtils.getAllOwnPropertyAsArray(selectedOrderDepartments);

        // Increase the endAt by 1 day, in order to search the newest record of user choosen endAt date
        var endAt = DateTimeUtils.nextDay(viewModelData.endAt);
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
        var searchCriteria = {
            "productNames": productNames,
            "hospitalNames": hospitalNames,
            "locationDepartmentNames": locationDepartmentNames,
            "orderDepartNames": orderDepartNames,
            "startAt": viewModelData.startAt,
            "endAt": endAt
        };
        return searchCriteria;
    }

    function getSalesQuantityReport(thicController) {
        var searchCriteria = buildSearchCriteria(thicController);
        var promise = AjaxUtils.ajaxCallAsPromise({
            method: "POST",
            url: "report/getSalesQuantityReport",
            data: JSON.stringify(searchCriteria),
            dataType: "json",
            contentType: "application/json"
        });
        promise.then(function(result) {
            oViewModel.setProperty("/salesQuantityReport", result.data);
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
            oViewModel.setProperty("/allProvinces", result.data);
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
            oViewModel.setProperty("/allHospitals", result.data);
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
            url: "listNormalProducts",
            dataType: "json",
            contentType: "application/json"
        });
        promise.then(function(result) {
            oViewModel.setProperty("/products", result.data);
        });
    }

    function onRefresh() {
        getSalesQuantityReport(this);
        setRegionsModel();
        setProvincesModel();
        setHospitalsModel();
        setDepartmentsModel();
        setProductsModel();
    }

    function initChart(thisController) {
        var dateText = resBundle.getText("date");
        var salesQuantityText = resBundle.getText("sales_quantity_chart_label");
        var departmentMeetingQuantityText = resBundle.getText("department_meeting_quantity");
        var oDataset = new sap.viz.ui5.data.FlattenedDataset({
            dimensions: [
                {
                    axis: 2,
                    name: dateText,
                    value: "{date}"
                }
            ],
            measures: [
                {
                    group: 1,
                    name: salesQuantityText,
                    value: "{salesQuantity}"
                }, {
                    group: 2,
                    name: departmentMeetingQuantityText,
                    value: "{departmentMeetingQuantity}"
                }
            ],
            data: {
                path: "/salesQuantityReport"
            }
        });
        var feedPrimaryValues = new sap.viz.ui5.controls.common.feeds.FeedItem({
            "uid": "primaryValues",
            "type": "Measure",
            "values": [
                salesQuantityText
            ]
        });
        var feedSecondaryValues = new sap.viz.ui5.controls.common.feeds.FeedItem({
            "uid": "secondaryValues",
            "type": "Measure",
            "values": [
                departmentMeetingQuantityText
            ]
        })
        var feedAxisLabels = new sap.viz.ui5.controls.common.feeds.FeedItem({
            "uid": "axisLabels",
            "type": "Dimension",
            "values": [
                dateText
            ]
        });

        // -------- VizFrame ----------------
        var oVizFrame = thisController.byId("viz_frame");
        oVizFrame.setDataset(oDataset);
        oVizFrame.setModel(oViewModel);
        oVizFrame.addFeed(feedPrimaryValues);
        oVizFrame.addFeed(feedSecondaryValues);
        oVizFrame.addFeed(feedAxisLabels);
        oVizFrame.setVizType("dual_line");
        oVizFrame.setVizProperties({
            plotArea: {
                dataLabel: {
                    visible: true
                },
                isFixedDataPointSize: true
            },
            legend: {
                title: {
                    visible: false
                }
            },
            title: {
                visible: true,
                text: resBundle.getText("sales_quantity_chart_title")
            }
        });
    }

    function init() {
        this.getView().setModel(oViewModel);
        this.onRefresh();
        initChart(this);
    }

    function showSearchHelp() {
// var msgStrip = this.byId("msgStrip");
// if (!msgStrip) {
// createMsgStrip(this);
// }
        var message = resBundle.getText("sales_quantity_report_search_help");
        UIUtils.showMessageToast(message);
    }

// function createMsgStrip(thisController) {
// Cannot use message strip, after the fix part height change, the flex part will always flicker, root cause not found
// var fixFlex = thisController.byId("idFixFlex");
// var msgStrip = new sap.m.MessageStrip(thisController.createId("msgStrip"), {
// text: resBundle.getText("sales_quantity_report_search_help"),
// showCloseButton: true,
// showIcon: false,
// type: "Information",
// close: function() {
// this.destroy();
// }
// });
// fixFlex.getFlexContent().shift(msgStrip);
// }

    var controller = Controller.extend("sales.analysis.SalesQuantityReport", {
        onInit: init,
        onRefresh: onRefresh,
        showSearchHelp: showSearchHelp
    });
    return controller;
});
