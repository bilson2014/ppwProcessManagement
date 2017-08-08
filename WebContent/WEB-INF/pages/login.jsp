<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<html lang="en">
<head>
	<%@ include file="/resources/common/global.jsp"%>
	<title>KAD登录页 - 咖啡兔(闫洪磊)</title>
	<script>
		var logon = ${not empty user};
		if (logon) {
			location.href = '${ctx}/main/index';
		}
	</script>
	<%@ include file="/resources/common/meta.jsp" %>
	<%@ include file="/resources/common/include-jquery-ui-theme.jsp" %>
    <%@ include file="/resources/common/include-base-styles.jsp" %>
    <style type="text/css">
        .login-center {
            width: 600px;
            margin-left:auto;
            margin-right:auto;
        }
        #loginContainer {
            margin-top: 3em;
        }
        .login-input {
            padding: 4px 6px;
            font-size: 14px;
            vertical-align: middle;
        }
    </style>

    <script src="${ctx }/js/common/jquery-1.8.3.js" type="text/javascript"></script>
    <script src="${ctx }/js/common/plugins/jui/jquery-ui-${themeVersion }.min.js" type="text/javascript"></script>
    <script type="text/javascript">
	$(function() {
		$('button').button({
			icons: {
				primary: 'ui-icon-key'
			}
		});
	});
	</script>
</head>

<body>
    <div id="loginContainer" class="login-center">
        <c:if test="${not empty param.error}">
            <h2 id="error" class="alert alert-error">用户名或密码错误！！！</h2>
        </c:if>
        <c:if test="${not empty param.timeout}">
            <h2 id="error" class="alert alert-error">未登录或超时！！！</h2>
        </c:if>

		<div style="text-align: center;">
            <h2>项目流转演示</h2>
		</div>
		<hr />
		<form action="${ctx }/user/logon" method="get">
			<table>
				<tr>
					<td width="200" style="text-align: right;">用户名：</td>
					<td><input id="userName" name="userName" class="login-input" placeholder="用户名（见下左表）" /></td>
				</tr>
				<tr>
					<td style="text-align: right;">密码：</td>
					<td><input id="password" name="password" type="password" class="login-input" value="000000" placeholder="默认为：000000" /></td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td>
						<button type="submit">登录Demo</button>
					</td>
				</tr>
			</table>
		</form>
		<hr />
		<div>
            <div style="float:left; width: 48%;margin-right: 2%;">
                <table border="1">
                    <caption>用户列表</caption>
                    <tr>
                        <th width="50" style="text-align: center">用户名</th>
                        <th style="text-align: center">角色</th>
                    </tr>
                    <!-- <tr>
                        <td>admin</td>
                        <td>管理员、用户</td>
                    </tr>
                    <tr>
                        <td>kafeitu</td>
                        <td>用户</td>
                    </tr>
                    <tr>
                        <td>hruser</td>
                        <td>人事、用户</td>
                    </tr>
                    <tr>
                        <td>leaderuser</td>
                        <td>部门经理、用户</td>
                    </tr> -->
                    <tr>
                        <td>huge</td>
                        <td>销售总监</td>
                    </tr>
                    <tr>
                        <td>test</td>
                        <td>销售</td>
                    </tr>
                    <tr>
                        <td>liuchao</td>
                        <td>创意总监</td>
                    </tr>
                    <tr>
                        <td>zhanggaoge</td>
                        <td>监制总监</td>
                    </tr>
                    <tr>
                        <td>wangyi</td>
                        <td>供应商管家</td>
                    </tr>
                    <tr>
                        <td>chenjingna</td>
                        <td>供应商采购</td>
                    </tr>
                    <tr>
                        <td>zhangxiaoran</td>
                        <td>策划</td>
                    </tr>
                    <tr>
                        <td>guofang</td>
                        <td>财务</td>
                    </tr>
                    <tr>
                        <td>lihonglei</td>
                        <td>监制</td>
                    </tr>
                    <tr>
                        <td>panfeng</td>
                        <td>策划供应商</td>
                    </tr>
                    <tr>
                        <td>youwen</td>
                        <td>制作供应商</td>
                    </tr>
                    <tr>
                        <td>liufeng</td>
                        <td>供应商总监</td>
                    </tr>
                    <tr>
                        <td>yangwei</td>
                        <td>财务总监</td>
                    </tr>
                    <tr>
                        <td>yanxueqin</td>
                        <td>客服总监</td>
                    </tr>
                </table>
            </div>
            
		</div>
    </div>
</body>
</html>
