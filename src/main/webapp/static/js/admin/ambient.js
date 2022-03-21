
var $dtAmbient;
var $mdAmbient = $('#ambientModal');
var $fmAmbient = $('#ambientModalForm');

$(function() {
	activeItemMenu("ambientItem", true);
	initDataTable();
	initAmbientFormValidation();
});


function initDataTable() {
	$dtAmbient = $('#ambientTable')
	.DataTable(
			{
				lengthMenu : [ [ 10, 25, 50, -1 ],
					[ '10', '25', '50', 'Mostrar todo' ] ],
					"iDisplayLength" : 10,
					"language" : optionLanguaje,
					"iDisplayStart" : 0,
					"sAjaxSource" : getCont() + "admin/ambient/list",
					"fnServerParams" : function(aoData) {
					},
					"aoColumns" : [
						{
							"mDataProp" : 'code'
						},
						{
							"mDataProp" : 'name'
						},
						{
							render : function(data, type, row, meta) {
								var options = '<div class="iconLine">';

								options += '<a onclick="showAmbient('
									+ meta.row
									+ ')" title="Editar"><i class="material-icons gris">mode_edit</i></a>';

								options += '<a onclick="deleteAmbient('
									+ meta.row
									+ ')" title="Borrar"><i class="material-icons gris">delete</i></a>';

								options += ' </div>';

								return options;
							}
						} ],
						ordering : false,
			});
}

function showAmbient(index){
	$fmAmbient.validate().resetForm();
	$fmAmbient[0].reset();
	var obj = $dtAmbient.row(index).data();
	$fmAmbient.find('#aId').val(obj.id);
	$fmAmbient.find('#aCode').val(obj.code);
	$fmAmbient.find('#aName').val(obj.name);
	$mdAmbient.find('#update').show();
	$mdAmbient.find('#save').hide();
	$mdAmbient.modal('show');
}

function updateAmbient() {
	if (!$fmAmbient.valid())
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
				url : getCont() + "admin/ambient/" ,
				dataType : "json",
				contentType: "application/json; charset=utf-8",
				timeout : 60000,
				data : JSON.stringify({
					id : $fmAmbient.find('#aId').val(),
					code : $fmAmbient.find('#aCode').val(),
					name : $fmAmbient.find('#aName').val(),
				}),
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status)
					$dtAmbient.ajax.reload();
					$mdAmbient.modal('hide');
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


function saveAmbient() {
	if (!$fmAmbient.valid())
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
				url : getCont() + "admin/ambient/" ,
				dataType : "json",
				contentType: "application/json; charset=utf-8",
				timeout : 60000,
				data : JSON.stringify({
					code : $fmAmbient.find('#aCode').val(),
					name : $fmAmbient.find('#aName').val(),
				}),
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status)
					$dtAmbient.ajax.reload();
					$mdAmbient.modal('hide');
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

function deleteAmbient(index) {
	var obj = $dtAmbient.row(index).data();
	Swal.fire({
		title: '\u00BFEst\u00e1s seguro que desea eliminar el registro?',
		text: 'Esta acci\u00F3n no se puede reversar.',
		...swalDefault
	}).then((result) => {
		if(result.value){
			blockUI();
			$.ajax({
				type : "DELETE",
				url : getCont() + "admin/ambient/"+obj.id ,
				timeout : 60000,
				data : {},
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status)
					$dtAmbient.ajax.reload();
					$mdAmbient.modal('hide');
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

function addAmbient(){
	$fmAmbient.validate().resetForm();
	$fmAmbient[0].reset();
	$mdAmbient.find('#save').show();
	$mdAmbient.find('#update').hide();
	$mdAmbient.modal('show');
}

function closeAmbient(){
	$mdAmbient.modal('hide');
}

function initAmbientFormValidation() {
	$fmAmbient.validate({
		rules : {
			'aCode' : {
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
			'aCode' : {
				required :  "Ingrese un valor",
				minlength : "Ingrese un valor",
				maxlength : "No puede poseer mas de {0} caracteres"
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