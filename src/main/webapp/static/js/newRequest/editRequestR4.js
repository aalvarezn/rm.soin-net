var $requestEditForm = $('#generateRequestForm');

var origForm=null;
var $dtUser;
var $trackingRFCForm = $('#trackingReleaseForm');
var $treeForm = $('#treeForm');
var network = null;
var nodes = [];
var edges = [];
var idUser;
$(function() {

	activeItemMenu("requestItem");
	 $('#systemId').selectpicker('val',$('#systemInfoId').val());
	 initRequestFormValidation();
	initUserTable();
	$requestEditForm.find('#update').hide();
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


	 $('#userTable tbody').on( 'click', 'tr', function () {
		
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
	var form = "#generateReleaseForm";
	
	changeSaveButton(true);
	$.ajax({
		// async : false,
		type : "PUT",
		url : getCont() + "request/saveRequest",
		timeout: 60000,
		dataType : "json",
		contentType: "application/json; charset=utf-8",
		data : JSON.stringify({
			// Informacion general
			id : $requestEditForm.find('#requestId').val(),
			senders:$requestEditForm.find('#senders').val(),
			message:$requestEditForm.find('#messagePer').val(),
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
$('#systemId').change(function() {
	
	$dtRFCs.ajax.reload();
});


function initUserTable() {
	var idRequest=$('#requestId').val();
	$dtUser = $('#userTable').DataTable(
			{
				
				'columnDefs' : [ {
					'visible' : false,
					'targets' : [ 0]
				} ],
				lengthMenu : [ [ 10, 25, 50, -1 ],
					[ '10', '25', '50', 'Mostrar todo' ] ],
					"iDisplayLength" : 10,
					"language" : optionLanguaje,
					"iDisplayStart" : 0,
					"processing" : true,
					"serverSide" : true,
					"sAjaxSource" : getCont() + "request/listUser/"+idRequest,
					"aoColumns" : [
						{
							"mDataProp" : "id",
						},
						{
							
							"mDataProp" : "name"
						},
						{
						
						"mDataProp" : "email"
					},
					{
						
						"mDataProp" : "type"
					},
					{
						
						"mDataProp" : "permissions"
					}
					,
					{
						
						"mDataProp" : "ambient.name"
					},
					{
						
						"mDataProp" : "espec"
					},
					
					{
						"mRender" : function(data, type, row, meta) {
							var options = '<div class="iconLineC">';
							
									options += options
									+ '<a onclick="showUser('
									+ row.id
									+ ')" title="Borrar"><i class="material-icons gris">mode_edit</i></a>'
									+ '<a onclick="confirmDeleteUser('
									+ row.id
									+ ')" title="Borrar"><i class="material-icons gris">delete</i></a>'
								
							
							return options;
						},
					} ],
					ordering : false,
			});
}

function showUser(id){
	$requestEditForm.find('#update').show();
	 resetSpaces();
	 	idUser=id;
		var dtUser = $('#userTable').dataTable();
		var idRow = dtUser.fnFindCellRowIndexes(id, 0); // idRow
		var data = $dtUser.row(idRow[0]).data();
		$requestEditForm.find('#user').val(data.name);
		$requestEditForm.find('#email').val(data.email);
		if(data.type==="Ambiente"){
			$("input[name=type][value='Ambiente']").prop("checked",true);
		}else if(data.type==="Aplicacion"){
			$("input[name=type][value='Aplicacion']").prop("checked",true);
		}else if(data.type==="SGRM"){
			$("input[name=type][value='SGRM']").prop("checked",true);
		}else if(data.type==="Base de datos"){
			$("input[name=type][value='Base de datos']").prop("checked",true);
		}
		
		const splitString = data.permissions.split(",");
		for(x=0;splitString.length>x;x++){
			if(splitString[x]==="Lectura"){
				$('#permission1').prop('checked',true);
			}
			if(splitString[x]==="Escritura"){
				$('#permission2').prop('checked',true);
			}
			if(splitString[x]==="Ejecucion"){
				$('#permission3').prop('checked',true);
			}
			if(splitString[x]==="Acceso"){
				$('#permission4').prop('checked',true);
			}
		}
		$requestEditForm.find('#espec').val(data.espec);
		$requestEditForm.find('#ambientId').selectpicker('val',data.ambient.id);
}

function confirmDeleteUser(index){
	Swal.fire({
		title: '\u00BFEst\u00e1s seguro que desea eliminar el registro?',
		text: 'Esta acci\u00F3n no se puede reversar.',
		...swalDefault
	}).then((result) => {
		if(result.value){
			blockUI();
			$.ajax({
				type : "DELETE",
				url : getCont() + "request/deleteUser/"+index,
				timeout : 60000,
				data : {},
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status)
					$dtUser.ajax.reload();
					//$mdSiges.modal('hide');
				},
				error : function(x, t, m) {
					unblockUI();
					
				}
			});
		}
	});
}

function modUser(){
	var permissions="";
	if ($('#permission1').is(":checked"))
	{
		if(permissions==""){
			permissions=$('#permission1').val();
		}
		
	}
	if ($('#permission2').is(":checked"))
	{
		if(permissions==""){
			permissions=$('#permission2').val();
		}else{
			permissions+=","+$('#permission2').val();
		}
		
	}
	if ($('#permission3').is(":checked"))
	{
		if(permissions==""){
			permissions=$('#permission3').val();
		}else{
			permissions+=","+$('#permission3').val();
		}
	}
	if ($('#permission4').is(":checked"))
	{
		if(permissions==""){
			permissions=$('#permission4').val();
		}else{
			permissions+=","+$('#permission4').val();
		}
	}
	if (!$requestEditForm.valid())
		return;
	Swal.fire({
		title: '\u00BFEst\u00e1s seguro que desea modificar el registro?',
		text: 'Esta acci\u00F3n no se puede reversar.',
		...swalDefault
	}).then((result) => {
		if(result.value){
			blockUI();
			$.ajax({
				type : "POST",
				url : getCont() + "request/modUser" ,
				dataType : "json",
				contentType: "application/json; charset=utf-8",
				timeout : 60000,
				data : JSON.stringify({
					id:idUser,
					name : $requestEditForm.find('#user').val(),
					email : $requestEditForm.find('#email').val(),
					type:$("input[type='radio'][name='type']:checked").val(),
					permissions:permissions,
					espec:$requestEditForm.find('#espec').val(),
					ambientId:$requestEditForm.find('#ambientId').val(),
					requestBaseId:$('#requestId').val(),
				}),
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status);
					/*window.location = getCont()
					+ "request/editRequest-"
					+ response.data;
					*/
					resetSpaces();
					$requestEditForm.find('#update').hide();
					$dtUser.ajax.reload();
					reloadPreview();
					//$mdImpact.modal('hide');
				},
				error : function(x, t, m) {
					unblockUI();
					console.log(x);
					console.log(t);
					console.log(m);
				}
			});
		}
	});
}


function addUser(){
	var permissions="";
	if ($('#permission1').is(":checked"))
	{
		if(permissions==""){
			permissions=$('#permission1').val();
		}
		
	}
	if ($('#permission2').is(":checked"))
	{
		if(permissions==""){
			permissions=$('#permission2').val();
		}else{
			permissions+=","+$('#permission2').val();
		}
		
	}
	if ($('#permission3').is(":checked"))
	{
		if(permissions==""){
			permissions=$('#permission3').val();
		}else{
			permissions+=","+$('#permission3').val();
		}
	}
	if ($('#permission4').is(":checked"))
	{
		if(permissions==""){
			permissions=$('#permission4').val();
		}else{
			permissions+=","+$('#permission4').val();
		}
	}
	if (!$requestEditForm.valid())
		return;
	Swal.fire({
		title: '\u00BFEst\u00e1s seguro que desea crear el registro?',
		text: 'Esta acci\u00F3n no se puede reversar.',
		...swalDefault
	}).then((result) => {
		if(result.value){
			blockUI();
			$.ajax({
				type : "POST",
				url : getCont() + "request/addUser" ,
				dataType : "json",
				contentType: "application/json; charset=utf-8",
				timeout : 60000,
				data : JSON.stringify({
					name : $requestEditForm.find('#user').val(),
					email : $requestEditForm.find('#email').val(),
					type:$("input[type='radio'][name='type']:checked").val(),
					permissions:permissions,
					espec:$requestEditForm.find('#espec').val(),
					ambientId:$requestEditForm.find('#ambientId').val(),
					requestBaseId:$('#requestId').val(),
				}),
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status);
					/*window.location = getCont()
					+ "request/editRequest-"
					+ response.data;
					*/
					$requestEditForm.find('#update').hide();
					resetSpaces();
					$dtUser.ajax.reload();
					reloadPreview();
					//$mdImpact.modal('hide');
				},
				error : function(x, t, m) {
					unblockUI();
					console.log(x);
					console.log(t);
					console.log(m);
				}
			});
		}
	});
}

function resetSpaces(){
	$requestEditForm.find('#user').val('');
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
			'user':{
				required : true,
				maxlength : 20,
			},
			'email':{
				required :true,
				maxlength : 20,
		        email: true
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
			
			'user' : {
				required : "Ingrese un valor",
				maxlength : "No puede poseer mas de {0} caracteres"
			},
			'email' : {
				required : "Ingrese un valor",
				maxlength : "No puede poseer mas de {0} caracteres",
				email:"Ingrese un correo v\u00E1lido"
					
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
				required:"Se debe seleccionar al menos una opci\u00F3n",		
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
	var form = "#generateRequestForm";
	changeSaveButton(true);
	changeSaveButton(true);
	var valueSchema="";

	$.ajax({
		// async : false,
		type : "PUT",
		url : getCont() + "request/saveRequest",
		timeout: 60000,
		dataType : "json",
		contentType: "application/json; charset=utf-8",
		data : JSON.stringify({
			// Informacion general
			id : $requestEditForm.find('#requestId').val(),
			senders:$requestEditForm.find('#senders').val(),
			message:$requestEditForm.find('#messagePer').val(),
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
		url : getCont() + "request/saveRequest",
		timeout: 60000,
		dataType : "json",
		contentType: "application/json; charset=utf-8",
		data : JSON.stringify({
			// Informacion general
			id : $requestEditForm.find('#requestId').val(),
			senders:$requestEditForm.find('#senders').val(),
			message:$requestEditForm.find('#messagePer').val(),
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


