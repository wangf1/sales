sap.ui.jsview("sales.basicData.User", (function() {
    "use strict";

    var getControllerName = function() {
        return "sales.basicData.User";
    };

    var createTableHeaderToolBar = function(oController) {
        var toolbarContent = [];
        toolbarContent.push(new sap.m.Title({
            text: "{i18n>hospital}"
        }));
        toolbarContent.push(new sap.m.SearchField({
            placeholder: "{i18n>quickSearchPlaceHolder}",
            width: "50%",
            showSearchButton: false,
            liveChange: function(e) {
                oController.onQuickFilter(e);
            }
        }));
        toolbarContent.push(new sap.m.ToolbarSpacer());
        toolbarContent.push(new sap.m.Button({
            text: "{i18n>refresh}",
            icon: "sap-icon://refresh",
            press: function(e) {
                oController.onRefresh(e);
            }
        }));
        toolbarContent.push(new sap.m.Button({
            text: "{i18n>add}",
            visible: "{permissionModel>/user/create}",
            icon: "sap-icon://add",
            press: function(e) {
                oController.onAdd(e);
            },
        }));
        toolbarContent.push(new sap.m.Button({
            text: "{i18n>delete}",
            visible: "{permissionModel>/user/delete}",
            icon: "sap-icon://delete",
            enabled: "{= ${/selectedRecords}.length>0 }",
            press: function() {
                oController.onDelete();
            }
        }));
        toolbarContent.push(new sap.m.Button({
            text: "{i18n>save}",
            visible: "{permissionModel>/user/update}",
            icon: "sap-icon://save",
            enabled: "{= ${/inlineChangedRecords}.length>0 || ${/newAddedRecords}.length>0}",
            press: function() {
                oController.onSaveAll();
            }
        }));

        var toolBar = new sap.m.Toolbar({
            content: toolbarContent
        });
        return toolBar;
    };

    function createTable(oController) {
        var tableCells = [];
        var tableColumns = [];
        var isAdminRole = sap.ui.getCore().getModel("permissionModel").getProperty("/user/create");
        oController.columnNames.forEach(function(columName) {
            tableColumns.push(new sap.m.Column({
                width: "30%",
                hAlign: sap.ui.core.TextAlign.Center,
                header: new sap.m.Button({
                    text: "{i18n>" + columName + "}",
                    press: function(e) {
                        oController.sortTable(e);
                    },
                    customData: [
                        new sap.ui.core.CustomData({
                            key: "column",
                            value: columName
                        }), new sap.ui.core.CustomData({
                            key: "descending",
                            value: true
                        })
                    ]
                })
            }));
            var cellEnabled = true;
            if (columName === "userName" || columName === "roles" || columName === "manager") {
                // Each sales person do not need see mamager and salesPerson columns
                var cellEnabled = "{permissionModel>/user/create}";
            }
            if (columName === "roles") {
                tableCells.push(new sap.m.ComboBox({
                    enabled: cellEnabled,
                    change: function(e) {
                        oController.onCellLiveChange(e);
                    },
                    selectedKey: "{" + columName + "}",
                    items: {
                        path: "/allRoles",
                        template: new sap.ui.core.Item({
                            key: "{}",
                            text: "{}"
                        }),
                        templateShareable: true
                    }
                }));
            } else if (columName === "manager" && isAdminRole) {
                // only for Admin role, then the manager column is a select, for user role, the manager column only display manager userName
                tableCells.push(new sap.m.Select({
                    enabled: cellEnabled,
                    change: function(e) {
                        oController.onCellLiveChange(e);
                    },
                    value: "{" + columName + "}",
                    selectedKey: "{" + columName + "}",
                    items: {
                        path: "/tableData",
                        template: new sap.ui.core.Item({
                            key: "{userName}",
                            text: "{userName}"
                        }),
                        templateShareable: true
                    }
                }));
            } else {
                tableCells.push(new sap.m.Input({
                    enabled: cellEnabled,
                    value: "{" + columName + "}",
                    liveChange: function(e) {
                        oController.onCellLiveChange(e);
                    }
                }).addStyleClass("input-in-table-cell"));
            }

        });

        var table = new sap.m.Table({
            id: oController.createId("theTable"),
            headerText: "{i18n>hospital}",
            mode: sap.m.ListMode.MultiSelect,
            width: "auto",
            selectionChange: function() {
                oController.onTableSelectionChange();
            },
            headerToolbar: createTableHeaderToolBar(oController),
            items: {
                path: "/tableData",
                template: new sap.m.ColumnListItem({
                    cells: tableCells
                })
            },
            columns: tableColumns

        });
        table.addStyleClass("sapUiResponsiveMargin");
        return table;
    }

    var createContent = function(oController) {
        var table = createTable(oController);
        return table;
    };

    var view = {
        getControllerName: getControllerName,
        createContent: createContent
    };
    return view;
})());
