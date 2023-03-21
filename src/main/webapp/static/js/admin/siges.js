
var $dtSiges;
var $mdSiges = $('#sigesModal');
var $fmSiges = $('#sigesModalForm');

$(function() {
	activeItemMenu("systemItem", true);
	initDataTable();
	initSigesFormValidation();
});


function initDataTable() {
	$dtSiges = $('#sigesTable')
	.DataTable(
			{
				lengthMenu : [ [ 10, 25, 50, -1 ],
					[ '10', '25', '50', 'Mostrar todo' ] ],
					"iDisplayLength" : 10,
					"language" : optionLanguaje,
					"iDisplayStart" : 0,
					"sAjaxSource" : getCont() + "admin/siges/list",
					"fnServerParams" : function(aoData) {
					},
					"aoColumns" : [
						{
							"mDataProp" : 'codeSiges'
						},
						{
							"mDataProp" : 'system.code'
						},
						{
							render : function(data, type, row, meta) {
								var options = '<div class="iconLineC">';

								options += '<a onclick="showSiges('
									+ meta.row
									+ ')" title="Editar"><i class="material-icons gris">mode_edit</i></a>';

								options += '<a onclick="deleteSiges('
									+ meta.row
									+ ')" title="Borrar"><i class="material-icons gris">delete</i></a>';

								options += ' </div>';

								return options;
							}
						} ],
						ordering : false,
			});
}

function showSiges(index){
	$fmSiges.validate().resetForm();
	$fmSiges[0].reset();
	var obj = $dtSiges.row(index).data();
	$fmSiges.find('#sId').val(obj.id);
	$fmSiges.find('#sCode').val(obj.codeSiges);
	$fmSiges.find('#sSystemId').selectpicker('val',obj.system.id);
	$fmSiges.find('#sEmailId').selectpicker('val',obj.emailTemplate.id);
	$mdSiges.find('#update').show();
	$mdSiges.find('#save').hide();
	$mdSiges.modal('show');
}

function updateSiges() {
	if (!$fmSiges.valid())
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
				url : getCont() + "admin/siges/" ,
				dataType : "json",
				contentType: "application/json; charset=utf-8",
				timeout : 60000,
				data : JSON.stringify({
					id : $fmSiges.find('#sId').val(),
					codeSiges : $fmSiges.find('#sCode').val(),
					systemId : $fmSiges.find('#sSystemId').val(),
					emailTemplateId :$fmSiges.find('#sEmailId').val()
				}),
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status)
					$dtSiges.ajax.reload();
					$mdSiges.modal('hide');
				},
				error : function(x, t, m) {
					unblockUI();
					
				}
			});
		}
	});
}


function saveSiges() {
	
	if (!$fmSiges.valid())
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
				url : getCont() + "admin/siges/" ,
				dataType : "json",
				contentType: "application/json; charset=utf-8",
				timeout : 60000,
				data : JSON.stringify({
					codeSiges : $fmSiges.find('#sCode').val(),
					systemId : $fmSiges.find('#sSystemId').val(),
					emailTemplateId :$fmSiges.find('#sEmailId').val()
				}),
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status)
					$dtSiges.ajax.reload();
					$mdSiges.modal('hide');
				},
				error : function(x, t, m) {
					unblockUI();
				
				}
			});
		}
	});
}

function deleteSiges(index) {
	var obj = $dtSiges.row(index).data();
	Swal.fire({
		title: '\u00BFEst\u00e1s seguro que desea eliminar el registro?',
		text: 'Esta acci\u00F3n no se puede reversar.',
		...swalDefault
	}).then((result) => {
		if(result.value){
			blockUI();
			$.ajax({
				type : "DELETE",
				url : getCont() + "admin/siges/"+obj.id ,
				timeout : 60000,
				data : {},
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status)
					$dtSiges.ajax.reload();
					$mdSiges.modal('hide');
				},
				error : function(x, t, m) {
					unblockUI();
					
				}
			});
		}
	});
}

function addSiges(){
	$fmSiges.validate().resetForm();
	$fmSiges[0].reset();
	$fmSiges.find('#sSystemId').selectpicker('val',"");
	$fmSiges.find('#sEmailId').selectpicker('val',"");
	$mdSiges.find('#save').show();
	$mdSiges.find('#update').hide();
	$mdSiges.modal('show');
}

function closeSiges(){
	$mdSiges.modal('hide');
}

function initSigesFormValidation() {
	$fmSiges.validate({
		rules : {
			'sCode' : {
				required : true,
				minlength : 1,
				maxlength : 100,
			},
			'sSystemId' : {
				required : true,
			},
			'sEmailId' : {
				required : true,
			}
		},
		messages : {
			'sCode' : {
				required :  "Ingrese un valor",
				minlength : "Ingrese un valor",
				maxlength : "No puede poseer mas de {0} caracteres"
			},
			'sSystemId' : {
				required : "Seleccione un valor"
			},
			'sEmailId' : {
				required : "Seleccione un valor"
			},
		},
		highlight,
		unhighlight,
		errorPlacement
	});
}