jQuery.sap.require("sales.common.IntTypeOnlyFormatValue");
jQuery.sap.require("sales.common.FloatTypeOnlyFormatValue");

sap.ui.jsview("sales.datacollect.RegionMeetings", (function() {
    "use strict";

    var getControllerName = function() {
        return "sales.datacollect.RegionMeetings";
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
            visible: "{permissionModel>/dataCollect/update}",
            press: function(e) {
                oController.onAdd(e);
            }
        }));
        toolbarContent.push(new sap.m.Button({
            text: "{i18n>delete}",
            icon: "sap-icon://delete",
            enabled: "{= ${/selectedRecords}.length>0 && ${/isSelectedRecordsEditable} }",
            visible: "{permissionModel>/dataCollect/update}",
            press: function() {
                oController.onDelete();
            }
        }));
        toolbarContent.push(new sap.m.Button({
            text: "{i18n>save}",
            icon: "sap-icon://save",
            enabled: "{= ${/inlineChangedRecords}.length>0 || ${/newAddedRecords}.length>0}",
            visible: "{permissionModel>/dataCollect/update}",
            press: function() {
                oController.onSaveAll();
            }
        }));
        toolbarContent.push(new sap.m.Button({
            text: "{i18n>export}",
            icon: "sap-icon://action",
            enabled: "{= ${/tableData}.length>0 }",
            visible: "{permissionModel>/dataCollect/export}",
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

    function createFormsCell(oController, columName) {
        var hBox = new sap.m.HBox();

        var input = new sap.m.Input({
            value: {
                path: columName,
            },
            tooltip: {
                path: columName,
            },
            enabled: false
        });
        hBox.addItem(input);

        var button = new sap.m.Button({
            icon: "sap-icon://edit",
            press: function(e) {
                oController.onEditMettingForms(e);
            }
        });
        hBox.addItem(button);

        return hBox;
    }

    function createTable(oController) {
        var tableCells = [];
        var tableColumns = [];
        oController.columnNames.forEach(function(columName) {
            var columnVisible = "{= ${columnVisiableModel>/" + columName + "} }";
            if (columName === "salesPersonFullName") {
                // Each sales person do not need see above columns
                columnVisible = "{permissionModel>/showSalesPersonForSalesRecord/read}";
            }
            var enableIfInThisMonth = "{= Date.parse(${date}) >= ${/firstDayOfCurrentMonth} || ${permissionModel>/user/delete} }";
            var enableIfInThisMonthOrLastMonth = "{= Date.parse(${date}) >= ${/firstDayOfPreviousMonth} || ${permissionModel>/user/delete} }";
            var width = "auto";
            if (columName === "name") {
                width = "10%"
            } else if (columName == "allKindsOfInputs") {
                width = "25%";
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
            if (columName === "date" || columName === "salesPersonFullName" || columName === "lastModifyAt" || columName === "lastModifyBy") {
                tableCells.push(new sap.m.Text({
                    text: "{" + columName + "}",
                }));
            } else if (columName === "region") {
                tableCells.push(new sap.m.Select({
                    change: function(e) {
                        oController.onRegionChanged(e);
                    },
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
                        oController.onCellLiveChange(e);
                    },
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
            } else if (columName === "status") {
                tableCells.push(new sap.m.Select({
                    change: function(e) {
                        oController.onCellLiveChange(e);
                    },
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
            } else if (columName === "type") {
                tableCells.push(new sap.m.Select({
                    change: function(e) {
                        oController.onCellLiveChange(e);
                    },
                    tooltip: "{" + columName + "}",
                    enabled: enableIfInThisMonth,
                    selectedKey: "{" + columName + "}",
                    items: {
                        path: "/allTypes",
                        template: new sap.ui.core.Item({
                            key: "{}",
                            text: "{}"
                        }),
                        templateShareable: true
                    }
                }));
            } else if (columName === "form") {
                var formsCell = createFormsCell(oController, columName);
                tableCells.push(formsCell);
            } else if (columName === "name") {
                // Above columns can only edit in current month
                tableCells.push(new sap.m.Input({
                    value: "{" + columName + "}",
                    tooltip: "{" + columName + "}",
                    editable: enableIfInThisMonth,
                    liveChange: function(e) {
                        oController.onCellLiveChange(e);
                    }
                }));
            } else if (columName === "allKindsOfInputs") {
                var vBox = new sap.m.VBox();
                var inputColumNames = [
                    "numberOfPeople", "satelliteMeetingCost", "exhibitionCost", "speakerCost", "otherCost", "otherTAndE", "department"
                ];
                inputColumNames.forEach(function(inputColumn) {
                    var hBox = new sap.m.HBox();
                    var labelText;
                    if (inputColumn === "department") {
                        labelText = "{i18n>department_form_label}";
                    } else {
                        labelText = "{i18n>" + inputColumn + "}";
                    }
                    hBox.addItem(new sap.m.Label({
                        text: labelText,
                        width: "4.5em"
                    }));
                    var inputEnabled = enableIfInThisMonthOrLastMonth;
                    var valueType = new sales.common.FloatTypeOnlyFormatValue()
                    if (inputColumn === "numberOfPeople") {
                        inputEnabled = enableIfInThisMonth;
                        valueType = new sales.common.IntTypeOnlyFormatValue({
                            groupingEnabled: true
                        });
                    }
                    var inputControl;
                    if (inputColumn === "department") {
                        inputControl = new sap.m.Select({
                            change: function(e) {
                                oController.onCellLiveChange(e);
                            },
                            tooltip: "{" + inputColumn + "}",
                            enabled: enableIfInThisMonth,
                            selectedKey: "{" + inputColumn + "}",
                            items: {
                                path: "/departmentNames",
                                template: new sap.ui.core.Item({
                                    key: "{name}",
                                    text: "{name}"
                                }),
                                templateShareable: true
                            }
                        });
                    } else {
                        inputControl = new sap.m.Input({
                            value: {
                                path: inputColumn,
                                type: valueType
                            },
                            tooltip: "{" + inputColumn + "} ",// Intend add a tail space here to convert number to string, to avoid "Uncaught Error:
                            // "1"
                            // is not valid for aggregation "tooltip" of Element" error which happen when input a
                            // number
                            editable: inputEnabled,
                            textAlign: sap.ui.core.TextAlign.Right,
                            liveChange: function(e) {
                                oController.onCellLiveChange(e);
                            }
                        }).addStyleClass("input-in-table-cell")
                    }
                    hBox.addItem(inputControl);
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
