jQuery.sap.require("sales.common.ObjectUtils");

jQuery.sap.declare("sales.records.SalesRecordsUIHelper");
jQuery.sap.declare("sales.common.DateTimeUtils");

sales.records.SalesRecordsUIHelper = (function() {
    "use strict";

    function onFilterListOpen(e, oController) {
        var key = e.getSource().getKey();
        var viewModel = e.getSource().getModel();
        switch (key) {
            case "province":
                var selectedKeys = oController.byId("filterRegion").getSelectedKeys();
                var selectedRegions = sales.common.ObjectUtils.getAllOwnPropertyAsArray(selectedKeys);

                if (selectedRegions.length === 0) {
                    viewModel.getData().provinces = viewModel.getData().allProvinces;
                } else {
                    var filteredProvinces = [];
                    viewModel.getData().allProvinces.forEach(function(province) {
                        selectedRegions.forEach(function(region) {
                            if (province.region === region) {
                                filteredProvinces.push(province);
                            }
                        });
                    });
                    viewModel.getData().provinces = filteredProvinces;
                }
                break;
            case "hospital":
                var selectedProvinceKeys = oController.byId("filterProvince").getSelectedKeys();
                var selectedProvinces = sales.common.ObjectUtils.getAllOwnPropertyAsArray(selectedProvinceKeys);
                if (selectedProvinces.length === 0) {
                    viewModel.getData().hospitals = viewModel.getData().allHospitals;
                } else {
                    var filteredHospitals = [];
                    viewModel.getData().allHospitals.forEach(function(hospital) {
                        selectedProvinces.forEach(function(province) {
                            if (hospital.province === province) {
                                filteredHospitals.push(hospital);
                            }
                        });
                    });
                    viewModel.getData().hospitals = filteredHospitals;
                }
                break;
            default:
                break;
        }
        viewModel.refresh();
    }

    function onFilterListClose(e, oController) {
        var key = e.getSource().getKey();
        var viewModel = e.getSource().getModel();
        switch (key) {
            case "region":
                oController.byId("filterProvince").removeSelectedKeys();
                break;
            case "province":
                oController.byId("filterHospital").removeSelectedKeys();
                break;
            default:
                break;
        }
        viewModel.refresh();
    }

    function createFacetFilter(oController) {
        var filters = [];
        filters.push(new sap.m.FacetFilterList(oController.createId("filterRegion"), {
            title: "{i18n>region}",
            key: "region",
            listClose: function(e) {
                onFilterListClose(e, oController);
            }
        }).bindItems({
            path: "/regions",
            template: new sap.m.FacetFilterItem({
                key: "{}",
                text: "{}"
            })
        }));
        filters.push(new sap.m.FacetFilterList(oController.createId("filterProvince"), {
            title: "{i18n>province}",
            key: "province",
            listClose: function(e) {
                onFilterListClose(e, oController);
            },
            listOpen: function(e) {
                onFilterListOpen(e, oController);
            }
        }).bindItems({
            path: "/provinces",
            template: new sap.m.FacetFilterItem({
                key: "{name}",
                text: "{name}"
            })
        }));
        filters.push(new sap.m.FacetFilterList(oController.createId("filterHospital"), {
            title: "{i18n>hospital}",
            key: "hospital",
            listOpen: function(e) {
                onFilterListOpen(e, oController);
            }
        }).bindItems({
            path: "/hospitals",
            template: new sap.m.FacetFilterItem({
                key: "{name}",
                text: "{name}"
            })
        }));
        filters.push(new sap.m.FacetFilterList(oController.createId("filterInstallDepartment"), {
            title: "{i18n>installDepartment}",
            key: "installDepartment"
        }).bindItems({
            path: "/departments",
            template: new sap.m.FacetFilterItem({
                key: "{name}",
                text: "{name}"
            })
        }));
        filters.push(new sap.m.FacetFilterList(oController.createId("filterOrderDepartment"), {
            title: "{i18n>orderDepartment}",
            key: "orderDepartment"
        }).bindItems({
            path: "/departments",
            template: new sap.m.FacetFilterItem({
                key: "{name}",
                text: "{name}"
            })
        }));
        filters.push(new sap.m.FacetFilterList(oController.createId("filterProduct"), {
            title: "{i18n>product}",
            key: "product"
        }).bindItems({
            path: "/products",
            template: new sap.m.FacetFilterItem({
                key: "{name}",
                text: "{name}"
            })
        }));

        var facetFilter = new sap.m.FacetFilter(oController.createId("facetFilter"), {
            type: "Simple",
            showReset: false,
            lists: filters
        });
        return facetFilter;
    }

    function onResetSearchCondition(oController) {
        oController.byId("filterRegion").removeSelectedKeys();
        oController.byId("filterProvince").removeSelectedKeys();
        oController.byId("filterHospital").removeSelectedKeys();
        oController.byId("filterInstallDepartment").removeSelectedKeys();
        oController.byId("filterOrderDepartment").removeSelectedKeys();
        oController.byId("filterProduct").removeSelectedKeys();
        oController.byId("facetFilter").rerender();// call rerender otherwise the facetFilter cannot refresh

        var oViewModel = oController.getView().getModel();
        var viewModelData = oViewModel.getData();
        viewModelData.startAt = sales.common.DateTimeUtils.firstDayOfCurrentMonth();
        viewModelData.endAt = sales.common.DateTimeUtils.today();
        oViewModel.refresh();
    }

    function createSearchPanel(oController) {

        var searchPanelLabel = new sap.m.Text({
            textAlign: sap.ui.core.TextAlign.Center,
            text: "{i18n>searchPanelHeader}"
        });
        var searchPanelLabelHBox = new sap.m.HBox({
            alignItems: sap.m.FlexAlignItems.Center,
            items: [
                searchPanelLabel
            ]
        });

        var facetFilter = createFacetFilter(oController);

        var hBoxStartAt = new sap.m.HBox();
        hBoxStartAt.setAlignItems(sap.m.FlexAlignItems.Center);
        hBoxStartAt.addItem(new sap.m.Label({
            text: "{i18n>startAt}"
        }));
        hBoxStartAt.addItem(new sap.m.DatePicker({
            value: "{/startAt}",
            valueFormat: "yyyy-MM-dd",
            displayFormat: "yyyy-MM-dd"
        }));

        var hBoxEndAt = new sap.m.HBox();
        hBoxEndAt.setAlignItems(sap.m.FlexAlignItems.Center);
        hBoxEndAt.addItem(new sap.m.Label({
            text: "{i18n>endAt}"
        }));
        hBoxEndAt.addItem(new sap.m.DatePicker({
            value: "{/endAt}",
            valueFormat: "yyyy-MM-dd",
            displayFormat: "yyyy-MM-dd"
        }));
        hBoxEndAt.addItem(new sap.m.ToolbarSpacer());

        var searchButton = new sap.m.Button({
            text: "{i18n>search}",
            icon: "sap-icon://search",
            type: sap.m.ButtonType.Emphasized,
            press: function() {
                oController.onRefresh();
            }
        });
        var resetButton = new sap.m.Button({
            text: "{i18n>resetSearchCondition}",
            icon: "sap-icon://reset",
            press: function() {
                onResetSearchCondition(oController);
            }
        });

        var hBoxAll = new sap.m.HBox({
            items: [
                searchPanelLabelHBox, facetFilter, hBoxStartAt, hBoxEndAt, searchButton, resetButton
            ]
        });

        var form = new sap.ui.layout.form.SimpleForm({
            // Must explicitly set layout type, otherwise in non-debug mode in Chrome browser, the UI will no response. Should be a UI5 bug.
            layout: sap.ui.layout.form.SimpleFormLayout.ResponsiveGridLayout,
            labelSpanL: 1,
            labelSpanM: 1,
            labelSpanS: 1,
            emptySpanL: 0,
            emptySpanM: 0,
            editable: true,
            content: [
                hBoxAll
            ]
        });
        form.addStyleClass("noPaddingAndMargin");
        return form;
    }

    var toExpose = {
        createSearchPanel: createSearchPanel
    };
    return toExpose;
})();
