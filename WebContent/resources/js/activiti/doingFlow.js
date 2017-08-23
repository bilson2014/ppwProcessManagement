
$().ready(function() {
	initPageEven();
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
		   var time =Date.parse($(otherTime[i]).text())/1000;
		   var getTime = Date.parse($(otherTime[i]).text());
		   $(otherTime[i]).text('截止于'+formatDate(getTime));
	    }		
	}
	
	var pauseTime = $('.pauseTime');
	if(pauseTime.length >= 0){
        for (var i = 0; i < pauseTime.length; i++) {
		   var time =Date.parse($(pauseTime[i]).text())/1000;
		   var getTime = Date.parse($(pauseTime[i]).text());
		   $(pauseTime[i]).text('暂停于'+formatDate(getTime));
	    }		
	}
	
	var finishTime = $('.finishTime');
	if(finishTime.length >= 0){
        for (var i = 0; i < finishTime.length; i++) {
		   var time =Date.parse($(finishTime[i]).text())/1000;
		   var getTime = Date.parse($(finishTime[i]).text());
		   $(finishTime[i]).text('结束于'+formatDate(getTime));
	    }		
	}
	
	
}






