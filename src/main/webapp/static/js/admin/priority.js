var $dtPriority;
var $mdPriority = $('#priorityModal');
var $fmPriority = $('#priorityModalForm');

$(function() {
	activeItemMenu("catalogueItem", true);
	initDataTable();
	initPriorityFormValidation();
});


function initDataTable() {
	$dtPriority = $('#priorityTable')
	.DataTable(
			{
				lengthMenu : [ [ 10, 25, 50, -1 ],
					[ '10', '25', '50', 'Mostrar todo' ] ],
					"iDisplayLength" : 10,
					"language" : optionLanguaje,
					"iDisplayStart" : 0,
					"sAjaxSource" : getCont() + "admin/priority/list",
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

								options += '<a onclick="showPriority('
									+ meta.row
									+ ')" title="Editar"><i class="material-icons gris">mode_edit</i></a>';

								options += '<a onclick="deletePriority('
									+ meta.row
									+ ')" title="Borrar"><i class="material-icons gris">delete</i></a>';

								options += ' </div>';

								return options;
							}
						} ],
						ordering : false,
			});
}

function showPriority(index){
	$fmPriority.validate().resetForm();
	$fmPriority[0].reset();
	var obj = $dtPriority.row(index).data();
	console.log(obj);
	$fmPriority.find('#pId').val(obj.id);
	$fmPriority.find('#pName').val(obj.name);
	$fmPriority.find('#pDescription').val(obj.description);
	$mdPriority.find('#update').show();
	$mdPriority.find('#save').hide();
	$mdPriority.modal('show');
}

function updatePriority() {
	if (!$fmPriority.valid())
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
				url : getCont() + "admin/priority/" ,
				dataType : "json",
				contentType: "application/json; charset=utf-8",
				timeout : 60000,
				data : JSON.stringify({
					id : $fmPriority.find('#pId').val(),
					name : $fmPriority.find('#pName').val(),
					description : $fmPriority.find('#pDescription').val(),
				}),
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status)
					$dtPriority.ajax.reload();
					$mdPriority.modal('hide');
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


function savePriority() {
	if (!$fmPriority.valid())
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
				url : getCont() + "admin/priority/" ,
				dataType : "json",
				contentType: "application/json; charset=utf-8",
				timeout : 60000,
				data : JSON.stringify({
					name : $fmPriority.find('#pName').val(),
					description : $fmPriority.find('#pDescription').val(),
				}),
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status)
					$dtPriority.ajax.reload();
					$mdPriority.modal('hide');
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

function deletePriority(index) {
	var obj = $dtPriority.row(index).data();
	Swal.fire({
		title: '\u00BFEst\u00e1s seguro que desea eliminar el registro?',
		text: 'Esta acci\u00F3n no se puede reversar.',
		...swalDefault
	}).then((result) => {
		if(result.value){
			blockUI();
			$.ajax({
				type : "DELETE",
				url : getCont() + "admin/priority/"+obj.id ,
				timeout : 60000,
				data : {},
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status)
					$dtPriority.ajax.reload();
					$mdPriority.modal('hide');
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

function addPriority(){
	$fmPriority.validate().resetForm();
	$fmPriority[0].reset();
	$mdPriority.find('#save').show();
	$mdPriority.find('#update').hide();
	$mdPriority.modal('show');
}

function closePriority(){
	$mdPriority.modal('hide');
}

function initPriorityFormValidation() {
	$fmPriority.validate({
		rules : {
			'pName' : {
				required : true,
				minlength : 1,
				maxlength : 50
			},
			'pDescription' : {
				minlength : 1,
				maxlength : 250,
			},
		},
		messages : {
			'pName' : {
				required :  "Ingrese un valor",
				minlength : "Ingrese un valor",
				maxlength : "No puede poseer mas de 50 caracteres"
			},
			'pDescription' : {
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