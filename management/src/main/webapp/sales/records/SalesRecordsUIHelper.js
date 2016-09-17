jQuery.sap.require("sales.common.ObjectUtils");

jQuery.sap.declare("sales.records.SalesRecordsUIHelper");

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

    var toExpose = {
        createFacetFilter: createFacetFilter
    };
    return toExpose;
})();
