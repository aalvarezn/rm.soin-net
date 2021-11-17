var tmpBRI = 0;
var tmpBFRI = 0;
var tmpCBRI = 0;
var $addDetailForm = $('#addDetailForm');
var $crontabForm = $('#crontabForm');
var $buttonForm = $('#buttonForm');
var $buttonFileForm = $('#buttonFileForm');

$(function() {
	$('.timeSelect i').on('click', function() {
		var type = $(this).data('type');
		$(this).toggleClass("active_square");
		var all = $(this).hasClass('active_square');
		selectAllItems(type, all);
	});

	$('.itemTime div').on('click', function() {
		$(this).toggleClass("active_square");
		var itemTimeAll = $(this).parent().parent().find('i');
		if(itemTimeAll.hasClass('active_square')){
			itemTimeAll.removeClass("active_square");
		}
	});

	$('.crb').on('click', function() {
		var type = $(this).data('type');
		changeCrontabButtonForm(type);
	});

	$crontabForm.find('input[type="checkbox"]').change(function() {
		if (this.checked) {
			$(this).val(1);
		} else {
			$(this).val(0);
		}
		enable_disableButton($crontabForm, this.id, this.checked);
	});

	$addDetailForm.find('input[type="checkbox"]').change(function() {
		if (this.checked) {
			$(this).val(1);
		} else {
			$(this).val(0);
		}
		enable_disableButton($addDetailForm, this.id, this.checked);
	});

	$buttonForm.find('input[type="checkbox"]').change(function() {
		if (this.checked) {
			$(this).val(1);
		} else {
			$(this).val(0);
		}
		enable_disableButton($buttonForm, this.id, this.checked);
	});
	
	$buttonFileForm.find('input[type="checkbox"]').change(function() {
		if (this.checked) {
			$(this).val(1);
		} else {
			$(this).val(0);
		}
	});
});

function enable_disableButton(form, name, enable) {
	if (name == 'executeDirectory') {
		form.find('#directoryName').prop("disabled", !enable);
	}
	if (name == 'executeUser') {
		form.find('#userName').prop("disabled", !enable);
	}
	if (name == 'waitCommand') {
		form.find('#timeCommand').prop("disabled", !enable);
	}
	if (name == 'principalPage') {
		form.find('#pageName').prop("disabled", !enable);
	}
}

var crontabTable = $releaseEditForm.find('#crontabTable').DataTable({
	responsive: true,
	"language" : {
		"emptyTable" : "No existen registros",
		"zeroRecords" : "No existen registros"
	},
	"searching" : false,
	"paging" : false,
});

var crontabButtonRowsTable = $('#crontabForm #buttonRowsTable').DataTable({
	responsive: true,
	"columnDefs": [
        {
            "targets": [ 0 ],
            "visible": false,
            "searchable": false
        }
    ],
	"language" : {
		"emptyTable" : "No existen registros",
		"zeroRecords" : "No existen registros"
	},
	"searching" : false,
	"paging" : false,
});

var buttonsTable = $releaseEditForm.find('#buttonsTable').DataTable({
	responsive: true,
	"columnDefs": [
        {
            "targets": [ 3 ],
            "visible": false,
            "searchable": false
        }
    ],
	"language" : {
		"emptyTable" : "No existen registros",
		"zeroRecords" : "No existen registros"
	},
	"searching" : false,
	"paging" : false,
});

var buttonRowsTable = $('#buttonForm #buttonRowsTable').DataTable({
	responsive: true,
	"columnDefs": [
        {
            "targets": [ 0 ],
            "visible": false,
            "searchable": false
        }
    ],
	"language" : {
		"emptyTable" : "No existen registros",
		"zeroRecords" : "No existen registros"
	},
	"searching" : false,
	"paging" : false,
});

var buttonsFileTable = $releaseEditForm.find('#buttonFilesTable').DataTable({
	responsive: true,
	"language" : {
		"emptyTable" : "No existen registros",
		"zeroRecords" : "No existen registros"
	},
	"searching" : false,
	"paging" : false,
});

var buttonFileRowsTable = $('#buttonFileForm #buttonFileRowsTable').DataTable({
	responsive: true,
	"columnDefs": [
        {
            "targets": [ 0 ],
            "visible": false,
            "searchable": false
        }
    ],
	"language" : {
		"emptyTable" : "No existen registros",
		"zeroRecords" : "No existen registros"
	},
	"searching" : false,
	"paging" : false,
});

function openCrontabForm() {
	$('#generateReleaseForm').hide();
	$('#crontabForm').show();
	$crontabForm.find('#editCrontab').hide();
	$crontabForm.find('#saveCrontab').show();
	changeCrontabButtonForm('crontab');
	clearCrontabForm();
	$addDetailForm.find('#addRow').data('type', 'crontab');
	window.scrollTo(0, 0);
}

function clearCrontabForm(){
	clearButtonForm($crontabForm, crontabButtonRowsTable);
	$crontabForm.find('div').removeClass("active_square");
	$crontabForm.find('i').removeClass("active_square");
}

function closeCrontabForm() {
	window.scrollTo(0, 0);
	crontabButtonRowsTable.rows().remove().draw();
	$crontabForm.find('div').removeClass("active_square");
	$crontabForm.find('i').removeClass("active_square");
	changeCrontabButtonForm('crontab');
	$('#crontabForm').hide();
	$('#generateReleaseForm').show();
}


function openEditCrontabForm(response, form){
	$('#generateReleaseForm').hide();
	$('#crontabForm').show();
	changeCrontabButtonForm('crontab');
	$crontabForm.find('#editCrontab').show();
	$crontabForm.find('#saveCrontab').hide();
	clearCrontabForm();
	window.scrollTo(0, 0);
	loadCrontabData(response, $crontabForm);
	loadButtonData(response.button, $crontabForm);
	loadButtonTable(form, response.button.detailsButtonCommands, crontabButtonRowsTable);
}

function loadCrontabData(crontab, form){
	clearButtonForm($buttonForm, buttonRowsTable);
	form.find('#crontab_id').val(crontab.id);
	form.find('#user').val(crontab.user);
	form.find('#commandCron').val(crontab.commandCron);
	form.find('#commandEntry').val(crontab.commandEntry);
	form.find('#descriptionCron').val(crontab.descriptionCron);
	if(crontab.active)
		activeInputCheckbox(form, 'isActive');
	
	activeSelectTime(crontab.minutes, 'minutes', form);
	activeSelectTime(crontab.hour, 'hour', form);
	activeSelectTime(crontab.days, 'days', form);
	activeSelectTime(crontab.month, 'month', form);
	activeSelectTime(crontab.weekDays, 'weekDays', form);
	
}

function activeSelectTime(time, tag, form){
	if(time == '*'){
		selectAllItems(tag, true);
	}else{
		let array = time.split(',');
		form.find('#'+tag+'Select').children('div').each(function (value, index) {
			if(array.indexOf(this.id) > -1){
				$(this).addClass("active_square");
			}
		});	
	}
}

function openButtonForm() {
	$('#generateReleaseForm').hide();
	$('#buttonForm').show();
	$buttonForm.find('#editButton').hide();
	$buttonForm.find('#saveButton').show();
	clearButtonForm($buttonForm, buttonRowsTable);
	window.scrollTo(0, 0);
}

function clearButtonForm(form, table){
	form[0].reset();
	form.find('.fieldError').css("visibility",
	"hidden");
	form.find('input[type="checkbox"]').val("0");
	form.find('#directoryName').prop("disabled", true);
	form.find('#userName').prop("disabled", true);
	form.find('#timeCommand').prop("disabled", true)
	form.find('#pageName').prop("disabled", true);
	table.rows().remove().draw();
}

function closeButtonForm() {
	window.scrollTo(0, 0);
	$('#buttonForm').hide();
	buttonRowsTable.rows().remove().draw();
	$('#generateReleaseForm').show();
}

function changeCrontabButtonForm(crontab) {
	if (crontab == 'crontab') {
		$('#button').hide();
		$('#crontab').show();
	} else {
		$('#crontab').hide();
		$('#button').show();
	}
	window.scrollTo(0, 0);
}

function selectAllItems(name, all) {
	if (all) {
		$('#' + name + 'Select div').addClass("active_square");
	} else {
		$('#' + name + 'Select div').removeClass("active_square");
	}

}

function openAddDetailModal(ele) {
	let form = ele.closest("form")[0].id;
	$addDetailForm[0].reset();
	$addDetailForm.find('input[type="checkbox"]').val('0');
	$addDetailForm.find('#formToAdd').val(form);
	$addDetailForm.find("#typeDetail").selectpicker('val', '');
	$('#addDetailModal').modal('show');

}

function closeAddDetailModal() {
	$('#addDetailModal').modal('hide');
}

function addButtonRow() {
	
	if(!validDetailForm()){
		return false;
	}
	var type = $addDetailForm.find('#formToAdd').val();
	let toform;
	let count;
	if (type == 'crontabForm') {
		toform = crontabButtonRowsTable;
		count = tmpCBRI--;
	} 
	if (type == 'buttonForm') {
		toform = buttonRowsTable;
		count = tmpBRI--;
	}
	
	if (type == 'buttonFileForm') {
		toform = buttonFileRowsTable;
		count = tmpBFRI--;
	}
	toform.row
			.add(
					[
							0,
							$addDetailForm.find('#name').val(),
							$addDetailForm.find('#description').val(),
							$addDetailForm.find('#typeDetail').children(
									"option:selected").val(),
							$addDetailForm.find('#typeText').val(),
							(($addDetailForm.find('#quotationMarks').val() == 1) ? 'Si'
									: 'No'),
							(($addDetailForm.find('#isRequired').val() == 1) ? 'Si'
									: 'No'),
							'<div style="text-align: center">'
									+ '<i onclick="deleteaButtonRow(' + count
									+ ', $(this))" '
									+ 'class="material-icons gris"'
									+ 'style="font-size: 30px;">delete</i>'
									+ '</div>' ]).node().id = count;
	toform.draw();
	closeAddDetailModal();

}

function deleteaButtonRow(index, ele) {
	let form = ele.closest("form")[0].id;
	let tableForm;
	if (form == 'crontabForm') {
		tableForm = crontabButtonRowsTable;
	} 
	if (form == 'buttonForm') {
		tableForm = buttonRowsTable;
	}
	if (form == 'buttonFileForm') {
		tableForm = buttonFileRowsTable;
	}
	
	tableForm.row('#' + index).remove().draw();
}

function showButtonErrors(errors, form) {
	form.find('.fieldError').css("visibility",
	"hidden");
	var error = errors;
	for (var i = 0; i < error.length; i++) {
		form.find(" #" + error[i].key + "_error").text(
				error[i].message);
		form.find(" #" + error[i].key + "_error").css("visibility",
				"visible");
	}
}

function getDetailsButton(table){
	let DetailsButtonCommands = [];
	let DetailButtonCommand = {};

	var data = table.rows().data();
	 data.each(function (value, index) {
		 DetailButtonCommand = {}
		 DetailButtonCommand ["id"] = value[0];
		 DetailButtonCommand ["name"] = value[1];
		 DetailButtonCommand ["description"] = value[2];
		 DetailButtonCommand ["typeName"] = value[3];
		 DetailButtonCommand ["typeText"] = value[4];
		 DetailButtonCommand ["quotationMarks"] = boolean(value[5]);
		 DetailButtonCommand ["isRequired"] = boolean(value[6]);
		 DetailsButtonCommands.push(DetailButtonCommand);
	 });
	 return DetailsButtonCommands;
}

function validDetailForm(){
	let result = true;
	$addDetailForm.find('.fieldError').css("visibility",
	"hidden");
	
	if(($addDetailForm.find('#name').val()).trim() == ""){
		$addDetailForm.find('#name_error').css("visibility",
		"visible");
		result = false;
	}
	
	if(($addDetailForm.find('#typeDetail').children(
	"option:selected").val()).trim() == ""){
		$addDetailForm.find('#typeDetail_error').css("visibility",
		"visible");
		result = false;
	}
	
	if(($addDetailForm.find('#description').val()).trim() == ""){
		$addDetailForm.find('#description_error').css("visibility",
		"visible");
		result = false;
	}
	
	return result;
	
}

function openEditButtonForm(response, form){
	$('#generateReleaseForm').hide();
	$('#buttonForm').show();
	$buttonForm.find('#editButton').show();
	$buttonForm.find('#saveButton').hide();
	$buttonForm[0].reset();
	buttonRowsTable.rows().remove().draw();
	window.scrollTo(0, 0);
	clearButtonForm($buttonForm, buttonRowsTable);
	loadButtonData(response, form);
	loadButtonTable(form, response.detailsButtonCommands, buttonRowsTable);
	
}

function loadButtonData(button, form){
	form.find('#button_id').val(button.id);
	form.find('#name').val(button.name);
	form.find('#description').val(button.description);
	form.find('#command').val(button.command);
	if(button.executeDirectory){
		activeInputCheckbox(form, 'executeDirectory');
		form.find('#directoryName').prop("disabled", false);
		form.find('#directoryName').val(button.directoryName);
	}
	if(button.executeUser){
		activeInputCheckbox(form, 'executeUser');
		form.find('#userName').prop("disabled", false);
		form.find('#userName').val(button.userName);
	}
	if(button.waitCommand){
		activeInputCheckbox(form, 'waitCommand');
		form.find('#timeCommand').prop("disabled", false);
		form.find('#timeCommand').val(button.timeCommand);
	}
	if(button.principalPage){
		activeInputCheckbox(form, 'principalPage');
		form.find('#pageName').prop("disabled", false);
		form.find('#pageName').val(button.pageName);
	}
	if(button.useUserEnvironment)
		activeInputCheckbox(form, 'useUserEnvironment');
	if(button.haveHTML)
		activeInputCheckbox(form, 'haveHTML');
	if(button.hideExecute)
		activeInputCheckbox(form, 'hideExecute');
	if(button.userminAvailability)
		activeInputCheckbox(form, 'userminAvailability');
	if(button.clearVariables)
		activeInputCheckbox(form, 'clearVariables');
}

function loadButtonTable(form, details, toform){
	var i;
	var detail;
	for (i = 0; i < details.length; i++) {
		detail = details[i];
		toform.row
		.add(
				[
					detail.id,
					detail.name,
					detail.description,
					detail.typeDetail.name,
					detail.typeText,
						((detail.quotationMarks) ? 'Si'
								: 'No'),
						((detail.isRequired) ? 'Si'
								: 'No'),
						'<div style="text-align: center">'
								+ '<i onclick="deleteaButtonRow(' + detail.id
								+ ', $(this))" '
								+ 'class="material-icons gris"'
								+ 'style="font-size: 30px;">delete</i>'
								+ '</div>' ]).node().id = detail.id;
toform.draw();
		
	} 
	
}

function addButtonTable(obj){
	buttonsTable.row
	.add(
			[
				obj.name,
				obj.description,
				obj.command,
				obj
				,
				' <div style="text-align: center"> '
					+' <i onclick="editButton('+obj.id+')" '
					+' class="material-icons gris" '
					+' style="font-size: 30px;">mode_edit</i> <i '
					+' onclick="deleteButton('+obj.id+')" '
					+' class="material-icons gris" '
					+' style="font-size: 30px;">delete</i> '
					+' </div> ' ])
	.node().id = obj.id;
	buttonsTable.draw();
}

function addCrontabTable(obj){
	crontabTable.row
	.add(
			[
				obj.commandCron,
				obj.user,
				obj.descriptionCron,
				obj.commandEntry,
				' <div style="text-align: center"> '
					+' <i onclick="editCrontab('+obj.id+')" '
					+' class="material-icons gris" '
					+' style="font-size: 30px;">mode_edit</i> <i '
					+' onclick="deleteCrontab('+obj.id+')" '
					+' class="material-icons gris" '
					+' style="font-size: 30px;">delete</i> '
					+' </div> ' ])
	.node().id = obj.id;
	crontabTable.draw();
}

// AJAX Button

function editButton(idButton){
	blockUI();
	var cont = getCont();
	$
	.ajax({
		type : "GET",
		url : cont + "button/" + "findButtonCommand/" + idButton,
		timeout: 60000,
		data : {},
		success : function(response) {
			if(response == null){
				swal(
						"Error!",
						"El bot\u00F3n seleccionado no existe.",
						"error");
			}else{
				openEditButtonForm(response, $buttonForm);
			}
			unblockUI();
		},
		error: function(x, t, m) {
			notifyAjaxError(x, t, m);
	    }
	});
}

function saveButtonCommand() {
	var form = "#generateReleaseForm";
	blockUI();
	var cont = getCont();
	$
			.ajax({
				type : "POST",
				url : cont + "button/" + "saveButtonCommand-"
						+ $(form + ' #release_id').val(),
				timeout: 60000,
				data : {
					id : 0,
					name : $buttonForm.find('#name').val(),
					description : $buttonForm.find('#description').val(),
					command : $buttonForm.find('#command').val(),
					executeDirectory : boolean($buttonForm.find('#executeDirectory')
							.val()),
					directoryName : $buttonForm.find('#directoryName').val(),
					executeUser : boolean($buttonForm.find('#executeUser').val()),
					userName : $buttonForm.find('#userName').val(),
					useUserEnvironment :boolean( $buttonForm
							.find('#useUserEnvironment').val()),
					haveHTML : boolean($buttonForm.find('#haveHTML').val()),
					hideExecute : boolean($buttonForm.find('#hideExecute').val()),
					userminAvailability : boolean($buttonForm.find(
							'#userminAvailability').val()),
					clearVariables : boolean($buttonForm.find('#clearVariables').val()),
					waitCommand : boolean($buttonForm.find('#waitCommand').val()),
					timeCommand : $buttonForm.find('#timeCommand').val(),
					principalPage : boolean($buttonForm.find('#principalPage').val()),
					pageName : $buttonForm.find('#pageName').val(),
					detailsButtonCommands : JSON
					.stringify(getDetailsButton(buttonRowsTable)),
				},
				success : function(response) {
					responseAjaxSaveButton(response);
				},
				error: function(x, t, m) {
					notifyAjaxError(x, t, m);
			    }
			});
}

function responseAjaxSaveButton(response){
	switch(response.status) {
	  case 'success':
		  addButtonTable(response.obj);
		  closeButtonForm();
		  swal("Correcto!", "Bot\u00F3n creado correctamente.", "success", 2000)
	    break;
	  case 'fail':
		  showButtonErrors(response.errors, $buttonForm);
		  swal("Formulario incompleto!", "El formulario posee campos obligatorios, favor verificar.",
			"warning")
	    break;
	  case 'exception':
		  swal("Error!", response.exception, "error")
	    break;
	  default:
		  console.log(response.status);
	  unblockUI();
	}
}

function updateButtonCommand() {
	var form = "#generateReleaseForm";
	blockUI();
	var cont = getCont();
	$
			.ajax({
				type : "POST",
				url : cont + "button/" + "updateButtonCommand-"
						+ $(form + ' #release_id').val(),
				timeout: 60000,
				data : {
					id :  $buttonForm.find('#button_id').val(),
					name : $buttonForm.find('#name').val(),
					description : $buttonForm.find('#description').val(),
					command : $buttonForm.find('#command').val(),
					executeDirectory : boolean($buttonForm.find('#executeDirectory')
							.val()),
					directoryName : $buttonForm.find('#directoryName').val(),
					executeUser : boolean($buttonForm.find('#executeUser').val()),
					userName : $buttonForm.find('#userName').val(),
					useUserEnvironment :boolean( $buttonForm
							.find('#useUserEnvironment').val()),
					haveHTML : boolean($buttonForm.find('#haveHTML').val()),
					hideExecute : boolean($buttonForm.find('#hideExecute').val()),
					userminAvailability : boolean($buttonForm.find(
							'#userminAvailability').val()),
					clearVariables : boolean($buttonForm.find('#clearVariables').val()),
					waitCommand : boolean($buttonForm.find('#waitCommand').val()),
					timeCommand : $buttonForm.find('#timeCommand').val(),
					principalPage : boolean($buttonForm.find('#principalPage').val()),
					pageName : $buttonForm.find('#pageName').val(),
					detailsButtonCommands : JSON
					.stringify(getDetailsButton(buttonRowsTable)),
				},
				success : function(response) {
					responseAjaxUpdateButton(response);
				},
				error: function(x, t, m) {
					notifyAjaxError(x, t, m);
			    }
			});
}

function responseAjaxUpdateButton(response){
	switch(response.status) {
	  case 'success':
		  buttonsTable.row($releaseEditForm.find('#buttonsTable #'+response.obj.id)).remove().draw();
		  addButtonTable(response.obj);
		  closeButtonForm();
		  swal("Correcto!", "Bot\u00F3n actualizado correctamente.", "success", 2000)
	    break;
	  case 'fail':
		  showButtonErrors(response.errors, $buttonForm);
		  swal("Formulario incompleto!", "El formulario posee campos obligatorios, favor verificar.",
			"warning")
	    break;
	  case 'exception':
		  swal("Error!", response.exception, "error")
	    break;
	  default:
		  console.log(response.status);
	  unblockUI();
	}
}

function deleteButton(idButton){
	blockUI();
	var cont = getCont();
	var ajaxReq = $
			.ajax({
				url : cont + "button/" + "deleteButtonCommand/" + idButton,
				timeout: 60000,
				type : 'DELETE',
				data : {},
				success : function(response) {
					responseAjaxDeleteButton(response, idButton);
				},
				error: function(x, t, m) {
					notifyAjaxError(x, t, m);
			    }
			});
}

function responseAjaxDeleteButton(response, idButton){
	switch (response.status) {
	case 'success':
		buttonsTable.row($releaseEditForm.find('#buttonsTable #'+idButton)).remove().draw();
		swal("Correcto!", "Bot\u00F3n eliminado correctamente.", "success", 2000)
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

// Seccion para funciones directas de crontabs

function showCrontabErrors(errors, form) {
	form.find('.fieldError').css("visibility",
	"hidden");
	var error = errors;
	let key = "";
	for (var i = 0; i < error.length; i++) {
		key = error[i].key;
		if(key.includes("button.")){
			key = key.replace('button.','');;
		}
		form.find(" #" + key + "_error").text(
				error[i].message);
		form.find(" #" + key + "_error").css("visibility",
				"visible");
	}
}

function getTimeElement(ele){
	let timeElement = '';
	if($crontabForm.find('[data-type="'+ele+'"]').hasClass("active_square"))
		return '*';
	
	$crontabForm.find('#'+ele+'Select').children('div').each(function (value, index) {
		if($(this).hasClass("active_square")){
			timeElement += this.id + ',';
		}
	});	
	if(timeElement.length > 0 && timeElement != '*'){
		timeElement = timeElement.slice(0,-1);
	}
	return timeElement;
}

function getButton(isNew){
	 let Button = {};
	 if(isNew){
		 Button ["id"] = 0; 
	 }else{
		 Button ["id"] = $crontabForm.find('#button_id').val();
	 }
	 Button ["name"] = $crontabForm.find('#name').val();
	 Button ["description"] = $crontabForm.find('#description').val();
	 Button ["command"] = $crontabForm.find('#command').val();
	 Button ["executeDirectory"] = boolean($crontabForm.find('#executeDirectory').val());
	 Button ["directoryName"] = $crontabForm.find('#directoryName').val();
	 Button ["executeUser"] = boolean($crontabForm.find('#executeUser').val());
	 Button ["userName"] = $crontabForm.find('#userName').val();
	 Button ["useUserEnvironment"] = boolean( $crontabForm.find('#useUserEnvironment').val());
	 Button ["haveHTML"] = boolean($crontabForm.find('#haveHTML').val());
	 Button ["hideExecute"] = boolean($crontabForm.find('#hideExecute').val());
	 Button ["userminAvailability"] = boolean($crontabForm.find('#userminAvailability').val());
	 Button ["clearVariables"] = boolean($crontabForm.find('#clearVariables').val());
	 Button ["waitCommand"] = boolean($crontabForm.find('#waitCommand').val());
	 Button ["timeCommand"] = $crontabForm.find('#timeCommand').val();
	 Button ["principalPage"] = boolean($crontabForm.find('#principalPage').val());
	 Button ["pageName"] = $crontabForm.find('#pageName').val();
	 Button ["detailsButtonCommands"] = JSON.stringify(getDetailsButton(crontabButtonRowsTable));
	 return Button;
}

// AJAX Crontab
function saveCrontabs() {
	var form = "#generateReleaseForm";
	blockUI();
	var cont = getCont();
	$
	.ajax({
		type : "POST",
		url : cont + "crontab/" + "saveCrontab-"
				+ $(form + ' #release_id').val(),
		timeout: 60000,
		data : {
			id : 0,
			user : $crontabForm.find('#user').val(),
			active : boolean($crontabForm.find('#isActive').val()),
			commandCron : $crontabForm.find('#commandCron').val(),
			commandEntry : $crontabForm.find('#commandEntry').val(),
			descriptionCron : $crontabForm.find('#descriptionCron').val(),
			minutes :  getTimeElement('minutes'),
			hour :  getTimeElement('hour'),
			days :  getTimeElement('days'),
			month :  getTimeElement('month'),
			weekDays :  getTimeElement('weekDays'),
			button: JSON.stringify(getButton(true)),
		},
		success : function(response) {
			responseAjaxSaveCrontab(response);
		},
		error: function(x, t, m) {
			notifyAjaxError(x, t, m);
	    }
	});
}


function responseAjaxSaveCrontab(response){
	switch (response.status) {
	case 'success':
		crontabTable.row($releaseEditForm.find('#crontabTable #'+response.obj.id)).remove().draw();
		buttonsTable.row($releaseEditForm.find('#buttonsTable #'+response.obj.button.id)).remove().draw();
		addCrontabTable(response.obj);
		addButtonTable(response.obj.button);
		closeCrontabForm();
		swal("Correcto!", "Crontab creado correctamente.", "success", 2000)
		break;
	case 'fail':
		showCrontabErrors(response.errors, $crontabForm);
		swal("Formulario incompleto!", "El formulario posee campos obligatorios, favor verificar.",
		"warning")
		break;
	case 'exception':
		swal("Error!", response.exception, "error")
		break;
	default:
		unblockUI();
	}
}


function deleteCrontab(idCrontab){
	blockUI();
	var cont = getCont();
	var ajaxReq = $
			.ajax({
				url : cont + "crontab/" + "deleteCrontab/" + idCrontab,
				timeout: 60000,
				type : 'DELETE',
				data : {},
				success : function(response) {
					responseAjaxDeleteCrontab(response);
				},
				error: function(x, t, m) {
					notifyAjaxError(x, t, m);
			    }
			});
}


function responseAjaxDeleteCrontab(response){
	switch (response.status) {
	case 'success':
		crontabTable.row($releaseEditForm.find('#crontabTable #'+response.obj.id)).remove().draw();
		buttonsTable.row($releaseEditForm.find('#buttonsTable #'+response.obj.button.id)).remove().draw();
		swal("Correcto!", "Crontab eliminado correctamente.", "success", 2000)
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

function editCrontab(idCrontab){
	blockUI();
	var cont = getCont();
	$
	.ajax({
		type : "GET",
		url : cont + "crontab/" + "findCrontab/" + idCrontab,
		timeout: 60000,
		data : {},
		success : function(response) {
			if(response == null){
				swal(
						"Error!",
						"El crontab seleccionado no existe.",
						"error");
			}else{
				openEditCrontabForm(response, $crontabForm);
			}
			unblockUI();
		},
		error: function(x, t, m) {
			notifyAjaxError(x, t, m);
	    }
	});
}

function updateCrontab() {
	var form = "#generateReleaseForm";
	blockUI();
	var cont = getCont();
	$
			.ajax({
				type : "POST",
				url : cont + "crontab/" + "updateCrontab-"
						+ $(form + ' #release_id').val(),
				timeout: 60000,
				data : {
					id :  $crontabForm.find('#crontab_id').val(),
					user : $crontabForm.find('#user').val(),
					active : boolean($crontabForm.find('#isActive').val()),
					commandCron : $crontabForm.find('#commandCron').val(),
					commandEntry : $crontabForm.find('#commandEntry').val(),
					descriptionCron : $crontabForm.find('#descriptionCron').val(),
					minutes :  getTimeElement('minutes'),
					hour :  getTimeElement('hour'),
					days :  getTimeElement('days'),
					month :  getTimeElement('month'),
					weekDays :  getTimeElement('weekDays'),
					button: JSON.stringify(getButton(false)),
				},
				success : function(response) {
					responseAjaxUpdateCrontab(response);
				},
				error: function(x, t, m) {
					notifyAjaxError(x, t, m);
			    }
			});
}

function responseAjaxUpdateCrontab(response){
	switch(response.status) {
	case 'success':
		crontabTable.row($releaseEditForm.find('#crontabTable #'+response.obj.id)).remove().draw();
		buttonsTable.row($releaseEditForm.find('#buttonsTable #'+response.obj.button.id)).remove().draw();
		addCrontabTable(response.obj);
		addButtonTable(response.obj.button);
		closeCrontabForm();
		swal("Correcto!", "Crontab modificado correctamente.", "success", 2000)		
		break;
	case 'fail':
		showCrontabErrors(response.errors, $crontabForm);
		swal("Formulario incompleto!", "El formulario posee campos obligatorios, favor verificar.",
		"warning")
		break;
	  case 'exception':
		  swal("Error!", response.exception, "error")
	    break;
	  default:
		  console.log(response.status);
	  unblockUI();
	}
}

// Seccion para funciones directas de boton de archivos

function openButtonFileForm() {
	$('#generateReleaseForm').hide();
	$('#buttonFileForm').show();
	$buttonFileForm.find('#editButton').hide();
	$buttonFileForm.find('#saveButton').show();
	clearButtonFileForm($buttonFileForm, buttonFileRowsTable);
	window.scrollTo(0, 0);
}

function clearButtonFileForm(form, table){
	form[0].reset();
	form.find('.fieldError').css("visibility",
	"hidden");
	form.find('input[type="checkbox"]').val("0");
	table.rows().remove().draw();
}

function closeButtonFileForm() {
	window.scrollTo(0, 0);
	$('#buttonFileForm').hide();
	buttonFileRowsTable.rows().remove().draw();
	$('#generateReleaseForm').show();
}

function loadButtonFileData(button, form){
	form.find('#button_id').val(button.id);
	form.find('#description').val(button.description);
	form.find('#descriptionHtml').val(button.descriptionHtml);
	form.find('#fileEdit').val(button.fileEdit);
	form.find('#owner').val(button.owner);
	form.find('#permissions').val(button.permissions);
	
	if(button.replaceVariables){
		activeInputCheckbox(form, 'replaceVariables');
	}
	if(button.userminAvailability){
		activeInputCheckbox(form, 'userminAvailability');
	}
	form.find('#commandBeforeEditing').val(button.commandBeforeEditing);
	form.find('#commandBeforeSaving').val(button.commandBeforeSaving);
	form.find('#commandBeforeExecuting').val(button.commandBeforeExecuting);
}

function openEditButtonFileForm(response, form){
	$('#generateReleaseForm').hide();
	$('#buttonFileForm').show();
	$buttonFileForm.find('#editButton').show();
	$buttonFileForm.find('#saveButton').hide();
	$buttonFileForm[0].reset();
	buttonFileRowsTable.rows().remove().draw();
	window.scrollTo(0, 0);
	clearButtonForm($buttonFileForm, buttonFileRowsTable);
	loadButtonFileData(response, form);
	loadButtonTable(form, response.detailsButtonFiles, buttonFileRowsTable);
}

function addButtonFileTable(obj){
	buttonsFileTable.row
	.add(
			[
				obj.description,
				obj.descriptionHtml,
				obj.fileEdit,
				' <div style="text-align: center"> '
					+' <i onclick="editButtonFile('+obj.id+')" '
					+' class="material-icons gris" '
					+' style="font-size: 30px;">mode_edit</i> <i '
					+' onclick="deleteButtonFile('+obj.id+')" '
					+' class="material-icons gris" '
					+' style="font-size: 30px;">delete</i> '
					+' </div> ' ])
	.node().id = obj.id;
	buttonsFileTable.draw();
}

// AJAX Button
function editButtonFile(idButton){
	blockUI();
	var cont = getCont();
	$
	.ajax({
		type : "GET",
		url : cont + "button/" + "findButtonFile/" + idButton,
		timeout: 60000,
		data : {},
		success : function(response) {
			if(response == null){
				swal(
						"Error!",
						"El bot\u00F3n seleccionado no existe.",
						"error");
			}else{
				openEditButtonFileForm(response, $buttonFileForm);
			}
			unblockUI();
		},
		error: function(x, t, m) {
			notifyAjaxError(x, t, m);
	    }
	});
}

function updateButtonFile() {
	var form = "#generateReleaseForm";
	blockUI();
	var cont = getCont();
	$
	.ajax({
		type : "POST",
		url : cont + "button/" + "updateButtonFile-"
				+ $(form + ' #release_id').val(),
		timeout: 60000,
		data : {
			id :  $buttonFileForm.find('#button_id').val(),
			description : $buttonFileForm.find('#description').val(),
			descriptionHtml : $buttonFileForm.find('#descriptionHtml').val(),
			fileEdit : $buttonFileForm.find('#fileEdit').val(),
			owner : $buttonFileForm.find('#owner').val(),
			permissions : $buttonFileForm.find('#permissions').val(),
			replaceVariables : boolean($buttonFileForm.find('#replaceVariables').val()),
			userminAvailability : boolean($buttonFileForm.find('#userminAvailability').val()),
			commandBeforeEditing : $buttonFileForm.find('#commandBeforeEditing').val(),
			commandBeforeSaving : $buttonFileForm.find('#commandBeforeSaving').val(),
			commandBeforeExecuting : $buttonFileForm.find('#commandBeforeExecuting').val(),
			detailsButtonFiles : JSON
			.stringify(getDetailsButton(buttonFileRowsTable)),
		},
		success : function(response) {
			responseAjaxUpdateButtonFile(response);
		},
		error: function(x, t, m) {
			notifyAjaxError(x, t, m);
	    }
	});
}

function responseAjaxUpdateButtonFile(response){
	switch(response.status) {
	  case 'success':
		  buttonsFileTable.row($releaseEditForm.find('#buttonFilesTable #'+response.obj.id)).remove().draw();
		  addButtonFileTable(response.obj);
		  closeButtonFileForm();
		  swal("Correcto!", "Bot\u00F3n modificado correctamente.", "success", 2000)
	    break;
	  case 'fail':
		  showButtonErrors(response.errors, $buttonFileForm);
		  swal("Formulario incompleto!", "El formulario posee campos obligatorios, favor verificar.",
			"warning")
	    break;
	  case 'exception':
		  swal("Error!", response.exception, "error")
	    break;
	  default:
		  console.log(response.status);
	  unblockUI();
	}
}


function saveButtonFile() {
	var form = "#generateReleaseForm";
	blockUI();
	var cont = getCont();
	$
			.ajax({
				type : "POST",
				url : cont + "button/" + "saveButtonFile-"
						+ $(form + ' #release_id').val(),
				timeout: 60000,
				data : {
					id :  0,
					description : $buttonFileForm.find('#description').val(),
					descriptionHtml : $buttonFileForm.find('#descriptionHtml').val(),
					fileEdit : $buttonFileForm.find('#fileEdit').val(),
					owner : $buttonFileForm.find('#owner').val(),
					permissions : $buttonFileForm.find('#permissions').val(),
					replaceVariables : boolean($buttonFileForm.find('#replaceVariables').val()),
					userminAvailability : boolean($buttonFileForm.find('#userminAvailability').val()),
					commandBeforeEditing : $buttonFileForm.find('#commandBeforeEditing').val(),
					commandBeforeSaving : $buttonFileForm.find('#commandBeforeSaving').val(),
					commandBeforeExecuting : $buttonFileForm.find('#commandBeforeExecuting').val(),
					detailsButtonFiles : JSON
					.stringify(getDetailsButton(buttonFileRowsTable)),
				},
				success : function(response) {
					responseAjaxSaveButtonFile(response);
				},
				error: function(x, t, m) {
					notifyAjaxError(x, t, m);
			    }
			});
}

function responseAjaxSaveButtonFile(response){
	switch(response.status) {
	  case 'success':
		  addButtonFileTable(response.obj);
		  closeButtonFileForm();
		  swal("Correcto!", "Bot\u00F3n creado correctamente.", "success", 2000)
	    break;
	  case 'fail':
		  showButtonErrors(response.errors, $buttonFileForm);
		  swal("Formulario incompleto!", "El formulario posee campos obligatorios, favor verificar.",
			"warning")
	    break;
	  case 'exception':
		  swal("Error!", response.exception, "error")
	    break;
	  default:
		  console.log(response.status);
	  unblockUI();
	}
}


function deleteButtonFile(idButton){
	blockUI();
	var cont = getCont();
	var ajaxReq = $
			.ajax({
				url : cont + "button/" + "deleteButtonFile/" + idButton,
				timeout: 60000,
				type : 'DELETE',
				data : {},
				success : function(response) {
					responseAjaxDeleteButtonFile(response, idButton);
				},
				error: function(x, t, m) {
					notifyAjaxError(x, t, m);
			    }
			});
}

function responseAjaxDeleteButtonFile(response, idButton){
	switch (response.status) {
	case 'success':
		buttonsFileTable.row($releaseEditForm.find('#buttonFilesTable #'+idButton)).remove().draw();
		swal("Correcto!", "Bot\u00F3n eliminado correctamente.", "success", 2000)
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

