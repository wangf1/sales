sap.ui.jsview("sales.records.ListInTable", (function() {
    "use strict";

    var getControllerName = function() {
        return "sales.records.ListInTable";
    };
    var createTableHeaderToolBar = function(oController) {
        var toolBar = new sap.m.Toolbar({
            content: [
                new sap.m.Title({
                    text: "{i18n>salesRecords}"
                }), new sap.m.ToolbarSpacer(), new sap.m.SearchField({
                    width: "50%",
                    search: oController.onFilterRecords
                })
            ]
        });
        return toolBar;
    };
    var createTable = function(oController) {
        var table = new sap.m.Table({
            id: oController.createId("recordsTable"),
            headerText: "{i18n>salesRecords}",
            width: "auto",
            items: {
                path: "/salesRecords",
                sorter: {
                    path: "installLocation/department/hospital/name",
                    group: true
                },
                template: new sap.m.ColumnListItem({
                    cells: [
                        new sap.m.Text({
                            text: "{installLocation/department/hospital/name}"
                        })
                    ]
                })
            },
            headerToolbar: createTableHeaderToolBar(oController),
            columns: [
                new sap.m.Column({
                    width: "30%",
                    hAlign: sap.ui.core.TextAlign.Center,
                    header: [
                        new sap.m.Text({
                            text: "{i18n>hospital}"
                        })
                    ]
                })
            ]

        });
        table.addStyleClass("sapUiResponsiveMargin");
        return table;
    };
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
