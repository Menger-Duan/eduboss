var EduBoss={
	cusTypes : null,
	cusOrgs : null,
	//dataDictCacheMap : {},//放到index中去了
	modelDateMap : {},
	//authFuncBtnTags : null, //放到index中去了
	rowList:[20,50,100],
	rowNum : 20,
};

var myscroll, myscrollIn;

function initScroll() {
	function loaded(){
	    myscroll=new iScroll("countSeRe",{hScroll:true,vScroll:false,hScrollbar:false, vScrollbar:false});
	 }
	function loadedIn(methodName){
		pullDownEl = document.getElementById('pullDown');
		pullDownOffset = pullDownEl.offsetHeight;
	    myscrollIn=new iScroll("countSeReInner",
	    		{vScroll:true,
	    		hScrollbar:false, 
	    		vScrollbar:false,
	    		scrollbarClass: 'myScrollbar', /* 重要样式 */
	    		useTransition: false, /* 此属性不知用意，本人从true改为false */
	    		topOffset: pullDownOffset,
	    		onRefresh: function () {
	    			if (pullDownEl.className.match('loading')) {
	    				pullDownEl.className = '';
	    				pullDownEl.querySelector('.pullDownLabel').innerHTML = '下拉刷新...';
	    			} 
	    		},
	    		onScrollMove: function () {
	    			if (this.y > 5 && !pullDownEl.className.match('flip')) {
	    				pullDownEl.className = 'flip';
	    				pullDownEl.querySelector('.pullDownLabel').innerHTML = '松手开始更新...';
	    				this.minScrollY = 0;
	    			} else if (this.y < 5 && pullDownEl.className.match('flip')) {
	    				pullDownEl.className = '';
	    				pullDownEl.querySelector('.pullDownLabel').innerHTML = '下拉刷新...';
	    				this.minScrollY = -pullDownOffset;
	    			}
	    		},
	    		onScrollEnd: function () {
	    			if (pullDownEl.className.match('flip')) {
	    				pullDownEl.className = 'loading';
	    				pullDownEl.querySelector('.pullDownLabel').innerHTML = '加载中...';	
	    				var startDate = $('#startDate').val();
	    				var endDate = $('#endDate').val();
	    				if ('loadFinanceAnalyze'==methodName) {
	    					loadFinanceAnalyze(token, startDate, endDate, basicOperationQueryLevelType);	
	    				} else {
	    					loadFinanceAnalyze(token, startDate, endDate, basicOperationQueryLevelType);
	    				}
	    			} 
	    		}
	    });
	 }
	 
	 window.addEventListener('touchmove', function (e) { e.preventDefault(); }, false);
	 window.addEventListener("DOMContentLoaded",loaded,false);
	 window.addEventListener("DOMContentLoaded",loadedIn,false);
     var tabTd =  $(".tdR3").width();
     var tabLen =  $(".coTab_1 th").length - 1;
     var winW = $(window).width();
     var url = window.location.href;
     if (url.indexOf("revenue") > 0) {
    	 $(".scrollInner").css({width: 160 * tabLen,minWidth:winW});
     } else if (url.indexOf("overview") > 0) {
    	 $(".scrollInner").css({width: 120 * tabLen,minWidth:winW});
     } else {
    	 $(".scrollInner").css({width: 90 * tabLen,minWidth:winW});
     }
     if(tabLen > 4 ){
	  	$(".countSeRe").addClass("overIcon");
	 }

}

///**
// * 初始化iScroll控件
// */
//function loadedIn() {
//	pullDownEl = document.getElementById('pullDown');
//	pullDownOffset = pullDownEl.offsetHeight;
//	
//	myscrollIn = new iScroll('countSeReInner', {
//		scrollbarClass: 'myScrollbar', /* 重要样式 */
//		useTransition: false, /* 此属性不知用意，本人从true改为false */
//		topOffset: pullDownOffset,
//		onRefresh: function () {
//			if (pullDownEl.className.match('loading')) {
//				pullDownEl.className = '';
//				pullDownEl.querySelector('.pullDownLabel').innerHTML = '下拉刷新...';
//			} 
//		},
//		onScrollMove: function () {
//			if (this.y > 5 && !pullDownEl.className.match('flip')) {
//				pullDownEl.className = 'flip';
//				pullDownEl.querySelector('.pullDownLabel').innerHTML = '松手开始更新...';
//				this.minScrollY = 0;
//			} else if (this.y < 5 && pullDownEl.className.match('flip')) {
//				pullDownEl.className = '';
//				pullDownEl.querySelector('.pullDownLabel').innerHTML = '下拉刷新...';
//				this.minScrollY = -pullDownOffset;
//			}
//		},
//		onScrollEnd: function () {
//			if (pullDownEl.className.match('flip')) {
//				pullDownEl.className = 'loading';
//				pullDownEl.querySelector('.pullDownLabel').innerHTML = '加载中...';	
//				var startDate = $('#startDate').val();
//				var endDate = $('#endDate').val();
//				loadFinanceAnalyze(token, startDate, endDate, basicOperationQueryLevelType);	
//			} 
//		},
//		vScroll:true,
//		hScrollbar:false, 
//		vScrollbar:false
//	});
//}
//
//function loaded(){
//    myscroll=new iScroll("countSeRe",{hScroll:true,vScroll:false,hScrollbar:false, vScrollbar:false});
//}


//年
function getYearTime(id)
	{
		var now = new Date();             //获取当前时间
		var beginTimes = now.getFullYear();     //开始计算
		var Month = now.getMonth() + 1 ;           //getMonth()是以0开始的月份

		var beginTimes = beginTimes + "-01" +"-01";        //格式 Y-m-d
		$(id).val(beginTimes);
	}
	
//月	
function getMonthTime(id)
	{
		var now = new Date();             //获取当前时间
		var beginTimes = now.getFullYear();     //开始计算
		var Month = now.getMonth() +1 ;           //getMonth()是以0开始的月份
		if(Month < 10){
			Month = '0' + Month
		}
		
		var beginTimes = beginTimes + "-" + Month +"-01";        //格式 Y-m-d
		$(id).val(beginTimes);
	}

//周
function getWeekTime(id)
	{
		var now = new Date();   //获取当前时间   
		var Year = now.getFullYear();   //开始计算
		var Month = now.getMonth() + 1;
		var Day = now.getDate()- now.getDay();
		
		if(now.getDay()==0)           //星期天表示 0 故当星期天的时候，获取上周开始的时候
		{
		  Day -= 7;
		}
		if(Month < 10){
			Month = '0' + Month
		}
		if(Day < 10){
			Day = '0' + Day
		}
		
		var beginTime = Year + "-" + Month +"-" + Day; //格式 Y-m-d
		$(id).val(beginTime);
	}

//日
function getDayTime(id)
	{
		var now = new Date();   //获取当前时间   
		var Year = now.getFullYear();   //开始计算
		var Month = now.getMonth() + 1;
		var Day = now.getDate();
		
		if(Month < 10){
			Month = '0' + Month
		}
		if(Day < 10){
			Day = '0' + Day
		}
		
		var beginTime = Year + "-" + Month +"-" + Day; //格式 Y-m-d
		$(id).val(beginTime);
	}

$(function(){
	/*
	// 公司/校区的选择
	$('#analysisHeader li').click(function(){
		$(this).addClass('on').siblings().removeClass('on');
		loadFinanceAnalyze(token, sessionStorage.startDate, sessionStorage.endDate, basicOperationQueryLevelType);
	});
	*/
	//点击查看上一段日期
	$('#pre-btn').click(function(){
		EduBossApp.statisticalAnalysis.getLastStatistics();
	});
	//点击查看下一段日期
	$('#next-btn').click(function(){
		EduBossApp.statisticalAnalysis.getNextStatistics();
	});
	//点击查看当前月/周/日
	$("#selectMWD li").click(function(){
		$("#selectMWD li").removeClass("on");
		$(this).addClass("on");
		var index = $(this).index();
		switch(index){
			case 0:
				EduBossApp.statisticalAnalysis.selectCurrentDate();
				break;
			case 1:
				EduBossApp.statisticalAnalysis.selectCurrentWeek();
				break;
			case 2:
				EduBossApp.statisticalAnalysis.selectCurrentMonth();
				break;
			default:
				;
		}
	});
	//初始日期
	//getDayTime('#startDay');
	//sgetDayTime('#endDay')
});

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

/*
 * 统计分析日期选择
 */
EduBossApp.statisticalAnalysis = {
	//会话期数据存储：开始结束日期和月/周/日的选择,以及查看下一日期的颜色
	localDataStorage: function(flag){
		if(!sessionStorage.selectMWD){
			//默认选择查看当月数据
			$('#selectMWD').children().eq(2).click();
			sessionStorage.selectMWD = 2;
			sessionStorage.startDate = $("#startDate").val();
			sessionStorage.endDate = $('#endDate').val();
			sessionStorage.nextBtnBackground = $('#next-btn').css('background');
		}
		//存储数据
		else if(flag){
			sessionStorage.selectMWD = this.getSelectMWD();
			sessionStorage.startDate = $("#startDate").val();
			sessionStorage.endDate = $('#endDate').val();
			sessionStorage.nextBtnBackground = $('#next-btn').css('background');
		}
		//读取数据
		else if(!flag){
			//$('#selectMWD').children().eq(sessionStorage.selectMWD).click();
			$("#selectMWD li").removeClass("on").eq(sessionStorage.selectMWD).addClass("on");
			$('#startDate').val(sessionStorage.startDate);
			$('#endDate').val(sessionStorage.endDate);
			$('#next-btn').css('background',sessionStorage.nextBtnBackground);
			loadFinanceAnalyze(token, sessionStorage.startDate, sessionStorage.endDate, basicOperationQueryLevelType);
		}
	},
	datepickerToStartdate: function(){
		$("#startDate").datepicker({
			dateFormat:"yy-mm-dd", 
			onSelect:function(startDate) {
				var endDate = $('#endDate').val();
				if(startDate <= endDate)
					loadFinanceAnalyze(token, startDate, endDate, basicOperationQueryLevelType);
				else
					$('#startDate').val(sessionStorage.startDate);
			},
			changeMonth: true,
			changeYear: true,
			prevText : '<i class="fa fa-chevron-left"></i>',
			nextText : '<i class="fa fa-chevron-right"></i>'
		});
	},
	datepickerToEnddate: function(){
		$("#endDate").datepicker({
			dateFormat:"yy-mm-dd",
		    onSelect : function(endDate){
		    	var startDate = $('#startDate').val();
				if(startDate <= endDate){
					if(endDate < EduBoss.dateUtils.getCurrentDate()){
						$('#next-btn').css('background','#fff');
						sessionStorage.nextBtnBackground =$('#next-btn').css('background');
					}
					else {
						$('#next-btn').css('background','#ccc');
						sessionStorage.nextBtnBackground =$('#next-btn').css('background');
					}
					loadFinanceAnalyze(token, startDate, endDate, basicOperationQueryLevelType);
				}
				else
					$('#endDate').val(sessionStorage.endDate);
		    },
			changeMonth: true,
			changeYear: true,
			prevText : '<i class="fa fa-chevron-left"></i>',
			nextText : '<i class="fa fa-chevron-right"></i>'
		});
	},
	//获取月/周/日的选择
	getSelectMWD: function(){
		var selectMWD = $('#selectMWD .on').text();
		switch(selectMWD){
			case "日":
				selectMWD = 0;
				break;
			case "周":
				selectMWD = 1;
				break;
			case "月":
				selectMWD = 2;
				break;
			default:
				;
		}
		return selectMWD;
	},

	selectCurrentDate: function () {
		$('#next-btn').css('background','#ccc');
		EduBoss.dateUtils.setCurrentDate($('#startDate')); 
		EduBoss.dateUtils.setCurrentDate($('#endDate'));
		var startDate = $('#startDate').val();
		var endDate = $('#endDate').val();
		loadFinanceAnalyze(token, startDate, endDate, basicOperationQueryLevelType);
	},

	selectCurrentWeek: function () {
		$('#next-btn').css('background','#ccc');
		EduBoss.dateUtils.setWeekDate($('#startDate'), $('#endDate'));
		var startDate = $('#startDate').val();
		var endDate = $('#endDate').val();
		loadFinanceAnalyze(token, startDate, endDate, basicOperationQueryLevelType);
	},

	selectCurrentMonth: function() {
		$('#next-btn').css('background','#ccc');
		EduBoss.dateUtils.setMonthDate($('#startDate'), $('#endDate'));
		var startDate = $('#startDate').val();
		var endDate = $('#endDate').val();
		loadFinanceAnalyze(token, startDate, endDate, basicOperationQueryLevelType);
	},

	selectCurrentYear: function() {
		EduBoss.dateUtils.setYearDate($('#startDate'), $('#endDate'));
		var startDate = $('#startDate').val();
		var endDate = $('#endDate').val();
		loadFinanceAnalyze(token, startDate, endDate, basicOperationQueryLevelType);
	},
	
	getLastStatistics: function(){
		var selectMWD = $('#selectMWD .on').text();
		if(selectMWD == "日")
			this.getLastDate();
		else if(selectMWD == "周")
			this.getLastWeek();
		else if(selectMWD == "月")
			this.getLastMonth();
	},

	getNextStatistics: function (){
		var selectMWD = $('#selectMWD .on').text();
		if(selectMWD == "日")
			this.getNextDate();
		else if(selectMWD == "周")
			this.getNextWeek();
		else if(selectMWD == "月")
			this.getNextMonth();
	},

	getLastDate: function(){
		if($('#startDate').val() <= EduBoss.dateUtils.getCurrentDate()){
			$('#next-btn').css('background','#fff');
		}
		var preDate = EduBoss.dateUtils.getPreviousDate($('#startDate').val());
		$('#startDate, #endDate').val(preDate);
		loadFinanceAnalyze(token, preDate, preDate, basicOperationQueryLevelType);
	},

	getLastWeek: function(){
		var startDate = $('#startDate').val();
		if(startDate <= EduBoss.dateUtils.getFirstDateOfWeek(EduBoss.dateUtils.getCurrentDate())){
			$('#next-btn').css('background','#fff');
		}
		var preWeekFirstDate = EduBoss.dateUtils.getPreviousFirstDateOfWeek(startDate);
		var preWeekLastDate = EduBoss.dateUtils.getPreviousLastDateOfWeek(startDate);
		$('#startDate').val(preWeekFirstDate);
		$('#endDate').val(preWeekLastDate)
		loadFinanceAnalyze(token, preWeekFirstDate, preWeekLastDate, basicOperationQueryLevelType);
	},

	getLastMonth: function(){
		var startDate = $('#startDate').val();
		if(startDate <= EduBoss.dateUtils.getFirstDateOfMonth(EduBoss.dateUtils.getCurrentDate())){
			$('#next-btn').css('background','#fff');
		}
		var preMonthFirstDate = EduBoss.dateUtils.getPreviousFirstDateOfMonth(startDate);
		var preMonthLastDate = EduBoss.dateUtils.getPreviousLastDateOfMonth(startDate);
		$('#startDate').val(preMonthFirstDate);
		$('#endDate').val(preMonthLastDate);
		loadFinanceAnalyze(token, preMonthFirstDate, preMonthLastDate, basicOperationQueryLevelType);
	},

	getNextDate: function(){
		var startDate = $('#startDate').val();
		var currentDate = EduBoss.dateUtils.getCurrentDate();
		if(startDate < currentDate){
			var nextDate = EduBoss.dateUtils.getNextDate($('#startDate').val());
			$('#startDate, #endDate').val(nextDate);
			loadFinanceAnalyze(token, nextDate, nextDate, basicOperationQueryLevelType);
		}
		if($('#startDate').val() >= currentDate){
			$('#next-btn').css('background','#ccc');
			return false;
		}
	},

	getNextWeek: function(){
		var startDate = $('#startDate').val();
		var currentDate = EduBoss.dateUtils.getFirstDateOfWeek(EduBoss.dateUtils.getCurrentDate());
		if(startDate < currentDate){
			var nextWeekFirstDate = EduBoss.dateUtils.getNextFirstDateOfWeek($('#startDate').val());
			var nextWeekLastDate = EduBoss.dateUtils.getNextLastDateOfWeek($('#endDate').val());
			$('#startDate').val(nextWeekFirstDate);
			$('#endDate').val(nextWeekLastDate)
			loadFinanceAnalyze(token, nextWeekFirstDate, nextWeekLastDate, basicOperationQueryLevelType);
		}
		if($('#startDate').val() >= currentDate){
			$('#next-btn').css('background','#ccc');
			return false;
		}
	},

	getNextMonth: function(){
		var startDate = $('#startDate').val();
		var endDate = $('#endDate').val();
		var currentDate = EduBoss.dateUtils.getFirstDateOfMonth(EduBoss.dateUtils.getCurrentDate());
		if(startDate < currentDate){
			var nextMonthFirstDate = EduBoss.dateUtils.getNextFirstDateOfMonth(startDate);
			var nextMonthLastDate = EduBoss.dateUtils.getNextLastDateOfMonth(startDate);
			$('#startDate').val(nextMonthFirstDate);
			$('#endDate').val(nextMonthLastDate);
			loadFinanceAnalyze(token, nextMonthFirstDate, nextMonthLastDate, basicOperationQueryLevelType);
		}
		if($('#startDate').val() >= currentDate){
			$('#next-btn').css('background','#ccc');
			return false;
		}	
	}
}

jQuery.cookie=function(name,value,options){
	  if(typeof value!='undefined'){
	      options=options||{};
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

//---------------------------------------------------  
//日期格式化  
//格式 YYYY/yyyy/YY/yy 表示年份  
//MM/M 月份  
//W/w 星期  
//dd/DD/d/D 日期  
//hh/HH/h/H 时间  
//mm/m 分钟  
//ss/SS/s/S 秒  
//---------------------------------------------------  
Date.prototype.Format = function(formatStr)   
{   
 var str = formatStr;   
 var Week = ['日','一','二','三','四','五','六'];  

 str=str.replace(/yyyy|YYYY/,this.getFullYear());   
 str=str.replace(/yy|YY/,(this.getYear() % 100)>9?(this.getYear() % 100).toString():'0' + (this.getYear() % 100));   
 
 str=str.replace(/MM/, (this.getMonth() + 1)>9?(this.getMonth() + 1).toString():'0' + (this.getMonth() + 1));   
 str=str.replace(/M/g,(this.getMonth() + 1));   
 
 str=str.replace(/w|W/g,Week[this.getDay()]);   

 str=str.replace(/dd|DD/,this.getDate()>9?this.getDate().toString():'0' + this.getDate());   
 str=str.replace(/d|D/g,this.getDate());   

 str=str.replace(/hh|HH/,this.getHours()>9?this.getHours().toString():'0' + this.getHours());   
 str=str.replace(/h|H/g,this.getHours());   
 str=str.replace(/mm/,this.getMinutes()>9?this.getMinutes().toString():'0' + this.getMinutes());   
 str=str.replace(/m/g,this.getMinutes());   

 str=str.replace(/ss|SS/,this.getSeconds()>9?this.getSeconds().toString():'0' + this.getSeconds());   
 str=str.replace(/s|S/g,this.getSeconds());   

 return str;   
}

//配置日期选择框语言
$.datepicker.regional['zh-CN'] = {   
    clearText: '清除',   
    clearStatus: '清除已选日期',   
    closeText: '关闭',   
    closeStatus: '不改变当前选择',   
    prevText: '<上月',   
    prevStatus: '显示上月',   
    prevBigText: '<<',   
    prevBigStatus: '显示上一年',   
    nextText: '下月>',   
    nextStatus: '显示下月',   
    nextBigText: '>>',   
    nextBigStatus: '显示下一年',   
    currentText: '今天',   
    currentStatus: '显示本月',   
    monthNames: ['一月','二月','三月','四月','五月','六月', '七月','八月','九月','十月','十一月','十二月'],   
    monthNamesShort: ['一','二','三','四','五','六', '七','八','九','十','十一','十二'],   
    monthStatus: '选择月份',   
    yearStatus: '选择年份',   
    weekHeader: '周',   
    weekStatus: '年内周次',   
    dayNames: ['星期日','星期一','星期二','星期三','星期四','星期五','星期六'],   
    dayNamesShort: ['周日','周一','周二','周三','周四','周五','周六'],   
    dayNamesMin: ['日','一','二','三','四','五','六'],   
    dayStatus: '设置 DD 为一周起始',   
    dateStatus: '选择 m月 d日, DD',   
    dateFormat: 'yy-mm-dd',   
    firstDay: 1,   
    initStatus: '请选择日期',   
    isRTL: false};   
$.datepicker.setDefaults($.datepicker.regional['zh-CN']);

//获取浏览器参数
function GetQueryString(name){
     var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
     var r = window.location.search.substr(1).match(reg);
     if(r!=null)return  unescape(r[2]); return null;
}

EduBossApp.commonUtils = {
	showMessage : function(title, content) {
		$(".modal-title").text(title);
		$(".modal-body").text(content);
		$("#myModal").modal("show");
	},
	
	checkLogin : function() {
		if (window.location.href.indexOf("login.html") < 0) {
			var token=GetQueryString("token");//取地址栏的参数					
			if(token){				
				if(token !=null && token.toString().length>1){
				   $.cookie('token', token);
				}					
			}			
			if (! $.cookie('token')) {
				window.location.href = "login.html";
			}
		}
	},
	
	transToPercentAndRound:function(data){  //输入小数，返回其保留两位小数的百分比
		if( typeof(data) == "number"){
			return Math.round(data * 10000) / 100 + '%';
		} else
			return "";
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
	},
	
	roundDealForDecimal:function(inputNum){  //四舍五入处理
		var retVal = parseFloat(inputNum);  
		if (isNaN(retVal))  
		{  
		alert('function:changeTwoDecimal->parameter error');  
		return false;  
		}  
		retVal = Math.round(inputNum*100)/100;  
		  
		return retVal;  
		
	},
	
	formatCurrency : function (num) {  
		if (!num) return '0';
	    num = num.toString().replace(/\$|\,/g,'');  
	    if(isNaN(num))  
	        num = "0";  
	    sign = (num == (num = Math.abs(num)));  
	    num = Math.floor(num*100+0.50000000001);  
	    cents = num%100;  
	    num = Math.floor(num/100).toString();  
	    if(cents<10)  
	    cents = "0" + cents;  
	    for (var i = 0; i < Math.floor((num.length-(1+i))/3); i++)  
	    num = num.substring(0,num.length-(4*i+3))+','+  
	    num.substring(num.length-(4*i+3));  
	    return (((sign)?'':'-') + num + '.' + cents);
	},
	
};


/**
 * 系统定义每周从周一开始，周日结束
 */
EduBoss.dateUtils = {
	parseDate : function(dateStr) {
		return new Date(Date.parse(dateStr.replace(/-/g,   "/")));
	},
	// 得到上个月的第一天
	getPreviousFirstDateOfMonth: function(theDate){
		theDate = EduBoss.dateUtils.parseDate(theDate);
		return this.addMonth(theDate.Format("yyyy-MM-dd"),-1);
	},
	// 得到上个月的最后一天
	getPreviousLastDateOfMonth: function(theDate){
		theDate = EduBoss.dateUtils.parseDate(theDate);
		return this.getLastDateOfMonth(this.addMonth(theDate.Format("yyyy-MM-dd"),-1));
	},
	// 得到下个月的第一天
	getNextFirstDateOfMonth: function(theDate){
		theDate = EduBoss.dateUtils.parseDate(theDate);
		return this.addMonth(theDate.Format("yyyy-MM-dd"),1);
	},
	// 得到下个月的最后一天
	getNextLastDateOfMonth: function(theDate){
		theDate = EduBoss.dateUtils.parseDate(theDate);
		return this.getLastDateOfMonth(this.addMonth(theDate.Format("yyyy-MM-dd"),1));
	},
	// 得到每周的第一天(周一)
	getFirstDateOfWeek : function(theDate){  
		theDate = EduBoss.dateUtils.parseDate(theDate);
		var firstDateOfWeek;  
		if(theDate.getDay() == 0){ // 周日则往前推六天
			theDate.setDate(theDate.getDate() -6);
		}else{
			theDate.setDate(theDate.getDate() + 1 - theDate.getDay());
		}
		firstDateOfWeek = theDate.Format("yyyy-MM-dd");  
		return firstDateOfWeek;   
	} , 
	// 得到每周的最后一天(周日)
	getLastDateOfWeek: function(theDate){  
		theDate = EduBoss.dateUtils.parseDate(theDate);
		var lastDateOf;  
		if(theDate.getDay() > 0){ // 非周日则归0至周日再往后推7天
			theDate.setDate(theDate.getDate() - theDate.getDay() + 7);  
		}
		lastDateOfWeek = theDate.Format("yyyy-MM-dd");  
		return lastDateOfWeek;   
	} ,
	// 得到上周的第一天(周一)
	getPreviousFirstDateOfWeek: function (theDate){
		theDate = EduBoss.dateUtils.parseDate(theDate);
		theDate.setDate(theDate.getDate() - 7);  
		return this.getFirstDateOfWeek(theDate.Format("yyyy-MM-dd"));
	},
	// 得到上周的最后一天(周日)
	getPreviousLastDateOfWeek:function (theDate){
		theDate = EduBoss.dateUtils.parseDate(theDate);
		theDate.setDate(theDate.getDate() - 7);  
		return this.getLastDateOfWeek(theDate.Format("yyyy-MM-dd"));
	},
	//得到下一周的第一天(周一)
	getNextFirstDateOfWeek: function(startDate){
		theDate = EduBoss.dateUtils.parseDate(startDate);
		return this.addDate(theDate.Format("yyyy-MM-dd"),7);
	},
	//得到下一周的第一天(周一)
	getNextLastDateOfWeek: function(endDate){
		theDate = EduBoss.dateUtils.parseDate(endDate);
		return this.addDate(theDate.Format("yyyy-MM-dd"),7);
	},
	//前一日
	getPreviousDate: function(theDate) {
		theDate = EduBoss.dateUtils.parseDate(theDate);
		var previousDate;
		theDate.setDate(theDate.getDate() - 1);
		previousDate = theDate.Format("yyyy-MM-dd");
		return previousDate;
	},
	//下一日
	getNextDate: function(theDate) {
		theDate = EduBoss.dateUtils.parseDate(theDate);
		var nextDate;
		theDate.setDate(theDate.getDate() + 1);
		nextDate = theDate.Format("yyyy-MM-dd");
		return nextDate;
	},
	addDate: function(theDate, addDates) {
		theDate = EduBoss.dateUtils.parseDate(theDate);
		var nextDate;
		theDate.setDate(theDate.getDate() + addDates);
		nextDate = theDate.Format("yyyy-MM-dd");
		return nextDate;
	},
	getCurrentDate: function(){
		return (new Date()).Format("yyyy-MM-dd");
	},
	getCurrentTime: function(){
		return (new Date()).Format("yyyy-MM-dd HH:mm:ss");
	},
	getCurrentMonthFirstDay: function(){
		return (new Date()).Format("yyyy-MM")+"-01";
	},
	//向前数七天
	getDateOfPreviousWeek: function (){
		var theDate = (new Date()).Format("yyyy-MM-dd");
		theDate.setDate(theDate.getDate() - 7);  
		return theDate.Format("yyyy-MM-dd");
	},
	addMonth: function(theDate, addMonths) {
		theDate = EduBoss.dateUtils.parseDate(theDate);
		var nextDate;
		theDate.setMonth(theDate.getMonth() + addMonths);
		nextDate = theDate.Format("yyyy-MM-dd");
		return nextDate;
	},
	addQuarter: function(theDate, addQuarters) {
		theDate = EduBoss.dateUtils.parseDate(theDate);
		var nextDate;
		theDate.setMonth(theDate.getMonth() + addQuarters);
		nextDate = theDate.Format("yyyy-MM-dd");
		return nextDate;
	},
	addYear: function(theDate, addYears) {
		theDate = EduBoss.dateUtils.parseDate(theDate);
		var nextDate;
		theDate.setYear(theDate.getFullYear() + addYears);
		nextDate = theDate.Format("yyyy-MM-dd");
		return nextDate;
	},
	getFirstDateOfMonth : function(theDate){  
		theDate = EduBoss.dateUtils.parseDate(theDate);
		var nextDate;
		theDate.setDate(1);
		nextDate = theDate.Format("yyyy-MM-dd");
		return nextDate;
	},
	getLastDateOfMonth : function(theDate){  
		theDate = EduBoss.dateUtils.parseDate(theDate);
		var day = new Date(theDate.getFullYear(), theDate.getMonth()+1, 0);
		var nextDate;
		theDate.setDate(day.getDate());
		nextDate = theDate.Format("yyyy-MM-dd");
		return nextDate;
	},
	getFirstDateOfYear : function(theDate){  
		theDate = EduBoss.dateUtils.parseDate(theDate);
		var nextDate;
		theDate.setMonth(0);
		theDate.setDate(1);
		nextDate = theDate.Format("yyyy-MM-dd");
		return nextDate;
	},
	getLastDateOfYear : function(theDate){  
		theDate = EduBoss.dateUtils.parseDate(theDate);
		var day = new Date(theDate.getFullYear(), 12, 0);
		var nextDate;
		theDate.setMonth(11);
		theDate.setDate(day.getDate());
		nextDate = theDate.Format("yyyy-MM-dd");
		return nextDate;
	},
	getFirstDateOfQuarter : function(theDate){  
		theDate = EduBoss.dateUtils.parseDate(theDate);
		if (theDate.getMonth()>=0 && theDate.getMonth()<=2) {
			theDate.setMonth(0);
		} else if (theDate.getMonth()>2 && theDate.getMonth()<=5) {
			theDate.setMonth(3);
		} else if (theDate.getMonth()>5 && theDate.getMonth()<=8) {
			theDate.setMonth(6);
		} else if (theDate.getMonth()>8 && theDate.getMonth()<=11) {
			theDate.setMonth(9);
		}
		var nextDate;
		theDate.setDate(1);
		nextDate = theDate.Format("yyyy-MM-dd");
		return nextDate;
	},
	getLastDateOfQuarter : function(theDate){  
		theDate = EduBoss.dateUtils.parseDate(theDate);
		if (theDate.getMonth()>=0 && theDate.getMonth()<=2) {
			theDate.setMonth(2);
		} else if (theDate.getMonth()>2 && theDate.getMonth()<=5) {
			theDate.setMonth(5);
		} else if (theDate.getMonth()>5 && theDate.getMonth()<=8) {
			theDate.setMonth(8);
		} else if (theDate.getMonth()>8 && theDate.getMonth()<=11) {
			theDate.setMonth(11);
		}
		var day = new Date(theDate.getFullYear(), theDate.getMonth()+1, 0);
		var nextDate;
		theDate.setDate(day.getDate());
		nextDate = theDate.Format("yyyy-MM-dd");
		return nextDate;
	},
	getDayCountOfDate : function(startDate,endDate){ // 获取日期间有多少天数
		startDate = EduBoss.dateUtils.parseDate(startDate);
		endDate = EduBoss.dateUtils.parseDate(endDate);
		return (endDate - startDate)/(1000*60*60*24) + 1;//获取日期之间相差的天数
	},
	getDayOfDate : function(startDate,endDate,dayNum){
		
	},
	getDaysBetweenDate : function(startDate,endDate){ // 获取日期之差
		startDate = EduBoss.dateUtils.parseDate(startDate);
		endDate = EduBoss.dateUtils.parseDate(endDate);
		return (endDate - startDate)/(1000*60*60*24);
	},
	getFirstWeekDate: function(date, weekDay) {
		debugger;
		var tmp = weekDay - EduBoss.dateUtils.parseDate(date).getDay();
		if (tmp >= 0) {
			return EduBoss.dateUtils.addDate(date, tmp);
		} else {
			return EduBoss.dateUtils.addDate(date, 7+tmp);
		}
	},
	setCurrentDate : function(element) {
		currentDate = EduBoss.dateUtils.getCurrentDate();
		element.val(currentDate);
	},
	setWeekDate : function($start, $end) {
		currentDate = EduBoss.dateUtils.getCurrentDate();
		startDate = EduBoss.dateUtils.getFirstDateOfWeek(currentDate);
		$start.val(startDate);
		endDate = EduBoss.dateUtils.getLastDateOfWeek(currentDate);
		$end.val(endDate);
	},
	setMonthDate : function($start, $end) {
		currentDate = EduBoss.dateUtils.getCurrentDate();
		startDate = EduBoss.dateUtils.getFirstDateOfMonth(currentDate);
		$start.val(startDate);
		endDate = EduBoss.dateUtils.getLastDateOfMonth(currentDate);
		$end.val(endDate);
	},
	setYearDate : function($start, $end) {
		currentDate = EduBoss.dateUtils.getCurrentDate();
		startDate = EduBoss.dateUtils.getFirstDateOfYear(currentDate);
		$start.val(startDate);
		endDate = EduBoss.dateUtils.getLastDateOfYear(currentDate);
		$end.val(endDate);
	},
};

$(document).ajaxError(function(e, xhr, options, exception) {
	if (xhr.responseText.indexOf("j_spring_security_check") > 0) {//如果返回的是登录界面，说明session已过期，直接把整个页面跳出到登录页面
		window.location.href = "login.html";
	} else {
		if(xhr.responseJSON.resultCode)
		{
//			EduBossApp.commonUtils.showMessage("提示",xhr.responseJSON.resultMessage);
			alert(xhr.responseJSON.resultMessage);
			window.location.href = "login.html";
		}
	}
	$(".loading").remove();
	$("#loginBtn").text("提交");
});

//每次打开页面先检查是否登录
EduBossApp.commonUtils.checkLogin();