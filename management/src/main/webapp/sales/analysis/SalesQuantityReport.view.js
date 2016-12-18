sap.ui.jsview("sales.analysis.SalesQuantityReport", (function() {
    "use strict";

    jQuery.sap.require("sales.records.SalesRecordsUIHelper");
    jQuery.sap.require("sap.viz.ui5.controls.VizFrame");
    jQuery.sap.require("sap.suite.ui.commons.ChartContainer");

    var getControllerName = function() {
        return "sales.analysis.SalesQuantityReport";
    };

    function createVizFrame(oController) {
        var vizFrame = new sap.viz.ui5.controls.VizFrame(oController.createId("viz_frame"), {
            height: "100%",
            width: "100%",
            uiConfig: {
                "applicationSet": "fiori",
                "showErrorMessage": "true"
            }
        }).addStyleClass("server-viz-frame");
        var popOver = new sap.viz.ui5.controls.Popover();
        popOver.connect(vizFrame.getVizUid());
        return vizFrame;
    }

    function createChartContainer(oController) {
        var vizFrame = createVizFrame(oController);
        var chartContainer = new sap.suite.ui.commons.ChartContainer(oController.createId("chart_container"), {
            showLegend: true,
            autoAdjustHeight: true,
            showZoom: false,
            showFullScreen: true,
            showPersonalization: false,
            title: "{i18n>sales_quantity_chart_title}",
            customIcons: new sap.ui.core.Icon(oController.createId("info_icon"), {
                src: "sap-icon://message-information",
                tooltip: "{i18n>sales_quantity_report_search_icon_tooltip}",
                width: "2em",
                press: function(oEvent) {
                    oController.showSearchHelp();
                }
            }),
            content: [
                new sap.suite.ui.commons.ChartContainerContent({
                    content: [
                        vizFrame
                    ]
                })
            ]
        });
        return chartContainer;
    }

    function createFixFlexLayout(oController) {
        var searchPanel = sales.records.SalesRecordsUIHelper.createSearchPanel(oController);
        var chartContainer = createChartContainer(oController);
        var fixFlex = new sap.ui.layout.FixFlex(oController.createId("idFixFlex"), {
            minFlexSize: 300,
            fixContent: [
                searchPanel
            ],
            flexContent: chartContainer

        });
        return fixFlex;

    }

    var createContent = function(oController) {
        var fixFlexLayout = createFixFlexLayout(oController);
        return fixFlexLayout;
    };

    var view = {
        getControllerName: getControllerName,
        createContent: createContent
    };
    return view;
})());
