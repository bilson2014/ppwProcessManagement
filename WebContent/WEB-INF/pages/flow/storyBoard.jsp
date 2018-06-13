<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%-- import CSS --%>
<spring:url value="/resources/css/flow/storyBoard.css" var="storyBoardCss"/>
<spring:url value="/resources/lib/webuploader/webuploader.css" var="webuploaderCss" />
<spring:url value="/resources/lib/jcrop/jquery.Jcrop.min.css" var="jcropCss"/>
<%-- import JS --%>
<spring:url value="/resources/lib/jquery/jquery-2.0.3.min.js" var="jqueryJs"/>
<spring:url value="/resources/lib/jqueryui/jquery-ui.min.js" var="jqueryuiJs"/>
<spring:url value="/resources/js/common.js" var="commonJs"/>
<spring:url value="/resources/js/flow/storyBoard.js" var="storyBoardJs"/>
<spring:url value="/resources/lib/webuploader/webuploader.js" var="webuploaderJs" />
<spring:url value="/resources/lib/jquery/ajaxfileupload.js" var="ajaxfileuploadJs"/>
<spring:url value="/resources/js/juicer.js" var="juicerJs" />
<spring:url value="/resources/lib/jcrop/jquery.Jcrop.min.js" var="jcropJs"/>
<spring:url value="/resources/lib/jcrop/jquery.color.js" var="jcropColorJs"/>
<spring:url value="/resources/lib/jquery.json/jquery.json-2.4.min.js" var="jsonJs"/>
<spring:url value="/resources/images" var="imgPath" />
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
<title>分镜工具</title>

<script type="text/javascript" src="${jqueryJs}"></script>
<script type="text/javascript" src="${jqueryuiJs}"></script>
<script type="text/javascript" src="${commonJs}"></script>
<script type="text/javascript" src="${webuploaderJs}"></script>
<script type="text/javascript" src="${ajaxfileuploadJs}"></script>
<script type="text/javascript" src="${juicerJs}"></script>
<script type="text/javascript" src="${jcropJs}"></script>
<script type="text/javascript" src="${jcropColorJs}"></script>
<script type="text/javascript" src="${jsonJs}"></script>

<link rel="stylesheet" href="${storyBoardCss}">
<link rel="stylesheet" href="${webuploaderCss}">
<link rel="stylesheet" href="${jcropCss}">

<link rel="shortcut icon" href="${imgPath }/favicon.ico" >
 <input type="hidden" id="id" value="${id}">


<!--[if lt IE 9]>
        <script>window.html5 || document.write('<script src="html5shivJs"><\/script>')</script>
    <![endif]-->

</head>

<body>
	
	<div class="page">
	       <div class="toolsHead">
	             <div id="projectName">未命名</div>
	             <div class="toolTitle">分镜工具</div>
	             <div class="openTool">打开</div>
	       </div>
	       <div class="setImg" id="setImg">
	              <div class="addItem" id="picker">
	                    <img src="/resources/images/flow/addImg.png">
	                    <div>点击添加镜头</div>
	                    <div>可上传JPG、PNG格式的文件，文件大小不能超过1M。</div>
	              </div>
	       </div>
	       
	       <div class="toolsHead">
	             <div class="sWord">补充视频信息</div>
	       </div>
	       
	       <div class="otherInfo">
	                 <div class="otherItem">
	                       <div class="otherTitle">分镜脚本名称</div>
	                       <input id="storyName">
	                 </div>
	               <!--   <div class="otherItem">
	                       <div class="otherTitle">产品线</div>
	                        <div class="orderSelect selectLine" id="isOther">
				                <div class="imgType" id="productLine">请选择产品</div>
				                <img src="/resources/images/flow/selectS.png">
				                <ul class="oSelect selectUl"  style="display: none;">
				                   <li data-id="0">全部</li>
				                   <li data-id="1">沟通阶段</li>
				                   <li data-id="2">方案阶段</li>
				                   <li data-id="3">商务阶段</li>
				                   <li data-id="4">制作阶段</li>
				                   <li data-id="5">交付阶段</li>
				                </ul>    
					     </div>
					      <div class="orderSelect" id="isOther">
				                <div class="imgType" id="productType">请选择视频类型</div>
				                <img src="/resources/images/flow/selectS.png">
				                <ul class="oSelect" id="orderCome" style="display: none;">
				                   <li data-id="0">全部</li>
				                   <li data-id="1">沟通阶段</li>
				                   <li data-id="2">方案阶段</li>
				                   <li data-id="3">商务阶段</li>
				                   <li data-id="4">制作阶段</li>
				                   <li data-id="5">交付阶段</li>
				                </ul>    
					     </div>
	                 </div> -->
	                  <div class="otherItem onebox" id="time">
	                       <div class="otherTitle oneboxTilte">视频时长</div>
	                       <div class="killDiv diy">
		                       <div class="boxItem" data-id="0">
		                            <div class="box"></div>
		                            <div class="des">30秒</div>
		                       </div>
		                       <div class="boxItem" data-id="1">
		                            <div class="box"></div>
		                            <div class="des">60秒</div>
		                       </div>
		                    </div>   
	                 </div>
	                  <div class="otherItem onebox" id="videoType">
	                       <div class="otherTitle oneboxTilte">画幅比例</div>
	                       <div class="killDiv diy">
		                       <div class="boxItem" data-id="0">
		                            <div class="box"></div>
		                            <div class="des">16:9&nbsp&nbsp(1920x1080)</div>
		                       </div>
		                       <div class="boxItem" data-id="1">
		                            <div class="box"></div>
		                            <div class="des">1:1&nbsp&nbsp(1024x1024)</div>
		                       </div>
		                       <div class="boxItem" data-id="2">
		                            <div class="box"></div>
		                            <div class="des">3:4&nbsp&nbsp(810x1080)</div>
		                       </div>
		                    </div>   
	                 </div>
	                 
	                 <div class="otherItem onebox a" id="videoStyleS">
	                       <div class="otherTitle lastTitle">影片风格</div>
	                       <div class="killDiv">
		                         <div class="killItem">
		                               <img src="/resources/images/flow/vStyle1.png"> 
				                       <div class="boxItem" data-id="0">
				                            <div class="box"></div>
				                            <div class="des">亚洲小清新</div>
				                       </div>
			                      </div> 
			                       <div class="killItem" >
		                               <img src="/resources/images/flow/vStyle2.png"> 
				                       <div class="boxItem" data-id="1">
				                            <div class="box"></div>
				                            <div class="des">韩国时尚风</div>
				                       </div>
			                      </div> 
			                       <div class="killItem" >
		                               <img src="/resources/images/flow/vStyle3.png"> 
				                       <div class="boxItem" data-id="2">
				                            <div class="box"></div>
				                            <div class="des">科技工业感</div>
				                       </div>
			                      </div> 
			                       <div class="killItem" >
		                               <img src="/resources/images/flow/vStyle4.png"> 
				                       <div class="boxItem" data-id="3">
				                            <div class="box"></div>
				                            <div class="des">复古时尚志</div>
				                       </div>
			                      </div> 
			                       <div class="killItem" >
		                               <img src="/resources/images/flow/vStyle5.png"> 
				                       <div class="boxItem" data-id="4">
				                            <div class="box"></div>
				                            <div class="des">欧美电影感</div>
				                       </div>
			                      </div> 
		                    </div>   
	                 </div>
	       </div>
	       
	       <div class="toolBtn">
	             <div class="saveProject" id="saveProject">保存至项目</div>
	             <div class="download">导出</div>
	       </div>
	       
	 
    </div>  
    
    
     

	<!-- 弹窗-->
	<div class="cusModel" id="loadProductModel">
	     <div class="modelCard">
	            <div class="cardTop">
	                   <div class="title">项目报价单</div>
	                   <div class="closeModel"></div>
	            </div>
	            <div class="modelBanner">
	                <div class="tap" id="" style="width:100%">您正在参与进行中的项目</div>
	            </div>
	            <div class="modelProductContent"></div>
	            <div class="modelControl">
	                 <div class="btn-c-g" id="cancleLoadProduct">取消</div>
	                 <div class="btn-c-r" id="CheckloadProduct">加载</div>
	            </div>     
	     </div>
	</div>
	
	 <div class="cusModel" id="checkSureModel">
           <div class="successModel">
               <div class="closeBtn"></div>
			   <div class="oSContent">
			        <div class="tdDes" style="padding-top:80px;">确认删除镜头吗?</div>
			        <div class="sureBtn" style="padding-top:40px;">
			           <div class="btn-c-r" id="tModel">确定</div>
			           <div class="btn-c-g" id="cModel">取消</div>
			        </div>
			   </div>
           </div>
      </div>
	
	<div class="tooltip-success-show"></div>
	
	<!-- photo Modal start -->
	<div class="cusModel" id="mymodal">
		<div class="modal-dialog">
			<div class="modal-content model-distance">
				<div class="modal-header model-no-border">
				          请选择照片区域
					<div class="closeBtn" id="closePhone"></div>
				</div>
				<div class="modal-body">
					<div class="modal-left">
						<div class="modal-original">
							<img id="modal-original-img">
						</div>
					</div>
					<div class="modal-right">
						<div class="modal-preview-container">
							<img alt="" src="" id="modal-preview">
						</div>
						<button class="btn btn-primary" type="button" id="uploadConfirmBt">确认</button>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- photo Modal end -->
	
	<form method="post" action="/continuity/export" id="toListForm" class="hide">
                        <input type="hidden" id="name" name="name" value="">
                        <input type="hidden" id="videoStyle" name="videoStyle" value="">
                        <input type="hidden" id="pictureRatio" name="pictureRatio" value="">
                        <input type="hidden" id="dimensionId" name="dimensionId" value="">
                        <input type="hidden" id="scriptContent" name="scriptContent" value="">
                        <input type="hidden" id="projectId" name="projectId" value="${projectId}">
     </form> 
    
	
	
</body>
<script type="text/javascript" src="${storyBoardJs}"></script>
</html>
