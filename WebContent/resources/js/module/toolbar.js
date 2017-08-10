require.config({
	baseUrl : 'resources/',
	paths : {
		checker : 'js/module/checker',
		model : 'js/module/model',
		common : 'js/module/common'
	}
});

define(['model','checker','common'],function(modal,checker,c){
	function Toolbar(){
		this.name = 'Toolbar';
	}
	
	Toolbar.prototype = {
		registerClick : function(){
			// 弹出电话预约界面
			$('.common-icons-tele-client').click(function(){
				$('#toolbar-modal').modal({
					keyboard: false
				})
				var call_able = true;
				$('#modal-call').off("click").on('click',function(){
					if(call_able){
						call_able = false;
						// 检测手机号码
						var phoneNumber = $('#phoneCall').val();
						if(phoneNumber != null && phoneNumber != '' && phoneNumber != undefined){
							var check = new checker.Checker();
							var common = new c.Common();
							if(check.isMobile(phoneNumber)){
								common.loadData(function(result){
									if(result){
										// 消息发送成功
										call_able = true;
										$('#modal-dd-second').empty();
										$('#modal-h3-first').text('视频营销专家正火速与您联系!');
									}else{
										// 消息发送失败
										$('.modal-message').empty();
										$('.modal-message').text('亲!拍拍出了点小问题，正在努力自我修复，请刷新后再试哦!');
										$('.modal-message').show('normal');
									}
								}, common.getContextPath() + '/appointment/' + phoneNumber, null);
							}else {
								$('.modal-message').empty();
								$('.modal-message').text('请输入正确的电话号码哦亲!');
								$('.modal-message').show('normal');
							}
						}else {
							$('.modal-message').empty();
							$('.modal-message').text('别忘记输入电话哦亲!');
							$('.modal-message').show('normal');
						}
					}
				});
			});
		}
	}
	
	return {
		Toolbar : Toolbar
	}
});