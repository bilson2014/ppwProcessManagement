var clip;
$().ready(function(){
	$('.frameHead .name').text($('#projectName').val());
	getFileInfo();	
});

function getFileMore(){
    $('.fileMore').off('click').on('click',function(){
    	if($(this).hasClass('open')){
    		$(this).removeClass('open');
    		$(this).parent().find('.fileContentMore').slideUp();
    	}else{
    		  $('.fileMore').removeClass('open');
    		  $('.fileContentMore').slideUp();
    		$(this).addClass('open');
    		$(this).parent().find('.fileContentMore').slideDown();
    	}
    });
}

//文件区域
function getFileInfo(){
	loadData(function(res){
		var res = res;
		var body =$('.setFile');
		body.html('');
		if(res != null && res != undefined && res.length>0){
			var newList = bulidFileList(res);
				for (var int = 0; int < newList.length; int++) {
					 var html =createFileInfo(newList[int]);
					 body.append(html);
				}
				getFileMore();
				bindFileShareBtn();
		}else{
			$('#daiban').show();
			$('#daibanword').text('未上传文件');
		}
	}, getContextPath() + '/resource/list/'+$('#projectId').val(),null);	
}
//文件排序
function bulidFileList(arr) {
    var len = arr.length;
    for (var i = 0; i < len; i++) {
        for (var j = 0; j < len - 1 - i; j++) {
        	var fileOne = new Date((arr[j].createDate).replace("CST","GMT+0800"));
        	var fileTwo =  new Date((arr[j+1].createDate).replace("CST","GMT+0800"));
            if (fileOne < fileTwo) {        // 相邻元素两两对比
                var temp = arr[j+1];        // 元素交换
                arr[j+1] = arr[j];
                arr[j] = temp;
            }
        }
    }
    return arr;
}
//文件卡片
function createFileInfo(res){
	var name = res.resourceName;
	var fileName = name.lastIndexOf(".");
	var finalName = name.substring(fileName + 1);
	var src = '/resources/images/pFlow/';
	switch (finalName) {
		case 'doc' :
		case 'docx' :
			src += 'doc.png';
			break;
		case 'xls' :
		case 'xlsx' :
			src += 'xls.png';
			break;
		case 'ppt' :
		case 'pptx' :
			src += 'ppt.png';
			break;
		case 'pdf' :
			src += 'pdf.png';
			break;
		case 'txt' :
			src += 'txt.png';
			break;
		case 'avi' :
			src += 'avi.png';
			break;
		case 'esp' :
			src += 'esp.png';
			break;
		case 'jpg' :
			src += 'jpg.png';
			break;
		case 'mov' :
			src += 'mov.png';
			break;
		case 'mp3' :
			src += 'mp3.png';
			break;
		case 'mp4' :
			src += 'mp4.png';
			break;
		case 'png' :
			src += 'png.png';
			break;
		case 'rar' :
			src += 'rar.png';
			break;
		case 'wav' :
			src += 'wav.png';
			break;
		case 'zip' :
			src += 'zip.png';
			break;
		default :
			src += 'file.png';
			break;
	}
	var fileName = name.lastIndexOf(".");
	var checkName = name.substring(0,fileName);
	var url = getDfsHostName() + res.resourcePath;
	var urls= getDfsHostName() + res.previewPath;
	var ss=res.previewPath;
	if (ss==null){
		var html = [
			'	 <div class="item">',
			'	        <img class="file" src="'+src+'">           ',
			'	        <img class="fileMore" src="/resources/images/pFlow/fileMore.png">  ',
			'	        <div class="fileContent">                                   ',
			'	              <div>'+checkName+'</div>                                     ',
			'	              <div>'+formatDate((res.createDate).replace("CST","GMT+0800"))+''+checkName+'</div>                                 ',
			'	        </div>                                                      ',
			'	        <div class="fileContentMore">                               ',
			'	             <div class="moreItem" style="width:50%;">                                 ',
			'                     <a href="/resource/getDFSFile/'+res.projectResourceId+'">',
			'		              <img src="/resources/images/pFlow/download.png">         ',
			'		              <div>下载</div>                                   ',
			'                     </a>',
			'	             </div>                                                 ',
			'	             <div class="moreItem share" style="width:50%;">                                 ',
			'		              <img src="/resources/images/pFlow/share.png">            ',
			'		              <div data-id="'+url+'">分享</div>                                   ',
			'	             </div>                                                 ',
			'	        </div>                                                      ',
			'	 </div>                                                             ',
		].join('');
		return html;
	}else{
		var html = [
			'	 <div class="item">',
			'	        <img class="file" src="'+src+'">           ',
			'	        <img class="fileMore" src="/resources/images/pFlow/fileMore.png">  ',
			'	        <div class="fileContent">                                   ',
			'	              <div>'+checkName+'</div>                                     ',
			'	              <div>'+formatDate((res.createDate).replace("CST","GMT+0800"))+''+checkName+'</div>                                 ',
			'	        </div>                                                      ',
			'	        <div class="fileContentMore">                               ',
			'	             <div class="moreItem">                                 ',
			'                     <a href="/resource/getDFSFile/'+res.projectResourceId+'">',
			'		              <img src="/resources/images/pFlow/download.png">         ',
			'		              <div>下载</div>                                   ',
			'                     </a>',
			'	             </div>                                                 ',
			'	             <div class="moreItem look">                                 ',
			'		              <a href="'+url+'"><img src="/resources/images/pFlow/look.png">            ',
			'		             <div data-id="'+url+'">查看</div></a>                                   ',
			'	             </div>                                                 ',
			'	             <div class="moreItem share">                                 ',
			'		              <img src="/resources/images/pFlow/share.png">            ',
			'		              <div data-id="'+url+'">分享</div>                                   ',
			'	             </div>                                                 ',
			'	        </div>                                                      ',
			'	 </div>                                                             ',
		].join('');
		return html;
	}
}
function bindFileShareBtn(){
	$('.share').on('click',function(){
		$('.modelTool .success button').attr("class","");
		$('.modelTool .success button').addClass('btn-c-r');
		$('.modelTool .success button').addClass('btnShare');
		$('.btnShare').text('复制链接');
		var key = $(this).find('div').attr('data-id');
		$('#setInfoCopy').text(key);
		showShare();
	});
}

function showShare(){
	$('#showShare').show();
	getCopyUrl();
	closeModel();
}

function closeModel(){
	$('#closeModel').off('click').on('click',function(){
		$('#showShare').hide();
		$('.btnShare').attr("class","");
		$('.btnShare').addClass('btn-c-r');
		$('.btnShare').text('复制链接');
	});
}

function getCopyUrl(){	
	var clipboard = new Clipboard('.btnShare');   
	   clipboard.on('success', function(e) {  
		        $('.btnShare').text('复制成功');
				$('.btnShare').attr("class","");
				$('.btnShare').addClass('btn-c-g');
	        });  
	   clipboard.on('error', function(e) {  
		   		$('.btnShare').text('复制失败');
	        }); 
}

function jumpShare(url) {
	$('#share-open').click();	
	share.init(url, '项目文件', getContextPath() + '/resources/banner/flex1.jsp');
}

