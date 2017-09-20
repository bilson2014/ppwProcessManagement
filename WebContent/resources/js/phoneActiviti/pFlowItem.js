$().ready(function() {
    $('.frameHead .name').text($('#projectName').val());
    $('#projectTime').text(formatDate($('#projectTime').text().replace("CST", "GMT+0800")));
    logobtn();
});
// 项目团队信息头像遮罩
function logobtn() {
    $('.teaminfo .teambox ul li').off('click').on('click', function() {
        var name = $(this).find('.reTalkItem').text();
        var img = $(this).find('img').attr('src');
        var phone = $(this).find('.telephone').text();
        var study = $(this).find('.study').text();
        console.log(study);
        $('.mask').show();
        $('.mask .box .name').text(name);
        $('.mask .box img').attr('src', img);
        $('.mask .box .text').text(study);
        $('.mask .box .phone a').text(phone);
        $('.mask .box .phone a').attr('href', 'tel:' + phone);

    })
    $('.mask .close').off('click').on('click', function() {
        $('.mask').hide();
    })
}