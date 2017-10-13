$().ready(function(){
	$('.frameHead .name').text('供应商信息修改');
	getInfo();
});

function getInfo(){
	loadData(function(res){
        var scheme = res.project_team_scheme;
        var produce = res.project_team_produce;
        if(scheme == undefined || scheme == "" || scheme ==null ){
		      $('#isHideTop').remove();
		    }else{
		    	 for (var i = 0; i < scheme.length; i++) {
		    		var  pt_name = scheme[i].pt_teamName;
		    		var  pt_linkman = scheme[i].pt_linkman;
		    		var  pt_telephone = scheme[i].pt_telephone;
		    		 $('#scName').val(produce[i].pt_teamName);
                     $('#scLink').val(scheme[i].pt_linkman);
                     $('#scTel').val(scheme[i].pt_telephone);
                     $('#scId').val(scheme[i].pt_projectTeamId);
                     $('#scTeamId').val(scheme[i].pt_teamId);
                     if(pt_name == undefined || pt_name == "" || pt_name == null){
                  		$('#scTeam').remove();
              		}
                     if(pt_linkman == undefined || pt_linkman == "" || pt_linkman == null){
                 		$('#scLink').remove();
             		}
                 	if(pt_telephone == undefined || pt_telephone == "" || pt_telephone == null){
                 		$('#scTel').remove();
             		}
				   }
		    }
        
        if(produce == undefined || produce == "" || produce ==null ){
		      $('#isHideBot').remove();
		    }else{
		    	 for (var i = 0; i < scheme.length; i++) {
		    		 var pt_name = produce[i].pt_teamName;
		    		 var pt_linkman = produce[i].pt_linkman;
  		    		 var pt_telephone = produce[i].pt_telephone;
                     $('#prName').val(produce[i].pt_teamName);
                     $('#prLink').val(produce[i].pt_linkman);
                     $('#prTel').val(produce[i].pt_telephone);
                     $('#prId').val(produce[i].pt_projectTeamId);
                     $('#prTeamId').val(produce[i].pt_teamId);
                     if(pt_name == undefined || pt_name == "" || pt_name == null){
                   		$('#prTeam').remove();
               		}
                     if(pt_linkman == undefined || pt_linkman == "" || pt_linkman == null){
                  		$('#mName').remove();
              		}
                  	if(pt_telephone == undefined || pt_telephone == "" || pt_telephone == null){
                  		$('#mPhone').remove();
              		}
				   }
		    }
          autoInput();
          autoInputTeam();
		}, getContextPath() + '/project/task/edit/parameter/'+$("#taskId").val()+"/"+$('#projectId').val()+"/pt",null);
	
	$('#submitProvide').off('click').on('click',function(){		
		if(checkProvider()){
			$('#toProForm').submit();
		}
	});

}

function checkProvider(){
    var error = $('.checkError');
    var errorP = $('.checkErrorP');
    $('.error').hide();
    for (var i = 0; i < error.length; i++) {
	    	 var word = $(error[i]).val();
	    	 if(word == undefined || word == "" || word ==null ){
	    		 $(error[i]).parent().find('.error').show();
	 			return false;
	 		}
	   }
    for (var i = 0; i < errorP.length; i++) {
   	 var phone = $(errorP[i]).val();
   	 if(!checkMobile(phone)){
   		 $(errorP[i]).parent().find('.error').show();
		return false;
		}
    }
    return true;
}

//自动联动客户信息
function autoInput(){
	$('#scName').bind('input propertychange', function() {
		 $('#scId').val("");
		var theName = $(this).val();
		 findAutoInfo(theName);
		 $('.utoInfoMake').show();
		 if(theName == null || theName == ""){
			 $('.utoInfoMake').hide();
		 }
	});
}

function findAutoInfo(userName){
	loadData(function(res){
		var res = res;
		var body = $('.utoInfoMake');
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
	$('.utoInfoMake li').off('click').on('click',function(){
		  $('.utoInfoMake').hide();
		  var name = $(this).text();
		  var id = $(this).attr('data-id');
		  var linkman = $(this).attr('data-linkman');
		  var phone = $(this).attr('data-phone');
		  $('#scName').val(name);
		  $('#scTeamId').val(id);
		  $('#scLink').val(linkman);
		  $('#scTel').val(phone);
	});
}

//自动联动制作供应商信息
function autoInputTeam(){
	$('#prName').bind('input propertychange', function() {
		 $('#prId').val("");
		var theName = $(this).val();
		findAutoInfoTeam(theName);
		 $('.utoInfoMakeTeam').show();
		 if(theName == null || theName == ""){
			 $('.utoInfoMakeTeam').hide();
		 }
	});
}

function findAutoInfoTeam(userName){
	loadData(function(res){
		var res = res;
		var body = $('.utoInfoMakeTeam');
		body.html('');
		if(res != null && res != undefined){
			for (var int = 0; int < res.length; int++) {
				   var html =createUserInfo(res[int].teamId,res[int].teamName,res[int].linkman,res[int].phoneNumber);
				   body.append(html);
			};
			autoLiTeam();
		}
	}, getContextPath() + '/team/listByName/'+userName,null);
}

function autoLiTeam(){
	$('.utoInfoMakeTeam li').off('click').on('click',function(){
		  $('.utoInfoMakeTeam').hide();
		  var name = $(this).text();
		  var id = $(this).attr('data-id');
		  var linkman = $(this).attr('data-linkman');
		  var phone = $(this).attr('data-phone');
		  $('#prName').val(name);
		  $('#prTeamId').val(id);
		  $('#prLink').val(linkman);
		  $('#prTel').val(phone);
	});
}

function createUserInfo(id,name,linkman,phone){
	var html = '<li data-id="'+id+'"  data-linkman="'+ linkman +'" data-phone="'+ phone +'">'+name+'</li>';
	return html;
}