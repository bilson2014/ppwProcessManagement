<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>


<spring:url value="/resources/css/activiti/aa.css" var="aaCss"/>
<spring:url value="/resources/lib/jquery/jquery-2.0.3.min.js" var="jqueryJs"/>
<spring:url value="/resources/js/common.js" var="commonJs"/>
<spring:url value="/resources/js/activiti/saveInfo.js" var="saveInfoJs"/>



<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!-->
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=9,chrome=1">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="keywords" content="">
<meta name="description" content="">
<meta name="baidu-site-verification" content="dMz6jZpIwd" />
<title></title>

  <link rel="stylesheet" href="${aaCss}">

<script type="text/javascript" src="resources/lib/Clamp/clamp.js"></script>
<script type="text/javascript" src="${jqueryJs}"></script>
<script type="text/javascript" src="${commonJs}"></script>
  <script type="text/javascript" src="${saveInfoJs}"></script>

<!--[if lt IE 9]>
        <script>window.html5 || document.write('<script src="html5shivJs"><\/script>')</script>
    <![endif]-->
</head>
<body>
  <!--main部分  -->
<div class="main">
        <div class="right" id="right">
            <!--信息模块  -->
            <div class="infos" id="infos">
                <div class="first">
                    <span class="text">员工头像</span>    
                    <div class="user-icon">
                        <img alt="用户头像" src="../resources/images/provider/initLogo.png" class="img-circle" id="user-img" />
                        <form:input type="hidden" path="teamPhotoUrl" id="user_img_url" />
                    </div>                 
                    <p>你可以上传JPG、GIF或PNG格式的文件，文件大小不能超过2M</p>
                    <!-- <div class="btn"> 上传头像</div> -->
                    <div class="upload-btn">
                        <!-- <button class="btn btn-primary" id="uploadBt" type="button">上传头像</button> -->
                        <div id="uploadBt">上传头像</div>
                        <input type="file" id="file" style="display: none;" />
                    </div>

                    <div class="name">
                        <span>员工姓名</span>
                        <span>攀峰尼</span>
                    </div>
                </div>
                <div class="second">
                    <div class="password">
                        <span>登录密码</span>
                        <span>使用账号登录时需要输入的密码</span>
                    </div>
                    <div class="username">
                        <span>用户名</span>
                        <span>未设置</span>
                    </div>
                    <div class="but" id="password">
                        设置
                    </div>
                </div>
                <div class="third">
                    <div class="phone">
                        <span>手机绑定</span>
                        <span>确保账号登录时的安全性，同时作为密码找回身份验证使用</span>
                    </div>
                    <div class="oldphone">
                        <span>原手机号</span>
                        <span id="nowphone">181****6793</span>
                    </div>
                    <div class="but" id="phone">
                        设置
                    </div>
                </div>
                <div class="four">
                    <div class="mail">
                        <span>邮箱绑定</span>
                        <span>作为项目邮件确认函工具使用，同时作为密码找回身份验证使用</span>
                    </div>
                    <div class="oldmail">
                        <span>现用邮箱</span>
                        <span id='nowmail'>dahfkdfakj@163.com</span>
                    </div>
                    <div class="but" id='emil'>
                        设置
                    </div>
                </div>
            </div>
            <!--密码模块  -->
            <div class="pas" id="pas">
                <div class="login">
                    <span>登录密码</span>
                    <span>使用账号登录时需要输入的密码</span>
                </div>
                <div class="newpas">
                    <span>新密码</span>
                    <input id='inputpas' type="password"></input>
                    <p></p>
                    <!-- <div id="correct">√</div>
                    <div id='mistake'>×</div> -->

                </div>
                <div class="reppas">
                    <span>确认密码</span>
                    <input id='inputrep' type="password"></input>
                    <div id="correctagn">√</div>
                    <div id='mistakeagn'>×</div>
                </div>
                <!-- <div class="verify">
                    <span>验证码</span>
                    <input id="inputver"></input>
                    <span>发送验证码</span>
                    <p>*验证码错误</p>
                </div> -->
                <div class="btn">
                    <span id="pascancel">取消</span>
                    <span id="saverep">保存</span>
                </div>
            </div>
            <!--手机模块  -->
            <div class="pho" id="pho">
                <div class="login">
                    <span>手机绑定</span>
                    <span>确保账号登录时的安全性，同时作为密码更改，密码找回，支付时验证身份使用</span>
                </div>
                <div class="phone">
                    <span>原手机号</span>
                    <span>181****6793</span>
                </div>
                <!-- <div class="verifyphone">
                    <span>验证手机</span>
                    <input id="inputverpho"></input>
                    <span>发送验证码</span>
                    <p></p>
                </div> -->
                <div class="newphone">
                    <span>新手机号</span>
                    <input id="inputnewpho"></input>
                    <p></p>
                </div>
                <div class="verifynewphone">
                    <span>验证手机</span>
                    <input id="inputvernewpho"></input>
                    <span id="send">发送验证码</span>
                    <p></p>
                </div>
                <div class="btn">
                    <span id="phocancel">取消</span>
                    <span id="savepho">保存</span>
                </div>
            </div>
            <!--邮箱模块  -->
            <div class="emil" id="emils">
                <div class="login">
                    <span>邮箱绑定</span>
                    <span>作为项目邮件确认函工具使用，同时作为密码找回身份验证使用</span>
                </div>
                <div class="oldemil">
                    <span>原邮箱</span>
                    <span>dahfkdfakj@163.com</span>
                </div>
                <div class="newemil">
                    <span>新邮箱</span>
                    <input id="inputnewemi"></input>
                    <span> <select>
                         <option selected>@paipianwang.com</option>
                           <option >@paipianwang.cn</option>
                          
                       </select>
                    
                </span>
                    <!-- <span>.cn .com</span> -->

                    <p></p>
                </div>
                <!-- <div class="verifyemil">
                    <span>验证码</span>
                    <input id="inputveremi"></input>
                    <span>发送验证码</span>
                    <p></p>
                </div> -->
                <div class="btn">
                    <span id="emilcancel">取消</span>
                    <span id="saveem">保存</span>
                </div>
            </div>
        </div>
    </div>
   
</body>
</html>