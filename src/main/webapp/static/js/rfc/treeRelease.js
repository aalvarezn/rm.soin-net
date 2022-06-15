let $treeForm = $('#treeForm');
var nodes = [];
var edges = [];
var network = null;
$(function() {
	activeItemMenu("treeItem");
	// If the document is clicked somewhere
	$('#mynetwork').bind("mousedown", function (e) {
//		alert('mynetwork')
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
			let txt = $(this).attr("data-release");
			let $temp = $("<input>")
			$("body").append($temp);
			$temp.val(txt).select();
			document.execCommand("copy");
			$temp.remove();
			notifyInfo('Copiado a portapapales');
			break;
		case 'tree':
			window.open(getCont() + "admin/tree/" + $(this).attr("data-id"), '_blank');
			break;
		default:
			break;
		}
		$(".node-menu").hide(100);
	});

	if($treeForm.find('#releaseNumber').val() != "" && $treeForm.find('#depth').val() >= 0)
		searchTree()
});


function searchTree() {
	$.ajax({
		type : "GET",
		url : getCont() + "admin/tree/tree/"
		+ $treeForm.find('#releaseNumber').val() + "/"
		+ $treeForm.find('#depth').val(),
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

function getNodes(){
	return nodes;
}

function getEdges(){
	return edges;
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
		console.log(response.status);
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

	network.on("oncontext", function (params) {
		params.event.preventDefault();
		if (typeof this.getNodeAt(params.pointer.DOM) !== 'undefined') {
			network.selectNodes([this.getNodeAt(params.pointer.DOM)]);
			let node = network.body.nodes[this.getNodeAt(params.pointer.DOM)];
			$('.node-menu li').attr('data-id', this.getNodeAt(params.pointer.DOM));
			$('.node-menu li').attr('data-release', node.options.numberRelease);

			$(".node-menu").finish().toggle(100);
			$(".node-menu").css({
				top: params.event.pageY + "px",
				left: params.event.pageX + "px"
			});
		}else{
			network.selectNodes([]);
		}
	});
}

function openReference(properties) {
	if (typeof properties.nodes[0] !== 'undefined') {
		window.open(getCont() + "release/summary-" + properties.nodes[0], '_blank'); 
	}
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