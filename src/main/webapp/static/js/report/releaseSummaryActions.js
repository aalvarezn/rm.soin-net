var $formChangeStatus = $('#changeStatusForm');

var trackingReleaseForm =$('#trackingReleaseForm');
var network = null;
var nodes = [];
var edges = [];
$(function() {
	$("#contentSummary textarea").parent().removeClass(
	'focused');
	$("#contentSummary input").parent().removeClass('focused');
	// autosize($('textarea'));

	$('.tableIni').DataTable({
		"language": optionLanguaje,
		"searching" : true,
		"paging" : true,
		"bInfo" : true
	});
	
	$('#configurationItemsTable').DataTable({
		"language": optionLanguaje,
		"searching" : true,
		"paging" : true,
		"bInfo" : true
	});
	
	$('#configurationItemsTable').on('draw.dt', function() {
		$("#countObject").text($('#configurationItemsTable').DataTable().rows().count());
	});
	
	
	$('textarea').each(
			function() {
				this.setAttribute('style', 'height:'
						+ (this.scrollHeight)
						+ 'px;overflow-y:hidden;');
			}).on('input', function() {
				this.style.height = 'auto';
				this.style.height = (this.scrollHeight) + 'px';
			});

	$formChangeStatus.find('#statusId').change(function() {
		$formChangeStatus.find('#motive').val($(this).children("option:selected").attr('data-motive'));
	});
	
	searchTree();
	initDataTracking();
});


function confirmCancelRelease(index){
	Swal.fire({
		title: '\u00BFEst\u00e1s seguro que desea cancelar el release?',
		text: "Esta acci\u00F3n no se puede reversar.",
		icon: 'question',
		showCancelButton: true,
		customClass: 'swal-wide',
		cancelButtonText: 'Cancelar',
		cancelButtonColor: '#f14747',
		confirmButtonColor: '#3085d6',
		confirmButtonText: 'Aceptar',
	}).then((result) => {
		if(result.value){
			cancelRelease(index);
		}		
	});
}

function cancelRelease(index) {
	blockUI();
	$.ajax({
		type : "GET",
		url : getCont() + "management/release/" + "cancelRelease",
		timeout : 60000,
		data : {
			idRelease : index
		},
		success : function(response) {
			responseCancelRelease(response);
		},
		error : function(x, t, m) {
			notifyAjaxError(x, t, m);
		}
	});
}

function responseCancelRelease(response) {
	switch (response.status) {
	case 'success':
		swal("Correcto!", "El release ha sido anulado exitosamente.",
				"success", 2000)
				location.reload(true);
		break;
	case 'fail':
		swal("Error!", response.exception, "error")
		break;
	case 'exception':
		swal("Error!", response.exception, "error")
		break;
	}
}

function changeStatusRelease(releaseId, releaseNumber) {
	$formChangeStatus[0].reset();
	$formChangeStatus.find('#idRelease').val(releaseId);
	$formChangeStatus.find('#releaseNumber').val(releaseNumber);
	$formChangeStatus.find('#dateChange').val(moment().format('DD/MM/YYYY hh:mm a'))
	$formChangeStatus.find('.selectpicker').selectpicker('refresh');
	$formChangeStatus.find("#statusId_error").css("visibility", "hidden");
	$('#changeStatusModal').modal('show');
}

function saveChangeStatusModal(){
	if (!validStatusRelease())
		return false;
	blockUI();
	$.ajax({
		type : "GET",
		url : getCont() + "management/release/" + "statusRelease",
		timeout : 60000,
		data : {
			idRelease : $formChangeStatus.find('#idRelease').val(),
			idStatus: $formChangeStatus.find('#statusId').children("option:selected").val(),
			dateChange: $formChangeStatus.find('#dateChange').val(),
			motive: $formChangeStatus.find('#motive').val()
		},
		success : function(response) {
			responseStatusRelease(response);
		},
		error : function(x, t, m) {
			notifyAjaxError(x, t, m);
		}
	});
}

function responseStatusRelease(response) {
	switch (response.status) {
	case 'success':
		swal("Correcto!", "El release ha sido modificado exitosamente.",
				"success", 2000);
		location.reload(true);
		closeChangeStatusModal();
		break;
	case 'fail':
		swal("Error!", response.exception, "error")
		break;
	case 'exception':
		swal("Error!", response.exception, "error")
		break;
	}
}

function closeChangeStatusModal(){
	$formChangeStatus[0].reset();
	$formChangeStatus.find('#userId').selectpicker('val', '');
	$('#changeStatusModal').modal('hide');
}


function validStatusRelease() {
	let valid = true;
	let statusId = $formChangeStatus.find('#statusId').children("option:selected")
	.val();
	if ($.trim(statusId) == "" || $.trim(statusId).length == 0) {
		$formChangeStatus.find("#statusId_error").css("visibility", "visible");
		return false;
	} else {
		$formChangeStatus.find("#statusId_error").css("visibility", "hidden");
		return true;
	}
}



function searchTree() {
	var releaseNumber = $("input[name='releaseNumber']").val(); 
	console.log(releaseNumber);
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
				navigationButtons: false,
				keyboard: false,
				dragNodes:false,
				dragView:false,
				hideNodesOnDrag:false,
				hideEdgesOnZoom:true,
				zoomView:false,
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
/*
	network.on("doubleClick", function(params) {
		openReference(params);
	});


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

function initDataTracking(){
	var idRelease=$('#releaseId').val();
	
		$.ajax({
			type: 'GET',
			url: getCont() + "release/getRelease-"+idRelease,
			success: function(result) {
				if(result.length!=0){
					loadTrackingRelease(result);
				}else{
					
				}
				
				
			}
		});
		
		
}

function loadTrackingRelease(rowData){
	trackingReleaseForm.find('tbody tr').remove();
	if(rowData.tracking.length == 0){
		trackingReleaseForm.find('tbody').append('<tr><td colspan="4" style="text-align: center;">No hay movimientos</td></tr>');
	}
	$.each(rowData.tracking, function(i, value) {
		trackingReleaseForm.find('tbody').append('<tr style="padding: 10px 0px 0px 0px;" > <td><span style="background-color: '+getColorNode(value.status)+';" class="round-step"></span></td>	<td>'+value.status+'</td>	<td>'+moment(value.trackingDate).format('DD/MM/YYYY h:mm:ss a')+'</td>	<td>'+value.operator+'</td> <td>'+(value.motive && value.motive != null && value.motive != 'null' ? value.motive:'' )+'</td>	</tr>');
	});
}

function closeTrackingReleaseModal(){
	trackingReleaseForm[0].reset();
	$('#trackingReleaseModal').modal('hide');
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

function exportPDF(releaseNumber){
    
    var pdf = new jsPDF('p','pt','letter');

    var idRelease=$('#releaseId').val();
	html2canvas(document.querySelector('#pdf')).then(canvas => {
		  var imageDate=canvas.toBlob((blob)=>{
			  var formData = new FormData();
				formData.append('file', blob);
			  console.log(blob);
			  console.log(formData);
			  $.ajax({
					type: 'POST',
					cache : false,
					contentType : false,
					processData : false,
					async : true,
					url: getCont() + "file/singleUploadReport-"+idRelease,
					data : formData,
				    beforeSend: function() {
				    	showSpinner();
				      },
					success: function(result) {
						 var link = document.createElement('a');
					        link.href= getCont()+'report/downloadRelease/'+ idRelease;
					        link.click();
					},
					   complete: function() {
						      // ocultar el mensaje de descarga después de completar la
								// solicitud
						      hideSpinner();
						    },
				});
		  });
		  /*
		  console.log(imageDate);
	    var imgData = canvas.toDataURL(
        'image/png'); 
	    console.log(typeof(imgData));
	    console.log(imgData);
		*/
	    /*$.ajax({
			type: 'GET',
			dataType: 'json',
			url: getCont() + "report/downloadRelease/"+idRelease,
			data:JSON.stringify({imageBase64: encodeURIComponent(imgData)}),
			success: function(result) {
				console.log(result);

			}
		});
	
		  const imgProps=pdf.getImageProperties(imgData);
	    const pdfWidth=pdf.internal.pageSize.getWidth();
	    const pdfHeight=(imgProps.height* pdfWidth)/imgProps.width;
	   pdf.addImage(imgData,'png',0,0,pdfWidth,pdfHeight);
		pdf.save('REPORTE-'+releaseNumber+'.pdf');
	*/
	});

    
/*
	html2canvas(document.querySelector('#pdf')).then((canvas) => {
		
		console.log(canvas);	
		var imgData=canvas.toDataUrl('image/png');
		console.log(""+ imgData);
		pdf.addImage(imgData,'png',10,10);
		pdf.save('REPORTE-'+releaseNumber+'.pdf');
	});
*/

}
function showSpinner(){
	var miElemento = document.getElementById("loading"); 
	miElemento.style.display = "flex";
}

function hideSpinner(){
	var miElemento = document.getElementById("loading"); 
	miElemento.style.display = "none";
}


