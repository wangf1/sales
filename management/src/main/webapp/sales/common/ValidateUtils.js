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

    function validateIntegerGreaterThan0(intString) {
        // Validate a string is a valid integer between 0 and max int32 2147483647
        var intString = intString + "";// the intString parameter maybe a int, must convert to string otherwise validation return false
        var intValue = parseInt(intString, 10);
        if (intString !== ("" + intValue) || intValue < 0 || intValue > 2147483647) {
            return false;
        } else {
            return true;
        }
    }

    function isNumeric(n) {
        var float = parseFloat(n);
        return !isNaN(float) && isFinite(n);
    }

    function isGreaterOrEqualThan0(n) {
        if (!isNumeric(n)) {
            return false;
        }
        var float = parseFloat(n);
        return float >= 0;
    }

    function isGreaterThan0(n) {
        if (!isNumeric(n)) {
            return false;
        }
        var float = parseFloat(n);
        return float > 0;
    }

    function isEmptyString(string) {
        if (string === undefined || string === null) {
            return true;
        }
        if (string.trim) {
            if (string.trim() === "") {
                return true;
            }
        }
        return false;
    }

    function isNegativeNumber(n) {
        if (!isNumeric(n)) {
            return false;
        }
        var float = parseFloat(n);
        return float < 0;
    }

    var toExpose = {
        validateIntegerGreaterOrEqualThan0: validateIntegerGreaterOrEqualThan0,
        isGreaterOrEqualThan0: isGreaterOrEqualThan0,
        isEmptyString: isEmptyString,
        validateIntegerGreaterThan0: validateIntegerGreaterThan0,
        isNegativeNumber: isNegativeNumber,
        isGreaterThan0: isGreaterThan0
    };
    return toExpose;
})();
