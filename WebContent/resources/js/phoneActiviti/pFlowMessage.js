$().ready(function(){
	getMessageMore();
});

function getMessageMore(){
	$('.showMessage').off('click').on('click',function(){
		if($(this).hasClass('open')){
			$(this).removeClass('open');
			$('.setMessage').hide();
		}else{
			$(this).addClass('open');
			$('.setMessage').show();
		}
	});
	$('.itemMore').off('click').on('click',function(){
		if($(this).hasClass('open')){
			$(this).removeClass('open');
			$(this).parent().find('input').hide();
		}else{
			$(this).addClass('open');
			$(this).parent().find('input').show();
		}
	});
	
}

function addMessage(){
	$('#submitTalkInfo').off('click').on('click',function(){
		var projectId = $('#projectId').val();
		var taskName = $('#taskName').val();
		var talkInfo = $('#talkInfo').val();
		$('.upInfo #submitTalkInfo').off('click');
		$('#areaError').text('');
		if(talkInfo == "" || talkInfo == null || talkInfo == undefined){
			$('#areaError').text('留言不能为空');
		}else{
		loadData(function(res){
			if(res.code == 200){
				initAllTalk();
				initAddTalk();
				$('#talkInfo').val('');
			}else{
				initAddTalk();
			}
		}, getContextPath() + '/message/addTopic',$.toJSON({
			projectId:projectId,
			taskName:taskName,
			content:talkInfo
		}));
		}
	});
}