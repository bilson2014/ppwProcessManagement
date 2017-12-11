// var setTableData = new Array();
 var finalAsc = new Array();
 var titleTr = "";
$().ready(function() {
	//document.domain = getUrl();
	$('body').off('click').on('click',function(){
		$('ul').slideUp();
		$('.oredrTypeSelect').removeClass('selectColor');
		$('.oredrMultSelect').removeClass('selectColor');
	});
     init();     
/*     $("#suppliers").table2excel({
         exclude: ".noExl",
         name: "Worksheet Name",
         filename: "xxxxx表" ,
         exclude_img: true,
         exclude_links: true,
         exclude_inputs: true
     });*/
});

function init(){
	dataEven();
	initMultSelect();
	costFunction.init();
	controlArray.init();
	initTypeItem();
	clickEven();
	getTableInfo();
}

function getTableInfo(){
	loadData(function(res){
		if(src != null && src !='' && src != undefined ){
			var src = res;
			$('#projectId').val(src.projectId);
			$('#quotationId').val(src.quotationId);
			$('#tax').val(src.taxRate);
			$('#projectName').val(src.projectName);
			$('#dayTime').val(src.updateDate);
			finalAsc.push(new cTable(src.items[0]));
			controlArray.createTable();
		}
	}, getContextPath() + '/quotation/get/'+$('#projectId').val(),null);
}

function clickEven(){
	
	$('.cancle').off('click').on('click',function(){
		$('#errorModel').hide();
	});
	
	$('.closeWindow').off('click').on('click',function(){
		$('#errorSame').hide();
	});
		
	$('.sureDel').off('click').on('click',function(){
		$('#errorModel').hide();
		var nowIndex = $(this).attr('data-id');
		finalAsc =  delArray(finalAsc,parseInt(nowIndex));
		controlArray.createTable();
	});	
	$('.closeModel').off('click').on('click',function(){
		  $('.cusModel').hide();
	});	
	$('.createQuo div').off('click').on('click',function(){
		$('.sureCheck').off('click').on('click',function(){
			$('#submitCheck').hide();
		});
		if(finalAsc[0] != undefined){
			console.info(finalAsc);
			submitDate();
		}
	});
	$('#dayNum,#needNum').blur(function(){
		var changeVue = $(this).val();
		if(changeVue <=0 || !checkRate(changeVue) || changeVue > 1000 || !isInteger(changeVue)){
			$(this).val('0');
		}
	});
	
	
}
function submitDate(){
    loadData(function(res){
    	if(res.result){
    		$('#submitCheck').show();
    		$('#isSuccess').text('生成成功');
    		$('#successContent').text('请确认文档');
    		$('#quotationId').val(res.msg);
    		window.location.href = getContextPath() + "/quotation/export/" + res.msg;
    		$(window.parent.parent.parent.document).find('html').scrollTop(0);
    		$(window.parent.parent.parent.document).find('body').scrollTop(0);
    	}else{
    		$('#submitCheck').show();
    		$('#isSuccess').text('生成失败');
    		$('#successContent').text('请确认网络情况');
    	}
	}, getContextPath() + '/quotation/save',$.toJSON({
		items : finalAsc,
		quotationId : $('#quotationId').val(),
		projectId : $('#projectId').val(),
		taxRate: $('#tax').val(),
		discount:$('#free').val(),
		subTotal:$('#localPrice').text(),
        total: $('#setFinalCost').text(),
        updateDate : $('#dayTime').val(),
        projectName : $('#projectName').val()	
	}));
	
}

var controlArray = {
		init:function(){
			$('#toAdd').off('click').on('click',function(){
				if(controlArray.checkData()){
					
					if(checkSame()){
						    controlArray.createArray();
							$('#projectParent').val('');
							$('#projectChilden').text('');
							$('#dayNum').val('');
							$('#needNum').val('');
							$('#setDir').text('');
							$('.orderItem').attr('data-content','');
					}else{
						$('#errorSame').show();
					}
				}
			});
			$('#toClear').off('click').on('click',function(){
				$('#projectParent').val('');
				$('#projectChilden').text('');
				$('#dayNum').val('');
				$('#needNum').val('');
				$('#setDir').text('');
				$('#type').text('');
				$('#orderCome').html('');
				$('.orderItem').attr('data-content','');
			});
		},		
		checkData:function(){
			var projectName = $('#projectName').val();
			var dayTime = $('#dayTime').val();
			var type = $('#type').text();
			var projectChilden = $('#projectChilden').text();
			var dayNum = $('#dayNum').val();
			var needNum = $('#needNum').val();
			$('.orderItem').attr('data-content','');
			if(projectName == null || projectName == "" || projectName == undefined){
				$('#projectNameError').attr('data-content','项目名称未填写');
				return false;
			}
			if(dayTime == null || dayTime == "" || dayTime == undefined){
				$('#dayTimeError').attr('data-content','日期异常');
				return false;
			}
			if(type == null || type == "" || type == undefined){
				$('#typeError').attr('data-content','类别未选择');
				return false;
			}
			if(projectChilden == null || projectChilden == "" || projectChilden == undefined){
				$('#projectChildenError').attr('data-content','项目未选择');
				return false;
			}
			if(dayNum == null || dayNum == "" || dayNum == undefined){
				$('#dayNumError').attr('data-content','天数未填写');
				return false;
			}
			if(needNum == null || needNum == "" || needNum == undefined){
				$('#needNumError').attr('data-content','数量未填写');
				return false;
			}
			return true;
		},
		
		createArray:function(){
			var projectName = $('#projectName').val();
			var dayTime = $('#dayTime').val();
			var type = $('#type').text();
			var typeId = $('#type').attr('data-id');
			var projectParent = $('#projectParent').val();
			var projectParentId = $('#projectParent').attr('data-id');
			var projectChilden = $('#projectChilden').text();
			var projectChildenId = $('#projectChilden').attr('data-id');
			var dayNum = $('#dayNum').val();
			var needNum = $('#needNum').val();
			var dir = $('#setDir').text();
			var unitPrice = $('#projectChilden').attr('data-price');
			var sum = dayNum * needNum * unitPrice;
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
			finalAsc.push(new cTable(map));
			finalAsc = orderBy(finalAsc, ['typeId'], 'asc');
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
					var costPrice = $(this).parent().parent().find('.payBaseCost').text();
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
					var costPrice = $(this).parent().parent().find('.payBaseCost').text();
					var costDay = $(this).parent().parent().find('.dayTd').find('.updateDay').val();
					var newPrice = (changeVue * costPrice * costDay);
					$(this).parent().parent().find('.cost').text(newPrice);
					finalAsc[nowIndex].quantity = changeVue;
					finalAsc[nowIndex].sum = newPrice;
					costFunction.finalCost();
				}
			});
		},
		delItem:function(){
			$('.delTable div').off('click').on('click',function(){
				var nowIndex = $(this).attr('data-id');
				$('.sureDel').attr('data-id',nowIndex);
				$('#errorModel').show();
			});
		},
		setCostEven : function(){
			$("#tax").blur(function(){
				if($(this).val() > 100 || $(this).val() < 0 ){
					$(this).val(6);
				}
				if(!checkRate($(this).val())||!isInteger(changeVue)){
					$(this).val(6);
				}
				costFunction.finalCost();
			});
			$("#free").blur(function(){
				if(!checkRate($(this).val())){
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
      var hasDay = $('#dayTime').val();
      if(hasDay ==""|| hasDay == null || hasDay == undefined){
    	  var nowDate = new Date();
    	  var   year=nowDate.getFullYear();     
    	  var   month=nowDate.getMonth()+1;     
    	  var   date=nowDate.getDate();      
    	  $('#dayTime').val(year+"-"+month+"-"+date);
      }
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

function createType(item){ 
		var html = [
		    		'<li data-id="'+item.typeId+'">'+item.typeName+'</li>'
		    	].join('');
		    	return html;
	}

function initMultType(){
	$('.oredrTypeSelect').off('click').on('click',function(e){
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
		var html = [
			        ''+hasTitle+'',
		    	    '<tr>',
 		    		'<td>'+item.itemName+'</td>',
 		    		'<td>'+item.detailName+'</td>',
 		    		'<td>'+item.description+'</td>',
 		    		'<td class="dayTd"><input data-id='+index+' class="updateDay" value="'+item.days+'"></td>',
 		    		'<td class="dayTd"><input data-id='+index+' class="updateNum" value="'+item.quantity+'"></td>',
 		    		'<td class="payCost payBaseCost">'+item.unitPrice+'</td>',
 		    		'<td class="cost payCost">'+item.sum+'</td>',
 		    		'<td class="delTable"><div data-id='+index+'>删除</div></td>',
		    		'</tr>'
		    	].join('');
		    	return html;
	}

function createDetail(item){ 
	var setChildren = "";
	for (var i = 0; i < item.children.length; i++) {
		setChildren += ' <div data-content="'+item.children[i].description+'" data-price="'+item.children[i].unitPrice+'"  data-id="'+item.children[i].typeId+'">'+item.children[i].typeName+'</div>';
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
		if(!$('.orderMultSelect').hasClass('selectColor')){
			$('.orderMultSelect').removeClass('selectColor');
			$(this).find('.setMultSelect').slideDown();
			$(this).addClass('selectColor');
		}
		e.stopPropagation();
	});
	$('.productList div').off('click').on('click',function(e){
		$('.setMultSelect').slideUp();
		$('#projectChilden').text($(this).text());
		$('#projectChilden').attr('data-id',$(this).attr('data-id'));
		$('#projectChilden').attr('data-price',$(this).attr('data-price'));
		var parentText = $(this).parent().parent().find('.multTitle').find('div').text();
		var parentId = $(this).parent().parent().find('.multTitle').find('div').attr('data-id');
		$('#projectParent').val(parentText);
		$('#projectParent').attr('data-id',parentId);
		$('#setDir').text($(this).attr('data-content'));
		$('.orderMultSelect').removeClass('selectColor'); 
		e.stopPropagation();
	});
}

//删除数组
function delArray(data,n) {　//n表示第几项，从0开始算起。
	//prototype为对象原型，注意这里为对象增加自定义方法的方法。
	　if(n<0)　//如果n<0，则不进行任何操作。
	return data;
	　else
	return data.slice(0,n).concat(data.slice(n+1,data.length));
	/*
	　concat方法：返回一个新数组，这个新数组是由两个或更多数组组合而成的。
	　这里就是返回this.slice(0,n)/this.slice(n+1,this.length)
	 组成的新数组，这中间，刚好少了第n项。
	　slice方法： 返回一个数组的一段，两个参数，分别指定开始和结束的位置。
	*/
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
			return true;
		}
	}
	else{
		return true;
	}
}

//分组排序
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
    /* 返回多种形式 return {
        results: results,
        totalSum: totalSum
      };*/
    } else {
      return source;
    }
    
  }


function isInteger(obj) {
	if(obj%1 === 0){
		return true;
	}else{
		return false;
	}
	return false;
}
