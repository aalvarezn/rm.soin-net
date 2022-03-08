/** Declaración de variables globales del contexto * */
var $dtSystem;
let $mdSystem = $('#systemModal');
let $fmSystem = $('#systemModalForm');
let $mdSystemPassword = $('#systemPasswordModal');
let $fmSystemPassword = $('#systemPasswordModalForm');


$(function() {
	activeItemMenu("systemItem", true);
	initDataTable();
	initMultiSelect();
	initSystemFormValidation();
});

$("#tableFilters #fProject").change(function() {
	$dtSystem.ajax.reload();
});


function showSystem(index){
	$fmSystem.validate().resetForm();
	$fmSystem[0].reset();
	$('.nav-tabs a[href="#tabHome"]').tab('show');
	var obj = $dtSystem.row(index).data();
	$fmSystem.find('#sId').val(obj.id);
	$fmSystem.find('#sName').val(obj.name);
	$fmSystem.find('#sCode').val(obj.code);
	$fmSystem.find('#sLeader').selectpicker('val', obj.leader.userName);
	$fmSystem.find('#sProject').selectpicker('val',  obj.project.code);

	$('#sTeam').multiSelect('deselect_all');
	obj.team.forEach(function (element) {
		$('#sTeam').multiSelect('select', element.userName);
	});
	$('#sTeam').multiSelect("refresh");

	$('#sManagers').multiSelect('deselect_all');
	obj.managers.forEach(function (element) {
		$('#sManagers').multiSelect('select', element.userName);
	});
	$('#sManagers').multiSelect("refresh");

	$fmSystem.find('#sImportObjects').prop('checked', obj.importObjects);
	$fmSystem.find('#sCustomCommands').prop('checked', obj.customCommands);

	$mdSystem.find('#update').show();
	$mdSystem.find('#save').hide();
	$mdSystem.modal('show');
}

function addSystem(){
	$fmSystem.validate().resetForm();
	$fmSystem[0].reset();
	$('.nav-tabs a[href="#tabHome"]').tab('show');

	$fmSystem.find('#sLeader').selectpicker('val', "");
	$fmSystem.find('#sProject').selectpicker('val',  "");

	$('#sTeam').multiSelect('deselect_all');
	$('#sTeam').multiSelect("refresh");

	$('#sManagers').multiSelect('deselect_all');
	$('#sManagers').multiSelect("refresh");

	$mdSystem.find('#save').show();
	$mdSystem.find('#update').hide();
	$mdSystem.modal('show');
}

function closeSystem(){
	$mdSystem.modal('hide');
}

function updateSystem() {
	if (!$fmSystem.valid())
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
				url : getCont() + "admin/system/" ,
				dataType : "json",
				contentType: "application/json; charset=utf-8",
				timeout : 60000,
				data : JSON.stringify({
					id : $fmSystem.find('#sId').val(),
					name : $fmSystem.find('#sName').val(),
					code : $fmSystem.find('#sCode').val(),
					leaderUserName : $fmSystem.find('#sLeader').val(),
					projectCode : $fmSystem.find('#sProject').val(),
					importObjects: $fmSystem.find('#sImportObjects').is(':checked'),
					customCommands: $fmSystem.find('#sCustomCommands').is(':checked'),
					strTeam : $fmSystem.find('#sTeam').val(),
					strManagers : $fmSystem.find('#sManagers').val()
				}),
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status)
					$dtSystem.ajax.reload();
					$mdSystem.modal('hide');
				},
				error : function(x, t, m) {
					unblockUI();
					console.log(x);
					console.log(t);
					console.log(m);
				}
			});
		}
	});
}

function saveSystem() {
	if (!$fmSystem.valid())
		return;
	Swal.fire({
		title: '\u00BFEst\u00e1s seguro que desea agregar el registro?',
		text: 'Esta acci\u00F3n no se puede reversar.',
		...swalDefault
	}).then((result) => {
		if(result.value){
			blockUI();
			$.ajax({
				type : "POST",
				url : getCont() + "admin/system/" ,
				dataType : "json",
				contentType: "application/json; charset=utf-8",
				timeout : 60000,
				data : JSON.stringify({
					name : $fmSystem.find('#sName').val(),
					code : $fmSystem.find('#sCode').val(),
					leaderUserName : $fmSystem.find('#sLeader').val(),
					projectCode : $fmSystem.find('#sProject').val(),
					importObjects: $fmSystem.find('#sImportObjects').is(':checked'),
					customCommands: $fmSystem.find('#sCustomCommands').is(':checked'),
					strTeam : $fmSystem.find('#sTeam').val(),
					strManagers : $fmSystem.find('#sManagers').val()
				}),
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status)
					$dtSystem.ajax.reload();
					$mdSystem.modal('hide');
				},
				error : function(x, t, m) {
					unblockUI();
					console.log(x);
					console.log(t);
					console.log(m);
				}
			});
		}
	});
}

function deleteSystem (index) {
	var obj = $dtSystem.row(index).data();
	Swal.fire({
		title: '\u00BFEst\u00e1s seguro que desea eliminar el registro?',
		text: 'Esta acci\u00F3n no se puede reversar.',
		...swalDefault
	}).then((result) => {
		if(result.value){
			blockUI();
			$.ajax({
				type : "DELETE",
				url : getCont() + "admin/system/"+obj.id ,
				timeout : 60000,
				data : {},
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status)
					$dtSystem.ajax.reload();
					$mdSystem.modal('hide');
				},
				error : function(x, t, m) {
					unblockUI();
					console.log(x);
					console.log(t);
					console.log(m);
				}
			});
		}
	});
}


function initMultiSelect(){
	$('#sTeam').multiSelect(
			{
				selectableHeader: "<div class='custom-header'>Usuarios</div><input type='text' class='search-input filterMS' autocomplete='off' placeholder='Buscar ...'>",
				selectionHeader: "<div class='custom-header'>Usuarios Equipo</div><input type='text' class='search-input filterMS' autocomplete='off' placeholder='Buscar ...'>",
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
					$fmSystem.find("#sTeam option[id='" + values + "']").attr("selected","selected");
				},
				afterDeselect : function(values) {
					$fmSystem.find("#sTeam option[id='" + values +"']").removeAttr('selected');
				}
			});
	$('#sManagers').multiSelect(
			{
				selectableHeader: "<div class='custom-header'>Usuarios</div><input type='text' class='search-input filterMS' autocomplete='off' placeholder='Buscar ...'>",
				selectionHeader: "<div class='custom-header'>Usuarios Gestores</div><input type='text' class='search-input filterMS' autocomplete='off' placeholder='Buscar ...'>",
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
					$fmSystem.find("#sManagers option[id='" + values + "']").attr("selected","selected");
				},
				afterDeselect : function(values) {
					$fmSystem.find("#sManagers option[id='" + values +"']").removeAttr('selected');
				}
			});
}

function initDataTable() {
	$dtSystem = $('#systemTable')
	.DataTable(
			{
				lengthMenu : [ [ 10, 25, 50, -1 ],
					[ '10', '25', '50', 'Mostrar todo' ] ],
					"iDisplayLength" : 10,
					"language" : optionLanguaje,
					"iDisplayStart" : 0,
					"processing" : true,
					"serverSide" : true,
					"sAjaxSource" : getCont() + "admin/system/list",
					"fnServerParams" : function(aoData) {
						aoData.push({
							"name" : "sProject",
							"value" : $('#tableFilters #fProject').val()
						});
					},
					"aoColumns" : [
						{
							"mDataProp" : "code"
						},
						{
							"mDataProp" : "name"
						},
						{
							"mDataProp" : "leader.name"
						},
						{
							"mDataProp" : "project.description"
						},
						{
							render : function(data, type, row, meta) {
								var options = '<div class="iconLine">';

								options += '<a onclick="showSystem('
									+ meta.row
									+ ')" title="Editar"><i class="material-icons gris">mode_edit</i></a>';

								options += '<a onclick="deleteSystem('
									+ meta.row
									+ ')" title="Borrar"><i class="material-icons gris">delete</i></a>';

								options += ' </div>';

								return options;
							}
						} ],
						ordering : false,
			});
}

function initSystemFormValidation() {
	$fmSystem.validate({
		ignore: ":hidden:not(.selectpicker)",
		rules : {
			'sCode' : {
				required : true,
				minlength : 1,
				maxlength : 10
			},
			'sName' : {
				required : true,
				minlength : 1,
				maxlength : 50
			},
			'sLeader' : {
				required : true,
			},
			'sProject' : {
				required : true,
			},
		},
		messages : {
			'sCode' : {
				required :  "Ingrese un valor",
				minlength : "Ingrese un valor",
				maxlength : "No puede poseer mas de {0} caracteres"
			},
			'sName' : {
				required : "Ingrese un valor",
				minlength : "Ingrese un valor",
				maxlength : "No puede poseer mas de {0} caracteres"
			},
			'sLeader' : {
				required : "Ingrese un valor",
			},
			'sProject' : {
				required : "Ingrese un valor",
			},
		},
		highlight,
		unhighlight,
		errorPlacement
	});
}