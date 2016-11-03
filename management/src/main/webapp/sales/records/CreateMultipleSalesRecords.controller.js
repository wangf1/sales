sap.ui.define([
    "sap/ui/core/mvc/Controller", "sap/ui/model/json/JSONModel", "sap/ui/model/Filter", "sap/ui/model/FilterOperator", "sales/common/AjaxUtils", "sales/common/i18nUtils",
    "sales/common/DateTimeUtils", "sales/common/ValidateUtils", "sales/common/UIUtils"
], function(Controller, JSONModel, Filter, FilterOperator, AjaxUtils, i18nUtils, DateTimeUtils, ValidateUtils, UIUtils) {
    "use strict";

    var salesRecordsData = {
        id: 0,
        region: "",
        province: "",
        hospital: "",
        installDepartment: "",
        product: "",
        orderDepartments: [
        // element format:
        // {
        // department: "",
        // quantity: 0
        // }
        ],
        selectedOrderDepartments: []
    };

    var salesRecordsModel = new JSONModel(salesRecordsData);

    function init() {
        this.getView().setModel(salesRecordsModel, "salesRecordsData");
    }

    function validateSalesRecords() {
        var resBundle = i18nUtils.initAndGetResourceBundle();
        var errorMessage;
        if (ValidateUtils.isEmptyString(salesRecordsData.hospital)) {
            errorMessage = resBundle.getText("hospitalRequired");
        } else if (ValidateUtils.isEmptyString(salesRecordsData.installDepartment)) {
            errorMessage = resBundle.getText("installDepartmentRequired");
        } else if (ValidateUtils.isEmptyString(salesRecordsData.product)) {
            errorMessage = resBundle.getText("productRequired");
        }
        if (salesRecordsData.orderDepartments.length === 0) {
            errorMessage = resBundle.getText("order_department_table_empty");
        }
        if (!errorMessage) {
            for (var i = 0; i < salesRecordsData.orderDepartments.length; i++) {
                var orderDepartment = salesRecordsData.orderDepartments[0];
                if (ValidateUtils.isEmptyString(orderDepartment.orderDepartment)) {
                    errorMessage = resBundle.getText("orderDepartmentRequired");
                    break;
                }
                var quantityValid = ValidateUtils.validateIntegerGreaterOrEqualThan0(orderDepartment.quantity);
                if (!quantityValid) {
                    errorMessage = resBundle.getText("quantityRequired");
                    break;
                }
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

    function filterProvinceAndHospital(thisController) {
        filterProvinceByRegion(thisController);
        filterHospitalByProvince(thisController);
    }

    function filterProvinceByRegion(thisController) {
        var viewModel = thisController.getView().getModel();
        var region = salesRecordsData.region;
        if (region === "") {
            region = viewModel.getData().regions[0];
        }
        var filteredProvinces = [];
        viewModel.getData().allProvinces.forEach(function(province) {
            if (province.region === region) {
                filteredProvinces.push(province);
            }
        });
        viewModel.getData().provinces = filteredProvinces;
        // Set the province to a new province in order to filterHospitalByProvince() function can works against new province
        salesRecordsData.province = filteredProvinces[0].name;
        viewModel.refresh();
    }

    function filterHospitalByProvince(thisController) {
        var viewModel = thisController.getView().getModel();
        var province = salesRecordsData.province;
        if (province === "") {
            province = viewModel.getData().provinces[0].name;
        }
        var filteredHospitals = [];
        viewModel.getData().allHospitals.forEach(function(hospital) {
            if (hospital.province === province) {
                filteredHospitals.push(hospital);
            }
        });
        viewModel.getData().hospitals = filteredHospitals;
        if (filteredHospitals[0]) {
            salesRecordsData.hospital = filteredHospitals[0].name;
        }
        viewModel.refresh();
    }

    function doInitialSelectFilter() {
        filterProvinceAndHospital(this);
    }

    function onRegionChanged() {
        filterProvinceByRegion(this);
        filterHospitalByProvince(this);
    }

    function onProvinceChanged() {
        filterHospitalByProvince(this);
    }

    function onAddOrderDepartment() {
        var table = this.byId("theTable");
        table.removeSelections();
        var newAdded = {};
        var departments = this.getView().getModel().getData().departments;
        newAdded["orderDepartment"] = departments[0].name;
        salesRecordsData.orderDepartments.unshift(newAdded);
        salesRecordsModel.refresh();
    }

    function onTableSelectionChange() {
        var table = this.byId("theTable");
        var rows = table.getSelectedContexts();
        var selectedRecords = [];
        rows.forEach(function(row) {
            selectedRecords.push(row.getObject());
        });
        salesRecordsData.selectedOrderDepartments = selectedRecords;
        salesRecordsModel.refresh();
    }

    function onDeleteOrderDepartment() {
        salesRecordsData.selectedOrderDepartments.forEach(function(element) {
            var index = salesRecordsData.orderDepartments.indexOf(element);
            if (index > -1) {
                salesRecordsData.orderDepartments.splice(index, 1);
            }
        });
        var table = this.byId("theTable");
        table.removeSelections();
        salesRecordsModel.refresh();
    }

    var controller = Controller.extend("sales.records.CreateMultipleSalesRecords", {
        onInit: init,
        validateSalesRecords: validateSalesRecords,
        salesRecordsModel: salesRecordsModel,
        doInitialSelectFilter: doInitialSelectFilter,
        onRegionChanged: onRegionChanged,
        onProvinceChanged: onProvinceChanged,
        columnNames: [
            "orderDepartment", "quantity"
        ],
        onAddOrderDepartment: onAddOrderDepartment,
        onTableSelectionChange: onTableSelectionChange,
        onDeleteOrderDepartment: onDeleteOrderDepartment

    });
    return controller;
});
