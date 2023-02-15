
var $dtPriorityIncidence;
var $mdPriorityIncidence = $('#statusIncidenceModal');
var $fmPriorityIncidence = $('#statusIncidenceModalForm');
var obj=null;
$(function() {
	activeItemMenu("incidenceManagementItem", true);
	initDataTable();
	initPriorityIncidenceFormValidation();
	dropDownChange();
	checkDB();
	// Datetimepicker plugin
	$('.datetimepicker').datetimepicker({
		format: 'HH:mm'
	});
	var prueba='00:00';
	$('.datetimepicker').attr('value', moment('2016-03-12 '+prueba+':00').format('HH:mm'));
});


function initDataTable() {
	$dtPriorityIncidence = $('#priorityIncidenceTable')
	.DataTable(
			{
				lengthMenu : [ [ 10, 25, 50, -1 ],
					[ '10', '25', '50', 'Mostrar todo' ] ],
					"iDisplayLength" : 10,
					"language" : optionLanguaje,
					"iDisplayStart" : 0,
					"sAjaxSource" : getCont() + "systemStatusIn/list",
					"fnServerParams" : function(aoData) {
					},
					"aoColumns" : [
						{
							"mDataProp" : 'system.name'
						},
						{
							"mDataProp" : 'status.name'
						},
						{
							render : function(data, type, row, meta) {
								var options = '<div class="iconLineC">';
								if(row.slaActive){
									options += '<a onclick="updateSLA('+row.id+')" title="Editar"><i class="material-icons gris" style="font-size: 30px;">check_circle</i></a>';
									}else{
										options += '<a onclick="updateSLA('+row.id+')" title="Editar"><i class="material-icons gris" style="font-size: 30px;">cancel</i></a>';
									}
								
								options += ' </div>';

								return options;
							}
						},
						
						{
							render : function(data, type, row, meta) {
								var options = '<div class="iconLineC">';

								options += '<a onclick="showPriorityIncidence('
									+ meta.row
									+ ')" title="Editar"><i class="material-icons gris">mode_edit</i></a>'
									+ '<a onclick="deletePriorityIncidence('
									+ meta.row
									+ ')" title="Borrar"><i class="material-icons gris">delete</i></a>';

			
								options += ' </div>';

								return options;
							}
						} ],
						ordering : false,
			});
}
function updateSLA(index) {	
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
				url : getCont() + "/systemStatusIn/changeSla/"+index ,
				timeout : 60000,
				data : {},
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status)
					$dtPriorityIncidence.ajax.reload();
				},
				error : function(x, t, m) {
					unblockUI();
					
				}
			});
		}
	});
}
function dropDownChange(){
	
	$('#sId').on('change', function(){
		var sId =$fmPriorityIncidence.find('#sId').val();
		console.log(sId+"vacio");
		if(sId!=""){
		$.ajax({
			type: 'GET',
			url: getCont() + "systemStatusIn/changeStatus/"+sId,
			success: function(result) {
				if(result.length!=0){
					var verif=true;
					var s = '';
					s+='<option value="">-- Seleccione una opci&oacute;n --</option>';
					for(var i = 0; i < result.length; i++) {
						s += '<option value="' + result[i].id + '">' + result[i].name + '</option>';
						if(obj!=null){
							if(result[i].id!==obj.status.id&&obj.system.id.toString()!==$fmPriorityIncidence.find('#sId').val()){
								verif=false;
							}
						}
					}
					$('#statusId').html(s);
					$('#statusId').prop('disabled', false);
					$('#save').prop('disabled', false);
					if(obj!==null){
						if(verif){
						$('#statusId').append('<option value="' + obj.status.id + '">' + obj.status.name + '</option>');
						}
					}
					$('#statusId').selectpicker('refresh');
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
function resetDrop(){
	var s = '';
	s+='<option value="">-- Seleccione una opci&oacute;n --</option>';
	$('#statusId').html(s);
	
	if(obj!==null){
		$('#statusId').append('<option value="' + obj.priority.id + '">' + obj.priority.name + '</option>');
		$('#statusId').prop('disabled', false);
	}else{
		$('#statusId').prop('disabled',true);
	}
	$('#save').prop('disabled',true);
	$('#statusId').selectpicker('refresh');
	
}

function checkDB(){
	$("#priorityIncidenceModalForm #sla").change(function() {
		console.log(this.checked);
		if (this.checked) {
			this.value = 1;
			var prueba='00:00';
			$fmPriorityIncidence.find('#time').val(prueba);
			
			$('#timeDiv').attr( "hidden",false);
		
		} else {
			this.value = 0;
			
			$('#timeDiv').attr( "hidden",true);
			
		}
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
				url : getCont() + "systemStatusIn/"+index ,
				timeout : 60000,
				data : {},
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status)
					$dtPriorityIncidence.ajax.reload();
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
		swal("Correcto!", "Requerimiento activado correctamente.", "success", 2000)
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
function showPriorityIncidence(index){
	$fmPriorityIncidence.validate().resetForm();
	$fmPriorityIncidence[0].reset();
	 obj = $dtPriorityIncidence.row(index).data();
		$('#statusId').prop('disabled', true);
	console.log(obj);
	$fmPriorityIncidence.find('#sId').selectpicker('val',obj.system.id);
	var s = '';
	s+='<option value="">-- Seleccione una opci&oacute;n --</option>';
	$('#statusId').html(s);
	 $('#statusId').append('<option value="' + obj.status.id + '">' + obj.status.name + '</option>');
		$('#statusId').selectpicker('val',obj.status.id);
		$('#statusId').selectpicker('refresh');
		$fmPriorityIncidence.find('#id').val(obj.id);
	if(obj.sla==1){
		$('#timeDiv').attr( "hidden",false);
		$fmPriorityIncidence.find('#sla').val(1);
		$('#sla').prop('checked',true);
		$fmPriorityIncidence.find('#time').val(obj.time);
	}else{
		$('#timeDiv').attr( "hidden",true);
		$fmPriorityIncidence.find('#sla').val(0);
		$fmPriorityIncidence.find('#time').val('00:00');
	}
	$mdPriorityIncidence.find('#update').show();
	$mdPriorityIncidence.find('#save').hide();
	$mdPriorityIncidence.modal('show');
	
	
}

function updatePriorityIncidence() {
	console.log($('#sla').prop("checked"));
	if (!$fmPriorityIncidence.valid())
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
				url : getCont() + "systemStatusIn/" ,
				dataType : "json",
				contentType: "application/json; charset=utf-8",
				timeout : 60000,
				data : JSON.stringify({
					id : $fmPriorityIncidence.find('#id').val(),
					systemId : $fmPriorityIncidence.find('#sId').val(),
					statusId:  $fmPriorityIncidence.find('#statusId').val(),
					slaActive:$('#sla').prop("checked")?1:0,
				}),
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status)
					$dtPriorityIncidence.ajax.reload();
					$mdPriorityIncidence.modal('hide');
				},
				error : function(x, t, m) {
					unblockUI();
					
				}
			});
		}
	});
}


function savePriorityIncidence() {

	if (!$fmPriorityIncidence.valid())
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
				url : getCont() + "systemStatusIn/" ,
				dataType : "json",
				contentType: "application/json; charset=utf-8",
				timeout : 60000,
				data : JSON.stringify({
					systemId : $fmPriorityIncidence.find('#sId').val(),
					statusId:  $fmPriorityIncidence.find('#statusId').val(),
					slaActive:$('#sla').prop("checked")?1:0
				}),
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status)
					$dtPriorityIncidence.ajax.reload();
					$mdPriorityIncidence.modal('hide');
				},
				error : function(x, t, m) {
					unblockUI();
				
				}
			});
		}
	});
}

function deletePriorityIncidence(index) {
	var obj1 =$dtPriorityIncidence.row(index).data();
	Swal.fire({
		title: '\u00BFEst\u00e1s seguro que desea eliminar el registro?',
		text: 'Esta acci\u00F3n no se puede reversar.',
		...swalDefault
	}).then((result) => {
		if(result.value){
			blockUI();
			$.ajax({
				type : "DELETE",
				url : getCont() + "systemStatusIn/"+obj1.id ,
				timeout : 60000,
				data : {},
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status)
					$dtPriorityIncidence.ajax.reload();
					$mdPriorityIncidence.modal('hide');
				},
				error : function(x, t, m) {
					unblockUI();
					
				}
			});
		}
	});
}

function addPriorityIncidence(){
	obj=null;
	console.log(obj);
	$fmPriorityIncidence.validate().resetForm();
	$fmPriorityIncidence.find('#sId').selectpicker('val',  "");
	$fmPriorityIncidence.find('#statusId').selectpicker('val',  "");
	$fmPriorityIncidence.find('#sla').val(0);
	$fmPriorityIncidence.find('#time').val('00:00');
	$('#statusId').prop('disabled', true);
	$('#timeDiv').attr( "hidden",true);
	$('#statusId').selectpicker('refresh');
	$fmPriorityIncidence[0].reset();
	$mdPriorityIncidence.find('#save').show();
	$mdPriorityIncidence.find('#update').hide();
	$mdPriorityIncidence.modal('show');
}

function closePriorityIncidence(){
	$mdPriorityIncidence.modal('hide');
}

function initPriorityIncidenceFormValidation() {
	$fmPriorityIncidence.validate({
		rules : {

			'sId' : {
				required : true,
			},
			'statusId' : {
				required : true,
			}
		},
		messages : {

			'sId' : {
				required :  "Ingrese un valor",
			},
			'statusId' : {
				required :  "Ingrese un valor",
			}
		},
		highlight,
		unhighlight,
		errorPlacement
	});
}