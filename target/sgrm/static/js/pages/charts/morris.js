//$(function () {
//    getMorris('line', 'line_chart');
//    getMorris('bar', 'bar_chart');
//    getMorris('area', 'area_chart');
//    getMorris('donut', 'donut_chart');
//});
//
//
//function getMorris(type, element) {
//    if (type === 'line') {
//        Morris.Line({
//            element: element,
//            data: [{
//                'period': '2017',
//                'licensed': 3407,
//                'sorned': 660
//            }, {
//                    'period': '2011',
//                    'certificacion': 3351,
//                    'borrador': 2032,
//					'en revision': 1023,
//                    'completados': 860
//                }, {
//                    'period': '2012',
//                    'certificacion': 3269,
//                    'borrador': 2045,
//					'en revision': 1023,
//                    'completados': 1002
//                }, {
//                    'period': '2013',
//                    'certificacion': 3246,
//                    'borrador': 661,
//					'en revision': 1023,
//                    'completados': 320
//                }, {
//                    'period': '2014',
//                    'certificacion': 2500,
//                    'borrador': 676,
//					'en revision': 3567,
//                    'completados': 320
//                }, {
//                    'period': '2015',
//                    'certificacion': 3155,
//                    'borrador': 2034,
//					'en revision': 1023,
//                    'completados': 540
//                }, {
//                    'period': '2016',
//                    'certificacion': 3226,
//                    'borrador': 2000,
//					'en revision': 1023,
//                    'completados': 2003
//                }, {
//                    'period': '2017',
//                    'certificacion': 3245,
//                    'borrador': null,
//					'en revision': 3659,
//                    'completados': 540
//                }, {
//                    'period': '2018',
//                    'certificacion': 3289,
//                    'borrador': null,
//					'en revision': 1023,
//                    'completados': 2345
//                },],
//            xkey: 'period',
//            ykeys: ['certificacion', 'borrador', 'en revision', 'completados'],
//            labels: ['CERTIFICACION', 'BORRADOR', 'EN REVISIÓN', 'COMPLETADOS'],
//            lineColors: ['rgb(233, 30, 99)', 'rgb(0, 188, 212)', 'rgb(0, 150, 136)', 'rgb(255, 152, 0)'],
//            lineWidth: 3
//        });
//    } else if (type === 'bar') {
//        Morris.Bar({
//            element: element,
//            data: [
//            	{
//                x: '2015',
//                y: 3,
//                z: 2,
//                a: 3,
//				b: 4
//            }, {
//                x: '2016',
//                y: 2,
//                z: null,
//                a: 1,
//				b: 4
//        	}, {
//                x: '2017',
//                y: 0,
//                z: 2,
//                a: 4,
//				b: 2,
//            }, {
//                x: '2018',
//                y: 2,
//                z: 4,
//                a: 3,
//				b: 1
//            }],
//            xkey: 'x',
//            ykeys: ['y', 'z', 'a', 'b'],
//            labels: ['COMPLETADOS', 'CERTIFICACIÓN', 'EN REVISIÓN', 'BORRADOR'],
//            barColors: ['rgb(233, 30, 99)', 'rgb(0, 188, 212)', 'rgb(0, 150, 136)', 'rgb(255, 152, 0)'],
//        });
//    }
//}