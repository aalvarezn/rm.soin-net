
var $dtEmail;
var $mdAmbient = $('#ambientModal');



$(function() {
	activeItemMenu("emailItem", true);
	initDataTable();
});


function initDataTable() {
	$dtEmail = $('#emailTable')
	.DataTable(
			{
				lengthMenu : [ [ 10, 25, 50, -1 ],
					[ '10', '25', '50', 'Mostrar todo' ] ],
					"iDisplayLength" : 10,
					"language" : optionLanguaje,
					"iDisplayStart" : 0,
					"sAjaxSource" : getCont() + "admin/email/list",
					"fnServerParams" : function(aoData) {
					},
					"aoColumns" : [
						{
							"mDataProp" : 'name'
						},
						{
							"mDataProp" : 'to'
						},
						{
							render:function(data,type,row,meta){
								var ccSplit="";
								if(row.cc!=null){
									ccSplit=row.cc.replace(/,/g, ', ');
								}
		
								
								return ccSplit;
							}
						},
						
						{
							"mDataProp" : 'subject'
						},
						
						{
							"mDataProp" : 'date',render:function(data){
							      return moment(data).format('DD/MM/YYYY h:mm:ss a');
							}
						},	
						{
							render : function(data, type, row, meta) {
								var options = '<div class="iconLine">';

								options += '<a onclick="editEmail('
									+ meta.row
									+ ')" title="Editar"><i class="material-icons gris">mode_edit</i></a>'
								options += '<a onclick="deleteEmail('
									+ meta.row
									+ ')" title="Borrar"><i class="material-icons gris">delete</i></a>';

								options += ' </div>';

								return options;
							}
						} ],
						ordering : false,
			});
}

function editEmail(index){
	var obj=$dtEmail.row(index).data();
	location.href =getCont() +"admin/email/editemail/"+obj.id;
}

function openAddEmail(){
	location.href =getCont() +"admin/email/editemail/";
}




/*
$(function() {
	activeItemMenu("configurationItem", true);
});
let $emailForm = $('#emailForm');
let $emailTable = $('#emailTable').DataTable({
	"language" : {
		"emptyTable" : "No existen registros",
		"zeroRecords" : "No existen registros"
	},
	"ordering" : true,
	"searching" : true,
	"paging" : true
});

$('.tagInit').tagsInput();

$(function() {
	$emailForm.find("textarea").parent().removeClass('focused');
	$emailForm.find("input").parent().removeClass('focused');

	$('#htmlBody').summernote({
		lang : 'es-ES',
		placeholder : 'Ingrese su plantilla de correo',
		tabsize : 2,
		height : 320
	});
});

function sendEmailTest() {
	resetErrors();
	blockUI();
	$.ajax({
		type : "POST",
		url : getCont() + "admin/email/emailTest",
		data : {
			// Informacion Email
			name : $emailForm.find('#name').val(),
			to : $emailForm.find('#to').val(),
			cc : $emailForm.find('#cc').val(),
			subject : $emailForm.find('#subject').val(),
			html : $('#htmlBody').summernote('code')
		},
		success : function(response) {
			ajaxSendEmailTest(response);
		},
		error : function(x, t, m) {
			console.log(x);
			console.log(t);
			console.log(m);
			notifyAjaxError(x, t, m);
		}
	});
}

function ajaxSendEmailTest(response) {

	switch (response.status) {
	case 'success':
		swal("Correcto!", "Correo enviado exitosamente.", "success", 2000)
		break;
	case 'fail':
		showEmailErrors(response.errors, $emailForm);
		unblockUI();
		break;
	case 'exception':
		swal("Exception!", response.exception, "warning")
		break;
	default:
		location.reload();
	}

}

function updateEmail() {
	resetErrors();
	blockUI();
	$.ajax({
		type : "POST",
		url : getCont() + "admin/email/updateEmail",
		data : {
			// Informacion Email
			id : $emailForm.find('#emailId').val(),
			name : $emailForm.find('#name').val(),
			to : $emailForm.find('#to').val(),
			cc : $emailForm.find('#cc').val(),
			subject : $emailForm.find('#subject').val(),
			html : $('#htmlBody').summernote('code')
		},
		success : function(response) {
			ajaxUpdateEmail(response);
		},
		error : function(x, t, m) {
			console.log(x);
			console.log(t);
			console.log(m);
			notifyAjaxError(x, t, m);
		}
	});
}

function ajaxUpdateEmail(response) {
	switch (response.status) {
	case 'success':
		swal("Correcto!", "Plantilla de correo actualizada exitosamente.",
				"success", 2000)
		break;
	case 'fail':
		showEmailErrors(response.errors, $emailForm);
		unblockUI();
		break;
	case 'exception':
		swal("Exception!", response.exception, "warning")
		break;
	default:
		location.reload();
	}
}

function copyToClipboard($element) {
	var txt = $element.attr("data-type");
	var $temp = $("<input>")
	$("body").append($temp);
	$temp.val(txt).select();
	document.execCommand("copy");
	$temp.remove();
	notifyInfo('Copiado a portapapales');
}

let $emailModal = $('#emailModal');
let $emailModalForm = $('#emailModalForm');

function openEmailModal() {
	clearEmailModal()
	$emailModal.modal('show');
}

function closeEmailModal() {
	clearEmailModal();
	$emailModal.modal('hide');
}

function clearEmailModal() {
	$emailModalForm[0].reset();
}

function saveEmail() {
	resetErrors();
	blockUI();
	$.ajax({
		type : "POST",
		url : getCont() + "admin/email/saveEmail",
		data : {
			// Informacion Email
			name : $emailModalForm.find('#name').val(),
			subject : $emailModalForm.find('#subject').val()
		},
		success : function(response) {
			ajaxSaveEmail(response);
		},
		error : function(x, t, m) {
			console.log(x);
			console.log(t);
			console.log(m);
			notifyAjaxError(x, t, m);
		}
	});
}

function ajaxSaveEmail(response) {
	switch (response.status) {
	case 'success':
		window.location = getCont() + "admin/email/editarEmail-"
				+ response.obj.id;
		swal("Correcto!", "Plantilla de correo creada exitosamente.",
				"success", 2000)
		break;
	case 'fail':
		showEmailErrors(response.errors, $emailModalForm);
		unblockUI();
		break;
	case 'exception':
		swal("Exception!", response.exception, "warning")
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

function showEmailErrors(error, $form) {
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


function confirmDeleteEmail(element) {
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
				deleteEmail(element);
			}		
		});
}


function deleteEmail(element){
	blockUI();
	var cont = getCont();
	$.ajax({
		type : "DELETE",
		url : cont + "admin/" + "email/deleteEmail/" + element,
		timeout : 60000,
		data : {},
		success : function(response) {
			ajaxDeleteEmail(response);
		},
		error : function(x, t, m) {
			notifyAjaxError(x, t, m);
		}
	});
}

function ajaxDeleteEmail(response){
	switch (response.status) {
	case 'success':	
		$emailTable.row($('#emailTable #'+ response.obj)).remove().draw();
		swal("Correcto!", "El correo ha sido eliminado exitosamente.",
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
*/
