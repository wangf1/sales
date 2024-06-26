sap.ui.define([
    "sap/ui/core/mvc/Controller", "sap/ui/model/json/JSONModel", "sap/ui/model/Filter", "sap/ui/model/FilterOperator", "sales/common/AjaxUtils", "sales/common/i18nUtils",
    "sales/common/DateTimeUtils", "sales/common/ValidateUtils", "sales/common/UIUtils", "sales/common/SortUtils"
], function(Controller, JSONModel, Filter, FilterOperator, AjaxUtils, i18nUtils, DateTimeUtils, ValidateUtils, UIUtils, SortUtils) {
    "use strict";

    var viewModelData = {
        items: []
    };

    var oViewModel = UIUtils.createJsonModelWithSizeLimit10000(viewModelData);

    function setModelAndInitialSelection(items, initialSelectedItemNames) {
        oViewModel.setProperty("/items", items);
        initSelection(this, initialSelectedItemNames);
    }

    function init() {
        this.getView().setModel(oViewModel);
    }

    function sortTable(e) {
        var table = this.byId("theTable");
        var columnName = e.getSource().getCustomData()[0].getValue();
        var customDataDescending = e.getSource().getCustomData()[1];
        SortUtils.sortTable(table, columnName, customDataDescending);
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

    function initSelection(thisController, selectedItems) {
        if (!selectedItems || selectedItems.length === 0) {
            // if there is no selected item, should keep last time's selection
            return;
        }
        var table = thisController.byId("theTable")._oTable;
        table.removeSelections();
        var tableItems = table.getItems();
        tableItems.forEach(function(tableItem) {
            selectedItems.forEach(function(itemName) {
                var item = tableItem.getBindingContext().getObject()
                if (itemName === item.name) {
                    table.setSelectedItem(tableItem);
                }
            });
        });

    }

    var controller = Controller.extend("sales.datacollect.ItemSelect", {
        onInit: init,
        sortTable: sortTable,
        onQuickFilter: onQuickFilter,
        columnNames: [
            "name"
        ],
        setModelAndInitialSelection: setModelAndInitialSelection
    });
    return controller;
});
