$(function() {
	initTableObject();
	initTableObject2();
	//initTableObjectSql();
});

	function initTableObject() {
		$dtObjects = $('#tableTest').DataTable(
				{

					lengthMenu : [ [ 10, 25, 50, -1 ],
						[ '10', '25', '50', 'Mostrar todo' ] ],
						"iDisplayLength" : 10,
						"language" : optionLanguaje,
						"iDisplayStart" : 0,
						"processing" : true,
						"serverSide" : true,
						"sAjaxSource" : getCont() + "release/listObjects",
						"fnServerParams" : function(aoData) {
							aoData.push({"name": "releaseId", "value": $('#release_id').val()},
							);
						},
						"aoColumns" : [
							{
								"mDataProp" : "objects.name",
							},
							{
								"mRender" : function(data, type, row, meta) {
									return moment(row.objects.revision_Date).format('DD/MM/YYYY h:mm:ss a');
								}
							},
							{
								"mDataProp" : "objects.revision_SVN"
							},
							{
								"mDataProp" : "objects.typeObject.name"
							},
							{
								"mDataProp" : "objects.configurationItem.name"
							}
							 ],
						ordering : false,
				});
	}
	function initTableObject2() {

		$dtObjects = $('#tableTest2').DataTable(
				{

					lengthMenu : [ [ 10, 25, 50, -1 ],
						[ '10', '25', '50', 'Mostrar todo' ] ],
						"iDisplayLength" : 10,
						"language" : optionLanguaje,
						"iDisplayStart" : 0,
						"processing" : true,
						"serverSide" : true,
						"sAjaxSource" : getCont() + "release/listObjects",
						"fnServerParams" : function(aoData) {
							aoData.push({"name": "releaseId", "value": $('#release_id').val()},
									{"name": "sql", "value": 1}
							);
						},
						"aoColumns" : [
							{
								"mDataProp" : "objects.name",
							},
							{
								"mRender" : function(data, type, row, meta) {
									options='<div class="switch">';
									if(row.objects.execute==1){
										options=options+'<label>NO<input disabled="disabled" type="checkbox" checked="checked" value="1"><span class="lever"></span>S&Iacute; </label> </div>';
									}else{
										options=options+'<label>NO<input disabled="disabled" type="checkbox" value="0"><span class="lever"></span>S&Iacute; </label> </div>'
									}
									return options;
								}
							},
							{
								"mRender" : function(data, type, row, meta) {
									if(row.objects.dbScheme!=null){
										return row.objects.dbScheme.replace( ',', ', ')
									}else{
										return "Sin esquema ingresado";
									}
									
									}
								}
							,
							{
								"mRender" : function(data, type, row, meta) {
									options='<div class="switch">';
									if(row.objects.executePlan==1){
										options=options+'<label>NO<input disabled="disabled" type="checkbox" checked="checked" value="1"><span class="lever"></span>S&Iacute; </label> </div>';
									}else{
										options=options+'<label>NO<input disabled="disabled" type="checkbox" value="0"><span class="lever"></span>S&Iacute; </label> </div>'
									}
									return options;
								}
							},
							 ],
						ordering : false,
				});
	}