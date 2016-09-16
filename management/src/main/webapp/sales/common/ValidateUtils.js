jQuery.sap.declare("sales.common.ValidateUtils");
sales.common.ValidateUtils = (function() {
    "use strict";

    function validateIntegerGreaterThan0(intString) {
        // Validate a string is a valid integer between 0 and max int32 2147483647
        var intValue = parseInt(intString, 10);
        if (intString !== ("" + intValue) || intValue < 0 || intValue > 2147483647) {
            return false;
        } else if (intValue === 0) {
            return false;
        } else {
            return true;
        }
    }

    var toExpose = {
        validateIntegerGreaterThan0: validateIntegerGreaterThan0
    };
    return toExpose;
})();
