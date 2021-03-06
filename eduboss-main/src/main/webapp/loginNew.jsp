<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en-us" id="extr-page">
<head>
    <meta charset="utf-8">
    <title id="systemName"> </title>
    <meta name="description" content="">
    <meta name="author" content="">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">

    <!-- #CSS Links -->
    <!-- Basic Styles -->
    <link rel="stylesheet" type="text/css" media="screen" href="framework/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" media="screen" href="framework/css/font-awesome.min.css">

    <!-- SmartAdmin Styles : Please note (smartadmin-production.css) was created using LESS variables -->
    <link rel="stylesheet" type="text/css" media="screen" href="framework/css/smartadmin-production.min.css">
    <link rel="stylesheet" type="text/css" media="screen" href="framework/css/smartadmin-skins.min.css">

    <!-- SmartAdmin RTL Support is under construction
         This RTL CSS will be released in version 1.5
    <link rel="stylesheet" type="text/css" media="screen" href="framework/css/smartadmin-rtl.min.css"> -->

    <!-- We recommend you use "your_style.css" to override SmartAdmin
         specific styles this will also ensure you retrain your customization with each SmartAdmin update.
    <link rel="stylesheet" type="text/css" media="screen" href="framework/css/your_style.css"> -->

    <!-- Demo purpose only: goes with demo.js, you can delete this css when designing your own WebApp -->
    <link rel="stylesheet" type="text/css" media="screen" href="framework/css/demo.min.css">

    <!-- #FAVICONS -->
    <link rel="shortcut icon" href="framework/img/favicon/favicon.ico" type="framework/image/x-icon">
    <link rel="icon" href="framework/img/favicon/favicon.ico" type="framework/image/x-icon">

    <!-- #GOOGLE FONT -->
    <!-- <link rel="stylesheet" href="http://fonts.googleapis.com/css?family=Open+Sans:400italic,700italic,300,400,700"> -->

    <!-- #APP SCREEN / ICONS -->
    <!-- Specifying a Webpage Icon for Web Clip
         Ref: https://developer.apple.com/library/ios/documentation/AppleApplications/Reference/SafariWebContent/ConfiguringWebApplications/ConfiguringWebApplications.html -->
    <link rel="apple-touch-icon" href="framework/img/splash/sptouch-icon-iphone.png">
    <link rel="apple-touch-icon" sizes="76x76" href="framework/img/splash/touch-icon-ipad.png">
    <link rel="apple-touch-icon" sizes="120x120" href="framework/img/splash/touch-icon-iphone-retina.png">
    <link rel="apple-touch-icon" sizes="152x152" href="framework/img/splash/touch-icon-ipad-retina.png">

    <!-- iOS web-app metas : hides Safari UI Components and Changes Status Bar Appearance -->
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">

    <!-- Startup image for web apps -->
    <link rel="apple-touch-startup-image" href="framework/img/splash/ipad-landscape.png"
          media="screen and (min-device-width: 481px) and (max-device-width: 1024px) and (orientation:landscape)">
    <link rel="apple-touch-startup-image" href="framework/img/splash/ipad-portrait.png"
          media="screen and (min-device-width: 481px) and (max-device-width: 1024px) and (orientation:portrait)">
    <link rel="apple-touch-startup-image" href="framework/img/splash/iphone.png"
          media="screen and (max-device-width: 320px)">
    <script src="framework/js/libs/jquery-2.0.2.min.js?_auto_version_"></script>
    
    <style type="text/css">
body{
	position:relative;
}
#content{
	position:absolute;
	right:0;
	top:0;
	z-index: 1;
	padding:0 62px;
}
.bg-pic{
	position:absolute;
	bottom:0;
	left:28%;
}
input:-moz-placeholder{
    color:#9e9e9e;
}
input::-moz-placeholder{
    color:#9e9e9e;
}
input::-webkit-input-placeholder{
    color:#9e9e9e;
}
input:-ms-input-placeholder{
    color:#9e9e9e;
}
#login-form input:-webkit-autofill{
	-webkit-box-shadow: 0 0 0px 1000px #393939 inset!important;
	background-image: none;
	-webkit-text-fill-color: #fff;
}


   </style>
   <script type="text/javascript">
   	$(document).ready(function (){
   		var oWindowHeight = $(window).height();
   		var oWindowWidth = $(window).width();
   		var oBgPic = $('.bg-pic img');
   		
   		if(oWindowWidth<1920){
   				var widthPercentage = oWindowWidth/1920;
   				$(oBgPic).css({'width':802*widthPercentage+'px','height':872*widthPercentage+'px'});
   			}
   			else{
   				$(oBgPic).css({'width':'802px','height':'872px'});
   			}
   		$('body').css({'height':oWindowHeight+'px'});
   		$(window).resize(function (){
   			var oWindowHeightChange = $(window).height();
   			var oWindowWidthChange = $(window).width();
   			if(oWindowWidthChange<1920){
   				var widthPercentage = oWindowWidthChange/1920;
   				$(oBgPic).css({'width':802*widthPercentage+'px','height':872*widthPercentage+'px'});
   			}
   			else{
   				$(oBgPic).css({'width':'802px','height':'872px'});
   			}
   			$('body').css({'height':oWindowHeightChange+'px'});
   		});

   	});
   </script>
</head>

<body class="animated fadeInDown" style="background:#c9c6c3;height:1080px;">


    <div id="logo-group" style="position: absolute;left:5%;top:4%;z-index: 1;">
        <img id="systemInfoLogoPic" src="images/peiyou_logo_03.png" alt="SmartAdmin">
      <!--  <span id="logo"> <img  src="/uploadfile/image/loginLogo.jpg" alt="SmartAdmin" style="height: 60px;width: 264px"> </span>  -->
    </div>

	<div class="bg-pic">
		<img src="images/background_pic_03.png">
	</div>


    <!-- MAIN CONTENT -->
    <div id="content" class="container"  style="width:22%;height: 100%;background-color:rgba(51,51,51,0.95);">

                    <form action="${pageContext.request.contextPath}/j_spring_security_check" id="login-form" method="post" 
                    style="width:100%;">
								<div>
									   <span style="font-weight:bold;font-size:28px;color:#fff;font-family:'arial';text-align: center;width:100%;display: inline-block;margin-top:270px;margin-bottom:70px;">LOGIN</span>
									    	<span id="errorMessage" style="float:right;color:red;font-size:14px;font-weight: bold;" class="${param.error == true ? '' : 'hide'}">${sessionScope['SPRING_SECURITY_LAST_EXCEPTION'].message}</span>
									    	<% 
									    		String errorMessage = "test";//sessionScope['SPRING_SECURITY_LAST_EXCEPTION'].message;
									    		/* if (StringUtils.isNotBlank(errorMessage)) {
									    			if (errorMessage.contains("UserDetailsService returned null")) {
									    			 	errorMessage = "找不到用户";
									    			 } else if (errorMessage.contains("Bad credentials")) {
									    			 	errorMessage = "密码错误";
									    			 } else {
									    			 	errorMessage = "登录失败"；
									    			 }
									    		} */
									    	 %>
									    	<script type="text/javascript">
									    		var errorMessage = $("#errorMessage").text(); 
									    		if (errorMessage.indexOf("UserDetailsService returned null") > -1) {
									    			$("#errorMessage").text("找不到用户，请重新输入！");
									    		} else if(errorMessage.indexOf("Bad credentials") > -1) {
									    			$("#errorMessage").text("密码错误，请重新输入！");
									    		} else {
									    			$("#errorMessage").text("登录失败，请重新输入！");
									    		}
									    	</script>
									    	<!-- 错误提示要换成中文 -->
								</div>
                                <!-- <hr style="border:none;border-top:1px solid white;height:0;margin-top: 6px;margin-bottom: 35px;"/> -->
								<fieldset style="width:100%;">
									
									<section style="width:100%;">
										<!-- <label class="label" style="margin-left:2%;color:black;"><font face="微软雅黑" color="#fff">用户名</font></label><br/> -->
										<label class="input" style="margin-top:6px;width:100%;position:relative;">
											<input type="text" name="j_username" placeholder="请输入用户名" value="${sessionScope['SPRING_SECURITY_LAST_USERNAME']}"
											style="padding-left:54px;border-style:none;height:42px;border-bottom:#fff 1px solid;background:none;color:#fff;width:100%;">
											<div class="input-icon" style="position:absolute;left:12px;top:9px;">
												<img src="images/login_icon_05.png">
											</div>
											</label>
									</section>
                                    
									<section style="margin-top:6%;width:100%;">
										<!-- <label class="label" style="margin-left:6px;color:black;"><font face=" 微软雅黑" color="#fff">密&nbsp;&nbsp;码</font></label><br/> -->
										<label class="input" style="margin-top:6px;width:100%;position:relative;">
											<input type="password" name="j_password" placeholder="请输入密码"
											style="padding-left:54px;border-style:none;height:42px;border-bottom:#fff 1px solid;background:none;color:#fff;width:100%;">
											<div class="input-icon" style="position:absolute;left:12px;top:9px;">
												<img src="images/login_icon_03.png">
											</div>
											</label>
									</section>

									<section>
									  <div style="margin-top:10px;float:right;">
										
											<input type="checkbox" name="_spring_security_remember_me" checked=""
											style="width:15px;height:15px;vertical-align:text-bottom;">
											<label style="font-weight:bold;"><font face="微软雅黑" color="#fff">自动登录</font></label>
									  </div>
									</section>
								</fieldset>
								<div align="center" style="margin-top:80px;">
									<button type="submit" style="background-color:#407dc0;border:none;width:100%;height:46px;">
										<font face="微软雅黑" color="white" size="2">登&nbsp;&nbsp;录</font>
									</button>
								</div>
							</form>

			</div>
			<%--<div style="margin-left:75%;display:none;">--%>
			<%--<script type="text/javascript">var cnzz_protocol = (("https:" == document.location.protocol) ? " https://" : " http://");document.write(unescape("%3Cspan id='cnzz_stat_icon_1253266024'%3E%3C/span%3E%3Cscript src='" + cnzz_protocol + "s9.cnzz.com/stat.php%3Fid%3D1253266024%26show%3Dpic' type='text/javascript'%3E%3C/script%3E"));</script>--%>
			<%--</div>--%>
		<!-- 	<div style="margin-left: 21%;">
                    <div class="col-xs-12 col-sm-12 col-md-6 col-lg-6">
                        <h5 class="about-heading"><font face="微软雅黑">先进时尚的UI和交互</font></h5>

                        <p><font face="微软雅黑">
                            采用国际先进的UI及交互框架，带来全新的体验！</font>
                        </p>
                        <h5 class="about-heading" style="padding-top:10px;"><font face="微软雅黑">量身定做，实用为王</font></h5>

                        <p><font face="微软雅黑">
                            基于实际使用需求，以提高工作效率，帮助管理运营为目的，量身定做。</font><br>
           <font face="微软雅黑">先进的技术架构，高可用性及性能稳定度，让用户不再与系统作斗争。</font>
                        </p>
                    </div>
                </div> -->


		<!--================================================== -->	

		<!-- PACE LOADER - turn this on if you want ajax loading to show (caution: uses lots of memory on iDevices)-->
		<script src="framework/js/plugin/pace/pace.min.js?_auto_version_"></script>

	    <!-- Link to Google CDN's jQuery + jQueryUI; fall back to local -->
	    <script src="framework/js/libs/jquery-2.0.2.min.js?_auto_version_"></script>

	    <script src="framework/js/libs/jquery-ui-1.10.3.min.js?_auto_version_"></script>

		<!-- JS TOUCH : include this plugin for mobile drag / drop touch events 		
		<script src="framework/js/plugin/jquery-touch/jquery.ui.touch-punch.min.js?_auto_version_"></script> -->

		<!-- BOOTSTRAP JS -->		
		<script src="framework/js/bootstrap/bootstrap.min.js?_auto_version_"></script>

		<!-- JQUERY VALIDATE -->
		<script src="framework/js/plugin/jquery-validate/jquery.validate.min.js?_auto_version_"></script>
		
		<!-- JQUERY MASKED INPUT -->
		<script src="framework/js/plugin/masked-input/jquery.maskedinput.min.js?_auto_version_"></script>
		
		<!--[if IE 8]>
			
			<h1>Your browser is out of date, please update your browser by going to www.microsoft.com/download</h1>
			
		<![endif]-->

		<!-- MAIN APP JS FILE -->
		<script src="framework/js/app.min.js?_auto_version_"></script>

		<script type="text/javascript">
// 			if("${sessionScope['SPRING_SECURITY_LAST_USERNAME']}"!=''){//如果已经登录就直接跳转到首页。
// 				 window.top.location.href="/eduboss/index.html";
// 			}
            if(window.top !== window){
                window.top.location.href="/eduboss/login.jsp";
            }

			runAllForms();

			$(function() {
				// Validation
				$("#login-form").validate({
					// Rules for form validation
					rules : {
						email : {
							required : true,
							email : true
						},
						password : {
							required : true,
							minlength : 3,
							maxlength : 20
						}
					},

					// Messages for form validation
					messages : {
						email : {
							required : 'Please enter your email address',
							email : 'Please enter a VALID email address'
						},
						password : {
							required : 'Please enter your password'
						}
					},

					// Do not change code below
					errorPlacement : function(error, element) {
						error.insertAfter(element.parent());
					}
				});
				/*
				var url = window.location.href.substring(0, window.location.href.indexOf("/eduboss/")) + '/eduboss/SystemAction/getSystemConfigList.do';
				$.get(EduBoss.serviceApi.getSystemConfigList,{"tag":"systemInfo"},function(data){
				alert(data.tag);
				if(data.length>0){
					$.each(data,function(i,item){
						if("systemInfo" == item.tag){
						var val = item.remark.substring(1);
							$("#systemInfoLogoPic").attr("src",val.substring(val.indexOf("/")));
							alert(item.remark);
						}
					});
				}
				});*/
			});
			/* $(document).ready(function(){
				var url = window.location.href.substring(0, window.location.href.indexOf("/eduboss/")) + '/eduboss/SystemAction/getSystemConfigList.do';
				$.get(url,{"tag":"systemHome"},function(data){debugger;
				if(data.length>0){
					$.each(data,function(i,item){
						if("systemHome" == item.tag){
							var picUrls = item.remark.split(",");
							if(picUrls[0]){
								var url1 = picUrls[0].substring(1);
								$("#systemInfoLogoPic").attr("src",url1.substring(url1.indexOf("/")));
								//alert(url1.substring(url1.indexOf("/")));
							}
							
							if(picUrls[1]){
								var url2 = picUrls[1].substring(1);
								$("#loginBigPic").attr("src",url2.substring(url2.indexOf("/")));
							}
							
						}
					});
				}
				});
			}); */
			/*$(document).ready(function(){
				var url = window.location.href.substring(0, window.location.href.indexOf("/eduboss/")) + '/eduboss/SystemAction/getSystemConfigList.do';
				$.get(url,{"tag":"systemHome"},function(data){
				if(data.length>0){
					$.each(data,function(i,item){
						if("systemHome" == item.tag){
							if(item.value!=''){//系统描述
								$("#systemDes").html(item.value);
							}
							var picUrls = item.remark.split(",");
							if(picUrls[0]){
								var url1 = picUrls[0];
								$("#systemInfoLogoPic").attr("src",url1);
							}
							
							if(picUrls[1]){								
								var url2 = picUrls[1];
								$("#loginBigPic").attr("src",url2);
							}
							
						}
					});
				}
				});
				
				$.get(url,{"tag":"systemInfo"},function(data){
					if(data.length>0){
						$.each(data,function(i,item){
								if(item.name!=''){//系统名字
									$("#systemName").html(item.name);
									$("#desName").html(item.name);
								}else{
									$("#systemName").html("EDUBOSS校长系统");
								}
							})
						}
					});
			});*/
		</script>
		
		
	</body>
</html>