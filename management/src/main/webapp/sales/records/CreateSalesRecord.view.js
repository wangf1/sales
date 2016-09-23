jQuery.sap.require("sales.records.SalesRecordsUIHelper");

sap.ui.jsview("sales.records.CreateSalesRecord", (function() {
    "use strict";

    var getControllerName = function() {
        return "sales.records.CreateSalesRecord";
    };

    var createContent = function(oController) {
        var form = new sap.ui.layout.form.SimpleForm({
            layout: sap.ui.layout.form.SimpleFormLayout.ResponsiveGridLayout,
            labelSpanL: 2,
            labelSpanM: 2,
            emptySpanL: 1,
            emptySpanM: 1,
            editable: true,
        });

        form.addContent(new sap.m.Label({
            text: "{i18n>region}"
        }));
        form.addContent(new sap.m.Select(oController.createId("selectRegion"), {
            selectedKey: "{salesRecord>/region}",
            change: function(e) {
                oController.onRegionChanged();
            },
            items: {
                path: "/regions",
                template: new sap.ui.core.Item({
                    key: "{}",
                    text: "{}"
                })
            }
        }));
        form.addContent(new sap.m.Label({
            text: "{i18n>province}"
        }));
        form.addContent(new sap.m.Select(oController.createId("selectProvince"), {
            selectedKey: "{salesRecord>/province}",
            change: function(e) {
                oController.onProvinceChanged();
            },
            items: {
                path: "/provinces",
                template: new sap.ui.core.Item({
                    key: "{name}",
                    text: "{name}"
                })
            }
        }));
        form.addContent(new sap.m.Label({
            text: "{i18n>hospital}"
        }));
        form.addContent(new sap.m.Select(oController.createId("selectHospital"), {
            selectedKey: "{salesRecord>/hospital}",
            items: {
                path: "/hospitals",
                template: new sap.ui.core.Item({
                    key: "{name}",
                    text: "{name}"
                })
            }
        }));
        form.addContent(new sap.m.Label({
            text: "{i18n>installDepartment}"
        }));
        form.addContent(new sap.m.Select(oController.createId("selectInstallDepartment"), {
            selectedKey: "{salesRecord>/installDepartment}",
            items: {
                path: "/departments",
                template: new sap.ui.core.Item({
                    key: "{name}",
                    text: "{name}"
                })
            }
        }));
        form.addContent(new sap.m.Label({
            text: "{i18n>orderDepartment}"
        }));
        form.addContent(new sap.m.Select(oController.createId("selectOrderDepartment"), {
            selectedKey: "{salesRecord>/orderDepartment}",
            items: {
                path: "/departments",
                template: new sap.ui.core.Item({
                    key: "{name}",
                    text: "{name}"
                })
            }
        }));
        form.addContent(new sap.m.Label({
            text: "{i18n>product}"
        }));
        form.addContent(new sap.m.Select(oController.createId("selectProduct"), {
            selectedKey: "{salesRecord>/product}",
            items: {
                path: "/products",
                template: new sap.ui.core.Item({
                    key: "{name}",
                    text: "{name}"
                })
            }
        }));

        form.addContent(new sap.m.Label({
            text: "{i18n>quantity}"
        }));
        form.addContent(new sap.m.Input({
            value: "{salesRecord>/quantity}"
        }));

        return form;
    };

    var view = {
        getControllerName: getControllerName,
        createContent: createContent
    };
    return view;
})());
