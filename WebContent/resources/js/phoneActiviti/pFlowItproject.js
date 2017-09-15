$().ready(function() {
    // 设置标题的信息
    $('.frameHead .name').text('项目信息修改');
    // $('.frameHead .name').text($('#projectName').val());
    // $(".choice option").click(function() {
    //     $(".choice").removeAttr("size");
    //     $(".choice").blur();
    //     this.attr("selected", "");
    // });

    // $(".choice").focus(function() {
    //     $(".choice").attr("size", "5");
    // })
    surebtn();

});
// sure 事件
function surebtn() {
    $('#surebtn').off('click').on('click', function() {
        var name = $('.name input').val();
        var day = $('.period input').val();
        var url = $('.film textarea').val();
        var describe = $('.describe textarea').val();
        $('.name p').text('');
        $('.period p').text('');
        $('.film p').text('');
        $('.describe p').text('');
        var num = /^[0-9]*$/;
        if (name.length == 0) {
            $('.name p').text('*项目名称不能为空');

        } else if (day.length == 0) {
            $('.period p').text('*项目周期不能为空');

        } else if (!num.test(day)) {
            $('.period p').text('*请输入正确的数字');
        } else if (url.length == 0) {
            $('.film p').text('*对标影片不能为空');

        } else if (describe.length == 0) {
            $('.describe p').text('*项目描述不能为空');

        } else {
            $('.name p').text('');
            $('.period p').text('');
            $('.film p').text('');
            $('.describe p').text('');
        }

    })
}