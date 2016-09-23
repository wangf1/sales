sap.ui.define([
    "sales/basicData/CRUDTableController", "sap/ui/model/json/JSONModel", "sap/ui/model/Filter", "sap/ui/model/FilterOperator", "sales/common/AjaxUtils", "sales/common/i18nUtils",
    "sales/common/DateTimeUtils", "sales/common/ValidateUtils", "sales/common/UIUtils", "sales/common/ArrayUtils", "sap/m/MessageBox"
], function(CRUDTableController, JSONModel, Filter, FilterOperator, AjaxUtils, i18nUtils, DateTimeUtils, ValidateUtils, UIUtils, ArrayUtils, MessageBox) {
    "use strict";

    var controller = CRUDTableController.extend("sales.basicData.Department", {
        columnNames: [
            "name"
        ],
        urlForListAll: "listAllDepartments",
        urlForSaveAll: "saveDepartmentNames",
        urlForDeleteAll: "deleteDepartmentNams",
    });
    return controller;
});
