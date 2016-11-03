jQuery.sap.require("sales.records.SalesRecordsUIHelper");

sap.ui.jsview("sales.records.CreateMultipleSalesRecords", (function() {
    "use strict";

    var getControllerName = function() {
        return "sales.records.CreateMultipleSalesRecords";
    };

    var createTableHeaderToolBar = function(oController) {
        var toolbarContent = [];
        toolbarContent.push(new sap.m.Label({
            text: "{i18n>orderDepartment}"
        }));
        toolbarContent.push(new sap.m.ToolbarSpacer());
        toolbarContent.push(new sap.m.Button({
            text: "{i18n>add}",
            icon: "sap-icon://add",
            press: function(e) {
                oController.onAddOrderDepartment(e);
            }
        }));
        toolbarContent.push(new sap.m.Button({
            text: "{i18n>delete}",
            icon: "sap-icon://delete",
            enabled: "{= ${salesRecordsData>/selectedOrderDepartments}.length>0 }",
            press: function() {
                oController.onDeleteOrderDepartment();
            }
        }));
        var toolBar = new sap.m.Toolbar({
            content: toolbarContent
        });
        return toolBar;
    };

    function createTable(oController) {
        var tableCells = [];
        var tableColumns = [];
        oController.columnNames.forEach(function(columName) {
            tableColumns.push(new sap.m.Column({
                width: "auto",
                hAlign: sap.ui.core.TextAlign.Center,
                header: new sap.m.Text({
                    text: "{i18n>" + columName + "}"
                })
            }));
            if (columName === "orderDepartment") {
                tableCells.push(new sap.m.Select({
                    selectedKey: "{salesRecordsData>" + columName + "}",
                    items: {
                        path: "/departments",
                        template: new sap.ui.core.Item({
                            key: "{name}",
                            text: "{name}"
                        }),
                        templateShareable: true
                    }
                }));
            } else {
                tableCells.push(new sap.m.Input({
                    value: "{salesRecordsData>" + columName + "}"
                }));
            }
        });

        var table = new sap.m.Table({
            id: oController.createId("theTable"),
            mode: sap.m.ListMode.MultiSelect,
            width: "auto",
            selectionChange: function() {
                oController.onTableSelectionChange();
            },
            headerToolbar: createTableHeaderToolBar(oController),
            items: {
                path: "salesRecordsData>/orderDepartments",
                template: new sap.m.ColumnListItem({
                    cells: tableCells
                })
            },
            columns: tableColumns

        });
        table.addStyleClass("sapUiTinyMargin");
        table.addStyleClass("sapUiSizeCompact");
        return table;
    }

    var createForm = function(oController) {
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
            selectedKey: "{salesRecordsData>/region}",
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
            selectedKey: "{salesRecordsData>/province}",
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
        form.addContent(new sap.m.ComboBox(oController.createId("selectHospital"), {
            selectedKey: "{salesRecordsData>/hospital}",
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
            selectedKey: "{salesRecordsData>/installDepartment}",
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
            selectedKey: "{salesRecordsData>/product}",
            items: {
                path: "/products",
                template: new sap.ui.core.Item({
                    key: "{name}",
                    text: "{name}"
                })
            }
        }));

        return form;
    };

    var createContent = function(oController) {
        var vBox = new sap.m.VBox();
        var form = createForm(oController);
        vBox.addItem(form);
        var table = createTable(oController);
        vBox.addItem(table);
        return vBox;
    }

    var view = {
        getControllerName: getControllerName,
        createContent: createContent
    };
    return view;
})());
