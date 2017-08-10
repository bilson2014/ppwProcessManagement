define(function(){
	function Bdmta(){
		this.name = 'Bdmta';
		this.key = 'b0ac6d7e1cee0e96c5c43106c5d43537';
	}

	Bdmta.prototype = {
		init : function(){
			var _hmt = _hmt || [];
			var hm = document.createElement('script');
			hm.src = '//hm.baidu.com/hm.js?' + this.key;
			var s = document.getElementsByTagName('script')[0];
			s.parentNode.insertBefore(hm, s);
		}
	}

	return {
		Bdmta : Bdmta
	}
});