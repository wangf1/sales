sap.ui.jsview("sales.datacollect.ColumnSelect", (function() {
    "use strict";

    jQuery.sap.require("sales.common.i18nUtils");

    var resBundle = sales.common.i18nUtils.initAndGetResourceBundle();

    var getControllerName = function() {
        return "sales.datacollect.ColumnSelect";
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
                value: {
                    path: columName,
                    formatter: function(value) {
                        return resBundle.getText(value);
                    }
                },
                enabled: false
            }).addStyleClass("input-in-table-cell"));
        });

        var tableSelectDialog = new sap.m.TableSelectDialog(this.createId("theTable"), {
            title: "{i18n>select_cloumns}",
            items: {
                path: "/columnVisiableArray",
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
        tableSelectDialog.addStyleClass("sapUiTinyMargin");
        tableSelectDialog.addStyleClass("sapUiSizeCompact");
        return tableSelectDialog;
    };

    var view = {
        getControllerName: getControllerName,
        createContent: createContent,
        dialog: null
    };
    return view;
})());
