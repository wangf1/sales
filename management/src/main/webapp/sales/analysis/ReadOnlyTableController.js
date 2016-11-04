sap.ui.define([
    "sap/ui/core/mvc/Controller", "sap/ui/model/json/JSONModel", "sap/ui/model/Filter", "sap/ui/model/FilterOperator", "sales/common/AjaxUtils", "sales/common/i18nUtils",
    "sales/common/DateTimeUtils", "sales/common/ValidateUtils", "sales/common/UIUtils", "sales/common/ArrayUtils", "sap/m/MessageBox", "sales/common/SortUtils"
], function(Controller, JSONModel, Filter, FilterOperator, AjaxUtils, i18nUtils, DateTimeUtils, ValidateUtils, UIUtils, ArrayUtils, MessageBox, SortUtils) {
    "use strict";

    var resBundle = i18nUtils.initAndGetResourceBundle();

    var viewModelData = {
        tableData: [],
    };

    var oViewModel = new JSONModel(viewModelData);

    function setTableModel() {
        // must clear table selection status
        var table = this.byId("theTable");
        table.removeSelections();

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
        this.getView().setModel(oViewModel);
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

    function onRefresh() {
        this.setTableModel();
        oViewModel.refresh();
    }

    function sortTable(e) {
        var table = this.byId("theTable");
        var columnName = e.getSource().getCustomData()[0].getValue();
        var customDataDescending = e.getSource().getCustomData()[1];
        SortUtils.sortTable(table, columnName, customDataDescending);
    }

    function afterShow() {
        this.onRefresh();
    }

    var controller = Controller.extend("sales.analysis.ReadOnlyTableController", {
        urlForListAll: "",
        columnNames: [],
        oViewModel: oViewModel,
        onInit: init,
        onQuickFilter: onQuickFilter,
        onRefresh: onRefresh,
        sortTable: sortTable,
        afterShow: afterShow,
        setTableModel: setTableModel
    });
    return controller;
});
