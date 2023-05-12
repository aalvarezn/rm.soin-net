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
	$('input[name="daterange"]').attr('value', moment().subtract(7, 'day').format("DD/MM/YYYY")+' - '+ moment().format('DD/MM/YYYY'));
	initRFCTable();
	initRFCFormValidation();
	dropDownChange();
	dropDownChangeSystem();
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

$('#tableFilters #projectId').change(function() {
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
								{"name": "sigesId", "value": $('#tableFilters #sigesId').children("option:selected").val()},
								{"name": "systemId", "value": $('#tableFilters #systemId').children("option:selected").val()},
								{"name": "projectId", "value": $('#tableFilters #projectId').children("option:selected").val()}
						);
					},
					"aoColumns" : [
						{
							"mDataProp" : "id",
						},
						{
						
						"mDataProp" : "rfcNumber"
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
							options = options
							+ '<a href="'
							+ getCont()
							+ 'report/summaryReportRFC-'
							+ row.id
							+ '" target="_blank" title="Reporte"><i class="material-icons gris" style="font-size: 25px;">report</i></a>';
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
						console.log(s);
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
		url : getCont() + "report/downloadreportrfc",
		timeout : 60000,
		data : {
			dateRange :$('#tableFilters input[name="daterange"]').val(),
			projectId: $('#tableFilters #projectId').children("option:selected").val(),
			systemId: $('#tableFilters #systemId').children("option:selected").val(),
			sigesId: $('#tableFilters #sigesId').children("option:selected").val()
		},
	    beforeSend: function() {
	    	showSpinner();
	      },
		success : function(response) {
			console.log(response);
			//console.log(atob(response.obj.file));
			
			var blob = new Blob([b64toBlob(response.obj.file,response.obj.ContentType)], {type: response.obj.ContentType});
			var link = document.createElement('a');
			link.href = window.URL.createObjectURL(blob);
			link.download = response.obj.name;
			link.click();   
		},
		error : function(x, t, m) {
			notifyAjaxError(x, t, m);
			  hideSpinner();
		},
		   complete: function() {
			      // ocultar el mensaje de descarga después de completar la
					// solicitud
			      hideSpinner();
			    },
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

function showSpinner(){
	var miElemento = document.getElementById("loading"); 
	miElemento.style.display = "flex";
}

function hideSpinner(){
	var miElemento = document.getElementById("loading"); 
	miElemento.style.display = "none";
}
