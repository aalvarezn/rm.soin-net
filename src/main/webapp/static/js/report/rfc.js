/** Declaración de variables globales del contexto * */
var $dtRFCs;

var $fmRFC = $('#formAddRFC');
var $formChangeUser = $('#changeUserForm');
var $trackingRFCForm = $('#trackingRFCForm');

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
	//$('input[name="daterange"]').attr('value', moment().subtract(7, 'day').format("DD/MM/YYYY")+' - '+ moment().format('DD/MM/YYYY'));

	activeItemMenu("RFCItem");
	dropDownChange();
	$("#addRFCSection").hide();
	$fmRFC.find("#sId").selectpicker('val',"");

	initRFCTable();
	initRFCFormValidation();
	console.log("hola prueba");
});
$('input[name="daterange"]').on('apply.daterangepicker', function(ev, picker) {
	$('input[name="daterange"]').val(picker.startDate.format('DD/MM/YYYY') + ' - ' + picker.endDate.format('DD/MM/YYYY'));
	$dtRFCs.ajax.reload();
});
function closeTrackingRFCModal(){
	$trackingRFCForm[0].reset();
	$('#trackingRFCModal').modal('hide');
}

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
	console.log("pruebaPr");
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
					"sAjaxSource" : getCont() + "report/listRFC",
					"fnServerParams" : function(aoData) {
						aoData.push({"name": "dateRange", "value": $('#tableFilters input[name="daterange"]').val()},
								{"name": "priorityId", "value": $('#tableFilters #priorityId').children("option:selected").val()},
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
						
						"mDataProp" : "system.name"
					}
					, {
						"mRender" : function(data, type, row, meta) {
							if(row.reasonChange)
								return row.reasonChange;
							else
								return 'Sin razon de cambio seleccionada';
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
									+ '<a onclick="editRFC('
									+ row.id
									+ ')" title="Editar"> <i class="material-icons gris">mode_edit</i></a>'
									+ '<a onclick="confirmDeleteRFC('
									+ row.id
									+ ')" title="Borrar"><i class="material-icons gris">delete</i></a>'
								
							}
							if($('#isDeveloper').val()){
								options = options
								+ '<a onclick="copyRFC('
								+ row.index
								+ ')" title="Copiar"><i class="material-icons gris">file_copy</i> </a>';
							}

							

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

function changeSlide() {
	$fmRFC.validate().resetForm();
	$fmRFC[0].reset();
	resetDrops();
	let change = $("#buttonAddRFC").is(":visible");
	$("#buttonAddRFC").toggle();
	let hide = change ? '#tableSection': '#addRFCSection';
	let show = !change ? '#tableSection': '#addRFCSection';
	
	$(hide).toggle("slide");
	$(show).show('slide', {
		direction : (change? 'right': 'left' )
	}, 500);
	if(change)
		$("#addRFCSection").insertAfter("#tableSection");
	else
		$("#tableSection").insertAfter("#addRFCSection")
}


function addRFCSection() {
	changeSlide();
}


function closeRFCSection(){
	changeSlide();
}

function editRFC(element) {
	var cont = getCont();
	window.location = cont + "	rfc/editRFC-" + element;
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
		url : cont + "rfc/" + "deleteRFC /" + element,
		timeout : 60000,
		data : {},
		success : function(response) {
			switch (response.status) {
			case 'success':
				swal("Correcto!", "El RFC ha sido eliminado exitosamente.",
						"success", 2000)
						$dtRFCs.DataTable().ajax.reload();
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

function createRFC() {
	if (!$fmRFC.valid())
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
				url : getCont() + "rfc/" ,
				dataType : "json",
				contentType: "application/json; charset=utf-8",
				timeout : 60000,
				data : JSON.stringify({
					codeProyect : $fmRFC.find('#sigesId').val(),
					systemId : $fmRFC.find('#sId').val()
				}),
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status);
					window.location = getCont()
					+ "rfc/editRFC-"
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
function initRFCFormValidation() {
	$fmRFC.validate({
		rules : {
			
			'sId':{
				required : true
			},
			'sigesId':{
				required :true
			}
			
		},
		messages : {
			
			'sId' : {
				required : "Ingrese un valor"
			},
			'sigesId' : {
				required : "Ingrese un valor"
			},
		},
		highlight,
		unhighlight,
		errorPlacement
	});
}
function dropDownChange(){

	$('#sId').on('change', function(){
		var sId =$fmRFC.find('#sId').val();
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
					$('#createRFC').prop('disabled', false);
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

function openRFCTrackingModal(idRFC) {

	var dtRFC = $('#dtRFCs').dataTable();
	var idRow = dtRFC.fnFindCellRowIndexes(idRFC, 0); // idRow
	var rowData = $dtRFCs.row(idRow[0]).data();
	
	$trackingRFCForm.find('#idRFC').val(rowData.id);
	$trackingRFCForm.find('#rfcNumber').text(rowData.numRequest);
	
	$.ajax({
		type : "GET",
		url : getCont() + "rfc/trackingRFC/"+ rowData.id ,
		timeout : 600000,
		data : {},
		success : function(response) {
			tracking=response.obj;
			loadTrackingRFC(tracking);
			$('#trackingRFCModal').modal('show');
		},
		error : function(x, t, m) {
			notifyAjaxError(x, t, m);
		}
	});
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
	$fmRFC.find('#sId').selectpicker('val',  "");
	resetDrop();
}	
function resetDrop(){
	var s = '';
	s+='<option value="">-- Seleccione una opci&oacute;n --</option>';
	$('#sigesId').html(s);
	$('#sigesId').prop('disabled',true);
	$('#createRFC').prop('disabled',true);
	$('#sigesId').selectpicker('refresh');
	
}

