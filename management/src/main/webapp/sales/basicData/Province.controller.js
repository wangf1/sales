sap.ui.define([
    "sap/ui/core/mvc/Controller", "sap/ui/model/json/JSONModel", "sap/ui/model/Filter", "sap/ui/model/FilterOperator", "sales/common/AjaxUtils", "sales/common/i18nUtils",
    "sales/common/DateTimeUtils", "sales/common/ValidateUtils", "sales/common/UIUtils", "sales/common/ArrayUtils", "sap/m/MessageBox"
], function(Controller, JSONModel, Filter, FilterOperator, AjaxUtils, i18nUtils, DateTimeUtils, ValidateUtils, UIUtils, ArrayUtils, MessageBox) {
    "use strict";

    var resBundle = i18nUtils.initAndGetResourceBundle();

    var columnNames = [
        "name", "region"
    ];

    var viewModelData = {
        provinces: [],
        selectedRecords: [],
        inlineChangedRecords: [],
        newAddedRecords: []
    };

    var oViewModel = new JSONModel(viewModelData);

    function setProvincesModel(thisController) {
        // must clear table selection status
        var table = thisController.byId("theTable");
        table.removeSelections();

        var promise = AjaxUtils.ajaxCallAsPromise({
            method: "GET",
            url: "listAllProvinces",
            dataType: "json",
            contentType: "application/json"
        });
        promise.then(function(result) {
            oViewModel.setProperty("/provinces", result.data);
        });
    }

    function init() {
        setProvincesModel(this);
        this.getView().setModel(oViewModel);
    }

    function onCellLiveChange(e) {
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
            allChangedRecords.push(item)
        });
        viewModelData.inlineChangedRecords = allChangedRecords;
        oViewModel.refresh();
    }

    function onQuickFilter() {
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
        viewModelData.provinces.push(newAdded);

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

    function onSaveAll() {
        var allNeedSave = [];
        viewModelData.inlineChangedRecords.forEach(function(item) {
            if (item.name.trim() === "" || item.region.trim() === "") {
                // do very basic validate
                return;
            }
            allNeedSave.push(item);
        });
        viewModelData.newAddedRecords.forEach(function(item) {
            if (item.name.trim() === "" || item.region.trim() === "") {
                // do very basic validate
                return;
            }
            allNeedSave.push(item);
        });
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

    function onRefresh() {
        setProvincesModel(this);
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
        sortTable: sortTable
    });
    return controller;
});
