/** Declaración de variables globales del contexto * */
var $dtUser;
let $mdUser = $('#userModal');
let $fmUser = $('#userModalForm');
let $mdUserPassword = $('#userPasswordModal');
let $fmUserPassword = $('#userPasswordModalForm');

$(function() {
	activeItemMenu("userItem", true);
	initRoles();
	initUserFormValidation();
	initDataTable();
});

$("#tableFilters #fStatus").change(function() {
	$dtUser.ajax.reload();
});


function showUser(index){
	$fmUser.validate().resetForm();
	$fmUser[0].reset();
	$('.nav-tabs a[href="#tabHome"]').tab('show');
	var obj = $dtUser.row(index).data();
	$fmUser.find('#uId').val(obj.id);
	$fmUser.find('#uUserName').val(obj.userName);
	$fmUser.find('#uName').val(obj.name);
	$fmUser.find('#uEmail').val(obj.email);
	$('#userGroups').multiSelect('deselect_all');
	obj.roles.forEach(function (element) {
		$('#userGroups').multiSelect('select', element.code);
	});
	$('#userGroups').multiSelect("refresh");
	$mdUser.find('#update').show();
	$mdUser.find('#save').hide();
	$mdUser.modal('show');
}


function updateUser() {
	if (!$fmUser.valid())
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
				url : getCont() + "admin/user/" ,
				dataType : "json",
				contentType: "application/json; charset=utf-8",
				timeout : 60000,
				data : JSON.stringify({
					id : $fmUser.find('#uId').val(),
					code : $fmUser.find('#uUserName').val(),
					name : $fmUser.find('#uName').val(),
					email : $fmUser.find('#uEmail').val(),
					strRoles : $fmUser.find('#userGroups').val()
				}),
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status)
					$dtUser.ajax.reload();
					$mdUser.modal('hide');
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

function createUser(){
	$fmUser.validate().resetForm();
	$fmUser[0].reset();
	$('.nav-tabs a[href="#tabHome"]').tab('show');
	$('#userGroups').multiSelect('deselect_all');
	$('#userGroups').multiSelect("refresh");
	$mdUser.find('#update').hide();
	$mdUser.find('#save').show();
	$mdUser.modal('show');
}

function setRoles(){}

function closeUser(){
	$mdUser.modal('hide');
}


function initRoles(){
	$('#userGroups').multiSelect(
			{
				selectableHeader: "<div class='custom-header'>Roles</div>",
				selectionHeader: "<div class='custom-header'>Roles asignados</div>",
				afterSelect : function(values) {
					$fmUser.find("#userGroups option[id='" + values + "']").attr("selected","selected");
				},
				afterDeselect : function(values) {
					$fmUser.find("#userGroups option[id='" + values +"']").removeAttr('selected');
				}
			});
	$fmUser.find("#ms-userGroups").find(".ms-selectable").before('<label for="name">Roles</label>');
	$fmUser.find("#ms-userGroups").find(".ms-selection").before('<label for="name">Roles Asignados</label>');
}

function initDataTable() {
	$dtUser = $('#userTable')
	.DataTable(
			{
				lengthMenu : [ [ 10, 25, 50, -1 ],
					[ '10', '25', '50', 'Mostrar todo' ] ],
					"iDisplayLength" : 10,
					"language" : optionLanguaje,
					"iDisplayStart" : 0,
					"processing" : true,
					"serverSide" : true,
					"sAjaxSource" : getCont() + "admin/user/list",
					"fnServerParams" : function(aoData) {
						aoData.push({
							"name" : "sStatus",
							"value" : $('#tableFilters #fStatus').val()
						});
					},
					"aoColumns" : [
						{
							"mDataProp" : "name"
						},
						{
							"mDataProp" : "userName"
						},
						{
							"mDataProp" : "email"
						},
						{
							"mRender" : function(data, type, row, meta) {
								if (row.lastLogin)
									return moment(row.lastLogin)
									.format(
									'DD/MM/YYYY h:mm:ss a');
								else
									return '';
							},
						},
						{
							"mRender" : function(data, type, row, meta) {
								return '<div class="iconLine align-center">'
								+ '<i onclick="softDelete('
								+ meta.row
								+ ')" class="material-icons gris" style="font-size: 30px;">'
								+ (row.active ? 'check_circle'
										: 'cancel')
										+ '</i></div>';
							},
						},
						{
							"mRender" : function(data, type, row, meta) {
								let options = '';
								options += '<i onclick="showUser('
									+ meta.row
									+ ')" class="material-icons gris" style="font-size: 30px;">mode_edit</i>';

								options += '<i onclick="changePassword('
									+ meta.row
									+ ')" class="material-icons gris" style="font-size: 30px;">lock</i>';

								return options;
							},
						} ],
						ordering : false,
			});
}

function initUserFormValidation() {
	$fmUser.validate({
		rules : {
			'uUserName' : {
				required : true,
				minlength : 1,
				maxlength : 50,
			},
			'uName' : {
				required : true,
				minlength : 1,
				maxlength : 100
			},
			'uEmail' : {
				required : true,
				minlength : 1,
				maxlength : 250
			},
		},
		messages : {
			'uUserName' : {
				required :  "Ingrese un valor",
				minlength : "Ingrese un valor",
				maxlength : "No puede poseer mas de 50 caracteres"
			},
			'uName' : {
				required : "Ingrese un valor",
				minlength : "Ingrese un valor",
				maxlength : "No puede poseer mas de 100 caracteres"
			},
			'uEmail' : {
				required : "Ingrese un valor",
				minlength : "Ingrese un valor",
				maxlength : "No puede poseer mas de 250 caracteres"
			},
		},
		highlight,
		unhighlight,
		errorPlacement
	});
}