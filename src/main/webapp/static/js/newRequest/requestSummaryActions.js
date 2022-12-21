var $formChangeStatus = $('#changeStatusForm');
$(function() {
	
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
	
	dropDownChange();
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
});


function confirmCancelRequest(index){
	Swal.fire({
		title: '\u00BFEst\u00e1s seguro que desea cancelar la solicitud?',
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
			cancelRequest(index);
		}		
	});
}
function cancelRequest(index) {
	blockUI();
	$.ajax({
		type : "GET",
		url : getCont() + "management/request/" + "cancelRequest",
		timeout : 60000,
		data : {
			idRequest : index
		},
		success : function(response) {
			responseCancelRequest(response);
		},
		error : function(x, t, m) {
			notifyAjaxError(x, t, m);
		}
	});
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

function responseCancelRequest(response) {
	switch (response.status) {
	case 'success':
		swal("Correcto!", "La solicitud ha sido anulado exitosamente.",
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

function changeStatusRequest(requestId, requestNumRequest) {
	$formChangeStatus[0].reset();
	$formChangeStatus.find('#idRequest').val(requestId);
	$formChangeStatus.find('#requestNumRequest').val(requestNumRequest);
	$formChangeStatus.find('.selectpicker').selectpicker('refresh');
	$formChangeStatus.find('#dateChange').val(moment().format('DD/MM/YYYY hh:mm a'));
	$formChangeStatus.find('.selectpicker').selectpicker('refresh');
	$formChangeStatus.find("#statusId_error").css("visibility", "hidden");
	$formChangeStatus.find(".fieldError").css("visibility", "hidden");
	$formChangeStatus.find('.fieldError').removeClass('activeError');
	$formChangeStatus.find('.form-line').removeClass('error');
	$formChangeStatus.find('.form-line').removeClass('focused');
	$('#divError').attr( "hidden",true);
	
	$('#changeStatusModal').modal('show');
}

function saveChangeStatusModal(){

	if (!validStatusRequest())
		return false;
	blockUI();
	$.ajax({
		type : "GET",
		url : getCont() + "management/request/statusRequest",
		timeout : 60000,
		data : {
			idRequest : $formChangeStatus.find('#idRequest').val(),
			idStatus: $formChangeStatus.find('#statusId').children("option:selected").val(),
			dateChange: $formChangeStatus.find('#dateChange').val(),
			idError: $formChangeStatus.find('#errorId').children("option:selected").val(),
			motive: $formChangeStatus.find('#motive').val()
		},
		success : function(response) {
			responseStatusRequest(response);
		},
		error : function(x, t, m) {
			notifyAjaxError(x, t, m);
		}
	});
}

function responseStatusRequest(response) {
	switch (response.status) {
	case 'success':
		swal("Correcto!", "La solicitud ha sido modificado exitosamente.",
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
	$formChangeStatus.find('#userId').selectpicker('val', '');
	$('#changeStatusModal').modal('hide');
}


function validStatusRequest() {
	let valid = true;
	$formChangeStatus.find(".fieldError").css("visibility", "hidden");
	$formChangeStatus.find('.fieldError').removeClass('activeError');
	$formChangeStatus.find('.form-line').removeClass('error');
	$formChangeStatus.find('.form-line').removeClass('focused');
	$.each($formChangeStatus.find('input[required]'), function( index, input ) {
		if($.trim(input.value) == ""){
			console.log(input.id);
			$formChangeStatus.find('#'+input.id+"_error").css("visibility","visible");
			$formChangeStatus.find('#'+input.id+"_error").addClass('activeError');
			$formChangeStatus.find('#'+input.id+"").parent().attr("class",
			"form-line error focused");
			valid = false;
		}
	});
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

	return valid;
}
