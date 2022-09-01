
var $dtTypePetition;
var $mdTypePetition = $('#typePetitionModal');
var $fmTypePetition = $('#typePetitionModalForm');

$(function() {
	activeItemMenu("typePetitionItem", true);
	initDataTable();
	initTypePetitionFormValidation();
});


function initDataTable() {
	$dtTypePetition = $('#typePetitionTable')
	.DataTable(
			{
				lengthMenu : [ [ 10, 25, 50, -1 ],
					[ '10', '25', '50', 'Mostrar todo' ] ],
					"iDisplayLength" : 10,
					"language" : optionLanguaje,
					"iDisplayStart" : 0,
					"sAjaxSource" : getCont() + "admin/typePetitionR4/list",
					"fnServerParams" : function(aoData) {
					},
					"aoColumns" : [
						{
							"mDataProp" : 'code'
						},
						{
							"mDataProp" : 'description'
						},
						{
							render : function(data, type, row, meta) {
								var options = '<div class="iconLineC">';
								if(row.status){
									options += '<a onclick="softDeleteRequest('+row.id+')" title="Editar"><i class="material-icons gris" style="font-size: 30px;">check_circle</i></a>';
									}else{
										options += '<a onclick="softDeleteRequest('+row.id+')" title="Editar"><i class="material-icons gris" style="font-size: 30px;">cancel</i></a>';
									}
								
								options += ' </div>';

								return options;
							}
						},
						{
							render : function(data, type, row, meta) {
								var options = '<div class="iconLineC">';

								options += '<a onclick="showTypePetition('
									+ meta.row
									+ ')" title="Editar"><i class="material-icons gris">mode_edit</i></a>';

			
								options += ' </div>';

								return options;
							}
						} ],
						ordering : false,
			});
}

function softDeleteRequest(index) {	
	console.log(index);
	Swal.fire({
		title: '\u00BFEst\u00e1s seguro que desea modificar el registro?',
		text: 'Esta acci\u00F3n no se puede reversar.',
		...swalDefault
	}).then((result) => {
		if(result.value){
			blockUI();
			$.ajax({
				type : "PUT",
				url : getCont() + "admin/typePetitionR4/"+index ,
				timeout : 60000,
				data : {},
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status)
					$dtTypePetition.ajax.reload();
				},
				error : function(x, t, m) {
					unblockUI();
					
				}
			});
		}
	});
}

function deleteSoftRequest(index) {
			let cont = getCont();
			blockUI();
			$.ajax({
				async : false,
				type : "POST",
				url : cont + "admin/request/" + "softDelete",
				timeout : 60000,
				data : {
					requestId : index
				},
				success : function(response) {
					ajaxDeleteSoftRequest(response, index);
				},
				error : function(x, t, m) {
					notifyAjaxError(x, t, m);
				}
			});
}

function ajaxDeleteSoftRequest(response, index) {
 switch (response.status) {
 case 'success':
	$('#requestTable').find('#softDeleteRequest_'+index).attr("onclick",'softDeleteRequest('+index+','+response.obj+')');
		if(response.obj == true){
		$('#requestTable').find('#softDeleteRequest_'+index).text('check_circle');
		swal("Correcto!", "Solicitud activado correctamente.", "success", 2000)
		}else{
		$('#requestTable').find('#softDeleteRequest_'+index).text('cancel');
		swal("Correcto!", "Requerimiento desactivado correctamente.", "success", 2000)
		}
		break;
		case 'fail':
		swal("Error!", response.exception, "error")
		break;
		case 'exception':
		swal("Exception!", response.exception, "warning")
		break;
		default:
		location.reload();
		}
}
function showTypePetition(index){
	$fmTypePetition.validate().resetForm();
	$fmTypePetition[0].reset();
	var obj = $dtTypePetition.row(index).data();
	$fmTypePetition.find('#sId').val(obj.id);
	$fmTypePetition.find('#sCode').val(obj.code);
	$fmTypePetition.find('#sDescription').val(obj.description);
	$mdTypePetition.find('#update').show();
	$mdTypePetition.find('#save').hide();
	$mdTypePetition.modal('show');
}

function updateTypePetition() {
	if (!$fmTypePetition.valid())
		return;
	Swal.fire({
		title: '\u00BFEst\u00e1s seguro que desea actualizar el registro?',
		text: 'Esta acci\u00F3n no se puede reversar.',
		...swalDefault
	}).then((result) => {
		if(result.value){
			blockUI();
			$.ajax({
				type : "PUT",
				url : getCont() + "admin/typePetitionR4/" ,
				dataType : "json",
				contentType: "application/json; charset=utf-8",
				timeout : 60000,
				data : JSON.stringify({
					id : $fmTypePetition.find('#sId').val(),
					code : $fmTypePetition.find('#sCode').val(),
					description:$fmTypePetition.find('#sDescription').val(),
				}),
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status)
					$dtTypePetition.ajax.reload();
					$mdTypePetition.modal('hide');
				},
				error : function(x, t, m) {
					unblockUI();
					
				}
			});
		}
	});
}


function saveTypePetition() {
	
	if (!$fmTypePetition.valid())
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
				url : getCont() + "admin/typePetitionR4/" ,
				dataType : "json",
				contentType: "application/json; charset=utf-8",
				timeout : 60000,
				data : JSON.stringify({
					code : $fmTypePetition.find('#sCode').val(),
					description:$fmTypePetition.find('#sDescription').val()
				}),
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status)
					$dtTypePetition.ajax.reload();
					$mdTypePetition.modal('hide');
				},
				error : function(x, t, m) {
					unblockUI();
				
				}
			});
		}
	});
}

function deleteTypePetition(index) {
	var obj = $dtTypePetition.row(index).data();
	Swal.fire({
		title: '\u00BFEst\u00e1s seguro que desea eliminar el registro?',
		text: 'Esta acci\u00F3n no se puede reversar.',
		...swalDefault
	}).then((result) => {
		if(result.value){
			blockUI();
			$.ajax({
				type : "DELETE",
				url : getCont() + "admin/typePetitionR4/"+obj.id ,
				timeout : 60000,
				data : {},
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status)
					$dtTypePetition.ajax.reload();
					$mdTypePetition.modal('hide');
				},
				error : function(x, t, m) {
					unblockUI();
					
				}
			});
		}
	});
}

function addTypePetition(){
	$fmTypePetition.validate().resetForm();
	$fmTypePetition[0].reset();
	$fmTypePetition.find('#sEmailId').selectpicker('val',"");
	$mdTypePetition.find('#save').show();
	$mdTypePetition.find('#update').hide();
	$mdTypePetition.modal('show');
}

function closeTypePetition(){
	$mdTypePetition.modal('hide');
}

function initTypePetitionFormValidation() {
	$fmTypePetition.validate({
		rules : {

			'sCode' : {
				required : true,
				minlength : 1,
				maxlength : 20,
			},

			'sDescription' : {
				required : true,
				minlength : 1,
				maxlength : 100,
			}
		},
		messages : {

			'sCode' : {
				required :  "Ingrese un valor",
				minlength : "Ingrese un valor",
				maxlength : "No puede poseer mas de {0} caracteres"
			},
			'sDescription' : {
				required :  "Ingrese un valor",
				minlength : "Ingrese un valor",
				maxlength : "No puede poseer mas de {0} caracteres"
			}
		},
		highlight,
		unhighlight,
		errorPlacement
	});
}