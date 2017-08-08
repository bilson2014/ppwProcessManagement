$().ready(function() {
	$('.agree').click(function() {
		var taskName = $(this).attr('tname');
		var param = '';
		if(taskName == '部门领导审批')
			param = 'deptLeaderPass';
		if(taskName == '人事审批')
			param = 'hrPass';
		if(taskName == '销假' || taskName == '调整申请')
			param = 'reApply';
		loadData($(this).attr('taskId'), param, true, function(ret) {
			alert(ret);
		});
	});
	
	$('.disagree').click(function() {
		var taskName = $(this).attr('tname');
		var param = '';
		if(taskName == '部门领导审批')
			param = 'deptLeaderPass';
		if(taskName == '人事审批')
			param = 'hrPass';
		if(taskName == '销假' || taskName == '调整申请')
			param = 'reApply';
		loadData($(this).attr('taskId'), param, false, function(ret) {
			alert(ret);
		});
	});
});

function loadData(taskId, key, value, func) {
	$.post('/vacation/complete/' + taskId, {key : key, value : value}, func);
}