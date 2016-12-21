sap.ui.jsview("sales.datacollect.Speakers", (function() {
    "use strict";

    var getControllerName = function() {
        return "sales.datacollect.Speakers";
    };

    var createTableHeaderToolBar = function(oController) {
        var toolbarContent = [];
        toolbarContent.push(new sap.m.SearchField({
            placeholder: "{i18n>quickSearchPlaceHolder}",
            width: "50%",
            search: function(e) {
                oController.onQuickFilter(e);
            }
        }));
        toolbarContent.push(new sap.m.ToolbarSpacer());

        var hBox = new sap.m.HBox();
        hBox.setAlignItems(sap.m.FlexAlignItems.Center);
        hBox.addItem(new sap.m.Label({
            text: "{i18n>startAt}"
        }));
        hBox.addItem(new sap.m.DatePicker({
            value: "{/startAt}",
            valueFormat: "yyyy-MM-dd",
            displayFormat: "yyyy-MM-dd"
        }));
        hBox.addItem(new sap.m.Label({
            text: "{i18n>endAt}"
        }));
        hBox.addItem(new sap.m.DatePicker({
            value: "{/endAt}",
            valueFormat: "yyyy-MM-dd",
            displayFormat: "yyyy-MM-dd"
        }));
        toolbarContent.push(hBox);

        toolbarContent.push(new sap.m.Button({
            text: "{i18n>search}",
            icon: "sap-icon://search",
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
            enabled: "{= ${/selectedRecords}.length>0 && ${/isSelectedRecordsEditable} }",
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
        toolbarContent.push(new sap.m.Button({
            text: "{i18n>export}",
            icon: "sap-icon://action",
            enabled: "{= ${/tableData}.length>0 }",
            visible: "{permissionModel>/user/delete}",
            press: function() {
                oController.onExport();
            }
        }));
        toolbarContent.push(new sap.m.Button({
            icon: "sap-icon://user-settings",
            tooltip: "{i18n>customize_table_tooltip}",
            enabled: "{= ${/tableData}.length>0 }",
            press: function() {
                oController.onCustomizeTable();
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
            var columnVisible = "{= ${/columnVisiableModel/" + columName + "} }";
            if (columName === "salesPersonFullName") {
                // Each sales person do not need see above columns
                columnVisible = "{permissionModel>/showSalesPersonForSalesRecord/read}";
            }
            tableColumns.push(new sap.m.Column({
                width: "30%",
                hAlign: sap.ui.core.TextAlign.Center,
                visible: columnVisible,
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
            var enableIfInThisMonth = "{= Date.parse(${date}) >= ${/firstDayOfCurrentMonth} || ${permissionModel>/user/delete} }";
            if (columName === "product") {
                tableCells.push(new sap.m.Select({
                    change: function(e) {
                        oController.onCellLiveChange(e);
                    },
                    value: "{" + columName + "}",
                    enabled: enableIfInThisMonth,
                    selectedKey: "{" + columName + "}",
                    items: {
                        path: "/allProducts",
                        template: new sap.ui.core.Item({
                            key: "{name}",
                            text: "{name}"
                        }),
                        templateShareable: true
                    }
                }));
            } else if (columName === "date" || columName === "salesPersonFullName" || columName === "lastModifyAt" || columName === "lastModifyBy") {
                tableCells.push(new sap.m.Text({
                    text: "{" + columName + "}",
                }));
            } else if (columName === "region") {
                tableCells.push(new sap.m.Select({
                    change: function(e) {
                        oController.onRegionChanged(e);
                    },
                    value: "{" + columName + "}",
                    enabled: enableIfInThisMonth,
                    selectedKey: "{" + columName + "}",
                    items: {
                        path: "/regions",
                        template: new sap.ui.core.Item({
                            key: "{}",
                            text: "{}"
                        }),
                        templateShareable: true
                    }
                }));
            } else if (columName === "province") {
                tableCells.push(new sap.m.Select({
                    change: function(e) {
                        oController.onProvinceChanged(e);
                    },
                    value: "{" + columName + "}",
                    enabled: enableIfInThisMonth,
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
            } else if (columName === "hospital") {
                tableCells.push(new sap.m.ComboBox({
                    change: function(e) {
                        oController.onCellLiveChange(e);
                    },
                    // Also bind value to model value, take advantage of the side effect that user cannot easily input value partly same as existing
                    // choice value
                    // Do not bind value, user has difficulty to input value that same as existing value
                    // value: "{" + columName + "}",
                    enabled: enableIfInThisMonth,
                    selectedKey: "{" + columName + "}",
                    items: {
                        path: "filteredHospitals",
                        template: new sap.ui.core.Item({
                            key: "{name}",
                            text: "{name}"
                        }),
                        templateShareable: true
                    }
                }));
            } else if (columName === "department") {
                tableCells.push(new sap.m.Select({
                    change: function(e) {
                        oController.onCellLiveChange(e);
                    },
                    value: "{" + columName + "}",
                    enabled: enableIfInThisMonth,
                    selectedKey: "{" + columName + "}",
                    items: {
                        path: "/departmentNames",
                        template: new sap.ui.core.Item({
                            key: "{name}",
                            text: "{name}"
                        }),
                        templateShareable: true
                    }
                }));
            } else {
                tableCells.push(new sap.m.Input({
                    value: "{" + columName + "}",
                    editable: enableIfInThisMonth,
                    liveChange: function(e) {
                        oController.onCellLiveChange(e);
                    }
                }).addStyleClass("input-in-table-cell"));
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
