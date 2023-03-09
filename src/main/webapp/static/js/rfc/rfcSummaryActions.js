var $formChangeStatus = $('#changeStatusForm');
var switchStatus=false;
$(function() {
	//initImpactFormValidation();
	$("#contentSummary textarea").parent().removeClass(
	'focused');
	$("#contentSummary input").parent().removeClass('focused');
	dropDownChange();
	$('.tableIni').DataTable({
		"language": optionLanguaje,
		"searching" : true,
		"paging" : true,
		"bInfo" : true
	});
	
	$('#configurationItemsTable').DataTable({
		"language": optionLanguaje,
		"searching" : true,
		"paging" : true,
		"bInfo" : true
	});
	
	$('#configurationItemsTable').on('draw.dt', function() {
		$("#countObject").text($('#configurationItemsTable').DataTable().rows().count());
	});
	
	
	$('textarea').each(
			function() {
				this.setAttribute('style', 'height:'
						+ (this.scrollHeight)
						+ 'px;overflow-y:hidden;');
			}).on('input', function() {
				this.style.height = 'auto';
				this.style.height = (this.scrollHeight) + 'px';
			});

	$formChangeStatus.find('#statusId').change(function() {
		$formChangeStatus.find('#motive').val($(this).children("option:selected").attr('data-motive'));
	});
	
	$formChangeStatus.find('#motive').prop('disabled', false);
	
	showSendEmail();
	 $('.tagInitMail').tagsInput({
		 placeholder: 'Ingrese los correos'
	 });
});


function confirmCancelRFC(index){
	Swal.fire({
		title: '\u00BFEst\u00e1s seguro que desea cancelar el rfc?',
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
			cancelRFC(index);
		}		
	});
}

function cancelRFC(index) {
	blockUI();
	$.ajax({
		type : "GET",
		url : getCont() + "management/rfc/" + "cancelRFC",
		timeout : 60000,
		data : {
			idRFC : index
		},
		success : function(response) {
			responseCancelRFC(response);
		},
		error : function(x, t, m) {
			notifyAjaxError(x, t, m);
		}
	});
}

function responseCancelRFC(response) {
	switch (response.status) {
	case 'success':
		swal("Correcto!", "El RFC ha sido anulado exitosamente.",
				"success", 2000)
				location.reload(true);
		break;
	case 'fail':
		swal("Error!", response.exception, "error")
		break;
	case 'exception':
		swal("Error!", response.exception, "error")
		break;
	}
}

function changeStatusRFC(releaseId, rfcNumRequest,cc) {
	$formChangeStatus[0].reset();
	// $formChangeStatus.validate().resetForm();
	$formChangeStatus.find('#idRFC').val(releaseId);
	$('.tagInitMail#senders').importTags(cc);
	$formChangeStatus.find('#rfcNumRequest').val(rfcNumRequest);
	$formChangeStatus.find('#dateChange').val(moment().format('DD/MM/YYYY hh:mm a'))
	$formChangeStatus.find('.selectpicker').selectpicker('refresh');
	$formChangeStatus.find(".fieldError").css("visibility", "hidden");
	$formChangeStatus.find('.fieldError').removeClass('activeError');
	$formChangeStatus.find('.form-line').removeClass('error');
	$formChangeStatus.find('.form-line').removeClass('focused');
	$('#divError').attr( "hidden",true);
	$('#changeStatusModal').modal('show');
}

function showSendEmail(){
	$('#sendMail').change(function() {
		// this will contain a reference to the checkbox
		if (this.checked) {
			
			 switchStatus= $(this).is(':checked');
			 console.log(switchStatus);
			$('#divEmail').attr( "hidden",false);
		} else {
			$('#divEmail').attr( "hidden",true);
			switchStatus= $(this).is(':checked');
			 console.log(switchStatus);
		}
		});
}
function saveChangeStatusModal(){

	if (!validStatusRFC())
		return false;
	blockUI();
	$.ajax({
		type : "GET",
		url : getCont() + "management/rfc/statusRFC",
		timeout : 60000,
		data : {
			idRFC : $formChangeStatus.find('#idRFC').val(),
			idStatus: $formChangeStatus.find('#statusId').children("option:selected").val(),
			dateChange: $formChangeStatus.find('#dateChange').val(),
			idError: $formChangeStatus.find('#errorId').children("option:selected").val(),
			motive: $formChangeStatus.find('#motive').val(),
			sendEmail:switchStatus,
			senders:$formChangeStatus.find('#senders').val()
		},
		success : function(response) {
			responseStatusRFC(response);
		},
		error : function(x, t, m) {
			notifyAjaxError(x, t, m);
		}
	});
}

function responseStatusRFC(response) {
	switch (response.status) {
	case 'success':
		swal("Correcto!", "El RFC ha sido modificado exitosamente.",
				"success", 2000);
		location.reload(true);
		closeChangeStatusModal();
		break;
	case 'fail':
		swal("Error!", response.exception, "error")
		break;
	case 'exception':
		swal("Error!", response.exception, "error")
		break;
	}
}

function closeChangeStatusModal(){
	$formChangeStatus[0].reset();
	// $formChangeStatus.validate().resetForm();
		$formChangeStatus.find('#userId').selectpicker('val', '');
		$('#changeStatusModal').modal('hide');
}


function validStatusRFC() {
	let valid = true;
	$formChangeStatus.find(".fieldError").css("visibility", "hidden");
	$formChangeStatus.find('.fieldError').removeClass('activeError');
	$formChangeStatus.find('.form-line').removeClass('error');
	$formChangeStatus.find('.form-line').removeClass('focused');
	
	$.each($formChangeStatus.find('select[required]'), function( index, select ) {
		if($.trim(select.value).length === 0 || select.value === ""){
			
			var statusSelected =$("#statusId").find("option:selected").text();
			if(select.id==="errorId"&&statusSelected!=="Error"){
				valid = true;
			}else{
				$formChangeStatus.find('#'+select.id+"_error").css("visibility","visible");
				$formChangeStatus.find('#'+select.id+"_error").addClass('activeError');
				valid = false;
			}
			

		
		}
	});

	$.each($formChangeStatus.find('textarea[required]'), function( index, textarea ) {
		if($.trim(textarea.value).length == 0 || textarea.value == ""){
			$formChangeStatus.find('#'+textarea.id+"_error").css("visibility","visible");
			$formChangeStatus.find('#'+textarea.id+"_error").addClass('activeError');
			$formChangeStatus.find('#'+textarea.id+"").parent().attr("class",
			"form-line error focused");
			valid = false;
		}
	});
	$.each($formChangeStatus.find('input[required]'), function( index, input ) {
		console.log($.trim(input.value) === "");
		console.log(input.id);
		if($.trim(input.value) === ""){
			console.log(input.id);
			if(input.id==="senders"){
				if(switchStatus){
					$formChangeStatus.find('#'+input.id+"_error").css("visibility","visible");
					$formChangeStatus.find('#'+input.id+"_error").addClass('activeError');
					$formChangeStatus.find('#'+input.id+"").parent().attr("class",
					"form-line error focused");
					valid = false;
				}
			}else{
			$formChangeStatus.find('#'+input.id+"_error").css("visibility","visible");
			$formChangeStatus.find('#'+input.id+"_error").addClass('activeError');
			$formChangeStatus.find('#'+input.id+"").parent().attr("class",
			"form-line error focused");
			valid = false;
			}
		}
	});
	return valid;
}

function dropDownChange(){
	
	$('#statusId').on('change', function(){
		
		var status =$("#statusId").find("option:selected").text();
		console.log(status);
		if(status==="Error"){
			$('#divError').attr( "hidden",false);
		}else{
			$('#divError').attr( "hidden",true);
		}
		
	});
}

function initImpactFormValidation() {
	$formChangeStatus.validate({
		
		rules : {
			'statusId' : {
				required : true,
				
			},
			'motive' : {
				required : true,
				minlength : 1,
				maxlength : 50,
			},
			'dateChange' : {
				required : true,
			
			},
		},
		messages : {
			'statusId' : {
				required :  "Ingrese un valor",
			},
			'motive' : {
				required : "Ingrese un valor",
				minlength : "Ingrese un valor",
				maxlength : "No puede poseer mas de {0} caracteres"
			},
			'dateChange' : {
				required : "Ingrese un valor",
				
			},
		},
		highlight,
		unhighlight,
		errorPlacement
	});
}