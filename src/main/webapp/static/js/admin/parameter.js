var $dtParameter;
var $mdParameter = $('#parameterModal');
var $fmParameter = $('#parameterModalForm');
var obj;

$(function() {
	activeItemMenu("configurationItem", true);
	initDataTable();
	initParameterFormValidation();
});


function initDataTable(){
	$dtParameter= $('#parameterTable')
	.DataTable(
			{
				lenghtMenu:[[10,25,50,-1], 
					['10','25','50','Mostrar todo']],
					"language" : optionLanguaje,
					"iDisplayStart" : 0,
					"sAjaxSource" : getCont() + "admin/parameter/list",
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
							"mDataProp" : 'paramValue'
						},
						{
							"mDataProp" : 'date',render:function(data){
							      return moment(data).format('DD/MM/YYYY HH:mm:ss');
							}
						},
						{
							render : function(data, type, row, meta) {
								var options = '<div class="iconLine">';

								options += '<a onclick="showParameter('
									+ meta.row
									+ ')" title="Editar"><i class="material-icons gris">mode_edit</i></a>';


								options += ' </div>';

								return options;
							}
						} ],
						ordering : false,
				
	});
}

function showParameter(index){
	$fmParameter.validate().resetForm();
	$fmParameter[0].reset();
	obj=$dtParameter.row(index).data();
	$fmParameter.find('#paramId').val(obj.id);
	$fmParameter.find('#description').val(obj.description);
	$fmParameter.find('#paramValue').val(obj.paramValue);
	$mdParameter.find('#update').show();
	$mdParameter.find('#save').hide();
	$mdParameter.modal('show');
}

function updateParameter(){
	if(!$fmParameter.valid())
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
				url : getCont() + "admin/parameter/" ,
				dataType : "json",
				contentType: "application/json; charset=utf-8",
				timeout : 60000,
				data : JSON.stringify({
					id : $fmParameter.find('#paramId').val(),
					description : $fmParameter.find('#description').val(),
					paramValue : $fmParameter.find('#paramValue').val(),
					code: obj.code,
				}),
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status)
					$dtParameter.ajax.reload();
					$mdParameter.modal('hide');
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


function closeParameter(){
	$mdParameter.modal('hide');
}

function initParameterFormValidation() {
	$fmParameter.validate({
		rules : {
			'description' : {
				required : true,
				minlength : 1,
				maxlength : 50,
			},
			'paramValue' : {
				required : true,
				minlength : 1,
				maxlength : 100
			},
		},
		messages : {
			'description' : {
				required :  "Ingrese un valor",
				minlength : "Ingrese un valor",
				maxlength : "No puede poseer mas de 50 caracteres"
			},
			'paramValue' : {
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
