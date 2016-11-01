jQuery.sap.require("sap.ui.model.type.Integer");

jQuery.sap.declare("sales.common.IntTypeOnlyFormatValue");
/**
 * This type can just return empty string is the input is not a valid int, I not yet debug code to find why, but it is suitable for my use.
 */
sap.ui.model.type.Integer.extend("sales.common.IntTypeOnlyFormatValue", (function() {
    "use strict";

    var toExpose = {
        parseValue: function(value) {
            return value;
        },

        validateValue: function(value) {
            return true;
        }
    };
    return toExpose;
})());
