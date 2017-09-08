$().ready(function(){
	getFileMore();
});

function getFileMore(){
    $('.fileMore').off('click').on('click',function(){
    	if($(this).hasClass('open')){
    		$(this).removeClass('open');
    		$(this).parent().find('.fileContentMore').slideUp();
    	}else{
    		$(this).addClass('open');
    		$(this).parent().find('.fileContentMore').slideDown();
    	}
    });
}