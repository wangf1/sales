jQuery.sap.require("sap.ui.model.Sorter");

jQuery.sap.declare("sales.common.SortUtils");

sales.common.SortUtils = (function() {
    "use strict";

    function intAwareComparator(a, b) {
        var result;
        // Use Number instead of parseFloat for strict parse,
        // in order to make data string like 2019-01-01 not parse as 2019.
        var numA = Number(a);
        var numB = Number(b);
        if (!isNaN(numA) && !isNaN(numB)) {
        	// Reason of sort by number is, string "100" should greater than "99"
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
