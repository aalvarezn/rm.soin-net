$(function() {
	activeItemMenu("userItem", true);
});
var $authorityTable = $('#authorityTable').DataTable({
	"language" : {
		"emptyTable" : "No existen registros",
		"zeroRecords" : "No existen registros"
	},
	"ordering" : true,
	"searching" : true,
	"paging" : true
});

var $authorityModal = $('#authorityModal');
var $authorityModalForm = $('#authorityModalForm');

function openAuthorityModal() {
	resetErrors();
	$authorityModalForm[0].reset();
	$('#btnUpdateAuthority').hide();
	$('#btnSaveAuthority').show();
	$authorityModal.modal('show');
}

function saveAuthority() {
	blockUI();
	$.ajax({
		type : "POST",
		url : getCont() + "admin/authority/" + "saveAuthority",
		data : {
			// Informacion roles
			id : 0,
			name : $authorityModalForm.find('#name').val(),
			description : $authorityModalForm.find('#description').val()
		},
		success : function(response) {
			ajaxSaveAuthority(response)
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

function ajaxSaveAuthority(response) {
	switch (response.status) {
	case 'success':
		location.reload();
		swal("Correcto!", "Rol creado correctamente.", "success", 2000)
		break;
	case 'fail':
		unblockUI();
		showAuthorityErrors(response.errors, $authorityModalForm);
		break;
	case 'exception':
		swal("Error!", response.exception, "error")
		break;
	default:
		console.log(response.status);
		unblockUI();
	}
}

function updateAuthorityModal(index) {
	resetErrors();
	$('#btnUpdateAuthority').show();
	$('#btnSaveAuthority').hide();
	blockUI();
	$.ajax({
		type : "GET",
		url : getCont() + "admin/" + "authority/findAuthority/" + index,
		timeout : 60000,
		data : {},
		success : function(response) {
			unblockUI();
			$authorityModalForm.find('#authorityId').val(index);
			$authorityModalForm.find('#name').val(response.name);
			$authorityModalForm.find('#description').val(response.description);
			$authorityModal.modal('show');
		},
		error : function(x, t, m) {
			notifyAjaxError(x, t, m);
		}
	});
}

function closeAuthorityModal() {
	$authorityModalForm[0].reset();
	$authorityModal.modal('hide');
}

function updateAuthority() {
	blockUI();
	$.ajax({
		type : "POST",
		url : getCont() + "admin/authority/" + "updateAuthority",
		data : {
			// Informacion rol
			id : $authorityModalForm.find('#authorityId').val(),
			name : $authorityModalForm.find('#name').val(),
			description : $authorityModalForm.find('#description').val()
		},
		success : function(response) {
			ajaxUpdateAuthority(response)
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

function ajaxUpdateAuthority(response) {
	switch (response.status) {
	case 'success':
		location.reload();
		swal("Correcto!", "Rol modificado correctamente.", "success", 2000)
		break;
	case 'fail':
		unblockUI();
		showAuthorityErrors(response.errors, $authorityModalForm);
		break;
	case 'exception':
		swal("Error!", response.exception, "error")
		break;
	default:
		console.log(response.status);
		unblockUI();
	}
}

function confirmDeleteAuthority(element) {
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
				deleteAuthority(element);
			}		
		});
}

function deleteAuthority(element){
	blockUI();
	$.ajax({
		type : "DELETE",
		url : getCont() + "admin/" + "authority/deleteAuthority/" + element,
		timeout : 60000,
		data : {},
		success : function(response) {
			ajaxDeleteAuthority(response);
		},
		error : function(x, t, m) {
			notifyAjaxError(x, t, m);
		}
	});
}

function ajaxDeleteAuthority(response){
	switch (response.status) {
	case 'success':
		location.reload();
		swal("Correcto!", "El rol ha sido eliminado exitosamente.",
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

function showAuthorityErrors(error, $form) {
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
