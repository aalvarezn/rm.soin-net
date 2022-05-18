var $rfcEditForm = $('#generateRFCForm');
var $dataRelease = [];
let $dataReleaseCheck = [];
var origForm=null;
var $dtRFCs;
var $dtRFCsAdd;


$(function() {

	activeItemMenu("managemetReleaseItem");
	
	initTable();
	initTableAdd();
	initData();
	 $('#releaseTable tbody').on( 'click', 'tr', function () {
	      if ( $(this).hasClass('selected') ) {
	            $(this).removeClass('selected');

	        }
	        else {
	        	$dtRFCs.$('tr.selected').removeClass('selected');
	            $(this).addClass('selected');
	          
	        }
	    } );
	 
	 $('#releaseTableAdd tbody').on( 'click', 'tr', function () {
	      if ( $(this).hasClass('selected') ) {
	            $(this).removeClass('selected');

	        }
	        else {
	        	$dtRFCsAdd.$('tr.selected').removeClass('selected');
	            $(this).addClass('selected');
	          
	        }
	    } );
	 $('#releaseTableAdd tbody').on( 'dblclick', 'tr', function () {
		 var data = $dtRFCsAdd.row( this ).data();
		 removeData(data);
	 });


	$('#dateBegin').datetimepicker({
		locale: 'es',
		format: 'DD/MM/YYYY hh:mm a',
		minDate : new Date(),
		sideBySide: true
	});
	$('#dateFinish').datetimepicker({
		locale: 'es',
		format: 'DD/MM/YYYY hh:mm a',
		sideBySide: true
	});
	   $("#dateBegin").on("dp.change", function (e) {
           $('#dateFinish').data("DateTimePicker").minDate(e.date);
       });
	   
	   $("#dateFinish").on("dp.change", function (e) {
		   if( $("#dateBegin").val()==""){
			   $('#dateFinish').data("DateTimePicker").minDate(new Date());
			   $("#dateBegin").val($("#dateFinish").val());   
		   }else{
			   $('#dateFinish').data("DateTimePicker").minDate($("#dateBegin").val());
		   }
		
       });

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
			sendPartialRFC();
		}
	});
	//console.log($('a data-action="togglePicker"'));
	
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
	origForm = $rfcEditForm.serialize();
	$('.tagInit').tagsInput({
		width:'400px'
	});
	checkDB();
	if($rfcEditForm.find('#requiredBD').val()==0){
		$("#tagShow").hide();
	}else{
		$("#tagShow").show();
	}
	//$dataReleaseCheck=$dataRelease;
	//dropDownChange();
	//initTable();
});
function nextTab(elem) {
	$(elem).next().find('a[data-toggle="tab"]').tab('show');
}

function prevTab(elem) {
	$(elem).prev().find('a[data-toggle="tab"]').tab('show');
}

function checkDB(){
	$("#generateRFCForm #requiredBD").change(function() {
		if (this.checked) {
			this.value = 1;
			
			$("#tagShow").show("slow");
		} else {
			this.value = 0;
			console.log("aca");
			$("#tagShow").hide("slow");
		}
	});
}

function formHasChanges(){
	
	if($rfcEditForm.serialize() === origForm && compareArrays($dataRelease,$dataReleaseCheck) ){
		return false;
	}else{
		return true;
	}
}

function previewRFC() {
	$('#previewReleaseModal').modal('show');
}

function closePreviewRFC() {
	$('#previewReleaseModal').modal('hide');
}

function compareArrays(arr1, arr2) {
	console.log($dataReleaseCheck);
	console.log("$dataReleaseCheck");
	console.log($dataRelease);
	console.log("$dataRelease");
	console.log($(arr1).not(arr2).length);
	console.log( $(arr2).not(arr1).length);
	return $(arr1).not(arr2).length == 0 && $(arr2).not(arr1).length == 0;
};
function sendPartialRFC() {
	var form = "#generateReleaseForm";
	console.log();
	changeSaveButton(true);

	$.ajax({
		// async : false,
		type : "PUT",
		url : getCont() + "rfc/saveRFC",
		timeout: 60000,
		dataType : "json",
		contentType: "application/json; charset=utf-8",
		data : JSON.stringify({
			// Informacion general
			id : $rfcEditForm.find('#rfcId').val(),
			codeProyect : $rfcEditForm.find('#rfcCode').val(),
			impactId : $rfcEditForm.find('#impactId').children(
			"option:selected").val(),
			priorityId : $rfcEditForm.find('#priorityId').children("option:selected")
			.val(),
			typeChangeId : $rfcEditForm.find('#typeChangeId').children(
			"option:selected").val(),
			requestDateBegin : $rfcEditForm.find('#dateBegin').val(),
			requiredBD: boolean($rfcEditForm.find('#requiredBD').val()),	
			schemaDB:$('.tagInit').val(),
			requestDateFinish : $rfcEditForm.find('#dateFinish').val(),
			reasonChange : $rfcEditForm.find('#rfcReason').val(),
			effect : $rfcEditForm.find('#rfcEffect').val(),
			releasesList: JSON.stringify($dataRelease),
			detail:$rfcEditForm.find('#detailRFC').val(),
			evidence:$rfcEditForm.find('#evidenceRFC').val(),
			returnPlan:$rfcEditForm.find('#returnPlanRFC').val(),
			requestEsp:$rfcEditForm.find('#requestEspRFC').val(),
		}),
		success : function(response) {
			//responseAjaxSendPartialRelease(response);
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

function changeSaveButton(save){
	if(save){
		$rfcEditForm.find('#btnSave').find('#btnText').text('GUARDANDO');
		$rfcEditForm.find('#btnSave').find('#btnIcon').text('cached');
	}else{
		$rfcEditForm.find('#btnSave').find('#btnText').text('GUARDAR');
		$rfcEditForm.find('#btnSave').find('#btnIcon').text('check_box');
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
					console.log("aca se asigna por primera vez");
				}else{
					
				}
				
				
			}
		});
		
		
	
}
function initTable(){

	
	$dtRFCs=$('#releaseTable').DataTable(
			{
				lengthMenu : [ [ 10, 25, 50, -1 ],
					[ '10', '25', '50', 'Mostrar todo' ] ],
						"iDisplayLength" : 5,
						"language" : {
							"emptyTable" : "No existen registros",
							"zeroRecords" : "No existen registros",
							"processing" : "Cargando",
						},
						"iDisplayStart" : 0,
						"processing" : true,
						"serverSide" : true,
						"sAjaxSource" :getCont() + "rfc/changeRelease",
						"fnServerParams": function ( aoData ) {
							aoData.push({"name": "systemId", "value": $('#systemId').children("option:selected").val()});
						}, 
						"aoColumns" : [
							{
								"mDataProp" : "numRelease",
							},
							{
								"mDataProp" : "system.code",
							}
							],
							responsive : true,
							ordering : false,
							select: true
					});
}

function addDataToTable(){
	var data = $dtRFCs.row('.selected').data();
	console.log(data);
	var verification=true;
	if(data!=undefined){
		
	
	if($dataRelease.length!=0){

		$dataRelease.forEach(function(element){
			if(element.id==data.id){
				console.log(element.id)
				verification=false;
				
			}
			
		});
		if(verification){
			let text ='{"id":'+(data.id).toString()+',"numRelease":"'+(data.numRelease).toString()+'"}';
			const obj = JSON.parse(text);
			$dataRelease.unshift(obj);
			 $('#releaseTableAdd').dataTable().fnClearTable();
			 $('#releaseTableAdd').dataTable().fnAddData($dataRelease);
			
		}else{
			swal("Error!", "El release ya ha sido agregado",
					"error", 2000);
			
		}
		
	}else{
		let text ='{"id":'+(data.id).toString()+',"numRelease":"'+(data.numRelease).toString()+'"}';
		const obj = JSON.parse(text);
		$dataRelease.unshift(obj);
		 $('#releaseTableAdd').dataTable().fnClearTable();
		 $('#releaseTableAdd').dataTable().fnAddData($dataRelease);
		
	}
		return;
	}
	 // var data = $dtRFCs.row(this).data();
    	//console.log(data);
}

function reloadPreview() {
	var src = $("#tinySummary").attr("src")
	$("#tinySummary").attr("src", src)
}

function initTableAdd(){
	$dtRFCsAdd=$('#releaseTableAdd').DataTable({
			"iDisplayLength" : 5,
			"language" : {
				"emptyTable" : "No existen registros",
				"zeroRecords" : "No existen registros",
				"processing" : "Cargando",
			},
		  		data: $dataRelease,
		       aoColumns: [
		         { mData: 'numRelease' },		
		      ]
		  });
}
function dropDownChange(){

	$('#systemId').on('change', function(){
		var sId =$rfcEditForm.find('#systemId').val();
		
		console.log(sId);
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
								"mDataProp" : "numRelease"
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


function removeData(data){

	$dataRelease.forEach(function (element,index){
        if(element.id == data.id){
        	console.log(index);	
        	$dataRelease.splice(index,1); 
        	if($dataRelease.length==0){
    
        		$dataRelease=[];
        		console.log($dataRelease==$dataRelease);
        		 $('#releaseTableAdd').dataTable().fnClearTable();
        		 
        		 return;
        	}
        	 $('#releaseTableAdd').dataTable().fnClearTable();
    		 $('#releaseTableAdd').dataTable().fnAddData($dataRelease);
    		
            return false; 
        }
});
}

function sendRFC() {
	var form = "#generateReleaseForm";
	changeSaveButton(true);
	console.log($rfcEditForm.find('#requiredBD').val());
	$.ajax({
		// async : false,
		type : "PUT",
		url : getCont() + "rfc/saveRFC",
		timeout: 60000,
		dataType : "json",
		contentType: "application/json; charset=utf-8",
		data : JSON.stringify({
			// Informacion general
			id : $rfcEditForm.find('#rfcId').val(),
			codeProyect : $rfcEditForm.find('#rfcCode').val(),
			impactId : $rfcEditForm.find('#impactId').children(
			"option:selected").val(),
			priorityId : $rfcEditForm.find('#priorityId').children("option:selected")
			.val(),
			typeChangeId : $rfcEditForm.find('#typeChangeId').children(
			"option:selected").val(),
			requestDateBegin : $rfcEditForm.find('#dateBegin').val(),
			requiredBD: boolean($rfcEditForm.find('#requiredBD').val()),
			schemaDB:$('.tagInit').val(),
			requestDateFinish : $rfcEditForm.find('#dateFinish').val(),
			reasonChange : $rfcEditForm.find('#rfcReason').val(),
			effect : $rfcEditForm.find('#rfcEffect').val(),
			releasesList: JSON.stringify($dataRelease),
			detail:$rfcEditForm.find('#detailRFC').val(),
			evidence:$rfcEditForm.find('#evidenceRFC').val(),
			returnPlan:$rfcEditForm.find('#returnPlanRFC').val(),
			requestEsp:$rfcEditForm.find('#requestEspRFC').val(),
		}),
		success : function(response) {
			responseAjaxSendRFC(response);
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

function requestRFC() {
	
	changeSaveButton(true);
	console.log($rfcEditForm.find('#requiredBD').val());
	$.ajax({
		// async : false,
		type : "PUT",
		url : getCont() + "rfc/saveRFC",
		timeout: 60000,
		dataType : "json",
		contentType: "application/json; charset=utf-8",
		data : JSON.stringify({
			// Informacion general
			id : $rfcEditForm.find('#rfcId').val(),
			codeProyect : $rfcEditForm.find('#rfcCode').val(),
			impactId : $rfcEditForm.find('#impactId').children(
			"option:selected").val(),
			priorityId : $rfcEditForm.find('#priorityId').children("option:selected")
			.val(),
			typeChangeId : $rfcEditForm.find('#typeChangeId').children(
			"option:selected").val(),
			requestDateBegin : $rfcEditForm.find('#dateBegin').val(),
			requiredBD: boolean($rfcEditForm.find('#requiredBD').val()),
			schemaDB:$('.tagInit').val(),
			requestDateFinish : $rfcEditForm.find('#dateFinish').val(),
			reasonChange : $rfcEditForm.find('#rfcReason').val(),
			effect : $rfcEditForm.find('#rfcEffect').val(),
			releasesList: JSON.stringify($dataRelease),
			detail:$rfcEditForm.find('#detailRFC').val(),
			evidence:$rfcEditForm.find('#evidenceRFC').val(),
			returnPlan:$rfcEditForm.find('#returnPlanRFC').val(),
			requestEsp:$rfcEditForm.find('#requestEspRFC').val(),
		}),
		success : function(response) {
			responseAjaxRequestRFC(response);
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

function responseAjaxRequestRFC(response) {
	if (response != null) {
		switch (response.status) {
		case 'success':
			resetErrors();
			reloadPreview();
			window.location = getCont() + "rfc/updateRFC/"
			+  $rfcEditForm.find('#rfcId').val();
			break;
		case 'fail':
			showReleaseErrors(response.errors);
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



function responseAjaxSendRFC(response) {
	if (response != null) {
		switch (response.status) {
		case 'success':
			resetErrors();
			reloadPreview();
			swal("Correcto!", "RFC guardado correctamente.",
					"success", 2000)
					$('#generateReleaseForm #applyFor').show();
			break;
		case 'fail':
			showReleaseErrors(response.errors);
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

function showReleaseErrors(errors) {
	resetErrors();// Eliminamos las etiquetas de errores previas
	var error = errors;
	console.log(errors);
	for (var i = 0; i < error.length; i++) {
		// Se modifica el texto de la advertencia y se agrega la de activeError
		$rfcEditForm.find(" #" + error[i].key + "_error").text(
				error[i].message);
		$rfcEditForm.find(" #" + error[i].key + "_error").css("visibility",
		"visible");
		$rfcEditForm.find(" #" + error[i].key + "_error").attr("class",
		"error fieldError activeError");
		// Si es input||textarea se marca el line en rojo
		if ($rfcEditForm.find(" #" + error[i].key).is("input")
				|| $rfcEditForm.find(" #" + error[i].key).is("textarea")) {
			$rfcEditForm.find(" #" + error[i].key).parent().attr("class",
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
