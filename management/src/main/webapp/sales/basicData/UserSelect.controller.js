sap.ui.define([
    "sap/ui/core/mvc/Controller", "sap/ui/model/json/JSONModel", "sap/ui/model/Filter", "sap/ui/model/FilterOperator", "sales/common/AjaxUtils", "sales/common/i18nUtils",
    "sales/common/DateTimeUtils", "sales/common/ValidateUtils", "sales/common/UIUtils"
], function(Controller, JSONModel, Filter, FilterOperator, AjaxUtils, i18nUtils, DateTimeUtils, ValidateUtils, UIUtils) {
    "use strict";

    var viewModelData = {
        users: []
    };

    var oViewModel = new JSONModel(viewModelData);

    function setTableModel() {
        var promise = AjaxUtils.ajaxCallAsPromise({
            method: "GET",
            url: "listAllUsers",
            dataType: "json",
            contentType: "application/json"
        });
        var promiseAfterSetTableModel = promise.then(function(result) {
            oViewModel.setProperty("/users", result.data);
        });
        return promiseAfterSetTableModel;
    }

    function init() {
        setTableModel();
        this.getView().setModel(oViewModel);
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

    function onQuickFilter(e) {
        var table = this.byId("theTable");
        var binding = table.getBinding("items");
        if (!binding) {
            return;
        }
        var value = e.getParameter("value");
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

    function initSelection(selectedUsers) {
        if (!selectedUsers || selectedUsers.length === 0) {
            // if there is no selected users, should keep last time's selection
            return;
        }
        var table = this.byId("theTable")._oTable;
        table.removeSelections();
        var tableItems = table.getItems();
        tableItems.forEach(function(item) {
            selectedUsers.forEach(function(userName) {
                var user = item.getBindingContext().getObject()
                if (userName === user.userName) {
                    table.setSelectedItem(item);
                }
            });
        });

    }

    var controller = Controller.extend("sales.basicData.UserSelect", {
        onInit: init,
        sortTable: sortTable,
        onQuickFilter: onQuickFilter,
        columnNames: [
            "userName", "password", "firstName"
        ],
        initSelection: initSelection
    });
    return controller;
});
