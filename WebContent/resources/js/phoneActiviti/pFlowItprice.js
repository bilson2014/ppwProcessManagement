$().ready(function() {
    // 设置标题的信息
    $('.frameHead .name').text('价格信息修改');
    openPriceInfo();
});
//验证判断 
function sumbitPrice() {
    var price = $('.porecast input').val();
    $('.porecast p').text('');
    var num = /^[0-9]*$/;
    
    if($('#porecast').hasClass('porecast')){
    	if(price == undefined || price == "" || price == null){
    		$('.porecast p').text('*预估价格不能为空');
 		   return false;
 	   }
    	 if (!num.test(price)) {
    	        $('.porecast p').text('*请输入正确的价格');
    	        return false;
    	    }
    }
     return true;
}
//价格信息修改
function openPriceInfo() {
    // 数据获取
    loadData(function(res) {
        // 表单添加id
        $('#priceId').val(res.projectFlow.pf_projectId);
        // 添加数据
        $('#est').val(res.projectFlow.pf_estimatedPrice);
    }, getContextPath() + '/project/task/edit/parameter/' + $("#currentTaskId").val() + "/" + $('#projectId').val() + "/pf", null);
    // 确定事件
    $('#sumbitPrice').off('click').on('click', function() {
        if (sumbitPrice()) {
            $('#toPriceForm').submit();
        }
    });
    // 取消事件
    $('.cancel div').off('click').on('click', function() {
        window.location.href = '/project/phone/flowinfo/' + $("#currentTaskId").val() + "/" + $('#projectId').val() + "/" + $('#processInstanceId').val();
    });
    // 返回的跳转
    $('.frameHead a').attr('href', '/project/phone/flowinfo/' + $("#currentTaskId").val() + "/" + $('#projectId').val() + "/" + $('#processInstanceId').val());
}

function removItemProject(res){
	var projectName = res.projectFlow.pf_estimatedPrice;

	if(projectName == undefined || projectName == "" || projectName == null){
		$('.porecast').remove();
	}
}