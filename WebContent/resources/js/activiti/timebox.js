var count=0;
var time,matter,whenval, times,chengnums;
var chengnums='';
var chengnum=[];
var jiafei='';
var textval='';
var yy;
var mm;
var dd;
var blackbox;
var chenggame = '';
var season;
var num;
var clickNumber = 0;
var arrobject = [];
var myxuan = new Array();
var cacheData = new Array();
var boxData = new Array();
var myMap = new Map();

$().ready(function() {
	document.domain = getUrl();
	$(window.parent.document).find('.frame').css('height',$('.pages').height() + 50);
    var date = new Date();
    var d = date.getDate();
    var m = date.getMonth();
    var y = date.getFullYear();
    m=m+1;
    
    $('#updateDate').val(y+'-'+m+'-'+d);
    $('#calendar').fullCalendar({
        header: {
            left: 'prev,next today',
            center: 'title',
            right: 'month,basicWeek,basicDay'
        },
        editable: true,
    });
    
    leftbtn();
    sun();
    initSelect();
    getday();
    bestthings();
    delselc();
    dbmatter();
    timebook();
    colorthing();
    loadProductEven();
    inthings();
     
    
  //打开项目
    $('#openFrom').on('click',function(){
       $('#loadProductModel').show();     
       	var body = $('.modelProductContent');
       	body.html('');
       	loadData(function(res){
       		for (var i = 0; i < res.length; i++) {
       			 body.append('<div class="modelProItem" projectname="'+res[i].projectName+'" data-id="'+res[i].quotationId+'" data-pid="'+res[i].projectId+' ">'+res[i].projectName+'</div>')
       		 }
       		 loadProductEven();
       	}, getContextPath() + '/schedule/list/synergetic','');
        loadProductEven();
    });	
    
    	setInterval(getCacheValue,cacheTime);
    	 var pro = $('#projectId').val();
    	    if(!pro){
    	    	loadCache(); 
    	    }
    	       	    
});

function getMap(){	
	for (var value of myMap.values()) {
		  	map['jobContent'] = value.jobContent;
			map['start'] = value.start;
			map['end'] = value.end;
			map['day'] = value.day; 
			boxData.push(map)
		}
}

// 初始化获取地址
function inthings(){
	if  ($('#projectNames').val()){
		$('#projectName').text($('#projectNames').val());
	}
	// 接受URL地址参数
    function getQueryString(name) {                                      
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");     
        var r = window.location.search.substr(1).match(reg);
        if (r != null) return unescape(r[2]); return null;						
    } 
    var urls=getQueryString('projectId');
    if ( urls== null || urls== "" || urls== undefined){
    	$('#openFrom').show();
    }else {
    	loadProductTable(urls);
    	$('#openFrom').hide();
    }  
}
function loadProductTable(id){
	
	getBoxInfo(0,id); 
	
/*	loadData(function(src){
		if (src.scheduleId!=null && src.scheduleId!="" && src.scheduleId!=undefined){
			$('#scheduleId').val(src.scheduleId);
			$('#projectId').val(src.projectId);
			$('#projectNames').val(src.projectName);
			$('#updateDate').val(src.updateDate);
			$('#projectName').text(src.projectName);	
			arrobject=src.items;
			var xiao=JSON.stringify(src.items);
			xiao = xiao.replace(/}{/g, '},{');
			var objectbox=src.items;
			var tibo=$('.fc-day');
			tibo.each(function(){
				var countext =  $(this).attr('data-date');
				for(var k in objectbox){
					if (countext==objectbox[k].start){
						if (!$(this).hasClass('fc-other-month')){
							$(this).find('textarea').attr('style',"border: none; resize: none;background: transparent;box-shadow: none;");
							$(this).find('textarea').val(objectbox[k].jobContent);
							$(this).find('textarea').text(objectbox[k].jobContent);	
						}					
					}					
				}
			})
			boxData = new Array(); 
			boxData = src.items;
		}else {
			$('#projectId').val(id);
			$('#projectNames').val($('#projectName').text());	
		}	
	}, getContextPath() + '/schedule/get/'+id,null);*/
}

// 日期样式添加事件
function colorthing(){
	var tibo=$('.fc-day');
	tibo.each(function(){
		if ( $(this).hasClass('fc-state-highlight')){
			$(this).find('.fc-day-number').attr('style',"color:#fff !important;background:#FE5453 !important;border-radius:50% !important;width: 30px;height: 30px;");
		}		
	});
}

// 双击事件和失去焦点事件文本框的显示
function dbmatter(){
	$('.orderSelect').click(function(){
		$(this).parent().parent().addClass('wolf');
		$(this).parent().parent().parent().siblings().removeClass('wolf');
		$(this).parent().parent().parent().parent().parent().find('.fc-day').removeClass('wolf');
		$(this).parent().parent().parent().addClass('wolf');
		season=$(this).parent().parent().find(".matter").val();
		$(this).parent().parent().parent().siblings().removeClass('cheng');
		$(this).parent().parent().parent().parent().parent().find('.fc-day').removeClass('cheng');
		$(this).parent().parent().parent().addClass('cheng');
		$(this).parent().parent().find(".matter").attr('style', 'display: block;');

		$(this).parent().parent().parent().focus();	
		$(this).parent().parent().parent().siblings().find('.fc-day').blur();
		$(this).parent().parent().parent().parent().find('.fc-day').blur();
		$(this).parent().parent().parent().attr('style', 'background: #F6F9F9;');
		$(this).parent().parent().parent().siblings().attr('style', 'background: ;');
		$(this).parent().parent().parent().parent().siblings().find('.fc-day').attr('style', 'background: ;');	
		$(this).parent().parent().find(".matter").attr('style', 'display: block;');
   	    $(this).parent().parent().find(".matter").attr('style', 'border: none; resize: none;background: transparent;box-shadow: none;');
   	    
		// 获取焦点
		//	$('.matter').blur();
		//	$(this).parent().parent().find('.matter').focus();	
		//	$(this).parent().parent().parent().parent().siblings().find('.matter').blur();
		//  $(this).parent().parent().find(".matter").focus();
	   	//  $(this).parent().parent().parent().parent().siblings().find('.matter').blur();
   	    
	});
	
	 $(".matter").blur(function(){	 
		 
		 	var matter= $(this).val();
		 	
			if(matter== null || matter == "" || matter == undefined){
				$(this).attr('style', 'display: none;');
			}else{
				$(this).attr('style', 'border: none; resize: none;background: transparent;box-shadow: none;');
			}
			
			$('tbody .fc-other-month .matter').attr('style','display: none;');
		
			var kous=$(this).val().trim();
			var nowtimes=$(this).parent().parent().attr('data-date');

//			if(boxData.length > 0){
				var isDiffer = true;
			
				
				for (var int = 0; int < boxData.length; int++) {				
					if(nowtimes == boxData[int].start){
						if(kous == ""||kous == null||kous == undefined){
							boxData.splice(int, 1);
//							isDiffer = false;
//							break;
						}else{
							boxData[int].jobContent = kous;
						}
						isDiffer = false;
						break;
					} 
				}
				if(isDiffer){
					
					if(kous != ""&&kous != null&&kous != undefined){
						var map = {};
						map['jobContent'] = kous;
						map['start'] = nowtimes;
						map['end'] = '';
						map['day'] = ''; 
						boxData.push(map);
					}
				}
			/*}else{
				var map = {};
				map['jobContent'] = kous;
				map['start'] = nowtimes;
				map['end'] = '';
				map['day'] = ''; 
				boxData.push(map);
			}*/
			
		
    	
	 });
	 $('.xuan .boxs').click(function(){
		 $(this).parent().addClass('wolf');	 
	 })
	 $('.fc-day .xuan').removeClass('wolf'); 	
	 $('.pages').click(function(){
		 $(this).find('.boxs .city-select').remove('.city-select');
		 $(this).find('.orderSelect').removeClass('selectColor');
		 var matter = $('.matter');
		 for (var int = 0; int < matter.length; int++) {
			 if($(matter[int]).val()==""){
				 $(matter[int]).attr('style','display:none');
			 }else{
				 $(matter[int]).attr('style','border: none; resize: none;background: transparent;box-shadow: none');
			 }
		}
	 })
	 $('.fc-day').click(function(){
		$(this).parent().addClass('wolf');	
		if (!$(this).hasClass('cheng')){
			$(this).parent().find('.boxs .city-select').remove('.city-select');
			$(this).parent().siblings().find('.boxs .city-select').remove('.city-select');
		}			
		$(this).attr('style', 'background: #F6F9F9;');
		$(this).siblings().attr('style', 'background: ;');
		$(this).parent().siblings().find('.fc-day').attr('style', 'background: ;');
		$(this).siblings().removeClass('cheng');
		$(this).parent().parent().find('.fc-day').removeClass('cheng');
		$(this).addClass('cheng');
		if (!$(this).find('.xuan').hasClass('wolf')&&$('.fc-day .xuan').hasClass('wolf')){
			$(this).siblings().find('.xuan').removeClass('wolf');
			$(this).parent().parent().find('.fc-day .xuan').removeClass('wolf');
			$(this).addClass('onlyul');
			setTimeout($(this).removeClass('onlyul'),1000);
			// 移除box的 内容
			$('.boxs .city-select').remove('.city-select');
			$('.orderSelect').removeClass('selectColor');
		}
		else if ($('.fc-day').hasClass('wolf')||$('.fc-day .xuan').hasClass('wolf')){
		//	$(this).find('.matter').blur();					
			$(this).removeClass('cheng');	
			$(this).siblings().removeClass('cheng');
			$(this).parent().parent().find('.fc-day').removeClass('cheng');
			$(this).addClass('cheng');
			$(this).find(".matter").attr('style', 'display: block;');			
		//	$(this).find('.matter').focus();			
			$(this).removeClass('wolf');				
			$(this).siblings().removeClass('wolf');
			$(this).parent().parent().find('.fc-day').removeClass('wolf');
			$(this).addClass('onlyul');
			$(this).removeClass('onlyul');
		//	setTimeout($(this).removeClass('onlyul'),1000);	
		}else {
			$(this).siblings().removeClass('cheng');
			$(this).parent().parent().find('.fc-day').removeClass('cheng');
			$(this).addClass('cheng');			
		//	$('.matter').blur();
			if($('.boxs').html()){
				$(this).siblings().find('.boxs .city-select').remove('.city-select');
				$(this).parent().siblings().find('.boxs .city-select').remove('.city-select');
				$(this).attr('style', 'background: ;');		
			}else {
				$(this).attr('style', 'background: #F6F9F9;');
		//		$('.matter').blur();
				$(this).siblings().attr('style', 'background: ;');
				$(this).parent().siblings().find('.fc-day').attr('style', 'background: ;');		 
		//		$(this).parent().siblings().find('.matter').blur();
				// 排除其余月份的 显示textarea框
				if ($(this).hasClass('fc-other-month')){
					$(this).find(".matter").attr('style', 'display: none;');
				}else {
					$(this).find(".matter").attr('style', 'display: block;');
					$(this).find(".matter").focus();
			   	    $(this).parent().parent().siblings().find('.matter').blur();
				}	 
			}
		}	
	 });	 
}
/*// 回显功能
function timebook(){
	var timebook;
	chengnums+=chengnum;
	chengnums = chengnums.replace(/}{/g, '},{');
	var chengnumsw='';
	chengnumsw='['+chengnums+']';	
	timebook=JSON.parse(chengnumsw.replace(/\n/g,'\\\\n'));
	chenggame=timebook;
	var timebookss;
	timebookss=JSON.stringify(timebook);
	
	var timebook;
	
	var oldInfo = new Array();
	chengnums+=chengnum;
	if(chengnums != ''){
		chengnums = '['+chengnums+']'
		chengnums = chengnums.replace(/}{/g, '},{');
		oldInfo =  jQuery.parseJSON(chengnums);
	}
	
	var newInfo = new Array();
	if(chengnum != ''){
		 chengnum = chengnum.replace(/}{/g, '},{');
		 newInfo = jQuery.parseJSON('['+chengnum+']');
	}
	
	for (var int = 0; int < newInfo.length; int++) {
		
		if(oldInfo.length == 0){
			oldInfo.push(newInfo[int])
		}else{
			for (var j = 0; j < oldInfo.length; j++) {
				if(oldInfo[j].start == newInfo[int].start){
					oldInfo[j].jobContent = newInfo[int].jobContent;
				}else{
					oldInfo.push(newInfo[int])
				}
			}
		}
	}
    
	timebook = oldInfo;
	var timebookss;
	timebookss=JSON.stringify(timebook);
		
// 完整的对象
	// 字符串的 timebook 去重
	var allArr = [];// 新数组
	for(var i=0;i<timebook.length;i++){
		var flag = true;
		for(var j=0;j<allArr.length;j++){
			if(timebook[i].start == allArr[j].start){
				flag = false;
			}
		}
		if(flag){
			allArr.push(timebook[i]);
		}
	}
	// 此后的 timebook 换成allAll
	timebook=allArr;	
	// timebook与 arrobject 数据合并修改
	for(var i in timebook){
		for (var j in arrobject){
			if (timebook[i].start==arrobject[j].start){				
				// 对项目原有的数据与修改的数据进行比较得出比较的 修改新的数据
				arrobject[j].jobContent=timebook[i].jobContent;
			}			
		}			
		if(arrobject==null||arrobject==undefined||arrobject==''){
			arrobject=[];			
		}
		arrobject.push(timebook[i]);
	}
	var allArr = [];// 新数组
	for(var i=0;i<arrobject.length;i++){
		var flag = true;
		for(var j=0;j<allArr.length;j++){
		if(arrobject[i].start == allArr[j].start){
			flag = false;
		}
	}
		if(flag){
			allArr.push(arrobject[i]);
		}
	}
	arrobject=allArr;
// 当导出的时候 取该处的值 导出即可
	var chaunqi=JSON.stringify(arrobject);
	chaunqi = chaunqi.replace(/}{/g, '},{');
	chaunqi = chaunqi.replace(/\\n/g,'111');	
	chaunqi = chaunqi.replace(/\\111/g,'/n');
	chaunqi = chaunqi.replace(/111/g,'/n');
	for (var i=0;i<chaunqi.length;i++){
		chaunqi = chaunqi.replace('/n','\\n');
	}
	$('#pumpum').val(chaunqi);
	var keys=[];
	var value=[];
	var tibo=$('.fc-day');
	tibo.each(function(){
		var countext =  $(this).attr('data-date');
		for (var k in timebook){
			keys.push(k);
			value.push(timebook[k]);
			if (countext==timebook[k].start){
				$(this).find('textarea').attr('style',"display: block;");
				$(this).find('textarea').val(timebook[k].jobContent);
				$(this).find('textarea').text(timebook[k].jobContent);				
				var meiyi=timebook[k].jobContent;				
				meiyi=meiyi.replace(/n/g,'\\\n');// 加了\
				meiyi=meiyi.replace(/\\/g,'  ');
				$(this).find('textarea').val(meiyi);
				$(this).find('textarea').text(meiyi);
			}
		}		
	});	
	// 加载报价单的时候
	$('.closeModel').on('click',function(){
		  $('.cusModel').hide();
	});		
}*/




// 回显功能New
function timebook(){

	var tibo=$('.fc-day');
	tibo.each(function(){
		var countext =  $(this).attr('data-date');
		for (var k in boxData){
			var s = countext;
			var a = boxData[k].start;
			if (countext==boxData[k].start){
				$(this).find('textarea').attr('style',"display: block;");
				$(this).find('textarea').val(boxData[k].jobContent);
				$(this).find('textarea').text(boxData[k].jobContent);				
				var meiyi=boxData[k].jobContent;				
				meiyi=meiyi.replace(/n/g,'\\\n');// 加了\
				meiyi=meiyi.replace(/\\/g,'');
				$(this).find('textarea').val(meiyi);
				$(this).find('textarea').text(meiyi);				
				 var thisValue = boxData[k].jobContent;
					 if(thisValue!=''&&thisValue!=null&&thisValue!=undefined){
							$(this).find('textarea').attr('style', 'border: none; resize: none;background: transparent;box-shadow: none;');
					 }else{
						    $(this).find('textarea').attr('style', 'display: none;');
					 }
			}
		}		
	});	
	// 加载报价单的时候
	$('.closeModel').on('click',function(){
		  $('.cusModel').hide();
	});		

}

// 选中项目的 回显
function getBoxInfo(getDay,secId){
	var tibo=$('.fc-day');
	tibo.each(function(){
		$(this).find('textarea').val('');
		$(this).find('textarea').text('');
		$(this).find('textarea').attr('style','display:none;')
	});	
	var id;
	if(secId!=''||secId!=null||secId!=undefined){
		id = $('#projectId').val();
	}
	else
	{
		id = secId;
	}
	
	loadData(function(res){		
		var arrMsg = res.items;
		$('#scheduleId').val(res.scheduleId);
		$('#projectId').val(res.projectId);
		$('#projectNames').val(res.projectNames);
		$('#updateDate').val(res.updateDate);
	if(arrMsg.length !=0){	
			var arrItem = arrMsg;
			var tibo=$('.fc-day');
			tibo.each(function(){
				$(this).find('textarea').val('');
				$(this).find('textarea').text('');
				$(this).find('textarea').attr('style','display:none;')
			});	
			
			    var getDate;
			    for (var int = 0; int < arrItem.length; int++) {
			    	var thisDay = arrItem[int].start;
			    	if(int == 0){
			    		getDate = thisDay;
			    	}else if(getDate > thisDay){
			    		getDate = thisDay;
			    	}
			    	
				}
				var date = new Date(getDate);
				var year = date.getFullYear(); //获取当前年份(2位)
				var month = date.getMonth(); //获取当前月份(0-11,0代表1月)
				$('#calendar').fullCalendar( 'gotoDate', date );
				$('.divine').text(year+"年"+(month+1)+"月");
				initEven();
				var tibo=$('.fc-day');
				tibo.each(function(){
					var countext =  $(this).attr('data-date');
					for(var k in arrItem){
						if (countext==arrItem[k].start){					
							if (!$(this).hasClass('fc-other-month')){
								$(this).find('textarea').attr('style',"border: none; resize: none;background: transparent;box-shadow: none;");
								$(this).find('textarea').val(arrItem[k].jobContent);
								$(this).find('textarea').text(arrItem[k].jobContent);
								if(arrItem[k].jobContent == ''||arrItem[k].jobContent == null || arrItem[k].jobContent ==undefined){
									$(this).find('textarea').attr('style',"display:none");	
								}
							}			
						}				
					}
				})
		}
	    boxData = new Array(); 
		boxData = arrMsg;
	/*	var objectbox=src.items;	
		arrobject=src.items;	
				
		if(getDay == 1){
			var date = new Date(arrobject[0].start);
			var year = date.getFullYear(); //获取当前年份(2位)
			var month = date.getMonth(); //获取当前月份(0-11,0代表1月)
			$('#calendar').fullCalendar( 'gotoDate', date );
			$('.divine').text(year+"年"+(month+1)+"月");
			initEven();
		}
		
		var xiaos=JSON.stringify(src.items);
		xiaos = xiaos.replace(/}{/g, '},{');
		xiaos=xiaos.replace(/\n/g,'\\\\n');
		$('#items').val(xiaos);	
		var tibo=$('.fc-day');
		chengnum='';
		tibo.each(function(){
			var countext =  $(this).attr('data-date');
			
			for(var k in objectbox){
				if (countext==objectbox[k].start){					
					if (!$(this).hasClass('fc-other-month')){
						$(this).find('textarea').attr('style',"border: none; resize: none;background: transparent;box-shadow: none;");
						$(this).find('textarea').val(objectbox[k].jobContent);
						$(this).find('textarea').text(objectbox[k].jobContent);	
					}			
				}				
			}
		})*/
	}, getContextPath() + '/schedule/get/'+id,null);
		
}
// 加载事件
function loadProductEven(){
	$('#cancleLoadProduct').on('click',function(){
		$('#loadProductModel').hide();
	});
	// 项目加载的时候回显
	$('#CheckloadProduct').off('click').on('click',function(){			
    	var thisName = $('.modelPActive').text();
		// 覆盖提示信息modelProItem
		if ($('.modelProItem').hasClass('modelPActive')){
			if ($('#projectId').val()== null || $('#projectId').val() == '' ||$('#projectId').val() == undefined){
				$('#loadProductModel').hide();
				$('#projectName').text(thisName);
				// 此处要打开 选中项目的 日程
				$('#projectNames').val($('.modelPActive').attr('projectname'));		
				$('#projectId').val($('.modelPActive').attr('data-pid'));
				getBoxInfo();	 				
			}else{
				$('#clearTable').show();
				$('#setTableTitle').html('排期表编辑中，是否加载并覆盖当前排期表?');
				$('.sureClear').off('click').on('click',function(){
					$('#projectNames').val($('.modelPActive').attr('projectname'));		
					$('#projectId').val($('.modelPActive').attr('data-pid'));
					getBoxInfo();
					$('#clearTable').hide();
					$('#loadProductModel').hide();
					$('#projectName').text(thisName);
				});
				$('.cancle').off('click').on('click',function(){							  
			    	$('#clearTable').hide();
				});   	
			}
		}				
	});	
	// 项目选择的 时候 样式
	 $('.modelProItem').off('click').on('click',function(){
		$('.modelProItem').removeClass('modelPActive');
		$(this).addClass('modelPActive');			
	});
	// 保存至项目
	 $('.createQuo .createFromTable').off('click').on('click',function(){
         $('.closeModel').off('click').on('click',function(){
        	 $('.cusModel').hide();
         }); 
         
         if (boxData.length > 0){
        	 if($('#projectId').val()==''||$('#projectId').val()== null||$('#projectId').val()== undefined){
        		 $('#showProductName').show();
        		 findAutoInfo('');
            	 $('#savesProductName').off('click').on('click',function(){
            		 var hasId = $('.modelMActive').attr('data-id');
            		 $('#projectId').val(hasId);
            		 var hasname= $('.modelMActive').text();
            		 $('#projectName').text(hasname);
            		 timebook();
            		 submitDateMyDate(0); 
            		 $('#showProductName').hide();               	 
                 });
            	 $('#cancleSavesProductName').off('click').on('click',function(){
            		 $('#showProductName').hide();
                 });
        	 }else{
        		 $('#errorSaveModel').show();
        		 $('.SaveModelBtn').off('click').on('click',function(){    

       /* 			 var popo;
        			 popo=chengnum;
        			 popo=popo.replace(/}{/g,'},{');
        			 popo='['+popo+']';
        			 popo=JSON.parse(popo.replace(/\n/g,'\\\\n'));
        			 // 對象
        			 popo=JSON.stringify(popo);*/
        			 timebook();
        			 submitDateMyDate(1); 
        		 });
        	 }
         }else {
        	  // 弹出新的窗口,显示提示项目内容不能为空 啦啦啦啦
             $('#submitCheck').show();
             $('#isSuccess').text('保存项目');
           	 $('#errorImg').show();
           	 $('#successContent').text('请编辑好排期变内容后，再保存至项目');
           	 $('.sureCheck').off('click').on('click',function(){
           		 $('#submitCheck').hide();
           	 });          	
         }        
	});	 
}
function submitDateMyDate(num){
//	timebook();
	// 最后确定了 之后在把要修改的 值赋给要保存的 数据
//	$('#items').val($('#pumpum').val());	
	var proName = $('.modelMActive').text();
	var proId = $('.modelMActive').attr('data-id');
	if(num == 1){
		proName = $('#projectName').text();
	}	
	if(proId == null || proId == '' || proId == undefined){
		proId = $('#projectId').val();
	}	
	var setItem = JSON.stringify(boxData);
	loadData(function(res){
	    if (res.result){
	 /*   	if(num == 0){
	    		$('#projectName').text($('#projectNames').val());
	    	}
	    	*/
	    	$('#errorSaveModel').hide();
	    	$('#submitCheck').show();
    		$('#isSuccess').text('保存至项目排期表');
    		$('#successContent').text('成功保存为项目排期表');
    		$('#quotationId').val(res.msg);
    		$('#errorImg').hide();
    		$(window.parent.parent.parent.document).find('html').scrollTop(0);
    		$(window.parent.parent.parent.document).find('body').scrollTop(0);
    		$('.sureCheck').off('click').on('click',function(){
    			$('#submitCheck').hide();
    			$('.cusModel').hide();
    		//	$('#items').val($('#pumpum').val());
    		//	getBoxInfo(0);
    		});
    		$('#productSelect').html('');
	    }else {
	    	$('#errorSaveModel').hide();
	    	$('#submitCheck').show();
    		$('#isSuccess').text('保存为失败了');
    		$('#errorImg').show();
    		$('#successContent').text(res.err);
    		$('.sureCheck').off('click').on('click',function(){
    			$('#submitCheck').hide();
    			$('.cusModel').hide();
    		});
    		$('#productSelect').html('');
	    }
	// 提交之后的 处理
  //	 	$('.matter').blur();
	}, getContextPath() + '/schedule/save',$.toJSON({
		scheduleId:  $('#scheduleId').val(), 
		projectId:   $('#projectId').val(),
		projectName: $('#projectName').text(),
		updateDate:  $('#updateDate').val(),
		itemContent:setItem
	}));  
}
// 加载所有项目 为保存排期表
function findAutoInfo(userName){
	loadData(function(res){
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
// 加载有排期表项目
function getLoadProduct(){
	var body = $('.modelProductContent');
	body.html('');
	loadData(function(res){
		for (var i = 0; i < res.length; i++) {
			body.append('<div class="modelProItem" data-id="'+res[i].quotationId+'" data-pid="'+res[i].projectId+' ">'+res[i].projectName+'</div>')
		 }
		 loadProductEven();
	}, getContextPath() + '/schedule/list/synergetic','');
}

// 生成排期表
function bestthings() {
    $('.best').on('click', function() {
    	sun();
    	timebook();
    	$('#calendar').find('.boxs .city-select').remove('.city-select');
    	$('#calendar').find('.fc-day').removeClass('cheng');
    	$('#calendar').find('.fc-day').attr('style', 'background: ;');
   // 	$('#calendar').find('.matter').blur();	 
    	var end=$(".fc-week td");
    	var projectName = $('#projectName').val();  	
    	$('#projectNames').val($('#projectName').text());
    	submitDate();	
    	$('.proerr').text('');
    	$('tbody .fc-other-month .matter').attr('style', 'display: none;');
 //   	$('.matter').blur();  	
    });
}
// 表单提交
function submitDate(){
	
	$('#toListForm #items').val(JSON.stringify(boxData));// 早晚要注释的	
	$('#toListForm #scheduleId').val($('#scheduleId').val());
	$('#toListForm #projectId').val($('#projectId').val());	
	$('#toListForm #projectName').val($('#projectId').val());	
	$('#toListForm #updateDate').val($('#updateDate').val());
	$('#toListForm').submit();	
	
}

// 实时获取textare的数据
/*function textareval(){
	 $("textarea").blur(function(){
		 $(this).text($(this).val());
	 });
}*/
// 获取事件的时间
function getday(){
	$('.fc-event-time').text('');// 清空插件中时间限制
	$('.fc-week td').on('click',function(){
		time=$(this).attr('data-date');
		// 获取时间2017-11-28 时间格式处理
		var times=JSON.stringify(time);
		yy=times.substr(1,4);// 年
		mm=times.substr(6,2);// 月
		dd=times.substr(9,2);// 日
	})	
	
	
	
}

// 下拉框
function sun(){
	
    // 多选
    var MulticitySelect1 = $('.city-select').citySelect({
        dataJson: cityData,// json 数据 是HTML显示的列表数据
        multiSelect: true,// 多选设置,默认不开启
        multiMaximum: 30,// 允许能选择几个 ，默认5，只用于多选
        search: false,// 开启搜索功能，默认是true ，开启
        placeholder: '请选择任务',
        onInit: function() {// 插件初始化的回调
               
        	$('.not-search').off('click').on('click',function(){    
        		      		$('.city-select').slideUp();
        	});
        	
        },
        onForbid: function() {// 插件禁止后再点击的回调

        },
        onTabsAfter: function(target) {// 点击tabs切换显示城市后的回调

        },
        // 选择城市后的回调
        onCallerAfter: function(target, values) {

        	var shaonv=JSON.stringify(values.name);
        	// 获取选中日期的 时间
        	var demo = $('#calendar').find('.cheng').attr('data-date');	
            matter=JSON.stringify(values);
            if (matter){            	
            	var bestval=$(".fc-week td[data-date="+time+"]").find(".city-info span");       
            	if (bestval.length==0){        	
            		var shus='';
            		shus=shaonv.substring(2,shaonv.length-2);
                   	$(".fc-week td[data-date="+demo+"]").find(".matter").val(season+shus);                	          	
                	$(".fc-week td[data-date="+demo+"]").find(".matter").attr('style', 'display:block;');
                	$(".fc-week td[data-date="+demo+"]").find(".matter").focus();  
            	}else {    
            		var shus='';
                	bestval.each(function(){
                		var countext =  $(this).text()+ '\n' ;
                		shus+=countext;
                	}); 
           // 添加当前的内容到当前时间下面
                 	$(".fc-week td[data-date="+time+"]").find(".matter").val(season+shus);                    	            	
                	$(".fc-week td[data-date="+time+"]").find(".matter").attr('style', 'display:block;');  
                	$(".fc-week td[data-date="+demo+"]").find(".matter").focus();
            	}
            }else{
            	$(".fc-week td[data-date="+time+"]").find(".matter").attr('style', 'display: none;');
            	$(".fc-week td[data-date="+time+"]").find(".matter").val('');
            	$(".fc-week td[data-date="+demo+"]").find(".matter").focus();
            }
            delselc();
        }       
    });
    // 多选设置城市接口
// MulticitySelect1.setCityVal('少女时代,啦的范德萨发啦');
}
// 刪除框数据的同步
function delselc(){
	
	$('.city-info i').on('click',function(e){

		var deltext=$(this).parent().parent().parent().find(".city-info span");
		if (deltext.length==1){
// 通过删除都删掉了
			$(".fc-week td[data-date="+time+"]").find(".matter").attr('style', 'display: none;');
        	$(".fc-week td[data-date="+time+"]").find(".matter").val('');
		}
 //		e.stopPropagation();
	});
}
// 月份左右跳转
function leftbtn() {
    $('tbody .fc-other-month .much').attr('style', 'display: none;');
    $('tbody .fc-other-month .boxs').attr('style', 'display: none;');
    $('tbody .fc-other-month .matter').attr('style', 'display: none;');
    $('.fc-header-left').on('click', function() {
    	initSelect();
    	sun();
    	getday();
    	dbmatter();
    	colorthing();
        $('tbody .fc-other-month .much').attr('style', 'display: none;');
        $('tbody .fc-other-month .boxs').attr('style', 'display: none;');       
        $('.fc-header-left .fc-button-today').removeAttr('style', 'pointer-events: none;');
        timebook();
        $('tbody .fc-other-month .matter').attr('style', 'display: none;');
        
    })
    $('.fc-button-today').on('click', function() {
    	initSelect();
    	sun();
    	getday();
    	dbmatter();
    	colorthing();
        if (!$('.fc-header-left .fc-button-today').hasClass('fc-state-disabled')) {
            $('tbody .fc-other-month .much').attr('style', 'display: none;');
            $('tbody .fc-other-month .boxs').attr('style', 'display: none;');
            $('tbody .fc-other-month .matter').attr('style', 'display: none;');
            $('.fc-header-left .fc-button-today').removeAttr('style', 'pointer-events: none;');
        } else {
            $('.fc-header-left .fc-button-today').attr('style', 'pointer-events: none;');
        }
        timebook();
        
    })
    $('.fc-header-right').hide();  
}


// 自定义复选框
function initSelect() {
	var wuding=$('.fc-header-title h2').text();
	$('.fc-header-title h2').attr('style', 'display:none');
	if(wuding.length==7){
		$('.divine').text(wuding.substring(3,7)+"年"+wuding.substring(0,3));
	}else {
		$('.divine').text(wuding.substring(4,8)+"年"+wuding.substring(0,4));
	}
	
	$('.city-select').slideUp();
	
    $('.orderSelect').off('click').on('click', function(e) {
    	$(this).parent().parent().parent().siblings().attr('style', 'background: ;');
    	$(this).parent().parent().parent().parent().siblings().find('.fc-day').attr('style', 'background: ;');
    	$(this).parent().parent().parent().attr('style', 'background: #F6F9F9;');
	//    $('.matter').blur();
    	$('.boxs .city-select').remove('.city-select');
    	// 添加append() - 在被选元素的结尾插入内容
    	// prepend() - 在被选元素的开头插入内容
    	// empty() - 从被选元素中删除子元素
     /*   if ($(this).hasClass('selectColor')) {    
        	$(this).parent().parent().find('.boxs .city-select').remove('.city-select');         	
        	$(this).parent().parent().find('.city-select').removeAttr('style', 'display: none;');
        	$(this).parent().parent().find('.city-select').slideUp();
            $(this).removeClass('selectColor');
        } else {*/
        	$(this).parent().parent().find('.boxs').prepend( "<div class='city-select' id=''></div>" );
        	sun();
            $('.orderSelect').removeClass('selectColor');
            $(this).parent().parent().find('.city-select').slideDown();
            $(this).addClass('selectColor'); 
            $(this).parent().parent().find('.boxs .city-pavilion').removeClass('hide');
       // }
        e.stopPropagation();
    });
    var pro = $('#projectName').text();
	if(pro == null || pro == undefined || pro == ''){
		$('#projectName').text('未命名项目');
	}
} 

function getCacheValue(){

	var arr = boxData;
	var scheduleId = $('#scheduleId').val();
	var projectId = $('#projectId').val();
	var projectNames = $('#projectName').text();
	var updateDate = $('#updateDate').val();
	var cacheItem = new Array();	
	var isDiff = false;
	
	if(cacheData.length !=0){	
		var cache=cacheData[0].item;
		var otherInfoItem = cacheData[0];	
		 
		if(cache.length != arr.length || scheduleId != otherInfoItem.scheduleId||projectId != otherInfoItem.projectId||projectNames != otherInfoItem.projectNames||	
	    		updateDate != otherInfoItem.updateDate
	    ){

	        isDiff = true;
	    }
		
        for(var k=0;k<arr.length;k++){
			var day = arr[k].day;
	        var end = arr[k].end;
	        var jobContent = arr[k].jobContent;
	        var start = arr[k].start;
	        
	        if(!isDiff){
	        	var otherItem = cache[k];
	        	if(day!=otherItem.day || end!=otherItem.end || jobContent!=otherItem.jobContent || start!=otherItem.start){
		        	
		        	isDiff = true;
		        }
	        }
	     
	        var map = {};
			map['jobContent'] = jobContent;
			map['start'] = start;
			map['end'] = end;
			map['day'] = day; 			
		    cacheItem.push(map);	
		}
	}else if(arr.length>0){
		isDiff = true;
		for(var k=0;k<arr.length;k++){
			var day = arr[k].day;
	        var end = arr[k].end;
	        var jobContent = arr[k].jobContent;
	        var start = arr[k].start;
	       
	        var map = {};
			map['jobContent'] = jobContent;
			map['start'] = start;
			map['end'] = end;
			map['day'] = day; 			
		    cacheItem.push(map);	
		}
	}
	
   if (isDiff){
		cacheData = new Array();
		cacheData.push(new cacheEntity(cacheItem,scheduleId,projectId,projectNames,updateDate));
		saveCache();
	}
}

function cacheEntity(item,scheduleId,projectId,projectNames,updateDate){
	this.item =  item;
	this.scheduleId = scheduleId;
	this.projectId = projectId;
	this.projectNames = projectNames;
	this.updateDate = updateDate;
}

function saveCache(){

	    var arr=JSON.stringify(cacheData[0]);
	
		loadData(function(res){
	           
			
			
		}, getContextPath() + '/cache/save',$.toJSON({
			type:1,
			dataContent:arr
		}));
			
}

function loadCache(){

	loadData(function(res){
           
		if(res.result){
			var arrMsg = jQuery.parseJSON(res.msg);
				$('#scheduleId').val(arrMsg.scheduleId);
				$('#projectId').val(arrMsg.projectId);
				$('#projectName').text(arrMsg.projectNames);
				$('#updateDate').val(arrMsg.updateDate);
			if(arrMsg.item.length !=0){	
					var arrItem = arrMsg.item;
					var tibo=$('.fc-day');
					tibo.each(function(){
						$(this).find('textarea').val('');
						$(this).find('textarea').text('');
						$(this).find('textarea').attr('style','display:none;')
					});	
					    var getDate;
					    for (var int = 0; int < arrItem.length; int++) {
					    	var thisDay = arrItem[int].start;
					    	cacheItem = new Array();
						   	var map = {};
							map['jobContent'] = arrItem[int].jobContent;
							map['start'] = arrItem[int].start;
							map['end'] = arrItem[int].end;
							map['day'] = arrItem[int].day; 
						    cacheItem.push(map);
							cacheData = new Array();
							cacheData.push(new cacheEntity(cacheItem,scheduleId,projectId,projectNames,updateDate));
					    	if(int == 0){
					    		getDate = thisDay;
					    	}else if(getDate > thisDay){
					    		getDate = thisDay;
					    	}
					    	
						}
						var date = new Date(getDate);
						var year = date.getFullYear(); //获取当前年份(2位)
						var month = date.getMonth(); //获取当前月份(0-11,0代表1月)
						$('#calendar').fullCalendar( 'gotoDate', date );
						$('.divine').text(year+"年"+(month+1)+"月");
						initEven();
						var tibo=$('.fc-day');
						tibo.each(function(){
							var countext =  $(this).attr('data-date');
							for(var k in arrItem){
								if (countext==arrItem[k].start){					
									if (!$(this).hasClass('fc-other-month')){
										$(this).find('textarea').attr('style',"border: none; resize: none;background: transparent;box-shadow: none;");
										$(this).find('textarea').val(arrItem[k].jobContent);
										$(this).find('textarea').text(arrItem[k].jobContent);	
										if(arrItem[k].jobContent == ''||arrItem[k].jobContent == null || arrItem[k].jobContent ==undefined){
										$(this).find('textarea').attr('style',"display:none");	
										}
									}			
								}				
							}
						})
				}
			    boxData = new Array();
				boxData = arrMsg.item;
				
		}
	},  getContextPath() + '/cache/get',$.toJSON({
		type:1
	}));
		
}

function initEven(){
	
	initSelect();
	sun();
	getday();
	dbmatter();
	colorthing();
    $('tbody .fc-other-month .much').attr('style', 'display: none;');
    $('tbody .fc-other-month .boxs').attr('style', 'display: none;');       
    $('.fc-header-left .fc-button-today').removeAttr('style', 'pointer-events: none;');
    timebook();
    $('tbody .fc-other-month .matter').attr('style', 'display: none;');
 //   $('.matter').blur();
	
}


