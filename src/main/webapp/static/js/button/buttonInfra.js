var $dtRequests;

var $formChangeStatus = $('#changeStatusForm');
var $formChangeUser = $('#changeUserForm');
var $trackingRequestForm = $('#trackingRequestForm');
var switchStatus=false;
$(document).ready(function() {
	// initImpactFormValidation
	activeItemMenu("managerRequestItem");
	initRFCTable();
});

$('#tableFilters #systemId').change(function() {
	$dtRequests.ajax.reload();
});

function refreshTable(){
	$dtRequests.ajax.reload();
}

function initRFCTable() {
	$dtRequests = $('#dtButtonInfra').DataTable(
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
					"sAjaxSource" : getCont() + "buttonInfra/list",
					"fnServerParams" : function(aoData) {
						aoData.push(
								{"name": "systemId", "value": $('#tableFilters #systemId').children("option:selected").val()}
						);
					},
					"aoColumns" : [
						{
							"mDataProp" : "id",
						},
						
						{
							
							"mDataProp" : "system.name"
						},
						{
						
						"mDataProp" : "name"
					},
					{
						
						"mDataProp" : "description"
					}
					 , {
						"mRender" : function(data, type, row, meta) {
							var options = '<div class="iconLine">';
							options += '<button title="Accionar bot\u00F3n" type="button" class="btn btn-primary setIcon" onclick="actionButton('+row.id+')">';
							options += '<span><i class="material-icons m-t--2" style="font-size: 25px;">offline_pin</i></span>Accionar';
							options += '</button>';
							options += '</div>';

							return options;

						},
					} ],
					ordering : false,
			});
}

function confirmAction(element) {
	// $('#deleteReleaseModal').modal('show');
	Swal.fire({
		title: '\u00BFEst\u00e1s seguro que desea realizar esta acci\u00F3n?',
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

function actionButton(element) {
	blockUI();
	var cont = getCont();
	$.ajax({
		type : "GET",
		url : cont + "buttonInfra/" + "action /" + element,
		timeout : 60000,
		data : {},
		success : function(response) {
			switch (response.status) {
			case 'success':
				swal("Correcto!", "La accion se ha sido realizado exitosamente.",
						"success", 2000)
						
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

