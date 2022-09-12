import $ from "jquery";

window.drawpivot = function(json) {
	console.log(json);
	$("#output").pivotUI(json);
}
