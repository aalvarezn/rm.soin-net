var $parameterTable = $('#parameterTable').DataTable({
	"language" : {
		"emptyTable" : "No existen registros",
		"zeroRecords" : "No existen registros"
	},
	"searching" : true,
	"paging" : true
});

var $parameterModal = $('#parameterModal');
var $parameterModalForm = $('#parameterModalForm');

function openParameterModal(index) {
	resetErrors();
	blockUI();
	$.ajax({
		type : "GET",
		url : getCont() + "admin/" + "parameter/findParameter/" + index,
		timeout : 60000,
		data : {},
		success : function(response) {
			unblockUI();
			$parameterModalForm.find('#paramId').val(index);
			$parameterModalForm.find('#description').val(response.description);
			$parameterModalForm.find('#paramValue').val(response.paramValue);
			$parameterModal.modal('show');
		},
		error : function(x, t, m) {
			notifyAjaxError(x, t, m);
		}
	});

}

function closeParameterModal() {
	$parameterModalForm[0].reset();
	$parameterModal.modal('hide');
}

function updateParameter() {
	resetErrors();
	blockUI();
	$.ajax({
		type : "POST",
		url : getCont() + "admin/parameter/" + "updateParameter",
		data : {
			// Informacion parametros
			id : $parameterModalForm.find('#paramId').val(),
			description : $parameterModalForm.find('#description').val(),
			paramValue : $parameterModalForm.find('#paramValue').val()
		},
		success : function(response) {
			ajaxUpdateParameter(response)
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

function ajaxUpdateParameter(response) {
	switch (response.status) {
	case 'success':
		location.reload();
		swal("Correcto!", "Par\u00E1metro modificado correctamente.", "success",
				2000)
		break;
	case 'fail':
		unblockUI();
		showParameterErrors(response.errors, $parameterModalForm);
		
		break;
	case 'exception':
		swal("Error!", response.exception, "error")
		break;
	default:
		console.log(response.status);
		unblockUI();
	}
}

function resetErrors() {
	$(".fieldError").css("visibility", "hidden");
	$(".fieldError").attr("class", "error fieldError");
	$(".fieldErrorLine").attr("class", "form-line");
}

function showParameterErrors(error, $form) {
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
