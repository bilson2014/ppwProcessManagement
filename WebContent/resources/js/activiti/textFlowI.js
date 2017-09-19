var InterValObj; // timer变量，控制时间  
var count = 120; // 间隔函数，1秒执行  
var curCount; // 当前剩余秒数 
var upload_Video;
var upload_VideoFile;
var video_max_size = 200*1024*1024; // 200MB
var video_err_msg = '视频大小超出200M上限,请重新上传!';
$().ready(function() {
	document.domain = getUrl();
	// 加载动态表单
	    var href = window.location.href;
	    var state = href.substr(href.lastIndexOf("?")+1,href.length);
	    if(state.trim() == "status=finished"){
	    	$('#imgWord').text('完成');
			$('#lastTimeWord').hide();
	    	$('#taskStage').val('交付阶段');
	    	getStageInfo($('#taskStage').val());
	    }else{
	    	var task = $('#taskStage').val();
	    	getStageInfo(task);
	    	$('#lastTimeWord').show();
	    }
	
	pageInit();
	initDaibanTime();
	initResouces();
});

function getScroll(){
	$(window.parent.parent.parent.document).find('body').scrollTop(0);
}

//初始化来源
function initResouces(){
	loadData(function (res){
		var body = $('#pResour');
		body.html('');
		var rowsR = res.result.resource;
		if(rowsR != null && rowsR != undefined){
			for (var int = 0; int < rowsR.length; int++) {
					var html =createOption(rowsR[int].id,rowsR[int].text);
				body.append(html);
			}
			initSelect();
		}	
	}, getContextPath() + '/product/productSelection',null);
}


function getHeight(){
	var height = $('.pages').height() + 300;
	$(window.parent.document).find('.frame').css('height',height);
}
//待办任务时间
function initDaibanTime(){
	   var nowData = Date.parse(new Date());
	   var time =Date.parse($('#missinTime').text().replace("CST","GMT+0800"));
	   var lastTime = (time - nowData);
	   var lastHour =(time - nowData)/3600000;
	   var getTime =$('#missinTime').text();
	   if(lastHour < 0){
		 $('#missinTime').text(' 已超时 '+getTimeString(lastTime)); 
	   }
	   if(lastHour >= 3){
		   $('#missinTime').text('剩余'+getTimeString(lastTime));
	   }
	   if(lastHour <3 && lastHour>=0){
		   $('#missinTime').text('剩余'+getTimeString(lastTime));
	   }
}


//流程信息
function initLastTime(ctyle,createTime){
	var time = 86400 * ctyle *1000;
	var day =  Date.parse(new Date(createTime));
	var totalTime = time + day;
	var nowData = Date.parse(new Date());
	var checkDay = nowData - totalTime;
	var href = window.location.href;
    var state = href.substr(href.lastIndexOf("?")+1,href.length);
    
    if(state.trim() == "pause"){
    	$('#imgFlow').addClass('imgRed');
		$('#imgWord').text('暂停');
		$('#imgWord').attr('style','color:#fe5453');
		$('#lastTimeWord').text("");
    }else{
    	if(checkDay >= 0){
    		$('#imgFlow').addClass('imgRed');
    		$('#imgWord').text('延误');
    		$('#imgWord').attr('style','color:#fe5453');
    		$('#lastTimeWord').text('超过'+parseInt((checkDay/86400000))+"天");
    	}
    	else{
    		$('#imgFlow').addClass('imgFlow');
    		$('#imgWord').text('正常');
    		$('#lastTimeWord').text('剩余'+parseInt(((totalTime - nowData)/86400000))+"天");
    	}	
    }
    if(state.trim() == "status=finished"){
    	$('#imgWord').text('完成');
    }
    
}

function stageTalkEven(){
	$('.findTalk').off('click').on('click',function(){
		var id = $(this).attr('data-id');
		var name = $(this).attr('data-name');
		$('#infoNameTitle').attr('data-name',name);
		$('#cusModel').show();
		getScroll();
		$('#infoNameTitle').text("");
		$('#stateContent').html("");
		$('#infoStartTime').text("");
		$('#infoEndTime').text("");
		$('#itemHeightInfo').html('');
		loadData(function(res){		
			initStageInfoTop(res);	
			loadStageInfoEven();
		}, getContextPath() + '/project/task-detail/'+id,null);
		
	});
}

function loadStageInfoEven(name){
	loadData(function(res){	
		var body =$('#itemHeightInfo');
		body.html('');
		if(res != null && res != undefined){
			for (var int = 0; int < res.length; int++) {
				   var html =crearteInfoCard(res[int]);
				   body.append(html);
			}
			openInfoCard();
			infoAddReplyEven();
		}
	}, getContextPath() + '/message/getTaskMsg/',$.toJSON({
		projectId:$('#projectId').val(),
		taskName:$('#infoNameTitle').attr('data-name')
	}));
}

function initStageInfoTop(res){
	$('#infoNameTitle').text(res.taskName);
	$('#stateContent').html(res.taskDescription);
	$('#infoStartTime').text(formatDate(res.startTime));
	$('#infoEndTime').text(formatDate(res.endTime));
	var checkStatus = res.taskStatus;
	if(checkStatus == "completed"){
		$('#stateImg').attr('src',"/resources/images/flow/toComplet.png");
		$('#stateWord').text('已完成');
		$('#stateWord').attr('style','color:#79D01B');
		$('#infoEndTitle').text('完成时间');
		$('#infoEndTime').text(formatDate(res.endTime));
	}
	if(checkStatus == "running"){
		$('#stateImg').attr('src',"/resources/images/flow/toWati.png");
		$('#stateWord').text('进行中');
		$('#stateWord').attr('style','color:#fe5453');
		$('#infoEndTitle').text('预计时间');
		$('#infoEndTime').text(formatDate(res.dueDate));
	}
	if(checkStatus == "futher"){
		$('#stateImg').attr('src',"/resources/images/flow/toStart.png");
		$('#stateWord').text('未开始');
		$('#stateWord').attr('style','color:#F5A623');
		$('#infoEndTitle').text('预计时间');
		$('#infoEndTime').text(formatDate(res.dueDate));
	}
}

function crearteInfoCard(res){		
	var  children= res.children;
	var body = '';
	if(children != null && children != undefined && children !=""){
		for (var int = 0; int < children.length; int++) {
			body +='<div>'+children[int].fromName+' 回复 :'+children[int].content+'</div><div>'+formatDate((children[int].createDate).replace("CST","GMT+0800"))+'</div>';
		}
	}
	if(res.fromUrl == null || res.fromUrl == "" ){
		var  imgUrl = "/resources/images/flow/def.png";
	}else{
		var  imgUrl = getDfsHostName()+res.fromUrl;
	}

	var html = [
				'<div class="infoItem">',
				'    <div  class="itemTop">',
				'          <img class="logo" src="'+imgUrl+'">                                                                                ',
				'           <ul>                                                                                                    ',
				'              <li><div>'+res.fromName+' : '+res.content+'</div><div>'+formatDate((res.createDate).replace("CST","GMT+0800"))+'</div><img class="modelOpen " src="/resources/images/flow/areaMore.png"></li>                                                             ',
				/*              <li><div>'+res.taskName+'</div> <img class="modelOpen " src="/resources/images/flow/areaMore.png"></li>',
*/				'           </ul>                                                                                                   ',
				'    </div>                                                                                                         ',
				'    <div class="itemArea">                                                                                         ',
				'              '+body+'                                       ',
				'          <input>                                                                                                  ',
				'    </div> ',
				'<div class="errorSpan"></div>',
				'    <div class="backInfoTalk btn-c-r"  data-parentId="'+res.projectMessageId+'"  data-name="'+res.taskName+'" data-projectId="'+res.projectId+'">回复</div>                                                                   ',
				'</div>                                                                                                       '
			].join('');
			return html;
}

function infoAddReplyEven(){
	
	 $('.backInfoTalk').off('click').on('click',function(){
		    var projectId = $(this).attr('data-projectId');
		    var name = $(this).attr('data-name');
		    var parentId = $(this).attr('data-parentId');
		    var content = $(this).parent().find('.itemArea').find('input').val();
		    
		    if(content == null || content == "" || content == undefined){
		    	$(this).parent().find('.errorSpan').text('回复不能为空');
		    }else{
				loadData(function(res){	
		           if(res.code == 200){
		            loadStageInfoEven();
		            initAllTalk();
		           }
				}, getContextPath() + '/message/addReply',$.toJSON({
					projectId:projectId,
					taskName:name,
					parentId:parentId,
					content:content
				}));
		    }
	 });
	
}


function createStageInfo(res,state){
	var whatLine = "lineOne";
	if(state == "s"){
		whatLine = "lineStart";
	}
	if(state == "e"){
		whatLine = "lineEnd";
	}
	
	var checkStatus = res.taskStatus;
	
	if(checkStatus == "completed"){
		var time = '<div class="time">'+formatDate(res.startTime)+'</div>';
		var imgUrl = '<div class="state"><img src="/resources/images/flow/toComplet.png"><div class="green">已完成</div></div>';
	}
	if(checkStatus == "running"){
		var time = '<div class="time">始于'+formatDate(res.startTime)+'</div>'; 
		var imgUrl = '<div class="state"><img src="/resources/images/flow/toWati.png"><div class="yellow">进行中</div></div>';
	}
	if(checkStatus == "futher"){
		var time = '<div class="time">预计'+formatDate(res.startTime)+'</div>';   
		var imgUrl = '<div class="state"><img src="/resources/images/flow/toStart.png"><div class="redWord">未开始</div></div>';
	}
    
	var html = [
   '<div class="listItem">                                                                                          ',
   '     <div class="'+whatLine+'"></div>                                                                              ',
   '     '+time+'                                                          ',
   '     <div class="user">'+res.taskName+'</div>                                                                          ',
   '     <div class="info hide"></div>                                                                           ',
   '     '+imgUrl+' ',
   '     <div class="find findTalk" data-id="'+res.taskId+'"  data-name="'+res.taskName+'">查看</div>                                                                               ',
   '</div>                                                                                                          ',
	].join('');
	return html;
}

function getStageInfo(stage){	
			var keys = stage;
			var resMap = "";			
			if(resMap == ""){
				loadData(function(res){
					initLastTime(res.projectCycle,res.createDate);
					if(res != null && res != undefined){
					    resMap = res;
						var sethtml="";
						var resKey = res[keys];
						getStageCard(keys,resKey);
					}
				}, getContextPath() + '/project/project-task/'+$('#projectId').val(),$.toJSON({projectName:keys}));
			}else{
				var resKey = resMap[keys];
				getStageCard(keys,resKey);
			}
}

function getStageCard(keys,resKey){
	var Stage = $('#taskStage').val();
	if(resKey.length > 0){
		 if(keys == "沟通阶段"){
			 $('.icons').attr('class','icons');
			 $('.icons').addClass('stepIcon');
		 }
	 if(keys == "方案阶段"){
		 $('.icons').attr('class','icons');
			 $('.icons').addClass('step2Icon');
		 }
	 if(keys == "商务阶段"){
		 $('.icons').attr('class','icons');
		 $('.icons').addClass('step3Icon');
	 }
	 if(keys == "制作阶段"){
		 $('.icons').attr('class','icons');
		 $('.icons').addClass('step4Icon');
	 }
	 if(keys == "交付阶段"){
		 $('.icons').attr('class','icons');
		 $('.icons').addClass('step5Icon');
	 }
		var body =$('#listContent');
		body.html('');
		var setBody = "";
		for (var int = 0; int < resKey.length; int++) {
			 if(int == 0){
				 var html =createStageInfo(resKey[int],"s"); 
				 $('#startTime').text(formatDate(resKey[int].startTime));
			 }if(int == resKey.length - 1){
				 var html =createStageInfo(resKey[int],"e"); 
			 }
			 if(int > 0 && int != resKey.length - 1)
			 {
				 var html =createStageInfo(resKey[int],'u');
			 }
			 setBody =html;
			 body.append(setBody);
		}
		stageTalkEven();
		getHeight();
	}
}

function toDoing(){
	$('#cancle').off('click').on('click',function(){
		$('#infoModel').hide();
	});
	$('#checkSure').off('click').on('click',function(){
		window.location.href = "/project/suspendProcess/"+$('#processInstanceId').val() + '/' + $('#projectId').val();
	});
}

function toPause(){
	$('#cancle').off('click').on('click',function(){
		$('#infoModel').hide();
	});
	$('#checkSure').off('click').on('click',function(){
		window.location.href = "/project/suspendProcess/"+$('#processInstanceId').val() + '/' + $('#projectId').val();
	});
}

function getFileInfo(){
	loadData(function(res){
		var res = res;
	/*	var body = $('.utoInfo');
		body.html('');*/
		if(res != null && res != undefined){
			for (var int = 0; int < res.length; int++) {
				   var html =createUserInfo(res[int].teamId,res[int].teamName,res[int].linkman,res[int].phoneNumber);
				   body.append(html);
			};
			
		}
	}, getContextPath() + '/resource/version/'+$('#currentTaskId').val(),null);
}


function checkState(){
	var href = window.location.href;
    var state = href.substr(href.lastIndexOf("?")+1,href.length);
    
    if(state.trim() != "task"){
    	$('#daiban').hide();
    }
    
    if(state.trim() == "task" ||state.trim() == "doing"){
    	$('#isBack').hide();
    }
    
    if(state.trim() == "pause"){
    	$('#isPause').hide();
    	$('#isCancle').hide();
    }
    
    if(state.trim() == "finish"){
    		 $('#isBack').hide();
    		 $('#isPause').hide();
    		 $('#isCancle').hide();
    }
    
    if(state.trim() == "task"){
    	addForm();
    }
}

function stageEven(){
	
	$('.stageTask').off('click').on('click',function(){
		getStageInfo($(this).attr('data-id'));
	});
	
}

function pageInit(){
	$('#toFinish').off('click').on('click',function(){
		$('#autoSet').show();
		$('#autoSet .item input').val('');
		$('#autoSet .item input').attr('data-id','');
	});
	var taskName = $('#taskName').val();
	 if(taskName == null || taskName == "" || taskName == undefined )	{
  	   $('#daiban').hide();
     }
	 var isStage = $('#taskStage').val();
	 if(isStage == "沟通阶段"){
		 $('.icons').addClass('stepIcon');
	 }
 if(isStage == "方案阶段"){
		 $('.flowIcon').addClass('step2');
		 $('.icons').addClass('step2Icon');
	 }
 if(isStage == "商务阶段"){
	 $('.flowIcon').addClass('step3');
	 $('.icons').addClass('step3Icon');
 }
 if(isStage == "制作阶段"){
	 $('.flowIcon').addClass('step4');
	 $('.icons').addClass('step4Icon');
 }
 if(isStage == "交付阶段"){
	 $('.flowIcon').addClass('step5');
	 $('.icons').addClass('step5Icon');
 }
 
    stageEven();
	openInfoCard();
	initEvenInfo();
	initSelect();
	flagEven();
	getFileInfo();
	initAddTalk();
	initAllTalk();
	getFileInfo();
	controlModel();
	checkState();
	getHeight();
	initWindow();
	initFileUpload();
	$('#projectCtyle').text($('#projectCtyle').text()+"天");
	if($('#projectTime').text()!=null && $('#projectTime').text()!="" && $('#projectTime').text()!=undefined )
    $('#projectTime').text(formatDate($('#projectTime').text().replace("CST","GMT+0800")));
}

//弹窗初始化
function initWindow(){
	openProjectInfo();
	openCusInfo();
	openPriceInfo();
	openProviderInfo();
	$('.btn-c-g').off('click').on('click',function(){
		$('.cusModel').hide();
	});
}
//项目信息修改
function openProjectInfo(){
	$('#openProjectInfo').off('click').on('click',function(){
		$('#showProjectInfo').show();
		getScroll();
		proInfoClear();
		loadData(function(res){
			  $('#proName').val(res.projectFlow.pf_projectName);
			  var Grade = res.projectFlow.pf_projectGrade;
			  $('#pf_projectGrade').attr('data-id',Grade);
				if(Grade == '5'){
					Grade = 'S';	  
				}
				if(Grade == '4'){
					Grade = 'A';			  
				}
				if(Grade == '3'){
					Grade = 'B';  
				}
				if(Grade == '2'){
					Grade = 'C';  
				}
				if(Grade == '1'){
					Grade = 'D';  
				}
				if(Grade == '0'){
					Grade = 'E';  
				}
			  $('#pf_ResourInput').val(res.projectFlow.pf_projectSource);
			  
			  var resour = $('#pResour li');
			  for (var i = 0; i < resour.length; i++) {
                     	if($(resour[i]).attr('data-id') == res.projectFlow.pf_projectSource){
                     		$('#pf_Resour').text($(resour[i]).text());
                     		$('#pf_Resour').attr('data-id',($(resour[i]).attr('data-id')))
                     	}			
			  }
				
			  $('#pf_projectGrade').text(Grade);
			  $('#proCycle').val(res.projectFlow.pf_projectCycle);
			  $('#proFdp').val(res.projectFlow.pf_filmDestPath);
			  $('#projectDes').val(res.projectFlow.pf_projectDescription);
		}, getContextPath() + '/project/task/edit/parameter/'+$("#currentTaskId").val()+"/"+$('#projectId').val()+"/pf",null);
	});
	
	$('#submitProject').off('click').on('click',function(){
		if(checkProviderInfo()){
			$('#toProjectForm').submit();
		}
	});
}

function checkProviderInfo(){
           var proName = $('#proName').val();
           var pf_projectGrade = $('#pf_projectGrade').attr('data-id');
           var pf_Resour = $('#pf_Resour').attr('data-id');
           var proCycle = $('#proCycle').val();
           var proFdp = $('#proFdp').val();
           var projectDes = $('#projectDes').val();
       
        if(proName == undefined || proName == "" || proName ==null ){
       		$('#proNameError').attr('data-content','项目名称未填写未填写');
       		return false;
       	}
        if(pf_projectGrade == undefined || pf_projectGrade == "" || pf_projectGrade ==null ){
       		$('#pf_projectGradeError').attr('data-content','项目评级未填写');
       		
       		return false;
       	}
        $('#pf_projectGradeInput').val(pf_projectGrade);
        if(pf_Resour == undefined || pf_Resour == "" || pf_Resour ==null ){
       		$('#pf_ResourInputError').attr('data-content','项目来源未填写');
       		return false;
       	}
        $('#pf_ResourInput').val(pf_Resour);
        if(proCycle == undefined || proCycle == "" || proCycle ==null ){
       		$('#proCycleError').attr('data-content','项目周期未填写');
       		return false;
       	}
        if(proFdp == undefined || proFdp == "" || proFdp ==null ){
       		$('#proFdpError').attr('data-content','对标影片地址未填写');
       		return false;
       	}
        if(projectDes == undefined || projectDes == "" || projectDes ==null ){
       		$('#projectDesError').attr('data-content','项目描述未填写');
       		return false;
       	}
        
        return true;
}

function proInfoClear(){
  $('#proName').val('');
  $('#pf_projectGrade').text('');
  $('#pf_projectGrade').attr('data-id','');
  $('#proCycle').val('');
  $('#proFdp').val('');
  $('.errorItem').attr('data-content','');
}

//用户信息修改
function openCusInfo(){
	$('#openCusInfo').off('click').on('click',function(){
		$('#showCusInfo').show();
		getScroll();
		cusClear();
		loadData(function(res){
			  $('#cusId').val(res.projectUser.pu_projectUserId);
			  $('#cusLinkman').val(res.projectUser.pu_linkman);
			  $('#cusTelephone').val(res.projectUser.pu_telephone);
		}, getContextPath() + '/project/task/edit/parameter/'+$("#currentTaskId").val()+"/"+$('#projectId').val()+"/pu",null);
	});
	$('#submitCus').off('click').on('click',function(){
		if(checkCusInfo()){
			$('#toCusForm').submit();
		}
		
	});
}


function cusClear(){
	$('#cusLinkman').val('');
	$('#cusTelephone').val('');
	$('.errorItem').attr('data-content','');
}

function checkCusInfo(){
    var cusLinkman = $('#cusLinkman').val();
    var cusTelephone = $('#cusTelephone').val();
    $('.errorItem').attr('data-content',''); 
	 if(cusLinkman == undefined || cusLinkman == "" || cusLinkman ==null ){
			$('#cusLinkmanError').attr('data-content','客户联系人未填写');
			return false;
		}
	 if(cusTelephone == undefined || cusTelephone == "" || cusTelephone ==null ){
			$('#cusTelephoneError').attr('data-content','客户联系人电话未填写');
			return false;
		}
	 
	 if (!checkMobile(cusTelephone)) {
			$('#cusTelephoneError').attr('data-content','电话未格式不正确');
			return false;
		}
	 return true;
}

//价格信息修改
function openPriceInfo(){
	$('#openPriceInfo').off('click').on('click',function(){
		$('#showPriceInfo').show();
		getScroll();
		priceClear();
		loadData(function(res){
			$('#priceId').val(res.projectFlow.pf_projectId)
			$('#est').val(res.projectFlow.pf_estimatedPrice);
			if(res.projectFlow.pf_projectBudget == null || res.projectFlow.pf_projectBudget == undefined || res.projectFlow.pf_projectBudget == ""){
				$('#pjsError').remove();
			}else{
				$('#pjs').val(res.projectFlow.pf_projectBudget);
			}
		}, getContextPath() + '/project/task/edit/parameter/'+$("#currentTaskId").val()+"/"+$('#projectId').val()+"/pf",null);
	});
	$('#sumbitPrice').off('click').on('click',function(){
		if(checkPrice()){
			$('#toPriceForm').submit();
		}
	});
}

function priceClear(){
	$('#est').val('');
	$('#pjs').val('');
	$('.errorItem').attr('data-content','');
}


function checkPrice(){
	var est = $('#est').val();
	var pjs = $('#pjs').val();
	 if(est == undefined || est == "" || est ==null ){
		 $('#estError').attr('data-content','预估价格未填写');
		 return false;
	 }
	 
	 var pjsName = $('#pjsError').hasClass('item'); 
	 if(pjsName){
		 if(pjs == undefined || pjs == "" || pjs ==null ){
	    	 $('#pjsError').attr('data-content','客户项目预算未填写');
			 return false;
		 }
	 }
     
     return true;
}

//供应商信息修改
function openProviderInfo(){
	$('#openProvider').off('click').on('click',function(){
		$('#showProvider').show();
		getScroll();
		clearProvi();
		loadData(function(res){
	          var scheme = res.project_team_scheme;
	          var produce = res.project_team_produce;
	          if(scheme == undefined || scheme == "" || scheme ==null ){
	  		      $('#isHideTop').remove();
	  		    }else{
	  		    	 for (var i = 0; i < scheme.length; i++) {
                           $('#scCusName').val(scheme[i].pt_teamName);
                           $('#scLink').val(scheme[i].pt_linkman);
                           $('#scTel').val(scheme[i].pt_telephone);
                           $('#scId').val(scheme[i].pt_projectTeamId);
					   }
	  		    }
	          
	          if(produce == undefined || produce == "" || produce ==null ){
	  		      $('#isHideBot').remove();
	  		    }else{
	  		    	 for (var i = 0; i < scheme.length; i++) {
                           $('#prCusName').val(produce[i].pt_teamName);
                           $('#prLink').val(produce[i].pt_linkman);
                           $('#prTel').val(produce[i].pt_telephone);
                           $('#prId').val(produce[i].pt_projectTeamId);
					   }
	  		    }
	          
			}, getContextPath() + '/project/task/edit/parameter/'+$("#currentTaskId").val()+"/"+$('#projectId').val()+"/pt",null);
	});
	
	$('#submitProvide').off('click').on('click',function(){		
		if(checkProvider()){
			$('#toProForm').submit();
		}
	});
}

function clearProvi(){
	$('.checkError').val('');
    $('.checkErrorP').val('');
    $('.errorItem').attr('data-content','');
}

function checkProvider(){
             	
    var error = $('.checkError');
    var errorP = $('.checkErrorP');
    $('.errorItem').attr('data-content','');
    for (var i = 0; i < error.length; i++) {
	    	 var word = $(error[i]).val();
	    	 if(word == undefined || word == "" || word ==null ){
	    		 $(error[i]).parent().attr('data-content','内容不能为空');
	 			return false;
	 		}
	   }
    for (var i = 0; i < errorP.length; i++) {
   	 var phone = $(errorP[i]).val();
   	 if(!checkMobile(phone)){
   		$(errorP[i]).parent().attr('data-content','电话格式不正确');
		return false;
		}
    }
    return true;
}

//表单验证
function checkForm(){
	$('#errorInfo').text('');
	var getCheckInfo = $('.checkInfo');
	var checkFlag = true;
	for (var i = 0; i < getCheckInfo.length; i++) {
		var check = $(getCheckInfo[i]).val();
               if(check == null || check == "" || check == undefined )	{
            	   checkFlag = false;
            	   initFormEven();
               }	
	}
	
	if(checkFlag){
		$('#toSubmitForm').prop("type","submit");
		$('.btnInput').off('click');
	}else{
		$('#errorInfo').text('请补充必填信息');
	}
}

function initFormEven(){
	$('.btnInput').off('click').on('click',function(){
		$('.btnInput').off('click');
		checkForm();
	});
	
	dataEven();
	autoInput();
	autoSelect();
	
}

function initFileUpload(){	
	$('.upFile').off('click').on('click',function(){
		 $('#upModel').show();
		 UploadSingleFile();
		 getFileType();
		 getScroll();
		 $('#errorType').attr('data-content','');
	});	
}

function getFileType(){
	loadData(function(res){    
		         var body = $('#orderType');
		         body.html('');
		    	 for (var i = 0; i < res.length; i++) {
		    		      var html = createOption(res[i].key,res[i].value); 
		    		      body.append(html);
				   }   
		    	 initSelect();
		}, getContextPath() + '/project/edit/resource/'+$("#currentTaskId").val()+"/"+$('#projectId').val(),null);
}

//文件上传
function UploadSingleFile(){
	upload_VideoFile && upload_VideoFile.destroy();
	var picker =$('#findFile'); 
	upload_VideoFile = WebUploader.create({
		auto:false,
		swf : '/resources/lib/webuploader/Uploader.swf',
		server : '/resource/addResource',
		pick : {
			id:picker,
			multiple :false//弹窗时不允许多选,
		},
		timeout:0,
		fileSingleSizeLimit : video_max_size,
	});
	
	upload_VideoFile.on('fileQueued', function(file) {
	    $('#getFileName').val(file.name);
	    
	    upload_VideoFile.option('formData', {
    		resourceName:$('#hasFile').text(),
    		taskId : $('#currentTaskId').val(),
    		resourceType:$('#hasFile').attr('data-id'),
    		flag : 1
    	});
	    
	    var file= $('#hasFile').text();
	    if(file == null || file == "" || file == undefined){
	    	 $('#errorType').attr('data-content','请选择类型');
	    }else{
	    	
	    	$('.singleProgress').show();
	 	    $('#upContent').hide();
	    	$('.upIng').show();
	  	    upload_VideoFile.upload();
	    }
	  
	});

	upload_VideoFile.on('uploadProgress',function(file, percentage) {
		$("#singleSetWidth").css('width', percentage * 100 + '%');
	});
	upload_VideoFile.on('uploadSuccess', function(file,response) {
		if(response){
			clearFile();
			$('.upIng').hide();
			$('.upSuccess').show();
			getFileInfo();
			initAllTalk();
		}		
	});
	
	/*$('#singleUpEv').off('click').on('click',function(){
		 upload_VideoFile.upload();	
	});*/
	
	$('#singleCacnle').off('click').on('click',function(){		
		clearFile();
	});
	
	$('#singleCacnleEven').off('click').on('click',function(){
		clearFile();
	});
	
}

function clearFile(){
	$('.cusModel').hide(); 
	 $('.upProgress').hide();
	 $('#upContent').show();
	 $("#singleSetWidth").css('width', 0);
	 $('#getFileName').val('');
	 $('#hasFile').text('');
	 $('#hasFile').attr('data-id','');
	 $('.upIng').hide();
	 $('.upSuccess').hide();
}


//动态上传
function UploadFile(){
	//upload_Video && upload_Video.destroy();
	var picker =$('#picker'); 
	upload_Video = WebUploader.create({
		auto:false,
		swf : '/resources/lib/webuploader/Uploader.swf',
		server : '/resource/addResource',
		pick : {
			id:picker,
			multiple :false//弹窗时不允许多选,
		},
		timeout:0,
		fileSingleSizeLimit : video_max_size,
	});
	
	upload_Video.option('formData', {
		resourceName:$('#file').val(),
		taskId : $('#taskId').val(),
		resourceType:$('#file').attr('data-name')
	});
	
	upload_Video.on('fileQueued', function(file) {
	    $('.uploadInput').val(file.name);
	    $('.longInput').val(file.name);
	    $('.proTitle').text(file.name);
	    $('.upProgress').show();
	    upload_Video.option('formData', {
    		resourceName:$('#file').attr('data-title'),
    		taskId : $('#currentTaskId').val(),
    		resourceType:$('#file').attr('data-name')
    	});
	    upload_Video.upload();
		$('.dynamic-form-table .item').hide();
	});
/*	upload_Video.on('fileQueued', function(file) {
		//跳转step2.添加信息
		_this.addProductMsg();
	});*/
	// 文件上传过程中创建进度条实时显示。
	upload_Video.on('uploadProgress',function(file, percentage) {
		$("#setWidth").css('width', percentage * 100 + '%');
		$('.upIng').show();
		$('.upSuccess').hide();
		$('.upError').hide();
		$('#btnInput').off('click');
	});
	upload_Video.on('uploadSuccess', function(file,response) {
		if(response){
			
			$('.upIng').hide();
			$('.upSuccess').show();
			$('.upError').hide();
			initFormEven();
		}else{
		
			$('.upIng').hide();
			$('.upSuccess').hide();
			$('.upError').show();
		}
	});
	upload_Video.on('error', function(type) {
		/* if (type=="Q_TYPE_DENIED"){
				$('#errorInfo').text('请上传mp4格式');
	        }else if(type=="F_EXCEED_SIZE"){
				$('#errorInfo').text(video_err_msg);
	        }*/
	});
	
	/*$("#uploadVideo").on('click', function() {
		upload_Video.option('formData', {
    		resourceName:$('#file').attr('data-title'),
    		taskId : $('#currentTaskId').val(),
    		resourceType:$('#file').attr('data-name')
    	});
		upload_Video.upload();
		$('#errorInfo').text('上传中...');
	});*/
}

//动态下拉框

function autoSelect(){
	
	$('.autoSelect').off('click').on('click',function(){
		 $(this).parent().find('ul').show();
	});
	$('.autoImg').off('click').on('click',function(){
		 $(this).parent().find('ul').show();
	});
	autoSelectUl();
}

function autoSelectUl(){
	
	$('.autoSelectUl li').off('click').on('click',function(){
		$('.autoSelectUl').hide();
		 var name = $(this).text();
		 var id = $(this).attr('data-id');
		 $(this).parent().parent().find('input').val(name);
		 $(this).parent().parent().find('.hideInput').val(id);
	});
	
}



//动态联动事件

//自动联动客户信息
function autoInput(){
	$('#pt_teamName').bind('input propertychange', function() {
		 $('#pt_teamId').val("");
		var theName = $(this).val();
		 findAutoInfo(theName);
		 $('.utoInfo').show();
		 if(theName == null || theName == ""){
			 $('.utoInfo').hide();
		 }
	});
}

function findAutoInfo(userName){
	loadData(function(res){
		var res = res;
		var body = $('.utoInfo');
		body.html('');
		if(res != null && res != undefined){
			for (var int = 0; int < res.length; int++) {
				   var html =createUserInfo(res[int].teamId,res[int].teamName,res[int].linkman,res[int].phoneNumber);
				   body.append(html);
			};
			autoLi();
		}
	}, getContextPath() + '/team/listByName/'+userName,null);
}

function autoLi(){
	
	$('.utoInfo li').off('click').on('click',function(){
		  $('.utoInfo').hide();
		  var name = $(this).text();
		  var id = $(this).attr('data-id');
		  var linkman = $(this).attr('data-linkman');
		  var phone = $(this).attr('data-phone');
		  $(this).parent().parent().find('input').val(name);
		  $('#pt_teamId').val(id);
		  $('#pt_linkman').val(linkman);
		  $('#pt_telephone').val(phone);
	});
	$('#pt_teamId').parent().hide();
}

function createUserInfo(id,name,linkman,phone){
	var html = '<li data-id="'+id+'"  data-linkman="'+ linkman +'" data-phone="'+ phone +'">'+name+'</li>';
	return html;
}


//加载动态表单
function addForm() {
	loadData(function(datas) {
		var trs = "";
		$.each(datas.taskFormData.formProperties, function() {
			var className = this.required === true ? "required" : "";
			this.value = this.value ? this.value : "";
			var isRe = this.required;
			trs += "<div class='item'>" + createFieldHtml(this, datas, className);
			trs += "</div>";
		});
		$('#setAutoInfo').html("<form class='dynamic-form' method='post'><div class='dynamic-form-table'></div></form>");
		var $form = $('.dynamic-form');
		$form.attr('action', '/project/task/complete/' + $('#currentTaskId').val());
		trs += '<div class="btnInput" id="btnInput"><input id="toSubmitForm" class="btn-c-r" type="button" value="提交"/></div>'
		
		// 添加table内容
		$('.dynamic-form-table').html(trs);
		initFormEven();
		var hasPicker = $('.picker');
		if(hasPicker !=null && hasPicker !="" && hasPicker !=undefined){
			UploadFile();
		}
	}, '/project/get-form/task/' + $('#currentTaskId').val() + '/' + $('#projectId').val(), null);
}

/**
 * 生成一个field的html代码
 */
function createFieldHtml(prop, obj, className) {
	return formFieldCreator[prop.type.name](prop, obj, className);
}

var formFieldCreator = {
'string': function(prop, datas, className) {
	if(prop.required){
		var result = "<div class='title'>" + prop.name + "<span> *</span></div>";
		var isCheck = "checkInfo";
	}else{
		var result = "<div class='title'>" + prop.name + "</div>";
		var isCheck = "noCheckInfo";
	}
	var isWhat = prop.id.split('_')[0];; 
	if (prop.writable === true) {
		if(prop.id == "pt_teamName"){
			result += "<input type='text' id='" + prop.id + "' name='" + prop.id + "' class='uploadInput "+isCheck+" " + className + "' value='" + prop.value + "' />";
			result += "<ul class='utoInfo'></ul>";
			return result;
		}
		
	     if(isWhat == 'schemeId'  || isWhat == 'superviseId')	{
	    	result += "<input readonly class='autoSelect' id='" + prop.id + "'  class='" + className + "'>";
	 		result += "<input type='hidden' class='hideInput' name='" + prop.id + "' >";
	 		result += "<img class='autoImg' src='/resources/images/flow/selectOrder.png'>";
	 		result += "<ul class='autoSelectUl'>";
	 		$.each(datas[prop.id], function(k, v) {
	 			result += "<li data-id='" + k + "'>" + v + "</li>";
	 		});
	 		result += "</ul>";
	 		return result;
	     }
		
		if(isWhat == "file"){
			result += "<input class='longInput checkInfo' readonly type='text' id='file' data-title='" + prop.name + "' data-name='" + prop.id + "'  name='" + prop.id + "' class='uploadInput "+isCheck+" " + className + "' value='" + prop.value + "' />";
			result += " <div id='picker' class='upload picker'>选择文件</div>";
		/*	result += " <div id='uploadVideo' class='uploadVideo'>上传</div>";*/
			return result;
		}
		result += "<input type='text' id='" + prop.id + "' name='" + prop.id + "' class='uploadInput "+isCheck+" " + className + "' value='" + prop.value + "' />";
	} else {
		result += "<input class='"+isCheck+"' value='" + prop.value + "' readonly/>";
	}
	return result;
},
'date': function(prop, datas, className) {
	if(prop.required){
		var result = "<div class='title'>" + prop.name + "<span> *</span></div>";
		var isCheck = "checkInfo";
	}else{
		var result = "<div class='title'>" + prop.name + "</div>";
		var isCheck = "noCheckInfo";
	}
	if (prop.writable === true) {
		result += "<input type='text' id='" + prop.id + "' name='" + prop.id + "' class='date "+isCheck+" " + className + "' value='" + prop.value + "'/>";
	} else {
		result += "<input class='"+isCheck+"' value='" + prop.value + "' readonly/>";
	}
	return result;
},
'long': function(prop, datas, className) {
	if(prop.required){
		var result = "<div class='title'>" + prop.name + "<span> *</span></div>";
		var isCheck = "checkInfo";
	}else{
		var result = "<div class='title'>" + prop.name + "</div>";
		var isCheck = "noCheckInfo";
	}
	if (prop.writable === true) {
		result += "<input type='text' id='" + prop.id + "' name='" + prop.id + "' class=' "+isCheck+" " + className + "' value='" + prop.value + "'/>";
	} else {
		result += "<input class='"+isCheck+"' value='" + prop.value + "' readonly/>";
	}
	return result;
},
'enum': function(prop, datas, className) {
	if(prop.required){
		var result = "<div class='title'>" + prop.name + "<span> *</span></div>";
		var isCheck = "checkInfo";
	}else{
		var result = "<div class='title'>" + prop.name + "</div>";
		var isCheck = "noCheckInfo";
	}
	if (prop.writable === true) {
		result += "<input readonly class='autoSelect' id='" + prop.id + "'  class='" + className + "'>";
		result += "<input type='hidden' class='hideInput' name='" + prop.id + "' >";
		result += "<img class='autoImg' src='/resources/images/flow/selectOrder.png'>";
		result += "<ul class='autoSelectUl'>";
		$.each(datas[prop.id], function(k, v) {
			result += "<li data-id='" + k + "'>" + v + "</li>";
		});
		result += "</ul>";
	} else {
		result += "<input class='"+isCheck+"' value='" + prop.value + "' readonly/>";
	}
	return result;
}
};


function initEvenInfo(){
	$('.closeModel').off('click').on('click',function(){
         $('.cusModel').hide();		
         $('#errorInfo').text('');
         $('.upProgress').hide();
         $('#setAutoInfo .item').show();
	});
	$('#myOrder').show();
}

function openInfoCard(){
	$('.controlOpen').off('click').on('click',function(){
		var nowItem = $(this);
            if(nowItem.hasClass('openItems')){
            	nowItem.removeClass('openItems');
            	nowItem.parent().parent().find('.getInfoItemContent').slideUp();
            	
            }else{
            	nowItem.addClass('openItems');
            	nowItem.parent().parent().find('.getInfoItemContent').slideDown();
            }	
            getHeight();
	});
	
	$('.openTalk').off('click').on('click',function(){
		var nowItem = $(this);
            if(nowItem.hasClass('openItem')){
            	nowItem.removeClass('openItem');
            	nowItem.parent().parent().parent().find('.infoContent').find('input').hide();
            	nowItem.parent().parent().parent().find('.upInfo').hide();
            }else{
            	nowItem.addClass('openItem');
            	nowItem.parent().parent().parent().find('.infoContent').find('input').show();
            	nowItem.parent().parent().parent().find('.upInfo').show();     
            }	
            getHeight();
	});
	
	$('.modelOpen').off('click').on('click',function(){
		var nowItem = $(this);
            if(nowItem.hasClass('openItem')){
            	nowItem.removeClass('openItem');
            	nowItem.parent().parent().parent().parent().find('.itemArea').find('input').slideUp();
            	nowItem.parent().parent().parent().parent().find('.backInfoTalk').hide();
            }else{
            	nowItem.addClass('openItem');
            	nowItem.parent().parent().parent().parent().find('.itemArea').find('input').slideDown();
             	nowItem.parent().parent().parent().parent().find('.backInfoTalk').show();
            }	
            getHeight();
	});
	
}

function flagEven(){
	$('.flag').off('click').on('click',function(){
		$('.flag').attr('src','/resources/images/flow/flag.png');
		$(this).attr('src','/resources/images/flow/flagRed.png');
	});
}

function dataEven(){
	$("#findTime").datepicker({
		language: 'zh',
		dateFormat:'yyyy-MM-dd'
     });
	$("#payTime").datepicker({
		language: 'zh',
		dateFormat:'yyyy-MM-dd'
     });
	$("#orderTime").datepicker({
		language: 'zh',
		dateFormat:'yyyy-MM-dd'
     });
	
	$(".date").datepicker({
		language: 'zh',
		dateFormat:'yyyy-MM-dd'
     });
	
}

//提示
function showWarn(){
	$('#showWarn').off('click').on('click',function(){
		$('#warnModel').show();
	});
}


//协同人清单
function helper(){
	$('#showHelper').off('click').on('click',function(){
		$('#helperModel').show();
	});
}


//客户信息修改
function showCusEdit(){
	$('#showCusEdit').off('click').on('click',function(){
		$('#cusInfoModel').show();
	});
}

//上传文件
function upModel(){
	$('#showUp').off('click').on('click',function(){
		$('#upModel').show();
	});
}

//报错
function showError(){
	$('#showError').off('click').on('click',function(){
		$('#errorModel').show();
	});
}
//完善客户信息
function finishCus(){
	$('#finishCus').off('click').on('click',function(){
		$('#cusModel').show();
	});
}


//初始化添加留言
function initAddTalk(){
	$('.upInfo #submitTalkInfo').off('click').on('click',function(){
		var projectId = $('#projectId').val();
		var taskName = $('#taskName').val();
		var talkInfo = $('#talkInfo').val();
		var taskId = $('#currentTaskId').val();
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
			taskId:taskId,
			projectId:projectId,
			taskName:taskName,
			content:talkInfo
		}));
		}
	});
}

//初始化 留言板前3条
function initTalk(){
	loadData(function(res){
		var res = res;
		if(res != null && res != undefined){
			for (var int = 0; int < res.length; int++) {
				   var html =createUserInfo();
				   body.append(html);
			};			
		}
	}, getContextPath() + '/message/getDefaultMsg'+$('#projectId').val(),null);
}

//全部留言信息
function initAllTalk(){
	loadData(function(res){
		var res = res;
		var body =$('.setAreaDiv');
		body.html('');
		if(res != null && res != undefined){
			for (var int = 0; int < res.length; int++) {
				   var html =createTalkInfo(res[int]);
				   body.append(html);
			}
			rePickTalk();
			openInfoCard();
			getHeight();
		}
	}, getContextPath() + '/message/getProjectMsg/'+$('#projectId').val(),null);
}
//留言回复
function rePickTalk(){
	
   $('.upInfo .toArea').off('click').on('click',function(){
	    var projectId = $(this).attr('data-id');
	    var taskName = $(this).attr('data-name');
	    var parentId = $(this).attr('data-parentId');
	    var content = $(this).parent().parent().find('.infoContent').find('input').val();
	    
	    if(content == null || content == "" || content == undefined){
	    	$(this).parent().parent().find('.errorSpan').text('回复不能为空');
	    }else{
	    
	    $('.upInfo .toArea').off('click');
			loadData(function(res){
				if(res.code == 200){
					  initAllTalk();
					  $('.upInfo .toArea').off('click');
				}else{
					  $('.upInfo .toArea').off('click');
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
			body +='<div><div>'+children[int].fromName+' 回复 :</div> <div>'+children[int].content+'</div><div>'+formatDate((children[int].createDate).replace("CST","GMT+0800"))+'</div></div>';
		}
	}
	if(res.fromUrl == null || res.fromUrl == ""){
		var  imgUrl = "/resources/images/flow/def.png";
	}else{
		var  imgUrl = getDfsHostName()+res.fromUrl;
	}
	var html = [
	    '<div class="areaItem">',
	    '   <div class="infoItem">',
		'	  <img src="'+imgUrl+'">',
		'       <div class="info">'+res.fromName+' : '+res.content+'</div>',
		'       <div class="time">',
		'       	<span>发布时间：'+formatDate((res.createDate).replace("CST","GMT+0800"))+'</span>',
		'     	    <div class="openTalk"></div>',
		'       </div>',
   		'   </div>',
		'   <div class="infoContent">',
		'     '+body+'',
		'      <input>',
		'   </div>',
		'<span class="errorSpan"></span>',
		'   <div class="upInfo">',
		'      <div class="btn-c-r toArea" data-id="'+res.projectId+'" data-name="'+res.taskName+'" data-parentId="'+res.projectMessageId+'">回复</div>',
		'   </div>',
		'</div>',

	].join('');
	return html;
}

//文件区域
function getFileInfo(){
	loadData(function(res){
		var res = res;
		var body =$('#projectFilm');
		body.html('');
		$('.noFile').show();
		$('.conMod').hide();
		if(res != null && res != undefined){
			$('.noFile').hide();
			$('.conMod').show();
			var newList = bulidFileList(res);
				for (var int = 0; int < newList.length; int++) {
					 var html =createFileInfo(newList[int]);
					 body.append(html);
				}
				getHeight();
		}
	}, getContextPath() + '/resource/list/'+$('#projectId').val(),null);	
}

function bulidFileList(arr) {
    var len = arr.length;
    for (var i = 0; i < len; i++) {
        for (var j = 0; j < len - 1 - i; j++) {
        	var fileOne = new Date((arr[j].createDate).replace("CST","GMT+0800"));
        	var fileTwo =  new Date((arr[j+1].createDate).replace("CST","GMT+0800"));
            if (fileOne < fileTwo) {        // 相邻元素两两对比
                var temp = arr[j+1];        // 元素交换
                arr[j+1] = arr[j];
                arr[j] = temp;
            }
        }
    }
    return arr;
}

//文件卡片
function createFileInfo(res){
	var name = res.resourceName;
	var fileName = name.lastIndexOf(".");
	var finalName = name.substring(fileName + 1);
	var src = '/resources/images/flow/';
	switch (finalName) {
		case 'doc' :
		case 'docx' :
			src += 'doc.png';
			break;
		case 'xls' :
		case 'xlsx' :
			src += 'xls.png';
			break;
		case 'ppt' :
		case 'pptx' :
			src += 'ppt.png';
			break;
		case 'pdf' :
			src += 'pdf.png';
			break;
		case 'txt' :
			src += 'txt.png';
			break;
		case 'avi' :
			src += 'avi.png';
			break;
		case 'esp' :
			src += 'esp.png';
			break;
		case 'jpg' :
			src += 'jpg.png';
			break;
		case 'mov' :
			src += 'mov.png';
			break;
		case 'mp3' :
			src += 'mp3.png';
			break;
		case 'mp4' :
			src += 'mp4.png';
			break;
		case 'png' :
			src += 'png.png';
			break;
		case 'rar' :
			src += 'rar.png';
			break;
		case 'wav' :
			src += 'wav.png';
			break;
		case 'zip' :
			src += 'zip.png';
			break;
		default :
			src += 'file.png';
			break;
	}
	var fileName = name.lastIndexOf(".");
	var checkName = name.substring(0,fileName);
	var html = [
		'<div class="filmItem">                                     ',
        '<img class="filmImg" src="'+src+'"> ',
        '<div class="filmName">'+checkName+'</div>                         ',
        '<div class="fileType"><div>'+res.resourceType+'</div></div>            ',
        '<div class="fileTypeName"><div>'+res.uploaderName+'</div></div>        ',
        '<div class="time"><div>'+formatDate((res.createDate).replace("CST","GMT+0800"))+'</div></div>        ',
        '<div class="icon">                                         ',
        '      <a href="/resource/getDFSFile/'+res.projectResourceId+'"><div class="download" ></div></a>                         ',
        '</div>                                                     ',
        '</div>                                                             ',
	].join('');
	return html;
}

function controlModel(){
	$('.conMod').off('click').on('click',function(){
		$('#controlModel').show();
		getScroll();
		getFileModelInfo();
	});
}

function getFileModelInfo(){
	loadData(function(res){
		var res = res;
		var body =$('#controlContent');
		body.html('');
		$('#errorSpan').text('');
		if(res != null && res != undefined){
			for(var key in res) { 
				var sethtml="";
				var resKey = res[key];
				if(resKey.length > 0){
					var head =createNoHead(key);
					sethtml += head;
					for (var int = 0; int < resKey.length; int++) {
						 var html =createNoInfo(res[key][int]);
						 sethtml +=html;
					}
					var end = "</div></div>";
					sethtml +=end;
					body.append(sethtml);
				}
             }
			fileCheckNo();
			openInfoCard();
		}else{
			$('#errorSpan').text('暂无文件');
		}
	}, getContextPath() + '/resource/version/'+$('#projectId').val(),null);	
}

//版本确认

function fileCheckNo(){
	
	$('.icon .flag').off('click').on('click',function(){
		var id = $(this).attr('data-id');
		loadData(function(res){
			if(res){
				getFileInfo();
				getFileModelInfo();
			}
		}, getContextPath() + '/resource/setValid/'+id,null);			
	});
	
}

//版本投头
function createNoHead(name){
	var html = [
		'<div class="item">',
        '<div class="itemTop">',
        '     <div class="controlOpen openItems"></div>',
        '     <div class="title">'+name+'</div>',
        '</div>',
        '<div class="getInfoItemContent">',
	].join('');
	return html;
}

//版本卡片
function createNoInfo(res){
	
	var name = res.resourceName;
	var fileName = name.lastIndexOf(".");
	var checkName = name.substring(0,fileName);
	var imgUrl = "/resources/images/flow/flag.png";
	if(res.flag == 1){
		imgUrl = "/resources/images/flow/flagRed.png";
	}
	var html = [
		 ' <div class="InfoItem">                                                                ',
		 '        <div class="fileName">'+checkName+'</div>                                             ',
		 '        <div class="name">'+res.uploaderName+'</div>                                                 ',
		 '        <div class="time">上传于:'+formatDate((res.createDate).replace("CST","GMT+0800"))+'</div>                                   ',
		 '        <div class="icon">                                                             ',
		 '                    <img class="flag" data-id="'+res.projectResourceId+'" src="'+imgUrl+'">           ',
		 '                    <a href="/resource/getDFSFile/'+res.projectResourceId+'"><div class="download" src="/resources/images/flow/download.png"></div></a>  ',
		 '        </div>                                                                         ',
	     '  </div>                     ',
	].join('');
	return html;
}

function createOption(value,text,price){
	
	var html = '<li data-price="'+ price +'" data-id="'+ value +'">'+text+'</li>';
	return html;
}


