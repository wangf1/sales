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

    var salesRecordModel = UIUtils.createJsonModelWithSizeLimit10000(salesRecordData);

    function init() {
        this.getView().setModel(salesRecordModel, "salesRecord");
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
            var quantityValid = ValidateUtils.validateIntegerGreaterOrEqualThan0(salesRecordData.quantity);
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

    function refreshUIForEditedRecord(recordToEdit) {
        salesRecordData.id = recordToEdit.id;
        salesRecordData.region = recordToEdit.region;
        salesRecordData.province = recordToEdit.province;
        salesRecordData.hospital = recordToEdit.hospital;
        salesRecordData.installDepartment = recordToEdit.installDepartment;
        salesRecordData.orderDepartment = recordToEdit.orderDepartment;
        salesRecordData.product = recordToEdit.product;
        salesRecordData.quantity = recordToEdit.quantity;
        salesRecordModel.refresh();
    }

    function filterProvinceAndHospital(thisController) {
        filterProvinceByRegion(thisController);
        filterHospitalByProvince(thisController);
    }

    function filterProvinceByRegion(thisController) {
        var viewModel = thisController.getView().getModel();
        var region = salesRecordData.region;
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

        var currentProvinceWithinRegion = false;
        for (var i = 0; i < filteredProvinces.length; i++) {
            if (filteredProvinces[i].name === salesRecordData.province) {
                currentProvinceWithinRegion = true;
                break;
            }
        }
        if (!currentProvinceWithinRegion) {
            // only set province to first one when the province is not in current region
            if (filteredProvinces[0]) {
                salesRecordData.province = filteredProvinces[0].name;
            }
        }
        viewModel.refresh();
    }

    function filterHospitalByProvince(thisController) {
        var viewModel = thisController.getView().getModel();
        var province = salesRecordData.province;
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
        var currentHospitalWithinProvince = false;
        for (var i = 0; i < filteredHospitals.length; i++) {
            if (filteredHospitals[i].name === salesRecordData.hospital) {
                currentHospitalWithinProvince = true;
                break;
            }
        }
        if (!currentHospitalWithinProvince) {
            // only set hospital to first one when the hospital is not in current province
            if (filteredHospitals[0]) {
                salesRecordData.hospital = filteredHospitals[0].name;
            }
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

    var controller = Controller.extend("sales.records.CreateSalesRecord", {
        onInit: init,
        validateSalesRecord: validateSalesRecord,
        salesRecordModel: salesRecordModel,
        refreshUIForEditedRecord: refreshUIForEditedRecord,
        doInitialSelectFilter: doInitialSelectFilter,
        onRegionChanged: onRegionChanged,
        onProvinceChanged: onProvinceChanged

    });
    return controller;
});
