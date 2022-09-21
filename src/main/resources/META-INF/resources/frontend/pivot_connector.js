import $ from "jquery";
import "pivottable/dist/pivot.js";

window.drawPivot = function(id, dataJson, optionsJson) {
	let dj = $.parseJSON(dataJson);
	let oj = $.parseJSON(optionsJson);
	$("#"+id).pivot(dj, oj);
}

window.drawPivotUI = function(id, dataJson, optionsJson, renderer, disabled) {
    var renderers = $.extend(
       $.pivotUtilities.renderers,
       $.pivotUtilities.c3_renderers,
       $.pivotUtilities.d3_renderers
    )
	let dj = $.parseJSON(dataJson);
	let oj = $.parseJSON(optionsJson);
	$("#"+id).pivotUI(dj, oj);	
    setupPivotPopupDragging();
    if (disabled) {
		disableFields(id);
	}
    if (renderer) {
	    const rendererSelect = $("#"+id).find(".pvtRenderer");
		rendererSelect.value = renderer;
		rendererSelect.dispatchEvent(new Event("change"));
    }
}

window.drawChartPivotUI = function(id, dataJson, cols, rows, disabledRenderers, renderer, disabled) {
    var renderers = $.extend(
       $.pivotUtilities.renderers,
       $.pivotUtilities.c3_renderers,
       $.pivotUtilities.d3_renderers
    )
	let dj = $.parseJSON(dataJson);
	const cs = cols.split(",");
	const rs = rows.split(",");
	$("#"+id).pivotUI(dj, { cols: cs, rows: rs, renderers: renderers }, true);
    setupPivotPopupDragging(id);
    if (renderer) {
	    $("#"+id).find(".pvtRenderer").val(renderer);
    }
    if (disabled) {
		disableFields(id);
	}
	if (disabledRenderers) {
		disableRenderers(id, disabledRenderers);
	}
}

function disableRenderers(id, disabledRenderers) {
	const disabled = disabledRenderers.split(",");
    for (let i=0;i<disabled.length;i++) {
		$("#"+id).find(".pvtRenderer").find("[value='"+disabled[i]+"']").css("display","none");
    }
}

function disableFields(id) {
	$("#"+id).find(".pvtUnused").css("display","none");
    const elements = $("#"+id).find(".ui-sortable-handle");
    for (let i=0;i<elements.length;i++) {
		elements[i].style.pointerEvents = "none";
		elements[i].getElementsByClassName("pvtTriangle")[0].style.display = "none";
	}
}

function setupPivotPopupDragging(id) {
    const elements = $("#"+id).find(".pvtFilterBox");
    for (let i=0;i<elements.length;i++) {
		dragPivotPopup(elements[i]);
	}	
}

function dragPivotPopup(elmnt) {
  var pos1 = 0, pos2 = 0, pos3 = 0, pos4 = 0;
  const element = elmnt.getElementsByTagName("h4")[0];
  element.onmousedown = dragMouseDown;

  function dragMouseDown(e) {
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