var systemConfigTable = $('#systemConfigTable').DataTable({
	"language" : {
		"emptyTable" : "No existen registros",
		"zeroRecords" : "No existen registros"
	},
	"searching" : true,
	"paging" : true
});

let $systemConfigModal = $('#systemConfigModal');
let $systemConfigForm = $('#systemConfigForm');

$(function() {

	$systemConfigForm.find('input[type="checkbox"]').change(function() {
		if (this.checked) {
			$(this).val(1);
		} else {
			$(this).val(0);
		}
	});
});

function openSystemConfigModal(index) {
	blockUI();
	$.ajax({
		type : "GET",
		url : getCont() + "admin/" + "systemConfig/findSystemConfig/" + index,
		timeout : 60000,
		data : {},
		success : function(response) {
			ajaxOpenSystemConfigModal(response);

		},
		error : function(x, t, m) {
			notifyAjaxError(x, t, m);
		}
	});
}

function ajaxOpenSystemConfigModal(obj) {
	if (obj !== "") {
		clearSystemConfigModal();
		$systemConfigForm.find('#systemConfigId').val(obj.id);
		if (obj.solutionInfo)
			activeInputCheckbox($systemConfigForm, 'solutionInfo');
		if (obj.definitionEnvironment)
			activeInputCheckbox($systemConfigForm, 'definitionEnvironment');
		if (obj.instalationData)
			activeInputCheckbox($systemConfigForm, 'instalationData');
		if (obj.dataBaseInstructions)
			activeInputCheckbox($systemConfigForm, 'dataBaseInstructions');
		if (obj.downEnvironment)
			activeInputCheckbox($systemConfigForm, 'downEnvironment');
		if (obj.environmentObservations)
			activeInputCheckbox($systemConfigForm, 'environmentObservations');
		if (obj.suggestedTests)
			activeInputCheckbox($systemConfigForm, 'suggestedTests');
		if (obj.configurationItems)
			activeInputCheckbox($systemConfigForm, 'configurationItems');
		if (obj.dependencies)
			activeInputCheckbox($systemConfigForm, 'dependencies');
		if (obj.attachmentFiles)
			activeInputCheckbox($systemConfigForm, 'attachmentFiles');
		if (obj.applicationVersion)
			activeInputCheckbox($systemConfigForm, 'applicationVersion');
		$systemConfigModal.modal('show');
	}
	unblockUI();
}

function closeSystemConfigModal() {
	clearSystemConfigModal();
	$systemConfigModal.modal('hide');
}

function clearSystemConfigModal() {
	$systemConfigForm[0].reset();
	$systemConfigForm.find('input[type="checkbox"]').val('0');
}

function updateSystemConfig() {
	blockUI();
	$.ajax({
		type : "POST",
		url : getCont() + "admin/systemConfig/" + "updateSystemConfig",
		data : {
			// Informacion SystemConfig
			id: $systemConfigForm.find('#systemConfigId').val(),
			solutionInfo : boolean($systemConfigForm.find('#solutionInfo').val()),
			definitionEnvironment :
			boolean($systemConfigForm.find('#definitionEnvironment').val()),
			instalationData :
			boolean($systemConfigForm.find('#instalationData').val()),
			dataBaseInstructions :
			boolean($systemConfigForm.find('#dataBaseInstructions').val()),
			downEnvironment :
			boolean($systemConfigForm.find('#downEnvironment').val()),
			environmentObservations :
			boolean($systemConfigForm.find('#environmentObservations').val()),
			suggestedTests :
			boolean($systemConfigForm.find('#suggestedTests').val()),
			configurationItems :
			boolean($systemConfigForm.find('#configurationItems').val()),
			dependencies : boolean($systemConfigForm.find('#dependencies').val()),
			attachmentFiles :
			boolean($systemConfigForm.find('#attachmentFiles').val()),
			applicationVersion :
			boolean($systemConfigForm.find('#applicationVersion').val())
		},
		success : function(response) {
			responseAjaxUpdateSystemConfig(response)
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

function responseAjaxUpdateSystemConfig(response) {
	switch (response.status) {
	case 'success':
		closeSystemConfigModal();
		swal("Correcto!", "Configuraci\u00F3n modificada correctamente.",
				"success", 2000)
		break;
	case 'fail':
		swal("Error!", response.exception, "error")
		break;
	case 'exception':
		swal("Error!", response.exception, "error")
		break;
	default:
		console.log(response.status);
		unblockUI();
	}
}