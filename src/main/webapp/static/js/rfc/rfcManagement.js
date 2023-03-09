var $dtRFCs;

var $formChangeStatus = $('#changeStatusForm');
var $formChangeUser = $('#changeUserForm');
var $trackingRFCForm = $('#trackingRFCForm');
var switchStatus=false;
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
	// initImpactFormValidation
	activeItemMenu("managerRFCItem");
	dropDownChange();
	showSendEmail();
	// $("#addRFCSection").hide();
	// $fmRFC.find("#sId").selectpicker('val',"");
	 $('.tagInitMail').tagsInput({
		 placeholder: 'Ingrese los correos'
	 });
	initRFCTable();
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

$('#tableFilters #priorityId').change(function() {
	$dtRFCs.ajax.reload();
});

$('#tableFilters #systemId').change(function() {
	$dtRFCs.ajax.reload();
});

$('#tableFilters #statusId').change(function() {
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
					"sAjaxSource" : getCont() + "management/rfc/list",
					"fnServerParams" : function(aoData) {
						aoData.push({"name": "dateRange", "value": $('#tableFilters input[name="daterange"]').val()},
								{"name": "statusId", "value": $('#tableFilters #statusId').children("option:selected").val()},
								{"name": "systemId", "value": $('#tableFilters #systemId').children("option:selected").val()}
						);
					},
					"aoColumns" : [
						{
							"mDataProp" : "id",
						},
						{
						
						"mDataProp" : "numRequest"
					},
					{
						
						"mDataProp" : "systemInfo.name"
					},
					{
						"mRender" : function(data, type, row, meta) {
							return row.user.fullName;
						},
					}, {
						"mRender" : function(data, type, row, meta) {
							return moment(row.requestDate).format('DD/MM/YYYY h:mm:ss a');
						},
					}, {
						"mRender" : function(data, type, row, meta) {
							if(row.status)
								return row.status.name;
							else
								return '';
						},
					}, {
						"mRender" : function(data, type, row, meta) {
							var options = '<div class="iconLineC">';
							
							if(row.status.name != 'Anulado') {
								options = options
								+ '<a onclick="confirmDeleteRFC('+row.id+')" title="Anular"><i class="material-icons gris" style="font-size: 25px;">highlight_off</i></a>';
							}
						
							options = options
							+ '<a onclick="changeStatusRFC('+row.id+')" title="Cambiar Estado"><i class="material-icons gris" style="font-size: 25px;">offline_pin</i></a>';
							options = options
							+ '<a onclick="openRFCTrackingModal('
							+ row.id
							+ ')" title="Rastreo"><i class="material-icons gris" style="font-size: 25px;">location_on</i> </a>';

							options = options
							+ '<a href="'
							+ getCont()
							+ 'rfc/summaryRFC-'
							+ row.id
							+ '" title="Resumen"><i class="material-icons gris">info</i></a> </div>';
							return options;
						},
					} ],
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
	case 'Error':
		return 'rgb(255,0,0)';
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
	// $formChangeStatus.validate().resetForm();
	console.log(rowData);
	console.log(rowData.siges);
	console.log(rowData.siges.emailTemplate);
	//$formChangeStatus.find('#senders').val(rowData.siges.emailTemplate.cc);
	$('.tagInitMail#senders').importTags(rowData.siges.emailTemplate.cc);
	
	$formChangeStatus.find('#idRFC').val(idRFC);
	$formChangeStatus.find('#rfcNumRequest').val(rowData.numRequest);
	$formChangeStatus.find('#dateChange').val(moment().format('DD/MM/YYYY hh:mm a'))
	$formChangeStatus.find('.selectpicker').selectpicker('refresh');
	$formChangeStatus.find(".fieldError").css("visibility", "hidden");
	$formChangeStatus.find('.fieldError').removeClass('activeError');
	$formChangeStatus.find('.form-line').removeClass('error');
	$formChangeStatus.find('.form-line').removeClass('focused');
	$('#divError').attr( "hidden",true);
	$('#divEmail').attr( "hidden",true);
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
	console.log(validStatusRFC());
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
			senders:$formChangeStatus.find('#senders').val(),
			
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
// $formChangeStatus.validate().resetForm();
	$formChangeStatus.find('#userId').selectpicker('val', '');
	$('#changeStatusModal').modal('hide');
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