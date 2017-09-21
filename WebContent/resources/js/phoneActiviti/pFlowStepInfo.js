$().ready(function(){
	$('.frameHead .name').text($('#projectName').val());
	$('.frameHead a').attr("href","/project/phone/flow/"+$('#url').val());
	initData();
});

function getMessageMore(){
     
	$('.itemMore').off('click').on('click',function(){
		if($(this).hasClass('open')){
			$(this).removeClass('open');
			$(this).parent().find('.reTalkItem').hide();
			$('.reTalkItem').attr('data-content','');
		}else{
			$('.itemMore').removeClass('open');
			$('.reTalkItem').hide();
			$(this).addClass('open');
			$(this).parent().find('.reTalkItem').show();
			$('.reTalkItem').attr('data-content','');
		}
	});

}

function initData(){
	var id = $('#taskId').val();
	loadData(function(res){		
		initStageInfoTop(res);
		loadStageInfoEven();
	}, getContextPath() + '/project/task-detail/'+id,null);
}



function initStageInfoTop(res){
	$('#infoNameTitle').text(res.taskName);
	$('#stateContent').html(res.taskDescription);
	$('#infoStartTime').text(formatDate(res.startTime));
	$('#infoEndTime').text(formatDate(res.endTime));
	var checkStatus = res.taskStatus;
	if(checkStatus == "completed"){
		$('#stateImg').attr('src',"/resources/images/pFlow/c.png");
		$('#stateWord').text('已完成');
		$('#infoEndTitle').text('完成时间');
		$('#stateWord').attr('style','color:#79D01B');
		$('#infoEndTitle').text('完成时间');
		$('#infoEndTime').text(formatDate(res.endTime));
	}
	if(checkStatus == "running"){
		$('#stateImg').attr('src',"/resources/images/pFlow/sWait.png");
		$('#stateWord').text('进行中');
		$('#stateWord').attr('style','color:#fe5453');
		$('#infoEndTitle').text('预计时间');
		$('#infoEndTime').text(formatDate(res.dueDate));
	}
	if(checkStatus == "futher"){
		$('#stateImg').attr('src',"/resources/images/pFlow/tagError.png");
		$('#stateWord').text('未开始');
		$('#stateWord').attr('style','color:#F5A623');
		$('#infoEndTitle').text('预计时间');
		$('#infoEndTime').text(formatDate(res.dueDate));
	}
	
}

function loadStageInfoEven(){
	loadData(function(res){	
		var body =$('.setMessageContent');
		body.html('');
		if(res != null && res != undefined){
			for (var int = 0; int < res.length; int++) {
				   var html =createTalkInfo(res[int]);
				   body.append(html);
			}
			getMessageMore();
			rePickTalk();
		}
	}, getContextPath() + '/message/getTaskMsg/',$.toJSON({
		taskId:$('#taskId').val(),
	}));
}

//留言回复
function rePickTalk(){
   $('.reTalk').off('click').on('click',function(){
	    var projectId = $(this).attr('data-id');
	    var taskName = $(this).attr('data-name');
	    var parentId = $(this).attr('data-parentId');
	    var content = $(this).parent().find('textarea').val();
	    if(content == null || content == "" || content == undefined){
	    	$(this).parent().attr('data-content',"回复不能为空");
	    }else{
	    $('.reTalk').off('click');
			loadData(function(res){
				if(res.code == 200){
					loadStageInfoEven();
				}else{
					rePickTalk();
				}
			}, getContextPath() + '/message/addReply',$.toJSON({
				projectId:projectId,
				taskName:taskName,
				content:content,
				parentId:parentId
			}));
	    }
   });
		
}

//留言卡片
function createTalkInfo(res){
	var  children= res.children;
	var body = '';
	if(children != null && children != undefined && children !=""){
		for (var int = 0; int < children.length; int++) {
			body +='<div class="contentItem" style="padding-left:0.5rem"><div class="name">'+children[int].fromName+' 回复 :</div><div>'+formatDate((children[int].createDate).replace("CST","GMT+0800"))+'</div> <div class="itemContent">'+children[int].content+'</div></div>';
		}
	}
	var html = [
		'<div class="item">                                                                                        ',
	    '     <img class="itemMore" src="/resources/images/pFlow/moreMessage.png">                                 ',
	    '     <div class="content">                                                                                ',
	    '             <div class="contentItem">                                                                    ',
	    '                    <div class="name">'+res.fromName+''+formatDate((res.createDate).replace("CST","GMT+0800"))+'</div>',
	    '                    <div class="itemContent">'+res.content+'</div>                                         ',
	    '             </div>                                                                                       ',
	    ' '+body+' ',             
	    '     </div>                                                                                               ',
	    '       <div class="reTalkItem" data-content=""> ',
		'	        <textarea></textarea>',
		'	        <div class="btn-c-r reTalk" data-id="'+res.projectId+'" data-name="'+res.taskName+'" data-parentId="'+res.projectMessageId+'">回复</div> ',
		'	    </div>                                                                                         ',
	    '      <div class="itemLine"></div>',
	    ' </div>' ,
	].join('');
	return html;
}