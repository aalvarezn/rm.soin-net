$(function() {
	activeItemMenu("releaseItem", true);
});
var $statusTable = $('#statusTable').DataTable({
	"language" : {
		"emptyTable" : "No existen registros",
		"zeroRecords" : "No existen registros"
	},
	"ordering" : true,
	"searching" : true,
	"paging" : true
});

var $statusModal = $('#statusModal');
var $statusModalForm = $('#statusModalForm');

function openStatusModal() {
	resetErrors();
	$statusModalForm[0].reset();
	$('#btnUpdateStatus').hide();
	$('#btnSaveStatus').show();
	$statusModal.modal('show');
}

function saveStatus() {
	blockUI();
	$.ajax({
		type : "POST",
		url : getCont() + "admin/status/" + "saveStatus",
		data : {
			// Informacion estados
			id : 0,
			name : $statusModalForm.find('#name').val(),
			description : $statusModalForm.find('#description').val()
		},
		success : function(response) {
			ajaxSaveStatus(response)
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

function ajaxSaveStatus(response) {
	switch (response.status) {
	case 'success':
		location.reload();
		swal("Correcto!", "Estado creado correctamente.", "success", 2000)
		break;
	case 'fail':
		unblockUI();
		showStatusErrors(response.errors, $statusModalForm);
		break;
	case 'exception':
		swal("Error!", response.exception, "error")
		break;
	default:
		console.log(response.status);
		unblockUI();
	}
}

function updateStatusModal(index) {
	resetErrors();
	$('#btnUpdateStatus').show();
	$('#btnSaveStatus').hide();
	blockUI();
	$.ajax({
		type : "GET",
		url : getCont() + "admin/" + "status/findStatus/" + index,
		timeout : 60000,
		data : {},
		success : function(response) {
			unblockUI();
			$statusModalForm.find('#statusId').val(index);
			$statusModalForm.find('#name').val(response.name);
			$statusModalForm.find('#description').val(response.description);
			$statusModal.modal('show');
		},
		error : function(x, t, m) {
			notifyAjaxError(x, t, m);
		}
	});
}

function closeStatusModal() {
	$statusModalForm[0].reset();
	$statusModal.modal('hide');
}

function updateStatus() {
	blockUI();
	$.ajax({
		type : "POST",
		url : getCont() + "admin/status/" + "updateStatus",
		data : {
			// Informacion estado
			id : $statusModalForm.find('#statusId').val(),
			name : $statusModalForm.find('#name').val(),
			description : $statusModalForm.find('#description').val()
		},
		success : function(response) {
			ajaxUpdateStatus(response)
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

function ajaxUpdateStatus(response) {
	switch (response.status) {
	case 'success':
		location.reload();
		swal("Correcto!", "Estado modificado correctamente.", "success", 2000)
		break;
	case 'fail':
		unblockUI();
		showStatusErrors(response.errors, $statusModalForm);
		break;
	case 'exception':
		swal("Error!", response.exception, "error")
		break;
	default:
		console.log(response.status);
		unblockUI();
	}
}

function confirmDeleteStatus(element) {
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
				deleteStatus(element);
			}		
		});
}

function deleteStatus(element){
	blockUI();
	$.ajax({
		type : "DELETE",
		url : getCont() + "admin/" + "status/deleteStatus/" + element,
		timeout : 60000,
		data : {},
		success : function(response) {
			ajaxDeleteStatus(response);
		},
		error : function(x, t, m) {
			notifyAjaxError(x, t, m);
		}
	});
}

function ajaxDeleteStatus(response){
	switch (response.status) {
	case 'success':
		location.reload();
		swal("Correcto!", "El estado ha sido eliminado exitosamente.",
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

function showStatusErrors(error, $form) {
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
