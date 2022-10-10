var $formChangeStatus = $('#changeStatusForm');
$(function() {
	initImpactFormValidation();
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
	
	
	setInterval(waitAndshow, 1000);
	//createFaction();
});

function waitAndshow() {
	var initialDate=Date.parse($('#dateInitial').val())/1000;
	var finalDate=Date.parse($('#dateFinal').val())/1000
	var	attentionTime=$('#attetionTime').val();
	console.log(attentionTime);
	var totalMinutes=(new Date().getTime()/1000-initialDate)/60;
	var miliTrans=new Date().getTime()/1000-initialDate;
	console.log(miliTrans);
	const hours = Math.floor(totalMinutes / 60);
	 const minutes = (totalMinutes % 60).toFixed(0);
	 if(minutes<10){
		 var timer=hours+":0"+minutes;
	 }else{
		 var timer=hours+":"+minutes;
	 }
	
	 document.getElementById('timer').innerHTML = timer;
	 
	 if(miliTrans>attentionTime){
		 var col= document.getElementById('timer').style.backgroundColor="#FF0000";
	 }
	 //
	 
}

function createFaction() {
    var timer = 0;
    
    while(true){
    	
    	 timer++;
    }
   
}

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
		url : getCont() + "management/incidence/" + "cancelIncidence",
		timeout : 60000,
		data : {
			idIncidence : index
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
		swal("Correcto!", "El ticket ha sido anulado exitosamente.",
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

function changeStatusRFC(releaseId, rfcNumRequest) {
	$formChangeStatus[0].reset();
	$formChangeStatus.validate().resetForm();
	$formChangeStatus.find('#idRFC').val(releaseId);
	$formChangeStatus.find('#rfcNumRequest').val(rfcNumRequest);
	$formChangeStatus.find('#dateChange').val(moment().format('DD/MM/YYYY hh:mm a'))
	$formChangeStatus.find('.selectpicker').selectpicker('refresh');
	$formChangeStatus.find("#statusId_error").css("visibility", "hidden");
	$('#changeStatusModal').modal('show');
}

function saveChangeStatusModal(){

	if (!$formChangeStatus.valid())
		return false;
	blockUI();
	$.ajax({
		type : "GET",
		url : getCont() + "management/incidence/statusIncidence",
		timeout : 60000,
		data : {
			idIncidence : $formChangeStatus.find('#idRFC').val(),
			idStatus: $formChangeStatus.find('#statusId').children("option:selected").val(),
			dateChange: $formChangeStatus.find('#dateChange').val(),
			motive: $formChangeStatus.find('#motive').val()
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
		swal("Correcto!", "El ticket ha sido modificado exitosamente.",
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
	$formChangeStatus.validate().resetForm();
	$formChangeStatus.find('#userId').selectpicker('val', '');
	$('#changeStatusModal').modal('hide');
}


function validStatusRFC() {
	let valid = true;
	let statusId = $formChangeStatus.find('#statusId').children("option:selected")
	.val();
	if ($.trim(statusId) == "" || $.trim(statusId).length == 0) {
		$formChangeStatus.find("#statusId_error").css("visibility", "visible");
		return false;
	} else {
		$formChangeStatus.find("#statusId_error").css("visibility", "hidden");
		return true;
	}
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