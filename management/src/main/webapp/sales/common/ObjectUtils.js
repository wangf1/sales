jQuery.sap.declare("sales.common.ObjectUtils");
sales.common.ObjectUtils = (function() {
    "use strict";

    function getAllOwnPropertyAsArray(object) {
        var props = [];
        for ( var key in object) {
            if (!object.hasOwnProperty(key)) {
                continue;
            }
            props.push(object[key]);
        }
        return props;
    }

    var toExpose = {
        getAllOwnPropertyAsArray: getAllOwnPropertyAsArray
    };
    return toExpose;
})();
