var InterValObj; // timer变量，控制时间  
var count = 120; // 间隔函数，1秒执行  
var curCount; // 当前剩余秒数 
$().ready(function() {
	init();
});

function init(){
	$('#closeCheck').off('click').on('click',function(){
		$('.tooltip-check').hide();
	});
	$('#sureCheck').off('click').on('click',function(){
		$('.tooltip-check').hide();
	});
	
}