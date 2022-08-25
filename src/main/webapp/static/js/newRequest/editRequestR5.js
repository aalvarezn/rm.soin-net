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
	 $('.tagInitMail').tagsInput({
		 placeholder: 'Ingrese los correos'
	 });

	const data=$requestEditForm.find('#ambientData').val();
	const splitString = data.split(",");
	var ambients="";
	for(x=0;splitString.length>x;x++){
		if(splitString[x]==="Desarrollo"){
			$('#ambient1').prop('checked',true);
		}
		if(splitString[x]==="QA"){
			$('#ambient2').prop('checked',true);
		}
		if(splitString[x]==="Pre-Produccion"){
			$('#ambient3').prop('checked',true);
		}
		if(splitString[x]==="Produccion"){
			$('#ambient4').prop('checked',true);
		}else if(splitString[x]!==""&&splitString[x]!=="Desarrollo"&&splitString[x]!=="QA"&&splitString[x]!=="Pre-Produccion"&&splitString[x]!=="Produccion"){
			$('#ambient5').prop('checked',true);
			$('#ambient6').addTag(splitString[x]);
			if(ambients!=""){
				ambients+=","+splitString[x];
			}else{
				ambients=splitString[x];
			}
			
			
		}
	}
	if(ambients!=""){
		$('#ambient6').importTags(ambients);
		$('#ambient6_tagsinput').css("display","flex");
	}

	
});

function changeAttributte(checkElement){
		if(checkElement.checked){
			$('#ambient6_tagsinput').slideToggle();
			$('#ambient6_tagsinput').css("display","flex")
		}else{
			$('#ambient6_tagsinput').slideToggle();
			
				$('#ambient6').importTags('');
			
			//$('#ambient6_tagsinput').removeTag('awd');
		}

}
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
	var form = "#generateReleaseForm";
	var ambients="";
	if ($('#ambient1').is(":checked"))
	{
		if(ambients==""){
			ambients=$('#ambient1').val();
		}
		
	}
	if ($('#ambient2').is(":checked"))
	{
		if(ambients==""){
			ambients=$('#ambient2').val();
		}else{
			ambients+=","+$('#ambient2').val();
		}
		
	}
	if ($('#ambient3').is(":checked"))
	{
		if(ambients==""){
			ambients=$('#ambient3').val();
		}else{
			ambients+=","+$('#ambient3').val();
		}
	}
	if ($('#ambient4').is(":checked"))
	{
		if(ambients==""){
			ambients=$('#ambient4').val();
		}else{
			ambients+=","+$('#ambient4').val();
		}
	}
	
	if ($('#ambient5').is(":checked"))
	{
		if(ambients==""){
			ambients=$('#ambient6').val();
		}else{
			ambients+=","+$('#ambient6').val();
		}
	}
	changeSaveButton(true);
	$.ajax({
		// async : false,
		type : "PUT",
		url : getCont() + "request/saveRequestR5",
		timeout: 60000,
		dataType : "json",
		contentType: "application/json; charset=utf-8",
		data : JSON.stringify({
			// Informacion general
			id : $requestEditForm.find('#requestR5Id').val(),
			senders:$requestEditForm.find('#senders').val(),
			message:$requestEditForm.find('#messagePer').val(),
			ambient:ambients,
			typeChange:$("input[type='radio'][name='type']:checked").val(),
			changeService:$requestEditForm.find('#change').val(),
			justify:$requestEditForm.find('#justify').val(),
			
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
	var ambients="";
	if ($('#ambient1').is(":checked"))
	{
		if(ambients==""){
			ambients=$('#ambient1').val();
		}
		
	}
	if ($('#ambient2').is(":checked"))
	{
		if(ambients==""){
			ambients=$('#ambient2').val();
		}else{
			ambients+=","+$('#ambient2').val();
		}
		
	}
	if ($('#ambient3').is(":checked"))
	{
		if(ambients==""){
			ambients=$('#ambient3').val();
		}else{
			ambients+=","+$('#ambient3').val();
		}
	}
	if ($('#ambient4').is(":checked"))
	{
		if(ambients==""){
			ambients=$('#ambient4').val();
		}else{
			ambients+=","+$('#ambient4').val();
		}
	}
	
	if ($('#ambient5').is(":checked"))
	{
		if(ambients==""){
			ambients=$('#ambient6').val();
		}else{
			ambients+=","+$('#ambient6').val();
		}
	}
	var form = "#generateRequestForm";
	changeSaveButton(true);
	changeSaveButton(true);
	var valueSchema="";

	$.ajax({
		// async : false,
		type : "PUT",
		url : getCont() + "request/saveRequestR5",
		timeout: 60000,
		dataType : "json",
		contentType: "application/json; charset=utf-8",
		data : JSON.stringify({
			// Informacion general
			id : $requestEditForm.find('#requestR5Id').val(),
			senders:$requestEditForm.find('#senders').val(),
			message:$requestEditForm.find('#messagePer').val(),
			ambient:ambients,
			typeChange:$("input[type='radio'][name='type']:checked").val(),
			changeService:$requestEditForm.find('#change').val(),
			justify:$requestEditForm.find('#justify').val(),
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
	var ambients="";
	if ($('#ambient1').is(":checked"))
	{
		if(ambients==""){
			ambients=$('#ambient1').val();
		}
		
	}
	if ($('#ambient2').is(":checked"))
	{
		if(ambients==""){
			ambients=$('#ambient2').val();
		}else{
			ambients+=","+$('#ambient2').val();
		}
		
	}
	if ($('#ambient3').is(":checked"))
	{
		if(ambients==""){
			ambients=$('#ambient3').val();
		}else{
			ambients+=","+$('#ambient3').val();
		}
	}
	if ($('#ambient4').is(":checked"))
	{
		if(ambients==""){
			ambients=$('#ambient4').val();
		}else{
			ambients+=","+$('#ambient4').val();
		}
	}
	
	if ($('#ambient5').is(":checked"))
	{
		if(ambients==""){
			ambients=$('#ambient6').val();
		}else{
			ambients+=","+$('#ambient6').val();
		}
	}
	changeSaveButton(true);
	$.ajax({
		// async : false,
		type : "PUT",
		url : getCont() + "request/saveRequestR5",
		timeout: 60000,
		dataType : "json",
		contentType: "application/json; charset=utf-8",
		data : JSON.stringify({
			// Informacion general
			id : $requestEditForm.find('#requestR5Id').val(),
			senders:$requestEditForm.find('#senders').val(),
			message:$requestEditForm.find('#messagePer').val(),
			ambient:ambients,
			typeChange:$("input[type='radio'][name='type']:checked").val(),
			changeService:$requestEditForm.find('#change').val(),
			justify:$requestEditForm.find('#justify').val(),
			
		}),
		success : function(response) {
			responseAjaxRequestRequest(response);
			changeSaveButton(false);
			origForm = $rfcEditForm.serialize();
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


