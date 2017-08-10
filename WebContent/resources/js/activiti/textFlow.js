var InterValObj; // timer变量，控制时间  
var count = 120; // 间隔函数，1秒执行  
var curCount; // 当前剩余秒数 
$().ready(function() {
	initMenuEven();
	doing();
	doPasue();
	doFinish();
});

function init(){

	
}

function initMenuEven(){
	
	$('#myPro').off('click').on('click',function(){
		var nThis = $(this);
		 if($(this).hasClass('open')){
			 nThis.removeClass('open');
			 $('#productList').slideUp();
		 }else{
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
	
	//二级菜单
	$('#productList li').off('click').on('click',function(){
		
	});
	
}

function doing(){
	$('#nowDoing').off('click').on('click',function(){
		$('#upName').text("待办项目");
		$('#downName').text("其它项目");
	});
}

function doPasue(){
	$('#pause').off('click').on('click',function(){
		$('#upName').text("暂停项目");
		$('#downName').text("暂停项目");  
	});
}

function doFinish(){
	$('#finish').off('click').on('click',function(){
		$('#upName').text("完成项目");
		$('#downName').text("取消项目");
	});
}

function createWaitCard(){
	var html = [
	           ' <div class="waitCard"> ' ,
               '    <div class="cardH">' ,
               '    <div class="title">这里是卡片的标题啊啊啊啊</div>' ,
               '    <div class="point">' ,
               '        <div class="showPoint">SA</div>' ,
               '        <div class="showDeil">' ,
               '            <div class="proPoint">项目评级<span>S</span></div>' ,
               '            <div class="cusPoint">客户评级<span>A</span></div>' ,
               '        </div>' ,
               '    </div>' ,
               '    <div class="your">负责项目</div>' ,
               '  </div>' ,
               '    <div class="cardContent">' ,
               '      <img src="/resources/images/flow/demoG.png">' ,
               '       <div class="setContent">' ,
               '           <div class="listName">上传周期表</div>' ,
               '           <div class="lastTime">已超时 24h 5min 45s</div>' ,
               '       </div>' ,
               '     </div>' ,
               '   </div>' ,
	].join('');
	return html;
}

function createotherCard(){
	var html = [
	           ' <div class="otherCard"> ' ,
               '    <div class="cardH">' ,
               '    <div class="title">这里是卡片的标题啊啊啊啊</div>' ,
               '    <div class="point">' ,
               '        <div class="showPoint">SA</div>' ,
               '        <div class="showDeil">' ,
               '            <div class="proPoint">项目评级<span>S</span></div>' ,
               '            <div class="cusPoint">客户评级<span>A</span></div>' ,
               '        </div>' ,
               '    </div>' ,
               '    <div class="user">负责人<span>她她她</span></div>' ,
               '  </div>' ,
               '    <div class="cardContent">' ,
               '       <div class="setContent">' ,
               '           <div class="lastTime">已超时 24h 5min 45s</div>' ,
               '       </div>' ,
               '          <img src="/resources/images/flow/newFinish.png">' ,
               '     </div>' ,
               '   </div>' ,
	].join('');
	return html;
}
