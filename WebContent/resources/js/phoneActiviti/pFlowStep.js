var resMap = "";
$().ready(function(){
	$('.frameHead .name').text($('#projectName').val());
	getStepMore();
	getStageInfo($('#projectStage').val());
	getStage();
});

function getStepMore(){
    $('.itemTop').off('click').on('click',function(){
    	if($(this).hasClass('open')){
			$(this).removeClass('open');
			$(this).parent().find('.itemInfo').slideUp();
		}else{
			getStageInfo($(this).attr('data-value'));
			$(this).addClass('open');
			$(this).parent().find('.itemInfo').slideDown();
		}
    })
}

function getStage(){
	
	var stage = $('#projectStage').val();
	if(stage == '1'){
		stage1();
	}
	if(stage == '2'){
		stage2();
	}
	if(stage == '3'){
		stage3();
	}
	if(stage == '4'){
		stage4();
	}
	if(stage == '5'){
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

function getLocation(){
	
	$('.setContent').off('click').on('click',function(){
             var id = $(this).attr('data-id');
             var pId = $('#projectId').val();
             var psId =  $('#processInstanceId').val();
             window.location.href ="/project/phone/info/"+id+"/"+pId+"/"+psId";
	});
	
}

function getStageInfo(stage){	
	var keys = stage;
	if(resMap == ""){
		loadData(function(res){
			if(res != null && res != undefined){
			    resMap = res;
			}
		}, getContextPath() + '/project/project-task/'+$('#projectId').val(),$.toJSON({projectName:keys}));
	}else{
		var resKey = resMap[keys];
		getStageCard(resKey,stage);
	}
}

function getStageCard(resKey,stage){
var Stage = $('#projectStage').val();
	if(resKey.length > 0){
		if(stage == '沟通阶段'){
			var body =$('#stageInfo1');
			body.html('');
		}
		if(stage == '方案阶段'){
			var body =$('#stageInfo2');
			body.html('');
		}
		if(stage == '商务阶段'){
			var body =$('#stageInfo3');
			body.html('');
		}
		if(stage == '制作阶段'){
			var body =$('#stageInfo4');
			body.html('');
		}
		if(stage == '交付阶段'){
			var body =$('#stageInfo5');
			body.html('');
		}
		
		var setBody = "";
		for (var int = 0; int < resKey.length; int++) {
			 var html =createStageInfo(resKey[int]); 
			 body.append(html);
		}
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