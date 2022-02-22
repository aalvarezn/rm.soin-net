$(function() {
	activeItemMenu("systemItem", true);
});
var $moduleTable = $('#moduleTable').DataTable({
	"language" : {
		"emptyTable" : "No existen registros",
		"zeroRecords" : "No existen registros"
	},
	"ordering" : true,
	"searching" : true,
	"paging" : true
});

var $moduleModal = $('#moduleModal');
var $moduleModalForm = $('#moduleModalForm');

function openModuleModal() {
	resetErrors();
	$moduleModalForm[0].reset();
	$moduleModalForm.find("#systemId").selectpicker('val', '');
	$('#btnUpdateModule').hide();
	$('#btnSaveModule').show();
	$moduleModal.modal('show');
}

function saveModule() {
	blockUI();
	$.ajax({
		type : "POST",
		url : getCont() + "admin/module/" + "saveModule",
		data : {
			// Informacion modulos
			id : 0,
			name : $moduleModalForm.find('#name').val(),
			description : $moduleModalForm.find('#description').val(),
			systemId: $moduleModalForm.find("#systemId").children("option:selected").val()
		},
		success : function(response) {
			ajaxSaveModule(response)
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

function ajaxSaveModule(response) {
	switch (response.status) {
	case 'success':
		location.reload();
		swal("Correcto!", "Modulo creado correctamente.", "success", 2000)
		break;
	case 'fail':
		unblockUI();
		showModuleErrors(response.errors, $moduleModalForm);
		break;
	case 'exception':
		swal("Error!", response.exception, "error")
		break;
	default:
		console.log(response.status);
		unblockUI();
	}
}

function updateModuleModal(index) {
	resetErrors();
	$('#btnUpdateModule').show();
	$('#btnSaveModule').hide();
	blockUI();
	$.ajax({
		type : "GET",
		url : getCont() + "admin/" + "module/findModule/" + index,
		timeout : 60000,
		data : {},
		success : function(response) {
			unblockUI();
			$moduleModalForm.find('#moduleId').val(index);
			$moduleModalForm.find('#name').val(response.name);
			$moduleModalForm.find('#description').val(response.description);
			$moduleModalForm.find("#systemId").selectpicker('val', response.system.id);
			$moduleModal.modal('show');
		},
		error : function(x, t, m) {
			notifyAjaxError(x, t, m);
		}
	});
}

function closeModuleModal() {
	$moduleModalForm[0].reset();
	$moduleModal.modal('hide');
}

function updateModule() {
	blockUI();
	$.ajax({
		type : "POST",
		url : getCont() + "admin/module/" + "updateModule",
		data : {
			// Informacion modulo
			id : $moduleModalForm.find('#moduleId').val(),
			name : $moduleModalForm.find('#name').val(),
			description : $moduleModalForm.find('#description').val(),
			systemId: $moduleModalForm.find("#systemId").children("option:selected").val()
		},
		success : function(response) {
			ajaxUpdateModule(response)
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

function ajaxUpdateModule(response) {
	switch (response.status) {
	case 'success':
		location.reload();
		swal("Correcto!", "Modulo modificado correctamente.", "success", 2000)
		break;
	case 'fail':
		unblockUI();
		showModuleErrors(response.errors, $moduleModalForm);
		break;
	case 'exception':
		swal("Error!", response.exception, "error")
		break;
	default:
		console.log(response.status);
		unblockUI();
	}
}

function confirmDeleteModule(element) {
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
				deleteModule(element);
			}		
		});
}

function deleteModule(element){
	blockUI();
	$.ajax({
		type : "DELETE",
		url : getCont() + "admin/" + "module/deleteModule/" + element,
		timeout : 60000,
		data : {},
		success : function(response) {
			ajaxDeleteModule(response);
		},
		error : function(x, t, m) {
			notifyAjaxError(x, t, m);
		}
	});
}

function ajaxDeleteModule(response){
	switch (response.status) {
	case 'success':
		location.reload();
		swal("Correcto!", "El modulo ha sido eliminado exitosamente.",
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

function showModuleErrors(error, $form) {
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
}
