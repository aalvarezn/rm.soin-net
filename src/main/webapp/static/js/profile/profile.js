function confirmChangePassword() {
	if (valid()) {
		Swal.fire({
			  title: 'Desea cambiar la contrase\u00f1a?',
			  text: "Es posible que requiera iniciar sesi\u00F3n para validar los cambios!",
			  icon: 'question',
			  showCancelButton: true,
			  customClass: 'swal-wide',
			  cancelButtonText: 'Cancelar',
			  cancelButtonColor: '#f14747',
			  confirmButtonColor: '#3085d6',
			  confirmButtonText: 'Aceptar',
			}).then((result) => {
				if(result.value){
					changePassword();
				}		
			});
	}

}

function changePassword() {

	var cont = getCont();
	var form = "#formChangePassword";

	var response = ajaxChangePassword(cont, form);
	if (response != null) {

		switch (response.status) {
		case 'success':
			$("#formChangePassword")[0].reset();
			matches(true);
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

}

function ajaxChangePassword(cont, form) {
	var answer = null;
	$.ajax({
		async : false,
		type : "POST",
		url : cont + "profile/" + "changePassword",
		timeout : 60000,
		data : {
			oldPassword : $(form + " #oldPassword").val(),
			newPassword : $(form + " #newPassword").val(),
			confirmPassword : $(form + " #confirmPassword").val()
		},
		success : function(response) {
			answer = response;
		},
		error : function(x, t, m) {
			notifyAjaxError(x, t, m);
		}
	});
	return answer;
}

function valid() {
	var oldPassword = $('#oldPassword').val();
	var confirmPassword = $('#confirmPassword').val();
	var newPassword = $('#newPassword').val();

	var valid = matches(false) && ((oldPassword.trim().length) >= 8)
			&& ((confirmPassword.trim().length) >= 8)
			&& ((newPassword.trim().length) >= 8);

	if (valid) {
		$('#change').removeAttr('disabled');
	} else {
		$("#change").attr("disabled", true);
	}

	return valid;
}

// PasswordOld
$("#oldPassword").bind("contextmenu", function(e) {
	e.preventDefault();
});

// PasswordConfirm
$("#confirmPassword").bind("contextmenu", function(e) {
	e.preventDefault();
});

// PasswordNew
$("#newPassword").bind("contextmenu", function(e) {
	e.preventDefault();
});

function myEvent(field, match) {
	if ((($('#' + field).val().trim().length) >= 8)
			&& !$.isNumeric($('#' + field).val())) {
		$('#' + field).parent().attr("class", "form-line focused");
	} else {
		$('#' + field).parent().attr("class", "form-line focused error");
	}
	if (match) {
		matches(true);
	}
	valid();
}

function matches(alert) {
	var confirmPassword = $('#confirmPassword').val();
	var newPassword = $('#newPassword').val();

	if (!alert) {
		return (confirmPassword == newPassword)
				&& !$.isNumeric($('#newPassword').val())
				&& !$.isNumeric($('#confirmPassword').val());
	}

	if (confirmPassword == newPassword) {
		$('#mensaje_error').hide();
		$('#mensaje_error').attr("class",
				"control-label col-md-12 text-success");
		$('#mensaje_error').show();
		$('#mensaje_error').html("Las constrase\u00f1as si coinciden");
	} else {

		$('#mensaje_error').show();
		$('#mensaje_error')
				.attr("class", "control-label col-md-12 text-danger");
		$('#mensaje_error').html("Las constrase\u00f1as no coinciden");

	}

	if ((confirmPassword.trim().length + newPassword.trim().length) == 0) {
		$('#mensaje_error').hide();
	}
}