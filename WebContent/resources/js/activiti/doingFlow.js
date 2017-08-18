
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
	
	var setTime =  $('.setLastTime');
	if(setTime.length >= 0){
		var nowData = Date.parse(new Date());
        for (var i = 0; i < setTime.length; i++) {
        	var test = $(setTime[i]).text();
		   var time =Date.parse($(setTime[i]).text());
		   var lastTime = (time - nowData);
		   var lastHour =(time - nowData)/3600000;
		   var passTime = (nowData - time);
		   getTise(lastTime);
		   var getTime =$(setTime[i]).text();
		   if(lastTime < 0){
			   $(setTime[i]).parent().parent().find('img').attr('src','/resources/images/flow/demoR.png');
			   $(setTime[i]).text(' 已超时 '+getTise(passTime));  //3
		   }
		   if(lastTime >= 3){
			   $(setTime[i]).parent().parent().find('img').attr('src','/resources/images/flow/demoG.png');
			   $(setTime[i]).text('剩余 '+getTise(lastTime));
		   }
		   if(lastTime <3 && lastHour>=0){
			   $(setTime[i]).parent().parent().find('img').attr('src','/resources/images/flow/demoY.png');
			   $(setTime[i]).text('剩余'+getTise(lastTime));
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
}






