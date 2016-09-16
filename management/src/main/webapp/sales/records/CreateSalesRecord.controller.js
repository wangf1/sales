sap.ui.define([
    "sap/ui/core/mvc/Controller", "sap/ui/model/json/JSONModel", "sap/ui/model/Filter", "sap/ui/model/FilterOperator", "sales/common/AjaxUtils", "sales/common/i18nUtils", "sales/common/DateTimeUtils"
], function(Controller, JSONModel, Filter, FilterOperator, AjaxUtils, i18nUtils, DateTimeUtils) {
    "use strict";

    var salesRecordData = {
        hospital: "",
        installDepartment: "",
        orderDepartment: "",
        product: "",
        quantity: 0
    };

    var salesRecordModel = new JSONModel(salesRecordData);

    function init() {
        this.getView().setModel(salesRecordModel, "salesRecord");
    }

    function onSave() {
        console.log("On Save...");
    }

    function getFirstOwnProperty(object) {
        for ( var key in object) {
            if (!object.hasOwnProperty(key)) {
                continue;
            }
            var value = object[key];
            return value;
        }
    }
    function onFilterListClose(e) {
        var key = e.getSource().getKey();
        var selectedKeys = e.getSource().getSelectedKeys();
        switch (key) {
            case "hospital":
                salesRecordData.hospital = getFirstOwnProperty(selectedKeys);
                break;
            case "installDepartment":
                salesRecordData.installDepartment = getFirstOwnProperty(selectedKeys);
                break;
            case "orderDepartment":
                salesRecordData.orderDepartment = getFirstOwnProperty(selectedKeys);
                break;
            case "product":
                salesRecordData.product = getFirstOwnProperty(selectedKeys);
                break;
            default:
                break;
        }
    }

    var controller = Controller.extend("sales.records.CreateSalesRecord", {
        onInit: init,
        onSave: onSave,
        onFilterListClose: onFilterListClose
    });
    return controller;
});
