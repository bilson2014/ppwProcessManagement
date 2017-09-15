$().ready(function() {
    $('.frameHead .name').text($('#projectName').val());
    $('#projectTime').text(formatDate($('#projectTime').text().replace("CST", "GMT+0800")));
    // var str = location.href; //取得整个地址栏
    // var num = str.indexOf("?")
    // str = str.substr(num + 1);
    // console.log(str);
    logobtn();
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