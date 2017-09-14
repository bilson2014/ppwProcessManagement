$().ready(function(){
	document.domain = getUrl();	
	windowChange();
	$(window.parent.document).find('.footBot').show();
});
function windowChange(){
	var screenWidth = parseInt(document.body.clientWidth);
	var fontSize = parseInt(screenWidth/1080*100);
	$('html').css('font-size',fontSize);
	$('.pagePhone').css('opacity','1');
	$(window.parent.document).find('.headerCom').hide();
	window.onload=function(){
		$(window.parent.document).find('.frame').css('height',$('body').height());
		
	};
}

function getHeight(){
	(window.parent.document).find('.frame').css('height',$('body').height());
}