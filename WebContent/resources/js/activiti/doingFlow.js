
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
		   var d = new Date(getTime);
		   $(otherTime[i]).text('截止于'+formatDate(d));
	    }		
	}
}

function formatDate(now)   {     
    var   year=now.getFullYear();     
    var   month=now.getMonth()+1;     
    var   date=now.getDate();     
    var   hour=now.getHours();     
    var   minute=now.getMinutes();     
    var   second=now.getSeconds();   
    return year+"年"+month+"月"+date+"日"+hour+"时"+minute+"分"; 
 }     
//时间戳
function getTise(time) {
    var oDate = new Date(time);
    var year = oDate.getFullYear();
    var hour = oDate.getHours();
    var Minutes = oDate.getMinutes();
    var second= oDate.getSeconds();
    var setDay ="";
    var date= oDate.getDate(); 
    if(date > 0){
    	setDay = date+" 天 ";
    }
    return setDay + hour + ' 时 ' + Minutes +" 分 ";
}




