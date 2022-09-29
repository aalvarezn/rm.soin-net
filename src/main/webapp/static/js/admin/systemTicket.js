$(function() {
	activeItemMenu("ticketsItem", true);
});
var $systemTable = $('#systemTable').DataTable({
	"language" : {
		"emptyTable" : "No existen registros",
		"zeroRecords" : "No existen registros"
	},
	"ordering" : true,
	"searching" : true,
	"paging" : true
});

var $systemModal = $('#systemModal');
var $systemModalForm = $('#systemModalForm');

$(function() {
	$systemModal.find('#managers').multiSelect({
		selectableHeader: "<div class='custom-header'>Usuarios</div><input type='text' class='search-input filterMS' autocomplete='off' placeholder='Buscar ...'>",
		selectionHeader: "<div class='custom-header'>Usuarios gestores</div><input type='text' class='search-input filterMS' autocomplete='off' placeholder='Buscar ...'>",
		afterInit: function(ms){
			var that = this,
			$selectableSearch = that.$selectableUl.prev(),
			$selectionSearch = that.$selectionUl.prev(),
			selectableSearchString = '#'+that.$container.attr('id')+' .ms-elem-selectable:not(.ms-selected)',
			selectionSearchString = '#'+that.$container.attr('id')+' .ms-elem-selection.ms-selected';

			that.qs1 = $selectableSearch.quicksearch(selectableSearchString)
			.on('keydown', function(e){
				if (e.which === 40){
					that.$selectableUl.focus();
					return false;
				}
			});

			that.qs2 = $selectionSearch.quicksearch(selectionSearchString)
			.on('keydown', function(e){
				if (e.which == 40){
					that.$selectionUl.focus();
					return false;
				}
			});
		},
		afterSelect : function(values) {
			$systemModal.find("#managers option[id='" + values + "']").attr("selected", "selected");
		},
		afterDeselect : function(values) {
			$systemModal.find("#managers option[id='" + values + "']").removeAttr('selected');
		}
	});
	$systemModal.find('#team').multiSelect({
		selectableHeader: "<div class='custom-header'>Usuarios</div> <input type='text' class='search-input filterMS' autocomplete='off' placeholder='Buscar ...'>",
		selectionHeader: "<div class='custom-header'>Equipo usuarios</div> <input type='text' class='search-input filterMS' autocomplete='off' placeholder='Buscar ...'>",
		afterInit: function(ms){
			var that = this,
			$selectableSearch = that.$selectableUl.prev(),
			$selectionSearch = that.$selectionUl.prev(),
			selectableSearchString = '#'+that.$container.attr('id')+' .ms-elem-selectable:not(.ms-selected)',
			selectionSearchString = '#'+that.$container.attr('id')+' .ms-elem-selection.ms-selected';

			that.qs1 = $selectableSearch.quicksearch(selectableSearchString)
			.on('keydown', function(e){
				if (e.which === 40){
					that.$selectableUl.focus();
					return false;
				}
			});

			that.qs2 = $selectionSearch.quicksearch(selectionSearchString)
			.on('keydown', function(e){
				if (e.which == 40){
					that.$selectionUl.focus();
					return false;
				}
			});
		},
		afterSelect : function(values) {
			$systemModal.find("#team option[id='" + values + "']").attr("selected", "selected");
		},
		afterDeselect : function(values) {
			$systemModal.find("#team option[id='" + values + "']").removeAttr('selected');
		}
	});

	$systemModal.find('input[type="checkbox"]').change(function() {
		if (this.checked) {
			$(this).val(1);
		} else {
			$(this).val(0);
		}
	});
});

function getSelectIds(form, name){
	let list = [];
	form.find(name).children("option:selected").each(function(j) {
		list.push(Number($(this).attr('id')));
	});
	return list;

}

function openSystemModal() {
	resetErrors();
	$systemModalForm.find('a[href="#tabHome"]').click();
	$systemModalForm.find('input[type="checkbox"]').val('0');
	$systemModalForm[0].reset();
	$systemModalForm.find("#leaderId").selectpicker('val', '');
	$systemModalForm.find("#proyectId").selectpicker('val', '');
	$systemModalForm.find('input[type="checkbox"]').val('0');

	$systemModalForm.find('#team option').removeAttr('selected');
	$systemModalForm.find('#managers option').removeAttr('selected');
	$systemModalForm.find('#team').multiSelect("refresh");
	$systemModalForm.find('#managers').multiSelect("refresh");
	$('#btnUpdateSystem').hide();
	$('#btnSaveSystem').show();
	$systemModal.modal('show');
}

function saveSystem() {
	blockUI();
	let userTeamId = getSelectIds($systemModalForm, "#team");
	let managersId = getSelectIds($systemModalForm, "#managers");
	$.ajax({
		type : "POST",
		url : getCont() + "admin/system/" + "saveSystem",
		data : {
			// Informacion sistemas
			id : 0,
			name : $systemModalForm.find('#name').val(),
			code : $systemModalForm.find('#code').val(),
			nomenclature : boolean($systemModalForm.find('#nomenclature').val()),
			importObjects : boolean($systemModalForm.find('#importObjects').val()),
			isBO : boolean($systemModalForm.find('#isBO').val()),
			isAIA : boolean($systemModalForm.find('#isAIA').val()),
			customCommands : boolean($systemModalForm.find('#customCommands').val()),
			installationInstructions : boolean($systemModalForm.find('#installationInstructions').val()),
			additionalObservations : boolean($systemModalForm.find('#additionalObservations').val()),
			proyectId: $systemModalForm.find("#proyectId").children("option:selected").val(),
			leaderId: $systemModalForm.find("#leaderId").children("option:selected").val(),
			userTeamId: JSON.stringify(userTeamId),
			managersId: JSON.stringify(managersId),
			emailId: $systemModalForm.find("#emailId").children("option:selected").val()
		},
		success : function(response) {
			ajaxSaveSystem(response)
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

function ajaxSaveSystem(response) {
	switch (response.status) {
	case 'success':
		location.reload();
		swal("Correcto!", "Sistema creado correctamente.", "success", 2000)
		break;
	case 'fail':
		unblockUI();
		$systemModalForm.find('a[href="#tabHome"]').click();
		showSystemErrors(response.errors, $systemModalForm);
		break;
	case 'exception':
		swal("Error!", response.exception, "error")
		break;
	default:
		console.log(response.status);
	unblockUI();
	}
}

function updateSystemModal(index) {
	resetErrors();
	$systemModalForm.find('a[href="#tabHome"]').click();
	$systemModalForm.find('input[type="checkbox"]').val('0');
	$systemModalForm.find('#team option').removeAttr('selected');
	$systemModalForm.find('#managers option').removeAttr('selected');
	$('#btnUpdateSystem').show();
	$('#btnSaveSystem').hide();
	blockUI();
	$.ajax({
		type : "GET",
		url : getCont() + "admin/" + "system/findSystem/" + index,
		timeout : 60000,
		data : {},
		success : function(response) {
			if (response == null) {
				swal("Error!", "El Sistema seleccionado no existe.", "error");
			} else {
				unblockUI();
				ajaxEditSystem(response);
			}
		},
		error : function(x, t, m) {
			notifyAjaxError(x, t, m);
		}
	});
}

function ajaxEditSystem(obj) {
	$systemModalForm.find('#systemId').val(obj.id);
	$systemModalForm.find('#name').val(obj.name);
	$systemModalForm.find('#code').val(obj.code);
	$systemModalForm.find("#leaderId").selectpicker('val', obj.leader.id);
	$systemModalForm.find("#proyectId").selectpicker('val', obj.proyect.id);
	if(obj.emailTemplate.length > 0)
		$systemModalForm.find("#emailId").selectpicker('val', obj.emailTemplate[0].id);
	
	if (obj.nomenclature)
		activeInputCheckbox($systemModalForm, 'nomenclature');
	if (obj.importObjects)
		activeInputCheckbox($systemModalForm, 'importObjects');
	if (obj.isBO)
		activeInputCheckbox($systemModalForm, 'isBO');
	if (obj.isAIA)
		activeInputCheckbox($systemModalForm, 'isAIA');
	if (obj.customCommands)
		activeInputCheckbox($systemModalForm, 'customCommands');
	if (obj.additionalObservations)
		activeInputCheckbox($systemModalForm, 'additionalObservations');
	if (obj.installationInstructions)
		activeInputCheckbox($systemModalForm, 'installationInstructions');

	var userIncidence = [];
	for (var i = 0, l = obj.usersIncidence.length; i < l; i++) {
		$systemModalForm.find('#team option').each(
				function(index, element) {
					if (element.id == obj.usersIncidence[i].id) {
						userIncidence.push((obj.usersIncidence[i].id).toString());
					}
				});
	}
	var managersIncidence = [];
	for (var i = 0, l = obj.managersIncidence.length; i < l; i++) {
		$systemModalForm.find('#managers option').each(
				function(index, element) {
					if (element.id == obj.managersIncidence[i].id) {
						managersIncidence.push((obj.managersIncidence[i].id).toString());
					}
				});
	}
	$systemModal.find('#managers').multiSelect('select',managersIncidence);
	$systemModal.find('#team').multiSelect('select', userIncidence);
	$systemModal.modal('show');
}

function closeSystemModal() {
	$systemModalForm[0].reset();
	$systemModal.find('#managers').multiSelect("deselect_all");
	$systemModal.find('#team').multiSelect("deselect_all");
	$systemModal.modal('hide');
}

function updateSystem() {
	blockUI();
	let userTeamId = getSelectIds($systemModalForm, "#team");
	let managersId = getSelectIds($systemModalForm, "#managers");
	$.ajax({
		type : "POST",
		url : getCont() + "admin/system/" + "updateSystemIncidence",
		data : {
			// Informacion sistema
			id : $systemModalForm.find('#systemId').val(),
			userIncidenceId: JSON.stringify(userTeamId),
			managersIncidenceId: JSON.stringify(managersId),
		},
		success : function(response) {
			ajaxUpdateSystem(response)
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

function ajaxUpdateSystem(response) {
	switch (response.status) {
	case 'success':
		location.reload();
		swal("Correcto!", "Sistema modificado correctamente.", "success", 2000)
		break;
	case 'fail':
		unblockUI();
		$systemModalForm.find('a[href="#tabHome"]').click();
		showSystemErrors(response.errors, $systemModalForm);
		break;
	case 'exception':
		swal("Error!", response.exception, "error")
		break;
	default:
		console.log(response.status);
	unblockUI();
	}
}

function confirmDeleteSystem(element) {
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
			deleteSystem(element);
		}		
	});
}

function deleteSystem(element){
	blockUI();
	$.ajax({
		type : "DELETE",
		url : getCont() + "admin/" + "system/deleteSystem/" + element,
		timeout : 60000,
		data : {},
		success : function(response) {
			ajaxDeleteSystem(response);
		},
		error : function(x, t, m) {
			notifyAjaxError(x, t, m);
		}
	});
}

function ajaxDeleteSystem(response){
	switch (response.status) {
	case 'success':
		location.reload();
		swal("Correcto!", "El sistema ha sido eliminado exitosamente.",
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

function showSystemErrors(error, $form) {
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
