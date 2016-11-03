jQuery.sap.require("sap.ui.model.type.Integer");
jQuery.sap.declare("sales.common.ValidateUtils");

jQuery.sap.declare("sales.common.IntTypeOnlyFormatValue");
/**
 *  For invalid value, just set empty value as it is
 */
sap.ui.model.type.Integer.extend("sales.common.IntTypeOnlyFormatValue", (function() {
    "use strict";

    var toExpose = {
        parseValue: function(value, sInternalType) {
            try {
                var parsed = sap.ui.model.type.Integer.prototype.parseValue.call(this, value, sInternalType);
                return parsed;
            } catch (e) {
                // For invalid value, just set empty value as it is
                return value;
            }
        },
        formatValue: function(value, sInternalType) {
            var result = sap.ui.model.type.Integer.prototype.formatValue.call(this, value, sInternalType);
            if (sales.common.ValidateUtils.isEmptyString(result)) {
                // If format has problem, just return the original value.
                result = value;
            }
            return result;
        }
    };
    return toExpose;
})());
