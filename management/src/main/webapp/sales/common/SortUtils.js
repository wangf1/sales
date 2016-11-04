jQuery.sap.require("sap.ui.model.Sorter");

jQuery.sap.declare("sales.common.SortUtils");

sales.common.SortUtils = (function() {
    "use strict";

    function intAwareComparator(a, b) {
        var result;
        var numA = parseFloat(a);
        var numB = parseFloat(b);
        if (!isNaN(numA) && !isNaN(numB)) {
            result = sap.ui.model.Sorter.defaultComparator(numA, numB);
        } else {
            result = sap.ui.model.Sorter.defaultComparator(a, b);
        }
        return result;
    }

    function sortTable(table, columnName, customDataDescending) {
        var binding = table.getBinding("items");
        var oldDescendingValue = customDataDescending.getValue();
        var newDescendingValue = !oldDescendingValue;
        customDataDescending.setValue(newDescendingValue);
        var nameSorter = new sap.ui.model.Sorter(columnName, newDescendingValue, false, intAwareComparator);
        binding.sort([
            nameSorter
        ]);
    }

    var toExpose = {
        intAwareComparator: intAwareComparator,
        sortTable: sortTable
    };
    return toExpose;
})();
