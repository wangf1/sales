jQuery.sap.require("sap.m.MessageToast");

jQuery.sap.declare("sales.common.UIUtils");
sales.common.UIUtils = (function() {
    "use strict";

    function showMessageToast(message) {
        sap.m.MessageToast.show(message, {
            duration: 5000,
            width: "25em",
            closeOnBrowserNavigation: false
        });
    }

    var toExpose = {
        showMessageToast: showMessageToast
    };
    return toExpose;
})();
