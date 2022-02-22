$(function() {
	activeItemMenu("systemItem", true);
});
var $typeObjectTable = $('#typeObjectTable').DataTable({
	"language" : {
		"emptyTable" : "No existen registros",
		"zeroRecords" : "No existen registros"
	},
	"ordering" : true,
	"searching" : true,
	"paging" : true
});

var $typeObjectModal = $('#typeObjectModal');
var $typeObjectModalForm = $('#typeObjectModalForm');

function openTypeObjectModal() {
	resetErrors();
	$typeObjectModalForm[0].reset();
	$typeObjectModalForm.find("#systemId").selectpicker('val', '');
	$('#btnUpdateTypeObject').hide();
	$('#btnSaveTypeObject').show();
	$typeObjectModal.modal('show');
}

function saveTypeObject() {
	blockUI();
	$.ajax({
		type : "POST",
		url : getCont() + "admin/typeObject/" + "saveTypeObject",
		data : {
			// Informacion tipo de objetos
			id : 0,
			name : $typeObjectModalForm.find('#name').val(),
			description : $typeObjectModalForm.find('#description').val(),
			systemId: $typeObjectModalForm.find("#systemId").children("option:selected").val()
		},
		success : function(response) {
			ajaxSaveTypeObject(response)
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

function ajaxSaveTypeObject(response) {
	switch (response.status) {
	case 'success':
		location.reload();
		swal("Correcto!", "Tipo de objeto creado correctamente.", "success", 2000)
		break;
	case 'fail':
		unblockUI();
		showTypeObjectErrors(response.errors, $typeObjectModalForm);
		break;
	case 'exception':
		swal("Error!", response.exception, "error")
		break;
	default:
		console.log(response.status);
		unblockUI();
	}
}

function updateTypeObjectModal(index) {
	resetErrors();
	$('#btnUpdateTypeObject').show();
	$('#btnSaveTypeObject').hide();
	blockUI();
	$.ajax({
		type : "GET",
		url : getCont() + "admin/" + "typeObject/findTypeObject/" + index,
		timeout : 60000,
		data : {},
		success : function(response) {
			unblockUI();
			$typeObjectModalForm.find('#typeObjectId').val(index);
			$typeObjectModalForm.find('#name').val(response.name);
			$typeObjectModalForm.find('#description').val(response.description);
			$typeObjectModalForm.find("#systemId").selectpicker('val', response.system.id);
			$typeObjectModal.modal('show');
		},
		error : function(x, t, m) {
			notifyAjaxError(x, t, m);
		}
	});
}

function closeTypeObjectModal() {
	$typeObjectModalForm[0].reset();
	$typeObjectModal.modal('hide');
}

function updateTypeObject() {
	blockUI();
	$.ajax({
		type : "POST",
		url : getCont() + "admin/typeObject/" + "updateTypeObject",
		data : {
			// Informacion tipo de objeto
			id : $typeObjectModalForm.find('#typeObjectId').val(),
			name : $typeObjectModalForm.find('#name').val(),
			description : $typeObjectModalForm.find('#description').val(),
			systemId: $typeObjectModalForm.find("#systemId").children("option:selected").val()
		},
		success : function(response) {
			ajaxUpdateTypeObject(response)
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

function ajaxUpdateTypeObject(response) {
	switch (response.status) {
	case 'success':
		location.reload();
		swal("Correcto!", "Tipo de objeto modificado correctamente.", "success", 2000)
		break;
	case 'fail':
		unblockUI();
		showTypeObjectErrors(response.errors, $typeObjectModalForm);
		break;
	case 'exception':
		swal("Error!", response.exception, "error")
		break;
	default:
		console.log(response.status);
		unblockUI();
	}
}

function confirmDeleteTypeObject(element) {
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
				deleteTypeObject(element);
			}		
		});
}

function deleteTypeObject(element){
	blockUI();
	$.ajax({
		type : "DELETE",
		url : getCont() + "admin/" + "typeObject/deleteTypeObject/" + element,
		timeout : 60000,
		data : {},
		success : function(response) {
			ajaxDeleteTypeObject(response);
		},
		error : function(x, t, m) {
			notifyAjaxError(x, t, m);
		}
	});
}

function ajaxDeleteTypeObject(response){
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

function showTypeObjectErrors(error, $form) {
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
