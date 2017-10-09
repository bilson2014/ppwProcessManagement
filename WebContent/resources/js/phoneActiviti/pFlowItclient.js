$().ready(function() {
    // 设置标题的信息
    $('.frameHead .name').text('客户信息修改');
    openCusInfo();
});
// sure 事件
function surebtn() {
    var name = $('.name input').val();
    var phone = $('.phone input').val();
    var cusEmail = $('#cusEmail').val();
    $('.name p').text('');
    $('.phone p').text('');
    if(cusEmail == undefined || cusEmail == "" || cusEmail ==null ){
		$('#emailError').text('邮箱地址未填写');
		return false;
	}
     if(!checkEmail(cusEmail)){
	 $('#emailError').attr('邮箱格式不正确');
		return false;
    }
    var num = /^1\d{10}$/;
    if (name.length == 0) {
        $('.name p').text('*客户联系人不能为空');

    } else if (phone.length == 0) {
        $('.phone p').text('*客户电话不能为空');

    } else if (!num.test(phone)) {
        $('.phone p').text('*请输入正确的手机号');
    } else {
        $('.name p').text('');
        $('.phone p').text('');
        return true;
    }
}
//用户信息修改
function openCusInfo() {
    // 数据获取添加用户数据
    loadData(function(res) {
        $('#cusId').val(res.projectUser.pu_projectUserId);
        $('#cusLinkman').val(res.projectUser.pu_linkman);
        $('#cusTelephone').val(res.projectUser.pu_telephone);
        $('#cusEmail').val(res.projectUser.pu_email);
    }, getContextPath() + '/project/task/edit/parameter/' + $("#currentTaskId").val() + "/" + $('#projectId').val() + "/pu", null);
    // 数据获取添加表单的id
    loadData(function(res) {
        // 表单添加id
        $('#proId').val(res.projectFlow.pf_projectId);
    }, getContextPath() + '/project/task/edit/parameter/' + $("#currentTaskId").val() + "/" + $('#projectId').val() + "/pf", null);
    // 确认提交
    $('#surebtn').off('click').on('click', function() {
        if (surebtn()) {
            $('#toCusForm').submit();
        }
    });
    // 取消事件
    $('.cancel div').off('click').on('click', function() {
        window.location.href = '/project/phone/flowinfo/' + $("#currentTaskId").val() + "/" + $('#projectId').val() + "/" + $('#processInstanceId').val();
    });
    // 返回的跳转
    $('.frameHead a').attr('href', '/project/phone/flowinfo/' + $("#currentTaskId").val() + "/" + $('#projectId').val() + "/" + $('#processInstanceId').val());
}