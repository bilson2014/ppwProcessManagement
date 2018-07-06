var successIntervalObj; // timer变量，控制时间
var setData;
var nowItem = new Array();

$().ready(function() {
	
	document.domain = getUrl();	
	initOption();
	
});


//打开项目
function openProejct(){
	
	$('#openProejct').off('click').on('click',function(){
		$('#loadProductModel').show();
		$(".modelProductContent").html('');
		$('#CheckloadProduct').text('打开');
		loadData(function(src){
			for (var int = 0; int < src.length; int++) {
				 $(".modelProductContent").append(juicer(productList_tpl.project_Tpl,{file:src[int]}));	 
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
		setReShow(src.resources,0);	
		$('#projectId').val(src.projectId);
		$('#id').val(src.id);
	}, getContextPath() + '/production/get/'+proId,'');
	
}
function setReShow(item,num){
	$('.noImg').hide();
	$('.toolBtn').removeClass('hide');
	if(num == 0){
		$('#setProduct').html('');
	}
	
	//生成父节点
	for (var int = 0; int < item.length; int++) {
		var theItem = $(item)[int];
		var hasBigItem = $('.BigItem');
		if(hasBigItem.length >0){
			var hasSame = false;
			for (var int2 = 0; int2 < hasBigItem.length; int2++) {
				var checkSame = $(hasBigItem[int2]).attr('data-id');
				if(checkSame == theItem.categoryId){
					hasSame = true;
					break;
				}else{
					hasSame = false;
				}
			}
			if(!hasSame){
				$('#setProduct').append('<div class="BigItem" data-id="'+theItem.categoryId+'"><div class="titleB">'+theItem.category+'</</div>')
			}
		}else{
			$('#setProduct').append('<div class="BigItem" data-id="'+theItem.categoryId+'"><div class="titleB">'+theItem.category+'</</div>');
			
		}
	}
	//生成子节点
	for (var int = 0; int < item.length; int++) {
		 var theItem = $(item)[int];
		 var hasBigItem = $('.BigItem');
		 var checkMidItem = $('.checkMidItem');
		 
		 if(checkMidItem.length > 0){			 
			    var hasSame = false;
				for (var int2 = 0; int2 < checkMidItem.length; int2++) {
					var checkSame = $(checkMidItem[int2]).attr('data-id');
					if(checkSame == theItem.subTypeId){
						hasSame = true;
						break;
					}else{
						hasSame = false;
					}
				}
			 for (var int2 = 0; int2 < hasBigItem.length; int2++) {
					var checkSame = $(hasBigItem[int2]).attr('data-id');
					if(checkSame == theItem.categoryId){
						var html = [
						    		'<div class="MidItem MidActive" ><div class="titleM ">'+theItem.subType+'<div></div></div>'+
						    		'<div class="itemContent checkMidItem itemCommonRed" data-id='+theItem.subTypeId+'></div></div>'
						    	].join('');
						if(!hasSame){
							$(hasBigItem[int2]).append(html);
						}
						break;
					}
				}
		 }else{			 
			 for (var int2 = 0; int2 < hasBigItem.length; int2++) {
					var checkSame = $(hasBigItem[int2]).attr('data-id');
					if(checkSame == theItem.categoryId){
						var html = [
						    		'<div class="MidItem MidActive" ><div class="titleM ">'+theItem.subType+'<div></div></div>'+
						    		'<div class="itemContent checkMidItem itemCommonRed" data-id='+theItem.subTypeId+'></div></div>'
						    	].join('');
						$(hasBigItem[int2]).append(html);
						break;
					}
				}
		 }
	}
	
	//生成内容
	for (var int = 0; int < item.length; int++) {
		 var theItem = $(item)[int];
		 var checkMidItem = $('.checkMidItem');
				for (var int2 = 0; int2 < checkMidItem.length; int2++) {
					var checkSame = $(checkMidItem[int2]).attr('data-id');
					if(checkSame == theItem.subTypeId){
						//去重
						if(num == 1){
						   var cancelSame = $(checkMidItem[int2]).find('.itemCommon');
						   var hasItem = false;
							   for (var int3 = 0; int3 < cancelSame.length; int3++) {
								    var theNum = $(cancelSame[int3]).attr('data-id');
								    if(theNum == theItem.id){
								    	hasItem = true;
								    	break;
								    }else{
								    	hasItem = false;
								    }
							   }
							if(!hasItem){
								$(checkMidItem[int2]).append(juicer(productList_tpl.item_Tpl,{item:theItem}));
							}
						}else{
							$(checkMidItem[int2]).append(juicer(productList_tpl.item_Tpl,{item:theItem}));
						}
						break;
					}
				}
		 }
	initOption();
	$(window.parent.parent.parent.document).find('html').scrollTop(0);
	$(window.parent.parent.parent.document).find('body').scrollTop(0);
	$(window.parent.document).find('.frame').css('height',$('.page').height() + 300);
}

/*  */

//获取我的项目
function getMyProject(){	
	$('#loadProductModel').show();
	$(".modelProductContent").html('');
	$('#CheckloadProduct').text('保存');
	$(window.parent.parent.parent.document).find('html').scrollTop(0);
	$(window.parent.parent.parent.document).find('body').scrollTop(0);
	loadData(function(item){
		for (var int = 0; int < item.length; int++) {
			$(".modelProductContent").append(juicer(productList_tpl.project_Tpl,{file:item[int]}));
		}
		initCheckProject();
		$('#CheckloadProduct').off('click').on('click',function(){
			var modelVal = $('.modelProductContent .modelPActive');
			if(modelVal.length>0){		
				loadData(function(res){				   
					if(res.result){
						 $('#projectName').val(modelVal.text());
                    	 $('#projectId').val(modelVal.attr('data-id'));
						 getValue(modelVal.attr('data-id'),0);
						
					}else{
                         $('#sameProject').show();
                         $('#toSame').off('click').on('click',function(){
                        	 $('#sameProject').hide();
                        	 $('#projectName').val(modelVal.text());
                        	 $('#projectId').val(modelVal.attr('data-id'));
                        	 getValue(modelVal.attr('data-id'),0);
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
            
		   var imgItem = $('.itemCommon');
		   if(imgItem.length > 0){
				if($('#id').val()!='' && $('#id').val()!=null && $('#id').val()!= undefined){
					getValue($('#projectId').val(),0);
				}else{
					getMyProject();			
				}
		   }else{
			   successToolTipShow('无内容保存');
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
			 $(".modelProductContent").append(juicer(productList_tpl.project_Tpl,{file:src[int]}));
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
	
	nowItem = new Array();
	var imgItem = $('.itemCommon');
	for (var int = 0; int < imgItem.length; int++) {
		var id= $(imgItem[int]).attr('data-id');
		var type= $(imgItem[int]).attr('data-type');
		var price= $(imgItem[int]).attr('data-price');
		var name= $(imgItem[int]).attr('data-name');
		var mainPhoto= $(imgItem[int]).attr('data-mainPhoto');
		var typeId= $(imgItem[int]).attr('data-typeId');
		var typeName= $(imgItem[int]).attr('data-typeName');
		var categoryId= $(imgItem[int]).attr('data-categoryId');
		var category= $(imgItem[int]).attr('data-category');
		var subTypeId= $(imgItem[int]).attr('data-subTypeId');
		var subType= $(imgItem[int]).attr('data-subType');
		var picScale= $(imgItem[int]).attr('data-picScale');
		nowItem.push(new resourcesEntity(id,type,price,name,mainPhoto,typeId,typeName,categoryId,category,subTypeId,subType,picScale));
	}
	
	var setArray = JSON.stringify(nowItem);

	if(who == 1){ 
		
		if(IEVersion()!= '-1'){
			$('#resources').val(setArray);
			$('#toListForm').submit();
		}else{
			
		   var projectId = $('#projectId').val();
		   var id = $('#id').val();
		   var url = getContextPath() + '/production/export';
		   var xhr = new XMLHttpRequest();
		   var form = new FormData();
		   form.append('projectId',projectId);
		   form.append('id',id);
		   form.append('resources',setArray);
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
		    
		    var theId = $('#projectId').val();
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
				 projectId:$('#projectId').val(),
				 id:$('#id').val(),
				 resources:nowItem
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
		$(window.parent.document).find('.frame').css('height',$('.page').height() + 300);
	});
	
	$('#openProejct').off('click').on('click',function(){
		openProjectModel();
	});
	
	$('#reToMake').off('click').on('click',function(){
		// toGetAddItem();
		$('.addModel').hide();
		$("#addSetProductInfo").html('');
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
			$('.toolBtn').addClass('hide');
			$('#id').val('');
			$('#projectId').val('');
			$('#"resources"').val('');
			
		});
	});
	
	$('.moveItem').off('click').on('click',function(){
	    var nowItem = $(this);
		$('#checkSureModel').show();
		$('.tdDes').text('确认删除吗');
		$(window.parent.parent.parent.document).find('html').scrollTop(0);
		$(window.parent.parent.parent.document).find('body').scrollTop(0);
		$('#tModel').off('click').on('click',function(){
			$('#checkSureModel').hide();
			nowItem.parent().parent().parent().remove();
			successToolTipShow('删除成功');
			toCleanNullItem();
		});
	});
	
	$('.closeModel,#cancleLoadProduct').off('click').on('click',function(){
		$('#loadProductModel').hide();
	});
	
	$('.closeBtn,#cModel').off('click').on('click',function(){
		$('.cusModel').hide();
	});
	
}

function toCleanNullItem(){
	
	var itemContent = $('.itemContent');
	for (var int = 0; int < itemContent.length; int++) {
		var hasItem = $(itemContent[int]).find('.itemCommon');
		if(!hasItem.length > 0){
			$(itemContent[int]).parent().remove();
		}
	}
	
	var BigItem = $('.BigItem');
	for (var j = 0; j < BigItem.length; j++) {
		var hasBigItem = $(BigItem[j]).find('.MidItem');
		if(!hasBigItem.length > 0){
			$(BigItem[j]).remove();
		}
	}
	
	var checkIsNull = $('.BigItem');
	if(!checkIsNull.length > 0){
		$('.noImg').show();
		$('.toolBtn').addClass('hide');
	}
	
}


//添加

function openAddDiv(){
	
	$('#openAdd').off('click').on('click',function(){
		$('.addModel').show();
		$('#addSetProductInfo').html('');
		$('#productType').text('');
		$('#productType').attr('data-id','');
		$('.show1').hide();
		$('.show2').hide();
		$('.show3').hide();
		$('.show4').hide();
		toCleanAdd();
		initSelectInfo();
	});
	
}

function toCleanAdd(){
	
	$('#nomalName').val('');
	$('#city').text('');
	$('#city').attr('data-id','');
	$('#beginPrice').val('');
	$('#endPrice').val('');
	$('#sex').text('');
	$('#sex').attr('data-id','');
	$('#beginAge').val('');
	$('#endAge').val('');
	$('#zone').text('');
	$('#zone').attr('data-id','');
	$('#actorLevel').text('');
	$('#actorLevel').attr('data-id','');
	$('#deviceType').text('');
	$('#deviceType').attr('data-id','');
	$('#speName').text('');
	$('#speName').attr('data-id','');
	$('#studioType').text('');
	$('#studioType').attr('data-id','');
	$('#directorLevel').text('');
	$('#directorLevel').attr('data-id','');
	$('#directorZone').text('');
	$('#directorZone').attr('data-id','');
	
	
	$('#cameramanLevel').text('');
	$('#cameramanLevel').attr('data-id','');
	$('#cameramanSkill').text('');
	$('#cameramanSkill').attr('data-id','');
	$('#lighterLevel').text('');
	$('#lighterLevel').attr('data-id','');
	$('#editorLevel').text('');
	$('#editorLevel').attr('data-id','');
	$('#packerLevel').text('');
	$('#packerLevel').attr('data-id','');
	$('#coloristLevel').text('');
	$('#coloristLevel').attr('data-id','');
	$('#propMasterLevel').text('');
	$('#propMasterLevel').attr('data-id','');
	$('#artistLevel').text('');
	$('#artistLevel').attr('data-id','');
	$('#dresserLevel').text('');
	$('#dresserLevel').attr('data-id','');
	$('#mixerLevel').text('');
	$('#mixerLevel').attr('data-id','');
	$('#clothingType').text('');
	$('#clothingType').attr('data-id','');
	$('#clothingAccredit').text('');
	$('#clothingAccredit').attr('data-id','');
	$('#propsAccredit').text('');
	$('#propsAccredit').attr('data-id','');
}


function initSelectInfo(){
	
	//城市
	
	if(!$("#cityUl").hasClass('hasInfo')){
		loadData(function(src){
		     $("#cityUl").html('');
		     $("#cityUl").addClass('hasInfo');
		for (var int = 0; int < src.length; int++) {
			 $("#cityUl").append('<li data-id='+src[int].cityID+'>'+src[int].city+'</li>');	 
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
	//摄影师
	initLevelSelectInfo('cameraman');
	if(!$("#cameramanSkillUl").hasClass('hasInfo')){
		loadData(function(src){
		     $("#cameramanSkillUl").html('');
		     $("#cameramanSkillUl").addClass('hasInfo');
		     for (var int = 0; int < src.specialSkillList.length; int++) {
		    	 $("#cameramanSkillUl").append('<li data-id='+src.specialSkillList[int].value+'>'+src.specialSkillList[int].text+'</li>');	 
		     }
		     chooseType();
		}, getContextPath() + '/production/cameraman/parameter','');
	}
	//灯光师
	initLevelSelectInfo('lighter');
	//剪辑师
	initLevelSelectInfo('editor');
	//包装师
	initLevelSelectInfo('packer');
	//调色师
	initLevelSelectInfo('colorist');
	//道具师
	initLevelSelectInfo('propMaster');
	//美术师
	initLevelSelectInfo('artist');
	//化妆师
	initLevelSelectInfo('dresser');
	//录音师
	initLevelSelectInfo('mixer');
	//服装
	if(!$("#clothingTypeUl").hasClass('hasInfo')){
		loadData(function(src){
		     $("#clothingTypeUl").html('');
		     $("#clothingTypeUl").addClass('hasInfo');
		     for (var int = 0; int < src.clothingTypeList.length; int++) {
		    	 $("#clothingTypeUl").append('<li data-id='+src.clothingTypeList[int].value+'>'+src.clothingTypeList[int].text+'</li>');	 
		     }
		     
		     $("#clothingAccreditUl").html('');
		     $("#clothingAccreditUl").addClass('hasInfo');
		     for (var int = 0; int < src.accreditList.length; int++) {
		    	 $("#clothingAccreditUl").append('<li data-id='+src.accreditList[int].value+'>'+src.accreditList[int].text+'</li>');	 
		     }
		     chooseType();
		}, getContextPath() + '/production/clothing/parameter','');
	}
	//道具
	if(!$("#propsAccreditUl").hasClass('hasInfo')){
		loadData(function(src){
		     $("#propsAccreditUl").html('');
		     $("#propsAccreditUl").addClass('hasInfo');
		     for (var int = 0; int < src.accreditList.length; int++) {
		    	 $("#propsAccreditUl").append('<li data-id='+src.accreditList[int].value+'>'+src.accreditList[int].text+'</li>');	 
		     }
		     chooseType();
		}, getContextPath() + '/production/props/parameter','');
	}
}

function initLevelSelectInfo(productionType){
	if(!$("#"+productionType+"LevelUl").hasClass('hasInfo')){
		loadData(function(src){
		     $("#"+productionType+"LevelUl").html('');
		     $("#"+productionType+"LevelUl").addClass('hasInfo');
		 for (var int = 0; int < src.length; int++) {
	    	 $("#"+productionType+"LevelUl").append(juicer(productList_tpl.mulit_Tpl,{itemInfo:src[int]}));
		}
		chooseType();
		}, getContextPath() + ' /quotation/production/select?productionType='+productionType,'');
	}
}


function searchInit(){
	
	$('.search').off('click').on('click',function(){
		
		var checkWho = $('#productType').attr('data-id');
		$("#addSetProductInfo").html('');
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
		case 'cameraman':
			searchCameraman();
			break;
		case 'lighter':
			searchPersonWithType();
			break;
		case 'editor':
			searchPersonWithType();
			break;
		case 'packer':
			searchPersonWithType();
			break;
		case 'colorist':
			searchPersonWithType();
			break;
		case 'propMaster':
			searchPersonWithType();
			break;
		case 'artist':
			searchPersonWithType();
			break;
		case 'costumer':
			searchPerson();
			break;
		case 'dresser':
			searchPersonWithType();
			break;
		case 'mixer':
			searchPersonWithType();
			break;
		case 'clothing':
			searchClothing();
			break;
		case 'props':
			searchProps();
			break;
			
		default:
			successToolTipShow('请选择类别');
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
		$("#addSetProductInfo").append(juicer(productList_tpl.search_Tpl,{itemInfo:src}));
		initAddCanEven();		
	}, getContextPath() +  '/production/resource/list',$.toJSON({
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
		$("#addSetProductInfo").append(juicer(productList_tpl.search_Tpl,{itemInfo:src}));
		initAddCanEven();
	}, getContextPath() +  '/production/resource/list',$.toJSON({
		category:category,
		city:city,
		beginPrice:beginPrice,
		endPrice:endPrice,
		typeId:typeId,
		specialty:zone,
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
		$("#addSetProductInfo").append(juicer(productList_tpl.search_Tpl,{itemInfo:src}));
		initAddCanEven();
	}, getContextPath() +  '/production/resource/list',$.toJSON({
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
	var typeId = $('#studioType').attr('data-id');
	var name = $('#nomalName').val();
	var beginPrice = $('#beginPrice').val();
	var endPrice = $('#endPrice').val();
	
	loadData(function(src){
		 $("#addSetProductInfo").append(juicer(productList_tpl.search_Tpl,{itemInfo:src}));
		 initAddCanEven();
	}, getContextPath() +  '/production/resource/list',$.toJSON({
		category:category,
		city:city,
		typeId:typeId,
		name:name,
		beginPrice:beginPrice,
		endPrice:endPrice,
	}));	
}

function searchCameraman(){
	
	var category = $('#productType').attr('data-id');
	var city = $('#city').attr('data-id');
	var typeId = $('#cameramanLevel').attr('data-id');
	var name = $('#nomalName').val();
	var beginPrice = $('#beginPrice').val();
	var endPrice = $('#endPrice').val();
	
	var specialSkill=$('#cameramanSkill').attr('data-id');
	
	loadData(function(src){
		 $("#addSetProductInfo").append(juicer(productList_tpl.search_Tpl,{itemInfo:src}));
		 initAddCanEven();
	}, getContextPath() +  '/production/resource/list',$.toJSON({
		category:category,
		city:city,
		typeId:typeId,
		name:name,
		beginPrice:beginPrice,
		endPrice:endPrice,
		specialSkill:specialSkill,
	}));	
}

function searchPersonWithType(){
	
	var category = $('#productType').attr('data-id');
	var city = $('#city').attr('data-id');
	var typeId = $('#'+category+'Level').attr('data-id');
	var name = $('#nomalName').val();
	var beginPrice = $('#beginPrice').val();
	var endPrice = $('#endPrice').val();
	
	loadData(function(src){
		 $("#addSetProductInfo").append(juicer(productList_tpl.search_Tpl,{itemInfo:src}));
		 initAddCanEven();
	}, getContextPath() +  '/production/resource/list',$.toJSON({
		category:category,
		city:city,
		typeId:typeId,
		name:name,
		beginPrice:beginPrice,
		endPrice:endPrice,
	}));	
}
function searchPerson(){
	
	var category = $('#productType').attr('data-id');
	var city = $('#city').attr('data-id');
	var name = $('#nomalName').val();
	var beginPrice = $('#beginPrice').val();
	var endPrice = $('#endPrice').val();
	
	loadData(function(src){
		 $("#addSetProductInfo").append(juicer(productList_tpl.search_Tpl,{itemInfo:src}));
		 initAddCanEven();
	}, getContextPath() +  '/production/resource/list',$.toJSON({
		category:category,
		city:city,
		name:name,
		beginPrice:beginPrice,
		endPrice:endPrice,
	}));	
}

function searchClothing(){
	
	var category = $('#productType').attr('data-id');
	var city = $('#city').attr('data-id');	
	var name = $('#nomalName').val();
	var beginPrice = $('#beginPrice').val();
	var endPrice = $('#endPrice').val();
	
	var type = $('#clothingType').attr('data-id');
	var accredit = $('#clothingAccredit').attr('data-id');
	
	loadData(function(src){
		 $("#addSetProductInfo").append(juicer(productList_tpl.search_Tpl,{itemInfo:src}));
		 initAddCanEven();
	}, getContextPath() +  '/production/resource/list',$.toJSON({
		category:category,
		city:city,
		type:type,
		accredit:accredit,
		name:name,
		beginPrice:beginPrice,
		endPrice:endPrice,
	}));	
}

function searchProps(){
	
	var category = $('#productType').attr('data-id');
	var city = $('#city').attr('data-id');	
	var name = $('#nomalName').val();
	var beginPrice = $('#beginPrice').val();
	var endPrice = $('#endPrice').val();
	
	var accredit = $('#propsAccredit').attr('data-id');
	
	loadData(function(src){
		 $("#addSetProductInfo").append(juicer(productList_tpl.search_Tpl,{itemInfo:src}));
		 initAddCanEven();
	}, getContextPath() +  '/production/resource/list',$.toJSON({
		category:category,
		city:city,
		accredit:accredit,
		name:name,
		beginPrice:beginPrice,
		endPrice:endPrice,
	}));	
}


function setCheckRedCommon(){
	
	var theAddItem = $('#addSetProductInfo').find('.itemCommon');
	var subId = $(theAddItem[0]).attr('data-subtypeid');
	var itemContent  = $('.itemContent');
	var itemContentNum;
	for (var int = 0; int < itemContent.length; int++) {
		var hasSubId = $(itemContent[int]).attr('data-id');
		if(hasSubId == subId){
			itemContentNum = $(itemContent[int]).find('.itemCommon');
			break;
		}
	}
	
	if(itemContentNum != undefined){
		for (var int2 = 0; int2 < theAddItem.length; int2++) {
			 var itemId = $(theAddItem[int2]).attr('data-id');
			 for (var int3 = 0; int3 < itemContentNum.length; int3++) {
				 if(itemId == $(itemContentNum[int3]).attr('data-id')){
					 $(theAddItem[int2]).addClass('itemCommonRed');
					 $(theAddItem[int2]).find('.addImgContent').hide();
					 $(theAddItem[int2]).find('.cancelImgContent').show();
				 }
			}
		}
	}
	
}

function setCheckRedDevice(){
	
	var theAddItem = $('#addSetProductInfo').find('.itemCommon');
	for (var int3 = 0; int3 < theAddItem.length; int3++) {
		var subId = $(theAddItem[int3]).attr('data-subtypeid');
		var itemContent  = $('.itemContent');
		var itemContentNum;
		for (var int = 0; int < itemContent.length; int++) {
			var hasSubId = $(itemContent[int]).attr('data-id');
			if(hasSubId == subId){
				itemContentNum = $(itemContent[int]).find('.itemCommon');
				if(itemContentNum.length > 0){
					for (var int2 = 0; int2 < theAddItem.length; int2++) {
						 var itemId = $(theAddItem[int2]).attr('data-id');
						 for (var int3 = 0; int3 < itemContentNum.length; int3++) {
							 if(itemId == $(itemContentNum[int3]).attr('data-id')){
								 $(theAddItem[int2]).addClass('itemCommonRed');
							 }
						}
					}
				}
			}
		}
	}
}


function initAddCanEven(){
	
	$('.addImgContent').off('click').on('click',function(){
		$(this).parent().parent().parent().addClass('itemCommonRed');
		$(this).parent().parent().parent().addClass('itemCommonRedS');
		$(this).hide();
		$(this).parent().find('.cancelImgContent').show();
		var item = $(this).parent().parent().parent();
		toGetAddItem(item);
	});
	
	$('.cancelImgContent').off('click').on('click',function(){
		$(this).parent().parent().parent().removeClass('itemCommonRed');
		$(this).parent().parent().parent().removeClass('itemCommonRedS');
		$(this).hide();
		$(this).parent().find('.addImgContent').show();
		var item = $(this).parent().parent().parent();
		toGetDelItem(item);
	});
	
	//标记
	var productType = $('#productType').attr('data-id');
	
	if(productType == 'device'){
		setCheckRedDevice();
	}else{
		setCheckRedCommon();
	}
	
}


function chooseType(){
	initSelect();
	$('body').off('click').on('click',function(){
		$('ul').slideUp();
		$('.setMultSelect').slideUp();
		$('.oredrTypeSelect').removeClass('selectColor');
		$('.orderMultSelect ').removeClass('selectColor');
		$('.orderSelect ').removeClass('selectColor');
	});
	
	$('.orderMultSelect').off('click').on('click',function(e){
		$('ul').slideUp();
		$('.orderSelect').removeClass('selectColor');
		if(!$(this).hasClass('selectColor')){
			$(this).removeClass('selectColor');
			$(this).find('.setMultSelect').slideDown();
			$(this).addClass('selectColor');
		}else{
			$(this).addClass('selectColor');
			$(this).find('.setMultSelect').slideUp();
			$(this).removeClass('selectColor');
		}
		e.stopPropagation();
	});
	
	$('.multSelectEven div').off('click').on('click',function(e){
		$('ul').slideUp();
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
	
/*	$('#productList div').off('click').on('click',function(e){
		$('.setMultSelect').slideUp();
		var parentText = $(this).text();
		var parentId = $(this).attr('data-id');
		$('#productType').text(parentText);
		$('#productType').attr('data-id',parentId);
		
		if(parentText == '演员组'){
			$('.showUnmInfo').hide();
			$('.show3').show();
			$('#searchName').show();
			toCleanAdd();
			
		}
		if(parentText == '导演组'){
			$('.showUnmInfo').hide();
			$('.show4').show();
			$('#searchName').show();
			toCleanAdd();
		}
		
		$('.orderMultSelect').removeClass('selectColor'); 
		e.stopPropagation();
	});
	*/
	$('.findTYpe').off('click').on('click',function(e){
		$('.oSelect').slideUp();
		var parentText = $(this).text();
		var parentId = $(this).attr('data-id');
		$('#productType').text(parentText);
		$('#productType').attr('data-id',parentId);
		$('.orderSelect').removeClass('selectColor'); 
		$('.oSelect').slideUp();
		if(parentText == '演员组'){
			$('.showUnmInfo').hide();
			$('.show3').show();
			$('#searchName').show();
			toCleanAdd();			
		}else if(parentText == '导演组'){
			$('.showUnmInfo').hide();
			$('.show4').show();
			$('#searchName').show();
			toCleanAdd();
		}else if(parentText == '设备'){
			$('.showUnmInfo').hide();
			$('.show2').show();
			$('#searchName').hide();
			toCleanAdd();
		}else if(parentText == '场地'){
			$('.showUnmInfo').hide();
			$('.show1').show();
			$('#searchName').show();
			toCleanAdd();
		}else if(parentText == '摄影师'){
			$('.showUnmInfo').hide();
			$('.show5').show();
			$('#searchName').show();
			toCleanAdd();
		}else if(parentText == '灯光师'){
			$('.showUnmInfo').hide();
			$('.show6').show();
			$('#searchName').show();
			toCleanAdd();
		}else if(parentText == '剪辑师'){
			$('.showUnmInfo').hide();
			$('.show7').show();
			$('#searchName').show();
			toCleanAdd();
		}else if(parentText == '包装师'){
			$('.showUnmInfo').hide();
			$('.show8').show();
			$('#searchName').show();
			toCleanAdd();
		}else if(parentText == '调色师'){
			$('.showUnmInfo').hide();
			$('.show9').show();
			$('#searchName').show();
			toCleanAdd();
		}else if(parentText == '道具师'){
			$('.showUnmInfo').hide();
			$('.show10').show();
			$('#searchName').show();
			toCleanAdd();
		}else if(parentText == '美术师'){
			$('.showUnmInfo').hide();
			$('.show11').show();
			$('#searchName').show();
			toCleanAdd();
		}else if(parentText == '服装师'){
			$('.showUnmInfo').hide();
			$('#searchName').show();
			toCleanAdd();
		}else if(parentText == '化妆师'){
			$('.showUnmInfo').hide();
			$('.show13').show();
			$('#searchName').show();
			toCleanAdd();
		}else if(parentText == '录音师'){
			$('.showUnmInfo').hide();
			$('.show14').show();
			$('#searchName').show();
			toCleanAdd();
		}else if(parentText == '服装'){
			$('.showUnmInfo').hide();
			$('.show15').show();
			$('#searchName').show();
			toCleanAdd();
		}else if(parentText == '道具'){
			$('.showUnmInfo').hide();
			$('.show16').show();
			$('#searchName').show();
			toCleanAdd();
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
		 search_Tpl:[
		              " {@each itemInfo.resources as item}"+ 
					  "	<div class=' {@if item.picScale == 2 }itemContentFive{@/if} {@if item.picScale == 1 }itemContentFour{@/if} itemCommon' data-picScale='${item.picScale}' data-id='${item.id}' data-type='${item.type}' data-price='${item.price}' data-name='${item.name}' data-mainPhoto='${item.mainPhoto}' data-typeId='${item.typeId}' data-typeName='${item.typeName}' data-categoryId='${item.categoryId}' data-category='${item.category}' data-subTypeId='${item.subTypeId}' data-subType='${item.subType}'>"+
		              '		<img src="' + getResourcesName()+ '${item.mainPhoto}" alt=${item.typeName}>'+
					  "		<img class='checkRed' src='/resources/images/flow/checkRed.png'>"+
					  "		<div class='info'>"+
					  "		        <div class='who'>{@if item.name!=null}${item.name} / {@/if}${item.typeName}</div>"+
					  "		        <div class='price'>{@if item.price > 99999}￥99999+ /天{@/if} {@if item.price < 99999}￥${item.price} /天{@/if}  </div>"+
					  "		</div>"+
					  "		<div class='showTool'>"+
					  "		    <div class='toolDiv'>"+
					  "		    		<div class='addImgContent'>添加</div><div class='cancelImgContent' >取消</div><div>查看详情</div>"+
					  "		    </div>"+
					  "		</div>"+
					  "	</div>"+
					  "	{@/each}"
		 ].join(""),
		 item_Tpl:[
					  "	<div class='{@if item.picScale == 2 }itemContentFive{@/if} {@if item.picScale == 1 }itemContentFour{@/if} itemCommon' data-picScale='${item.picScale}' data-id='${item.id}' data-type='${item.type}' data-price='${item.price}' data-name='${item.name}' data-mainPhoto='${item.mainPhoto}' data-typeId='${item.typeId}' data-typeName='${item.typeName}' data-categoryId='${item.categoryId}' data-category='${item.category}' data-subTypeId='${item.subTypeId}' data-subType='${item.subType}'>"+
		              '		<img src="' + getResourcesName()+ '${item.mainPhoto}" alt=${item.typeName}>'+
					  "		<img class='checkRed' src='/resources/images/flow/checkRed.png'>"+
					  "		<div class='info'>"+
					  "		        <div class='who'>{@if item.name!=null}${item.name} / {@/if}${item.typeName}</div>"+
					  "		        <div class='price'>{@if item.price > 99999}￥99999+ /天{@/if} {@if item.price < 99999}￥${item.price} /天{@/if}</div>"+
					  "		</div>"+
					  "		<div class='showTool'>"+
					  "		    <div class='toolDiv'>"+
					  "		    		<div class='moveItem'>移除</div><div>查看详情</div>"+
					  "		    </div>"+
					  "		</div>"+
					  "	</div>"
		 ].join(""),
}

function resourcesEntity(id,type,price,name,mainPhoto,typeId,typeName,categoryId,category,subTypeId,subType,picScale){
	this.id =  id;
	this.type = type;
	this.price = price;
	this.name = name;
	this.mainPhoto = mainPhoto;
	this.typeId = typeId;
	this.typeName = typeName;
	this.categoryId = categoryId;
	this.category = category;
	this.subTypeId = subTypeId;
	this.subType = subType;
	this.picScale =  picScale;
}

//同步添加
function toGetAddItem(item){
	
		    nowItem = new Array();
			var theValue = $(item);
			var id= theValue.attr('data-id');
			var type= theValue.attr('data-type');
			var price= theValue.attr('data-price');
			var name= theValue.attr('data-name');
			var mainPhoto= theValue.attr('data-mainPhoto');
			var typeId= theValue.attr('data-typeId');
			var typeName= theValue.attr('data-typeName');
			var categoryId= theValue.attr('data-categoryId');
			var category= theValue.attr('data-category');
			var subTypeId= theValue.attr('data-subTypeId');
			var subType= theValue.attr('data-subType');
			var picScale= theValue.attr('data-picScale');
			nowItem.push(new resourcesEntity(id,type,price,name,mainPhoto,typeId,typeName,categoryId,category,subTypeId,subType,picScale));
            setReShow(nowItem,1);

	
}
//同步删除
function toGetDelItem(item){
	
    nowItem = new Array();
	var theValue = $(item);
	var id= theValue.attr('data-id');
	var type= theValue.attr('data-type');
	var price= theValue.attr('data-price');
	var name= theValue.attr('data-name');
	var mainPhoto= theValue.attr('data-mainPhoto');
	var typeId= theValue.attr('data-typeId');
	var typeName= theValue.attr('data-typeName');
	var categoryId= theValue.attr('data-categoryId');
	var category= theValue.attr('data-category');
	var subTypeId= theValue.attr('data-subTypeId');
	var subType= theValue.attr('data-subType');
	var picScale= theValue.attr('data-picScale');
	nowItem.push(new resourcesEntity(id,type,price,name,mainPhoto,typeId,typeName,categoryId,category,subTypeId,subType,picScale));
	clearCancelItem(nowItem);
    toCleanNullItem();
    
}

function clearCancelItem(item){
	for (var int = 0; int < item.length; int++) {
		 var theItem = $(item)[int];
		 var checkMidItem = $('.checkMidItem');
				for (var int2 = 0; int2 < checkMidItem.length; int2++) {
					var checkSame = $(checkMidItem[int2]).attr('data-id');
					if(checkSame == theItem.subTypeId){
						//去重
						   var cancelSame = $(checkMidItem[int2]).find('.itemCommon');
						   var hasItem = false;
							   for (var int3 = 0; int3 < cancelSame.length; int3++) {
								    var theNum = $(cancelSame[int3]).attr('data-id');
								    if(theNum == theItem.id){
								    	$(cancelSame[int3]).remove();
								    	break;
								    }
							   }
					  }
				}
		 }
}

/*function toGetAddItem(){
		
	var itemCommonRed = $('.itemCommonRedS');
	
	if(itemCommonRed.length > 0){
		nowItem = new Array();
		for (var int = 0; int < itemCommonRed.length; int++) {
			var theValue = $(itemCommonRed[int]);
			var id= theValue.attr('data-id');
			var type= theValue.attr('data-type');
			var price= theValue.attr('data-price');
			var name= theValue.attr('data-name');
			var mainPhoto= theValue.attr('data-mainPhoto');
			var typeId= theValue.attr('data-typeId');
			var typeName= theValue.attr('data-typeName');
			var categoryId= theValue.attr('data-categoryId');
			var category= theValue.attr('data-category');
			var subTypeId= theValue.attr('data-subTypeId');
			var subType= theValue.attr('data-subType');
			var picScale= theValue.attr('data-picScale');
			nowItem.push(new resourcesEntity(id,type,price,name,mainPhoto,typeId,typeName,categoryId,category,subTypeId,subType,picScale));
		}
		$('.addModel').hide();
		setReShow(nowItem,1);
		$("#addSetProductInfo").html('');
	}else{
		$('.addModel').hide();
		$("#addSetProductInfo").html('');
	}
}*/

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
