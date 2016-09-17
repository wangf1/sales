sap.ui.jsview("sales.main", (function() {
    "use strict";

    var getControllerName = function() {
        return "sales.main";
    };

    var createContent = function(oController) {

        var iconTabBar = new sap.m.IconTabBar({
            upperCase: true,
            select: function(e) {
                oController.onTabSelect(e);
            },
            items: [
                new sap.m.IconTabFilter(this.createId("tb_salesRecords"), {
                    text: "{i18n>salesRecords}",
                    key: "salesRecords"
                }), new sap.m.IconTabFilter(this.createId("tb_province"), {
                    text: "{i18n>province}",
                    key: "province"
                }), new sap.m.IconTabFilter(this.createId("tb_hospital"), {
                    text: "{i18n>hospital}",
                    key: "hospital"
                }), new sap.m.IconTabFilter(this.createId("tb_department"), {
                    text: "{i18n>department}",
                    key: "department"
                }), new sap.m.IconTabFilter(this.createId("tb_product"), {
                    text: "{i18n>product}",
                    key: "product"
                }), new sap.m.IconTabFilter(this.createId("tb_user"), {
                    text: "{i18n>user}",
                    key: "user"
                })
            ]
        });

        var pageContainer = new sap.m.NavContainer(this.createId("pageContainer")).addStyleClass("icon-tab-bar");

        var page = new sap.m.Page({
            showHeader: false,
            enableScrolling: true,
            subHeader: new sap.m.Toolbar({
                content: [
                    new sap.m.Text({
                        text: "{i18n>app_header}"
                    })
                ]
            }),
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
