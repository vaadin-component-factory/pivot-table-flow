import $ from "jquery";

window.drawPivot = function(dataJson, optionsJson) {
	let dj = $.parseJSON(dataJson);
	let oj = $.parseJSON(optionsJson);
	$("#output").pivot(dj, oj);
}

window.drawPivotUI = function(dataJson, optionsJson) {
	let dj = $.parseJSON(dataJson);
	let oj = $.parseJSON(optionsJson);
	$("#output").pivotUI(dj, oj);
}