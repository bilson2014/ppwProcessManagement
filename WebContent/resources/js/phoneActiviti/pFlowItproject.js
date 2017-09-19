var title;
var rowsR;
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
        var rowsR = res.result.resource;
        var newarr = [];
        for (var i = 0; i < rowsR.length; i++) {
            // console.log(rowsR[i].id);
            // console.log(rowsR[i].text);
            newarr.push(new city(rowsR[i].id, rowsR[i].text));
        }
        // $.each(rowsR, function(i, v) {
        //     console.log(v);
        // newarr.push(new city(i, v));
        // $.each(v, function(i, val) {
        //     if (i == 'id') {
        //         var dd = val;
        //         // newarr.push(new city(dd, cc));
        //     }
        //     if (i == 'text') {
        //         var cc = val;
        //     }
        //     newarr.push(new city(dd, cc));
        //     console.log(newarr);
        //     // else {
        //     //     console.log('1');
        //     //     // newarr.push(new city(i, val));
        //     // }
        // })
        // })
        newarr = JSON.stringify(newarr);
        initSelectIos(newarr);
    }, getContextPath() + '/product/productSelection', null);
}

function createOption(value, text, price) {
    var html = '<li data-price="' + price + '" data-id="' + value + '">' + text + '</li>';
    return html;
}


function selectEven() {
    $('.setSelect').off('click').on('click', function() {

        initResouces();

        $(window.parent.document).find('.pagePhone').scrollTop(9999);
    })
}

function initSelectIos(nnn) {
    // initResouces();
    // console.log("sssasjkdhadshakjda" + nn);
    var a = nnn;
    console.log(a);
    // var data = rowsR;
    // console.log(data);
    // var data = [{
    //     'id': 10001,
    //     'value': '看情况'
    // }, {
    //     'id': 10002,
    //     'value': '1万元及以上'
    // }, {
    //     'id': 10003,
    //     'value': '2万元及以上'
    // }, {
    //     'id': 10004,
    //     'value': '3万元及以上'
    // }, {
    //     'id': 10005,
    //     'value': '5万元及以上'
    // }, {
    //     'id': 10006,
    //     'value': '10万元及以上'
    // }, ];
    // var data = [{ "id": 4, "value": "线下-电销" }, { "id": 5, "value": "线下-直销" }, { "id": 6, "text": "线下-活动" }, { "id": 7, "text": "线下-渠道" }, { "id": 8, "text": "复购" }, { "id": 1, "text": "线上-网站" }, { "id": 2, "text": "线上-活动" }, { "id": 3, "text": "线上-新媒体" }, { "id": 9, "text": "线上-400" }, { "id": 10, "text": "线上-商桥" }, { "id": 11, "text": "线上-PC-首页banner" }, { "id": 12, "text": "线上-PC-直接下单" }, { "id": 13, "text": "线上-PC-成本计算器" }, { "id": 14, "text": "线上-PC-供应商首页" }, { "id": 15, "text": "线上-PC-作品" }, { "id": 16, "text": "线上-移动-首页banner" }, { "id": 17, "text": "线上-移动-成本计算器" }, { "id": 18, "text": "线上-移动-作品" }, { "id": 19, "text": "线上-公众号-成本计算器 " }, { "id": 20, "text": "线上-公众号-直接下单" }, { "id": 21, "text": "线上-公众号-作品" }];
    // var data = [{ "id": 4, "value": "线下-电销" }, { "id": 5, "value": "线下-直销" }, { "id": 6, "value": "线下-活动" }, { "id": 7, "value": "线下-渠道" }, { "id": 8, "value": "复购" }, { "id": 1, "value": "线上-网站" }, { "id": 2, "value": "线上-活动" }, { "id": 3, "value": "线上-新媒体" }, { "id": 9, "value": "线上-400" }, { "id": 10, "value": "线上-商桥" }, { "id": 11, "value": "线上-PC-首页banner" }, { "id": 12, "value": "线上-PC-直接下单" }, { "id": 13, "value": "线上-PC-成本计算器" }, { "id": 14, "value": "线上-PC-供应商首页" }, { "id": 15, "value": "线上-PC-作品" }, { "id": 16, "value": "线上-移动-首页banner" }, { "id": 17, "value": "线上-移动-成本计算器" }, { "id": 18, "value": "线上-移动-作品" }, { "id": 19, "value": "线上-公众号-成本计算器 " }, { "id": 20, "value": "线上-公众号-直接下单" }, { "id": 21, "value": "线上-公众号-作品" }];
    // console.log(data);
    var bankSelect = new IosSelect(1, nnn, {
        title: title,
        itemHeight: 35,
        oneLevelId: '',
        callback: function(selectOneObj) {
            var a = selectOneObj.id;
            console.log(a);
            $('#setinput').attr('data-id', selectOneObj.id);
            $('#setinput').text(selectOneObj.value);
            // .text($(resour[i]).text());
        }
    });
}

function city(id, text) {
    this.id = id;
    this.value = text;
}