jQuery.sap.require("sap.m.MessageToast");

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

    function buildColumnVisiableModelFromColumns(columns) {
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
        });
        return columnVisiableModel;
    }

    var toExpose = {
        showMessageToast: showMessageToast,
        buildColumnVisiableModelFromColumns: buildColumnVisiableModelFromColumns
    };
    return toExpose;
})();
