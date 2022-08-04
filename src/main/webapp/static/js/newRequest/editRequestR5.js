var $requestEditForm = $('#generateRequestForm');
var $dataRelease = [];
let $dataReleaseCheck = [];
var origForm=null;
var $dtUser;
var $trackingRFCForm = $('#trackingReleaseForm');
var $treeForm = $('#treeForm');
var network = null;
var nodes = [];
var edges = [];
$(function() {

	activeItemMenu("requestItem");
	 $('#systemId').selectpicker('val',$('#systemInfoId').val());
	 initRequestFormValidation();
	initUserTable();
	
	 $('#releaseTable tbody').on( 'click', 'tr', function () {
	      if ( $(this).hasClass('selected') ) {
	            $(this).removeClass('selected');

	        }
	        else {
	        	// $dtRFCs.$('tr.selected').removeClass('selected');
	            $(this).addClass('selected');
	          
	        }
	    } );
	 
	 $('#releaseTableAdd tbody').on( 'click', 'tr', function () {
	      if ( $(this).hasClass('selected') ) {
	            $(this).removeClass('selected');

	        }
	        else {
	        	// $dtRFCsAdd.$('tr.selected').removeClass('selected');
	            $(this).addClass('selected');
	          
	        }
	    } );

	 
	 $('.tagInit').tagsInput();
	  
	$('.nav-tabs > li a[title]').tooltip();
	// Wizard
	$('.stepper a[data-toggle="tab"]').on('show.bs.tab', function(e) {
		if($dataRelease!=0){
			 $('#releaseTableAdd').dataTable().fnClearTable();
			 $('#releaseTableAdd').dataTable().fnAddData($dataRelease);
		}else{
			$('#releaseTableAdd').dataTable().fnClearTable();
		}
		
		
		var $target = $(e.target);
		if ($target.parent().hasClass('disabled')) {
			return false;
		}
		if(formHasChanges()){
			sendPartialRequest();
		}
	});
	
	
	var toggle = $('.picker-switch a[data-action="togglePicker"]');
	
	$(".next-step").click(function(e) {
		var $active = $('.stepper li.active');
		while($active.next().hasClass('disabled')){
			$active = $active.next();
			e.stopPropagation();
		}
		nextTab($active);
		$('html, body').animate({scrollTop: '0px'}, 300);
	});

	$(".prev-step").click(function(e) {
		var $active = $('.stepper li.active');
		while($active.prev().hasClass('disabled')){
			$active = $active.prev();
			e.stopPropagation();
		}
		prevTab($active);
		$('html, body').animate({scrollTop: '0px'}, 300);
	});
	origForm = $requestEditForm.serialize();
	$('.tagInit').tagsInput({
		width:'400px'
	});

	const data=$requestEditForm.find('#ambientData').val();
	console.log(data);
	const splitString = data.split(",");
	var ambients="";
	for(x=0;splitString.length>x;x++){
		if(splitString[x]==="Desarrollo"){
			$('#ambient1').prop('checked',true);
		}
		if(splitString[x]==="QA"){
			$('#ambient2').prop('checked',true);
		}
		if(splitString[x]==="Pre-Produccion"){
			$('#ambient3').prop('checked',true);
		}
		if(splitString[x]==="Produccion"){
			$('#ambient4').prop('checked',true);
		}else if(splitString[x]!==""){
			$('#ambient5').prop('checked',true);
			$('#ambient6').addTag(splitString[x]);
			if(ambients!=""){
				ambients+=","+splitString[x];
			}else{
				ambients=splitString[x];
			}
			
			
		}
	}
	if(ambients!=""){
		$('#ambient6').importTags(ambients);
		$('#ambient6_tagsinput').css("display","block")
	}
	console.log(splitString);

	
});

function changeAttributte(checkElement){
		if(checkElement.checked){
			$('#ambient6_tagsinput').slideToggle();
		}else{
			$('#ambient6_tagsinput').slideToggle();
			
				$('#ambient6').importTags('');
			
			//$('#ambient6_tagsinput').removeTag('awd');
		}

}
function nextTab(elem) {
	$(elem).next().find('a[data-toggle="tab"]').tab('show');
}

function prevTab(elem) {
	$(elem).prev().find('a[data-toggle="tab"]').tab('show');
}



function formHasChanges(){
	
	if($requestEditForm.serialize() === origForm  ){
		return false;
	}else{
		return true;
	}
}

function previewRequest() {
	$('#previewRequestModal').modal('show');
}

function closePreviewRequest() {
	$('#previewRequestModal').modal('hide');
}

function compareArrays(arr1, arr2) {
	return $(arr1).not(arr2).length == 0 && $(arr2).not(arr1).length == 0;
};
function sendPartialRequest() {
	var form = "#generateReleaseForm";
	var ambients="";
	if ($('#ambient1').is(":checked"))
	{
		if(ambients==""){
			ambients=$('#ambient1').val();
		}
		
	}
	if ($('#ambient2').is(":checked"))
	{
		if(ambients==""){
			ambients=$('#ambient2').val();
		}else{
			ambients+=","+$('#ambient2').val();
		}
		
	}
	if ($('#ambient3').is(":checked"))
	{
		if(ambients==""){
			ambients=$('#ambient3').val();
		}else{
			ambients+=","+$('#ambient3').val();
		}
	}
	if ($('#ambient4').is(":checked"))
	{
		if(ambients==""){
			ambients=$('#ambient4').val();
		}else{
			ambients+=","+$('#ambient4').val();
		}
	}
	
	if ($('#ambient5').is(":checked"))
	{
		if(ambients==""){
			ambients=$('#ambient6').val();
		}else{
			ambients+=","+$('#ambient6').val();
		}
	}
	changeSaveButton(true);
	$.ajax({
		// async : false,
		type : "PUT",
		url : getCont() + "request/saveRequest",
		timeout: 60000,
		dataType : "json",
		contentType: "application/json; charset=utf-8",
		data : JSON.stringify({
			// Informacion general
			id : $requestEditForm.find('#rfcId').val(),
			senders:$requestEditForm.find('#senders').val(),
			message:$requestEditForm.find('#messagePer').val(),
		}),
		success : function(response) {
			// responseAjaxSendPartialRelease(response);
			changeSaveButton(false);
			origForm = $requestEditForm.serialize();
			reloadPreview();
		},
		error: function(x, t, m) {
			notifyAjaxError(x, t, m);
			changeSaveButton(false);
		}
	});
}

function changeSaveButton(save){
	if(save){
		$requestEditForm.find('#btnSave').find('#btnText').text('GUARDANDO');
		$requestEditForm.find('#btnSave').find('#btnIcon').text('cached');
	}else{
		$requestEditForm.find('#btnSave').find('#btnText').text('GUARDAR');
		$requestEditForm.find('#btnSave').find('#btnIcon').text('check_box');
	}
}
$('#systemId').change(function() {
	
	$dtRFCs.ajax.reload();
});
function initData(){
	var idRFC=$('#rfcId').val();
	
		$.ajax({
			type: 'GET',
			url: getCont() + "rfc/getRFC-"+idRFC,
			success: function(result) {
				if(result.length!=0){
					$dataRelease=result.releases;
					$dataReleaseCheck=$dataRelease.slice();
				}else{
					
				}
				
				
			}
		});
		
		
	
}

function initUserTable() {
	var idRequest=$('#requestId').val();
	$dtUser = $('#userTable').DataTable(
			{
				
				'columnDefs' : [ {
					'visible' : false,
					'targets' : [ 0]
				} ],
				lengthMenu : [ [ 10, 25, 50, -1 ],
					[ '10', '25', '50', 'Mostrar todo' ] ],
					"iDisplayLength" : 10,
					"language" : optionLanguaje,
					"iDisplayStart" : 0,
					"processing" : true,
					"serverSide" : true,
					"sAjaxSource" : getCont() + "request/listUser/"+idRequest,
					"aoColumns" : [
						{
							"mDataProp" : "id",
						},
						{
							
							"mDataProp" : "name"
						},
						{
						
						"mDataProp" : "email"
					},
					{
						
						"mDataProp" : "type"
					},
					{
						
						"mDataProp" : "permissions"
					}
					,
					{
						
						"mDataProp" : "ambient.name"
					},
					{
						
						"mDataProp" : "espec"
					},
					
					{
						"mRender" : function(data, type, row, meta) {
							var options = '<div class="iconLineC">';
							
									options = options

									+ '<a onclick="confirmDeleteUser('
									+ row.id
									+ ')" title="Borrar"><i class="material-icons gris">delete</i></a>'
								
							
							return options;
						},
					} ],
					ordering : false,
			});
}



function confirmDeleteUser(index){
	Swal.fire({
		title: '\u00BFEst\u00e1s seguro que desea eliminar el registro?',
		text: 'Esta acci\u00F3n no se puede reversar.',
		...swalDefault
	}).then((result) => {
		if(result.value){
			blockUI();
			$.ajax({
				type : "DELETE",
				url : getCont() + "request/deleteUser/"+index,
				timeout : 60000,
				data : {},
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status)
					$dtUser.ajax.reload();
					//$mdSiges.modal('hide');
				},
				error : function(x, t, m) {
					unblockUI();
					
				}
			});
		}
	});
}


function addUser(){
	var permissions="";
	if ($('#permission1').is(":checked"))
	{
		if(permissions==""){
			permissions=$('#permission1').val();
		}
		
	}
	if ($('#permission2').is(":checked"))
	{
		if(permissions==""){
			permissions=$('#permission2').val();
		}else{
			permissions+=","+$('#permission2').val();
		}
		
	}
	if ($('#permission3').is(":checked"))
	{
		if(permissions==""){
			permissions=$('#permission3').val();
		}else{
			permissions+=","+$('#permission3').val();
		}
	}
	if ($('#permission4').is(":checked"))
	{
		if(permissions==""){
			permissions=$('#permission4').val();
		}else{
			permissions+=","+$('#permission4').val();
		}
	}
	if (!$requestEditForm.valid())
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
				url : getCont() + "request/addUser" ,
				dataType : "json",
				contentType: "application/json; charset=utf-8",
				timeout : 60000,
				data : JSON.stringify({
					name : $requestEditForm.find('#name').val(),
					email : $requestEditForm.find('#email').val(),
					type:$("input[type='radio'][name='type']:checked").val(),
					permissions:permissions,
					espec:$requestEditForm.find('#espec').val(),
					ambientId:$requestEditForm.find('#ambientId').val(),
					requestBaseId:$('#requestId').val(),
				}),
				success : function(response) {
					unblockUI();
					notifyMs(response.message, response.status);
					/*window.location = getCont()
					+ "request/editRequest-"
					+ response.data;
					*/
					resetSpaces();
					$dtUser.ajax.reload();
					reloadPreview();
					//$mdImpact.modal('hide');
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

function resetSpaces(){
	$requestEditForm.find('#name').val('');
	$requestEditForm.find('#email').val('');
	$("input:radio").attr("checked", false);
	$('#permission1').prop('checked',false);
	$('#permission2').prop('checked',false);
	$('#permission3').prop('checked',false);
	$('#permission4').prop('checked',false);
	$requestEditForm.find('#espec').val('');
	$requestEditForm.find('#ambientId').selectpicker('val',"");
}
function initRequestFormValidation() {
	$requestEditForm.validate({
		rules : {
			'name':{
				required : true,
				maxlength : 20,
			},
			'email':{
				required :true,
				maxlength : 20,
			},
			'ambientId':{
				required:true,		
			},
			'espec':{
				required:true,
				minlength : 2,			
			},
			'type':{
				required:true,		
			}
			,
			'permission':{
				required:true,		
			}
				
			
		},
		messages : {
			
			'name' : {
				required : "Ingrese un valor",
				maxlength : "No puede poseer mas de {0} caracteres"
			},
			'email' : {
				required : "Ingrese un valor",
				maxlength : "No puede poseer mas de {0} caracteres"
					
			},

			'ambientId' : {
				required : "Seleccione un valor"
			},
			'espec' : {
				required : "Ingrese un valor",
				minlength : "Debe ser un minimo de dos caracteres"
			},
			'type':{
				required:"Se debe seleccionar una casilla",		
			},
			'permission':{
				required:"Se debe seleccionar al menos una opcion",		
			}
		},
		highlight,
		unhighlight,
		errorPlacement
	});
}
function addAfterCheck(){
var dataRFC = $dtRFCs.rows('.selected').data();
	var contador=0;
	
	var verification=true;
	if(dataRFC!=undefined){
		
	
	if($dataRelease.length!=0){

		for(var x=0;x<dataRFC.length;x++){
			
			var data= dataRFC[x];

			$dataRelease.forEach(function(element){
				
				if(element.id==data.id){
					
					verification=false;
					contador=0;
				}
				
			});
			if(verification){
				var description=data.description;
				let text ='{"id":'+(data.id).toString()+',"releaseNumber":"'+(data.releaseNumber).toString()+'","description":"'+description.replace(/\n|\r/g, "")+'","user":'+JSON.stringify(data.user)+',"haveSQL":'+data.haveSQL+',"haveDependecy":"'+data.haveDependecy+'","createDate":'+data.createDate+',"status":{"name":"'+(data.status.name).toString()+'"},"tracking":'+JSON.stringify(data.tracking)+'}';
				const obj = JSON.parse(text);
				$dataRelease.unshift(obj);
				 $('#releaseTableAdd').dataTable().fnClearTable();
				 $('#releaseTableAdd').dataTable().fnAddData($dataRelease);
				 contador=1;
			}else{
				
			}
			
			verification=true;
			
		}
		$dtRFCs.$('tr.selected').removeClass('selected');
		 if(contador=1){
			 notifyMs('Se agregaron correctamente los releases','success');
		 }
		 else{
			 notifyMs('Estos releases ya fueron agregados','error');
		 }
	}else{
		for(var x=0;x<dataRFC.length;x++){
			
			var data= dataRFC[x];
			
			$dataRelease.forEach(function(element){
				
				if(element.id==data.id){
					
					verification=false;
					contador=0;
				}
				
			});
			if(verification){
				
				var description=""+data.description+"";
				let text ='{"id":'+(data.id).toString()+',"releaseNumber":"'+(data.releaseNumber).toString()+'","description":"'+description.replace(/\n|\r/g, "")+'","user":'+JSON.stringify(data.user)+',"haveSQL":'+data.haveSQL+',"haveDependecy":"'+data.haveDependecy+'","createDate":'+data.createDate+',"status":{"name":"'+(data.status.name).toString()+'"},"tracking":'+JSON.stringify(data.tracking)+'}';
				const obj = JSON.parse(text);
				$dataRelease.unshift(obj);
				 $('#releaseTableAdd').dataTable().fnClearTable();
				 $('#releaseTableAdd').dataTable().fnAddData($dataRelease);
				contador=1;
			}else{
				contador=0;
			}
			
			verification=true;
			
		}
		 $dtRFCs.$('tr.selected').removeClass('selected');
		 if(contador=1){
			 notifyMs('Se agregaron correctamente los releases','success');
		 }
		 else{
			 notifyMs('Estos releases ya fueron agregados','error');
		 }
	}
		return;
	}
}

function reloadPreview() {
	var src = $("#tinySummary").attr("src")
	$("#tinySummary").attr("src", src)
}

function initTableAdd(){
	$dtRFCsAdd=$('#releaseTableAdd').DataTable({
		
		'columnDefs' : [ {
			'visible' : false,
			'targets' : [ 0]
		}, 
		{
			'visible' : false,
			'targets' : [ 0]
		}, 
		
		],
			"iDisplayLength" : 15,
			"language" : {
				"emptyTable" : "No existen registros",
				"zeroRecords" : "No existen registros",
				"processing" : "Cargando",
			},
		  		data: $dataRelease,
		  		"aoColumns" : [
					{
						"mDataProp" : "id",
					},
					{
						"mDataProp" : "releaseNumber",
					},{
						"mDataProp" : "description",
					},
					{
						"mDataProp" : "user.fullName",
					},
					{
						
						"mDataProp" : "status.name"
					}
					,{
						"mRender" : function(data, type, row, meta) {
							return moment(row.createDate).format('DD/MM/YYYY h:mm:ss a');
						},
					},
					{
						"mRender" : function(data, type, row, meta) {
							var options = '<div class="iconLineR">';
							if(row.haveSQL){
								options = options
								+ '<a onclick="" title="Tiene base de datos"><i class="material-icons verde" style="font-size: 25px;margin-right: 5px;"><span class="material-symbols-outlined">database</span></i></a>';
							}
							if(row.haveDependecy>0){
								options = options
								+ '<a onclick="" title="Tiene dependencias"><i class="material-icons naranja" style="font-size: 25px;"><span class="material-symbols-outlined">warning</span></i></a>';
							}
							options = options
							+ '<a onclick="openRFCTrackingModal('
							+ row.id+
							',1'
							+ ')" title="\u00C1rbol de dependencias"><i class="material-icons gris" style="font-size: 25px;">location_on</i> </a>';
							
							options = options
							+ '<a onclick="openTreeModal('
							+ row.id+
							',1'
							+ ')" title="Rastreo"><i class="material-icons gris" style="font-size: 25px;">device_hub</i> </a>';
							
							options = options
							+ '<a href="'
							+ getCont()
							+ 'release/summary-'
							+ row.id
							+ '" target="_blank" title="Resumen"><i class="material-icons gris">info</i></a> </div>';
							
							return options;
							return options;
						},
						sWidth: '30px'
					} 
					
					
					],
		  });
}
function dropDownChange(){

	$('#systemId').on('change', function(){
		var sId =$rfcEditForm.find('#systemId').val();
		if(sId!=""){
			$dtRFCs = $('#releaseTable').DataTable(
					{
						lengthMenu : [ [ 10, 25, 50, -1 ],
							[ '10', '25', '50', 'Mostrar todo' ] ],
							"iDisplayLength" : 10,
							"language" : optionLanguaje,
							"iDisplayStart" : 0,
							"processing" : true,
							"serverSide" : true,
							"sAjaxSource" : getCont() + "rfc/changeRelease",
							"fnServerParams" : function(aoData) {
							},
							"aoColumns" : [ {
								"mDataProp" : "releaseNumber"
							},
							{
								"mRender" : function(data, type, row, meta) {
									let options = 'a';
									return options;
								}
							}
							
							],
							ordering : false,
							select:{
								style:'multi'
							}
					});
		}else{
			resetDrop();
		}
		
	});
}

function removeSelectedData(){
	var dataTableRelease = $dtRFCsAdd.rows('.selected').data();
	if(dataTableRelease.length!=0){
	if($dataRelease.length!=0){

		for(var x=0;x<dataTableRelease.length;x++){
			
			var data= dataTableRelease[x];
			$dataRelease.forEach(function(element,index){
				
				if(element.id==data.id){
					
					$dataRelease.splice(index,1); 
		        	if($dataRelease.length==0){
		    
		        		$dataRelease=[];
		        		 $('#releaseTableAdd').dataTable().fnClearTable();
		        		 notifyMs('Se removieron correctamente los releases','success');
		        		 return;
		        	}
		        	 $('#releaseTableAdd').dataTable().fnClearTable();
		    		 $('#releaseTableAdd').dataTable().fnAddData($dataRelease);
		    		 notifyMs('Se removieron correctamente los releases','success');
		            return false; 
					
				}
				
			});
		
		}
		$dtRFCs.$('tr.selected').removeClass('selected');
	}else{
		swal("Sin selecci\u00F3n!", "No se ha seleccionado ning\u00FAn release para remover",
		"warning");
		return;
	}
	}else{
		swal("Sin selecci\u00F3n!", "No se ha seleccionado ning\u00FAn release",
		"warning");
		return;
	}
}
function removeData(data){

	$dataRelease.forEach(function (element,index){
        if(element.id == data.id){
        	$dataRelease.splice(index,1); 
        	if($dataRelease.length==0){
    
        		$dataRelease=[];
        		 $('#releaseTableAdd').dataTable().fnClearTable();
        		 
        		 return;
        	}
        	 $('#releaseTableAdd').dataTable().fnClearTable();
    		 $('#releaseTableAdd').dataTable().fnAddData($dataRelease);
    		
            return false; 
        }
});
}

function sendRequest() {
	var form = "#generateRequestForm";
	changeSaveButton(true);
	changeSaveButton(true);
	var valueSchema="";

	$.ajax({
		// async : false,
		type : "PUT",
		url : getCont() + "request/saveRequest",
		timeout: 60000,
		dataType : "json",
		contentType: "application/json; charset=utf-8",
		data : JSON.stringify({
			// Informacion general
			id : $requestEditForm.find('#requestId').val(),
			senders:$requestEditForm.find('#senders').val(),
			message:$requestEditForm.find('#messagePer').val(),
		}),
		success : function(response) {
			responseAjaxSendRequest(response);
			changeSaveButton(false);
			origForm = $requestEditForm.serialize();
			//$dataReleaseCheck=$dataRelease.slice();
			reloadPreview();
		},
		error: function(x, t, m) {
			notifyAjaxError(x, t, m);
			changeSaveButton(false);
		}
	});
}

function requestRequest() {
	
	changeSaveButton(true);
	$.ajax({
		// async : false,
		type : "PUT",
		url : getCont() + "request/saveRequest",
		timeout: 60000,
		dataType : "json",
		contentType: "application/json; charset=utf-8",
		data : JSON.stringify({
			// Informacion general
			id : $requestEditForm.find('#requestId').val(),
			senders:$requestEditForm.find('#senders').val(),
			message:$requestEditForm.find('#messagePer').val(),
		}),
		success : function(response) {
			responseAjaxRequestRequest(response);
			changeSaveButton(false);
			origForm = $rfcEditForm.serialize();
			$dataReleaseCheck=$dataRelease.slice();
			reloadPreview();
		},
		error: function(x, t, m) {
			notifyAjaxError(x, t, m);
			changeSaveButton(false);
		}
	});
}

function responseAjaxRequestRequest(response) {
	if (response != null) {
		switch (response.status) {
		case 'success':
			resetErrors();
			reloadPreview();
			window.location = getCont() + "request/updateRequest/"
			+  $requestEditForm.find('#requestId').val();
			break;
		case 'fail':
			showRequestErrors(response.errors);
			countErrorsByStep();
			var numItems = $('.yourclass').length
			swal("Avance guardado!", "El formulario a\u00FAn posee campos incompletos.",
			"warning")
			break;
		case 'exception':
			swal("Error!", response.exception, "error")
			break;
		default:
			unblockUI();
		}
	}
}



function responseAjaxSendRequest(response) {
	if (response != null) {
		switch (response.status) {
		case 'success':
			resetErrors();
			reloadPreview();
			swal("Correcto!", "Solicitud guardada correctamente.",
					"success", 2000)
					$('#generateReleaseForm #applyFor').show();
			break;
		case 'fail':
			showRequestErrors(response.errors);
			countErrorsByStep();
			var numItems = $('.yourclass').length
			swal("Avance guardado!", "El formulario a\u00FAn posee campos incompletos.",
			"warning")
			break;
		case 'exception':
			swal("Error!", response.exception, "error")
			break;
		default:
			unblockUI();
		}
	}
}

function resetErrors() {
	$(".fieldError").css("visibility", "hidden");
	$(".fieldError").attr("class", "error fieldError");
	$(".fieldErrorLine").attr("class", "form-line");
	$('.labelCount_Error').css("visibility", "hidden");
}

function showRequestErrors(errors) {
	resetErrors();// Eliminamos las etiquetas de errores previas
	var error = errors;
	for (var i = 0; i < error.length; i++) {
		// Se modifica el texto de la advertencia y se agrega la de activeError
		$requestEditForm.find(" #" + error[i].key + "_error").text(
				error[i].message);
		$requestEditForm.find(" #" + error[i].key + "_error").css("visibility",
		"visible");
		$requestEditForm.find(" #" + error[i].key + "_error").attr("class",
		"error fieldError activeError");
		// Si es input||textarea se marca el line en rojo
		if ($requestEditForm.find(" #" + error[i].key).is("input")
				|| $requestEditForm.find(" #" + error[i].key).is("textarea")) {
			$requestEditForm.find(" #" + error[i].key).parent().attr("class",
			"form-line error fieldErrorLine");
		}
	}
}
function countErrorsByStep() {
	var step1 = $("#step1").find(".activeError").length;
	if (step1 != 0) {
		$("#step1Errors").css("visibility", "visible");
	}
	var step2 = $("#step2").find(".activeError").length;
	if (step2 != 0) {
		$("#step2Errors").css("visibility", "visible");
	}
	var step3 = $("#step3").find(".activeError").length;
	if (step3 != 0) {
		$("#step3Errors").css("visibility", "visible");
	}
	var step4 = $("#step4").find(".activeError").length;
	if (step4 != 0) {
		$("#step4Errors").css("visibility", "visible");
	}
}


