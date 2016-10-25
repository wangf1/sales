sap.ui.define([
    "sap/ui/core/mvc/Controller", "sap/ui/model/json/JSONModel", "sap/ui/model/Filter", "sap/ui/model/FilterOperator", "sales/common/AjaxUtils", "sales/common/i18nUtils",
    "sales/common/DateTimeUtils", "sales/common/ValidateUtils", "sales/common/UIUtils"
], function(Controller, JSONModel, Filter, FilterOperator, AjaxUtils, i18nUtils, DateTimeUtils, ValidateUtils, UIUtils) {
    "use strict";

    var resBundle = i18nUtils.initAndGetResourceBundle();

    function init() {
        onTabSelect.bind(this, {
            getParameter: function(key) {
                return "agencyRecruit";
            }
        })();
    }

    function buildViewDataAccordingToTabKey(key) {
        var viewData = {};
        if (key === "agencyTraining") {
            viewData = {
                "usedForAgencyTraining": true,
            };
        }
        return viewData;
    }

    function createPageByTabKey(key, thisController) {
        var viewName;
        switch (key) {
            case "agencyRecruit":
                viewName = "sales.datacollect.AgencyRecruit"
                break;
            case "agencyTraining":
                viewName = "sales.datacollect.AgencyRecruit"
                break;
            case "bids":
                viewName = "sales.datacollect.Bids"
                break;
            case "speakers":
                viewName = "sales.datacollect.Speakers"
                break;
            default:
                break;
        }
        var view = sap.ui.view({
            type: sap.ui.core.mvc.ViewType.JS,
            viewName: viewName,
            viewData: buildViewDataAccordingToTabKey(key)
        });
        var page = new sap.m.Page(thisController.createId(key), {
            showHeader: false,
            enableScrolling: true,
            showNavButton: false,
            content: [
                view
            ],
            customData: [
                new sap.ui.core.CustomData({
                    key: "tabKey",
                    value: key
                })
            ]
        });
        return page;
    }

    function onTabSelect(e) {
        var container = this.byId("pageContainer");
        var key = e.getParameter("selectedKey");
        var selectedPage;
        var pages = container.getPages();
        pages.forEach(function(page) {
            if (page.getCustomData()[0].getValue() === key) {
                selectedPage = page;
            }
        })
        if (!selectedPage) {
            selectedPage = createPageByTabKey(key, this);
            container.addPage(selectedPage);
        }
        container.to(selectedPage);
        if (selectedPage.getContent()[0].getController().afterShow) {
            selectedPage.getContent()[0].getController().afterShow();
        }
    }

    var controller = Controller.extend("sales.datacollect.DataCollect", {
        onInit: init,
        onTabSelect: onTabSelect,
    });
    return controller;
});
