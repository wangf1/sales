sap.ui.jsview("sales.datacollect.AgencyRecruit", (function() {
    "use strict";

    jQuery.sap.require("sales.common.StringArrayAsCommaStringType");

    var getControllerName = function() {
        return "sales.datacollect.AgencyRecruit";
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

    function initColumnNamesAccordingToUsageType(thisController) {
        var columns = [
            "date", "region", "province", "salesPersonFullName", "agency", "products"
        ];
        var viewData = thisController.getView().getViewData();
        if (!viewData.usedForAgencyTraining) {
            // Agency recruit should have level
            columns.push("level");
        } else {
            columns.push("trainingContent");
        }
        columns.push("lastModifyAt");
        columns.push("lastModifyBy");
        thisController.columnNames = columns;
    }

    function createProductsCell(oController, columName) {
        var hBox = new sap.m.HBox();

        var input = new sap.m.Input({
            value: {
                path: columName,
                type: new sales.common.StringArrayAsCommaStringType()
            },
            tooltip: {
                path: columName,
                type: new sales.common.StringArrayAsCommaStringType()
            },
            editable: false
        });
        hBox.addItem(input);

        var button = new sap.m.Button({
            icon: "sap-icon://edit",
            press: function(e) {
                oController.onEditProducts(e);
            }
        });
        hBox.addItem(button);

        return hBox;
    }

    function createTable(oController) {
        var tableCells = [];
        var tableColumns = [];
        initColumnNamesAccordingToUsageType(oController);
        oController.columnNames.forEach(function(columName) {
            var columnVisible = "{= ${columnVisiableModel>/" + columName + "} }";
            if (columName === "salesPersonFullName") {
                // Each sales person do not need see above columns
                columnVisible = "{permissionModel>/showSalesPersonForSalesRecord/read}";
            }
            var width = "auto";
            if (columName === "trainingContent") {
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
            var enableIfInThisMonth = "{= Date.parse(${date}) >= ${/firstDayOfCurrentMonth} || ${permissionModel>/user/delete} }";
            if (columName === "products") {
                var hBox = createProductsCell(oController, columName);
                tableCells.push(hBox);
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
                        oController.onCellLiveChange(e);
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
            } else if (columName === "agency") {
                var controlSeetings = {
                    change: function(e) {
                        oController.onAgencyChanged(e);
                    },
                    enabled: enableIfInThisMonth,
                    selectedKey: "{" + columName + "}",
                    items: {
                        path: "/agencies",
                        template: new sap.ui.core.Item({
                            key: "{name}",
                            text: "{name}"
                        }),
                        templateShareable: true
                    }
                };
                var cellControl;
                var viewData = oController.getView().getViewData();
                if (viewData.usedForAgencyTraining) {
                    cellControl = new sap.m.Select(controlSeetings);
                } else {
                    // Agency recruit can input new value
                    cellControl = new sap.m.ComboBox(controlSeetings);
                }
                tableCells.push(cellControl);
            } else if (columName === "level") {
                tableCells.push(new sap.m.Select({
                    change: function(e) {
                        oController.onCellLiveChange(e);
                    },
                    value: "{" + columName + "}",
                    enabled: enableIfInThisMonth,
                    selectedKey: "{" + columName + "}",
                    items: {
                        path: "/agencyLevels",
                        template: new sap.ui.core.Item({
                            key: "{}",
                            text: "{}"
                        }),
                        templateShareable: true
                    }
                }));
            } else if (columName === "trainingContent") {
                tableCells.push(new sap.m.TextArea({
                    change: function(e) {
                        oController.onCellLiveChange(e);
                    },
                    value: "{" + columName + "}",
                    enabled: enableIfInThisMonth
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
