
$().ready(function() {
	document.domain = getUrl();	
	$(window.parent.document).find('.frame').css('height',$('.pages').height() + 280);
	titleNameInput();
	initPageEven();
	toSearch();
});
function titleNameInput(){
	$('.productListAreas #titleNameInput').focus(function(){
		$(this).addClass('pp');
	});
	$('.productListAreas #titleNameInput').blur(function(){
		$(this).removeClass('pp');
	});
}
function initPageEven(){
	$(window.parent.document).find('.frame').css('height',$('.pages').height() + 280);
	getDate();
	$('#toCreate').off('click').on('click',function(){
		$(window.parent.parent.parent.document).find('#toCreate').click();
	});
}
//全局搜索
function toSearch(){
	$('.search').off('click').on('click',function(){
		 var search = $('.titleNameWork input').val();
		 
		 if(search == "" || search == null || search ==undefined){
			 $('.errorItem').removeClass('errorTr');
		 }else{
			 $('.errorItem').addClass('errorTr');
			 $('.productList li').removeClass('checkLi');
			 getAllSearchInfo(search);
		 }
	});
	initSelect();
	$('.orderSelect li').off('click').on('click',function(e){    
	    var name = $(this).attr('data-id');
	   	$(this).parent().parent().find('div').text($(this).text());
	   	$('.productList li').removeClass('checkLi');
	   	getState(name);
    });
	
}

function getAllSearchInfo(search){
	loadData(function(res){     
		var setCard = $('#setCard');
		var otherCard = $('#otherCard');
		setCard.html('');
		otherCard.html('');
		$('#otherWord').text('其它任务');
		$('#isOther').hide();
		if(res != null && res != undefined){
				$('#daibanName').addClass('hide');
			for (var int = 0; int < res.length; int++) {
				 if(res[int].agent == 1){
					 var html = createWaitCard(res[int]);
					 setCard.append(html);
					 $('#daibanName').removeClass('hide');
				 }else{
					 var html = createOtherCard(res[int]);
					 otherCard.append(html);
				 }
			}
			$(window.parent.document).find('.frame').css('height',$('.pages').height() + 100);
			$('#daiNum').text($('.waitCard').length);
			$('#otherNum').text($('.otherCard').length);
			if($('.waitCard').length == 0){
				$(window.parent.parent.parent.parent.parent.document).find('#cardNum').hide();
			}else{
				$(window.parent.parent.parent.parent.parent.document).find('#cardNum').show();
				$(window.parent.parent.parent.parent.parent.document).find('#cardNum').text($('.waitCard').length);
			  }
//			$(window.parent.document).find('.frame').css('height',$('.pages').height() + 100);
		}
//		$(window.parent.document).find('.frame').css('height',$('.pages').height() + 100);
	}, getContextPath() + '/project/search', $.toJSON({
		projectName : search
	}));
}

function getState(name){
	var otherCard = $('#otherCard');
	otherCard.html('');
	$('#otherWord').text('其它任务');
	loadData(function(res){     
		if(res != null && res != undefined){
			for (var int = 0; int < res.length; int++) {
					 var html = createOtherCard(res[int]);
					 otherCard.append(html);
				 }
			$('#otherNum').text($('.otherCard').length);
//			$(window.parent.document).find('.frame').css('height',$('.pages').height() + 100);
		 }	
	}, getContextPath() + '/project/agent/search', $.toJSON({
		taskStage : name
	}));
}

//获取日期时间
function getDate(){
	
	$('#daiNum').text($('.waitCard').length);
	$('#otherNum').text($('.otherCard').length);
	if($('div').hasClass("waitCard")){
	if($('.waitCard').length == 0){
		$(window.parent.parent.parent.parent.parent.document).find('#cardNum').hide();
	}else{
		$(window.parent.parent.parent.parent.parent.document).find('#cardNum').show();
		$(window.parent.parent.parent.parent.parent.document).find('#cardNum').text($('.waitCard').length);
	  }
	}
	var setTime =  $('.setLastTime');
	if(setTime.length >= 0){
		var nowData = Date.parse(new Date());
        for (var i = 0; i < setTime.length; i++) {
		   var time =Date.parse($(setTime[i]).text().replace("CST","GMT+0800"));
		   var lastTime = (time - nowData);
		   var lastHour =(time - nowData)/3600000;
		   var getTime =$(setTime[i]).text();
		   if(lastHour < 0){
			   $(setTime[i]).parent().parent().find('img').attr('src','/resources/images/flow/demoR.png');
			   $(setTime[i]).text(' 已超时 '+getTimeString(lastTime)); 
		   }
		   if(lastHour >= 3){
			   $(setTime[i]).parent().parent().find('img').attr('src','/resources/images/flow/demoG.png');
			   $(setTime[i]).text('剩余'+getTimeString(lastTime));
		   }
		   if(lastHour <3 && lastHour>=0){
			   $(setTime[i]).parent().parent().find('img').attr('src','/resources/images/flow/demoY.png');
			   $(setTime[i]).text('剩余'+getTimeString(lastTime));
		   }
		   
	    }		
	}

	var otherTime = $('.otherTime');
	if(otherTime.length >= 0){
        for (var i = 0; i < otherTime.length; i++) { 
		   var getTime = Date.parse($(otherTime[i]).text().replace("CST","GMT+0800"));
		   $(otherTime[i]).text('截止于'+formatDate(getTime));
	    }		
	}
	
	var pauseTime = $('.pauseTime');
	if(pauseTime.length >= 0){
        for (var i = 0; i < pauseTime.length; i++) {
		  
		   var getTime = Date.parse($(pauseTime[i]).text().replace("CST","GMT+0800"));
		   $(pauseTime[i]).text('暂停于'+formatDate(getTime));
	    }		
	}
	
	var finishTime = $('.finishTime');
	if(finishTime.length >= 0){
        for (var i = 0; i < finishTime.length; i++) {
		   var getTime = Date.parse($(finishTime[i]).text().replace("CST","GMT+0800"));
		   $(finishTime[i]).text('结束于'+formatDate(getTime));
	    }		
	}
	
	var dueTime = $('.setBorder');
	if(dueTime.length >= 0){
        for (var i = 0; i < dueTime.length; i++) {
           var theTime=  $(dueTime[i]).attr('data-content');
		   var getTime = Date.parse(theTime.replace("CST","GMT+0800"));
		   var nowData = Date.parse(new Date());
		   var lastHour =(getTime - nowData)/3600000;
		   if(lastHour < 0){
			   $(dueTime[i]).addClass('redWord');
		   }
	    }		
	}
}

function createWaitCard(res){
	var isWho = "";
	var timeImg = "";
	var time = ""
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
		   timeImg = '<img src="/resources/images/flow/demoR.png">'; 
		   time='已超时'+getTimeString(lastTime); 
	   }
	   if(lastHour >= 3){
		   timeImg = '<img src="/resources/images/flow/demoG.png">';
		   time='剩余'+getTimeString(lastTime); 
	   }
	   if(lastHour <3 && lastHour>=0){
		   timeImg = '<img src="/resources/images/flow/demoY.png">';
		   time='剩余'+getTimeString(lastTime);
	   }

	
  var html = [
	  '<div class="waitCard cardNum">',
	  '   <a href="/project/task/'+res.taskId+'/'+res.projectId+'/'+res.processInstanceId+'?task">',
	  '     <div class="cardH">                                                                                                       ',
	  '         <div class="title">'+res.projectName+'</div>                                                          ',
	  '     </div>                                                                                                                    ',
	  '     <div class="cardContent">                                                                                                 ',
	  '          '+timeImg+'                                                                        ',
	  '          <div class="setContent">                                                                                             ',
	  '              <div class="listName">'+res.taskName+'</div>                                                                   ',
	  '              <div class="lastTime setLastTime">'+time+'</div>                                                    ',
	  '              '+isWho+'   ',
	  '          </div>                                                                                                               ',
	  '     </div>                                                                                                                    ',
	  '    </a>                                                                                                                       ',
	  '</div>          ',                                                                                                             
	].join('');                                                                                                                       
	return html;
}

function createOtherCard(res){
	var isWho = "";
	var taskStatus = res.taskStatus;
	var taskStage = res.taskStage;
	var time = res.dueDate;
	var img = "";
	var redWord = "";
	var aTag = '<a href="/project/task/'+res.taskId+'/'+res.projectId+'/'+res.processInstanceId+'?doing">';
	
	if(res.isPrincipal == 1){
		isWho = '<div class="your">'+res.principalName+'</div>';  
	}else{
	    isWho = '<div class="user">负责人:'+res.principalName+'</div>';  
	}
	if(taskStatus == "running" || taskStatus == null){
		 time ="截止于"+formatDate(res.dueDate);
			var nowData = Date.parse(new Date());
			   var lastTime = (time - nowData);
			   var lastHour =(time - nowData)/3600000;
			   if(lastHour < 0){
				   redWord = "redWord";
			}
		if(taskStage == '沟通阶段'){
			 img= '<img src="/resources/images/flow/isTalk.png"> ';
		}
		if(taskStage == '方案阶段'){
			img= '<img src="/resources/images/flow/isFang.png"> ';
				}
		if(taskStage == '商务阶段'){
			img= '<img src="/resources/images/flow/isPrice.png"> ';
		}
		if(taskStage == '制作阶段'){
			img= '<img src="/resources/images/flow/isMake.png"> ';
		}
        if(taskStage == '交付阶段'){
        	img= '<img src="/resources/images/flow/isPay.png"> ';
		}	
	}
	
	var taskname = res.taskName;
	
	if(taskStatus == "suspend"){
		  img= '<img src="/resources/images/flow/isPause.png"> ';
		  var getTime = res.suspendDate;
		  time ="暂停于"+formatDate(getTime);
		  aTag = '<a href="/project/task/'+res.taskId+'/'+res.projectId+'/'+res.processInstanceId+'?pause">';
	}
	if(taskStatus == "completed"){
		  img= '<img src="/resources/images/flow/isFinish.png"> ';
		  var getTime = res.finishedDate;
		  time = "结束于"+formatDate(getTime);
		  taskname = "";
		  aTag = '<a href="/project/task/'+res.taskId+'/'+res.projectId+'/'+res.processInstanceId+'?status=finished">';
	}	
	if(taskStatus == "cancel"){
		  img= '<img src="/resources/images/flow/isCancle.png"> ';
		  var getTime = res.cancelDate;
		  time = "取消于"+formatDate(getTime);
		  aTag = '<a href="/project/task/'+res.taskId+'/'+res.projectId+'/'+res.processInstanceId+'?cancel">';
	}
		
	            var html = [
				' <div class="otherCard redWord">',
				'	        '+aTag+' ',
				'           <div class="cardH">                                                                                                           ',
				'               <div class="title">'+res.projectName+'</div>                                                              ',              
				'           </div>                                                                                                                        ',
				'           <div class="cardContent">                                                                                                     ',
				'                <div class="setContent">                                                                                                 ',
				'                    <div class="listName">'+taskname+'</div>                                                                       ',
				'                    <div class="lastTime otherTime">'+time+'</div>                                                       ',
				'                '+isWho+'  ',
				'                </div>                                                                                                                   ',
				'                '+img+'                                                                          ',
				'           </div>                                                                                                                        ',
				'          </a>                                                                                                                           ',
				'      </div>                                                                                                                             ',
				'</div>                                                                                                                                 ',
		].join('');                                                                                                                       
		return html;
	}
