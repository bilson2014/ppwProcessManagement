define(['mob-share'],function(mobShare){
	var M = mobShare;
	function Share(){
		this.name = 'Share';
	}

	Share.prototype = {
		init : function(url,title,img){
			M.config({
				 
				debug: false, // 开启调试，将在浏览器的控制台输出调试信息
			
				appkey: '8c49c537a706', // appkey
			
				params: {
					url: url, // 分享链接
					title: title, // 分享标题
					description: '拍片网-中国领先的视频营销服务平台', // 分享内容
					pic: img, // 分享图片，使用逗号,隔开
				},
			 
				callback: function( plat, params ) {
					
				}
			});
		}
	}
	
	return {
		Share : Share
	}
});