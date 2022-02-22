$(function() {
	activeItemMenu("ambientItem", true);
});
var $typeAmbientTable = $('#typeAmbientTable').DataTable({
	"language" : {
		"emptyTable" : "No existen registros",
		"zeroRecords" : "No existen registros"
	},
	"ordering" : true,
	"searching" : true,
	"paging" : true
});

var $typeAmbientModal = $('#typeAmbientModal');
var $typeAmbientModalForm = $('#typeAmbientModalForm');

function openTypeAmbientModal() {
	resetErrors();
	$typeAmbientModalForm[0].reset();
	$('#btnUpdateTypeAmbient').hide();
	$('#btnSaveTypeAmbient').show();
	$typeAmbientModal.modal('show');
}

function saveTypeAmbient() {
	blockUI();
	$.ajax({
		type : "POST",
		url : getCont() + "admin/typeAmbient/" + "saveTypeAmbient",
		data : {
			// Informacion tipo de ambientes
			id : 0,
			name : $typeAmbientModalForm.find('#name').val(),
			description : $typeAmbientModalForm.find('#description').val()
		},
		success : function(response) {
			ajaxSaveTypeAmbient(response)
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

function ajaxSaveTypeAmbient(response) {
	switch (response.status) {
	case 'success':
		location.reload();
		swal("Correcto!", "Tipo de ambiente creado correctamente.", "success", 2000)
		break;
	case 'fail':
		unblockUI();
		showTypeAmbientErrors(response.errors, $typeAmbientModalForm);
		break;
	case 'exception':
		swal("Error!", response.exception, "error")
		break;
	default:
		console.log(response.status);
		unblockUI();
	}
}

function updateTypeAmbientModal(index) {
	resetErrors();
	$('#btnUpdateTypeAmbient').show();
	$('#btnSaveTypeAmbient').hide();
	blockUI();
	$.ajax({
		type : "GET",
		url : getCont() + "admin/" + "typeAmbient/findTypeAmbient/" + index,
		timeout : 60000,
		data : {},
		success : function(response) {
			unblockUI();
			$typeAmbientModalForm.find('#typeAmbientId').val(index);
			$typeAmbientModalForm.find('#name').val(response.name);
			$typeAmbientModalForm.find('#description').val(response.description);
			$typeAmbientModal.modal('show');
		},
		error : function(x, t, m) {
			notifyAjaxError(x, t, m);
		}
	});
}

function closeTypeAmbientModal() {
	$typeAmbientModalForm[0].reset();
	$typeAmbientModal.modal('hide');
}

function updateTypeAmbient() {
	blockUI();
	$.ajax({
		type : "POST",
		url : getCont() + "admin/typeAmbient/" + "updateTypeAmbient",
		data : {
			// Informacion tipo de ambiente
			id : $typeAmbientModalForm.find('#typeAmbientId').val(),
			name : $typeAmbientModalForm.find('#name').val(),
			description : $typeAmbientModalForm.find('#description').val()
		},
		success : function(response) {
			ajaxUpdateTypeAmbient(response)
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

function ajaxUpdateTypeAmbient(response) {
	switch (response.status) {
	case 'success':
		location.reload();
		swal("Correcto!", "Tipo de ambiente modificado correctamente.", "success", 2000)
		break;
	case 'fail':
		unblockUI();
		showTypeAmbientErrors(response.errors, $typeAmbientModalForm);
		break;
	case 'exception':
		swal("Error!", response.exception, "error")
		break;
	default:
		console.log(response.status);
		unblockUI();
	}
}

function confirmDeleteTypeAmbient(element) {
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
				deleteTypeAmbient(element);
			}		
		});
}

function deleteTypeAmbient(element){
	blockUI();
	$.ajax({
		type : "DELETE",
		url : getCont() + "admin/" + "typeAmbient/deleteTypeAmbient/" + element,
		timeout : 60000,
		data : {},
		success : function(response) {
			ajaxDeleteTypeAmbient(response);
		},
		error : function(x, t, m) {
			notifyAjaxError(x, t, m);
		}
	});
}

function ajaxDeleteTypeAmbient(response){
	switch (response.status) {
	case 'success':
		location.reload();
		swal("Correcto!", "El tipo de ambiente ha sido eliminado exitosamente.",
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

function showTypeAmbientErrors(error, $form) {
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
