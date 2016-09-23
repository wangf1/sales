sap.ui.define([
    "sap/ui/core/mvc/Controller", "sap/ui/model/json/JSONModel", "sap/ui/model/Filter", "sap/ui/model/FilterOperator", "sales/common/AjaxUtils", "sales/common/i18nUtils",
    "sales/common/DateTimeUtils", "sales/common/UIUtils", "sap/m/MessageBox", "sales/common/ObjectUtils", "sales/common/ValidateUtils"
], function(Controller, JSONModel, Filter, FilterOperator, AjaxUtils, i18nUtils, DateTimeUtils, UIUtils, MessageBox, ObjectUtils, ValidateUtils) {
    "use strict";

    var resBundle = i18nUtils.initAndGetResourceBundle();

    var columNames = [
        "region", "province", "manager", "salesPerson", "hospital", "hospitalLevel", "product", "installDepartment", "orderDepartment", "quantity", "date"
    ];

    var viewModelData = {
        salesRecords: [],
        regions: [],
        provinces: [],
        allProvinces: [],
        hospitals: [],
        allHospitals: [],
        departments: [],
        products: [],
        startAt: DateTimeUtils.firstDayOfCurrentMonth(),
        endAt: DateTimeUtils.today(),
        selectedRecords: [],
        inlineChangedRecords: []
    };

    var oViewModel = new JSONModel(viewModelData);

    function getSalesRecordsPromise() {
        var lastMonthFirstDayString = DateTimeUtils.firstDayOfPreviousMonth();
        var promise = AjaxUtils.ajaxCallAsPromise({
            method: "GET",
            url: "salesRecordsAdvanceSearch?startFrom=" + lastMonthFirstDayString,
            dataType: "json",
            contentType: "application/json"
        });
        return promise;
    }

    function setSalesRecordsModel() {
        var promise = getSalesRecordsPromise();
        promise.then(function(result) {
            oViewModel.setProperty("/salesRecords", result.data);
        });
    }

    function setRegionsModel() {
        var promise = AjaxUtils.ajaxCallAsPromise({
            method: "GET",
            url: "getRegionsByCurrentUser",
            dataType: "json",
            contentType: "application/json"
        });
        promise.then(function(result) {
            oViewModel.setProperty("/regions", result.data);
        });
    }
    function setProvincesModel() {
        var promise = AjaxUtils.ajaxCallAsPromise({
            method: "GET",
            url: "getProvincesByCurrentUser",
            dataType: "json",
            contentType: "application/json"
        });
        promise.then(function(result) {
            oViewModel.setProperty("/provinces", result.data);
            oViewModel.setProperty("/allProvinces", result.data);
        });
    }
    function setHospitalsModel() {
        var promise = AjaxUtils.ajaxCallAsPromise({
            method: "GET",
            url: "getHospitalsByCurrentUser",
            dataType: "json",
            contentType: "application/json"
        });
        promise.then(function(result) {
            oViewModel.setProperty("/hospitals", result.data);
            oViewModel.setProperty("/allHospitals", result.data);
        });
    }
    function setDepartmentsModel() {
        var promise = AjaxUtils.ajaxCallAsPromise({
            method: "GET",
            url: "listAllDepartments",
            dataType: "json",
            contentType: "application/json"
        });
        promise.then(function(result) {
            oViewModel.setProperty("/departments", result.data);
        });
    }
    function setProductsModel() {
        var promise = AjaxUtils.ajaxCallAsPromise({
            method: "GET",
            url: "listAllProducts",
            dataType: "json",
            contentType: "application/json"
        });
        promise.then(function(result) {
            oViewModel.setProperty("/products", result.data);
        });
    }

    var init = function() {
        // set i18n model
        i18nUtils.initAndGetResourceBundle();
        // populate view model date
        setSalesRecordsModel(this);
        setRegionsModel();
        setProvincesModel();
        setHospitalsModel();
        setDepartmentsModel();
        setProductsModel();
        // set model into view
        this.getView().setModel(oViewModel);
    };

    function onFilterRecords(e) {
        var table = this.byId("recordsTable");
        var binding = table.getBinding("items");
        if (!binding) {
            return;
        }
        var value = e.getSource().getValue();
        if (value.trim() === "") {
            binding.filter([]);
        } else {
            var fs = [];
            columNames.forEach(function(column) {
                if (column === "quantity" || column === "date") {
                    return;
                }
                fs.push(new sap.ui.model.Filter(column, sap.ui.model.FilterOperator.Contains, value));
            });
            var filters = new sap.ui.model.Filter(fs, false);
            binding.filter(filters);
        }
    }

    function doAdvanceSearchSalesRecord(hospitals, installDepartments, orderDepartments, products) {
        /*
        searchCriteriaFormatExample:
        {
            "productNames": [
                "PCT-Q"
            ],
            "hospitalNames": [
                "长征", "长海"
            ],
            "locationDepartmentNames": [
                "ICU"
            ],
            "orderDepartNames": [
                "ICU"
            ],
            "startAt": "2016-08-16",
            "endAt": "2016-09-17"
        }
        */
        var productNames = ObjectUtils.getAllOwnPropertyAsArray(products);
        var hospitalNames = ObjectUtils.getAllOwnPropertyAsArray(hospitals);
        var locationDepartmentNames = ObjectUtils.getAllOwnPropertyAsArray(installDepartments);
        var orderDepartNames = ObjectUtils.getAllOwnPropertyAsArray(orderDepartments);

        // Increase the endAt by 1 day, in order to search the newest record of user choosen endAt date
        var endAtDate = new Date(viewModelData.endAt);
        endAtDate.setDate(endAtDate.getDate() + 1);
        var endAt = DateTimeUtils.yyyyMMdd(endAtDate);

        var searchCriteria = {
            "productNames": productNames,
            "hospitalNames": hospitalNames,
            "locationDepartmentNames": locationDepartmentNames,
            "orderDepartNames": orderDepartNames,
            "startAt": viewModelData.startAt,
            "endAt": endAt
        };
        var promise = AjaxUtils.ajaxCallAsPromise({
            method: "POST",
            url: "salesRecordsAdvanceSearch",
            data: JSON.stringify(searchCriteria),
            dataType: "json",
            contentType: "application/json"
        });
        promise.then(function(result) {
            oViewModel.setProperty("/salesRecords", result.data);
        });
    }

    function onAdvanceSearchSalesRecord() {
// var selectedRegins = this.byId("filterRegion").getSelectedKeys();
// var selectedProvinces = this.byId("filterProvince").getSelectedKeys();
        var selectedHospitals = this.byId("filterHospital").getSelectedKeys();
        var selectedInstallDepartments = this.byId("filterInstallDepartment").getSelectedKeys();
        var selectedOrderDepartments = this.byId("filterOrderDepartment").getSelectedKeys();
        var selectedProducts = this.byId("filterProduct").getSelectedKeys();
        doAdvanceSearchSalesRecord(selectedHospitals, selectedInstallDepartments, selectedOrderDepartments, selectedProducts);
    }

    function removeSalesRecordFrom(salesRecords, savedRecordId) {
        var theIndex;
        for (var i = 0; i < salesRecords.length; i++) {
            var record = salesRecords[i];
            if (savedRecordId === record.id) {
                theIndex = i;
                break;
            }
        }
        if (theIndex !== undefined) {
            salesRecords.splice(theIndex, 1);
        }
    }

    function doSaveSalesRecord(salesRecord, isEditMode) {
        var promise = AjaxUtils.ajaxCallAsPromise({
            method: "POST",
            url: "saveSalesRecord",
            data: JSON.stringify(salesRecord),
            dataType: "json",
            contentType: "application/json"
        });
        promise.then(function(result) {
            var savedRecord = result.data;
            var message;
            if (savedRecord.alreadyExisting) {
                removeSalesRecordFrom(viewModelData.salesRecords, savedRecord.id);
            }
            // put the saved record at fist of the table
            viewModelData.salesRecords.unshift(savedRecord);
            if (savedRecord.alreadyExisting && !isEditMode) {
                message = resBundle.getText("salesRecordAlreadyExisting");
                UIUtils.showMessageToast(message);
            } else {
                message = resBundle.getText("salesOrderSaved");
                UIUtils.showMessageToast(message);
            }
            oViewModel.refresh();
        });
    }

    function onAddOrEditSalesRecord(e) {
        var view = sap.ui.view({
            type: sap.ui.core.mvc.ViewType.JS,
            viewName: "sales.records.CreateSalesRecord"
        });

        var action = e.getSource().getCustomData()[0].getValue();
        var isEditMode = action === "edit";
        var dlgTitle = resBundle.getText("add");
        if (isEditMode) {
            var toEdit = viewModelData.selectedRecords[0];
            view.getController().refreshUIForEditedRecord(toEdit);
            dlgTitle = resBundle.getText("edit");
        } else {
            // In add mode, must clear the salesRecord ID which remains last time
            var salesRecordToAdd = view.getModel("salesRecord").getData();
            salesRecordToAdd.id = 0;
        }

        var dlg = new sap.m.Dialog({
            contentWidth: "60%",
            contentHeight: "50%",
            title: dlgTitle,
            horizontalScrolling: false,
            verticalScrolling: true,
            content: [
                view
            ],
            afterClose: function() {
                dlg.destroy();
            }
        });
        dlg.addButton(new sap.m.Button({
            text: "{i18n>save}",
            press: function() {
                var salesRecord = view.getModel("salesRecord").getData();
                var valid = view.getController().validateSalesRecord();
                if (!valid) {
                    return;
                }
                doSaveSalesRecord(salesRecord, isEditMode);
                dlg.close();
            }
        }));
        dlg.addButton(new sap.m.Button({
            text: "{i18n>cancel}",
            press: function() {
                dlg.close();
            }
        }));
        dlg.setModel(oViewModel);
        view.getController().doInitialSelectFilter();
        dlg.open();
    }

    function doDeleteSalesRecords() {
        var recordIds = [];
        viewModelData.selectedRecords.forEach(function(record) {
            recordIds.push(record.id);
        });
        var promise = AjaxUtils.ajaxCallAsPromise({
            method: "POST",
            url: "deleteSalesRecords",
            data: JSON.stringify(recordIds),
            dataType: "json",
            contentType: "application/json"
        });
        promise.then(function(result) {
            var message = resBundle.getText("deleteSuccess");
            UIUtils.showMessageToast(message);
            var removedIds = result.data;
            removedIds.forEach(function(id) {
                removeSalesRecordFrom(viewModelData.salesRecords, id);
            });
            oViewModel.refresh();
        });
    }

    function onDeleteSalesRecord() {
        MessageBox.confirm(resBundle.getText("confirmDelete"), {
            title: resBundle.getText("confirm"),
            onClose: function(flgValue) {
                if (flgValue === sap.m.MessageBox.Action.OK) {
                    doDeleteSalesRecords();
                }
            }
        });
    }

    function onRecordTableSelectionChange() {
        var table = this.byId("recordsTable");
        var rows = table.getSelectedContexts();
        var selectedRecords = [];
        rows.forEach(function(row) {
            selectedRecords.push(row.getObject());
        });
        viewModelData.selectedRecords = selectedRecords;
        oViewModel.refresh();
    }

    function onResetSearchCondition() {
        this.byId("filterRegion").removeSelectedKeys();
        this.byId("filterProvince").removeSelectedKeys();
        this.byId("filterHospital").removeSelectedKeys();
        this.byId("filterInstallDepartment").removeSelectedKeys();
        this.byId("filterOrderDepartment").removeSelectedKeys();
        this.byId("filterProduct").removeSelectedKeys();
        this.byId("facetFilter").rerender();// call rerender otherwise the facetFilter cannot refresh

        viewModelData.startAt = DateTimeUtils.firstDayOfCurrentMonth();
        viewModelData.endAt = DateTimeUtils.today();
        oViewModel.refresh();
    }

    function onQuantityLiveChange(e) {
        var record = e.getSource().getBindingContext().getObject();
        var quantity = e.getSource().getValue();
        var quantityValid = ValidateUtils.validateIntegerGreaterThan0(quantity);
        if (!quantityValid) {
            e.getSource().setValueState(sap.ui.core.ValueState.Error);
            e.getSource().setValueStateText(resBundle.getText("quantityRequired"));
            removeSalesRecordFrom(viewModelData.inlineChangedRecords, record.id);
            return;
        } else {
            e.getSource().setValueState(sap.ui.core.ValueState.None);
        }
        // Remove before add, to avoid duplicate add
        removeSalesRecordFrom(viewModelData.inlineChangedRecords, record.id);
        /* The reason why create a new array rather than use existing array is if use existing array, the save button enable status binding "{=
         ${/inlineChangedRecords}.length>0 }" just not work.*/
        var allChangedRecords = [
            record
        ];
        viewModelData.inlineChangedRecords.forEach(function(item) {
            allChangedRecords.push(item)
        });
        viewModelData.inlineChangedRecords = allChangedRecords;
        oViewModel.refresh();
    }

    function onSaveAllSalesRecords() {
        var promise = AjaxUtils.ajaxCallAsPromise({
            method: "POST",
            url: "saveSalesRecords",
            data: JSON.stringify(viewModelData.inlineChangedRecords),
            dataType: "json",
            contentType: "application/json"
        });
        promise.then(function(result) {
            viewModelData.inlineChangedRecords = [];
            var message = resBundle.getText("salesOrderSaved");
            UIUtils.showMessageToast(message);
            oViewModel.refresh();
        });
    }

    function sortTable(e) {
        var table = this.byId("recordsTable");
        var binding = table.getBinding('items');
        var columnName = e.getSource().getCustomData()[0].getValue();
        var customDataDescending = e.getSource().getCustomData()[1];
        var oldDescendingValue = customDataDescending.getValue();
        var newDescendingValue = !oldDescendingValue;
        customDataDescending.setValue(newDescendingValue);
        var nameSorter = new sap.ui.model.Sorter(columnName, newDescendingValue);
        binding.sort([
            nameSorter
        ]);
    }

    var controller = Controller.extend("sales.records.ListInTable", {
        onInit: init,
        onFilterRecords: onFilterRecords,
        onAdvanceSearchSalesRecord: onAdvanceSearchSalesRecord,
        columNames: columNames,
        onAddOrEditSalesRecord: onAddOrEditSalesRecord,
        onDeleteSalesRecord: onDeleteSalesRecord,
        onRecordTableSelectionChange: onRecordTableSelectionChange,
        onResetSearchCondition: onResetSearchCondition,
        onQuantityLiveChange: onQuantityLiveChange,
        onSaveAllSalesRecords: onSaveAllSalesRecords,
        sortTable: sortTable
    });
    return controller;
});
