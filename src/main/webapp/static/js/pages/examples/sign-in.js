$(function () {
    $('#sign_in').validate({
        highlight: function (input) {
            $(input).parents('.form-line').addClass('error');
        },
        unhighlight: function (input) {
            $(input).parents('.form-line').removeClass('error');
        },
        errorPlacement: function (error, element) {
            $(element).parents('.input-group').append(error);
        }
    });

    dropDownChange();
});


function dropDownChange(){

	$('#projectId').on('change', function(){
		var projectId =$('#projectId').val();
		console.log(getCont() + "/getSystem/"+projectId);
		if(projectId!="0"){
		$.ajax({
			type: 'GET',
			url: getCont() + "/getSystem/"+projectId,
			success: function(result) {
				if(result.length!=0){
					var s = '';
					s+='<option value="">Seleccione un sistema</option>';
					for(var i = 0; i < result.length; i++) {
						s += '<option value="' + result[i].id + '">' + result[i].name + '</option>';
					}
					$('#systemId').html(s);
					$('#systemId').prop('disabled', false);
					//$('#systemId').selectpicker('refresh');
				}else{
					resetDrop();
				}
				
				
			}
		});
		
		}else{
			resetDropPriorityMain();
		}
		
	});
}

function resetDropPriorityMain(){

	var s = '';
	s+='<option value="0">Seleccione un sistema</option>';
	$('#systemId').html(s);
	$('#systemId').prop('disabled',true);
	//$('#systemId').selectpicker('refresh');

}

function resetDrop(){

	var s = '';
	s+='<option value="0">Sin sistemas disponibles/option>';
	$('#systemId').html(s);
	$('#systemId').prop('disabled',true);
	//$('#systemId').selectpicker('refresh');

}
function getCont() {
	var fullUrl = window.location.pathname.substring(0, window.location.pathname.indexOf("/", 2));
	return fullUrl;
}