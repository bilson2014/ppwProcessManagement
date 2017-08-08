<%@page import="org.springframework.beans.factory.config.PropertiesFactoryBean"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<html lang="en">
<head>
	<%@ include file="/resources/common/global.jsp"%>
	<script>
		var notLogon = ${empty user};
		if (notLogon) {
			location.href = '${ctx}/login?timeout=true';
		}
	</script>
	<%@ include file="/resources/common/meta.jsp" %>
    <title>Activiti</title>
    <%@ include file="/resources/common/include-base-styles.jsp" %>
	<%@ include file="/resources/common/include-jquery-ui-theme.jsp" %>
    <link rel="stylesheet" type="text/css" href="${ctx }/resources/css/menu.css" />
    <%@ include file="/resources/common/include-custom-styles.jsp" %>
	<link href="${ctx }/resources/css/main.css" type="text/css" rel="stylesheet"/>
	<style type="text/css">
	.ui-tabs-panel {height: 100%; width: 100%;}
	.ui-tabs .ui-tabs-nav li a {padding-right: .5em;}
	#tabs li .ui-icon-close { float: left; margin: 0.5em 0.2em 0 0; cursor: pointer; }
	#add_tab { cursor: pointer; }
	</style>

    <script src="${ctx }/resources/js/common/jquery-1.8.3.js" type="text/javascript"></script>
    <script src="${ctx }/resources/js/common/plugins/jui/jquery-ui-${themeVersion }.min.js" type="text/javascript"></script>
    <script src="${ctx }/resources/js/common/plugins/jui/extends/themeswitcher/jquery.ui.switcher.js" type="text/javascript"></script>
    <script src="${ctx }/resources/js/common/plugins/tools/jquery.cookie.js" type="text/javascript"></script>
	<script src="${ctx }/resources/js/common/plugins/jui/extends/layout/jquery.layout.min.js?v=1.3" type="text/javascript"></script>
	<script src='${ctx }/resources/js/common/common.js' type="text/javascript"></script>
    <script src='${ctx }/resources/js/module/main/main-frame.js' type="text/javascript"></script>
</head>
<body>
<!-- #TopPane -->
<div id="topPane" class="ui-layout-north ui-widget ui-widget-header ui-widget-content">
	<div style="padding-left:5px; font-size: 16px; margin-top: 1px;">
       	<table id="topTable" style="padding: 0px;margin: 0px;margin-top: -5px" width="100%">
       		<tr>
       			<td width="40px">
       				<img src="${ctx }/resources/images/logo.png" height="48" align="top"  style="margin-top:5px" />
       			</td>
       			<td>
       				<span style="font-size: 17px;color:#FFFF33">Activiti<br/>演示系统</span><br/>
       			</td>
       			<td>
       				<div style="float:right; color: #fff;font-size: 12px;margin-top: 2px">
		        		<div>
		        			<label for="username">欢迎：</label>
		        			<span title="角色：${groupNames }">${user.firstName } ${user.lastName }/${user.id }</span>
		        		</div>
		        		<div style="text-align: right;">
		        			<a id="chang-theme" href="#">切换风格</a>
		       				<a href="#" id="loginOut">安全退出</a>
		        		</div>
		        	</div>
       			</td>
       		</tr>
       	</table>
       </div>
</div>

<!-- RightPane -->
<div id="centerPane" class="ui-layout-center ui-helper-reset ui-widget-content">
	<div id="tabs">
		<ul><li><a class="tabs-title" href="#tab-index">首页</a><span class='ui-icon ui-icon-close' title='关闭标签页'></span></li></ul>
		<div id="tab-index">
			<iframe id="mainIframe" name="mainIframe" src="welcome" class="module-iframe" scrolling="auto" frameborder="0" style="width:100%;height:100%;"></iframe>
		</div>
	</div>
</div>

<!-- #BottomPane -->
<div id="bottomPane" class="ui-layout-south ui-widget ui-widget-content">
	<div class="footer ui-state-default">
		<a href="#" target="_blank">Jack(郭阳)</a>
		<span class="copyright">©2017-2018</span>
	</div>
</div>
<%@ include file="menu.jsp" %>
<div id="themeswitcherDialog"><div id="themeSwitcher"></div></div>
</body>
</html>
