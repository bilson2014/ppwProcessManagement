var title;
var rowsR
$().ready(function() {
    // 设置标题的信息
    $('.frameHead .name').text('项目信息修改');
    // 初始化项目来源
    initResouces()
    pp();
    //项目信息修改事件
    projectchange();

    selectEven();
});
// 确认事件
function surebtn() {
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
        return true;
    }
}
//项目信息修改
function projectchange() {
    loadData(function(res) {
        console.log(res);
        // 表单添加id
        $('#proId').val(res.projectFlow.pf_projectId);
        // 获取项目评级
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
        console.log(res.projectFlow.pf_projectGrade);
        console.log(Grade);
        $('#pf_projectGradeInput').val(Grade);


        $('#pf_projectGrade').text(Grade);
        // 获取项目来源
        var resour = $('#pResour li');
        for (var i = 0; i < resour.length; i++) {
            if ($(resour[i]).attr('data-id') == res.projectFlow.pf_projectSource) {
                $('#pf_Resour').text($(resour[i]).text());
                $('#pf_Resour').attr('data-id', ($(resour[i]).attr('data-id')))
            }
        }
        $('#pf_ResourInput').val(res.projectFlow.pf_projectSource);
        //项目名称
        $('#proName').val(res.projectFlow.pf_projectName);
        // 项目周期
        $('#proCycle').val(res.projectFlow.pf_projectCycle);
        // 对标影片
        $('#proFdp').val(res.projectFlow.pf_filmDestPath);
        // 项目描述
        $('#projectDes').val(res.projectFlow.pf_projectDescription);
    }, getContextPath() + '/project/task/edit/parameter/' + $("#currentTaskId").val() + "/" + $('#projectId').val() + "/pf", null);
    $('#surebtn').off('click').on('click', function() {
        if (surebtn()) {
            $('#toProjectForm').submit();
        }
    });
    // 取消事件
    $('.cancel div').off('click').on('click', function() {
        window.location.href = '/project/phone/flowinfo/' + $("#currentTaskId").val() + "/" + $('#projectId').val() + "/" + $('#processInstanceId').val();
    });
    // 返回的跳转
    $('.frameHead a').attr('href', '/project/phone/flowinfo/' + $("#currentTaskId").val() + "/" + $('#projectId').val() + "/" + $('#processInstanceId').val());
    // 项目评级下拉框
    $('.projectbox .rate .orderSelect').off('click').on('click', function() {
        $('.projectbox .rate .orderSelect .oSelect').toggleClass('show');
    })
    $('.projectbox .rate .orderSelect .oSelect li').off('click').on('click', function() {
        var text = $(this).text();
        $('#pf_projectGrade').text(text);
        $('.projectbox .rate .orderSelect .oSelect').removeClass('show');
    })
}
// 项目评级
function pp() {
    $('.projectbox .rate .orderSelect .oSelect').removeClass('show');
    $('#pf_projectGrade').attr('data-id', '');
    $('.projectbox .rate .orderSelect .oSelect').removeClass('show');
}
//初始化来源
function initResouces() {
    loadData(function(res) {
        console.log(res);

        var rowsR = res.result.resource;
        console.log(rowsR);

        var newarr = [];
        $.each(rowsR, function(i, v) {
            $.each(v, function(j, val) {
                newarr.push(val);
            })
        })
        console.log(newarr);
        // var data = [];
        // for (i in rowsR) {
        //     data.push(i);

        // }
        // for (var i = 0; i < data.length; i++) {
        //     var datas = {};
        //     datas.id = data[i];
        //     data.push(datas);
        // }



        // var a = JSON.parse(rowsR);
        // console.log(a);
        // var b = JSON.stringify(a);
        // console.log(b);
        // var c = [];
        // for (var i = 0; i < a.length; i++) {
        //     c[i] = a[i];
        // }
        // console.log(c);
        // console.log(data);
        // console.log(datas);
    }, getContextPath() + '/product/productSelection', null);
}

function createOption(value, text, price) {
    var html = '<li data-price="' + price + '" data-id="' + value + '">' + text + '</li>';
    return html;
}


function selectEven() {
    $('.setSelect').off('click').on('click', function() {
        initSelectIos();
        $(window.parent.document).find('.pagePhone').scrollTop(9999);
    })
}

function initSelectIos() {
    console.log($('#setinput').text());
    var data = rowsR;

    // var data = [{
    //     'id': '10001',
    //     'value': '看情况'
    // }, {
    //     'id': '10002',
    //     'value': '1万元及以上'
    // }, {
    //     'id': '10003',
    //     'value': '2万元及以上'
    // }, {
    //     'id': '10004',
    //     'value': '3万元及以上'
    // }, {
    //     'id': '10005',
    //     'value': '5万元及以上'
    // }, {
    //     'id': '10006',
    //     'value': '10万元及以上'
    // }, ];
    var bankSelect = new IosSelect(1, [data], {
        title: title,
        itemHeight: 35,
        oneLevelId: '',
        callback: function(selectOneObj) {
            $('#setinput').attr('data-id', selectOneObj.id);
            $('#setinput').text(selectOneObj.value);
            // .text($(resour[i]).text());
        }
    });
}