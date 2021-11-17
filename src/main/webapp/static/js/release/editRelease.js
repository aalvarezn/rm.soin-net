var $releaseEditForm = $('#generateReleaseForm');
let countAmbients, ambientInterval, $auxAmb;
$("#ambient").keyup(function() {
	$auxAmb = $(this).val();
	clearInterval(ambientInterval);
	ambientInterval = setInterval(function() {
		if (countAmbients === 0 && $auxAmb.length != 0) {
			notifyInfo('Sin resultados');
		}
		clearInterval(ambientInterval);
	}, 1000);
});

let countModifiedComponent, modifiedComponentInterval, $auxMod;
$("#modifiedComponent").keyup(function() {
	$auxMod = $(this).val();
	clearInterval(modifiedComponentInterval);
	modifiedComponentInterval = setInterval(function() {
		if (countModifiedComponent === 0 && $auxMod.length != 0) {
			notifyInfo('Sin resultados');
		}
		clearInterval(modifiedComponentInterval);
	}, 1000);
});

let countDependencyFunctional;
jQuery("#dependencyFunctional")
		.autocomplete(
				{
					source : function(request, response) {
						$.get(getCont()
								+ "release/"
								+ "releaseAutoComplete-"
								+ $releaseEditForm
										.find('#dependencyFunctional').val()
								+ "-"
								+ $releaseEditForm.find('#release_id').val(),
								function(data) {
									response(data);
								});
					},
					response : function(event, ui) {
						countDependencyFunctional = ui.content.length;
						if (countDependencyFunctional === 0) {
							notifyInfo('Sin resultados');
						}
					},
					select : function(e, ui) {
						if (!($('#listFunctionalsDependencies ul #'
								+ ui.item.id).length)) {
							addDependency(ui.item, true)
						}
						$(this).val('');
						return false;
					},
					delay : 0
				});

let countDependencyTechnical;
jQuery("#dependencyTechnical")
		.autocomplete(
				{
					source : function(request, response) {
						$.get(getCont()
								+ "release/"
								+ "releaseAutoComplete-"
								+ $releaseEditForm.find('#dependencyTechnical')
										.val() + "-"
								+ $releaseEditForm.find('#release_id').val(),
								function(data) {
									response(data);
								});
					},
					response : function(event, ui) {
						countDependencyTechnical = ui.content.length;
						if (countDependencyTechnical === 0) {
							notifyInfo('Sin resultados');
						}
					},
					select : function(e, ui) {
						if (!($('#listTechnicalsDependencies ul #' + ui.item.id).length)) {
							addDependency(ui.item, false)
						}
						$(this).val('');
						return false;
					},
					delay : 0
				});

$(function() {
	$('.nav-tabs > li a[title]').tooltip();
	// Wizard
	$('a[data-toggle="tab"]').on('show.bs.tab', function(e) {
		var $target = $(e.target);
		if ($target.parent().hasClass('disabled')) {
			return false;
		}
	});

	$(".next-step").click(function(e) {
		var $active = $('.wizard .nav-tabs li.active');
		$active.next().removeClass('disabled');
		nextTab($active);
	});

	$(".prev-step").click(function(e) {
		var $active = $('.wizard .nav-tabs li.active');
		prevTab($active);
	});

	loadAutoCompleteAmbient('');
	loadAutoCompleteModifiedComponent('');

	var table1 = $('.tableIni').DataTable({
		"language" : {
			"emptyTable" : "No existen registros",
			"zeroRecords" : "No existen registros"
		},
		"searching" : false,
		"paging" : false
	});

	$("#generateReleaseForm textarea").parent().removeClass('focused');
	$("#generateReleaseForm input").parent().removeClass('focused');
	$("#generateReleaseForm #ambientsObservations").hide();

	if ($("#generateReleaseForm #downRequired").is(":checked")) {
		$("#generateReleaseForm #environmentsActions").show();
	}

	validEmptySection();
	$releaseEditForm.find('input[type="checkbox"]').change(function() {
		if (this.checked) {
			$(this).val(1);
		} else {
			$(this).val(0);
		}
		enable_disableButton($crontabForm, this.id, this.checked);
	});
});

/* -------------------------- Release -------------------------- */

function loadAutoCompleteModifiedComponent(search) {
	$
			.ajax({
				type : "GET",
				url : getCont() + "release/" + "modifiedComponentAutoComplete",
				timeout: 60000,
				data : {},
				success : function(response) {
					autocompleteModifiedComponent(response);
				},
				error: function(x, t, m) {
					notifyAjaxError(x, t, m);
			    }
			});
}

function autocompleteModifiedComponent(data) {
	jQuery("#modifiedComponent")
			.autocomplete(
					{
						source : data,
						response : function(event, ui) {
							countModifiedComponent = ui.content.length;
						},
						select : function(e, ui) {
							if (!($('#listComponents ul #' + ui.item.id).length)) {
								$("#listComponents ul")
										.prepend(
												'<li id="'
														+ ui.item.id
														+ '" class="nav-item dependency" >'
														+ ui.item.name
														+ ' <span class="flr m-b--10" style="margin-top: -3px;"> <a onclick="deleteModifiedComponent('
														+ ui.item.id
														+ ')" title="Borrar"><i class=" gris">'
														+ '<span class="lnr lnr-cross-circle p-l-5"></span></i></a>'
														+ ' </span>' + ' </li>');
							}
							$(this).val('');
							return false;
						},
						delay : 0
					});
}

function deleteModifiedComponent(id) {
	$('#listComponents ul #' + id).remove();
}

function loadAutoCompleteRelease(search, isFunctional) {
	$
			.ajax({
				type : "GET",
				url : getCont() + "release/" + "releaseAutoComplete-" + search,
				timeout: 60000,
				data : {
					release_id : $('#generateReleaseForm #release_id').val()
				},
				success : function(response) {
					autocompleteRelease(response, isFunctional);
				},
				error: function(x, t, m) {
					notifyAjaxError(x, t, m);
			    }
			});
}

function addDependency(item, isFunctional) {
	if (isFunctional) {
		$("#listFunctionalsDependencies ul")
				.append(
						'<li id="'
								+ item.id
								+ '" value="'
								+ item.value
								+ '" class="nav-item dependency" >'
								+ item.name
								+ ' <span class="flr m-b--10" style="margin-top: -3px;"> <a onclick="deleteDependency('
								+ item.id
								+ ','
								+ isFunctional
								+ ')" title="Borrar"><i class="gris"><span class="lnr lnr-cross-circle p-l-5"></span></i></a>'
								+ ' </span>' + ' </li>');
	} else {
		$("#listTechnicalsDependencies ul")
				.append(
						'<li id="'
								+ item.id
								+ '" value="'
								+ item.value
								+ '" class="nav-item dependency" >'
								+ item.name
								+ ' <span class="flr m-b--10" style="margin-top: -3px;"> <a onclick="deleteDependency('
								+ item.id
								+ ','
								+ isFunctional
								+ ')" title="Borrar"><i class="gris"><span class="lnr lnr-cross-circle p-l-5"></span></i></a>'
								+ ' </span>' + ' </li>');
	}
}

function deleteDependency(id, isFunctional) {
	let listDependency = (isFunctional) ? '#listFunctionalsDependencies'
			: '#listTechnicalsDependencies';

	$(listDependency + ' ul #' + id).remove();
}

function loadAutoCompleteAmbient(search) {
	var cont = getCont();
	$
			.ajax({
				type : "GET",
				url : cont + "ambient/" + "ambientAutoComplete-" + search + "-"
						+ $('#generateReleaseForm #systemCode').val(),
				timeout: 60000,
				data : {},
				success : function(response) {
					autocompleteAmbient(response);
				},
				error: function(x, t, m) {
					notifyAjaxError(x, t, m);
			    }
			});
};

function autocompleteAmbient(data) {
	$("#ambient")
			.autocomplete(
					{
						source : data,
						response : function(event, ui) {
							countAmbients = ui.content.length;
						},
						select : function(e, ui) {
							if (!($('#listAmbients ul #' + ui.item.id).length)) {
								$("#listAmbients ul")
										.prepend(
												'<li id="'
														+ ui.item.id
														+ '" class="nav-item dependency" >'
														+ ui.item.name
														+ ' <span class="flr m-b--10" style="margin-top: -3px;"> <a onclick="deleteAmbient('
														+ ui.item.id
														+ ')" title="Borrar"><i class=" gris">'
														+ '<span class="lnr lnr-cross-circle p-l-5"></span></i></a>'
														+ ' </span>' + ' </li>');
							}
							$(this).val('');
							return false;
						}
					});
}

function deleteAmbient(id) {
	$('#listAmbients ul #' + id).remove();
}

function nextTab(elem) {
	$(elem).next().find('a[data-toggle="tab"]').click();
}

function prevTab(elem) {
	$(elem).prev().find('a[data-toggle="tab"]').click();
}

$("#uploadBtn").change(function() {
	$("#uploadFile").val(this.value);
});

function listLi(name) {
	var list = [];
	$('#generateReleaseForm #' + name + ' ul').find('li').each(function(j) {
		list.push(Number($(this).attr('id')));
	});
	return list;
}

function listRowsId(name) {
	var list = [];

	$.each($("#" + name + " tbody tr"), function(index, value) {
		if (value.id != "") {
			list.push(Number(value.id));
		}
	});
	return list;

}

$("#generateReleaseForm #requiredFunctionalDes").change(function() {
	if (this.checked) {
		$('#generateReleaseForm #divRequiredFunctionalDes').show();
	} else {
		$('#generateReleaseForm #divRequiredFunctionalDes').hide();
	}
});

$("#generateReleaseForm #downRequired").change(function() {
	if (this.checked) {
		this.value = 1;
		$("#generateReleaseForm #environmentsActions").show("slow");
	} else {
		this.value = 0;
		$("#generateReleaseForm #environmentsActions").hide("slow");
	}
});

function listItemObjects() {
	var itemObjects = [];
	$.each($("#configurationItemsTable tbody tr"), function(index, value) {
		if (value.id != "") {
			if (value.children[5].textContent != 'Base Datos') {
				itemObjects.push({
					id : value.id,
					name : value.children[0].textContent,
					isSql : 0
				});
			}
		}
	});
	$.each($("#sqlObjectTable tbody tr"), function(index, value) {
		if (value.id != "") {
			itemObjects.push({
				id : value.id,
				execute : $('#sqlObjectTable #obj_sql_exec_' + value.id).val(),
				dbScheme : $('#sqlObjectTable #form-tags-' + value.id).val(),
				executePlan : $('#sqlObjectTable #obj_sql_rp_' + value.id)
						.val(),
				isSql : 1
			});
		}
	});
	return itemObjects;
}

$('#sqlObjectTable input[type=file]').change(function() {
	var fileCount = this.files.length;
	$(this).prev().find('span').html(fileCount);
});

function requestRelease() {
	var cont = getCont();
	var form = "#generateReleaseForm";	
	var dependenciesFunctionals = listLi('listFunctionalsDependencies');
	var dependenciesTechnical = listLi('listTechnicalsDependencies');
	var ambients = listLi('listAmbients');
	var modifiedComponents = listLi('listComponents');
	var actions = listRowsId('environmentActionTable');
	var objectItemConfiguration = listItemObjects();
	
	blockUI();
	$
			.ajax({
				type : "POST",
				url : cont + "release/" + "saveRelease",
				timeout: 60000,
				data : {
					// Informacion general
					release_id : $(form + ' #release_id').val(),
					releaseNumber : $(form + ' #releaseNumber').val(),
					systemCode : $(form + ' #systemCode').val(),
					impactId : $(form + ' #impactId').children(
							"option:selected").val(),
					riskId : $(form + ' #riskId').children("option:selected")
							.val(),
					priorityId : $(form + ' #priorityId').children(
							"option:selected").val(),
					description : $(form + ' #description').val(),
					
					versionNumber : $(form + ' #versionNumber').val(),
					
					// Tipos de reporte
					reportHaveArt : boolean($(form + ' #reportHaveArt').val()),
					reportfixedTelephony : boolean($(form + ' #reportfixedTelephony').val()),
					reportHistoryTables : boolean($(form + ' #reportHistoryTables').val()),
					reportNotHaveArt : boolean($(form + ' #reportNotHaveArt').val()),
					reportMobileTelephony : boolean($(form + ' #reportMobileTelephony').val()),
					reportTemporaryTables : boolean($(form + ' #reportTemporaryTables').val()),
					
					billedCalls : boolean($(form + ' #billedCalls').val()),
					notBilledCalls : boolean($(form + ' #notBilledCalls').val()),

					// Informacion de la solucion
					functionalSolution : $(form + ' #functionalSolution').val(),
					technicalSolution : $(form + ' #technicalSolution').val(),
					notInstalling : $(form + ' #notInstalling').val(),
					observations : $(form + ' #observations').val(),

					// Definicion de ambientes
					ambient : JSON.stringify(ambients),
					preConditions : $(form + ' #preConditions').val(),
					postConditions : $(form + ' #postConditions').val(),
					
					// Componentes de AIA
					modifiedComponent : JSON.stringify(modifiedComponents),

					// Datos de instalacion
					installationInstructions : $(
							form + ' #installationInstructions').val(),
					verificationInstructions : $(
							form + ' #verificationInstructions').val(),
					rollbackPlan : $(form + ' #rollbackPlan').val(),

					// Instalación de Base de Datos
					certInstallationInstructions : $(
							form + ' #certInstallationInstructions').val(),
					certVerificationInstructions : $(
							form + ' #certVerificationInstructions').val(),
					certRollbackPlan : $(form + ' #certRollbackPlan').val(),
					prodInstallationInstructions : $(
							form + ' #prodInstallationInstructions').val(),
					prodVerificationInstructions : $(
							form + ' #prodVerificationInstructions').val(),
					prodRollbackPlan : $(form + ' #prodRollbackPlan').val(),

					// Pruebas minimas
					minimalEvidence : $(form + ' #minimalEvidence').val(),

					downRequired : $(form + ' #downRequired').val(),
					actions : JSON.stringify(actions),
					observationsProd : $(form + ' #observationsProd').val(),
					observationsPreQa : $(form + ' #observationsPreQa').val(),
					observationsQa : $(form + ' #observationsQa').val(),
					objectItemConfiguration : JSON
							.stringify(objectItemConfiguration),
					dependenciesFunctionals : JSON
							.stringify(dependenciesFunctionals),
					dependenciesTechnical : JSON
							.stringify(dependenciesTechnical),
					functionalDescription : $(form + ' #functionalDescription')
							.val()
				},
				success : function(response) {
					console.log(response);
					responseAjaxRequestRelease(response);
				},
				error: function(x, t, m) {
					console.log(x);
					console.log(t);
					console.log(m);
					notifyAjaxError(x, t, m);
			    }
			});

}

function responseAjaxRequestRelease(response) {
	var form = "#generateReleaseForm";
	if (response != null) {
		switch (response.status) {
		case 'success':
			unblockUI();
			window.location = getCont() + "release/updateRelease/"
					+ $(form + ' #release_id').val();
			break;
		case 'fail':
			showReleaseErrors(response.errors);
			countErrorsByStep();
			var numItems = $('.yourclass').length
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
}

function sendRelease() {
	var form = "#generateReleaseForm";
	var dependenciesFunctionals = listLi('listFunctionalsDependencies');
	var dependenciesTechnical = listLi('listTechnicalsDependencies');
	var ambients = listLi('listAmbients');
	var modifiedComponents = listLi('listComponents');
	var actions = listRowsId('environmentActionTable');
	var objectItemConfiguration = listItemObjects();
	blockUI();
	var cont = getCont();
	$
			.ajax({
				// async : false,
				type : "POST",
				url : cont + "release/" + "saveRelease",
				timeout: 60000,
				data : {
					// Informacion general
					release_id : $(form + ' #release_id').val(),
					releaseNumber : $(form + ' #releaseNumber').val(),
					systemCode : $(form + ' #systemCode').val(),
					impactId : $(form + ' #impactId').children(
							"option:selected").val(),
					riskId : $(form + ' #riskId').children("option:selected")
							.val(),
					priorityId : $(form + ' #priorityId').children(
							"option:selected").val(),
					description : $(form + ' #description').val(),
					
					versionNumber : $(form + ' #versionNumber').val(),
					
					// Tipos de reporte
					reportHaveArt : boolean($(form + ' #reportHaveArt').val()),
					reportfixedTelephony : boolean($(form + ' #reportfixedTelephony').val()),
					reportHistoryTables : boolean($(form + ' #reportHistoryTables').val()),
					reportNotHaveArt : boolean($(form + ' #reportNotHaveArt').val()),
					reportMobileTelephony : boolean($(form + ' #reportMobileTelephony').val()),
					reportTemporaryTables : boolean($(form + ' #reportTemporaryTables').val()),
					
					billedCalls : boolean($(form + ' #billedCalls').val()),
					notBilledCalls : boolean($(form + ' #notBilledCalls').val()),

					// Informacion de la solucion
					functionalSolution : $(form + ' #functionalSolution').val(),
					technicalSolution : $(form + ' #technicalSolution').val(),
					notInstalling : $(form + ' #notInstalling').val(),
					observations : $(form + ' #observations').val(),

					// Definicion de ambientes
					ambient : JSON.stringify(ambients),
					preConditions : $(form + ' #preConditions').val(),
					postConditions : $(form + ' #postConditions').val(),
					
					// Componentes de AIA
					modifiedComponent : JSON.stringify(modifiedComponents),

					// Datos de instalacion
					installationInstructions : $(
							form + ' #installationInstructions').val(),
					verificationInstructions : $(
							form + ' #verificationInstructions').val(),
					rollbackPlan : $(form + ' #rollbackPlan').val(),

					// Instalación de Base de Datos
					certInstallationInstructions : $(
							form + ' #certInstallationInstructions').val(),
					certVerificationInstructions : $(
							form + ' #certVerificationInstructions').val(),
					certRollbackPlan : $(form + ' #certRollbackPlan').val(),
					prodInstallationInstructions : $(
							form + ' #prodInstallationInstructions').val(),
					prodVerificationInstructions : $(
							form + ' #prodVerificationInstructions').val(),
					prodRollbackPlan : $(form + ' #prodRollbackPlan').val(),

					// Pruebas minimas
					minimalEvidence : $(form + ' #minimalEvidence').val(),

					downRequired : $(form + ' #downRequired').val(),
					actions : JSON.stringify(actions),
					observationsProd : $(form + ' #observationsProd').val(),
					observationsPreQa : $(form + ' #observationsPreQa').val(),
					observationsQa : $(form + ' #observationsQa').val(),
					objectItemConfiguration : JSON
							.stringify(objectItemConfiguration),
					dependenciesFunctionals : JSON
							.stringify(dependenciesFunctionals),
					dependenciesTechnical : JSON
							.stringify(dependenciesTechnical),
					functionalDescription : $(form + ' #functionalDescription')
							.val()
				},
				success : function(response) {
					responseAjaxSendRelease(response);
				},
				error: function(x, t, m) {
					notifyAjaxError(x, t, m);
			    }
			});
}

function responseAjaxSendRelease(response) {
	if (response != null) {
		switch (response.status) {
		case 'success':
			resetErrors();
			reloadPreview();
			swal("Correcto!", "Release guardado correctamente.",
			"success", 2000)
			$('#generateReleaseForm #applyFor').show();
			break;
		case 'fail':
			showReleaseErrors(response.errors);
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

function showReleaseErrors(errors) {
	resetErrors();// Eliminamos las etiquetas de errores previas
	var error = errors;
	for (var i = 0; i < error.length; i++) {
		// Se modifica el texto de la advertencia y se agrega la de activeError
		$releaseEditForm.find(" #" + error[i].key + "_error").text(
				error[i].message);
		$releaseEditForm.find(" #" + error[i].key + "_error").css("visibility",
				"visible");
		$releaseEditForm.find(" #" + error[i].key + "_error").attr("class",
				"error fieldError activeError");
		// Si es input||textarea se marca el line en rojo
		if ($releaseEditForm.find(" #" + error[i].key).is("input")
				|| $releaseEditForm.find(" #" + error[i].key).is("textarea")) {
			$releaseEditForm.find(" #" + error[i].key).parent().attr("class",
					"form-line error fieldErrorLine");
		}
	}
}

function validEmptySection() {
	var i;
	for (i = 2; i <= 4; i++) {
		if ($("#step" + i).find(".activeSection").length == 0) {
			$("#empty_" + i).show();
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

function reloadPreview() {
	var src = $("#tinySummary").attr("src")
	$("#tinySummary").attr("src", src)
}

function previewRelease() {
	$('#previewReleaseModal').modal('show');
}

function closePreviewRelease() {
	$('#previewReleaseModal').modal('hide');
}

function synchronizeObjects() {
	blockUI();
	var cont = getCont();
	$
			.ajax({
				type : "POST",
				url : cont + "release/" + "synchronize/"
						+ $('#generateReleaseForm #release_id').val(),
				timeout: 60000,
				data : {},
				success : function(response) {
					responseAjaxSynchronize(response);
				},
				error: function(x, t, m) {
					notifyAjaxError(x, t, m);
			    }
			});
}

function responseAjaxSynchronize(response) {

	switch (response.status) {
	case 'success':
		addRowObject(response.obj.objects);
		$.each(response.obj.dependencies, function(key, value) {
			modifyDependency(value.to_release);
		});
		swal("Correcto!", "Sincronizacion finalizada correctamente.",
			"success", 2000)

		break;
	case 'fail':
		swal("Error!", response.exception, "error")
		break;
	case 'exception':
		swal("Exception!", response.exception, "error")
		break;
	default:
		unblockUI();
	}

}