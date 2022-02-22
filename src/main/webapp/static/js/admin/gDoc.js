$(function() {
	activeItemMenu("documentItem", true);
});
var $gDocTable = $('#gDocTable').DataTable({
	"language" : {
		"emptyTable" : "No existen registros",
		"zeroRecords" : "No existen registros"
	},
	"ordering" : true,
	"searching" : true,
	"paging" : true
});

var $gDocModal = $('#gDocModal');
var $gDocModalForm = $('#gDocModalForm');

function openGDocModal() {
	resetErrors();
	$gDocModalForm[0].reset();
	$gDocModalForm.find("#proyectId").selectpicker('val', '');
	$('#btnUpdateGDoc').hide();
	$('#btnSaveGDoc').show();
	$gDocModal.modal('show');
}

function saveGDoc() {
	blockUI();
	$.ajax({
		type : "POST",
		url : getCont() + "admin/gDoc/" + "saveGDoc",
		data : {
			// Informacion gDocs
			id : 0,
			description: $gDocModalForm.find('#description').val(),
			credentials: $gDocModalForm.find('#credentials').val(),
			spreadSheet: $gDocModalForm.find('#spreadSheet').val(),
			proyectId: $gDocModalForm.find("#proyectId").children("option:selected").val()
		},
		success : function(response) {
			ajaxSaveGDoc(response)
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

function ajaxSaveGDoc(response) {
	switch (response.status) {
	case 'success':
		location.reload();
		$gDocModal.modal('hide');
		swal("Correcto!", "GDoc creado correctamente.", "success", 2000)
		break;
	case 'fail':
		unblockUI();
		showGDocErrors(response.errors, $gDocModalForm);
		break;
	case 'exception':
		swal("Error!", response.exception, "error")
		break;
	default:
		console.log(response.status);
	unblockUI();
	}
}

function updateGDocModal(index) {
	resetErrors();
	$('#btnUpdateGDoc').show();
	$('#btnSaveGDoc').hide();
	blockUI();
	$.ajax({
		type : "GET",
		url : getCont() + "admin/" + "gDoc/findGDoc/" + index,
		timeout : 60000,
		data : {},
		success : function(response) {
			unblockUI();
			$gDocModalForm.find('#gDocId').val(index);
			$gDocModalForm.find('#description').val(response.description);
			$gDocModalForm.find('#credentials').val(response.credentials);
			$gDocModalForm.find('#spreadSheet').val(response.spreadSheet);
			$gDocModalForm.find("#proyectId").selectpicker('val', response.proyect.id);
			$gDocModal.modal('show');
		},
		error : function(x, t, m) {
			notifyAjaxError(x, t, m);
		}
	});
}

function closeGDocModal() {
	$gDocModalForm[0].reset();
	$gDocModal.modal('hide');
}

function updateGDoc() {
	blockUI();
	$.ajax({
		type : "POST",
		url : getCont() + "admin/gDoc/" + "updateGDoc",
		data : {
			// Informacion gDoc
			id: $gDocModalForm.find('#gDocId').val(),
			description: $gDocModalForm.find('#description').val(),
			credentials: $gDocModalForm.find('#credentials').val(),
			spreadSheet: $gDocModalForm.find('#spreadSheet').val(),
			proyectId: $gDocModalForm.find("#proyectId").children("option:selected").val()
		},
		success : function(response) {
			ajaxUpdateGDoc(response)
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

function ajaxUpdateGDoc(response) {
	switch (response.status) {
	case 'success':
		location.reload();
		$gDocModal.modal('hide');
		swal("Correcto!", "GDoc modificado correctamente.", "success", 2000)
		break;
	case 'fail':
		unblockUI();
		showGDocErrors(response.errors, $gDocModalForm);
		break;
	case 'exception':
		swal("Error!", response.exception, "error")
		break;
	default:
		console.log(response.status);
	unblockUI();
	}
}

function confirmDeleteGDoc(element) {
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
			deleteGDoc(element);
		}		
	});
}

function deleteGDoc(element){
	blockUI();
	$.ajax({
		type : "DELETE",
		url : getCont() + "admin/" + "gDoc/deleteGDoc/" + element,
		timeout : 60000,
		data : {},
		success : function(response) {
			ajaxDeleteGDoc(response);
		},
		error : function(x, t, m) {
			notifyAjaxError(x, t, m);
		}
	});
}

function ajaxDeleteGDoc(response){
	switch (response.status) {
	case 'success':
		location.reload();
		swal("Correcto!", "El gDoc ha sido eliminado exitosamente.",
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

function showGDocErrors(error, $form) {
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
