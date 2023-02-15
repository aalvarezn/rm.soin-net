var $formChangeStatus = $('#changeStatusFormIns');
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
	
	
	//setInterval(waitAndshow, 1000);
	//createFaction();
});
/*
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
		 var col= document.getElementById('timer').style.color="#DD1C5E";
	 }
	 //
	 
}
*/
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
		swal("Correcto!", "El ticket ha sido tramitado exitosamente.",
				"success", 2000);
		window.location = getCont() + "	incidenceManagement/";
		
		//closeChangeStatusModal();
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


function requestIncidence(){
	console.log($formChangeStatus.valid());
	console.log($formChangeStatus);
	console.log($('#rfcId').val());
	console.log($formChangeStatus.find('#nodeId').children("option:selected").val());
	console.log($formChangeStatus.find('#motive').val());
	if (!$formChangeStatus.valid())
		return false;
	blockUI();
	$.ajax({
		type : "POST",
		url : getCont() + "incidenceManagement/changeStatus",
		timeout : 60000,
		data : {
			idIncidence : $('#rfcId').val(),
			idNode: $formChangeStatus.find('#nodeId').children("option:selected").val(),
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

function initImpactFormValidation() {
	$formChangeStatus.validate({
		
		rules : {
			'nodeId' : {
				required : true,
				
			},
			'motive' : {
				required : true,
				minlength : 1,
				maxlength : 255,
			}
		},
		messages : {
			'nodeId' : {
				required :  "Seleccione una opci&oacute;n",
			},
			'motive' : {
				required : "Ingrese un valor",
				minlength : "Ingrese un valor",
				maxlength : "No puede poseer mas de {0} caracteres"
			}
		},
		highlight,
		unhighlight,
		errorPlacement
	});
}

function changeStatusRelease(releaseId) {
	var dtReleases = $('#dtReleases').dataTable(); // tabla
	var idRow = dtReleases.fnFindCellRowIndexes(releaseId, 0); // idRow
	var rowData = releaseTable.row(idRow).data();
	formChangeStatus[0].reset();
	formChangeStatus.find("#nodeId").find('option').remove();
	
	formChangeStatus.find('#nodeId').append('<option value="">-- Seleccione una opci\u00F3n --</option>' );

	let userId = $('#userInfo_Id').val();
	let allowActor = null;
	$.each(rowData.node.edges, function(i, value) {
		if(value.nodeTo.status && value.nodeTo.status !== null){
			allowActor = rowData.node.actors.find(element => element.id == userId);
			if( typeof allowActor !== 'undefined'){
				formChangeStatus.find('#nodeId').append('<option data-motive="'+value.nodeTo.status.motive+'"  value="'+value.nodeTo.id+'">'+value.nodeTo.label+'</option>' );

			}
		}
	});

	formChangeStatus.find('.selectpicker').selectpicker('refresh');
	formChangeStatus.find('#idRelease').val(rowData.id);
	formChangeStatus.find('#releaseNumber').val(rowData.releaseNumber);
	formChangeStatus.find("#nodeId_error").css("visibility", "hidden");
	$('#changeStatusModal').modal('show');
}