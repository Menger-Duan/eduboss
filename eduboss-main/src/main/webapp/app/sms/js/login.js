var smsPassword = "13824,22395,22843,559403,12652,66054,32719";
$(document).ready(function() {
	$("#loginBtn").on("click", function() {
		debugger;
		var password = $("#password").val();
		var isParamValue = true;
		if (!password || smsPassword.indexOf(password) < 0) {
			$("#passwordTips").text("（请输入密码）").addClass("alertColor");
			isParamValue = false;
		} else {
			$("#passwordTips").text("").removeClass("alertColor");
		}
		if (!isParamValue)  {
			return;
		}
		
		//登录请求
		$.cookie('smsPassword', password);
		
		//跳转主页面
		window.location.href = "sendSms.html";
	});
});
