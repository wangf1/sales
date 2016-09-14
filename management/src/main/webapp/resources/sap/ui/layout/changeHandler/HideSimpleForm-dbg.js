/*!
 * UI development toolkit for HTML5 (OpenUI5)
 * (c) Copyright 2009-2016 SAP SE or an SAP affiliate company.
 * Licensed under the Apache License, Version 2.0 - see LICENSE.txt.
 */

sap.ui.define([
	'jquery.sap.global'
], function(jQuery) {
	"use strict";

	/**
	 * Change handler for hiding of a control.
	 * @alias sap.ui.fl.changeHandler.HideControl
	 * @author SAP SE
	 * @version 1.40.6
	 * @experimental Since 1.27.0
	 */
	var HideForm = { };

	/**
	 * Hides a control.
	 *
	 * @param {sap.ui.fl.Change} oChange change object with instructions to be applied on the control map
	 * @param {sap.ui.core.Control} oControl control that matches the change selector for applying the change
	 * @public
	 */
	HideForm.applyChange = function(oChangeWrapper, oControl, oModifier, oView) {
		var oChange = oChangeWrapper.getDefinition();
		var sHideId = oChange.content.sHideId;

		var oCtrl = oModifier.byId(sHideId, oView);
		var aContent = oModifier.getAggregation(oControl, "content");
		var iStart = -1;

		if (oChange.changeType === "hideSimpleFormField") {
			aContent.some(function (oField, index) {
				if (oField === oCtrl) {
					iStart = index;
					oModifier.setVisible(oField, false);
				}
				if (iStart >= 0 && index > iStart) {
					if ((oModifier.getControlType(oField) === "sap.m.Label") ||
							(oModifier.getControlType(oField) === "sap.ui.core.Title") ||
							(oModifier.getControlType(oField) === "sap.m.Title") ||
							(oModifier.getControlType(oField) === "sap.m.Toolbar")) {
						return true;
					} else {
						oModifier.setVisible(oField, false);
					}
				}
			});
		} else if (oChange.changeType === "removeSimpleFormGroup") {
			aContent.some(function (oField, index) {
				if (oField === oCtrl) {
					iStart = index;
				}
				if (iStart >= 0 && index > iStart) {
					if ((oModifier.getControlType(oField) === "sap.ui.core.Title") ||
							(oModifier.getControlType(oField) === "sap.m.Title") ||
							(oModifier.getControlType(oField) === "sap.m.Toolbar")) {
						if (iStart === 0) {
							oModifier.removeAggregation(oControl, "content", oField, oView);
							oModifier.insertAggregation(oControl, "content", oField, 0, oView);
						}
						return true;
					} else {
						oModifier.setVisible(oField, false);
					}
				}
			});
			oModifier.removeAggregation(oControl, "content", oCtrl, oView);
		}

		return true;
	};

	/**
	 * Completes the change by adding change handler specific content
	 *
	 * @param {sap.ui.fl.Change} oChange change object to be completed
	 * @param {object} oSpecificChangeInfo as an empty object since no additional attributes are required for this operation
	 * @public
	 */
	HideForm.completeChangeContent = function(oChangeWrapper, oSpecificChangeInfo) {
		var oChange = oChangeWrapper.getDefinition();
		if (oSpecificChangeInfo.sHideId) {
			oChange.content.sHideId = oSpecificChangeInfo.sHideId;
		} else {
			throw new Error("oSpecificChangeInfo.sHideId attribute required");
		}
	};

	return HideForm;
},
/* bExport= */true);
