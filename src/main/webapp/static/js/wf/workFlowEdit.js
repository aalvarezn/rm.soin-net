var $network;
var container = document.getElementById('wfEdit');
var nodes = new vis.DataSet([]);
var edges = new vis.DataSet([]);
var scales=0.8;
var x;
var y;
var position={
		x:x,
		y:y
}
//provide the data in the vis format
var data = {
		nodes : nodes,
		edges : edges
};

var groups = {
		action : {
			shape : 'icon',
			icon : {
				face : "'Font Awesome 5 Free'",
				weight : "bold", // Font Awesome 5 doesn't work properly
				// unless bold.
				code : '\uf085',
				size : 40,
				color : '#00294c'
			}
		},
		start : {
			shape : 'icon',
			icon : {
				face : "'Font Awesome 5 Free'",
				weight : "bold", // Font Awesome 5 doesn't work properly
				// unless bold.
				code : '\uf0a3',
				size : 40,
				color : '#27a941'
			}
		},
		finish : {
			shape : 'icon',
			icon : {
				face : "'Font Awesome 5 Free'",
				weight : "bold", // Font Awesome 5 doesn't work properly
				// unless bold.
				code : '\uf111',
				size : 40,
				color : '#b22323'
			}
		}
};
var options = {
		locale: 'es',
		nodes : {
			shape : 'box',
			color : '#00294c',
			font : {
				color : 'black'
			},
			group : 'action',
		},
		edges : {
			arrows : {
				to : {
					enabled : true,
					type : "arrow"
				}
			},
			smooth : {
				enabled : true,
				type : 'continuous',
				forceDirection : 'none',
				roundness : 0
			}
		},
		groups : groups,
		physics : {
			enabled : false
		},
		interaction : {
			dragNodes : true,
			dragView : true,
			hoverConnectedEdges : true,
			keyboard : {
				enabled : false,
				speed : {
					x : 10,
					y : 10,
					zoom : 0.02
				},
				bindToWindow : true
			},
			navigationButtons : true,
			selectable : true,
			selectConnectedEdges : true,
			tooltipDelay : 300,
			zoomView : true
		},
		manipulation : {
			enabled : true,
			editEdge : false,
			addEdge : function(edgeData, callback) {
				let save = true;
				if (edgeData.from === edgeData.to) {
					save = false;
					alert('No se permite autoenlaces');
				}
				if (typeof nodes.get(edgeData.to) !== "undefined") {
					if (nodes.get(edgeData.from).group === 'finish') {
						save = false;
						alert('Una actividad de fin no posee pasos posteriores.');
					}
				}
				if (existEdge(edgeData)) {
					save = false;
					alert('El enlace ya existe');
				}
				
				if (existEdgeInvert(edgeData)) {
					save = false;
					alert('Ya hay un enlace directo para este nodo');
				}
				if (save) {
					saveEdge(edgeData, callback);
				}
			},
			deleteEdge : function(edgeData, callback) {
				deleteEdge(edgeData.edges[0]);
			},
			addNode : function(nodeData, callback) {
				nodeData.label = 'actividad';
				nodeData.group = 'action';
				saveNode(nodeData, callback);
			},
			deleteNode : function(nodeData, callback) {
				deleteNode(nodeData.nodes[0], callback)
			}
		}
};

$(function() {

	$nodeModal
	.find('#users')
	.multiSelect(
			{
				selectableHeader: "<div class='custom-header'>Usuarios</div><input type='text' class='search-input filterMS' autocomplete='off' placeholder='Buscar ...'>",
				selectionHeader: "<div class='custom-header'>Notificar</div><input type='text' class='search-input filterMS' autocomplete='off' placeholder='Buscar ...'>",
				afterInit: function(ms){
					var that = this,
					$selectableSearch = that.$selectableUl.prev(),
					$selectionSearch = that.$selectionUl.prev(),
					selectableSearchString = '#'+that.$container.attr('id')+' .ms-elem-selectable:not(.ms-selected)',
					selectionSearchString = '#'+that.$container.attr('id')+' .ms-elem-selection.ms-selected';

					that.qs1 = $selectableSearch.quicksearch(selectableSearchString)
					.on('keydown', function(e){
						if (e.which === 40){
							that.$selectableUl.focus();
							return false;
						}
					});

					that.qs2 = $selectionSearch.quicksearch(selectionSearchString)
					.on('keydown', function(e){
						if (e.which == 40){
							that.$selectionUl.focus();
							return false;
						}
					});
				},
				afterSelect : function(values) {
					$nodeModal.find(
							"#users option[id='" + values + "']").attr(
									"selected", "selected");
				},
				afterDeselect : function(values) {
					$nodeModal.find(
							"#users option[id='" + values + "']")
							.removeAttr('selected');
				}
			});

	$nodeModal.find('#actors').multiSelect(
			{
				selectableHeader: "<div class='custom-header'>Usuarios</div><input type='text' class='search-input filterMS' autocomplete='off' placeholder='Buscar ...'>",
				selectionHeader: "<div class='custom-header'>Actores</div><input type='text' class='search-input filterMS' autocomplete='off' placeholder='Buscar ...'>",
				afterInit: function(ms){
					var that = this,
					$selectableSearch = that.$selectableUl.prev(),
					$selectionSearch = that.$selectionUl.prev(),
					selectableSearchString = '#'+that.$container.attr('id')+' .ms-elem-selectable:not(.ms-selected)',
					selectionSearchString = '#'+that.$container.attr('id')+' .ms-elem-selection.ms-selected';

					that.qs1 = $selectableSearch.quicksearch(selectableSearchString)
					.on('keydown', function(e){
						if (e.which === 40){
							that.$selectableUl.focus();
							return false;
						}
					});

					that.qs2 = $selectionSearch.quicksearch(selectionSearchString)
					.on('keydown', function(e){
						if (e.which == 40){
							that.$selectionUl.focus();
							return false;
						}
					});
				},
				afterSelect : function(values) {
					$nodeModal.find("#actors option[id='" + values + "']")
					.attr("selected", "selected");
				},
				afterDeselect : function(values) {
					$nodeModal.find("#actors option[id='" + values + "']")
					.removeAttr('selected');
				}
			});

	loadWorkFlow();
});

function loadWorkFlow() {
	$.ajax({
		type : "GET",
		url : getCont() + "wf/workFlow/" + "loadWorkFlow/"
		+ $("#wfEdit").data("id"),
		timeout : 60000,
		data : {},
		success : function(response) {
			ajaxLoadWorkFlow(response);
		},
		error : function(x, t, m) {
			console.log(x);
			console.log(t);
			console.log(m);
		}
	});
}

function getSelectIds(form, name) {
	let list = [];
	form.find(name).children("option:selected").each(function(j) {
		list.push(Number($(this).attr('id')));
	});
	return list;

}

function ajaxLoadWorkFlow(response) {
	if (response != null) {
		let dataNodes = response.nodes;
		if (dataNodes.length > 0) {
			// Se recorre cada nodo para ser agregado
			for (var i = 0; i < dataNodes.length; i++) {
				var visNode = {
						id : dataNodes[i].id,
						label : dataNodes[i].label,
						group : dataNodes[i].group,
						x : dataNodes[i].x,
						y : dataNodes[i].y,
						status : dataNodes[i].status,
						sendEmail : dataNodes[i].sendEmail,
						users : dataNodes[i].users,
						actors : dataNodes[i].actors,
				};

				// Se agrega el nodo
				nodes.add(visNode);
				// Se recorre cada arista del nodo para ser agregada
				if (dataNodes[i].edges.length > 0) {
					let dataEdges = dataNodes[i].edges;
					for (var j = 0; j < dataEdges.length; j++) {
						var visEdge = {
								id : dataEdges[j].id,
								from : dataNodes[i].id,
								to : dataEdges[j].nodeTo.id
						};
						// Se agrega la arista
						edges.add(visEdge);
					}
				}
			}
		}
	}

	if (document.fonts) {
		// Decent browsers: Make sure the fonts are loaded.
		document.fonts
		.load('normal normal 900 24px/1 "Font Awesome 5 Free"')
		.catch(console.error.bind(console, "Failed to load Font Awesome 5."))
		.then(function () {

			$network = new vis.Network(container, data, options);
			$network.enableEditMode();
			if(x===undefined){
				$network.moveTo({
					scale : scales,
				});
			}else{
				$network.moveTo({
					scale : scales,
					position:position
				});
			}
		

			$network.on("doubleClick", function(properties) {
				openUpdateNodeModal(properties);
			});
			$network.on("dragEnd", function(params) {
				updateNodePosition(this.getNodeAt(params.pointer.DOM), params);
			});
		})
		.catch(
				console.error.bind(
						console,
						"Failed to render the network with Font Awesome 5."
				)
		);
	}
}

function saveNode(nodeData, callback) {
	$.ajax({
		type : "POST",
		url : getCont() + "wf/workFlow/" + "saveNode",
		data : {
			id : 0,
			label : nodeData.label,
			workFlowId : $("#wfEdit").data("id"),
			x : parseFloat(nodeData.x),
			y : parseFloat(nodeData.y),
			group : 'action'
		},
		success : function(response) {
			ajaxSaveNode(response, nodeData, callback);
		},
		error : function(x, t, m) {
			console.log(x);
			console.log(t);
			console.log(m);
			notifyAjaxError(x, t, m);
		}
	});
}

function ajaxSaveNode(response, nodeData, callback) {
	switch (response.status) {
	case 'success':
		nodeData.id = response.obj.id;
		callback(nodeData);
		break;
	case 'fail':
		swal("Error!", response.exception, "error");
		callback();
		break;
	case 'exception':
		swal("Error!", response.exception, "error");
		callback();
		break;
	default:
		console.log(response.status);
	}
}

function deleteNode(element, callback) {
	$.ajax({
		type : "DELETE",
		url : getCont() + "wf/workFlow/" + "deleteNode/" + element,
		timeout : 60000,
		data : {},
		success : function(response) {
			ajaxDeleteNode(response, callback);
		},
		error : function(x, t, m) {
			notifyAjaxError(x, t, m);
		}
	});
}

function ajaxDeleteNode(response, callback) {
	switch (response.status) {
	case 'success':
		removeNode(response.obj)
		$network.enableEditMode();
		break;
	case 'fail':
		callback();
		swal("Error!", response.exception, "error");
		$network.enableEditMode();
		break;
	case 'exception':
		callback();
		swal("Error!", response.exception, "warning")
		$network.enableEditMode();
		break;
	default:
		location.reload();
	}
}

let $nodeModal = $('#nodeModal');
let $nodeForm = $('#nodeForm');

function updateNodeModal() {
	let activity = $nodeForm.find('#typeGroup').children("option:selected")
	.val();
	let usersNotyIds = getSelectIds($nodeForm, "#users");
	let actorsNotyIds = getSelectIds($nodeForm, "#actors");
	$.ajax({
		type : "POST",
		url : getCont() + "wf/workFlow/" + "updateNode",
		data : {
			id : $nodeForm.find('#id').val(),
			label : $nodeForm.find('#name').val(),
			workFlowId : $("#wfEdit").data("id"),
			x : $nodeForm.find('#x').val(),
			y : $nodeForm.find('#y').val(),
			group : activity,
			statusId : $nodeForm.find("#statusId").children("option:selected")
			.val(),
			sendEmail : $nodeForm.find('#sendEmail').prop('checked'),
			usersIds : JSON.stringify(usersNotyIds),
			actorsIds : JSON.stringify(actorsNotyIds),
		},
		success : function(response) {
			ajaxUpdateNode(response)
		},
		error : function(x, t, m) {
			console.log(x);
			console.log(t);
			console.log(m);
			notifyAjaxError(x, t, m);
		}
	});
}

function ajaxUpdateNode(response) {
	switch (response.status) {
	case 'success':
		//console.log(response.obj);
		//console.log(nodes);
		//console.log( $network.getScale());
		//console.log( $network.getViewPosition());
		try {
			nodes.update({
				id : response.obj.id,
				label : response.obj.label,
				group : response.obj.group,
				x : response.obj.x,
				y : response.obj.y,
				status : response.obj.status,
				sendEmail : response.obj.sendEmail,
				users : response.obj.users,
				actors : response.obj.actors
			});
		}catch(err){
			//console.log(err);
			 nodes = new vis.DataSet([]);
			 edges = new vis.DataSet([]);
			 x=$network.getViewPosition().x;
			 y=$network.getViewPosition().y;
			 scales=$network.getScale();
			  position = {
						x : x,
						y : y
				};
			  data = {
						nodes : nodes,
						edges : edges
				};
			  $network.getScale();
			  $network.getViewPosition();
			loadWorkFlow();
		}
		
		$network.redraw();
		$nodeModal.modal('hide');
		break;
	case 'fail':
		console.log(response);
		break;
	case 'exception':
		swal("Error!", response.exception, "error")
		break;
	default:
		console.log(response.status);
	}
}

function updateNodePosition(nodeId, params) {
	let node = nodes.get(nodeId);
	let position = params.pointer.canvas;
	if (typeof node.id !== 'undefined') {
		$.ajax({
			type : "POST",
			url : getCont() + "wf/workFlow/" + "updateNodePosition",
			data : {
				id : node.id,
				workFlowId : $("#wfEdit").data("id"),
				x : position.x,
				y : position.y,
			},
			success : function(response) {
				ajaxUpdateNode(response)
			},
			error : function(x, t, m) {
				console.log(x);
				console.log(t);
				console.log(m);
				notifyAjaxError(x, t, m);
			}
		});
	}
}

function saveEdge(edgeData, callback) {
	$.ajax({
		type : "POST",
		url : getCont() + "wf/workFlow/" + "saveEdge",
		data : {
			id : 0,
			nodeFromId : edgeData.from,
			nodeToId : edgeData.to,
		},
		success : function(response) {
			ajaxEdgeNode(response, edgeData, callback);
		},
		error : function(x, t, m) {
			console.log(x);
			console.log(t);
			console.log(m);
		}
	});
}

function ajaxEdgeNode(response, edgeData, callback) {
	switch (response.status) {
	case 'success':
		edgeData.id = response.obj.id;
		callback(edgeData);
		break;
	case 'fail':
		swal("Error!", response.exception, "error")
		break;
	case 'exception':
		swal("Error!", response.exception, "error")
		break;
	default:
		console.log(response.status);
	}
}

function deleteEdge(element) {
	$.ajax({
		type : "DELETE",
		url : getCont() + "wf/workFlow/" + "deleteEdge/" + element,
		timeout : 60000,
		data : {},
		success : function(response) {
			ajaxDeleteEdge(response);
		},
		error : function(x, t, m) {
			notifyAjaxError(x, t, m);
		}
	});
}

function ajaxDeleteEdge(response) {
	switch (response.status) {
	case 'success':
		removeEdge(response.obj)
		$network.enableEditMode();
		break;
	case 'fail':
		swal("Error!", response.exception, "error")
		break;
	case 'exception':
		swal("Error!", response.exception, "warning")
		break;
	default:
		location.reload();
	}
}

function removeNode(id) {
	try {
		nodes.remove({
			id : id
		});
	} catch (err) {
		alert(err);
	}
}

function removeEdge(id) {
	try {
		edges.remove({
			id : id
		});
	} catch (err) {
		alert(err);
	}
}

//show nodes info
function showNodes() {
	let listNodes = $network.canvas.body.nodes;
	$.each(listNodes, function(index, value) {
		if (typeof value !== "undefined") {
			console.log(value);
		}
	});
}

function showEdges() {
	for (var i = 1; i <= edges.length; i++) {
		console.log(edges.get(i));
	}
	console.log($network.canvas.body.nodes);

}

function existEdge(edgeData) {
	var found = edges.get({
		filter: function (item) {
			return (item != null && item.to == edgeData.to && item.from == edgeData.from);
		}
	});
	return  found.length > 0;
}

function existEdgeInvert(edgeData) {
	var found = edges.get({
		filter: function (item) {
			return (item != null && item.to == edgeData.from && item.from == edgeData.to);
		}
	});
	return  found.length > 0;
}
function closeNodeModal() {
	$nodeModal.modal('hide');
}

function openUpdateNodeModal(properties) {
	if (typeof properties.nodes[0] !== 'undefined') {
		let tempNode = nodes.get(properties.nodes[0]);
		$nodeForm.find('#users option').removeAttr('selected');
		$nodeForm.find('#actors option').removeAttr('selected');
		$nodeForm.find('a[href="#tabHome"]').click();
		$nodeForm[0].reset();
		$nodeForm.find("#statusId").selectpicker('val', '');
		if (tempNode.status != null)
			$nodeForm.find("#statusId").selectpicker('val', tempNode.status.id);
		$nodeForm.find("#typeGroup").selectpicker('val', tempNode.group);
		$nodeForm.find('#sendEmail').prop('checked', tempNode.sendEmail);
		$nodeForm.find('#id').val(tempNode.id);
		$nodeForm.find('#name').val(tempNode.label);
		$nodeForm.find('#x').val(tempNode.x);
		$nodeForm.find('#y').val(tempNode.y);


		if(typeof tempNode.users !== 'undefined'){
			for (var i = 0, l = tempNode.users.length; i < l; i++) {
				$nodeForm.find('#users option').each(
						function(index, element) {
							if (element.id == tempNode.users[i].id) {
								$nodeForm.find(
										"#users option[id='" + element.id + "']")
										.attr("selected", "selected");
							}
						});
			}
		}

		if(typeof tempNode.actors !== 'undefined'){
			for (var i = 0, l = tempNode.actors.length; i < l; i++) {
				$nodeForm.find('#actors option').each(
						function(index, element) {
							if (element.id == tempNode.actors[i].id) {
								$nodeForm.find(
										"#actors option[id='" + element.id + "']")
										.attr("selected", "selected");
							}
						});
			}
		}
		$nodeForm.find('#actors').multiSelect("refresh");
		$nodeForm.find('#users').multiSelect("refresh");
		$nodeModal.modal('show');
	}
}