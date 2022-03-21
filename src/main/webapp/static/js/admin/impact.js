var $dtImpact;
var $mdImpact = $('#impactModal');
var $fmImpact = $('#impactModalForm');

$(function() {
	activeItemMenu("catalogueItem", true);
	initDataTable();
	initImpactFormValidation();
});


function initDataTable() {
	$dtImpact = $('#impactTable')
	.DataTable(
			{
				lengthMenu : [ [ 10, 25, 50, -1 ],
					[ '10', '25', '50', 'Mostrar todo' ] ],
					"iDisplayLength" : 10,
					"language" : optionLanguaje,
					"iDisplayStart" : 0,
					"sAjaxSource" : getCont() + "admin/impact/list",
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
								var options = '<div class="iconLine">';

								options += '<a onclick="showImpact('
									+ meta.row
									+ ')" title="Editar"><i class="material-icons gris">mode_edit</i></a>';

								options += '<a onclick="deleteImpact('
									+ meta.row
									+ ')" title="Borrar"><i class="material-icons gris">delete</i></a>';

								options += ' </div>';

								return options;
							}
						} ],
						ordering : false,
			});
}

function showImpact(index){
	$fmImpact.validate().resetForm();
	$fmImpact[0].reset();
	var obj = $dtImpact.row(index).data();
	$fmImpact.find('#iId').val(obj.id);
	$fmImpact.find('#iName').val(obj.name);
	$fmImpact.find('#iDescription').val(obj.description);
	$mdImpact.find('#update').show();
	$mdImpact.find('#save').hide();
	$mdImpact.modal('show');
}

function updateImpact() {
	if (!$fmImpact.valid())
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
				url : getCont() + "admin/impact/" ,
				dataType : "json",
				contentType: "application/json; charset=utf-8",
				timeout : 60000,
				data : JSON.stringify({
					id : $fmImpact.find('#iId').val(),
					name : $fmImpact.find('#iName').val(),
					description : $fmImpact.find('#iDescription').val(),
				}),
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status)
					$dtImpact.ajax.reload();
					$mdImpact.modal('hide');
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


function saveImpact() {
	if (!$fmImpact.valid())
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
				url : getCont() + "admin/impact/" ,
				dataType : "json",
				contentType: "application/json; charset=utf-8",
				timeout : 60000,
				data : JSON.stringify({
					name : $fmImpact.find('#iName').val(),
					description : $fmImpact.find('#iDescription').val(),
				}),
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status)
					$dtImpact.ajax.reload();
					$mdImpact.modal('hide');
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

function deleteImpact(index) {
	var obj = $dtImpact.row(index).data();
	Swal.fire({
		title: '\u00BFEst\u00e1s seguro que desea eliminar el registro?',
		text: 'Esta acci\u00F3n no se puede reversar.',
		...swalDefault
	}).then((result) => {
		if(result.value){
			blockUI();
			$.ajax({
				type : "DELETE",
				url : getCont() + "admin/impact/"+obj.id ,
				timeout : 60000,
				data : {},
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status)
					$dtImpact.ajax.reload();
					$mdImpact.modal('hide');
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

function addImpact(){
	$fmImpact.validate().resetForm();
	$fmImpact[0].reset();
	$mdImpact.find('#save').show();
	$mdImpact.find('#update').hide();
	$mdImpact.modal('show');
}

function closeImpact(){
	$mdImpact.modal('hide');
}

function initImpactFormValidation() {
	$fmImpact.validate({
		rules : {
			'iName' : {
				required : true,
				minlength : 1,
				maxlength : 50
			},
			'iDescription' : {
				required : true,
				minlength : 1,
				maxlength : 250,
			},
		},
		messages : {
			'iName' : {
				required :  "Ingrese un valor",
				minlength : "Ingrese un valor",
				maxlength : "No puede poseer mas de 50 caracteres"
			},
			'iDescription' : {
				required : "Ingrese un valor",
				minlength : "Ingrese un valor",
				maxlength : "No puede poseer mas de 250 caracteres"
			},
		},
		highlight,
		unhighlight,
		errorPlacement
	});
}