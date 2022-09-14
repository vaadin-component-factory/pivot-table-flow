import $ from "jquery";
import "pivottable/dist/pivot.js";

window.drawPivot = function(id, dataJson, optionsJson) {
	let dj = $.parseJSON(dataJson);
	let oj = $.parseJSON(optionsJson);
	$("#"+id).pivot(dj, oj);
}

window.drawPivotUI = function(id, dataJson, optionsJson) {
    var renderers = $.extend(
       $.pivotUtilities.renderers,
       $.pivotUtilities.c3_renderers,
       $.pivotUtilities.d3_renderers
    )
	let dj = $.parseJSON(dataJson);
	let oj = $.parseJSON(optionsJson);
	$("#"+id).pivotUI(dj, oj);	
}

window.drawChartPivotUI = function(id, dataJson, cols, rows) {
    var renderers = $.extend(
       $.pivotUtilities.renderers,
       $.pivotUtilities.c3_renderers,
       $.pivotUtilities.d3_renderers
    )
	let dj = $.parseJSON(dataJson);
	$("#"+id).pivotUI(dj, { cols: [cols], rows: [rows] , renderers: renderers }, true);
	
}