
var $dtAction;
var $mdAction = $('#actionEnvironmentModal');
var $fmAction = $('#actionEnvironmentModalForm');

$(function() {
	activeItemMenu("actionItem", true);
	initDataTable();
	initActionFormValidation();
});


function initDataTable() {
	$dtAction = $('#actionEnvironmentTable')
	.DataTable(
			{
				lengthMenu : [ [ 10, 25, 50, -1 ],
					[ '10', '25', '50', 'Mostrar todo' ] ],
					"iDisplayLength" : 10,
					"language" : optionLanguaje,
					"iDisplayStart" : 0,
					"sAjaxSource" : getCont() + "admin/action/list",
					"fnServerParams" : function(aoData) {
					},
					"aoColumns" : [
						{
							"mDataProp" : 'name'
						},
						{
							"mDataProp" : 'system.name'
						},
						{
							render : function(data, type, row, meta) {
								var options = '<div class="iconLineC">';

								options += '<a onclick="showAction('
									+ meta.row
									+ ')" title="Editar"><i class="material-icons gris">mode_edit</i></a>';

								options += '<a onclick="deleteAction('
									+ meta.row
									+ ')" title="Borrar"><i class="material-icons gris">delete</i></a>';

								options += ' </div>';

								return options;
							}
						} ],
						ordering : false,
			});
}

function showAction(index){
	$fmAction.validate().resetForm();
	$fmAction[0].reset();
	var obj = $dtAction.row(index).data();
	$fmAction.find('#aId').val(obj.id);
	$fmAction.find('#aSystemId').selectpicker('val', obj.system.id);
	$fmAction.find('#aName').val(obj.name);
	$mdAction.find('#update').show();
	$mdAction.find('#save').hide();
	$mdAction.modal('show');
}

function updateAction() {
	if (!$fmAction.valid())
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
				url : getCont() + "admin/action/" ,
				dataType : "json",
				contentType: "application/json; charset=utf-8",
				timeout : 60000,
				data : JSON.stringify({
					id : $fmAction.find('#aId').val(),
					systemId : $fmAction.find('#aSystemId').val(),
					name : $fmAction.find('#aName').val(),
				}),
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status)
					$dtAction.ajax.reload();
					$mdAction.modal('hide');
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


function saveAction() {
	if (!$fmAction.valid())
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
				url : getCont() + "admin/action/" ,
				dataType : "json",
				contentType: "application/json; charset=utf-8",
				timeout : 60000,
				data : JSON.stringify({
					systemId : $fmAction.find('#aSystemId').val(),
					name : $fmAction.find('#aName').val(),
				}),
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status)
					$dtAction.ajax.reload();
					$mdAction.modal('hide');
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

function deleteAction(index) {
	var obj = $dtAction.row(index).data();
	Swal.fire({
		title: '\u00BFEst\u00e1s seguro que desea eliminar el registro?',
		text: 'Esta acci\u00F3n no se puede reversar.',
		...swalDefault
	}).then((result) => {
		if(result.value){
			blockUI();
			$.ajax({
				type : "DELETE",
				url : getCont() + "admin/action/"+obj.id ,
				timeout : 60000,
				data : {},
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status)
					$dtAction.ajax.reload();
					$mdAction.modal('hide');
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

function addAction(){
	$fmAction.validate().resetForm();
	$fmAction[0].reset();
	$fmAction.find('#aSystemId').selectpicker('val', "");
	$mdAction.find('#save').show();
	$mdAction.find('#update').hide();
	$mdAction.modal('show');
}

function closeAction(){
	$mdAction.modal('hide');
}

function initActionFormValidation() {
	$fmAction.validate({
		rules : {
			'aSystemId' : {
				required : true,
				minlength : 1,
				maxlength : 50,
			},
			'aName' : {
				required : true,
				minlength : 1,
				maxlength : 100
			},
		},
		messages : {
			'aSystemId' : {
				required :  "Ingrese un valor",

			},
			'aName' : {
				required : "Ingrese un valor",
				minlength : "Ingrese un valor",
				maxlength : "No puede poseer mas de {0} caracteres"
			},
		},
		highlight,
		unhighlight,
		errorPlacement
	});
}

