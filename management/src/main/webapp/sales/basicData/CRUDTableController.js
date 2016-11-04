sap.ui.define([
    "sap/ui/core/mvc/Controller", "sap/ui/model/json/JSONModel", "sap/ui/model/Filter", "sap/ui/model/FilterOperator", "sales/common/AjaxUtils", "sales/common/i18nUtils",
    "sales/common/DateTimeUtils", "sales/common/ValidateUtils", "sales/common/UIUtils", "sales/common/ArrayUtils", "sap/m/MessageBox", "sales/common/SortUtils"
], function(Controller, JSONModel, Filter, FilterOperator, AjaxUtils, i18nUtils, DateTimeUtils, ValidateUtils, UIUtils, ArrayUtils, MessageBox, SortUtils) {
    "use strict";

    var resBundle = i18nUtils.initAndGetResourceBundle();

    var viewModelData = {
        tableData: [],
        selectedRecords: [],
        inlineChangedRecords: [],
        newAddedRecords: [],
        firstDayOfCurrentMonth: Date.parse(DateTimeUtils.firstDayOfCurrentMonth())
    };

    var oViewModel = new JSONModel(viewModelData);

    function setTableModel() {
        var promise = AjaxUtils.ajaxCallAsPromise({
            method: "GET",
            url: this.urlForListAll,
            dataType: "json",
            contentType: "application/json"
        });
        var promiseAfterSetTableModel = promise.then(function(result) {
            oViewModel.setProperty("/tableData", result.data);
        });
        return promiseAfterSetTableModel;
    }

    function init() {
// setTableModel(this);
        this.getView().setModel(oViewModel);
    }

    function setValueToModelForComboBox(e) {
        // Cannot bind combobox value property to model, since UI will have problem when input append string based on exiting selected item.
        // So I have not two-way bind for combobox, so I should set explictly set the value to model.
        var comboBox = e.getSource();
        if (comboBox.getMetadata().getName() !== sap.m.ComboBox.getMetadata().getName()) {
            // Only set value to model for combobox
            return;
        }
        var record = comboBox.getBindingContext().getObject();
        var propertyName = comboBox.getBindingPath("selectedKey");
        record[propertyName] = comboBox.getValue();
    }

    function onCellLiveChange(e) {
        setValueToModelForComboBox(e);

        var record = e.getSource().getBindingContext().getObject();
        if (record.id === undefined) {
            // For new added one, do not use inlineChangedRecords array to track, but use newAddedRecords to track
            return;
        }
        // Remove before add, to avoid duplicate add
        ArrayUtils.removeFromById(viewModelData.inlineChangedRecords, record.id);
        /* The reason why create a new array rather than use existing array is if use existing array, the save button enable status binding "{=
         ${/inlineChangedRecords}.length>0 }" just not work.*/
        var allChangedRecords = [
            record
        ];
        viewModelData.inlineChangedRecords.forEach(function(item) {
            allChangedRecords.push(item);
        });
        viewModelData.inlineChangedRecords = allChangedRecords;
        oViewModel.refresh();
    }

    function onQuickFilter(e) {
        var table = this.byId("theTable");
        var binding = table.getBinding("items");
        if (!binding) {
            return;
        }
        var value = e.getSource().getValue();
        if (value.trim() === "") {
            binding.filter([]);
        } else {
            var fs = [];
            this.columnNames.forEach(function(column) {
                fs.push(new sap.ui.model.Filter(column, sap.ui.model.FilterOperator.Contains, value));
            });
            var filters = new sap.ui.model.Filter(fs, false);
            binding.filter(filters);
        }
    }

    function onAdd() {
        var newAdded = {};
        this.columnNames.forEach(function(property) {
            newAdded[property] = "";
        });
        viewModelData.tableData.unshift(newAdded);

        /* The reason why create a new array rather than use existing array is if use existing array, the button enable status binding just not work.*/
        var allAddedRecords = [
            newAdded
        ];
        viewModelData.newAddedRecords.forEach(function(item) {
            allAddedRecords.push(item);
        });
        viewModelData.newAddedRecords = allAddedRecords;
        oViewModel.refresh();
        return newAdded;
    }

    function validateEachPropertyNotEmpty(object) {
        for ( var key in object) {
            if (!object.hasOwnProperty(key)) {
                continue;
            }
            var value = object[key];
            if (!value) {
                return false;
            }
            if (value.trim) {
                if (value.trim() === "") {
                    return false;
                }
            }
        }
        return true;
    }

    function validateEachItemBeforeSave(object) {
        var isValid = validateEachPropertyNotEmpty(object);
        return isValid;
    }

    function validateBeforeSaveShowMessageToast(object) {
        var isValid = this.validateEachItemBeforeSave(object);
        if (!isValid) {
            var message = resBundle.getText("before_save_validate_fail");
            UIUtils.showMessageToast(message);
        }
        return isValid;
    }

    function onSaveAll() {
        var allNeedSave = [];
        var i;
        for (i = 0; i < viewModelData.inlineChangedRecords.length; i++) {
            var item = viewModelData.inlineChangedRecords[i];
            if (!this.validateBeforeSaveShowMessageToast(item)) {
                return;
            }
            allNeedSave.push(item);
        }
        for (i = 0; i < viewModelData.newAddedRecords.length; i++) {
            var item = viewModelData.newAddedRecords[i];
            if (!this.validateBeforeSaveShowMessageToast(item)) {
                return;
            }
            allNeedSave.push(item);
        }
        var promise = AjaxUtils.ajaxCallAsPromise({
            method: "POST",
            url: this.urlForSaveAll,
            data: JSON.stringify(allNeedSave),
            dataType: "json",
            contentType: "application/json"
        });
        var that = this;
        promise.then(function(result) {
            that.onRefresh();// Refresh all in order to have ID for new added record
            var message = resBundle.getText("save_success");
            UIUtils.showMessageToast(message);
        });
    }

    function doDeleteRecords(thisController) {
        var recordIds = [];
        viewModelData.selectedRecords.forEach(function(record) {
            if (record.id === undefined) {
                // Remove the new added record
                var i = viewModelData.tableData.indexOf(record);
                viewModelData.tableData.splice(i, 1);
                i = viewModelData.newAddedRecords.indexOf(record);
                viewModelData.newAddedRecords.splice(i, 1);
                return;
            }
            recordIds.push(record.id);
        });
        if (recordIds.length === 0) {
            // No need server side delete
            var message = resBundle.getText("deleteSuccess");
            UIUtils.showMessageToast(message);
            oViewModel.refresh();
            return;
        }
        var promise = AjaxUtils.ajaxCallAsPromise({
            method: "POST",
            url: thisController.urlForDeleteAll,
            data: JSON.stringify(recordIds),
            dataType: "json",
            contentType: "application/json"
        });
        promise.then(function(result) {
            var message = resBundle.getText("deleteSuccess");
            UIUtils.showMessageToast(message);
            thisController.onRefresh();
        });
    }

    function onDelete() {
        var that = this;
        MessageBox.confirm(resBundle.getText("confirmDelete"), {
            title: resBundle.getText("confirm"),
            onClose: function(flgValue) {
                if (flgValue === sap.m.MessageBox.Action.OK) {
                    doDeleteRecords(that);
                }
            }
        });
    }

    function clearSelectAndChangedData() {
        // must clear table selection status
        var table = this.byId("theTable");
        table.removeSelections();
        // clear models
        viewModelData.selectedRecords = [];
        viewModelData.inlineChangedRecords = [];
        viewModelData.newAddedRecords = [];
        oViewModel.refresh();
    }

    function onRefresh() {
        this.setTableModel();
        this.clearSelectAndChangedData();
        oViewModel.refresh();
    }

    function isSelectedRecordsDeletable() {
        // Only allow delete record of current month
        viewModelData.isSelectedRecordsEditable = true;
        var isAdminRole = sap.ui.getCore().getModel("permissionModel").getProperty("/user/create");
        if (!isAdminRole) {
            // Admin user can edit any record!
            viewModelData.selectedRecords.forEach(function(record) {
                var isInCurrentMonth = Date.parse(record.date) >= viewModelData.firstDayOfCurrentMonth;
                if (!isInCurrentMonth) {
                    viewModelData.isSelectedRecordsEditable = false;
                }
            });
        }
    }

    function onTableSelectionChange() {
        var table = this.byId("theTable");
        var rows = table.getSelectedContexts();
        var selectedRecords = [];
        rows.forEach(function(row) {
            selectedRecords.push(row.getObject());
        });
        viewModelData.selectedRecords = selectedRecords;

        this.isSelectedRecordsDeletable();
        oViewModel.refresh();
    }

    function sortTable(e) {
        var table = this.byId("theTable");
        var columnName = e.getSource().getCustomData()[0].getValue();
        var customDataDescending = e.getSource().getCustomData()[1];
        SortUtils.sortTable(table, columnName, customDataDescending);
    }

    function afterShow() {
        // Get model data in afterShow method instead of init method, to make sure refresh data every time when the tab selected
        // The major reason is multiple view share same controller prototype, the viewModel is different,
        // so must rebuilt the view model when the view changed
        this.onRefresh();
    }

    function validateHospital(object) {
        var hospitalsOfProvince = object["filteredHospitals"];
        var selectedHospital = object["hospital"];
        var hospitalValid = false;
        for (var i = 0; i < hospitalsOfProvince.length; i++) {
            if (hospitalsOfProvince[i].name === selectedHospital) {
                hospitalValid = true;
            }
        }
        if (!hospitalValid) {
            var message = resBundle.getText("before_save_hospital_invalid");
            UIUtils.showMessageToast(message);
        }
        return hospitalValid;
    }

    var controller = Controller.extend("sales.basicData.CRUDTableController", {
        urlForListAll: "",
        urlForSaveAll: "",
        urlForDeleteAll: "",
        columnNames: [],
        oViewModel: oViewModel,
        onInit: init,
        onCellLiveChange: onCellLiveChange,
        onQuickFilter: onQuickFilter,
        onAdd: onAdd,
        onDelete: onDelete,
        onSaveAll: onSaveAll,
        onRefresh: onRefresh,
        onTableSelectionChange: onTableSelectionChange,
        sortTable: sortTable,
        afterShow: afterShow,
        setTableModel: setTableModel,
        validateEachItemBeforeSave: validateEachItemBeforeSave,
        clearSelectAndChangedData: clearSelectAndChangedData,
        isSelectedRecordsDeletable: isSelectedRecordsDeletable,
        validateBeforeSaveShowMessageToast: validateBeforeSaveShowMessageToast,
        validateHospital: validateHospital
    });
    return controller;
});
