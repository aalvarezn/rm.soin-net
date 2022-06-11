if (typeof jQuery === "undefined") {
	throw new Error("jQuery plugins need to be before this file");
}

$.AdminBSB = {};
$.AdminBSB.options = {
		colors : {
			red : '#F44336',
			pink : '#E91E63',
			purple : '#9C27B0',
			deepPurple : '#673AB7',
			indigo : '#3F51B5',
			blue : '#2196F3',
			lightBlue : '#03A9F4',
			cyan : '#00BCD4',
			teal : '#009688',
			green : '#4CAF50',
			lightGreen : '#8BC34A',
			lime : '#CDDC39',
			yellow : '#ffe821',
			amber : '#FFC107',
			orange : '#FF9800',
			deepOrange : '#FF5722',
			brown : '#795548',
			grey : '#9E9E9E',
			blueGrey : '#607D8B',
			black : '#000000',
			white : '#ffffff'
		},
		leftSideBar : {
			scrollColor : 'rgba(0,0,0,0.5)',
			scrollWidth : '4px',
			scrollAlwaysVisible : false,
			scrollBorderRadius : '0',
			scrollRailBorderRadius : '0',
			scrollActiveItemWhenPageLoad : true,
			breakpointWidth : 1170
		},
		dropdownMenu : {
			effectIn : 'fadeIn',
			effectOut : 'fadeOut'
		}
}

/*
 * Left Sidebar - Function
 * =================================================================================================
 * You can manage the left sidebar menu options
 * 
 */
$.AdminBSB.leftSideBar = {
		activate : function() {
			var _this = this;
			var $body = $('body');
			var $overlay = $('.overlay');

			// Close sidebar
			$(window).click(
					function(e) {
						var $target = $(e.target);
						if (e.target.nodeName.toLowerCase() === 'i') {
							$target = $(e.target).parent();
						}

						if (!$target.hasClass('bars') && _this.isOpen()
								&& $target.parents('#leftsidebar').length === 0) {
							if (!$target.hasClass('js-right-sidebar'))
								$overlay.fadeOut();
							$body.removeClass('overlay-open');
						}
					});

			$.each($('.menu-toggle.toggled'), function(i, val) {
				$(val).next().slideToggle(0);
			});

			// When page load
			$.each($('.menu .list li.active'), function(i, val) {
				var $activeAnchors = $(val).find('a:eq(0)');

				$activeAnchors.addClass('toggled');
				$activeAnchors.next().show();
			});

			// Collapse or Expand Menu
			$('.menu-toggle')
			.on(
					'click',
					function(e) {
						var $this = $(this);
						var $content = $this.next();

						if ($($this.parents('ul')[0]).hasClass('list')) {
							var $not = $(e.target).hasClass('menu-toggle') ? e.target
									: $(e.target).parents('.menu-toggle');

							$.each($('.menu-toggle.toggled').not($not)
									.next(), function(i, val) {
								if ($(val).is(':visible')) {
									$(val).prev().toggleClass('toggled');
									$(val).slideUp();
								}
							});
						}

						$this.toggleClass('toggled');
						$content.slideToggle(320);
					});

			// Set menu height
			_this.setMenuHeight(true);
			_this.checkStatusForResize(true);
			$(window).resize(function() {
				_this.setMenuHeight(false);
				_this.checkStatusForResize(false);
			});

		},
		setMenuHeight : function(isFirstTime) {
			if (typeof $.fn.slimScroll != 'undefined') {
				var configs = $.AdminBSB.options.leftSideBar;
				var height = ($(window).height() - ($('.legal').outerHeight()
						+ $('.user-info').outerHeight() + $('.navbar')
						.innerHeight()));
				var $el = $('.list');

				if (!isFirstTime) {
					$el.slimscroll({
						destroy : true
					});
				}

				$el.slimscroll({
					height : height + "px",
					color : configs.scrollColor,
					size : configs.scrollWidth,
					alwaysVisible : configs.scrollAlwaysVisible,
					borderRadius : configs.scrollBorderRadius,
					railBorderRadius : configs.scrollRailBorderRadius
				});

				// Scroll active menu item when page load, if option set = true
				if ($.AdminBSB.options.leftSideBar.scrollActiveItemWhenPageLoad) {
					var item = $('.menu .list li.active')[0];
					if (item) {
						var activeItemOffsetTop = item.offsetTop;
						if (activeItemOffsetTop > 150)
							$el.slimscroll({
								scrollTo : activeItemOffsetTop + 'px'
							});
					}
				}
			}
		},
		checkStatusForResize : function(firstTime) {
			var $body = $('body');
			var $openCloseBar = $('.navbar .navbar-header .bars');
			var width = $body.width();

			if (firstTime) {
				$body.find('.content, .sidebar').addClass('no-animate').delay(1000)
				.queue(function() {
					$(this).removeClass('no-animate').dequeue();
				});
			}

			if (width < $.AdminBSB.options.leftSideBar.breakpointWidth) {
				$body.addClass('ls-closed');
				$openCloseBar.fadeIn();
			} else {
				$body.removeClass('ls-closed');
				$openCloseBar.fadeOut();
			}
		},
		isOpen : function() {
			return $('body').hasClass('overlay-open');
		}
};
//==========================================================================================================================

/*
 * Navbar - Function
 * =======================================================================================================
 * You can manage the navbar
 * 
 */
$.AdminBSB.navbar = {
		activate : function() {
			var $body = $('body');
			var $overlay = $('.overlay');

			// Open left sidebar panel
			$('.bars').on('click', function() {
				$body.toggleClass('overlay-open');
				if ($body.hasClass('overlay-open')) {
					$overlay.fadeIn();
				} else {
					$overlay.fadeOut();
				}
			});

			// Close collapse bar on click event
			$('.nav [data-close="true"]').on('click', function() {
				var isVisible = $('.navbar-toggle').is(':visible');
				var $navbarCollapse = $('.navbar-collapse');

				if (isVisible) {
					$navbarCollapse.slideUp(function() {
						$navbarCollapse.removeClass('in').removeAttr('style');
					});
				}
			});
		}
}
//==========================================================================================================================

/*
 * Input - Function
 * ========================================================================================================
 * You can manage the inputs(also textareas) with name of class 'form-control'
 * 
 */
$.AdminBSB.input = {
		activate : function($parentSelector) {
			$parentSelector = $parentSelector || $('body');

			// On focus event
			$parentSelector.find('.form-control').focus(function() {
				$(this).closest('.form-line').addClass('focused');
			});

			// On focusout event
			$parentSelector.find('.form-control').focusout(function() {
				var $this = $(this);
				if ($this.parents('.form-group').hasClass('form-float')) {
					if ($this.val() == '') {
						$this.parents('.form-line').removeClass('focused');
					}
				} else {
					$this.parents('.form-line').removeClass('focused');
				}
			});

			// On label click
			$parentSelector.on('click', '.form-float .form-line .form-label',
					function() {
				$(this).parent().find('input').focus();
			});

			// Not blank form
			$parentSelector.find('.form-control').each(function() {
				if ($(this).val() !== '') {
					$(this).parents('.form-line').addClass('focused');
				}
			});
		}
}
//==========================================================================================================================

/*
 * Form - Select - Function
 * ================================================================================================
 * You can manage the 'select' of form elements
 * 
 */
$.AdminBSB.select = {
		activate : function() {
			if ($.fn.selectpicker) {
				$('select:not(.ms)').selectpicker();
			}
		}
}
//==========================================================================================================================


/*
 * Browser - Function
 * ======================================================================================================
 * You can manage browser
 * 
 */
var edge = 'Microsoft Edge';
var ie10 = 'Internet Explorer 10';
var ie11 = 'Internet Explorer 11';
var opera = 'Opera';
var firefox = 'Mozilla Firefox';
var chrome = 'Google Chrome';
var safari = 'Safari';

$(function() {
	$.AdminBSB.leftSideBar.activate();
	$.AdminBSB.navbar.activate();
	$.AdminBSB.input.activate();
	$.AdminBSB.select.activate();

	setTimeout(function() {
		$('.page-loader-wrapper').fadeOut();
	}, 50);

	if($('.selectpicker').lenght){
		$('.selectpicker').selectpicker({
			noneResultsText: 'Sin resultados'
		});
	}

	$('.topArrow').click(function() {
		$('html, body').animate({scrollTop: '0px'}, 300);
	});

	$(window).scroll(function() {
		if($(this).scrollTop() > 0){
			$('.topArrow').slideDown(300);
		}else{
			$('.topArrow').slideUp(300);
		}
	});
});

var token = $("input[name='_csrf']").val();
var header = "X-CSRF-TOKEN";
$(document).ajaxSend(function(e, xhr, options) {
	xhr.setRequestHeader(header, token);
});

$.ajaxSetup({
})

function formatDate(date) {
	var d = new Date(date), month = '' + (d.getMonth() + 1), day = ''
		+ (d.getDate() + 1), year = d.getFullYear();

	if (month.length < 2)
		month = '0' + month;
	if (day.length < 2)
		day = '0' + day;

	return [ day, month, year ].join('-');
}

function formatDateCustom(date, sign) {
	var d = new Date(date), month = '' + (d.getMonth() + 1), day = ''
		+ (d.getDate() + 1), year = d.getFullYear();

	if (month.length < 2)
		month = '0' + month;
	if (day.length < 2)
		day = '0' + day;

	return [ day, month, year ].join(sign);
}

function validFunctions(name, event) {
	var fn = window[name];
	if (typeof fn === 'function') {
		alert(name);
	} else {
		swal(
				"Exception!",
				"El sistema ha detectado datos antiguos almacenados, favor borrar cache y reintentar.",
		"warning")
	}
}

function getCont() {
	var cont = window.location.pathname.substring(0, window.location.pathname
			.indexOf("/", 2));
	return $('#systemContent').val();
}

function notifyInfo(text) {
	$.notify({
		title: "",
		message: text,
		type: 'info'
	});
}

function notifyAjaxError(x, t, m) {	
	switch (x.status) {
	case 403 : 
		swal("Error!","Solicitud rechazada por el servidor, favor validar permisos.", "error");
		break;
	case 500 : 

		if(typeof $(x.responseText).find("#error-message")[0].innerHTML != 'undefined'){
			swal("Error!",$(x.responseText).find("#error-message")[0].innerHTML, "error");
		}else{
			if(x.responseText.indexOf("java.sql.SQLException: ORA-01017") >= 0){
				swal("Error!","No se pudo establecer conexi\u00F3n con la base de datos, favor validar accesos.", "error");
			}else{
				if(x.responseText.indexOf("The Network Adapter could not establish the connection") >= 0){
					swal("Error!","Error de conexi\u00F3n en la solicitud, verifique su acceso a internet.", "error");
				}else{
					console.log(x);
					swal("Error!","Error interno al procesar la solicitud, favor contactar a soporte para notificar el error.", "error");
				}
			}

		}
		break;
	}
	if(t==="timeout") {
		swal(
				"Error!",
				"Tiempo de espera agotado, verifique la conexi\u00F3n e intente de nuevo.",
		"error");
	} 


}

//$('.datepicker').datepicker();

function blockUI(time) {
	Swal.fire({
		title : "Procesando...",
		text : "Por favor espere",
		imageUrl : getCont() + "/static/images/preloader.gif",
		imageHeight: 50,
		showConfirmButton : false,
		allowOutsideClick : false,
		customClass: 'swal-wide',
	});
}

function unblockUI() {
//	$.unblockUI();
	Swal.close()
}

$(function() {
	var data = $('#postMSG').val();
	if (data !== undefined && data != '' && data != null) {
		postLoadMSG(data);
	}
});

function postLoadMSG(msg) {
	console.log("Error!", msg, "error");
}

function getUserName() {
	return $('#userInfo_username').val();
}

function boolean(variable){
	let answer = false;

	if (variable == null){
		return answer;
	}

	if(variable == 1){
		answer= true;
	}
	if(variable.toLowerCase() == 'si' || variable.toLowerCase() == 'sí'){
		answer= true;
	}
	return answer;
}

function swalLoading() {
	swal({
		title : "Procesando...",
		text : "Por favor espere",
		imageUrl : getCont() + "/static/images/preloader.gif",
		showConfirmButton : false,
		allowOutsideClick : false
	});
}

function activeItemMenu(name, toggled){
	if(toggled){
		$("#menuListItems #"+ name).click();
	}else{
		$("#menuListItems #"+ name).addClass( "toggled" );
	}
}

function myAlert(errors, form){
	var error = errors;
	for (var i = 0; i < error.length; i++) {
		displayFieldError("#"+form + " #" + error[i].key,error[i].message );
	}
}

function activeInputCheckbox(form, name){
	form.find('#'+name).val('1');
	form.find('#'+name).prop("checked", true);
}

function changeMenuItemTo(name){
	var menu = $('#menuListItems');
	menu.find('a').removeClass("toggled");
	menu.find('#'+name).addClass("toggled");
}

function swal(tittle, message, type){
	Swal.fire({
		icon: type,
		title: tittle,
		text: message,
		customClass: 'swal-wide',
	})
}

function swal(tittle, message, type, time){
	Swal.fire({
		icon: type,
		title: tittle,
		text: message,
		customClass: 'swal-wide',
		timer: time,
		confirmButtonText: "Aceptar",
	})
}

function swalReport(tittle, message, type){
	Swal.fire({
		icon: type,
		title: tittle,
		html: message,
		customClass: 'swal-wide',
		confirmButtonText: "Aceptar",
	})
}
function isNumeric(str){
	return $.isNumeric(str);
}

let optionLanguaje = {
		"emptyTable" : "No existen registros",
		"zeroRecords" : "No existen registros",
		"processing" : "Cargando",
		"info" : "Mostrando _START_ a _END_ de _TOTAL_ registros",
		"infoEmpty" : "Mostrando 0 a 0 de 0 registros",
		"infoFiltered" : "",
};

let optionRangePicker =  {
		"autoUpdateInput": false,
		"opens": 'left',
		"orientation": 'right',
		"locale": {
			"format": "DD/MM/YYYY",
			"separator": " - ",
			"applyLabel": "Aplicar",
			"cancelLabel": "Cancelar",
			"fromLabel": "Desde",
			"toLabel": "Hasta",
			"customRangeLabel": "Custom",
			"daysOfWeek": [
				"Do",
				"Lu",
				"Ma",
				"Mi",
				"Ju",
				"Vi",
				"Sa"
				],
				"monthNames": [
					"Enero",
					"Febrero",
					"Marzo",
					"Abril",
					"Mayo",
					"Junio",
					"Julio",
					"Agosto",
					"Septiembre",
					"Octubre",
					"Noviembre",
					"Deciembre"
					],
					"firstDay": 1
		}
};

var highlight = function(input) {
	$(input).parents('.form-line').addClass('error');
};
var unhighlight = function(input) {
	$(input).parents('.form-line').removeClass('error');
};
var errorPlacement = function(error, element) {
	$(element).parents('.form-group').append(error);;
};

var swalDefault = {
		icon: 'question',
		showCancelButton: true,
		customClass: 'swal-wide',
		cancelButtonText: 'Cancelar',
		cancelButtonColor: '#f14747',
		confirmButtonColor: '#3085d6',
		confirmButtonText: 'Aceptar',
};


function notifyMs(message, status) {
	switch (status) {
	case 'success':
		Swal.fire({
			icon: status,
			title: 'Correcto',
			text: message,
			customClass: 'swal-wide',
			timer: 2000
		})
		break;
	case 'error':
		Swal.fire({
			icon: status,
			title: 'Error',
			text: message,
			customClass: 'swal-wide',
			timer: 2000
		})
		break;
	case 'warning':
		Swal.fire({
			icon: status,
			title: 'Precaución',
			text: message,
			customClass: 'swal-wide',
			timer: 2000
		})
		break;
	case 'info':
		Swal.fire({
			icon: status,
			title: 'Información',
			text: message,
			customClass: 'swal-wide',
			timer: 2000
		})
		break;
	default:
		Swal.fire({
			icon: status,
			title: 'Error',
			text: message,
			customClass: 'swal-wide',
			timer: 2000
		})
		break;
	}
}
/*
//add the rule here
jQuery.validator.addMethod("selectOption",function(value,element,param) {
	console.log(element);
	return elementValue != param;
},
"Value cannot be {0}");
*/