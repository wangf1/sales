jQuery.sap.require('sap.ui.layout.Grid');
sap.ui.jsview("sales.records.ListInTable", (function() {
    "use strict";

    var getControllerName = function() {
        return "sales.records.ListInTable";
    };

    function createFacetFilter(oController) {
        var filters = [];
        filters.push(new sap.m.FacetFilterList(oController.createId("filterRegion"), {
            listClose: function(ec) {

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
        filters.push(new sap.m.FacetFilterList(oController.createId("filterProvince"), {
            listClose: function(ec) {

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
        filters.push(new sap.m.FacetFilterList(oController.createId("filterHospital"), {
            listClose: function(ec) {

            },
            title: "{i18n>hospital}",
            key: "hospital"
        }).bindItems({
            path: "/hospitals",
            template: new sap.m.FacetFilterItem({
                key: "{id}",
                text: "{name}"
            })
        }));
        filters.push(new sap.m.FacetFilterList(oController.createId("filterInstallDepartment"), {
            listClose: function(ec) {

            },
            title: "{i18n>installDepartment}",
            key: "installDepartment"
        }).bindItems({
            path: "/departments",
            template: new sap.m.FacetFilterItem({
                key: "{id}",
                text: "{name}"
            })
        }));
        filters.push(new sap.m.FacetFilterList(oController.createId("filterOrderDepartment"), {
            listClose: function(ec) {

            },
            title: "{i18n>orderDepartment}",
            key: "orderDepartment"
        }).bindItems({
            path: "/departments",
            template: new sap.m.FacetFilterItem({
                key: "{id}",
                text: "{name}"
            })
        }));
        filters.push(new sap.m.FacetFilterList(oController.createId("filterProduct"), {
            listClose: function(ec) {

            },
            title: "{i18n>product}",
            key: "product"
        }).bindItems({
            path: "/products",
            template: new sap.m.FacetFilterItem({
                key: "{id}",
                text: "{name}"
            })
        }));

        var facetFilter = new sap.m.FacetFilter({
            type: "Simple",
            showReset: false,
            lists: filters
        });
        return facetFilter;
    }

    function createSearchPanel(oController) {
        var toolBar = new sap.m.Toolbar();
        toolBar.addContent(new sap.m.Label({
            text: "{i18n>searchPanelHeader}"
        }));
        toolBar.addContent(new sap.m.ToolbarSpacer());
        toolBar.addContent(new sap.m.Button({
            text: "{i18n>search}",
            icon: "sap-icon://search",
            press: function() {
                oController.onAdvanceSearchSalesRecord();
            }
        }));

        var hBox = new sap.m.HBox();
        hBox.setAlignItems(sap.m.FlexAlignItems.Center);
        var facetFilter = createFacetFilter(oController);
        hBox.addItem(facetFilter);
        hBox.addItem(new sap.m.Label({
            text: "{i18n>startAt}"
        }));
        hBox.addItem(new sap.m.DatePicker({
            value: "{/startAt}",
            valueFormat: "yyyy-MM-dd",
            displayFormat: "yyyy-MM-dd",
        }));
        hBox.addItem(new sap.m.Label({
            text: "{i18n>endAt}"
        }));
        hBox.addItem(new sap.m.DatePicker({
            value: "{/endAt}",
            valueFormat: "yyyy-MM-dd",
            displayFormat: "yyyy-MM-dd",
        }));

        var form = new sap.ui.layout.form.SimpleForm({
            layout: sap.ui.layout.form.SimpleFormLayout.ResponsiveGridLayout,
            editable: true,
            minWidth: 1024,
            toolbar: toolBar
        });
        form.addContent(facetFilter);
        form.addContent(hBox);

        return form;
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
