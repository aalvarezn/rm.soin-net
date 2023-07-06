
var $dtTypeChange;
var $mdTypeChange = $('#typeChangeModal');
var $fmTypeChange = $('#typeChangeModalForm');

$(function() {
	activeItemMenu("releaseItem", true);
	initDataTable();
	initTypeChangeFormValidation();
});


function initDataTable() {
	$dtTypeChange = $('#typeChangeTable')
	.DataTable(
			{
				lengthMenu : [ [ 10, 25, 50, -1 ],
					[ '10', '25', '50', 'Mostrar todo' ] ],
					"iDisplayLength" : 10,
					"language" : optionLanguaje,
					"iDisplayStart" : 0,
					"sAjaxSource" : getCont() + "admin/typeChange/list",
					"fnServerParams" : function(aoData) {
					},
					"aoColumns" : [
						{
							"mDataProp" : 'name'
						},
						{
							"mDataProp" : 'description'
						},
						
						{
							render : function(data, type, row, meta) {
								var options = '<div class="iconLineC">';

								options += '<a onclick="showTypeChange('
									+ meta.row
									+ ')" title="Editar"><i class="material-icons gris">mode_edit</i></a>';

			
								options += ' </div>';

								return options;
							}
						} ],
						ordering : false,
			});
}

function showTypeChange(index){
	$fmTypeChange.validate().resetForm();
	$fmTypeChange[0].reset();
	var obj = $dtTypeChange.row(index).data();
	$fmTypeChange.find('#sId').val(obj.id);
	$fmTypeChange.find('#sName').val(obj.name);
	$fmTypeChange.find('#sDescription').val(obj.description);
	$mdTypeChange.find('#update').show();
	$mdTypeChange.find('#save').hide();
	$mdTypeChange.modal('show');
}

function updateTypeChange() {
	if (!$fmTypeChange.valid())
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
				url : getCont() + "admin/typeChange/" ,
				dataType : "json",
				contentType: "application/json; charset=utf-8",
				timeout : 60000,
				data : JSON.stringify({
					id : $fmTypeChange.find('#sId').val(),
					name : $fmTypeChange.find('#sName').val(),
					description:$fmTypeChange.find('#sDescription').val(),
				}),
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status)
					$dtTypeChange.ajax.reload();
					$mdTypeChange.modal('hide');
				},
				error : function(x, t, m) {
					unblockUI();
					
				}
			});
		}
	});
}


function saveTypeChange() {
	
	if (!$fmTypeChange.valid())
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
				url : getCont() + "admin/typeChange/" ,
				dataType : "json",
				contentType: "application/json; charset=utf-8",
				timeout : 60000,
				data : JSON.stringify({
					name : $fmTypeChange.find('#sName').val(),
					description:$fmTypeChange.find('#sDescription').val(),
				}),
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status)
					$dtTypeChange.ajax.reload();
					$mdTypeChange.modal('hide');
				},
				error : function(x, t, m) {
					unblockUI();
				
				}
			});
		}
	});
}

function deleteTypeChange(index) {
	var obj = $dtTypeChange.row(index).data();
	Swal.fire({
		title: '\u00BFEst\u00e1s seguro que desea eliminar el registro?',
		text: 'Esta acci\u00F3n no se puede reversar.',
		...swalDefault
	}).then((result) => {
		if(result.value){
			blockUI();
			$.ajax({
				type : "DELETE",
				url : getCont() + "admin/typeChange/"+obj.id ,
				timeout : 60000,
				data : {},
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status)
					$dtTypeChange.ajax.reload();
					$mdTypeChange.modal('hide');
				},
				error : function(x, t, m) {
					unblockUI();
					
				}
			});
		}
	});
}

function addTypeChange(){
	$fmTypeChange.validate().resetForm();
	$fmTypeChange[0].reset();
	$mdTypeChange.find('#save').show();
	$mdTypeChange.find('#update').hide();
	$mdTypeChange.modal('show');
}

function closeTypeChange(){
	$mdTypeChange.modal('hide');
}

function initTypeChangeFormValidation() {
	$fmTypeChange.validate({
		rules : {

			'sName' : {
				required : true,
				minlength : 1,
				maxlength : 20,
			},

			'sDescription' : {
				required : true,
				minlength : 1,
				maxlength : 20,
			}
		},
		messages : {

			'sName' : {
				required :  "Ingrese un valor",
				minlength : "Ingrese un valor",
				maxlength : "No puede poseer mas de {0} caracteres"
			},
			'sDescription' : {
				required :  "Ingrese un valor",
				minlength : "Ingrese un valor",
				maxlength : "No puede poseer mas de {0} caracteres"
			},
		},
		highlight,
		unhighlight,
		errorPlacement
	});
}