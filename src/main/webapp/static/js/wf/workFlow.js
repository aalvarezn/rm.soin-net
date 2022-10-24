$(function() {
	activeItemMenu("wfItem", true);
});
var $workFlowTable = $('#workFlowTable').DataTable({
	"language" : {
		"emptyTable" : "No existen registros",
		"zeroRecords" : "No existen registros"
	},
	"ordering" : true,
	"searching" : true,
	"paging" : true
});

var $workFlowModal = $('#workFlowModal');
var $workFlowModalForm = $('#workFlowModalForm');

function openWorkFlowModal() {
	resetErrors();
	$workFlowModalForm[0].reset();
	$('#btnSaveWorkFlow').show();
	$workFlowModal.modal('show');
}

function saveWorkFlow() {
	blockUI();
	$.ajax({
		type : "POST",
		url : getCont() + "wf/workFlow/" + "saveWorkFlow",
		data : {
			// Informacion tramites
			id : 0,
			name : $workFlowModalForm.find('#name').val(),
			systemId: $workFlowModalForm.find("#systemId").children("option:selected").val(),
			typeId:$workFlowModalForm.find("#typeId").children("option:selected").val(),
		},
		success : function(response) {
			ajaxSaveWorkFlow(response)
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

function ajaxSaveWorkFlow(response) {
	switch (response.status) {
	case 'success':
		location.reload();
		swal("Correcto!", "Tramite creado correctamente.", "success", 2000)
		break;
	case 'fail':
		unblockUI();
		showWorkFlowErrors(response.errors, $workFlowModalForm);
		break;
	case 'exception':
		swal("Error!", response.exception, "error")
		break;
	default:
		console.log(response.status);
		unblockUI();
	}
}

function closeWorkFlowModal() {
	$workFlowModalForm[0].reset();
	$workFlowModal.modal('hide');
}

function confirmDeleteWorkFlow(element) {
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
				deleteWorkFlow(element);
			}		
		});
}

function deleteWorkFlow(element){
console.log(getCont() + "wf/" + "workFlow/deleteWorkFlow/" + element);
	blockUI();
	$.ajax({
		type : "DELETE",
		url : getCont() + "wf/workFlow/deleteWorkFlow/" + element,
		timeout : 60000,
		data : {},
		success : function(response) {
			ajaxDeleteWorkFlow(response);
		},
		error : function(x, t, m) {
			notifyAjaxError(x, t, m);
		}
	});
}
function deleteStatusIncidence(index) {
	var obj = $dtStatusIncidence.row(index).data();
	Swal.fire({
		title: '\u00BFEst\u00e1s seguro que desea eliminar el registro?',
		text: 'Esta acci\u00F3n no se puede reversar.',
		...swalDefault
	}).then((result) => {
		if(result.value){
			blockUI();
			$.ajax({
				type : "DELETE",
				url : getCont() + "admin/statusIncidence/"+obj.id ,
				timeout : 60000,
				data : {},
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status)
					$dtStatusIncidence.ajax.reload();
					$mdStatusIncidence.modal('hide');
				},
				error : function(x, t, m) {
					unblockUI();
					
				}
			});
		}
	});
}

function ajaxDeleteWorkFlow(response){
	switch (response.status) {
	case 'success':
		location.reload();
		swal("Correcto!", "El tramite ha sido eliminado exitosamente.",
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

function showWorkFlowErrors(error, $form) {
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
