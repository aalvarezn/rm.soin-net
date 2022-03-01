var $dtRisk;
var $mdRisk = $('#riskModal');
var $fmRisk = $('#riskModalForm');

$(function() {
	activeItemMenu("catalogueItem", true);
	initDataTable();
	initRiskFormValidation();
});


function initDataTable() {
	$dtRisk = $('#riskTable')
	.DataTable(
			{
				lengthMenu : [ [ 10, 25, 50, -1 ],
					[ '10', '25', '50', 'Mostrar todo' ] ],
					"iDisplayLength" : 10,
					"language" : optionLanguaje,
					"iDisplayStart" : 0,
					"sAjaxSource" : getCont() + "admin/risk/list",
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

								options += '<a onclick="showRisk('
									+ meta.row
									+ ')" title="Editar"><i class="material-icons gris">mode_edit</i></a>';

								options += '<a onclick="deleteRisk('
									+ meta.row
									+ ')" title="Borrar"><i class="material-icons gris">delete</i></a>';

								options += ' </div>';

								return options;
							}
						} ],
						ordering : false,
			});
}

function showRisk(index){
	$fmRisk.validate().resetForm();
	$fmRisk[0].reset();
	var obj = $dtRisk.row(index).data();
	$fmRisk.find('#rId').val(obj.id);
	$fmRisk.find('#rName').val(obj.name);
	$fmRisk.find('#rDescription').val(obj.description);
	$mdRisk.find('#update').show();
	$mdRisk.find('#save').hide();
	$mdRisk.modal('show');
}

function updateRisk() {
	if (!$fmRisk.valid())
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
				url : getCont() + "admin/risk/" ,
				dataType : "json",
				contentType: "application/json; charset=utf-8",
				timeout : 60000,
				data : JSON.stringify({
					id : $fmRisk.find('#rId').val(),
					name : $fmRisk.find('#rName').val(),
					description : $fmRisk.find('#rDescription').val(),
				}),
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status)
					$dtRisk.ajax.reload();
					$mdRisk.modal('hide');
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


function saveRisk() {
	if (!$fmRisk.valid())
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
				url : getCont() + "admin/risk/" ,
				dataType : "json",
				contentType: "application/json; charset=utf-8",
				timeout : 60000,
				data : JSON.stringify({
					name : $fmRisk.find('#rName').val(),
					description : $fmRisk.find('#rDescription').val(),
				}),
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status)
					$dtRisk.ajax.reload();
					$mdRisk.modal('hide');
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

function deleteRisk(index) {
	var obj = $dtRisk.row(index).data();
	Swal.fire({
		title: '\u00BFEst\u00e1s seguro que desea eliminar el registro?',
		text: 'Esta acci\u00F3n no se puede reversar.',
		...swalDefault
	}).then((result) => {
		if(result.value){
			blockUI();
			$.ajax({
				type : "DELETE",
				url : getCont() + "admin/risk/"+obj.id ,
				timeout : 60000,
				data : {},
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status)
					$dtRisk.ajax.reload();
					$mdRisk.modal('hide');
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

function addRisk(){
	$fmRisk.validate().resetForm();
	$fmRisk[0].reset();
	$mdRisk.find('#save').show();
	$mdRisk.find('#update').hide();
	$mdRisk.modal('show');
}

function closeRisk(){
	$mdRisk.modal('hide');
}

function initRiskFormValidation() {
	$fmRisk.validate({
		rules : {
			'rName' : {
				required : true,
				minlength : 1,
				maxlength : 50
			},
			'rDescription' : {
				required : true,
				minlength : 1,
				maxlength : 250,
			},
		},
		messages : {
			'rName' : {
				required :  "Ingrese un valor",
				minlength : "Ingrese un valor",
				maxlength : "No puede poseer mas de 50 caracteres"
			},
			'rDescription' : {
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