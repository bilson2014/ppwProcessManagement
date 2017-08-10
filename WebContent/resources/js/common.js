var uploader;
var hasVideo = false;
var kaptcharInterValObj; // timer变量，控制时间
var successIntervalObj;
var initM = 3;
var counts = 120; // 间隔函数，1秒执行
var curCounts = 0; // 当前剩余秒数 - 注册
var InterValObj; // timer变量，控制时间 - 注册
$().ready(function(){
	getImgUrl();
	controlInput();
	controlMenu();
	// 弹出电话预约界面
	$('.common-icons-tele-client').click(function(){
		$('#toolbar-modal').modal({
			keyboard: false
		})
		$('#modal-call').on('click',function(){
			// 检测手机号码
			var phoneNumber = $('#phoneCall').val();
			if(phoneNumber != null && phoneNumber != '' && phoneNumber != undefined){
				if(checkMobile(phoneNumber)){
					loadData(function(result){
						if(result){
							// 消息发送成功
							$('#modal-dd-second').empty();
							$('#modal-h3-first').text('视频营销专家正火速与您联系!');
							
						}else{
							// 消息发送失败
							$('.modal-message').empty();
							$('.modal-message').text('亲!拍拍出了点小问题，正在努力自我修复，请刷新后再试哦!');
							$('.modal-message').show('normal');
						}
					}, getContextPath() + '/appointment/' + phoneNumber, null);
				}else {
					$('.modal-message').empty();
					$('.modal-message').text('请输入正确的电话号码哦亲!');
					$('.modal-message').show('normal');
				}
			}else {
				$('.modal-message').empty();
				$('.modal-message').text('别忘记输入电话哦亲!');
				$('.modal-message').show('normal');
			}
		});
	});
	
	$('#s-btn').on('click',function(){
		var q = $('#search-q').val().trim();
		if(q != null && q != '' && q != undefined){
			$('#s-form').submit();
		} else if(q == null || q == ''){
			$('#search-q').val('*');
			$('#s-form').submit();
		}
	});
	
	//点击搜索图标时搜索lt 20161208
	   $('.bannerSearchFind').on('click',function(){
	    	$('#s-btn').click();
	    });
	//end
	
	//检测用户是否完善登录名和密码 wanglc
	loadData(function(flag){
		if(!flag){
			$(".right-part").append('<div class="warn">!</div>');
			//.append('<div class="warnWindom"><div class="divImg"></div><div></div></div>');
			//如果是供应商和客户info页面
			var url = window.location.href;
			if(url.indexOf("provider/company-info") != -1 || url.indexOf("user/info") != -1){
				$("#safe-point").append('<div class="warn" style="left: 72px;">!</div>');
			}
		}
	}, getContextPath() + '/loginName/validate');
	
	playVideo();
	chickShowOrder();
	
	$('body').on('click',function(){
		var ulArray = $('.dropdown').find('ul');
		for (var int = 0; int < ulArray.length; int++) {
			if($(ulArray[int]).css("display")!="none"){
				$(ulArray[int]).slideUp();
			}
		}
		return;
	});
	
	  //下拉监听
	$(".dropdown").off('click').on('click',function(){
		var ul = $(this).find('ul');
		if(ul.css("display")!="none"){
			ul.slideUp();
		}else{
			$(".dropdown").find('ul').slideUp();
			$(this).find('ul').slideDown();
		}
		return false;
	});
	
	// 初始化默认选择
	var dropdown = $('.dropdown');
	if(dropdown!=null && dropdown.length >0){
		for (var int = 0; int < dropdown.length; int++) {
			var li = $(dropdown[int]).find('ul li');
			if(li != null && li.length >0){
				for (var int2 = 0; int2 < li.length; int2++) {
					if($(li[int2]).attr('selected') == 'selected'){
						var span = $(dropdown[int]).find('span');
						span.attr('data-value',$(li[int2]).attr('data-value'));
						span.text($(li[int2]).text());
					}
				}
			}
		}
	}
});

function debug(obj) {

	var linebreak = "\r\n";
	// var linebreak = "; ";
	var msg = "OBJECT->" + linebreak;
	msg += obj + linebreak;
	for ( var e in obj) {
		msg += e + "=" + obj[e] + linebreak;
	}
	return alert(msg);
}

//获取项目路径
function getContextPath() {
	var path = document.location.pathname.substr(1);
	path = "/" + path.substr(0, path.indexOf("/"));
	return '';
}

//AJAX GET
function getData(Func,url){
	$.ajax({
		url : url,
		type : 'GET',
		cache:false,
		dataType : 'json',
		success : function(data){
			Func(data);
		},
		error : function(jqXHR, textStatus, errorThrown) {
			console.error('ajax(' + url + ')[' + jqXHR.status + ']' + jqXHR.statusText);
			console.error(jqXHR.responseText);
			console.error('[' + textStatus + ']' + errorThrown);
		}
	});
}

// AJAX POST
function loadData(Func,url,param){
	$.ajax({
		url : url,
		type : 'POST',
		data : param,
		dataType : 'json',
		cache:false,
		contentType : 'application/json; charset=UTF-8',
		success : function(data){
			Func(data);
		},
		error : function(jqXHR, textStatus, errorThrown) {
			console.error('ajax(' + url + ')[' + jqXHR.status + ']' + jqXHR.statusText);
			console.error(jqXHR.responseText);
			console.error('[' + textStatus + ']' + errorThrown);
		}
	});
}
//AJAX POST 
function loadData2(Func,url,param){
	$.ajax({
		url : url,
		type : 'POST',
		cache:false,
		data : param,
		dataType : 'json',
		success : function(data){
			Func(data);
		},
		error : function(jqXHR, textStatus, errorThrown) {
			console.error('ajax(' + url + ')[' + jqXHR.status + ']' + jqXHR.statusText);
			console.error(jqXHR.responseText);
			console.error('[' + textStatus + ']' + errorThrown);
		}
	});
}
// ajax submit form
function submitForm(Func,url,param,checkElement){
	var processing = $(checkElement).hasClass('X');
	var rtoken = $('#rtoken').val();
	// 页面存在rtoken添加在提交的参数内
	if(rtoken != undefined && rtoken != null && rtoken != ''){
		var obj = $.evalJSON(param);
		obj.rtoken = rtoken;
		param = $.toJSON(obj);
	}
	if(!processing){
		$(checkElement).addClass('X');
		$.ajax({
			url : url,
			type : 'POST',
			data : param,
			cache:false,
			dataType : 'json',
			contentType : 'application/json; charset=UTF-8',
			success : function(data){
				$(checkElement).removeClass('X');
				Func(data);
			},
			error : function(jqXHR, textStatus, errorThrown) {
				$(checkElement).removeClass('X');
				console.error('ajax(' + url + ')[' + jqXHR.status + ']' + jqXHR.statusText);
				console.error(jqXHR.responseText);
				console.error('[' + textStatus + ']' + errorThrown);
			}
		});
	}else{
		console.log('处理中请勿重复点击！');
	}
}

//AJAX POST
function syncLoadData(Func,url,param){
	$.ajax({
		url : url,
		type : 'POST',
		data : param,
		dataType : 'json',
		async:false,
		contentType : 'application/json; charset=UTF-8',
		success : function(data){
			Func(data);
		},
		error : function(jqXHR, textStatus, errorThrown) {
			console.error('ajax(' + url + ')[' + jqXHR.status + ']' + jqXHR.statusText);
			console.error(jqXHR.responseText);
			console.error('[' + textStatus + ']' + errorThrown);
		}
	});
}

//导出excel
function download(url,condition){
	$.growlUI('报表输出中…', '正在为您输出报表，请稍等。。。');
	var inputHTML = '<input type="hidden" name="json" value="'+ htmlSpecialCharsEntityEncode(decodeURIComponent(condition)) +'" />';
	$('<form action="'+ getContextPath() + url +'" method="POST">' + inputHTML + '</form>').appendTo('body').submit().remove();
}

var htmlSpecialCharsRegEx = /[<>&\r\n"']/gm;

var htmlSpecialCharsPlaceHolders = {
	'<' : 'lt;',
	'>' : 'gt;',
	'&' : 'amp;',
	'\r' : "#13;",
	'\n' : "#10;",
	'"' : 'quot;',
	"'" : 'apos;' /*single quotes just to be safe*/
};

function htmlSpecialCharsEntityEncode(str) {
	return str.replace(htmlSpecialCharsRegEx, function(match) {
		return '&' + htmlSpecialCharsPlaceHolders[match];
	});
}

/**
 * 获取文件名
 */
function getFileName(val){
	var arr = val.split('/');
	var imgName = '';
	for(var i = 0;i < arr.length;i ++){
		if(i == arr.length - 1 ){
			imgName = arr[i]
		}
	}
	return imgName;
}

/**
 * 获取 主机名
 * @returns http://localhost:8080
 */
function getHostName(){
	
	return window.location.protocol + '//' + window.location.host;
}
/**
 * 获取 dfs的主机名
 */
function getDfsHostName(){
	var rPath = $('#storage_node').val();
	return rPath == undefined ? "http://resource.apaipian.com/resource/" : rPath;
}
/**
 * 数据加分隔符
 * @param number
 * @returns
 */
function thousandCount(number) {
	var tableData;
	if (number == 0) {
		tableData = 0;
	} else {
		if(number != undefined){
			tableData = number.toLocaleString();
			var indexOf = tableData.indexOf(".");
			if (indexOf > -1) {
				tableData = tableData.substring(0, indexOf);
			}
		}
	}
	return tableData;
}
/**
 * 去掉千分位显示
 */
function DisThousandCount(number){
	var tableData;
	if (number == 0) {
		tableData = 0;
	} else {
		tableData = number.toLocaleString();
		var indexOf = tableData.indexOf(".");
		if (indexOf > -1) {
			tableData = tableData.substring(0, tableData.indexOf("."));
		}
		tableData = tableData.replace(',','');
	}
	return tableData;
}
// 重新编码
function htmlSpecialCharsEntityEncode(str) {
	return str.replace(htmlSpecialCharsRegEx, function(match) {
		return '&' + htmlSpecialCharsPlaceHolders[match];
	});
}
var htmlSpecialCharsRegEx = /[<>&\r\n"']/gm;
var htmlSpecialCharsPlaceHolders = {
	'<' : 'lt;',
	'>' : 'gt;',
	'&' : 'amp;',
	'\r' : "#13;",
	'\n' : "#10;",
	'"' : 'quot;',
	"'" : 'apos;' /*single quotes just to be safe*/
};

/**
 * 验证 手机号
 * @param str
 */
function checkMobile(str) {
	var reg = /^(0|86|17951)?(13[0-9]|15[012356789]|17[013678]|18[0-9]|14[57])[0-9]{8}$/;
	if(str.match(reg)){
		return true;
	} else{
		return false;
	}
}

/**
 * 验证 邮箱地址
 */
function checkEmail(str){
	reg = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
	if(str.match(reg))
		return true; 
	else
		return false;
}

/*
 * 验证 数字
 */
function checkNumber(str){
	reg = /^[1-9]+[0-9]*]*$/;
	if(str.match(reg))
		return true;
	else
		return false;
}

function checkDecimal(str){
	reg =/^\d+(\.\d+)?$/;
	if(str.match(reg))
		return true;
	else
		return false;
}
/**
 * 检验 用户名格式
 * @param name 用户名
 */
function checkUserName(name){
	var reg = /^[a-zA-Z0-9_]{6,16}$/ ;
	if(name.match(reg))
		return true; 
	else
		return false;
}
/**
 * 加密
 * @param word
 * @returns
 */
function Encrypt(word){
	var cryptKey = '0102030405060708';
	var key = CryptoJS.enc.Utf8.parse(cryptKey);
	var iv  = CryptoJS.enc.Utf8.parse(cryptKey);
	var srcs = CryptoJS.enc.Utf8.parse(word);
	var encrypted = CryptoJS.AES.encrypt(srcs, key, { iv: iv,mode:CryptoJS.mode.CBC});  
	return encrypted.toString();
}
//使用方法
/*
 webupload({
	 auto:true,
	 server: '',//url
	 pick: '',//点击弹窗
	 submitBtn:'',//提交按钮
	 formData : {},//参数
	 fileQueued:function(file){//选中后执行
	 },
	 uploadProgress:function(file, percentage){},//进度显示
	 uploadSuccess:function( file ,response){//成功回调
	 },
	 uploadComplete:function(file){},
	 uploadError:function(file){}
});
*/
function webupload(param) {
	uploader && uploader.destroy();//及时销毁,避免,切换导致按钮增大
	var auto = param.auto;
	var submitBtn = param.submitBtn;
	var accept = param.accept;
	var fileNumLimit = param.fileNumLimit;
	var server = param.server;
	var pick = param.pick;
	var data = param.formData;
	var chunked = param.chunked;
	var resize = param.resize;
	if (auto != true) {
		auto = false;
	}
	if (chunked == null || chunked == undefined ) {
		chunked = false;
	}
	if (resize == null || resize == undefined) {
		resize = false;
	}
	if(fileNumLimit == null && fileNumLimit == undefined){
		fileNumLimit=9999;
	}
	uploader = WebUploader.create({
		auto:auto,
		// swf文件路径
		swf : '/resources/lib/webuploader/Uploader.swf',
		// 文件接收服务端。
		server : server,
		// 选择文件的按钮。可选。
		// 内部根据当前运行是创建，可能是input元素，也可能是flash.
		pick : pick,
		accept:accept,
		// 不压缩image, 默认如果是jpeg，文件上传前会压缩一把再上传！
		resize : resize,
		
		fileNumLimit:fileNumLimit,
		// 开起分片上传。
		chunked : chunked,
		formData : data,
		duplicate: true//允许重复上传同一个
	});
	// 当有文件被添加进队列的时候
	uploader.on('beforeFileQueued', function(file) {
		if(param.beforeFileQueued){
			param.beforeFileQueued(file);
		}else{
		}
	});
	// 当有文件被添加进队列的时候
	uploader.on('fileQueued', function(file) {
		if(param.fileQueued){
			param.fileQueued(file);
		}else{
			//$('.uploader-list').append('<div id="' + file.id + '" class="item">'
			//			+ '<h4 class="info">' + file.name + '</h4>'
			//			+ '<p class="state">等待上传...</p>' + '</div>');
			//uploader.md5File(file)
			// 及时显示进度
			//.progress(function(percentage) {
			//	console.log('Percentage:', percentage);
			//})
			// 完成
			//.then(function(val) {
			//	console.log('md5 result:', val);
			//});
		}
	});
	// 文件上传过程中创建进度条实时显示。
	uploader.on('uploadProgress',function(file, percentage) {
		if(param.uploadProgress){
			param.uploadProgress(file,percentage);
		}else{
			/*var $li = $('#' + file.id), $percent = $li
			.find('.progress .progress-bar');
			// 避免重复创建
			if (!$percent.length) {
				$percent = $(
						'<div class="progress progress-striped active">'
								+ '<div class="progress-bar" role="progressbar" style="width: 0%">'
								+ '</div>' + '</div>')
						.appendTo($li).find('.progress-bar');
			}
			$li.find('p.state').text('上传中');
			$percent.css('width', percentage * 100 + '%');*/
		}
	});
	uploader.on('uploadSuccess', function(file,response) {
		if(param.uploadSuccess){
			param.uploadSuccess(file,response);
		}else{
		}
	});
	uploader.on('uploadError', function(file) {
		if(param.uploadError){
			param.uploadError(file);
		}else{
			//$('#' + file.id).find('p.state').text('上传出错');
		}
	});

	uploader.on('uploadComplete', function(file) {
		if(param.uploadComplete){
			param.uploadComplete(file);
		}else{
			//$( '#'+file.id ).find('p.state').text('已上传');
			//$('#' + file.id).find('.progress').fadeOut();
		}
	});
	$(submitBtn).on('click', function() {
		uploader.upload();
		//uploader.stop();
	});
}


// 分享
var share = {
		init :function(url,title,img){
			mobShare.config({
				 
				debug: true, // 开启调试，将在浏览器的控制台输出调试信息
			
				appkey: '8c49c537a706', // appkey
			
				params: {
					url: url, // 分享链接
					title: title, // 分享标题
					description: '拍片网-中国领先的视频营销服务平台', // 分享内容
					pic: img, // 分享图片，使用逗号,隔开
				},
			 
				callback: function( plat, params ) {
					
				}
			});
		}
}

var _hmt = _hmt || [];
(function() {
	var hm = document.createElement("script");
	hm.src = "//hm.baidu.com/hm.js?b0ac6d7e1cee0e96c5c43106c5d43537";
	var s = document.getElementsByTagName("script")[0];
	s.parentNode.insertBefore(hm, s);
})();

var _hmt = _hmt || [];

(function() {

  var hm = document.createElement("script");

  hm.src = "https://hm.baidu.com/hm.js?0087b7e6cc5371a3a8bf40eeb6ee6f0a";

  var s = document.getElementsByTagName("script")[0];

  s.parentNode.insertBefore(hm, s);

})();

var _vds = _vds || [];
window._vds = _vds;
(function(){
  _vds.push(['setAccountId', '9f2e33a3d43b5d78']);
  (function() {
    var vds = document.createElement('script');
    vds.type='text/javascript';
    vds.async = true;
    vds.src = ('https:' == document.location.protocol ? 'https://' : 'http://') + 'dn-growing.qbox.me/vds.js';
    var s = document.getElementsByTagName('script')[0];
    s.parentNode.insertBefore(vds, s);
  })();
})();

//3.0
function playVideo() {
	$('#showVideo')
			.on(
					'click',
					function() {
						$('#playVideo').removeClass('hide');
						if (!hasVideo) {
							var $body = ' <div class="openVideo" title="双击关闭视频" id="playVideo">'
								    +   '<div class="openVideoCommon"></div>'
									+ '<div id="videoRoata"><div class="videoClose" id="commonCloseVideo"></div><video autoplay controls loop poster="/resources/images/banner/moreInfo.JPG"  name="media" id="header3Video"> '
									+
									// '<source
									// src="/product/video/paipianwangMovie.mp4"
									// id="source" type="video/mp4">' +
									'<source  src="http://www.apaipian.com/product/video/paipianwangMovie.mp4"  id="source" type="video/mp4">'
									+ '</video></div>';
							$body += '</div>';
							$("body").append($body);
							hasVideo = true;
							initClose();
							setTimeout(function(){
								 $('#header3Video').addClass('active');
								},100);	
						} else {
							document.getElementById('header3Video').play();
							setTimeout(function(){
								 $('#header3Video').addClass('active');
								},100);
						}
					});

}

function initClose() {
	
	$('#commonCloseVideo').on('click',function(){
		
		setTimeout(function(){
			$('#playVideo').addClass('hide');
			},300);
		document.getElementById("header3Video").pause();
	    $('#header3Video').removeClass('active');
		
	});
	
	$('#playVideo').on('dblclick', function() {
		
		setTimeout(function(){
			$('#playVideo').addClass('hide');
			},300);
		$('#header3Video').removeClass('active');
		document.getElementById("header3Video").pause();
	});
}

function chickShowOrder() {
	$('#wantOrder').on('click', function() {
		showOrder('宣传片');
	});
}

function showOrder(typeName) {

    if ($('div').hasClass('comOrder')) {
    	$("#submit-indent-recomment").text(typeName);
        $('.comOrder').show();
    } else {

        var $body = '<div class="comOrder">' +
            '<div class="cOrder" id="cOrder">' +
            '<div class="cCloseBtn" id="closeBtn">' +
            ' 	<div></div>' +
            '</div>' +
            '<div class="cOrderTitle">立即下单，专业团队为您服务</div>' +
            '<form id="cOrder-form" role="form" method="post" autocomplete="off" accept-charset="UTF-8">'+
            ' <div class="cOrderItem">' +
            '<div class="dropdown dropdowns" id="selectType">' +
            '<div class="btn btn-default dropdown-toggle" type="button" id="commonOrderUlTogle" data-toggle="dropdown">' +
            '<span data-content="0" id="submit-indent-recomment">' + typeName + '</span>' +
            '<div class="carets"></div>' +
            '</div>' +
            '<ul class="dropdown-menu dropdown-menus" id="commonOrderUl" role="menu" aria-labelledby="dropdownMenu1">' +
            '<li data-content="0">宣传片</li>' +
            '<li data-content="1">广告片</li>' +
            '<li data-content="2">MG动画</li>' +
            '<li data-content="3">病毒视频</li>' +
            '<li data-content="4">微电影</li>' +
            '<li data-content="5">证言影片</li>' +
            '<li data-content="6">公益片</li>' +
            '<li data-content="7">MV</li>' +
            '<li data-content="8">预告片</li>' +
            '<li data-content="9">纪录片</li>' +
            '</ul>' +
            '</div>' +
            '</div>' +
            '<div class="cOrderItem" data-content="" id="indent_tele_error" >' +
            ' <input id="indent_tele" placeholder="您的电话号">' +
            '</div>' +
            '<div class="cOrderItem" data-content="" id="indent_code_error">' +
            ' <input id="phoneCode" placeholder="输入手机验证码">' +
            '<div class="btn-c-r" id="getPhoneCode">获取验证码</div>' +
            ' </div>' +
            ' <div class="cOrderBtn btn-c-r" id="order-btn">确认下单</div>' +
            ' </form>' +
            '  <div class="cOrderBotTitle">下单后,专业顾问将在2小时之内与您致电确认具体需求</div>' +
            '</div>';
        $body += '</div>';
        $("body").append($body);
        initOrderClick();
    }

	$('.cCloseBtn').on('click', function() {
		$('.comOrder').remove();
		curCounts = 0;
		window.clearInterval(InterValObj); // 停止计时器
	});
}


function initOrderClick(){
	var flag = true;
	$('#order-btn').off("click").on("click",function(){
		var xx = $(this).attr('data-xaioyu');
		var comment = $(this).attr('data-comment');
		var sir = $("#submit-indent-recomment").text();
		
		var indentName = '网站-PC-直接下单';
		var indentSour = '12';
		if('provider' == xx){
			indentName = '网站-PC-供应商首页下单';
			sir += ' ';
			sir += comment;
			var indentSour = '14';
		}
		if(checkDatas(1)){ // 检查数据完整性
				showError($('#indent_tele_error'),'');
				
				// 提交表单
				if(flag){
					flag = false;
					loadData2(function(msg){
						if(msg.ret){
							$('.comOrder').hide();
							showSuccess();
						}
						else{
							showError($('#indent_code_error'),'验证码错误');
						}
						
						
						flag = true;
					}, getContextPath() + '/order/deliver', 
						{	
						csrftoken:$("#csrftoken").val(),
						indent_tele:$("#indent_tele").val(),
						phoneCode:'-1',
						indent_recomment:sir,
						indentName:indentName,
						productId:-1,
						teamId:-1,
						serviceId:-1,
						phoneCode : $('#phoneCode').val(),
						indentSource:indentSour
					});	
				}
		}
	});
	$('#getPhoneCode').off("click").on('click',function(){
		if(curCounts == 0 && checkDatas(2)){
			curCounts = counts;
			var telephone = $('#indent_tele').val().trim();
			$('#getPhoneCode').text('已发送(' + curCounts + ')');
			$('#getPhoneCode').attr('disabled', 'disabled');
			InterValObj = window.setInterval(SetRemainTimes, 1000); // 启动计时器，1秒钟执行一次s
			loadData(function(flag) {
				if (!flag) {
					// 发送不成功
					// 显示重新发送
					sendCode = true;
					$('#getPhoneCode').text('重新获取');
					$('#getPhoneCode').removeAttr('disabled');
				}
			}, getContextPath() + '/login/verification/' + telephone, null);
		}
	});

	$('#commonOrderUlTogle').blur(function() {
		//alert(1);
		// $('#commonOrderUl').slideUp();
	});

	  //下拉监听
	$(".dropdown").off('click').on('click',function(){

		var ul = $(this).find('ul');
		if(ul.css("display")!="none"){
			ul.slideUp();
		}else{
			$(this).find('ul').slideDown();
		}
		return false;
	});

	
	$('#commonOrderUl li').on(
			'click',
			function() {
				$(this).parent().parent().find('.dropdown-toggle').find('span')
						.text($(this).text());
				var info = parseInt($(this).attr('data-info'));
				$(this).parent().parent().find('.dropdown-toggle').find('span')
						.attr("data-content", ($(this).attr('data-content')));
				$(this).parent().slideUp();
				return false;
			});
}

function SetRemainTimes() {
	if (curCounts == 0) {
		window.clearInterval(InterValObj); // 停止计时器
		sendCode = true;
		$('#getPhoneCode').text('重新获取');
		$('#getPhoneCode').removeAttr('disabled')
		// 清除session code
		getData(function(data) {
			// 清除session code
		}, getContextPath() + '/login/clear/code');
	} else {
		curCounts--;
		$("#getPhoneCode").text('已发送(' + curCounts + ')');
	}
}

// 检查数据完整性
function checkDatas(type) {
	// 检查数据

	showError($('#indent_tele_error'), '');
	showError($('#indent_code_error'), '');
	var contactTele = $('#indent_tele').val().trim();
	var phoneCode = $('#phoneCode').val().trim();
	var flag = true;

	if (contactTele == '' || contactTele == null || contactTele == undefined) {
		showError($('#indent_tele_error'), '请输入手机号码');
		flag = false;
		return flag;
	}

	if (!checkMobile(contactTele)) {
		showError($('#indent_tele_error'), '手机格式不正确');
		flag = false;
		return flag;
	}

	if ((phoneCode == '' || phoneCode == null || phoneCode == undefined)
			&& type == 1) {
		showError($('#indent_code_error'), '请输入手机验证码');
		flag = false;
		return flag;
	}
	return flag;
}

// 验证 手机号
function checkMobile(str) {
	var reg = /^(0|86|17951)?(13[0-9]|15[012356789]|17[0678]|18[0-9]|14[57])[0-9]{8}$/;
	if (str.match(reg)) {
		return true;
	} else {
		return false;
	}
}

function showError(id, error) {
	if (error == "" || error == null) {
		id.attr('data-content', "");
	} else {
		id.attr('data-content', '*' + error);
	}
}

// 成功 提示框弹出方法

function showSuccess() {
	 $('#price').addClass('showPrice');
     $('#price').removeClass('noShow');
     $('#order').removeClass('showOrder');
	if ($('div').hasClass('showSuccess')) {
	} else {

		var $body = '<div class="showSuccess">'
				+ '<div class="successModal">'
	            +'<div class="cCloseBtn" id="closeSuccess">'
	            +' 	<div></div>'
	            +'</div>'
				+ ' 	<div class="show-zero2 zeromodal-icon zeromodal-success">'
				+ ' 		<span class="line tip"></span>'
				+ '   	<span class="line long"></span>'
				+ '   	<div class="placeholder"></div>'
				+ '	</div>'
				+ '   <div class="desBig">您已下单成功!</div>'
				+ '   <div class="desSmall">视频管家将在两小时内与您沟通</div>'
				+ '   <div class="successInfo"><span id="toPortal"></span><span id="last3">3</span>秒后关闭</div>'
				+ '</div>';
		$body += '</div>';
		$("body").append($body);
		successToolTipShow();
		$('.comOrder').remove();
		curCounts = 0;
		window.clearInterval(InterValObj); // 停止计时器
	}
}

function successToolTipShow() {
	$('#toPortal').on('click', function() {
		//window.location.href = getContextPath() + '/';
		$('.showSuccess').remove();
	});
	$('#handClsoe').on('click', function() {
		//window.location.href = getContextPath() + '/';
		$('.showSuccess').remove();
	});
	$('#closeSuccess').on('click', function() {
		//window.location.href = getContextPath() + '/';
		$('.showSuccess').remove();
	});
	window.clearInterval(successIntervalObj);
	successIntervalObj = window.setInterval(firstSuccessTooltip, 1000);
}
function firstSuccessTooltip() {
	if (initM < 0) {
		$('#last3').text('0');
		//window.location.href = getContextPath() + '/';
		clearInterval(successIntervalObj);
		$('.showSuccess').remove();
	} else {
		$('#last3').text(initM--);
	}
}



//通用错误提示
function showCommonError(id, error) {
	if (error == "" || error == null) {
		id.attr('data-content', "");
		id.find('input').removeClass('errorLCommon');
	} else {
		id.attr('data-content', '*' + error);
		id.find('input').addClass('errorLCommon');
	}
}

function resumeCommonError(document){
	showCommonError($(document),'');
    $('input').removeClass('errorLCommon');
}

Date.prototype.Format = function (fmt) {
    var o = {
        "M+": this.getMonth() + 1, //月份 
        "d+": this.getDate(), //日 
        "h+": this.getHours(), //小时 
        "m+": this.getMinutes(), //分 
        "s+": this.getSeconds(), //秒 
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
        "S": this.getMilliseconds() //毫秒 
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}

function getImgUrl(){
	var p = $('#getImgUrl').attr('data-value');
	if(p!=null && p!=''){
		if(p.indexOf("/resources/") == -1){
			$('#getImgUrl').attr('src',getDfsHostName() + p);
		}
	}
}

function controlInput(){
	$("#search-q").focus(function(){
	 $('.middle-part').addClass('consorlInput');
	}); 
	
	$("#search-q").blur(function(){
		 $('.middle-part').removeClass('consorlInput');
		});
}
//控制英文
function controlEnglish(){
//	var enSize = $('#english').text();
//	var result = '';
//	var begin = 0;
//	var end = 0;
//	for (var int = 0; int < enSize.length; int++) {
//		var charcode = parseInt(enSize.charCodeAt(int));
//		if(charcode >= 65 && charcode <= 90 && int != 0){
//			end = int;
//			console.log(begin+'b');
//			console.log(end +'e');
//			var tmp = enSize.substring(begin,end);
//			begin = end;
//			result+=tmp;
//			result+= ' ';
//		}
//		
//		if(int == enSize.length -1){
//			var tmp = enSize.substring(end,enSize.length);
//			result+=tmp;
//			result+= ' ';
//		}
//	}
//	$('#english').text(result);
}

function controlMenu(){
	
	if($('div').hasClass('projectType')){
		  $('.projectType').waypoint(function(direction) {
		        if (direction == "up") { // 了解 拍片网之前
		           $('.motionTitles').removeClass('setTopTgasDiv');
		        }else{
		           $('.motionTitles').addClass('setTopTgasDiv');
		        }
		    });  
	}
	if($('div').hasClass('titleTag')){
		  $('.page').waypoint(function(direction) {
		        if (direction == "up") { // 了解 拍片网之前
		           $('.titleTag').removeClass('setTopTgasDiv');
		        }else{
		           $('.titleTag').addClass('setTopTgasDiv');
		        }
		    });  
	}		
}

//自定义复选框
function initSelect(){
	
	$('.orderSelect').off('click').on('click',function(e){
		$('.oSelect').hide();
		if($(this).hasClass('selectColor')){
			$('.oSelect').slideUp();
			$(this).removeClass('selectColor');
		}
		else
		{
			$('.orderSelect').removeClass('selectColor');
			$(this).find('.oSelect').slideDown();
			$(this).addClass('selectColor');
		}
		e.stopPropagation();
	});
	$('.oSelect li').off('click').on('click',function(e){
		 var id = $(this).attr('data-id');
	   	 $(this).parent().parent().find('div').text($(this).text());
	   	 $(this).parent().parent().find('div').attr('data-id',id);
	   	 $(this).parent().slideUp();
	   	 $('.orderSelect').removeClass('selectColor');
	     e.stopPropagation();
	});
	$('body').off('click').on('click',function(e){
		 $('.oSelect').slideUp();
		 $('.orderSelect').removeClass('selectColor');
		 e.stopPropagation();
	});
	
}


