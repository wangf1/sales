jQuery.sap.require("sap.ui.model.SimpleType");
jQuery.sap.require("sales.common.ArrayUtils");

jQuery.sap.declare("sales.common.StringArrayAsCommaStringType");

sap.ui.model.SimpleType.extend("sales.common.StringArrayAsCommaStringType", (function() {
    "use strict";

    var toExpose = {

        formatValue: function(stringArray) {
            var commaSeparateString = sales.common.ArrayUtils.stringArrayToCommaString(stringArray);
            return commaSeparateString;
        },

        parseValue: function(commaSeparateString) {
            var re = /\s*,\s*/;
            var stringArray = commaSeparateString.split(re);
            return stringArray;
        },

        validateValue: function(stringArray) {
            return true;
        }
    };
    return toExpose;
})());
