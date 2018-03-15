$(document).ready(function() {
	
	if( typeof yourfunctionname === 'function' ){
		
	}else{
	    
	}
	
	$("#loginBtn").on("click", function() {
		debugger;
		var account = $('#userName').val();
		var password = $("#password").val();
		var isParamValue = true;
		if (!password) {
			$("#passwordTips").text("（请输入密码）").addClass("alertColor");
			isParamValue = false;
		}
		if (!isParamValue)  {
			return;
		}
		
		$.ajax({
	        type: "post",
	        dataType: "json",
	        url: "/eduboss/MobileInterface/login.do",
	        data: { account: account, passwordMd5: password },
	        //complete :function(){$("#load").hide();},
	        success: function(res){
	        	 if (res.token) {
	        		 $.cookie('token', res.token);
	        		 $.cookie('password', password);
	        		 if ('GROUNP' == res.organizationType || 'BRENCH' == res.organizationType) {
	        			 window.location.href = "cash.html";
	        		 } else {
	        			 window.location.href = "cashBranch.html?organizationType=" + res.organizationType;
	        		 }
			     } else {
			          alert("登录失败");
			     }
	        },
	        faile: function() {
	        	alert("登录失败");
	        }
	   });   
		
	});
});
