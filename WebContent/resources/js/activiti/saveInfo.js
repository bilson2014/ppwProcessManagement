var InterValObj; // timer变量，控制时间  
var count = 120; // 间隔函数，1秒执行  
var curCount; // 当前剩余秒数 
var uploader;

$().ready(function(){
    //弹框
    init();

    $('#orderlist').addClass('hide');
    $('#projectlist').addClass('hide');
    $('#projectlist').toggleClass('hide');
    $('#orderlist').addClass('hide');
   
    $('#order').click(function() {
        $('#orderlist').toggleClass('hide');
        $('#projectlist').addClass('hide');

    })
    $('#less').click(function() {
        $('#right').attr('calss', 'rightsize');
        $('#leftsmall').toggleClass('show');
        $('#left').toggleClass('hide');
        $('#right').toggleClass('rightsize');

    })
    //左边字体图标
    $('#more').click(function() {
        $('#right').attr('calss', 'rightsize');
        $('#leftsmall').toggleClass('show');
        $('#left').toggleClass('hide');
        $('#right').toggleClass('rightsize');

    })

    loginpassword();
    verpassword();
    phonebind();
    verphone();
    emilbind();
    veremil();
    userpicInfo();
    // verifyData();
});

    //文件上传
    //头像修改
    function userpicInfo() {

        uploader && uploader.destroy();
        uploader = WebUploader.create({
            auto: true,
            swf: '/resources/lib/webuploader/Uploader.swf',
            server: '/provider/upload/teamPhoto',
            pick: '#uploadBt',
            accept: {
                title: 'Images',
                extensions: 'jpg,png,jpeg',
                mimeTypes: 'image/jpeg,image/png'
            },
            resize: true,
            chunked: false,
            fileSingleSizeLimit: 1024 * 2048,
            duplicate: true //允许重复上传同一个
        });
        uploader.on('uploadSuccess', function(file, response) {
            var path = response._raw;
            if (path != '' && path != null) {
                if (path.indexOf('false@error') > -1) {
                    if (path.indexOf("error=1") > -1) {
                        showErrorLeader($('.user-img-content'), '文件超过最大限制');
                    } else if (path.indexOf("error=2") > -1) {
                        showErrorLeader($('.user-img-content'), '格式不正确');
                    }
                } else {
                    $('#user_img_url').val(path);
                    var img = getDfsHostName() + path;
                    $('#user-img').attr('src', img);
                    showErrorLeader($('.user-img-content'), '');
                }
            } else {
                showErrorLeader($('.user-img-content'), '上传失败!');
            }
        });
        uploader.on('error', function(type) {
            if (type == "Q_TYPE_DENIED") {
                showErrorLeader($('.user-img-content'), '格式不正确!');
            } else if (type == "F_EXCEED_SIZE") {
                showErrorLeader($('.user-img-content'), '文件超过最大限制!');
            }
        });
    }
    //弹出框
    function init() {
        $('#closeCheck').off('click').on('click', function() {
            $('.tooltip-check').hide();
        });
        $('#sureCheck').off('click').on('click', function() {
            $('.tooltip-check').hide();
        });

    }

    //设置登录密码
    function loginpassword() {
        $('#password').click(function() {
            $('#pas').toggleClass('show');
            $('#infos').addClass('hide');
        })
        $('#pascancel').click(function() {
            $('#pas').toggleClass('show');
            $('#infos').toggleClass('hide');
            $('#inputpas').val('');
            $('#inputrep').val('');
            $('#correctagn').hide();
            $('#mistakeagn').hide();
            $('#mistakeagn').show();
            $('#pas .newpas p').text('');
        })
    }
    //验证密码
    function verpassword() {
        $('#inputpas').blur(function() {
            var newpas = this.value;
            if (newpas.length <= 0) {
                $('#pas .newpas p').text('*新密码不能为空');
                $('#mistakeagn').hide();
                $('#correctagn').hide();
                return false;
            } else {
                $('#pas .newpas p').text('');
            }
        })
        $('#inputrep').blur(function() {
            var inputrep = this.value;
            if (inputrep.length > 0) {
                $('#mistakeagn').hide();
                $('#correctagn').show();
                return false;
            } else {
                $('#correctagn').toggleClass('.hide');
                $('#mistakeagn').toggleClass('.show');
            }
        })
        $('#saverep').click(function() {
            if ($('#inputpas').val().trim() == '' || $('#inputpas').val().trim() == null) {
                $('#pas .newpas p').text('*新密码不能为空');
                return false;
            }
            if ($('#inputpas').val() != $('#inputrep').val()) {
                $('#mistakeagn').show();
                return false;
            }
            //弹框显示
            $('.tooltip-check').show();
            //弹框中的内容部分
            $('#checkInfo').text('登录密码设置成功！！！');

        })
        $('#sureCheck').click(function() {
            // $('.tips').fadeOut(500);
            if ($('#checkInfo').text().trim() == '登录密码设置成功！！！') {
                $('#pas').toggleClass('show');
                $('#infos').toggleClass('hide');
                $('#inputpas').val('');
                $('#inputrep').val('');
                $('#correctagn').hide();
                $('#mistakeagn').hide();
            }
        })
    }
    //设置手机绑定
    function phonebind() {
        $('#phone').click(function() {
            $('#pho').toggleClass('show');
            $('#infos').addClass('hide');
        })
        $('#phocancel').click(function() {
            $('#pho').hide();
            $('#infos').show();
            $('#pho').toggleClass('show');
            $('#infos').toggleClass('hide');
            $('#inputvernewpho').val('');
            $('#inputnewpho').val('');
            $('#pho .verifynewphone p').text('');
            $('#pho .newphone p').text('');
        })
    }
    //验证手机
    function verphone() {
        $('#inputnewpho').click(function() {
            $('#pho .verifynewphone p').hide();
        })
        $('#inputnewpho').blur(function() {
            var inputnewpho = this.value;
            var pho = $('#inputnewpho').val();
            var rge = /^1[34578]\d{9}$/;
            if (inputnewpho.length <= 0) {
                $('#pho .newphone p').text("*手机号码不能为空");
                $('#pho .verifynewphone p').text('');
                return false;
            } else if (!rge.test(pho)) {
                $('#pho .newphone p').text("*请输入正确的手机码");
                return false;
            } else {
                $('#pho .newphone p').hide();
            }
        })
        $('#inputvernewpho').blur(function() {
            $('#pho .verifynewphone p').show();
            var inputvernewpho = this.value;
            if (inputvernewpho.length <= 0) {
                $('#pho .verifynewphone p').text("*验证码不能为空");
                $('#pho .newphone p').text('');
                return false;
            } else {
                $('#pho .verifynewphone p').hide();
            }
        })
        $('#send').click(function() {
            $('#pho .verifynewphone p').hide();
            var inputnewpho = $('#inputnewpho').val();
            var pho = $('#inputnewpho').val();
            var rge = /^1[34578]\d{9}$/;
            if (inputnewpho.length <= 0) {
                $('#pho .newphone p').text("*手机号码不能为空");
                $('#pho .verifynewphone p').text('');
                return false;
            } else if (!rge.test(pho)) {
                $('#pho .newphone p').text("*请输入正确的手机码");
                return false;
            } else {
                $('#pho .newphone p').hide();
                //弹框显示
                $('.tooltip-check').show();
                //弹框中的内容部分
                $('#checkInfo').text('验证发送成功');
            }
        })
        $('#savepho').click(function() {
            $('#pho .verifynewphone p').show();
            var inputvernewpho = $('#inputvernewpho').val();
            var pho = $('#inputvernewpho').val();
            var rge = 1111;
            //  var rge = /^1[34578]\d{9}$/;
            if (inputvernewpho.length <= 0) {
                $('#pho .verifynewphone p').text("*验证码不能为空");
                $('#pho .newphone p').text('');
                return false;
            } else if (rge != pho) {
                $('#pho .verifynewphone p').text("*请输入正确的验证码");
                return false;
            } else {
                $('#pho .verifynewphone p').hide();
                //弹框显示
                $('.tooltip-check').show();
                //弹框中的内容部分
                $('#checkInfo').text('手机绑定成功！！！');

            }
        })
        $('#sureCheck').click(function() {
            // $('.tips').fadeOut(500);
            if ($('#checkInfo').text().trim() == '手机绑定成功！！！') {
                $('#nowphone').text($('#inputnewpho').val());
                $('#pho').toggleClass('show');
                $('#infos').toggleClass('hide');
                $('#inputvernewpho').val('');
                $('#inputnewpho').val('');

            }
        })
    }
    //设置邮箱绑定
    function emilbind() {
        $('#emil').click(function() {
            $('#emils').addClass('show');
            $('#infos').addClass('hide');
        })
        $('#emilcancel').click(function() {
            $('#emils').toggleClass('show');
            $('#infos').toggleClass('hide');
            $('#emils .newemil p').hide();
            $('#inputnewemi').val('');
        })
    }
    //验证邮箱
    function veremil() {
        $('#inputnewemi').blur(function() {
            $('#emils .newemil p').show();
            var inputnewemi = $('#inputnewemi').val().trim();
            if (inputnewemi == '' || inputnewemi == null) {
                $('#emils .newemil p').text("*邮箱不能为空");
            } else {
                $('#emils .newemil p').hide();
            }
        })
        $('#saveem').click(function() {
            $('#emils .newemil p').show();
            var inputnewemi = $('#inputnewemi').val().trim();
            if (inputnewemi == '' || inputnewemi == null) {
                $('#emils .newemil p').text("*邮箱不能为空");
                return false;
            } else {
                $('#emils .newemil p').hide();
                //弹框显示
                $('.tooltip-check').show();
                //弹框中的内容部分
                $('#checkInfo').text('新邮箱设置成功！！！');
            }
        })
        $('#sureCheck').click(function() {
            if ($('#checkInfo').text().trim() == '新邮箱设置成功！！！') {
                $('#nowmail').text($('#inputnewemi').val() + $('#emils .newemil select option:selected').text());
                $('#emils').toggleClass('show');
                $('#infos').toggleClass('hide');
                $('#inputnewemi').val('');
            }
        })
    }
   



