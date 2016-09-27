sap.ui.define([
    "sap/ui/core/mvc/Controller", "sap/ui/model/json/JSONModel", "sap/ui/model/Filter", "sap/ui/model/FilterOperator", "sales/common/AjaxUtils", "sales/common/i18nUtils",
    "sales/common/DateTimeUtils", "sales/common/ValidateUtils", "sales/common/UIUtils"
], function(Controller, JSONModel, Filter, FilterOperator, AjaxUtils, i18nUtils, DateTimeUtils, ValidateUtils, UIUtils) {
    "use strict";

    var viewModelData = {
        currentUser: {}
    };

    var oViewModel = new JSONModel(viewModelData);

    var resBundle = i18nUtils.initAndGetResourceBundle();

    function getCurrentUserName() {
        var promise = AjaxUtils.ajaxCallAsPromise({
            method: "GET",
            url: "getCurrentUser",
        });
        promise.then(function(result) {
            viewModelData.currentUser = result.data;
            oViewModel.refresh();
        });
    }

    function getResourcePermissionForCurrentUser() {
        var promise = AjaxUtils.ajaxCallAsPromise({
            method: "GET",
            url: "getResourcePermissionForCurrentUser",
        });
        promise.then(function(result) {
            var permissionModel = new JSONModel(result.data);
            sap.ui.getCore().setModel(permissionModel, "permissionModel");
        });
    }

    function init() {
        getCurrentUserName();
        getResourcePermissionForCurrentUser();
        this.getView().setModel(oViewModel);
        // initial screen is the salesRecords page
        onTabSelect.bind(this, {
            getParameter: function(key) {
                return "salesRecords";
            }
        })();
    }

    function createPageByTabKey(key, thisController) {
        var viewName;
        switch (key) {
            case "salesRecords":
                viewName = "sales.records.ListInTable"
                break;
            case "province":
                viewName = "sales.basicData.Province"
                break;
            case "hospital":
                viewName = "sales.basicData.Hospital"
                break;
            case "department":
                viewName = "sales.basicData.Department"
                break;
            case "product":
                viewName = "sales.basicData.Product"
                break;
            case "user":
                viewName = "sales.basicData.User"
                break;
            default:
                break;
        }
        var view = sap.ui.view({
            type: sap.ui.core.mvc.ViewType.JS,
            viewName: viewName,
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

    function onLogout() {
        var promise = AjaxUtils.ajaxCallAsPromise({
            method: "POST",
            url: "logout",
        });
        promise.then(function(result) {
            window.location.href = "login";
        });
    }

    var controller = Controller.extend("sales.main", {
        onInit: init,
        onTabSelect: onTabSelect,
        onLogout: onLogout
    });
    return controller;
});
