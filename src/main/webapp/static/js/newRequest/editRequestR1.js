var $requestEditForm = $('#generateRequestForm');
var origForm=null;
var $dtUser;
var $trackingRFCForm = $('#trackingReleaseForm');
var $treeForm = $('#treeForm');
var network = null;
var nodes = [];
var edges = [];
$(function() {

	activeItemMenu("requestItem");
	 $('#systemId').selectpicker('val',$('#systemInfoId').val());
	 initRequestFormValidation();

	 $('#releaseTable tbody').on( 'click', 'tr', function () {
	      if ( $(this).hasClass('selected') ) {
	            $(this).removeClass('selected');

	        }
	        else {
	        	// $dtRFCs.$('tr.selected').removeClass('selected');
	            $(this).addClass('selected');
	          
	        }
	    } );
	 
	 $('#releaseTableAdd tbody').on( 'click', 'tr', function () {
	      if ( $(this).hasClass('selected') ) {
	            $(this).removeClass('selected');

	        }
	        else {
	        	// $dtRFCsAdd.$('tr.selected').removeClass('selected');
	            $(this).addClass('selected');
	          
	        }
	    } );


		$('#timeAnswer').datetimepicker({
			locale: 'es',
			format: 'DD/MM/YYYY hh:mm a',
			minDate : new Date(),
			sideBySide: true
		});
		
	 $('.tagInit').tagsInput();
	  
	$('.nav-tabs > li a[title]').tooltip();
	// Wizard
	$('.stepper a[data-toggle="tab"]').on('show.bs.tab', function(e) {
		
		var $target = $(e.target);
		if ($target.parent().hasClass('disabled')) {
			return false;
		}
		if(formHasChanges()){
			sendPartialRequest();
		}
	});
	
	
	var toggle = $('.picker-switch a[data-action="togglePicker"]');
	
	$(".next-step").click(function(e) {
		var $active = $('.stepper li.active');
		while($active.next().hasClass('disabled')){
			$active = $active.next();
			e.stopPropagation();
		}
		nextTab($active);
		$('html, body').animate({scrollTop: '0px'}, 300);
	});

	$(".prev-step").click(function(e) {
		var $active = $('.stepper li.active');
		while($active.prev().hasClass('disabled')){
			$active = $active.prev();
			e.stopPropagation();
		}
		prevTab($active);
		$('html, body').animate({scrollTop: '0px'}, 300);
	});
	origForm = $requestEditForm.serialize();
	$('.tagInit').tagsInput({
		width:'400px'
	});


	
});

function nextTab(elem) {
	$(elem).next().find('a[data-toggle="tab"]').tab('show');
}

function prevTab(elem) {
	$(elem).prev().find('a[data-toggle="tab"]').tab('show');
}

function formHasChanges(){
	
	if($requestEditForm.serialize() === origForm  ){
		return false;
	}else{
		return true;
	}
}

function previewRequest() {
	$('#previewRequestModal').modal('show');
}

function closePreviewRequest() {
	$('#previewRequestModal').modal('hide');
}

function compareArrays(arr1, arr2) {
	return $(arr1).not(arr2).length == 0 && $(arr2).not(arr1).length == 0;
};
function sendPartialRequest() {
	changeSaveButton(true);
	$.ajax({
		// async : false,
		type : "PUT",
		url : getCont() + "request/saveRequestR1",
		timeout: 60000,
		dataType : "json",
		contentType: "application/json; charset=utf-8",
		data : JSON.stringify({
			// Informacion general
			id : $requestEditForm.find('#requestR1Id').val(),
			senders:$requestEditForm.find('#senders').val(),
			message:$requestEditForm.find('#messagePer').val(),
			timeAnswer:$requestEditForm.find('#timeAnswer').val(),
			initialRequeriments:$requestEditForm.find('#requeriments').val(),
			observations:$requestEditForm.find('#observations').val(),
			
		}),
		success : function(response) {
			// responseAjaxSendPartialRelease(response);
			changeSaveButton(false);
			origForm = $requestEditForm.serialize();
			reloadPreview();
		},
		error: function(x, t, m) {
			notifyAjaxError(x, t, m);
			changeSaveButton(false);
		}
	});
}

function changeSaveButton(save){
	if(save){
		$requestEditForm.find('#btnSave').find('#btnText').text('GUARDANDO');
		$requestEditForm.find('#btnSave').find('#btnIcon').text('cached');
	}else{
		$requestEditForm.find('#btnSave').find('#btnText').text('GUARDAR');
		$requestEditForm.find('#btnSave').find('#btnIcon').text('check_box');
	}
}

function resetSpaces(){
	$requestEditForm.find('#name').val('');
	$requestEditForm.find('#email').val('');
	$("input:radio").attr("checked", false);
	$('#permission1').prop('checked',false);
	$('#permission2').prop('checked',false);
	$('#permission3').prop('checked',false);
	$('#permission4').prop('checked',false);
	$requestEditForm.find('#espec').val('');
	$requestEditForm.find('#ambientId').selectpicker('val',"");
}
function initRequestFormValidation() {
	$requestEditForm.validate({
		rules : {
			'name':{
				required : true,
				maxlength : 20,
			},
			'email':{
				required :true,
				maxlength : 20,
			},
			'ambientId':{
				required:true,		
			},
			'espec':{
				required:true,
				minlength : 2,			
			},
			'type':{
				required:true,		
			}
			,
			'permission':{
				required:true,		
			}
				
			
		},
		messages : {
			
			'name' : {
				required : "Ingrese un valor",
				maxlength : "No puede poseer mas de {0} caracteres"
			},
			'email' : {
				required : "Ingrese un valor",
				maxlength : "No puede poseer mas de {0} caracteres"
					
			},

			'ambientId' : {
				required : "Seleccione un valor"
			},
			'espec' : {
				required : "Ingrese un valor",
				minlength : "Debe ser un minimo de dos caracteres"
			},
			'type':{
				required:"Se debe seleccionar una casilla",		
			},
			'permission':{
				required:"Se debe seleccionar al menos una opcion",		
			}
		},
		highlight,
		unhighlight,
		errorPlacement
	});
}

function reloadPreview() {
	var src = $("#tinySummary").attr("src")
	$("#tinySummary").attr("src", src)
}




function sendRequest() {

	changeSaveButton(true);
	var valueSchema="";

	$.ajax({
		// async : false,
		type : "PUT",
		url : getCont() + "request/saveRequestR1",
		timeout: 60000,
		dataType : "json",
		contentType: "application/json; charset=utf-8",
		data : JSON.stringify({
			// Informacion general
			id : $requestEditForm.find('#requestR1Id').val(),
			senders:$requestEditForm.find('#senders').val(),
			message:$requestEditForm.find('#messagePer').val(),
			timeAnswer:$requestEditForm.find('#timeAnswer').val(),
			initialRequeriments:$requestEditForm.find('#requeriments').val(),
			observations:$requestEditForm.find('#observations').val(),
		}),
		success : function(response) {
			responseAjaxSendRequest(response);
			changeSaveButton(false);
			origForm = $requestEditForm.serialize();
			reloadPreview();
		},
		error: function(x, t, m) {
			notifyAjaxError(x, t, m);
			changeSaveButton(false);
		}
	});
}

function requestRequest() {
	
	changeSaveButton(true);
	$.ajax({
		// async : false,
		type : "PUT",
		url : getCont() + "request/saveRequestR1",
		timeout: 60000,
		dataType : "json",
		contentType: "application/json; charset=utf-8",
		data : JSON.stringify({
			// Informacion general
			id : $requestEditForm.find('#requestR1Id').val(),
			senders:$requestEditForm.find('#senders').val(),
			message:$requestEditForm.find('#messagePer').val(),
			timeAnswer:$requestEditForm.find('#timeAnswer').val(),
			initialRequeriments:$requestEditForm.find('#requeriments').val(),
			observations:$requestEditForm.find('#observations').val(),
		}),
		success : function(response) {
			responseAjaxRequestRequest(response);
			changeSaveButton(false);
			origForm = $requestEditForm.serialize();
			reloadPreview();
		},
		error: function(x, t, m) {
			notifyAjaxError(x, t, m);
			changeSaveButton(false);
		}
	});
}

function responseAjaxRequestRequest(response) {
	if (response != null) {
		switch (response.status) {
		case 'success':
			resetErrors();
			reloadPreview();
			window.location = getCont() + "request/updateRequest/"
			+  $requestEditForm.find('#requestId').val();
			break;
		case 'fail':
			showRequestErrors(response.errors);
			countErrorsByStep();
			var numItems = $('.yourclass').length
			swal("Avance guardado!", "El formulario a\u00FAn posee campos incompletos.",
			"warning")
			break;
		case 'exception':
			swal("Error!", response.exception, "error")
			break;
		default:
			unblockUI();
		}
	}
}



function responseAjaxSendRequest(response) {
	if (response != null) {
		switch (response.status) {
		case 'success':
			resetErrors();
			reloadPreview();
			swal("Correcto!", "Solicitud guardada correctamente.",
					"success", 2000)
					$('#generateReleaseForm #applyFor').show();
			break;
		case 'fail':
			showRequestErrors(response.errors);
			countErrorsByStep();
			var numItems = $('.yourclass').length
			swal("Avance guardado!", "El formulario a\u00FAn posee campos incompletos.",
			"warning")
			break;
		case 'exception':
			swal("Error!", response.exception, "error")
			break;
		default:
			unblockUI();
		}
	}
}

function resetErrors() {
	$(".fieldError").css("visibility", "hidden");
	$(".fieldError").attr("class", "error fieldError");
	$(".fieldErrorLine").attr("class", "form-line");
	$('.labelCount_Error').css("visibility", "hidden");
}

function showRequestErrors(errors) {
	resetErrors();// Eliminamos las etiquetas de errores previas
	var error = errors;
	for (var i = 0; i < error.length; i++) {
		// Se modifica el texto de la advertencia y se agrega la de activeError
		$requestEditForm.find(" #" + error[i].key + "_error").text(
				error[i].message);
		$requestEditForm.find(" #" + error[i].key + "_error").css("visibility",
		"visible");
		$requestEditForm.find(" #" + error[i].key + "_error").attr("class",
		"error fieldError activeError");
		// Si es input||textarea se marca el line en rojo
		if ($requestEditForm.find(" #" + error[i].key).is("input")
				|| $requestEditForm.find(" #" + error[i].key).is("textarea")) {
			$requestEditForm.find(" #" + error[i].key).parent().attr("class",
			"form-line error fieldErrorLine");
		}
	}
}
function countErrorsByStep() {
	var step1 = $("#step1").find(".activeError").length;
	if (step1 != 0) {
		$("#step1Errors").css("visibility", "visible");
	}
	var step2 = $("#step2").find(".activeError").length;
	if (step2 != 0) {
		$("#step2Errors").css("visibility", "visible");
	}
	var step3 = $("#step3").find(".activeError").length;
	if (step3 != 0) {
		$("#step3Errors").css("visibility", "visible");
	}
	var step4 = $("#step4").find(".activeError").length;
	if (step4 != 0) {
		$("#step4Errors").css("visibility", "visible");
	}
}


