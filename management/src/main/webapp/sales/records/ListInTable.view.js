sap.ui.jsview("sales.records.ListInTable", (function() {
    "use strict";

    var getControllerName = function() {
        return "sales.records.ListInTable";
    };

    function createSearchPanel(oController) {
        var filters = [];
        filters.push(new sap.m.FacetFilterList(oController.createId("rpt_ffl_app_id"), {
            multiselect: true,
            listClose: function(ec) {
                oController.onApplicationFacetFilter(ec);
            },
            title: "{i18n>region}",
            key: "region"
        }).bindItems({
            path: "/regions",
            template: new sap.m.FacetFilterItem({
                key: "{}",
                text: "{}"
            })
        }));
        filters.push(new sap.m.FacetFilterList(oController.createId("rpt_ffl_vendor"), {
            multiselect: true,
            listClose: function(ec) {
                oController.onApplicationFacetFilter(ec);
            },
            title: "{i18n>province}",
            key: "province"
        }).bindItems({
            path: "/provinces",
            template: new sap.m.FacetFilterItem({
                key: "{id}",
                text: "{name}"
            })
        }));

        var facetFilter = new sap.m.FacetFilter({
            type: "Simple",
            showReset: true,
            lists: filters,
            reset: function(re) {
                oController.onFilterReset(re);
            }
        });
        return facetFilter;
    }

    var createTableHeaderToolBar = function(oController) {
        var toolbarContent = [];
        toolbarContent.push(new sap.m.Title({
            text: "{i18n>salesRecords}"
        }));
        toolbarContent.push(new sap.m.ToolbarSpacer());
        toolbarContent.push(new sap.m.SearchField({
            width: "50%",
            search: oController.onFilterRecords
        }));
        var toolBar = new sap.m.Toolbar({
            content: toolbarContent
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
