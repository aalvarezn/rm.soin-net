
var $dtbutton;
var $mdbutton = $('#buttonModal');
var $fmbutton = $('#buttonModalForm');

$(function() {
	
	activeItemMenu("managerButtonItem");
	initDataTable();
	initbuttonFormValidation();
});


function initDataTable() {
	$dtbutton = $('#buttonTable')
	.DataTable(
			{
				lengthMenu : [ [ 10, 25, 50, -1 ],
					[ '10', '25', '50', 'Mostrar todo' ] ],
					"iDisplayLength" : 10,
					"language" : optionLanguaje,
					"iDisplayStart" : 0,
					"sAjaxSource" : getCont() + "management/buttonInfra/list",
					"fnServerParams" : function(aoData) {
					},
					"aoColumns" : [
						{
							"mDataProp" : 'name'
						},
						{
							"mDataProp" : 'rute'
						},
						{
							"mDataProp" : 'description'
						},
						{
							"mDataProp" : 'system.name'
						},
						{
							render : function(data, type, row, meta) {
								var options = '<div class="iconLineC">';

								options += '<a onclick="showbutton('
									+ meta.row
									+ ')" title="Editar"><i class="material-icons gris">mode_edit</i></a>';

								options += '<a onclick="deletebutton('
									+ meta.row
									+ ')" title="Borrar"><i class="material-icons gris">delete</i></a>';

								options += ' </div>';

								return options;
							}
						} ],
						ordering : false,
			});
}

function showbutton(index){
	$fmbutton.validate().resetForm();
	$fmbutton[0].reset();
	var obj = $dtbutton.row(index).data();
	$fmbutton.find('#sId').val(obj.id);
	$fmbutton.find('#sName').val(obj.name);
	$fmbutton.find('#sRute').val(obj.rute);
	$mdbutton.find('#largeModalLabel').text("Editar Boton Infra");
	$fmbutton.find('#sDescription').val(obj.description);
	$fmbutton.find('#systemId').selectpicker('val',obj.system.id);
	$('#systemId').prop('disabled', true);
	$mdbutton.find('#update').show();
	$mdbutton.find('#save').hide();
	$mdbutton.modal('show');
}

function updatebutton() {
	if (!$fmbutton.valid())
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
				url : getCont() + "management/buttonInfra/" ,
				dataType : "json",
				contentType: "application/json; charset=utf-8",
				timeout : 60000,
				data : JSON.stringify({
					id : $fmbutton.find('#sId').val(),
					name : $fmbutton.find('#sName').val(),
					systemId :$fmbutton.find('#systemId').val(),
					rute :$fmbutton.find('#sRute').val(),
					description:$fmbutton.find('#sDescription').val(),
				}),
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status)
					$dtbutton.ajax.reload();
					$mdbutton.modal('hide');
				},
				error : function(x, t, m) {
					unblockUI();
					
				}
			});
		}
	});
}


function savebutton() {
	
	if (!$fmbutton.valid())
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
				url : getCont() + "management/buttonInfra/" ,
				dataType : "json",
				contentType: "application/json; charset=utf-8",
				timeout : 60000,
				data : JSON.stringify({
					name : $fmbutton.find('#sName').val(),
					rute : $fmbutton.find('#sRute').val(),
					systemId :$fmbutton.find('#systemId').val(),
					description:$fmbutton.find('#sDescription').val(),
				}),
				success : function(response) {
					unblockUI();
					if(response.status==="error"){
						notifyMs(response.message, response.status)
					}else{
						notifyMs(response.message, response.status)
						$dtbutton.ajax.reload();
						$mdbutton.modal('hide');
					}
					
				},
				error : function(x, t, m) {
					unblockUI();
				
				}
			});
		}
	});
}

function deletebutton(index) {
	var obj = $dtbutton.row(index).data();
	Swal.fire({
		title: '\u00BFEst\u00e1s seguro que desea eliminar el registro?',
		text: 'Esta acci\u00F3n no se puede reversar.',
		...swalDefault
	}).then((result) => {
		if(result.value){
			blockUI();
			$.ajax({
				type : "DELETE",
				url : getCont() + "management/buttonInfra/"+obj.id ,
				timeout : 60000,
				data : {},
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status)
					$dtbutton.ajax.reload();
					$mdbutton.modal('hide');
				},
				error : function(x, t, m) {
					unblockUI();
					
				}
			});
		}
	});
}

function addbutton(){
	$fmbutton.validate().resetForm();
	$fmbutton[0].reset();
	$fmbutton.find('#systemId').selectpicker('val',"");
	$mdbutton.find('#save').show();
	$mdbutton.find('#update').hide();
	$mdbutton.find('#largeModalLabel').text("Agregar Boton Infra");
	$('#systemId').prop('disabled', false);
	$mdbutton.modal('show');
}

function closebutton(){
	$mdbutton.modal('hide');
}

function initbuttonFormValidation() {
	$fmbutton.validate({
		rules : {
			'sRute' : {
				required : true,
				minlength : 1,
				maxlength : 100,
			},
			'sName' : {
				required : true,
				minlength : 1,
				maxlength : 50,
			},
			'sDescription' : {
				minlength : 1,
				maxlength : 100,
			},
			'systemId':{
				required:true,
				remote: {
                    url: getCont() + 'management/buttonInfra/validateSystemId', // Cambia esta URL al endpoint de tu servidor
                    type: 'post', // Método HTTP que envía la solicitud (POST o GET)
                    data: {
                        systemId: function() {
                            return $('#systemId').val(); // Valor del campo 'systemId'
                        },
                        sId: function() {
                            return $('#sId').val(); 
                    },
                    dataFilter: function(response) {
                        // Procesar la respuesta del servidor (true o false)
                        if (response === "true") {
                            return true; // El systemId es válido
                        } else {
                            return false; // El systemId ya está en uso
                        }
                    }
			}
				}
		}
		},
		messages : {
			'sRute' : {
				required :  "Ingrese un valor",
				minlength : "Ingrese un valor",
				maxlength : "No puede poseer mas de {0} caracteres"
			},
			'sName' : {
				required :  "Ingrese un valor",
				minlength : "Ingrese un valor",
				maxlength : "No puede poseer mas de {0} caracteres"
			},
			'sDescription' : {
				minlength : "Ingrese un valor",
				maxlength : "No puede poseer mas de {0} caracteres"
			},
			'systemId':{
				required :  "Ingrese un valor",
				 remote: "Este ID ya esta en uso. Por favor, elija otro."
			}
		},
		highlight,
		unhighlight,
		errorPlacement
	});
}