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
    // 确认事件
    surebtn();
    //项目信息修改事件
    projectchange();

});
// sure 事件
function surebtn() {
    $('#surebtn').off('click').on('click', function() {
        console.log('666');
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
//项目信息修改
function projectchange() {
    loadData(function(res) {
        //项目 名称
        $('#proName').val(res.projectFlow.pf_projectName);
        console.log($('#proName').val());
        // 项目评级
        var Grade = res.projectFlow.pf_projectGrade;
        $('#pf_projectGrade').attr('data-id', Grade);
        if (Grade == '5') {
            Grade = 'S';
        }
        if (Grade == '4') {
            Grade = 'A';
        }
        if (Grade == '3') {
            Grade = 'B';
        }
        if (Grade == '2') {
            Grade = 'C';
        }
        if (Grade == '1') {
            Grade = 'D';
        }
        if (Grade == '0') {
            Grade = 'E';
        }
        // 项目来源
        $('#pf_ResourInput').val(res.projectFlow.pf_projectSource);

        var resour = $('#pResour li');
        for (var i = 0; i < resour.length; i++) {
            if ($(resour[i]).attr('data-id') == res.projectFlow.pf_projectSource) {
                $('#pf_Resour').text($(resour[i]).text());
                $('#pf_Resour').attr('data-id', ($(resour[i]).attr('data-id')))
            }
        }

        $('#pf_projectGrade').text(Grade);
        $('#proCycle').val(res.projectFlow.pf_projectCycle);
        $('#proFdp').val(res.projectFlow.pf_filmDestPath);
        $('#projectDes').val(res.projectFlow.pf_projectDescription);
    }, getContextPath() + '/project/task/edit/parameter/' + $("#currentTaskId").val() + "/" + $('#projectId').val() + "/pf", null);

}