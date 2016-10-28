sap.ui.jsview("sales.basicData.Province", (function() {
    "use strict";

    jQuery.sap.require("sales.common.StringArrayAsCommaStringType");

    var getControllerName = function() {
        return "sales.basicData.Province";
    };

    var createTableHeaderToolBar = function(oController) {
        var toolbarContent = [];
        toolbarContent.push(new sap.m.Title({
            text: "{i18n>province}"
        }));
        toolbarContent.push(new sap.m.SearchField({
            placeholder: "{i18n>quickSearchPlaceHolder}",
            width: "50%",
            search: function(e) {
                oController.onQuickFilter(e);
            }
        }));
        toolbarContent.push(new sap.m.ToolbarSpacer());
        toolbarContent.push(new sap.m.Button({
            text: "{i18n>refresh}",
            icon: "sap-icon://refresh",
            press: function(e) {
                oController.onRefresh(e);
            }
        }));
        toolbarContent.push(new sap.m.Button({
            text: "{i18n>add}",
            icon: "sap-icon://add",
            press: function(e) {
                oController.onAdd(e);
            }
        }));
        toolbarContent.push(new sap.m.Button({
            text: "{i18n>delete}",
            icon: "sap-icon://delete",
            enabled: "{= ${/selectedRecords}.length>0 }",
            press: function() {
                oController.onDelete();
            }
        }));
        toolbarContent.push(new sap.m.Button({
            text: "{i18n>save}",
            icon: "sap-icon://save",
            enabled: "{= ${/inlineChangedRecords}.length>0 || ${/newAddedRecords}.length>0}",
            press: function() {
                oController.onSaveAll();
            }
        }));

        var toolBar = new sap.m.Toolbar({
            content: toolbarContent
        });
        return toolBar;
    };

    function createSalesPersonsCell(oController, columName) {
        var hBox = new sap.m.HBox();

        var input = new sap.m.Input({
            value: {
                path: columName,
                type: new sales.common.StringArrayAsCommaStringType()
            },
            enabled: false,
            change: function(e) {
                oController.onCellLiveChange(e);
            }
        });
        hBox.addItem(input);

        var button = new sap.m.Button({
            icon: "sap-icon://edit",
            press: function(e) {
                oController.onEditSalesPersons(e);
            }
        });
        hBox.addItem(button);

        return hBox;
    }

    function createTable(oController) {
        var tableCells = [];
        var tableColumns = [];
        oController.columnNames.forEach(function(columName) {
            tableColumns.push(new sap.m.Column({
                width: "30%",
                hAlign: sap.ui.core.TextAlign.Center,
                header: new sap.m.Button({
                    text: "{i18n>" + columName + "}",
                    press: function(e) {
                        oController.sortTable(e);
                    },
                    customData: [
                        new sap.ui.core.CustomData({
                            key: "column",
                            value: columName
                        }), new sap.ui.core.CustomData({
                            key: "descending",
                            value: true
                        })
                    ]
                })
            }));
            if (columName === "region") {
                tableCells.push(new sap.m.ComboBox({
                    selectedKey: "{" + columName + "}",
                    change: function(e) {
                        oController.onCellLiveChange(e);
                    },
                    items: {
                        path: "/regions",
                        template: new sap.ui.core.Item({
                            key: "{}",
                            text: "{}"
                        }),
                        templateShareable: true
                    }
                }));
            } else if (columName === "salesPersons") {
                var hBox = createSalesPersonsCell(oController, columName);
                tableCells.push(hBox);
            } else {
                tableCells.push(new sap.m.Input({
                    value: "{" + columName + "}",
                    liveChange: function(e) {
                        oController.onCellLiveChange(e);
                    }
                }).addStyleClass("input-in-table-cell"));
            }
        });

        var table = new sap.m.Table({
            id: oController.createId("theTable"),
            headerText: "{i18n>province}",
            mode: sap.m.ListMode.MultiSelect,
            width: "auto",
            selectionChange: function() {
                oController.onTableSelectionChange();
            },
            headerToolbar: createTableHeaderToolBar(oController),
            items: {
                path: "/provinces",
                sorter: {
                    path: "region",
                    group: true
                },
                template: new sap.m.ColumnListItem({
                    cells: tableCells
                })
            },
            columns: tableColumns

        });
        table.addStyleClass("sapUiResponsiveMargin");
        return table;
    }

    var createContent = function(oController) {
        var table = createTable(oController);
        return table;
    };

    var view = {
        getControllerName: getControllerName,
        createContent: createContent
    };
    return view;
})());
