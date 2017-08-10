var pageSize = 20;
var currentSize = 0;
// 功能维度
var typeArray = new Array('产品宣传片','企业宣传片','产品广告','微电影','路演视频','病毒视频','品牌宣传片','活动宣传片','城市宣传片','形象宣传片','个人宣传片','创意广告','活动广告','品牌广告','TVC','发布会','访谈','公益片','暖场片','纪录片','公益广告','系列广告');
// 行业维度
var busArray = new Array('智能硬件','教育','医疗保健','互联网','电商','旅游','母婴','体育','金融','电子产品','大数据','游戏','服装纺织','家居建材','餐饮美食','食品','饮料','农业','房地产','创投','首饰','日用美妆','云计算');
// 制作维度
var proArray = new Array('MG动画','Flash动画','明星演员','包装','三维','航拍','动画','电影级','广告级','长镜头','定格动画');
var industry = 'industry='; // 行业
var genre = 'genre='; // 类型
var production = ' production='; // 制作

$().ready(function(){
	search.showTitle(); // 在标题栏显示搜索内容
	search.initCrumbs(); // 初始化面包屑布局
	search.loadItem(); // 装配 功能、行业、制作维度
	search.itemToggle(); // 注册 行业分类 点击事件
	search.loadPrice(); // 装配价格
	search.pagination(); // 分页
});

var search = {
	showTitle : function() {
		var q = $('#q').val();
		if(q != '' && q != undefined) {
			if(q.indexOf(industry) > -1 || q.indexOf(genre) > -1 || q.indexOf(production) > -1) {
				// 如果含有tags字样，那么证明只对tags字段进行搜索
				q.replace(industry,'');
				q.replace(genre,'');
				q.replace(production,'');
			}
		}
		$('#search-q').val(q == '*' ? '' : q);
	},
	initCrumbs : function() {
		// 初始化面包屑布局 
		var q = $('#q').val();
		var industry = $('#industry').val(); // 行业
		var genre = $('#genre').val(); // 类型
		var production = $('#production').val(); // 制作
		
		q = q.replace(/"/g,'&quot').replace(/“/g,'&quot').replace(/”/g,'&quot');	
		var $tagBody ='';
		if(q != undefined  && q.trim() != '' && q.trim() != '*') {
			// 将搜索内容写入面包屑布局
			// 去除空格以及','
			var re2='(\\s*)(,)(\\s*)';	
		    var p = new RegExp(re2,["gm"]);
		    q = q.replace(p, ' ');
		    var qArray = q.split(/\s+/);
			if(qArray != '') {
				$.each(qArray,function(i,tag){
					if(tag.trim() != '' && tag.trim() != undefined) {
						$tagBody += '<div class="tag" id="tagType">';
						$tagBody += '<div class="controlCard">';
						$tagBody += '<span>'+ tag.trim() +'</span><span class="tagX" id="tagContentX" data-type="condition" data-text='+ tag.trim() +'>x</span></div>';
						$tagBody += '</div>';
					}
				});
			} else {
				// 全部
				$tagBody += '<div class="tag" id="tagType">';
				$tagBody += '<div class="controlCard">';
				$tagBody += '<span>全部</span><span class="tagX" id="tagContentX" data-text="全部">x</span></div>';
				$tagBody += '</div>';
			}
			
		}else {
			// q为空，则为全部
			$tagBody += '<div class="tag" id="tagType">';
			$tagBody += '<div class="controlCard">';
			$tagBody += '<span>全部</span><span class="tagX" id="tagContentX" data-text="全部">x</span></div>';
			$tagBody += '</div>';
		}
		
		// 行业
		if(industry != undefined  && industry != null && industry.trim() != '') {
			// 将搜索内容写入面包屑布局
			// 去除空格以及','
			var re2='(\\s*)(,)(\\s*)';	
		    var p = new RegExp(re2,["gm"]);
		    industry = industry.replace(p, ' ');
		    var qArray = industry.split(/\s+/);
			if(qArray != '') {
				$.each(qArray,function(i,tag){
					if(tag.trim() != '' && tag.trim() != undefined) {
						$tagBody += '<div class="tag" id="tagType">';
						$tagBody += '<div class="controlCard">';
						$tagBody += '<span>'+ tag.trim() +'</span><span class="tagX" id="tagContentX" data-type="indy" data-text='+ tag.trim() +'>x</span></div>';
						$tagBody += '</div>';
					}
				});
			}
		}
		
		// 类型
		if(genre != undefined  && genre != null && genre.trim() != '') {
			// 将搜索内容写入面包屑布局
			// 去除空格以及','
			var re2='(\\s*)(,)(\\s*)';	
		    var p = new RegExp(re2,["gm"]);
		    genre = genre.replace(p, ' ');
		    var qArray = genre.split(/\s+/);
			if(qArray != '') {
				$.each(qArray,function(i,tag){
					if(tag.trim() != '' && tag.trim() != undefined) {
						$tagBody += '<div class="tag" id="tagType">';
						$tagBody += '<div class="controlCard">';
						$tagBody += '<span>'+ tag.trim() +'</span><span class="tagX" id="tagContentX" data-type="gen" data-text='+ tag.trim() +'>x</span></div>';
						$tagBody += '</div>';
					}
				});
			}
		}
		
		// 制作
		if(production != undefined  && production != null && production.trim() != '') {
			// 将搜索内容写入面包屑布局
			// 去除空格以及','
			var re2='(\\s*)(,)(\\s*)';	
		    var p = new RegExp(re2,["gm"]);
		    production = production.replace(p, ' ');
		    var qArray = production.split(/\s+/);
			if(qArray != '') {
				$.each(qArray,function(i,tag){
					if(tag.trim() != '' && tag.trim() != undefined) {
						$tagBody += '<div class="tag" id="tagType">';
						$tagBody += '<div class="controlCard">';
						$tagBody += '<span>'+ tag.trim() +'</span><span class="tagX" id="tagContentX" data-type="pro" data-text='+ tag.trim() +'>x</span></div>';
						$tagBody += '</div>';
					}
				});
			}
		}
		
		// 价格
		var price = $('#price').val();
		if(price != undefined  && price != '') {
			price = parsePrice(price);
			if(price != null) {
				$tagBody += '<div class="tag" id="tagType">';
				$tagBody +='<div class="controlCard">';
				$tagBody +='<span>'+ price +'</span><span class="tagX" id="tagContentX" data-type="price">x</span></div>';
				$tagBody += '</div>';
			}
			
		}
		
		// 写入页面
		$("#videoTag").append($tagBody);
		
		// 注册面包屑布局的点击事件
		$('.tagX').unbind('click');
		$('.tagX').bind('click',crumbsClick);
	},
	loadItem : function(){
			// 清除数据
			$('#item-list').empty();
			$('#bus-item-list').empty();
			$('#production-item-list').empty();
			$ul = '';
			$ulBus = '';
			$ulPro = '';
			var q = $('#q').val();
			var industry = $('#industry').val(); // 行业
			var genre = $('#genre').val(); // 类型
			var production = $('#production').val();
			
			var price = $('#price').val();
			
			q = q.replace(/"/g,'&quot').replace(/“/g,'&quot').replace(/”/g,'&quot');
			var param = '?q=' + q;
			if(price != null && price != undefined && price != ''){
				param += '&price=' + price;
			}
			
			var tParam = '&industry=@_@';
			
			var gParam = '&genre=@_@';
			
			var pParam = '&production=@_@';
			
			if(industry != null && industry != undefined && industry != '') {
				gParam += '&industry=' + industry;
				pParam += '&industry=' + industry;
			}
			
			if(genre != null && genre != undefined && genre != '') {
				tParam += '&genre=' + genre;
				pParam += '&genre=' + genre;
			}
			
			if(production != null && production != undefined && production != '') {
				gParam += '&production=' + production;
				tParam += '&production=' + production;
			}
			
			$.each(typeArray,function(i,item){
				// 去除重复
				if(industry != undefined && industry != '' && industry != null) {
					if(industry.indexOf(item) > -1)
						tParam = tParam.replace('@_@',industry);
					else
						tParam = tParam.replace('@_@',industry + ' ' + item);
				}else {
					tParam = tParam.replace('@_@',item + ' ');
				}
				$ul += '<li>';
				$ul += '<a href="'+ getContextPath() + '/search' + param + tParam.trim() + '" class="itemAll" data-id="'+ item +'">'+ item +'</a>';
				$ul += '</li>';
				
				tParam = '&industry=@_@';
				if(genre != null && genre != undefined && genre != '')
					tParam += '&genre=' + genre;
				if(production != null && production != undefined && production != '')
					tParam += '&production=' + production;
				
			});
			
			$.each(busArray,function(i,item){
				// 去除重复
				if(genre != undefined && genre != '' && genre != null) {
					if(genre.indexOf(item) > -1) 
						gParam = gParam.replace('@_@',genre);
					else 
						gParam = gParam.replace('@_@',genre + ' ' + item);
				}else {
					gParam = gParam.replace('@_@',item + ' ');
				}
				$ulBus += '<li>';
				$ulBus += '<a href="'+ getContextPath() + '/search' + param + gParam.trim() + '" class="BusItemAll" data-id="'+ item +'">'+ item +'</a>';
				$ulBus += '</li>';
				
				gParam = '&genre=@_@';
				if(industry != null && industry != undefined && industry != '')
					gParam += '&industry=' + industry;
				if(production != null && production != undefined && production != '')
					gParam += '&production=' + production;
			});
			
			$.each(proArray,function(i,item){
				// 去除重复
				if(production != undefined && production != '' && production != null) {
					if(production.indexOf(item) > -1) 
						pParam = pParam.replace('@_@',production);
					else 
						pParam = pParam.replace('@_@',production + ' ' + item);
				}else {
					pParam = pParam.replace('@_@',item + ' ');
				}
				$ulPro += '<li>';
				$ulPro += '<a href="'+ getContextPath() + '/search' + param + pParam.trim() + '" class="ProItemAll" data-id="'+ item +'">'+ item +'</a>';
				$ulPro += '</li>';
				
				pParam = '&production=@_@';
				if(industry != null && industry != undefined && industry != '')
					pParam += '&industry=' + industry;
				if(genre != null && genre != undefined && genre != '')
					pParam += '&genre=' + genre;
				
			});
			
			$('#item-list').append($ul);
			$('#bus-item-list').append($ulBus);
			$('#production-item-list').append($ulPro);
	},
	loadPrice : function(){ // 装配价格
		$('#price-list').empty();
		var q = $('#q').val();
		var production = $('#production').val();
		var industry = $('#industry').val(); // 行业
		var genre = $('#genre').val(); // 类型
		
		q = q.replace(/"/g,'&quot').replace(/“/g,'&quot').replace(/”/g,'&quot');
		var param = '?q=' + q;
		
		if(industry != null && industry != undefined && industry != '') {
			param += '&industry=' + industry.trim();
		}
		
		if(genre != null && genre != undefined && genre != '') {
			param += '&genre=' + genre.trim();
		}
		
		if(production != null && production != undefined && production != '') {
			param += '&production=' + production.trim();
		}
		
		var $priceLi = '<li><a href="'+ getContextPath() + '/search' + param + '&price=' + '[1 TO *]' +'" data-price="[1 TO *]" class="priceAll">有价格</a></li>';
		$priceLi += '<li><a href="'+ getContextPath() + '/search' + param + '&price=' + '[1 TO 30000]' +'" data-price="[1 TO 30000]" class="priceAll">0~3万</a></li>';
		$priceLi += '<li><a href="'+ getContextPath() + '/search' + param + '&price=' + '[30001 TO 60000]' +'" data-price="[30001 TO 60000]" class="priceAll">3~6万</a></li>';
		$priceLi += '<li><a href="'+ getContextPath() + '/search' + param + '&price=' + '[60001 TO 100000]' +'" data-price="[60001 TO 100000]" class="priceAll">6~10万</a></li>';
		$priceLi += '<li><a href="'+ getContextPath() + '/search' + param + '&price=' + '[100001 TO *]' +'" data-price="[100001 TO *]" class="priceAll">10万以上</a></li>';
		$priceLi += '<li><div class="price-section"><input type="text" id="start-price" /> 万 ~ <input type="text" id="end-price" /> 万 <a id="price-alink" href="javascript:void(0);" ><button id="price-btn" class="btn btn-primary" type="button" >确定</button></a></div></li>';
		$('#price-list').append($priceLi);
		
		// 绑定input 值变动事件
		$('#start-price').change(function(){
			// 判断输入格式
			if(!checkNumber($(this).val().trim())){
				// 如果不是数字,则清空
				$(this).val('');
			}
		});
		
		$('#end-price').change(function(){
			// 判断输入格式
			if(!checkNumber($(this).val().trim())){
				// 如果不是数字，则清空
				$(this).val('');
			}
		});
		
		// 注册 价格 确定按钮
		$('#price-btn').on('click',function(){
			var priceBegin = $('#start-price').val().trim();
			var priceEnd = $('#end-price').val().trim();
			if((priceBegin != null && priceBegin != '' && priceBegin != undefined) || (priceEnd != null && priceEnd != '' && priceEnd != undefined)){
				// 拼接搜索条件
				var condition = '&price=[';
				if(priceBegin != null && priceBegin != '' && priceBegin != undefined){
					condition += priceBegin * 10000 + 1 + ' TO ';
				}else {
					condition += '* TO ';
				}
				
				if(priceEnd != null && priceEnd != '' && priceEnd != undefined){
					condition += priceEnd * 10000 + ']';
				}else {
					condition += '*]';
				}
				$('#price-alink').attr('href',getContextPath() + '/search' + param + condition).click();
			}else {
				return false;
			}
		});
		
	},
	pagination : function(){
		$(".pagination").createPage({
			pageCount: Math.ceil($('#total').val() / pageSize),
			current: 1,
			backFn:function(p){
				// 点击 翻页，查询符合条件的视频
				loadProduction((p - 1) * pageSize);
				currentSize = (p - 1) * pageSize;
			}
		});
	},
	itemToggle : function(){ // 注册 行业分类 点击事件
		$('#more-link').on('click',function(){
			if($('#item-list').hasClass('extend')){
				// 收起
				$('#item-list').removeClass('extend');
			}else {
				// 放下
				$('#item-list').addClass('extend');
			}
		});
	}
}

// 面包布局项目点击事件
function crumbsClick() {
	// 类型
	var strType = $(this).data('type');
	// 文案
	var strText = $(this).data('text');
	// 搜索维度信息
	var q = $('#q').val();
	q = q.replace(/"/g,'&quot').replace(/“/g,'&quot').replace(/”/g,'&quot');
	
	var price = $('#price').val();
	
	var industry = $('#industry').val(); // 行业
	var genre = $('#genre').val(); // 类型
	var production = $('#production').val(); // 制作
	
	var param = '';
	if(strType != undefined && strType != '') {
		// 类型不为空时，排除了 “全部”的可能
		if(strType == 'condition'){
			// 点击标签时，移除这个标签
			if(strText != undefined && strText != '' && strText != null) {
				q = q.replace(strText,'');
			}
			param = recombineSearchCondition(q, industry, genre, production,price);
		} else if (strType == 'indy') {
			// 取消行业标签时
			// 点击标签时，移除这个标签
			if(strText != undefined && strText != '' && strText != null) {
				industry = industry.replace(strText,'');
			}
			param = recombineSearchCondition(q, industry, genre, production,price);
		} else if(strType == 'gen') {
			// 取消类型标签时
			// 点击标签时，移除这个标签
			if(strText != undefined && strText != '' && strText != null) {
				genre = genre.replace(strText,'');
			}
			param = recombineSearchCondition(q, industry, genre, production, price);
		} else if(strType == 'pro') {
			// 取消类型标签时
			// 点击标签时，移除这个标签
			if(strText != undefined && strText != '' && strText != null) {
				production = production.replace(strText,'');
			}
			param = recombineSearchCondition(q, industry, genre, production, price);
		} else if(strType == 'price') {
			// 取消价格时
			param = recombineSearchCondition(q, industry, genre, production, '');
		}
	}else {
		// 类型为空时，则证明是“全部”，不做任何处理
		return ;
	}
	
	location.href = getContextPath() + '/search' + param;
}

// 重组url
function recombineSearchCondition(q,industry,genre,production,price) {
	var param = '';
	if(q != null && q != undefined && q != '') {
		param = '?q=' + q;
	}
	if(industry != null && industry != undefined && industry != '') {
		var indy = industry.split(' ');
		var temp = '';
		for(var i = 0;i < indy.length;i ++) {
			if(indy[i].trim() != '') {
				temp += indy[i].trim() + ' ';
			}
		}
		if(temp != '' && temp != null)
			param += '&industry=' + temp.trim();
	}
	
	if(genre != null && genre != undefined && genre != '') {
		var gen = genre.split(' ');
		var temp = '';
		for(var i = 0;i < gen.length;i ++) {
			if(gen[i].trim() != '') {
				temp += gen[i].trim() + ' ';
			}
		}
		if(temp != '' && temp != null)
			param += '&genre=' + temp.trim();
	}
	
	if(production != null && production != undefined && production != '') {
		var production = production.split(' ');
		var temp = '';
		for(var i = 0;i < production.length;i ++) {
			if(production[i].trim() != '') {
				temp += production[i].trim() + ' ';
			}
		}
		if(temp != '' && temp != null)
			param += '&production=' + temp.trim();
	}
	
	if(price != null && price != undefined && price != '') {
		param += '&price=' + price;
	}
	
	// 判断param中是否含有q
	if(param != '' && param != null) {
		if(param.indexOf('?q=') < 0) {
			// param不包含q,那么把&替换成?
			param = '?q=' + param;
		}
	}
	return param;
}

// 价格解析
function parsePrice(price) {
	if(price != undefined && price != '') {
		// 价钱不为空时
		if(price.indexOf('TO') > -1) {
			// 分解
			var pArray = price.split('TO');
			var startPrice = pArray[0].split('[')[1].trim();
			var endPrice = pArray[1].split(']')[0].trim();
			if(startPrice == '*') {
				// 开始价格如果为*，则认为只有结尾价格有效
				if(endPrice == '*') {
					// 开始、结束价格都为*则认为是查询全部价格
					return null;
				}
				
				return (Number(endPrice) / 10000) + '万以内';
			}
			
			if(endPrice == '*'){
				// 结尾价格如果为*，则认为只有开始价格有效
				if(startPrice == '*') {
					// 开始、结束价格都为*,则认为是查询全部价格
					return null;
				} else if(startPrice == '1'){
					return '有价格';
				}
				
				return (Number(startPrice - 1) / 10000) + '万以上';
			}
			
			return (Number(startPrice - 1) / 10000) + '~' + (Number(endPrice) / 10000) + '万';
		}
	}
	return null;
}

// 解析时长
function parseLength(length) {
	if(length != undefined && length != '') {
		// 时长不为空时
		if(length.indexOf('TO') > -1) {
			// 分解
			var lArray = length.split('TO');
			var startLength = lArray[0].split('[')[1].trim();
			var endLength = lArray[1].split(']')[0].trim();
			if(startLength == '*') {
				// 开始价格如果为*，则认为只有结尾价格有效
				if(endLength == '*') {
					// 开始、结束价格都为*则认为是查询全部价格
					return null;
				}
				
				return (parseInt(endLength)) + '秒以内';
			}
			
			if(endLength == '*'){
				// 结尾价格如果为*，则认为只有开始价格有效
				if(startLength == '*') {
					// 开始、结束价格都为*则认为是查询全部价格
					return null;
				}
				
				return (parseInt(startLength)) + '秒以上';
			}
			return Number(startLength) + '~' + Number(endLength) + '秒';
		}
	}
	return null;
}

// 加载视频
function loadProduction(start){
	// 清空 videowrap
	$('#video-content').empty();
	loadData(function(list){
		
		$body = '';
		if(list != null && list.length > 0){
			 // 此布局是一行4个
			$('#video-content').empty(); // 清空区域
			if(list != null && list.length > 0){
				var $body = '';
				$.each(list,function(i,solr){
					var num = parseInt(solr.indentProjectId);
					var imgPath = '/resources/images/index/noImg.jpg';
					var imageUrl = solr.picLDUrl;
					var itemflag = parseInt(solr.teamFlag);
					if(imageUrl != undefined && imageUrl != null && imageUrl != ""){
						imgPath = getDfsHostName() + imageUrl;
					}
					if(i % 4 == 0){
						$body += '<div class="video-row">';
					}
					$body += '<div class="video-card video-col-4">';
					var targetHref = getContextPath() + '/play/' + solr.teamId + '_' + solr.productId + '.html';
					$body += '<a href="'+ targetHref +'" target="_blank">';
					$body += '<img class="img-card-4" src="'+ imgPath +'" alt="拍片网" />';
					$body += '<div class="video-desc-section">';
					$body += '<h3>'+ solr.productName +'</h3>';
					$body += '<div class="video-tags">';
					
					var tags = solr.tags;
					
					if(tags != '' && tags != null) {
						var re2='(\\s*)(,|，)(\\s*)';	
					    var p = new RegExp(re2,["gm"]);
					    tags = tags.replace(p, ' ');
					    var tagsArray = tags.split(/\s+/);
						$.each(tagsArray,function(i,tag) {
							if(i <= 2)
								$body += tag;
							if (i < 2 && i != tagsArray.length - 1)
								$body += ' / ';
						})
					}
					
					$body += '</div>';
					$body += '</div>';
					$body += '<div class="video-price-section">';
					$body += '<div class="video-price">';
					$body += '<h2>￥'+ ((solr.price == 0) ? '欢迎询价' : thousandCount(solr.price)) +'</h2>&nbsp;&nbsp;';
					if(solr.price < solr.orignalPrice){
						$body += '<h3>原价&nbsp;￥&nbsp;'+ thousandCount(solr.orignalPrice) +'</h3>';
					}
					$body += '</div>';
					$body += '</div>';
					$body += '</a>';
					$body += '<div class="line">';
					$body += '  <div class="videoCardLine"></div>';
					$body += '</div>';
					var loginTel = $('#rolephoneImg').val();
					if(loginTel!=null && loginTel!= "" ){
						if(num != 0 && itemflag == 1){
							$body +='<img class="roleImg" src="/resources/images/play/roleOur.png">';
						}
						if(itemflag == 4){
							$body +='<img class="roleImg" src="/resources/images/play/rolePlay.png">';
						}
						if(num == 0 && itemflag == 1){
							$body +='<img class="roleImg" src="/resources/images/play/rolePro.png">';
						}
					}
					
					$body +='<div class="videoProvider">';
					
					
					if(itemflag != 4 ) {
						$body +='<a href="'+getHostName()+'/provider/info_'+solr.teamId+'.html">';
						if(solr.teamPhotoUrl!=null && solr.teamPhotoUrl!=''){
							$body +=' <img src="'+getDfsHostName()+''+solr.teamPhotoUrl+'">';
						}else{
							$body +=' <img src="/resources/images/user/default.png">';
						}
						$body +=' <div>'+solr.teamName+'</div>';
						$body +='</a>';
					}
					$body +='</div>';
				
					$body +='</div>';
					if(i % 4 == 3){
						$body += '</div>';
					}
				});

				$('#video-content').append($body); // 填充数据
			}else {
				// 如果没有数据，则显示 "对不起，没有查询到您想要的数据"
				$('#video-content').append('<div class="prompt-word">您找的影片遗落在外星球了！快 登录 拍片网飞船吧！</div>');
			}
		}
	}, getContextPath() + '/search/pagination', $.toJSON({
		begin : start,
		limit : pageSize,
		condition : $('#q').val().trim(),
		industry : $('#industry').val().trim(),
		genre : $('#genre').val().trim(),
		production : $('#production').val().trim(),
		priceFq : $('#price').val(),
		lengthFq : $('#length').val()
	}));
}


var searchVideo= {
		addType : function(word){
			if(word!="全部"){
			var formBody ='<div class="tag" id="tagType">';
			formBody +='<div class="controlCard">';
			formBody +='<span>'+word+'</span><span class="tagX">x</span></div>';
			$("#videoTag").append(formBody);
			this.typeClick();
			}
			if(word=="女性"||word=="体育健身"||word=="o2o"){
				$('#more-link').click();
			}
		},
		addPrice: function(word){
			if(word!="全部"){
			var formBody ='<div class="tag" id="tagPrice">';
			formBody +='<div class="controlCard">';
			formBody +='<span >'+word+'</span><span class="tagX">x</span></div>';
			$("#videoTag").append(formBody);
			this.priceClick();
			}
		},
		addTime: function(word){
			if(word!="全部"){
			var formBody ='<div class="tag" id="tagTime">';
			formBody +='<div class="controlCard"  >';
			formBody +='<span >'+word+'</span><span class="tagX">x</span></div>';
			$("#videoTag").append(formBody);
			this.timeClick();
			}
		},
		typeClick:function(){
			$('#tagType').on('click',function(){
				var url = $('#item-all').attr('href');
				location.href=url;
				
			});
		},
		priceClick:function(){
			$('#tagPrice').on('click',function(){
				var url = $('#price-all').attr('href');
				location.href=url;
				
			});
		},
		timeClick:function(){
			$('#tagTime').on('click',function(){
				var url = $('#length-all').attr('href');
				location.href=url;
				
			});
		}
}

