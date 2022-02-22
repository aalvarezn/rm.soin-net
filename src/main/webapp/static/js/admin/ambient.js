$(function() {
	activeItemMenu("ambientItem", true);
});
var $ambientTable = $('#ambientTable').DataTable({
	"language" : {
		"emptyTable" : "No existen registros",
		"zeroRecords" : "No existen registros"
	},
	"ordering" : true,
	"searching" : true,
	"paging" : true
});

var $ambientModal = $('#ambientModal');
var $ambientModalForm = $('#ambientModalForm');

function openAmbientModal() {
	resetErrors();
	$ambientModalForm[0].reset();
	$ambientModalForm.find("#systemId").val(""),
	$ambientModalForm.find("#typeAmbientId").val("")
	$('.selectpicker').selectpicker('refresh');
	$('#btnUpdateAmbient').hide();
	$('#btnSaveAmbient').show();
	$ambientModal.modal('show');
}

function saveAmbient() {
	blockUI();
	$.ajax({
		type : "POST",
		url : getCont() + "admin/ambient/" + "saveAmbient",
		data : {
			// Informacion ambientes
			id : 0,
			code : $ambientModalForm.find('#code').val(),
			name : $ambientModalForm.find('#name').val(),
			details : $ambientModalForm.find('#details').val(),
			serverName : $ambientModalForm.find('#serverName').val(),
			systemId: $ambientModalForm.find("#systemId").children("option:selected").val(),
			typeAmbientId: $ambientModalForm.find("#typeAmbientId").children("option:selected").val()
		},
		success : function(response) {
			ajaxSaveAmbient(response)
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

function ajaxSaveAmbient(response) {
	switch (response.status) {
	case 'success':
		location.reload();
		swal("Correcto!", "Ambiente creado correctamente.", "success", 2000)
		break;
	case 'fail':
		unblockUI();
		showAmbientErrors(response.errors, $ambientModalForm);
		break;
	case 'exception':
		swal("Error!", response.exception, "error")
		break;
	default:
		console.log(response.status);
		unblockUI();
	}
}

function updateAmbientModal(index) {
	resetErrors();
	$('#btnUpdateAmbient').show();
	$('#btnSaveAmbient').hide();
	blockUI();
	$.ajax({
		type : "GET",
		url : getCont() + "admin/" + "ambient/findAmbient/" + index,
		timeout : 60000,
		data : {},
		success : function(response) {
			unblockUI();
			$ambientModalForm.find('#ambientId').val(index);
			$ambientModalForm.find('#systemId').val(response.system.id);
			$ambientModalForm.find('#typeAmbientId').val(response.typeAmbient.id);
			$ambientModalForm.find('#code').val(response.code);
			$ambientModalForm.find('#name').val(response.name);
			$ambientModalForm.find('#details').val(response.details);
			$ambientModalForm.find('#serverName').val(response.serverName);
			$('.selectpicker').selectpicker('refresh');
			$ambientModal.modal('show');
		},
		error : function(x, t, m) {
			notifyAjaxError(x, t, m);
		}
	});
}

function closeAmbientModal() {
	$ambientModalForm[0].reset();
	$ambientModal.modal('hide');
}

function updateAmbient() {
	blockUI();
	$.ajax({
		type : "POST",
		url : getCont() + "admin/ambient/" + "updateAmbient",
		data : {
			// Informacion ambiente
			id : $ambientModalForm.find('#ambientId').val(),
			code : $ambientModalForm.find('#code').val(),
			name : $ambientModalForm.find('#name').val(),
			details : $ambientModalForm.find('#details').val(),
			serverName : $ambientModalForm.find('#serverName').val(),
			systemId: Number($ambientModalForm.find("#systemId").children("option:selected").val()),
			typeAmbientId: Number($ambientModalForm.find("#typeAmbientId").children("option:selected").val())
		},
		success : function(response) {
			ajaxUpdateAmbient(response)
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

function ajaxUpdateAmbient(response) {
	switch (response.status) {
	case 'success':
		location.reload();
		swal("Correcto!", "Ambiente modificado correctamente.", "success", 2000)
		break;
	case 'fail':
		unblockUI();
		showAmbientErrors(response.errors, $ambientModalForm);
		break;
	case 'exception':
		swal("Error!", response.exception, "error")
		break;
	default:
		console.log(response.status);
		unblockUI();
	}
}

function confirmDeleteAmbient(element) {
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
				deleteAmbient(element);
			}		
		});
}

function deleteAmbient(element){
	blockUI();
	$.ajax({
		type : "DELETE",
		url : getCont() + "admin/" + "ambient/deleteAmbient/" + element,
		timeout : 60000,
		data : {},
		success : function(response) {
			ajaxDeleteAmbient(response);
		},
		error : function(x, t, m) {
			notifyAjaxError(x, t, m);
		}
	});
}

function ajaxDeleteAmbient(response){
	switch (response.status) {
	case 'success':
		location.reload();
		swal("Correcto!", "El ambiente ha sido eliminado exitosamente.",
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

function showAmbientErrors(error, $form) {
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
