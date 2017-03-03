jQuery.sap.declare("sales.common.ArrayUtils");
sales.common.ArrayUtils = (function() {
    "use strict";

    function removeFromById(arrayOfObjectWithID, idToRemove) {
        var theIndex;
        for (var i = 0; i < arrayOfObjectWithID.length; i++) {
            var record = arrayOfObjectWithID[i];
            if (idToRemove === record.id) {
                theIndex = i;
                break;
            }
        }
        if (theIndex !== undefined) {
            arrayOfObjectWithID.splice(theIndex, 1);
        }
    }

    function stringArrayToCommaString(stringArray) {
        var commaSeparateString = "";
        if (!stringArray) {
            return commaSeparateString;
        }
        stringArray.forEach(function(string) {
            if (commaSeparateString !== "") {
                commaSeparateString = commaSeparateString + ", " + string;
            } else {
                commaSeparateString = string;
            }
        });
        return commaSeparateString;
    }

    function commaStringToArray(commaSeparateString) {
        var re = /\s*,\s*/;
        var stringArray = commaSeparateString.split(re);
        return stringArray;
    }

    var toExpose = {
        removeFromById: removeFromById,
        stringArrayToCommaString: stringArrayToCommaString,
        commaStringToArray: commaStringToArray
    };
    return toExpose;
})();
