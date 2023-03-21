$.fn.dataTable.ext.errMode = 'none';
var releaseTable = $('#dtReleases').DataTable({
	'columnDefs' : [ {
		'visible' : false,
		'targets' : [ 4, 5 ]
	} ]
});
var formReleaseDraft = $('#formAddReleaseDraft');
var formChangeUser = $('#changeUserForm');
var trackingReleaseForm = $('#trackingReleaseForm');
$(function() {

	activeItemMenu("releasesQAItem");
	loadTableRelease('systemReleaseQA');
	setTab();

	$("#addReleaseSection").hide();

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


	loadTableRelease('systemReleaseQA');
	setTab();
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

$('#formAddReleaseDraft #requirement_name').keydown(function( event ) {
	if ( event.which == 13 || event.which == 32 ) {
		event.preventDefault();
	}
});

function loadTableRelease(nameTable) {
	console.log(nameTable);
	if (releaseTable != undefined) {
		releaseTable.destroy();
	}

	releaseTable = $('#dtReleases')
	.on(
			'error.dt',
			function(e, settings, techNote, message) {
				console.log(e);
				console.log(settings);
				console.log(techNote);
				console.log(message);
				unblockUI();
				swal("Error!",
						"Se ha presentado un error en la solicitud."
						+ "\n Por favor intente de nuevo.",
				"error");
			})
			.DataTable(
					{
						'columnDefs' : [ {
							'visible' : false,
							'targets' : [ 0, 4, 5 ]
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
						"sAjaxSource" : getCont() + "release/" + nameTable,
						"fnServerParams": function ( aoData ) {
							aoData.push({"name": "dateRange", "value": $('#tableFilters input[name="daterange"]').val()},
									{"name": "systemId", "value": $('#tableFilters #systemId').children("option:selected").val()},
									{"name": "statusId", "value": $('#tableFilters #statusId').children("option:selected").val()});
						}, 
						"aoColumns" : [
							{
								"sClass" : "hideColumn",
								"mDataProp" : "id"
							},
							
							{
								"mDataProp" : "releaseNumber",
							},
							{
								"mDataProp" : "system.name",
							},
							{
								"sClass" : "block-with-text",
								mRender : function(data, type, row) {
									return '<span class="" data-toggle="tooltip" data-placement="top" title="'
									+ ((row.description != null) ? row.description
											: '')
											+ '">'
											+ ((row.description != null) ? row.description
													: '') + '</span>';
								}
							},

							{
								"sClass" : "hideColumn",
								"mDataProp" : "observations",
							},
							{
								"sClass" : "hideColumn",
								mRender : function(data, type, row) {
									return row;
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
								mRender : function(data, type, row) {
									var options = '<div class="iconLineC">';
									options = options
									+ '<a onclick="openReleaseTrackingModal('
									+ row.id
									+ ')" title="Rastreo"><i class="material-icons gris" style="font-size: 25px;">location_on</i> </a>';

									options = options
									+ '<a href="'
									+ getCont()
									+ 'release/summaryQA-'
									+ row.id
									+ '" title="Resumen"><i class="material-icons gris">info</i></a> </div>';
									return options;
								}
							} ],
							responsive : true,
							ordering : false,
							info : true
					});
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

/* -------------------------- Request -------------------------- */
let countRequest;
jQuery("#requirement_name")
.autocomplete(
		{
			source : function(request, response) {
				$.get(getCont() + "request/" + "requestAutoComplete-"
						+ $("#requirement_name").val(), function(data) {
					response(data);
				});
			},
			response : function(event, ui) {
				countRequest = ui.content.length;
				if (countRequest === 0) {
					notifyInfo('Sin resultados');
				}
			},
			select : function(e, ui) {
				if (!($('#listRequirement ul #' + ui.item.id).length)) {
					if ($('#listRequirement ul li').length < 1) {
						$("#listRequirement ul")
						.append(
								'<li id="'
								+ ui.item.id
								+ '" class="list-group-item">'
								+ ui.item.name
								+ ' <span class="flr"> <a onclick="deleteRequirement('
								+ ui.item.id
								+ ')" title="Borrar"><i class="material-icons gris">delete</i></a>'
								+ ' </span>' + ' </li>');
					} else {
						notifyInfo('Ya existe un TPO/BT ');
					}

				}
				$(this).val('');
				return false;
			},
			delay : 0
		});

function deleteRequirement(id) {
	$('#listRequirement ul #' + id).remove();
}

$("input[name=group1]").on("change", function() {
	$("#formAddReleaseDraft #requirement_name").val("");
	if ($("input[name='group1']:checked").val() != 'TPO/BT') {
		$("#listRequirement").hide();
		$('#requirement_name').autocomplete('disable');
	} else {
		$("#listRequirement").show()
		$('#requirement_name').autocomplete('enable');
	}
});

function setTab() {
	if ($("#listType").val() == "userRelease") {
		$('#releaseTabs .nav-tabs a[href="#releases"]').tab('show');
	}
	if ($("#listType").val() == "teamRelease") {
		$('#releaseTabs .nav-tabs a[href="#equipos"]').tab('show');
	}

	if ($("#listType").val() == "systemRelease") {
		$('#releaseTabs .nav-tabs a[href="#sistemas"]').tab('show');
	}
}

function changeSlide() {
	$("#tableSection").toggle("slide");
	$("#buttonAddRelease").hide();
	$("#addReleaseSection").show('slide', {
		direction : 'right'
	}, 500);
	$("#addReleaseSection").insertAfter("#tableSection");
}

function openAddReleaseSection() {
	$('#createRelease').show();
	$('#copyRelease').hide();
	$('#system_id').show();
	formReleaseDraft.find('#system_idDiv').show();
	formReleaseDraft.find('#divAddObjectOption').hide();
	changeSlide()
}

function copyRelease(cellValue) {
	var dtReleases = $('#dtReleases').dataTable(); // tabla
	var idRow = dtReleases.fnFindCellRowIndexes(cellValue, 0); // idRow
	var rowData = releaseTable.row(idRow).data();
	formReleaseDraft.find('#idRelease').val(rowData.id);
	formReleaseDraft.find('#description').val(rowData.description);
	formReleaseDraft.find('#observations').val(rowData.observations);
	$('#copyRelease').show();
	$('#createRelease').hide();
	formReleaseDraft.find('#system_idDiv').hide();
	formReleaseDraft.find('#divAddObjectOption').show();
	changeSlide()
}

function closeAddReleaseSection() {
	clearValidCreateRelease();
	$("#addReleaseSection").hide('slide', {
		direction : 'right'
	}, 400);
	$("#buttonAddRelease").show();
	$("#tableSection").toggle("slide");
	$("#tableSection").insertAfter("#addReleaseSection");

}

function editRelease(element) {
	var cont = getCont();
	window.location = cont + "release/editRelease-" + element;
}

function confirmDeleteRelease(element) {
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
			deleteRelease(element);
		}		
	});
}

function deleteRelease(element) {
	blockUI();
	var cont = getCont();
	$.ajax({
		type : "DELETE",
		url : cont + "release/" + "deleteRelease/" + element,
		timeout : 60000,
		data : {},
		success : function(response) {
			switch (response.status) {
			case 'success':
				swal("Correcto!", "El release ha sido eliminado exitosamente.",
						"success", 2000)
						$('#dtReleases').DataTable().ajax.reload();
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

function closeDeleteReleaseModal() {
	$('#deleteReleaseModal').modal('hide');
}

function summaryRelease(element) {
	alert('Resumen release:' + element);
}

function changeSelectView(view) {
	switch (view) {
	case 1:
		selectTableRelease('userRelease');
		break;
	case 2:
		selectTableRelease('teamRelease');
		break;
	case 3:
		selectTableRelease('systemRelease');
		break;
	default:
		window.location = cont;
	break;
	}
}

function selectTableRelease(name) {
	blockUI();
	try {
		loadTableRelease(name);
		$('#dtReleases').DataTable().ajax.reload(function() {
			unblockUI();
		});
		$('.count-to').countTo('restart')
	} catch (error) {
		unblockUI();
		swal("Error!", "Error al realizar la solicitud.", "error");
	}
	;
}

function createCopyRelease() {
	var cont = getCont();
	if (validCreateRelease(false)) {
		let requeriment = formReleaseDraft.find("input[name=group1]:checked")
		.val();
		let requirement_name = formReleaseDraft.find("#requirement_name").val();

		if (requeriment == 'TPO/BT') {
			requirement_name = listLi();
		}
		$
		.ajax({
			type : "POST",
			url : cont + "release/" + "release-copy",
			timeout : 60000,
			data : {
				idRelease : formReleaseDraft.find('#idRelease').val(),
				description : formReleaseDraft.find('#description')
				.val(),
				observations : formReleaseDraft.find('#observations')
				.val(),
				requeriment : requeriment,
				requirement_name : requirement_name,
				addObject : formReleaseDraft.find("#addObjectOption")
				.is(":checked")
			},
			success : function(response) {
				switch (response.status) {
				case 'success':
					Swal.fire({
						title: 'Correcto!',
						text: "El release ha sido creado exitosamente.",
						icon: 'success',
						customClass: 'swal-wide',
						timer : 2000,
						showConfirmButton : false,
					}).then((result) => {
						window.location = cont
						+ "release/editRelease-"
						+ response.data;		
					}); 
					break;
				case 'fail':
					swal("Error!","A ocurrido un problema, por favor intente de nuevo.","error")
					break;
				case 'exception':
					swal("Error!", response.exception, "error")
					break;
				default:
					alert("default");
				}
			},
			error : function(x, t, m) {
				notifyAjaxError(x, t, m);
			}
		});
	}
}

function createRelease() {

	var cont = getCont();
	var form = "#formAddReleaseDraft";
	var system_id = $(form + " #system_id").children("option:selected").val();
	var description = $(form + " #description").val();
	var observations = $(form + " #observations").val();
	var requeriment = $(form + " input[name=group1]:checked").val();
	var requirement_name = $(form + " #requirement_name").val();

	if (requeriment == 'TPO/BT') {
		requirement_name = listLi();
	}

	var valid = validCreateRelease(true);
	if (valid) {
		$
		.ajax({
			type : "POST",
			url : cont + "release/" + "release-generate",
			timeout : 60000,
			data : {
				system_id : system_id,
				description : description,
				observations : observations,
				requeriment : requeriment,
				requirement_name : requirement_name
			},
			success : function(response) {
				switch (response.status) {
				case 'success':
					Swal.fire({
						title: 'Correcto!',
						text: "El release ha sido creado exitosamente.",
						icon: 'success',
						customClass: 'swal-wide',
						timer : 2000,
						showConfirmButton : false,
					}).then((result) => {
						window.location = cont
						+ "release/editRelease-"
						+ response.data;		
					});
					break;
				case 'fail':
					swal(
							"Error!",
							"A ocurrido un problema, por favor intente de nuevo.",
					"error")
					break;
				case 'exception':
					swal("Error!", response.exception, "error")
					break;
				default:
					alert("default");
				}
			},
			error : function(x, t, m) {
				notifyAjaxError(x, t, m);
			}
		});
	}
}

function clearValidCreateRelease() {
	$("#formAddReleaseDraft")[0].reset();
	$("#formAddReleaseDraft #system_id_error").css("visibility", "hidden");
	$("#formAddReleaseDraft #description").parent().attr("class", "form-line");
	$("#formAddReleaseDraft #description_error").css("visibility", "hidden");
	$("#formAddReleaseDraft #observations").parent().attr("class", "form-line");
	$("#formAddReleaseDraft #observations_error").css("visibility", "hidden");
	$("#formAddReleaseDraft #requirement_name").parent().attr("class",
	"form-line");
	$("#formAddReleaseDraft #requirement_name_error").css("visibility",
	"hidden");
	$('#listRequirement ul').find('li').each(function(j) {
		$(this).remove();
	});
	$("#formAddReleaseDraft #system_id").selectpicker('val', '');
	$('.selectpicker').selectpicker('refresh');
}

function validCreateRelease(complete) {
	var valid = true;
	var system_id = $("#formAddReleaseDraft #system_id").children(
	"option:selected").val();
	var description = $("#formAddReleaseDraft #description");
	var observations = $("#formAddReleaseDraft #observations");
	var requeriment = $("#formAddReleaseDraft input[name=group1]:checked")
	.val();
	var requirement_name = $("#formAddReleaseDraft #requirement_name");

	if (complete) {
		if ($.trim(system_id) == "" || $.trim(system_id).length < 2) {
			$("#formAddReleaseDraft #system_id_error").css("visibility",
			"visible");
			valid = false;
		} else {
			$("#formAddReleaseDraft #system_id_error").css("visibility",
			"hidden");
		}
	}

	if ($.trim(description.val()) == "" || $.trim(description.val()).length < 2) {
		description.parent().attr("class", "form-line focused error");
		$("#formAddReleaseDraft #description_error").text("Campo Requerido.");
		$("#formAddReleaseDraft #description_error").css("visibility",
		"visible");
		valid = false;
	} else {
		description.parent().attr("class", "form-line");
		$("#formAddReleaseDraft #description_error")
		.css("visibility", "hidden");
	}
	if ($.trim(observations.val()) == ""
		|| $.trim(observations.val()).length < 2) {
		observations.parent().attr("class", "form-line focused error");
		$("#formAddReleaseDraft #observations_error").text("Campo Requerido.");
		$("#formAddReleaseDraft #observations_error").css("visibility",
		"visible");
		valid = false;
	} else {
		observations.parent().attr("class", "form-line");
		$("#formAddReleaseDraft #observations_error").css("visibility",
		"hidden");
	}

	if (requeriment != 'TPO/BT') { // Si no se trata de una tpo

		if ($.trim(requirement_name.val()) == ""
			|| $.trim(requirement_name.val()).length < 2) {
			$("#formAddReleaseDraft #requirement_name").parent().attr("class",
			"form-line focused error");
			$("#formAddReleaseDraft #requirement_name_error").text(
			"Campo Requerido.");
			$("#formAddReleaseDraft #requirement_name_error").css("visibility",
			"visible");
			valid = false;
		} else {
			$("#formAddReleaseDraft #requirement_name_error").css("visibility",
			"hidden");
			$("#formAddReleaseDraft #requirement_name").parent().attr("class",
			"form-line");
		}

	} else {
		if ($('#listRequirement ul').children("li").length == 0) {
			valid = false;
			$("#formAddReleaseDraft #requirement_name").parent().attr("class",
			"form-line focused error");

			$("#formAddReleaseDraft #requirement_name_error").text(
			"Campo Requerido.");
			$("#formAddReleaseDraft #requirement_name_error").css("visibility",
			"visible");
		} else {
			$("#formAddReleaseDraft #requirement_name_error").css("visibility",
			"hidden");
			$("#formAddReleaseDraft #requirement_name").parent().attr("class",
			"form-line");
		}

	}

	return valid;
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

function listLi() {
	var listRequirement = "";
	$('#listRequirement ul').find('li').each(function(j) {
		listRequirement = listRequirement + $(this).attr('id') + ","
	});
	return listRequirement;
}

function listRequirement() {
	var cont = getCont();
	$.ajax({
		type : 'get',
		url : cont + "request/" + "requestList",
		timeout : 60000,
		success : function(data) {
			$.each(data, function(key, value) {
				$("#formAddReleaseDraft #requirement_name_select").append(
						new Option("option text", "value"));
			});
			$('.selectpicker').selectpicker('refresh');
		},
		error : function(x, t, m) {
			notifyAjaxError(x, t, m);
		}
	});
}

function openReleaseTrackingModal(releaseId) {
	var dtReleases = $('#dtReleases').dataTable(); // tabla
	var idRow = dtReleases.fnFindCellRowIndexes(releaseId, 0); // idRow
	var rowData = releaseTable.row(idRow).data();
	trackingReleaseForm.find('#idRelease').val(rowData.id);
	trackingReleaseForm.find('#releaseNumber').text(rowData.releaseNumber);
	loadTrackingRelease(rowData);
	$('#trackingReleaseModal').modal('show');
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
