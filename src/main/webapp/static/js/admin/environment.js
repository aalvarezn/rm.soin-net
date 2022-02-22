$(function() {
	activeItemMenu("systemItem", true);
});
var $environmentTable = $('#environmentTable').DataTable({
	"language" : {
		"emptyTable" : "No existen registros",
		"zeroRecords" : "No existen registros"
	},
	"ordering" : true,
	"searching" : true,
	"paging" : true
});

var $environmentModal = $('#environmentModal');
var $environmentModalForm = $('#environmentModalForm');

$(function() {
	$environmentModal.find('input[type="checkbox"]').change(function() {
		if (this.checked) {
			$(this).val(1);
		} else {
			$(this).val(0);
		}
	});
});

function openEnvironmentModal() {
	resetErrors();
	$environmentModalForm[0].reset();
	$environmentModalForm.find("#systemId").selectpicker('val', '');
	$environmentModalForm.find('input[type="checkbox"]').val('0');
	$('#btnUpdateEnvironment').hide();
	$('#btnSaveEnvironment').show();
	$environmentModal.modal('show');
}

function saveEnvironment() {
	blockUI();
	$.ajax({
		type : "POST",
		url : getCont() + "admin/environment/" + "saveEnvironment",
		data : {
			// Informacion tipo de objetos
			id : 0,
			name : $environmentModalForm.find('#name').val(),
			description : $environmentModalForm.find('#description').val(),
			systemId: $environmentModalForm.find("#systemId").children("option:selected").val(),
			external : boolean($environmentModalForm.find('#external').val())
		},
		success : function(response) {
			ajaxSaveEnvironment(response)
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

function ajaxSaveEnvironment(response) {
	switch (response.status) {
	case 'success':
		location.reload();
		swal("Correcto!", "Tipo de objeto creado correctamente.", "success", 2000)
		break;
	case 'fail':
		unblockUI();
		showEnvironmentErrors(response.errors, $environmentModalForm);
		break;
	case 'exception':
		swal("Error!", response.exception, "error")
		break;
	default:
		console.log(response.status);
	unblockUI();
	}
}

function updateEnvironmentModal(index) {
	resetErrors();
	$('#btnUpdateEnvironment').show();
	$('#btnSaveEnvironment').hide();
	$environmentModalForm.find('input[type="checkbox"]').val('0');
	blockUI();
	$.ajax({
		type : "GET",
		url : getCont() + "admin/" + "environment/findEnvironment/" + index,
		timeout : 60000,
		data : {},
		success : function(response) {
			unblockUI();
			$environmentModalForm.find('#environmentId').val(index);
			$environmentModalForm.find('#name').val(response.name);
			$environmentModalForm.find('#description').val(response.description);
			$environmentModalForm.find("#systemId").selectpicker('val', response.system.id);
			if (response.external)
				activeInputCheckbox($environmentModalForm, 'external');
			$environmentModal.modal('show');
		},
		error : function(x, t, m) {
			notifyAjaxError(x, t, m);
		}
	});
}

function closeEnvironmentModal() {
	$environmentModalForm[0].reset();
	$environmentModal.modal('hide');
}

function updateEnvironment() {
	blockUI();
	$.ajax({
		type : "POST",
		url : getCont() + "admin/environment/" + "updateEnvironment",
		data : {
			// Informacion tipo de objeto
			id : $environmentModalForm.find('#environmentId').val(),
			name : $environmentModalForm.find('#name').val(),
			description : $environmentModalForm.find('#description').val(),
			systemId: $environmentModalForm.find("#systemId").children("option:selected").val(),
			external : boolean($environmentModalForm.find('#external').val())
		},
		success : function(response) {
			ajaxUpdateEnvironment(response)
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

function ajaxUpdateEnvironment(response) {
	switch (response.status) {
	case 'success':
		location.reload();
		swal("Correcto!", "Tipo de objeto modificado correctamente.", "success", 2000)
		break;
	case 'fail':
		unblockUI();
		showEnvironmentErrors(response.errors, $environmentModalForm);
		break;
	case 'exception':
		swal("Error!", response.exception, "error")
		break;
	default:
		console.log(response.status);
	unblockUI();
	}
}

function confirmDeleteEnvironment(element) {
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
			deleteEnvironment(element);
		}		
	});
}

function deleteEnvironment(element){
	blockUI();
	$.ajax({
		type : "DELETE",
		url : getCont() + "admin/" + "environment/deleteEnvironment/" + element,
		timeout : 60000,
		data : {},
		success : function(response) {
			ajaxDeleteEnvironment(response);
		},
		error : function(x, t, m) {
			notifyAjaxError(x, t, m);
		}
	});
}

function ajaxDeleteEnvironment(response){
	switch (response.status) {
	case 'success':
		location.reload();
		swal("Correcto!", "El tipo de objeto ha sido eliminado exitosamente.",
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

function showEnvironmentErrors(error, $form) {
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
