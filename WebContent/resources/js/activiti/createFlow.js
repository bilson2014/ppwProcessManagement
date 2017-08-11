var InterValObj; // timer变量，控制时间  
var count = 120; // 间隔函数，1秒执行  
var curCount; // 当前剩余秒数 
var totalPrice = 0;
var timePrice = 0;
var multPrice =0;
$().ready(function() {
	//getProduct();
	//checkInfo();
	
	getSynInfo();
	initAllSelectEven();
	initAutoChoose();
	autoInput();
	submitEven();
	dataEven();
	getProduct();
	
	var myDate = new Date();
	//获取当前年
	var year=myDate.getFullYear();
	//获取当前月
	var month=myDate.getMonth()+1;
	//获取当前日
	var date=myDate.getDate();  
	$('#pf_createDate').val(year+"-"+month+"-"+date);
	
});

function submitEven(){
	$('#toSubmit').off('click').on('click',function(){
		
		if(checkInfo()){

			$('#toListForm').submit();

		}
	//	
	});
}

function initAllSelectEven(){
	initSelect();
	initMultSelect();
	productConfigLengthEven();
	timePriceEven();
	$('#setProduct li').off('click');
	initProductEven();
}

function dataEven(){
/*	$("#pf_createDate").datepicker({
		language: 'zh',
		dateFormat:'yyyy-MM-dd'
     });*/	
}

function checkInfo(){
	$('.errorItem').attr('data-content','');	
	var projectNo = $('#projectNo').val();//项目编号
	var projectName = $('#projectName').val();//项目名称
	var projectGrade = $('#projectGrade').attr('data-id');//项目评级
	var projectSource = $('#projectSource').attr('data-id');//项目编号
	var productId = $('#productId').attr('data-id');//产品线
	var productConfigLevelId = $('#productConfigLevelId').attr('data-id');//等级
	var productConfigLength = $('#productConfigLength').attr('data-id');//时长
	var productConfigAdditionalPackageIds = $('#productConfigAdditionalPackageIds').attr('data-id');//附加包
	var createDate = $('#pf_createDate').val();//立项时间
	var projectSql = $('#pf_projectSql').val();//项目周期
	var filmDestPath = $('#pf_filmDestPath').val();//对标影片
	
	//项目信息
	if(projectNo == undefined || projectNo == "" || projectNo ==null ){
		$('#projectNoError').attr('data-content','项目编号未填写');
		$('#projectNo').focus();
		return false;
	}
	if(projectName == undefined || projectName == "" || projectName ==null ){
		$('#projectNameError').attr('data-content','项目名称未填写');
		$('#projectName').focus();
		return false;
	}
	if(projectGrade == undefined || projectGrade == "" || projectGrade ==null ){
		$('#projectGradeError').attr('data-content','项目评级未填写');
		return false;
	}else{
		$('#pf_projectGrade').val(projectGrade);
	}
	if(projectSource == undefined || projectSource == "" || projectSource ==null ){
		$('#projectSourceError').attr('data-content','项目来源未填写');
		return false;
	}else{
		$('#pf_projectSource').val(projectSource);
	}
	if(productId == undefined || productId == "" || productId ==null ){
		$('#productIdError').attr('data-content','产品线未填写');
		return false;
	}else{
		$('#pf_productId').val(productId);
		$('#pf_productName').val($('#productId').text());
	}
	if(productId != '0'){
		if(productConfigLevelId == undefined || productConfigLevelId == "" || productConfigLevelId ==null ){
			$('#productConfigLevelIdError').attr('data-content','等级未填写');
			return false;
		}
		if(productConfigLength == undefined || productConfigLength == "" || productConfigLength ==null ){
			$('#productConfigLengthError').attr('data-content','时长未填写');
			return false;
		}
		if(productConfigAdditionalPackageIds != undefined && productConfigAdditionalPackageIds != "" && productConfigAdditionalPackageIds ==null ){
			$('#pf_productConfigAdditionalPackageIds').val(productConfigAdditionalPackageIds);
		}
	}
	if(createDate == undefined || createDate == "" || createDate ==null ){
		$('#createDateError').attr('data-content','立项时间未填写');
		$('#createDate').focus();
		return false;
	}
		
	if(projectSql == undefined || projectSql == "" || projectSql ==null ){
		$('#projectSqlError').attr('data-content','项目周期未填写');
		$('#pf_projectSql').focus();
		return false;
	}
	if(!checkNumber(projectSql)){
		$('#projectSqlError').attr('data-content','项目周期填写错误');
		$('#pf_projectSql').focus();
		return false;
	}
	if(filmDestPath == undefined || filmDestPath == "" || filmDestPath ==null ){
		$('#filmDestPathError').attr('data-content','对标影片未填写');
		$('#filmDestPath').focus();
		return false;
	}
	
	//协同人信息
	var customerDirector = $('#customerDirector').attr('data-id');//客服总监
	var saleDirector = $('#saleDirector').attr('data-id');//销售总监
	var creativityDirector = $('#creativityDirector').attr('data-id');//创意总监
	var superviseDirector = $('#superviseDirector').attr('data-id');//监制总监
	var teamDirector = $('#teamDirector').attr('data-id');//供应商总监
	var teamProvider = $('#teamProvider').attr('data-id');//供应商管家
	var teamPurchase = $('#teamPurchase').attr('data-id');//供应商采购
	var financeDirector = $('#financeDirector').attr('data-id');//财务主管
	var finance = $('#finance').attr('data-id');//财务出纳
	if(customerDirector == undefined || customerDirector == "" || customerDirector ==null ){
		$('#customerDirectorError').attr('data-content','客服总监未选择');
		return false;
	}else{
		$('#ps_customerDirector').val(customerDirector);
	}
	if(saleDirector == undefined || saleDirector == "" || saleDirector ==null ){
		$('#saleDirectorError').attr('data-content','销售总监未选择');
		return false;
	}else{
		$('#ps_saleDirector').val(saleDirector);
	}
	if(creativityDirector == undefined || creativityDirector == "" || creativityDirector ==null ){
		$('#creativityDirectorError').attr('data-content','创意总监未选择');
		return false;
	}else{
		$('#ps_creativityDirector').val(creativityDirector);
	}
	if(superviseDirector == undefined || superviseDirector == "" || superviseDirector ==null ){
		$('#superviseDirectorError').attr('data-content','监制总监未选择');
		return false;
	}else{
		$('#ps_superviseDirector').val(superviseDirector);
	}
	if(teamDirector == undefined || teamDirector == "" || teamDirector ==null ){
		$('#teamDirectorError').attr('data-content','供应商总监未选择');
		return false;
	}else{
		$('#ps_teamDirector').val(teamDirector);
	}
	if(teamProvider == undefined || teamProvider == "" || teamProvider ==null ){
		$('#teamProviderError').attr('data-content','供应商管家未选择');
		return false;
	}else{
		$('#ps_teamProvider').val(teamProvider);
	}
	if(teamPurchase == undefined || teamPurchase == "" || teamPurchase ==null ){
		$('#teamPurchaseError').attr('data-content','供应商采购未选择');
		return false;
	}else{
		$('#ps_teamPurchase').val(teamPurchase);
	}
	if(financeDirector == undefined || financeDirector == "" || financeDirector ==null ){
		$('#financeDirectorError').attr('data-content','财务主管未选择');
		return false;
	}else{
		$('#ps_financeDirector').val(financeDirector);
	}
	if(finance == undefined || finance == "" || finance ==null ){
		$('#financeError').attr('data-content','财务出纳未选择');
		return false;
	}else{
		$('#ps_finance').val(teamPurchase);
	}
	//客户信息
	var userName = $('#pu_userName').attr('data-id');//客户名称
	var linkman = $('#pu_linkman').val();//客户联系人
	var telephone = $('#pu_telephone').val();//客户电话
	var userLevel = $('#userLevel').attr('data-id');//客户评级
	var email = $('#pu_email').val();//客户评级
	if(userName == undefined || userName == "" || userName ==null ){
		$('#userNameError').attr('data-content','客户名称未填写');
		$('#pu_userName').focus();
		return false;
	}
	if(linkman == undefined || linkman == "" || linkman ==null ){
		$('#linkmanError').attr('data-content','客户联系人未填写');
		$('#pu_linkman').focus();
		return false;
	}
	if(telephone == undefined || telephone == "" || telephone ==null ){
		$('#telephoneError').attr('data-content','客户电话未填写');
		$('#pu_telephone').focus();
		return false;
	}
	
	// 验证电话号码正确性
	if (!checkMobile(telephone)) {
		$('#telephoneError').attr('data-content','手机号码格式未填写');
		$('#pu_telephone').focus();
		return false;
	}
	
	if(userLevel == undefined || userLevel == "" || userLevel ==null ){
		$('#userLevelError').attr('data-content','客户评级未填写');
		$('#pu_userLevel').focus();
		return false;
	}
	
	if(email == undefined || email == "" || email ==null ){
		$('#emailError').attr('data-content','邮箱未填写');
		$('#pu_email').focus();
		return false;
	}
	
	// 验证邮箱正确性
	if (!checkEmail(email)) {
		$('#emailError').attr('data-content','邮箱格式不正确');
		$('#pu_email').focus();
		return false;
	}
	
	return true;

}

function initMultSelect(){
	$('.orderMultSelect').off('click').on('click',function(e){
		if(!$('.multSelect').hasClass('selectColor')){
			$('.orderMultSelect').removeClass('selectColor');
			$(this).find('.multSelect').slideDown();
			$(this).addClass('selectColor');
		}
		e.stopPropagation();
	});
	$('.multSelect li input').off('click').on('click',function(e){
		 var nowThis = $('.multSelect li input');
		 var checkInfo = $('#multInfo').text();
		 var multInfo = '';
		 var multID = '';
		 var realPrice = 0;
		 for (var int = 0; int < nowThis.length; int++) {
			 if($(nowThis[int]).is(':checked')){
				   if(multInfo == null||multInfo == ""){
					   multInfo = multInfo + $(nowThis[int]).parent().find('div').text();
					   multID = multID + $(nowThis[int]).attr('data-id');
					   realPrice = parseInt(realPrice) + parseInt($(nowThis[int]).attr('data-price'));
				   }else{
					   multInfo = multInfo +"+"+ $(nowThis[int]).parent().find('div').text();
					   multID = multID +","+$(nowThis[int]).attr('data-id');
					   realPrice = parseInt(realPrice) + parseInt($(nowThis[int]).attr('data-price'));
				   }
			 }
		}
		 realPrice = parseInt(realPrice) +  parseInt(totalPrice) +  parseInt(timePrice);
		 $('#estimatedPrice').val(realPrice);
		 $('#productConfigAdditionalPackageIds').text(multInfo);
		 $('#pf_productConfigAdditonalPackageName').val(multInfo);
		 $('#pf_productConfigAdditionalPackageIds').attr('data-id',multID);
		 var id = $(this).attr('data-id');
		 var multInfo = $('#multInfo').text();
	});
	$('body').off('click').on('click',function(e){
		 $('.multSelect').slideUp();
		 $('.oSelect').slideUp();
		 $('.orderSelect').removeClass('selectColor');
		 $('.orderMultSelect').removeClass('selectColor');
		 e.stopPropagation();
	});
}
//自动联动客户信息
function autoInput(){
	$('#pu_userName').bind('input propertychange', function() {
		$(this).attr('data-id','');
		var theName = $(this).val();
		 findAutoInfo(theName);
		 $('.autoFindCus').show();
		 if(theName == null || theName == ""){
			 $('.autoFindCus').hide();
		 }
	});
}

function initAutoChoose(){
	$('.autoFindCus li').off('click').on('click',function(e){
		 var name = $(this).text();
		 var id = $(this).attr('data-id');
		 var level = $(this).attr('data-clientLevel');
		 $('#pu_userName').val(name);
		 $('#pu_userName').attr('data-id',id);
		 $('#pu_userId').val(id);
		 $('#pu_linkman').val($(this).attr('data-realName'));
		 $('#pu_telephone').val($(this).attr('data-phone'));
		 if($(this).attr('data-email')!='null'){
		 $('#pu_email').val($(this).attr('data-email'));
		 }
		 getValue(level);
		 $('.autoFindCus').hide();
	});
}

function findAutoInfo(userName){
	loadData(function(res){
		var res = res;
		var body = $('.autoFindCus');
		body.html('');
		if(res != null && res != undefined){
			for (var int = 0; int < res.length; int++) {
				   var html =createUserInfo(res[int].id,res[int].userName,res[int].telephone,res[int].realName,res[int].clientLevel,res[int].email);
				   body.append(html);
			};
			initAutoChoose();
		}
	}, getContextPath() + '/user/search/info', $.toJSON({
		userName : userName
	}));
}

//获取协同人
function getSynInfo(){
	loadData(function(res){
		//客服总监
		var customerDirectors = res.result.customerDirector;
		var body = $('#customerDirectors');
		body.html('');
		if(customerDirectors != null && customerDirectors != undefined){
			for (var int = 0; int < customerDirectors.length; int++) {
					var html =createOption(customerDirectors[int].id,customerDirectors[int].firstName);
				   body.append(html);
			};
			
		}
		//销售总监
		var saleDirectors = res.result.saleDirector;
		var body = $('#saleDirectors');
		body.html('');
		if(saleDirectors != null && saleDirectors != undefined){
			for (var int = 0; int < saleDirectors.length; int++) {
					var html =createOption(saleDirectors[int].id,saleDirectors[int].firstName);
				   body.append(html);
			};
			
		}
		//创意总监
		var creativityDirectors = res.result.creativityDirector;
		var body = $('#creativityDirectors');
		body.html('');
		if(creativityDirectors != null && creativityDirectors != undefined){
			for (var int = 0; int < creativityDirectors.length; int++) {
					var html =createOption(creativityDirectors[int].id,creativityDirectors[int].firstName);
				   body.append(html);
			};
			
		}
		//监制总监
		var superviseDirectors = res.result.superviseDirector;
		var body = $('#superviseDirectors');
		body.html('');
		if(superviseDirectors != null && superviseDirectors != undefined){
			for (var int = 0; int < superviseDirectors.length; int++) {
					var html =createOption(superviseDirectors[int].id,superviseDirectors[int].firstName);
				   body.append(html);
			};
			
		}
		//供应商总监
		var teamDirectors = res.result.teamDirector;
		var body = $('#teamDirectors');
		body.html('');
		if(teamDirectors != null && teamDirectors != undefined){
			for (var int = 0; int < teamDirectors.length; int++) {
					var html =createOption(teamDirectors[int].id,teamDirectors[int].firstName);
				   body.append(html);
			};
			
		}
		
		//供应商管家
		var teamProviders = res.result.teamProvider;
		var body = $('#teamProviders');
		body.html('');
		if(teamProviders != null && teamProviders != undefined){
			for (var int = 0; int < teamProviders.length; int++) {
					var html =createOption(teamProviders[int].id,teamProviders[int].firstName);
				   body.append(html);
			};
			
		}
		
		//供应商采购
		var teamPurchases = res.result.teamPurchase;
		var body = $('#teamPurchases');
		body.html('');
		if(teamPurchases != null && teamPurchases != undefined){
			for (var int = 0; int < teamPurchases.length; int++) {
					var html =createOption(teamPurchases[int].id,teamPurchases[int].firstName);
				   body.append(html);
			};
			
		}
		
		//财务总监
		var financeDirectors = res.result.financeDirector;
		var body = $('#financeDirectors');
		body.html('');
		if(financeDirectors != null && financeDirectors != undefined){
			for (var int = 0; int < financeDirectors.length; int++) {
					var html =createOption(financeDirectors[int].id,financeDirectors[int].firstName);
				   body.append(html);
			};
			
		}
		
		//财务出纳
		var finances = res.result.finance;
		var body = $('#finances');
		body.html('');
		if(finances != null && finances != undefined){
			for (var int = 0; int < finances.length; int++) {
					var html =createOption(finances[int].id,finances[int].firstName);
				   body.append(html);
			};
			initAllSelectEven();
		}		
		initAllSelectEven();
	}, getContextPath() + '/synergy/memberGroup',null);	
}

function getValue(id){	
	 var hasLi = $('#cusLevel li');
	 for (var int = 0; int < hasLi.length; int++) {
			var hasId = $(hasLi[int]).attr('data-id');
			if(hasId == id){
				$('#userLevel').text($(hasLi[int]).text());
				$('#userLevel').attr('data-id',hasId);
				$('#pu_userLevel').val($(hasLi[int]).text());
				$('#pu_userLevel').attr('data-id',hasId);
			}
	};
}

//产品事件
function initProductEven(){
	$('#setProduct li').off('click').on('click',function(e){
		    $('#productConfigLevelId').text('');
		    $('#productConfigLevelId').attr('data-id');
		    $('#pf_productConfigLevelId').val('');
		    $('#productConfigLength').text('');
		    $('#productConfigLength').attr('data-id');
		    $('#pf_productConfigLength').val('');
		    $('#productConfigAdditionalPackageIds').text('');
		    $('#productConfigAdditionalPackageIds').attr('data-id');
		    $('#pf_productConfigAdditionalPackageIds').val('');
		    var id = $(this).attr('data-id');
		   	$(this).parent().parent().find('div').text($(this).text());
		   	$(this).parent().parent().find('div').attr('data-id',id);
		   	$(this).parent().slideUp();
		   	totalPrice = $(this).attr('data-price');
		   	$('.orderSelect').removeClass('selectColor');
		   	if($(this).text()=="非标"){
		   		$('.noclick').off('click');
		   	}else{
		   		productConfigLengthEven();
		   	}
		   	getLevel(id);
		   	e.stopPropagation();
	});
}
//等级事件
function productConfigLengthEven(){
	$('#setpProductConfigLength li').off('click').on('click',function(e){
		    $('#productConfigLength').text('');
		    $('#productConfigLength').attr('data-id');
		    $('#pf_productConfigLength').val('');
		    $('#productConfigAdditionalPackageIds').text('');
		    $('#productConfigAdditionalPackageIds').attr('data-id');
		    $('#pf_productConfigAdditionalPackageIds').val('');	    
		    var id = $(this).attr('data-id');
		   	$(this).parent().parent().find('div').text($(this).text());
		   	$(this).parent().parent().find('div').attr('data-id',id);
		   	$('#pf_productConfigLevelId').val(id);
		   	$('#pf_productConfigLevelName').val($(this).text());
		   	$(this).parent().slideUp();
		   	$('.orderSelect').removeClass('selectColor');
		   	totalPrice = parseInt($(this).attr('data-price'));
		   	$('#estimatedPrice').val(totalPrice);		   	
		   	getTime(id)
		   	$('#timePrice li').off('click');
		   	timePriceEven();
			initMultSelect();
		   	e.stopPropagation();
	});
}
//时长事件
function timePriceEven(){
	$('#setTime li').off('click').on('click',function(e){
		    var id = $(this).attr('data-id');
		   	$(this).parent().parent().find('div').text($(this).text());
		   	$(this).parent().parent().find('div').attr('data-id',id);
		   	$(this).parent().slideUp();
		   	$('#pf_productConfigLength').val(id);
		   	$('.orderSelect').removeClass('selectColor');
		   	timePrice = parseInt(totalPrice) + parseInt($(this).attr('data-price'));
		   	$('#estimatedPrice').val(timePrice);	   
		   	e.stopPropagation();
	});
}

function getProduct(){
	loadData(function (res){
		var body = $('#setProduct');
		body.html('');
		var html = createOption('0','非标','0');
		var rows = res.result.chanpin;
		body.append(html);
		if(res != null && res != undefined){
			for (var int = 0; int < rows.length; int++) {
					var html =createOption(rows[int].id,rows[int].text,rows[int].price);
				body.append(html);
			};
			initProductEven();
		}
		var body = $('#pResour');
		body.html('');
		var rowsR = res.result.resource;
		body.append(html);
		if(rowsR != null && rowsR != undefined){
			for (var int = 0; int < rowsR.length; int++) {
					var html =createOption(rowsR[int].id,rowsR[int].text);
				body.append(html);
			};
			
		}
		
		var body = $('#cusLevel');
		body.html('');
		var rowsC = res.result.clientLevel;
		body.append(html);
		if(rowsC != null && rowsC != undefined){
			for (var int = 0; int < rowsC.length; int++) {
					var html =createOption(rowsC[int].id,rowsC[int].text);
				body.append(html);
			};
			
		}
		
	}, getContextPath() + '/product/productSelection',null);
}

function getLevel(id){
	loadData(function (res){
		var body = $('#setpProductConfigLength');
		body.html('');
		var rows = res.result.config;
		if(res != null && res != undefined){
			for (var int = 0; int < rows.length; int++) {
					var html =createOption(rows[int].id,rows[int].text,rows[int].price);
				body.append(html);
			};
			initAllSelectEven();
		}
	}, getContextPath() + '/product/ConfigSelection/'+ id,null);
}

function getTime(id){
	loadData(function (res){
		var body = $('#setTime');
		body.html('');
		var rows = res.result.dimension;
		if(res != null && res != undefined){
			for (var int = 0; int < rows.length; int++) {
					var html =createOption(rows[int].id,rows[int].text,rows[int].price);
				body.append(html);
			};
			initAllSelectEven();
		}
		var bodys = $('#setMult');
		bodys.html('');
		var multRow = res.result.modules;
		if(res != null && res != undefined){
			for (var int = 0; int < multRow.length; int++) {
					var html =createMultOption(multRow[int].id,multRow[int].text,multRow[int].price);
				bodys.append(html);
			};
			initAllSelectEven();
		}
	}, getContextPath() + '/product/detailSelection/'+ id,null);
}


function createOption(value,text,price){
		var html = '<li data-price="'+ price +'" data-id="'+ value +'">'+text+'</li>';
		return html;
}

function createUserInfo(id,name,phone,realName,clientLevel,email){
	var html = '<li data-email="'+email+'"  data-clientLevel="'+ clientLevel +'" data-realName="'+ realName +'" data-phone="'+ phone +'" data-id="'+ id +'">'+name+'</li>';
	return html;
}

function createMultOption(value,text,price){
	var html = ' <li><input type="checkbox" data-id="'+ value +'" data-price="'+ price +'"><div>'+text+'</div></li>';
	return html;
}





