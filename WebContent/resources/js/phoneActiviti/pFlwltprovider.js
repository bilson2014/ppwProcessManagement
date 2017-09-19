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
                     $('#scCusName').val(scheme[i].pt_teamName);
                     $('#scLink').val(scheme[i].pt_linkman);
                     $('#scTel').val(scheme[i].pt_telephone);
                     $('#scId').val(scheme[i].pt_projectTeamId);
				   }
		    }
        
        if(produce == undefined || produce == "" || produce ==null ){
		      $('#isHideBot').remove();
		    }else{
		    	 for (var i = 0; i < scheme.length; i++) {
                     $('#prCusName').val(produce[i].pt_teamName);
                     $('#prLink').val(produce[i].pt_linkman);
                     $('#prTel').val(produce[i].pt_telephone);
                     $('#prId').val(produce[i].pt_projectTeamId);
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