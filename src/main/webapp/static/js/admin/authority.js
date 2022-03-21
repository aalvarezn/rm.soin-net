var $dtAuthority;
var $mdAuthority = $('#authorityModal');
var $fmAuthority = $('#authorityModalForm');

$(function() {
	activeItemMenu("userItem", true);
	initDataTable();
	initAuthorityFormValidation();
});


function initDataTable() {
	$dtAuthority = $('#authorityTable')
	.DataTable(
			{
				lengthMenu : [ [ 10, 25, 50, -1 ],
					[ '10', '25', '50', 'Mostrar todo' ] ],
					"iDisplayLength" : 10,
					"language" : optionLanguaje,
					"iDisplayStart" : 0,
					"sAjaxSource" : getCont() + "admin/authority/list",
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

								options += '<a onclick="showAuthority('
									+ meta.row
									+ ')" title="Editar"><i class="material-icons gris">mode_edit</i></a>';

								options += '<a onclick="deleteAuthority('
									+ meta.row
									+ ')" title="Borrar"><i class="material-icons gris">delete</i></a>';

								options += ' </div>';

								return options;
							}
						} ],
						ordering : false,
			});
}

function showAuthority(index){
	$fmAuthority.validate().resetForm();
	$fmAuthority[0].reset();
	var obj = $dtAuthority.row(index).data();
	$fmAuthority.find('#aId').val(obj.id);
	$fmAuthority.find('#aCode').val(obj.code);
	$fmAuthority.find('#aName').val(obj.name);
	$mdAuthority.find('#update').show();
	$mdAuthority.find('#save').hide();
	$mdAuthority.modal('show');
}

function updateAuthority() {
	if (!$fmAuthority.valid())
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
				url : getCont() + "admin/authority/" ,
				dataType : "json",
				contentType: "application/json; charset=utf-8",
				timeout : 60000,
				data : JSON.stringify({
					id : $fmAuthority.find('#aId').val(),
					code : $fmAuthority.find('#aCode').val(),
					name : $fmAuthority.find('#aName').val(),
				}),
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status)
					$dtAuthority.ajax.reload();
					$mdAuthority.modal('hide');
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


function saveAuthority() {
	if (!$fmAuthority.valid())
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
				url : getCont() + "admin/authority/" ,
				dataType : "json",
				contentType: "application/json; charset=utf-8",
				timeout : 60000,
				data : JSON.stringify({
					code : $fmAuthority.find('#aCode').val(),
					name : $fmAuthority.find('#aName').val(),
				}),
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status)
					$dtAuthority.ajax.reload();
					$mdAuthority.modal('hide');
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

function deleteAuthority(index) {
	var obj = $dtAuthority.row(index).data();
	Swal.fire({
		title: '\u00BFEst\u00e1s seguro que desea eliminar el registro?',
		text: 'Esta acci\u00F3n no se puede reversar.',
		...swalDefault
	}).then((result) => {
		if(result.value){
			blockUI();
			$.ajax({
				type : "DELETE",
				url : getCont() + "admin/authority/"+obj.id ,
				timeout : 60000,
				data : {},
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status)
					$dtAuthority.ajax.reload();
					$mdAuthority.modal('hide');
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

function addAuthority(){
	$fmAuthority.validate().resetForm();
	$fmAuthority[0].reset();
	$mdAuthority.find('#save').show();
	$mdAuthority.find('#update').hide();
	$mdAuthority.modal('show');
}

function closeAuthority(){
	$mdAuthority.modal('hide');
}

function initAuthorityFormValidation() {
	$fmAuthority.validate({
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
				maxlength : "No puede poseer mas de 50 caracteres"
			},
			'aName' : {
				required : "Ingrese un valor",
				minlength : "Ingrese un valor",
				maxlength : "No puede poseer mas de 100 caracteres"
			},
		},
		highlight,
		unhighlight,
		errorPlacement
	});
}