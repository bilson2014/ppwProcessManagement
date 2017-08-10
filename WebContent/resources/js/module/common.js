define(['jquery'],function($){
	function Common(){
		this.name = 'Common';
	}

	Common.prototype = {
		debug : function(obj){ // debug 查看
			var linebreak = "\r\n";

			var msg = "OBJECT->" + linebreak;
			msg += obj + linebreak;
			for ( var e in obj) {
			msg += e + "=" + obj[e] + linebreak;
			}

			return alert(msg);
		},
		getContextPath : function(){ // 获取项目根路径
			var path = document.location.pathname.substr(1);
			path = "/" + path.substr(0, path.indexOf("/"));
			return '';
		},
		getData : function(Func,url){ // Ajax By Get
			$.ajax({
				url : url,
				type : 'GET',
				dataType : 'json',
				success : function(data){
					Func(data);
				},
				error : function(jqXHR, textStatus, errorThrown) {
					console.error('ajax(' + url + ')[' + jqXHR.status + ']' + jqXHR.statusText);
					console.error(jqXHR.responseText);
					console.error('[' + textStatus + ']' + errorThrown);
				}
			});
		},
		loadData : function(Func,url,param){ // Ajax By Post
			$.ajax({
				url : url,
				type : 'POST',
				data : param,
				dataType : 'json',
				contentType : 'application/json; charset=UTF-8',
				success : function(data){
					Func(data);
				},
				error : function(jqXHR, textStatus, errorThrown) {
					console.error('ajax(' + url + ')[' + jqXHR.status + ']' + jqXHR.statusText);
					console.error(jqXHR.responseText);
					console.error('[' + textStatus + ']' + errorThrown);
				}
			});
		},
		thousandCount : function(number){ // 数据加分隔符
			var tableData;
			if (number == 0) {
				tableData = 0;
			} else {
				tableData = number.toLocaleString();
				var indexOf = tableData.indexOf(".");
				if (indexOf > -1) {
					tableData = tableData.substring(0, indexOf);
				}
			}
			return tableData;
		},
		DisThousandCount : function(number) { // 去掉千分位显示
			var tableData;
			if (number == 0) {
				tableData = 0;
			} else {
				tableData = number.toLocaleString();
				var indexOf = tableData.indexOf(".");
				if (indexOf > -1) {
					tableData = tableData.substring(0, tableData.indexOf("."));
				}
				tableData = tableData.replace(',','');
			}
			return tableData;
		},
		htmlSpecialCharsEntityEncode : function(str){
			var htmlSpecialCharsRegEx = /[<>&\r\n"']/gm;

			var htmlSpecialCharsPlaceHolders = {
				'<' : 'lt;',
				'>' : 'gt;',
				'&' : 'amp;',
				'\r' : "#13;",
				'\n' : "#10;",
				'"' : 'quot;',
				"'" : 'apos;' /*single quotes just to be safe*/
			};

			return str.replace(htmlSpecialCharsRegEx, function(match) {
				return '&' + htmlSpecialCharsPlaceHolders[match];
			});
		},
		getFileName : function(str){
			var arr = str.split('/');
			var imgName = '';
			for(var i = 0;i < arr.length;i ++){
				if(i == arr.length - 1 ){
					imgName = arr[i]
				}
			}
			
			return imgName;
		},
		getHostName : function(){ // 获取 主机名
			return window.location.protocol + '//' + window.location.host;
		},
		getDfsHostName:function(){
			var rPath = $('#storage_node').val();
			return rPath == undefined ? "http://resource.apaipian.com/resource/" : rPath;
		}

	}

	return {
		Common : Common
	}

});