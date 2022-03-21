//PREGUNTAR SI HAY QUE HACERLO ASI O VOLVERLO A COMO ANTES

var $dtDocTemplate;
var $mdDocTemplate = $('#docTemplateModal');
var $fmDocTemplate = $('#docTemplateModalForm');
$(document).ready(function () {
	
	activeItemMenu("documentItem", true);
	initDataTable();
	initDocTemplate();
});



function initDataTable() {
	$dtDocTemplate = $('#docTemplateTable')
	.DataTable(
			{
				lengthMenu : [ [ 10, 25, 50, -1 ],
					[ '10', '25', '50', 'Mostrar todo' ] ],
					"iDisplayLength" : 10,
					"language" : optionLanguaje,
					"iDisplayStart" : 0,
					"sAjaxSource" : getCont() + "admin/docTemplate/list",
					"fnServerParams" : function(aoData) {
					},
					"aoColumns" : [
						{
							"mDataProp" : 'code'
						},
						{
							"mDataProp" : 'name'
						},
						{
							"mDataProp" : 'wordGenerator'
						},
						{
							"mDataProp" : 'sufix'
						},
						{
							render : function(data, type, row, meta) {
								var options = '<div class="iconLine">';

								options += '<a onclick="showDocTemplate('
									+ meta.row
									+ ')" title="Editar"><i class="material-icons gris">mode_edit</i></a>';

								options += '<a onclick="deleteDocTemplate('
									+ meta.row
									+ ')" title="Borrar"><i class="material-icons gris">delete</i></a>';

								options += ' </div>';

								return options;
							}
						} ],
						ordering : false,
			});
}

function showDocTemplate(index){
	$fmDocTemplate.validate().resetForm();
	$fmDocTemplate[0].reset();
	var obj = $dtDocTemplate.row(index).data();
	$fmDocTemplate.find('#dId').val(obj.id);
	$fmDocTemplate.find('#dCode').val(obj.code);
	$fmDocTemplate.find('#dName').val(obj.name);
	$fmDocTemplate.find('#dComponentGenerator').val(obj.wordGenerator);
	$fmDocTemplate.find('#dSufix').val(obj.sufix);
	$mdDocTemplate.find('#update').show();
	$mdDocTemplate.find('#save').hide();
	$mdDocTemplate.modal('show');
}

function updateDocTemplate() {
	if (!$fmDocTemplate.valid())
		return;
	Swal.fire({
		title: '\u00BFEst\u00e1s seguro que desea actualizar el registro?',
		text: 'Esta acci\u00F3n no se puede reversar.',
		...swalDefault
	}).then((result) => {
		if(result.value){
			blockUI();
			$.ajax({
				type : "PUT",
				url : getCont() + "admin/docTemplate/" ,
				dataType : "json",
				contentType: "application/json; charset=utf-8",
				timeout : 60000,
				data : JSON.stringify({
					 id : $fmDocTemplate.find('#dId').val(),
					 code: $fmDocTemplate.find('#dCode').val(),
					 name:	$fmDocTemplate.find('#dName').val(),
					 wordGenerator: $fmDocTemplate.find('#dComponentGenerator').val(),
					 sufix: $fmDocTemplate.find('#dSufix').val()
				}),
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status)
					$dtDocTemplate.ajax.reload();
					$mdDocTemplate.modal('hide');
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


function saveDocTemplate() {
	if (!$fmDocTemplate.valid())
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
				url : getCont() + "admin/docTemplate/" ,
				dataType : "json",
				contentType: "application/json; charset=utf-8",
				timeout : 60000,
				data : JSON.stringify({
			     code: $fmDocTemplate.find('#dCode').val(),
				 name:	$fmDocTemplate.find('#dName').val(),
				 wordGenerator: $fmDocTemplate.find('#dComponentGenerator').val(),
				 sufix: $fmDocTemplate.find('#dSufix').val(),
				}),
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status)
					$dtDocTemplate.ajax.reload();
					$mdDocTemplate.modal('hide');
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

function deleteDocTemplate(index) {
	var obj = $dtDocTemplate.row(index).data();
	Swal.fire({
		title: '\u00BFEst\u00e1s seguro que desea eliminar el registro?',
		text: 'Esta acci\u00F3n no se puede reversar.',
		...swalDefault
	}).then((result) => {
		if(result.value){
			blockUI();
			$.ajax({
				type : "DELETE",
				url : getCont() + "admin/docTemplate/"+obj.id ,
				timeout : 60000,
				data : {},
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status)
					$dtDocTemplate.ajax.reload();
					$mdDocTemplate.modal('hide');
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

function addDocTemplate(){
	$fmDocTemplate.validate().resetForm();
	$fmDocTemplate[0].reset();
	$mdDocTemplate.find('#save').show();
	$mdDocTemplate.find('#update').hide();
	$mdDocTemplate.modal('show');
}

function closeDocTemplate(){
	$mdDocTemplate.modal('hide');
}

function initDocTemplate() {
	$fmDocTemplate.validate({
		rules : {
			'dCode' : {
				required : true,
				minlength : 1,
				maxlength : 100,
			},
			'dName' : {
				required : true,
				minlength : 1,
				maxlength : 100
			},
			'dComponentGenerator' : {
				required : true,
				minlength : 1,
				maxlength : 100
			},
			'dSufix' : {
				required : true,
				minlength : 1,
				maxlength : 50
			}
		},
		messages : {
			'dCode' : {
				required :  "Ingrese un valor",
				minlength : "Ingrese un valor",
				maxlength : "No puede poseer mas de {0} caracteres"
			},
			'dName' : {
				required : "Ingrese un valor",
				minlength : "Ingrese un valor",
				maxlength : "No puede poseer mas de {0} caracteres"
			},
			'dComponentGenerator' : {
				required : "Ingrese un valor",
				minlength : "Ingrese un valor",
				maxlength : "No puede poseer mas de {0} caracteres"
			},
			'dSufix' : {
				required : "Ingrese un valor",
				minlength : "Ingrese un valor",
			},
		},
		highlight,
		unhighlight,
		errorPlacement
	});
}




/*
$(function() {
	activeItemMenu("documentItem", true);
});
var $docTemplateTable = $('#docTemplateTable').DataTable({
	"language" : {
		"emptyTable" : "No existen registros",
		"zeroRecords" : "No existen registros"
	},
	"ordering" : true,
	"searching" : true,
	"paging" : true
});

var $docTemplateModal = $('#docTemplateModal');
var $docTemplateModalForm = $('#docTemplateModalForm');

function openDocTemplateModal() {
	resetErrors();
	$docTemplateModalForm[0].reset();
	$docTemplateModalForm.find("#systemId").selectpicker('val', '');
	$('#btnUpdateDocTemplate').hide();
	$('#btnSaveDocTemplate').show();
	$docTemplateModal.modal('show');
}

function saveDocTemplate() {
	blockUI();
	$.ajax({
		type : "POST",
		url : getCont() + "admin/docTemplate/" + "saveDocTemplate",
		data : {
			// Informacion plantilla de releases
			id : 0,
			name : $docTemplateModalForm.find('#name').val(),
			templateName : $docTemplateModalForm.find('#templateName').val(),
			componentGenerator : $docTemplateModalForm.find('#componentGenerator').val(),
			sufix : $docTemplateModalForm.find('#sufix').val(),
			systemId: $docTemplateModalForm.find("#systemId").children("option:selected").val()
		},
		success : function(response) {
			ajaxSaveDocTemplate(response)
		},
		error : function(x, t, m) {
			console.log(x);
			console.log(t);
			console.log(m);
			unblockUI();
			notifyAjaxError(x, t, m);
		}
	});
}

function ajaxSaveDocTemplate(response) {
	switch (response.status) {
	case 'success':
		location.reload();
		swal("Correcto!", "Plantilla de release creada correctamente.", "success", 2000)
		break;
	case 'fail':
		unblockUI();
		showDocTemplateErrors(response.errors, $docTemplateModalForm);
		break;
	case 'exception':
		swal("Error!", response.exception, "error")
		break;
	default:
		console.log(response.status);
		unblockUI();
	}
}

function updateDocTemplateModal(index) {
	resetErrors();
	$('#btnUpdateDocTemplate').show();
	$('#btnSaveDocTemplate').hide();
	blockUI();
	$.ajax({
		type : "GET",
		url : getCont() + "admin/" + "docTemplate/findDocTemplate/" + index,
		timeout : 60000,
		data : {},
		success : function(response) {
			unblockUI();
			$docTemplateModalForm.find('#docTemplateId').val(index);
			$docTemplateModalForm.find('#name').val(response.name);
			$docTemplateModalForm.find('#templateName').val(response.templateName);
			$docTemplateModalForm.find('#componentGenerator').val(response.componentGenerator);
			$docTemplateModalForm.find('#sufix').val(response.sufix);
			$docTemplateModalForm.find("#systemId").selectpicker('val', response.system.id);
			$docTemplateModal.modal('show');
		},
		error : function(x, t, m) {
			notifyAjaxError(x, t, m);
		}
	});
}

function closeDocTemplateModal() {
	$docTemplateModalForm[0].reset();
	$docTemplateModal.modal('hide');
}

function updateDocTemplate() {
	blockUI();
	$.ajax({
		type : "POST",
		url : getCont() + "admin/docTemplate/" + "updateDocTemplate",
		data : {
			// Informacion plantilla de release
			id : $docTemplateModalForm.find('#docTemplateId').val(),
			name : $docTemplateModalForm.find('#name').val(),
			templateName : $docTemplateModalForm.find('#templateName').val(),
			componentGenerator : $docTemplateModalForm.find('#componentGenerator').val(),
			sufix : $docTemplateModalForm.find('#sufix').val(),
			systemId: $docTemplateModalForm.find("#systemId").children("option:selected").val()
		},
		success : function(response) {
			ajaxUpdateDocTemplate(response)
		},
		error : function(x, t, m) {
			console.log(x);
			console.log(t);
			console.log(m);
			unblockUI();
			notifyAjaxError(x, t, m);
		}
	});
}

function ajaxUpdateDocTemplate(response) {
	switch (response.status) {
	case 'success':
		location.reload();
		swal("Correcto!", "Plantilla de release modificada correctamente.", "success", 2000)
		break;
	case 'fail':
		unblockUI();
		showDocTemplateErrors(response.errors, $docTemplateModalForm);
		break;
	case 'exception':
		swal("Error!", response.exception, "error")
		break;
	default:
		console.log(response.status);
		unblockUI();
	}
}

function confirmDeleteDocTemplate(element) {
	Swal.fire({
		  title: '\u00BFEst\u00e1s seguro que desea eliminar?',
		  text: "Esta acci\u00F3n no se puede reversar.",
		  icon: 'question',
		  showCancelButton: true,
		  customClass: 'swal-wide',
		  cancelButtonText: 'Cancelar',
		  cancelButtonColor: '#f14747',
		  confirmButtonColor: '#3085d6',
		  confirmButtonText: 'Aceptar',
		}).then((result) => {
			if(result.value){
				deleteDocTemplate(element);
			}		
		});
}

function deleteDocTemplate(element){
	blockUI();
	$.ajax({
		type : "DELETE",
		url : getCont() + "admin/" + "docTemplate/deleteDocTemplate/" + element,
		timeout : 60000,
		data : {},
		success : function(response) {
			ajaxDeleteDocTemplate(response);
		},
		error : function(x, t, m) {
			notifyAjaxError(x, t, m);
		}
	});
}

function ajaxDeleteDocTemplate(response){
	switch (response.status) {
	case 'success':
		location.reload();
		swal("Correcto!", "La plantilla de release ha sido eliminada exitosamente.",
				"success", 2000)
		break;
	case 'fail':
		swal("Error!", response.exception, "error")
		break;
	case 'exception':
		swal("Error!", response.exception, "warning")
		break;
	default:
		location.reload();
	}
}

function resetErrors() {
	$(".fieldError").css("visibility", "hidden");
	$(".fieldError").attr("class", "error fieldError");
	$(".fieldErrorLine").attr("class", "form-line");
}

function showDocTemplateErrors(error, $form) {
	resetErrors();// Eliminamos las etiquetas de errores previas
	for (var i = 0; i < error.length; i++) {
		// Se modifica el texto de la advertencia y se agrega la de activeError
		$form.find(" #" + error[i].key + "_error").text(error[i].message);
		$form.find(" #" + error[i].key + "_error").css("visibility", "visible");
		$form.find(" #" + error[i].key + "_error").attr("class",
				"error fieldError activeError");
		// Si es input||textarea se marca el line en rojo
		if ($form.find(" #" + error[i].key).is("input")
				|| $form.find(" #" + error[i].key).is("textarea")) {
			$form.find(" #" + error[i].key).parent().attr("class",
					"form-line error focused fieldErrorLine");
		}
	}
}*/
