//$().ready(function(){
//	document.domain = getUrl();
//	$(window.parent.document).find('.frame').css('height',$('.infos').height() + 300);
//
//});
var count=0;
var time,matter,whenval,textval;
var yy;
var mm;
var dd;
$().ready(function() {
	document.domain = getUrl();
	$(window.parent.document).find('.frame').css('height',$('.pages').height() + 50);
    var date = new Date();
//    console.log(date);
    var d = date.getDate();
    var m = date.getMonth();
    var y = date.getFullYear();
    m=m+1;
    $('#updateDate').val(y+'-'+m+'-'+d);
    

//    var dayData = [{
//        title: one,
//        start: new Date(y, m, 1),
//        allDay: false
//    },
//    {
//    	 title: two,
//         start: new Date(y, m, 1),
//         allDay: false
//    },
//    {
//         title: 'All Day Event2',
//         start: new Date(y, m, 1),
//         allDay: false
//    },
//    {
//        title: 'Lunch',
//        start: new Date(y, m, d, 12, 0),
//        end: new Date(y, m, d, 14, 0),
//        allDay: false
//    },
//    {
//        title: 'Gee',
//        start: new Date(yy,mm,dd),
//        end: new Date(2017,11,1),
//        allDay: false
//    },
//    {
//        title: 'values.name',
//        start: new Date(y, m, 28),
//        end: new Date(y, m, 29),
//        url: 'http://google.com/'
//    }
//
//];
    $('#calendar').fullCalendar({
        header: {
            left: 'prev,next today',
            center: 'title',
            right: 'month,basicWeek,basicDay'
        },
        editable: true,
//        events: dayData,
    });
    leftbtn();
    initSelect();
    sun();
    initSelect();
    getday();
    bestthings();
    delselc();
    textareval();
 
});

	
//生成排期表
function bestthings() {
    $('.best').on('click', function() {
    	sun();
    	var end=$(".fc-week td");
    	var gamething=[];
    	end.each(function(){
    		var kou=$(this).find('.matter').text();
    		if(kou){
    			var nowtime=$(this).attr('data-date');
    			if (gamething.length>0){
    				var jsonthing=',{"jobContent":"'+kou+'","start":"'+nowtime+'"}';		
    			}else {
    				var jsonthing='{"jobContent":"'+kou+'","start":"'+nowtime+'"}';
    			}
    				
    			gamething+=jsonthing;	
    		}
    	})
    	gamething="["+ gamething+"]";
    	gamething=JSON.parse(gamething);
    	var projectName = $('#projectName').val();  	
    	if(projectName== null || projectName == "" || projectName == undefined){
    		$('.proerr').text('项目名称未填写');
			$('#projectName').focus();
			return false;
    	}else {
    		loadData(function(res){
    	    	if (res.result){
    	    		console.log('成功了');
    	    		window.location.href = getContextPath() + "/schedule/export/" + res.msg;
    	    	}else {
    	    		console.log('失败了');
    	    	}
    	    	console.log(res);
    		}, getContextPath() + '/schedule/save',$.toJSON({
    			scheduleId:$('#scheduleId').val(), 
    			projectId: $('#projectId').val(),
    			projectName: $('#projectName').val(),
    			updateDate:  $('#updateDate').val(),
    			items:gamething
    		    
    		}));
    	}
    	  
    	
    	  
    	

    	 
    	 
    	 
    	 
    });
}
//实时获取textare的数据
function textareval(){
	 $("textarea").blur(function(){
		 $(this).text($(this).val());
	 });
}
//获取事件的时间
function getday(){
	$('.fc-event-time').text('');//清空插件中时间限制
	$('.fc-week td').on('click',function(){
		time=$(this).attr('data-date');
		//获取时间2017-11-28 时间格式处理
		var times=JSON.stringify(time);
		yy=times.substr(1,4);//年
		mm=times.substr(6,2);//月
		dd=times.substr(9,2);//日
	})
}

//下拉框
function sun(){
    // 多选
    var MulticitySelect1 = $('.city-select').citySelect({
        dataJson: cityData,//json 数据 是HTML显示的列表数据
        multiSelect: true,//多选设置,默认不开启
        multiMaximum: 30,//允许能选择几个 ，默认5，只用于多选
        search: false,//开启搜索功能，默认是true ，开启
        placeholder: '请选择任务',
        onInit: function() {//插件初始化的回调
//            console.log(this)
        },
        onForbid: function() {//插件禁止后再点击的回调
//            console.log(this)
        },
        onTabsAfter: function(target) {//点击tabs切换显示城市后的回调
//            console.log(event)
        },
        //选择城市后的回调
        onCallerAfter: function(target, values) {
            matter=JSON.stringify(values);
            if (matter){
            	var bestval=$(".fc-week td[data-date="+time+"]").find(".city-info span");
            	var shus='';
            	bestval.each(function(){
            		var countext =  $(this).text()+ ' ,   ';
            		shus+=countext;
            	});
//            	添加当前的内容到当前时间下面
            	$(".fc-week td[data-date="+time+"]").find(".matter").attr('style', 'display: block;');
            	$(".fc-week td[data-date="+time+"]").find(".matter").text(shus);
            	whenval=$(".fc-week td[data-date="+time+"]").find(".matter").text();   
            	$(".fc-week td[data-date="+time+"]").find(".matter").val(whenval); 
            	var change=values.name.toString();//数据形式的转换
            	var jsonstring="{'jobContent':'"+values.name+"','start':'"+time+"'}";     
            	textval=jsonstring+textval;
            }else{
            	$(".fc-week td[data-date="+time+"]").find(".matter").attr('style', 'display: none;');
            	$(".fc-week td[data-date="+time+"]").find(".matter").text('');
            	console.log('没有数据可以添加');
            }
            delselc();
        }
        
    });


    // 多选设置城市接口
//    MulticitySelect1.setCityVal('少女时代,啦的范德萨发啦');

}
//刪除框数据的同步 
function delselc(){
	$('.city-info i').on('click',function(){
		var deltext=$(this).parent().parent().parent().find(".city-info span");
		if (deltext.length==1){
//			通过删除都删掉了
			$(".fc-week td[data-date="+time+"]").find(".matter").attr('style', 'display: none;');
        	$(".fc-week td[data-date="+time+"]").find(".matter").text('');
		}
	})	
}
// 月份左右跳转
function leftbtn() {
    $('tbody .fc-other-month .much').attr('style', 'display: none;');
    $('tbody .fc-other-month .boxs').attr('style', 'display: none;');
    $('.fc-header-left .fc-button').on('click', function() {
    	 initSelect();
    	 sun();
    	 getday();
        $('tbody .fc-other-month .much').attr('style', 'display: none;');
        $('tbody .fc-other-month .boxs').attr('style', 'display: none;');
        $('.fc-header-left .fc-button-today').removeAttr('style', 'pointer-events: none;');
    })
    $('.fc-header-left .fc-button-today').on('click', function() {
    	 initSelect();
    	 sun();
    	 getday();
        if (!$('.fc-header-left .fc-button-today').hasClass('fc-state-disabled')) {
            $('tbody .fc-other-month .much').attr('style', 'display: none;');
            $('tbody .fc-other-month .boxs').attr('style', 'display: none;');
            $('.fc-header-left .fc-button-today').removeAttr('style', 'pointer-events: none;');
        } else {
            $('.fc-header-left .fc-button-today').attr('style', 'pointer-events: none;');
        }
    })

    $('.fc-header-right').hide();
   
}


//自定义复选框
function initSelect() {
	$('.city-select').slideUp();
    $('.orderSelect').off('click').on('click', function(e) {
    	$('.boxs .city-select').remove('.city-select');
    	//添加append() - 在被选元素的结尾插入内容
    	//prepend() - 在被选元素的开头插入内容
    	//empty() - 从被选元素中删除子元素
        
        if ($(this).hasClass('selectColor')) {
        	$(this).parent().parent().find('.boxs .city-select').remove('.city-select');
        	$(this).parent().parent().find('.city-select').removeAttr('style', 'display: none;');
        	$(this).parent().parent().find('.city-select').slideUp();
            $(this).removeClass('selectColor');

        } else {

        	$(this).parent().parent().find('.boxs').prepend( "<div class='city-select' id=''></div>" );
        	sun();
            $('.orderSelect').removeClass('selectColor');
            $(this).parent().parent().find('.city-select').slideDown();
            $(this).addClass('selectColor');           
        }
        e.stopPropagation();
    });

}