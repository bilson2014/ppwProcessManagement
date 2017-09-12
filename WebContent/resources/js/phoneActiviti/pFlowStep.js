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

function getStage(){
	
}

function stage1(){
	
	$('#stage1').addClass('yellowItem');
	
}

function stage2(){
	$('#stage1').addClass('greenItem');
	$('#stage2').addClass('yellowItem');
}

function stage3(){
	$('#stage1').addClass('greenItem');
	$('#stage2').addClass('greenItem');
	$('#stage3').addClass('yellowItem');
}
function stage4(){
	$('#stage1').addClass('greenItem');
	$('#stage2').addClass('greenItem');
	$('#stage3').addClass('greenItem');
	$('#stage4').addClass('yellowItem');
}
function stage5(){
	$('#stage1').addClass('greenItem');
	$('#stage2').addClass('greenItem');
	$('#stage3').addClass('greenItem');
	$('#stage4').addClass('greenItem');
	$('#stage5').addClass('yellowItem');
}