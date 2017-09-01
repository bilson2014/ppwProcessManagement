var InterValObj; // timer变量，控制时间  
var count = 120; // 间隔函数，1秒执行  
var curCount; // 当前剩余秒数 
$().ready(function() {
	initMenuEven();
	doing();
	doPasue();
	doFinish();
	var carNum = $('.cardNum');
	if(carNum.length == null || carNum.length=="" ){
		$('#cardNum').hide();
	}else{
		$('#cardNum').text(carNum.length);
	}
	$('#productList').show();
	$('#myPro').addClass('open');
	$('#nowDoing').addClass('checkLi');
	$('#setRealName').text($('#realName').val());
	var url = $('#photo').val();
	if(url != null && url !=""  && url !=undefined)
	$('#newMenuLogo').attr('src',getDfsHostName()+url );
	checkState();
});



function checkState(){
	 var href = window.location.href;
	 var state = href.substr(href.lastIndexOf("?")+1,href.length);
	    if(state.trim() == "pause"){
	    	doPasue();
	    }
	    if(state.trim() == "finish"){
	    	doFinish();
	    }
}

function initMenuEven(){
	
	$('#myPro').off('click').on('click',function(){
		var nThis = $(this);
		 if($(this).hasClass('open')){
			 nThis.removeClass('open');
			 $('#productList').slideUp();
		 }
		 else
		 {
			 nThis.addClass('open');
			 $('#productList').slideDown();
		 }
	});
	
	//特换到小菜单
	$('#toMin').off('click').on('click',function(){
		$('.flowMenu').addClass('changeMenu');
		$('.page').addClass('toMinLeft');
		if($('#myPro').hasClass('open')){
			$('#minMyPro').addClass('open');
		}else{
			$('#minMyPro').removeClass('open');
		}
	});
	
	//切换回大菜单
	$('#menuHead').off('click').on('click',function(){
		$('.flowMenu').removeClass('changeMenu');
		$('.page').removeClass('toMinLeft');
	});
	
}

function doing(){
	$('#nowDoing').off('click').on('click',function(){
		$('#upName').text("待办项目");
		$('#downName').text("其它项目");
		$('.productList li').removeClass('checkLi');
        $(this).addClass('checkLi');
        $('#hideDiv').show();
        $('.frame').attr('src',getContextPath()+"/project/running-doing");
	});
}

function doPasue(){
	$('#pause').off('click').on('click',function(){
		$('#upName').text("暂停项目");
		$('#downName').text("暂停项目"); 
		$('.productList li').removeClass('checkLi');
        $(this).addClass('checkLi');
        $('#hideDiv').hide();
        $('.frame').attr('src',getContextPath()+"/project/suspend-task");
	});
}

function doFinish(){
	$('#finish').off('click').on('click',function(){
		$('#upName').text("完成项目");
		$('#downName').text("取消项目");
		$('.productList li').removeClass('checkLi');
        $(this).addClass('checkLi');
        $('#hideDiv').show();
        $('.frame').attr('src',getContextPath()+"/project/finished/list");
	});
}




