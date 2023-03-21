
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
	initAttentionFormValidation();
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
	initDataTable();
});
/*

jQuery("#lead").autocomplete(
		{
			source : function(request, response) {
				$.get(getCont() + "admin/attentionGroup/" + "requestAutoComplete-"
						+ $("#lead").val(), function(data) {
					response(data);
				});
			},
			response : function(event, ui) {
				countRequest = ui.content.length;
				console.log(ui);
				if (countRequest === 0) {
					notifyInfo('Sin resultados');
				}
			},
			select : function(e, ui) {
				
				if (!($('#listName ul #' + ui.item.id).length)) {
					if ($('#listName ul li').length < 1) {
						
						console.log(ui.item.name);
						$("#listName ul")
						.append(
								'<li id="'
								+ ui.item.id
								+ '" class="list-group-item">'
								+ ui.item.name
								+ ' <span class="flr"> <a onclick="deleteName('
								+ ui.item.id
								+ ')" title="Borrar"><i class="material-icons gris">delete</i></a>'
								+ ' </span>' + ' </li>');
					} else {
						notifyInfo('Ya existe un nombre ');
					}

				}
				$(this).val('');
				return false;
			},
			delay : 0
		});
*/
function initDataTable() {
	$dtAttentionGroup = $('#attentionTable')
	.DataTable(
			{
				lengthMenu : [ [ 10, 25, 50, -1 ],
					[ '10', '25', '50', 'Mostrar todo' ] ],
					"iDisplayLength" : 10,
					"language" : optionLanguaje,
					"iDisplayStart" : 0,
					"sAjaxSource" : getCont() + "admin/attentionGroup/list",
					"fnServerParams" : function(aoData) {
					},
					"aoColumns" : [
						{
							"mDataProp" : 'name'
						},
						{
							"mDataProp" : 'code'
						},
						{
							render : function(data, type, row, meta) {
								var options = '<div class="iconLineC">';

								options += '<a onclick="ajaxEditSystem('
									+ meta.row
									+ ')" title="Editar"><i class="material-icons gris">mode_edit</i></a>';

								options += '<a onclick="deleteAttentionGroup('
									+ meta.row
									+ ')" title="Borrar"><i class="material-icons gris">delete</i></a>';

								options += ' </div>';

								return options;
							}
						} ],
						ordering : false,
			});
}
function getSelectIds(form, name){
	let list = [];
	form.find(name).children("option:selected").each(function(j) {
		list.push(Number($(this).attr('id')));
	});
	return list;

}

function openSystemModal() {
	$systemModalForm.validate().resetForm();
	$systemModalForm[0].reset();
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

function updateSystemModal() {
	let usersAttentionId = getSelectIds($systemModalForm, "#team");
	if (!$systemModalForm.valid())
		return;
	Swal.fire({
		title: '\u00BFEst\u00e1s seguro que desea actualizar el registro?',
		text: 'Esta acci\u00F3n no se puede reversar.',
		...swalDefault
	}).then((result) => {
		if(result.value){
			blockUI();
			$.ajax({
				type : "PUT",
				url : getCont() + "admin/attentionGroup/" ,
				dataType : "json",
				contentType: "application/json; charset=utf-8",
				timeout : 60000,
				data : JSON.stringify({
					id : $systemModalForm.find('#id').val(),
					name : $systemModalForm.find('#name').val(),
					code : $systemModalForm.find('#code').val(),
					leaderId:$systemModalForm.find('#leaderId').val(),
					usersAttentionId: usersAttentionId
				}),
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status)
					$dtAttentionGroup.ajax.reload();
					$systemModal.modal('hide');
				},
				error : function(x, t, m) {
					unblockUI();
					
				}
			});
		}
	});
}


function saveSystem() {
	let usersAttentionId = getSelectIds($systemModalForm, "#team");
	console.log(usersAttentionId);
	if (!$systemModalForm.valid())
		return;
	Swal.fire({
		title: '\u00BFEst\u00e1s seguro que desea crear el registro?',
		text: 'Esta acci\u00F3n no se puede reversar.',
		...swalDefault
	}).then((result) => {
		if(result.value){
			blockUI();
			$.ajax({
				type : "POST",
				url : getCont() + "admin/attentionGroup/" ,
				dataType : "json",
				contentType: "application/json; charset=utf-8",
				timeout : 60000,
				data : JSON.stringify({
					// Informacion sistemas
					name : $systemModalForm.find('#name').val(),
					code : $systemModalForm.find('#code').val(),
					leaderId:$systemModalForm.find('#leaderId').val(),
					usersAttentionId: usersAttentionId
				}),
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status)
					$dtAttentionGroup.ajax.reload();
					$systemModal.modal('hide');
				},
				error : function(x, t, m) {
					unblockUI();
				
				}
			});
		}
	});
}

function deleteAttentionGroup(index) {
	var obj = $dtAttentionGroup.row(index).data();
	Swal.fire({
		title: '\u00BFEst\u00e1s seguro que desea eliminar el registro?',
		text: 'Esta acci\u00F3n no se puede reversar.',
		...swalDefault
	}).then((result) => {
		if(result.value){
			blockUI();
			$.ajax({
				type : "DELETE",
				url : getCont() + "admin/attentionGroup/"+obj.id ,
				timeout : 60000,
				data : {},
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status)
					$dtAttentionGroup.ajax.reload();
					$systemModal.modal('hide');
				},
				error : function(x, t, m) {
					unblockUI();
					
				}
			});
		}
	});
}
/*
function saveSystem() {
	blockUI();
	let usersAttentionId = getSelectIds($systemModalForm, "#team");
	$.ajax({
		type : "POST",
		url : getCont() + "admin/system/" + "saveSystem",
		data : {
			// Informacion sistemas
			name : $systemModalForm.find('#name').val(),
			code : $systemModalForm.find('#code').val(),
			usersAttentionId: JSON.stringify(usersAttentionId),
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
*/
/*
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
*/


function ajaxEditSystem(index) {
	$systemModalForm.validate().resetForm();
	$systemModalForm[0].reset();
	var obj = $dtAttentionGroup.row(index).data();
	$systemModalForm.find('#id').val(obj.id);
	$systemModalForm.find('#name').val(obj.name);
	$systemModalForm.find('#code').val(obj.code);
	$systemModalForm.find('#leaderId').selectpicker('val',obj.lead.id);
	var userAttention = [];
	for (var i = 0, l = obj.userAttention.length; i < l; i++) {
		$systemModalForm.find('#team option').each(
				function(index, element) {
					if (element.id == obj.userAttention[i].id) {
						userAttention.push((obj.userAttention[i].id).toString());
					}
				});
	}
	$systemModal.find('#team').multiSelect('select', userAttention);
	$systemModal.modal('show');
	$('#btnUpdateSystem').show();
	$('#btnSaveSystem').hide();
}

function closeSystemModal() {
	$systemModalForm[0].reset();
	$systemModal.find('#managers').multiSelect("deselect_all");
	$systemModal.find('#team').multiSelect("deselect_all");
	$systemModal.modal('hide');
}

function updateSystem() {

	let usersAttentionId = getSelectIds($systemModalForm, "#team");
	console.log(usersAttentionId);
	$.ajax({
		type : "POST",
		url : getCont() + "admin/attentionGroup/",
		data : {
			// Informacion sistema
			id : $systemModalForm.find('#systemId').val(),
			name : $systemModalForm.find('#name').val(),
			code : $systemModalForm.find('#code').val(),
			leaderId:$systemModalForm.find('#leaderId').val(),
			usersAttentionId: usersAttentionId
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

function initAttentionFormValidation() {
	$systemModalForm.validate({
		rules : {
			'code' : {
				required : true,
				minlength : 1,
				maxlength : 10,
			},
			'name' : {
				required : true,
				minlength : 1,
				maxlength : 50,
			},
			
			'leaderId' : {
				required : true,

			},

		},
		messages : {
			'code' : {
				required :  "Ingrese un valor",
				minlength : "Ingrese un valor",
				maxlength : "No puede poseer mas de {0} caracteres"
			},
			'name' : {
				required :  "Ingrese un valor",
				minlength : "Ingrese un valor",
				maxlength : "No puede poseer mas de {0} caracteres"
			},	
			'leaderId' : {
				required :  "Seleccione una opcion",
			},
		},
		highlight,
		unhighlight,
		errorPlacement
	});
}