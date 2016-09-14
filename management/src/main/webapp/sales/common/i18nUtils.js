jQuery.sap.declare("sales.common.i18nUtils");
sales.common.i18nUtils = (function() {
    "use strict";

    function initAndGetResourceBundle() {

        var res = sap.ui.getCore().getModel("i18n");
        if (!res) {
            res = new sap.ui.model.resource.ResourceModel({
                bundleUrl: jQuery.sap.getModulePath("sales", "/i18n/i18n.properties"),
                bundleLocale: sap.ui.getCore().getConfiguration().getLanguage()
            });
            sap.ui.getCore().setModel(res, "i18n");
        }
        return res.getResourceBundle();
    }

    var toExpose = {
        initAndGetResourceBundle: initAndGetResourceBundle
    };
    return toExpose;
})();
