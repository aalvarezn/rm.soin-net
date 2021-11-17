$(function() {
	initiateLast4Year();
	initiateLastMonth();

});

function initiateLast4Year() {
	var cont = getCont();
	$.ajax({
		type : "GET",
		url : cont + "statistic/" + "getLastFourYears",
		timeout : 60000,
		data : {},
		success : function(response) {
			getMorrisBar('bar_chart', response);
		},
		error : function(x, t, m) {
			notifyAjaxError(x, t, m);
		}
	});
}

function initiateLastMonth() {
	var cont = getCont();
	$.ajax({
		type : "GET",
		url : cont + "statistic/" + "getLastMonth",
		timeout : 60000,
		data : {},
		success : function(response) {
			getMorrisDonut('donut_chart', response);
		},
		error : function(x, t, m) {
			notifyAjaxError(x, t, m);
		}
	});
}

function getMorrisBar(element, data) {
	Morris.Bar({
		element : element,
		data : [ {
			x : data[0].year,
			y : data[0].amount,
			z : data[1].amount,
			a : data[2].amount,
			b : data[3].amount
		}, {
			x : data[4].year,
			y : data[4].amount,
			z : data[5].amount,
			a : data[6].amount,
			b : data[7].amount
		}, {
			x : data[8].year,
			y : data[8].amount,
			z : data[9].amount,
			a : data[10].amount,
			b : data[11].amount
		}, {
			x : data[12].year,
			y : data[12].amount,
			z : data[13].amount,
			a : data[14].amount,
			b : data[15].amount
		} ],
		xkey : 'x',
		ykeys : [ 'y', 'z', 'a', 'b' ],
		labels : [ data[0].status, data[1].status, data[2].status,
				data[3].status ],
		barColors : [ 'rgb(233, 30, 99)', 'rgb(0, 188, 212)',
				'rgb(0, 150, 136)', 'rgb(255, 152, 0)' ],
	});
}

function getMorrisDonut(element, data) {
	Morris.Donut({
		element : element,
		data : [ {
			label : data[0].status,
			value : data[0].amount
		}, {
			label : data[1].status,
			value : data[1].amount
		}, {
			label : data[2].status,
			value : data[2].amount
		}, {
			label : data[3].status,
			value : data[3].amount
		} ],
		colors : [ 'rgb(233, 30, 99)', 'rgb(0, 188, 212)', 'rgb(0, 150, 136)',
				'rgb(255, 152, 0)' ],
		formatter : function(y) {
			return y + ''
		}
	});
}