define(['jquery','common'],function($,c){
	var common;
	function VideoLayout(){
		this.name = 'VideoLayout'
		common = new c.Common();
	}

	VideoLayout.prototype = {
		column3 : function(list,divId){ // 此布局是一行3个
			if(list != null && list.length > 0){
				$('#' + divId).empty(); // 清空区域
				var $body = '';
				$.each(list,function(i,product){
					//var imgName = common.getFileName(product.picLDUrl);
					//var imgPath = common.getHostName() + '/product/img/' + imgName;
					var imgPath = common.getDfsHostName() + product.picLDUrl;
					if(i % 3 == 0){
						$body += '<div class="video-row">';
					}
					$body += '<div class="video-card video-col-3">';
					$body += '<a href="'+ common.getContextPath() +'/play/'+ product.teamId +'_'+ product.productId +'.html" target="_blank">';
					$body += '<img class="img-card-3" src="'+ imgPath +'" alt='+ product.productName + '_拍片网' +'/>';
					$body += '</a>';
					$body += '<div class="video-desc-section">';
					$body += '<h3>'+ product.productName +'</h3>';
					/*$body += '<h4>'+ product.teamName +'</h4>';*/
					$body += '<h4></h4>';
					$body += '<div class="video-desc" title="'+ product.pDescription +'">';
					$body += product.pDescription;
					$body += '</div>';
					$body += '</div>';
					$body += '<div class="video-price-section">';
					$body += '<div class="video-price">';
					if(product.serviceRealPrice < product.servicePrice){ // 如果折扣价 不小于 原价，则不显示原价
						$body += '<h2>￥'+ common.thousandCount(product.serviceRealPrice) +'</h2>&nbsp;&nbsp;';
						$body += '<h3>'+ common.thousandCount(product.servicePrice) +'</h3>';
					}else {
						$body += '<h2>￥'+ common.thousandCount(product.serviceRealPrice) +'</h2>&nbsp;&nbsp;';
					}
					
					$body += '</div>';
					$body += '<a href="'+ common.getContextPath() +'/play/'+ product.teamId +'_'+ product.productId +'.html" target="_blank">了解详情</a>';
					$body += '</div>';
					$body += '</div>';

					if(i % 3 == 2){
						$body += '</div>';
					}
				});

				$('#' + divId).append($body); // 填充数据
			}
		},
		column4 : function(list,divId){ // 此布局是是一行4个
			if(list != null && list.length > 0){
				var $body = '';
				$.each(list,function(i,product){
					var imgName = common.getFileName(product.picLDUrl);
					var imgPath = common.getHostName() + '/product/img/' + imgName;
					if(i % 4 == 0){
						$body += '<div class="video-row">';
					}
					$body += '<div class="video-card video-col-4">';
					$body += '<a href="javascript:void(0);">';
					$body += '<img class="img-card-4" src="'+ imgPath +'" />';
					$body += '</a>';
					$body += '<div class="video-desc-section">';
					$body += '<h3>'+ product.productName +'</h3>';
					/*$body += '<h4>'+ product.teamName +'</h4>';*/
					$body += '<h4></h4>';
					$body += '<div class="video-desc">';
					$body += product.pDescription;
					$body += '</div>';
					$body += '</div>';
					$body += '<div class="video-price-section">';
					$body += '<div class="video-price">';
					$body += '<h2>￥'+ product.serviceRealPrice +'</h2>&nbsp;&nbsp;';
					$body += '<h3>'+ product.servicePrice +'</h3>';
					$body += '<a href="javascript:void(0);">了解详情</a>';
					$body += '</div>';
					$body += '</div>';
					$body += '</div>';

					if(i % 4 == 3){
						$body += '</div>';
					}
				});

				$('#' + divId).append($body);
		}
	}
}

	return {
		VideoLayout : VideoLayout
	}
});