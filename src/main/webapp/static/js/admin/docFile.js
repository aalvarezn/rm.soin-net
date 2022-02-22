$(function() {
	activeItemMenu("documentItem", true);
	$docFileModalForm.find("#inputFile").unbind();
	$docFileModalForm.find("#inputFile").change(function() {
		if($(this).val() != ""){
			$docFileModalForm.find("#inputFile").parent().find('label').text($(this).val());
		}else{
			$docFileModalForm.find("#inputFile").parent().find('label').text("Seleccionar archivo");
		}

	});
});

var $docFileTable = $('#docFileTable').DataTable({
	"language" : {
		"emptyTable" : "No existen registros",
		"zeroRecords" : "No existen registros"
	},
	"ordering" : true,
	"searching" : true,
	"paging" : true
});

var $docFileModal = $('#docFileModal');
var $docFileModalForm = $('#docFileModalForm');

function openDocFileModal() {
	resetErrors();
	$docFileModal.modal('show');
}

function uploadInputFile() {
	if($docFileModalForm.find("#inputFile").val() == "")
		return ;
	var formData = new FormData();
	formData
	.append('file', $docFileModalForm.find('#inputFile')[0].files[0]);
	// Ajax call for file uploaling
	var ajaxReq = $.ajax({
		url : getCont() + "admin/docFile/" + "uploadDocFile",
		timeout : 60000,
		type : 'POST',
		data : formData,
		contentType : false,
		processData : false,
		success : function(response) {
			ajaxFileUpload(response);
		},
		error : function(x, t, m) {
			notifyAjaxError(x, t, m);
		},
	});
}

function ajaxFileUpload(response) {
	switch (response.status) {
	case 'success':
		location.reload();
		swal("Correcto!", "Plantilla archivo cargada correctamente.", "success", 2000)
		break;
	case 'fail':
		unblockUI();
		showdocFileErrors(response.errors, $docFileModalForm);
		break;
	case 'exception':
		swal("Error!", response.exception, "error")
		break;
	default:
		console.log(response.status);
	unblockUI();
	}
}

function closeDocFileModal() {
	$docFileModalForm[0].reset();
	$docFileModalForm.find("#inputFile").change();
	$docFileModal.modal('hide');
}

function confirmDeleteDocFile(name, ext) {
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
			deleteDocFile(name, ext);
		}		
	});
}

function deleteDocFile(name, ext){
	blockUI();
	$.ajax({
		type : "DELETE",
		url : getCont() + "admin/docFile/delete/" + name+ '/'+ext,
		timeout : 60000,
		data : {},
		success : function(response) {
			ajaxDeleteDocFile(response);
		},
		error : function(x, t, m) {
			notifyAjaxError(x, t, m);
		}
	});
}

function ajaxDeleteDocFile(response){
	switch (response.status) {
	case 'success':
		location.reload();
		swal("Correcto!", "La plantilla archivo ha sido eliminada exitosamente.",
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

function showDocFileErrors(error, $form) {
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
