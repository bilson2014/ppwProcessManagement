var InterValObj; // timer变量，控制时间  
var count = 120; // 间隔函数，1秒执行  
var curCount; // 当前剩余秒数 
var upload_Video;
var video_max_size = 200*1024*1024; // 200MB
var video_err_msg = '视频大小超出200M上限,请重新上传!';
$().ready(function() {
	openInfoCard();
	initEvenInfo();
	initSelect();
	flagEven();
	// 加载动态表单
	pageInit();
	addForm();
	$(window.parent.document).find('.frame').css('height',$('.pages').height() + 300);
	checkState();
	getFileInfo();
	//finishTalk();
	
	var body = createTalkInfo();
	$('.setAreaDiv').append(body);
	initAddTalk();
});

function getFileInfo(){
	loadData(function(res){
		var res = res;
	/*	var body = $('.utoInfo');
		body.html('');*/
		if(res != null && res != undefined){
			for (var int = 0; int < res.length; int++) {
				   var html =createUserInfo(res[int].teamId,res[int].teamName,res[int].linkman,res[int].phoneNumber);
				   body.append(html);
			};
			
		}
	}, getContextPath() + '/resource/version/'+$('#currentTaskId').val(),null);
}


function checkState(){
	var href = window.location.href;
    var state = href.substr(href.lastIndexOf("?")+1,href.length);
    if(state.trim() != "task"){
    	$('#daiban').hide();
    }
}

function pageInit(){
	$('#toFinish').off('click').on('click',function(){
		$('#autoSet').show();
		$('#autoSet .item input').val('');
		$('#autoSet .item input').attr('data-id','');
	});
	var taskName = $('#taskName').val();
	 if(taskName == null || taskName == "" || taskName == undefined )	{
  	   $('#daiban').hide();
     }
	 var isStage = $('#taskStage').val();
/*	 if(isStage == "沟通阶段"){
 
	 }*/
 if(isStage == "方案阶段"){
		 $('.flowIcon').addClass('step2');
	 }
 if(isStage == "商务阶段"){
	 $('.flowIcon').addClass('step3');
 }
 if(isStage == "制作阶段"){
	 $('.flowIcon').addClass('step4');
 }
 if(isStage == "交付阶段"){
	 $('.flowIcon').addClass('step5');
 }
	 
}

//表单验证
function checkForm(){
	$('#errorInfo').text('');
	var getCheckInfo = $('.checkInfo');
	var checkFlag = true;
	for (var i = 0; i < getCheckInfo.length; i++) {
		var check = $(getCheckInfo[i]).val();
               if(check == null || check == "" || check == undefined )	{
            	   checkFlag = false;
            	   initFormEven();
               }	
	}
	
	if(checkFlag){
		$('#toSubmitForm').prop("type","submit");
		$('.btnInput').off('click');
	}else{
		$('#errorInfo').text('请补充必填信息');
	}
}

function initFormEven(){
	$('.btnInput').off('click').on('click',function(){
		$('.btnInput').off('click');
		checkForm();
	});
	
	dataEven();
	autoInput();
	autoSelect();
}

//上传
function UploadFile(){
	upload_Video && upload_Video.destroy();
	var picker =$('#picker'); 
	upload_Video = WebUploader.create({
		auto:false,
		swf : '/resources/lib/webuploader/Uploader.swf',
		server : '/resource/addResource',
		pick : {
			id:picker,
			multiple :false//弹窗时不允许多选,
		},
		timeout:0,
		fileSingleSizeLimit : video_max_size,
	});
	
	upload_Video.option('formData', {
		resourceName:$('#file').val(),
		taskId : $('#taskId').val(),
		resourceType:$('#file').attr('data-name')
	});
	
	upload_Video.on('fileQueued', function(file) {
	    $('.uploadInput').val(file.name);
	   $('.btnInput').off('click');
	});
/*	upload_Video.on('fileQueued', function(file) {
		//跳转step2.添加信息
		_this.addProductMsg();
	});*/
	// 文件上传过程中创建进度条实时显示。
	/*upload_Video.on('uploadProgress',function(file, percentage) {
		$(".progress-bar").css('width', percentage * 100 + '%');
	});*/
	upload_Video.on('uploadSuccess', function(file,response) {
		
		if(response){
			$('#errorInfo').text('上传成功');
			initFormEven();
		}else{
			$('#errorInfo').text('上传失败');
		}
	});
	upload_Video.on('error', function(type) {
		/* if (type=="Q_TYPE_DENIED"){
				$('#errorInfo').text('请上传mp4格式');
	        }else if(type=="F_EXCEED_SIZE"){
				$('#errorInfo').text(video_err_msg);
	        }*/
	});
	
	$("#uploadVideo").on('click', function() {
		upload_Video.option('formData', {
    		resourceName:$('#file').attr('data-title'),
    		taskId : $('#currentTaskId').val(),
    		resourceType:$('#file').attr('data-name')
    	});
		upload_Video.upload();
		$('#errorInfo').text('上传中...');
	});
}

//动态下拉框

function autoSelect(){
	
	$('.autoSelect').off('click').on('click',function(){
		 $(this).parent().find('ul').show();
	});
	autoSelectUl();
}

function autoSelectUl(){
	
	$('.autoSelectUl li').off('click').on('click',function(){
		$('.autoSelectUl').hide();
			var name = $(this).text();
			var id = $(this).attr('data-id');
		 $(this).parent().parent().find('input').val(name);
		 $(this).parent().parent().find('.hideInput').val(id);
	});
	
}



//动态联动事件

//自动联动客户信息
function autoInput(){
	$('#pt_teamName').bind('input propertychange', function() {
		 $('#pt_teamId').val("");
		var theName = $(this).val();
		 findAutoInfo(theName);
		 $('.utoInfo').show();
		 if(theName == null || theName == ""){
			 $('.utoInfo').hide();
		 }
	});
}

function findAutoInfo(userName){
	loadData(function(res){
		var res = res;
		var body = $('.utoInfo');
		body.html('');
		if(res != null && res != undefined){
			for (var int = 0; int < res.length; int++) {
				   var html =createUserInfo(res[int].teamId,res[int].teamName,res[int].linkman,res[int].phoneNumber);
				   body.append(html);
			};
			autoLi();
		}
	}, getContextPath() + '/team/listByName/'+userName,null);
}

function autoLi(){
	
	$('.utoInfo li').off('click').on('click',function(){
		  $('.utoInfo').hide();
		  var name = $(this).text();
		  var id = $(this).attr('data-id');
		  var linkman = $(this).attr('data-linkman');
		  var phone = $(this).attr('data-phone');
		  $(this).parent().parent().find('input').val(name);
		  $('#pt_teamId').val(id);
		  $('#pt_linkman').val(linkman);
		  $('#pt_telephone').val(phone);
	});
	$('#pt_teamId').parent().hide();
}

function createUserInfo(id,name,linkman,phone){
	var html = '<li data-id="'+id+'"  data-linkman="'+ linkman +'" data-phone="'+ phone +'">'+name+'</li>';
	return html;
}


//加载动态表单
function addForm() {
	loadData(function(datas) {
		var trs = "";
		$.each(datas.taskFormData.formProperties, function() {
			var className = this.required === true ? "required" : "";
			this.value = this.value ? this.value : "";
			var isRe = this.required;
			trs += "<div class='item'>" + createFieldHtml(this, datas, className);
			trs += "</div>";
		});
		//$('#formState').html("<form class='dynamic-form' method='post'><table class='dynamic-form-table'></table></form>");
		$('#setAutoInfo').html("<form class='dynamic-form' method='post'><div class='dynamic-form-table'></div></form>");
		var $form = $('.dynamic-form');
		$form.attr('action', '/project/task/complete/' + $('#currentTaskId').val());
		trs += '<div class="btnInput" id="btnInput"><input id="toSubmitForm" class="btn-c-r" type="button" value="提交"/></div>'
		
		// 添加table内容
		$('.dynamic-form-table').html(trs);
		initFormEven();
		var hasPicker = $('.picker');
		if(hasPicker !=null && hasPicker !="" && hasPicker !=undefined){
			UploadFile();
		}
	}, '/project/get-form/task/' + $('#currentTaskId').val(), null);
}

/**
 * 生成一个field的html代码
 */
function createFieldHtml(prop, obj, className) {
	return formFieldCreator[prop.type.name](prop, obj, className);
}

var formFieldCreator = {
'string': function(prop, datas, className) {
	if(prop.required){
		var result = "<div class='title'>" + prop.name + "<span> *</span></div>";
		var isCheck = "checkInfo";
	}else{
		var result = "<div class='title'>" + prop.name + "</div>";
		var isCheck = "noCheckInfo";
	}
	var isWhat = prop.id.split('_')[0];; 
	if (prop.writable === true) {
		if(prop.id == "pt_teamName"){
			result += "<input type='text' id='" + prop.id + "' name='" + prop.id + "' class='uploadInput "+isCheck+" " + className + "' value='" + prop.value + "' />";
			result += "<ul class='utoInfo'></ul>";
			return result;
		}
		
	     if(isWhat == 'schemeId'  || isWhat == 'superviseId')	{
	    	result += "<input readonly class='autoSelect' id='" + prop.id + "'  class='" + className + "'>";
	 		result += "<input type='hidden' class='hideInput' name='" + prop.id + "' >";
	 		result += "<img class='autoImg' src='/resources/images/flow/selectOrder.png'>";
	 		result += "<ul class='autoSelectUl'>";
	 		$.each(datas[prop.id], function(k, v) {
	 			result += "<li data-id='" + k + "'>" + v + "</li>";
	 		});
	 		result += "</ul>";
	 		return result;
	     }
		
		if(isWhat == "file"){
			result += "<input readonly type='text' id='file' data-title='" + prop.name + "' data-name='" + prop.id + "'  name='" + prop.id + "' class='uploadInput "+isCheck+" " + className + "' value='" + prop.value + "' />";
			result += " <div id='picker' class='upload picker'>选择文件</div>";
			result += " <div id='uploadVideo' class='uploadVideo'>上传</div>";
			return result;
		}
		result += "<input type='text' id='" + prop.id + "' name='" + prop.id + "' class='uploadInput "+isCheck+" " + className + "' value='" + prop.value + "' />";
	} else {
		result += "<input class='"+isCheck+"' value='" + prop.value + "' readonly/>";
	}
	return result;
},
'date': function(prop, datas, className) {
	if(prop.required){
		var result = "<div class='title'>" + prop.name + "<span> *</span></div>";
		var isCheck = "checkInfo";
	}else{
		var result = "<div class='title'>" + prop.name + "</div>";
		var isCheck = "noCheckInfo";
	}
	if (prop.writable === true) {
		result += "<input type='text' id='" + prop.id + "' name='" + prop.id + "' class='date "+isCheck+" " + className + "' value='" + prop.value + "'/>";
	} else {
		result += "<input class='"+isCheck+"' value='" + prop.value + "' readonly/>";
	}
	return result;
},
'long': function(prop, datas, className) {
	if(prop.required){
		var result = "<div class='title'>" + prop.name + "<span> *</span></div>";
		var isCheck = "checkInfo";
	}else{
		var result = "<div class='title'>" + prop.name + "</div>";
		var isCheck = "noCheckInfo";
	}
	if (prop.writable === true) {
		result += "<input type='text' id='" + prop.id + "' name='" + prop.id + "' class=' "+isCheck+" " + className + "' value='" + prop.value + "'/>";
	} else {
		result += "<input class='"+isCheck+"' value='" + prop.value + "' readonly/>";
	}
	return result;
},
'enum': function(prop, datas, className) {
	if(prop.required){
		var result = "<div class='title'>" + prop.name + "<span> *</span></div>";
		var isCheck = "checkInfo";
	}else{
		var result = "<div class='title'>" + prop.name + "</div>";
		var isCheck = "noCheckInfo";
	}
	if (prop.writable === true) {
		result += "<input readonly class='autoSelect' id='" + prop.id + "'  class='" + className + "'>";
		result += "<input type='hidden' class='hideInput' name='" + prop.id + "' >";
		result += "<img class='autoImg' src='/resources/images/flow/selectOrder.png'>";
		result += "<ul class='autoSelectUl'>";
		$.each(datas[prop.id], function(k, v) {
			result += "<li data-id='" + k + "'>" + v + "</li>";
		});
		result += "</ul>";
	} else {
		result += "<input class='"+isCheck+"' value='" + prop.value + "' readonly/>";
	}
	return result;
}
};

/*var formFieldCreator = {
		'string': function(prop, datas, className) {
			var result = "<td width='120'>" + prop.name + "：</td>";
			if (prop.writable === true) {
				result += "<td><input type='text' id='" + prop.id + "' name='" + prop.id + "' class='" + className + "' value='" + prop.value + "' />";
			} else {
				result += "<td>" + prop.value;
			}
			return result;
		},
		'date': function(prop, datas, className) {
			var result = "<td width='120'>" + prop.name + "：</td>";
			if (prop.writable === true) {
				result += "<td><input type='text' id='" + prop.id + "' name='" + prop.id + "' class='date " + className + "' value='" + prop.value + "'/>";
			} else {
				result += "<td>" + prop.value;
			}
			return result;
		},
		'long': function(prop, datas, className) {
			var result = "<td width='120'>" + prop.name + "：</td>";
			if (prop.writable === true) {
				result += "<td><input type='text' id='" + prop.id + "' name='" + prop.id + "' class='" + className + "' value='" + prop.value + "'/>";
			} else {
				result += "<td>" + prop.value;
			}
			return result;
		},
		'enum': function(prop, datas, className) {
			var result = "<td width='120'>" + prop.name + "：</td>";
			if (prop.writable === true) {
				result += "<td><select id='" + prop.id + "' name='" + prop.id + "' class='" + className + "'>";
				$.each(datas[prop.id], function(k, v) {
					result += "<option value='" + k + "'>" + v + "</option>";
				});
				result += "</select>";
			} else {
				result += "<td>" + prop.value;
			}
			return result;
		}
	};*/

function initEvenInfo(){
	$('#toFinish').off('click').on('click',function(){
         $('#cusModel').show();		
	});
	$('.closeModel').off('click').on('click',function(){
         $('.cusModel').hide();		
	});
	$('#myOrder').show();
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
	
	$(".date").datepicker({
		language: 'zh',
		dateFormat:'yyyy-MM-dd'
     });
	
}

//提示
function showWarn(){
	$('#showWarn').off('click').on('click',function(){
		$('#warnModel').show();
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


//初始化添加留言
function initAddTalk(){
	$('.upInfo #submitTalkInfo').off('click').on('click',function(){
		var projectId = $('#projectId').val();
		var taskName = $('#taskName').val();
		var talkInfo = $('#talkInfo').val();
		loadData(function(res){
			var res = res;
		}, getContextPath() + '/message/addTopic',$.toJSON({
			projectId:projectId,
			taskName:taskName,
			content:talkInfo
		}));
	});
}

//初始化 留言板
function initTalk(){
	loadData(function(res){
		var res = res;
	/*	var body = $('.utoInfo');
		body.html('');*/
		if(res != null && res != undefined){
			for (var int = 0; int < res.length; int++) {
				   var html =createUserInfo();
				   body.append(html);
			};			
		}
	}, getContextPath() + '/message/getDefaultMsg'+$('#projectId').val(),null);
}

function createTalkInfo(res){

	var html = [
	    '<div class="areaItem">',
	    '   <div class="infoItem">',
		'	  <img src="/resources/images/flow/def.png">',
		'       <div class="info">策划人：完成 上传策划方案 任务</div>',
		'       <div class="time">',
		'       	<span>发布时间：22017-07-09  14：00</span>',
		'     	    <div class="openTalk"></div>',
		'       </div>',
   		'   </div>',
		'   <div class="infoContent">',
		'      <div>负责人:<span>策划方案需要调整一下</span></div>',
		'      <div>负责人:<span>策划方案需要调整一下</span></div>',
		'      <input>',
		'   </div>',
		'   <div class="upInfo">',
		'      <div class="btn-c-r">提交</div>',
		'   </div>',
		'</div>',

	].join('');
	return html;
}


























