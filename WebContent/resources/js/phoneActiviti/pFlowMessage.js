$().ready(function(){
	getMessageMore();
});

function getMessageMore(){
	$('.showMessage').off('click').on('click',function(){
		if($(this).hasClass('open')){
			$(this).removeClass('open');
			$('.setMessage').hide();
		}else{
			$(this).addClass('open');
			$('.setMessage').show();
		}
	});
	$('.itemMore').off('click').on('click',function(){
		if($(this).hasClass('open')){
			$(this).removeClass('open');
			$(this).parent().find('input').hide();
		}else{
			$(this).addClass('open');
			$(this).parent().find('input').show();
		}
	});
	
	
}