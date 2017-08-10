$().ready(function(){
	// 搜索智能提示
	suggest.smartSuggest();
	
	// 如果 搜索栏为空，则隐藏提示栏
	suggest.prototypeChange();
});

var suggest = {
	smartSuggest : function(){
			var oT=document.getElementById('search-q');
			var oUl=document.getElementById('shelper');
			var now=-1;
			var oldValue='';
			
			oT.onkeyup=function(ev){
				var oEvent=ev || event;
				if (oEvent.keyCode==40 || oEvent.keyCode==38)
				{
					return false;
				}
				
				oldValue=this.value;
				
				var token = $('#search-q').val().trim();
				if(token != null && token != '' && token != undefined){
					loadData(function(list){
						if(list != null && list != '' && list != undefined) {

							oUl.style.display='block';
							oUl.innerHTML='';
							var arr=list;
							
							for (var i=0; i<arr.length; i++)
							{
								var oLi=document.createElement('li');
								oLi.innerHTML=arr[i].condition;
								oUl.appendChild(oLi);
							}
							
							// 移入移出 点击跳转
							var aLi=oUl.children;
							for (var i=0; i<aLi.length; i++)
							{
								(function (index){
									aLi[i].onmouseover=function (){
										aLi[index].className='on';
									};
									
									aLi[i].onmouseout=function (){
										aLi[index].className='';
									};
									
									aLi[i].onclick=function (){
										$('#search-q').val(this.innerHTML);
										$('#s-btn').click();
										oUl.style.display='none';
									};
									
								})(i);
							}
						
						}
					}, getContextPath() + '/suggest/' + token, null);
				}
			};
			
			oT.onkeydown=function (ev){
				var aLi=oUl.children;
				
				var oEvent=ev || event;
				
				if (oEvent.keyCode == 40)
				{
					now++;
					if (now == aLi.length)
					{
						now=-1;
					}
					
					for (var i=0; i<aLi.length; i++)
					{
						aLi[i].className='';
					}
					
					if (now == -1)
					{
						oT.value=oldValue;
					}
					else
					{
						aLi[now].className='on';
						oT.value=aLi[now].innerHTML;
					}
					
					document.title=now;
				}
				
			}
	},
	prototypeChange : function(){
		$('#search-q').bind('input propertychange',function(){
			if($(this).val().trim() == '' || $(this).val().trim() == null || $(this).val().trim() == undefined){
				$('#shelper').css('display','none');
			}
		});
	}
}