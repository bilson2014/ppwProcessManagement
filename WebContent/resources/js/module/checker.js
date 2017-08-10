/*！
	*数据验证相关
*/
define(['jquery'],function($){
	function Checker(){
		this.name = 'Checker'
	}

	Checker.prototype= {
		isNull : function(str){ // 是否为空
			if($.trim(str)){
				return false;
			}else {
				return true;
			}
		},
		isMobile : function(telephoneNumber){ // 是否是手机号码
			var reg = /^(0|86|17951)?(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$/;
			if(telephoneNumber.match(reg)){
				return true;
			} else{
				return false;
			}
		},
		isEmail : function(email){ // 是否是邮箱
			reg = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
			if(email.match(reg))
				return true; 
			else
				return false;
		},
		isNumber : function(number){ // 是否是数字
			reg = /^[1-9]+[0-9]*]*$/;
			if(number.match(reg))
				return true;
			else
				return false;
		},
		isUserName : function(userName){ // 是否符合用户名规则
			var reg = /^[a-zA-Z0-9_]{6,16}$/ ;
			if(userName.match(reg))
				return true; 
			else
				return false;
		}
	}


	return {
		Checker : Checker
	}
});