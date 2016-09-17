sap.ui.define([
    "sap/ui/core/mvc/Controller", "sap/ui/model/json/JSONModel", "sap/ui/model/Filter", "sap/ui/model/FilterOperator", "sales/common/AjaxUtils", "sales/common/i18nUtils",
    "sales/common/DateTimeUtils", "sales/common/ValidateUtils", "sales/common/UIUtils"
], function(Controller, JSONModel, Filter, FilterOperator, AjaxUtils, i18nUtils, DateTimeUtils, ValidateUtils, UIUtils) {
    "use strict";

    var salesRecordData = {
        id: 0,
        region: "",
        province: "",
        hospital: "",
        installDepartment: "",
        orderDepartment: "",
        product: "",
        quantity: 0,
    };

    var selectedKeysBackup = {
        region: {},
        province: {},
        hospital: {},
        installDepartment: {},
        orderDepartment: {},
        product: {}
    }

    var salesRecordModel = new JSONModel(salesRecordData);

    // restore last time selection to save user input
    function initSelectedItems(thisController) {
        thisController.byId("filterRegion").setSelectedKeys(selectedKeysBackup.region);
        thisController.byId("filterProvince").setSelectedKeys(selectedKeysBackup.province);
        thisController.byId("filterHospital").setSelectedKeys(selectedKeysBackup.hospital);
        thisController.byId("filterInstallDepartment").setSelectedKeys(selectedKeysBackup.installDepartment);
        thisController.byId("filterOrderDepartment").setSelectedKeys(selectedKeysBackup.orderDepartment);
        thisController.byId("filterProduct").setSelectedKeys(selectedKeysBackup.product);
    }

    function init() {
        this.getView().setModel(salesRecordModel, "salesRecord");
        initSelectedItems(this);
    }

    function validateSalesRecord() {
        var resBundle = i18nUtils.initAndGetResourceBundle();
        var errorMessage;
        if (salesRecordData.hospital.trim() === "") {
            errorMessage = resBundle.getText("hospitalRequired");
        } else if (salesRecordData.installDepartment.trim() === "") {
            errorMessage = resBundle.getText("installDepartmentRequired");
        } else if (salesRecordData.orderDepartment.trim() === "") {
            errorMessage = resBundle.getText("orderDepartmentRequired");
        } else if (salesRecordData.product.trim() === "") {
            errorMessage = resBundle.getText("productRequired");
        }
        if (!errorMessage) {
            var quantityValid = ValidateUtils.validateIntegerGreaterThan0(salesRecordData.quantity);
            if (!quantityValid) {
                errorMessage = resBundle.getText("quantityRequired");
            }
        }
        if (!errorMessage) {
            return true;
        } else {
            UIUtils.showMessageToast(errorMessage);
            return false;
        }
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
            case "region":
                selectedKeysBackup.region = selectedKeys;
                break;
            case "province":
                selectedKeysBackup.province = selectedKeys;
                break;
            case "hospital":
                salesRecordData.hospital = getFirstOwnProperty(selectedKeys);
                selectedKeysBackup.hospital = selectedKeys;
                break;
            case "installDepartment":
                salesRecordData.installDepartment = getFirstOwnProperty(selectedKeys);
                selectedKeysBackup.installDepartment = selectedKeys;
                break;
            case "orderDepartment":
                salesRecordData.orderDepartment = getFirstOwnProperty(selectedKeys);
                selectedKeysBackup.orderDepartment = selectedKeys;
                break;
            case "product":
                salesRecordData.product = getFirstOwnProperty(selectedKeys);
                selectedKeysBackup.product = selectedKeys;
                break;
            default:
                break;
        }
    }

    function refreshUIForEditedRecord(recordToEdit) {
        salesRecordData.id = recordToEdit.id;
        salesRecordData.hospital = recordToEdit.hospital;
        salesRecordData.installDepartment = recordToEdit.installDepartment;
        salesRecordData.orderDepartment = recordToEdit.orderDepartment;
        salesRecordData.product = recordToEdit.product;
        salesRecordData.quantity = recordToEdit.quantity;
        salesRecordModel.refresh();

        selectedKeysBackup.region[recordToEdit.region] = recordToEdit.region;
        selectedKeysBackup.province[recordToEdit.province] = recordToEdit.province;
        selectedKeysBackup.hospital[recordToEdit.hospital] = recordToEdit.hospital;
        selectedKeysBackup.installDepartment[recordToEdit.installDepartment] = recordToEdit.installDepartment;
        selectedKeysBackup.orderDepartment[recordToEdit.orderDepartment] = recordToEdit.orderDepartment;
        selectedKeysBackup.product[recordToEdit.product] = recordToEdit.product;

        initSelectedItems(this);
    }

    var controller = Controller.extend("sales.records.CreateSalesRecord", {
        onInit: init,
        onFilterListClose: onFilterListClose,
        validateSalesRecord: validateSalesRecord,
        salesRecordModel: salesRecordModel,
        refreshUIForEditedRecord: refreshUIForEditedRecord
    });
    return controller;
});
