$().ready(function() {
    // 设置标题的信息
    // $('.frameHead .name').text('客户信息修改');
    $('.frameHead .name').text($('#projectName').val());
    console.log($('#projectName').val());
    surebtn();
    openCusInfo();
});
// sure 事件
function surebtn() {
    $('#surebtn').off('click').on('click', function() {
        var name = $('.name input').val();
        var phone = $('.phone input').val();

        $('.name p').text('');
        $('.phone p').text('');

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

    })
}


//用户信息修改
function openCusInfo() {
    loadData(function(res) {
        $('#cusId').val(res.projectUser.pu_projectUserId);
        $('#cusLinkman').val(res.projectUser.pu_linkman);
        $('#cusTelephone').val(res.projectUser.pu_telephone);
    }, getContextPath() + '/project/task/edit/parameter/' + $("#currentTaskId").val() + "/" + $('#projectId').val() + "/pu", null);

    $('#submitCus').off('click').on('click', function() {
        if (surebtn()) {
            $('#toCusForm').submit();
        }

    });
}