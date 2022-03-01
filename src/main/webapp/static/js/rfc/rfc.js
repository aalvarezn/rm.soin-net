/** Declaración de variables globales del contexto * */
var $dtRFCs;

var formReleaseDraft = $('#formAddReleaseDraft');
var formChangeUser = $('#changeUserForm');
var trackingReleaseForm = $('#trackingReleaseForm');

$(function() {
	activeItemMenu("rfcItem");
	$("#addRFCSection").hide();
	$('input[name="daterange"]').daterangepicker(optionRangePicker);
	initRFCTable();
});

function initRFCTable() {
	$dtRFCs = $('#dtRFCs').DataTable(
			{
				lengthMenu : [ [ 10, 25, 50, -1 ],
					[ '10', '25', '50', 'Mostrar todo' ] ],
					"iDisplayLength" : 10,
					"language" : optionLanguaje,
					"iDisplayStart" : 0,
					"processing" : true,
					"serverSide" : true,
					"sAjaxSource" : getCont() + "rfc/list",
					"fnServerParams" : function(aoData) {
					},
					"aoColumns" : [ {
						"mDataProp" : "numRequest"
					}, {
						"mRender" : function(data, type, row, meta) {
							return row.numRequest;
						},
					}, {
						"mRender" : function(data, type, row, meta) {
							if(row.priority)
								return row.priority.name;
							else
								return '';
						},
					}, {
						"mRender" : function(data, type, row, meta) {
							return row.numRequest;
						},
					}, {
						"mRender" : function(data, type, row, meta) {
							return moment(row.requestDate).format('DD/MM/YYYY h:mm:ss a');
						},
					}, {
						"mRender" : function(data, type, row, meta) {
							if(row.status)
								return row.status.name;
							else
								return '';
						},
					}, {
						"mRender" : function(data, type, row, meta) {
							return "---";
						},
					} ],
					ordering : false,
			});
}

function changeSlide() {
	let change = $("#buttonAddRFC").is(":visible");
	$("#buttonAddRFC").toggle();
	let hide = change ? '#tableSection': '#addRFCSection';
	let show = !change ? '#tableSection': '#addRFCSection';
	$(hide).toggle("slide");
	$(show).show('slide', {
		direction : (change? 'right': 'left' )
	}, 500);
	if(change)
		$("#addRFCSection").insertAfter("#tableSection");
	else
		$("#tableSection").insertAfter("#addRFCSection")
}


function addRFCSection() {
	changeSlide();
}


function closeRFCSection(){
	changeSlide();
}