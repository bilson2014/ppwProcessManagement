var image_max_size = 20*1024*1024; // 20M
var image_err_msg = '图片大小超出20MB上限,请重新上传!';
var upload_Video;
var upload_Update;
var successIntervalObj; // timer变量，控制时间
var nowPoint = [[1,2,3],[4,5,6]];
var setData = new Array();
var setCache = new Array();
//头像裁剪参数 start
var jcrop_api;
var x;
var y;
var x2;
var y2;
var w;
var h;
var timer = null;
var loadTime = 0;
//头像裁剪参数 end
var delImgGroup = '';
var inputNum = '';
$().ready(function() {
	document.domain = getUrl();
	initOption();
	initPos();
	imgUpload.init();
	imgUpdate.init();
	setInterval(autoSave,cacheTime);
	loadSave();
});

function initPos(){
	
	$('.toHide').off('click').on('click',function(){		
		$('#topBtn').addClass('topBtn');
		$('#botBtn').addClass('botBtn');
		$('#info').addClass('showInfoWord');
		$('.toHide').hide();
		
		$('#topBtn').off('click').on('click',function(){
			$('#picker .webuploader-element-invisible').click();
			returnOld();
		});
		$('#botBtn').off('click').on('click',function(){
			$(".addItem").before(juicer(videoList_tpl.upload_Tpl,{textarea:'',text:'',file:'/resources/images/flow/def.jpg',path:''}));
			initOption();
			initSortable();
			returnOld();
		});	
	});		
	
	$(".addItem").hover(function(){
	},function(){
		returnOld();
	});
	
	
}


function returnOld(){
	
	$('#topBtn').removeClass('topBtn');
	$('#botBtn').removeClass('botBtn');
	$('#info').removeClass('showInfoWord');
	$('.toHide').show();
	
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
		
	}, getContextPath() + '/continuity/list/synergetic','');
	
}

//回显
function reShow(proId){
	
	loadData(function(src){
		setReShow(src);		
	}, getContextPath() + '/continuity/get/'+proId,'');
	
}

function setReShow(item){
	$('.boxItem').removeClass('active');
	$('#projectName').text(item.projectName);
	$('#id').val(item.id);
	$('#projectId').val(item.projectId);
	$('#createTime').val(item.createTime);	
	var imgItem = item.scripts;
	$(".imgItem ").remove();
	for (var int = 0; int < imgItem.length; int++) {
		var path = imgItem[int].picture;
		var imgPath = getResourcesName() + path;
		
		if(path == ''){
			imgPath = '/resources/images/flow/def.jpg';
		}
		
		var text = imgItem[int].type;
		var des  = imgItem[int].description;

		$(".addItem").before(juicer(videoList_tpl.upload_Tpl,{textarea:des,text:text,file:imgPath,path:path}));
	}
	
	var checkImgType = $('.checkImgType');
	for (var int2 = 0; int2 < checkImgType.length; int2++) {
		 var textId = $(checkImgType[int2]).attr('data-id');
		 var ulId = $('.checkImgType').parent().find('ul li');
		 for (var int3 = 0; int3 < ulId.length; int3++) {
			  var ulTextId = $(ulId[int3]).attr('data-id');
			  if(ulTextId == textId){
				  $(checkImgType[int2]).text($(ulId[int3]).text());
				  continue;
			  }
		}
	}
	
	
	var dimensionId = item.dimensionId; 
	var pictureRatio = item.pictureRatio;
	var videoStyle = item.videoStyle;
	
    
	if(dimensionId == 30 || dimensionId == 60 ||  dimensionId == 120 ||  dimensionId == 180 ||  dimensionId == 300 ||  dimensionId == 600){
		getActiveVal($('#time .boxItem'),dimensionId);
	}else{
        $('#setother').attr('data-id',dimensionId);	
        $('#setother').find('input').val(dimensionId);
        $('#setother').addClass('active');
	}
	getActiveVal($('#videoType .boxItem'),pictureRatio);
	getActiveVal($('#videoStyleS .boxItem'),videoStyle);
		
	function getActiveVal(id,val){
		var setDId = id;
		for (var int4 = 0; int4 < setDId.length; int4++) {
			if($(setDId[int4]).attr('data-id') == val){
				$(setDId[int4]).addClass('active');
				return;
			}
		}
	}
	initImgSize();
	initOption();
	initSortable();
	$('#loadProductModel').hide();
			
}


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
						 $('#projectName').text(modelVal.text());
                    	 $('#projectId').val(modelVal.attr('data-id'));
					}else{
                         $('#sameProject').show();
                         $('#toSame').off('click').on('click',function(){
                        	 getValue(modelVal.attr('data-id'),0);
                        	 $('#sameProject').hide();
                        	 $('#projectName').text(modelVal.text());
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
	}, getContextPath() + '/continuity/synergetic/listByName', $.toJSON({
		projectName:'',
	}));
}

function initOption(){
	
	$(window.parent.document).find('.frame').css('height',$('.page').height() + 50);
	newSelectCheck();
	initSelect();
	initCheckBox();
	saveToproject();
	$('.closeModel,#cancleLoadProduct').off('click').on('click',function(){
		$('#loadProductModel').hide();
	});
	$('.openTool').off('click').on('click',function(){
		openProjectModel();
	});
	$('.download').off('click').on('click',function(){
		
		if(checkError()){
			getValue('',1); 
		}
		
	});
	delImgEven();
	reUpdate();
	
	$('#setSecond').bind(' input propertychange ',function(){
		
			if(isNumber($(this).val())){
				$(this).parent().attr('data-id',$(this).val());
			}
		
	});
}

//保存到项目
function saveToproject(){	
	$('#saveProject').off('click').on('click',function(){
		if(checkError()){
			if($('#id').val()!='' && $('#id').val()!=null && $('#id').val()!= undefined){
				getValue($('#projectId').val(),0);
			}else{
				getMyProject();			
			}
		}
	});
}

function initCheckProject(){
	
	 $('.modelProItem').off('click').on('click',function(){
			$('.modelProItem').removeClass('modelPActive');
			$(this).addClass('modelPActive');
	});
	
}

function optEntity( type,picture,description){
	this.type =  type;
	this.picture = picture;
	this.description = description;
}

function getValue(projectId,who){
		
	setData = new Array();
	var imgItem = $('.imgItem');
	for (var int = 0; int < imgItem.length; int++) {
		 var type = $(imgItem[int]).find('.checkImgType').attr('data-id');
		 var image = $(imgItem[int]).find('.loadImg').attr('data-id');
		 var text = $(imgItem[int]).find('.checkImgText').val();
/*		 if(type == ""){
			 type = null;
		 }*/
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
		   form.append('videoStyle',videoStyle);
		   form.append('pictureRatio',pictureRatio);
		   form.append('scriptContent',setArray);
		   form.append('projectId',projectId);
		   form.append('createTime',createTime);

		   xhr.open('POST', url, true);        // 也可以使用POST方式，根据接口
		   xhr.responseType = "blob";    // 返回类型blob
		   // 定义请求完成的处理函数，请求前也可以增加加载框/禁用下载按钮逻辑
		   successToolTipShow('文件制作中');
		   xhr.onload = function () {
		       // 请求完成
		       if (this.status === 200) {  
		    	   // 返回200
		    	   var name = $('#projectName').text();
		    	   if(name == "未命名项目"){
		    		   name = "分镜脚本"
		    	   }else{
		    		   name = "《"+name+"》分镜脚本";
		    	   }
		    	   successToolTipShow('导出成功');
		           var blob = this.response;

			   // 转换完成，创建一个a标签用于下载
		           var a = document.createElement('a');
		           a.download = name+'.pdf';
		           a.href =URL.createObjectURL(blob);
		           $("body").append(a);    // 修复firefox中无法触发click
		           a.click();
		           $(a).remove();

		         /*  var reader = new FileReader();
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
		           */
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
			}, getContextPath() + '/continuity/save', $.toJSON({
				 scripts:setData,
				// name:storyName,
				 delImgs:delImgGroup,
				 videoStyle:videoStyle,
				 pictureRatio:pictureRatio,
				 dimensionId:dimensionId,
				 projectId:projectId,
				 id:$('#id').val()
			}));
	}
		
}

function IEVersion() {
    var userAgent = navigator.userAgent; //取得浏览器的userAgent字符串  
    var isIE = userAgent.indexOf("compatible") > -1 && userAgent.indexOf("MSIE") > -1; //判断是否IE<11浏览器  
    var isEdge = userAgent.indexOf("Edge") > -1 && !isIE; //判断是否IE的Edge浏览器  
    var isIE11 = userAgent.indexOf('Trident') > -1 && userAgent.indexOf("rv:11.0") > -1;
    if(isIE) {
        var reIE = new RegExp("MSIE (\\d+\\.\\d+);");
        reIE.test(userAgent);
        var fIEVersion = parseFloat(RegExp["$1"]);
        if(fIEVersion == 7) {
            return 7;
        } else if(fIEVersion == 8) {
            return 8;
        } else if(fIEVersion == 9) {
            return 9;
        } else if(fIEVersion == 10) {
            return 10;
        } else {
            return 6;//IE版本<=7
        }   
    } else if(isEdge) {
        return 'edge';//edge
    } else if(isIE11) {
        return 11; //IE11  
    }else{
        return -1;//不是ie浏览器
    }
}


function newSelectCheck(){
	
	$('.productLine').off('click').on('click',function(e){
		$('.selectUl').hide();
		if($(this).hasClass('selectColor')){
			$('.selectUl').slideUp();
			$(this).removeClass('selectColor');
		}
		else
		{
			$('.productLine').removeClass('selectColor');
			$(this).find('.selectUl').slideDown();
			$(this).addClass('selectColor');
		}
		e.stopPropagation();
	});
	$('.selectUl li').off('click').on('click',function(e){
		 var id = $(this).attr('data-id');
	   	 $(this).parent().parent().find('div').text($(this).text());
	   	 $(this).parent().parent().find('div').attr('data-id',id);
	   	 $(this).parent().slideUp();
	   	 $('.productLine').removeClass('selectColor');
	     e.stopPropagation();
	});
	
}

function initImgSize(){
	var needWidth = $('.loadImg').css('width');
	var needHeight = $('.loadImg').css('height');	
	var changeImg = $('.backgroundImg');
	$(changeImg).each(function() {
		$(this).load(function(){
			var realHeight = $(this).height();
			var realWidth  = $(this).width();			
		/*	if(realHeight >= realWidth){				
				$(this).css('height',needHeight).css('width','auto');
			}
			else{
				$(this).css('height','auto').css('width',needWidth);*/
				if(realWidth/realHeight < (16/9)){
					$(this).css('height',needHeight).css('width','auto');
				}else{
					$(this).css('height','auto').css('width',needWidth);
				}
			//}
		});
    });
}

function initSortable(){
	$( ".setImg" ).sortable({
		cursor:'move',
		items:'.imgItem',
		opacity:0.6,
	});
	$( ".setImg" ).disableSelection();      
}

function initCheckBox(){
	
	getBoxHasInput($('#time .boxItem'));
	getBox($('#videoType .boxItem'));
	
	$('.killItem').off('click').on('click',function(){
		 $('.killItem .boxItem').removeClass('active');
		 $(this).find('.boxItem').addClass('active');
	});
	
	function getBox(id){
		id.on('click',function(){
			id.removeClass('active');
			$(this).addClass('active');
		})
	}
	
	function getBoxHasInput(id){
		id.on('click',function(){
			id.removeClass('active');
			$(this).addClass('active');
			$('#setSecond').val('');
			$('#setother').attr('data-id','');
		})
	}
	
}

function checkError(){
		
	/*var checkImgType = $('.checkImgType');
	if(checkImgType.length>0){
		for (var int = 0; int < checkImgType.length; int++) {
			  var val = $(checkImgType[int]).attr('data-id');
			  if(val == '' || val == null || val ==undefined){
					successToolTipShow('镜头类型未填写');
					 return false;
				}
		}
	}else{
		successToolTipShow('镜头未添加');
		return false;
	}*/
	
/*	var checkImgText = $('.checkImgText');
	if(checkImgText.length>0){
		for (var int = 0; int < checkImgText.length; int++) {
			  var val = $(checkImgText[int]).val();
			  if(val == '' || val == null || val ==undefined){
					successToolTipShow('镜头要求未填写');
					 return false;
				}
		}
	}else{
		successToolTipShow('镜头未添加');
		return false;
	}*/
		
	/*var storyName = $('#storyName').val();
	if(storyName == '' || storyName == null || storyName ==undefined){
		successToolTipShow('脚本名称未填写');
		return false;
	}*/
	
	//附加
	/*var howManyBox = $('.onebox');
	for (var int = 0; int < howManyBox.length; int++) {
			 var thisItem = $(howManyBox[int]).find('.boxItem');
			    for (var j = 0; j < thisItem.length; j++) {
					 if($(thisItem[j]).hasClass('active')){
						 break;
					 }
					 successToolTipShow('视频信息不完整');
					 return false;
				}
	}*/
	
	var time = $('#time .active');
	if(!time.length > 0){
		successToolTipShow('视频时长未选择');
		return false;
	}
	
	var hasother = $('#setSecond');
	
	if(hasother.parent().hasClass('active')){
		
		if(hasother.val()=='' ||hasother.val()==null||hasother.val()==undefined){
			successToolTipShow('视频时长未填写');
			return false;
		}
		
	}
	
	var videoType = $('#videoType .killDiv  .active');
	if(!videoType.length > 0){
		successToolTipShow('画幅比例未选择');
		return false;
	}
	
	var videoStyle = $('#videoStyleS .active');
	if(!videoStyle.length > 0){
		successToolTipShow('影片风格未选择');
		return false;
	}
	
	return true;
	
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

//文件批量上传
var imgUpload = {
		init : function() {
			//批量上传
			this.multipUploadFile();
		},

		multipUploadFile:function(){
			upload_Video && upload_Video.destroy();
			var picker =$('#picker'); 
			upload_Video = WebUploader.create({
				auto:true,
				swf : '/resources/lib/webuploader/Uploader.swf',
				server : '/web/upload',
				timeout:60*60*1000,
				pick : picker,
				fileSingleSizeLimit : image_max_size,
				threads :1,
				duplicate :true,
				multiple:true,
				accept :{
				    title: 'Images',
				    extensions: 'jpg,png,jpeg',
				    mimeTypes: 'image/jpeg,image/png'
				}
			});
			
			upload_Video.on('uploadProgress',function(file, percentage) {
				
			});

			upload_Video.on('uploadSuccess', function(file,response) {
				
				if(response.code == 0){
					    var path = response.result;
						var imgPath = getResourcesName() + path;
						$(".addItem").before(juicer(videoList_tpl.upload_Tpl,{textarea:'',text:'',file:imgPath,path:path}));
						initImgSize();						
						initSortable();
						delImgEven();	
						initSelect();
						initCheckBox();
						reUpdate();
						$(window.parent.document).find('.frame').css('height',$('.page').height() + 50);
				}else{
					successToolTipShow('图片处理失败,请联系客服协助您上传(400-660-9728)');
				}
				
			});
			upload_Video.on('uploadError', function(file,reason) {
				successToolTipShow(reason);
			});
			upload_Video.on('error', function(type) {
				 if (type=="Q_TYPE_DENIED"){
					 	successToolTipShow('请上传正确格式的图片');
			        }else if(type=="F_EXCEED_SIZE"){
						successToolTipShow(image_err_msg);
			        }
			});

		}
		
		
}

//删除图片
function delImgEven(){
	
	$('.delLoadImg').off('click').on('click',function(){
		var thiItem = $(this);
		var path = $(this).parent().attr('data-id');
		
		$('#checkSureModel').show();
		$('.closeBtn,#cModel').off('click').on('click',function(){
			$('#checkSureModel').hide();
		});
		$('#tModel').off('click').on('click',function(){
			delImgGroup += path +';';
			thiItem.parent().parent().remove();
			$('#checkSureModel').hide();
		});
		
	})
	
}

//删除图片
function reUpdate(){
	
	$('.updateImg').off('click').on('click',function(){
		$(this).addClass('hasUpdate');
		$('#updateImg .webuploader-element-invisible').click();
	})
	
}

//图片更新
var imgUpdate = {
		init : function() {
			//批量上传
			this.uploadFile();
		},
		uploadFile:function(){
			upload_Update && upload_Update.destroy();
			var picker =$('#updateImg'); 
			upload_Update = WebUploader.create({
				auto:true,
				swf : '/resources/lib/webuploader/Uploader.swf',
				server : '/web/upload',
				timeout:60*60*1000,
				pick : picker,
				fileSingleSizeLimit : image_max_size,
				threads :1,
				duplicate :true,
				multiple:false,
				accept :{
				    title: 'Images',
				    extensions: 'jpg,png,jpeg',
				    mimeTypes: 'image/jpeg,image/png'
				}
			});
			
			upload_Update.on('uploadSuccess', function(file,response) {
				var uploaderId = '#rt_'+file.source.ruid;
			//	var nowEven = $(uploaderId).parent().parent();
				var nowEven = $('.hasUpdate').parent();
				var delImg = nowEven.attr('data-id');	
				if(response.code == 0){
					    delImgGroup += delImg +';';
					    var path = response.result;
						var imgPath = getResourcesName() + path;
						nowEven.find('.backgroundImg').attr('src',imgPath);
						nowEven.attr('data-id',path);
						initImgSize();
						$('.updateImg').removeClass('hasUpdate');
				}else{
					successToolTipShow('图片处理失败,请联系客服协助您上传(400-660-9728)');
				}
				
			});
			upload_Update.on('uploadError', function(file,reason) {
				successToolTipShow(reason);
			});
			
			upload_Update.on('filesQueued', function(file) {
				if(file.length > 1){
					successToolTipShow('只能选择一张图片替换');
					upload_Update.reset();
				}
			});
			
			upload_Update.on('error', function(type) {
				 if (type=="Q_TYPE_DENIED"){
					 	successToolTipShow('请上传正确格式的图片');
			        }else if(type=="F_EXCEED_SIZE"){
						successToolTipShow(image_err_msg);
			        }
			});

		}
}

var videoList_tpl = {
		upload_Tpl:[
		"<div class='imgItem'>"+
        "<div class='orderSelect'>"+
        "        <div class='imgType checkImgType' data-id='${text}'>请选择镜头类型</div>"+
        "        <img src='/resources/images/flow/selectS.png'>"+
        "        <ul class='oSelect' style='display: none;'>"+
        "           <li data-id='1'>极远景</li>"+
        "           <li data-id='2'>远景</li>"+
        "           <li data-id='3'>大全景</li>"+
        "           <li data-id='4'>全景</li>"+
        "           <li data-id='5'>中景</li>"+
        "           <li data-id='6'>近景</li>"+
        "           <li data-id='7'>特写</li>"+
        "           <li data-id='8'>大特写</li>"+
        "        </ul>    "+
	    " </div>"+
	    " <div class='loadImg' data-id='${path}'>"+
	    "        <div class='updateImg'>重新上传</div>"+
	    "        <img class='delLoadImg' src='/resources/images/flow/del.png'>"+
	    "        <img class='backgroundImg' src='${file}'>"+
	    " </div>"+
	    " <textarea class='checkImgText' placeholder='请输入镜头要求...' >${textarea}</textarea>"+
        "</div>"
		].join(""),
        project_Tpl:[
             "<div class='modelProItem' data-showId='${file.id}' data-id='${file.projectId}'>${file.projectName}</div>"
    		].join("")
}

function  isNumber(number){ // 是否是数字
	reg = /^[1-9]+[0-9]*]*$/;
	if(number.match(reg))
		return true;
	else
		return false;
}

//缓存

function autoSave(){
		
		setData = new Array();
		var imgItem = $('.imgItem');
		var isDiffer = false;
		for (var int = 0; int < imgItem.length; int++) {
			 var type = $(imgItem[int]).find('.checkImgType').attr('data-id');
			 var image = $(imgItem[int]).find('.loadImg').attr('data-id');
			 var text = $(imgItem[int]).find('.checkImgText').val();
			 setData.push(new optEntity(type,image,text));
			 
			 if(setCache.length > 0){
				 if(setCache[0].scripts.length != imgItem.length){
					 isDiffer = true;
				 }

				 else if(setCache[0].scripts[int].type!=type || setCache[0].scripts[int].picture!=image || setCache[0].scripts[int].description!=text){					 
					 isDiffer = true;
				 }
			 }
		}
		var storyName = $('#storyName').text();
		var projectId =$('#projectId').val();
		var id = $('#id').val();
		var createTime = $('#createTime').val();
		var projectName = $('#projectName').text();
		var dimensionId = $('#time .active').attr('data-id');	
        if(dimensionId == undefined){
        	dimensionId = "";
        }
		var pictureRatio = $('#videoType .active').attr('data-id');
		 if(pictureRatio == undefined){
			 pictureRatio = "";
	        }
		var videoStyle = $('#videoStyleS .active').attr('data-id');
		 if(videoStyle == undefined){
			 videoStyle = "";
	        }
			 
		 if(setCache.length > 0){
			 if(setCache[0].createTime != createTime || setCache[0].projectName != projectName || setCache[0].projectId != projectId || setCache[0].id != id ||setCache[0].storyName != storyName || setCache[0].dimensionId != dimensionId || setCache[0].pictureRatio != pictureRatio || setCache[0].videoStyle != videoStyle  ){
				 isDiffer = true;
			 }		
		 }
		
		 if(setCache.length == 0){
			setCache = new Array();
			setCache.push(new cacheEntity(setData,storyName,dimensionId,pictureRatio,videoStyle,id,projectId,projectName,createTime,delImgGroup));
			saveCache();
		 }
		 else if (isDiffer){
			setCache = new Array();
			setCache.push(new cacheEntity(setData,storyName,dimensionId,pictureRatio,videoStyle,id,projectId,projectName,createTime,delImgGroup));
			saveCache();
		 }
				
}

function cacheEntity(setImg,storyName,dimensionId,pictureRatio,videoStyle,id,projectId,projectName,createTime,delImgGroup){
	this.scripts =  setImg;
	this.storyName = storyName;
	this.dimensionId = dimensionId;
	this.pictureRatio = pictureRatio;
	this.videoStyle = videoStyle;
	this.id = id;
	this.projectId = projectId;
	this.projectName = projectName;
	this.createTime = createTime;
	this.delImgGroup = delImgGroup;
}

function saveCache(){
	
    var jsonStr = JSON.stringify(setCache);
	
	loadData(function(item){
		
	
		
	}, getContextPath() + '/cache/save', $.toJSON({
		type:2,
		dataContent:jsonStr
	}));
		
}

function loadSave(){
		
		loadData(function(res){		
			if(res.result){
				var itemRes = jQuery.parseJSON(res.msg);
				$('#projectId').val(itemRes[0].projectId);
				$('#id').val(itemRes[0].id);
				delImgGroup = itemRes[0].delImgGroup;
				setReShow(itemRes[0]);	
				$(window.parent.document).find('.frame').css('height',$('.page').height() + 50);
			}
		}, getContextPath() + '/cache/get',$.toJSON({
			type:2
		}));
		
}
