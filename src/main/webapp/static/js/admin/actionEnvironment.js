$(function() {
	activeItemMenu("systemItem", true);
});
var $actionEnvironmentTable = $('#actionEnvironmentTable').DataTable({
	"language" : {
		"emptyTable" : "No existen registros",
		"zeroRecords" : "No existen registros"
	},
	"ordering" : true,
	"searching" : true,
	"paging" : true
});

var $actionEnvironmentModal = $('#actionEnvironmentModal');
var $actionEnvironmentModalForm = $('#actionEnvironmentModalForm');

function openActionEnvironmentModal() {
	resetErrors();
	$actionEnvironmentModalForm[0].reset();
	$actionEnvironmentModalForm.find("#systemId").selectpicker('val', '');
	$('#btnUpdateActionEnvironment').hide();
	$('#btnSaveActionEnvironment').show();
	$actionEnvironmentModal.modal('show');
}

function saveActionEnvironment() {
	blockUI();
	$.ajax({
		type : "POST",
		url : getCont() + "admin/action/" + "saveActionEnvironment",
		data : {
			// Informacion accións
			id : 0,
			name : $actionEnvironmentModalForm.find('#name').val(),
			description : $actionEnvironmentModalForm.find('#description').val(),
			systemId: $actionEnvironmentModalForm.find("#systemId").children("option:selected").val()
		},
		success : function(response) {
			ajaxSaveActionEnvironment(response)
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

function ajaxSaveActionEnvironment(response) {
	switch (response.status) {
	case 'success':
		location.reload();
		swal("Correcto!", "Acci\u00F3n creada correctamente.", "success", 2000)
		break;
	case 'fail':
		unblockUI();
		showActionEnvironmentErrors(response.errors, $actionEnvironmentModalForm);
		break;
	case 'exception':
		swal("Error!", response.exception, "error")
		break;
	default:
		console.log(response.status);
	unblockUI();
	}
}

function updateActionEnvironmentModal(index) {
	resetErrors();
	$('#btnUpdateActionEnvironment').show();
	$('#btnSaveActionEnvironment').hide();
	blockUI();
	$.ajax({
		type : "GET",
		url : getCont() + "admin/" + "action/findActionEnvironment/" + index,
		timeout : 60000,
		data : {},
		success : function(response) {
			unblockUI();
			$actionEnvironmentModalForm.find('#actionEnvironmentId').val(index);
			$actionEnvironmentModalForm.find('#name').val(response.name);
			$actionEnvironmentModalForm.find("#systemId").selectpicker('val', response.system.id);
			$actionEnvironmentModal.modal('show');
		},
		error : function(x, t, m) {
			notifyAjaxError(x, t, m);
		}
	});
}

function closeActionEnvironmentModal() {
	$actionEnvironmentModalForm[0].reset();
	$actionEnvironmentModal.modal('hide');
}

function updateActionEnvironment() {
	blockUI();
	$.ajax({
		type : "POST",
		url : getCont() + "admin/action/" + "updateActionEnvironment",
		data : {
			// Informacion tipo de objeto
			id : $actionEnvironmentModalForm.find('#actionEnvironmentId').val(),
			name : $actionEnvironmentModalForm.find('#name').val(),
			systemId: $actionEnvironmentModalForm.find("#systemId").children("option:selected").val()
		},
		success : function(response) {
			ajaxUpdateActionEnvironment(response)
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

function ajaxUpdateActionEnvironment(response) {
	switch (response.status) {
	case 'success':
		location.reload();
		swal("Correcto!", "Acci\u00F3n modificada correctamente.", "success", 2000)
		break;
	case 'fail':
		unblockUI();
		showActionEnvironmentErrors(response.errors, $actionEnvironmentModalForm);
		break;
	case 'exception':
		swal("Error!", response.exception, "error")
		break;
	default:
		console.log(response.status);
	unblockUI();
	}
}

function confirmDeleteActionEnvironment(element) {
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
			deleteActionEnvironment(element);
		}		
	});
}

function deleteActionEnvironment(element){
	blockUI();
	$.ajax({
		type : "DELETE",
		url : getCont() + "admin/" + "action/deleteActionEnvironment/" + element,
		timeout : 60000,
		data : {},
		success : function(response) {
			ajaxDeleteActionEnvironment(response);
		},
		error : function(x, t, m) {
			notifyAjaxError(x, t, m);
		}
	});
}

function ajaxDeleteActionEnvironment(response){
	switch (response.status) {
	case 'success':
		location.reload();
		swal("Correcto!", "La acci\u00F3n ha sido eliminada exitosamente.",
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

function showActionEnvironmentErrors(error, $form) {
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
