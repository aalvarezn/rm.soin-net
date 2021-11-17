var nodes = new vis.DataSet([ {
	id : 1,
	label : 'Inicio',
	type : 'start',
	shape : 'box',
	color : '#00294c',
	font : {
		color : 'black'
	},
	group : 'start',
	x : -200,
	y : 0
}, {
	id : 2,
	label : 'Actividad',
	shape : 'box',
	color : '#00294c',
	font : {
		color : 'black'
	},
	group : 'action',
	x : 0,
	y : 0
}, {
	id : 4,
	label : 'Fin',
	shape : 'box',
	color : '#00294c',
	font : {
		color : 'black'
	},
	group : 'finish',
	x : 200,
	y : 0
} ]);

// create an array with edges
var edges = new vis.DataSet([ {
	from : 1,
	to : 2
}, {
	from : 2,
	to : 4
} ]);

// create a network
var container = document.getElementById('mynetwork');

// provide the data in the vis format
var data = {
	nodes : nodes,
	edges : edges
};

var sample = {
	edit : 'Editar',
	del : 'Eliminar seleccionado',
	back : 'Atras',
	addNode : 'Agregar Actividad',
	addEdge : 'Enlace',
	editNode : 'Edit Node',
	editEdge : 'Edit Edge',
	addDescription : 'Click in an empty space to place a new node.',
	edgeDescription : 'Seleccione la actividad y arrastre el enlace a otra',
	editEdgeDescription : 'Click on the control points and drag them to a node to connect to it.'
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
	locales : {
		sample : sample
	},
	locale : 'sample',
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
			if (edgeData.from === edgeData.to) {
				alert('No se permite autoenlaces');
				return;
			}
			if (typeof nodes.get(edgeData.to) !== "undefined") {
				if (nodes.get(edgeData.to).group === 'start') {
					return;
				}
			}
			existEdge();

			console.log(edgeData);
			callback(edgeData);
		},
		addNode : function(nodeData, callback) {
			nodeData.label = 'actividad';
			callback(nodeData);
		}
	}
};

$(function() {
	// initialize your network!
	var network = new vis.Network(container, data, options);
	// show properties
	network.on("doubleClick", function(properties) {
		doubleClickNode(properties);
	});

});
let $nodeModal = $('#nodeModal');
let $nodeForm = $('#nodeForm');
// add a node
function addNode() {
	try {
		nodes.add({
			id : 6,
			label : 6
		});
	} catch (err) {
		alert(err);
	}
}

// show nodes info
function showNodes() {
	for (var i = 1; i <= nodes.length; i++) {
		console.log('Nodo: ' + i);
		console.log(nodes.get(i));
	}
}

function existEdge() {
	for (var j = 1; j <= edges.length; j++) {
		console.log(edges.get(j));
	}
}

function closeNodeModal() {
	$nodeModal.modal('hide');
}

function doubleClickNode(properties) {
	if (typeof properties.nodes[0] !== 'undefined') {
		let tempNode = nodes.get(properties.nodes[0]);
		if (tempNode.group === 'start') {
			return;
		}
		$nodeForm.find('#idActivity').val(tempNode.id);
		$nodeModal.modal('show');
	}
}

function updateNodeModal() {

	let activity = ($nodeForm.find('#isFinal').prop("checked")) ? 'finish'
			: 'action';
	nodes.update({
		id : $nodeForm.find('#idActivity').val(),
		label : $nodeForm.find('#name').val(),
		group : activity
	});
	$nodeModal.modal('hide');
}