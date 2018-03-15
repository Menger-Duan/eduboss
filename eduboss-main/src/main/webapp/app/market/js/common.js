var EduBossApp={};
EduBossApp.serviceAddress=window.location.href.substring(0, window.location.href.indexOf("/eduboss/")) + '/eduboss/';
EduBossApp.serviceApi = {
	login : EduBossApp.serviceAddress+"MobileInterface/login.do",
	getMyCustomerList : EduBossApp.serviceAddress+"MobileInterface/getCustomersForJqGrid.do",
	editCustomer : EduBossApp.serviceAddress+"MobileInterface/editCustomer.do",
	findCustomerById : EduBossApp.serviceAddress+"MobileInterface/findCustomerById.do",
	getCapumsForSelection : EduBossApp.serviceAddress+"MobileInterface/getCapumsForSelection.do",
	getStaffForSelection : EduBossApp.serviceAddress+"MobileInterface/getStaffForByRoleCodeSelection.do",
	sendSms : EduBossApp.serviceAddress + "MobileInterface/sendSms.do",
};
jQuery.cookie=function(name,value,options){
	  if(typeof value!='undefined'){
	      options=options||{};on
	      if(value===null){
	          value='';
	          options.expires=-1;
	      }
	      var expires='';
	      if(options.expires&&(typeof options.expires=='number'||options.expires.toUTCString)){
	           var date;
	          if(typeof options.expires=='number'){
	              date=new Date();
	              date.setTime(date.getTime()+(options.expires * 24 * 60 * 60 * 1000));
	           }else{
	              date=options.expires;
	          }
	          expires=';expires='+date.toUTCString();
	       }
	      var path=options.path?';path='+options.path:'';
	      var domain=options.domain?';domain='+options.domain:'';
	      var secure=options.secure?';secure':'';
	      document.cookie=[name,'=',encodeURIComponent(value),expires,path,domain,secure].join('');
	   }else{
	      var cookieValue=null;
	      if(document.cookie&&document.cookie!=''){
	          var cookies=document.cookie.split(';');
	          for(var i=0;i<cookies.length;i++){
	              var cookie=jQuery.trim(cookies[i]);
	              if(cookie.substring(0,name.length+1)==(name+'=')){
	                  cookieValue=decodeURIComponent(cookie.substring(name.length+1));
	                  break;
	              }
	          }
	      }
	      return cookieValue;
	  }
	};	

EduBossApp.commonUtils = {
	showMessage : function(title, content) {
		$(".modal-title").text(title);
		$(".modal-body").text(content);
		$("#myModal").modal("show");
	},
	
	checkLogin : function() {
		if (window.location.href.indexOf("login.html") < 0) {
			if (!$.cookie('smsPassword')) {
				window.location.href = "login.html";
			}
		}
	},
	
	strToDateTime : function(str) {
		var date = null;
		if (str) {
			var dateStr = str.split(" ")[0];
			var timeStr = str.split(" ")[1];
			var dateArray = dateStr.split("-");
			var timeArray = timeStr.split(":");
			var y,m,d,h,mi,s;
			y=parseInt(dateArray[0]);
			m=parseInt(dateArray[1] - 1);
			d=parseInt(dateArray[2]);
			h=parseInt(timeArray[0]);
			mi=parseInt(timeArray[1]);
			s=parseInt(timeArray[2]);
			var tempDate = new Date();
			tempDate.setFullYear(y);
			tempDate.setDate(d);
			tempDate.setMonth(m); 
			tempDate.setHours(h, mi, s, 0);
			date = tempDate;
		}
		return date;
	},
	
	getUrlParameter : function(parameterName) {
		var paramValue = "";
		var urlArray = window.location.href.split("?");
		if (window.location.href.indexOf("?") > 0) {
			var paramArray = window.location.href.split("?")[1].split("&");
			for (x in paramArray) {
				if (paramArray[x].indexOf(parameterName) > -1) {
					paramValue = paramArray[x].replace(parameterName+"=", "");
				}
			}
		}
		return paramValue;
	},
	
	fillElmentValueByTag : function(obj, tag) {
		$.each($("*["+tag+"]"), function(i, elment) {
			//适用于非列表类型数据，根据定义好的标签名从json对象中取值然后填到元素中
			//处理多层取值：有多层次时，在db-data-field-name用.表明层级，在这里逐层取到最后一层的值
			var propertyNameArray =  $(elment).attr(tag).split(".");
			var value = obj;
			for (x in propertyNameArray) {
				if (value) value = value[propertyNameArray[x]];
			}
			//根据类型赋值
			if ($(elment).is("input") || $(elment).is("select")) {
				$(elment).val(value);
			} else {
				$(elment).text(value);
			}
			
		});
	},
	
	clearElmentValueByTag : function(tag) {
		$.each($("*["+tag+"]"), function(i, elment) {
			//根据类型赋值
			if ($(elment).is("input") || $(elment).is("select")) {
				$(elment).val("");
			} else {
				$(elment).text("");
			}
		});
	},
	
	initSelection : function(url, selectId, defaultHtml) {
		$.get(url, function(data) {
			try {
//				var resposne = eval("(" + data + ")");
				debugger;
				var resposne = data;
				if (resposne.value) {
					var optionHtml = defaultHtml;
					var valueArray = resposne.value;
					for (x in valueArray) {
						optionHtml += "<option value='"+x+"'>"+valueArray[x]+"</option>"
					}
					$("#"+selectId).html(optionHtml);
				}
			} catch(e) {
				alert(data);
			}
		});
	}
	
};
$(document).ajaxError(function(e, xhr, options, exception) {
	if (xhr.responseText.indexOf("j_spring_security_check") > 0) {//如果返回的是登录界面，说明session已过期，直接把整个页面跳出到登录页面
		window.location.href = "login.html";
	} else {
		if(xhr.responseJSON.resultCode)
		{
			EduBossApp.commonUtils.showMessage("提示",xhr.responseJSON.resultMessage);
		}
	}
	$(".loading").remove();
	$("#loginBtn").text("提交");
});

//每次打开页面先检查是否登录
EduBossApp.commonUtils.checkLogin();

