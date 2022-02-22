$(function() {
	activeItemMenu("releaseItem", true);
});
var $riskTable = $('#riskTable').DataTable({
	"language" : {
		"emptyTable" : "No existen registros",
		"zeroRecords" : "No existen registros"
	},
	"ordering" : true,
	"searching" : true,
	"paging" : true
});

var $riskModal = $('#riskModal');
var $riskModalForm = $('#riskModalForm');

function openRiskModal() {
	resetErrors();
	$riskModalForm[0].reset();
	$('#btnUpdateRisk').hide();
	$('#btnSaveRisk').show();
	$riskModal.modal('show');
}

function saveRisk() {
	blockUI();
	$.ajax({
		type : "POST",
		url : getCont() + "admin/risk/" + "saveRisk",
		data : {
			// Informacion riesgos
			id : 0,
			name : $riskModalForm.find('#name').val(),
			description : $riskModalForm.find('#description').val()
		},
		success : function(response) {
			ajaxSaveRisk(response)
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

function ajaxSaveRisk(response) {
	switch (response.status) {
	case 'success':
		location.reload();
		swal("Correcto!", "Riesgo creado correctamente.", "success", 2000)
		break;
	case 'fail':
		unblockUI();
		showRiskErrors(response.errors, $riskModalForm);
		break;
	case 'exception':
		swal("Error!", response.exception, "error")
		break;
	default:
		console.log(response.status);
		unblockUI();
	}
}

function updateRiskModal(index) {
	resetErrors();
	$('#btnUpdateRisk').show();
	$('#btnSaveRisk').hide();
	blockUI();
	$.ajax({
		type : "GET",
		url : getCont() + "admin/" + "risk/findRisk/" + index,
		timeout : 60000,
		data : {},
		success : function(response) {
			unblockUI();
			$riskModalForm.find('#riskId').val(index);
			$riskModalForm.find('#name').val(response.name);
			$riskModalForm.find('#description').val(response.description);
			$riskModal.modal('show');
		},
		error : function(x, t, m) {
			notifyAjaxError(x, t, m);
		}
	});
}

function closeRiskModal() {
	$riskModalForm[0].reset();
	$riskModal.modal('hide');
}

function updateRisk() {
	blockUI();
	$.ajax({
		type : "POST",
		url : getCont() + "admin/risk/" + "updateRisk",
		data : {
			// Informacion riesgo
			id : $riskModalForm.find('#riskId').val(),
			name : $riskModalForm.find('#name').val(),
			description : $riskModalForm.find('#description').val()
		},
		success : function(response) {
			ajaxUpdateRisk(response)
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

function ajaxUpdateRisk(response) {
	switch (response.status) {
	case 'success':
		location.reload();
		swal("Correcto!", "Riesgo modificado correctamente.", "success", 2000)
		break;
	case 'fail':
		unblockUI();
		showRiskErrors(response.errors, $riskModalForm);
		break;
	case 'exception':
		swal("Error!", response.exception, "error")
		break;
	default:
		console.log(response.status);
		unblockUI();
	}
}

function confirmDeleteRisk(element) {
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
				deleteRisk(element);
			}		
		});
}

function deleteRisk(element){
	blockUI();
	$.ajax({
		type : "DELETE",
		url : getCont() + "admin/" + "risk/deleteRisk/" + element,
		timeout : 60000,
		data : {},
		success : function(response) {
			ajaxDeleteRisk(response);
		},
		error : function(x, t, m) {
			notifyAjaxError(x, t, m);
		}
	});
}

function ajaxDeleteRisk(response){
	switch (response.status) {
	case 'success':
		location.reload();
		swal("Correcto!", "El riesgo ha sido eliminado exitosamente.",
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

function showRiskErrors(error, $form) {
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
