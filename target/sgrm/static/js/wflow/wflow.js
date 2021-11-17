let cont = getCont();
let $wflowTable = $('#wflowTable').DataTable({
	"language" : {
		"emptyTable" : "No existen registros",
		"zeroRecords" : "No existen registros"
	},
	"searching" : true,
	"paging" : true
});

let $wflowModal = $('#wflowModal');

function createWflowModal() {
	$wflowModal.modal('show');

}

function closeWflowModal() {
	$wflowModal.modal('hide');
}

function createWFlow() {
	alert('prevista para redirigir a la pagina changewflow');
	window.location = cont + "admin/wfprocess/change";
}

// -------- wfpackage --------

let $wfpackageTable = $('#wfpackageTable').DataTable({
	"language" : {
		"emptyTable" : "No existen registros",
		"zeroRecords" : "No existen registros"
	},
	"searching" : true,
	"paging" : true
});

$wfpackageModal = $('#wfpackageModal');
$wfpackageForm = $('#wfpackageForm');

function openwfpackageModal() {
	$wfpackageForm.find('#idpackage').val(0);
	$wfpackageModal.modal('show');
}

function closewfpackageModal() {
	$wfpackageModal.modal('hide');
}

function editwfpackageModal(index) {
	$wfpackageForm.find('#idpackage').val(index);
	$wfpackageModal.modal('show');
}

function packageModal() {
	let index = $wfpackageForm.find('#idpackage').val();
	// $wfpackageTable.cell("#" + index, 0).data(
	// $wfpackageForm.find('#name').val()).draw();
	// $wfpackageTable.cell("#" + index, 1).data(
	// $wfpackageForm.find('#description').val()).draw();
	// closewfpackageModal();
	// swal("Correcto!", "Texto de confirmacion", "success");
	blockUI();
	$.ajax({
		type : "POST",
		url : getCont() + "admin/wfpackages/" + "updatePackage",
		data : {
			id : index,
			name : $wfpackageForm.find('#name').val(),
			description : $wfpackageForm.find('#description').val()
		},
		success : function(response) {
			console.log(response);
			swal("Correcto!", "Texto de confirmacion", "success");
		},
		error : function(x, t, m) {
			notifyAjaxError(x, t, m);
		}
	});
}

// -------- ##wfpackage --------

