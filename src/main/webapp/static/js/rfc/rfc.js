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
	activeItemMenu("rfcItem");
	$("#addRFCSection").hide();
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
});

$('input[name="daterange"]').on('apply.daterangepicker', function(ev, picker) {
	$(this).val(picker.startDate.format('DD/MM/YYYY') + ' - ' + picker.endDate.format('DD/MM/YYYY'));
	releaseTable.ajax.reload();
});

$('input[name="daterange"]').on('cancel.daterangepicker', function(ev, picker) {
	$(this).val('');
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
								"mDataProp" : "id"
							},
							{
								"mDataProp" : "system.name",
							},
							{
								"mDataProp" : "releaseNumber",
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
									var options = '<div class="iconLine">';
									if (row.status.name == 'Borrador') {
										if(row.user.username == getUserName()){
											options = options
											+ '<a onclick="editRelease('
											+ row.id
											+ ')" title="Editar"> <i class="material-icons gris">mode_edit</i></a>'
											+ '<a onclick="confirmDeleteRelease('
											+ row.id
											+ ')" title="Borrar"><i class="material-icons gris">delete</i></a>'
										}
									}
									if($('#isDeveloper').val()){
										options = options
										+ '<a onclick="copyRelease('
										+ row.id
										+ ')" title="Copiar"><i class="material-icons gris">file_copy</i> </a>';
									}

									if (getUserName() == row.system.leader.username) {
										options = options
										+ '<a onclick="openChangeUserModal('
										+ row.id
										+ ')" title="Asignar"><i class="material-icons gris">person_add</i> </a>';
									}

									options = options
									+ '<a onclick="openReleaseTrackingModal('
									+ row.id
									+ ')" title="Rastreo"><i class="material-icons gris" style="font-size: 25px;">location_on</i> </a>';

									options = options
									+ '<a href="'
									+ getCont()
									+ 'release/summary-'
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

function openAddRFCSection() {
	formReleaseDraft.find('#system_idDiv').show();
	formReleaseDraft.find('#divAddObjectOption').hide();
	changeSlide()
}



function closeAddRFCSection() {
	clearValidCreateRelease();
	$("#addRFCSection").hide('slide', {
		direction : 'right'
	}, 400);
	$("#buttonAddRFC").show();
	$("#tableSection").toggle("slide");
	$("#tableSection").insertAfter("#addRFCSection");

}





