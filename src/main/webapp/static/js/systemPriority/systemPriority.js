
var $dtPriorityIncidence;
var $mdPriorityIncidence = $('#priorityIncidenceModal');
var $fmPriorityIncidence = $('#priorityIncidenceModalForm');
var obj=null;
$(function() {
	activeItemMenu("ticketsItem", true);
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
					"sAjaxSource" : getCont() + "systemPriority/list",
					"fnServerParams" : function(aoData) {
					},
					"aoColumns" : [
						{
							"mDataProp" : 'system.name'
						},
						{
							"mDataProp" : 'priority.name'
						},
						{
							render : function(data, type, row, meta) {
								if(row.sla==0){
									return 'Sin temporizador'
								}else{
									return row.time;
								}
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

function dropDownChange(){
	
	$('#sId').on('change', function(){
		var sId =$fmPriorityIncidence.find('#sId').val();
		console.log(sId+"vacio");
		if(sId!=""){
		$.ajax({
			type: 'GET',
			url: getCont() + "systemPriority/changePriority/"+sId,
			success: function(result) {
				if(result.length!=0){
					var s = '';
					s+='<option value="">-- Seleccione una opci&oacute;n --</option>';
					for(var i = 0; i < result.length; i++) {
						s += '<option value="' + result[i].id + '">' + result[i].name + '</option>';
					}
					$('#priorityId').html(s);
					$('#priorityId').prop('disabled', false);
					$('#save').prop('disabled', false);
					if(obj!==null){
						$('#priorityId').append('<option value="' + obj.priority.id + '">' + obj.priority.name + '</option>');
						console.log("no estoy nulo");
					}
					$('#priorityId').selectpicker('refresh');
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
	$('#priorityId').html(s);
	
	if(obj!==null){
		$('#priorityId').append('<option value="' + obj.priority.id + '">' + obj.priority.name + '</option>');
		$('#priorityId').prop('disabled', false);
	}else{
		$('#priorityId').prop('disabled',true);
	}
	$('#save').prop('disabled',true);
	$('#priorityId').selectpicker('refresh');
	
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
				url : getCont() + "systemPriority/"+index ,
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
	console.log(obj);
	$fmPriorityIncidence.find('#sId').selectpicker('val',obj.system.id);
	var s = '';
	s+='<option value="">-- Seleccione una opci&oacute;n --</option>';
	$('#priorityId').html(s);
	 $('#priorityId').append('<option value="' + obj.priority.id + '">' + obj.priority.name + '</option>');
		$('#priorityId').selectpicker('val',obj.priority.id);
		$('#priorityId').selectpicker('refresh');
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
				url : getCont() + "systemPriority/" ,
				dataType : "json",
				contentType: "application/json; charset=utf-8",
				timeout : 60000,
				data : JSON.stringify({
					id : $fmPriorityIncidence.find('#id').val(),
					systemId : $fmPriorityIncidence.find('#sId').val(),
					priorityId:  $fmPriorityIncidence.find('#priorityId').val(),
					sla:$fmPriorityIncidence.find('#sla').val(),
					time:$fmPriorityIncidence.find('#time').val()
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
				url : getCont() + "systemPriority/" ,
				dataType : "json",
				contentType: "application/json; charset=utf-8",
				timeout : 60000,
				data : JSON.stringify({
					systemId : $fmPriorityIncidence.find('#sId').val(),
					priorityId:  $fmPriorityIncidence.find('#priorityId').val(),
					sla:$fmPriorityIncidence.find('#sla').val(),
					time:$fmPriorityIncidence.find('#time').val()
					
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
				url : getCont() + "systemPriority/"+obj1.id ,
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
	$fmPriorityIncidence.find('#priorityId').selectpicker('val',  "");
	$fmPriorityIncidence.find('#sla').val(0);
	$fmPriorityIncidence.find('#time').val('00:00');
	$('#priorityId').prop('disabled', true);
	$('#timeDiv').attr( "hidden",true);
	$('#priorityId').selectpicker('refresh');
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
			'priorityId' : {
				required : true,
			}
		},
		messages : {

			'sId' : {
				required :  "Ingrese un valor",
			},
			'priorityId' : {
				required :  "Ingrese un valor",
			}
		},
		highlight,
		unhighlight,
		errorPlacement
	});
}