sap.ui.jsview("sales.datacollect.DataCollect", (function() {
    "use strict";

    var getControllerName = function() {
        return "sales.datacollect.DataCollect";
    };

    var createContent = function(oController) {

        var iconTabBar = new sap.m.IconTabBar({
            upperCase: true,
            select: function(e) {
                oController.onTabSelect(e);
            },
            items: [
                new sap.m.IconTabFilter({
                    text: "{i18n>agency_recruit}",
                    key: "agencyRecruit"
                }), new sap.m.IconTabFilter({
                    text: "{i18n>agency_training}",
                    key: "agencyTraining"
                }), new sap.m.IconTabFilter({
                    text: "{i18n>bids_label}",
                    key: "bids"
                }), new sap.m.IconTabFilter({
                    text: "{i18n>speakers_label}",
                    key: "speakers"
                }), new sap.m.IconTabFilter({
                    text: "{i18n>DepartmentMeetings_label}",
                    key: "departmentMeetings"
                }), new sap.m.IconTabFilter({
                    text: "{i18n>RegionMeetings_label}",
                    key: "regionMeetings"
                })
            ]
        });
        iconTabBar.addStyleClass("emptyIconTabFilter");

        var pageContainer = new sap.m.NavContainer(this.createId("pageContainer")).addStyleClass("navigater-container");

        var page = new sap.m.Page({
            showHeader: false,
            enableScrolling: false,
            content: [
                iconTabBar, pageContainer
            ]
        });
        var app = new sap.m.App();
        app.addPage(page);
        return app;
    };

    var view = {
        getControllerName: getControllerName,
        createContent: createContent
    };
    return view;
})());
