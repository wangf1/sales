jQuery.sap.declare("sales.records.SalesRecordsUIHelper");

sales.records.SalesRecordsUIHelper = (function() {
    "use strict";

    function createFacetFilter(oController) {
        var filters = [];
        filters.push(new sap.m.FacetFilterList(oController.createId("filterRegion"), {
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
            title: "{i18n>hospital}",
            key: "hospital",
        }).bindItems({
            path: "/hospitals",
            template: new sap.m.FacetFilterItem({
                key: "{id}",
                text: "{name}"
            })
        }));
        filters.push(new sap.m.FacetFilterList(oController.createId("filterInstallDepartment"), {
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

    var toExpose = {
        createFacetFilter: createFacetFilter
    };
    return toExpose;
})();
