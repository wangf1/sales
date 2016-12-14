sap.ui.jsview("sales.analysis.LostCustomer", (function() {
    "use strict";

    var getControllerName = function() {
        return "sales.analysis.LostCustomer";
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
            text: "{i18n>old_month}"
        }));
        hBox.addItem(new sap.m.DatePicker({
            value: "{/oldMonth}",
            valueFormat: "yyyy-MM-dd",
            displayFormat: "yyyy-MM"
        }));
        hBox.addItem(new sap.m.Label({
            text: "{i18n>new_month}"
        }));
        hBox.addItem(new sap.m.DatePicker({
            value: "{/newMonth}",
            valueFormat: "yyyy-MM-dd",
            displayFormat: "yyyy-MM"
        }));
        toolbarContent.push(hBox);

        toolbarContent.push(new sap.m.Button({
            text: "{i18n>search}",
            icon: "sap-icon://search",
            press: function(e) {
                oController.onRefresh(e);
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
            tableCells.push(new sap.m.Text({
                text: "{" + columName + "}",
                liveChange: function(e) {
                    oController.onCellLiveChange(e);
                }
            }));
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
// table.addStyleClass("sapUiResponsiveMargin");
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
