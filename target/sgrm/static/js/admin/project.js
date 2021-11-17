var $projectTable = $('#projectTable').DataTable({
	"language" : {
		"emptyTable" : "No existen registros",
		"zeroRecords" : "No existen registros"
	},
	"searching" : true,
	"paging" : true
});

var $projectModal = $('#projectModal');
var $projectModalForm = $('#projectModalForm');

function openProjectModal() {
	resetErrors();
	$projectModalForm[0].reset();
	$('#btnUpdateProject').hide();
	$('#btnSaveProject').show();
	$projectModal.modal('show');
}

function saveProject() {
	blockUI();
	$.ajax({
		type : "POST",
		url : getCont() + "admin/project/" + "saveProject",
		data : {
			// Informacion proyectos
			id : 0,
			code : $projectModalForm.find('#code').val(),
			description : $projectModalForm.find('#description').val()
		},
		success : function(response) {
			ajaxSaveProject(response)
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

function ajaxSaveProject(response) {
	switch (response.status) {
	case 'success':
		location.reload();
		swal("Correcto!", "Proyecto creado correctamente.", "success", 2000)
		break;
	case 'fail':
		unblockUI();
		showProjectErrors(response.errors, $projectModalForm);
		break;
	case 'exception':
		swal("Error!", response.exception, "error")
		break;
	default:
		console.log(response.status);
		unblockUI();
	}
}

function updateProjectModal(index) {
	resetErrors();
	$('#btnUpdateProject').show();
	$('#btnSaveProject').hide();
	blockUI();
	$.ajax({
		type : "GET",
		url : getCont() + "admin/" + "project/findProject/" + index,
		timeout : 60000,
		data : {},
		success : function(response) {
			unblockUI();
			$projectModalForm.find('#projectId').val(index);
			$projectModalForm.find('#code').val(response.code);
			$projectModalForm.find('#description').val(response.description);
			$projectModal.modal('show');
		},
		error : function(x, t, m) {
			notifyAjaxError(x, t, m);
		}
	});
}

function closeProjectModal() {
	$projectModalForm[0].reset();
	$projectModal.modal('hide');
}

function updateProject() {
	blockUI();
	$.ajax({
		type : "POST",
		url : getCont() + "admin/project/" + "updateProject",
		data : {
			// Informacion proyecto
			id : $projectModalForm.find('#projectId').val(),
			code : $projectModalForm.find('#code').val(),
			description : $projectModalForm.find('#description').val()
		},
		success : function(response) {
			ajaxUpdateProject(response)
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

function ajaxUpdateProject(response) {
	switch (response.status) {
	case 'success':
		location.reload();
		swal("Correcto!", "Proyecto modificado correctamente.", "success", 2000)
		break;
	case 'fail':
		unblockUI();
		showProjectErrors(response.errors, $projectModalForm);
		break;
	case 'exception':
		swal("Error!", response.exception, "error")
		break;
	default:
		console.log(response.status);
		unblockUI();
	}
}

function confirmDeleteProject(element) {
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
				deleteProject(element);
			}		
		});
}

function deleteProject(element){
	blockUI();
	$.ajax({
		type : "DELETE",
		url : getCont() + "admin/" + "project/deleteProject/" + element,
		timeout : 60000,
		data : {},
		success : function(response) {
			ajaxDeleteProject(response);
		},
		error : function(x, t, m) {
			notifyAjaxError(x, t, m);
		}
	});
}

function ajaxDeleteProject(response){
	switch (response.status) {
	case 'success':
		location.reload();
		swal("Correcto!", "El proyecto ha sido eliminado exitosamente.",
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

function showProjectErrors(error, $form) {
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
