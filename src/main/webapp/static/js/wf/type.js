$(function() {
	activeItemMenu("wfItem", true);
});
var $typeTable = $('#typeTable').DataTable({
	"language" : {
		"emptyTable" : "No existen registros",
		"zeroRecords" : "No existen registros"
	},
	"ordering" : true,
	"searching" : true,
	"paging" : true
});

var $typeModal = $('#typeModal');
var $typeModalForm = $('#typeModalForm');

function openTypeModal() {
	resetErrors();
	$typeModalForm[0].reset();
	$('#btnUpdateType').hide();
	$('#btnSaveType').show();
	$typeModal.modal('show');
}

function saveType() {
	blockUI();
	$.ajax({
		type : "POST",
		url : getCont() + "wf/type/" + "saveType",
		data : {
			// Informacion tipos
			id : 0,
			name : $typeModalForm.find('#name').val(),
			description : $typeModalForm.find('#description').val()
		},
		success : function(response) {
			ajaxSaveType(response)
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

function ajaxSaveType(response) {
	switch (response.status) {
	case 'success':
		location.reload();
		swal("Correcto!", "Tipo creado correctamente.", "success", 2000)
		break;
	case 'fail':
		unblockUI();
		showTypeErrors(response.errors, $typeModalForm);
		break;
	case 'exception':
		swal("Error!", response.exception, "error")
		break;
	default:
		console.log(response.status);
		unblockUI();
	}
}

function updateTypeModal(index) {
	resetErrors();
	$('#btnUpdateType').show();
	$('#btnSaveType').hide();
	blockUI();
	$.ajax({
		type : "GET",
		url : getCont() + "wf/" + "type/findType/" + index,
		timeout : 60000,
		data : {},
		success : function(response) {
			unblockUI();
			$typeModalForm.find('#typeId').val(index);
			$typeModalForm.find('#name').val(response.name);
			$typeModalForm.find('#description').val(response.description);
			$typeModal.modal('show');
		},
		error : function(x, t, m) {
			notifyAjaxError(x, t, m);
		}
	});
}

function closeTypeModal() {
	$typeModalForm[0].reset();
	$typeModal.modal('hide');
}

function updateType() {
	blockUI();
	$.ajax({
		type : "POST",
		url : getCont() + "wf/type/" + "updateType",
		data : {
			// Informacion tipo
			id : $typeModalForm.find('#typeId').val(),
			name : $typeModalForm.find('#name').val(),
			description : $typeModalForm.find('#description').val()
		},
		success : function(response) {
			ajaxUpdateType(response)
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

function ajaxUpdateType(response) {
	switch (response.status) {
	case 'success':
		location.reload();
		swal("Correcto!", "Tipo modificado correctamente.", "success", 2000)
		break;
	case 'fail':
		unblockUI();
		showTypeErrors(response.errors, $typeModalForm);
		break;
	case 'exception':
		swal("Error!", response.exception, "error")
		break;
	default:
		console.log(response.status);
		unblockUI();
	}
}

function confirmDeleteType(element) {
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
				deleteType(element);
			}		
		});
}

function deleteType(element){
	blockUI();
	$.ajax({
		type : "DELETE",
		url : getCont() + "wf/" + "type/deleteType/" + element,
		timeout : 60000,
		data : {},
		success : function(response) {
			ajaxDeleteType(response);
		},
		error : function(x, t, m) {
			notifyAjaxError(x, t, m);
		}
	});
}

function ajaxDeleteType(response){
	switch (response.status) {
	case 'success':
		location.reload();
		swal("Correcto!", "El tipo ha sido eliminado exitosamente.",
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

function showTypeErrors(error, $form) {
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
