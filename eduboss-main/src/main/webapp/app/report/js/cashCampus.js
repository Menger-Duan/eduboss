var token = $.cookie('token');
var basicOperationQueryLevelType = "BRENCH";
var brenchId = EduBossApp.commonUtils.getUrlParameter('brenchId');
var brenchName = decodeURI(EduBossApp.commonUtils.getUrlParameter('brenchName'));
var organizationType = EduBossApp.commonUtils.getUrlParameter('organizationType');

function selectCurrentDate() {
	$('#next-btn').css('background','#ccc');
	EduBoss.dateUtils.setCurrentDate($('#startDate')); 
	EduBoss.dateUtils.setCurrentDate($('#endDate'));
	var startDate = $('#startDate').val();
	var endDate = $('#endDate').val();
	loadFinanceAnalyze(token, startDate, endDate, basicOperationQueryLevelType);
};

function selectCurrentWeek() {
	//debugger;
	$('#next-btn').css('background','#ccc');
	EduBoss.dateUtils.setWeekDate($('#startDate'), $('#endDate'));
	var startDate = $('#startDate').val();
	var endDate = $('#endDate').val();
	loadFinanceAnalyze(token, startDate, endDate, basicOperationQueryLevelType);
}

function selectCurrentMonth() {
	$('#next-btn').css('background','#ccc');
	EduBoss.dateUtils.setMonthDate($('#startDate'), $('#endDate'));
	var startDate = $('#startDate').val();
	var endDate = $('#endDate').val();
	loadFinanceAnalyze(token, startDate, endDate, basicOperationQueryLevelType);
}

function selectCurrentYear() {
	EduBoss.dateUtils.setYearDate($('#startDate'), $('#endDate'));
	var startDate = $('#startDate').val();
	var endDate = $('#endDate').val();
	loadFinanceAnalyze(token, startDate, endDate, basicOperationQueryLevelType);
}

function getLastDate(){
	if($('#startDate').val() == EduBoss.dateUtils.getCurrentDate()){
		$('#next-btn').css('background','#fff');
	}
	var preDate = EduBoss.dateUtils.getPreviousDate($('#startDate').val());
	$('#startDate, #endDate').val(preDate);
	loadFinanceAnalyze(token, preDate, preDate, basicOperationQueryLevelType);
}

function getLastWeek(){
	var startDate = $('#startDate').val();
	if(startDate == EduBoss.dateUtils.getFirstDateOfWeek(EduBoss.dateUtils.getCurrentDate())){
		$('#next-btn').css('background','#fff');
	}
	var preWeekFirstDate = EduBoss.dateUtils.getPreviousFirstDateOfWeek(startDate);
	var preWeekLastDate = EduBoss.dateUtils.getPreviousLastDateOfWeek(startDate);
	$('#startDate').val(preWeekFirstDate);
	$('#endDate').val(preWeekLastDate)
	loadFinanceAnalyze(token, preWeekFirstDate, preWeekLastDate, basicOperationQueryLevelType);
}

function getLastMonth(){
	var startDate = $('#startDate').val();
	if(startDate == EduBoss.dateUtils.getFirstDateOfMonth(EduBoss.dateUtils.getCurrentDate())){
		$('#next-btn').css('background','#fff');
	}
	var preMonthFirstDate = EduBoss.dateUtils.getPreviousFirstDateOfMonth(startDate);
	var preMonthLastDate = EduBoss.dateUtils.getPreviousLastDateOfMonth(startDate);
	$('#startDate').val(preMonthFirstDate);
	$('#endDate').val(preMonthLastDate);
	loadFinanceAnalyze(token, preMonthFirstDate, preMonthLastDate, basicOperationQueryLevelType);
}

function getNextDate(){
	var startDate = $('#startDate').val();
	var currentDate = EduBoss.dateUtils.getCurrentDate();
	if(startDate < currentDate){
		var nextDate = EduBoss.dateUtils.getNextDate($('#startDate').val());
		$('#startDate, #endDate').val(nextDate);
		loadFinanceAnalyze(token, nextDate, nextDate, basicOperationQueryLevelType);
	}
	if($('#startDate').val() == currentDate){
		$('#next-btn').css('background','#ccc');
		return false;
	}
}

function getNextWeek(){
	var startDate = $('#startDate').val();
	var currentDate = EduBoss.dateUtils.getFirstDateOfWeek(EduBoss.dateUtils.getCurrentDate());
	if(startDate < currentDate){
		var nextWeekFirstDate = EduBoss.dateUtils.getNextFirstDateOfWeek($('#startDate').val());
		var nextWeekLastDate = EduBoss.dateUtils.getNextLastDateOfWeek($('#endDate').val());
		$('#startDate').val(nextWeekFirstDate);
		$('#endDate').val(nextWeekLastDate)
		loadFinanceAnalyze(token, nextWeekFirstDate, nextWeekLastDate, basicOperationQueryLevelType);
	}
	if($('#startDate').val() == currentDate){
		$('#next-btn').css('background','#ccc');
		return false;
	}
}
function getNextMonth(){
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
	if($('#startDate').val() == currentDate){
		$('#next-btn').css('background','#ccc');
		return false;
	}	
}

//获取上个月/周/日的日期
function getLastAvenue(){
	var selectMWD = $('#selectMWD .on').text();
	if(selectMWD == "日")
		getLastDate();
	else if(selectMWD == "周")
		getLastWeek();
	else if(selectMWD == "月")
		getLastMonth();
}

//获取下个月/周/日的日期
function getNextAvenue(){
	var selectMWD = $('#selectMWD .on').text();
	if(selectMWD == "日")
		getNextDate();
	else if(selectMWD == "周")
		getNextWeek();
	else if(selectMWD == "月")
		getNextMonth();
}

//获取月/周/日的选择
function getSelectMWD(selectedObject){
	var selectMWD = selectedObject.text();
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
}

function loadFinanceAnalyze(token, startDate, endDate, basicOperationQueryLevelType) {
	$.ajax({
        type: "get",
        dataType: "json",
        url: "/eduboss/MobileInterface/getFinanceAnalyze.do?token=" + token + "&startDate=" + startDate 
        	+ "&endDate=" + endDate + "&basicOperationQueryLevelType=" + basicOperationQueryLevelType + "&blCampusId=" + brenchId,
        //complete :function(){$("#load").hide();},
        success: function(res){
        	//debugger;
        	if (res.rows.length <= 0) {
        			
        	}
        	$('#countSeReInner .coTab_2').empty();
        	var totalPaidTotalAmount = 0;
        	var newPaidTotalAmount = 0;
        	var introducePaidCashAmount = 0;
        	var rePaidCashAmount = 0;
        	var contracts = 0;
        	var return_fee = 0;

			//更新会话期存储：开始结束日期和月/周/日的选择
			sessionStorage.selectMWD = getSelectMWD($('#selectMWD .on'));
			sessionStorage.startDate = $("#startDate").val();
			sessionStorage.endDate = $('#endDate').val();
			sessionStorage.nextBtnBackground = $('#next-btn').css('background');

        	$.each(res.rows, function(n, item) {
        		var campusId = item.campusId.substring(0,item.campusId.indexOf('_'));
        		var campusName = item.campusId.substring(item.campusId.indexOf('_')+1);
        		totalPaidTotalAmount += parseFloat(item.countPaidTotalAmount);
        		newPaidTotalAmount += parseFloat(item.countPaidCashAmount_new);
        		rePaidCashAmount += parseFloat(item.countPaidCashAmount_re);
        		introducePaidCashAmount += parseFloat(item.introduce);
        		contracts += parseFloat(item.contracts);
        		return_fee += parseFloat(item.return_fee);
        		var month_rate = '-';
        		if (item.target_value && item.target_value > 0) {
        			month_rate = EduBossApp.commonUtils.transToPercentAndRound(item.countPaidTotalAmount/item.target_value);
        		}
        		
            	var tr = '<tr>'
                 	tr +=  '<td class="tdR1">' + item.BRANCH_SORT + '</td>'
                 	tr +=  '<td class="tdR2"><div class="tdR2" style="overflow:hidden;"><a href="cashBranch.html?brenchId=' + brenchId + '&brenchName=' + encodeURI(brenchName) + '">' + brenchName + '</a></div></td>'
                 	tr +=  '<td class="tdR3"><b>' + EduBossApp.commonUtils.formatCurrency(item.countPaidTotalAmount) + '</b></td>'
                 	tr +=  '<td class="tdR4"><b>' + month_rate + '</b></td>'
                 	tr +=  '<td class="tdR5"><b>' + EduBossApp.commonUtils.formatCurrency(item.countPaidCashAmount_new) + '</b></td>'
                 	tr +=  '<td class="tdR6"><b>' + EduBossApp.commonUtils.formatCurrency(item.countPaidCashAmount_re) + '</b></td>'
                 	tr +=  '<td class="tdR7"><b>' + EduBossApp.commonUtils.formatCurrency(item.introduce) + '</b></td>'
                 	tr +=  '<td class="tdR8"><b>' + item.contracts + '</b></td>'
                 	tr +=  '<td class="tdR9"><b>' +EduBossApp.commonUtils.formatCurrency(item.countPaidTotalAmount/item.contracts) + '</b></td>'
                 	tr +=  '<td class="tdR10"><b>' + EduBossApp.commonUtils.formatCurrency(item.return_fee) + '</b></td>'
                 	tr +=  '</tr>';
             $('#countSeReInner .coTab_2').append(tr);
          });
          $('#totalPaidTotalAmount').text(EduBossApp.commonUtils.formatCurrency(totalPaidTotalAmount));
          $('#newPaidTotalAmount').text(EduBossApp.commonUtils.formatCurrency(newPaidTotalAmount));
          $('#introducePaidCashAmount').text(EduBossApp.commonUtils.formatCurrency(introducePaidCashAmount));
          $('#rePaidCashAmount').text(EduBossApp.commonUtils.formatCurrency(rePaidCashAmount));
          $('#contracts').text(contracts);
          $('#returnPremium').text(return_fee);
          
          refreshSroll();
        }
   }); 
	
	function refreshSroll() {
		var scrollHeight = $($('#countSeReInner .coTab_2 tr')[0]).height() * ($('#countSeReInner .coTab_2 tr').length)
			+ (document.body.offsetHeight - $('#countSeReInner, .scroll').offset().top) - ($('#countSeRe').height()/2);
		$('#countSeReInner .scroll').height(scrollHeight);
		myscrollIn.refresh();
	};
};


$(document).ready(function() {
	
	var countSeReInnerHeight =  document.body.offsetHeight - $('#countSeReInner, .scroll').offset().top;
	var countSeReHeight =  document.body.offsetHeight - $('#countSeRe').offset().top;
	$('#countSeReInner').height(countSeReInnerHeight)
	$('#countSeRe').height(countSeReHeight);
	
	initScroll();
	
	if (organizationType == 'CAMPUS') {
		$('#cashUrl').attr('href', 'javascript:void(0)');
		$('#revenueUrl').attr('href', 'revenueCampus.html?organizationType=' + organizationType);
	} else {
		$('#cashUrl').attr('href', 'cash.html');
		$('#revenueUrl').attr('href', 'revenue.html');
	}
	
	if (brenchName) {
		$('#headName').text(brenchName);
	}
	
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
	
	//会话期存储：开始结束日期和月/周/日的选择,以及查看下一日期的颜色
	if(!sessionStorage.selectMWD){
		//默认选择查看当月数据
		$('#selectMWD').children().eq(2).click();
		sessionStorage.selectMWD = 2;
		sessionStorage.startDate = $("#startDate").val();
		sessionStorage.endDate = $('#endDate').val();
		sessionStorage.nextBtnBackground = $('#next-btn').css('background');
	}
	else{
		$('#selectMWD').children().eq(sessionStorage.selectMWD).click();
		$('#startDate').val(sessionStorage.startDate);
		$('#endDate').val(sessionStorage.endDate);
		sessionStorage.nextBtnBackground = $('#next-btn').css('background',sessionStorage.nextBtnBackground);
		loadFinanceAnalyze(token, sessionStorage.startDate, sessionStorage.endDate, basicOperationQueryLevelType);
	}
	
});