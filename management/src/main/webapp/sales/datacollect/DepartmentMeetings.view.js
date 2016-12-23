jQuery.sap.require("sales.common.FloatTypeOnlyFormatValue");

sap.ui.jsview("sales.datacollect.DepartmentMeetings", (function() {
    "use strict";

    var getControllerName = function() {
        return "sales.datacollect.DepartmentMeetings";
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
            var enableIfInThisMonth = "{= Date.parse(${date}) >= ${/firstDayOfCurrentMonth} || ${permissionModel>/user/delete} }";
            var enableIfInThisMonthOrLastMonth = "{= Date.parse(${date}) >= ${/firstDayOfPreviousMonth} || ${permissionModel>/user/delete} }";
            var width = "auto";
            if (columName === "hospital") {
                width = "15%"
            } else if (columName == "columnsNeedInOneCell") {
                width = "15%";
            }
            tableColumns.push(new sap.m.Column({
                width: width,
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
            if (columName === "product") {
                tableCells.push(new sap.m.Select({
                    change: function(e) {
                        oController.onCellLiveChange(e);
                    },
                    value: "{" + columName + "}",
                    tooltip: "{" + columName + "}",
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
                    tooltip: "{" + columName + "}",
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
                    tooltip: "{" + columName + "}",
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
                    tooltip: "{" + columName + "}",
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
                    tooltip: "{" + columName + "}",
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
            } else if (columName === "status") {
                tableCells.push(new sap.m.Select({
                    change: function(e) {
                        oController.onCellLiveChange(e);
                    },
                    value: "{" + columName + "}",
                    tooltip: "{" + columName + "}",
                    enabled: enableIfInThisMonthOrLastMonth,
                    selectedKey: "{" + columName + "}",
                    items: {
                        path: "availableStatuses",
                        template: new sap.ui.core.Item({
                            key: "{}",
                            text: "{}"
                        }),
                        templateShareable: true
                    }
                }));
            } else if (columName === "purpose") {
                tableCells.push(new sap.m.Select({
                    change: function(e) {
                        oController.onCellLiveChange(e);
                    },
                    value: "{" + columName + "}",
                    tooltip: "{" + columName + "}",
                    enabled: enableIfInThisMonth,
                    selectedKey: "{" + columName + "}",
                    items: {
                        path: "/department_meeting_purposes",
                        template: new sap.ui.core.Item({
                            key: "{}",
                            text: "{}"
                        }),
                        templateShareable: true
                    }
                }));
            } else if (columName === "subject") {
                tableCells.push(new sap.m.Select({
                    change: function(e) {
                        oController.onCellLiveChange(e);
                    },
                    value: "{" + columName + "}",
                    tooltip: "{" + columName + "}",
                    enabled: enableIfInThisMonth,
                    selectedKey: "{" + columName + "}",
                    items: {
                        path: "/department_meeting_subjects",
                        template: new sap.ui.core.Item({
                            key: "{}",
                            text: "{}"
                        }),
                        templateShareable: true
                    }
                }));
            } else if (columName === "columnsNeedInOneCell") {
                var columnsInCell = [
                    "planCost", "actualCost"
                ];
                var vBox = new sap.m.VBox();
                columnsInCell.forEach(function(inputColumn) {
                    var hBox = new sap.m.HBox();
                    hBox.addItem(new sap.m.Label({
                        text: "{i18n>" + inputColumn + "}"
                    }));
                    var inputEnabled = enableIfInThisMonthOrLastMonth;
                    if (inputColumn === "planCost") {
                        inputEnabled = enableIfInThisMonth;
                    }
                    var valueControl = new sap.m.Input({
                        value: {
                            path: inputColumn,
                            type: new sales.common.FloatTypeOnlyFormatValue()
                        },
                        tooltip: "{" + inputColumn + "}",
                        editable: inputEnabled,
                        textAlign: sap.ui.core.TextAlign.Right,
                        liveChange: function(e) {
                            oController.onCellLiveChange(e);
                        }
                    }).addStyleClass("input-in-table-cell");
                    hBox.addItem(valueControl);
                    vBox.addItem(hBox);
                });
                tableCells.push(vBox);
            } else {
                tableCells.push(new sap.m.Input({
                    value: "{" + columName + "}",
                    tooltip: "{" + columName + "}",
                    editable: enableIfInThisMonthOrLastMonth,
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
        table.addStyleClass("tinyTDPadding");
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
