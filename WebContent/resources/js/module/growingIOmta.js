define(function() {
	function GrowingIOmta(){
		this.name = 'GrowingIOmta';
		this.key = '9f2e33a3d43b5d78';
	}
	
	GrowingIOmta.prototype = {
			
		init : function(){
			var _vds = _vds || [];
			window._vds = _vds;
			_vds.push(['setAccountId', this.key]);
			  (function() {
			    var vds = document.createElement('script');
			    vds.type='text/javascript';
			    vds.async = true;
			    vds.src = ('https:' == document.location.protocol ? 'https://' : 'http://') + 'dn-growing.qbox.me/vds.js';
			    var s = document.getElementsByTagName('script')[0];
			    s.parentNode.insertBefore(vds, s);
			  })();
		}
	}
});