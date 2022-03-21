var id=$('#eId').val();
var $fmEmail = $('#emailForm'); 
$(function() {
	activeItemMenu("configurationItem", true);
	initEmailFormValidation();
	changeButton();
	$('#eBody').summernote({
		lang : 'es-ES',
		placeholder : 'Ingrese su plantilla de correo',
		tabsize : 2,
		height : 320
	});
	
});


function updateEmail() {


	if (!$fmEmail.valid())
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
				url : getCont() + "admin/email/" ,
				dataType : "json",
				contentType: "application/json; charset=utf-8",
				timeout : 60000,
				data : JSON.stringify({
					id : $fmEmail.find('#eId').val(),
					name : $fmEmail.find('#eName').val(),
					to : $fmEmail.find('#eTo').val(),
					cc : $fmEmail.find('#eCc').val(),
					subject: $fmEmail.find('#eSubject').val(),
					body: $fmEmail.find('#eBody').val()
				}),
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status)
					//$dtAmbient.ajax.reload();
					//$mdAmbient.modal('hide');
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


function saveEmail() {
	console.log($fmEmail.valid());
	if (!$fmEmail.valid())
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
				url : getCont() + "admin/email/" ,
				dataType : "json",
				contentType: "application/json; charset=utf-8",
				timeout : 60000,
				data : JSON.stringify({
					name : $fmEmail.find('#eName').val(),
					to : $fmEmail.find('#eTo').val(),
					cc : $fmEmail.find('#eCc').val(),
					subject: $fmEmail.find('#eSubject').val(),
					body: $fmEmail.find('#eBody').val()
				}),
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status)
					id= response.data;
					$('#eId').val(id);
					changeButton();
					//$dtAmbient.ajax.reload();
					//$mdAmbient.modal('hide');
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

function sendEmailTest() {
	//resetErrors();
	blockUI();
	$.ajax({
		type : "POST",
		url : getCont() + "admin/email/emailTest",
		data : {
			// Informacion Email
			name : $fmEmail.find('#eName').val(),
			to : $fmEmail.find('#eTo').val(),
			cc : $fmEmail.find('#eCc').val(),
			subject: $fmEmail.find('#eSubject').val(),
			body: $fmEmail.find('#eBody').val()
		},
		success : function(response) {
			unblockUI();
			notifyMs(response.message, response.status)
		},
		error : function(x, t, m) {
			console.log(x);
			console.log(t);
			console.log(m);
			
		}
	});
}


function changeButton(){
	if(!isEmpty(id)){
		$('#save').hide();
		$('#update').show();
	}else{
		$('#save').show();
		$('#update').hide();
	}
}
function isEmpty(str) {
    return (!str || str.length === 0 );
}
$('.tagInit').tagsInput();

function copyToClipboard($element) {
	var txt = $element.attr("data-type");
	var $temp = $("<input>")
	$("body").append($temp);
	$temp.val(txt).select();
	document.execCommand("copy");
	$temp.remove();
	notifyInfo('Copiado a portapapales');
}
function initEmailFormValidation() {
	$fmEmail.validate({
		rules : {
			'eName' : {
				required : true,
				minlength : 1,
				maxlength : 30,
			},
			'eTo' : {
				minlength : 1,
				maxlength : 255
			},
			'eCc' : {
				minlength : 1,
				maxlength : 350
			},
			'eSubject' : {
				minlength : 1,
				maxlength : 100
			},

		},
		messages : {
			'eName' : {
				required :  "Ingrese un valor",
				minlength : "Ingrese un valor",
				maxlength : "No puede poseer mas de {0} caracteres"
			},
			'eTo' : {
				minlength : "Ingrese un valor",
				maxlength : "No puede poseer mas de {0} caracteres"
			},
			'eCc' : {
				minlength : "Ingrese un valor",
				maxlength : "No puede poseer mas de {0} caracteres"
			},
			'eSubject' : {
				minlength : "Ingrese un valor",
				maxlength : "No puede poseer mas de {0} caracteres"
			},

		},
		highlight,
		unhighlight,
		errorPlacement
	});
}