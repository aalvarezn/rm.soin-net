var releaseTable = $('#dtReleases').DataTable();
var formChangeUser = $('#changeUserForm');
var formChangeStatus = $('#changeStatusForm');
var trackingReleaseForm = $('#trackingReleaseForm');
$(function() {
	activeItemMenu("reportItem",true);
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



	formChangeStatus.find('#statusId').change(function() {
		formChangeStatus.find('#motive').val($(this).children("option:selected").attr('data-motive'));
	});
	$('input[name="daterange"]').attr('value', moment().subtract(7, 'day').format("DD/MM/YYYY")+' - '+ moment().format('DD/MM/YYYY'));
	loadTableRelease();
	
});

$('input[name="daterange"]').on('apply.daterangepicker', function(ev, picker) {
	$(this).val(picker.startDate.format('DD/MM/YYYY') + ' - ' + picker.endDate.format('DD/MM/YYYY'));
	releaseTable.ajax.reload();
});

$('input[name="daterange"]').on('cancel.daterangepicker', function(ev, picker) {
	$(this).val('');
	releaseTable.ajax.reload();
});

$('#tableFilters #systemId').change(function() {
	releaseTable.ajax.reload();
});

$('#tableFilters #projectId').change(function() {
	releaseTable.ajax.reload();
});

$('#tableFilters #statusId').change(function() {
	releaseTable.ajax.reload();
});

function loadTableRelease() {
	if (releaseTable != undefined) {
		releaseTable.destroy();
	}
	releaseTable = $('#dtReleases')
	.on(
			'error.dt',
			function(e, settings, techNote, message) {
				unblockUI();
				swal("Error!",
						"Se ha presentado un error en la solicitud."
						+ "\n Por favor intente de nuevo.",
				"error");
			})
			.DataTable(
					{
						"columnDefs" : [ {
							"targets" : [ 0 ],
							"visible" : false,
							"searchable" : false
						} ],
						"iDisplayLength" : 10,
						"language" : {
							"emptyTable" : "No existen registros",
							"zeroRecords" : "No existen registros",
							"processing" : "Cargando",
						},
						"iDisplayStart" : 0,
						"processing" : true,
						"serverSide" : true,
						"sAjaxSource" : getCont() + "report/listRelease",
						"fnServerParams": function ( aoData ) {
							aoData.push(
									{"name": "dateRange", "value": $('#tableFilters input[name="daterange"]').val()},
									{"name": "systemId", "value": $('#tableFilters #systemId').children("option:selected").val()},
									{"name": "projectId", "value": $('#tableFilters #projectId').children("option:selected").val()},
									{"name": "statusId", "value": $('#tableFilters #statusId').children("option:selected").val()});
						}, 
						"aoColumns" : [
							{
								"mDataProp" : "id"
							},
							{
								"mDataProp" : "releaseNumber",
							},
							{
								"sClass" : "block-with-text",
								mRender : function(data, type, row) {
									return '<span class="" data-toggle="tooltip" data-placement="top" title="'
									+ ((row.description != null) ? row.description
											: "")
											+ '">'
											+ ((row.description != null) ? row.description
													: "") + '</span>';
								}
							},
							{
								"mDataProp" : "user.fullName",
								"sDefaultContent" : "admin",
							},
							{
								mRender : function(data, type, row) {
									return moment(row.createDate).format('DD/MM/YYYY h:mm:ss a');
								}
							},
							{
								"mDataProp" : "system.proyect.code",
							},
							{
								"mDataProp" : "status.name",
							},
							{
								"mDataProp" : "retries",
							},
							{
								mRender : function(data, type, row) {
									var options = '<div class="iconLine"> ';
									options = options
									+ '<a href="'
									+ getCont()
									+ 'report/summaryReportRelease-'
									+ row.id
									+ '" target="_blank" title="Reporte"><i class="material-icons gris" style="font-size: 25px;">report</i></a>';
									options = options
									+ '<a onclick="openReleaseTrackingModal('
									+ row.id
									+ ')"  title="Rastreo"><i class="material-icons gris" style="font-size: 25px;">location_on</i> </a>';
									options = options
									+ '<a href="'
									+ getCont()
									+ 'report/summaryRelease-'
									+ row.id
									+ '" target="_blank" title="Resumen"><i class="material-icons gris" style="font-size: 25px;">info</i></a>'
									+ ' </div>';
									return options;
								}
							} ],
							responsive : true,
							ordering : false,
							info : true
					});
}



function confirmCancelRelease(index){
	Swal.fire({
		title: '\u00BFEst\u00e1s seguro que desea cancelar el release?',
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
			cancelRelease(index);
		}		
	});
}

function downLoadReport(){
	console.log($('#tableFilters input[name="daterange"]').val().replaceAll("/","^"));
	$.ajax({
		type : "GET",
		cache : false,
		contentType: "application/json; charset=utf-8",
		async : true,
		url : getCont() + "report/downloadreportrelease",
		data : {
			dateRange :$('#tableFilters input[name="daterange"]').val(),
			projectId: $('#tableFilters #projectId').children("option:selected").val(),
			systemId: $('#tableFilters #systemId').children("option:selected").val()
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
		
		   complete: function() {
			      // ocultar el mensaje de descarga después de completar la
					// solicitud
			      hideSpinner();
			    },
		error : function(x, t, m) {
			notifyAjaxError(x, t, m);
			  hideSpinner();
		}
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
function cancelRelease(index) {
	blockUI();
	$.ajax({
		type : "GET",
		url : getCont() + "management/release/" + "cancelRelease",
		timeout : 60000,
		data : {
			idRelease : index
		},
		success : function(response) {
			responseCancelRelease(response);
		},
		error : function(x, t, m) {
			notifyAjaxError(x, t, m);
		}
	});
}

function responseCancelRelease(response) {
	switch (response.status) {
	case 'success':
		swal("Correcto!", "El release ha sido anulado exitosamente.",
				"success", 2000)
				releaseTable.ajax.reload();
		break;
	case 'fail':
		swal("Error!", response.exception, "error")
		break;
	case 'exception':
		swal("Error!", response.exception, "error")
		break;
	}
}

function changeStatusRelease(releaseId) {
	var dtReleases = $('#dtReleases').dataTable(); // tabla
	var idRow = dtReleases.fnFindCellRowIndexes(releaseId, 0); // idRow
	var rowData = releaseTable.row(idRow).data();
	formChangeStatus[0].reset();
	formChangeStatus.find('#motive').val('');
	formChangeStatus.find('.selectpicker').selectpicker('refresh');
	formChangeStatus.find('#idRelease').val(rowData.id);
	formChangeStatus.find('#releaseNumber').val(rowData.releaseNumber);
	formChangeStatus.find("#fieldError").css("visibility", "hidden");
	formChangeStatus.find('#dateChange').val(moment().format('DD/MM/YYYY hh:mm a'))
	$('#changeStatusModal').modal('show');
}

function saveChangeStatusModal(){
	if (!validStatusRelease())
		return false;
	blockUI();
	$.ajax({
		type : "GET",
		url : getCont() + "management/release/" + "statusRelease",
		timeout : 60000,
		data : {
			idRelease : formChangeStatus.find('#idRelease').val(),
			idStatus: formChangeStatus.find('#statusId').children("option:selected").val(),
			dateChange: formChangeStatus.find('#dateChange').val(),
			motive: formChangeStatus.find('#motive').val()
		},
		success : function(response) {
			responseStatusRelease(response);
		},
		error : function(x, t, m) {
			notifyAjaxError(x, t, m);
		}
	});
}

function responseStatusRelease(response) {
	switch (response.status) {
	case 'success':
		swal("Correcto!", "El release ha sido modificado exitosamente.",
				"success", 2000);
		closeChangeStatusModal();
		releaseTable.ajax.reload();
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
	formChangeStatus[0].reset();
	formChangeStatus.find('#userId').selectpicker('val', '');
	$('#changeStatusModal').modal('hide');
}


function validStatusRelease() {
	let valid = true;
	formChangeStatus.find(".fieldError").css("visibility", "hidden");
	formChangeStatus.find('.fieldError').removeClass('activeError');
	formChangeStatus.find('.form-line').removeClass('error');
	formChangeStatus.find('.form-line').removeClass('focused');
	$.each(formChangeStatus.find('input[required]'), function( index, input ) {
		if($.trim(input.value) == ""){
			formChangeStatus.find('#'+input.id+"_error").css("visibility","visible");
			formChangeStatus.find('#'+input.id+"_error").addClass('activeError');
			formChangeStatus.find('#'+input.id+"").parent().attr("class",
			"form-line error focused");
			valid = false;
		}
	});
	$.each(formChangeStatus.find('select[required]'), function( index, select ) {
		if($.trim(select.value).length == 0 || select.value == ""){
			formChangeStatus.find('#'+select.id+"_error").css("visibility","visible");
			formChangeStatus.find('#'+select.id+"_error").addClass('activeError');
			valid = false;
		}
	});

	$.each(formChangeStatus.find('textarea[required]'), function( index, textarea ) {
		if($.trim(textarea.value).length == 0 || textarea.value == ""){
			formChangeStatus.find('#'+textarea.id+"_error").css("visibility","visible");
			formChangeStatus.find('#'+textarea.id+"_error").addClass('activeError');
			formChangeStatus.find('#'+textarea.id+"").parent().attr("class",
			"form-line error focused");
			valid = false;
		}
	});

	return valid;
}

function openChangeUserModal(releaseId) {
	var dtReleases = $('#dtReleases').dataTable(); // tabla
	var idRow = dtReleases.fnFindCellRowIndexes(releaseId, 0); // idRow
	var rowData = releaseTable.row(idRow).data();
	formChangeUser.find('#idRelease').val(rowData.id);
	formChangeUser.find('#releaseNumber').val(rowData.releaseNumber);
	loadSelectChangeUser(rowData);
	$('#changeUserModal').modal('show');
}


function openReleaseTrackingModal(releaseId) {
	console.log(releaseId);
	var dtReleases = $('#dtReleases').dataTable(); // tabla
	console.log(dtReleases);
	var idRow = dtReleases.fnFindCellRowIndexes(releaseId, 0); // idRow
	console.log(idRow[0]);
	var rowData = releaseTable.row(idRow).data();
	console.log(rowData);
	trackingReleaseForm.find('#idRelease').val(rowData.id);
	trackingReleaseForm.find('#releaseNumber').text(rowData.releaseNumber);

	
	$.ajax({
		type : "GET",
		url : getCont() + "rfc/tracking/"+ rowData.id ,
		timeout : 600000,
		data : {},
		success : function(response) {
			tracking=response.obj;
			loadTrackingRelease(tracking);
			$('#trackingReleaseModal').modal('show');
		},
		error : function(x, t, m) {
			notifyAjaxError(x, t, m);
		}
	});

	
	
	
}

function loadTrackingRelease(rowData){
	trackingReleaseForm.find('tbody tr').remove();
	if(rowData.tracking.length == 0){
		trackingReleaseForm.find('tbody').append('<tr><td colspan="4" style="text-align: center;">No hay movimientos</td></tr>');
	}
	$.each(rowData.tracking, function(i, value) {
		trackingReleaseForm.find('tbody').append('<tr style="padding: 10px 0px 0px 0px;" > <td><span style="background-color: '+getColorNode(value.status)+';" class="round-step"></span></td>	<td>'+value.status+'</td>	<td>'+moment(value.trackingDate).format('DD/MM/YYYY h:mm:ss a')+'</td>	<td>'+value.operator+'</td> <td>'+(value.motive && value.motive != null && value.motive != 'null' ? value.motive:'' )+'</td>	</tr>');
	});
}

function closeTrackingReleaseModal(){
	trackingReleaseForm[0].reset();
	$('#trackingReleaseModal').modal('hide');
}

function loadSelectChangeUser(rowData) {
	formChangeUser.find('#userId').find('option').remove().end().append(
	'<option value="">-- Seleccione una opci\u00F3n --</option>');

	$.each(rowData.system.userTeam, function(i, value) {
		formChangeUser.find('#userId').append($('<option>', {
			value : value.id,
			text : value.fullName
		}));
	});
	formChangeUser.find("#userId_error").css("visibility", "hidden");
	formChangeUser.find('#userId').selectpicker('val', '');
	formChangeUser.find('#userId').selectpicker('refresh');
}

function closeChangeUserModal() {
	formChangeUser[0].reset();
	formChangeUser.find('#userId').selectpicker('val', '');
	$('#changeUserModal').modal('hide');
}

function saveChangeUserModal() {
	var cont = getCont();
	if (!validAssignRelease())
		return false;
	blockUI();
	$.ajax({
		type : "POST",
		url : cont + "release/" + "assignRelease",
		timeout : 60000,
		data : {
			idRelease : formChangeUser.find('#idRelease').val(),
			idUser : formChangeUser.find('#userId').children("option:selected")
			.val()
		},
		success : function(response) {
			responseChangeUserModal(response);
		},
		error : function(x, t, m) {
			notifyAjaxError(x, t, m);
		}
	});
}

function responseChangeUserModal(response) {

	switch (response.status) {
	case 'success':
		swal("Correcto!", "El release ha sido modificado exitosamente.",
				"success", 2000)
				closeChangeUserModal();
		releaseTable.ajax.reload();
		break;
	case 'fail':
		swal("Error!", response.exception, "error")
		break;
	case 'exception':
		swal("Error!", response.exception, "error")
		break;
	}
}

function validAssignRelease() {
	let valid = true;
	let userId = formChangeUser.find('#userId').children("option:selected")
	.val();
	if ($.trim(userId) == "" || $.trim(userId).length == 0) {
		formChangeUser.find("#userId_error").css("visibility", "visible");
		return false;
	} else {
		formChangeUser.find("#userId_error").css("visibility", "hidden");
		return true;
	}
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

function exportPDF(){
	 location.href=getCont()+'report/downloadReportGeneral';
}

function showSpinner(){
	var miElemento = document.getElementById("loading"); 
	miElemento.style.display = "flex";
}

function hideSpinner(){
	var miElemento = document.getElementById("loading"); 
	miElemento.style.display = "none";
}