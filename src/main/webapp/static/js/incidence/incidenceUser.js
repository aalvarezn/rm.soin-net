/** Declaración de variables globales del contexto * */
var $dtRFCs;

var $fmIncidence = $('#fmIncidence');
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
	// $('input[name="daterange"]').attr('value', moment().subtract(7,
	// 'day').format("DD/MM/YYYY")+' - '+ moment().format('DD/MM/YYYY'));

	activeItemMenu("incidenceItem");
	$("#addIncidenceSection").hide();
	
	initIncidenceTable();
	initIncidenceFormValidation();
	dropDownChange();
	resetDrops();
	dropDownChangeMain();
	resetDropsMain();
	
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

$('#tableFilters #typeId').change(function() {
	$dtRFCs.ajax.reload();
});

$('#tableFilters #statusId').change(function() {
	$dtRFCs.ajax.reload();
});
$('#tableFilters #systemId').change(function() {
	$dtRFCs.ajax.reload();
});

function initIncidenceTable() {
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
					"sAjaxSource" : getCont() + "incidence/list",
					"fnServerParams" : function(aoData) {
						aoData.push({"name": "dateRange", "value": $('#tableFilters input[name="daterange"]').val()},
								{"name": "systemId", "value": $('#tableFilters #systemId').children("option:selected").val()},
								{"name": "priorityId", "value": $('#tableFilters #priorityId').children("option:selected").val()},
								{"name": "statusId", "value": $('#tableFilters #statusId').children("option:selected").val()},
								{"name": "typeId", "value": $('#tableFilters #typeId').children("option:selected").val()}
						);
					},
					"aoColumns" : [
						{
							"mDataProp" : "id",
						},
						{
						
						"mDataProp" : "numTicket"
					},
					{
						
						"mDataProp" : "title"
					}
					,
					{
						
						"mDataProp" : "detail"
					}
					, 
					{
						"mRender" : function(data, type, row, meta) {
							if(row.user)
								return row.user.fullName;
							else
								return 'Sin asignar';
						},
					}, 
					{
						"mRender" : function(data, type, row, meta) {
							return row.createFor;
						},
					}, {
						"mRender" : function(data, type, row, meta) {
							return moment(row.updateDate).format('DD/MM/YYYY h:mm:ss a');
						},
					}, {
						"mRender" : function(data, type, row, meta) {
							if(row.priority)
								return row.priority.priority.name;
							else
								return 'Sin prioridad asignada';
						},
					}, 
					 {
						"mRender" : function(data, type, row, meta) {
							if(row.status)
								return row.status.status.name;
							else
								return '';
						},
					},
					{
						"mRender" : function(data, type, row, meta) {
							var options = '<div class="iconLine">';
							if (row.status.status.name == 'Borrador') {
								
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
							+ '<a onclick="openIncidenceTrackingModal('
							+ row.id
							+ ')" title="Rastreo"><i class="material-icons gris" style="font-size: 25px;">location_on</i> </a>';

							options = options
							+ '<a href="'
							+ getCont()
							+ 'incidence/summaryIncidence-'
							+ row.id
							+ '" title="Resumen"><i class="material-icons gris">info</i></a> </div>';
							return options;
						},
					} ],
					ordering : false,
			});
}

function changeSlide() {
	$fmIncidence.validate().resetForm();
	$fmIncidence[0].reset();
	resetDrops();
	let change = $("#buttonAddIncidence").is(":visible");
	$("#buttonAddIncidence").toggle();
	let hide = change ? '#tableSection': '#addIncidenceSection';
	let show = !change ? '#tableSection': '#addIncidenceSection';
	
	$(hide).toggle("slide");
	$(show).show('slide', {
		direction : (change? 'right': 'left' )
	}, 500);
	if(change)
		$("#addRFCSection").insertAfter("#tableSection");
	else
		$("#tableSection").insertAfter("#addRFCSection")
}


function addIncidenceSection() {
	changeSlide();
}


function closeIncidenceSection(){
	changeSlide();
}

function editRFC(element) {
	var cont = getCont();
	window.location = cont + "	incidence/editIncidence-" + element;
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

function createIncidence() {
	if (!$fmIncidence.valid())
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
				url : getCont() + "incidence/" ,
				dataType : "json",
				contentType: "application/json; charset=utf-8",
				timeout : 60000,
				data : JSON.stringify({
					typeIncidenceId : $fmIncidence.find('#tId').val(),
					priorityId : $fmIncidence.find('#pId').val(),
					systemId : $fmIncidence.find('#sId').val(),
					title : $fmIncidence.find('#title').val()
				}),
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status);
					window.location = getCont()
					+ "incidence/editIncidence-"
					+ response.data;
					
					// $dtImpact.ajax.reload();
					// $mdImpact.modal('hide');
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
function initIncidenceFormValidation() {
	$fmIncidence.validate({
		rules : {
			
			'tId':{
				required : true
			},
			'sId':{
				required : true
			},
			'pId':{
				required : true
			},
			'title':{
				required :true
			}
			
		},
		messages : {
			
			'tId' : {
				required : "Ingrese un valor"
			},
			'sId' : {
				required : "Ingrese un valor"
			},
			'pId' : {
				required : "Ingrese un valor"
			},
			'title' : {
				required : "Ingrese un valor",
				maxlength : "No puede poseer mas de {0} caracteres"
				
			},
		},
		highlight,
		unhighlight,
		errorPlacement
	});
}

function openIncidenceTrackingModal(idIncidence) {

	var dtRFC = $('#dtRFCs').dataTable();
	var idRow = dtRFC.fnFindCellRowIndexes(idIncidence, 0); // idRow
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
function dropDownChangeMain(){
	console.log("AWDAW");
	$('#systemId').on('change', function(){
		var systemId =$('#tableFilters #systemId').val();
		console.log(systemId);
		if(systemId!=""){
		$.ajax({
			type: 'GET',
			url: getCont() + "systemPriority/getPriority/"+systemId,
			success: function(result) {
				if(result.length!=0){
					var s = '';
					s+='<option value="">-- Todos --</option>';
					for(var i = 0; i < result.length; i++) {
						s += '<option value="' + result[i].id + '">' + result[i].priority.name + '</option>';
					}
					$('#priorityId').html(s);
					$('#priorityId').prop('disabled', false);
					$('#createRFC').prop('disabled', false);
					$('#priorityId').selectpicker('refresh');
				}else{
					resetDropPriorityMain();
				}
				
				
			}
		});
		
		$.ajax({
			type: 'GET',
			url: getCont() + "systemTypeIn/getypeIncidence/"+systemId,
			success: function(result) {
				if(result.length!=0){
					var s = '';
					s+='<option value="">-- Todos --</option>';
					for(var i = 0; i < result.length; i++) {
						s += '<option value="' + result[i].id + '">' + result[i].typeIncidence.code + '</option>';
					}
					$('#typeId').html(s);
					$('#typeId').prop('disabled', false);
					$('#createRFC').prop('disabled', false);
					$('#typeId').selectpicker('refresh');
				}else{
					resetDropTypeMain();
				}
				
				
			}
		});
		$.ajax({
			type: 'GET',
			url: getCont() + "systemStatusIn/getStatus/"+systemId,
			success: function(result) {
				if(result.length!=0){
					var s = '';
					s+='<option value="">-- Todos --</option>';
					for(var i = 0; i < result.length; i++) {
						if(status.name!='Anulado'){
							s += '<option value="' + result[i].id + '">' + result[i].status.name + '</option>';
						}
						
					}
					$('#statusId').html(s);
					$('#statusId').prop('disabled', false);
					$('#statusId').selectpicker('refresh');
				}else{
					resetDropStatusMain();
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
	console.log("AWDAW");
	$('#sId').on('change', function(){
		var sId =$fmIncidence.find('#sId').val();
		console.log("awdaw");
		if(sId!=""){
		$.ajax({
			type: 'GET',
			url: getCont() + "systemPriority/getPriority/"+sId,
			success: function(result) {
				if(result.length!=0){
					var s = '';
					s+='<option value="">-- Seleccione una opci&oacute;n --</option>';
					for(var i = 0; i < result.length; i++) {
						s += '<option value="' + result[i].id + '">' + result[i].priority.name + '</option>';
					}
					$('#pId').html(s);
					$('#pId').prop('disabled', false);
					$('#createRFC').prop('disabled', false);
					$('#pId').selectpicker('refresh');
				}else{
					resetDropPriority();
				}
				
				
			}
		});
		
		$.ajax({
			type: 'GET',
			url: getCont() + "systemTypeIn/getypeIncidence/"+sId,
			success: function(result) {
				if(result.length!=0){
					var s = '';
					s+='<option value="">-- Seleccione una opci&oacute;n --</option>';
					for(var i = 0; i < result.length; i++) {
						s += '<option value="' + result[i].id + '">' + result[i].typeIncidence.code + '</option>';
					}
					$('#tId').html(s);
					$('#tId').prop('disabled', false);
					$('#createRFC').prop('disabled', false);
					$('#tId').selectpicker('refresh');
				}else{
					resetDropType();
				}
				
				
			}
		});
		}else{
			resetDropPriority();
			resetDropType();
		}
		
	});
}
function resetDrops(){
	$fmIncidence.find('#sId').selectpicker('val',  "");
	resetDropPriority();
	resetDropType();
}	
function resetDropsMain(){
	$fmIncidence.find('#systemId').selectpicker('val',  "");
	resetDropPriority();
	resetDropType();
}	
function resetDropPriority(){
	var s = '';
	s+='<option value="">-- Seleccione una opci&oacute;n --</option>';
	$('#pId').html(s);
	$('#pId').prop('disabled',true);
	$('#createRFC').prop('disabled',true);
	$('#pId').selectpicker('refresh');
	
}
function resetDropPriorityMain(){
	var s = '';
	s+='<option value="">-- Todos --</option>';
	$('#priorityId').html(s);
	$('#priorityId').prop('disabled',true);
	$('#createRFC').prop('disabled',true);
	$('#priorityId').selectpicker('refresh');
	
}

function resetDropStatusMain(){
	var s = '';
	s+='<option value="">-- Todos --</option>';
	$('#statusId').html(s);
	$('#statusId').prop('disabled',true);
	$('#statusId').selectpicker('refresh');
	
}

function resetDropTypeMain(){
	var s = '';
	s+='<option value="">-- Todos --</option>';
	$('#typeId').html(s);
	$('#typeId').prop('disabled',true);
	$('#createRFC').prop('disabled',true);
	$('#typeId').selectpicker('refresh');
	
}
function resetDropStatus(){
	var s = '';
	s+='<option value="">-- Seleccione una opci&oacute;n --</option>';
	$('#statusId').html(s);
	$('#statusId').prop('disabled',true);
	$('#statusId').selectpicker('refresh');
	
}
function resetDropType(){
	var s = '';
	s+='<option value="">-- Seleccione una opci&oacute;n --</option>';
	$('#tId').html(s);
	$('#tId').prop('disabled',true);
	$('#createRFC').prop('disabled',true);
	$('#tId').selectpicker('refresh');
	
}

