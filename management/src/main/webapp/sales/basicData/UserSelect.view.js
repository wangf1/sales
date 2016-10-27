jQuery.sap.require("sales.records.SalesRecordsUIHelper");

sap.ui.jsview("sales.basicData.UserSelect", (function() {
    "use strict";

    var getControllerName = function() {
        return "sales.basicData.UserSelect";
    };

    var createContent = function(oController) {
        var tableCells = [];
        var tableColumns = [];
        oController.columnNames.forEach(function(columName) {
            tableColumns.push(new sap.m.Column({
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

            tableCells.push(new sap.m.Input({
                value: "{" + columName + "}",
                enabled: false
            }).addStyleClass("input-in-table-cell"));
        });

        var tableSelectDialog = new sap.m.TableSelectDialog(this.createId("theTable"), {
            items: {
                path: "/users",
                template: new sap.m.ColumnListItem({
                    cells: tableCells
                })
            },
            columns: tableColumns,
            liveChange: function(e) {
                oController.onQuickFilter(e);
            },
            multiSelect: true,
            rememberSelections: true
        });
        this.dialog = tableSelectDialog;
        return tableSelectDialog;
    };

    var view = {
        getControllerName: getControllerName,
        createContent: createContent,
        dialog: null
    };
    return view;
})());