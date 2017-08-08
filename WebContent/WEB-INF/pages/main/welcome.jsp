<?xml version="1.0" encoding="UTF-8" ?>
<%@page import="com.paipianwang.activiti.utils.PropertyFileUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<!doctype html>
<html lang="en">
<head>
	<%@ include file="/resources/common/global.jsp"%>
	<%@ include file="/resources/common/meta.jsp"%>

	<%@ include file="/resources/common/include-base-styles.jsp" %>
    <%@ include file="/resources/common/include-jquery-ui-theme.jsp" %>
    <link href="${ctx }/resources/js/common/plugins/jui/extends/portlet/jquery.portlet.min.css?v=1.1.2" type="text/css" rel="stylesheet" />
    <link href="${ctx }/resources/js/common/plugins/qtip/jquery.qtip.css?v=1.1.2" type="text/css" rel="stylesheet" />
    <%@ include file="/resources/common/include-custom-styles.jsp" %>
    <style type="text/css">
    	.template {display:none;}
    	.version {margin-left: 0.5em; margin-right: 0.5em;}
    	.trace {margin-right: 0.5em;}
        .center {
            width: 1200px;
            margin-left:auto;
            margin-right:auto;
        }
    </style>

    <script src="${ctx }/resources/js/common/jquery-1.8.3.js" type="text/javascript"></script>
    <script src="${ctx }/resources/js/common/plugins/jui/jquery-ui-${themeVersion }.min.js" type="text/javascript"></script>
    <script src="${ctx }/resources/js/common/plugins/jui/extends/portlet/jquery.portlet.pack.js?v=1.1.2" type="text/javascript"></script>
    <script src="${ctx }/resources/js/common/plugins/qtip/jquery.qtip.pack.js" type="text/javascript"></script>
	<script src="${ctx }/resources/js/common/plugins/html/jquery.outerhtml.js" type="text/javascript"></script>
	<script src="${ctx }/resources/js/module/activiti/workflow.js" type="text/javascript"></script>
    <script src="${ctx }/resources/js/module/main/welcome-portlet.js" type="text/javascript"></script>
</head>
<body style="margin-top: 1em;">
	<div class="center">
        <div id='portlet-container'></div>
    </div>

</body>
</html>
