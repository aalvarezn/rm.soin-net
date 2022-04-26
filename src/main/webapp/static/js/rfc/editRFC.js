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
			console.log("aca se esta asignando");
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