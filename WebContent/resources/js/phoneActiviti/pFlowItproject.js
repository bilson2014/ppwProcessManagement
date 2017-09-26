var title;
var rowsR;
$().ready(function() {
    console.log('/project/phone/flowinfo/' + $("#currentTaskId").val() + "/" + $('#projectId').val() + "/" + $('#processInstanceId').val());
    // 设置标题的信息
    $('.frameHead .name').text('项目信息修改');
    // 初始化项目来源
    // initResouces()
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

    var num = /^[0-9]*$/;
    if (name.length == 0) {
        $('.name p').text('*项目名称不能为空');

    } else if (day.length == 0) {
        $('.period p').text('*项目周期不能为空');

    } else if (!num.test(day)) {
        $('.period p').text('*请输入正确的数字');
    } else if (url.length == 0) {
        $('.film p').text('*对标影片不能为空');

    }  else {
        $('.name p').text('');
        $('.period p').text('');
        $('.film p').text('');
       
        return true;
    }
}
//项目信息修改
function projectchange() {
    loadData(function(res) {
      
        // 表单添加id
        $('#proId').val(res.projectFlow.pf_projectId);
        // 获取项目评级
        var Grade = res.projectFlow.pf_projectGrade;
        if (Grade == '5') {
            Grades = 'S';
        }
        if (Grade == '4') {
            Grades = 'A';
        }
        if (Grade == '3') {
            Grades = 'B';
        }
        if (Grade == '2') {
            Grades = 'C';
        }
        if (Grade == '1') {
            Grades = 'D';
        }
        if (Grade == '0') {
            Grades = 'E';
        }
        $('#pf_projectGradeInput').val(res.projectFlow.pf_projectGrade);
        $('#setinputs').text(Grades);
        // 获取项目来源
        var num = res.projectFlow.pf_projectSource;
        var name;
        if (num == 1) {
            name = '线上-网站';
        }
        if (num == 2) {
            name = '线上-活动';
        }
        if (num == 3) {
            name = '线上-新媒体 ';
        }
        if (num == 4) {
            name = '线上-电销';
        }
        if (num == 5) {
            name = '线下-直销';
        }
        if (num == 6) {
            name = '线下-活动';
        }
        if (num == 7) {
            name = '线下-渠道';
        }
        if (num == 8) {
            name = '复购';
        }
        if (num == 9) {
            name = '线上-400';
        }
        if (num == 10) {
            name = '线上-商桥';
        }
        if (num == 11) {
            name = '线上-PC-首页banner';
        }
        if (num == 12) {
            name = '线上-PC-直接下单';
        }
        if (num == 13) {
            name = '线上-PC-成本计算器';
        }
        if (num == 14) {
            name = '线上-PC-供应商首页';
        }
        if (num == 15) {
            name = '线上-PC-作品';
        }
        if (num == 16) {
            name = '线上-移动-首页banner';
        }
        if (num == 17) {
            name = '线上-移动-成本计算器';
        }
        if (num == 18) {
            name = '线上-移动-作品';
        }
        if (num == 19) {
            name = '线上-公众号-成本计算器';
        }
        if (num == 20) {
            name = '线上-公众号-直接下单';
        }
        if (num == 21) {
            name = '线上-公众号-作品';
        }
        $('#pf_ResourInput').val(res.projectFlow.pf_projectSource);
        $('#setinput').text(name);
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
    $('.projectbox .surebox .cancel div').off('click').on('click', function() {
        //     console.log("33");
        //     window.location.href = '/project/phone/flowinfo/' + $("#currentTaskId").val() + "/" + $('#projectId').val() + "/" + $('#processInstanceId').val();

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
//初始化项目来源和项目评级
function initResouce() {
    loadData(function(res) {
        console.log(res);
        var rowsR = res.result.resource;
        var newarr = [];
        for (var i = 0; i < rowsR.length; i++) {
            newarr.push(new city(rowsR[i].id, rowsR[i].text));
        }
        newarr = JSON.stringify(newarr);
        initSelectIos(newarr);
    }, getContextPath() + '/product/productSelection', null);
}

function initResouces() {
    loadData(function(res) {
        var grade = res.result.clientLevel;
        var newarrs = [];
        for (var i = 0; i < grade.length; i++) {
            newarrs.push(new city(grade[i].id, grade[i].text));
        }
        newarrs = JSON.stringify(newarrs);
        initSelectIosgrade(newarrs);
    }, getContextPath() + '/product/productSelection', null);
}

function createOption(value, text, price) {
    var html = '<li data-price="' + price + '" data-id="' + value + '">' + text + '</li>';
    return html;
}

// 项目来源和项目评级
function selectEven() {
    $('#source').off('click').on('click', function() {
        initResouce();
        $(window.parent.document).find('.pagePhone').scrollTop(9999);
    })
    $('#grade').off('click').on('click', function() {
        initResouces();
        $(window.parent.document).find('.pagePhone').scrollTop(9999);
    })
}
// 项目来源获取
function initSelectIos(nnn) {
    var nnn = [{ "id": 4, "value": "线下-电销" }, { "id": 5, "value": "线下-直销" }, { "id": 6, "value": "线下-活动" }, { "id": 7, "value": "线下-渠道" }, { "id": 8, "value": "复购" }, { "id": 1, "value": "线上-网站" }, { "id": 2, "value": "线上-活动" }, { "id": 3, "value": "线上-新媒体" }, { "id": 9, "value": "线上-400" }, { "id": 10, "value": "线上-商桥" }, { "id": 11, "value": "线上-PC-首页banner" }, { "id": 12, "value": "线上-PC-直接下单" }, { "id": 13, "value": "线上-PC-成本计算器" }, { "id": 14, "value": "线上-PC-供应商首页" }, { "id": 15, "value": "线上-PC-作品" }, { "id": 16, "value": "线上-移动-首页banner" }, { "id": 17, "value": "线上-移动-成本计算器" }, { "id": 18, "value": "线上-移动-作品" }, { "id": 19, "value": "线上-公众号-成本计算器 " }, { "id": 20, "value": "线上-公众号-直接下单" }, { "id": 21, "value": "线上-公众号-作品" }];
    var bankSelect = new IosSelect(1, [nnn], {
        title: title,
        itemHeight: 35,
        oneLevelId: '',
        callback: function(selectOneObj) {
            $('#setinput').attr('data-id', selectOneObj.id);
            $('#setinput').text(selectOneObj.value);
            $('#pf_ResourInput').val(selectOneObj.id);
        }
    });
}

function city(id, text) {
    this.id = id;
    this.value = text;
}
// // 项目评级获取
function initSelectIosgrade(mmm) {
    var mmm = [{ "id": "5", "value": "S" }, { "id": "4", "value": "A" }, { "id": "3", "value": "B" }, { "id": "2", "value": "C" }, { "id": "1", "value": "D" }, { "id": "0", "value": "E" }];
    var bankSelect = new IosSelect(1, [mmm], {
        title: title,
        itemHeight: 35,
        oneLevelId: '',
        callback: function(selectOneObj) {
            $('#setinputs').attr('data-id', selectOneObj.id);
            $('#setinputs').text(selectOneObj.value);
            $('#pf_projectGradeInput').val(selectOneObj.id);
        }
    });
}