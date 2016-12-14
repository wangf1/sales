sap.ui.define([
    "sap/ui/core/mvc/Controller", "sales/common/i18nUtils", "sales/common/DateTimeUtils", "sap/ui/model/json/JSONModel"
], function(Controller, i18nUtils, DateTimeUtils, JSONModel) {
    "use strict";

    var resBundle = i18nUtils.initAndGetResourceBundle();

    var viewModelData = {
        startAt: DateTimeUtils.firstDayOfPreviousMonth(),
        endAt: DateTimeUtils.today(),
    };

    var oViewModel = new JSONModel(viewModelData);

    function init() {
        this.getView().setModel(oViewModel);
    }

    var controller = Controller.extend("sales.analysis.SalesQuantityReport", {
        onInit: init,
    });
    return controller;
});
