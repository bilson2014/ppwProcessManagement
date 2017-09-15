$().ready(function() {

    $('.frameHead .name').text('华为手机最新产品宣传页');
    $('#projectTime').text(formatDate($('#projectTime').text().replace("CST", "GMT+0800")));
    console.log('666');
    // $('.frameHead .name').text($('#projectName').val());
    // loadData(function(res) {


    // }, getContextPath() + '/message/addReply', $.toJSON({
    //     projectId: projectId,
    //     taskName: taskName,
    //     content: content,
    //     parentId: parentId
    // }));

    var str = location.href; //取得整个地址栏
    var num = str.indexOf("?")
    str = str.substr(num + 1);
    console.log(str);
    logobtn();
    console.log($('.teaminfo .teambox ul li div').val());
});
// 项目团队信息头像遮罩
function logobtn() {
    $('.teaminfo .teambox ul li').off('click').on('click', function() {
        var name = $(this).find('.reTalkItem').text();
        var img = $(this).find('img').attr('src');
        var phone = $(this).find('.telephone').text();

        $('.mask').show();
        $('.mask .box .name').text(name);
        $('.mask .box img').attr('src', img);
        $('.mask .box .phone').text(phone);


    })
    $('.mask .close').off('click').on('click', function() {
        $('.mask').hide();
    })
}