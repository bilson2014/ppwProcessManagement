
$().ready(function() {
	initPageEven();
	toSearch();
});

function initPageEven(){
	/*	var waitCard = $('.waitCard');
	var cardNUm = waitCard.length;
	if(waitCard.length == null || waitCard.length=="" ){
		$(window.parent.parent.parent.document).find('#cardNum').hide();
	}else{
		$(window.parent.document).find('#cardNum').text(cardNUm);
	}*/
	$(window.parent.document).find('.frame').css('height',$('.pages').height() + 300);
	getDate();
	$('#toCreate').off('click').on('click',function(){
		$(window.parent.parent.parent.document).find('#toCreate').click();
	});
}

//全局搜索
function toSearch(){
	$('.search').off('click').on('click',function(){
		 var search = $('.titleNameWork input').val();
		 getAllSearchInfo(search);
	});
	initSelect();
	$('.orderSelect li').off('click').on('click',function(e){    
	    var id = $(this).attr('data-id');
	   	$(this).parent().parent().find('div').text($(this).text());
	   	$(this).parent().parent().find('div').attr('data-id',id);
	   	$('.productList li').removeClass('checkLi');
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
				 if(res[i].agent == 1){
					 var html = createWaitCard(res[i]);
					 setCard.append(html);
				 }else{
					 var html = createOtherCard(res[i]);
					 otherCard.append(html);
				 }
			};
		}
	}, getContextPath() + '/project/search', $.toJSON({
		projectName : search
	}));
}

//获取日期时间
function getDate(){
	
	$('#daiNum').text($('.waitCard').length);
	if($('div').hasClass("waitCard")){
	if($('.waitCard').length == 0){
		$(window.parent.parent.parent.parent.parent.document).find('#cardNum').hide();
	}else{
		$(window.parent.parent.parent.parent.parent.document).find('#cardNum').show();
		$(window.parent.parent.parent.parent.parent.document).find('#cardNum').text($('.waitCard').length);
	}
	}
	$('#otherNum').text($('.otherCard').length);
	
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
	   var time =Date.parse(res.dueDate.replace("CST","GMT+0800"));
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
		   time='剩余'+getTimeString(lastTime); );
	   }

	
  var html = [
	  '<div class="waitCard cardNum">',
	  '   <a href="/project/task/'+res.taskId+'/'+res.projectId+'/'+res.processInstanceId+'?task">',
	  '     <div class="cardH">                                                                                                       ',
	  '         <div class="title">'+res.projectName+'</div>                                                          ',
	  '              '+isWho+' ',
	  '     </div>                                                                                                                    ',
	  '     <div class="cardContent">                                                                                                 ',
	  '          '+timeImg+'                                                                        ',
	  '          <div class="setContent">                                                                                             ',
	  '              <div class="listName">'+res.taskName+'</div>                                                                   ',
	  '              <div class="lastTime setLastTime">'+time+'</div>                                                    ',
	  '          </div>                                                                                                               ',
	  '     </div>                                                                                                                    ',
	  '    </a>                                                                                                                       ',
	  '</div>          ',                                                                                                             
	].join('');                                                                                                                       
	return html;
}

function createOtherCard(res){
	
/*	<c:if test="${staff.taskStage == '沟通阶段'}">
    <img src="/resources/images/flow/isTalk.png">
    </c:if>
    <c:if test="${ staff.taskStage == '方案阶段'}">
    <img src="/resources/images/flow/isFang.png">
    </c:if>
    <c:if test="${ staff.taskStage == '商务阶段'}">
    <img src="/resources/images/flow/isPrice.png">
    </c:if>
    <c:if test="${ staff.taskStage == '制作阶段'}">
    <img src="/resources/images/flow/isMake.png">
    </c:if>
    <c:if test="${staff.taskStage == '交付阶段'}">
    <img src="/resources/images/flow/isPay.png">
    </c:if>*/
	var isWho = "";
	var taskStatus = res.taskStatus;
	var taskStage = res.taskStage;
	var time = "";
	var img = "";
	var aTag = '<a href="/project/task/'+res.taskId+'/'+res.projectId+'/'+res.processInstanceId+'?task">';
	if(res.isPrincipal == 1){
		isWho = '<div class="your">'+res.principalName+'</div>';  
	}else{
	    isWho = '<div class="user">负责人:'+res.principalName+'</div>';  
	}
	if(taskStatus == "进行中"){
		 var getTime = Date.parse(res.createTime.replace("CST","GMT+0800"));
		 time ="截止于"+timeformatDate(getTime);
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
	
	if(taskStatus == "暂停"){
		  img= '<img src="/resources/images/flow/suspendDate.png"> ';
		  var getTime = Date.parse(res.pauseTime.replace("CST","GMT+0800"));
		  time ="暂停于"+timeformatDate(getTime);
	}
	if(taskStatus == "完成"){
		  img= '<img src="/resources/images/flow/isPay.png"> ';
		  var getTime = Date.parse(res.finishedDate.replace("CST","GMT+0800"));
		  time = "结束于"+timeformatDate(getTime);
	}
	
	            var html = [
				' <div class="otherCard">',
				'	        '+aTag+' ',
				'           <div class="cardH">                                                                                                           ',
				'               <div class="title">'+res.projectName+'</div>                                                              ',
				'                '+isWho+'                                                                                 ',                                                                                                                 ',
				'           </div>                                                                                                                        ',
				'           <div class="cardContent">                                                                                                     ',
				'                <div class="setContent">                                                                                                 ',
				'                    <div class="listName">'+res.taskName+'</div>                                                                       ',
				'                    <div class="lastTime otherTime">'+time+'</div>                                                       ',
				'                </div>                                                                                                                   ',
				'                '+img+'                                                                          ',
				'           </div>                                                                                                                        ',
				'          </a>                                                                                                                           ',
				'      </div>                                                                                                                             ',
				'</div>                                                                                                                                 ',
		].join('');                                                                                                                       
		return html;
	}





