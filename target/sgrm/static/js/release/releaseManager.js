var releaseTable = $('#dtReleases').DataTable();

$(function() {
	loadTableRelease('systemRelease');

});

function loadTableRelease(nameTable) {
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
							"targets" : [ 4 ],
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
						"sAjaxSource" : getCont() + "release/" + nameTable,
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
										var date = new Date(row.createDate);
										var month = date.getMonth() + 1;
										return date.getDate()
												+ "/"
												+ (month.length > 1 ? month
														: "0" + month) + "/"
												+ date.getFullYear();
									}
								},
								{
									"mDataProp" : "status.name",
								},
								{
									mRender : function(data, type, row) {
										var options = '<div class="iconLine"> ';
										options = options
												+ '<a href="'
												+ getCont()
												+ 'release/summary-'
												+ row.id
												+ '" title="Administrar"><i class="material-icons gris">settings_applications</i></a>'
										options = options
												+ '<a href="'
												+ getCont()
												+ 'release/summary-'
												+ row.id
												+ '" title="Resumen"><i class="material-icons gris">info</i></a>'

												+ ' </div>';
										return options;
									}
								} ],
						responsive : true,
						ordering : false,
						info : true
					});
}