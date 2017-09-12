$().ready(function(){
	getStepMore();
	getStageInfo();
});

function getStepMore(){
    $('.itemTop').off('click').on('click',function(){
    	if($(this).hasClass('open')){
			$(this).removeClass('open');
			$(this).parent().find('.itemInfo').slideUp();
		}else{
			$(this).addClass('open');
			$(this).parent().find('.itemInfo').slideDown();
		}
    })
}

function getStage(){
	
	var stage = $('#taskStage').val();
	if(stage == '沟通阶段'){
		stage1();
	}
	if(stage == '方案阶段'){
		stage2();
	}
	if(stage == '商务阶段'){
		stage3();
	}
	if(stage == '制作阶段'){
		stage4();
	}
	if(stage == '交付阶段'){
		stage5();
	}

}

function stage1(){
	checkId($('#stage1'));
}

function stage2(){
	$('#stage1').addClass('greenItem');
	checkId($('#stage2'));
}

function stage3(){
	$('#stage1').addClass('greenItem');
	$('#stage2').addClass('greenItem');
	checkId($('#stage3'));
}
function stage4(){
	$('#stage1').addClass('greenItem');
	$('#stage2').addClass('greenItem');
	$('#stage3').addClass('greenItem');
	checkId($('#stage4'));
}
function stage5(){
	$('#stage1').addClass('greenItem');
	$('#stage2').addClass('greenItem');
	$('#stage3').addClass('greenItem');
	$('#stage4').addClass('greenItem');
	checkId($('#stage5'));
}

function checkId(id){
	var projectStatus = $('#projectStatus').val();
	if(projectStatus == 'suspend'){
	   id.addClass('pauseItem');	
	}
	if(projectStatus == 'cancel'){
	   id.addClass('cancleItem');	
	}
	if(projectStatus == 'finished'){
		id.addClass('greenItem');
	}
	if(projectStatus == "" || projectStatus == null || projectStatus == undefined ){
		id.addClass('yellowItem');
	}
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
		var body =$('.itemInfo');
		body.html('');
		var setBody = "";
		for (var int = 0; int < resKey.length; int++) {
			 var html =createStageInfo(resKey[int]); 
			 body.append(html);
		}
		getHeight();
	}
}

function createStageInfo(res){

	
	var checkStatus = res.taskStatus;
	
	if(checkStatus == "completed"){
		var imgUrl = '<img src="/resources/images/pFlow/c.png">';
	}
	if(checkStatus == "running"){
		var imgUrl = '<img src="/resources/images/pFlow/tagDo.png">';
	}
	if(checkStatus == "futher"){
		var imgUrl = '<img src="/resources/images/pFlow/tagError.png">';
	}
    
	var html = [
	   '<div class="setContent" data-id="'+res.taskId+'"  data-name="'+res.taskName+'">',
	   '       '+imgUrl+' ',
	   '       <img src="/resources/images/pFlow/check.png" />',
	   '       <div>'+res.taskName+'</div>',
	   '</div>',
	].join('');
	return html;
}