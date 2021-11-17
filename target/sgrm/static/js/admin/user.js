var $userTable = $('#userTable').DataTable({
	"language" : {
		"emptyTable" : "No existen registros",
		"zeroRecords" : "No existen registros"
	},
	"searching" : true,
	"paging" : true
});

let $userModal = $('#userModal');
let $userForm = $('#userForm');

let $userPasswordModal = $('#userPasswordModal');
let $userPasswordForm = $('#userPasswordForm');

$(function() {
	$('#userGroups').multiSelect(
			{
			  selectableHeader: "<div class='custom-header'>Roles</div>",
			  selectionHeader: "<div class='custom-header'>Roles asignados</div>",
			  afterSelect : function(values) {
				$userForm.find("#userGroups option[id='" + values + "']").attr("selected", "selected");
			  },
			  afterDeselect : function(values) {
				$userForm.find("#userGroups option[id='" + values + "']").removeAttr('selected');
			  }
			});
	$userForm.find("#ms-userGroups").find(".ms-selectable").before('<label for="name">Roles</label>');
	$userForm.find("#ms-userGroups").find(".ms-selection").before('<label for="name">Roles Asignados</label>');
});

function changeBtnModal(index){
	if(index == 1){
		$userModal.find('#modalTitle').text('Crear Usuario');
		$userModal.find('#btnSave').show();
		$userModal.find('#btnUpdate').hide();
	}else{
		$userModal.find('#modalTitle').text('Modificar Usuario');
		$userModal.find('#btnUpdate').show();
		$userModal.find('#btnSave').hide();
	}
}

// ------ Create User ------
function createUser(){
	clearUserModal();
	changeBtnModal(1);
	$('#userGroups').multiSelect("refresh");
	resetErrors();
	$userModal.modal('show');
}

function saveUser(){
	blockUI();
	let groupIds = getGroupsId($userForm, "#userGroups");
	$.ajax({
		type : "POST",
		url : getCont() + "admin/user/" + "saveUser",
		data : {
			username: $userForm.find('#username').val(),
			shortName: $userForm.find('#shortName').val(),
			fullName: $userForm.find('#fullName').val(),
			emailAddress: $userForm.find('#emailAddress').val(),
			rolesId: JSON.stringify(groupIds)
		},
		success : function(response) {
			responseAjaxSaveUser(response)
		},
		error : function(x, t, m) {
			notifyAjaxError(x, t, m);
		}
	});
}

function responseAjaxSaveUser(response){
	switch (response.status) {
	case 'success':
		location.reload();
		swal("Correcto!","Usuario creado exitosamente.","success", 2000)
		break;
	case 'fail':
		unblockUI();
//		$( "#tabs" ).tabs({ active: # });
		showUserErrors(response.errors, $userForm);
		break;
	case 'exception':
		swal("Exception!", response.exception, "warning")
		break;
	default:
		location.reload();
	}
}

// ------ #Create User ------

// ------ Update User ------
function editUser(index) {
	clearUserModal();
	blockUI();
	$.ajax({
		type : "GET",
		url : getCont() + "admin/user/" + "findUser/" + index,
		data : {},
		success : function(response) {
			if (response == null) {
				swal("Error!", "El Usuario seleccionado no existe.", "error");
			} else {
				unblockUI();
				ajaxEditUser(response);
			}
		},
		error : function(x, t, m) {
			notifyAjaxError(x, t, m);
		}
	});
}

function ajaxEditUser(obj) {
	$userForm.find('#userId').val(obj.id);
	$userForm.find('#fullName').val(obj.fullName);
	$userForm.find('#username').val(obj.username);
	$userForm.find('#shortName').val(obj.shortName);
	$userForm.find('#emailAddress').val(obj.emailAddress);
	$userForm.find('#shortName').val(obj.shortName);

	for (var i = 0, l = obj.authorities.length; i < l; i++) {
		$userForm.find('#userGroups option').each(
				function(index, element) {
					if (element.id == obj.authorities[i].id) {
						$userForm.find(
								"#userGroups option[id='" + element.id + "']")
								.attr("selected", "selected");
					}
				});
	}
	$('#userGroups').multiSelect("refresh");
	resetErrors();
	changeBtnModal(2);
	$userModal.modal('show');
}

function closeUserModal() {
	clearUserModal();
	$userModal.modal('hide');
}

function clearUserModal() {
	$userForm[0].reset();
	$userForm.find('#active').val(0);
	$userForm.find('#userGroups option').each(
			function(index, element) {
				$userForm.find("#userGroups option[id='" + element.id + "']")
						.removeAttr('selected');
			});
}

function updateUser() {
	blockUI();
	let groupIds = getGroupsId($userForm, "#userGroups");
	$.ajax({
		type : "POST",
		url : getCont() + "admin/user/" + "updateUser",
		data : {
			id: $userForm.find('#userId').val(),
			username: $userForm.find('#username').val(),
			shortName: $userForm.find('#shortName').val(),
			fullName: $userForm.find('#fullName').val(),
			emailAddress: $userForm.find('#emailAddress').val(),
			rolesId: JSON.stringify(groupIds)
		},
		success : function(response) {
			responseAjaxUpdateUser(response)
		},
		error : function(x, t, m) {
			notifyAjaxError(x, t, m);
		}
	});
}

function responseAjaxUpdateUser(response) {
	switch (response.status) {
	case 'success':
		location.reload();
		swal("Correcto!","Usuario actualizado exitosamente.","success", 2000)
		break;
	case 'fail':
		unblockUI();
		showUserErrors(response.errors, $userForm);
		break;
	case 'exception':
		swal("Exception!", response.exception, "warning")
		break;
	default:
		location.reload();
	}
}
// ------ #Update User ------


// ------ Soft-Delete User ------
function confirmDeleteUser(index, active) {	
	let textTittle = (active) ? 'desactivar':'activar';
	let textMessage = (active) ? 'activado':'desactivado';
	
	Swal.fire({
		  title: '\u00BFEst\u00e1s seguro que desea '+textTittle+' al usuario?',
		  text: "El usuario puede ser "+textMessage+" nuevamente.",
		  icon: 'question',
		  showCancelButton: true,
		  customClass: 'swal-wide',
		  cancelButtonText: 'Cancelar',
		  cancelButtonColor: '#f14747',
		  confirmButtonColor: '#3085d6',
		  confirmButtonText: 'Aceptar',
		}).then((result) => {
			if(result.value){
				deleteUser(index)
			}		
		});
}

function deleteUser(index) {
	let cont = getCont();
	let form = $userPasswordForm;
	$.ajax({
		async : false,
		type : "POST",
		url : cont + "/admin/user/" + "softDelete",
		timeout : 60000,
		data : {
			userId : index
		},
		success : function(response) {
			answerDeleteUser(response, index);
		},
		error : function(x, t, m) {
			notifyAjaxError(x, t, m);
		}
	});
}

function answerDeleteUser(response, index) {
	switch (response.status) {
	case 'success':
		$('#userTable').find('#softDeleteUser_'+index).attr("onclick",'confirmDeleteUser('+index+','+response.obj+')');
		if(response.obj == true){
			$('#userTable').find('#softDeleteUser_'+index).text('check_circle');
			swal("Correcto!", "Usuario activado correctamente.", "success", 2000)
		}else{
			$('#userTable').find('#softDeleteUser_'+index).text('cancel');
			swal("Correcto!", "Usuario desactivado correctamente.", "success", 2000)
		}
		break;
	case 'fail':
		swal("Error!", response.exception, "error")
		break;
	case 'exception':
		swal("Exception!", response.exception, "warning")
		break;
	default:
		location.reload();
	}
}
// ------ #Soft-Delete User ------


// ------ Remove User ------
function confirmRemoveUser(element) {
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
				removeUser(element);
			}		
		});
}

function removeUser(index){
	blockUI();
	$.ajax({
		type : "DELETE",
		url : getCont() + "admin/" + "user/removeUser/" + index,
		timeout : 60000,
		data : {},
		success : function(response) {
			ajaxRemoveUser(response);
		},
		error : function(x, t, m) {
			notifyAjaxError(x, t, m);
		}
	});
}

function ajaxRemoveUser(response){
	switch (response.status) {
	case 'success':
		location.reload();
		swal("Correcto!", "El usuario ha sido eliminado exitosamente.",
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


// ------ #Remove User ------

// ------ Change password ------
function changePasswordModal(index) {
	clearPasswordModal();
	$userPasswordForm.find('#userId').val(index);
	$userPasswordModal.modal('show');
}

function closePasswordModal() {
	clearPasswordModal();
	$userPasswordModal.modal('hide');
}

function clearPasswordModal() {
	$userPasswordForm[0].reset();
	$userPasswordForm.find(".fieldError").hide();
	$userPasswordForm.find(".form-line").attr("class", "form-line");
}

function updatePassword() {
	let cont = getCont();
	let form = $userPasswordForm;
	if (validUpdatePassword()) {
		$.ajax({
			async : false,
			type : "POST",
			url : cont + "/admin/user/" + "changePassword",
			timeout : 60000,
			data : {
				userId : form.find("#userId").val(),
				newPassword : form.find("#newPassword").val(),
				confirmPassword : form.find("#confirmPassword").val(),
			},
			success : function(response) {
				answerUpdatePassword(response);
			},
			error : function(x, t, m) {
				notifyAjaxError(x, t, m);
			}
		});
	}
}

function answerUpdatePassword(response) {
	switch (response.status) {
	case 'success':
		closePasswordModal();
		swal("Correcto!",
				"Tu contrase\u00f1a a sido actualizada exitosamente.",
				"success")
		break;
	case 'fail':
		swal("Error!", response.exception, "error")
		break;
	case 'exception':
		swal("Exception!", response.exception, "warning")
		break;
	default:
		location.reload();
	}
}

function validUpdatePassword() {
	let answer = true;
	let form = $userPasswordForm;
	let $newPassword = form.find("#newPassword");
	let $newPassword_field = form.find("#newPassword_field");
	let $confirmPassword = form.find("#confirmPassword");
	let $confirmPassword_field = form.find("#confirmPassword_field");

	if ($newPassword.val().trim().length <= 7) {
		$newPassword_field.text('Requere al menos 8 caracteres.');
		$newPassword.parent().attr("class", "form-line focused error");
		$newPassword_field.show();
		answer = false;
	} else {
		$newPassword_field.hide();
		$newPassword.parent().attr("class", "form-line focused");
	}
	if ($confirmPassword.val().trim().length <= 7) {
		$confirmPassword_field.text('Requere al menos 8 caracteres.');
		$confirmPassword.parent().attr("class", "form-line focused error");
		$confirmPassword_field.show();
		answer = false;
	} else {
		$confirmPassword_field.hide();
		$confirmPassword.parent().attr("class", "form-line focused");
	}

	if ($.isNumeric($newPassword.val())) {
		$newPassword_field.text('No puede contener solo n\u00FAmeros.');
		$newPassword.parent().attr("class", "form-line focused error");
		$newPassword_field.show();
		answer = false;
	}
	if ($.isNumeric($confirmPassword.val())) {
		$confirmPassword_field.text('No puede contener solo n\u00FAmeros.');
		$confirmPassword.parent().attr("class", "form-line focused error");
		$confirmPassword_field.show();
		answer = false;
	}
	return answer;
}
// ------ #Change password ------

function listGroupRoles() {
	var list = [];
	$userForm.find("#userGroups > option").each(function() {
		if ($(this).is(':selected')) {
			list.push(Number($(this).attr('id')));
			// alert(this.text + ' ' + this.value);
		}
	});
	return list;
}


function getGroupsId(form, name){
	let list = [];
	form.find(name).children("option:selected").each(function(j) {
		list.push(Number($(this).attr('id')));
	});
	return list;
	
}

function resetErrors() {
	$(".fieldError").css("visibility", "hidden");
	$(".fieldError").attr("class", "error fieldError");
	$(".fieldErrorLine").attr("class", "form-line");
}

function showUserErrors(error, $form) {
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
