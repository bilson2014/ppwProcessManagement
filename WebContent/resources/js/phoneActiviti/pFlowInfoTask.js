$().ready(function(){
	selectEven();
});


function selectEven(){
	$('.setSelect').off('click').on('click',function(){
		initSelectIos();
		$(window.parent.document).find('.pagePhone').scrollTop(9999);
	})
}


function initSelectIos(){
	
	var data = [ {
		'id' : '10001',
		'value' : '看情况'
	}, {
		'id' : '10002',
		'value' : '1万元及以上'
	}, {
		'id' : '10003',
		'value' : '2万元及以上'
	}, {
		'id' : '10004',
		'value' : '3万元及以上'
	}, {
		'id' : '10005',
		'value' : '5万元及以上'
	}, {
		'id' : '10006',
		'value' : '10万元及以上'
	}, ];
	var bankSelect = new IosSelect(1, [ data ], {
		title : '价格区间',
		itemHeight : 35,
		oneLevelId : '',
		callback : function(selectOneObj) {
             $('#setinput').attr('data-id',selectOneObj.id);
             $('#setinput').val(selectOneObj.value);
		}
	});
	
}



