var $rfcEditForm = $('#generateRFCForm');
var $dataRelease = [];
let $dataReleaseCheck = [];
var origForm=null;
var $dtRFCs;
var $dtRFCsAdd;
var $trackingRFCForm = $('#trackingReleaseForm');
var $treeForm = $('#treeForm');
var network = null;
var nodes = [];
var edges = [];
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
		   
           $('#dateFinish').data("DateTimePicker").minDate(e.date.add(60,'m'));
       });
	   
	   $("#dateFinish").on("dp.change", function (e) {
		   if( $("#dateBegin").val()==""){
			  
		   }else{
			   var dateI=$('#dateBegin').data("DateTimePicker").date().add(1,'hours');
			   var date1hour=moment(dateI, "DD-MM-YYYY");

			   $('#dateFinish').data("DateTimePicker").minDate(date1hour);
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
	// $dataReleaseCheck=$dataRelease;
	// dropDownChange();
	// initTable();
	
	// If the document is clicked somewhere
	$('#mynetwork').bind("mousedown", function (e) {
// alert('mynetwork')
		// If the clicked element is not the menu
		if (!$(e.target).parents(".node-menu").length > 0) {

			// Hide it
			$(".node-menu").hide(100);
		}
	});

	$(".node-menu li").click(function(){
		switch ($(this).attr("id")) {
		case 'summary':
			window.open(getCont() + "release/summary-" + $(this).attr("data-id"), '_blank');
			break;
		case 'clipboard':
			let txt =$(this).attr("data-release");
		    var textField = document.createElement('textarea');
		    textField.innerText = txt;
		    document.body.appendChild(textField);
		    textField.select();
		    textField.focus(); // SET FOCUS on the TEXTFIELD
		    document.execCommand('copy');
		    textField.remove();

			notifyInfo('Copiado a portapapales');
			break;
		default:
			break;
		}
		$(".node-menu").hide(100);
	});
	
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
	return $(arr1).not(arr2).length == 0 && $(arr2).not(arr1).length == 0;
};
function sendPartialRFC() {
	var form = "#generateReleaseForm";
	
	changeSaveButton(true);
	var valueSchema="";
	if(boolean($rfcEditForm.find('#requiredBD').val())==false){
		$('#bd').val('');
	}else{
		valueSchema=$('#bd').val();
	}
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
			schemaDB:valueSchema,
			requestDateFinish : $rfcEditForm.find('#dateFinish').val(),
			reasonChange : $rfcEditForm.find('#rfcReason').val(),
			effect : $rfcEditForm.find('#rfcEffect').val(),
			releasesList: JSON.stringify($dataRelease),
			detail:$rfcEditForm.find('#detailRFC').val(),
			evidence:$rfcEditForm.find('#evidenceRFC').val(),
			returnPlan:$rfcEditForm.find('#returnPlanRFC').val(),
			requestEsp:$rfcEditForm.find('#requestEspRFC').val(),
			senders:$rfcEditForm.find('#senders').val(),
			message:$rfcEditForm.find('#messagePer').val(),
		}),
		success : function(response) {
			// responseAjaxSendPartialRelease(response);
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
				}else{
					
				}
				
				
			}
		});
		
		
	
}
function initTable(){

	
	$dtRFCs=$('#releaseTable').DataTable(
			{
				'columnDefs' : [ {
					'visible' : false,
					'targets' : [ 0]
				} ],
				lengthMenu : [ [ 10, 25, 50, -1 ],
					[ '10', '25', '50', 'Mostrar todo' ] ],
						"iDisplayLength" : 15,
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
								"mDataProp" : "id",
							},
							{
								"mDataProp" : "releaseNumber",
							},
							{
								"mDataProp" : "description",
							},
							{
								
								"mDataProp" : "user.fullName"
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
									',0'
									+ ')" title="Rastreo"><i class="material-icons gris" style="font-size: 25px;">location_on</i> </a>';
									options = options
									+ '<a onclick="openTreeModal('
									+ row.id+
									',0'
									+ ')" title="Árbol de dependencias"><i class="material-icons gris" style="font-size: 25px;">device_hub</i> </a>';
									options = options
									+ '<a href="'
									+ getCont()
									+ 'release/summary-'
									+ row.id
									+ '" target="_blank" title="Resumen"><i class="material-icons gris">info</i></a> </div>';
								
									
									return options;
								},
							} 
							
							
							],
							responsive : true,
							ordering : false,
							select: true
					});
}

function openRFCTrackingModal(idRFC,table) {
	var dtRFC ;
	
	if(table==0){
		dtRFC = $('#releaseTable').dataTable();
	}else{
		dtRFC = $('#releaseTableAdd').dataTable();
	}
	
	var idRow = dtRFC.fnFindCellRowIndexes(idRFC, 0); // idRow
	var rowData;
		if(table==0){
			rowData=$dtRFCs.row(idRow[0]).data();
		}else{
			rowData=$dtRFCsAdd.row(idRow[0]).data();
		}
	$trackingRFCForm.find('#idRelease').val(rowData.id);
	$trackingRFCForm.find('#releaseNumber').text(rowData.releaseNumber);
	
	loadTrackingRFC(rowData);
	$('#trackingReleaseModal').modal('show');
}

function openTreeModal(idRFC,table) {
var dtRFC ;
	
	if(table==0){
		dtRFC = $('#releaseTable').dataTable();
	}else{
		dtRFC = $('#releaseTableAdd').dataTable();
	}
	
	var idRow = dtRFC.fnFindCellRowIndexes(idRFC, 0); // idRow
	var rowData;
		if(table==0){
			rowData=$dtRFCs.row(idRow[0]).data();
		}else{
			rowData=$dtRFCsAdd.row(idRow[0]).data();
		}
		
		$treeForm.find('#idRelease').val(rowData.id);
		$treeForm.find('#releaseNumber').text(rowData.releaseNumber);
		searchTree(rowData.releaseNumber);
	$('#treeModal').modal('show');
}
function searchTree(releaseNumber) {
	$.ajax({
		type : "GET",
		url : getCont() + "rfc/tree/"+ releaseNumber + "/2",
		timeout : 60000,
		data : {},
		success : function(response) {
			ajaxSearchTree(response);
			
		},
		error : function(x, t, m) {
			notifyAjaxError(x, t, m);
		}
	});

}

function ajaxSearchTree(response) {
	
	switch (response.status) {
	case 'success':
		draw(response.obj);
		break;
	case 'fail':
		destroy();
		swal("Error!", response.exception, "error");
		break;
	case 'exception':
		destroy();
		swal("Error!", response.exception, "error")
		break;
	default:
	destroy();
	}
}

function destroy() {
	if (network !== null) {
		nodes = [];
		edges = [];
		network.destroy();
		network = null;
	}
}

function draw(objs) {
	destroy();
	if (objs == null)
		return;
	var data = getTreeNetwork(objs);
	// create a network
	var container = document.getElementById("mynetwork");
	var options = {
			interaction: {
				navigationButtons: true,
				keyboard: true,
			},
			nodes: {
				shape: "box",
				borderWidth: 2,
				shadow: true,
			},
			layout: {
				hierarchical: {
					direction: "UD",
					nodeSpacing: 300,
					sortMethod: "directed"
				}
			}
	};
	network = new vis.Network(container, data, options);


	network.on("stabilizationIterationsDone", function () {
		network.setOptions({
			nodes: {physics: false},
			edges: {physics: false},
		});
	});

	network.on("doubleClick", function(params) {
		openReference(params);
	});

	network.once('stabilized', function() {
		var scaleOption = { scale : 0.7 };
		network.moveTo(scaleOption);
	})
/*
 * network.on("oncontext", function (params) { params.event.preventDefault(); if
 * (typeof this.getNodeAt(params.pointer.DOM) !== 'undefined') {
 * network.selectNodes([this.getNodeAt(params.pointer.DOM)]); let node =
 * network.body.nodes[this.getNodeAt(params.pointer.DOM)]; $('.node-menu
 * li').attr('data-id', this.getNodeAt(params.pointer.DOM)); $('.node-menu
 * li').attr('data-release', node.options.numberRelease);
 * 
 * $(".node-menu").finish().toggle(100); $(".node-menu").css({ top:
 * params.event.pageY + "px", left: params.event.pageX + "px" }); }else{
 * network.selectNodes([]); } });
 */
}

function getTreeNetwork(objs) {
	for (let i = 0; i < objs.length; i++) {
		if(!nodes.some(node => node.id === objs[i].childrenId)){
			nodes.push({
				id : objs[i].childrenId,
				font: { color:'white', multi: true },
				label : '<b>NIVEL</b>: '+ objs[i].depthNode + '\n'
				+'<b>'+ objs[i].children + '</b>\n'
				+ '<b>ESTADO</b>: '+objs[i].status,
				margin: { top: 10, right: 10, bottom: 10, left: 10 },
				color: getColorNode(objs[i].status),
				numberRelease: objs[i].children,
				level: objs[i].depthNode,
			});
		}
		if (objs[i].fatherId)
			edges.push({
				from : objs[i].childrenId,
				to : objs[i].fatherId,
			});
	}

	return {
		nodes : nodes,
		edges : edges
	};
}
function openReference(properties) {
	if (typeof properties.nodes[0] !== 'undefined') {
		window.open(getCont() + "release/summary-" + properties.nodes[0], '_blank'); 
	}
}
function loadTrackingRFC(rowData){
	$trackingRFCForm.find('tbody tr').remove();
	if(rowData.tracking.length == 0){
		$trackingRFCForm.find('tbody').append('<tr><td colspan="4" style="text-align: center;">No hay movimientos</td></tr>');
	}
	$.each(rowData.tracking, function(i, value) {
		$trackingRFCForm.find('tbody').append('<tr style="padding: 10px 0px 0px 0px;" > <td><span style="background-color: '+getColorNode(value.status)+';" class="round-step"></span></td>	<td>'+value.status+'</td>	<td>'+moment(value.trackingDate).format('DD/MM/YYYY h:mm:ss a')+'</td>	<td>'+value.operator+'</td> <td>'+(value.motive && value.motive != null && value.motive != 'null' ? value.motive:'' )+'</td>	</tr>');
	});
}

function closeTrackingRFCModal(){
	$trackingRFCForm[0].reset();
	$('#trackingReleaseModal').modal('hide');
}

function closeTreeModal(){
	$treeForm[0].reset();
	$('#treeModal').modal('hide');
}
function getColorNode(status){
	switch (status) {
	case 'Produccion':
		return 'rgb(0, 150, 136)';
		break;
	case 'Certificacion':
		return 'rgb(255, 152, 0)';
		break;
	case 'Solicitado':
		return 'rgb(76, 175, 80)';
		break;
	case 'Borrador':
		return 'rgb(31, 145, 243)';
		break;
	case 'Anulado':
		return 'rgb(233, 30, 99)';
		break;
	default:
		return 'rgb(0, 181, 212)';
	break;
	}
}


function addDataToTable(){
	var dataRFC = $dtRFCs.rows('.selected').data();
	var verification=true;
	var verification2=false;
	var text ='`<table id="table" class="table tableIni table-bordered table-striped table-hover" border=1>' +
			'<thead>' +
			'<tr>'+
			'<th>Numero release</th>'+
			'</tr>' +
			'</thead>'+
			'<tbody>';
	if(dataRFC!=undefined){
	if(dataRFC.length!=0){
		
	for(var x=0;x<dataRFC.length;x++){
			
			var data= dataRFC[x];

				if(data.haveDependecy>0){
					
					verification=false;
					verification2=true;
				}
				

			if(!verification){
			
				 text+='<tr><td>'+data.releaseNumber+'</tr></td>';
				
			}else{
				
			}
			
			verification=true;
			
		}
	text+='</tr></tbody></table>`';
		if(verification2){
			console.log(text);
			Swal.fire({
				title: '\u00BFEst\u00e1s seguro que desea agregar?\nLos siguientes releases tienen dependencias en estado solicitado o borrado:',
				html:text,		
				icon: 'question',
				showCancelButton: true,
				customClass: 'swal-wide',
				cancelButtonText: 'Cancelar',
				cancelButtonColor: '#f14747',
				confirmButtonColor: '#3085d6',
				confirmButtonText: 'Aceptar',
			}).then((result) => {
				if(result.value){
					addAfterCheck();
				}		
			});
			
			
		}else{
			addAfterCheck();
		}
		
	
		
	}else{
		swal("Sin seleccion!", "No se ha seleccionado ningun release",
		"warning");
		return;
	}

	
	}else{
		swal("Sin seleccion!", "No se ha seleccionado ningun release",
		"warning");
		return;
	}
	
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
							+ ')" title="Árbol de dependencias"><i class="material-icons gris" style="font-size: 25px;">location_on</i> </a>';
							
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
		swal("Sin selecci\u00F3n!", "No se ha seleccionado ningun release para remover",
		"warning");
		return;
	}
	}else{
		swal("Sin selecci\u00F3n!", "No se ha seleccionado ningun release",
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

function sendRFC() {
	var form = "#generateReleaseForm";
	changeSaveButton(true);
	changeSaveButton(true);
	var valueSchema="";
	if(boolean($rfcEditForm.find('#requiredBD').val())==false){
		$('#bd').val('');
	}else{
		valueSchema=$('#bd').val();
	}
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
			schemaDB:valueSchema,
			requestDateFinish : $rfcEditForm.find('#dateFinish').val(),
			reasonChange : $rfcEditForm.find('#rfcReason').val(),
			effect : $rfcEditForm.find('#rfcEffect').val(),
			releasesList: JSON.stringify($dataRelease),
			detail:$rfcEditForm.find('#detailRFC').val(),
			evidence:$rfcEditForm.find('#evidenceRFC').val(),
			returnPlan:$rfcEditForm.find('#returnPlanRFC').val(),
			requestEsp:$rfcEditForm.find('#requestEspRFC').val(),
			senders:$rfcEditForm.find('#senders').val(),
			message:$rfcEditForm.find('#messagePer').val(),
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
	var valueSchema="";
	if(boolean($rfcEditForm.find('#requiredBD').val())==false){
		$('#bd').val('');
	}else{
		valueSchema=$('#bd').val();
	}
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
			schemaDB:valueSchema,
			requestDateFinish : $rfcEditForm.find('#dateFinish').val(),
			reasonChange : $rfcEditForm.find('#rfcReason').val(),
			effect : $rfcEditForm.find('#rfcEffect').val(),
			releasesList: JSON.stringify($dataRelease),
			detail:$rfcEditForm.find('#detailRFC').val(),
			evidence:$rfcEditForm.find('#evidenceRFC').val(),
			returnPlan:$rfcEditForm.find('#returnPlanRFC').val(),
			requestEsp:$rfcEditForm.find('#requestEspRFC').val(),
			senders:$rfcEditForm.find('#senders').val(),
			message:$rfcEditForm.find('#messagePer').val(),
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
			showRFCErrors(response.errors);
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
			showRFCErrors(response.errors);
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

function showRFCErrors(errors) {
	resetErrors();// Eliminamos las etiquetas de errores previas
	var error = errors;
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
