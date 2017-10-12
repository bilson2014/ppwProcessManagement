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
		    		 pt_linkman = scheme[i].pt_linkman;
		    		 pt_telephone = scheme[i].pt_telephone;
                     $('#scLink').val(scheme[i].pt_linkman);
                     $('#scTel').val(scheme[i].pt_telephone);
                     $('#scId').val(scheme[i].pt_projectTeamId);
                     if(pt_linkman == undefined || pt_linkman == "" || pt_linkman == null){
                 		$('#pName').remove();
             		}
                 	if(pt_telephone == undefined || pt_telephone == "" || pt_telephone == null){
                 		$('#pPhone').remove();
             		}
				   }
		    }
        
        if(produce == undefined || produce == "" || produce ==null ){
		      $('#isHideBot').remove();
		    }else{
		    	 for (var i = 0; i < scheme.length; i++) {
		    		 var pt_linkman = produce[i].pt_linkman;
  		    		 var pt_telephone = produce[i].pt_telephone;
                     $('#prCusName').val(produce[i].pt_teamName);
                     $('#prLink').val(produce[i].pt_linkman);
                     $('#prTel').val(produce[i].pt_telephone);
                     $('#prId').val(produce[i].pt_projectTeamId);
                     if(pt_linkman == undefined || pt_linkman == "" || pt_linkman == null){
                  		$('#mName').remove();
              		}
                  	if(pt_telephone == undefined || pt_telephone == "" || pt_telephone == null){
                  		$('#mPhone').remove();
              		}
				   }
		    }
        
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