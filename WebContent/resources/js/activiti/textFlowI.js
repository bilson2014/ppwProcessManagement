var InterValObj; // timer变量，控制时间  
var count = 120; // 间隔函数，1秒执行  
var curCount; // 当前剩余秒数 
$().ready(function() {
	dataEven();
	openInfoCard();
	initEvenInfo();
	initSelect();
	flagEven();
});

function initEvenInfo(){
	$('#toFinish').off('click').on('click',function(){
         $('#cusModel').show();		
	});
	$('.closeModel').off('click').on('click',function(){
         $('.cusModel').hide();		
	});
	$('#myOrder').show();
	openBill();
	openPrice();
	budgetPrice();
	realPrice();
	invoicePrice();
	cusInfo();
	helper();
	controlModel();
	showCusEdit();
	upModel();
	showError();
	finishCus();
	showPlot();
	showExecutive();
	showWarn();
	proInfo();
}

function openInfoCard(){
	$('.controlOpen').off('click').on('click',function(){
		var nowItem = $(this);
            if(nowItem.hasClass('openItem')){
            	nowItem.removeClass('openItem');
            	nowItem.parent().parent().find('.getInfoItemContent').slideUp();
            }else{
            	nowItem.addClass('openItem');
            	nowItem.parent().parent().find('.getInfoItemContent').slideDown();
            }		     
	});
	
	$('.openTalk').off('click').on('click',function(){
		var nowItem = $(this);
		nowItem.parent().parent().parent().find('.infoContent').find('input').focus();
            if(nowItem.hasClass('openItem')){
            	nowItem.removeClass('openItem');
            	/*nowItem.parent().parent().parent().find('.infoContent').find('input').hide();*/
            }else{
            	nowItem.addClass('openItem');
            	/*nowItem.parent().parent().parent().find('.infoContent').find('input').show();*/
            }		     
	});
	
	$('.modelOpen').off('click').on('click',function(){
		var nowItem = $(this);
            if(nowItem.hasClass('openItem')){
            	nowItem.removeClass('openItem');
            	nowItem.parent().parent().parent().parent().find('.itemArea').slideUp();
            }else{
            	nowItem.addClass('openItem');
            	nowItem.parent().parent().parent().parent().find('.itemArea').slideDown();
            }		     
	});
	
}

function flagEven(){
	$('.flag').off('click').on('click',function(){
		$('.flag').attr('src','/resources/images/flow/flag.png');
		$(this).attr('src','/resources/images/flow/flagRed.png');
	});
}

function dataEven(){
	$("#findTime").datepicker({
		language: 'zh',
		dateFormat:'yyyy-MM-dd'
     });
	$("#payTime").datepicker({
		language: 'zh',
		dateFormat:'yyyy-MM-dd'
     });
	$("#orderTime").datepicker({
		language: 'zh',
		dateFormat:'yyyy-MM-dd'
     });
	
}

//发票
function openBill(){
	$('#openBill').off('click').on('click',function(){
		$('#getBillModel').show();
	});
}
//收款信息
function openPrice(){
	$('#showPrice').off('click').on('click',function(){
		$('#getPriceModel').show();
		
	});
}
//客户预算
function budgetPrice(){
	$('#showBudget').off('click').on('click',function(){
		$('#budgetModel').show();
		
	});
}
//实际金额
function realPrice(){
	$('#showRealPrice').off('click').on('click',function(){
		$('#priceModel').show();
	});
}
//实际策划
function showPlot(){
	$('#showPlot').off('click').on('click',function(){
		$('#plotModel').show();
		$('#plotTitle').text('分配策划');
	});
}
//分配监制
function showExecutive(){
	$('#showshowExecutive').off('click').on('click',function(){
		$('#plotModel').show();
		$('#plotTitle').text('分配监制');
	});
}
//提示
function showWarn(){
	$('#showWarn').off('click').on('click',function(){
		$('#warnModel').show();
	});
}

//发票信息
function invoicePrice(){
	$('#invoiceInfo').off('click').on('click',function(){
		$('#invoiceModel').show();
	});
}

//客户转账信息
function cusInfo(){
	$('#showCusPrice').off('click').on('click',function(){
		$('#cusPriceModel').show();
		$('#cusPriceModelTitle').text('客户转账信息');
	});
}

//供应商转账信息
function proInfo(){
	$('#showProPrice').off('click').on('click',function(){
		$('#cusPriceModel').show();
		$('#cusPriceModelTitle').text('供应商转账信息');
	});
}

//协同人清单
function helper(){
	$('#showHelper').off('click').on('click',function(){
		$('#helperModel').show();
	});
}
//版本管理
function controlModel(){
	$('#showControl').off('click').on('click',function(){
		$('#controlModel').show();
	});
}

//客户信息修改
function showCusEdit(){
	$('#showCusEdit').off('click').on('click',function(){
		$('#cusInfoModel').show();
	});
}

//上传文件
function upModel(){
	$('#showUp').off('click').on('click',function(){
		$('#upModel').show();
	});
}

//报错
function showError(){
	$('#showError').off('click').on('click',function(){
		$('#errorModel').show();
	});
}
//完善客户信息
function finishCus(){
	$('#finishCus').off('click').on('click',function(){
		$('#cusModel').show();
	});
}



























