jQuery.sap.declare("sales.common.ValidateUtils");
sales.common.ValidateUtils = (function() {
    "use strict";

    function validateIntegerGreaterOrEqualThan0(intString) {
        // Validate a string is a valid integer between 0 and max int32 2147483647
        var intString = intString + "";// the intString parameter maybe a int, must convert to string otherwise validation return false
        var intValue = parseInt(intString, 10);
        if (intString !== ("" + intValue) || intValue < 0 || intValue > 2147483647) {
            return false;
        } else {
            return true;
        }
    }

    var toExpose = {
        validateIntegerGreaterOrEqualThan0: validateIntegerGreaterOrEqualThan0
    };
    return toExpose;
})();
