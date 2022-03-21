$(function() {
	activeItemMenu("releaseItem", true);
});
var $impactTable = $('#impactTable').DataTable({
	"language" : {
		"emptyTable" : "No existen registros",
		"zeroRecords" : "No existen registros"
	},
	"ordering" : true,
	"searching" : true,
	"paging" : true
});

var $impactModal = $('#impactModal');
var $impactModalForm = $('#impactModalForm');

function openImpactModal() {
	resetErrors();
	$impactModalForm[0].reset();
	$('#btnUpdateImpact').hide();
	$('#btnSaveImpact').show();
	$impactModal.modal('show');
}

function saveImpact() {
	blockUI();
	$.ajax({
		type : "POST",
		url : getCont() + "admin/impact/" + "saveImpact",
		data : {
			// Informacion impactos
			id : 0,
			name : $impactModalForm.find('#name').val(),
			description : $impactModalForm.find('#description').val()
		},
		success : function(response) {
			ajaxSaveImpact(response)
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

function ajaxSaveImpact(response) {
	switch (response.status) {
	case 'success':
		location.reload();
		swal("Correcto!", "Impacto creado correctamente.", "success", 2000)
		break;
	case 'fail':
		unblockUI();
		showImpactErrors(response.errors, $impactModalForm);
		break;
	case 'exception':
		swal("Error!", response.exception, "error")
		break;
	default:
		console.log(response.status);
		unblockUI();
	}
}

function updateImpactModal(index) {
	resetErrors();
	$('#btnUpdateImpact').show();
	$('#btnSaveImpact').hide();
	blockUI();
	$.ajax({
		type : "GET",
		url : getCont() + "admin/" + "impact/findImpact/" + index,
		timeout : 60000,
		data : {},
		success : function(response) {
			unblockUI();
			$impactModalForm.find('#impactId').val(index);
			$impactModalForm.find('#name').val(response.name);
			$impactModalForm.find('#description').val(response.description);
			$impactModal.modal('show');
		},
		error : function(x, t, m) {
			notifyAjaxError(x, t, m);
		}
	});
}

function closeImpactModal() {
	$impactModalForm[0].reset();
	$impactModal.modal('hide');
}

function updateImpact() {
	blockUI();
	$.ajax({
		type : "POST",
		url : getCont() + "admin/impact/" + "updateImpact",
		data : {
			// Informacion impacto
			id : $impactModalForm.find('#impactId').val(),
			name : $impactModalForm.find('#name').val(),
			description : $impactModalForm.find('#description').val()
		},
		success : function(response) {
			ajaxUpdateImpact(response)
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

function ajaxUpdateImpact(response) {
	switch (response.status) {
	case 'success':
		location.reload();
		swal("Correcto!", "Impacto modificado correctamente.", "success", 2000)
		break;
	case 'fail':
		unblockUI();
		showImpactErrors(response.errors, $impactModalForm);
		break;
	case 'exception':
		swal("Error!", response.exception, "error")
		break;
	default:
		console.log(response.status);
		unblockUI();
	}
}

function confirmDeleteImpact(element) {
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
				deleteImpact(element);
			}		
		});
}

function deleteImpact(element){
	blockUI();
	$.ajax({
		type : "DELETE",
		url : getCont() + "admin/" + "impact/deleteImpact/" + element,
		timeout : 60000,
		data : {},
		success : function(response) {
			ajaxDeleteImpact(response);
		},
		error : function(x, t, m) {
			notifyAjaxError(x, t, m);
		}
	});
}

function ajaxDeleteImpact(response){
	switch (response.status) {
	case 'success':
		location.reload();
		swal("Correcto!", "El impacto ha sido eliminado exitosamente.",
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

function showImpactErrors(error, $form) {
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
