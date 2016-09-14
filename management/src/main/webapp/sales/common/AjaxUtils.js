jQuery.sap.declare("sales.common.AjaxUtils");

jQuery.sap.require("sales.common.i18nUtils");

sales.common.AjaxUtils = (function() {
    "use strict";

    function createBusyDialog(msg) {
        if (!msg) {
            return null;
        }
        var dialog = new sap.m.Dialog({
            showHeader: false,
            contentWidth: "60%",
            contentHeight: "60px",
            horizontalScrolling: false,
            verticalScrolling: false,
            type: sap.m.DialogType.Message,
            content: [
                new sap.m.HBox({
                    items: [
                        new sap.m.BusyIndicator(), new sap.m.Text({
                            text: msg
                        })
                    ]
                })
            ],
            afterClose: function() {
                dialog.destroy();
            }
        }).addStyleClass("hmc-busy-indicator");
        return dialog;
    }

    function ajaxCallAsPromise(ajaxmeta, settings) {
        if (!ajaxmeta.method) {
            ajaxmeta.method = "GET";
        }
        if (!ajaxmeta.async) {
            ajaxmeta.async = true;
        }
        if (!ajaxmeta.cache) {
            ajaxmeta.cache = false;
        }
        if (!ajaxmeta.doingwhat) {
            var resBundle = sales.common.i18nUtils.initAndGetResourceBundle();
            ajaxmeta.doingwhat = resBundle.getText("getDataFromServer");
        }

        settings = settings || {};
        if (settings.hasCustomErrorHandler === undefined) {
            settings.hasCustomErrorHandler = false;
        }
        if (settings.defaultErrorMessage === undefined) {
            settings.defaultErrorMessage = "";
        }

        var that = this;
        var dialog = createBusyDialog(ajaxmeta.doingwhat);
        if (dialog !== null) {
            dialog.open();
        }
        return new Promise(function(resolve, reject) {
            jQuery.ajax({
                url: ajaxmeta.url,
                method: ajaxmeta.method,
                type: ajaxmeta.method,
                dataType: ajaxmeta.dataType,
                data: ajaxmeta.data,
                async: ajaxmeta.async,
                cache: ajaxmeta.cache,
                contentType: ajaxmeta.contentType,
                beforeSend: ajaxmeta.beforeSend,
                headers: ajaxmeta.headers,
                success: function(data, textStatus, jqXHR) {
                    if (dialog) {
                        dialog.close();
                    }
                    resolve({
                        data: data,
                        textStatus: textStatus,
                        jqXHR: jqXHR
                    });
                },
                error: function(jqXHR, textStatus, errorThrown) {
                    if (dialog) {
                        dialog.close();
                    }
                    if (settings.hasCustomErrorHandler) {
                        reject({
                            jqXHR: jqXHR,
                            textStatus: textStatus,
                            errorThrown: errorThrown
                        });
                    } else {
                        jQuery.sap.require("sap.m.MessageBox");
                        var errorMsg = that.parseErrorMessage(jqXHR);
                        if (errorMsg === "") {
                            if (settings.defaultErrorMessage !== "") {
                                errorMsg = settings.defaultErrorMessage;
                            } else {
                                errorMsg = that.getCommonResourceBundle().getText("UNKNOWN_ERROR");
                            }
                        }
                        sap.m.MessageBox.alert(errorMsg);
                    }
                }
            });
        });
    }

    var toExpose = {
        ajaxCallAsPromise: ajaxCallAsPromise
    };
    return toExpose;
})();
