sap.ui.define([
    "sap/ui/core/mvc/Controller", "sap/ui/model/json/JSONModel", "sap/ui/model/Filter", "sap/ui/model/FilterOperator", "sales/common/AjaxUtils", "sales/common/i18nUtils",
    "sales/common/DateTimeUtils", "sales/common/ValidateUtils", "sales/common/UIUtils", "sales/common/ArrayUtils", "sap/m/MessageBox"
], function(Controller, JSONModel, Filter, FilterOperator, AjaxUtils, i18nUtils, DateTimeUtils, ValidateUtils, UIUtils, ArrayUtils, MessageBox) {
    "use strict";

    var resBundle = i18nUtils.initAndGetResourceBundle();

    var columnNames = [
        "name", "region", "salesPersons"
    ];

    var viewModelData = {
        provinces: [],
        selectedRecords: [],
        inlineChangedRecords: [],
        newAddedRecords: [],
        regions: []
    };

    var oViewModel = new JSONModel(viewModelData);

    var userSelectDialog;
    var provinceToEditSalesPersons;

    function setProvincesModel(thisController) {
        // must clear table selection status
        var table = thisController.byId("theTable");
        table.removeSelections();

        var promise = AjaxUtils.ajaxCallAsPromise({
            method: "GET",
            url: "getProvincesByCurrentUser",
            dataType: "json",
            contentType: "application/json"
        });
        promise.then(function(result) {
            oViewModel.setProperty("/provinces", result.data);
            setAllRegionsModel(result.data);
        });
    }

    function setAllRegionsModel(procinces) {
        var regions = [];
        procinces.forEach(function(province) {
            if (regions.indexOf(province.region) < 0) {
                regions.push(province.region);
            }
        });
        oViewModel.setProperty("/regions", regions);
    }

    function init() {
        setProvincesModel(this);
        this.getView().setModel(oViewModel);
    }

    function tableItemDataChanged(changedItem) {
        if (changedItem.id === undefined) {
            // Must refresh model in order UI can view salesPersons change
            oViewModel.refresh();
            // For new added one, do not use inlineChangedRecords array to track, but use newAddedRecords to track
            return;
        }
        // Remove before add, to avoid duplicate add
        ArrayUtils.removeFromById(viewModelData.inlineChangedRecords, changedItem.id);
        /* The reason why create a new array rather than use existing array is if use existing array, the save button enable status binding "{=
         ${/inlineChangedRecords}.length>0 }" just not work.*/
        var allChangedRecords = [
            changedItem
        ];
        viewModelData.inlineChangedRecords.forEach(function(item) {
            allChangedRecords.push(item)
        });
        viewModelData.inlineChangedRecords = allChangedRecords;
        // Must refresh model in order change button enabled status
        oViewModel.refresh();
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
        tableItemDataChanged(record);
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
            columnNames.forEach(function(column) {
                if (column == "salesPersons") {
                    // salesPersons is not string, cannot be search by "Contains" filter
                    return;
                }
                fs.push(new sap.ui.model.Filter(column, sap.ui.model.FilterOperator.Contains, value));
            });
            var filters = new sap.ui.model.Filter(fs, false);
            binding.filter(filters);
        }
    }

    function onAdd() {
        var newAdded = {
            name: "",
            region: ""
        };
        viewModelData.provinces.unshift(newAdded);

        /* The reason why create a new array rather than use existing array is if use existing array, the button enable status binding just not work.*/
        var allAddedRecords = [
            newAdded
        ];
        viewModelData.newAddedRecords.forEach(function(item) {
            allAddedRecords.push(item)
        });
        viewModelData.newAddedRecords = allAddedRecords;
        oViewModel.refresh();
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

    function onSaveAll() {
        var allNeedSave = [];
        var i;
        for (i = 0; i < viewModelData.inlineChangedRecords.length; i++) {
            var item = viewModelData.inlineChangedRecords[i];
            if (!validateEachItemBeforeSave(item)) {
                var message = resBundle.getText("before_save_validate_fail");
                UIUtils.showMessageToast(message);
                return;
            }
            allNeedSave.push(item);
        }
        for (i = 0; i < viewModelData.newAddedRecords.length; i++) {
            var item = viewModelData.newAddedRecords[i];
            if (!validateEachItemBeforeSave(item)) {
                var message = resBundle.getText("before_save_validate_fail");
                UIUtils.showMessageToast(message);
                return;
            }
            allNeedSave.push(item);
        }
        var promise = AjaxUtils.ajaxCallAsPromise({
            method: "POST",
            url: "saveProvinces",
            data: JSON.stringify(allNeedSave),
            dataType: "json",
            contentType: "application/json"
        });
        var that = this;
        promise.then(function(result) {
            setProvincesModel(that);// Refresh all province in order to have ID for new added record
            viewModelData.inlineChangedRecords = [];
            viewModelData.newAddedRecords = [];
            var message = resBundle.getText("save_success");
            UIUtils.showMessageToast(message);
            oViewModel.refresh();
        });
    }

    function doDeleteRecords() {
        var recordIds = [];
        viewModelData.selectedRecords.forEach(function(record) {
            if (record.id === undefined) {
                // Remove the new added record
                var i = viewModelData.provinces.indexOf(record);
                viewModelData.provinces.splice(i, 1);
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
            url: "deleteProvinces",
            data: JSON.stringify(recordIds),
            dataType: "json",
            contentType: "application/json"
        });
        promise.then(function(result) {
            var message = resBundle.getText("deleteSuccess");
            UIUtils.showMessageToast(message);
            var removedIds = result.data;
            removedIds.forEach(function(id) {
                ArrayUtils.removeFromById(viewModelData.provinces, id);
            });
            oViewModel.refresh();
        });
    }

    function onDelete() {
        MessageBox.confirm(resBundle.getText("confirmDelete"), {
            title: resBundle.getText("confirm"),
            onClose: function(flgValue) {
                if (flgValue === sap.m.MessageBox.Action.OK) {
                    doDeleteRecords();
                }
            }
        });
    }

    function clearSelectAndChangedData() {
        viewModelData.selectedRecords = [];
        viewModelData.inlineChangedRecords = [];
        viewModelData.newAddedRecords = [];
        oViewModel.refresh();
    }

    function onRefresh() {
        setProvincesModel(this);
        clearSelectAndChangedData
    }

    function onTableSelectionChange() {
        var table = this.byId("theTable");
        var rows = table.getSelectedContexts();
        var selectedRecords = [];
        rows.forEach(function(row) {
            selectedRecords.push(row.getObject());
        });
        viewModelData.selectedRecords = selectedRecords;
        oViewModel.refresh();
    }

    function sortTable(e) {
        var table = this.byId("theTable");
        var binding = table.getBinding("items");
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

    function onEditSalesPersonsDialogConfirm(oEvent) {
        var selectedUsers = [];
        var aContexts = oEvent.getParameter("selectedContexts");
        aContexts.forEach(function(oContext) {
            var user = oContext.getObject();
            selectedUsers.push(user.userName);
        });
        provinceToEditSalesPersons.salesPersons = selectedUsers;
        tableItemDataChanged(provinceToEditSalesPersons);
    }

    function onEditSalesPersons(e) {
        provinceToEditSalesPersons = e.getSource().getBindingContext().getObject();
        if (!userSelectDialog) {
            userSelectDialog = sap.ui.view({
                type: sap.ui.core.mvc.ViewType.JS,
                viewName: "sales.basicData.UserSelect"
            });
            // Should attach confirm event listener ONLY one time
            userSelectDialog.dialog.attachConfirm(function(selectConfirmEvent) {
                onEditSalesPersonsDialogConfirm(selectConfirmEvent);
            });
        }
        userSelectDialog.getController().initSelection(provinceToEditSalesPersons.salesPersons);
        // Must call addDependent otherwise the dialog will cannot access the i18n model
        this.getView().addDependent(userSelectDialog);
        userSelectDialog.dialog.open();
    }

    var controller = Controller.extend("sales.basicData.Province", {
        onInit: init,
        onCellLiveChange: onCellLiveChange,
        onQuickFilter: onQuickFilter,
        onAdd: onAdd,
        onDelete: onDelete,
        onSaveAll: onSaveAll,
        onRefresh: onRefresh,
        onTableSelectionChange: onTableSelectionChange,
        columnNames: columnNames,
        sortTable: sortTable,
        onEditSalesPersons: onEditSalesPersons
    });
    return controller;
});
