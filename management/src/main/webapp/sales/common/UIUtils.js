jQuery.sap.require("sap.m.MessageToast");
jQuery.sap.require("sap.ui.model.json.JSONModel");

jQuery.sap.declare("sales.common.UIUtils");
sales.common.UIUtils = (function() {
    "use strict";

    function showMessageToast(message) {
        sap.m.MessageToast.show(message, {
            duration: 5000,
            width: "25em",
            closeOnBrowserNavigation: false
        });
    }

    function buildColumnVisiableModelFromColumns(columns, dataType) {
        var columnVisiableModel = {};
        columns.forEach(function(column) {
            if (column === "lastModifyAt" || column === "lastModifyBy") {
                columnVisiableModel[column] = false;
            } else if (column === "salesPersonFullName" || column === "managerFullName") {
                // Do not let user config salesPerson and Manager name visibility, to avoid confusion, since sales user cannot view thiese two column
                return;
            } else {
                columnVisiableModel[column] = true;
            }
            // Type specific setting
            if (dataType === "SalesRecord") {
                columnVisiableModel["region"] = false;
                columnVisiableModel["price"] = false;
                columnVisiableModel["hospitalLevel"] = false;
            }
        });
        return columnVisiableModel;
    }

    function createJsonModelWithSizeLimit10000(modelData) {
        var oViewModel = new sap.ui.model.json.JSONModel(modelData);
        oViewModel.setSizeLimit(10000);
        return oViewModel;
    }

    var toExpose = {
        showMessageToast: showMessageToast,
        buildColumnVisiableModelFromColumns: buildColumnVisiableModelFromColumns,
        createJsonModelWithSizeLimit10000: createJsonModelWithSizeLimit10000
    };
    return toExpose;
})();
