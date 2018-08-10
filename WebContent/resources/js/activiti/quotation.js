// var setTableData = new Array();
 var finalAsc = new Array();
 var lastAsc = new Array();
 var cacheTable = new Array();
 var titleTr = "";
$().ready(function() {
	document.domain = getUrl();
	$(window.parent.document).find('.frame').css('height',$('.pages').height() + 50);
	$('body').off('click').on('click',function(){
		$('ul').slideUp();
		$('.oredrTypeSelect').removeClass('selectColor');
		$('.orderMultSelect ').removeClass('selectColor');
	});
    init();
    setInterval(autoSave,5000);
    
    var pro = $('#projectId').val();
    if(!pro){
    	loadSave(); 
    }
    
   
});

function init(){
	
	findModelNames();
	projectName();
	initMultSelect();
	costFunction.init();
	controlArray.init();
	initTypeItem();
	clickEven();
	getTableInfo();
	var pro = $('#projectName').text();
	if(pro == null || pro == undefined || pro == ''){
		$('#projectName').text('未命名项目');
	}else{
		$('#openFrom').hide();
	}	
	
}

function getTableInfo(){
	var project = $('#projectId').val();
	if(project != null && project !='' && project != undefined ){
	loadData(function(src){
		if(src != null && src !='' && src != undefined ){
			$('#projectId').val(src.projectId);
			$('#quotationId').val(src.quotationId);
			$('#tax').val(src.taxRate);
			$('#projectName').val(src.projectName);
			$('#free').val(src.discount);
		//	$('#dayTime').val(src.updateDate);
			for (var int = 0; int < src.items.length; int++) {
				finalAsc.push(new cTable(src.items[int]));
			}
			sortItem(finalAsc);
			controlArray.createTable();
			dataEven();
			$('.orderItem').attr('data-content','');
		}
	}, getContextPath() + '/quotation/get/'+$('#projectId').val(),null);
	}else{
		dataEven();
	}
}

function clickEven(){
	
	$('.cancle').off('click').on('click',function(){
		$('#errorModel').hide();
	});
	
	$('#surCancleBtn').off('click').on('click',function(){
		$('#errorModel').hide();
	});
	
	$('#toModel').off('click').on('click',function(){
		$(window.parent.parent.parent.document).find('html').scrollTop(0);
		$(window.parent.parent.parent.document).find('body').scrollTop(0);
		$('#productWindow').show();
		$('.modelContent').html('');
	    $('.tap').removeClass('active');
		$('#productLine').addClass('active');
		$('#delProduct').hide();
		loadProdcut(1);
	});
	
	$('#dayNum').on('blur',function(){
		$('#dayNumError').attr('data-content','');
	});
	
	$('#needNum').on('blur',function(){
		$('#needNumError').attr('data-content','');
	});
	
	$('.closeWindow').off('click').on('click',function(){
		$('#errorSame').hide();
	});
	$('.sureDel').off('click').on('click',function(){
		$('#errorModel').hide();
		$('.setTr').html('');
		$('#type').text('');
		$('#type').attr('data-id','');
		$('#projectChilden').text('');
		$('#projectChilden').attr('data-id','');
		$('#projectChilden').attr('data-price','');
		$('#projectChilden').attr('data-full','');
        $('.orderItem').attr('data-content','');
        $('#dayNum').val('');
        $('#needNum').val('');
        $('#setCost').text('');  
        $('#setDir').text('');
        
        finalAsc = new Array();
        $('#localPrice').text(0);
        $('#setFinalCost').text(0);
		/*var nowIndex = $(this).attr('data-id');
		finalAsc =  delArray(finalAsc,parseInt(nowIndex));
		controlArray.createTable();*/
        lastAsc = new Array();
        saveCache();
	});	
	$('.closeModel').off('click').on('click',function(){
		  $('.cusModel').hide();
	});	
	$('.createQuo .createFrom').off('click').on('click',function(){
		$('.sureCheck').off('click').on('click',function(){
			$('#submitCheck').hide();
		});
	    if((controlArray.checkData(1))){
				if(finalAsc[0] != undefined){
					if(checkModelDay(0)){
						 submitCheck();
					}
			    }
				else{
					$(window.parent.parent.parent.document).find('html').scrollTop(0);
		    		$(window.parent.parent.parent.document).find('body').scrollTop(0);
					$('#submitCheck').show();
		    		$('#isSuccess').text('生成报价单');
		    		$('#errorImg').show();
		    		$('#successContent').text('报价单生成失败,请填写表格数据');
				}
		    }
	});	
	
	$('.createModel').off('click').on('click',function(){		
		var setTr = $('.setTr tr').length;
		$('#templateId').val('');
		$('#getModelName').val('');
		if(setTr > 0){
			$(window.parent.parent.parent.document).find('html').scrollTop(0);
    		$(window.parent.parent.parent.document).find('body').scrollTop(0);
			$('#showModelName').show();
			$('#tempNameError').attr('data-content','');
			$('#modelName').val('');
			$('#saveModelName').off('click').on('click',function(){
				var modelName = $('#getModelName').val();
				if(modelName == '' || modelName == undefined || modelName == null){
					$('#getModelName').focus();
					$('#tempNameError').attr('data-content','未填写项目名');
				}else{
					checkProduct();
				}
			});
		}else{
			$(window.parent.parent.parent.document).find('html').scrollTop(0);
    		$(window.parent.parent.parent.document).find('body').scrollTop(0);
				$('#submitCheck').show();
	    		$('#isSuccess').text('保存至项目 ');
	    		$('#errorImg').show();
	    		$('#successContent').text('保存个人模板失败，请填写数据后再进行保存');
	    		$('.sureCheck').off('click').on('click',function(){
	    			$('#submitCheck').hide();
	    		});
		}
	});
	
    $('.createQuo .createFromTable').off('click').on('click',function(){

         var hasId = $('#projectId').val();
         var setTr = $('.setTr tr').length;
         
         $('.closeModel').off('click').on('click',function(){
        	 $('.cusModel').hide();
         });
         
         
         if(checkModelDay(1)){
         if(setTr > 0){
             if(hasId == null || hasId == '' || hasId == undefined){
            	 $('#showProductName').show();
            	 $(window.parent.parent.parent.document).find('html').scrollTop(0);
         		$(window.parent.parent.parent.document).find('body').scrollTop(0);
            	 projectName();
               //  $('#toSetProductName').val($('#projectName').text());
            	 $('#savesProductName').off('click').on('click',function(){
            		 hasId = $('.modelMActive').attr('data-id');
            		 $('#projectId').val(hasId);
                	 if(hasId == null || hasId == '' || hasId == undefined){
                		// $('#projectNameError').attr('data-content','请选择项目');
                	 }else{
                		 submitDateMyDate(0);           		 
                	 }
                 });
            	 $('#cancleSavesProductName').off('click').on('click',function(){
            		 $('#showProductName').hide();
                 });
        	 }else{
        		 $(window.parent.parent.parent.document).find('html').scrollTop(0);
         		$(window.parent.parent.parent.document).find('body').scrollTop(0);
        		 $('#errorSaveModel').show();
        		 $('.SaveModelBtn').off('click').on('click',function(){
        			 submitDateMyDate(1);
        		 });
        	 }
         }else{
        	 $(window.parent.parent.parent.document).find('html').scrollTop(0);
     		$(window.parent.parent.parent.document).find('body').scrollTop(0);
        	 $('#submitCheck').show();
        	 $('#isSuccess').text('保存项目');
     		$('#errorImg').show();
     		$('#successContent').text('不能保存空报价单，请添加数据后再保存至项目');
     		$('.sureCheck').off('click').on('click',function(){
     			$('#submitCheck').hide();
     		});
         }
        }
	 });
 
	$('#checkbox').off('click').on('click',function(){
		if(this.checked){
			allPack();
		}else{
			allPackClear();
		}
	});
	
	$('#openFrom').off('click').on('click',function(){
		$(window.parent.parent.parent.document).find('html').scrollTop(0);
		$(window.parent.parent.parent.document).find('body').scrollTop(0);
	       $('#loadProductModel').show();
	       initLoadProduct();
	       loadProductEven();
	});	
	
}

function allPack(){
	$('#dayNum').attr('readonly','true');
	$('#dayNum').addClass('noBorder');
	$('#dayNum').val('');
	$('#dayT').hide();
	$('#needNum').attr('readonly','true');
	$('#needNum').addClass('noBorder');
	$('#needNum').val('整包');
	$('#needT').hide();
}
function allPackClear(){
	$('#dayNum').removeAttr('readonly');
	$('#dayNum').removeClass('noBorder');
	$('#dayNum').val('');
	$('#dayT').show();
	$('#needNum').removeAttr('readonly');
	$('#needNum').removeClass('noBorder');
	$('#needNum').val('');
	$('#needT').show();
}

function submitCheck(){

	submitDate();
  /*  loadData(function(res){
    	if(res.result){
    		$('#submitCheckBtn').show();
    		$('#setCheck').text('该项目已存在，是否仍然更新并生成报价单？');
    		initCheckBtn();
    	}else{
    		$('#submitCheckBtn').show();
    		$('#setCheck').text('该项目不存在，不能持久化报价单，是否依然继续生成报价单？');
    		$(window.parent.parent.parent.document).find('html').scrollTop(0);
    		$(window.parent.parent.parent.document).find('body').scrollTop(0);
    		initCheckBtn();
    	}
	}, getContextPath() + '/quotation/validate/project-name',$.toJSON({
        projectName : $('#projectName').text()	
	}));*/
	
}

function initCheckBtn(){
	$('.submitCheckBtn').off('click').on('click',function(){
		$('#submitCheckBtn').hide();
		submitDate();
	});
	$('.cancle').off('click').on('click',function(){
		$('#submitCheckBtn').hide();
	});
}

function submitDate(){
	
	var jsonStr=JSON.stringify(finalAsc);
	$('#sitems').val(jsonStr);
	$('#squotationId').val($('#quotationId').val());
	$('#sprojectId').val($('#projectId').val());
	$('#staxRate').val($('#tax').val());
	$('#sdiscount').val($('#free').val());
	$('#ssubTotal').val($('#localPrice').text());
	$('#stotal').val($('#setFinalCost').text());
	$('#sprojectName').val($('#projectName').text());
	$('#toListForm').submit();
	
/*    loadData(function(res){
    	if(res.result){
    		$('#submitCheck').show();
    		$('#isSuccess').text('生成报价单');
    		$('#successContent').text('成功生成报价单,请查看下载文档');
    		$('#quotationId').val(res.msg);
    		$('#errorImg').hide();
    		window.location.href = getContextPath() + "/quotation/export/" + res.msg;
    		$(window.parent.parent.parent.document).find('html').scrollTop(0);
    		$(window.parent.parent.parent.document).find('body').scrollTop(0);
    	}else{
    		$('#submitCheck').show();
    		$('#isSuccess').text('生成报价单');
    		$('#errorImg').show();
    		$('#successContent').text('报价单生成失败,请重试或联系技术部');
    	}
	}, getContextPath() + '/quotation/save',$.toJSON({
		items : finalAsc,
		quotationId : $('#quotationId').val(),
		projectId : $('#projectId').val(),
		taxRate: $('#tax').val(),
		discount:$('#free').val(),
		subTotal:$('#localPrice').text(),
        total: $('#setFinalCost').text(),
        updateDate : '',
        projectName : $('#projectName').text()	
	}));*/
}

var controlArray = {
		init:function(){
			$('#toAdd').off('click').on('click',function(){
				if(controlArray.checkData(0)){
					if(checkSame()){
						    controlArray.createArray();
					}else{
						$(window.parent.parent.parent.document).find('html').scrollTop(0);
			    		$(window.parent.parent.parent.document).find('body').scrollTop(0);
						$('#errorSame').show();
					}
				}
			});
			$('#toClear').off('click').on('click',function(){
				$('#errorModel').show();
			});
		},		
		checkData:function(num){
			var projectName = $('#projectName').text();
		//	var dayTime = $('#dayTime').val();
			var type = $('#type').text();
			var projectChilden = $('#projectChilden').text();
			var dayNum = $('#dayNum').val();
			var needNum = $('#needNum').val();
			$('.orderItem').attr('data-content','');
			if(num == 1){
				if(projectName == null || projectName == "" || projectName == undefined){
					$('#projectNameError').attr('data-content','项目名称未填写');
					$('#projectName').focus();
					return false;
				}
			}else{
						/*if(dayTime == null || dayTime == "" || dayTime == undefined){
							$('#dayTimeError').attr('data-content','日期异常');
							return false;
						}*/
						if(type == null || type == "" || type == undefined){
							$('#typeError').attr('data-content','类别未选择');
							return false;
						}
						if(projectChilden == null || projectChilden == "" || projectChilden == undefined){
							$('#projectChildenError').attr('data-content','项目未选择');
							return false;
						}
						
						var checkPack = $('#projectChilden').attr('data-full');
						if(checkPack != 1 ){
							if(dayNum == null || dayNum == "" || dayNum == undefined){
								$('#dayNumError').attr('data-content','天数未填写');
								return false;
							}
							if(dayNum == 0){
								$('#dayNumError').attr('data-content','天数不正确');
								return false;
							}
							if(!checkRate(dayNum) ||  !isInteger(dayNum)){
								$('#dayNumError').attr('data-content','请输入整数');
								return false;
							}
							if(needNum == null || needNum == "" || needNum == undefined){
								$('#needNumError').attr('data-content','数量未填写');
								return false;
							}
							if(needNum == 0){
								$('#needNumError').attr('data-content','数量未填写');
								return false;
							}
							if(!checkRate(needNum) ||  !isInteger(needNum)){
								$('#needNumError').attr('data-content','请输入整数');
								return false;
							}
						}
						
						if(checkPack == 0 ){
							
						}
						
			}
			return true;
		},
		
		createArray:function(){
			var projectName = $('#projectName').text();
			var dayTime = $('#dayTime').val();
			var type = $('#type').text();
			var typeId = $('#type').attr('data-id');
			var projectParent = $('#projectParent').val();
			var projectParentId = $('#projectParent').attr('data-id');
			var projectChilden = $('#projectChilden').text();
			var projectChildenId = $('#projectChilden').attr('data-id');
			var projectFull =  $('#projectChilden').attr('data-full');
			var dayNum = $('#dayNum').val();
			var needNum = $('#needNum').val();
			var dir = $('#setDir').text();
			var unitPrice = $('#projectChilden').attr('data-price');
			if(projectFull != 1){
				var sum = dayNum * needNum * unitPrice;
			}else{
				dayNum = -1;
				needNum = -1;
				var sum = unitPrice;
			}
			var map = {};
			map['typeId'] = typeId;
			map['typeName'] = type;
			map['itemId'] = projectParentId;
			map['itemName'] = projectParent; 
			map['detailId'] = projectChildenId; 
			map['detailName'] = projectChilden; 
			map['quantity'] = needNum; 
			map['days'] = dayNum; 
			map['unitPrice'] = unitPrice;
			map['sum'] = sum;
			map['description'] = dir;
			map['fullJob'] = projectFull;		
			finalAsc.push(new cTable(map));
			/*finalAsc = orderBy(finalAsc, ['typeId'], 'asc');
			finalAsc = orderByTwo();*/
			//reLoadItem(finalAsc);
			sortItem(finalAsc);
			controlArray.createTable();
			
		},
		
		createTable:function(){
			 titleTr = "";
		     $('.setTr').html('');
		     for (var int = 0; int < finalAsc.length; int++) {
		         $('.setTr').append(createMultOption(finalAsc[int],int));
		     }
		     costFunction.finalCost();
		     costFunction.init();
		 	 $(window.parent.document).find('.frame').css('height',$('.pages').height() + 50);
		}
}

function checkRate(input) {
	 var re = /^[0-9]+.?[0-9]*$/;   //判断字符串是否为数字     //判断正整数 /^[1-9]+[0-9]*]*$/  
	 if (!re.test(input)) {
     return false;
 }
	 return true;
}

var costFunction = {
		init : function() {
			// 初始化视频加载
			this.setCostEven();
			this.updateDay();
			this.delItem();
		},
		updateDay :function(){
			$('.updateDay').blur(function(){
				var changeVue = $(this).val();
				var nowIndex = $(this).attr('data-id');
				if(changeVue <=0 || !checkRate(changeVue) || changeVue > 1000 ||!isInteger(changeVue)){
					$(this).val(finalAsc[nowIndex].days);
				}else{
					var costPrice = $(this).parent().parent().find('.payBaseCost').find('input').val();
					var costNum = $(this).parent().parent().find('.dayTd').find('.updateNum').val();
					var newPrice = (changeVue * costPrice *costNum);
					$(this).parent().parent().find('.cost').text(newPrice);
					finalAsc[nowIndex].days = changeVue;
					finalAsc[nowIndex].sum = newPrice;
					costFunction.finalCost();
				}
			});
			$('.updateNum').blur(function(){
				var nowIndex = $(this).attr('data-id');
				var changeVue = $(this).val();
				if(changeVue <=0 || !checkRate(changeVue) || changeVue > 1000 || !isInteger(changeVue)){
					$(this).val(finalAsc[nowIndex].quantity);
				}else{
					var costPrice = $(this).parent().parent().find('.payBaseCost').find('input').val();
					var costDay = $(this).parent().parent().find('.dayTd').find('.updateDay').val();
					var newPrice = (changeVue * costPrice * costDay);
					$(this).parent().parent().find('.cost').text(newPrice);
					finalAsc[nowIndex].quantity = changeVue;
					finalAsc[nowIndex].sum = newPrice;
					costFunction.finalCost();
				}
			});
			$('.updateBase').blur(function(){
				var nowIndex = $(this).attr('data-id');
				var changeVue = $(this).val();
				if(changeVue <=0 || !checkRate(changeVue) || changeVue > 10000000 || !isInteger(changeVue)){
					$(this).val(finalAsc[nowIndex].unitPrice);
				}else{
					var costPrice = $(this).parent().parent().find('.dayTd').find('.updateNum').val();
					var costDay = $(this).parent().parent().find('.dayTd').find('.updateDay').val();
					if(costPrice == undefined||costDay == undefined){
						finalAsc[nowIndex].unitPrice = changeVue;
						finalAsc[nowIndex].sum = changeVue;
						$(this).parent().parent().find('.cost').text(changeVue);
					}else{
						var newPrice = (changeVue * costPrice * costDay);
						$(this).parent().parent().find('.cost').text(newPrice);
						finalAsc[nowIndex].unitPrice = changeVue;
						finalAsc[nowIndex].sum = newPrice;
					}
					
					costFunction.finalCost();
				}
			});
		},
		delItem:function(){
			$('.delTable div').off('click').on('click',function(){
				var nowIndex = $(this).attr('data-id');
				finalAsc =  delArray(finalAsc,parseInt(nowIndex));
				controlArray.createTable();
				/*$('.sureDel').attr('data-id',nowIndex);
				$('#errorModel').show();*/
			});
		},
		setCostEven : function(){
			$("#tax").blur(function(){
				if($(this).val() > 100 || $(this).val() < 0 ){
					$(this).val(6);
				}
				if(!checkRate($(this).val())||!isInteger($(this).val())){
					$(this).val(6);
				}
				costFunction.finalCost();
			});
			$("#free").blur(function(){
				if(!checkRate($(this).val())||!isInteger($(this).val())){
					$(this).val(0);
				}
				costFunction.finalCost();
			});			
		},
		finalCost : function() {
			 var cost = $('.cost');
			 var finalPrice = 0;
			 var totalPrice = 0;
			 var tax = $('#tax').val();
			 var free = $('#free').val();
			 var floatFinal = 0;
			 for (var i = 0; i < cost.length; i++) {
				 totalPrice = parseInt(totalPrice) + parseInt($(cost[i]).text());
			 }
			 $('#localPrice').text(totalPrice);
			 var setTax = parseFloat(totalPrice*(1 + tax/100) - free);
			 finalPrice =  (Math.round(setTax*100))/100;
			$('#setFinalCost').text(finalPrice);
		},
	}


function dataEven(){
    /*  var hasDay = $('#dayTime').val();
      if(hasDay ==""|| hasDay == null || hasDay == undefined){
    	  var nowDate = new Date();
    	  var   year=nowDate.getFullYear();     
    	  var   month=nowDate.getMonth()+1;     
    	  var   date=nowDate.getDate();      
    	  $('#dayTime').val(year+"-"+month+"-"+date);
      }*/
	/*$("#dayTime").datepicker({
			language: 'zh',
			dateFormat:'yyyy-MM-dd'
	     });*/
}

function initTypeItem(){
	loadData(function(res){
		var src = res;
		var body = $('#orderType');
		body.html('');
		if(src != null && src != undefined){
			for (var i = 0; i < src.length; i++) {
				$('#orderType').append(createType(src[i]))
			}
			initMultType();
		}
	}, getContextPath() + '/quotation/select/type?typeId=',null);

}
//选项目
function projectName(){
	
/*	$('#toSetProductName').bind('input propertychange', function() {
		var theName = $(this).val();
		$('#projectId').val('');
		findAutoInfo(theName);
	});*/
	findAutoInfo('');
/*	$('#toSetProductName').off('click').on('click',function(){
		findAutoInfo('');
	});
	
	$('#toSetProductName').parent().find('img').off('click').on('click',function(){
		findAutoInfo('');
	});*/
	/*$('#toSetProductName').on('blur', function() {
		var setTr = $('.setTr tr').length;
		var ps = $('#productSelect li').length;
		var productId = $('#productId').val();
		if(ps <= 0){
			if(productId == undefined || productId == ''){			
				if(setTr > 0){
					$('#setTableTitle').text('已存在报表信息是否清空');
				  	clearTable();
				}
			}
		}
	});*/
	
}

function clearTable(){
	$('#clearTable').show();
	$('#formTitle').html('报表信息');
	$('.sureClear').off('click').on('click',function(){
		$('.setTr').html('');
		finalAsc = new Array();
		$('#clearTable').hide();
	});
	$('.cancle').off('click').on('click',function(){
		$('#clearTable').hide();
	});
}

function findAutoInfo(userName){
	loadData(function(res){
		console.log(res);
		var res = res;
		var body = $('#productSelect');
		body.html('');
		if(res != null && res != undefined){
			$('#productSelect').show();
			for (var int = 0; int < res.length; int++) {
				   var html =createProduct(res[int]);
				   body.append(html);
			};
			initAutoChoose();
		}else{
			$('#productSelect').hide();
		}
	}, getContextPath() + '/project/synergetic/listByName', $.toJSON({
		projectName : userName
	}));
}

function createProduct(item){ 
	var html = [
	    		'<div class="modelMItem" data-id="'+item.projectId+'">'+item.projectName+'</div>'
	    	].join('');
	    	return html;
}

function initAutoChoose(){

	 $('.modelMItem').off('click').on('click',function(){
			$('.modelMItem').removeClass('modelMActive');
			$(this).addClass('modelMActive');
	});
}


//排序
function reLoadItem(item){
		loadData(function(res){
			finalAsc = res;
			controlArray.createTable();
		}, getContextPath() + '/quotation/order',$.toJSON({
	        items : item
		}));
}

function createType(item){ 
		var html = [
		    		'<li data-id="'+item.typeId+'">'+item.typeName+'</li>'
		    	].join('');
		    	return html;
	}

function initMultType(){
	$('.oredrTypeSelect').off('click').on('click',function(e){
		    $('ul').slideUp();
		    $('.orderSelect').removeClass('selectColor');
		if(!$('.oredrTypeSelect').hasClass('selectColor')){
			$('.oredrTypeSelect').removeClass('selectColor');
			$(this).find('#orderType').slideDown();
			$(this).addClass('selectColor');
		}
		e.stopPropagation();
	});
	$('#orderType li').off('click').on('click',function(e){		
		$('.oredrTypeSelect').removeClass('selectColor');
		$('#type').text($(this).text());
		$('#type').attr("data-id",$(this).attr('data-id'));
		$('#orderType').slideUp();
		var typeId = $(this).attr('data-id');
		$('#projectChilden').text('');
		$('#projectChilden').attr('data-id','');
		$('#projectChilden').attr('data-price','');
		$('#projectChilden').attr('data-full','');
		$('#setDir').text('');
		$('#setCost').text('');
		$('#orderCome').html('');
		$('#typeError').attr('data-content','');
		loadData(function(res){
			var src = res;
			var body = $('#orderCome');
			body.html('');
			if(src != null && src != undefined){
				for (var i = 0; i < src.length; i++) {
					$('#orderCome').append(createDetail(src[i]))
				}
				initMultSelect();
			}
		}, getContextPath() + '/quotation/select/type?typeId='+typeId,null);
		e.stopPropagation();
	});
}

function cTable(map) {	
	//类型id
	this.typeId = map.typeId;
	//类型名称
	this.typeName = map.typeName;
	//项目id
	this.itemId = map.itemId;
	//项目名称
	this.itemName = map.itemName;
	//明细id
	this.detailId = map.detailId;
	//明细名称
	this.detailName = map.detailName;
	//明细id
	this.detailId = map.detailId;
	//类型描述
	this.description = map.description;
	//单价
	this.unitPrice = map.unitPrice;
	//数量
	this.quantity = map.quantity;
	//天数
	this.days = map.days;
	//总额
	this.sum = map.sum;
	
	this.fullJob = map.fullJob;
}

function getTrTitle(item){
	    	if(item.typeId != titleTr){
	    		setTitle = '<tr class="titleTr"><td colSpan="8">'+item.typeName+'</td></tr>'
	    		titleTr = item.typeId;
	    		return setTitle;
	    	}else{
	    		titleTr = item.typeId;
	    		return "";
	    	}
}

function createMultOption(item,index){
    
    var hasTitle = getTrTitle(item);
    var hasRead = "";
    var days = '<input '+hasRead+' data-id='+index+' class="updateDay" value="'+item.days+'">';
    var quantity = '<input '+hasRead+' data-id='+index+' class="updateNum" value="'+item.quantity+'">';
    if(item.days == '-1'){
    	days = '<div  data-id='+index+' class="updateDay">整包</div>';
    	quantity = '<div  data-id='+index+' class="updateDay">整包</div>';
    }
		var html = [
			        ''+hasTitle+'',
		    	    '<tr>',
 		    		'<td>'+item.itemName+'</td>',
 		    		'<td>'+item.detailName+'</td>',
 		    		'<td class="dayTd" >'+days+'</td>',
 		    		'<td class="dayTd" >'+quantity+'</td>',
 		    		'<td class="payCost payBaseCost dayTd"><input class="updateBase" data-id='+index+' style="width:80px" value='+item.unitPrice+'></td>',
 		    		'<td class="cost payCost ">'+item.sum+'</td>',
 		    		'<td class="delTable"><div data-id='+index+'>删除</div></td>',
		    		'</tr>'
		    	].join('');
		    	return html;
	}

function createDetail(item){ 
	var setChildren = "";
	for (var i = 0; i < item.children.length; i++) {
		setChildren += ' <div data-full="'+item.children[i].fullJob+'" data-content="'+item.children[i].description+'" data-price="'+item.children[i].unitPrice+'"  data-id="'+item.children[i].typeId+'">'+item.children[i].typeName+'</div>';
	}
		var html = [
					  ' <li>                                                   ',
				      '   <div class="multSelect">                             ',
				      '        <div class="multTitle">                         ',
				      '            <img src="/resources/images/index/quoIcon.png" >   ',
				      '            <div class="title"  data-id="'+item.typeId+'">'+item.typeName+'</div>               ',
				      '        </div>                                          ',
					  '        <div class="productList" id="productList">      ',
					  '     '+setChildren+'                                    ',
				      '        </div>                                          ',
				      '   </div>                                               ',
				      '</li>                                                   '
		    	].join('');
		    	return html;
}

function initMultSelect(){
	$('.orderMultSelect').off('click').on('click',function(e){
		$('ul').slideUp();
		$('.orderSelect ').removeClass('selectColor');
		if(!$('.orderMultSelect').hasClass('selectColor')){
			$('.orderMultSelect').removeClass('selectColor');
			$(this).find('.setMultSelect').slideDown();
			$(this).addClass('selectColor');
		}
		$('#projectChildenError').attr('data-content','');
		$('#dayNumError').attr('data-content','');
		$('#needNumError').attr('data-content','');
		e.stopPropagation();
	});
	$('.productList div').off('click').on('click',function(e){
		$('.setMultSelect').slideUp();
		$('#projectChilden').text($(this).text());
		$('#projectChilden').attr('data-id',$(this).attr('data-id'));
		$('#projectChilden').attr('data-price',$(this).attr('data-price'));
		$('#projectChilden').attr('data-full',$(this).attr('data-full'));
		var parentText = $(this).parent().parent().find('.multTitle').find('div').text();
		var parentId = $(this).parent().parent().find('.multTitle').find('div').attr('data-id');
		$('#projectParent').val(parentText);
		$('#projectParent').attr('data-id',parentId);
		$('#setDir').html($(this).attr('data-content'));
		$('.orderMultSelect').removeClass('selectColor'); 
		var unitPrice = $('#projectChilden').attr('data-price');
		$('#setCost').text(unitPrice);
	    if($(this).attr('data-full') == 1){
	    	allPack();
	    }else{
	    	allPackClear();
	    }
		e.stopPropagation();
	});
}

/*//删除数组
function delArray(data,n) {　//n表示第几项，从0开始算起。
	//prototype为对象原型，注意这里为对象增加自定义方法的方法。
	　if(n<0)　//如果n<0，则不进行任何操作。
	return data;
	　else
	return data.slice(0,n).concat(data.slice(n+1,data.length));
	
	　concat方法：返回一个新数组，这个新数组是由两个或更多数组组合而成的。
	　这里就是返回this.slice(0,n)/this.slice(n+1,this.length)
	 组成的新数组，这中间，刚好少了第n项。
	　slice方法： 返回一个数组的一段，两个参数，分别指定开始和结束的位置。
	
}*/

//删除数组
function delArray(data,n) {
	
    if(n<0){
    	return data;
	}
	else{
		return data.slice(0,n).concat(data.slice(n+1,data.length));
	}
	
}

//重复验证

function checkSame(){
	if(finalAsc.length>0){
		for (var i = 0; i < finalAsc.length; i++) {
			var thisItem = finalAsc[i].detailId;
			var checkValue =  $('#projectChilden').attr('data-id');
			if(thisItem == checkValue){
				return false;
			}
		}
		return true;
	}
	else{
		return true;
	}
}

/*//分组排序
function orderBy(source, orders, type) {

    if (source instanceof Array && orders instanceof Array && orders.length > 0) {

      var ordersc = orders.concat([]);
      var sorttype = type || 'asc';
      var results = [];
      var totalSum = {};

      function grouporder(source, orders, totalSum) {

        source.sort(function(a, b) {
          var convertA = a[orders[0]];
          var convertB = b[orders[0]];
          if (typeof convertA == 'string' && typeof convertB == 'string') {
            if (sorttype.toUpperCase() == 'ASC') {
              return convertA.localeCompare(convertB);
            } else {
              return convertB.localeCompare(convertA);
            }
          } else {
            if (sorttype.toUpperCase() == 'ASC') {
              return convertA - convertB;
            } else {
              return convertB - convertA;
            }
          }

        })

        var groupmap = new Map();
        source.forEach((item) => {
          if (groupmap.has(item[orders[0]])) {
            groupmap.get(item[orders[0]]).push(item);
          } else {
            groupmap.set(item[orders[0]], []);
            groupmap.get(item[orders[0]]).push(item);
          }
        })

        orders.shift();

        for (let [key, val] of groupmap) {

          totalSum[key] = {};
          totalSum[key].name = key;
          totalSum[key].value = val.length;
          if (orders.length == 0) {
            results = results.concat(val);
          } else {
            totalSum[key].children = {};
            var orderscopy = orders.concat([]);
            grouporder(val, orderscopy, totalSum[key].children);
          }
        }
      }

      grouporder(source, ordersc, totalSum);
      return results;
     返回多种形式 return {
        results: results,
        totalSum: totalSum
      };
    } else {
      return source;
    }
    
  }*/


function isInteger(obj) {
	if(obj%1 === 0){
		return true;
	}else{
		return false;
	}
	return false;
}


function orderByTwo(){
		var item;
		var size=finalAsc.length;
		var items = finalAsc;
		for(var  i=0;i<size;i++){
			if(i<2){
				continue;
			}
			item=items[i];
			//向上找到跟他相同的 typeId+itemId 插入
			for(var j=i-2;j>=0;j--){
				if(item.typeId == (items[j].typeId)){
					if(item.itemId ==(items[j].itemId)){
						items = delArray(items,i);
						items.splice(j+1,0,item);  
						break;
					}			
				}else{
					break;
				}
			}
		}
		return items;
}


//产品线

function productLineEven(){
		
	$('#myModel').off('click').on('click',function(){
		 $('.tap').removeClass('active');
		 $(this).addClass('active');
		 $('#delProduct').show();
		 loadProdcut(0);
	});
	
    $('#productLine').off('click').on('click',function(){
    	$('.tap').removeClass('active');
		$(this).addClass('active');
		$('#delProduct').hide();
		loadProdcut(1);
	});
    
    $('.modelItem').off('click').on('click',function(){
    	$('.modelItem').removeClass('modelActive');
		$(this).addClass('modelActive');
	});
    
    $('#cancleProduct').off('click').on('click',function(){
    	$('#productWindow').hide();
	});
    
    $('#loadProduct').off('click').on('click',function(){
    	var setTr = $('.setTr tr').length;
    	var thisId = $('.modelActive').attr('data-id');
 
    	var thisName = $('.modelActive').text();
    	if(thisId>0){
	    	if(setTr > 0){
	    		$('#clearTable').show();
				$('#setTableTitle').html('报价单编辑中，是否加载并覆盖当前报价单?');
				$('.sureClear').off('click').on('click',function(){
					loadProductTable(thisId);
					$('#clearTable').hide();
				    $('#productWindow').hide();
				    $('#templateId').val(thisId);
				  //  $('#projectName').text(thisName);
				});
				$('.cancle').off('click').on('click',function(){							  
			    	$('#clearTable').hide();
				});
				$('#quotationId').val('');
				
	    	}else{
	    		loadProductTable(thisId);
	        	$('#productWindow').hide();
	        	$('#quotationId').val('');
			//	$('#projectName').text(thisName);
	    	}
    	}

	});
    
    $('#delProduct').off('click').on('click',function(){
    	var thisId = $('.modelActive').attr('data-id');
    	$('#templateId').val(thisId);
    	if(thisId>0){
    		$('#submitCheckBtn').show();
    		$('.submitCheckBtn').off('click').on('click',function(){
    			delProduct(thisId);
    		});
    		
    		$('.cancle').off('click').on('click',function(){
    			$('#submitCheckBtn').hide();
    		});
    		
    	}
    	
	});
    
}

function loadProdcut(num){
	loadData(function(res){
	  var result;
	  var body = $('.modelContent');
	  body.html('');
	  if(num == 0 ){
		  result = res.person;
	  }else{
		  result = res.chanpin;
	  }
	  
	  for (var i = 0; i < result.length; i++) {
		  body.append('<div class="modelItem" data-id="'+result[i].id+'">'+result[i].name+'</div>')
	  }
	  productLineEven();
	}, getContextPath() + '/quotation/temp/list',null);
}

function loadProdcut(num){
	loadData(function(res){
	  var result;
	  var body = $('.modelContent');
	  body.html('');
	  if(num == 0 ){
		  result = res.person;
	  }else{
		  result = res.chanpin;
	  }
	  
	  for (var i = 0; i < result.length; i++) {
		  body.append('<div class="modelItem" data-id="'+result[i].id+'">'+result[i].name+'</div>')
	  }
	  productLineEven();
	}, getContextPath() + '/quotation/temp/list',null);
}

function loadProductTable(id){
	loadData(function(src){
			$('#templateId').val(src.templateId);
			$('#quotationId').val(src.quotationId);
			$('#tax').val(src.taxRate);
			$('#projectName').val(src.projectName);
			$('#free').val(src.discount);
			 finalAsc = src.items;
		/*	 finalAsc = orderBy(finalAsc, ['typeId'], 'asc');
			 finalAsc = orderByTwo()*/;
			 sortItem(finalAsc);
			 controlArray.createTable();
		     $('#productWindow').hide();
		}, getContextPath() + '/quotation/temp/get/'+id,null);
}

function checkProduct(){
	/*loadData(function(res){
		if(res.result){
			saveProduct();			
		}else{
			
		}
	}, getContextPath() + '/quotation/temp/validate-name',$.toJSON({
        templateId : $('#templateId').val(),
        templateName : $('#getModelName').val()	
	}));*/
	var getModelName = $('#getModelName').val();
	var list = $('#tempSelect li');
	if(list.length > 0){
		for (var int = 0; int < list.length; int++) {
			if($(list[int]).text() == getModelName){
				$('#templateId').val($(list[int]).attr('data-id'));
			}
			saveProduct();	
		}
	}else{
		saveProduct();	
	}
}


function saveProduct(){
	
	loadData(function(res){
    	if(res.result){
    		$('#showModelName').hide();
    	}else{
    		$('#submitCheck').show();
    		$('#isSuccess').text('生成模板');
    		$('#errorImg').show();
    		$('#successContent').text(res.err);
    		$('.sureCheck').off('click').on('click',function(){
    			$('#submitCheck').hide();
    		});
    	}
	}, getContextPath() + '/quotation/temp/save',$.toJSON({
		items : finalAsc,
		taxRate: $('#tax').val(),
		discount:$('#free').val(),
		subTotal:$('#localPrice').text(),
        total: $('#setFinalCost').text(),
        templateId : $('#templateId').val(),
        templateName : $('#getModelName').val()	
	}));
	
}

function delProduct(id){
	
	loadData(function(res){
    	if(res.result){
    		$('#submitCheckBtn').hide();
    		$('.modelActive').remove();
    	}else{
    		$('#submitCheck').show();
    		$('#isSuccess').text('删除模板');
    		$('#errorImg').show();
    		$('#successContent').text('模板删除失败,请重试或联系技术部');
    	}
	}, getContextPath() + '/quotation/temp/delete/'+id,'');
	
}


function initLoadProduct(){
		getLoadProduct();
		projectName();
}

function loadProductEven(){
	
	$('#cancleLoadProduct').off('click').on('click',function(){
		$('#loadProductModel').hide();
	});
	
	$('#CheckloadProduct').off('click').on('click',function(){
			$('#projectId').val($('.modelPActive').attr('data-pid'));
			var setTr = $('.setTr tr').length;
	    	var thisId = $('.modelPActive').attr('data-id');
	    	var thisName = $('.modelPActive').text();
	    	if(thisId>0){
		    	if(setTr > 0){
		    		$('#clearTable').show();
					$('#setTableTitle').html('报价单编辑中，是否加载并覆盖当前报价单?');
					$('.sureClear').off('click').on('click',function(){
						finalAsc = new Array();
						getTableInfo();
						$('#clearTable').hide();
						$('#loadProductModel').hide();
						$('#projectName').text(thisName);
					});
					$('.cancle').off('click').on('click',function(){							  
				    	$('#clearTable').hide();
					});
		    	}else{
		    		finalAsc = new Array();
		    		getTableInfo();
		    		$('#loadProductModel').hide();
		    		$('#projectName').text(thisName);
		    	}
	    	}
	});	
	 $('.modelProItem').off('click').on('click',function(){
			$('.modelProItem').removeClass('modelPActive');
			$(this).addClass('modelPActive');
	});
	 	
}

//加载报价单

function getLoadProduct(){
	var body = $('.modelProductContent');
	body.html('');
	loadData(function(res){
		  for (var i = 0; i < res.length; i++) {
			  body.append('<div class="modelProItem" data-id="'+res[i].quotationId+'" data-pid="'+res[i].projectId+' ">'+res[i].projectName+'</div>')
		  }
		  loadProductEven();
	}, getContextPath() + '/quotation/list/synergetic','');
}

function submitDateMyDate(num){
	var proName = $('.modelMActive').text();
	var proId = $('.modelMActive').attr('data-id');
	if(num == 1){
		proName = $('#projectName').text();
	}
	
	if(proId == null || proId == '' || proId == undefined){
		proId = $('#projectId').val();
	}
	
    loadData(function(res){
    	if(res.result){
    		$('#submitCheck').show();
    		$('#isSuccess').text('保存至项目报价单');
    		$('#successContent').text('成功保存为项目报价单');
    		$('#quotationId').val(res.msg);
    		$('#errorImg').hide();
    		$(window.parent.parent.parent.document).find('html').scrollTop(0);
    		$(window.parent.parent.parent.document).find('body').scrollTop(0);
    		$('.sureCheck').off('click').on('click',function(){
    			$('#submitCheck').hide();
    			$('.cusModel').hide();
    		});
    		$('#productSelect').html('');
    	}else{
    		$('#submitCheck').show();
    		$('#isSuccess').text('保存为项目报价单');
    		$('#errorImg').show();
    		$('#successContent').text(res.err);
    		$('.sureCheck').off('click').on('click',function(){
    			$('#submitCheck').hide();
    			$('.cusModel').hide();
    		});
    		$('#productSelect').html('');
    	}
	}, getContextPath() + '/quotation/save',$.toJSON({
		items : finalAsc,
		quotationId : $('#quotationId').val(),
		projectId : proId,
		taxRate: $('#tax').val(),
		discount:$('#free').val(),
		subTotal:$('#localPrice').text(),
        total: $('#setFinalCost').text(),
        updateDate : '',
        projectName : proName
	}));
}

function findModelNames(){
	$('#getModelName').bind('input propertychange', function() {
		var theName = $(this).val();
		$('#templateId').val('');
		findAutoModelInfo(theName);
	});
}

function findAutoModelInfo(userName){
	loadData(function(res){
		var res = res;
		var body = $('#tempSelect');
		body.html('');
		if(res != null && res != undefined){
			$('#tempSelect').show();
			for (var int = 0; int < res.length; int++) {
				   var html =createModelProduct(res[int]);
				   body.append(html);
			};
			initAutoModelChoose();
		}else{
			$('#tempSelect').hide();
		}
	}, getContextPath() + '/quotation/temp/listByName', $.toJSON({
		templateName : userName
	}));
}

function createModelProduct(item){ 
	var html = [
	    		'<li data-id="'+item.templateId+'">'+item.templateName+'</li>'
	    	].join('');
	    	return html;
}

function initAutoModelChoose(){
	$('#tempSelect li').off('click').on('click',function(e){
		 var name = $(this).text();
		 var id = $(this).attr('data-id');
		 $('#getModelName').val(name);
		 $('#templateId').val(id);
		 $('#productSelect').hide();				
	});
}

function checkModelDay(num){
	var dayTd = $('.dayTd');
	for (var int = 0; int < dayTd.length; int++) {
		  var checkPoint = $(dayTd[int]).find('input').val();
		  if(checkPoint =='' || checkPoint ==null || checkPoint== undefined){
			  var checkPoints = $(dayTd[int]).find('.updateDay').text();
			  if(checkPoints =='' || checkPoints ==null || checkPoints== undefined){
				  $('#submitCheck').show();
				  $(window.parent.parent.parent.document).find('html').scrollTop(0);
		    	  $(window.parent.parent.parent.document).find('body').scrollTop(0);
				  if(num == 1){
					  $('#isSuccess').html('保存至项目');
					  $('#successContent').html('报价单保存至项目失败，天数或数量不能为空');
				  }else{
					  $('#isSuccess').html('导出报价单');
					  $('#successContent').html('导出报价单失败，天数或数量不能为空');
				  }
	              $('.sureCheck').off('click').on('click',function(){
	            	  $('#submitCheck').hide();
				  });
				  return false;
			  }
			
		  }
	}
	return true;
}

//新排序
function sortItem(resources){
	var temp;
	for(var i=0;i<resources.length;i++){
		for(var j=i;j<resources.length;j++){
			if(compare(resources[i],resources[j])>0){
				temp=resources[i];
				resources[i]=resources[j];
				resources[j]=temp;
			}
		}
	}
}

function compare(nodeA,nodeB){
	if(nodeA.typeId > nodeB.typeId){
		return 1;
	}else if(nodeA.typeId < nodeB.typeId){
		return -1;
	}else{
		if(nodeA.itemId > nodeB.itemId){
			return 1;
		}else if(nodeA.itemId < nodeB.itemId){
			return -1;
		}
	}
	return 0;
}


//缓存

function autoSave(){
	
	
	var isDiffer = false;
	//表格不同
	if(lastAsc.length > 0){
		if(lastAsc[0].cacheTable.length != finalAsc.length){
			isDiffer = true;
			console.info('不同1');
		}else {
			
			for (var int = 0; int < finalAsc.length; int++) {
				var last = lastAsc[0].cacheTable[int];
				var finalS = finalAsc[int];
				if(last.typeId != finalS.typeId || last.typeName != finalS.typeName ||
				   last.itemId != finalS.itemId || last.itemName != finalS.itemName ||	
				   last.detailId != finalS.detailId || last.detailName != finalS.detailName ||
				   last.description != finalS.description || last.unitPrice != finalS.unitPrice ||	           
				   last.quantity != finalS.quantity || last.days != finalS.days ||
				   last.sum != finalS.sum || last.fullJob != finalS.fullJob 
				){
					isDiffer = true;
					console.info('不同2');
				}
				
     		}
		}
	}
	
	
	var type = $('#type').text();
	var typeId = $('#type').attr('data-id');
	if(typeId == undefined){
		typeId = '';
	}
	
	var projectParentId = $('#projectParentId').attr('data-id');
	if(projectParentId == undefined){
		projectParentId = '';
	}	
	var projectParent = $('#projectParent').val();
	
	var projectChildenId = $('#projectChildenId').attr('data-id');
	if(projectChildenId == undefined){
	   projectChildenId = '';
	}
	var projectChilden = $('#projectChilden').text();
	var dayNum = $('#dayNum').val();
	var needNum = $('#needNum').val();
	var setCost = $('#setCost').text();
	var tax = $('#tax').val();
	var free = $('#free').val();
	var setDir = $('#setDir').text();
	var projectId = $('#projectId').val();
	var quotationId = $('#quotationId').val();
	var projectName = $('#projectName').text();
	var templateId = $('#templateId').val();
	
	
	
	
	if(lastAsc.length > 0){
		if(typeId !=lastAsc[0].typeId || projectParentId != lastAsc[0].projectParentId || dayNum != lastAsc[0].dayNum ||  needNum != lastAsc[0].needNum
				||  tax != lastAsc[0].tax	||  free != lastAsc[0].free	
		){
			isDiffer = true;
			console.info('不同3');
		}
	}
	
	if(lastAsc.length == 0){
		getcacheTable();
		lastAsc = new Array();
		lastAsc.push(new cacheItem(cacheTable,type,typeId,projectParentId,projectParent,projectChildenId,projectChilden,dayNum,needNum,setCost,tax,free,setDir,quotationId,projectId,projectName,templateId));
		saveCache();
	}else if(isDiffer){
		getcacheTable();
		lastAsc = new Array();
		lastAsc.push(new cacheItem(cacheTable,type,typeId,projectParentId,projectParent,projectChildenId,projectChilden,dayNum,needNum,setCost,tax,free,setDir,quotationId,projectId,projectName,templateId));
		saveCache();
	}
	
}

function saveCache(){
	
    var jsonStr = JSON.stringify(lastAsc);
	
	loadData(function(item){
		
		console.log('缓存成功');
		
	}, getContextPath() + '/cache/save', $.toJSON({
		type:0,
		dataContent:jsonStr
	}));
		
}

function getcacheTable(){
	
	cacheTable = new Array();
	
	for (var int = 0; int < finalAsc.length; int++) {

		var map = {};
		map['typeId'] = finalAsc[int].typeId;
		map['typeName'] = finalAsc[int].typeName;
		map['itemId'] = finalAsc[int].itemId;
		map['itemName'] = finalAsc[int].itemName; 
		map['detailId'] = finalAsc[int].detailId; 
		map['detailName'] = finalAsc[int].detailName; 
		map['quantity'] = finalAsc[int].quantity; 
		map['days'] = finalAsc[int].days; 
		map['unitPrice'] = finalAsc[int].unitPrice;
		map['sum'] = finalAsc[int].sum;
		map['description'] = finalAsc[int].description;
		map['fullJob'] = finalAsc[int].fullJob;		
		cacheTable.push(new cTable(map));
		
	}
	
}

function cacheItem(cacheTable,type,typeId,projectParentId,projectParent,projectChildenId,projectChilden,dayNum,needNum,setCost,tax,free,setDir,quotationId,projectId,projectName,templateId){
	
	this.cacheTable = cacheTable;
	this.type = type;
	this.typeId = typeId;
	this.projectParentId = projectParentId;
	this.projectParent = projectParent;
	this.projectChildenId = projectChildenId;
	this.projectChilden = projectChilden;
	this.dayNum = dayNum;
	this.needNum = needNum;
	this.setCost = setCost;
	this.tax = tax;
	this.free = free;
	this.setDir = setDir
	this.quotationId = quotationId;
	this.projectId = projectId;
	this.projectName = projectName;
	this.templateId = templateId;
	
}

function loadSave(){
	//开关	
		loadData(function(res){		
			if(res.result){
				var itemRes = jQuery.parseJSON(res.msg);
				$('#type').text(itemRes[0].type);
				$('#type').attr('data-id',itemRes[0].typeId);
                $('#projectParent').attr('data-id',itemRes[0].projectParentId);
				$('#projectParent').val(itemRes[0].projectParent);
				$('#projectChilden').attr('data-id',itemRes[0].projectChildenId);
                $('#projectChilden').text(itemRes[0].projectChilden);
                $('#dayNum').val(itemRes[0].dayNum);
                $('#needNum').val(itemRes[0].needNum);
                $('#setCost').text(itemRes[0].setCost);
				$('#tax').val(itemRes[0].tax);
				$('#free').val(itemRes[0].free);
				$('#setDir').text(itemRes[0].setDir);
				$('#quotationId').val(itemRes[0].quotationId);
				$('#projectId').val(itemRes[0].projectId);
				$('#projectName').text(itemRes[0].projectName);
				$('#templateId').val(itemRes[0].templateId);
			
				if(itemRes[0].typeId !=''){		
					console.info('嘿'+itemRes[0].typeId);
					loadData(function(res){
						var src = res;
						var body = $('#orderCome');
						body.html('');
						if(src != null && src != undefined){
							for (var i = 0; i < src.length; i++) {
								$('#orderCome').append(createDetail(src[i]))
							}
							initMultSelect();
						}
					}, getContextPath() + '/quotation/select/type?typeId='+itemRes[0].typeId,null);
					
				}
								
				if(itemRes[0].cacheTable.length > 0){
					finalAsc = itemRes[0].cacheTable;
					//sortItem(finalAsc);
					controlArray.createTable();
				}	
				
			}
			$(window.parent.document).find('.frame').css('height',$('.pages').height() + 50);
		}, getContextPath() + '/cache/get',$.toJSON({
			type:0
		}));	
}

