$().ready(function() {

    $('.frameHead .name').text('华为手机最新产品宣传页');
    console.log('666');
    // $('.frameHead .name').text($('#projectName').val());

});

function getMessageMore() {
    $('.showMessage').off('click').on('click', function() {
        if ($(this).hasClass('open')) {
            $(this).removeClass('open');
            $('.setMessage').hide();
        } else {
            $(this).addClass('open');
            $('.setMessage').show();
            $('.setMessage').attr('data-content', '');
            $('#addmessage').val('');
        }
    });
    $('.itemMore').off('click').on('click', function() {
        if ($(this).hasClass('open')) {
            $(this).removeClass('open');
            $(this).parent().find('.reTalkItem').hide();
            $('.reTalkItem').attr('data-content', '');
        } else {
            $(this).addClass('open');
            $(this).parent().find('.reTalkItem').show();
            $('.reTalkItem').attr('data-content', '');
        }
    });
}