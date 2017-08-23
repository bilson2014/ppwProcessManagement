<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="r" uri="/mytaglib" %>
<%-- import CSS --%>
<spring:url value="/resources/css/activiti/flowInfo.css" var="flowInfoCss"/>
<spring:url value="/resources/lib/AirDatepicker/dist/css/datepicker.min.css" var="datepickerCss" />
<spring:url value="/resources/lib/webuploader/webuploader.css" var="webuploaderCss" />
<spring:url value="/resources/lib/Bootstrap/css/bootstrap.min.css"
	var="bootstrapCss" />
<%-- import JS --%>
<spring:url value="/resources/lib/jquery/jquery-2.0.3.min.js" var="jqueryJs"/>
<spring:url value="/resources/js/common.js" var="commonJs"/>
<spring:url value="/resources/lib/AirDatepicker/dist/js/datepicker.min.js" var="datepickerJs" />
<spring:url value="/resources/lib/AirDatepicker/dist/js/i18n/datepicker.zh.js" var="datepickerZhJs" />
<spring:url value="/resources/js/activiti/textFlowI.js" var="textFlowIJs"/>
<spring:url value="/resources/lib/webuploader/webuploader.js" var="webuploaderJs" />
<%-- <spring:url value="/resources/js/activiti/dynamic-form-handler.js" var="dynamicJs"/> --%>
<spring:url value="/resources/images" var="imgPath" />
<spring:url value="/resources/lib/jquery.json/jquery.json-2.4.min.js" var="jsonJs" />
<spring:url value="/resources/lib/json/ezmorph.jar" var="ezmorphJs" />


<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!-->
<html class="no-js">
<!--<![endif]-->

<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=9,chrome=1">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="keywords" content="">
<meta name="description" content="">
<meta name="baidu-site-verification" content="dMz6jZpIwd" />
<title></title>
<link rel="stylesheet" href="${datepickerCss}">
<link rel="stylesheet" href="${flowInfoCss}">
<link rel="stylesheet" href="${webuploaderCss}">
<link rel="stylesheet" href="${bootstrapCss}">


<!--[if lt IE 9]><script>window.html5 || document.write('<script src="html5shivJs"><\/script>')</script><![endif]-->

</head>
<body>

<input type="hidden" value="${taskStage}" id="taskStage"/>
<input type="hidden" value="${taskId }" id="currentTaskId" />
<input type="hidden" value="${taskName}" id="taskName" />
<input type="hidden" value="${projectId}" id="projectId" />
<input type="hidden" value="${processInstanceId}" id="processInstanceId" />
<input type="hidden" value="${price_info}" id="price_info" />


<%-- <input type="hidden" value="${user_info}" id="user_info" />
<input type="hidden" value="${price_info}" id="price_info" />
<!-- 制作供应商 -->
<input type="hidden" value="${teamProduct_info}" id="teamProduct_info" />
<!-- 策划供应商 -->
<input type="hidden" value="${teamPlan_info}" id="teamPlan_info" /> --%>



 


	<input type="hidden" id="storage_node" value="${file_locate_storage_path }" />
	    <div class="pages">
<!-- 	    <div id="showPrice">收款信息</div>
	    <div id="showBudget">客户预算</div>
	    <div id="showRealPrice">实际金额</div>
	    <div id="showPlot">策划</div>
	    <div id="invoiceInfo">发票信息</div>
	    <div id="showHelper">协同人清单</div>
	    <div id="showControl">版本管理</div>
	    <div id="showCusEdit">用户信息修改</div>
	    <div id="showUp">上传文件</div>
	    <div id="showError">驳回</div>
	    <div id="finishCus">完善客户信息</div>
	    <div id="showshowExecutive">分配监制</div> -->
	    
	     <div class="infoTitle">
	                     <div class="titleName" >${projectName}</div>
	                     <c:if test="${!empty projectGrade || !empty userLevel}">
		                     <div class="point">
	                              <div class="showPoint">${projectGrade}${userLevel}</div>
	                              <div class="showDeil showDownDeil">
	                               <c:if test="${!empty projectGrade}">
	                                    <div class="proPoint">项目评级<span>${projectGrade}</span></div>
	                                    </c:if>
	                                     <c:if test="${!empty userLevel}">
	                                    <div class="cusPoint">客户评级<span>${userLevel}</span></div>
	                                    </c:if>
	                              </div>
		                     </div>
	                     </c:if>
	                     <div class="proControl">
	                                                   项目操作
	                         <div class="newControl">
	                              <a id="isPause" href="/project/suspendProcess/${processInstanceId}"><div id="isPause">暂停项目</div></a>
	                              <a id="isBack" href="/project/activateProcess/${processInstanceId}"><div id="isBack">恢复项目</div></a>
	                         </div>
	                     </div>
	      </div>
	    
	           <div class="productInfo" id="daiban">
	               
	                <div class="infoLine"></div>
	                <div class="waitMission" id="waitMission">
	                       <div class="missionTop">
	                            <div class="missinName">待办任务 : </div>
	                            <div class="missinInfo" id="taskName">${taskName}</div>
	<!--                             <div class="missinState"><img src="/resources/images/provider/toWait.png"><div>进行中</div></div>-->
	                            <div class="missinTime" ><img src="/resources/images/flow/lastTime.png"><div id="missinTime">${dueDate}</div></div> 
	                            <div class="contentDiv">
	                               <div class="setContent">
	                                    <div class="redContent hide"></div>
	                                    <div class="simContent">${taskDescription}</div>
	                                    <div class="setBtn">
	                                         <div class="redBtn btn-c-r" id="toFinish">任务操作</div>
	                                         <!-- <div class="redBtn btn-c-r">确认完成</div> -->
	                                    </div>
	                               </div>
	                            </div>
	                       </div>
	                     
	                   </div>    
                     </div>
	                   <div class="productInfo secondProduct">    
	                       <div class="projectTitle ">项目进度及历史</div>
	                        <div class="timeFlow">
	                            <div class="imgFlow" id="imgFlow">
	                                  <div id="imgWord"></div>
	                                  <div id="lastTimeWord"></div>
	                            </div>
	                            <div class="flowIcon">
	                                 <div class="stageTask" data-id="沟通阶段">沟通</div>
	                                 <div class="stageTask" data-id="方案阶段">方案</div>
	                                 <div class="stageTask" data-id="商务阶段">商务</div>
	                                 <div class="stageTask" data-id="制作阶段">制作</div>
	                                 <div class="stageTask" data-id="交付阶段">交付</div>
	                                 <img class="icons" src="/resources/images/flow/down.png">
	                            </div>
	                        </div>
	                       <div class="setListDiv">
	                               <div class="ListTop">
	                                     <div class="startTime" >阶段起始时间 : <span id="startTime"></span></div>
	                                     <div class="endTime hide">阶段计划完成时间<span></span></div>
	                               </div>
	                               <div class="listContent" id="listContent">
	                                  <!--  <div class="listItem">
	                                        <div class="lineStart"></div>
	                                        <div class="time">预计：2017-07-09  14：00</div>
	                                        <div class="user">策划人AAA</div>
	                                        <div class="info">各种信息</div>
	                                        <div class="state"><img src="/resources/images/provider/toWait.png"><div class="green">已完成</div></div>
	                                        <div class="find">查看</div>
	                                   </div>
	                                    <div class="listItem">
	                                        <div class="lineOne"></div>
	                                        <div class="time">预计：2017-07-09  14：00</div>
	                                        <div class="user">策划人AAA</div>
	                                        <div class="info">各种信息</div>
	                                        <div class="state"><img src="/resources/images/provider/toWait.png"><div class="redWord">已报错</div></div>
	                                        <div class="find">查看</div>
	                                   </div>
	                                    <div class="listItem">
	                                        <div class="lineOne"></div>
	                                        <div class="time">预计：2017-07-09  14：00</div>
	                                        <div class="user">策划人AAA</div>
	                                        <div class="info">各种信息</div>
	                                        <div class="state"><img src="/resources/images/provider/toWait.png"><div class="yellow">进行中</div></div>
	                                        <div class="find">查看</div>
	                                   </div>
	                                    <div class="listItem">
	                                        <div class="lineOne"></div>
	                                        <div class="time">预计：2017-07-09  14：00</div>
	                                        <div class="user">策划人AAA</div>
	                                        <div class="info">各种信息</div>
	                                        <div class="state"><img src="/resources/images/provider/toWait.png"><div class="dark">延误</div></div>
	                                        <div class="find">查看</div>
	                                   </div>
	                                    <div class="listItem">
	                                        <div class="lineEnd"></div>
	                                        <div class="time">预计：2017-07-09  14：00</div>
	                                        <div class="user">策划人AAA</div>
	                                        <div class="info">各种信息</div>
	                                        <div class="state"><img src="/resources/images/provider/toWait.png"><div class="gray">未开始</div></div>
	                                        <div class="find">查看</div>
	                                   </div> -->
	                               </div>
	                       </div>
	                       <c:if test="${!empty synergyList}"> 
	                         <div class="getInfoItem">
	                            <div class="getInfoItemTop">
	                                 <div class="controlOpen"></div>
	                                 <div class="info">团队信息</div>
	                            </div>
	                            <div class="getInfoItemContent">
	                            <c:forEach var="item" items="${synergyList}"> 
											  <div class="imgItem">
										  <c:if test="${!empty item.imgUrl}"> 
	                                          <img src="${file_locate_storage_path }${item.imgUrl}">
	                                      </c:if>
	                                       <c:if test="${empty item.imgUrl}"> 
	                                          <img src="/resources/images/flow/def.png">
	                                      </c:if>
	                                      <ul>
	                                          <li>${item.employeeName}</li>
	                                          <li>${item.employeeGroup}</li>
	                                          <li>${item.telephone}</li>
	                                      </ul>
	                                 </div>
								 </c:forEach> 
	                              <!--     <div class="imgItem">
	                                      <img src>
	                                      <ul>
	                                          <li>三维</li>
	                                          <li>负责人</li>
	                                          <li>线上-网站</li>
	                                      </ul>
	                                 </div> -->
	                              
	                            </div>
	                         </div>   
	                       </c:if>
	                        <c:if test="${!empty flow_info}"> 
	                       <div class="getInfoItem">
	                            <div class="getInfoItemTop">
	                                 <div class="controlOpen"></div>
	                                 <div class="info">项目信息</div>
	                                 <div class="time"></div>
	                            </div>
	                            <div class="getInfoItemContent">
	                                  <div class="contentItem">
	                                         <div class="contentItem">
			                                  <div class="item">
			                                          <div>项目编号</div>
			                                          <div>${flow_info["projectId"]}</div>
			                                  </div>
			                                  <div class="item">
			                                          <div>项目名称</div>
			                                          <div>${flow_info["projectName"]}</div>
			                                  </div>
			                                  <div class="item">
			                                          <div>项目评级</div>
			                                          <div>${flow_info["projectGrade"]}</div>
			                                  </div>
			                                  <div class="item">
			                                          <div>项目来源</div>
			                                          <div>${flow_info["projectSource"]}</div>
			                                  </div>
		                                  </div>
	                                  </div>
	                                   <div class="contentItem">
	                                         <div class="contentItem">
			                                  <div class="item">
			                                          <div>产品线</div>
			                                          <div>${flow_info["productName"]}</div>
			                                  </div>
			                                  <div class="item">
			                                          <div>项目周期</div>
			                                          <div id="projectCtyle">${flow_info["projectCycle"]}</div>
			                                  </div>
			                                  <div class="item">
			                                          <div>立项时间</div>
			                                          <div id="projectTime">${flow_info["createDate"]}</div>
			                                  </div>
		                                  </div>
	                                  </div>
	                                  <div class="longItem">
			                                          <div>项目配置</div>
			                                          <div>${flow_info["productConfigLevelName"]}</div>
			                           </div>
			                           <div class="longItem">
			                                          <div>对标影片</div>
			                                          <div><a href="${flow_info['filmDestPath']}">${flow_info["filmDestPath"]}</a></div>
			                           </div>
			                           <div class="longItem">
			                                          <div>项目描述</div>
			                                          <div>${flow_info["projectDescription"]}</div>
			                           </div>
	                
	                            </div>
	                       </div>
	                       
	                        </c:if> 
	                      <c:if test="${!empty user_info}"> 
		                      <div class="getInfoItem">
		                            <div class="getInfoItemTop">
		                                 <div class="controlOpen"></div>
		                                 <div class="info">客户信息</div>
		                                
		                            </div>
		                            <div class="getInfoItemContent">
		                                  <div class="contentItem">
			                                  <div class="item">
			                                          <div>客户名称</div>
			                                          <div>${user_info["userName"]}</div>
			                                  </div>
			                                  <div class="item">
			                                          <div>客户评级</div>
			                                          <div>${user_info["userLevel"]}</div>
			                                  </div>
			                                  <div class="item">
			                                          <div>客户联系人</div>
			                                          <div>${user_info["linkman"]}</div>
			                                  </div>
			                                  <div class="item">
			                                          <div>客户电话</div>
			                                          <div>${user_info["telephone"]}</div>
			                                  </div>
		                                  </div>
		                            </div>
		                       </div>
	                        </c:if>
	                        
	                        <c:if test="${!empty teamProduct_info || !empty teamPlan_info}"> 
	                        <div class="getInfoItem">
	                            <div class="getInfoItemTop">
	                                 <div class="controlOpen"></div>
	                                 <div class="info">供应商信息</div>
	                                 <div class="time"></div>
	                                 <div class="update hide btn-c-r">更新</div>
	                            </div>
	                            <div class="getInfoItemContent">
	                                  <div class="title"><div class="long"></div><div class="short"></div>策划供应商</div>
	                                  <div class="contentItem">
				                                     <div class="item">
						                                          <div>供应商名称</div>
						                                          <div>${teamProduct_info["teamName"]}</div>
						                             </div>
						                             <div class="item">
						                                          <div>供应商联系人</div>
						                                          <div>${teamProduct_info["linkman"]}</div>
						                             </div>
						                             <div class="item">
						                                          <div>供应商联系电话</div>
						                                          <div>${teamProduct_info["telephone"]}</div>
						                             </div>
									 </div>
	                               
	                                  <div class="title"><div class="long"></div><div class="short"></div>制作供应商</div>
	                                  <div class="contentItem" data-value="${teamProduct_info}">
	                                  
	                                  <c:if test="${!empty teamProduct_info}">
					                             <div class="contentItem">	      
				                                     <div class="item">
						                                          <div>供应商名称</div>
						                                          <div>${teamProduct_info["teamName"]}</div>
						                             </div>
						                             <div class="item">
						                                          <div>供应商联系人</div>
						                                          <div>${teamProduct_info["linkman"]}</div>
						                             </div>
						                             <div class="item">
						                                          <div>供应商联系电话</div>
						                                          <div>${teamProduct_info["telephone"]}</div>
						                             </div>
										         </div>
							          </c:if>
	       
	                                  </div>
	                            </div>
	                       </div>
	                      </c:if> 
	                       <c:if test="${!empty price_info}">
	                       <div class="getInfoItem">
	                            <div class="getInfoItemTop">
	                                 <div class="controlOpen"></div>
	                                 <div class="info">价格信息</div>
	                            </div>
	                            <div class="getInfoItemContent">
	                                  <div class="contentItem">	      
	                                     <div class="item">
			                                          <div>预估价格</div>
			                                          <div>${price_info["estimatedPrice"]}</div>
			                             </div>
			                             <div class="item">
			                                          <div>客户项目预算</div>
			                                          <div>${price_info["projectBudget"]}</div>
			                             </div>
			                             <div class="item">
			                                          <div>策划供应商预算</div>
			                                          <div>${teamProduct_info["budget"]}</div>
			                             </div>
			                             <div class="item">
			                                          <div>项目来源</div>
			                                          <div>${flow_info["projectSource"]}</div>
			                             </div>
	                                                           	                                  
		                        <%--           <c:forEach var="item" items="${price_info}"> 
													<div class="item">
			                                          <div>${item.key}</div>
			                                          <div>${item.value}</div>
			                                       </div>
										  </c:forEach>  --%>
										 
							         </div>
							          <div class="contentItem">	      
	                                     <div class="item">
			                                          <div>产品线</div>
			                                          <div>${price_info["产品线"]}</div>
			                             </div>
			                             <div class="item">
			                                          <div>项目周期</div>
			                                          <div>${price_info["项目周期"]}</div>
			                             </div>
			                             <div class="item">
			                                          <div>立项时间</div>
			                                          <div>${price_info["立项时间"]}</div>
			                             </div>
			                             <div class="item">
			                                          <div>负责人</div>
			                                          <div>${price_info["负责人"]}</div>
			                             </div>
							         </div>
	                            </div>
	                       </div>
	                        </c:if>
	                                              
	                   <div class="projectTitle margin-top">项目文件
	                        <div class="conMod btn-c-r">版本管理</div>
	                        <div class="upFile hide btn-c-r">上传</div>
	                   </div>
	                   <div class="projectFilm" id="projectFilm"></div>
	                       
	                       <div class="projectTitle">留言评论区</div>
	                       <div class="toSetArea">
	                             <textarea id="talkInfo"></textarea>
	                             <div class="upInfo" style="display:block">
	                                 <div class="btn-c-r" id="submitTalkInfo">提交</div>
	                             </div>
	                             <div class="errorSpan errorSpans" id="areaError"></div>
	                       </div>
	                       <div class="setAreaDiv">
	                        
	                          <!--    <div class="areaItem">
	                                 <div class="infoItem">
	                                     <img src="/resources/images/flow/def.png">
	                                     <div class="info">策划人：完成 上传策划方案 任务</div>
	                                     <div class="time">
	                                        <span>发布时间：22017-07-09  14：00</span>
	                                        <div class="openTalk"></div>
	                                     </div>
	                                 </div>
	                                 <div class="infoContent">
	                                       <div>负责人:<span>策划方案需要调整一下</span></div>
	                                       <div>负责人:<span>策划方案需要调整一下</span></div>
	                                       <input>
	                                 </div>
	                                 <div class="upInfo">
	                                            <div class="btn-c-r">提交</div>
	                                 </div>
	                            </div> -->
	                             <div class="getMore hide">
	                                  <div>展开更多</div>
	                                  <div></div>
	                             </div>
	                       </div>
	                </div>
	           </div>              
	    </div>
	<!-- js-->
	
	<div class="cusModel" id="cusModel" >

     <div class="modelCard">
            <div class="cardTop">
                   <div class="title" id="infoNameTitle">完善客户信息</div>
                   <div class="state">
	                   <img id="stateImg" src="/resources/images/flow/toStart.png">
	                   <div id="stateWord"></div>
                   </div>
                   <div class="closeModel"></div>
            </div>
            <div class="cardContent">
                 <div class="contentItem">
	                  <div class="title">事件说明 : </div>
	                  <div class="content" id="stateContent">打算打打三大所大所多</div>
	             </div>
	              <div class="contentItem">
	                  <div class="title">开始时间 : </div>
	                  <div class="content" id="infoStartTime">2017-11-12</div>
	             </div>   
	              <div class="contentItem">
	                  <div class="title" id="infoEndTitle">截止时间 : </div>
	                  <div class="content" id="infoEndTime">2017-11-12</div>
	             </div>
	             <div class="itemHeight" id="itemHeightInfo">
	             <!-- <div class="infoItem">
	                       <div  class="itemTop">
	                             <img class="logo" src="">
	                              <ul>
	                                 <li><span></span></li>
	                                 <li><span></span> <img class="modelOpen" src="/resources/images/flow/areaMore.png"></li>
	                              </ul>
	                       </div>
	                       <div class="itemArea">
	                             <div><span>负责人 : </span><span>需要调整一下</span></div>
	                             <div><span>负责人回复负责人 :</span><span>需要调整一下</span></div>
	                             <input>
	                       </div>
	                       <div class="backInfoTalk btn-c-r">回复</div>
	             </div> -->
	           
	             </div>
            </div>
     </div>
     
</div>

<!-- 报错 -->
<div class="cusModel" id="errorModel" >
     <div class="modelCard">
            <div class="cardTop">
                   <div class="title">报错备注</div>
                   <div class="closeModel"></div>
            </div>
            <div class="errorContent">
                 <div class="title">请将您的报错里有些如下方留言框中：</div>
                 <textarea></textarea>
                 <div class="errorInfo">注意：报错后将开始预备报错任务相同的新任务，请确认信息无误后进行报错。</div>
                 <div class="btnMid">
                      <div class="btn-c-g">取消</div>
                      <div class="btn-c-r">确认</div>
                 </div>
            </div>
     </div>
</div>
<!-- 上传文件 -->
<div class="cusModel" id="upModel">
     <div class="modelCard">
            <div class="cardTop">
                   <div class="title">文件上传</div>
                   <div class="closeModel"></div>
            </div>
            <div class="upContent">
                 <div class="item">
                      <div class="title">选择分类</div>
                      <div class="orderSelect" >
			                <div id="sIndentSource"></div>
			                <img src="${imgPath}/flow/selectOrder.png">
			                <ul class="oSelect searchSelect" id="orderCome">
			                    <li data-id="">全部</li>	
			                </ul>    
				        </div>
                 </div>
                  <div class="item">
                      <div class="title">选择文件</div>
                      <input>
                      <div class="find">浏览</div>
                 </div>
                 <div class="btnMid">
                      <div class="btn-c-g">取消</div>
                      <div class="btn-c-r">确认</div>
                 </div>
            </div>
     </div>
</div>

<!-- 客户信息修改 -->
<div class="cusModel" id="cusInfoModel">
     <div class="modelCard">
            <div class="cardTop">
                   <div class="title">客户信息修改</div>
                   <div class="closeModel"></div>
            </div>
            <div class="cusInfoContent">
                 <div class="item">
                       <div class="title">客户名称</div>
                       <input>
                 </div>
                  <div class="item">
                       <div class="title">客户联系人</div>
                       <input>
                 </div>
                  <div class="item">
                       <div class="title">客户联系电话</div>
                       <input>
                 </div>
                  <div class="item">
                       <div class="title">客户评分</div>
                       <div class="point">客户评分</div>
                 </div>
	             <div class="btnMid">
	                      <div class="btn-c-g">取消</div>
	                      <div class="btn-c-r">确认</div>
	             </div>
            </div>
     </div>
</div>

<!-- 版本管理 -->
<div class="cusModel" id="controlModel">
     <div class="modelCard">
            <div class="cardTop">
                   <div class="title">版本管理<span class="errorSpan" id="errorSpan"></span></div>
                   <div class="closeModel"></div>
            </div>
            <div class="controlContent" id="controlContent">
                      <div class="item">
                             <div class="itemTop hide">
                                  <div class="controlOpen"></div>
                                  <div class="title">需求文档</div>
                             </div>
                             <div class="getInfoItemContent">
                                
	                            <div class="InfoItem">
                                  <div class="fileName">文件名</div>
                                  <div class="name">策划人</div>
                                  <div class="time">上传于:2017 17:59:08</div>
                                  <div class="icon">
                                              <img class="flag" src="/resources/images/flow/flag.png">
                                              <img class="download" src="/resources/images/flow/download.png">
	                                         <!--  <img class="look" src="/resources/images/flow/look.png">
	                                          <img class="share" src="/resources/images/flow/share.png">
	                                          <img class="download" src="/resources/images/flow/download.png"> -->
	                              </div>
	                            </div>    
                             </div>
                      </div>
                      
            </div>
     </div>
</div>

<!-- 协同人清单 -->
<div class="cusModel" id="helperModel">
     <div class="modelCard">
            <div class="cardTop">
                   <div class="title">协同人清单</div>
                   <div class="closeModel"></div>
            </div>
            <div class="helperContent">
                   <div class="helpItem">
                       <div class="title">客服总监</div>
                       <div class="name">人名</div>     
                   </div>
                   <div class="helpItem">
                       <div class="title">销售总监</div>
                       <div class="name">人名</div>     
                   </div>
                   <div class="helpItem">
                       <div class="title">创意总监</div>
                       <div class="name">人名</div>     
                   </div>
                   <div class="helpItem">
                       <div class="title">策划</div>
                       <div class="name">人名</div>     
                   </div>
                   <div class="helpItem">
                       <div class="title">供应商总监</div>
                       <div class="name">人名</div>     
                   </div>
                   <div class="helpItem">
                       <div class="title">供应商管家</div>
                       <div class="name">人名</div>     
                   </div>
                   <div class="helpItem">
                       <div class="title">供应商采购</div>
                       <div class="name">人名</div>     
                   </div>
                   <div class="helpItem">
                       <div class="title">监制总监</div>
                       <div class="name">人名</div>     
                   </div>
                   <div class="helpItem">
                       <div class="title">监制</div>
                       <div class="name">人名</div>     
                   </div>
                   <div class="helpItem">
                       <div class="title">财务出纳</div>
                       <div class="name">人名</div>     
                   </div>
                   <div class="helpItem">
                       <div class="title">财务主管</div>
                       <div class="name">人名</div>     
                   </div>
            </div>
     </div>
</div>

<!-- 客户转账信息 -->
<div class="cusModel" id="cusPriceModel">
     <div class="modelCard">
            <div class="cardTop">
                   <div class="title" id="cusPriceModelTitle">客户转账信息</div>
                   <div class="closeModel"></div>
            </div>
            <div class="cusPriceContent">
                   <div class="cusPriceItem">
                       <div class="title">交易流水号</div>
                       <div class="name">人名</div>     
                   </div>
                   <div class="cusPriceItem">
                       <div class="title">交易方式</div>
                       <div class="name">人名</div>     
                   </div>
                   <div class="cusPriceItem">
                       <div class="title">交易时间</div>
                       <div class="name">人名</div>     
                   </div>
                   <div class="cusPriceItem">
                       <div class="title">订单编号</div>
                       <div class="name">人名</div>     
                   </div>
                   <div class="cusPriceItem">
                       <div class="title">交易方式</div>
                       <div class="name">人名</div>     
                   </div>
                   <div class="cusPriceItem">
                       <div class="title">交易金额</div>
                       <div class="name">人名</div>     
                   </div>
                   <div class="cusPriceItem">
                       <div class="title">描述</div>
                       <div class="name">人名</div>     
                   </div>
            </div>
     </div>
</div>

<!-- 发票信息 -->
<div class="cusModel" id="invoiceModel">
     <div class="modelCard">
            <div class="cardTop">
                   <div class="title">发票信息</div>
                   <div class="closeModel"></div>
            </div>
            <div class="invoiceContent">
                   <div class="invoiceItem">
                       <div class="title">发票类型</div>
                       <div class="name">人名</div>     
                   </div>
                   <div class="invoiceItem">
                       <div class="title">发票编号</div>
                       <div class="name">人名</div>     
                   </div>
                   <div class="invoiceItem">
                       <div class="title">发票内容</div>
                       <div class="name">人名</div>     
                   </div>
                    <div class="invoiceItem">
                       <div class="title">价税合计</div>
                       <div class="name">人名</div>     
                   </div>
                   <div class="invoiceItem">
                       <div class="title">发票税率</div>
                       <div class="name">人名</div>     
                   </div>
                   <div class="invoiceItem">
                       <div class="title">开票时间</div>
                       <div class="name">人名</div>     
                   </div>
                   <div class="invoiceItem">
                       <div class="title">付款时间</div>
                       <div class="name">人名</div>     
                   </div>
                   <div class="invoiceItem">
                       <div class="title">提供人</div>
                       <div class="name">人名</div>     
                   </div>
                   <div class="invoiceItem">
                       <div class="title">描述</div>
                       <div class="name">人名</div>     
                   </div>
            </div>
     </div>
</div>

 <!-- 提示 -->
<div class="cusModel" id="warnModel">
     <div class="modelCard smallModel">
            <div class="cardTop">
                   <div class="title">提示</div>
                   <div class="closeModel"></div>
            </div>
            <div class="warnContent">
                     <img src="/resources/images/flow/warn.png">
                    <div class="info">内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容</div>
                    <div class="btnMid">
                      <div class="btn-c-g">取消</div>
                      <div class="btn-c-r">确认</div>
                    </div>  
            </div>
     </div>
</div>

 <!-- 分配策划 -->
<div class="cusModel" id="plotModel" >
     <div class="modelCard smallModel">
            <div class="cardTop">
                   <div class="title" id="plotTitle">分配策划</div>
                   <div class="closeModel"></div>
            </div>
            <div class="plotContent">
	                      <div class="title">策划人</div>
				          <div class="orderSelect" >
				                <div id="sIndentSource"></div>
				                <img src="${imgPath}/flow/selectOrder.png">
				                <ul class="oSelect" id="orderCome">
				                     <li>adasda</li>
				                     <li>dasdas</li>
				                     <li>dasda</li>
				                </ul>    
					      </div>
			     <div class="btnMid">
                      <div class="btn-c-g">取消</div>
                      <div class="btn-c-r">确认</div>
                 </div>
	     </div>
	</div>
</div>

 <!-- 填写供应商实际金额 -->
<div class="cusModel" id="priceModel">
     <div class="modelCard">
            <div class="cardTop">
                   <div class="title">填写供应商实际金额</div>
                   <div class="closeModel"></div>
            </div>
            <div class="priceContent">
	                      <div class="title">实际金额</div>
				          <input>
				          <div class="yuan">元</div>
				          <div class="title" style="margin-top:20px;">发票抬头</div>
				          <div class="orderSelect" >
				                <div id="sIndentSource"></div>
				                <img src="${imgPath}/flow/selectOrder.png">
				                <ul class="oSelect" id="orderCome">
				                     <li>adasda</li>
				                     <li>dasdas</li>
				                     <li>dasda</li>
				                </ul>    
					      </div>
				          
						  <div class="btnMid">
			                      <div class="btn-c-g">取消</div>
			                      <div class="btn-c-r">确认</div>
			              </div>
	         </div>
	</div>
</div>

 <!-- 填写客户预算信息 -->
<div class="cusModel" id="budgetModel">
     <div class="modelCard">
            <div class="cardTop">
                   <div class="title">填写客户预算信息</div>
                   <div class="closeModel"></div>
            </div>
            <div class="budgetContent">
                    <div class="item">
                         <div class="title">项目预算</div>
                         <input>
                         <div class="yuan">元</div>
                    </div>
                    <div class="itemTime">
                         <div class="title">项目交付时间</div>
                         <input>
                    </div>
                    <div class="itemTime">
                         <div class="title">约定付款时间</div>
                         <input>
                    </div>
                    <div class="btnMid">
		                      <div class="btn-c-g">取消</div>
		                      <div class="btn-c-r">确认</div>
		                </div>
            </div>
	</div>
</div>
 <!-- 填写收款信息 -->
<div class="cusModel" id="getPriceModel" >
     <div class="modelCard">
            <div class="cardTop">
                   <div class="title">填写收款信息</div>
                   <div class="closeModel"></div>
            </div>
            <div class="getPriceContent">
                    <div class="item">
                         <div class="title">交易流水号</div>
                         <input>
                    </div>
                    <div class="itemTime">
                         <div class="title">交易时间</div>
                         <input id="orderTime">
                    </div>
                    <div class="itemTime">
                         <div class="title">交易金额</div>
                         <input>
                         <div class="yuan">元</div>
                    </div>
                    <div class="item">
                         <div class="title">交易流水号</div>
                         <input>
                    </div>
                    <div class="item">
                         <div class="title areaTitle">描述</div>
                         <textarea></textarea>
                    </div>
                    <div class="btnMid">
		                      <div class="btn-c-g">取消</div>
		                      <div class="btn-c-r">确认</div>
		            </div>
            </div>
</div>
</div>

<!-- 分配策划供应商 -->
<div class="cusModel" id="">
     <div class="modelCard">
            <div class="cardTop">
                   <div class="title">分配策划供应商</div>
                   <div class="closeModel"></div>
            </div>
            <div class="getPriceContent">
                    <div class="item">
                         <div class="title">供应商名称</div>
                         <input>
                    </div>
                    <div class="itemTime">
                         <div class="title">供应商联系人</div>
                         <input id="orderTime">
                    </div>
                    <div class="itemTime">
                         <div class="title">供应商电话</div>
                         <input>
                    </div>
                    <div class="itemTime slow">
                         <div class="title">供应商预算价格</div>
                         <input>
                         <div class="yuan">元</div>
                    </div>
                    <div class="itemTime">
                         <div class="title">制作类型</div>
                         <div class="orderSelect" >
				                <div id="sIndentSource"></div>
				                <img src="${imgPath}/flow/selectOrder.png">
				                <ul class="oSelect" id="orderCome">
				                     <li>adasda</li>
				                     <li>dasdas</li>
				                     <li>dasda</li>
				                </ul>    
					      </div>
                    </div>
                    
                    <div class="item">
                         <div class="title areaTitle">制作内容</div>
                         <textarea></textarea>
                    </div>
                    
                    <div class="itemTime">
                         <div class="title">对接人</div>
                         <input>
                    </div>
                    
                    <div class="itemTime">
                         <div class="title">对接电话</div>
                         <input>
                    </div>
                    
                    <div class="itemTime">
                         <div class="title">项目交付时间</div>
                         <input>
                    </div>
                    
                    <div class="btnMid">
		                      <div class="btn-c-g">取消</div>
		                      <div class="btn-c-r">确认</div>
		            </div>
            </div>
</div>
</div>

<!-- 分配制作供应商 -->
<div class="cusModel" id="">
     <div class="modelCard">
            <div class="cardTop">
                   <div class="title">分配制作供应商</div>
                   <div class="closeModel"></div>
            </div>
            <div class="getPriceContent">
                    <div class="item">
                         <div class="title">供应商名称</div>
                         <input>
                    </div>
                    <div class="itemTime">
                         <div class="title">供应商联系人</div>
                         <input id="orderTime">
                    </div>
                    <div class="itemTime">
                         <div class="title">供应商电话</div>
                         <input>
                    </div>
                    <div class="itemTime bigSlow">
                         <div class="title">供应商预算价格</div>
                          <div class="orderSelect" >
				                <div id="sIndentSource"></div>
				                <img src="${imgPath}/flow/selectOrder.png">
				                <ul class="oSelect" id="orderCome">
				                     <li>adasda</li>
				                     <li>dasdas</li>
				                     <li>dasda</li>
				                </ul>    
					      </div>
					      <div class="yuan">元</div>
                    </div>
                    <div class="itemTime bBigSlow">
                         <div class="title">策划内容</div>
                         <div class="orderSelect" >
				                <div id="sIndentSource"></div>
				                <img src="${imgPath}/flow/selectOrder.png">
				                <ul class="oSelect" id="orderCome">
				                     <li>adasda</li>
				                     <li>dasdas</li>
				                     <li>dasda</li>
				                </ul>    
					      </div>
                    </div>
                    
                    <div class="item">
                         <div class="title areaTitle">制作内容</div>
                         <textarea></textarea>
                    </div>
                    
                    <div class="itemTime">
                         <div class="title">对接人</div>
                         <input>
                    </div>
                    
                    <div class="itemTime">
                         <div class="title">对接电话</div>
                         <input>
                    </div>
                    
                    <div class="itemTime">
                         <div class="title">项目交付时间</div>
                         <input>
                    </div>
                    
                    <div class="btnMid">
		                      <div class="btn-c-g">取消</div>
		                      <div class="btn-c-r">确认</div>
		            </div>
            </div>
</div>
</div>

<!-- 项目信息修改 -->
<div class="cusModel">
     <div class="modelCard">
            <div class="cardTop">
                   <div class="title">项目信息修改</div>
                   <div class="closeModel"></div>
            </div>
            <div class="getPriceContent">
                    <div class="item">
                         <div class="title">项目名称</div>
                         <input>
                    </div>
                    <div class="itemTime">
                         <div class="title">项目评级</div>
                         <div class="orderSelect so" >
				                <div id="sIndentSource"></div>
				                <img src="${imgPath}/flow/selectOrder.png">
				                <ul class="oSelect" id="orderCome">
				                     <li>adasda</li>
				                     <li>dasdas</li>
				                     <li>dasda</li>
				                </ul>    
					      </div>
                    </div>
                    <div class="itemTime">
                         <div class="title">项目周期</div>
                         <input>
                         <div class="yuan">天</div>
                    </div>
                     <div class="itemTime">
                         <div class="title">等级</div>
                         <div class="orderSelect so" >
				                <div id="sIndentSource"></div>
				                <img src="${imgPath}/flow/selectOrder.png">
				                <ul class="oSelect" id="orderCome">
				                     <li>adasda</li>
				                     <li>dasdas</li>
				                     <li>dasda</li>
				                </ul>    
					      </div>
                    </div>
                    <div class="itemTime">
                         <div class="title">时长</div>
                         <input id="orderTime">
                    </div>
                   
                    <div class="itemTime bBigSlow">
                         <div class="title">附加包</div>
                         <div class="orderSelect" >
				                <div id="sIndentSource"></div>
				                <img src="${imgPath}/flow/selectOrder.png">
				                <ul class="oSelect" id="orderCome">
				                     <li>adasda</li>
				                     <li>dasdas</li>
				                     <li>dasda </li>
				                </ul>    
					      </div>
                    </div>
                    
                    <div class="itemTime bBigSlow">
                         <div class="title">对标影片</div>
                         <input >
                    </div>
                    
                    <div class="item">
                         <div class="title areaTitle">制作内容</div>
                         <textarea></textarea>
                    </div>
                    
                    <div class="btnMid">
		                      <div class="btn-c-g">取消</div>
		                      <div class="btn-c-r">确认</div>
		            </div>
            </div>
</div>
</div>

<!-- 客户信息修改 -->
<div class="cusModel">
     <div class="modelCard">
            <div class="cardTop">
                   <div class="title">客户信息修改</div>
                   <div class="closeModel"></div>
            </div>
            <div class="getPriceContent">
                    <div class="itemTime">
                         <div class="title">客户名称</div>
                         <input>
                    </div>
                    <div class="itemTime">
                         <div class="title">客户联系人</div>
                         <input>
                    </div>
                    <div class="itemTime">
                         <div class="title">客户联系人</div>
                         <input>
                    </div>
                    <div class="itemTime">
                         <div class="title">客户评级</div>
                         <div class="orderSelect so" >
				                <div id="sIndentSource"></div>
				                <img src="${imgPath}/flow/selectOrder.png">
				                <ul class="oSelect" id="orderCome">
				                     <li>adasda</li>
				                     <li>dasdas</li>
				                     <li>dasda</li>
				                </ul>    
					      </div>
                    </div>
                    
                    <div class="btnMid">
		                      <div class="btn-c-g">取消</div>
		                      <div class="btn-c-r">确认</div>
		            </div>
            </div>
</div>
</div>

<!-- 供应商信息修改 -->
<div class="cusModel">
     <div class="modelCard">
            <div class="cardTop">
                   <div class="title">供应商信息修改 </div>
                   <div class="closeModel"></div>
            </div>
            <div class="getPriceContent">
                    
                    <div class="bigTitle">策划供应商</div>
            
                    <div class="item">
                         <div class="title">客户名称</div>
                         <input>
                    </div>
                    <div class="itemTime">
                         <div class="title">客户联系人</div>
                         <input>
                    </div>
                    <div class="itemTime">
                         <div class="title">客户联系人</div>
                         <input>
                    </div>
                    
                    <div class="bigTitle">策划供应商</div>
            
                    <div class="item">
                         <div class="title">客户名称</div>
                         <input>
                    </div>
                    <div class="itemTime">
                         <div class="title">客户联系人</div>
                         <input>
                    </div>
                    <div class="itemTime">
                         <div class="title">客户联系人</div>
                         <input>
                    </div>
                    
                    
                    <div class="btnMid">
		                      <div class="btn-c-g">取消</div>
		                      <div class="btn-c-r">确认</div>
		            </div>
            </div>
</div>
</div>

<!-- 其它信息修改 -->
<div class="cusModel">
     <div class="modelCard">
            <div class="cardTop">
                   <div class="title">其它信息修改 </div>
                   <div class="closeModel"></div>
            </div>
            <div class="otherContent">
                     
                     <div class="item">
	                       <div class="title">客户约定付款时间</div>
	                       <input>
                     </div>
                      <div class="item">
	                       <div class="title">客户项目交付时间</div>
	                       <input>
                     </div>
                     
                      <div class="item">
	                       <div class="title">策划供应商启动函备注信息</div>
	                       <textarea></textarea>
                     </div>
                     
                     <div class="item">
	                       <div class="title">策划供应商启动函备注信息</div>
	                       <textarea></textarea>
                     </div>
                    
                    
                    <div class="btnMid">
		                      <div class="btn-c-g">取消</div>
		                      <div class="btn-c-r">确认</div>
		            </div>
            </div>
</div>
</div>

<!-- 动态加载信息信息修改 -->
<div class="cusModel" id="autoSet">
     <div class="modelCard">
            <div class="cardTop">
                   <div class="title">${taskName}<span id="errorInfo"></span> </div>
                   <div class="closeModel"></div>
            </div>
			                 <div class="upProgress">
								<div class="proTitle" id="proTitle">上传进度</div>
								<div  class="progress progress-striped active">
									<div id="setWidth" class="progress-bar progress-bar-danger progress-bar-striped" role="progressbar"
										aria-valuenow="0" aria-valuemin="0" aria-valuemax="100"
										style="width: 0;"></div>
								</div>
								<div class="upIng">上传中...</div>
								<div class="upSuccess hide">
									<img src="/resources/images/provider/sure.png">上传成功
								</div>
								<div class="upError hide">
									<img src="/resources/images/provider/error.png">上传失败请重新上传
								</div>
							</div>
            <div class="otherContent" id="setAutoInfo">
                    
          <!--            <div class="item">
	                       <div class="title">客户约定付款时间</div>
	                       <input>
                     </div>
                      <div class="item">
	                       <div class="title">客户项目交付时间</div>
	                       <input>
                     </div> -->
                                     
            </div>
</div>
</div>

<!-- 价格信息修改 -->
<div class="cusModel" >
     <div class="modelCard">
            <div class="cardTop">
                   <div class="title">价格信息修改 </div>
                   <div class="closeModel"></div>
            </div>
            <div class="getPriceContent">
                    <div class="itemTime">
                         <div class="title">预估价格</div>
                         <input>
                         <div class="yuan syuan">元</div>
                    </div>
                    <div class="itemTime">
                         <div class="title">客户项目预算</div>
                         <input>
                         <div class="yuan syuan">元</div>
                    </div>
                    <div class="itemTime">
                         <div class="title">策划供应商预算</div>
                         <input>
                         <div class="yuan syuan">元</div>
                    </div>
                    <div class="itemTime">
                         <div class="title">制作供应商预算</div>
                         <div class="orderSelect so" >
				                <div id="sIndentSource"></div>
				                <img src="${imgPath}/flow/selectOrder.png">
				                <ul class="oSelect" id="orderCome">
				                     <li>adasda</li>
				                     <li>dasdas</li>
				                     <li>dasda</li>
				                </ul>    
					      </div>
					      <div class="yuan syuan">元</div>
                    </div>
                    <div class="itemTime">
                         <div class="title">制作供应商结算</div>
                         <input>
                         <div class="yuan syuan">元</div>
                    </div>
                    
                    <div class="btnMid">
		                      <div class="btn-c-g">取消</div>
		                      <div class="btn-c-r">确认</div>
		            </div>
            </div>
</div>
</div>


 <!-- 填写供应商发票信息 -->
<div class="cusModel" id="getBillModel">
     <div class="modelCard">
            <div class="cardTop">
                   <div class="title">填写供应商发票信息</div>
                   <div class="closeModel"></div>
            </div>
            <div class="getBillContent">
                    <div class="item">
                      <div class="title">发票类型</div>
                      <div class="orderSelect" >
			                <div id="sIndentSource"></div>
			                <img src="${imgPath}/flow/selectOrder.png">
			                <ul class="oSelect searchSelect" id="orderCome">
			                    <li data-id="">全部</li>
			                </ul>    
				      </div>
				   </div>
				   
				   <div class="item">
                      <div class="title">发票税率</div>
                      <input>
				   </div>
				    <div class="item">
                      <div class="title">发票编号</div>
                      <input>
				   </div>
				    <div class="item">
                      <div class="title">开票时间</div>
                      <input name="findTime" id="findTime">
				   </div>
				    <div class="item">
                      <div class="title">发票内容</div>
                      <input>
				   </div>
				    <div class="item">
                      <div class="title">付款时间</div>
                      <input id="payTime">
				   </div>
				    <div class="item">
                      <div class="title">税价合计</div>
                      <input name="payTime">
				   </div>
	               <div class="itemArea">
                      <div class="title">备注</div>
                      <textarea></textarea>
				   </div>
				   
				    <div class="btnMid">
		                      <div class="btn-c-g">取消</div>
		                      <div class="btn-c-r">确认</div>
		            </div>
            </div>
     </div>       
</div>

 <!-- 提示信息 -->
<div class="cusModel" id="infoModel">
     <div class="modelCard">
            <div class="cardTop">
                   <div class="title">操作确认</div>
                   <div class="closeModel"></div>
            </div>
            <div class="warnInfo ">确认操作吗</div>
		    <div class="btnMid margin-bottom">
                      <div class="btn-c-g" id="cancle">取消</div>
                      <div class="btn-c-r" id="checkSure">确认</div>
            </div>
      </div>    
</div>

<%--  <!-- 文件上传 -->
<div class="cusModel" id="getBillModel" style="display:block">
     <div class="modelCard">
            <div class="cardTop">
                   <div class="title">填写供应商发票信息</div>
                   <img class="closeModel" src="${imgPath}/flow/canclemodal.png">
            </div>
     </div>       
</div> --%>
	
<script type="text/javascript" src="${jqueryJs}"></script>
<script type="text/javascript" src="${commonJs}"></script>
<script type="text/javascript" src="${dynamicJs}"></script>
<script type="text/javascript" src="${datepickerJs}"></script>
<script type="text/javascript" src="${datepickerZhJs}"></script>
<script type="text/javascript" src="${webuploaderJs}"></script>
<script type="text/javascript" src="${jsonJs}"></script>
<script type="text/javascript" src="${textFlowIJs}"></script>
</body>

</html>
