var $dtEnvironment;
var $mdEnvironment = $('#environmentModal');
var $fmEnvironment = $('#environmentModalForm');


$(function() {
	activeItemMenu("systemItem", true);
	initSystems();
	$mdEnvironment.find('input[type="checkbox"]').change(function() {
		if (this.checked) {
			$(this).val(1);
		} else {
			$(this).val(0);
		}
	});
	initDataTable();
	initEnvironmentFormValidation();
});

function initDataTable() {
	$dtEnvironment = $('#environmentTable')
	.DataTable(
			{
				lengthMenu : [ [ 10, 25, 50, -1 ],
					[ '10', '25', '50', 'Mostrar todo' ] ],
					"iDisplayLength" : 10,
					"language" : optionLanguaje,
					"iDisplayStart" : 0,
					"sAjaxSource" : getCont() + "admin/environment/list",
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
								if(!row.external){
									options+='<i class="material-icons gris" style="font-size: 30px;">cancel</i>';
								}else{
									options+='<i class="material-icons gris" style="font-size: 30px;">check_circle</i>';
								}
								options+='</div>';
								return options;
							}
						},
						{
							render : function(data, type, row, meta) {
								var options = '<div class="iconLineC">';

								options += '<a onclick="showEnvironment('
									+ meta.row
									+ ')" title="Editar"><i class="material-icons gris">mode_edit</i></a>';

								

								options += ' </div>';

								return options;
							}
						} ],
						ordering : false,
			});
}

function showEnvironment(index){
	$fmEnvironment.validate().resetForm();
	$fmEnvironment[0].reset();
	$('.nav-tabs a[href="#tabHome"]').tab('show');
	var obj = $dtEnvironment.row(index).data();
	$fmEnvironment.find('#eId').val(obj.id);
	$fmEnvironment.find('#eName').val(obj.name);
	$fmEnvironment.find('#eDescription').val(obj.description);
	$fmEnvironment.find('#eExternal').prop('checked', obj.external);
	$('#systemGroups').multiSelect('deselect_all');
	obj.systems.forEach(function (element) {
		$('#systemGroups').multiSelect('select', element.name);
	});
	$('#systemGroups').multiSelect("refresh");
	$mdEnvironment.find('#update').show();
	$mdEnvironment.find('#save').hide();
	$mdEnvironment.modal('show');
}

function updateEnvironment() {
	if (!$fmEnvironment.valid())
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
				url : getCont() + "admin/environment/" ,
				dataType : "json",
				contentType: "application/json; charset=utf-8",
				timeout : 60000,
				data : JSON.stringify({
					id : $fmEnvironment.find('#eId').val(),
					name : $fmEnvironment.find('#eName').val(),
					description : $fmEnvironment.find('#eDescription').val(),
					external :$fmEnvironment.find('#eExternal').is(':checked'),
					strSystems :$fmEnvironment.find('#systemGroups').val()
					
				}),
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status)
					$dtEnvironment.ajax.reload();
					$mdEnvironment.modal('hide');
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


function saveEnvironment() {
	if (!$fmEnvironment.valid())
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
				url : getCont() + "admin/environment/" ,
				dataType : "json",
				contentType: "application/json; charset=utf-8",
				timeout : 60000,
				data : JSON.stringify({
					name : $fmEnvironment.find('#eName').val(),
					description : $fmEnvironment.find('#eDescription').val(),
					external :$fmEnvironment.find('#eExternal').is(':checked'),
					strSystems :$fmEnvironment.find('#systemGroups').val()
				}),
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status)
					$dtEnvironment.ajax.reload();
					$mdEnvironment.modal('hide');
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
/*
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
					console.log(x);
					console.log(t);
					console.log(m);
				}
			});
		}
	});
}
*/
function addEnvironment(){
	$fmEnvironment.validate().resetForm();
	$fmEnvironment[0].reset();
	$('.nav-tabs a[href="#tabHome"]').tab('show');
	$('#systemGroups').multiSelect('deselect_all');
	$('#systemGroups').multiSelect("refresh");
	$mdEnvironment.find('#save').show();
	$mdEnvironment.find('#update').hide();
	$mdEnvironment.modal('show');
}

function closeEnvironment(){
	$mdEnvironment.modal('hide');
}

function initEnvironmentFormValidation() {
	$fmEnvironment.validate({
		rules : {
			'eName' : {
				required : true,
				minlength : 1,
				maxlength : 100,
			},
			'eDescription' : {
				required : true,
				minlength : 1,
				maxlength : 100,
			},
		},
		messages : {
			'eName' : {
				required :  "Ingrese un valor",
				minlength : "Ingrese un valor",
				maxlength : "No puede poseer mas de {0} caracteres"
			},
			'eDescription' : {
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
function initSystems(){
	$('#systemGroups').multiSelect(
			{
				selectableHeader: "<div class='custom-header'>Sistemas</div>",
				selectionHeader: "<div class='custom-header'>Sistemas asignados</div>",
				afterSelect : function(values) {
					$fmEnvironment.find("#systemGroups option[id='" + values + "']").attr("selected","selected");
				},
				afterDeselect : function(values) {
					$fmEnvironment.find("#systemGroups option[id='" + values +"']").removeAttr('selected');
				}
			});
	$fmEnvironment.find("#ms-systemGroups").find(".ms-selectable").before('<label for="name">Sistemas</label>');
	$fmEnvironment.find("#ms-systemGroups").find(".ms-selection").before('<label for="name">Sistemas Asignados</label>');
}




