$().ready(function(){
	$('.frameHead .name').text($('#projectName').val());
	getMessageMore();
	initAllTalk();
	addMessage();
});

function getMessageMore(){
	$('.showMessage').off('click').on('click',function(){
		if($(this).hasClass('open')){
			$(this).removeClass('open');
			$('.setMessage').hide();
		}else{
			$(this).addClass('open');
			$('.setMessage').show();
			$('.setMessage').attr('data-content','');
			$('#addmessage').val('');
		}
	});
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

function addMessage(){
	$('#submitTalkInfo').off('click').on('click',function(){
		var projectId = $('#projectId').val();
		var talkInfo = $('#addmessage').val();
		var taskId = $('#taskId').val();
		var taskName = $('#taskName').val();
		$('#submitTalkInfo').off('click');
		if(talkInfo == "" || talkInfo == null || talkInfo == undefined){
			$('.setMessage').attr("data-content",'留言不能为空');
		}else{
		loadData(function(res){
			if(res.code == 200){
				$('.setMessage').attr('data-content','');
				$('#addmessage').val('');
				initAllTalk();
			}else{
				addMessage();
			}
		}, getContextPath() + '/message/addTopic',$.toJSON({
			taskId:taskId,
			taskName:taskName,
			projectId:projectId,
			content:talkInfo
		}));
		}
	});
}

//全部留言信息
function initAllTalk(){
	loadData(function(res){
		var res = res;
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
	}, getContextPath() + '/message/getProjectMsg/'+$('#projectId').val(),null);
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
					initAllTalk();
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
		'	        <textarea></textarea> ',
		'	        <div class="btn-c-r reTalk" data-id="'+res.projectId+'" data-name="'+res.taskName+'" data-parentId="'+res.projectMessageId+'">回复</div> ',
		'	    </div>                                                                                         ',
	    '      <div class="itemLine"></div>',
	    ' </div>' ,
	].join('');
	return html;
}
