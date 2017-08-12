var InterValObj; // timer变量，控制时间  
var count = 120; // 间隔函数，1秒执行  
var curCount; // 当前剩余秒数 
$().ready(function() {
	
/*	var waitCard = $('.waitCard');
	var cardNUm = waitCard.length;
	if(waitCard.length == null || waitCard.length=="" ){
		$(window.parent.parent.parent.document).find('#cardNum').hide();
	}else{
		$(window.parent.document).find('#cardNum').text(cardNUm);
	}*/
	$(window.parent.document).find('.frame').css('height',$('.pages').height() + 300);
	getDate();
});

function getDate(){
	
	var setTime =  $('.setLastTime');
	if(setTime.length >= 0){
		var nowData = Date.parse(new Date())/1000;
        for (var i = 0; i < setTime.length; i++) {
		   var time =Date.parse($(setTime[i]).text())/1000;
		   var getTime = Date.parse($(setTime[i]).text());
		   var isTime =(time - nowData)/3600;
		   var d = new Date(getTime);
		   if(isTime < 0){
			   $(setTime[i]).parent().parent().find('img').attr('src','/resources/images/flow/demoR.png');
			   $(setTime[i]).text(' 延误 '+formatDate(d));
		   }
		   if(isTime >= 3){
			   $(setTime[i]).parent().parent().find('img').attr('src','/resources/images/flow/demoG.png');
			   $(setTime[i]).text(' 正常 '+formatDate(d));
		   }
		   if(isTime <3 && isTime>=0){
			   $(setTime[i]).parent().parent().find('img').attr('src','/resources/images/flow/demoY.png');
			   $(setTime[i]).text(' 紧急 '+formatDate(d));
		   }
		   
	    }		
	}
	
}

function   formatDate(now)   {     
    var   year=now.getFullYear();     
    var   month=now.getMonth()+1;     
    var   date=now.getDate();     
    var   hour=now.getHours();     
    var   minute=now.getMinutes();     
    var   second=now.getSeconds();   
    return year+"-"+month+"-"+date+" "+hour+":"+minute+":"+second; 
    }     


