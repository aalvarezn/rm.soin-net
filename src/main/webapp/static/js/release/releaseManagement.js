var releaseTable = $('#dtReleases').DataTable();
var formChangeUser = $('#changeUserForm');
var formChangeStatus = $('#changeStatusForm');
var trackingReleaseForm = $('#trackingReleaseForm');
var switchStatus=false;
$(function() {
	activeItemMenu("managemetReleaseItem");
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

	formChangeStatus.find('#statusId').change(function() {
		formChangeStatus.find('#motive').val($(this).children("option:selected").attr('data-motive'));
	});

	loadTableRelease();
	dropDownChange();
	showSendEmail();
	 $('.tagInitMail').tagsInput({
		 placeholder: 'Ingrese los correos'
	 });
});

function refreshTable(){
	releaseTable.ajax.reload();
}

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
						"sAjaxSource" : getCont() + "management/release/systemRelease",
						"fnServerParams": function ( aoData ) {
							aoData.push({"name": "dateRange", "value": $('#tableFilters input[name="daterange"]').val()},
									{"name": "systemId", "value": $('#tableFilters #systemId').children("option:selected").val()},
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
								"mDataProp" : "status.name",
							},
							{
								"mDataProp" : "retries",
							},
							{
								mRender : function(data, type, row) {
									var options = '<div class="iconLine"> ';

									if(row.status.name != 'Anulado') {
										options = options
										+ '<a onclick="confirmCancelRelease('+row.id+')" title="Anular"><i class="material-icons gris" style="font-size: 25px;">highlight_off</i></a>';
									}
									options = options
									+ '<a onclick="changeStatusRelease('+row.id+')" title="Cambiar Estado"><i class="material-icons gris" style="font-size: 25px;">offline_pin</i></a>';
									options = options
									+ '<a onclick="openChangeUserModal('
									+ row.id
									+ ')" title="Asignar"><i class="material-icons gris" style="font-size: 25px;">account_circle</i> </a>';
									options = options
									+ '<a onclick="openReleaseTrackingModal('
									+ row.id
									+ ')" title="Rastreo"><i class="material-icons gris" style="font-size: 25px;">location_on</i> </a>';
									options = options
									+ '<a href="'
									+ getCont()
									+ 'release/summary-'
									+ row.id
									+ '" title="Resumen"><i class="material-icons gris" style="font-size: 25px;">info</i></a>'
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
function changeStatusRelease(releaseId) {
	var dtReleases = $('#dtReleases').dataTable(); // tabla
	var idRow = dtReleases.fnFindCellRowIndexes(releaseId, 0); // idRow
	var rowData = releaseTable.row(idRow).data();
	formChangeStatus[0].reset();
	formChangeStatus.find('#motive').val('');
	
	console.log(rowData);
	if(rowData.system.emailTemplate.length===0){
		$('.tagInitMail#senders').importTags( "" );
	}else{
		console.log(rowData.system.emailTemplate[0].cc);
		$('.tagInitMail#senders').importTags(rowData.system.emailTemplate[0].cc ? rowData.system.emailTemplate[0].cc : "" );
		
	}
	
	formChangeStatus.find('.selectpicker').selectpicker('refresh');
	formChangeStatus.find('#note').val("");
	formChangeStatus.find('#idRelease').val(rowData.id);
	formChangeStatus.find('#releaseNumber').val(rowData.releaseNumber);
	formChangeStatus.find(".fieldError").css("visibility", "hidden");
	formChangeStatus.find('.fieldError').removeClass('activeError');
	formChangeStatus.find('.form-line').removeClass('error');
	formChangeStatus.find('.form-line').removeClass('focused');
	$('#divError').attr( "hidden",true);
	$('#divEmail').attr( "hidden",true);
	formChangeStatus.find('#dateChange').val(moment().format('DD/MM/YYYY hh:mm a'))
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
			idError: formChangeStatus.find('#errorId').children("option:selected").val(),
			dateChange: formChangeStatus.find('#dateChange').val(),
			motive: formChangeStatus.find('#motive').val(),
			sendEmail:switchStatus,
			senders:formChangeStatus.find('#senders').val(),
			note:formChangeStatus.find('#note').val()
			
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

	$.each(formChangeStatus.find('select[required]'), function( index, select ) {
	
		if($.trim(select.value).length == 0 || select.value == ""){
			
			var statusSelected =$("#statusId").find("option:selected").text();
			if(select.id==="errorId"&&statusSelected!=="Error"){
				valid = true;
			}else{
				formChangeStatus.find('#'+select.id+"_error").css("visibility","visible");
				formChangeStatus.find('#'+select.id+"_error").addClass('activeError');
				valid = false;
			}
		
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
	$.each(formChangeStatus.find('input[required]'), function( index, input ) {
		if($.trim(input.value) === ""){
			console.log(input.id);
			if(input.id==="senders"){
				if(switchStatus){
				formChangeStatus.find('#'+input.id+"_error").css("visibility","visible");
					formChangeStatus.find('#'+input.id+"_error").addClass('activeError');
					formChangeStatus.find('#'+input.id+"").parent().attr("class",
					"form-line error focused");
					valid = false;
				}
			}else{
			formChangeStatus.find('#'+input.id+"_error").css("visibility","visible");
			formChangeStatus.find('#'+input.id+"_error").addClass('activeError');
			formChangeStatus.find('#'+input.id+"").parent().attr("class",
			"form-line error focused");
			valid = false;
			}
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
	var dtReleases = $('#dtReleases').dataTable(); // tabla
	var idRow = dtReleases.fnFindCellRowIndexes(releaseId, 0); // idRow
	var rowData = releaseTable.row(idRow).data();
	trackingReleaseForm.find('#idRelease').val(rowData.id);
	trackingReleaseForm.find('#releaseNumber').text(rowData.releaseNumber);
	$.ajax({
		type : "GET",
		url : getCont() + "release/tracking/"+ rowData.id ,
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
	case 'Error':
		return 'rgb(255,0,0)';
		break;
	case 'Anulado':
		return 'rgb(233, 30, 99)';
		break;
	default:
		return 'rgb(0, 181, 212)';
	break;
	}
}