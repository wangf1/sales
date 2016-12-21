sap.ui.define([
    "sap/ui/core/mvc/Controller", "sap/ui/model/json/JSONModel", "sap/ui/model/Filter", "sap/ui/model/FilterOperator", "sales/common/AjaxUtils", "sales/common/i18nUtils",
    "sales/common/DateTimeUtils", "sales/common/ValidateUtils", "sales/common/UIUtils", "sales/common/SortUtils"
], function(Controller, JSONModel, Filter, FilterOperator, AjaxUtils, i18nUtils, DateTimeUtils, ValidateUtils, UIUtils, SortUtils) {
    "use strict";

    var viewModelData = {
        columnVisiableArray: []
    };

    var oViewModel = new JSONModel(viewModelData);
    /**
     * Input like this:
            columnVisiableModel = {
                "date": true,
                "region": true,
                "province": true,
                "salesPersonFullName": true,
                "hospital": true,
                "department": true,
                "product": true,
                "purpose": true,
                "status": true,
                "columnsNeedInOneCell": true
            };
     @ Output like this:
            [
                {name: "date", value: true},
                {name: "region", value: true},
                .................
            ];
     */
    function transformColumnVisiableModel(columnVisiableModel) {
        var resultModel = [];
        Object.keys(columnVisiableModel).forEach(function(key) {
            resultModel.push({
                name: key,
                value: columnVisiableModel[key]
            });
        });
        return resultModel;
    }

    function initSelection(columnVisiableModel, thisController) {
        var table = thisController.byId("theTable")._oTable;
        table.removeSelections();
        var tableItems = table.getItems();
        tableItems.forEach(function(item) {
            var columnData = item.getBindingContext().getObject()
            if (columnVisiableModel[columnData.name] === true) {
                table.setSelectedItem(item);
            }
        });

    }

    function setTableModel(columnVisiableModel) {
        var columnVisiableArray = transformColumnVisiableModel(columnVisiableModel);
        oViewModel.setProperty("/columnVisiableArray", columnVisiableArray);
        initSelection(columnVisiableModel, this);
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

    var controller = Controller.extend("sales.datacollect.ColumnSelect", {
        setTableModel: setTableModel,
        onInit: init,
        sortTable: sortTable,
        onQuickFilter: onQuickFilter,
        columnNames: [
            "name"
        ]
    });
    return controller;
});
