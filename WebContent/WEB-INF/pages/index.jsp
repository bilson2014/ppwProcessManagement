<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!-->
<html class="no-js">
<!--<![endif]-->
<body>
<h2>Hello World!</h2>

	<form action="/vacation/start" method="post">
		<table>
			<tr>
				<td>请假类型</td>
				<td>
					<select name="leaveType" >
						<option value="年假" selected="selected">年假</option>
						<option value="产假">产假</option>
						<option value="事假">事假</option>
						<option value="病假">病假</option>
						<option value="婚假">婚假</option>
						<option value="例假">例假</option>
					</select>
				</td>
				
			</tr>
			
			<tr>
				<td>开始时间：</td>
				<td><input name="startTime" value="2017-07-25 08:00:00"/></td>
				<td>截止时间：</td>
				<td><input name="endTime" value="2017-07-26 00:00:00"/></td>
			</tr>
			
			<tr>
				<td>
					<button type="submit" >提交</button>
				</td>
			</tr>
			
		</table>
	</form>
</body>
</html>
