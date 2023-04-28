var $dtRFCs;

var $formChangeStatus = $('#changeStatusForm');
var $formChangeUser = $('#changeUserForm');
var $trackingRFCForm = $('#trackingRFCForm');

$(document).ready(function() {
	$('input[name="daterange"]').daterangepicker({
		"autoUpdateInput": false,
		"opens": 'left',
		"orientation": 'right',
		"locale": {
			"format": "DD/MM/YYYY",
			"separator": " - ",
			"applyLabel": "Aplicar",
			"cancelLabel": "Cancelar",
			"fromLabel": "Desde",
			"toLabel": "Hasta",
			"customRangeLabel": "Custom",
			"daysOfWeek": [
				"Do",
				"Lu",
				"Ma",
				"Mi",
				"Ju",
				"Vi",
				"Sa"
				],
				"monthNames": [
					"Enero",
					"Febrero",
					"Marzo",
					"Abril",
					"Mayo",
					"Junio",
					"Julio",
					"Agosto",
					"Septiembre",
					"Octubre",
					"Noviembre",
					"Deciembre"
					],
					"firstDay": 1
		}
	});
	// Datetimepicker plugin
	$('.datetimepicker').datetimepicker({
		locale: 'es',
		format: 'DD/MM/YYYY hh:mm a',
		maxDate : new Date()
	});
	$('input[name="daterange"]').attr('value', moment().subtract(7, 'day').format("DD/MM/YYYY")+' - '+ moment().format('DD/MM/YYYY'));
	initImpactFormValidation
	activeItemMenu("errorItem",true);
	// dropDownChange();
	// $("#addRFCSection").hide();
	// $fmRFC.find("#sId").selectpicker('val',"");

	initRFCTable();
	dropDownChange();
	dropDownChangeSystem();
	// initRFCFormValidation();
});

$('input[name="daterange"]').on('apply.daterangepicker', function(ev, picker) {
	$('input[name="daterange"]').val(picker.startDate.format('DD/MM/YYYY') + ' - ' + picker.endDate.format('DD/MM/YYYY'));
	$dtRFCs.ajax.reload();
});

$('input[name="daterange"]').on('cancel.daterangepicker', function(ev, picker) {
	$('input[name="daterange"]').val('');
	$dtRFCs.ajax.reload();
});

$('#tableFilters #sigesId').change(function() {
	$dtRFCs.ajax.reload();
});

$('#tableFilters #projectId').change(function() {
	$dtRFCs.ajax.reload();
});
$('#tableFilters #systemId').change(function() {
	$dtRFCs.ajax.reload();
});

$('#tableFilters #errorId').change(function() {
	$dtRFCs.ajax.reload();
});
function refreshTable(){
	$dtRFCs.ajax.reload();
}
function initRFCTable() {
	$dtRFCs = $('#dtRFCs').DataTable(
			{
				
				'columnDefs' : [ {
					'visible' : false,
					'targets' : [ 0]
				} ],
				lengthMenu : [ [ 10, 25, 50, -1 ],
					[ '10', '25', '50', 'Mostrar todo' ] ],
					"iDisplayLength" : 10,
					"language" : optionLanguaje,
					"iDisplayStart" : 0,
					"processing" : true,
					"serverSide" : true,
					"sAjaxSource" : getCont() + "management/error/rfc/list",
					"fnServerParams" : function(aoData) {
						aoData.push({"name": "dateRange", "value": $('#tableFilters input[name="daterange"]').val()},
								{"name": "sigesId", "value": $('#tableFilters #sigesId').children("option:selected").val()},
								{"name": "errorId", "value": $('#tableFilters #errorId').children("option:selected").val()},
								{"name": "systemId", "value": $('#tableFilters #systemId').children("option:selected").val()},
								{"name": "projectId", "value": $('#tableFilters #projectId').children("option:selected").val()}
						);
					},
					"aoColumns" : [
						{
							"mDataProp" : "id",
						},
						
						{
						
						"mDataProp" : "rfc.numRequest"
					},
					
					{
						
						"mDataProp" : "system.name"
					},
					{
						
						"mDataProp" : "siges.codeSiges"
					},
					{
						
						"mRender" : function(data, type, row, meta) {
							if(row.error!=null){
								return row.error.name;
							}else{
								return "Sin error seleccionado";
							}
							
						},
						
						
					},
					{
						
						"mDataProp" : "observations"
					}, 
					{
						"mRender" : function(data, type, row, meta) {
							return moment(row.errorDate).format('DD/MM/YYYY h:mm:ss a');
						},
					},
					{
						"mRender" : function(data, type, row, meta) {
							return row.rfc.user.fullName;
						},
					}
					 ],
					ordering : false,
			});
}

function openRFCTrackingModal(idRFC) {
	var dtRFC = $('#dtRFCs').dataTable();
	var idRow = dtRFC.fnFindCellRowIndexes(idRFC, 0); // idRow
	var rowData = $dtRFCs.row(idRow[0]).data();
	$trackingRFCForm.find('#idRFC').val(rowData.id);
	$trackingRFCForm.find('#rfcNumber').text(rowData.numRequest);
	
	loadTrackingRFC(rowData);
	$('#trackingRFCModal').modal('show');
}

function loadTrackingRFC(rowData){
	$trackingRFCForm.find('tbody tr').remove();
	if(rowData.tracking.length == 0){
		$trackingRFCForm.find('tbody').append('<tr><td colspan="4" style="text-align: center;">No hay movimientos</td></tr>');
	}
	$.each(rowData.tracking, function(i, value) {
		$trackingRFCForm.find('tbody').append('<tr style="padding: 10px 0px 0px 0px;" > <td><span style="background-color: '+getColorNode(value.status)+';" class="round-step"></span></td>	<td>'+value.status+'</td>	<td>'+moment(value.trackingDate).format('DD/MM/YYYY h:mm:ss a')+'</td>	<td>'+value.operator+'</td> <td>'+(value.motive && value.motive != null && value.motive != 'null' ? value.motive:'' )+'</td>	</tr>');
	});
}

function closeTrackingRFCModal(){
	$trackingRFCForm[0].reset();
	$('#trackingRFCModal').modal('hide');
}
function getColorNode(status){
	switch (status) {
	case 'Produccion':
		return 'rgb(0, 150, 136)';
		break;
	case 'Certificacion':
		return 'rgb(255, 152, 0)';
		break;
	case 'Solicitado':
		return 'rgb(76, 175, 80)';
		break;
	case 'Borrador':
		return 'rgb(31, 145, 243)';
		break;
	case 'Anulado':
		return 'rgb(233, 30, 99)';
		break;
	default:
		return 'rgb(0, 181, 212)';
	break;
	}
}


function confirmDeleteRFC(element) {
	// $('#deleteReleaseModal').modal('show');
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
			deleteRFC(element);
		}		
	});
}

function deleteRFC(element) {
	blockUI();
	var cont = getCont();
	$.ajax({
		type : "DELETE",
		url : cont + "management/rfc/" + "deleteRFC /" + element,
		timeout : 60000,
		data : {},
		success : function(response) {
			switch (response.status) {
			case 'success':
				swal("Correcto!", "El RFC ha sido eliminado exitosamente.",
						"success", 2000)
						$dtRFCs.ajax.reload();
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
		},
		error : function(x, t, m) {
			notifyAjaxError(x, t, m);
		}
	});
}

function changeStatusRFC(idRFC) {
	var dtRFC = $('#dtRFCs').dataTable();
	var idRow = dtRFC.fnFindCellRowIndexes(idRFC, 0); // idRow
	var rowData = $dtRFCs.row(idRow[0]).data();
	$formChangeStatus[0].reset();
	$formChangeStatus.validate().resetForm();
	$formChangeStatus.find('#idRFC').val(idRFC);
	$formChangeStatus.find('#rfcNumRequest').val(rowData.numRequest);
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
		url : getCont() + "management/rfc/statusRFC",
		timeout : 60000,
		data : {
			idRFC : $formChangeStatus.find('#idRFC').val(),
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
		swal("Correcto!", "El RFC ha sido modificado exitosamente.",
				"success", 2000);
		$dtRFCs.ajax.reload();
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

const b64toBlob = (b64Data, contentType='', sliceSize=512) => {
	  const byteCharacters = atob(b64Data);
	  const byteArrays = [];

	  for (let offset = 0; offset < byteCharacters.length; offset += sliceSize) {
	    const slice = byteCharacters.slice(offset, offset + sliceSize);

	    const byteNumbers = new Array(slice.length);
	    for (let i = 0; i < slice.length; i++) {
	      byteNumbers[i] = slice.charCodeAt(i);
	    }

	    const byteArray = new Uint8Array(byteNumbers);
	    byteArrays.push(byteArray);
	  }

	  const blob = new Blob(byteArrays, {type: contentType});
	  return blob;
	}
	 
function downLoadReport1(){
	const params = new URLSearchParams();
	params.set('dateRange', $('#tableFilters input[name="daterange"]').val());
	params.set('sigesId', $('#tableFilters #sigesId').children("option:selected").val());
	params.set('errorId', $('#tableFilters #errorId').children("option:selected").val());
	params.set('systemId', $('#tableFilters #systemId').children("option:selected").val());
	params.set('projectId', $('#tableFilters #projectId').children("option:selected").val());
	const target = getCont() + "management/error/downloaderrorrfc?" + params.toString();
	var xhr = new XMLHttpRequest();
	xhr.open('GET', target, true);
	xhr.responseType = 'json';
	xhr.onload = function(e) {
		console.log(e);
		if (this.status == 200) {
			console.log(xhr.getAllResponseHeaders());
			console.log(this.response);
			var b64Data = this.response;
			// var contentType =
			// this.response.getResponseHeader("Content-Type"); //Obtenemos el
			// tipo de los datos
			// console.log(response.getResponseHeader("Content-Disposition"));
			// var filename =
			// this.response.getResponseHeader("Content-Disposition");//Obtenemos
			// el nombre del fichero a desgargar
			// console.log( this.response);
			// filename = filename.substring(filename.lastIndexOf("=") + 1) ||
			// "download";
			 // var request = new XMLHttpRequest ();
			 // request.open ('GET', document.location, false);
			// console.log(request.getAllResponseHeaders().toLowerCase());
			var blob = new Blob([this.response], { type: 'application/pdf'});
			var link = document.createElement('a');
			link.href = window.URL.createObjectURL(blob);
			link.download = "report.pdf";
			link.click();       
		}
	};
	xhr.send();

}

function dropDownChange(){

	$('#projectId').on('change', function(){
		var projectId =$('#tableFilters #projectId').val();
		console.log(projectId);
		if(projectId!=""){
		$.ajax({
			type: 'GET',
			url: getCont() + "management/error/getSystem/"+projectId,
			success: function(result) {
				console.log(result);
				if(result.length!=0){
					var s = '';
					s+='<option value="">-- Todos --</option>';
					for(var i = 0; i < result.length; i++) {
						s += '<option value="' + result[i].id + '">' + result[i].name + '</option>';
					}
					$('#systemId').html(s);
					$('#systemId').prop('disabled', false);
					$('#systemId').selectpicker('refresh');
				}else{
					resetDropPriorityMain();
				}
				
				
			}
		});
		
		}else{
			resetDropPriorityMain();
			resetDropTypeMain();
			resetDropStatusMain();
		}
		
	});
}
function dropDownChange(){

	$('#projectId').on('change', function(){
		var projectId =$('#tableFilters #projectId').val();
		console.log(projectId);
		if(projectId!=""){
		$.ajax({
			type: 'GET',
			url: getCont() + "management/error/getSystem/"+projectId,
			success: function(result) {
				console.log(result);
				if(result.length!=0){
					var s = '';
					s+='<option value="">-- Todos --</option>';
					for(var i = 0; i < result.length; i++) {
						s += '<option value="' + result[i].id + '">' + result[i].name + '</option>';
					}
					$('#systemId').html(s);
					$('#systemId').prop('disabled', false);
					$('#systemId').selectpicker('refresh');
				}else{
					resetDropPriorityMain();
				}
				
				
			}
		});
		
		}else{
			
		}
		
	});
}

function dropDownChangeSystem(){

	$('#systemId').on('change', function(){
		var systemId =$('#tableFilters #systemId').val();
		console.log(systemId);
		if(systemId!=""){
		$.ajax({
			type: 'GET',
			url: getCont() + "management/error/getSiges/"+systemId,
			success: function(result) {
				console.log(result);
				if(result.length!=0){
					var s = '';
					s+='<option value="">-- Todos --</option>';
					for(var i = 0; i < result.length; i++) {
						s += '<option value="' + result[i].id + '">' + result[i].codeSiges + '</option>';
					}
					$('#sigesId').html(s);
					$('#sigesId').prop('disabled', false);
					$('#sigesId').selectpicker('refresh');
				}else{
					resetDropPrioritySecond();
				}
				
				
			}
		});
		
		}else{
			resetDropPrioritySecond();
		}
		
	});
}

function resetDropPrioritySecond(){
	var s = '';
	s+='<option value="0">-- Todos --</option>';
	$('#sigesId').html(s);
	$('#sigesId').prop('disabled',true);
	$('#sigesId').selectpicker('refresh');
	
}
function resetDropPriorityMain(){
	
	var s = '';
	s+='<option value="0">-- Todos --</option>';
	$('#systemId').html(s);
	$('#systemId').prop('disabled',true);
	$('#systemId').selectpicker('refresh');
	
	var x = '';
	x+='<option value="0">-- Todos --</option>';
	$('#sigesId').html(x);
	$('#sigesId').prop('disabled',true);
	$('#sigesId').selectpicker('refresh');
}

function downLoadReport(){
	console.log($('#tableFilters input[name="daterange"]').val().replaceAll("/","^"));
	$.ajax({
		type : "GET",
		cache : false,
		contentType: "application/json; charset=utf-8",
		async : true,
		url : getCont() + "management/error/downloaderrorrfc",
		timeout : 60000,
		data : {
			dateRange :$('#tableFilters input[name="daterange"]').val(),
			sigesId: $('#tableFilters #sigesId').children("option:selected").val(),
			errorId: $('#tableFilters #errorId').children("option:selected").val(),
			systemId: $('#tableFilters #systemId').children("option:selected").val(),
			projectId: $('#tableFilters #projectId').children("option:selected").val()
		}, beforeSend: function() {
	    	showSpinner();
	      },
		success : function(response) {
			console.log(response);
			// console.log(atob(response.obj.file));
			
			var blob = new Blob([b64toBlob(response.obj.file,response.obj.ContentType)], {type: response.obj.ContentType});
			var link = document.createElement('a');
			link.href = window.URL.createObjectURL(blob);
			link.download = response.obj.name;
			link.click();   
		},
		   complete: function() {
			      hideSpinner();
			    },
		error : function(x, t, m) {
			notifyAjaxError(x, t, m);
		}
	});
}
function showSpinner(){
	var miElemento = document.getElementById("loading"); 
	miElemento.style.display = "flex";
}

function hideSpinner(){
	var miElemento = document.getElementById("loading"); 
	miElemento.style.display = "none";
}