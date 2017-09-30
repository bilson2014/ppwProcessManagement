$().ready(function(){
	searchEven();
	$(window.parent.document).find('.headerCom').show();
	$(window.parent.document).find('.footBot').hide();
});

//搜索事件
function searchEven(){
	$('#toSearch').off('click').on('click',function(){
		var search= $(this).parent().find('input').val();
		getAllSearchInfo(search);
	});
}
function getAllSearchInfo(search){
	loadData(function(res){     
		var setCard = $('#setCard');
		var otherCard = $('#otherCard');
		setCard.html('');
		otherCard.html('');
		if(res != null && res != undefined){
			for (var int = 0; int < res.length; int++) {
				 if(res[int].agent == 1){
					 var html = createWaitCard(res[int]);
					 setCard.append(html);
				 }else{
					 var html = createOtherCard(res[int]);
					 otherCard.append(html);
				 }
			}
			getHeight();
		}
	}, getContextPath() + '/project/search', $.toJSON({
		projectName : search
	}));
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
	var aTag = '<a href="/project/phone/todo/'+res.taskId+'/'+res.projectId+'/'+res.processInstanceId+'?task">';
	if(res.isPrincipal == 1){
		isWho = '<div class="your">'+res.principalName+'</div>';  
	}else{
	    isWho = '<div class="user">负责人:'+res.principalName+'</div>';  
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
	    '     <div class="cardName">'+res.projectName+'</div>                                   ',
	    ' </div>                                                                     ',
	    ' <div class="cardBot">                                                      ',
	    '     '+isWho+'                                       ',
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
	var redWord = "";
	var taskname = res.taskName;
	var aTag = '<a href="/project/phone/todo/'+res.taskId+'/'+res.projectId+'/'+res.processInstanceId+'">';
	if(res.isPrincipal == 1){
		isWho = '<div class="your">'+res.principalName+'</div>';  
	}else{
	    isWho = '<div class="user">负责人:'+res.principalName+'</div>';  
	}
	var taskname = res.taskName;
	
	if(stage == 1){
		 time ="暂停于"+formatDate(res.suspendDate);
		 img= '<img class="taskImg" src="/resources/images/pFlow/isPause.png"> ';
		 var aTag = '<a href="/project/phone/todo/'+res.taskId+'/'+res.projectId+'/'+res.processInstanceId+'?pause">';
	}
	if(stage == 2){
		 time ="完成于"+formatDate(res.endTime);
		 img= '<img class="taskImg" src="/resources/images/pFlow/isFinish.png"> ';
		 var aTag = '<a href="/project/phone/todo/'+res.taskId+'/'+res.projectId+'/'+res.processInstanceId+'?finish">';
		 taskname = "";
	}
	if(stage == 3){
		 time ="取消于"+formatDate(res.suspendDate);
		 img= '<img class="taskImg" src="/resources/images/pFlow/isCancle.png"> ';
		 var aTag = '<a href="/project/phone/todo/'+res.taskId+'/'+res.projectId+'/'+res.processInstanceId+'?cancel">';
		 taskname = "";
	}
	if(stage == 0){
	if(taskStatus == "running" || taskStatus == null){
		   var nowData = Date.parse(new Date());
		   var time =res.dueDate;
		   var lastTime = (time - nowData);
		   var lastHour =(time - nowData)/3600000;
		   if(lastHour < 0){
		   redWord = "redWord";
		   }
		   time ="截止于"+formatDate(res.dueDate);
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

	
	
/*	if(taskStatus == "suspend"){
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
	}*/
	            var html = [
	            	' '+aTag+' ',
	            	' <div class="otherCard '+redWord+'">                                                    ',
	                ' <div class="cardTop">                                                      ',
	                '     <div class="cardName">'+res.pmsProjectFlow.projectName+'</div>                                   ',
	                ' </div>                                                                     ',
	                ' <div class="cardBot">                                                      ',
	        	    '     '+isWho+'                                       ',
                    '        <div class="taskName">'+taskname+'</div>                              ',
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
	var redWord="";
	var aTag = '<a href="/project/phone/todo/'+res.taskId+'/'+res.projectId+'/'+res.processInstanceId+' ">';
	if(res.isPrincipal == 1){
		isWho = '<div class="your">'+res.principalName+'</div>';  
	}else{
	    isWho = '<div class="user">负责人:'+res.principalName+'</div>';  
	}
	if(taskStatus == "running" || taskStatus == null){
		  var nowData = Date.parse(new Date());
		   var time =res.dueDate;
		   var lastTime = (time - nowData);
		   var lastHour =(time - nowData)/3600000;
		   if(lastHour < 0){

			   redWord = "redWord";
		   }
			 time ="截止于"+formatDate(res.dueDate);	   
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
	            	' <div class="otherCard '+redWord+'">                                                    ',
	                ' <div class="cardTop">                                                      ',
	                '     <div class="cardName">'+res.projectName+'</div>                                   ',
	                ' </div>                                                                     ',
	                ' <div class="cardBot">                                                      ',
	        	    '     '+isWho+'                                       ',
                    '        <div class="taskName">'+res.taskName+'</div>                              ',
                    '        <div class="taskTime">'+time+'</div>                     ',
                    '        '+img+'     ',
	                ' </div>                                                                     ',
	                ' </div>',
	                ' </a>',
		].join('');                                                                                                                       
		return html;
	}
