var successIntervalObj; // timer变量，控制时间

$().ready(function() {
	
	document.domain = getUrl();	
	initOption();
	$('body').off('click').on('click',function(){
		$('ul').slideUp();
		$('.oredrTypeSelect').removeClass('selectColor');
		$('.orderMultSelect ').removeClass('selectColor');
	});
	
});


//打开项目
function openProejct(){
	
	$('#openProejct').off('click').on('click',function(){
		$('#loadProductModel').show();
		$(".modelProductContent").html('');
		$('#CheckloadProduct').text('打开');
		loadData(function(src){
			for (var int = 0; int < src.length; int++) {
				 $(".modelProductContent").append(juicer(videoList_tpl.project_Tpl,{file:src[int]}));	 
			}
			initCheckProject();
			$('#CheckloadProduct').off('click').on('click',function(){
				var modelVal = $('.modelProductContent .modelPActive');
				if(modelVal.length>0){
				//	reShow(modelVal.attr('data-id'));
					$('#loadProductModel').hide();
				}
			});
			
		}, getContextPath() + '/continuity/list/synergetic','');
		
	});
	
}

//回显
function reShow(proId){
	
	loadData(function(src){
		setReShow(src);		
	}, getContextPath() + '/production/get/'+proId,'');
	
}

function setReShow(){}

//获取我的项目
function getMyProject(){	
	$('#loadProductModel').show();
	$(".modelProductContent").html('');
	$('#CheckloadProduct').text('保存');
	loadData(function(item){
		for (var int = 0; int < item.length; int++) {
			$(".modelProductContent").append(juicer(videoList_tpl.project_Tpl,{file:item[int]}));
		}
		initCheckProject();
		$('#CheckloadProduct').off('click').on('click',function(){
			var modelVal = $('.modelProductContent .modelPActive');
			if(modelVal.length>0){		
				loadData(function(res){				   
					if(res.result){
						 getValue(modelVal.attr('data-id'),0);
						 $('#projectName').val(modelVal.text());
                    	 $('#projectId').val(modelVal.attr('data-id'));
					}else{
                         $('#sameProject').show();
                         $('#toSame').off('click').on('click',function(){
                        	 getValue(modelVal.attr('data-id'),0);
                        	 $('#sameProject').hide();
                        	 $('#projectName').val(modelVal.text());
                        	 $('#projectId').val(modelVal.attr('data-id'));
                         });
                         $('#toCSame').off('click').on('click',function(){
                        	 $('#sameProject').hide();
                         });
                         $('.closeBtn').off('click').on('click',function(){
                        	 $('#sameProject').hide();
                         });
					}
				}, getContextPath() + '	/continuity/validate/'+modelVal.attr('data-id'),'');
			}
		});
	}, getContextPath() + '/project/synergetic/listByName', $.toJSON({
		projectName:$('#projectName').val(),
		projectId:$('#projectId').val()
	}));
}

function initCheckProject(){
	
	 $('.modelProItem').off('click').on('click',function(){
			$('.modelProItem').removeClass('modelPActive');
			$(this).addClass('modelPActive');
	});
	
}

//保存到项目
function saveToproject(){	
	$('#saveTo').off('click').on('click',function(){
		if(checkError()){
			if($('#id').val()!='' && $('#id').val()!=null && $('#id').val()!= undefined){
				getValue($('#projectId').val(),0);
			}else{
				getMyProject();			
			}
		}
	});
}


//打开项目
function openProjectModel(){
	
	$('#loadProductModel').show();
	$(".modelProductContent").html('');
	$('#CheckloadProduct').text('打开');
	loadData(function(src){
		
		for (var int = 0; int < src.length; int++) {
			 $(".modelProductContent").append(juicer(videoList_tpl.project_Tpl,{file:src[int]}));
		}		
		initCheckProject();
		$('#CheckloadProduct').off('click').on('click',function(){
			var modelVal = $('.modelProductContent .modelPActive');
			if(modelVal.length>0){
				reShow(modelVal.attr('data-id'));
				$('#loadProductModel').hide();
			}
		});
		
	}, getContextPath() + '/production/list/synergetic','');
	
}


//保存导出
function getValue(projectId,who){
	
	setData = new Array();
	var imgItem = $('.imgItem');
	for (var int = 0; int < imgItem.length; int++) {
		 var type = $(imgItem[int]).find('.checkImgType').attr('data-id');
		 var image = $(imgItem[int]).find('.loadImg').attr('data-id');
		 var text = $(imgItem[int]).find('.checkImgText').val();

		 setData.push(new optEntity(type,image,text));
	}
	
	var storyName = $('#storyName').text();
	var dimensionId = $('#time .active').attr('data-id');	
	var pictureRatio = $('#videoType .active').attr('data-id');
	var videoStyle = $('#videoStyleS .active').attr('data-id');
	var setArray = JSON.stringify(setData);

	if(who == 1){ 
		
		if(IEVersion()!= '-1'){

			$('#name').val(storyName);
			$('#dimensionId').val(dimensionId);
			$('#videoStyle').val(videoStyle);
			$('#pictureRatio').val(pictureRatio);
			$('#scriptContent').val(setArray);
			$('#toListForm').submit();
			
		}else{
			
		   var projectId = $('#projectId').val();
		   var createTime = $('#createTime').val();
		   var url = getContextPath() + '/continuity/export';
		   var xhr = new XMLHttpRequest();
		   var form = new FormData();
		   form.append('dimensionId',dimensionId);


		   xhr.open('POST', url, true);        // 也可以使用POST方式，根据接口
		   xhr.responseType = "blob";    // 返回类型blob
		   // 定义请求完成的处理函数，请求前也可以增加加载框/禁用下载按钮逻辑
		   successToolTipShow('文件制作中');
		   xhr.onload = function () {
		       // 请求完成
		       if (this.status === 200) {
		           // 返回200
		    	   var name = $('#projectName').text();
		    	   if(name == "未命名"){
		    		   name = "分镜脚本"
		    	   }else{
		    		   name = "《"+name+"》分镜脚本";
		    	   }
		    	   successToolTipShow('导出成功');
		           var blob = this.response;
		           var reader = new FileReader();
		           reader.readAsDataURL(blob);    // 转换为base64，可以直接放入a表情href
		           reader.onload = function (e) {
		               // 转换完成，创建一个a标签用于下载
		               var a = document.createElement('a');
		               a.download = name+'.pdf';
		               a.href = e.target.result;
		               $("body").append(a);    // 修复firefox中无法触发click
		               a.click();
		               $(a).remove();
		           }
		       }else{
		    	   $('#madeModel').hide();
		    	   successToolTipShow('导出失败');
		       }
		   };
		   // 发送ajax请求
		   xhr.send(form);
		}
		   
	}else{
			loadData(function(src){
				if(src.result){
					$('#id').val(src.msg);
					$('#loadProductModel').hide();
					delImgGroup = '';
					successToolTipShow('保存成功');
				}
				else{
					$('#loadProductModel').hide();
					successToolTipShow('保存失败');
				}
			}, getContextPath() + '/production/save', $.toJSON({
				 scripts:setData,
			}));
	}
		
}



function initOption(){
	
	$('.MidActive').find('.itemContent').slideDown();
	
	$('.titleM').off('click').on('click',function(){
		if($(this).parent().hasClass('MidActive')){
			$(this).parent().removeClass('MidActive');
			$(this).parent().find('.itemContent').slideUp();
		}
		else{
			$(this).parent().addClass('MidActive');
			$(this).parent().find('.itemContent').slideDown();
		}
	});
	
	$('#openProejct').off('click').on('click',function(){
		openProjectModel();
	});
			
	delItem();
	chooseType();
	openAddDiv();
	saveToproject();
	searchInit();
	
	//$(".setProductInfo").before(juicer(videoList_tpl.upload_Tpl,{item:''}));
	
}


function delItem(){
	
	$('.delP').off('click').on('click',function(){
		$('#checkSureModel').show();
		$('.tdDes').text('确认清空列表吗?');
		$('#tModel').off('click').on('click',function(){
			$('#checkSureModel').hide();
			$('.setProductInfo').html('');
			$('.noImg').show();
		});
	});
	
	$('.moveItem').off('click').on('click',function(){
	    var nowItem = $(this);
		$('#checkSureModel').show();
		$('.tdDes').text('确认删除吗');
		$('#tModel').off('click').on('click',function(){
			$('#checkSureModel').hide();
			nowItem.parent().parent().parent().remove();
		});
	});
	
	$('.closeModel,#cancleLoadProduct').off('click').on('click',function(){
		$('#loadProductModel').hide();
	});
	
	$('.closeBtn,#cModel').off('click').on('click',function(){
		$('.cusModel').hide();
	});
	
}


//添加

function openAddDiv(){
	
	$('#openAdd').off('click').on('click',function(){
		$('.addModel').show();
		initSelectInfo();
	});
	
}

function initSelectInfo(){
	
	
	//城市
	
	if(!$("#cityUl").hasClass('hasInfo')){
		loadData(function(src){
		     $("#cityUl").html('');
		     $("#cityUl").addClass('hasInfo');
		for (var int = 0; int < src.length; int++) {
			 $("#cityUl").append('<li data-id='+src[int].cityId+'>'+src[int].city+'</li>');	 
		}
		chooseType();
		}, getContextPath() + '/all/citys','');
	}
	//导演
	if(!$("#directorLevelUl").hasClass('hasInfo')){
		loadData(function(src){
		     $("#directorLevelUl").html('');
		     $("#directorLevelUl").addClass('hasInfo');
		     for (var int = 0; int < src.length; int++) {
		    	 $("#directorLevelUl").append(juicer(productList_tpl.mulit_Tpl,{itemInfo:src[int]}));
			}
		     chooseType();
		}, getContextPath() + '/quotation/production/select?productionType=director','');
	}
	
	if(!$("#directorZoneUl").hasClass('hasInfo')){
		loadData(function(src){
		     $("#directorZoneUl").html('');
		     $("#directorZoneUl").addClass('hasInfo');
		for (var int = 0; int < src.specialtyList.length; int++) {
			 $("#directorZoneUl").append('<li data-id='+src.specialtyList[int].value+'>'+src.specialtyList[int].text+'</li>');	 
		}
		chooseType();
		}, getContextPath() + '/production/director/parameter','');
	}
	
	
	//演员
	
	if(!$("#zoneUl").hasClass('hasInfo')){
		loadData(function(src){
		     $("#zoneUl").html('');
		     $("#zoneUl").addClass('hasInfo');
		for (var int = 0; int < src.zoneList.length; int++) {
			 $("#zoneUl").append('<li data-id='+src.zoneList[int].value+'>'+src.zoneList[int].text+'</li>');	 
		}
		chooseType();
		}, getContextPath() + '/production/actor/parameter','');
	}
	
	if(!$("#actorLevelUl").hasClass('hasInfo')){
		loadData(function(src){
		     $("#actorLevelUl").html('');
		     $("#actorLevelUl").addClass('hasInfo');
		     for (var int = 0; int < src.length; int++) {
		    	 var mainCode = src[int].typeName;
			     var secondCode = src[int].children;
		    	 $("#actorLevelUl").append(juicer(productList_tpl.mulit_Tpl,{itemInfo:src[int]}));
			}
		chooseType();
		}, getContextPath() + ' /quotation/production/select?productionType=actor','');
	}
	
	//场地
	
	if(!$("#studioUl").hasClass('hasInfo')){
		loadData(function(src){
		     $("#studioUl").html('');
		     $("#studioUl").addClass('hasInfo');
		 for (var int = 0; int < src.length; int++) {
	    	 $("#studioUl").append(juicer(productList_tpl.mulit_Tpl,{itemInfo:src[int]}));
		}
		chooseType();
		}, getContextPath() + ' /quotation/production/select?productionType=studio','');
	}
	
	//设备
	if(!$("#deviceTypeUl").hasClass('hasInfo')){
		loadData(function(src){
		     $("#deviceTypeUl").html('');
		     $("#deviceTypeUl").addClass('hasInfo');
		 for (var int = 0; int < src.length; int++) {
	    	 $("#deviceTypeUl").append(juicer(productList_tpl.mulit_Tpl,{itemInfo:src[int]}));
		}
		chooseType();
		}, getContextPath() + ' /quotation/production/select?productionType=device','');
	}

	
}


function searchInit(){
	
	$('.search').off('click').on('click',function(){
		
		var checkWho = $('#productType').attr('data-id');
		
		switch (checkWho) {
		case 'actor':
			searchActor();
			break;
		case 'director':
			searchDirector();
			break;
		case 'device':
			searchDevice();
			break;
		case 'studio':
			searchStudio();
			break;
		default:
			alert('error');
			break;
		}
		
	});	
}

function searchActor(){
	
	var category = $('#productType').attr('data-id');
	var city = $('#city').attr('data-id');
	var sex = $('#sex').text();
	var beginAge = $('#beginAge').val();
	var endAge = $('#endAge').val();
	var zone = $('#zone').attr('data-id');
	var typeId = $('#actorLevel').attr('data-id');
	var name = $('#nomalName').val();
	var beginPrice = $('#beginPrice').val();
	var endPrice = $('#endPrice').val();
	
	loadData(function(src){

	}, getContextPath() +  '/production/production/resource/list',$.toJSON({
		category:category,
		city:city,
		sex:sex,
		beginAge:beginAge,
		endAge:endAge,
		beginPrice:beginPrice,
		endPrice:endPrice,
		zone:zone,
		typeId:typeId,
		name:name,
	}));	
}

function searchDirector(){
	
	var category = $('#productType').attr('data-id');
	var city = $('#city').attr('data-id');
	var zone = $('#directorZone').attr('data-id');
	var typeId = $('#directorLevel').attr('data-id');
	var name = $('#nomalName').val();
	var beginPrice = $('#beginPrice').val();
	var endPrice = $('#endPrice').val();
	
	loadData(function(src){

	}, getContextPath() +  '/production/production/resource/list',$.toJSON({
		category:category,
		city:city,
		beginPrice:beginPrice,
		endPrice:endPrice,
		typeId:typeId,
		zone:zone,
		name:name,
	}));	
}

function searchDevice(){
	
	var category = $('#productType').attr('data-id');
	var city = $('#city').attr('data-id');
	var typeId = $('#speName').attr('data-id');
	var type = $('#deviceType').attr('data-id');
	var beginPrice = $('#beginPrice').val();
	var endPrice = $('#endPrice').val();
	
	loadData(function(src){

	}, getContextPath() +  '/production/production/resource/list',$.toJSON({
		category:category,
		city:city,
		typeId:typeId,
		type:type,
		beginPrice:beginPrice,
		endPrice:endPrice,
	}));	
}

function searchStudio(){
	
	var category = $('#productType').attr('data-id');
	var city = $('#city').attr('data-id');
	var typeId = $('#speName').attr('data-id');
	var name = $('#nomalName').val();
	var beginPrice = $('#beginPrice').val();
	var endPrice = $('#endPrice').val();
	
	loadData(function(src){

	}, getContextPath() +  '/production/production/resource/list',$.toJSON({
		category:category,
		city:city,
		typeId:typeId,
		name:name,
		beginPrice:beginPrice,
		endPrice:endPrice,
	}));	
}


function chooseType(){
	
	initSelect();
	
	$('.orderMultSelect').off('click').on('click',function(e){
		if(!$('.setMultSelect').hasClass('selectColor')){
			$('.setMultSelect').removeClass('selectColor');
			$(this).find('.setMultSelect').slideDown();
			$(this).addClass('selectColor');
		}
		e.stopPropagation();
	});
	
	$('.multSelectEven div').off('click').on('click',function(e){
		$('.setMultSelect').slideUp();
		var parentText = $(this).text();
		var parentId = $(this).attr('data-id');
		$(this).parent().parent().parent().parent().parent().find('.imgType').text(parentText);
		$(this).parent().parent().parent().parent().parent().find('.imgType').attr('data-id',parentId);
		$('.orderMultSelect').removeClass('selectColor'); 
		
		if($(this).parent().parent().parent().parent().hasClass('deviceTypeUl')){
			if(!$("#speNameUl").hasClass('hasInfo')){
				loadData(function(src){
				     $("#speNameUl").html('');
				     $("#speNameUl").addClass('hasInfo');
				 for (var int = 0; int < src.length; int++) {
			    	 $("#speNameUl").append('<li data-id='+src[int].key+'>'+src[int].value+'</li>');
				}
				 chooseType();
				}, getContextPath() + '/quotation/production/children?typeId='+parentId,'');
			}
		}
		
		e.stopPropagation();
	});
	
	$('#productList div').off('click').on('click',function(e){
		$('.setMultSelect').slideUp();
		var parentText = $(this).text();
		var parentId = $(this).attr('data-id');
		$('#productType').text(parentText);
		$('#productType').attr('data-id',parentId);
		
		if(parentText == '演员组'){
			$('.showUnmInfo').hide();
			$('.show3').show();
			$('#searchName').show();
			loadData(function(src){
				$('.actorLevelUl').append('<li></li>');
				initSelect();
			}, getContextPath() +  '/quotationtype/production/select?productionType=actor','');
		}
		if(parentText == '导演组'){
			$('.showUnmInfo').hide();
			$('.show4').show();
			$('#searchName').show();
			loadData(function(src){
				$('.directorLevelUl').append('<li></li>');
				initSelect();
			}, getContextPath() +  '/quotationtype/production/select?productionType=director','');

		}
		
		$('.orderMultSelect').removeClass('selectColor'); 
		e.stopPropagation();
	});
	
	$('.findTYpe').off('click').on('click',function(e){
		$('.setMultSelect').slideUp();
		var parentText = $(this).text();
		var parentId = $(this).attr('data-id');
		$('#productType').text(parentText);
		$('#productType').attr('data-id',parentId);
		$('.orderMultSelect').removeClass('selectColor'); 
		$('.oSelect').slideUp();
		if(parentText == '设备'){
			$('.showUnmInfo').hide();
			$('.show2').show();
			$('#searchName').hide();
			loadData(function(src){
				$('#speName').append('<li></li>');				
				initSelect();
			}, getContextPath() +  '/quotationtype/production/select?productionType=device&subType=xx','');
			loadData(function(src){
				$('#deviceTypeUl').append('<li></li>');				
				initSelect();
			}, getContextPath() +  '/quotationtype/production/select?productionType=device','');
			$('#deviceTypeUl').append('<li></li>');
		}
		if(parentText == '场地'){
			$('.showUnmInfo').hide();
			$('.show1').show();
			$('#searchName').show();
			loadData(function(src){
				$('.actorLevelUl').append('<li></li>');
				initSelect();
			}, getContextPath() +  '/quotationtype/production/select?productionType=director','');
			
		}
		e.stopPropagation();
	});
	
}


var productList_tpl = {
		upload_Tpl:[
		" <div class='videoCard'>                            "+
		"{@each firstCode as item}"+ 
	    "   <div class='BigItem'>${item}" +
	    "      <div class='titleB'>标题分类</div>"+
	    "          <div class='MidItem MidActive'>"+
	    "              <div class='titleM'>标题分类小<div></div></div>"+
	    "			   <div class='itemContent'>"+
	    "              {@each secondCode as item2}"+
	    "                       {@if it.num==3}                     "+
		"		                <div class='itemContentFive itemCommon'>"+
	    "                       {@/if}"+
	    "                       {@if it.num==3}                     "+
		"		                <div class='itemContentFour itemCommon'>"+
	    "                       {@/if}"+
		"	                        <img src='https://file1.apaipian.com:8000/group1/M00/00/26/Cgqp51m40FGATWfEAAAKH4Shw48846.jpg'>"+
		"	                        <div class='info'>"+
		"	                                <div class='who'>我 / 员工</div>"+
		"	                                <div class='price'>￥600</div>"+
		"	                        </div>"+
		"	                        <div class='showTool'>"+
	    "                                <div class='toolDiv'>"+
	    "			                        <div class='moveItem'>移除</div><div>查看详情</div>"+
	    "                                </div>"+
	    "                           </div>"+
        "                      </div>"+
	    "              {@/each}"+
	    "             </div>"+
	    "          </div>"+
	    "      </div>"+
	    "{@/each}"       
		].join(""),
		 project_Tpl:[
		              "<div class='modelProItem' data-showId='${file.id}' data-id='${file.projectId}'>${file.projectName}</div>"
		 ].join(""),
		 mulit_Tpl:[
		              "   <li>"+
					  "	       <div class='multSelect'>  "+                                   
					  "	              <div class='multTitle'>"+                                     
					  "	                   <img class='quoIcon' src='/resources/images/index/quoIcon.png'>"+               
					  "	                   <div class='title' data-id=''>${itemInfo.typeName}</div>"+                       
					  "	              </div>"+                                                  
					  "	              <div class='productList multSelectEven'>"+  
					  "                   {@each itemInfo.children as children}"+ 
					  "	                        <div data-id='${children.typeId}' >${children.typeName}</div>"+ 
					  "                   {@/each}"+
					  "	              </div>"+   
					  "	        </div>"+ 
		              "   </li>"
		 ].join(""),
}

//错误提示
function successToolTipShow(error){
		window.clearInterval(successIntervalObj);
		$('.tooltip-success-show').show();
		$(".tooltip-success-show").text(error);
		$(window.parent.parent.parent.document).find('html').scrollTop(0);
		$(window.parent.parent.parent.document).find('body').scrollTop(0);
		successIntervalObj = window.setInterval(hideSuccessTooltip, 3000);
	}
	
function hideSuccessTooltip(){
		$('.tooltip-success-show').hide();
	}
