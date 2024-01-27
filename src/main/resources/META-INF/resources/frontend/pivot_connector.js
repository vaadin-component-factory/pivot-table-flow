//import $ from "jquery";
//import "pivottable/dist/pivot.min.js";

window.drawPivotUI = function(id, dataJson, optionsJson, renderer, aggregator, column, disabled, noui) {
  let dj = $.parseJSON(dataJson);
  let oj = $.parseJSON(optionsJson);
  const table = $("#"+id);
  table.pivotUI(dj, { onRefresh: function() { table.get(0).dispatchEvent(new CustomEvent("pivot-refreshed")) }, ...oj } );	
  setupPivotPopupDragging();
  patchKeynav(id);
  if (disabled) {
    disableFields(id);
  }
  if (renderer) {
    $("#"+id).find(".pvtRenderer").val(renderer);
  }
  setAggregator(id, aggregator, column);
  if (noui) {
    disableUI(id);
  }
}

window.drawChartPivotUI = function(id, dataJson, cols, rows, disabledRenderers, renderer, aggregator, column, disabled, noui) {
  var renderers = $.extend(
     $.pivotUtilities.renderers,
     $.pivotUtilities.c3_renderers,
     $.pivotUtilities.export_renderers
  )
  let dj = $.parseJSON(dataJson);
  const cs = cols.split(",");
  const rs = rows.split(",");
  const table = $("#"+id);
  table.pivotUI(dj, { onRefresh: function() { table.get(0).dispatchEvent(new CustomEvent("pivot-refreshed")) }, cols: cs, rows: rs, renderers: renderers }, true);
  setupPivotPopupDragging(id);
  patchKeynav(id);
  if (renderer) {
    $("#"+id).find(".pvtRenderer").val(renderer);
  }
  setAggregator(id, aggregator, column);
  if (disabled) {
    disableFields(id);
  }
  if (disabledRenderers) {
    disableRenderers(id, disabledRenderers);
  }
  if (noui) {
    disableUI(id);
  }
}

window.getPivotTableResult = function(id) {
  const table = $("#"+id).find(".pvtTable");

  var data = "{}";  
  if (table) {
    data = table.tableToJSON();
  }

  return data;
}

window.setPivotTableI18n = function(id, i18nJson) {
  const i18n = $.parseJSON(i18nJson);
  const renderer = $("#"+id).find(".pvtRenderer");
  const aggregator = $("#"+id).find(".pvtAggregator");
  for (let i=0;i<i18n.length;i++) {
    renderer.find("[value='"+i18n[i].key+"']").text(i18n[i].text);
    aggregator.find("[value='"+i18n[i].key+"']").text(i18n[i].text);
  }	
}

function patchKeynav(id) {
  const buttons = $("#"+id).find(".pvtAttr");
  for (let i=0;i<buttons.length;i++) {
	const button = buttons[i];
	button.setAttribute("tabindex", "0");
	button.addEventListener("keydown", (e) => { if ( [13,32].includes(e.keyCode)) { button.children[0].click() } });
	button.children[0].addEventListener("click", (e) => {
      const popups = $("#"+id).find(".pvtFilterBox");
      for (let j=0;j<popups.length;j++) {
		 if (popups[j].checkVisibility()) popups[j].focus();
      }
	});
  }
  
  const btns = $("#"+id).find("button");
  for (let i=0;i<btns.length;i++) {
	btns[i].setAttribute("tabindex", "0");
  }
  const inputs = $("#"+id).find(".pvtFilter");
  for (let i=0;i<inputs.length;i++) {
	inputs[i].setAttribute("tabindex", "0");
	inputs[i].addEventListener("click", (e) => {
      if (inputs[i].getAttribute("checked") === "checked") {
        inputs[i].removeAttribute("checked");
      } else {
        inputs[i].setAttribute("checked", "checked");
      }
	});
  }
  const popups = $("#"+id).find(".pvtFilterBox");
  for (let i=0;i<popups.length;i++) {
	const popup = popups[i];
	popup.setAttribute("tabindex", "0");
    popup.addEventListener("keydown", (e) => { if (e.keyCode == 27) { popup.getElementsByTagName("button")[1].click() } });
  }

}

function disableUI(id) {
  $("#"+id).find(".pvtUiCell").css("display","none");	
}

function setAggregator(id, aggregator, column) {
 if (aggregator) {
    $("#"+id).find(".pvtAggregator").val(aggregator);
    if (column) {
      setTimeout(() => { 
        $("#"+id).find(".pvtAttrDropdown").val(column);
        $("#"+id).find(".pvtAttrDropdown").trigger("change");
        }, 100);
	}
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
		elements[i].children[0].removeAttribute("tabindex");
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
  elmnt.setAttribute("popup","");
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