/** Declaración de variables globales del contexto * */
var $dtRFCs;

var $fmRFC = $('#formAddRFC');
var $formChangeUser = $('#changeUserForm');
var $trackingReleaseForm = $('#trackingReleaseForm');

$(function() {
	activeItemMenu("managerRFCItem");
	dropDownChange();
	$("#addRFCSection").hide();
	$fmRFC.find("#sId").selectpicker('val',"");
	$('input[name="daterange"]').daterangepicker(optionRangePicker);
	initRFCTable();
	initRFCFormValidation();
});

function initRFCTable() {
	$dtRFCs = $('#dtRFCs').DataTable(
			{
				lengthMenu : [ [ 10, 25, 50, -1 ],
					[ '10', '25', '50', 'Mostrar todo' ] ],
					"iDisplayLength" : 10,
					"language" : optionLanguaje,
					"iDisplayStart" : 0,
					"processing" : true,
					"serverSide" : true,
					"sAjaxSource" : getCont() + "rfc/list",
					"fnServerParams" : function(aoData) {
					},
					"aoColumns" : [ {
						"mDataProp" : "numRequest"
					}, {
						"mRender" : function(data, type, row, meta) {
							return row.numRequest;
						},
					}, {
						"mRender" : function(data, type, row, meta) {
							if(row.priority)
								return row.priority.name;
							else
								return '';
						},
					}, {
						"mRender" : function(data, type, row, meta) {
							return row.numRequest;
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
							return "---";
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
				}),
				success : function(response) {
					console.log(response.data);
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
function dropDownChange(){

	$('#sId').on('change', function(){
		var sId =$fmRFC.find('#sId').val();
		
		console.log(sId);
		if(sId!=""){
		$.ajax({
			type: 'GET',
			url: getCont() + "rfc/changeProject/"+sId,
			success: function(result) {
				console.log(result.length);
				if(result.length!=0){
					var s = '';
					s+='<option value="">-- Seleccione una opci&oacute;n --</option>';
					for(var i = 0; i < result.length; i++) {
						s += '<option value="' + result[i].codeSiges + '">' + result[i].codeSiges + '</option>';
					}
					console.log(s);
					$('#sigesId').html(s);
					$('#sigesId').prop('disabled', false);
					$('#sigesId').selectpicker('refresh');
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
function resetDrops(){
	$fmRFC.find('#sId').selectpicker('val',  "");
	resetDrop();
}	
function resetDrop(){
	var s = '';
	s+='<option value="">-- Seleccione una opci&oacute;n --</option>';
	$('#sigesId').html(s);
	$('#sigesId').prop('disabled',true);
	$('#sigesId').selectpicker('refresh');
	
}


