import $ from "jquery";

window.drawPivot = function(id, dataJson, optionsJson) {
	let dj = $.parseJSON(dataJson);
	let oj = $.parseJSON(optionsJson);
	$("#"+id).pivot(dj, oj);
}

window.drawPivotUI = function(id, dataJson, optionsJson) {
//    var renderers = $.extend(
//       $.pivotUtilities.renderers,
//       $.pivotUtilities.c3_renderers,
//       $.pivotUtilities.d3_renderers
//    );
	let dj = $.parseJSON(dataJson);
	let oj = $.parseJSON(optionsJson);
	$("#"+id).pivotUI(dj, oj);
}