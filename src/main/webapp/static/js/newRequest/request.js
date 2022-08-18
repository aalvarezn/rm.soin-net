/** Declaración de variables globales del contexto * */
var $dtRFCs;
var $dtUsers;
var $fmRequest = $('#formAddRequest');
var $fmR1 = $('#formAddR1');
var $formChangeUser = $('#changeUserForm');
var $trackingRequestForm = $('#trackingRequestForm');

$(function() {

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

	activeItemMenu("requestItem");
	dropDownChange();
	dropDownChangeRequest();
	$("#addRequestSection").hide();
	$fmRequest.find("#sId").selectpicker('val',"");

	initRequestTable();
	initRequestFormValidation();
	initRequestR1FormValidation();
	$('#createR1').hide();

	
});
$('input[name="daterange"]').on('apply.daterangepicker', function(ev, picker) {
	$('input[name="daterange"]').val(picker.startDate.format('DD/MM/YYYY') + ' - ' + picker.endDate.format('DD/MM/YYYY'));
	$dtRFCs.ajax.reload();
});
function closeTrackingRequestModal(){
	$trackingRequestForm[0].reset();
	$('#trackingRequestModal').modal('hide');
}

$('input[name="daterange"]').on('cancel.daterangepicker', function(ev, picker) {
	$('input[name="daterange"]').val('');
	$dtRFCs.ajax.reload();
});

$('#tableFilters #typePetitionId').change(function() {
	$dtRFCs.ajax.reload();
});

$('#tableFilters #systemId').change(function() {
	$dtRFCs.ajax.reload();
});

$('#tableFilters #statusId').change(function() {
	$dtRFCs.ajax.reload();
});



function initRequestTable() {
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
					"sAjaxSource" : getCont() + "request/list",
					"fnServerParams" : function(aoData) {
						aoData.push({"name": "dateRange", "value": $('#tableFilters input[name="daterange"]').val()},
								{"name": "typePetitionId", "value": $('#tableFilters #typePetitionId').children("option:selected").val()},
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
					}
					, {
						"mRender" : function(data, type, row, meta) {
							if(row.typePetition)
								return row.typePetition.code;
							else
								return '';
						},
					}, {
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
							var options = '<div class="iconLine">';
							if (row.status.name == 'Borrador') {
								
									options = options
									+ '<a onclick="editRequest('
									+ row.id
									+ ')" title="Editar"> <i class="material-icons gris">mode_edit</i></a>'
									+ '<a onclick="confirmDeleteRequest('
									+ row.id
									+ ')" title="Borrar"><i class="material-icons gris">delete</i></a>'
								
							}


						
							options = options
							+ '<a onclick="openRequestTrackingModal('
							+ row.id
							+ ')" title="Rastreo"><i class="material-icons gris" style="font-size: 25px;">location_on</i> </a>';

							options = options
							+ '<a href="'
							+ getCont()
							+ 'request/summaryRequest-'
							+ row.id
							+ '" title="Resumen"><i class="material-icons gris">info</i></a> </div>';
							return options;
						},
					} ],
					ordering : false,
			});
}

function changeSlide() {
	$fmRequest.validate().resetForm();
	$fmRequest[0].reset();
	resetDrops();
	let change = $("#buttonAddRequest").is(":visible");
	$("#buttonAddRequest").toggle();
	let hide = change ? '#tableSection': '#addRequestSection';
	let show = !change ? '#tableSection': '#addRequestSection';
	
	$(hide).toggle("slide");
	$(show).show('slide', {
		direction : (change? 'right': 'left' )
	}, 500);
	if(change)
		$("#addRequestSection").insertAfter("#tableSection");
	else
		$("#tableSection").insertAfter("#addRequestSection")
}


function addRequestSection() {
	changeSlide();
}


function closeRequestSection(){
	changeSlide();
}

function editRequest(element) {
	var cont = getCont();
	window.location = cont + "	request/editRequest-" + element;
}



function confirmDeleteRequest(element) {
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
			deleteRequest(element);
		}		
	});
}

function deleteRequest(element) {
	blockUI();
	var cont = getCont();
	$.ajax({
		type : "DELETE",
		url : cont + "management/request/" + "deleteRequest /" + element,
		timeout : 60000,
		data : {},
		success : function(response) {
			switch (response.status) {
			case 'success':
				swal("Correcto!", "La solicitud ha sido eliminado exitosamente.",
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

function createRequest() {
	if (!$fmRequest.valid())
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
				url : getCont() + "request/" ,
				dataType : "json",
				contentType: "application/json; charset=utf-8",
				timeout : 60000,
				data : JSON.stringify({
					codeProyect : $fmRequest.find('#sigesId').val(),
					systemId : $fmRequest.find('#sId').val(),
					typePetitionId:$fmRequest.find('#tId').val(),
				}),
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status);
					window.location = getCont()
					+ "request/editRequest-"
					+ response.data;
					
					//$dtImpact.ajax.reload();
					//$mdImpact.modal('hide');
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
function initRequestFormValidation() {
	$fmRequest.validate({
		rules : {
			
			'sId':{
				required : true
			},
			'sigesId':{
				required :true
			},
			'tId':{
				required:true
			}
				
			
		},
		messages : {
			
			'sId' : {
				required : "Ingrese un valor"
			},
			'sigesId' : {
				required : "Ingrese un valor"
			},

			'tId' : {
				required : "Ingrese un valor"
			}
		},
		highlight,
		unhighlight,
		errorPlacement
	});
}

function initRequestR1FormValidation() {
	$fmR1.validate({
		rules : {
			
			'sId2':{
				required : true
			},
			'tId2':{
				required :true
			},
			'sCode':{
				required:true,
				minlength : 5,
				maxlength : 50,
				
			}
				
			
		},
		messages : {
			
			'sId2' : {
				required : "Ingrese un valor"
			},
			'sCode' : {
				required : "Ingrese un valor",
				minlength : "Debe contener mas de {0} caracteres",
				maxlength : "No puede poseer mas de {0} caracteres"
			},

			'tId2' : {
				required : "Ingrese un valor"
			}
		},
		highlight,
		unhighlight,
		errorPlacement
	});
}


function createRequestR1() {
	if (!$fmR1.valid())
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
				url : getCont() + "request/" ,
				dataType : "json",
				contentType: "application/json; charset=utf-8",
				timeout : 60000,
				data : JSON.stringify({
					codeOpportunity : $fmR1.find('#sCode').val().toUpperCase(),
					systemId : $fmR1.find('#sId2').val(),
					typePetitionId:$fmR1.find('#tId2').val(),
				}),
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status);
					window.location = getCont()
					+ "request/editRequest-"
					+ response.data;
					
					//$dtImpact.ajax.reload();
					//$mdImpact.modal('hide');
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
function dropDownChangeRequest(){
	$('#tId').on('change', function(){
		var typeRequest =$('#tId option:selected').text();
		console.log(typeRequest);
		if(typeRequest==='RM-P1-R1'){
			$('#formAddRequest').attr( "hidden",true);
			$('#formAddR1').attr( "hidden",false);
			$('#sigesId').prop('disabled', false);
			$('#createRequest').hide();
			$('#createR1').show();
			$('#sId').selectpicker('refresh');
			$('#sId2').selectpicker('val',  "");
			$('#tId2').selectpicker('val',  $('#tId').val());
			
		}else{
			resetDrop();
			console.log("awdawd");
			$('#formAddR1').attr( "hidden",true);
			$('#formAddRequest').attr( "hidden",false);

		}
	});
	
	$('#tId2').on('change', function(){
		var typeRequest =$('#tId2 option:selected').text();
		console.log(typeRequest);
		if(typeRequest==='RM-P1-R1'){
		
		}else{
			resetDrop();
			console.log("awdawd");
			$('#formAddR1').attr( "hidden",true);
			$('#formAddRequest').attr( "hidden",false);
			$('#sId').selectpicker('val',  "");
			$('#tId').selectpicker('val',  $('#tId2').val());
			$('#createRequest').show();
			$('#createR1').hide();
		}
	});
}
function dropDownChange(){

	$('#sId').on('change', function(){
		var sId =$fmRequest.find('#sId').val();
		if(sId!=""){
		$.ajax({
			type: 'GET',
			url: getCont() + "rfc/changeProject/"+sId,
			success: function(result) {
				if(result.length!=0){
					var s = '';
					s+='<option value="">-- Seleccione una opci&oacute;n --</option>';
					for(var i = 0; i < result.length; i++) {
						s += '<option value="' + result[i].codeSiges + '">' + result[i].codeSiges + '</option>';
					}
					$('#sigesId').html(s);
					$('#sigesId').prop('disabled', false);
					$('#createRequest').prop('disabled', false);
					$('#sigesId').selectpicker('refresh');
				}else{
					resetDrop();
				}
				
				
			}
		});
		}else{
			resetDrop();
		}
		
	});
}
function openRequestTrackingModal(idRequest) {

	var dtRFC = $('#dtRFCs').dataTable();
	var idRow = dtRFC.fnFindCellRowIndexes(idRequest, 0); // idRow
	var rowData = $dtRFCs.row(idRow[0]).data();
	
	$trackingRequestForm.find('#idRequest').val(rowData.id);
	$trackingRequestForm.find('#requestNumber').text(rowData.numRequest);
	
	loadTrackingRequest(rowData);
	$('#trackingRequestModal').modal('show');
}

function loadTrackingRequest(rowData){
	$trackingRequestForm.find('tbody tr').remove();
	if(rowData.tracking.length == 0){
		$trackingRequestForm.find('tbody').append('<tr><td colspan="4" style="text-align: center;">No hay movimientos</td></tr>');
	}
	$.each(rowData.tracking, function(i, value) {
		$trackingRequestForm.find('tbody').append('<tr style="padding: 10px 0px 0px 0px;" > <td><span style="background-color: '+getColorNode(value.status)+';" class="round-step"></span></td>	<td>'+value.status+'</td>	<td>'+moment(value.trackingDate).format('DD/MM/YYYY h:mm:ss a')+'</td>	<td>'+value.operator+'</td> <td>'+(value.motive && value.motive != null && value.motive != 'null' ? value.motive:'' )+'</td>	</tr>');
	});
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

function resetDrops(){
	$fmRequest.find('#sId').selectpicker('val',  "");
	resetDrop();
}	
function resetDrop(){
	var s = '';
	s+='<option value="">-- Seleccione una opci&oacute;n --</option>';
	$('#sigesId').html(s);
	$('#sigesId').prop('disabled',true);
	$('#createRequest').prop('disabled',true);
	$('#sigesId').selectpicker('refresh');
	
}


