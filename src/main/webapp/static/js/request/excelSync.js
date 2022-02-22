function openExcelSync() {
	blockUI();
	Swal.fire({
		title: '\u00BFEst\u00e1s seguro que desea actualizar los requerimientos?',
		text: "Esta acci\u00F3n se ejecutar\u00e1 en segundo plano y puede tomar varios minutos.",
		icon: 'question',
		showCancelButton: true,
		customClass: 'swal-wide',
		cancelButtonText: 'Cancelar',
		cancelButtonColor: '#f14747',
		confirmButtonColor: '#3085d6',
		confirmButtonText: 'Aceptar',
	}).then((result) => {
		if(result.value){
			syncExcel();
		}		
	});
}

function syncExcel(){
	blockUI();
	$.ajax({
		type : "POST",
		url : getCont() + "admin/request/" + "syncExcel",
		data : {
		},
		success : function(response) {
			ajaxSyncExcel(response);
		},
		error : function(x, t, m) {
			console.log(x);
			console.log(t);
			console.log(m);
			notifyAjaxError(x, t, m);
		}
	});
}

function ajaxSyncExcel(response){
	switch (response.status) {
	case 'success':
		swal("Correcto!", "Proceso iniciado, recarge la pantalla en unos minutos.", "success", 2000)
		break;
	case 'fail':
		swal("Error!", response.exception, "error")
		break;
	case 'exception':
		swal("Error!", response.exception, "error")
		break;
	default:
		console.log(response.status);
	unblockUI();
	}
}
