var ieo = 0;
var $actionForm = $('#environmentActionForm');
$(function() {
	var table3 = $('#environmentActionTable').DataTable({
		"language" : {
			"emptyTable" : "No existen registros",
			"zeroRecords" : "No existen registros"
		},
		"searching" : false,
		"paging" : false
	});
});

function openEnvironmentActionModal() {
	clearEnvironmentActionModal();
	$('#environmentActionModal').modal('show');
}

function closeEnvironmentActionModal() {
	clearEnvironmentActionModal();
	$('#environmentActionModal').modal('hide');

}

function addEnvironmentAction() {
	if (!validEnvironmentAction()) {
		return false;
	}
	var environment = $actionForm.find('#environment').children(
			"option:selected");
	var actionEnvironment = $actionForm.find('#actionEnvironment').children(
			"option:selected");
	var actionTime = $actionForm.find('input[name=group1]:checked').val();
	blockUI();
	$.ajax({
		type : "POST",
		url : getCont() + "release/" + "addReleaseEnvironmentAction/"
				+ $('#generateReleaseForm #release_id').val(),
		timeout : 60000,
		data : {
			time : actionTime,
			environment : environment.val(),
			action : actionEnvironment.val(),
			observation : $actionForm.find('#observation').val()
		},
		success : function(response) {
			responseAjaxAddAction(response);
		},
		error : function(x, t, m) {
			notifyAjaxError(x, t, m);
		}
	});
}

function addActionTable(response) {
	var environment = $actionForm.find('#environment').children(
			"option:selected");
	var actionEnvironment = $actionForm.find('#actionEnvironment').children(
			"option:selected");
	var actionTime = $actionForm.find('input[name=group1]:checked').val();
	var environmentActionTable = $('#environmentActionTable').DataTable();

	environmentActionTable.row
			.add(
					[
							$('#observation').val(),
							actionTime,
							'<input id="' + environment.val()
									+ '" type="text" name="typeObject" value="'
									+ environment.text()
									+ '" readonly="" class="inputTable">',
							'<input id="' + actionEnvironment.val()
									+ '" type="text" name="typeObject" value="'
									+ actionEnvironment.text()
									+ '" readonly="" class="inputTable">',
							'<div style="text-align: center"><i onClick="deleteaEnvironmentAction('
									+ response.data
									+ ')" class="material-icons gris" style="font-size: 30px;"[>delete</i></div>' ])
			.node().id = response.data;
	environmentActionTable.draw();
	closeEnvironmentActionModal();
	swal("Correcto!", "Acci\u00F3n agregada correctamente.", "success", 2000)
}

function responseAjaxAddAction(response) {
	switch (response.status) {
	case 'success':
		addActionTable(response);
		break;
	case 'fail':
		swal("Error!", "Error al agregar la acci\u00F3n.", "error")
		break;
	case 'exception':
		swal("Error!", response.exception, "error")
		break;
	default:
		unblockUI();
	}
}

function responseAjaxDeleteAction(response, id) {
	var environmentActionTable = $('#environmentActionTable').DataTable();
	switch (response.status) {
	case 'success':
		environmentActionTable.row($('#environmentActionTable #' + id))
				.remove().draw();
		swal("Correcto!", "Acci\u00F3n eliminada correctamente.", "success", 2000)
		break;
	case 'fail':
		swal("Error!", "Error al eliminar la acci\u00F3n.", "error")
		break;
	case 'exception':
		swal("Error!", response.exception, "error")
		break;
	default:
		unblockUI();
	}
}

function deleteaEnvironmentAction(id) {
	var cont = getCont();
	blockUI();
	$.ajax({
		type : "DELETE",
		url : cont + "release/" + "deleteReleaseEnvironmentAction/"
				+ $('#generateReleaseForm #release_id').val() + "/" + id,
		timeout : 60000,
		data : {},
		success : function(response) {
			responseAjaxDeleteAction(response, id);
		},
		error : function(x, t, m) {
			notifyAjaxError(x, t, m);
		}
	});
}

function clearEnvironmentActionModal() {
	$actionForm[0].reset();
	$actionForm.find("#environment").selectpicker('val', '');
	$actionForm.find("#actionEnvironment").selectpicker('val', '');

	$actionForm.find("input").parent().attr("class", "form-line");
	$actionForm.find(".fieldError").css("visibility", "hidden");

}

function validEnvironmentAction() {
	var valid = true;
	$actionForm.find("input").parent().attr("class", "form-line");
	$actionForm.find(".fieldError").css("visibility", "hidden");

	var observation = $actionForm.find("#observation");
	if ($.trim(observation.val()) == "") {
		$actionForm.find("#observation_error").css("visibility", "visible");
		observation.parent().attr("class", "form-line focused error");
		valid = false;
	}

	var environment = $actionForm.find("#environment").children(
			"option:selected").val();

	var actionEnvironment = $actionForm.find("#actionEnvironment").children(
			"option:selected").val();

	if ($.trim(environment) == "") {
		$actionForm.find("#environment_error").css("visibility", "visible");
		valid = false;
	}
	if ($.trim(actionEnvironment) == "") {
		$actionForm.find("#actionEnvironment_error").css("visibility",
				"visible");
		valid = false;
	}
	return valid;
}