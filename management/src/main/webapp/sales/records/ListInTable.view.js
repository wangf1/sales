jQuery.sap.require("sap.ui.layout.form.SimpleForm");
jQuery.sap.require("sales.records.SalesRecordsUIHelper");

sap.ui.jsview("sales.records.ListInTable", (function() {
    "use strict";

    var getControllerName = function() {
        return "sales.records.ListInTable";
    };

    function createSearchPanel(oController) {
        var toolBar = new sap.m.Toolbar();
        toolBar.addContent(new sap.m.Label({
            text: "{i18n>searchPanelHeader}"
        }));
        toolBar.addContent(new sap.m.ToolbarSpacer({
            width: "10%"
        }));
        toolBar.addContent(new sap.m.Button({
            text: "{i18n>search}",
            icon: "sap-icon://search",
            press: function() {
                oController.onAdvanceSearchSalesRecord();
            }
        }));
        toolBar.addContent(new sap.m.Button({
            text: "{i18n>resetSearchCondition}",
            icon: "sap-icon://reset",
            press: function() {
                oController.onResetSearchCondition();
            }
        }));

        var hBox = new sap.m.HBox();
        hBox.setAlignItems(sap.m.FlexAlignItems.Center);

        var facetFilter = sales.records.SalesRecordsUIHelper.createFacetFilter(oController);
        hBox.addItem(facetFilter);

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

        var form = new sap.ui.layout.form.SimpleForm({
            // Must explicitly set layout type, otherwise in non-debug mode in Chrome browser, the UI will no response. Should be a UI5 bug.
            layout: sap.ui.layout.form.SimpleFormLayout.ResponsiveGridLayout,
            labelSpanL: 2,
            labelSpanM: 2,
            emptySpanL: 1,
            emptySpanM: 1,
            editable: true,
            minWidth: 1024,
            toolbar: toolBar
        });
        form.addContent(new sap.m.Label());
        form.addContent(facetFilter);
        form.addContent(new sap.m.Label());
        form.addContent(hBox);

        return form;
    }

    var createTableHeaderToolBar = function(oController) {
        var toolbarContent = [];
        toolbarContent.push(new sap.m.Title({
            text: "{i18n>salesRecords}"
        }));
        toolbarContent.push(new sap.m.SearchField({
            placeholder: "{i18n>quickSearchPlaceHolder}",
            width: "50%",
            showSearchButton: false,
            liveChange: function(e) {
                oController.onFilterRecords(e);
            }
        }));
        toolbarContent.push(new sap.m.ToolbarSpacer());
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
                oController.onAddOrEditSalesRecord(e);
            }
        }));
        toolbarContent.push(new sap.m.Button({
            text: "{i18n>edit}",
            icon: "sap-icon://edit",
            enabled: "{= ${/selectedRecords}.length===1 }",
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
            enabled: "{= ${/selectedRecords}.length>0 }",
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

        var toolBar = new sap.m.Toolbar({
            content: toolbarContent
        });
        return toolBar;
    };
    var createTable = function(oController) {
        var tableColumns = [];
        var tableCells = [];
        oController.columNames.forEach(function(name) {
            tableColumns.push(new sap.m.Column({
                width: "30%",
                hAlign: sap.ui.core.TextAlign.Center,
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
                    value: "{" + name + "}",
                    liveChange: function(e) {
                        oController.onQuantityLiveChange(e);
                    }
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
        table.addStyleClass("sapUiResponsiveMargin");
        return table;
    };
    var createContent = function(oController) {
        var searchPanel = createSearchPanel(oController);
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
