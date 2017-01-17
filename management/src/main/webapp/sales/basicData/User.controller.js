sap.ui.define([
    "sales/basicData/CRUDTableController", "sap/ui/model/json/JSONModel", "sap/ui/model/Filter", "sap/ui/model/FilterOperator", "sales/common/AjaxUtils", "sales/common/i18nUtils",
    "sales/common/DateTimeUtils", "sales/common/ValidateUtils", "sales/common/UIUtils", "sales/common/ArrayUtils", "sap/m/MessageBox"
], function(CRUDTableController, JSONModel, Filter, FilterOperator, AjaxUtils, i18nUtils, DateTimeUtils, ValidateUtils, UIUtils, ArrayUtils, MessageBox) {
    "use strict";

    var oViewModel = CRUDTableController.prototype.oViewModel;

    function refreshAllRoles() {
        var promise = AjaxUtils.ajaxCallAsPromise({
            method: "GET",
            url: "listAllRoles",
            dataType: "json",
            contentType: "application/json"
        });
        promise.then(function(result) {
            oViewModel.setProperty("/allRoles", result.data);
        });
    }

    function setTableModel() {
        var promiseAfterSetTableModel = CRUDTableController.prototype.setTableModel.call(this);
        var managers = [];
        promiseAfterSetTableModel.then(function() {
            var tableData = oViewModel.getProperty("/tableData");
            tableData.forEach(function(user) {
                managers.push(user);
            });
            var emtpyItem = {
                userName: ""
            };
            managers.unshift(emtpyItem);
            oViewModel.setProperty("/managers", managers);
            oViewModel.refresh();
        });
    }

    function onRefresh() {
        refreshAllRoles();
        CRUDTableController.prototype.onRefresh.call(this);
    }

    function onAdd() {
        var newAdded = CRUDTableController.prototype.onAdd.call(this);
        var users = oViewModel.getProperty("/tableData");
        var manager;
        for (var i = 0; i < users.length; i++) {
            var user = users[i];
            if (user.userName !== "") {
                manager = user.userName;
            }
        }
        newAdded["manager"] = manager;
        return newAdded;
    }

    function validateRequiredFieldNotNull(object) {
        for ( var key in object) {
            if (!object.hasOwnProperty(key)) {
                continue;
            }
            if (key === "manager") {
                continue;
            }
            var value = object[key];
            if (value === undefined || value === null || value === "") {
                var message = resBundle.getText("before_save_validate_fail");
                UIUtils.showMessageToast(message);
                return false;
            }
            if (value.trim) {
                if (value.trim() === "") {
                    var message = resBundle.getText("before_save_validate_fail");
                    UIUtils.showMessageToast(message);
                    return false;
                }
            }
        }
        return true;
    }

    function validateBeforeSaveShowMessageToast(object) {
        var isValid = validateRequiredFieldNotNull(object, this);
        return isValid;
    }

    var controller = CRUDTableController.extend("sales.basicData.User", {
        columnNames: [
            "userName", "password", "firstName", "lastName", "roles", "manager"
        ],
        urlForListAll: "listAllUsers",
        urlForSaveAll: "saveUsers",
        urlForDeleteAll: "deleteUsers",
        onRefresh: onRefresh,
        onAdd: onAdd,
        setTableModel: setTableModel,
        validateBeforeSaveShowMessageToast: validateBeforeSaveShowMessageToast
    });
    return controller;
});
