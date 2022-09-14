import $ from "jquery";

window.drawPivot = function(id, dataJson, optionsJson) {
	let dj = $.parseJSON(dataJson);
	let oj = $.parseJSON(optionsJson);
	$("#"+id).pivot(dj, oj);
}

window.drawPivotUI = function(id, dataJson, optionsJson) {
	let dj = $.parseJSON(dataJson);
	let oj = $.parseJSON(optionsJson);
	$("#"+id).pivotUI(dj, oj);
}