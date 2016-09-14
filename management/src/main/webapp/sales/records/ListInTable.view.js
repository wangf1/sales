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
        var tableColumns = [];
        var tableCells = [];
        var columNames = [
            "region", "province", "manager", "salesPerson", "hospital", "product", "installDepartment", "orderDepartment", "quantity", "date"
        ];
        columNames.forEach(function(name) {
            tableColumns.push(new sap.m.Column({
                width: "30%",
                hAlign: sap.ui.core.TextAlign.Center,
                header: [
                    new sap.m.Text({
                        text: "{i18n>" + name + "}"
                    })
                ]
            }));
            tableCells.push(new sap.m.Text({
                text: "{" + name + "}"
            }));
        });

        var table = new sap.m.Table({
            id: oController.createId("recordsTable"),
            headerText: "{i18n>salesRecords}",
            width: "auto",
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
        var table = createTable(oController);
        return table;
    };

    var view = {
        getControllerName: getControllerName,
        createContent: createContent
    };
    return view;
})());
