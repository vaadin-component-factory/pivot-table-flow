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
    setupPivotPopupDragging();
}

window.drawChartPivotUI = function(id, dataJson, cols, rows) {
    var renderers = $.extend(
       $.pivotUtilities.renderers,
       $.pivotUtilities.c3_renderers,
       $.pivotUtilities.d3_renderers
    )
	let dj = $.parseJSON(dataJson);
	$("#"+id).pivotUI(dj, { cols: [cols], rows: [rows] , renderers: renderers }, true);
    setupPivotPopupDragging();
}

function setupPivotPopupDragging() {
    const elements = document.getElementsByClassName("pvtFilterBox");
	console.log("Setup dragging: "+elements.length);
    for (let i=0;i<elements.length;i++) {
        console.log("Setup");
		dragPivotPopup(elements[i]);
	}	
}

function dragPivotPopup(elmnt) {
  var pos1 = 0, pos2 = 0, pos3 = 0, pos4 = 0;
  console.log("dragPivotPopup");
  elmnt.onmousedown = dragMouseDown;

  function dragMouseDown(e) {
	console.log("Drag");
    e = e || window.event;
    e.preventDefault();
    // get the mouse cursor position at startup:
    pos3 = e.clientX;
    pos4 = e.clientY;
    document.onmouseup = closeDragElement;
    // call a function whenever the cursor moves:
    document.onmousemove = elementDrag;
  }

  function elementDrag(e) {
    e = e || window.event;
    e.preventDefault();
    // calculate the new cursor position:
    pos1 = pos3 - e.clientX;
    pos2 = pos4 - e.clientY;
    pos3 = e.clientX;
    pos4 = e.clientY;
    // set the element's new position:
    elmnt.style.top = (elmnt.offsetTop - pos2) + "px";
    elmnt.style.left = (elmnt.offsetLeft - pos1) + "px";
  }

  function closeDragElement() {
    // stop moving when mouse button is released:
    document.onmouseup = null;
    document.onmousemove = null;
  }
}