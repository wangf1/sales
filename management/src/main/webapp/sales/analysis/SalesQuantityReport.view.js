sap.ui.jsview("sales.analysis.SalesQuantityReport", (function() {
    "use strict";

    jQuery.sap.require("sales.records.SalesRecordsUIHelper");
    jQuery.sap.require('sap.viz.ui5.controls.VizFrame');
    jQuery.sap.require('sap.suite.ui.commons.ChartContainer');

    var getControllerName = function() {
        return "sales.analysis.SalesQuantityReport";
    };

    function createVizFrame(oController) {
        var vizFrame = new sap.viz.ui5.controls.VizFrame(oController.createId('viz_frame'), {
            height: '100%',
            width: '100%',
            uiConfig: {
                'applicationSet': 'fiori',
                'showErrorMessage': 'true'
            }
        }).addStyleClass('server-viz-frame');
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
            title: '{i18n>sales_quantity_chart_title}',
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
        var content = new sap.m.VBox({
            width: "100%",
            height: "100%",
            items: [
                searchPanel, chartContainer
            ]
        });
// var fixFlex = new sap.ui.layout.FixFlex({
// minFlexSize: 250,
// fixContent: [
// searchPanel
// ],
// flexContent: [
// chartContainer
// ]
// });
// return fixFlex;
        return content;
    }

    var createContent = function(oController) {
        var fixFlexLayout = createFixFlexLayout(oController);
        return fixFlexLayout;
    };

    var view = {
        height: "100%",
        getControllerName: getControllerName,
        createContent: createContent
    };
    return view;
})());
