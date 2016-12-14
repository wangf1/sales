jQuery.sap.require("sap.ui.layout.form.SimpleForm");
jQuery.sap.require("sales.records.SalesRecordsUIHelper");
jQuery.sap.require("sales.common.IntTypeOnlyFormatValue");

sap.ui.jsview("sales.records.ListInTable", (function() {
    "use strict";

    var getControllerName = function() {
        return "sales.records.ListInTable";
    };

    var createTableHeaderToolBar = function(oController) {
        var toolbarContent = [];
        toolbarContent.push(new sap.m.SearchField({
            placeholder: "{i18n>quickSearchPlaceHolder}",
            width: "50%",
            search: function(e) {
                oController.onFilterRecords(e);
            }
        }));
        toolbarContent.push(new sap.m.ToolbarSpacer());
        toolbarContent.push(new sap.m.Button({
            text: "{i18n>refresh}",
            icon: "sap-icon://refresh",
            tooltip: "{i18n>refresh_sales_record_tooltip}",
            press: function(e) {
                oController.onRefresh();
            }
        }));
        toolbarContent.push(new sap.m.Button({
            text: "{i18n>add}",
            icon: "sap-icon://add",
            customData: [
                new sap.ui.core.CustomData({
                    key: "action",
                    value: "add"
                })
            ],
            press: function(e) {
                oController.onAddSalesRecords(e);
            }
        }));
        toolbarContent.push(new sap.m.Button({
            text: "{i18n>edit}",
            icon: "sap-icon://edit",
            enabled: "{= ${/selectedRecords}.length===1 && ${/isSelectedSalesRecordEditable} }",
            customData: [
                new sap.ui.core.CustomData({
                    key: "action",
                    value: "edit"
                })
            ],
            press: function(e) {
                oController.onAddOrEditSalesRecord(e);
            }
        }));
        toolbarContent.push(new sap.m.Button({
            text: "{i18n>delete}",
            icon: "sap-icon://delete",
            enabled: "{= ${/selectedRecords}.length>0 && ${/isSelectedSalesRecordEditable} }",
            press: function() {
                oController.onDeleteSalesRecord();
            }
        }));
        toolbarContent.push(new sap.m.Button({
            text: "{i18n>save}",
            icon: "sap-icon://save",
            enabled: "{= ${/inlineChangedRecords}.length>0 }",
            press: function() {
                oController.onSaveAllSalesRecords();
            }
        }));
        toolbarContent.push(new sap.m.Button({
            text: "{i18n>export}",
            icon: "sap-icon://action",
            enabled: "{= ${/salesRecords}.length>0 }",
            visible: "{permissionModel>/user/delete}",
            press: function() {
                oController.onExportSalesRecords();
            }
        }));
        toolbarContent.push(new sap.m.Button({
            text: "{i18n>cloneLastMonthData}",
            tooltip: "{i18n>cloneLastMonthData_tooltip}",
            icon: "sap-icon://copy",
            press: function() {
                oController.cloneLastMonthData();
            }
        }));

        var toolBar = new sap.m.Toolbar({
            content: toolbarContent
        });
        return toolBar;
    };
    var createTable = function(oController) {
        var tableColumns = [];
        var tableCells = [];
        oController.columNames.forEach(function(name) {
            var columnVisible = true;
            if (name === "manager" || name === "price") {
                // Each sales person do not need see above columns
                columnVisible = "{permissionModel>/user/delete}";
            }
            if (name === "salesPerson") {
                columnVisible = "{permissionModel>/showSalesPersonForSalesRecord/read}";
            }
            var width = "auto";
            if (name === "hospital") {
                width = "20%";
            }
            tableColumns.push(new sap.m.Column({
                width: width,
                hAlign: sap.ui.core.TextAlign.Left,
                visible: columnVisible,
                header: new sap.m.Button({
                    text: "{i18n>" + name + "}",
                    press: function(e) {
                        oController.sortTable(e);
                    },
                    customData: [
                        new sap.ui.core.CustomData({
                            key: "column",
                            value: name
                        }), new sap.ui.core.CustomData({
                            key: "descending",
                            value: true
                        })
                    ]
                })
            }));
            if (name === "quantity") {
                tableCells.push(new sap.m.Input({
                    value: {
                        path: name,
                        type: new sales.common.IntTypeOnlyFormatValue({
                            groupingEnabled: true
                        })
                    },
                    // Admin user can edit any record!
                    editable: "{= Date.parse(${date}) >= Date.parse(${/firstDayOfCurrentMonth}) || ${permissionModel>/user/delete} }",
                    liveChange: function(e) {
                        oController.onQuantityLiveChange(e);
                    },
                    textAlign: sap.ui.core.TextAlign.Right
                }).addStyleClass("input-in-table-cell"));
            } else {
                tableCells.push(new sap.m.Text({
                    text: "{" + name + "}"
                }));
            }
        });

        var table = new sap.m.Table({
            id: oController.createId("recordsTable"),
            headerText: "{i18n>salesRecords}",
            mode: sap.m.ListMode.MultiSelect,
            width: "auto",
            selectionChange: function() {
                oController.onRecordTableSelectionChange();
            },
            headerToolbar: createTableHeaderToolBar(oController),
            items: {
                path: "/salesRecords",
                sorter: {
                    path: "hospital",
                    group: true
                },
                template: new sap.m.ColumnListItem({
                    cells: tableCells
                })
            },
            columns: tableColumns

        });
        table.addStyleClass("sapUiTinyMargin");
        table.addStyleClass("sapUiSizeCompact");
        return table;
    };
    var createContent = function(oController) {
        var searchPanel = sales.records.SalesRecordsUIHelper.createSearchPanel(oController);
        var table = createTable(oController);
        var content = new sap.m.VBox({
            width: "100%",
            items: [
                searchPanel, table
            ]
        });
        return content;
    };

    var view = {
        getControllerName: getControllerName,
        createContent: createContent
    };
    return view;
})());
