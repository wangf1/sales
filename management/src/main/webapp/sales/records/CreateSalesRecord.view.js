jQuery.sap.require("sales.records.SalesRecordsUIHelper");

sap.ui.jsview("sales.records.CreateSalesRecord", (function() {
    "use strict";

    var getControllerName = function() {
        return "sales.records.CreateSalesRecord";
    };

    var createContent = function(oController) {
        var form = new sap.ui.layout.form.SimpleForm({
            layout: sap.ui.layout.form.SimpleFormLayout.ResponsiveGridLayout,
            labelSpanL: 1,
            labelSpanM: 1,
            emptySpanL: 1,
            emptySpanM: 1,
            editable: true,
        });

        form.addContent(new sap.ui.core.Title({
            text: "{i18n>selectHospitalAndDepartment}"
        }));
        form.addContent(new sap.m.Label({
            text: ""
        }));
        var facetFilter = sales.records.SalesRecordsUIHelper.createFacetFilter(oController);
        var filterLists = facetFilter.getLists();
        filterLists.forEach(function(list) {
            // For creation, the facetFilter should be single select
            list.setMode(sap.m.ListMode.SingleSelectMaster);
            // attach list close event hanlder
            list.attachListClose(function(e) {
                oController.onFilterListClose(e);
            });
        });
        form.addContent(facetFilter);

        form.addContent(new sap.ui.core.Title({
            text: "{i18n>inputQuantity}"
        }));
        form.addContent(new sap.m.Label({
            text: "{i18n>quantity}"
        }));
        form.addContent(new sap.m.Input({
            value: "{salesRecord>/quantity}"
        }));

        return form;
    };

    var view = {
        getControllerName: getControllerName,
        createContent: createContent
    };
    return view;
})());
