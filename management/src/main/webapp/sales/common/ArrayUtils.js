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

    var toExpose = {
        removeFromById: removeFromById
    };
    return toExpose;
})();
