var InterValObj; // timer变量，控制时间  
var count = 120; // 间隔函数，1秒执行  
var curCount; // 当前剩余秒数 
var upload_Video;
var upload_VideoFile;
var video_max_size = 200*1024*1024; // 200MB
var video_err_msg = '视频大小超出200M上限,请重新上传!';
var taskIdname ="";
var noNeed = true;
$().ready(function() {
	document.domain = getUrl();
	
	// 加载动态表单
	    var href = window.location.href;
	    var paramLength=href.indexOf('&');
	    if(paramLength<0){
	    	paramLength=href.length;
	    }
	    var state = href.substr(href.lastIndexOf("?")+1,paramLength-href.lastIndexOf("?")-1);
	    if(state.trim() == "status=finished"){
	    	$('#imgWord').text('完成');
			$('#lastTimeWord').hide();
	    	$('#taskStage').val('交付阶段');
	    	getStageInfo($('#taskStage').val());
	    	$('#imgFlow').addClass('imgFinish');
	    }else{
	    	var task = $('#taskStage').val();
	    	getStageInfo(task);
	    	$('#lastTimeWord').show();
	    }
	pageInit();
	initDaibanTime();
	initResouces();
	initStateBtn();
	provNewInit();
	loadProvCard();
});

function getScroll(){
	 $(window.parent.parent.parent.document).find('html').scrollTop(0);
	$(window.parent.parent.parent.document).find('body').scrollTop(0);
}

function initStateBtn(){
	 $('#isBack').off('click').on('click',function(){
		 $('#isReView').show();
	 });
	 
	 $('#isPause').off('click').on('click',function(){
		 $('#isPauseModel').show();
		 $('#puaseReasonError').attr('data-content','');
	 });
	 
	 $('#checkpause').off('click').on('click',function(){
		 $('#puaseReasonError').attr('data-content','');
		 var checkVal = $('#puaseReason').val();
		 if(checkVal !="" && checkVal !=null && checkVal!=undefined){
			   $('#toProjectpause').submit();
		   }else{
			   $('#puaseReasonError').attr('data-content','请填写暂停原因');
		   }
	 });
	 
	 $('#isCancle').off('click').on('click',function(){
		 $('#isCancleModel').show();
		 $('#cancleReasonError').attr('data-content','');
	 });
	 
	 $('#checkcancle').off('click').on('click',function(){
		 $('#cancleReasonError').attr('data-content','');
		 var checkVal = $('#cancleReason').val();
		 if(checkVal !="" && checkVal !=null && checkVal!=undefined){
			   $('#toProjectcancle').submit();
		   }else{
			   $('#cancleReasonError').attr('data-content','请填写取消原因');
		   }
	 });

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
	var paramLength=href.indexOf('&');
    if(paramLength<0){
    	paramLength=href.length;
    }
    var state = href.substr(href.lastIndexOf("?")+1,paramLength-href.lastIndexOf("?")-1);
    
    if(state.trim() == "pause"){
    	$('#imgFlow').addClass('imgRed');
		$('#imgWord').text('暂停');
		$('#imgWord').attr('style','color:#fe5453');
		$('#lastTimeWord').text("");
		$('.update').hide();
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
    	$('.proControl').hide();
    	$('.update').hide();
    	$('.conMod').hide();
    	$('.upFile').hide();
    	$(window.parent.parent.parent.parent.parent.document).find('#productList li').removeClass('checkLi');
    	$(window.parent.parent.parent.parent.parent.document).find('#finish').addClass('checkLi');
    }
    if(state.trim() == "cancel"){
    	$('#imgWord').text('取消');
    	$('.proControl').hide();
    	$('.update').hide();
    }
    
}

function stageTalkEven(){
	$('.findTalk').off('click').on('click',function(){
		taskIdname= $(this).attr('data-id');
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
		}, getContextPath() + '/project/task-detail/'+taskIdname,null);
		
	});
}

function loadStageInfoEven(){
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
		taskId:taskIdname
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
			body +='<div class="itemPos"><div>'+children[int].fromName+' 回复 :'+children[int].content+'</div><div>'+formatDate((children[int].createDate).replace("CST","GMT+0800"))+'</div></div>';
		}
	}
	if(res.fromUrl == null || res.fromUrl == "" ){
		var  imgUrl = "/resources/images/flow/def.png";
	}else{
		var  imgUrl = getDfsHostName()+res.fromUrl;
	}
	var isSys;
	if(res.messageType == 1){
		isSys = 	'       <div>'+res.fromName+' : '+res.content+'</div>';
	}else{
		isSys = 	'       <div class="sys">【系统自动】'+res.fromName+' : '+res.content+'</div>';
	}

	var html = [
				'<div class="infoItem">',
				'    <div  class="itemTop">',
				'          <img class="logo" src="'+imgUrl+'">                                                                                ',
				'           <ul>                                                                                                    ',
				'              <li>'+isSys+'<div>'+formatDate((res.createDate).replace("CST","GMT+0800"))+'</div><img class="modelOpen " src="/resources/images/flow/areaMore.png"></li>                                                             ',
				'           </ul>                                                                                                   ',
				'    </div>                                                                                                         ',
				'    <div class="itemArea">                                                                                         ',
				'              '+body+'                                       ',
				'     <div class="reviewItem">',
				'      <input>',
				'    <div class="backInfoTalk btn-c-r"  data-parentId="'+res.projectMessageId+'"  data-name="'+res.taskName+'" data-projectId="'+res.projectId+'">回复</div>',
				'     </div>',
				'    </div> ',
				'<div class="errorSpan"></div>',
				'</div>                                                                                                       '
			].join('');
			return html;
}

function infoAddReplyEven(){
	
	 $('.backInfoTalk').off('click').on('click',function(){
		    var projectId = $(this).attr('data-projectId');
		    var name = $(this).attr('data-name');
		    var parentId = $(this).attr('data-parentId');
		    var content = $(this).parent().find('input').val();
		    
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
	var paramLength=href.indexOf('&');
    if(paramLength<0){
    	paramLength=href.length;
    }
    var state = href.substr(href.lastIndexOf("?")+1,paramLength-href.lastIndexOf("?")-1);
    
    if(state.trim() != "task"){
    	$('#daiban').hide();
    }
    
    if(state.trim() == "task" ||state.trim() == "doing"){
    	$('#isBack').hide();
    }
    
    if(state.trim() == "pause"){
    	$('#isPause').hide();
    	$('#isCancle').hide();
    	$('#isCancle').hide();
    	
    }
    
    if(state.trim() == "status=finished"){
    		 $('#isBack').hide();
    		 $('#isPause').hide();
    		 $('#isCancle').hide();
    		 $('.addPro').hide();
    	     $('.delPro').hide();
    }
    if(state.trim() == "cancel"){
		 $('.addPro').hide();
	     $('.delPro').hide();
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
			  removItemProject(res);
		}, getContextPath() + '/project/task/edit/parameter/'+$("#currentTaskId").val()+"/"+$('#projectId').val()+"/pf",null);
	});
	
	$('#submitProject').off('click').on('click',function(){
		if(checkProviderInfo()){
			$('#toProjectForm').submit();
		}
	});
}

function removItemProject(res){
	var projectName = res.projectFlow.pf_projectName;
	var Grade = res.projectFlow.pf_projectGrade;
	var pSource = res.projectFlow.pf_projectSource;
	var proCycle = res.projectFlow.pf_projectCycle;
	var proFdp = res.projectFlow.pf_projectSource;
	var projectDes = res.projectFlow.pf_projectDescription;
	
	
	if(projectName == undefined || projectName == "" || projectName == null){
		$('#proNameError').remove();
	}
	
	if(Grade == undefined || Grade == "" || Grade == null){
		$('#pf_projectGradeError').remove();
	}
	if(pSource == undefined || pSource == "" || pSource == null){
		$('#pf_ResourInputError').remove();
	}
	if(proCycle == undefined || proCycle == "" || proCycle == null){
		$('#proCycleError').remove();
	}
	if(proFdp == undefined || proFdp == "" || proFdp == null){
		$('#proFdpError').remove();
	}
	if(projectDesError == undefined || projectDesError == "" || projectDesError == null){
		$('#proFdpError').remove();
	}
}

function checkProviderInfo(){
           var proName = $('#proName').val();
           var pf_projectGrade = $('#pf_projectGrade').attr('data-id');
           var pf_Resour = $('#pf_Resour').attr('data-id');
           var proCycle = $('#proCycle').val();
           var proFdp = $('#proFdp').val();
        
       	if($('#proNameError').hasClass('errorItem')){
	        if(proName == undefined || proName == "" || proName ==null ){
	       		$('#proNameError').attr('data-content','项目名称未填写未填写');
	       		return false;
	       	}
       	}
       	
     	if($('#pf_projectGradeError').hasClass('errorItem')){
	        if(pf_projectGrade == undefined || pf_projectGrade == "" || pf_projectGrade ==null ){
	       		$('#pf_projectGradeError').attr('data-content','项目评级未填写');
	       		
	       		return false;
	       	}
     	}
     	
    	if($('#pf_ResourInputError').hasClass('errorItem')){
    		 $('#pf_projectGradeInput').val(pf_projectGrade);
    	        if(pf_Resour == undefined || pf_Resour == "" || pf_Resour ==null ){
    	       		$('#pf_ResourInputError').attr('data-content','项目来源未填写');
    	       		return false;
    	       	}
    	        $('#pf_ResourInput').val(pf_Resour);
     	}
    	if($('#proCycleError').hasClass('errorItem')){
    		 if(proCycle == undefined || proCycle == "" || proCycle ==null ){
    	       		$('#proCycleError').attr('data-content','项目周期未填写');
    	       		return false;
    	       	}
     	}
    	if($('#proFdpError').hasClass('errorItemArea')){
            if(proFdp == undefined || proFdp == "" || proFdp ==null ){
           		$('#proFdpError').attr('data-content','对标影片地址未填写');
           		return false;
           	}
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
			  $('#cusEmail').val(res.projectUser.pu_email);
			  removItemCus(res);
		}, getContextPath() + '/project/task/edit/parameter/'+$("#currentTaskId").val()+"/"+$('#projectId').val()+"/pu",null);
	});
	$('#submitCus').off('click').on('click',function(){
		if(checkCusInfo()){
			$('#toCusForm').submit();
		}
	});
}

function removItemCus(res){
	var cusLinkman = res.projectUser.pu_linkman;
	var cusTelephone = res.projectUser.pu_telephone;
	var cusEmail = res.projectUser.pu_email;

		if(cusLinkman == undefined || cusLinkman == "" || cusLinkman == null){
			$('#cusLinkmanError').remove();
		}

		if(cusTelephone == undefined || cusTelephone == "" || cusTelephone == null){
			$('#cusTelephoneError').remove();
		}
 	
		if(cusEmail == undefined || cusEmail == "" || cusEmail == null){
			$('#cusEmailError').remove();
		}
}


function cusClear(){
	$('#cusLinkman').val('');
	$('#cusTelephone').val('');
	$('#cusEmail').val('');
	$('.errorItem').attr('data-content','');
}

function checkCusInfo(){
    var cusLinkman = $('#cusLinkman').val();
    var cusTelephone = $('#cusTelephone').val();
    var cusEmail = $('#cusEmail').val();
    $('.errorItem').attr('data-content',''); 
    
    
	if($('#cusLinkmanError').hasClass('errorItem')){
		 if(cusLinkman == undefined || cusLinkman == "" || cusLinkman ==null ){
				$('#cusLinkmanError').attr('data-content','客户联系人未填写');
				return false;
			}
	}
	if($('#cusTelephoneError').hasClass('errorItem')){
		 if(cusTelephone == undefined || cusTelephone == "" || cusTelephone ==null ){
				$('#cusTelephoneError').attr('data-content','客户联系人电话未填写');
				return false;
			}
		 
		 if (!checkMobile(cusTelephone)) {
				$('#cusTelephoneError').attr('data-content','电话格式不正确');
				return false;
			}
		}
	if($('#cusEmailError').hasClass('errorItem')){
		 if (cusEmail == undefined || cusEmail == "" || cusEmail ==null) {
				$('#cusEmailError').attr('data-content','邮箱未填写');
				return false;
			} 
		 if (!checkEmail(cusEmail)) {
				$('#cusEmailError').attr('data-content','邮箱格式不正确');
				return false;
			}
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
			$('#priceId').val(res.projectFlow.pf_projectId);
			$('#est').val(res.projectFlow.pf_estimatedPrice);
			if(res.projectFlow.pf_projectBudget == null || res.projectFlow.pf_projectBudget == undefined || res.projectFlow.pf_projectBudget == ""){
				$('#pjsError').remove();
			}else{
				$('#pjs').val(res.projectFlow.pf_projectBudget);
			}
			removPriceCus(res);
		}, getContextPath() + '/project/task/edit/parameter/'+$("#currentTaskId").val()+"/"+$('#projectId').val()+"/pf",null);
	});
	$('#sumbitPrice').off('click').on('click',function(){
		if(checkPrice()){
			$('#toPriceForm').submit();
		}
	});
}

function removPriceCus(res){
	var est = res.projectFlow.pf_estimatedPrice;
	var pjs = res.projectFlow.pf_projectBudget;
		if(est == undefined || est == "" || est == null){
			$('#estError').remove();
		}
		if(pjs == undefined || pjs == "" || pjs == null){
			$('#pjsError').remove();
		}
}

function priceClear(){
	$('#est').val('');
	$('#pjs').val('');
	$('.errorItem').attr('data-content','');
}


function checkPrice(){
	var est = $('#est').val();
	var pjs = $('#pjs').val();
	
	
	if($('#estError').hasClass('errorItem')){
		 if(est == undefined || est == "" || est ==null ){
			 $('#estError').attr('data-content','预估价格未填写');
			 return false;
		 }
	}
	
	if($('#pjsError').hasClass('errorItem')){
			 var pjsName = $('#pjsError').hasClass('item'); 
			 if(pjsName){
				 if(pjs == undefined || pjs == "" || pjs ==null ){
			    	 $('#pjsError').attr('data-content','客户项目预算未填写');
					 return false;
				 }
			 }
		}

     return true;
}

//供应商信息修改
function openProviderInfo(){
	$('#openProvider').off('click').on('click',function(){
		$('#showProvider').show();
		 $(window.parent.parent.parent.document).find('html').scrollTop(0);
		$(window.parent.parent.parent.document).find('body').scrollTop(0);
		getScroll();
		clearProvi();
		autoTeamInput();
		autoTeamMakeInput();
		loadData(function(res){
	          var scheme = res.project_team_scheme;
	          var produce = res.project_team_produce;
	          if(scheme == undefined || scheme == "" || scheme ==null ){
	  		      $('#isHideTop').remove();
	  		    }else{
	  		    	 $('.setHideTop').html('');
	  		    	 for (var i = 0; i < scheme.length; i++) {
	  		    		     var pt_teamName = scheme[i].pt_teamName;
		  		    		 var pt_linkman = scheme[i].pt_linkman;
		  		    		 var pt_telephone = scheme[i].pt_telephone;
		  		    		 var pt_email = scheme[i].pt_email;
                             var scId = scheme[i].pt_projectTeamId;
                             var teamId = scheme[i].pt_teamId;
                        
                        var body = $('.setHideTop');
		                        body.append('<input type="hidden" name="pt_projectTeamId" value="'+scId+'">');
		                        body.append('<input type="hidden" name="pt_projectTeamId" value="'+teamId+'">'); 
	                        	body.append(createUpdateCard('供应商团队',pt_teamName,'pt_teamName','readonly',1));
	                    		body.append(createUpdateCard('供应商联系人',pt_linkman,'pt_linkman','',1));
	                    		body.append(createUpdateCard('供应商联系电话',pt_telephone,'pt_telephone','',0));
	                    		body.append(createUpdateCard('供应商邮箱',pt_email,'pt_email','',1));
	                    	var html = [
	            				'<div class="itemTime itemLong">',
	            					'<div class="title">修改原因</div>',
	            					'<textarea></textarea>',
	            				'</div>'
	            			].join('');
	                    	body.append(html);
	                    	body.append('<br/>');
					  }
	  		    }
	          
	          if(produce == undefined || produce == "" || produce ==null ){
	  		      $('#isHideBot').remove();
	  		    }else{
	  		    	$('.setHideBot').html('');
	  		    	 for (var i = 0; i < produce.length; i++) {
	  		    		 var pt_teamName = produce[i].pt_teamName;
	  		    		 var pt_linkman = produce[i].pt_linkman;
	  		    		 var pt_telephone = produce[i].pt_telephone;
	  		    		 var scId = produce[i].pt_projectTeamId;
                         var teamId = produce[i].pt_teamId;
                         var pt_email = scheme[i].pt_email;
                         
		                         var body = $('.setHideBot');
		                         body.append('<input type="hidden" name="pt_projectTeamId" value="'+scId+'">');
		                         body.append('<input type="hidden" name="pt_projectTeamId" value="'+teamId+'">'); 
	                        	body.append(createUpdateCard('供应商团队',pt_teamName,'pt_teamName','readonly',1));
	                        	body.append(createUpdateCard('供应商联系人',pt_linkman,'pt_linkman','',1));
	                       		body.append(createUpdateCard('供应商联系电话',pt_telephone,'pt_telephone','',0));
	                    		body.append(createUpdateCard('供应商邮箱',pt_email,'pt_email','',1));
	                       	var html = [
	            				'<div class="itemTime itemLong">',
	            					'<div class="title">修改原因</div>',
	            					'<textarea></textarea>',
	            				'</div>'
	            			].join('');
	                       	body.append(html);
	                       	body.append('<br/>');
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

function createUpdateCard(name,value,setName,read,phone){	
	var setPhone = "checkErrorP"
      if(phone == 1){
    	  setPhone = "checkError"
      }
				var html = [
				'<div class="itemTime errorItem">',
					'<div class="title">'+name+'</div>',
					'<input '+read+' class="'+setPhone+'" value="'+value+'" name="'+setName+'">',
				'</div>'
				].join('');
	return html;
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
var checkClick = true;
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
	
	var getCheckInfoUrl = $('.isUrl');
	for (var i = 0; i < getCheckInfoUrl.length; i++) {
		var check = $(getCheckInfoUrl[i]).val();
               if(!IsUrl(check)){
            	   checkFlag = false;
            	   $('#errorInfo').text('填写格式错误');
            	   initFormEven();
               }	
	}
	
	if(checkFlag && checkClick){
		checkClick = false; 
		$('.dynamic-form').submit();
	}else{
		$('#errorInfo').text('请补充必填信息');
		initFormEven();
	}
}

function IsUrl(str){   
	var Url=str;
	var Expression=/http(s)?:\/\/([\w-]+\.)+[\w-]+(\/[\w- .\/?%&=]*)?/;
	var objExp=new RegExp(Expression);
	if(objExp.test(Url)==true){
	return true;
	}else{
	return false;
	}	 
}

function initFormEven(){
	$('#toSubmitForm').off('click').on('click',function(){
		$('#toSubmitForm').off('click');
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
		$('.dynamic-form-table .itemCard').hide();
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
		$('#toSubmitForm').off('click');
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
	$('#pt_teamId').parent().hide();
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
			trs += "<div class='itemCard'>" + createFieldHtml(this, datas, className);
			trs += "</div>";
		//	trs +=  createFieldHtml(this, datas, className);
		});
		$('#setAutoInfo').html("<form class='dynamic-form' method='post'><div class='dynamic-form-table'></div></form>");
		var $form = $('.dynamic-form');
		$form.attr('action', '/project/task/complete/' + $('#currentTaskId').val());
		// 添加table内容
		$('.dynamic-form-table').html(trs);
		
		var hasPicker = $('.picker');
		if(hasPicker !=null && hasPicker !="" && hasPicker !=undefined){
			UploadFile();
		}
		getWatermarkUrl();
		getScheme();
		getProduce();
		getLoadTeamFinanceInfo();
		getLoadProduceTeamFinanceInfo();
		if(noNeed){
			var setbtn = '<div class="btnInput" id="btnInput"><input id="toSubmitForm" class="btn-c-r" type="button" value="提交"/></div>';
			$('.dynamic-form-table').append(setbtn);
			initFormEven();
		}
	}, '/project/get-form/task/' + $('#currentTaskId').val() + '/' + $('#projectId').val(), null);
}

/**
 * 生成一个field的html代码
 */
function createFieldHtml(prop, obj, className) {
	return formFieldCreator[prop.type.name](prop, obj, className);
}

function getWatermarkUrl(){
	if($('div').hasClass('watermarkUrl')){
		loadData(function(res){
			$('#info_watermarkUrl').text(res.sampleUrl);
			$('#info_watermarkUrl').attr('href',res.sampleUrl);
			if(res.samplePassword == '' || res.samplePassword == null || res.samplePassword == undefined ){
				$('#info_watermarkPass').val('无');
			}else{
				$('#info_watermarkPass').val(res.samplePassword);
			}
			
		}, getContextPath() + '/project/getFlowSample/' + $('#projectId').val(),null);
	}
}

function getScheme(){
	if($('input').hasClass('info_scheme_teamName')){
		loadData(function(res){
			$('.info_scheme_teamName').val(res.teamName);
			$('.info_scheme_linkman').val(res.linkman);
			$('.info_scheme_telephone').val(res.telephone);
			$('.info_scheme_budget').val(res.budget);
			$('.info_scheme_planContent').val(res.planContent);
			$('.info_scheme_planTime').val(res.planTime);
			$('.info_scheme_accessMan').val(res.accessMan);
			$('.info_scheme_accessManTelephone').val(res.accessManTelephone);
		}, getContextPath() + '/project/getSchemeTeamInfo/' + $('#projectId').val(),null);
	}
}

function getProduce(){
	if($('input').hasClass('info_produce_teamName')){
		loadData(function(res){
			$('.info_produce_teamName').val(res.teamName);
			$('.info_produce_linkman').val(res.linkman);
			$('.info_produce_telephone').val(res.telephone);
			$('.info_produce_budget').val(res.budget);
			$('.info_produce_makeContent').val(res.makeContent);
			$('.info_produce_makeTime').val(res.makeTime);
		}, getContextPath() + '/project/getProduceTeamInfo/' + $('#projectId').val(),null);
	}
}


function getLoadTeamFinanceInfo(){
	if($('.missinInfo').text()=='【供应商管家】填写制作供应商结算信息'){
		noNeed = false;
		loadData(function(res){
			if(res != null && res != undefined && res != ''){
				var body = $('.dynamic-form-table');
				for (var int = 0; int < res.length; int++) {
					   body.append('<br/>');
					   body.append(getItemInfo('供应商名称','addpt_teamName',res[int].addpt_teamName));
					   body.append(getItemInfo('供应商价格','addpt_actualPrice',''));
					   body.append(getItemInfo('发票抬头','addpt_invoiceHead',''));
					   body.append(getItemInfo('','addpt_projectTeamId',res[int].addpt_projectTeamId));
				};	
				var setbtn = '<div class="btnInput" id="btnInput"><input id="toSubmitForm" class="btn-c-r" type="button" value="提交"/></div>';
				$('.dynamic-form-table').append(setbtn);
				InitVoiceHead();
			}
		}, getContextPath() + '/project/loadTeamFinanceInfo/' + $('#projectId').val()+'/'+ $('#currentTaskId').val(),null);
	}
}
function InitVoiceHead(){
	var autoSelectUlNum = $('.autoSelectUl li');
	var body =$('.setAutoSelectUl');
    for (var i = 0; i < autoSelectUlNum.length; i++) {
    	body.append('<li data-id='+$(autoSelectUlNum[i]).text()+'>'+$(autoSelectUlNum[i]).text()+'</li>')
	}
    initFormEven();
}

function getLoadProduceTeamFinanceInfo(){
	if($('.missinInfo').text()=='【财务】填写付款信息'){
		noNeed = false;
		loadData(function(res){
	       if(res != null && res != undefined && res != ''){
				var body = $('.dynamic-form-table');
				for (var int = 0; int < res.length; int++) {
					   body.append('<br/>');
					   body.append(getItemInfo('供应商名称','teamName',res[int].addft_teamName));
					   body.append(getItemInfo('交易流水号','addft_billNo',''));
					   body.append(getItemInfo('付款时间','addft_payTime',''));
					   body.append(getItemInfo('交易金额','addft_payPrice',''));
					   body.append(getItemInfo('描述','addft_description',''));
					   body.append(getItemInfo('','addft_projectId',$('#projectId').val()));
				};			
				dataEven();
				var setbtn = '<div class="btnInput" id="btnInput"><input id="toSubmitForm" class="btn-c-r" type="button" value="提交"/></div>';
				$('.dynamic-form-table').append(setbtn);
				initFormEven();
			}
		}, getContextPath() + '/project/loadProduceTeamFinanceInfo/' + $('#projectId').val()+'/'+ $('#currentTaskId').val(),null);
	}
}

function getItemInfo(name,dp,value){
	
	if(dp == 'addpt_projectTeamId' || dp =='addft_projectId'){
	var html = [
		     '<input type="hidden" id="'+dp+'" name="'+dp+'" class="required '+dp+'" value='+value+'>',
	].join('');
	}else{
	var ul ="";	
	if(dp == "addpt_invoiceHead"){
		var html = [
			'<div class="itemCard"><div class="title">发票抬头<span> *</span></div>',
			    '<input readonly="" class="autoSelect checkInfo" id="'+dp+'">',
			    '<input type="hidden" class="hideInput" name="pt_invoiceHead">',
			    '<img class="autoImg" src="/resources/images/flow/selectOrder.png">',
			    '<ul class="autoSelectUl setAutoSelectUl"></ul>',
			'</div>',
			].join('');
	}else{	
		    var isRead ='';
		    var isCheck = 'checkInfo';
		    var isDate = '';
		    if(name == '供应商名称') {
		    	isRead = "readonly";
		    	isCheck = '';
		    }
		    if(name == '付款时间') {
		    	isDate ='date';
		    }
			var html = [
				     '<div class="itemCard">',
				     '     <div class="title">'+name+'<span>*</span></div>',
				     '     <input '+isRead+' type="text" id="'+dp+'" name="'+dp+'" class="'+isDate+' '+isCheck+' required '+dp+'" value='+value+'>',
				     '     '+ul+'',
				     '</div>',
			].join('');
		}
	}
	return html;
	
}

var formFieldCreator = {
'string': function(prop, datas, className) {
	var addClass ="";
	if(prop.id =="pt_teamId"){
		addClass = "hide";
	}
	
	// --> filter start with pt_ value by jack at 20170926 begin
	if(prop.id.indexOf('pt_') > -1){
		prop.value = '';
	}
	
	if(prop.id.indexOf('dl_') > -1){
		prop.value = '';
	}
	
	if(prop.id.indexOf('file_') >-1){
		proValue = '';
	}
	
	
	if(prop.required){	
		var result = "<div id='getInfoTitle' class=' title' "+addClass+"'>" + prop.name + "<span> *</span></div>";
		var isCheck = "checkInfo";
	}else{
		var result = "<div class='title' "+addClass+"'>" + prop.name + "</div>";
		var isCheck = "noCheckInfo";
	}
	
	if(prop.id == 'info_watermarkUrl'){
		result += "<input type='hidden' class='' value='" + prop.value + "' readonly placeholder='" + prop.value + "' name='" + prop.id + "'/>";
		result += "<div class='watermarkUrl' style='width:100% !important'><a id='info_watermarkUrl' href='"+ prop.value + "' target='_blacnk'>" + prop.value + "</a></div>";
		return result;
	}
	
	var isWhat = prop.id.split('_')[0];
	var str = prop.id;
	var isRead = str.indexOf('info');
		
	if (prop.writable === true) {
		if(isRead == 0){
			result += "<input id='" + prop.id + "' class='" + prop.id + "' value='" + prop.value + "' readonly placeholder='" + prop.value + "' name='" + prop.id + "'/>";
			return result;
		}
		if(prop.id == "pt_teamName"){
			result += "<input type='text' id='" + prop.id + "' name='" + prop.id + "' class='uploadInput '"+isCheck+"' '" + className + "'  value='" + prop.value + "' />";
			result += "<ul class='utoInfo'></ul>";
			return result;
		}
		
		if(prop.id =="pt_teamId"){
			result += "<input class='hide' type='text' id='" + prop.id + "' name='" + prop.id + "' class='uploadInput '"+isCheck+"' '" + className + "'  value='" + prop.value + "' />";
			return result;
		}
		
		if(prop.id.indexOf("Url")>-1){
			result += "<input class='isUrl' value='" + prop.value + "' name='" + prop.id + "'  />";
			return result;
		}
		
	     if(isWhat == 'schemeId'  || isWhat == 'superviseId' || isWhat == 'teamProviderId')	{
	    	result += "<input readonly class='autoSelect checkInfo' id='" + prop.id + "'  class='" + className + "'>";
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
			result += "<input class='longInput checkInfo' readonly type='text' id='file' data-title='" + prop.name + "' data-name='" + prop.id + "'  name='" + prop.id + "' class='uploadInput '"+isCheck+"' '" + className + "' '      />";
			result += " <div id='picker' class='upload picker '>上传</div>";
		/*	result += " <div id='uploadVideo' class='uploadVideo'>上传</div>";*/
			return result;
		}
		result += "<input id='" + prop.id + "' type='text' id='" + prop.id + "' name='" + prop.id + "' class='uploadInput "+isCheck+" " + className + " " + prop.id + "'  value=' "+ prop.value +"'   />";
	} else {
		result += "<input id='" + prop.id + "' class='" + prop.id + "' value='" + prop.value + "' readonly placeholder='" + prop.value + "' name='" + prop.id + "'/>";
	}
	return result;
},
'date': function(prop, datas, className) {
	
	if(prop.id.indexOf('dl_') > -1){
		prop.value = '';
	}
	
	if(prop.required){
		var result = "<div id='getInfoTitle' class='title'>" + prop.name + "<span> *</span></div>";
		var isCheck = "checkInfo";
	}else{
		var result = "<div class='title'>" + prop.name + "</div>";
		var isCheck = "noCheckInfo";
	}
	
	var isRead = prop.id.indexOf('info');
	
	if (prop.writable === true && isRead != 0) {
		result += "<input type='text' id='" + prop.id + "' name='" + prop.id + "' class='date "+isCheck+" " + className + " '" + prop.id + "'' value='" + prop.value + "'/>";
	} else {
		result += "<input name='" + prop.id + "' class='" + prop.id + "' value='" + prop.value + "' readonly placeholder='" + prop.value + "' />";
	}
	return result;
},
'long': function(prop, datas, className) {
	if(prop.id.indexOf('pt_') > -1){
		prop.value = '';
	}
	
	if(prop.id.indexOf('dl_') > -1){
		prop.value = '';
	}
	
	if(prop.required){
		var result = "<div id='getInfoTitle' class='title'>" + prop.name + "<span> *</span></div>";
		var isCheck = "checkInfo";
	}else{
		var result = "<div class='title'>" + prop.name + "</div>";
		var isCheck = "noCheckInfo";
	}
	
	var isRead = prop.id.indexOf('info');
	
	if (prop.writable === true && isRead != 0) {
		result += "<input type='text' id='" + prop.id + "' name='" + prop.id + "' class='uploadInput "+isCheck+" " + className + " " + prop.id + "' />";
	} else {
		result += "<input name='" + prop.id + "' class='" + prop.id + "' value='" + prop.value + "' readonly placeholder='" + prop.value + "' />";
	}
	return result;
},
'enum': function(prop, datas, className) {
	if(prop.id.indexOf('dl_') > -1){
		prop.value = '';
	}
	
	if(prop.required){
		var result = "<div id='getInfoTitle' class='title'>" + prop.name + "<span> *</span></div>";
		var isCheck = "checkInfo";
	}else{
		var result = "<div class='title'>" + prop.name + "</div>";
		var isCheck = "noCheckInfo";
	}
	
	var isRead = prop.id.indexOf('info');
	
	if (prop.writable === true && isRead != 0) {
		result += "<input readonly class='autoSelect checkInfo' id='" + prop.id + "'  class='" + className + "'>";
		result += "<input type='hidden' class='hideInput' name='" + prop.id + "' >";
		result += "<img class='autoImg' src='/resources/images/flow/selectOrder.png'>";
		result += "<ul class='autoSelectUl'>";
		$.each(datas[prop.id], function(k, v) {
			result += "<li data-id='" + k + "'>" + v + "</li>";
		});
		result += "</ul>";
	} else {
		result += "<input name='" + prop.id + "' class='"+isCheck+"' value='" + prop.value + "' readonly placeholder='" + prop.value + "' />";
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
            	nowItem.parent().parent().parent().find('.infoContent').find('.reviewItem').hide();
            	nowItem.parent().parent().parent().find('.upInfo').hide();
            }else{
            	nowItem.addClass('openItem');
            	nowItem.parent().parent().parent().find('.infoContent').find('.reviewItem').show();
            	nowItem.parent().parent().parent().find('.upInfo').show();     
            }	
            getHeight();
	});
	
	$('.modelOpen').off('click').on('click',function(){
		var nowItem = $(this);
            if(nowItem.hasClass('openItem')){
            	nowItem.removeClass('openItem');
            	nowItem.parent().parent().parent().parent().find('.itemArea').find('.reviewItem').slideUp();
            	nowItem.parent().parent().parent().parent().find('.reviewItem').hide();
            }else{
            	nowItem.addClass('openItem');
            	nowItem.parent().parent().parent().parent().find('.itemArea').find('r.eviewItem').slideDown();
             	nowItem.parent().parent().parent().parent().find('.reviewItem').show();
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
	
   $('.toArea').off('click').on('click',function(){
	    var projectId = $(this).attr('data-id');
	    var taskName = $(this).attr('data-name');
	    var parentId = $(this).attr('data-parentId');
	    var content = $(this).parent().find('input').val();    
	    if(content == null || content == "" || content == undefined){
	    	$(this).parent().parent().find('.errorSpan').text('回复不能为空');
	    }else{
	    
	    $('.toArea').off('click');
			loadData(function(res){
				if(res.code == 200){
					  initAllTalk();
					  $('.toArea').off('click');
				}else{
					  $('.toArea').off('click');
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
	var isSys;
	if(res.messageType == 1){
		isSys = 	'       <div class="info">'+res.fromName+' : '+res.content+'</div>';
	}else{
		isSys = 	'       <div class="info sys">【系统自动】'+res.fromName+' : '+res.content+'</div>';
	}
	
	var html = [
	    '<div class="areaItem">',
	    '   <div class="infoItem">',
		'	  <img src="'+imgUrl+'">',
		'       '+isSys+'',
		'       <div class="time">',
		'       	<span>发布时间：'+formatDate((res.createDate).replace("CST","GMT+0800"))+'</span>',
		'     	    <div class="openTalk"></div>',
		'       </div>',
   		'   </div>',
		'   <div class="infoContent">',
		'     '+body+'',
		'     <div class="reviewItem">',
		'      <input>',
		'      <div class="btn-c-r toArea" data-id="'+res.projectId+'" data-name="'+res.taskName+'" data-parentId="'+res.projectMessageId+'">回复</div>',
		'     </div>',
		'   </div>',
		'<span class="errorSpan"></span>',
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
				 $('.btnShare').text('复制链接');
				shareEven();
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
	var url = getDfsHostName() + res.resourcePath;
	var urls=getDfsHostName() +res.previewPath;
	var pang=res.previewPath;
	if(pang==null){
		var html = [
			'<div class="filmItem">                                     ',
	        '<img class="filmImg" src="'+src+'"> ',
	        '<div class="filmName">'+checkName+'</div>                         ',
	        '<div class="fileType"><div>'+res.resourceType+'</div></div>            ',
	        '<div class="fileTypeName"><div>'+res.uploaderName+'</div></div>        ',
	        '<div class="time"><div>'+formatDate((res.createDate).replace("CST","GMT+0800"))+'</div></div>        ',
	        '<div class="icon">                                         ',
	        '      <div class="share" data-content="'+url+'"></div>                        ',
	        '      <a href="/resource/getDFSFile/'+res.projectResourceId+'"><div class="download" ></div></a>                         ',
	        '</div>                                                     ',
	        '</div>                                                             ',
		].join('');
		return html;
	}else {
		var html = [
			'<div class="filmItem">                                     ',
	        '<img class="filmImg" src="'+src+'"> ',
	        '<div class="filmName">'+checkName+'</div>                         ',
	        '<div class="fileType"><div>'+res.resourceType+'</div></div>            ',
	        '<div class="fileTypeName"><div>'+res.uploaderName+'</div></div>        ',
	        '<div class="time"><div>'+formatDate((res.createDate).replace("CST","GMT+0800"))+'</div></div>        ',
	        '<div class="icon">                                         ',	      
	        '      <a href="'+urls+'" target="_black"><div class="look" data-content="'+url+'"></div></a>                         ',
	        '      <div class="share" data-content="'+url+'"></div>                        ',
	        '      <a href="/resource/getDFSFile/'+res.projectResourceId+'"><div class="download" ></div></a>                         ',
	        '</div>                                                     ',
	        '</div>                                                             ',
		].join('');
		return html;
	}
	
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


function shareEven(){
	$('.share').off('click').on('click',function(){
		$('.btnShare').text('复制链接');
		$('.modelCard .btnShare').css('background','#fe5453');
	    $('.modelCard .btnShare').addClass('btn-c-r');
	    $(window.parent.parent.parent.document).find('html').scrollTop(0);
		$(window.parent.parent.parent.document).find('body').scrollTop(0);
		$('#isCopy').show();
		var url = $(this).attr('data-content');
		$('#setInfoCopy').text(url);
		var clipboard = new Clipboard('.btnShare');   
		   clipboard.on('success', function(e) { 
			   $('.modelCard .btnShare').css('background','#999 ');
			   $('.modelCard .btnShare').removeClass('btn-c-r');		
			   $('.btnShare').text('复制成功');
		        });  
		   clipboard.on('error', function(e) {  
			   $('.modelCard .btnShare').css('background','#999 ');
			   $('.modelCard .btnShare').removeClass('btn-c-r');
			        $('.btnShare').text('复制失败');
		        }); 	
	});
}

//自动联动供应商信息
function autoTeamInput(){
	$('#scTeamName').bind('input propertychange', function() {
		 var theName = $(this).val();
		 $('#scTeamId').val("");
		 findAutoInfoTeam(theName);
		 $('.utoInfoTeam').show();
		 if(theName == null || theName == ""){
			 $('.utoInfoTeam').hide();
		 }
	});
}

function findAutoInfoTeam(userName){
	loadData(function(res){
		var res = res;
		var body = $('.utoInfoTeam');
		body.html('');
		if(res != null && res != undefined){
			for (var int = 0; int < res.length; int++) {
				   var html =createUserInfo(res[int].teamId,res[int].teamName,res[int].linkman,res[int].phoneNumber);
				   body.append(html);
			};
			autoTeamLi();
		}
	}, getContextPath() + '/team/listByName/'+userName,null);
}

function autoTeamLi(){
	$('.utoInfoTeam li').off('click').on('click',function(){
		  $('.utoInfoTeam').hide();
		  var name = $(this).text();
		  var id = $(this).attr('data-id');
		  var linkman = $(this).attr('data-linkman');
		  var phone = $(this).attr('data-phone');
		  $(this).parent().parent().find('input').val(name);
		  $('#scTeamName').val(name);
		  $('#scTeamId').val(id);
		  $('#scLink').val(linkman);
		  $('#scTel').val(phone);
	});
}

//自动联动制作供应商信息
function autoTeamMakeInput(){
	$('#prTeamName').bind('input propertychange', function() {
		 var theName = $(this).val();
		 $('#prTeamId').val("");
		 findAutoInfoMakeTeam(theName);
		 $('.utoInfoMakeTeam').show();
		 if(theName == null || theName == ""){
			 $('.utoInfoMakeTeam').hide();
		 }
	});
}

function findAutoInfoMakeTeam(userName){
	loadData(function(res){
		var res = res;
		var body = $('.utoInfoMakeTeam');
		body.html('');
		if(res != null && res != undefined){
			for (var int = 0; int < res.length; int++) {
				   var html =createUserInfo(res[int].teamId,res[int].teamName,res[int].linkman,res[int].phoneNumber);
				   body.append(html);
			};
			autoTeamMakeLi();
		}
	}, getContextPath() + '/team/listByName/'+userName,null);
}

function autoTeamMakeLi(){
	$('.utoInfoMakeTeam li').off('click').on('click',function(){
		  $('.utoInfoMakeTeam').hide();
		  var name = $(this).text();
		  var id = $(this).attr('data-id');
		  var linkman = $(this).attr('data-linkman');
		  var phone = $(this).attr('data-phone');
		  $(this).parent().parent().find('input').val(name);
		  $('#prTeamName').val(name);
		  $('#prTeamId').val(id);
		  $('#prLink').val(linkman);
		  $('#prTel').val(phone);
	});
}


function provNewInit(){
	createProv();
	delProv();
}
//删除供应商
function delProv(){
	$('.delPro').off('click').on('click',function(){
		$('#errorDelProv').show();
		 $(window.parent.parent.parent.document).find('html').scrollTop(0);
		$(window.parent.parent.parent.document).find('body').scrollTop(0);
		var proId = $(this).attr('data-idp');
		initDelProv(proId,$(this));
	});
}

function initDelProv(proId,item){
	var ss = item.attr('data-idp');
	$('#cancleProveReason').val('');
	$('#errorProveReason').text('');
	$('#checkReasopnProv').off('click').on('click',function(){
		var setValue = $('#cancleProveReason').val();
		if(setValue!=""&&setValue!=null&&setValue!=undefined){
			loadData(function(res){
				if(res.result){
					$('#errorDelProv').hide();
					item.parent().parent().find('.item').find('.'+proId+'').css('color','#fe5453');
					item.parent().parent().find('.item').find('.'+proId+'').text('已删除')
				}else{
					$('#errorProveReason').text(res.err);
				}
			}, getContextPath() + '/project/delete/produce/team',$.toJSON({
				projectTeamId:proId,
				projectId:$('#projectId').val(),
				reason:$('#cancleProveReason').val()
			}));
		}else{
			$('#errorProveReason').text('请填写删除原因');
		}
	});
	
}

//新增供应商
function createProv(){
	$('.addPro').off('click').on('click',function(){
		  var id = $(this).parent().parent().find('.contentTeamId').val();
		  $('#prov_teamId').val(id);
		  $('#createProivder').show();		
		  $(window.parent.parent.parent.document).find('body').scrollTop(0);
		  $(window.parent.parent.parent.document).find('html').scrollTop(0);
		  autoInputCreateProv();
		  dataEven();
		  $('.checkProvError').val('');
		  $('.checkProvErrorP').val('');
		  $('.checkProvErrorEmail').val('');
		  $('#checkCprov').off('click').on('click',function(){
			  if(checkCreateProvider()){
				  addSubmitProv();
			  }
		  });
	});
}

//自动联动客户信息
function autoInputCreateProv(){
	$('#prov_teamName').bind('input propertychange', function() {
		 $('#prov_teamId').val("");
		var theName = $(this).val();
		 findAutoInfoCreateProv(theName);
		 $('.createUi').show();
		 if(theName == null || theName == ""){
			 $('.createUi').hide();
		 }
	});
}

function findAutoInfoCreateProv(userName){
	loadData(function(res){
		var res = res;
		var body = $('.createUi');
		body.html('');
		if(res != null && res != undefined){
			for (var int = 0; int < res.length; int++) {
				   var html =createUserInfo(res[int].teamId,res[int].teamName,res[int].linkman,res[int].phoneNumber);
				   body.append(html);
			};
			autoLiCreateProv();
		}
	}, getContextPath() + '/team/listByName/'+userName,null);
}

function autoLiCreateProv(){
	
	$('.createUi li').off('click').on('click',function(){
		  $('.createUi').hide();
		  var name = $(this).text();
		  var id = $(this).attr('data-id');
		  var linkman = $(this).attr('data-linkman');
		  var phone = $(this).attr('data-phone');
		  $(this).parent().parent().find('input').val(name);
		  $('#prov_teamId').val(id);
		  $('#prov_linkman').val(linkman);
		  $('#prov_telephone').val(phone);
		  
	});
}

function checkCreateProvider(){
    var error = $('.checkProvError');
    var errorP = $('.checkProvErrorP');
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
    
    if(!checkEmail($('.checkProvErrorEmail').val())){
    	$('.checkProvErrorEmail').parent().attr('data-content','邮箱格式不正确');
    	return false;
    }
    
    return true;
}

function loadProvCard(){
	loadData(function(res){
		if(res != null && res != undefined){
		   console.info(res);
		}
	}, getContextPath() + '/project/getProduceTeamInfo/'+$('#projectId').val(),'');
}


function addSubmitProv(){
	
	var prov_teamId =   $('#prov_teamId').val();
	var prov_teamName = $('#prov_teamName').val();
	var prov_linkman =  $('#prov_linkman').val();
	var prov_telephone =  $('#prov_telephone').val();
	var prov_budget =   $('#prov_budget').val();
	var prov_makeContent =   $('#prov_makeContent').val();
	var prov_makeTime =   $('#prov_makeTime').val();
	var comment  = $('#comment').val();
	var email  = $('#prov_email').val();
	loadData(function(res){
		initLastTime(res.projectCycle,res.createDate);
		if(res != null && res != undefined){
			   var html = [
					'<div class="item">',
				    '   <div>供应商名称</div>',
				    '     <div>',
				    '        '+prov_teamName+'',
				    '     </div>',
				    '</div> ',
				    '<div class="item">',
				    '   <div>供应商联系人</div>',
				    '     <div>',
				    '        '+prov_linkman+'',
				    '     </div>',
				    '</div> ', 
				    '<div class="item">',
				    '   <div>供应商联系电话</div>',
				    '     <div>',
				    '         '+prov_telephone+'',
				    '     </div>',
				    '</div> ', 
				    '<div class="item smallItem">',
				    '   <div>状态</div>',
				    '     <div class="status" style="color:green">',
				    '         正常',
				    '     </div>',
				    '</div> ', 
				    '<div class="item smallItem">',
				    '   <div>操作</div>',
				    '     <div class="delPro" data-idp='+res+'>',
				    '         删除',
				    '     </div>',
				    '</div> ',
				    '<div class="item">',
				    '   <div>供应商制作邮箱</div>',
				    '     <div>',
				    '         '+email+'',
				    '     </div>',
				    '</div> ', 
				    '<div class="item">',
				    '   <div>供应商制作内容</div>',
				    '     <div>',
				    '         '+prov_makeContent+'',
				    '     </div>',
				    '</div> ', 
				].join('');
			   $('#createProivder').hide();
			   $('#makeProvItem').show();
			   $('#makeProvItem').append(html);
		}
	}, getContextPath() + '/project/add/produce/team/',$.toJSON({
		projectId:$('#projectId').val(),
		teamId:prov_teamId,
		teamName:prov_teamName,
		linkman:prov_linkman,
		telephone:prov_telephone,
		budget:prov_budget,
		makeContent:prov_makeContent,
		makeTime:prov_makeTime,
		budget:prov_budget,
		comment:comment
	}));
	
}




