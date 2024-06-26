sap.ui.define([
    "sap/ui/core/mvc/Controller", "sap/ui/model/json/JSONModel", "sap/ui/model/Filter", "sap/ui/model/FilterOperator", "sales/common/AjaxUtils", "sales/common/i18nUtils",
    "sales/common/DateTimeUtils", "sales/common/ValidateUtils", "sales/common/UIUtils"
], function(Controller, JSONModel, Filter, FilterOperator, AjaxUtils, i18nUtils, DateTimeUtils, ValidateUtils, UIUtils) {
    "use strict";

    var resBundle = i18nUtils.initAndGetResourceBundle();

    function init() {
        // initial screen is the salesRecords page
        onTabSelect.bind(this, {
            getParameter: function(key) {
                return "newCustomer";
            }
        })();
    }

    function createPageByTabKey(key, thisController) {
        var viewName;
        switch (key) {
            case "newCustomer":
                viewName = "sales.analysis.NewCustomer"
                break;
            case "lostCustomer":
                viewName = "sales.analysis.LostCustomer"
                break;
            case "salesQuantityReport":
                viewName = "sales.analysis.SalesQuantityReport"
                break;
            default:
                break;
        }
        var view = sap.ui.view({
            type: sap.ui.core.mvc.ViewType.JS,
            viewName: viewName,
            // It's very important to set view height to 100% for two reason:
            // 1. if not set to 100%, the FixFlex container's flex part will always flicker when the window size is smaller than the minFlexSize.
            // 2. if not set to 100%, view content may not be seen.
            height: "100%"
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
        var pages = container.getPages();
        pages.forEach(function(page) {
            // Destroy pages can dramatically improve UI performance
            page.destroy();
        })
        var selectedPage = createPageByTabKey(key, this);
        container.addPage(selectedPage);
        container.to(selectedPage);
        if (selectedPage.getContent()[0].getController().afterShow) {
            selectedPage.getContent()[0].getController().afterShow();
        }
    }

    var controller = Controller.extend("sales.analysis.Analysis", {
        onInit: init,
        onTabSelect: onTabSelect,
    });
    return controller;
});
