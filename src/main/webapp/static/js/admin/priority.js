$(function() {
	activeItemMenu("releaseItem", true);
});
var $priorityTable = $('#priorityTable').DataTable({
	"language" : {
		"emptyTable" : "No existen registros",
		"zeroRecords" : "No existen registros"
	},
	"ordering" : true,
	"searching" : true,
	"paging" : true
});

var $priorityModal = $('#priorityModal');
var $priorityModalForm = $('#priorityModalForm');

function openPriorityModal() {
	resetErrors();
	$priorityModalForm[0].reset();
	$('#btnUpdatePriority').hide();
	$('#btnSavePriority').show();
	$priorityModal.modal('show');
}

function savePriority() {
	blockUI();
	$.ajax({
		type : "POST",
		url : getCont() + "admin/priority/" + "savePriority",
		data : {
			// Informacion prioridads
			id : 0,
			name : $priorityModalForm.find('#name').val(),
			description : $priorityModalForm.find('#description').val()
		},
		success : function(response) {
			ajaxSavePriority(response)
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

function ajaxSavePriority(response) {
	switch (response.status) {
	case 'success':
		location.reload();
		swal("Correcto!", "Prioridad creada correctamente.", "success", 2000)
		break;
	case 'fail':
		unblockUI();
		showPriorityErrors(response.errors, $priorityModalForm);
		break;
	case 'exception':
		swal("Error!", response.exception, "error")
		break;
	default:
		console.log(response.status);
		unblockUI();
	}
}

function updatePriorityModal(index) {
	resetErrors();
	$('#btnUpdatePriority').show();
	$('#btnSavePriority').hide();
	blockUI();
	$.ajax({
		type : "GET",
		url : getCont() + "admin/" + "priority/findPriority/" + index,
		timeout : 60000,
		data : {},
		success : function(response) {
			unblockUI();
			$priorityModalForm.find('#priorityId').val(index);
			$priorityModalForm.find('#name').val(response.name);
			$priorityModalForm.find('#description').val(response.description);
			$priorityModal.modal('show');
		},
		error : function(x, t, m) {
			notifyAjaxError(x, t, m);
		}
	});
}

function closePriorityModal() {
	$priorityModalForm[0].reset();
	$priorityModal.modal('hide');
}

function updatePriority() {
	blockUI();
	$.ajax({
		type : "POST",
		url : getCont() + "admin/priority/" + "updatePriority",
		data : {
			// Informacion prioridad
			id : $priorityModalForm.find('#priorityId').val(),
			name : $priorityModalForm.find('#name').val(),
			description : $priorityModalForm.find('#description').val()
		},
		success : function(response) {
			ajaxUpdatePriority(response)
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

function ajaxUpdatePriority(response) {
	switch (response.status) {
	case 'success':
		location.reload();
		swal("Correcto!", "Prioridad modificada correctamente.", "success", 2000)
		break;
	case 'fail':
		unblockUI();
		showPriorityErrors(response.errors, $priorityModalForm);
		break;
	case 'exception':
		swal("Error!", response.exception, "error")
		break;
	default:
		console.log(response.status);
		unblockUI();
	}
}

function confirmDeletePriority(element) {
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
				deletePriority(element);
			}		
		});
}

function deletePriority(element){
	blockUI();
	$.ajax({
		type : "DELETE",
		url : getCont() + "admin/" + "priority/deletePriority/" + element,
		timeout : 60000,
		data : {},
		success : function(response) {
			ajaxDeletePriority(response);
		},
		error : function(x, t, m) {
			notifyAjaxError(x, t, m);
		}
	});
}

function ajaxDeletePriority(response){
	switch (response.status) {
	case 'success':
		location.reload();
		swal("Correcto!", "La prioridad ha sido eliminada exitosamente.",
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

function showPriorityErrors(error, $form) {
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
