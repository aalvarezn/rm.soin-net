var ios = 0;
var $objectItemForm = $('#objectItemForm');
$(function() {
	$('#sqlObjectTable .tagInit').tagsInput();
});

$("#sqlObjectTable input[type = checkbox]").change(function() {
	if (this.checked) {
		$(this).val("1");
	} else {
		$(this).val("0");
	}
});

function changeSelectOption(origin, destiny) {
	if ($objectItemForm.find("#" + origin).children("option:selected").text() == 'Base Datos') {
		$.each($objectItemForm.find("#" + destiny + " option"), function(index,
				value) {
			if (value.innerText == 'Base Datos') {
				$objectItemForm.find("#" + destiny).selectpicker('val',
						value.id);
			}
		});
	}
}

function openObjectItemModal() {
	$objectItemForm[0].reset();
	$objectItemForm.focus();
	$('#addObjectItemModal').modal('show');
}

function closeObjectItemModal() {
	$('#addObjectItemModal').modal('hide');
	clearObjectItemModal();
}

function clearObjectItemModal() {
	$objectItemForm[0].reset();
	$objectItemForm.find("#typeObject").selectpicker('val', '');
	$objectItemForm.find("#itemConfiguration").selectpicker('val', '');

	$('#addObjectItemModal input').parent().attr("class", "form-line");
	$('#addObjectItemModal .fieldError').css("visibility", "hidden");

}
function responseAjaxSendReleaseObject(response) {
	switch (response.status) {
	case 'success':
		addRowObjectTable(response.data);
		modifyDependency(response.obj);
		closeObjectItemModal();
		swal("Correcto!", "Objeto guardado correctamente.",
				"success", 2000)
		break;
	case 'fail':
		swal("Error!", response.exception, "error")
		break;
	case 'exception':
		swal("Error!", response.exception, "error")
		break;
	default:
		unblockUI();
	}
}

function removeDependency(obj) {
	if (obj != null) {
		deleteDependency(obj.id, true);
		deleteDependency(obj.id, false);
	}
}
function modifyDependency(obj) {
	if (obj != null) {
		deleteDependency(obj.id, true);
		deleteDependency(obj.id, false);
		$("#listTechnicalsDependencies ul").append(
				'<li id="' + obj.id + '" value="' + obj.id
						+ '" class="nav-item dependency" >' + obj.releaseNumber
						+ ' </li>');
	}
}

function addObjectItem() {
	if (validAddObjectItem()) {
//		blockUI();
		var cont = getCont();
		var id = $('#generateReleaseForm #release_id').val();
		$.ajax({
			type : "POST",
			url : cont + "release/" + "addConfigurationItem/" + id,
			timeout : 60000,
			data : {
				// Informacion general
				name : $objectItemForm.find('#name').val(),
				revisionSVN : $objectItemForm.find('#revision').val(),
				revisionDate : formatDate($objectItemForm.find('#revisionDate')
						.val()),
				descriptionItem : $objectItemForm.find('#description').val(),
				objectType : $objectItemForm.find('#typeObject').children(
						"option:selected").val(),
				objectConfigurationItem : $objectItemForm.find(
						'#itemConfiguration').children("option:selected").val()
			},
			success : function(response) {				
				responseAjaxSendReleaseObject(response);
			},
			error : function(x, t, m) {
				notifyAjaxError(x, t, m);
			}
		});
	}
}
function addRowObjectTable(objId) {
	var table = $('#configurationItemsTable').DataTable();
	var typeObject = $objectItemForm.find("#typeObject").children(
			"option:selected");
	var itemConfiguration = $objectItemForm.find("#itemConfiguration")
			.children("option:selected");

	table.row
			.add(
					[
							$objectItemForm.find("#name").val(),
							$objectItemForm.find("#description").val(),
							$objectItemForm.find("#revision").val(),
							formatDateCustom($objectItemForm.find(
									"#revisionDate").val(), '/'),
							'<input id="'
									+ typeObject.val()
									+ '" type="text" name="typeObject" value="'
									+ typeObject.text()
									+ '" readonly="" style="border-color: transparent;background-color: transparent;">',
							'<input id="'
									+ itemConfiguration.val()
									+ '" type="text" name="itemConfiguration" value="'
									+ itemConfiguration.text()
									+ '" readonly="" style="border-color: transparent;background-color: transparent;">',
							'<div style="text-align: center"> <i onclick="deleteconfigurationItemsRow('
									+ objId
									+ ')" class="material-icons gris" style="font-size: 30px;">delete</i></div>' ])
			.node().id = objId;
	table.draw();

	if (itemConfiguration.text() == 'Base Datos') {
		triggerDataBaseFile(objId, $objectItemForm.find("#name").val());
	}
	closeObjectItemModal();
}

function triggerDataBaseFile(objId, name) {
	var sqlObjectTable = $('#sqlObjectTable').DataTable();
	sqlObjectTable.row
			.add(
					[
							name,
							'<div class="switch"> <label>NO<input id="obj_sql_exec_'
									+ objId
									+ '" type="checkbox" value="0"><span class="lever"></span>S&Iacute; </label></div>',
							'<input id="form-tags-'
									+ objId
									+ '" name="tags-1" type="text" class="tag_input" value="">'
									+ '<div class="form-group m-b-0i"><label id="form-tags-'
									+ objId
									+ '_error" maxlength="150" class="error fieldError"'
									+ 'for="name" style="visibility: hidden;">Campo Requerido.</label></div>',
							'<div class="switch"> <label>NO<input id="obj_sql_rp_'
									+ objId
									+ '" type="checkbox" value="0"><span class="lever"></span>S&Iacute; </label></div>' ])
			.node().id = objId;
	sqlObjectTable.draw();

	$('#form-tags-' + objId).tagsInput();

	$("#obj_sql_exec_" + objId).change(function() {
		if (this.checked) {
			$(this).val("1");
		} else {
			$(this).val("0");
		}
	});

	$("#obj_sql_rp_" + objId).change(function() {
		if (this.checked) {
			$(this).val("1");
		} else {
			$(this).val("0");
		}
	});

	$("#file-input_" + objId).change(function() {
		var fileCount = this.files.length;
		$(this).prev().find('span').html(fileCount);
	})
}

function deleteconfigurationItemsRow(id) {
	blockUI();
	var cont = getCont();
	var idRelease = $('#generateReleaseForm #release_id').val();
	var ajaxReq = $.ajax({
		url : cont + "release/" + "deleteConfigurationItem/" + idRelease + "/"
				+ id,
		timeout : 60000,
		type : 'DELETE',
		data : {},
		success : function(response) {
			responseAjaxDeleteConfigurationItem(response, id);
		},
		error : function(x, t, m) {
			notifyAjaxError(x, t, m);
		}
	});
}

function responseAjaxDeleteConfigurationItem(response, id) {
	switch (response.status) {
	case 'success':
		var tableConfItems = $('#configurationItemsTable').DataTable();
		var tableSqlObject = $('#sqlObjectTable').DataTable();
		tableConfItems.row($('#configurationItemsTable #' + id)).remove()
				.draw();
		tableSqlObject.row($('#sqlObjectTable #' + id)).remove().draw();
		removeDependency(response.obj);
		swal("Correcto!", "Objeto eliminado correctamente.", "success", 2000)
		break;
	case 'fail':
		swal("Error!", response.exception, "error")
		break;
	case 'exception':
		swal("Error!", response.exception, "error")
		break;
	default:
		unblockUI();
	}
}

function validAddObjectItem() {
	var valid = true;
	$objectItemForm.find("input").parent().attr("class", "form-line");
	$objectItemForm.find(".fieldError").css("visibility", "hidden");

	$.each($objectItemForm.find("input"), function(index, value) {
		if ($.trim(value.value) == "") {
			$objectItemForm.find("#" + value.id + "_error").css("visibility",
					"visible");
			value.parentNode.className = "form-line focused error";
			valid = false;
		}
	});

	var typeObject = $objectItemForm.find("#typeObject").children(
			"option:selected").val();

	var itemConfiguration = $objectItemForm.find("#itemConfiguration")
			.children("option:selected").val();

	if ($.trim(typeObject) == "") {
		$objectItemForm.find("#typeObject_error").css("visibility", "visible");
		valid = false;
	}

	if ($.trim(itemConfiguration) == "") {
		$objectItemForm.find("#itemConfiguration_error").css("visibility",
				"visible");
		valid = false;
	}
	return valid;
}

function validObjectSql() {
	var valid = true;

	var table = $('#sqlObjectTable').DataTable();

	table.rows().data()
			.each(
					function(value, index) {

						if ($.trim($(
								'#sqlObjectTable #form-tags-' + value.DT_RowId)
								.val()) == "") {
							$(
									'#sqlObjectTable #form-tags-'
											+ value.DT_RowId + '_error').css(
									"visibility", "visible");
							valid = false;
						}
					});

	return valid;

}