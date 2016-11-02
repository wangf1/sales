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
                    key: "province",
                    visible: "{permissionModel>/province/update}"
                }), new sap.m.IconTabFilter(this.createId("tb_hospital"), {
                    text: "{i18n>hospital}",
                    key: "hospital",
                    visible: "{permissionModel>/hospital/update}"
                }), new sap.m.IconTabFilter(this.createId("tb_department"), {
                    text: "{i18n>department}",
                    key: "department",
                    visible: "{permissionModel>/departmentName/update}"
                }), new sap.m.IconTabFilter(this.createId("tb_product"), {
                    text: "{i18n>product}",
                    key: "product",
                    visible: "{permissionModel>/product/update}"
                }), new sap.m.IconTabFilter(this.createId("tb_product_price"), {
                    text: "{i18n>productPrice}",
                    key: "productPrice",
                    visible: "{permissionModel>/productPrice/update}"
                }), new sap.m.IconTabFilter(this.createId("tb_user"), {
                    text: "{i18n>user}",
                    key: "user",
                    visible: "{permissionModel>/user/update}"
                }), new sap.m.IconTabFilter(this.createId("tb_analysis"), {
                    text: "{i18n>analysis}",
                    key: "analysis",
// visible: "{permissionModel>/user/create}"
                }), new sap.m.IconTabFilter(this.createId("tb_dataCollect"), {
                    text: "{i18n>data_collect_tab}",
                    key: "dataCollect",
                    visible: "{permissionModel>/user/update}"
                })
            ]
        });
        iconTabBar.addStyleClass("emptyIconTabFilter");

        var pageContainer = new sap.m.NavContainer(this.createId("pageContainer")).addStyleClass("navigater-container");

        var page = new sap.m.Page({
            showHeader: false,
            enableScrolling: true,
            subHeader: new sap.m.Toolbar({
                content: [
                    new sap.m.Text({
                        text: "{i18n>app_header}"
                    }), new sap.m.ToolbarSpacer(), new sap.m.Text({
                        text: "{i18n>hi} {/currentUser/fullNameWithLoginName}"
                    }), new sap.m.Button({
                        text: '{i18n>log_out}',
                        icon: 'sap-icon://log',
                        press: function(e) {
                            oController.onLogout();
                        }
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
