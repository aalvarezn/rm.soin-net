$(function() {
	activeItemMenu("systemItem", true);
});
var $configurationItemTable = $('#configurationItemTable').DataTable({
	"language" : {
		"emptyTable" : "No existen registros",
		"zeroRecords" : "No existen registros"
	},
	"ordering" : true,
	"searching" : true,
	"paging" : true
});

var $configurationItemModal = $('#configurationItemModal');
var $configurationItemModalForm = $('#configurationItemModalForm');

function openConfigurationItemModal() {
	resetErrors();
	$configurationItemModalForm[0].reset();
	$configurationItemModalForm.find("#systemId").selectpicker('val', '');
	$('#btnUpdateConfigurationItem').hide();
	$('#btnSaveConfigurationItem').show();
	$configurationItemModal.modal('show');
}

function saveConfigurationItem() {
	blockUI();
	$.ajax({
		type : "POST",
		url : getCont() + "admin/configurationItem/" + "saveConfigurationItem",
		data : {
			// Informacion item de configuracions
			id : 0,
			name : $configurationItemModalForm.find('#name').val(),
			description : $configurationItemModalForm.find('#description').val(),
			systemId: $configurationItemModalForm.find("#systemId").children("option:selected").val()
		},
		success : function(response) {
			ajaxSaveConfigurationItem(response)
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

function ajaxSaveConfigurationItem(response) {
	switch (response.status) {
	case 'success':
		location.reload();
		swal("Correcto!", "Item de configuraci\u00F3n creado correctamente.", "success", 2000)
		break;
	case 'fail':
		unblockUI();
		showConfigurationItemErrors(response.errors, $configurationItemModalForm);
		break;
	case 'exception':
		swal("Error!", response.exception, "error")
		break;
	default:
		console.log(response.status);
		unblockUI();
	}
}

function updateConfigurationItemModal(index) {
	resetErrors();
	$('#btnUpdateConfigurationItem').show();
	$('#btnSaveConfigurationItem').hide();
	blockUI();
	$.ajax({
		type : "GET",
		url : getCont() + "admin/" + "configurationItem/findConfigurationItem/" + index,
		timeout : 60000,
		data : {},
		success : function(response) {
			unblockUI();
			$configurationItemModalForm.find('#configurationItemId').val(index);
			$configurationItemModalForm.find('#name').val(response.name);
			$configurationItemModalForm.find('#description').val(response.description);
			$configurationItemModalForm.find("#systemId").selectpicker('val', response.system.id);
			$configurationItemModal.modal('show');
		},
		error : function(x, t, m) {
			notifyAjaxError(x, t, m);
		}
	});
}

function closeConfigurationItemModal() {
	$configurationItemModalForm[0].reset();
	$configurationItemModal.modal('hide');
}

function updateConfigurationItem() {
	blockUI();
	$.ajax({
		type : "POST",
		url : getCont() + "admin/configurationItem/" + "updateConfigurationItem",
		data : {
			// Informacion item de configuracion
			id : $configurationItemModalForm.find('#configurationItemId').val(),
			name : $configurationItemModalForm.find('#name').val(),
			description : $configurationItemModalForm.find('#description').val(),
			systemId: $configurationItemModalForm.find("#systemId").children("option:selected").val()
		},
		success : function(response) {
			ajaxUpdateConfigurationItem(response)
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

function ajaxUpdateConfigurationItem(response) {
	switch (response.status) {
	case 'success':
		location.reload();
		swal("Correcto!", "Item de configuraci\u00F3n modificado correctamente.", "success", 2000)
		break;
	case 'fail':
		unblockUI();
		showConfigurationItemErrors(response.errors, $configurationItemModalForm);
		break;
	case 'exception':
		swal("Error!", response.exception, "error")
		break;
	default:
		console.log(response.status);
		unblockUI();
	}
}

function confirmDeleteConfigurationItem(element) {
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
				deleteConfigurationItem(element);
			}		
		});
}

function deleteConfigurationItem(element){
	blockUI();
	$.ajax({
		type : "DELETE",
		url : getCont() + "admin/" + "configurationItem/deleteConfigurationItem/" + element,
		timeout : 60000,
		data : {},
		success : function(response) {
			ajaxDeleteConfigurationItem(response);
		},
		error : function(x, t, m) {
			notifyAjaxError(x, t, m);
		}
	});
}

function ajaxDeleteConfigurationItem(response){
	switch (response.status) {
	case 'success':
		location.reload();
		swal("Correcto!", "El item de configuraci\u00F3n ha sido eliminado exitosamente.",
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

function showConfigurationItemErrors(error, $form) {
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
