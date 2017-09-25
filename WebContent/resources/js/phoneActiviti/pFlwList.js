$().ready(function(){
	tagEven();
	searchEven();
	loadDoing();
	$(window.parent.document).find('.headerCom').show();
	$(window.parent.document).find('.footBot').hide();
	
});


function tagEven(){
	$('.menuTag div').off('click').on('click',function(){
		var id = $(this).attr('data-id');
		if(id == 0){
			$('.menuTag').addClass('tagOne');
			$('.menuTag').removeClass('tagTwo');
			$('.menuTag').removeClass('tagThree');
			$('.search').hide();
			$('#daiban').hide();
			loadPause();
		}
		if(id == 1){
			$('.menuTag').removeClass('tagOne');
			$('.menuTag').addClass('tagTwo');
			$('.menuTag').removeClass('tagThree');
			$('.search').show();
			$('.search li').removeClass('checkSearch');
			$('#getAll').addClass('checkSearch');
			$('#daiban').hide();
			loadDoing();
		}
		if(id == 2){
			$('.menuTag').removeClass('tagOne');
			$('.menuTag').removeClass('tagTwo');
			$('.menuTag').addClass('tagThree');
			$('.search').hide();
			$('#daiban').hide();
			loadFinish();
		}
	});
}
//搜索事件
function searchEven(){
	$('.search li').off('click').on('click',function(){
		$('.search li').removeClass('checkSearch');
		$(this).addClass('checkSearch');
		var id = $(this).attr('data-id');
		getState(id);
	});
}
//进行中
function loadDoing(){	
	var setMission =  $('.setMission');
	var otherCard  =  $('.setCard');
	setMission.html('');
	otherCard.html('');

	loadData(function(res){  
			var gTasks = res.gTasks;
			var runningTasks = res.runningTasks;
			if(gTasks != null && gTasks != undefined && gTasks != ''){
				for (var int = 0; int < gTasks.length; int++) {
					 var html = createWaitCard(gTasks[int]);
					 setMission.append(html);
				 }
			     $(window.parent.document).find('.frame').css('height',$('body').height() + 100);
			 }	
			 else{
				 $('#daiban').show();
			 }	
		if(runningTasks != null && runningTasks != undefined){
			for (var int = 0; int < runningTasks.length; int++) {
				 var html = createStateOtherCard(runningTasks[int],0);
				 otherCard.append(html);
			 }
		     $(window.parent.document).find('.frame').css('height',$('body').height() + 100);
		 }	
	}, getContextPath() + '/project/ajax/loadRuntasks',null);	
}

function loadPause(){
	$('.setMission').html('');
	var otherCard  =  $('.setCard');
	otherCard.html('');
	loadData(function(res){  
		if(res != null && res != undefined && res != ''){
			for (var int = 0; int < res.length; int++) {
				 var html = createStateOtherCard(res[int],1);
				 otherCard.append(html);
			 }
		     $(window.parent.document).find('.frame').css('height',$('body').height() + 100);
		 }else{
			 $('#daiban').show();
		 }	
	}, getContextPath() + '/project/ajax/loadSuspendList',null);	
}

function loadFinish(){
	$('.setMission').html('');
	var otherCard  =  $('.setCard');
	otherCard.html('');
	loadData(function(res){  

		if(res != null && res != undefined && res != ''){
			for (var int = 0; int < res.length; int++) {				
				if(res[int].pmsProjectFlow.projectStatus == 'finished'){
					var html = createStateOtherCard(res[int],2);
				}else{
					var html = createStateOtherCard(res[int],3);
				}
				 otherCard.append(html);
			 }
		     $(window.parent.document).find('.frame').css('height',$('body').height() + 100);
		 }else{
			 $('#daiban').show();
		 }	

	}, getContextPath() + '/project/ajax/loadFinishedList',null);
}

//获取搜索
function getState(name){
	$('.setMission').html('');
	var otherCard = $('.setCard');
	otherCard.html('');
	if(name == 0){
		loadDoing();
	}else{
		loadData(function(res){     
			if(res != null && res != undefined){
				for (var int = 0; int < res.length; int++) {
						 var html = createOtherCard(res[int]);
						 otherCard.append(html);
					 }
				$(window.parent.document).find('.frame').css('height',$('body').height() + 100);
			 }	
		}, getContextPath() + '/project/search/stage', $.toJSON({
			taskStage : name
		}));
	}
}

function createWaitCard(res){
	var isWho = "";
	var timeImg = "";
	var time = "";
	var aTag = '<a href="/project/phone/todo/'+res.taskId+'/'+res.pmsProjectFlow.projectId+'/'+res.processInstanceId+'?task">';
	if(res.isPrincipal == 1){
		isWho = '<div class="your">'+res.pmsProjectFlow.principalName+'</div>';  
	}else{
	    isWho = '<div class="user">负责人:'+res.pmsProjectFlow.principalName+'</div>';  
	}
	   var nowData = Date.parse(new Date());
	   var time =res.dueDate;
	   var lastTime = (time - nowData);
	   var lastHour =(time - nowData)/3600000;
	   if(lastHour < 0){
		   timeImg = '<img class="taskImg" src="/resources/images/pFlow/demoR.png">'; 
		   time='已超时'+getTimeString(lastTime); 
	   }
	   if(lastHour >= 3){
		   timeImg = '<img class="taskImg" src="/resources/images/pFlow/demoG.png">';
		   time='剩余'+getTimeString(lastTime); 
	   }
	   if(lastHour <3 && lastHour>=0){
		   timeImg = '<img class="taskImg" src="/resources/images/pFlow/demoY.png">';
		   time='剩余'+getTimeString(lastTime);
	   }

  var html = [
	    ' '+aTag+' ',
	    '<div class="MissionCard">',
	    ' <div class="cardTop">                                                      ',
	    '<div class="cardState">待办</div>',
	    '     <div class="cardName">'+res.pmsProjectFlow.projectName+'</div>                                   ',
	    '     '+isWho+'                                       ',
	    ' </div>                                                                     ',
	    ' <div class="cardBot">                                                      ',
	    '        <div class="taskName">'+res.taskName+'</div>                              ',
	    '        <div class="taskTime">'+time+'</div>                     ',
	    '        '+timeImg+'     ',
	    ' </div>                                                                     ',
	    ' </div>',  
	    '</a>',
	].join('');                                                                                                                       
	return html;
}

function createStateOtherCard(res,stage){
	var isWho = "";
	var taskStatus = res.taskStatus;
	var taskStage = res.taskStage;
	var time = "";
	var img = "";
	var aTag = '<a href="/project/phone/todo/'+res.taskId+'/'+res.pmsProjectFlow.projectId+'/'+res.processInstanceId+'">';
	if(res.isPrincipal == 1){
		isWho = '<div class="your">'+res.pmsProjectFlow.principalName+'</div>';  
	}else{
	    isWho = '<div class="user">负责人:'+res.pmsProjectFlow.principalName+'</div>';  
	}
	
	if(stage == 1){
		 time ="暂停于"+formatDate(res.pmsProjectFlow.suspendDate);
		 img= '<img class="taskImg" src="/resources/images/pFlow/isPause.png"> ';
	}
	if(stage == 2){
		 time ="完成于"+formatDate(res.pmsProjectFlow.suspendDate);
		 img= '<img class="taskImg" src="/resources/images/pFlow/isFinish.png"> ';
	}
	if(stage == 3){
		 time ="取消于"+formatDate(res.pmsProjectFlow.suspendDate);
		 img= '<img class="taskImg" src="/resources/images/pFlow/isCancle.png"> ';
	}
	if(stage == 0){
	if(taskStatus == "running" || taskStatus == null){
			time ="截止于"+formatDate(res.pmsProjectFlow.createDate);
			if(taskStage == '沟通阶段'){
				 img= '<img class="taskImg" src="/resources/images/pFlow/isTalk.png"> ';
			}
			if(taskStage == '方案阶段'){
				img= '<img class="taskImg" src="/resources/images/pFlow/isF.png"> ';
					}
			if(taskStage == '商务阶段'){
				img= '<img class="taskImg" src="/resources/images/pFlow/isBus.png"> ';
			}
			if(taskStage == '制作阶段'){
				img= '<img class="taskImg" src="/resources/images/pFlow/ismade.png"> ';
			}
	        if(taskStage == '交付阶段'){
	        	img= '<img class="taskImg" src="/resources/images/pFlow/isPay.png"> ';
			}	
		}
	}
	
	var taskname = res.taskName;
	
	if(taskStatus == "suspend"){
		  img= '<img class="taskImg" src="/resources/images/pFlow/isPause.png"> ';
		  var getTime = res.suspendDate;
		  time ="暂停于"+formatDate(getTime);
	}
	if(taskStatus == "completed"){
		  img= '<img class="taskImg" src="/resources/images/pFlow/isFinish.png"> ';
		  var getTime = res.finishedDate;
		  time = "结束于"+formatDate(getTime);
		  taskname = "";
	}
	
	if(taskStatus == "cancel"){
		  img= '<img class="taskImg" src="/resources/images/pFlow/isCancle.png"> ';
		  var getTime = res.cancelDate;
		  time = "取消于"+formatDate(getTime);
	}
	            var html = [
	            	' '+aTag+' ',
	            	' <div class="otherCard">                                                    ',
	                ' <div class="cardTop">                                                      ',
	                '     <div class="cardName">'+res.pmsProjectFlow.projectName+'</div>                                   ',
	                '     '+isWho+'                                       ',
	                ' </div>                                                                     ',
	                ' <div class="cardBot">                                                      ',
                    '        <div class="taskName">'+res.taskName+'</div>                              ',
                    '        <div class="taskTime">'+time+'</div>                     ',
                    '        '+img+'     ',
	                ' </div>                                                                     ',
	                ' </div>',
	                '</a>',
		].join('');                                                                                                                       
		return html;
	}




function createOtherCard(res){
	var isWho = "";
	var taskStatus = res.taskStatus;
	var taskStage = res.taskStage;
	var time = "";
	var img = "";
	var aTag = '<a href="/project/phone/todo/'+res.taskId+'/'+res.projectId+'/'+res.processInstanceId+' ">';
	if(res.isPrincipal == 1){
		isWho = '<div class="your">'+res.principalName+'</div>';  
	}else{
	    isWho = '<div class="user">负责人:'+res.principalName+'</div>';  
	}
	if(taskStatus == "running" || taskStatus == null){
		 time ="截止于"+formatDate(res.createTime);
		if(taskStage == '沟通阶段'){
			 img= '<img class="taskImg" src="/resources/images/pFlow/isTalk.png"> ';
		}
		if(taskStage == '方案阶段'){
			img= '<img class="taskImg" src="/resources/images/pFlow/isF.png"> ';
				}
		if(taskStage == '商务阶段'){
			img= '<img class="taskImg" src="/resources/images/pFlow/isBus.png"> ';
		}
		if(taskStage == '制作阶段'){
			img= '<img class="taskImg" src="/resources/images/pFlow/ismade.png"> ';
		}
        if(taskStage == '交付阶段'){
        	img= '<img class="taskImg" src="/resources/images/pFlow/isPay.png"> ';
		}	
	}
	
	var taskname = res.taskName;
	
	if(taskStatus == "suspend"){
		  img= '<img class="taskImg" src="/resources/images/pFlow/isPause.png"> ';
		  var getTime = res.suspendDate;
		  time ="暂停于"+formatDate(getTime);
	}
	if(taskStatus == "completed"){
		  img= '<img class="taskImg" src="/resources/images/pFlow/isFinish.png"> ';
		  var getTime = res.finishedDate;
		  time = "结束于"+formatDate(getTime);
		  taskname = "";
	}
	
	if(taskStatus == "cancel"){
		  img= '<img class="taskImg" src="/resources/images/pFlow/isCancle.png"> ';
		  var getTime = res.cancelDate;
		  time = "取消于"+formatDate(getTime);
	}
	            var html = [
	            	' '+aTag+' ',
	            	' <div class="otherCard">                                                    ',
	                ' <div class="cardTop">                                                      ',
	                '     <div class="cardName">'+res.projectName+'</div>                                   ',
	                '     '+isWho+'                                       ',
	                ' </div>                                                                     ',
	                ' <div class="cardBot">                                                      ',
                    '        <div class="taskName">'+res.taskName+'</div>                              ',
                    '        <div class="taskTime">'+time+'</div>                     ',
                    '        '+img+'     ',
	                ' </div>                                                                     ',
	                ' </div>',
	                ' </a>',
		].join('');                                                                                                                       
		return html;
	}
