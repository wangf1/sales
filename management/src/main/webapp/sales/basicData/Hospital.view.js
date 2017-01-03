sap.ui.jsview("sales.basicData.Hospital", (function() {
    "use strict";

    var getControllerName = function() {
        return "sales.basicData.Hospital";
    };

    var createTableHeaderToolBar = function(oController) {
        var toolbarContent = [];
        toolbarContent.push(new sap.m.Input({
            placeholder: "{i18n>quickSearchPlaceHolder}",
            width: "50%",
            change: function(e) {
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
            if (columName === "level") {
                tableCells.push(new sap.m.Select({
                    change: function(e) {
                        oController.onCellLiveChange(e);
                    },
                    value: "{" + columName + "}",
                    selectedKey: "{" + columName + "}",
                    items: {
                        path: "/levels",
                        template: new sap.ui.core.Item({
                            key: "{name}",
                            text: "{name}"
                        }),
                        templateShareable: true
                    }
                }));
            } else if (columName === "province") {
                tableCells.push(new sap.m.Select({
                    change: function(e) {
                        oController.onCellLiveChange(e);
                    },
                    selectedKey: "{" + columName + "}",
                    items: {
                        path: "filteredProvinces",
                        template: new sap.ui.core.Item({
                            key: "{name}",
                            text: "{name}"
                        }),
                        templateShareable: true
                    }
                }));
            } else if (columName === "region") {
                tableCells.push(new sap.m.Select(oController.createId("selectRegion"), {
                    selectedKey: "{" + columName + "}",
                    change: function(e) {
                        oController.onRegionChanged(e);
                    },
                    items: {
                        path: "/regions",
                        template: new sap.ui.core.Item({
                            key: "{}",
                            text: "{}"
                        }),
                        templateShareable: true
                    },
                }));
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
            headerText: "{i18n>hospital}",
            mode: sap.m.ListMode.MultiSelect,
            // Important note: When make the table growing, a in-row ComboBox which has a change event handler will cannot input value, the reason is
            // unknown. So when table can grow, do not use inline edit/add, use a dialog to edit/add table items.
            growing: true,
            growingThreshold: 200,
            width: "auto",
            selectionChange: function() {
                oController.onTableSelectionChange();
            },
            headerToolbar: createTableHeaderToolBar(oController),
            items: {
                path: "/tableData",
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
