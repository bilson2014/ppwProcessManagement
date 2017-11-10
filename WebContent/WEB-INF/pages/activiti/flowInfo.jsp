<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="r" uri="/mytaglib" %>
<%-- import CSS --%>
<spring:url value="/resources/css/activiti/flowInfo.css" var="flowInfoCss"/>
<spring:url value="/resources/lib/AirDatepicker/dist/css/datepicker.min.css" var="datepickerCss" />
<spring:url value="/resources/lib/webuploader/webuploader.css" var="webuploaderCss" />
<spring:url value="/resources/lib/Bootstrap/css/bootstrap.min.css" var="bootstrapCss" />
<%-- import JS --%>
<spring:url value="/resources/lib/jquery/jquery-2.0.3.min.js" var="jqueryJs"/>
<spring:url value="/resources/js/common.js" var="commonJs"/>
<spring:url value="/resources/lib/AirDatepicker/dist/js/datepicker.min.js" var="datepickerJs" />
<spring:url value="/resources/lib/AirDatepicker/dist/js/i18n/datepicker.zh.js" var="datepickerZhJs" />
<spring:url value="/resources/js/activiti/textFlowI.js" var="textFlowIJs"/>
<spring:url value="/resources/lib/webuploader/webuploader.js" var="webuploaderJs" />
<spring:url value="/resources/images" var="imgPath" />
<spring:url value="/resources/lib/jquery.json/jquery.json-2.4.min.js" var="jsonJs" />
<spring:url value="/resources/lib/json/ezmorph.jar" var="ezmorphJs" />
<spring:url value="/resources/lib/clipboard/clipboard.min.js" var="zclipJs" />


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
<body class="bb">

<input type="hidden" value="${taskStage}" id="taskStage"/>
<input type="hidden" value="${taskId }" id="currentTaskId" />
<input type="hidden" value="${taskName}" id="taskName" />
<input type="hidden" value="${projectId}" id="projectId" />
<input type="hidden" value="${processInstanceId}" id="processInstanceId" />
<input type="hidden" value="${price_info}" id="price_info" />

	<input type="hidden" id="storage_node" value="${file_locate_storage_path }" />
	    <div class="pages">
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
	                     
	                     <r:group role="sale" role2="saleDirector">
	                     	<div class="proControl">
	                                                        	项目操作
		                         <div class="newControl">
		                              <a id="isPause" <%-- href="/project/suspendProcess/${processInstanceId}/${projectId}" --%>><div id="">暂停项目</div></a>
		                              <a id="isBack"  <%-- href="/project/activateProcess/${processInstanceId}/${projectId}" --%>><div id="">恢复项目</div></a>
		                              <a id="isCancle"<%--  href="/project/cancelProcess/${processInstanceId}/${projectId}" --%>><div id="">取消项目</div></a>
		                         </div>
	                     	</div>
	                     </r:group>
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
	                               <div class="listContent" id="listContent"></div>
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
	                            </div>
	                         </div>   
	                       </c:if>
	                        <c:if test="${not empty flow_info}"> 
	                       <div class="getInfoItem">
	                            <div class="getInfoItemTop">
	                                 <div class="controlOpen"></div>
	                                 <div class="info">项目信息</div>
	                                 <r:group role="sale" role2="saleDirector">
	                                 <div class="update btn-c-r" id="openProjectInfo">更新</div>
	                                 </r:group>
	                            </div>
	                            <div class="getInfoItemContent">
	                                  <div class="contentItem">
	                                         <div class="contentItem">
			                                  <c:if test="${not empty flow_info['projectId']}">
			                                  		<div class="item">
				                                          <div>项目编号</div>
				                                          <div>${flow_info["projectId"]}</div>
			                                  		</div>
			                                  </c:if>
			                                  
			                                  <c:if test="${not empty flow_info['projectName']}">
					                                  <div class="item">
				                                          <div>项目名称</div>
				                                          <div>${flow_info["projectName"]}</div>
					                                  </div>
			                                  </c:if>
			                                  
			                                   <c:if test="${not empty flow_info['projectGrade']}">
			                                   		<div class="item">
				                                          <div>项目评级</div>
				                                          <div>${flow_info["projectGrade"]}</div>
				                                    </div>
			                                   </c:if>
			                                  
			                                  <c:if test="${not empty flow_info['projectSource']}">
			                                  		<div class="item">
				                                          <div>项目来源</div>
				                                          <div>${flow_info["projectSource"]}</div>
					                                 </div>
			                                  </c:if>
		                                  </div>
	                                  </div>
	                                   <div class="contentItem">
	                                         <div class="contentItem">
	                                         <c:if test="${not empty flow_info['createDate']}">
	                                         	<div class="item">
			                                          <div>立项时间</div>
			                                          <div id="projectTime">
			                                          	${flow_info["createDate"]}
			                                          </div>
			                                  	</div>
	                                         </c:if>
	                                         
			                                 <c:if test="${not empty flow_info['productName']}">
			                                 	<div class="item">
			                                          <div>项目周期</div>
			                                          <c:if test="${flow_info['projectCycle'] == 0}">
			                                     		     <div>待定</div>
			                                     	  </c:if>
			                                     	   <c:if test="${flow_info['projectCycle'] > 0}">
			                                     		     <div>${flow_info["projectCycle"]}</div>
			                                     	  </c:if>
			                                          
				                                </div>
				                                <div class="item">
				                                          <div>产品线</div>
				                                          <div>${flow_info["productName"]}</div>
				                                </div>
			                                 </c:if>
		                                  </div>
	                                  </div>
	                                  
	                                  <c:if test="${not empty flow_info['productConfigLevelName']}">
		                                  	<div class="longItem">
	                                          <div>项目配置</div>
	                                          <div>${flow_info["productConfigLevelName"]}
		                                          <c:if test="${!empty flow_info['productConfigLengthName']}"> 
		                                              +  ${flow_info['productConfigLengthName']}
		                                          </c:if>
		                                          <c:if test="${not empty flow_info['productConfigAdditionalPackageName']}"> 
		                                              +  ${flow_info['productConfigAdditionalPackageName']}
		                                          </c:if>
	                                          </div>
			                           </div>
	                                  </c:if>
	                                  
	                                  <c:if test="${not empty flow_info['filmDestPath']}">
		                                  	<div class="longItem">
	                                          <div>对标影片</div>
	                                          <div><a href="${flow_info['filmDestPath']}" target="_blank">${flow_info["filmDestPath"]}</a></div>
				                            </div>
	                                  </c:if>
			                           
			                           <c:if test="${not empty flow_info['projectDescription']}">
			                           		<div class="longItem">
	                                          <div>项目描述</div>
	                                          <div>${flow_info["projectDescription"]}</div>
				                           </div>
			                           </c:if>
			                           
			                           <c:if test="${not empty flow_info['sampleUrl']}">
		                                  	<div class="longItem">
	                                          <div>水印样片地址</div>
	                                          <div><a href='${flow_info["sampleUrl"]}' target="_blank">${flow_info["sampleUrl"]}</a></div>
				                            </div>
	                                  </c:if>
	                                  
	                                  <c:if test="${not empty flow_info['samplePassword']}">
		                                  	<div class="longItem">
	                                          <div>水印样片密码</div>
	                                          <div>${flow_info["samplePassword"]}</div>
				                            </div>
	                                  </c:if>
	                            </div>
	                       </div>
	                       
	                        </c:if> 
	                      <c:if test="${!empty user_info}"> 
		                      <div class="getInfoItem">
		                            <div class="getInfoItemTop">
		                                 <div class="controlOpen"></div>
		                                 <div class="info">客户信息</div>
		                                 <r:group role="sale" role2="saleDirector">
		                                 <div class="update btn-c-r" id="openCusInfo">更新</div>
		                                 </r:group>
		                            </div>
		                            <div class="getInfoItemContent">
		                                  <div class="contentItem">
		                                  		<c:if test="${not empty user_info['userName']}">
				                                  <div class="item">
			                                          <div>客户名称</div>
			                                          <div>${user_info["userName"]}</div>
				                                  </div>
				                                 
		                                  		</c:if>
		                                  		
		                                  		<c:if test="${not empty user_info['userLevel']}">
		                                  			<div class="item">
			                                          <div>客户评级</div>
			                                          <div>${user_info["userLevel"]}</div>
				                                    </div>
		                                  		</c:if>
			                                  
			                                  <c:if test="${not empty user_info['linkman']}">
			                                  	  <div class="item">
			                                          <div>客户联系人</div>
			                                          <div>${user_info["linkman"]}</div>
				                                  </div>
			                                  </c:if>
			                                  
			                                  <c:if test="${not empty user_info['telephone']}">
			                                  	<div class="item">
		                                          <div>客户电话</div>
		                                          <div>${user_info["telephone"]}</div>
			                                    </div>
			                                  </c:if>
			                                  
			                                  <c:if test="${not empty user_info['email']}">
			                                  	<div class="item">
		                                          <div>邮箱地址</div>
		                                          <div>${user_info["email"]}</div>
			                                    </div>
			                                  </c:if>
		                                  </div>
		                            </div>
		                       </div>
	                        </c:if>
	                        
	                        <c:if test="${not empty teamProduct_info || not empty teamPlan_info}">
	                        <div class="getInfoItem">
	                            <div class="getInfoItemTop">
	                                 <div class="controlOpen"></div>
	                                 <div class="info">供应商信息</div>
	                                 <div class="time"></div>

	                                 <r:addProductTeam projectId="${projectId}">
	                                     <div class="addPro">新增</div>
	                                 </r:addProductTeam >
	                                 <r:group role="teamProvider" role2="teamDirector">
	                                 <div class="update btn-c-r" id="openProvider">更新</div>
	                                 </r:group>
	                            </div>
	                            <div class="getInfoItemContent">
	                            	<c:if test="${not empty teamPlan_info}">
	                            		<div class="title"><div class="long"></div><div class="short"></div>策划供应商</div>
	                            			<c:forEach items="${teamPlan_info }" var="plan">
			                                  <div class="contentItem">
			                                  		<c:if test="${not empty plan['teamName']}">
					                                     <div class="item">
					                                          <div>供应商名称</div>
					                                          <div>
						                                         ${plan["teamName"]}
					                                           </div>
							                             </div>
						                             </c:if>
						                             
						                             <c:if test="${not empty plan['linkman']}">
							                             <div class="item">
					                                          <div>供应商联系人</div>
					                                          <div>
					                                          	${plan["linkman"]}
					                                          </div>
							                             </div>
						                             </c:if>
						                             
						                             <c:if test="${not empty plan['telephone']}">
							                             <div class="item">
					                                          <div>供应商联系电话</div>
					                                          <div>
						                                          ${plan["telephone"]}
					                                          </div>
							                             </div>
						                             </c:if>
						     
						                             <c:if test="${not empty plan['email']}">
							                             <div class="item">
						                                          <div>供应商邮箱</div>
						                                          <div>${plan["email"]}</div>
								                           </div>
							                           </c:if>
												</div>
	                            			</c:forEach>
	                            	</c:if>
	                               
	                             <c:if test="${!empty teamProduct_info}">
		                                  <div class="title"><div class="long"></div><div class="short"></div>制作供应商</div>
			                                  	<div class="contentItem" id="makeProvItem">
			                                             	  <c:forEach items="${teamProduct_info }" var="product">
						                             	<c:if test="${not empty product['teamName']}">
						                             		<div class="item">
					                                          <div>供应商名称</div>
					                                          <div>${product["teamName"]}</div>
							                             	</div>
						                             	</c:if>
					                                     <c:if test="${not empty product['linkman']}">
					                                     	<div class="item">
					                                          <div>供应商联系人</div>
					                                          <div>${product["linkman"]}</div>
							                             	</div>
					                                     </c:if>
							                             
							                             <c:if test="${not empty product['telephone']}">
							                             	<div class="item">
					                                          <div>供应商联系电话</div>
					                                          <div>${product["telephone"]}</div>
							                             	</div>
							                             </c:if>
									                          <c:if test="${not empty product['flag']}">
									                             <div class="item smallItem">
							                                          <div>状态</div>
							                                           <c:if test="${product['flag'] == 0}">
							                                                     <div class='${product["projectTeamId"]}' style="color:green">正常</div>                                           
							                                           </c:if>
							                                            <c:if test="${product['flag'] == 1}">
							                                                     <div class='${product["projectTeamId"]}' style="color:#fe5453">已删除</div>
							                                           </c:if>
									                             </div>
								                             </c:if>
								                           <c:if test="${product['flag'] == 0}">
							                                  <div class="item smallItem">
							                                          <div>操作</div>
							                                          <div class="delPro" data-id='${product["teamId"]}' data-idp='${product["projectTeamId"]}' >删除</div>
									                          </div>                                       
						                                   </c:if>
						                                   <c:if test="${not empty product['email']}">
									                             <div class="item">
								                                          <div>供应商邮箱</div>
								                                          <div>${product["email"]}</div>
										                           </div>
							                           		</c:if>
							                           		<c:if test="${not empty product['makeContent']}">
							                             <div class="item">
						                                          <div>供应商制作内容</div>
						                                          <div>${product["makeContent"]}</div>
								                           </div>
							                           </c:if>
							                           </br>
							                           </c:forEach>
								                 </div>                                       
							              </c:if>
	                                  </div>
	                            </div>
	                      </c:if> 
	                      
	                       <c:if test="${!empty price_info}">
	                       <div class="getInfoItem">
	                            <div class="getInfoItemTop">
	                                 <div class="controlOpen"></div>
	                                 <div class="info">价格信息</div>
	                                 <r:group role="sale" role2="saleDirector">
	                                 <div class="update btn-c-r" id="openPriceInfo">更新</div>
	                                 </r:group>
	                            </div>
	                            <div class="getInfoItemContent">
	                                  <div class="contentItem">	      
	                                     	<c:if test="${not empty price_info['estimatedPrice']}">
			                                     <div class="item">
			                                     		<div>预估价格</div>
			                                     		<c:if test="${price_info['estimatedPrice'] == 0}">
			                                     		     <div>待定</div>
			                                     		</c:if>
			                                     		<c:if test="${price_info['estimatedPrice'] > 0}">
			                                     		   <div>${price_info["estimatedPrice"]}</div>
			                                     		</c:if>
			                                          	
					                             </div>
	                                     	</c:if>
	                                     	
	                                     	<c:if test="${not empty price_info['projectBudget']}">
	                                     		<div class="item">
			                                          <div>客户项目预算</div>
			                                          <c:if test="${price_info['projectBudget'] == 0}">
			                                             <div>待定</div>
			                                          </c:if> 
			                                          <c:if test="${price_info['projectBudget'] > 0}">
			                                             <div>${price_info['projectBudget']}</div>
			                                          </c:if>  
					                             </div>
	                                     	</c:if>
							         </div>
	                            </div>
	                       </div>
	                        </c:if>
	                                              
	                   <div class="projectTitle margin-top">项目文件
		                   <r:group role="sale" role2="saleDirector" role3="scheme">
		                        <div class="conMod btn-c-r">版本管理</div>
		                        <div class="upFile btn-c-r">文件更新</div>
		                   </r:group>
	                   </div>
	                   <div class="noFile">暂无文件上传...</div>
	                   <div class="projectFilm" id="projectFilm"></div>
	                   <!-- 留言区的权限判断 -->
	              
	                    <r:identity role="employee">
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
	                	</r:identity>
	                	<r:identity role="customer">
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
	                	</r:identity>
	                
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
	             <!--留言区的权限判断  -->
	             <r:identity role="customer">
	             	<div class="itemHeight" id="itemHeightInfo"></div>
	             </r:identity>
	             <r:identity role="employee">
	             	<div class="itemHeight" id="itemHeightInfo"></div>
	             </r:identity>
	             
	             <r:identity role="provider">
	             </r:identity>
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
                   <div class="closeModel" id="singleCacnleEven"></div>
            </div>
            
            <div class="upProgress singleProgress" style="margin-bottom:40px;" id="singleUp">
								<div class="proTitle" id="proTitle">上传进度</div>
								<div  class="progress progress-striped active">
									<div id="singleSetWidth" class="progress-bar progress-bar-danger progress-bar-striped" role="progressbar"
										aria-valuenow="0" aria-valuemin="0" aria-valuemax="100"
										style="width: 0;"></div>
								</div>
								<div class="upIng">上传中...</div>
								<div class="upSuccess">
									<img src="/resources/images/provider/sure.png">上传成功
								</div>
								<div class="upError">
									<img src="/resources/images/provider/error.png">上传失败,请关闭窗口重新上传
								</div>
		   </div>
            
            <div class="upContent" id="upContent">
                 <div class="item errorItem" id="errorType">
                      <div class="title">选择分类</div>
                      <div class="orderSelect" >
			                <div id="hasFile"></div>
			                <img src="${imgPath}/flow/selectOrder.png">
			                <ul class="oSelect searchSelect" id="orderType">
			                   <li data-id="需求文档">需求文档</li>
			                   <li data-id="Q&amp;A文档">Q&amp;A文档</li>
			                   <li data-id="排期表">排期表</li>
			                   <li data-id="策划方案">策划方案</li>
			                   <li data-id="报价单">报价单</li>
			                   <li data-id="制作导演信息">制作导演信息</li>
			                   <li data-id="分镜头脚本">分镜头脚本</li>
			                   <li data-id="花絮">花絮</li>
			                   <li data-id="成片">成片</li>
			                </ul>    
				        </div>
                 </div>
                  <div class="item">
                      <div class="title">选择文件</div>
                      <input id="getFileName">
                      <div class="findFile" id="findFile">上传</div>
                 </div>
                 <div class="btnMid">
                      <div class="btn-c-g" style="position: relative;left: 59px;" id="singleCacnle">取消</div>
                      <div class="btn-c-r hide" id="singleUpEv">上传</div>
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

 <!-- 提示 -->
<div class="cusModel" id="warnModel">
     <div class="modelCard smallModel">
            <div class="cardTop">
                   <div class="title">提示</div>
                   <div class="closeModel"></div>
            </div>
            <div class="warnContent">
                     <img src="/resources/images/flow/warn.png">
                    <div class="info">内容内容内容内容内容内容内容内容内容内容</div>
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
<div class="cusModel" id="showProjectInfo">
     <div class="modelCard">
            <div class="cardTop">
                   <div class="title">项目信息修改</div>
                   <div class="closeModel"></div>
            </div>
           <form method="post" action="/project/edit/information" id="toProjectForm"> 
            <input type="hidden" id="proId" name="pf_projectId" value="${flow_info['projectId']}">
            <div class="getPriceContent">
                    <div class="item errorItem" id="proNameError">
                         <div class="title">项目名称</div>
                         <input id="proName" name="pf_projectName" value="">
                    </div>
                    <div class="itemTime errorItem" id="pf_projectGradeError">
                         <div class="title">项目评级</div>
                         <input type="hidden" id="pf_projectGradeInput" name="pf_projectGrade" value="">
                         <div class="orderSelect so" >
				                <div id="pf_projectGrade"></div>
				                <img src="${imgPath}/flow/selectOrder.png">
				                <ul class="oSelect" id="orderCome">
					                   <li data-id="5">S</li>
					                   <li data-id="4">A</li>
					                   <li data-id="3">B</li>
					                   <li data-id="2">C</li>
					                   <li data-id="1">D</li>
					                   <li data-id="0">E</li>
				                </ul>    
					      </div>
                    </div>
                    <div class="itemTime errorItem" id="pf_ResourInputError">
                         <div class="title">项目来源</div>
                         <input type="hidden" id="pf_ResourInput" name="pf_projectSource" value="">
                         <div class="orderSelect " >
				                <div id="pf_Resour"></div>
				                <img src="${imgPath}/flow/selectOrder.png">
				                <ul class="oSelect" id="pResour">
				                </ul>    
					      </div>
                    </div>
                    <div class="itemTime errorItem" id="proCycleError">
                         <div class="title">项目周期</div>
                         <input id="proCycle" name="pf_projectCycle">
                         <div class="yuan">天</div>
                    </div>
     
                    <div class="itemTime bBigSlow errorItem" id="proFdpError">
                         <div class="title">对标影片</div>
                         <input id="proFdp"  name="pf_filmDestPath" value="">
                    </div>
                    
                    <div class="item errorItemArea" id="projectDesError">
                         <div class="title areaTitle">项目描述</div>
                         <textarea id="projectDes"  name="pf_projectDescription"></textarea>
                    </div>
                    
                    <div class="btnMid">
		                      <div class="btn-c-g">取消</div>
		                      <div class="btn-c-r" id="submitProject">确认</div>
		            </div>
            </div>
           </form>   
</div>
</div>

<!-- 客户信息修改 -->
<div class="cusModel" id="showCusInfo">
     <div class="modelCard">
            <div class="cardTop">
                   <div class="title">客户信息修改</div>
                   <div class="closeModel"></div>
            </div>
            <form method="post" action="/project/edit/information" id="toCusForm">
             <input type="hidden" id="proId" name="projectId" value="${flow_info['projectId']}">
	            <div class="getPriceContent">
	                    <input type="hidden" id="cusId" name="pu_projectUserId">
	                    <div class="itemTime errorItem" id="cusLinkmanError">
	                         <div class="title">客户联系人</div>
	                         <input id="cusLinkman" name="pu_linkman" value=''>
	                    </div>
	                    <div class="itemTime errorItem" id="cusTelephoneError">
	                         <div class="title">客户电话</div>
	                         <input id="cusTelephone" name="pu_telephone" value=''>
	                    </div>
	                    <div class="itemTime errorItem" id="cusEmailError">
	                         <div class="title">邮箱地址</div>
	                         <input id="cusEmail" name="pu_email" value=''>
	                    </div>
	                    <div class="btnMid">
			                      <div class="btn-c-g">取消</div>
			                      <div class="btn-c-r" id="submitCus">确认</div>
			            </div>
	            </div>
	        </form>      
</div>
</div>

<!-- 供应商信息修改 -->
<div class="cusModel" id="showProvider">
     <div class="modelCard">
            <div class="cardTop">
                   <div class="title">供应商信息修改 </div>
                   <div class="closeModel"></div>
            </div>
            <div class="getPriceContent">
             <form method="post" action="/project/edit/teamInformation" id="toProForm">
             <input type="hidden" id="proId" name="projectId"" value="${flow_info['projectId']}">
                    <div id="isHideTop">
	                    <div class="bigTitle">策划供应商</div>
	                    <div class="setHideTop"></div>
	                    
	                   <!--  <div class="setHideTop">
	                    <input type="hidden" id="scId"  name="pt_projectTeamId">
	                    <input type="hidden" id="scTeamId"  name="pt_teamId">
	                    <div class="itemTime errorItem" id="scTeamError">
	                         <div class="title">供应商团队</div>
	                         <input class="checkError" id="scTeamName" name="pt_teamName">
	                         <ul class="utoInfoTeam"></ul>
	                    </div>
	                    <div class="itemTime errorItem" id="scLinkError">
	                         <div class="title">供应商联系人</div>
	                         <input class="checkError" id="scLink" name="pt_linkman">
	                    </div>
	                    <div class="itemTime errorItem" id="scTelError">
	                         <div class="title">供应商联系电话</div>
	                         <input class="checkErrorP" id="scTel" name="pt_telephone">
	                    </div>
	                    </div> -->
                   </div> 
                   
                  <div id="isHideBot"> 
	                    <div class="bigTitle">制作供应商</div>
	                    <div class="setHideBot"></div>
	                    
	                   <!--  <input type="hidden" id="prId" name="pt_projectTeamId">
	                    <input type="hidden" id="prTeamId"  name="pt_teamId">
	                    <div class="itemTime errorItem" id="prTeamError">
	                         <div class="title">供应商团队</div>
	                         <input class="checkError" id="prTeamName" name="pt_teamName">
	                         <ul class="utoInfoMakeTeam"></ul>
	                    </div>
	                    <div class="itemTime errorItem" id="prLinkError">
	                         <div class="title">供应商联系人</div>
	                         <input class="checkError" id="prLink" name="pt_linkman">
	                    </div>
	                    <div class="itemTime errorItem" id="prTelError">
	                         <div class="title">供应商联系电话</div>
	                         <input class="checkErrorP" id="prTel" name="pt_telephone">
	                    </div> -->
                   </div> 
                 </form>  
                    <div class="btnMid">
		                      <div class="btn-c-g">取消</div>
		                      <div class="btn-c-r" id="submitProvide">确认</div>
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
	            <div class="cardTop autoSetTop">
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
									<div class="upSuccess">
										<img src="/resources/images/provider/sure.png">上传成功
									</div>
									<div class="upError">
										<img src="/resources/images/provider/error.png">上传失败,请关闭窗口重新上传
									</div>
								</div>
	            <div class="otherContent otherContentItem" id="setAutoInfo"></div>
	</div>
</div>

<!-- 价格信息修改 -->
<div class="cusModel" id="showPriceInfo">
     <div class="modelCard">
            <div class="cardTop">
                   <div class="title">价格信息修改 </div>
                   <div class="closeModel"></div>
            </div>
            <form method="post" action="/project/edit/information" id="toPriceForm">
	            <div class="getPriceContent">
	                    <input type="hidden" id="priceId" name="pf_projectId">
	                    <div class="itemTime errorItem" id="estError">
	                         <div class="title">预估价格</div>
	                         <input id="est" name="pf_estimatedPrice">
	                         <div class="yuan syuan">元</div>
	                    </div>
	                    <div class="itemTime errorItem" id="pjsError">
	                         <div class="title">客户项目预算</div>
	                         <input id="pjs" name="pf_projectBudget">
	                         <div class="yuan syuan">元</div>
	                    </div>
	                    <div class="btnMid">
			                      <div class="btn-c-g">取消</div>
			                      <div class="btn-c-r" id="sumbitPrice">确认</div>
			            </div>
	            </div>
            </form>
</div>
</div>

<!-- 提示信息 -->
<div class="cusModel" id="infoModel" >
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

 <!-- 提示信息 -->
<div class="cusModel" id="isReView" >
     <div class="modelCard">
            <div class="cardTop">
                   <div class="title">恢复确认</div>
                   <div class="closeModel"></div>
            </div>
            <div class="warnInfo">确认恢复吗</div>
           <a id="isBack"  href="/project/activateProcess/${processInstanceId}/${projectId}">
			    <div class="btnMid margin-bottom">
	                      <div class="btn-c-g" id="cancle">取消</div>
	                      <div class="btn-c-r">确认</div>
	            </div>
	       </a>     
      </div>    
</div>

 <!-- 提示信息 -->
<div class="cusModel" id="isPauseModel">
     <div class="modelCard">
            <div class="cardTop">
                   <div class="title">暂停确认</div>
                   <div class="closeModel"></div>
            </div>
            <form method="post" action="/project/suspendProcess/${processInstanceId}/${projectId}" id="toProjectpause"> 
               <div class="ReasonItem" id="puaseReasonError">
		            <div class="warnReason">暂停原因</div>
		            <textarea id="puaseReason" name="remark"></textarea>
	           </div>  
			    <div class="btnMid margin-bottom">
	                      <div class="btn-c-g" id="cancle">取消</div>
	                      <div class="btn-c-r" id="checkpause">确认</div>
	            </div>
            </form>
      </div>    
</div>
 <!-- 提示信息 -->
<div class="cusModel" id="isCancleModel">
     <div class="modelCard">
            <div class="cardTop">
                   <div class="title">取消确认</div>
                   <div class="closeModel"></div>
            </div>
            <form method="post" action="/project/cancelProcess/${processInstanceId}/${projectId}" id="toProjectcancle"> 
               <div class="ReasonItem" id="cancleReasonError">
		            <div class="warnReason">取消原因</div>
		            <textarea id="cancleReason" name="remark"></textarea>
	           </div>  
			    <div class="btnMid margin-bottom">
	                      <div class="btn-c-g" id="cancle">取消</div>
	                      <div class="btn-c-r" id="checkcancle">确认</div>
	            </div>
            </form>
      </div>    
</div>
 <!-- 提示信息 -->
<div class="cusModel" id="isCopy">
     <div class="modelCard">
            <div class="cardTop">
                   <div class="title">分享</div>
                   <div class="closeModel"></div>
            </div>
		        <a class="copyUrl" id="setInfoCopy"></a>
		        <div class="midDiv">
	               <button class="btnShare btn-c-r" data-clipboard-action="copy" data-clipboard-target="#setInfoCopy">复制链接</button>  
	            </div>
      </div>    
</div>

<!-- 新增供应商 -->
<div class="cusModel" id="createProivder">
	<div class="modelCard">
	            <div class="cardTop">
	                   <div class="title">新增供应商</div>
	                   <div class="closeModel"></div>
	            </div>
	            <div class="otherContent otherContentCreate" id="ctc">
	                 <input type="hidden" id="prov_teamId" name="pt_teamId" class="checkProvError">
	                 <div class="itemCard errorItem"><div class="title">供应商名称<span> *</span></div>
		                 <input type="text" id="prov_teamName" name="pt_teamName" class="uploadInput  required checkProvError" value="">
		                 <ul class="utoInfo createUi" style="display: none;"></ul>
	                 </div>
	                 <div class="itemCard errorItem"><div class="title">供应商联系人<span>*</span></div>
		                 <input type="text" id="prov_linkman" name="pt_linkman" class="uploadInput  required checkProvError" value="">
	                 </div>
	                 <div class="itemCard errorItem"><div class="title">供应商电话<span>*</span></div>
		                 <input type="text" id="prov_telephone" name="pt_telephone" class="uploadInput  required checkProvErrorP" value="">
	                 </div>
	                  <div class="itemCard errorItem"><div class="title">供应商邮箱<span>*</span></div>
		                 <input  type="text" id="prov_email" name="pt_email" class="uploadInput  required checkProvErrorEmail" value="">
	                 </div>
	                 <div class="itemCard errorItem"><div class="title">供应商预算<span>*</span></div>
		                 <input type="text" id="prov_budget" name="pt_budget" class="uploadInput  required checkProvError" value="">
	                 </div>
	                  <div class="itemCard errorItem"><div class="title">供应商制作内容<span>*</span></div>
		                 <input type="text" id="prov_makeContent" name="pt_makeContent" class="uploadInput  required checkProvError" value="">
	                 </div>
	                  <div class="itemCard errorItem"><div class="title">供应商制作时间<span>*</span></div>
		                 <input  type="text" readonly id="prov_makeTime" name="pt_makeTime" class="date uploadInput  required checkProvError" value="">
	                 </div>
	                
	                  <div class="itemCard errorItem"><div class="title">供应商启动函备注信息<span></span></div>
		                 <input  type="text" id="comment" name="comment" class="uploadInput  required" value="">
	                 </div>
	                 <div class="btnMid margin-bottom">
	                      <div class="btn-c-g" id="cancleCprov">取消</div>
	                      <div class="btn-c-r" id="checkCprov">确认</div>
	                 </div>
	            </div>
	</div>
</div>

<!-- 新增供应商删除 -->
<div class="cusModel" id="errorDelProv" >
     <div class="modelCard">
         
            <div class="cardTop">
                   <div class="title">删除供应商确认</div>
                   <div class="closeModel"></div>
            </div>
               <div class="ReasonItem" id="cancleReasonError">
		            <div class="warnReason">删除原因</div>
		            <textarea id="cancleProveReason" name="remark"></textarea>
		            <div class="error" id="errorProveReason" style="color:#fe5453;font-size:12px">error</div>
	           </div>  
			    <div class="btnMid margin-bottom">
	                      <div class="btn-c-g" >取消</div>
	                      <div class="btn-c-r" id="checkReasopnProv">确认</div>
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
<!-- <form class="dynamic-form" method="post" action="/project/task/complete/47853">
    <input value="panfeng" placeholder="panfeng" name="pt_teamName" />
</form> -->
<script type="text/javascript" src="${jqueryJs}"></script>
<script type="text/javascript" src="${commonJs}"></script>
<script type="text/javascript" src="${dynamicJs}"></script>
<script type="text/javascript" src="${datepickerJs}"></script>
<script type="text/javascript" src="${datepickerZhJs}"></script>
<script type="text/javascript" src="${webuploaderJs}"></script>
<script type="text/javascript" src="${zclipJs}"></script>
<script type="text/javascript" src="${jsonJs}"></script>
<script type="text/javascript" src="${textFlowIJs}"></script>
</body>

</html>
