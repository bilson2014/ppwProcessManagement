var upload_Video;
var data = new Array();
var title;
var video_max_size = 200*1024*1024;
var hasPicker = false;
$().ready(function(){
	$('.frameHead .name').text($('#projectName').val());
	initDaibanTime();
	$(window.parent.document).find('.footBot .item').removeClass('checkItem');
    $(window.parent.document).find('.footBot #toMission').addClass('checkItem');
    checkState();
   

});
function isIos(){
	var u = navigator.userAgent;
	var isIOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端
	return isIOS;
}

function checkState(){
	var href = window.location.href;
    var state = href.substr(href.lastIndexOf("?")+1,href.length);
    $(window.parent.document).find('.footBot #toFile').attr('data-content',$('#url').val());
    $(window.parent.document).find('.footBot #toMessage').attr('data-content',$('#url').val());
    $(window.parent.document).find('.footBot #toInfo').attr('data-content',$('#url').val());
    $(window.parent.document).find('.footBot #toFlow').attr('data-content',$('#url').val());
    if(state.trim()!= "task"){
    	$('#daiban').show();
    	$(window.parent.document).find('.footBot #toMission').attr('data-content',$('#url').val());
    }else{
    	$(window.parent.document).find('.footBot #toMission').attr('data-content',$('#url').val()+"?task");
    	    selectEven();
    		UploadFile();
    		addForm(); 
    		initDaibanTime();
    }
}

//待办任务时间
function initDaibanTime(){
	   var nowData = Date.parse(new Date());
	   var time =Date.parse($('#missinTime').text().replace("CST","GMT+0800"));
	   var lastTime = (time - nowData);
	   var lastHour =(time - nowData)/3600000;
	   var getTime =$('#missinTime').text();
	   if(lastHour < 0){
		 $('#missinTime').text(' 已超时 '+getTimeString(lastTime)); 
	   }
	   if(lastHour >= 3){
		   $('#missinTime').text('剩余'+getTimeString(lastTime));
	   }
	   if(lastHour <3 && lastHour>=0){
		   $('#missinTime').text('剩余'+getTimeString(lastTime));
	   }
}

function selectEven(){
	$('.setSelect').off('click').on('click',function(){
		initSelectIos();
		$(window.parent.document).find('.pagePhone').scrollTop(9999);
	})
	
	$('.orderSelect div').off('click').on('click',function(){
		initSelectIos();
		$(window.parent.document).find('.pagePhone').scrollTop(9999);
	})
	
	
}

function initSelectIos(){
	/*var data = [ {
		'id' : '10001',
		'value' : '看情况'
	}, {
		'id' : '10002',
		'value' : '1万元及以上'
	}, {
		'id' : '10003',
		'value' : '2万元及以上'
	}, {
		'id' : '10004',
		'value' : '3万元及以上'
	}, {
		'id' : '10005',
		'value' : '5万元及以上'
	}, {
		'id' : '10006',
		'value' : '10万元及以上'
	}, ];*/
	var bankSelect = new IosSelect(1, [ data ], {
		title : title,
		itemHeight : 35,
		oneLevelId : '',
		callback : function(selectOneObj) {
             $('#setinput').attr('data-id',selectOneObj.id);
             $('#setinput').val(selectOneObj.value);
             $('#setinputValue').val(selectOneObj.id);
		}
	});
}

function city(id, text) {
	this.id = id;
	this.value = text;
}

function dataEven(){
	
	$(".date").datepicker({
		language: 'zh',
		dateFormat:'yyyy-MM-dd'
     });
	
}

//动态上传
function UploadFile(){
	$('.name').hide();
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
		resourceName:$('#file').attr('data-title'),
		taskId : $('#taskId').val(),
		resourceType:$('#file').attr('data-name')
	});
	
	upload_Video.on('fileQueued', function(file) {
	    $('.setFileName').val(file.name);
	    $('.longInput').val(file.name);
	    $('.proTitle').text(file.name);
	    $('.upProgress').show();
	    upload_Video.option('formData', {
    		resourceName:$('#file').attr('data-title'),
    		taskId : $('#taskId').val(),
    		resourceType:$('#file').attr('data-name')
    	});
	    upload_Video.upload();
		$('.dynamic-form-table .item').hide();
		$('#toSubmitForm').off('click');
	});

	upload_Video.on('uploadProgress',function(file, percentage) {
		$("#setWidth").css('width', percentage * 100 + '%');
		$('.upIng').show();
		$('.upSuccess').hide();
		$('.upError').hide();
		$('#btnInput').off('click');
	});
	upload_Video.on('uploadSuccess', function(file,response) {
		if(response){
			formCheck();
			$('.upIng').hide();
			$('.upSuccess').show();
			$('.upError').hide();
		}else{		
			$('.upIng').hide();
			$('.upSuccess').hide();
			$('.upError').show();
		}
	});
}


/**
 * 生成一个field的html代码
 */

function formCheck(){
	$('#toSubmitForm').off('click').on('click',function(){
		if(checkForm()){
			$('.modelTool').show();
		}
	});
	$('#closeInfo').off('click').on('click',function(){
		$('.modelTool').hide();
	});
	$('#checkInfo').off('click').on('click',function(){
		$('.dynamic-form').submit();
	});
	
}

//表单验证
function checkForm(){
	$('.item').attr('data-content','');
	var getCheckInfo = $('.checkInfo');
	var checkFlag = true;
	for (var i = 0; i < getCheckInfo.length; i++) {
		var check = $(getCheckInfo[i]).val();
               if(check == null || check == "" || check == undefined )	{
            	   checkFlag = false;
            	   $(getCheckInfo[i]).parent().attr('data-content','请补充必填信息');
            	   $(getCheckInfo[i]).parent().parent().attr('data-content','请补充必填信息');
            	   return false;
               }	
	}
	return true;
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
		$('#setContent').html("<form class='dynamic-form' method='post'><div class='dynamic-form-table'></div></form>");
		var $form = $('.dynamic-form');
		$form.attr('action', '/project/phone/completeTask/' + $('#taskId').val());
		trs += '<div class="btnInput" id="btnInput"><input id="toSubmitForm" class="btn-c-r" type="button" value="提交"/></div>'	
		// 添加table内容
		$('.dynamic-form-table').html(trs);
		if(hasPicker){
			if(isIos()){
				$('.dynamic-form-table').hide();
				$('#daiban').show();
				$('#daibanword').html('iOS暂不支持文件上传</br>请到PC或安卓系统上传文件');
			}else{
				UploadFile();
			}
		}
		autoInput();
		selectEven();
		formCheck();
		dataEven();	
	}, '/project/get-form/task/' + $('#taskId').val() + '/' + $('#projectId').val(), null);
}

function createFieldHtml(prop, obj, className) {
	return formFieldCreator[prop.type.name](prop, obj, className);
}

var formFieldCreator = {
		'string': function(prop, datas, className) {
			title = prop.name;
			var proValue = '';
			if(prop.required){
				var result = "<div class='name'>" + prop.name + "</div>";
				var isCheck = "checkInfo";
			}else{
				var result = "<div class='name'>" + prop.name + "</div>";
				var isCheck = "noCheckInfo";
			}
			var isWhat = prop.id.split('_')[0];
			var str = prop.id;
			var isRead = str.indexOf('info');
			
			if(str.indexOf('pt_') < 0){
				proValue = prop.value;
			}
			
			if (prop.writable === true) {
				if(isRead == 0){
					result += "<input class='' value='" + proValue + "' readonly name='" + prop.id + "'  />";
					return result;
				}
				if(isWhat == "file"){
					result += "<input readonly id='file'  data-title="+prop.name+" data-name="+prop.id+" name='" + prop.id + "' class='picker setFileName uploadInput "+isCheck+" " + className + "' value='" + proValue + "' >";
					result += "<div class='upload' id='picker'>上传</div>";
					hasPicker = true;
					return result;
				}
				if(prop.id == "pt_teamName"){
					result += "<input type='text' id='" + prop.id + "' name='" + prop.id + "' class='uploadInput "+isCheck+" " + className + "' value='" + proValue + "' />";
					result += "<ul class='utoInfo'></ul>";
					return result;
				}
				
			     if(isWhat == 'schemeId'  || isWhat == 'superviseId' || isWhat == 'teamProviderId')	{
/*			 		result += "<input readonly id='setinputValue' class='autoSelect checkInfo' id='" + prop.id + "'  class='" + className + "' name='" + prop.id + "' >";
			 		result += "<img class='autoImg' src='/resources/images/flow/selectOrder.png'>";*/
					result += "<div class='orderSelect'>";
					result += "   <input  type='hidden' id='setinputValue'   class='" + className + "' name='" + prop.id + "'/>";
					result += "   <input readonly class='setSelect checkInfo'  id='setinput' id='" + prop.id + "'  class='" + className + "'/>";
					result += "   <div></div>";
					result += "</div>";
			 		$.each(datas[prop.id], function(k, v) {
			 			data.push(new city(k, v));
			 		});
			 		return result;
			     }
				result += "<input type='text' id='" + prop.id + "' name='" + prop.id + "' class='uploadInput "+isCheck+" " + className + "' value='" + proValue + "' />";
			} else {
				result += "<input class='' value='" + proValue + "' readonly name='" + prop.id + "'  />";
			}
			return result;
		},
		'date': function(prop, datas, className) {
			var proValue = '';
			var str = prop.id;
			
			if(str.indexOf('pt_') < 0){
				proValue = prop.value;
			}
			
			if(prop.required){
				var result = "<div class='name'>" + prop.name + "</div>";
				var isCheck = "checkInfo";
			}else{
				var result = "<div class='name'>" + prop.name + "</div>";
				var isCheck = "noCheckInfo";
			}
			if (prop.writable === true) {
				result += "<input readonly type='text' id='" + prop.id + "' name='" + prop.id + "' class='date "+isCheck+" " + className + "' value='" + proValue + "'/>";
			} else {
				result += "<input class='' value='" + proValue + "' name='" + prop.id + "' readonly/>";
			}
			return result;
		},
		'long': function(prop, datas, className) {
			var proValue = '';
			var str = prop.id;
			
			if(str.indexOf('pt_') < 0){
				proValue = prop.value;
			}
			
			if(prop.required){
				var result = "<div class='name'>" + prop.name + "</div>";
				var isCheck = "checkInfo";
			}else{
				var result = "<div class='name'>" + prop.name + "</div>";
				var isCheck = "noCheckInfo";
			}
			if (prop.writable === true) {
				result += "<input type='text' id='" + prop.id + "' name='" + prop.id + "' class=' "+isCheck+" " + className + "' value='" + proValue + "'/>";
			} else {
				result += "<input class='' value='" + proValue + "' readonly name='" + prop.id + "'/>";
			}
			return result;
		},
		'enum': function(prop, datas, className) {
			title = prop.name;
			var proValue = '';
			var str = prop.id;
			if(str.indexOf('pt_') < 0){
				proValue = prop.value;
			}
			if(prop.required){
				var result = "<div class='name'>" + prop.name + "</div>";
				var isCheck = "checkInfo";
			}else{
				var result = "<div class='name'>" + prop.name + "</div>";
				var isCheck = "noCheckInfo";
			}
	
			if (prop.writable === true) {
				result += "<div class='orderSelect'>";
				result += "   <input  type='hidden' id='setinputValue'   class='" + className + "' name='" + prop.id + "'/>";
				result += "   <input readonly class='setSelect checkInfo'  id='setinput' id='" + prop.id + "'  class='" + className + "'/>";
				result += "   <div></div>";
				result += "</div>";
				$.each(datas[prop.id], function(k, v) {
					data.push(new city(k, v));
				});
			} else {
				result += "<input class='' value='" + proValue + "' readonly name='" + prop.id + "'/>";
			}
			return result;
		}
	};

//自动联动客户信息
function autoInput(){
	$('#pt_teamId').parent().hide();
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



