sap.ui.jsview("sales.analysis.Analysis", (function() {
    "use strict";

    var getControllerName = function() {
        return "sales.analysis.Analysis";
    };

    var createContent = function(oController) {

        var iconTabBar = new sap.m.IconTabBar({
            upperCase: true,
            select: function(e) {
                oController.onTabSelect(e);
            },
            items: [
                new sap.m.IconTabFilter({
                    text: "{i18n>new_customer}",
                    key: "newCustomer"
                }), new sap.m.IconTabFilter({
                    text: "{i18n>lost_customer}",
                    key: "lostCustomer",
                    visible: "{permissionModel>/province/update}"
                })
            ]
        });

        var pageContainer = new sap.m.NavContainer(this.createId("pageContainer")).addStyleClass("icon-tab-bar");

        var page = new sap.m.Page({
            showHeader: false,
            enableScrolling: true,
            content: [
                iconTabBar, pageContainer
            ]
        });
        var app = new sap.m.App();
        app.addPage(page);
        return app;
    };

    var view = {
        getControllerName: getControllerName,
        createContent: createContent
    };
    return view;
})());
