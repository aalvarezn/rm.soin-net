var ifc = 0;
var ift = 0;
$(function() {
	var table2 = $('#attachedFilesTable').DataTable({
		"language" : {
			"emptyTable" : "No existen registros",
			"zeroRecords" : "No existen registros"
		},
		"searching" : false,
		"paging" : false
	});

	$("input[type=file]").change(function() {
		var fileName = $(this).val();
		var files = $('#files')[0].files;

		for (var i = 0, f; f = files[i]; i++) {
			if (!existFile(f.name)) {
				appendRowTableFile(f);
			}
		}
	});
});

function existFile(nameFile) {
	var exist = false;
	var tableRow = $("#tableFiles tbody td").filter(function() {
		if (($(this).text() == nameFile)) {
			exist = true;
		}
	}).closest("tr");
	return exist;

}

function appendRowTableFile(f) {
	ift++;
	var tableFile = $('#tableFiles tbody');
	var newRowContent = '<tr id="'
			+ ift
			+ '"><td>'
			+ f.name
			+ '<div id="plcI_'
			+ ift
			+ '"></div></td><td>'
			+ kFormatter(f.size)
			+ '</td>'
			+ '<td><div id="lod_if_'
			+ ift
			+ '" style="display:none;" class="loaderInput"></div></td>'
			+ '<td class="align-right">'
			+ '<a onclick="deleteFile('
			+ ift
			+ ')" download="" class=""> <i class="material-icons gris" style="font-size: 30px;"><span class="lnr lnr-cross-circle"></span></i>'
			+ '</a>' + '</td>'

	'</tr>';
	tableFile.append(newRowContent);

	var x = $("#files");
	var y = x.clone();
	y.attr("id", "file_" + ift);
	y.css('display', 'none');
	y.insertAfter("#plcI_" + ift);
}

function deleteFile(num) {
	$('#tableFiles tbody tr#' + num).remove();
}

function kFormatter(num) {
	if (Math.abs(num) > 999 && Math.abs(num) < 999999) {
		return Math.abs(num) > 999 ? Math.sign(num)
				* ((Math.abs(num) / 1000).toFixed(1)) + 'Kb' : Math.sign(num)
				* Math.abs(num)
	}
	if (Math.abs(num) > 999999) {
		return Math.abs(num) > 999 ? Math.sign(num)
				* ((Math.abs(num) / 1000000).toFixed(1)) + 'Mb' : Math
				.sign(num)
				* Math.abs(num)
	}

}

function openAddFileModal() {
	$('#addFileModal').modal('show');
}

function closeAddFileModal() {
	$('#addFileModal').modal('hide');
	$("#tableFiles tbody tr").remove();
}

$('#addFileModal input[type=file]').change(function() {
	$(this).parent().parent().find('label').text(this.value);
});

function uploads() {

	var cont = getCont();
	var form = $('#addFileModal #files[]')[0];
	var formData = new FormData(form)
}

function upload() {
	$('#addFileModal table input[type=file]').each(function(index, value) {
		var idRow = $(this).closest('tr').prop('id');
		var f = $(this)[0].files;
		uploadInputFile(f, idRow);
	});

	if ($('#addFileModal table input[type=file]').length == 0) {
		closeAddFileModal();
		swal("Correcto!", "Archivos agregado correctamente.", "success", 2000)
	}

}

function uploadInputFile(f, idRow) {
	var cont = getCont()
	var formData = new FormData();
	formData
			.append('file', $('#addFileModal table #file_' + idRow)[0].files[0]);
	// Ajax call for file uploaling
	var ajaxReq = $.ajax({
		url : cont + "file/" + "singleUploadRFC-" + $('#rfcId').val(),
		timeout : 60000,
		type : 'POST',
		data : formData,
		cache : false,
		contentType : false,
		processData : false,
		async : false,
		success : function(response) {
			responseFileUpload(response, idRow);
		},
		error : function(x, t, m) {
			notifyAjaxError(x, t, m);
		},
		xhr : function() {
			// Get XmlHttpRequest object
			var xhr = $.ajaxSettings.xhr();
			// Set onprogress event handler
			xhr.upload.onprogress = function(event) {
				$('#lod_if_' + idRow).css('display', 'block');
			};
			return xhr;
		},
		beforeSend : function(xhr) {
			$('#lod_if_' + idRow).css('display', 'block');
		}
	});
}

function responseFileUpload(response, idRow) {

	switch (response.status) {
	case 'success':
		deleteFile(idRow);
		addReleaseFileRow(response.obj);
		reloadPreview();
		break;
	case 'fail':
		swal("Error!", response.exception, "error")
		break;
	case 'exception':
		swal("Error!", response.exception, "error")
		break;
	default:
		location.reload();
	}

}

function addReleaseFileRow(rfcFile) {

	var attachedFilesTable = $('#attachedFilesTable').DataTable();
	var myDate = new Date(rfcFile.revisionDate);
	if ($('table#attachedFilesTable ').find('#' + rfcFile.id).length != 0) {
		attachedFilesTable.row($('#attachedFilesTable #' + rfcFile.id))
				.remove().draw();
	}

	attachedFilesTable.row
			.add(
					[
						rfcFile.name,
							myDate.toLocaleString(),
							'<div style="text-align: center">'
									+ '<div class="iconLine">'
									+ '<a onclick="deleteReleaseFile('
									+ rfcFile.id
									+ ')" download="" class=""> <i class="material-icons gris" style="font-size: 30px;">delete</i>'
									+ '</a>'
									+ '<a href="'
									+ getCont()
									+ 'file/singleDownloadRFC-'
									+ rfcFile.id
									+ '" download="" class=""> <i class="material-icons gris" style="font-size: 30px;">cloud_download</i>'
									+ '</a>' + '</div>' + '</div>' ]).node().id = rfcFile.id;
	attachedFilesTable.draw();
}

function deleteReleaseFile(id) {
	Swal.fire({
		  buttons: { 
			  cancel: true, 
			  confirm: true,
		  },
		  title: '\u00BFEst\u00e1s seguro que desea eliminar?',
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
				ajaxDeleteReleaseFile(id);
			}		
		});
}

function ajaxDeleteReleaseFile(id) {

	var cont = getCont();
	// Ajax call for file uploaling
	var ajaxReq = $.ajax({
		url : cont + "file/" + "deleteFileUploadRFC-" + id,
		timeout : 60000,
		type : 'DELETE',
		data : {},
		success : function(response) {
			responseDeleteReleaseFile(response);
			reloadPreview();
		},
		error : function(x, t, m) {
			notifyAjaxError(x, t, m);
		}
	});

}
function responseDeleteReleaseFile(response) {

	switch (response.status) {
	case 'success':
		var attachedFilesTable = $('#attachedFilesTable').DataTable();
		attachedFilesTable.row($('#attachedFilesTable #' + response.data))
				.remove().draw();
		swal("Correcto!", "Archivos eliminado correctamente.", "success", 2000)
		break;
	case 'fail':
		swal("Error!", response.exception, "error")
		break;
	case 'exception':
		swal("Error!", response.exception, "error")
		break;
	default:
		location.reload();
	}

}

function reloadPreview() {
	var src = $("#tinySummary").attr("src")
	$("#tinySummary").attr("src", src)
}
