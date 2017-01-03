sap.ui.define([
    "sap/ui/core/mvc/Controller", "sap/ui/model/json/JSONModel", "sap/ui/model/Filter", "sap/ui/model/FilterOperator", "sales/common/AjaxUtils", "sales/common/i18nUtils",
    "sales/common/DateTimeUtils", "sales/common/ValidateUtils", "sales/common/UIUtils", "sales/common/SortUtils"
], function(Controller, JSONModel, Filter, FilterOperator, AjaxUtils, i18nUtils, DateTimeUtils, ValidateUtils, UIUtils, SortUtils) {
    "use strict";

    var viewModelData = {
        products: []
    };

    var oViewModel = UIUtils.createJsonModelWithSizeLimit10000(viewModelData);

    function setTableModel() {
        var promise = AjaxUtils.ajaxCallAsPromise({
            method: "GET",
            url: "listNormalProducts",
            dataType: "json",
            contentType: "application/json"
        });
        var promiseAfterSetTableModel = promise.then(function(result) {
            oViewModel.setProperty("/products", result.data);
        });
        return promiseAfterSetTableModel;
    }

    function init() {
        setTableModel();
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

    function initSelection(selectedProducts) {
        if (!selectedProducts || selectedProducts.length === 0) {
            // if there is no selected product, should keep last time's selection
            return;
        }
        var table = this.byId("theTable")._oTable;
        table.removeSelections();
        var tableItems = table.getItems();
        tableItems.forEach(function(item) {
            selectedProducts.forEach(function(productName) {
                var product = item.getBindingContext().getObject()
                if (productName === product.name) {
                    table.setSelectedItem(item);
                }
            });
        });

    }

    var controller = Controller.extend("sales.datacollect.ProductSelect", {
        onInit: init,
        sortTable: sortTable,
        onQuickFilter: onQuickFilter,
        columnNames: [
            "name"
        ],
        initSelection: initSelection
    });
    return controller;
});
