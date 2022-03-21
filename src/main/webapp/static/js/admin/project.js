var $dtProject;
var $mdProject = $('#projectModal');
var $fmProject = $('#projectModalForm');

$(function() {
	activeItemMenu("projectItem");
	initDataTable();
	initProjectFormValidation();
});


function initDataTable() {
	$dtProject = $('#projectTable')
	.DataTable(
			{
				lengthMenu : [ [ 10, 25, 50, -1 ],
					[ '10', '25', '50', 'Mostrar todo' ] ],
					"iDisplayLength" : 10,
					"language" : optionLanguaje,
					"iDisplayStart" : 0,
					"sAjaxSource" : getCont() + "admin/project/list",
					"fnServerParams" : function(aoData) {
					},
					"aoColumns" : [
						{
							"mDataProp" : 'code'
						},
						{
							"mDataProp" : 'description'
						},
						{
							render : function(data, type, row, meta) {
								var options = '<div class="iconLine">';

								options += '<a onclick="showProject('
									+ meta.row
									+ ')" title="Editar"><i class="material-icons gris">mode_edit</i></a>';

								options += '<a onclick="deleteProject('
									+ meta.row
									+ ')" title="Borrar"><i class="material-icons gris">delete</i></a>';

								options += ' </div>';

								return options;
							}
						} ],
						ordering : false,
			});
}

function showProject(index){
	$fmProject.validate().resetForm();
	$fmProject[0].reset();
	var obj = $dtProject.row(index).data();
	$fmProject.find('#pId').val(obj.id);
	$fmProject.find('#pCode').val(obj.code);
	$fmProject.find('#pDescription').val(obj.description);
	$mdProject.find('#update').show();
	$mdProject.find('#save').hide();
	$mdProject.modal('show');
}

function updateProject() {
	if (!$fmProject.valid())
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
				url : getCont() + "admin/project/" ,
				dataType : "json",
				contentType: "application/json; charset=utf-8",
				timeout : 60000,
				data : JSON.stringify({
					id : $fmProject.find('#pId').val(),
					code : $fmProject.find('#pCode').val(),
					description : $fmProject.find('#pDescription').val(),
				}),
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status)
					$dtProject.ajax.reload();
					$mdProject.modal('hide');
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


function saveProject() {
	if (!$fmProject.valid())
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
				url : getCont() + "admin/project/" ,
				dataType : "json",
				contentType: "application/json; charset=utf-8",
				timeout : 60000,
				data : JSON.stringify({
					code : $fmProject.find('#pCode').val(),
					description : $fmProject.find('#pDescription').val(),
				}),
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status)
					$dtProject.ajax.reload();
					$mdProject.modal('hide');
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

function deleteProject(index) {
	var obj = $dtProject.row(index).data();
	Swal.fire({
		title: '\u00BFEst\u00e1s seguro que desea eliminar el registro?',
		text: 'Esta acci\u00F3n no se puede reversar.',
		...swalDefault
	}).then((result) => {
		if(result.value){
			blockUI();
			$.ajax({
				type : "DELETE",
				url : getCont() + "admin/project/"+obj.id ,
				timeout : 60000,
				data : {},
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status)
					$dtProject.ajax.reload();
					$mdProject.modal('hide');
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

function addProject(){
	$fmProject.validate().resetForm();
	$fmProject[0].reset();
	$mdProject.find('#save').show();
	$mdProject.find('#update').hide();
	$mdProject.modal('show');
}

function closeProject(){
	$mdProject.modal('hide');
}

function initProjectFormValidation() {
	$fmProject.validate({
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