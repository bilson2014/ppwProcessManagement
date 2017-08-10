define(['CryptoJS();'],function(c){

	function Encrypt(){
		this.name = 'Encrypt';
		this.config = {
			cryptKey : '0102030405060708'
		}
		var CryptoJS = new c.CryptoJS();
	}

	Encrypt.prototype = {
		encrypt : function(str){ // 加密
			var cryptKey = this.config.cryptKey;
			var key = CryptoJS.enc.Utf8.parse(cryptKey);
			var iv  = CryptoJS.enc.Utf8.parse(cryptKey);
			var srcs = CryptoJS.enc.Utf8.parse(word);
			var encrypted = CryptoJS.AES.encrypt(srcs, key, { iv: iv,mode:CryptoJS.mode.CBC});  
			return encrypted.toString();
		}
	}

	return {
		Encrypt : Encrypt
	}
});