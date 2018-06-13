var StepTool;
var stepListJson;
var isShow = false;
var checkHidden = false;
var currentIndex;
var hasClick = false;
var nowImg = 2;
var countCheck = 0;
var isHistory = false;
var uploader;
// add by guoyang, 2016-04-19 03:17 begin
// -> 添加时间变量
var oTimer;
// add by guoyang, 2016-04-19 03:17 end

$().ready(function() {

	var flow = {
			//方法都在init中
			init:function(){
				//初始化文档上传
				this.initUploadFile();
			},
			initUploadFile:function(){
				var _this = this;
				$('#toolbar-modal').on('shown.bs.modal', function (e) {
					uploader = WebUploader.create({
						auto:false,
						swf : '/resources/lib/webuploader/Uploader.swf',
						server : '/mgr/resource/addResource',
						pick : {
							id:'#picker',
							multiple :false
						},
						fileNumLimit : 1,
						chunked : false,
					});
					uploader.on('beforeFileQueued', function(file) {
						 //删除所有文件,只上传一个
						 var array = uploader.getFiles();
						 for(var i=0;i<array.length;i++){
							 uploader.removeFile( array[i] );
						 }
					});
					uploader.on('fileQueued', function(file) {
						 $("#upload-file-name").val(file.name);
					});
					// 文件上传过程中创建进度条实时显示。
					uploader.on('uploadProgress',function(file, percentage) {
						if($('.progress-bar-success').text()==''){
							$('#toolbar-modal').modal('hide');
							$("#upload-file-name").val("");
							$('.progress-bar-success').text('0')
							$('.progress-bar-success').attr('aria-valuenow','0').css({"width":'0%'});
							$('#mymodal').modal({backdrop: 'static', keyboard: false});
						}
						//$('.progress-bar-success').text('已完成' + (percentage*100).toFixed(2) + '%')
						$('.progress-bar-success').text('已完成' + Math.round((percentage*100)) + '%')
						$('.progress-bar-success').attr('aria-valuenow',(percentage*100)).css({"width":percentage*100+'%'});
						
					});
					uploader.on('uploadSuccess', function(file,response) {
						loadfiledata(false);
						loadcommentdata(false);
						$('#mymodal').modal('hide');
						$("#input-value").val("");
						$("#upload-file-name").val("");
					});
					_this.submit();
				})
			},
		// 点击保存按钮
		submit : function() {
			var _this = this;
			$('#upload-circle-btn').off("click").on('click', function() {
				$('.progress-bar-success').text("");
				var cat = $("#input-value").val();
				if (cat == '') {
					showAlert(errorNotNull);
					return false;
				}
				// 手动添加参数
				uploader.option('formData', {
					projectId : getCurrentProject(),
					tag : $("#input-value").val()
				});
				uploader.upload();
			});
		},
	}
	flow.init();
	$('.bottom-div').show();
	init();
	showOrderTime();
	loadprojecctlist();
	$(".flowbtn").on("click", function() {
		// $('#toolbar-check').modal({backdrop: 'static', keyboard: false});
		// initModalBtn();
		// $("#toolbar-check").modal('show');
		// $(".sure-margin").on('click');
		// $(".check-step").removeClass("hide");
		// $("#listLoadCheck").addClass("hide");
		// $(".check-step").html("请确认本阶段所有步骤已经完成<br/>即将进入下个阶段,您确定吗？");
		checkList();
		// setModalEvent(checkList);
	});
	$(".cancle-margin").on("click", function() {
		$("#toolbar-check").modal('hide');
	});
	$(".more-file-btn").on("click", function() {
		if ($('#more-FileImg').hasClass('circle-180')) {
			loadfiledata(false);
			moreFile(false);
		} else {
			loadfiledata(true);
			moreFile(true);
		}
	});
	$(".comment-btn").on("click", function() {
		submitcomment();
	});
	$(".more-comment").on("click", function() {
		if ($('#more-commentImg').hasClass('circle-180')) {
			loadcommentdata(false);
			moreComment(false);
		} else {
			loadcommentdata(true);
			moreComment(true);
		}
	});
	$(".newBtn").on("click", function() {
		window.location.href = getContextPath() + "/mgr/flow/add-view";
	});
	$(".pausebtn").on("click", function() {
		initModalBtn();
		pauseBtn();
	});
	$(".cancelbtn").on("click", function() {
		initModalBtn();
		cancelBtn();
	});
	$("#upload-info-btn-id").on("click", function() {
		// window.location.href = getContextPath()
		// + "/mgr/projects/upadte-view";
		submitForm();
	});
	$("#canclestep").on('click', function() {
		$("#toolbar-check").modal('hide');
	});
	$("#canclestepPause").on('click', function() {
		$("#toolbar-pause-re").modal('hide');
	});
	$("#new-project").on('click', function() {
		window.location.href = getContextPath() + "/mgr/flow/add-view";
	});
	$("#project-report").on('click', function() {
		window.location.href = getContextPath()
				+ "/mgr/projects/get/report";
	});
	$(".canclemodal").on('click', function() {
		$('#toolbar-modal').modal('hide');
	});
	$("#toolbar-no-message-btn").on('click', function() {
		$("#toolbar-no-message").modal('hide');
		$(".check-message").text("错误！");
	});
	$(".prev-task").on("click", function() {
		initModalBtn();
		PrevTaskBtn();
	});
	$("#cancleControl").on("click", function() {
		$('#toolbar-pause-re').modal('hide');
	});
	$(".checkListClose").on("click", function() {
		$('#next-modal').modal('hide');
		$('#checkListLabel').text('加载中.....');
		$('#checkListUL').html('');
	});
	$("#checkListcancle").on("click", function() {
		$('#next-modal').modal('hide');
		$('#checkListLabel').text('加载中.....');
		$('#checkListUL').html('');
	});
	ControlPay.initControlPay();
});

function initModalBtn() {
	$(".check-step").css('color', '#666');
	$(".sure-margin").removeClass('gray-btn-no');
	$(".sure-margin").addClass('red-btn');
}

function checkPorjectInfo() {
	var key = getCurrentProject();
	loadData(function(msg) {
		if (msg.errorCode == 200) {
			$(".sure-margin").off('click');
			$(".check-step").removeClass("hide");
			$(".check-step").css('color', '#666');
			$(".check-step").html('确认完成项目吗？');
			$("#listLoadCheck").addClass("hide");
			$(".sure-margin").on('click');
			setModalEvent(checkList);
		}

		if (msg.errorCode == 300) {
			$(".sure-margin").off('click');
			$(".check-step").removeClass("hide");
			$(".check-step").css('color', '#fb9b6a');
			$(".check-step").html('还有订单未被支付，您确定要完成项目吗？');
			$("#listLoadCheck").addClass("hide");
			$(".sure-margin").on('click');
			setModalEvent(checkList);
		}

		if (msg.errorCode == 500) {
			$(".sure-margin").off('click');
			$(".check-step").removeClass("hide");
			$(".check-step").css('color', '#fe5453');
			$(".sure-margin").removeClass('red-btn');
			$(".sure-margin").addClass('gray-btn-no');
			$(".check-step").html(msg.result + "，才能完成项目！");
			$("#listLoadCheck").addClass("hide");
			$(".sure-margin").off('click');
		}

	}, getContextPath() + '/mgr/projects/verifyProjectInfo', $.toJSON({
		id : key
	}));
}

function submitForm() {
	var key = getCurrentProject();
	var path = getContextPath() + "/mgr/projects/upadte-view";
	var formBody = '<form action="'
			+ path
			+ '" method="post" oncomplete="false" id="submitkey" style="display: none;">';
	formBody += '<input type="text" name="key" value="' + key
			+ '" style="display: none">';
	formBody += '</form>';
	$('#upload-info-btn-id').append(formBody);
	$('#submitkey').submit().remove();
}
function init() {
	$("#input-value").click(function() {
		var select_lis = document.getElementById("ul-select")
				.getElementsByTagName("li");
		$("#ul-select").css("visibility", "visible");
		isShow = true;
		for (i = 0; i < select_lis.length; i++) {
			select_lis[i].onclick = function() {
				document.getElementById("input-value").value = this.innerHTML;
				$("#ul-select").css("visibility", "hidden");
			}
		}
	});

	function getFileName(o) {
		var pos = o.lastIndexOf("\\");
		return o.substring(pos + 1);
	}
	$("#upload-file-btn-id").off("click").on("click", function() {
		$('#toolbar-modal').modal({
					backdrop : 'static',
					keyboard : false
				});
			// $('#toolbar-modal').modal('show');
		});
	$('#cancle-btn').click(function() {
		$('#toolbar-modal').modal('hide');
		$("#upload-file-name").val("");
		$("#input-value").val("");
	});
	$('#cancle-img').click(function() {
		$('#toolbar-modal').modal('hide');
		$("#upload-file-name").val("");
		$("#input-value").val("");
	});
	$('.modal').on('click', function() {
		if (checkHidden) {
			checkHidden = false;
			$("#ul-select").css("visibility", "hidden");
		}
		if (isShow) {
			isShow = false;
			checkHidden = true;
		}
	});

	loadFileTags();
	getBtnWidth();
}

var click2 = false;
function nextFlow() {
	var key = getCurrentProject();
	if (key != null && !click2) {
		click2 = true;
		loadData(function(msg) {
			click2 = false;
			if (msg.result == "true") {
				$('#next-modal').modal('hide');
				$('#checkListLabel').text('加载中.....');
				$('#checkListUL').html('')
			} else {
				$('#checkListLabel').text(msg.result);
			}
			loadprojecctlist();
		}, getContextPath() + '/mgr/flow/completeTask', $.toJSON({
			id : key
		}));
	}
}

var click = false;
function nextFlow2() {
	var key = getCurrentProject();
	if (key != null && !click) {
		click = true;
		loadData(function(msg) {
			click = false;
			if (msg.result == "true") {
				$('#next-modal').modal('hide');
				$('#checkListLabel').text('加载中.....');
				$('#checkListUL').html('')
			} else {
				$('#checkListLabel').text(msg.result);
			}
			loadprojecctlist();
		}, getContextPath() + '/mgr/flow/completeTask', $.toJSON({
			id : key,
			skipPay : "true"
		}));
	}
}
function checkList() {
	$('#next-modal').modal({
		backdrop : 'static',
		keyboard : false
	});
	$('#nextflowbtn').off('click');
	$('#nextflowbtn').addClass('hide');
	var key = getCurrentProject();
	if (key != null) {
		loadData(function(msg) {
			var html = msg.result.html;
			$('#checkListUL').html(html);
			if (msg.result.isok) {
				$('#nextflowbtn').on('click', nextFlow);
				$('#nextflowbtn').removeClass('hide');
				$('#checkListLabel').text('您确定进入下一步吗？');
			} else {
				$('#nextflowbtn').addClass('hide');
				$('#nextflowbtn').off('click');
				$('#checkListLabel').text('请先将内容补充完整！');
			}
		}, getContextPath() + '/mgr/flow/verifyIntegrity', $.toJSON({
			id : key
		}));
	}
}

function setModalMessageEvent(Confirm) {
	$("#sureControl").off('click');
	$("#sureControl").on('click', Confirm);
}

function setModalEvent(Confirm) {
	$(".sure-margin").off('click');
	$(".sure-margin").on('click', Confirm);
}
function getBtnWidth() {
	// var select_btn =
	// document.getElementById("btndiv-id").getElementsByTagName("button");
	var select_btn = $('#btndiv-id').find('button');
	var count = 0;
	for (var int = 0; int < select_btn.length; int++) {
		var value = $(select_btn[int]).css('display');
		if (value != 'none')
			count++;
	}
	var width = 0;
	if (count == 1) {
		width = 105;
	} else if (count == 2) {
		width = 108 * count + 24 * (count - 1) + 30;
	} else if (count > 2) {
		width = 108 * count + 24 * (count - 1) + 4;
	}
	$(".flowbtndiv").css("width", width);
}
// 加载文件类型
function loadFileTags() {
	loadData(function(msg) {
		var ul = $("#ul-select");
		$(ul).html("");
		for (var int = 0; int < msg.length; int++) {
			var li = $("<li>" + msg[int] + "</li>");
			$(ul).append(li);
		}
	}, getContextPath() + '/mgr/comment/getTags', $.toJSON({}));
}
// 取消按钮
function cancelBtn() {
	$('#toolbar-pause-re').modal({
		backdrop : 'static',
		keyboard : false
	});
	// $("#toolbar-check").modal('show');
	$("#reason").val('');
	$("#pauseWord").text("您确定要取消项目吗？");
	$("#reason").attr('placeholder', '取消原因');
	$('#sureControl').removeClass('no-red-btn');
	$('#sureControl').addClass('red-btn');
	$('#sureControl').removeClass('no-red-btn');
	$('#sureControl').addClass('red-btn');
	$('#reason').addClass('textareaInfo');
	$('#reason').removeClass('textareaInfoError');
	$('#pauseError').addClass('hide');
	noWorkproject = false;
	setModalMessageEvent(cancel);
}
function cancel() {
	var key = getCurrentProject();
	var reason = $('#reason').val().trim();
	if (reason == null || reason == "" || reason == undefined) {
		$('#sureControl').removeClass('red-btn');
		$('#sureControl').addClass('no-red-btn');
		$('#reason').removeClass('textareaInfo');
		$('#reason').addClass('textareaInfoError');
		$('#pauseError').removeClass('hide');
	} else {
		$('#pauseError').addClass('hide');
		$('#sureControl').removeClass('no-red-btn');
		$('#sureControl').addClass('red-btn');
		$('#reason').addClass('textareaInfo');
		$('#reason').removeClass('textareaInfoError');
		if (key != null) {
			$(".sure-margin").off('click');
			$("#sureControl").off('click');
			loadData(function(msg) {
				$("#toolbar-pause-re").modal('hide');
				loadprojecctlist();
			}, getContextPath() + '/mgr/projects/cancelProject', $
			.toJSON({
				id : key,
				description : reason
			}));
		}
	}

}
function PrevTaskBtn() {
	$('#toolbar-check').modal({
		backdrop : 'static',
		keyboard : false
	});
	// $("#toolbar-check").modal('show');
	$(".check-step").text("您确定要回退到上一步吗？");
	setModalEvent(PrevTask);
}
function PrevTask() {
	$(".sure-margin").off('click');
	var key = getCurrentProject();
	if (key != null) {
		loadData(function(msg) {
			loadprojecctlist();
			$("#toolbar-check").modal('hide');
		}, getContextPath() + '/mgr/flow/jumpPrevTask', $.toJSON({
			id : key
		}));
	}
}
// 暂停按钮
function pauseBtn() {
	$('#toolbar-pause-re').modal({
		backdrop : 'static',
		keyboard : false
	});
	$("#reason").val('');
	$("#pauseWord").text("您确定暂停项目吗？");
	$("#reason").attr('placeholder', '暂停原因');
	$('#sureControl').removeClass('no-red-btn');
	$('#sureControl').addClass('red-btn');
	$('#reason').addClass('textareaInfo');
	$('#reason').removeClass('textareaInfoError');
	$('#pauseError').addClass('hide');
	// $("#toolbar-check").modal('show');
	setModalMessageEvent(pause);

}
// 恢复按钮
function resumeBtn() {
	$('#toolbar-check').modal({
		backdrop : 'static',
		keyboard : false
	});
	// $("#toolbar-check").modal('show');
	$(".check-step").text("您确定要恢复项目吗？");
	setModalEvent(resume);
}
function pause() {
	$(".sure-margin").off('click');
	$("#sureControl").off('click');
	var key = getCurrentProject();
	var input = $('#reason').val().trim();
	if (input == null || input == "" || input == undefined) {
		$('#sureControl').removeClass('red-btn');
		$('#sureControl').addClass('no-red-btn');
		$('#reason').removeClass('textareaInfo');
		$('#reason').addClass('textareaInfoError');
		$('#pauseError').removeClass('hide');
		setModalMessageEvent(pause);
	} else {
		$('#pauseError').addClass('hide');
		$('#sureControl').removeClass('no-red-btn');
		$('#sureControl').addClass('red-btn');
		$('#reason').addClass('textareaInfo');
		$('#reason').removeClass('textareaInfoError');
		if (key != null) {
			loadData(function(msg) {
				$(".flowbtn").hide();
				$(".prev-task").hide();
				getBtnWidth();
				$("#toolbar-pause-re").modal('hide');
				loadprojecctlist();
			}, getContextPath() + '/mgr/flow/suspendProcess', $.toJSON({
				id : key,
				description : input
			}));
		}
	}

}
function resume() {
	$(".sure-margin").off('click');
	var key = getCurrentProject();
	if (key != null) {
		loadData(function(msg) {
			$(".pausebtn").text("暂停");
			$(".flowbtn").removeClass('gray-btn');
			$(".pausebtn").removeClass('red-btn');
			$(".pausebtn").addClass('gray-btn');
			$(".flowbtn").addClass('red-btn');
			$(".pausebtn").off("click");
			$(".pausebtn").on("click", function() {
				pauseBtn();
				loadflowdata();
			});
			$(".flowbtn").show();
			$(".prev-task").show();
			getBtnWidth();
			$("#toolbar-check").modal('hide');
			loadprojecctlist();
		}, getContextPath() + '/mgr/flow/resumeProcess', $.toJSON({
					id : key
		}));
	}
}

// 提交评论
function submitcomment() {
	var key = getCurrentProject();
	if (key != null) {
		var comment = $(".comment").val();
		if (comment == null || comment == '') {
			showAlert(errorNotNull);
			return;
		}
		loadData(function(msg) {
			$(".comment").val("");
			loadcommentdata(false);
		}, getContextPath() + '/mgr/comment/addComment', $.toJSON({
			icContent : comment,
			icIndentId : key
		}));
	}
}
// 加载流程模块
function loadflowdata() {
	var key = getCurrentProject();
	if (key != null) {
		loadData(function(msg) {
			// 构建流程结构对象，填充流程时间信息
			stepListJson = new Array(msg.length);
			for (var i = 0; i < msg.length; i++) {
				stepListJson[i] = {
					StepNum : (i + 1),
					StepText : msg[i].name,
					StepDescription : msg[i].description
				};
				var time = msg[i].scheduledTime;
				// 填充预计时间
				if (time != null) {
					if (time.fdStartTime != null && time.fdStartTime != '') {
						$("#et_" + msg[i].taskDefinitionKey)
						.text(	time.fdStartTime);
					} else {
						$("#et_" + msg[i].taskDefinitionKey).text('未设置');
					}
				}
				// 填充实际时间
				if (msg[i].createTime != null && msg[i].createTime != '') {
					$("#cu_" + msg[i].taskDefinitionKey).text(msg[i].createTime
					.split(' ')[0]);
				}
			}
			// 当前进行到第几步；默认为第一步
			var currentStep = 1;
			// 创建流程结构对象
			StepTool = new Step_Tool_dc("test", "mycall");
			// 使用工具对，页面绘制相关流程步骤图形显示
			StepTool.drawStep(currentStep, stepListJson);
			// 配置当前正在执行中的任务
			loadData(function(msg) {
				// 时间逻辑！检测任务是否超时，设置状态提示
				$("#cu_" + msg.taskDefinitionKey).text(getCurrentTime());
				$("#stepword_" + msg.taskDefinitionKey).text("进行中");
				var et_date = $("#et_" + msg.taskDefinitionKey).text();
				var overdue = dateCompare(getCurrentTime(), et_date);
				if (overdue) {
					resetTime('curr');
					$("#stepword_" + msg.taskDefinitionKey).addClass('timeout');
				} else {
					resetTime('curr');
					$("#stepword_" + msg.taskDefinitionKey).addClass('doing');
				}
				// 设置步骤状态
				var lablearray = $("li[id^='stepword_']");
				for (var int = 0; int < lablearray.length; int++) {
					var currLable = lablearray[int];
					var id = 'stepword_' + msg.taskDefinitionKey;
					if (id == currLable.id) {
						break;
					}
					$(currLable).text('完成');
				}

				var times = $("li[id^='cu_']");
				var isEnd = false;
				for (var int = 0; int < times.length; int++) {
					var item = times[int];
					var id = item.id;
					var taskKey;
					if (id != null && id != '' && (id.indexOf('_') != -1)) {
						var idarray = id.split('_');
						taskKey = idarray[1];
					}
					if (isEnd) {
						$(item).text("");
					}
					if (taskKey == msg.taskDefinitionKey)
						isEnd = true;
				}
				// 填充当前任务描述信息
				$(".description-text").text(msg.description);
				// 迁移节点
				var num;
				var shangwuIndex = -1;
				$(".test ul li").each(function(index) {
					num = parseInt(jQuery(this).attr("data-value"));
					var text = jQuery(this).attr("data-text");
					if(text.trim() == "商务")
						shangwuIndex = num;
					if (text.trim() == msg.name.trim()) {
						if (parseInt(num) == 1) {
							$('.prev-task').addClass('hide');
						} else {
							$('.prev-task').removeClass('hide');
						}
						StepTool.drawStep(num, stepListJson);
						currentIndex = num;
						// TODO:lt add payList beigin 20160622
						var type = $("#type").val();
						if (!isHistory) {
							if (shangwuIndex != -1 && currentIndex >= shangwuIndex) {
								$('#managerId').removeClass('hide');
								// $('#cusId').removeClass('hide');
								$('#Outline').removeClass('hide');
								$('#Online').removeClass('hide');
								if (type == "employee") {
									checkHasListForEmFirst();
								}
								if (type == "customer") {
									checkHasList();
									checkHasNoPayList();
								}
							} else {
								$('#managerId').addClass('hide');
								$('#cusId').addClass('hide');
								$('#payListPage').html('');
								$('#payHistoryList').slideUp();
							}
						} else {
							if (type == "customer") {
								checkHasList();
								checkHasNoPayList();
							}
							if (type == "employee") {
								checkHasListForEm();
							}
						}
						// end
						return;
					}
				});
				// 暂停状态
				if (msg.suspended) {
					$(".flowbtn").hide();
					$(".prev-task").hide();
					$(".pausebtn").text("恢复");
					$(".flowbtn").removeClass('red-btn');
					$(".pausebtn").removeClass('gray-btn');
					$(".pausebtn").addClass('red-btn');
					$(".flowbtn").addClass('gray-btn');
					$(".pausebtn").off("click");
					// 配置按钮功能为恢复项目运行
					$(".pausebtn").on("click", function() {
						resumeBtn();
					});
					$("#Online").hide();
					$("#Outline").hide();
				} else {
					$(".flowbtn").show();
					$(".prev-task").show();
					$(".pausebtn").text("暂停");
					$(".flowbtn").removeClass('gray-btn');
					$(".pausebtn").removeClass('red-btn');
					$(".pausebtn").addClass('gray-btn');
					$(".flowbtn").addClass('red-btn');
					$(".pausebtn").off("click");
					$(".pausebtn").on("click", function() {
						pauseBtn();
					});
					$("#Online").show();
					$("#Outline").show();

				}
				getBtnWidth();
				if (msg.name.trim() == '任务不存在') {
					StepTool.drawStep(num, stepListJson);
					currentIndex = num;
					finish();
					return;
				}
				// 添加特效时间
				$(".drop-content a").each(function(index) {
					$(this).css({
						'animation-delay' : (index / 10) + 's'
					});
				});
			// 配置弹出信息
				(function() {
					var init, isMobile, setupExamples, setupHero, _Drop
					_Drop = Drop.createContext({
						classPrefix : 'drop'
					});
					init = function() {
						return setupExamples();
					};
					setupExamples = function() {
						return $('.test').each(function() {
							var $example, $target, content, drop, openOn, theme;
							$example = $(this);
							theme = $example.data('theme');
							openOn = $example.data('open-on') || 'click';
							$target = $example.find('.drop-target');
							$target.addClass(theme);
							content = function() {
								return $('.drop-content').html();
							};
							for (var int = 0; int < $target.length; int++) {
								drop = new _Drop({
											target : $target[int],
											classes : theme,
											position : 'bottom right',
											constrainToWindow : true,
											constrainToScrollParent : false,
											openOn : openOn,
											content : content
										});
								// 设置弹出框内部信息
								drop.on("open", function() {
									var text = jQuery(this.target).attr("data-description");
									$(".description-c").html("<span class='word-size'>"+ text + "</span>");
									$(".content-title").html("<p class='word-title'>"
									+ jQuery(this.target).text() + "阶段</p>");
								});
							}
						});
					};
					init();
				}).call(this);
			}, getContextPath() + '/mgr/flow/getCurrectTask', $.toJSON({
				id : key
			}));
		}, getContextPath() + '/mgr/flow/getnodes', $.toJSON({
			id : key
		}));
	}
}
function resetTime(mode) {
	if (mode != 'curr') {
		$("dd[id^='et_']").html("");
		$("dd[id^='cu_']").html("");
		$("li[id^='stepword_']").html('未完成');
		$("li[id^='stepword_']").removeClass('timeout');
		$("li[id^='stepword_']").removeClass('doing');
	} else {
		$("li[id^='stepword_']").removeClass('timeout');
		$("li[id^='stepword_']").removeClass('doing');
	}
}
// 加载文件模块
function loadfiledata(more) {
	moreFile(false);
	var tab = $(".file-table");
	tab.html("");
	$("#loadmore-fileGIG").removeClass('hide');
	$("#more-file-btn").show();
	var key = getCurrentProject();
	if (key != null) {
		loadData(function(msg) {
			tab.html("");
			$("#loadmore-fileGIG").addClass('hide');
			if (msg.length == 0) {
				tab.html("<div class=\"file-div\"><img  class=\"nofile\" src=\"/resources/images/flow/nofile.png\"/></div>");
				$("#more-file-btn").hide();
			}
			for (var i = 0; i < msg.length; i++) {
				var name = msg[i].irOriginalName;
				var divRoot = $("<div class=\"file-div\"></div>");
				var fileName = name.lastIndexOf(".");
				var finalName = name.substring(fileName + 1);
				var src = '/resources/images/flow/';
				switch (finalName) {
					case 'doc' :
					case 'docx' :
						src += 'doc.png';
						break;
					case 'xls' :
					case 'xlsx' :
						src += 'xls.png';
						break;
					case 'ppt' :
					case 'pptx' :
						src += 'ppt.png';
						break;
					case 'pdf' :
						src += 'pdf.png';
						break;
					case 'txt' :
						src += 'txt.png';
						break;
					case 'avi' :
						src += 'avi.png';
						break;
					case 'esp' :
						src += 'esp.png';
						break;
					case 'jpg' :
						src += 'jpg.png';
						break;
					case 'mov' :
						src += 'mov.png';
						break;
					case 'mp3' :
						src += 'mp3.png';
						break;
					case 'mp4' :
						src += 'mp4.png';
						break;
					case 'png' :
						src += 'png.png';
						break;
					case 'rar' :
						src += 'rar.png';
						break;
					case 'wav' :
						src += 'wav.png';
						break;
					case 'zip' :
						src += 'zip.png';
						break;
					default :
						src += 'file.png';
						break;
				}
				var fileimg = $("<img class=\"img-icon\" id=\"img-icon-id\"src='"
						+ src + "'>");
				var div1 = $("<div class=\"file-icon div-table\"></div>");
				div1.append(fileimg);
				var div2 = $("	<div class=\"div-table-file-type\"><div class=\"file-name\">"
						+ msg[i].irOriginalName
						+ "</div><div class=\"file-type\">"
						+ msg[i].irtype
						+ "</div></div>");
				var userNameView = "";
				if (msg[i].userViewModel != null) {
					userNameView = msg[i].userViewModel.userName;
				} else {
					userNameView = "";
				}
				var lookA = $("<a href='javascript:void(0);' target='_blank' ></a>");
				var chakan = $("<img class=\"qrcode-img div-table img-look\""
						+ "src=\"/resources/images/flow/lookbg.png\" data-state='"
						+ msg[i].state + "'  data-url='" + msg[i].irId
						+ "'  id='chakan" + msg[i].irId + "'>");
				lookA.append(chakan);
				var fenxiang = $("<img class=\"qrcode-img div-table img-margin img-share \""
						+ "src=\"/resources/images/flow/sharebg.png\" data-state='"
						+ msg[i].state + "' data-url='" + msg[i].irId + "' />");
				var xiazai = $("<img src=\"/resources/images/flow/downloadbg.png\"/>");
				lookA.on("mouseenter", function() {
					jQuery(this).find("img").attr("src",
							'/resources/images/flow/lookbg.png');
				});
				lookA.on("mouseleave", function() {
					jQuery(this).find("img").attr("src",
							'/resources/images/flow/lookbg.png');
				});
				// chakan.unbind('click');
				chakan.bind('click', function() {
					var state = jQuery(this).attr("data-state");
					var fileId = jQuery(this).attr("data-url");
					var key = getCurrentProject();
					var a = jQuery(this).parent();
					var self = jQuery(this);
					a.unbind('click');
					switch (state) {
						case 'transformation' :
							syncLoadData(function(msg) {
								switch (msg.state) {
									case 'transformation' :
										a.bind('click', function() {
											return false;
										});
										a.attr('href',
												'javascript:void(0);');
										showAlert(errorTransformation);
										break;
									case 'finish' :
										var state = self.attr(
												"data-state", "finish");
										self.next().attr("data-state",
												"finish");
										jumpView(fileId, a);
										break;
									case 'fail' :
										break;
								}
							}, getContextPath()
							+ '/mgr/resource/get/state', $
							.toJSON({
								irId : fileId,
								irIndentId : key
							}));
							break;
						case 'finish' :
							jumpView(fileId, a);
							break;
						case 'fail' :
							break;
					}
				});
				fenxiang.on("mouseenter", function() {
					jQuery(this).attr("src",
							'/resources/images/flow/sharebg.png');
				});
				fenxiang.on("mouseleave", function() {
					jQuery(this).attr("src",
							'/resources/images/flow/sharebg.png');
				});
				fenxiang.on("click", function() {
					var self = jQuery(this);
					var state = jQuery(this).attr("data-state");
					var fileId = jQuery(this).attr("data-url");
					var key = getCurrentProject();
					var self = jQuery(this);
					switch (state) {
						case 'transformation' :
							syncLoadData(function(msg) {
								switch (msg.state) {
									case 'transformation' :
										showAlert(errorTransformation);
										break;
									case 'finish' :
										var state = self.attr(
												"data-state", "finish");

										self.prev().attr("data-state",
												"finish");
										jumpShare(fileId);
										break;
									case 'fail' :
										break;
								}
							}, getContextPath()+ '/mgr/resource/get/state', $.toJSON({
								irId : fileId,
								irIndentId : key
							}));
							break;
						case 'finish' :
							jumpShare(fileId);
							break;
						case 'fail' :
							break;
					}
				});
				xiazai.on("mouseenter", function() {
							jQuery(this).attr("src",
									'/resources/images/flow/downloadbg.png');
						});
				xiazai.on("mouseleave", function() {
							jQuery(this).attr("src",
									'/resources/images/flow/downloadbg.png');
						});

				var div3 = $("<div class=\"div-table \"><p class=\"file-user-name\">"
						+ userNameView
						+ "</p><p class=\"file-time\">上传于<strong>"
						+ msg[i].irCreateDate + "</strong></p></div>");
				var div4 = $("<div class=\"qrcode-td div-table\"></div>");
				div4.append(lookA);
				div4.append(fenxiang);
				var div5 = $("<div class=\"div-download\"></div>");
				var downloada = $("<a href=\"" + getContextPath()
						+ '/mgr/getDFSFile/' + msg[i].irId + "\"></a>");
				downloada.append(xiazai);
				div5.append(downloada);
				divRoot.append(div1);
				divRoot.append(div2);
				divRoot.append(div3);
				divRoot.append(div4);
				divRoot.append(div5);
				tab.append(divRoot);

				if (!more && i >= 4) {
					break;
				}
			}
			if (msg.length <= 4) {
				$('#more-file-btn').hide();
			} else {
				$('#more-file-btn').show();
			}
		}, getContextPath() + '/mgr/comment/getResourceList', $.toJSON({
			id : key
		}));
	}
}
function jumpView(fileId, a) {
	// var link= getHostName() + getContextPath();
	var link = getDfsHostName();
	syncLoadData(function(msg) {
		// link+="/portal/project/doc/"+msg.url;
		link += msg.url;
		a.attr("href", link);
	}, getContextPath() + '/mgr/doc/getDocView', $.toJSON({
		irId : fileId
	}));
}
function jumpShare(fileId) {
	syncLoadData(function(msg) {
		var fileName = msg.url;
		var extName = fileName.substring(fileName.lastIndexOf(".") + 1,
				fileName.length);
		// var link= getHostName() + getContextPath();
		var link = getDfsHostName();
		/*
		 * if(extName=='mp4'){ link+="/mgr/doc/video/"+msg.url; }else{
		 * link+="/portal/project/doc/"+msg.url; }
		 */
		link += msg.url;
		$('#share-open').click();
		share.init(link, 'hehe', getContextPath()
						+ '/resources/banner/flex1.jsp');
	}, getContextPath() + '/mgr/doc/getDocView', $.toJSON({
		irId : fileId
	}));
}
// 加载评论模块
function loadcommentdata(more) {
	moreComment(false);
	var tab = $(".message-table");
	tab.html("");
	$("#loadHeight").addClass('loadHeight');
	var key = getCurrentProject();
	$("#loadmore-CommentGIF").removeClass('hide');
	if(key != null ){
		loadData(
				function(msg) {
					tab.html("");
					$("#loadmore-CommentGIF").addClass('hide');
					$("#loadHeight").removeClass('loadHeight');
					if(msg.length==0){
						tab.html(" <img  class=\"nomessage\" src=\"/resources/images/flow/nomessage.png\"/>");
						$(".more-comment").hide();
					}
					for (var i = 0; i < msg.length; i++) {
						var tr = $("<tr></tr>");
						var imgx=$("<img class=\"message-portrait-img\""
								+ " src=\"/resources/images/flow/file.png\">");
						var td1 = $("<td class=\"message-portrait\" rowspan=\"2\"></td>");

						var user = msg[i].userViewModel;
						var text = "未知";
						if (user != null) {
							if(user.imgUrl != undefined && user.imgUrl != '') {
								if(user.imgUrl.indexOf('resources/images') < 0) {
									user.imgUrl = getDfsHostName() + user.imgUrl;
								}
							}
							imgx.attr("src", user.imgUrl);
							text = user.userName;
						}
						td1.append(imgx);
						var td2 = $("<td><label class=\"msg-comm-name\">"
								+ text
								+ "</label><label class=\"msg-comm-time\">"+msg[i].icCreateDate.split(' ')[0]+"</label></td>");
						tr.append(td1);
						tr.append(td2);
						tab.append(tr);

						var tr2 = $("<tr></tr>");
						var td2_1 = ("<td>" + msg[i].icContent + "</tf>");
						tr2.append(td2_1);

						tab.append(tr2);

						if (!more && i == 2)
							break;
					}
					if(msg.length <= 2){
						$('#more-comment').hide();
					}else{
						$('#more-comment').show();
					}
				}, getContextPath() + '/mgr/comment/getAllComment', $.toJSON({
					id : key
			}));
	}
}
var initClick = true;
var hasRunPro = false;
var btnState = -1;
// 加载项目列表
function loadprojecctlist() {
	loadData(function(msg) {
		// 获取页面控件
		// -----------------------------init--------------------------
		var doing = $("#myProjectId");
		var help = $("#helpProjectId");
		var pause = $("#pauseProjectId");
		var history = $("#historyProjectId");
		// 清空控件内容
		doing.html('');
		help.html('');
		pause.html('');
		history.html('');

		var type = $("#type").val();
		var userId = $('#userId').val();
		// --------------------------------------分组数据------------------------------------
		if (msg != null && msg.length > 0) {
			for (var i = 0; i < msg.length; i++) {

				// 构造控件
				var liStar = $('<li></li>')
				var a = $('<a class="indent-a title-content" data-state='
						+ msg[i].state
						+ ' data-value="'
						+ msg[i].id
						+ '">' + msg[i].projectName + '</a>');
				// 绑定点击事件
				$(a).on("click", function() {
					firstClick = true;
					// 取出ID
					var key = $(this).attr("data-value");
					// 设置当前选中项目ID
					putCurrentProject(key);
					var state = jQuery(this).attr('data-state');
					loadprojecctlist();
					ControlPay.closeList();
					var type = $("#type").val();
					if (type == "customer") {
						checkHasList();
						checkHasNoPayList();
					}
						// if(type=="employee"){
						// checkHasListForEm();
						// }
					});
				liStar.append(a);

				switch (msg[i].state) {
					case 0 : // --- 正常
						// 过滤协同项目
						if (type == "employee") {
							if (userId == msg[i].userId) {
								doing.append(liStar);
							}
						} else {
							doing.append(liStar);
						}
						if (msg[i].id == getCurrentProject()) {
							btnState = 0;
						}
						break;
					case 1 : // --- 完成
					case 2 : // --- 取消
						history.append(liStar);
						if (msg[i].id == getCurrentProject()) {
							btnState = 2;
						}
						break;
					case 3 : // --- 暂停
						pause.append(liStar);
						if (msg[i].id == getCurrentProject()) {
							btnState = 1;
						}
						break;
				}
			}
		}
		// 数据分发完成 ---》根据角色调整view

		switch (type) {
			case 'customer' :
				var pauseLi = pause.children('li');
				$(doing).append(pauseLi);
				break;
			case 'provider' :
				var pauseLi = $(pause).children('li');
				$(doing).append(pauseLi);
				break;
			case 'employee' :
				loadSynerhyList();
				break;
		}
		if (initClick) {
			initClick = false;
			// 加载第一个项目
			if (doing.children('li').length > 0) {
				hasRunPro = true;
				$(doing.children('li')[0]).find('a').click();
				btnState = 0;
			} else if (pause.children('li').length > 0) {
				hasRunPro = true;
				$(pause.children('li')[0]).find('a').click();
				btnState = 1;
			} else if (help.children('li').length > 0) {
				hasRunPro = true;
				$(help.children('li')[0]).find('a').click();
				btnState = 2;
			}
		}
		if (hasRunPro && !initClick) {
			$(".noproject").addClass('hide');
			$(".right-page").removeClass('hide');
		} else {
			$(".right-page").addClass('hide');
			$(".noproject").removeClass('hide');
			$(".noproject").addClass('set-width');
			// $(".indentlisthistory").show();
			// $(".indent-more-add").removeClass('circle-180');
		}
		if (btnState == 0) {
			show(true);
		} else if (btnState == 1) {
			show(false);
		} else if (btnState == 2) {
			finish();
		}
		resetTime('');
		loadflowdata();
		loadfiledata(false);
		loadcommentdata(false);
		loadIndentInfo();
		updateProjectTreeView();
	}, getContextPath() + '/mgr/projects/all-project', $.toJSON({}));
}
// 加载项目列表视图
function updateProjectTreeView() {
	$("#menuId ul li a").each(function(index, item) {
		var num = jQuery(item).attr("data-value");
		var key = getCurrentProject();
		if (key != null) {
			if (num == key) {
				$(item).addClass('indent-selected');
				$(item)
						.prepend('<label class="border-select"></label>');
			} else {
				$(item).removeClass("class", "indent-selected");
			}
		}
	});
	$(".menu ul li").menu({
		autohide : 0
	});
	if (countCheck == 0) {
		$("#doingProject").click();
		$("#myProjectId").slideDown();
		countCheck++;
	}
}
// 加载项目基础信息
function loadIndentInfo() {
	var key = getCurrentProject();
	if (key != null && key != undefined && key != '') {
		loadData(function(msg) {
			var projectId = $(".projectId");
			var projectName = $(".projectName");
			var userName = $(".userName");
			var teamName = $(".teamName");
			var userContact = $(".userContact");
			var teamContact = $(".teamContact");
			var userPhone = $(".userPhone");
			var teamPhone = $(".teamPhone");
			var viedoPrice = $(".viedoPrice");
			var description = $(".indent-area");
			// clear
			$(projectId).text("");
			$(projectName).text("");
			$(userName).text("");
			$(teamName).text("");
			$(teamContact).text("");
			$(userContact).text("");
			$(userPhone).text("");
			$(teamPhone).text("");
			$(viedoPrice).text("");
			// assignment
			$(projectId).text(msg.serial);
			$(projectName).text(msg.projectName);
			$(userName).text(msg.userName);
			$(teamName).text(msg.teamName);
			$(teamContact).text(msg.teamContact);
			$(userContact).text(msg.userContact);
			$(userPhone).text(msg.userPhone);
			$(teamPhone).text(msg.teamPhone);
			// 暂时显示预计价格
			$(viedoPrice).text(msg.priceFirst + '~' + msg.priceLast
					+ '元');

			$(description).text(msg.description);

		}, getContextPath() + '/mgr/projects/get-projectInfo', $.toJSON({
			id : key
		}));
	}
}
// 添加协同人
function loadSynerhyList() {
	var key = getCurrentProject();
	syncLoadData(function(msg) {
		if (msg.length <= 0) {
			$('#page').removeClass('hide');
		} else {
			$('#page').removeClass('hide');
			var help = $("#helpProjectId");
			help.html('');
			for (var i = 0; i < msg.length; i++) {
				if (msg[i].state == 0) {
					var liStar = $('<li></li>')
					var a = $('<a class="indent-a title-content" data-state='
							+ msg[i].state
							+ ' data-value="'
							+ msg[i].id
							+ '">'
							+ msg[i].projectName
							+ '</a>');
					$(a).on("click", function() {
						firstClick = true;
						var key = $(this).attr("data-value");
						putCurrentProject(key);
						var state = jQuery(this)
								.attr('data-state');
						loadprojecctlist();
					});
					liStar.append(a);
					help.append(liStar);
					if (msg[i].id == getCurrentProject()) {
						show(false);
					}
				}
			}
		}
	}, getContextPath() + '/mgr/projects/get/synergys', $.toJSON({
		id : key
	}));
}
// 完成项目页面样式
function finish() {
	$("#btndiv-id").hide();
	$("#upload-info-btn-id").hide();
	$("#upload-file-btn-id").hide();
	//$(".comment").hide();
	//$(".comment-btn").hide();
	moreFile(false);	moreComment(false);
	$("#Online").addClass('hide');
	$("#Outline").addClass('hide');
	// $('#managerId').removeClass('hide');
	// $('#cusId').removeClass('hide');
	$('#userContentId').addClass('hide');
	
	$('.noproject').addClass('hide');
	$('.right-page').removeClass('hide');
	isHistory = true;
}
// 未完成项目样式
function show(isSynergy) {
	$("#btndiv-id").show();
	if (isSynergy) {
		$("#upload-info-btn-id").show();
	} else {
		$("#upload-info-btn-id").hide();
	}
	$("#upload-file-btn-id").show();
	$(".comment").show();
	$(".comment-btn").show();
	$('#userContentId').removeClass('hide');
	isHistory = false;
}
// 获取当前进行中的项目 cookie-->currentproject
function getCurrentProject() {
	// return $.cookie('currentproject');

	var service_val = $("#service-key").val();
	if (service_val == null || service_val == '') {
		return null;
	} else {
		return service_val;
	}
}
// 设置当前项目
function putCurrentProject(key) {
	// $.cookie("currentproject", key + '');
	$("#service-key").val(key + "");
}
/**
 * 
 * 流程相关回掉实现
 */
// 点击
function mycall(restult) {
	// alert("mycall"+result.value+":"+result.text);
	// StepTool.drawStep(result.value, stepListJson);
}
// 移入
function mouseenter(restult) {
	// console.log("in" + restult.value);
}
// 移出
function mouseleave(restult) {
	// console.log("out" + restult.value);
}
// 获取当前时间
function getCurrentTime() {
	var myDate = new Date();
	var year = myDate.getFullYear();
	var month = myDate.getMonth() + 1;
	var day = myDate.getDate();
	return year + '-' + (month < 10 ? '0' + month : month) + '-' + day;
}
// 时间比较
function dateCompare(date1, date2) {
	var time1 = date1.replace(/-/g, "/");
	var date1 = new Date(time1);
	var time2 = date2.replace(/-/g, "/");
	var date2 = new Date(time2);

	if (date1.getTime() > date2.getTime())
		return true;
	else
		return false;
}
var errorTransformation = '文档转换中，请稍后！';
var errorNotNull = '输入内容不能为空';
var errorNotNull = '输入内容不能为空';
function showAlert(message) {
	$(".check-message").text(message);
	$('#toolbar-no-message').modal({
		backdrop : 'static',
		keyboard : false
	});
	// $("#toolbar-no-message").modal('show');
}
// add by guoyang, 2016-04-19 03:14 begin
// -> 添加进度条显示
// TODO 文件上传进度方法删除了，之后都改为 百度的上传控件，使用FastDFS存储
function getProgress() {
	var now = new Date();
	loadData(function(data) {
		var progress = Number((data.pBytesRead / data.pContentLength)
				* 100).toFixed(0)
				+ '%';
		$('.progress-bar-success').text('已完成' + progress)
		$('.progress-bar-success').attr('aria-valuenow', progress).css(
			{
				"width" : progress
			});
	}, getContextPath() + '/upfile/progress', now.getTime());
}
// add by guoyang, 2016-04-19 03:14 end
Array.prototype.contains = function(element) {
	for (var i = 0; i < this.length; i++) {
		if (this[i] == element) {
			return true;
		}
	}
	return false;
}
// add by lt, 2016-05-11 14:56 begin
// -> 添加hover显示时间
function showOrderTime() {
	$('#stepword_gt').mouseover(function() {
		$('#div_gt').removeClass('opacity-li');
	});
	$('#stepword_gt').mouseout(function() {
		$('#div_gt').addClass('opacity-li');
	});
	$('#stepword_fa').mouseover(function() {
		$('#div_fa').removeClass('opacity-li');
	});
	$('#stepword_fa').mouseout(function() {
		$('#div_fa').addClass('opacity-li');
	});
	$('#stepword_sw').mouseover(function() {
		$('#div_sw').removeClass('opacity-li');
	});
	$('#stepword_sw').mouseout(function() {
		$('#div_sw').addClass('opacity-li');
	});
	$('#stepword_zz').mouseover(function() {
		$('#div_zz').removeClass('opacity-li');
	});
	$('#stepword_zz').mouseout(function() {
		$('#div_zz').addClass('opacity-li');
	});
	$('#stepword_jf').mouseover(function() {
		$('#div_jf').removeClass('opacity-li');
	});
	$('#stepword_jf').mouseout(function() {
		$('#div_jf').addClass('opacity-li');
	});
}

// 支付
var ControlPay = {
	clickOnLine : function() {
		$('#Online').on('click', function() {
			ControlPay.showOnlineInfo();
		});
	},
	showOnlineInfo : function() {
		// $('#payInfo').slideDown();
		$('#copyListSuccess').addClass('hide');
		$('#toolbar-OnOff').modal({
			backdrop : 'static',
			keyboard : false
		});
		ControlPay.initOnlineInfo();
	},

	initOnlineInfo : function() {
		var mydate = new Date();
		var t = formatterDateTime(mydate);
		// var t=mydate.toLocaleString();
		$('#checkWay').val("1");
		$("#payOrder").text('支付单号');
		$('#Online').addClass('pay-click-btn');
		$('#Online').removeClass('pay-btn');
		$('#Outline').removeClass('pay-click-btn');
		$('#Outline').addClass('pay-btn');
		$('#pay-time').text('发起收款时间');
		$('#pay-people').text('付款方');
		$('#OnlineInfo').removeClass('hide');
		$('#payTime-online').removeClass('hide');
		$('#payTime-online').val(t);
		$('#payTime-outline').addClass('hide');
		$('#order-online').removeClass('hide');
		$('#order-outline').addClass('hide');
		$('#pay-sure').text('确认');
		$('#link').addClass('hide');
		$('#payTitleId').text('收款信息');
		ControlPay.initBillNo();
		checkPayList.checkOnBlur();
	},
	initBillNo : function() {
		var key = getCurrentProject();
		loadData(function(msg) {
			$("#order-online").val(msg.billNo);
			$("#projectName").val(msg.projectName);
			$("#cusName").val(msg.userName);
			$("#payMoney").val(msg.payPrice);
		}, getContextPath() + '/pay/get/billno', $.toJSON({
			projectId : key
		}));
	},
	clickOutLine : function() {
		$('#Outline').on('click', function() {
			ControlPay.showOutlineInfo();
		});
	},
	showOutlineInfo : function() {
		// $('#payInfo').slideDown();
		$('#copyListSuccess').addClass('hide');
		$('#toolbar-OnOff').modal({
					backdrop : 'static',
					keyboard : false
				});
		// $("#toolbar-OnOff").modal('show');
		ControlPay.initOutlineInfo();
	},
	initOutlineInfo : function() {
		$('#checkWay').val("2");
		$("#payOrder").text('银行流水号');
		$('#Outline').addClass('pay-click-btn');
		$('#Outline').removeClass('pay-btn');
		$('#Online').removeClass('pay-click-btn');
		$('#Online').addClass('pay-btn');
		$('#pay-time').text('收款时间');
		$('#pay-people').text('客户名称');
		$('#OnlineInfo').removeClass('hide');
		$('#payTime-online').addClass('hide');
		$('#payTime-outline').removeClass('hide');
		$('#order-online').addClass('hide');
		$('#order-outline').removeClass('hide');
		$('#pay-sure').text('确认');
		$('#link').addClass('hide');
		$('#payTitleId').text('收款信息');
		$('#payTime-outline').datepicker({
					language : 'zh',
					dateFormat : 'yyyy-MM-dd ',
					timeFormat : 'hh:mm:ss',
					minDate : 0
				});
		ControlPay.initBillNo();
		checkPayList.checkOutBlur();
	},
	clickpay : function() {
		$('#pay-sure').on('click', function() {
					ControlPay.initPayInfo();
		});
	},
	// TODO:
	initPayInfo : function() {
		var check = $('#checkWay').val();
		if (check == "1") {
			var orderId = $("#order-online").val().trim();
			var projectName = $("#projectName").val().trim();
			var cusName = $("#cusName").val().trim();
			var payMoney = $("#payMoney").val().trim();
			var projectId = getCurrentProject();
			// 发起线上支付
			if (checkPayList.checkOnLinePayList()) {
				loadData(function(msg) {
					if (msg.errorCode == 200) {
						var url = msg.result;
						$("#shareLink").val(getHostName() + url);
						$('#pay-sure').text('关闭');
						$('#checkWay').val('3');
						$('#OnlineInfo').addClass('hide');
						$('#link').removeClass('hide');
						$('#payTitleId').text('链接信息');
						ZeroClipboard.config({
									hoverClass : "hand"
						});
						var client = new ZeroClipboard($("#copyLink"));
						client.on("copy", function(e) {
							$('#copyListSuccess').removeClass('hide');
						});
						$('#loadEmployee').removeClass('hide');
						$('#payHistory').removeClass('hide');
						if ($('#payHistory').hasClass('payBtnPosClick')) {
							payList();
						}
						checkHasListForEmFirst();
					} else {
						// alert("出错啦"+msg.errorCode);
					}
				}, getContextPath() + '/pay/sendpay', $.toJSON({
					billNo : orderId,
					projectName : projectName,
					userName : cusName,
					payPrice : payMoney,
					projectId : projectId
				}));
			}
		} else if (check == "2") {
			var key = getCurrentProject();
			var outlineTime = $("#payTime-outline").val().trim();
			var orderOutLine = $("#order-outline").val().trim();
			var projectName = $("#projectName").val().trim();
			var cusName = $("#cusName").val().trim();
			var payMoney = $("#payMoney").val().trim();

			$('#loadEmployee').removeClass('hide');
			$('#payHistory').removeClass('hide');
			if (checkPayList.checkOutLinePayList()) {
				loadData(function(msg) {
					if (msg.errorCode == 200) {
						ControlPay.openHistory();
						payList();

					} else {
						// alert("出错啦"+msg.errorCode);
					}
				}, getContextPath() + '/pay/offline/save', $.toJSON({
					projectId : key,
					payTime : outlineTime,
					unOrderId : orderOutLine,
					projectName : projectName,
					userName : cusName,
					payPrice : payMoney
				}));
			}
		} else if (check == "3") {
			$('#pay-sure').text('确认');
			$('#checkWay').val('1');
			$('#OnlineInfo').removeClass('hide');
			$('#link').addClass('hide');
			$('#toolbar-OnOff').modal('hide');
		}
		
		$('#Online').removeClass('pay-click-btn');
		$('#Online').addClass('pay-btn');
		$('#Outline').removeClass('pay-click-btn');
		$('#Outline').addClass('pay-btn');
	},
	clickPayOpenHistory : function() {
		// 立即前往
		$('#openHistory').on('click', function() {
			if ($('#payHistoryBtnOrder').hasClass('payBtnPosClick')) {
				ControlPay.closeList();
			} else {
				ControlPay.openHistory();
				$("#payHistoryBtnOrder").addClass('payBtnPosClick');
				var base_Card = $("div[class^=payCard]");
				payList();
			}
		});
		// 管家历史按钮
		$('#payHistory').on('click', function() {
			if ($(this).hasClass('payBtnPosClick')) {
				ControlPay.closeList();
			} else {
				ControlPay.openHistory();
				$("#payHistory").addClass('payBtnPosClick');
				var base_Card = $("div[class^=payId]");
				payList();
			}
		});
		// 管家展开按钮
		$('#loadEmployee').on('click', function() {
			if ($("#payHistory").hasClass('payBtnPosClick')) {
				ControlPay.closeList();
			} else {
				ControlPay.openHistory();
				$("#payHistory").addClass('payBtnPosClick');
				var base_Card = $("div[class^=payId]");
				payList();
			}
		});
		// 客户历史按钮
		$('#payHistoryBtnOrder').on('click', function() {
			if ($(this).hasClass('payBtnPosClick')) {
				ControlPay.closeList();
			} else {
				ControlPay.openHistory();
				$("#payHistoryBtnOrder").addClass('payBtnPosClick');
				var base_Card = $("div[class^=payCard]");
				payList();
			}
		});
		// 客户展开按钮
		$('#loadCus').on('click', function() {
			if ($("#payHistoryBtnOrder").hasClass('payBtnPosClick')) {
				ControlPay.closeList();
			} else {
				ControlPay.openHistory();
				$("#payHistoryBtnOrder").addClass('payBtnPosClick');
				var base_Card = $("div[class^=payId]");
				payList();
			}
		});
	},
	closeList : function() {
		$("#payHistoryList").slideUp();
		$('#payHistory').removeClass('payBtnPosClick');
		$('#payHistoryBtnOrder').removeClass('payBtnPosClick');
		$("#payListPage").html('');
		$("#loadWordEmployee").text('展开更多');
		$("#circleEmployeeImg").removeClass('circle-180');
		$("#loadWordCus").text('展开更多');
		$("#circleCusImg").removeClass('circle-180');
	},
	openHistory : function() {
		$('#listLoad').show();
		$("#payHistoryList").slideDown();
		$('#toolbar-OnOff').modal('hide');
		$("#loadWordEmployee").text('收起');
		$("#circleEmployeeImg").addClass('circle-180');
		$("#loadWordCus").text('收起');
		$("#circleCusImg").addClass('circle-180');
	},
	copyLink : function() {
		$('#copyLink').on('click', function() {
		});
	},
	closeMore : function() {
		$("#canclePay").on('click', function() {
			$('#toolbar-OnOff').modal('hide');
			$('#Online').removeClass('pay-click-btn');
			$('#Online').addClass('pay-btn');
			$('#Outline').removeClass('pay-click-btn');
			$('#Outline').addClass('pay-btn');
		});
	},
	initControlPay : function() {
		ControlPay.clickOnLine();
		ControlPay.clickOutLine();
		ControlPay.clickpay();
		ControlPay.clickPayOpenHistory();
		ControlPay.copyLink();
		ControlPay.closeMore();
	}
}
var checkPayList = {
	// 支付验证线下
	checkOutLinePayList : function() {
		var payTime = $('#payTime-outline');
		var payorder = $('#order-outline');
		var projectName = $('#projectName');
		var cusName = $('#cusName');
		var payMoney = $('#payMoney');
		var payTimeError = $('#payTime-outlineError');
		var payorderError = $('#order-outlineError');
		var projectNameError = $('#projectNameError');
		var cusNameError = $('#cusNameError');
		var payMoneyError = $('#payMoneyError');
		var payTimeDiv = $('#payTime-outlineDiv');
		var payorderDiv = $('#order-outlineDiv');
		var projectNameDiv = $('#projectNameDiv');
		var cusNameDiv = $('#cusNameDiv');
		var payMoneyDiv = $('#payMoneyDiv');
		if (payTime.val() == '' || payTime.val() == null) {
			payTime.focus();
			payTimeDiv.addClass('has-error');
			payTimeError.removeClass('hide');
			payTimeError.text('请填写时间');
			return false;
		} else {
			payTimeDiv.removeClass('has-error');
			payTimeError.addClass('hide');
		}
		if (payorder.val() == '' || payorder.val() == null) {
			payorder.focus();
			payorderDiv.addClass('has-error');
			payorderError.removeClass('hide');
			payorderError.text('请填写单号');
			return false;
		} else {
			payorderDiv.removeClass('has-error');
			payorderError.addClass('hide');
		}
		if (projectName.val() == '' || projectName.val() == null) {
			projectName.focus();
			projectNameDiv.addClass('has-error');
			projectNameError.removeClass('hide');
			projectNameError.text('请填写项目名');
			return false;
		} else {
			projectNameDiv.removeClass('has-error');
			projectNameError.addClass('hide');
		}
		if (cusName.val() == '' || cusName.val() == null) {
			cusName.focus();
			cusNameDiv.addClass('has-error');
			cusNameError.removeClass('hide');
			cusNameError.text('请填写客户名称');
			return false;
		} else {
			cusNameDiv.removeClass('has-error');
			cusNameError.addClass('hide');
		}
		if (payMoney.val() == '' || payMoney.val() == null) {
			payMoney.focus();
			payMoneyDiv.addClass('has-error');
			payMoneyError.removeClass('hide');
			payMoneyError.text('请填写支付金额');
			return false;
		} else if (!checkNumber(payMoney.val())) {
			payMoney.focus();
			payMoneyDiv.addClass('has-error');
			payMoneyError.removeClass('hide');
			payMoneyError.text('请输入数字');
			return false;
		}else {
			payMoneyDiv.removeClass('has-error');
			payMoneyError.addClass('hide');
		}
		return true;
	},
	checkOutBlur : function() {
		var payTime = $('#payTime-outline');
		var payorder = $('#order-outline');
		var projectName = $('#projectName');
		var cusName = $('#cusName');
		var payMoney = $('#payMoney');
		var payTimeError = $('#payTime-outlineError');
		var payorderError = $('#order-outlineError');
		var projectNameError = $('#projectNameError');
		var cusNameError = $('#cusNameError');
		var payMoneyError = $('#payMoneyError');
		var payTimeDiv = $('#payTime-outlineDiv');
		var payorderDiv = $('#order-outlineDiv');
		var projectNameDiv = $('#projectNameDiv');
		var cusNameDiv = $('#cusNameDiv');
		var payMoneyDiv = $('#payMoneyDiv');
		$(payTime).on('blur', function() {
			payTimeDiv.removeClass('has-error');
			payTimeError.addClass('hide');
		});
		$(payorder).on('blur', function() {
			payorderDiv.removeClass('has-error');
			payorderError.addClass('hide');
		});
		$(projectName).on('blur', function() {
			projectNameDiv.removeClass('has-error');
			projectNameError.addClass('hide');
		});
		$(cusName).on('blur', function() {
			cusNameDiv.removeClass('has-error');
			cusNameError.addClass('hide');
		});
		$(payMoney).on('blur', function() {
			payMoneyDiv.removeClass('has-error');
			payMoneyError.addClass('hide');
		});
	},
	checkOnBlur : function() {
		var projectName = $('#projectName');
		var cusName = $('#cusName');
		var payMoney = $('#payMoney');
		var projectNameError = $('#projectNameError');
		var cusNameError = $('#cusNameError');
		var payMoneyError = $('#payMoneyError');
		var projectNameDiv = $('#projectNameDiv');
		var cusNameDiv = $('#cusNameDiv');
		var payMoneyDiv = $('#payMoneyDiv');
		$(projectName).on('blur', function() {
			projectNameDiv.removeClass('has-error');
			projectNameError.addClass('hide');
		});
		$(cusName).on('blur', function() {
			cusNameDiv.removeClass('has-error');
			cusNameError.addClass('hide');
		});
		$(payMoney).on('blur', function() {
			payMoneyDiv.removeClass('has-error');
			payMoneyError.addClass('hide');
		});
	},
	// 线上
	checkOnLinePayList : function() {

		var projectName = $('#projectName');
		var cusName = $('#cusName');
		var payMoney = $('#payMoney');

		var projectNameError = $('#projectNameError');
		var cusNameError = $('#cusNameError');
		var payMoneyError = $('#payMoneyError');

		var projectNameDiv = $('#projectNameDiv');
		var cusNameDiv = $('#cusNameDiv');
		var payMoneyDiv = $('#payMoneyDiv');

		if (projectName.val() == '' || projectName.val() == null) {
			projectName.focus();
			projectNameDiv.addClass('has-error');
			projectNameError.removeClass('hide');
			projectNameError.text('请填写项目名');
			return false;
		} else {
			projectNameDiv.removeClass('has-error');
			projectNameError.addClass('hide');
		}
		if (cusName.val() == '' || cusName.val() == null) {
			cusName.focus();
			cusNameDiv.addClass('has-error');
			cusNameError.removeClass('hide');
			cusNameError.text('请填写付款方');
			return false;
		} else {
			cusNameDiv.removeClass('has-error');
			cusNameError.addClass('hide');
		}
		if (payMoney.val() == '' || payMoney.val() == null) {
			payMoney.focus();
			payMoneyDiv.addClass('has-error');
			payMoneyError.removeClass('hide');
			payMoneyError.text('请填写支付金额');
			return false;
		} else if (!checkNumber(payMoney.val())) {
			payMoney.focus();
			payMoneyDiv.addClass('has-error');
			payMoneyError.removeClass('hide');
			payMoneyError.text('请输入数字');
			return false;
		} else {
			payMoneyDiv.removeClass('has-error');
			payMoneyError.addClass('hide');
		}
		return true;
	}

}
function payList() {
	var listnode = $("#payListPage");
	listnode.html('');
	var key = getCurrentProject();
	loadData(function(msg) {
		if (msg != null) {
			msg.forEach(function(deal) {
				var backgruond = "";
				var btn_shareLink = "";
				var left_time = "";
				var right_time = "";
				var btn_goClose = "";
				var off_btnTime = "";
				var type = $("#type").val();
				switch (deal.dealStatus) {
					case 0 : // 正常
						backgruond = '	<div class="payCard-info backgroundWait">';
						if (type == "customer") {
							btn_shareLink = '<button class="info-btn red-btn" name="toPay" data-token="'
									+ deal.token + '">去支付</button>';
						} else {
							btn_shareLink = '<button class="info-btn red-btn" name="toShare" data-token="'
									+ deal.token + '">分享支付链接</button>';
							btn_goClose = '<button class="info-close gray-btn" name="toClose" data-token="'
									+ deal.token + '">关闭订单</button>';
						}

						off_btnTime = '<li><div class="contentTitle">付款时间</div><div class="contentWord">'
								+ deal.payTime + '</div></li>';
						left_time = '<li><div class="contentTitle">发起时间</div><div class="contentWord">'
								+ deal.createTime + '</div></li>';
						right_time = '<li><div class="contentTitle">逾期时间</div><div class="contentWord">'
								+ deal.orderTimeOut + '</div></li>';
						break;
					case 1 : // 完成
						backgruond = '	<div class="payCard-info backgroundFinish">';
						left_time = '<li><div class="contentTitle">发起时间</div><div class="contentWord">'
								+ deal.createTime + '</div></li>';
						off_btnTime = '<li><div class="contentTitle">付款时间</div><div class="contentWord">'
								+ deal.payTime + '</div></li>';
						break;
					case 2 : // 支付关闭
						backgruond = '	<div class="payCard-info backgroundFail">';
						left_time = '<li><div class="contentTitle">发起时间</div><div class="contentWord">'
								+ deal.createTime + '</div></li>';
						right_time = '<li><div class="contentTitle">失败时间</div><div class="contentWord">'
								+ deal.orderTimeOut + '</div></li>';
						off_btnTime = '<li><div class="contentTitle">付款时间</div><div class="contentWord">'
								+ deal.payTime + '</div></li>';
						break;
				}

				if (deal.dealLogSource == 0) {
					// 线上
					var $body = '<div class="payId payCard">'
							+ '	<div class="payCard-top">'
							+ '		<div class="cardLeftStatue payInline">线上支付</div>'
							+ '	</div>';
					$body += backgruond;
					$body += '		<div class="info-left">'
							+ '			<div class="infoTitle" id="project">'
							+ deal.projectName + '</div>';
					$body += btn_shareLink;
					$body += btn_goClose;
					$body += '		</div>'
							+ '		<div class="info-right">'
							+ '			<ul class="payInline">'
							+ '				<li><div class="contentTitle">支付方</div><div class="contentWord ">'
							+ deal.userName
							+ '</div></li>'
							+ '				<li><div class="contentTitle">支付金额</div><div class="contentWord " >'
							+ deal.payPrice + '元</div></li>';
					$body += left_time;
					$body += '			</ul>'
							+ '			<ul class="rightUl payInline">'
							+ '				<li><div class="contentTitle">收款方</div><div class="contentWord order">'
							+ deal.proceedsSide
							+ '</div></li>'
							+ '				<li><div class="contentTitle ">订单号</div><div class="contentWord order">'
							+ deal.billNo + '</div></li>';
					$body += right_time;
					$body += '			</ul>' + '		</div>' + '	</div>' + '</div>';
				} else {
					// 线下
					var $body = '<div class="payId payCard">'
							+ '	<div class="payCard-top">'
							+ '		<div class="cardLeftStatue payInline">线下支付</div>'
							+ '	</div>';
					$body += backgruond;
					$body += '		<div class="info-left">'
							+ '			<div class="infoTitle" id="project">'
							+ deal.projectName + '</div>';
					$body += '		</div>'
							+ '		<div class="info-right">'
							+ '			<ul class="payInline">'
							+ '				<li><div class="contentTitle">支付方</div><div class="contentWord">'
							+ deal.userName
							+ '</div></li>'
							+ '				<li><div class="contentTitle">支付金额</div><div class="contentWord">'
							+ deal.payPrice + '元</div></li>';
					$body += off_btnTime;
					$body += '			</ul>'
							+ '			<ul class="rightUl payInline">'
							+ '				<li><div class="contentTitle">收款方</div><div class="contentWord">'
							+ deal.proceedsSide
							+ '</div></li>'
							+ '				<li><div class="contentTitle ">银行流水号</div><div class="contentWord order">'
							+ deal.billNo + '</div></li>';
					$body += '			</ul>' + '		</div>' + '	</div>' + '</div>';
				}
				listnode.append($body);
				ZeroClipboard.config({
					hoverClass : "hand"
				});
				var client = new ZeroClipboard($("#toShare"));
				$('#listLoad').hide();
			});
		}
		toShare();
		toPay();
		toClose();
		clickLink();
	}, getContextPath() + '/pay/get/deallogs', $.toJSON({
		projectId : key
	}));

}

function toShare() {
	var deleteSynergys = $("[name^=toShare]");
	deleteSynergys.off('click');
	var cout = deleteSynergys.length;
	deleteSynergys.on('click', function() {
		$('#copySuccess').addClass('hide');
		var token = $(this).attr("data-token");
		getData(function(msg) {
			if (msg.errorCode == 200) {
				$('#shareLinkList').val('https://www.apaipian.com:8446'
				+ msg.result);
				ZeroClipboard.config({
							hoverClass : "handShare"
				});
				var clientShare = new ZeroClipboard($("#copyShareLink"));
				clientShare.on("copy", function(e) {
							$('#copySuccess').removeClass('hide');
				});
				$('#toolbar-share').modal({
							backdrop : 'static',
							keyboard : false
				});
			} else {
				// alert(msg.errorMsg);
			}
		}, getContextPath() + '/pay/shareurl?token=' + token);
	});
}

function toPay() {
	var deleteSynergys = $("[name^=toPay]");
	deleteSynergys.off('click');
	var cout = deleteSynergys.length;
	deleteSynergys.on('click', function() {
		var token = $(this).attr("data-token");
		getData(function(msg) {
					if (msg.errorCode == 200) {
						var url = getHostName() + msg.result;
						// window.location.href = url;
						var a = document.createElement("a");
						a.setAttribute("href", url);
						a.setAttribute("target", "_blank");
						a.setAttribute("class", "hide");
						document.body.appendChild(a);
						a.click();
						a.remove();
					} else {
						// alert(msg.errorMsg);
					}
		}, getContextPath() + '/pay/shareurl?token=' + token);
	});
}

function toClose() {
	var deleteSynergys = $("[name^=toClose]");

	deleteSynergys.off('click');
	var cout = deleteSynergys.length;
	deleteSynergys.on('click', function() {
		var token = $(this).attr("data-token");
		toCheckListClose(token);
	});
}
// TODO:
function checkHasList() {
	var key = getCurrentProject();
	loadData(function(msg) {
		if (msg.errorCode == 200) {
			if (msg.result > 0) {
				$('#cusId').removeClass('hide');
			} else {
				$('#cusId').addClass('hide');
			}
		} else {
			$('#cusId').addClass('hide');
		}
	}, getContextPath() + '/pay/hasOrderHistory', $.toJSON({
		projectId : key
	}));

}

function checkHasNoPayList() {
	var key = getCurrentProject();
	loadData(function(msg) {
		if (msg.errorCode == 200) {
			if (msg.result >= 1) {
				$('#userContentId').removeClass('hide');

			} else {
				$('#userContentId').addClass('hide');

			}
		} else {
			$('#cusId').addClass('hide');
		}
	}, getContextPath() + '/pay/hasNotPayOrder', "");
}

function checkHasListForEm() {
	var key = getCurrentProject();
	loadData(function(msg) {
		if (msg.errorCode == 200) {
			if (msg.result > 0) {
				$('#loadEmployee').removeClass('hide');
				$('#payHistory').removeClass('hide');
				$('#managerId').removeClass('hide');
			} else {
				$('#loadEmployee').addClass('hide');
				$('#payHistory').addClass('hide');
				$('#managerId').addClass('hide');
			}
		} else {
			$('#managerId').addClass('hide');
		}
	}, getContextPath() + '/pay/hasOrderHistory', $.toJSON({
		projectId : key
	}));
}

function checkHasListForEmFirst() {
	var key = getCurrentProject();
	loadData(function(msg) {
		if (msg.errorCode == 200) {
			if (msg.result > 0) {
				$('#loadEmployee').removeClass('hide');
				$('#payHistory').removeClass('hide');
			} else {
				$('#loadEmployee').addClass('hide');
				$('#payHistory').addClass('hide');
			}
		} else {
			$('#managerId').addClass('hide');
		}
	}, getContextPath() + '/pay/hasOrderHistory', $.toJSON({
				projectId : key
	}));
}

function toCheckListClose(token) {
	$('.check-step').html('您确定关闭这个支付记录吗？');
	$('#close-list').modal({
		backdrop : 'static',
		keyboard : false
	});
	$('#sureClose').off('click').on('click', function() {
		loadData(function(msg) {
			if (msg.errorCode == 200) {
				$('#payListPage').html('');
				payList();
				$('#close-list').modal('hide');
			} else {
				alert('关闭失败');
			}
		}, getContextPath() + '/pay/offorder', $.toJSON({
			token : token
		}));
	});

	$('#falseClose').on('click', function() {
		$('#close-list').modal('hide');
		$('#sureClose').unbind("click");

	});

	$('#canclestepClose').on('click', function() {
		$('#close-list').modal('hide');
		$('#sureClose').unbind("click");
	});

}
function clickLink() {
	$('#canclePayLink').on('click', function() {
				$('#toolbar-share').modal('hide');
			});

	$('.pay-redLink-btn').on('click', function() {
				$('#toolbar-share').modal('hide');
			});

}
 
var ControlTree = {
	CommonDoingProjectTree : function() {
		$('#doingProjectId').slideDown();

	},
	OpenDoingProjectTree : function() {
		$('#doingProject').removeClass('inactive');
		$('#doingProject').addClass('active');
		$('#doingProjectId').slideDown();

	},
	OpenMyProjectTree : function() {
		ControlTree.CommonDoingProjectTree();
		$('#myProject').removeClass('inactive');
		$('#myProject').addClass('active');
		$('#myProjectId').slideDown();
	},
	OpenHelpProjectTree : function() {
		ControlTree.CommonDoingProjectTree();

		$('#helpProject').removeClass('inactive');
		$('#helpProject').addClass('active');
		$('#helpProjectId').slideDown();
	},
	OpenPauseProjectTree : function() {
		ControlTree.CommonDoingProjectTree();

		$('#pauseProject').removeClass('inactive');
		$('#pauseProject').addClass('active');
		$('#pauseProjectId').slideDown();
	},
	OpenHistoryProjectTree : function() {
		$('#historyProject').removeClass('inactive');
		$('#historyProject').addClass('active');
		$('#historyProjectId').slideDown();
	},
	CloseDoingProjectTree : function() {
		$('#doingProject').removeClass('active');
		$('#doingProject').addClass('inactive');
		$('#doingProjectId').slideUp();
	},
	CloseMyProjectTree : function() {
		$('#myProject').removeClass('active');
		$('#myProject').addClass('inactive');
		$('#myProjectId').slideUp();
	},
	CloseHelpProjectTree : function() {
		$('#helpProject').removeClass('active');
		$('#helpProject').addClass('inactive');
		$('#helpProjectId').slideUp();
	},
	ClosePauseProjectTree : function() {
		$('#pauseProject').removeClass('active');
		$('#pauseProject').addClass('inactive');
		$('#pauseProjectId').slideUp();
	},
	CloseHistoryProjectTree : function() {
		$('#historyProject').removeClass('active');
		$('#historyProject').addClass('inactive');
		$('#historyProjectId').slideUp();
	},
	showTreeImg : function() {
		if (nowImg == 0) {
			$('#helpProject').removeClass('inactive');
			$('#helpProject').addClass('active');
		}
		else if (nowImg == 1) {
			$('#pauseProject').removeClass('inactive');
			$('#pauseProject').addClass('active');
		}
		else if (nowImg == 2) {
			$('#myProject').removeClass('inactive');
			$('#myProject').addClass('active');
		}
	},
	shutMyProject : function() {
		$('#myProjectId').on('click', function() {
			if (('#myProject').attr('class') == 'active') {
				CloseMyProjectTree();
			} else if (('#myProject').attr('class') == 'inactive') {
				OpenMyProjectTree();
			}
		});
	}
}

function formatterDateTime(date) {
	var datetime = date.getFullYear()
			+ "-"// "年"
			+ ((date.getMonth() + 1) > 10 ? (date.getMonth() + 1) : "0"
					+ (date.getMonth() + 1))
			+ "-"// "月"
			+ (date.getDate() < 10 ? "0" + date.getDate() : date.getDate())
			+ " "
			+ (date.getHours() < 10 ? "0" + date.getHours() : date.getHours())
			+ ":"
			+ (date.getMinutes() < 10 ? "0" + date.getMinutes() : date
					.getMinutes())
			+ ":"
			+ (date.getSeconds() < 10 ? "0" + date.getSeconds() : date
					.getSeconds());
	return datetime;
}

function showError(str) {
	$("#toolbar-list").modal('show');
	var info = str + ""
	$('#showErrorList').html(info);
	$('#closeThis').on('click', function() {
		$("#toolbar-list").modal('hide');
	});
}

function hideSuccessTooltip() {
	$("#toolbar-list").modal('hide');
}

function moreComment(open) {
	if (!open) {
		$('#more-commentImg').removeClass('circle-180');
		$('#more-comment-text').text("展开更多");
	} else {
		$('#more-comment-text').text("收起");
		$('#more-commentImg').addClass("circle-180");
	}
}

function moreFile(open) {
	if (!open) {
		$('#fileWord').text("展开更多");
		$('#more-FileImg').removeClass("circle-180");
	} else {
		$('#fileWord').text("收起");
		$('#more-FileImg').addClass("circle-180");
	}
}
