$().ready(function(){
	getStepMore();
});

function getStepMore(){
 
    $('.itemTop').off('click').on('click',function(){
    	
    	if($(this).hasClass('open')){
			$(this).removeClass('open');
			$(this).parent().find('.itemInfo').slideUp();
		}else{
			$(this).addClass('open');
			$(this).parent().find('.itemInfo').slideDown();
		}
    })
    
    
}