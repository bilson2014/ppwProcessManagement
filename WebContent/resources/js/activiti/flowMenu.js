var InterValObj; // timer变量，控制时间  
var count = 120; // 间隔函数，1秒执行  
var curCount; // 当前剩余秒数 
$().ready(function() {
	initMenuEven();
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
			 $('#myOrder').removeClass('open');
			 $('#orderList').slideUp();
			 $('#productList').slideDown();
		 }
	});
	
	$('#myOrder').off('click').on('click',function(){
		var nThis = $(this);
		 if($(this).hasClass('open')){
			 nThis.removeClass('open');
			 $('#orderList').slideUp();
		 }else{
			 nThis.addClass('open');
			 $('#myPro').removeClass('open');
			 $('#productList').slideUp();
			 $('#orderList').slideDown();
		 }
	});

	$('.productList li').off('click').on('click',function(){
               $('.productList li').removeClass('checkLi');
               $(this).addClass('checkLi');
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
	
	
	$('#minMyPro').off('click').on('click',function(){
		$('.flowMenu').removeClass('changeMenu');
		$('.page').removeClass('toMinLeft');
		if(!$('.productList li').hasClass('checkLi')){
			$('#myPro').addClass('open');
			$('#nowDoing').addClass('checkLi');
		}
		$('#productList').slideDown();
	});
}