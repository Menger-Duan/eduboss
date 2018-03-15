var token = $.cookie('token');
var basicOperationQueryLevelType = EduBossApp.commonUtils.getUrlParameter('basicOperationQueryLevelType');
if (!basicOperationQueryLevelType) {
	basicOperationQueryLevelType = "GROUNP";
} else {
	$('#analysisHeader li:first-child').removeClass('on').siblings().addClass('on');
}

function loadFinanceAnalyze(token, startDate, endDate, basicOperationQueryLevelType) {
	$.ajax({
        type: "get",
        dataType: "json",
        url: "/eduboss/MobileInterface/getAllTotalForMobile.do?token=" + token + "&startDate=" + startDate + "&endDate=" + endDate + "&basicOperationQueryLevelType=" + basicOperationQueryLevelType,
        success: function(res){
        	$('#countSeReInner .coTab_2').empty();
        	
        	var realTotalIncome = 0,
        		promotionTotalIncome = 0,
	        	paidTotalAmount = 0,
	        	contracts = 0,
	        	cusCount = 0,
        		newStudent = 0;
			//更新会话期存储：开始结束日期和月/周/日的选择
			EduBossApp.statisticalAnalysis.localDataStorage(true);

		    $.each(res.rows, function(n, item) {
        		var brenchId = item.BRENCH.substring(0,item.BRENCH.indexOf('_'));
        		var brenchName = item.BRENCH.substring(item.BRENCH.indexOf('_')+1);
        		realTotalIncome += item.realIncome;
        		promotionTotalIncome += item.promotionIncome;
        		paidTotalAmount += item.paidTotalAmount;
        		contracts += item.contracts;
        		cusCount += item.cusCount;
        		newStudent += item.newStudent;
        		
        		var avgAmount = '-';
        		if (item.contracts && item.contracts > 0) {
        			avgAmount = EduBossApp.commonUtils.formatCurrency(item.paidTotalAmount/item.contracts);
        		}
        		
            	var tr = '<tr>'
            		if (basicOperationQueryLevelType == "GROUNP") {
            			tr +=  '<td class="tdR1">'+item.BRANCH_SORT+'</td>'
            			tr +=  '<td class="overViewTdR2"><a href="overviewBranch.html?brenchId=' + brenchId + '&brenchName=' + encodeURI(brenchName) + '">' + brenchName + '</a></td>'
                	} else {
                		var campusId = item.CAMPUS.substring(0,item.CAMPUS.indexOf('_'));
    	        		var campusName = item.CAMPUS.substring(item.CAMPUS.indexOf('_')+1);
                		tr +=  '<td class="tdR1">'+item.CAMPUS_SORT+'</td>'
                    	tr +=  '<td class="tdR2"><div class="tdR2" style="overflow:hidden;"><a href="overviewBranch.html?brenchId=' + campusId + '&brenchName=' + encodeURI(campusName) + '">' + campusName + '</a></div></td>'
                	}
                	tr +=  '<td class="overViewTdR3"><b>' + EduBossApp.commonUtils.formatCurrency(item.paidTotalAmount) + '</b></td>'
                	tr +=  '<td class="overViewTdR4"><b>' + EduBossApp.commonUtils.formatCurrency(item.realIncome) + '</b></td>'
//                	tr +=  '<td class="overViewTdR4"><b>' + EduBossApp.commonUtils.formatCurrency(item.realIncome) + '/' + EduBossApp.commonUtils.formatCurrency(item.promotionIncome) + '</b></td>'
                	tr +=  '<td class="overViewTdR5"><b>' + item.newStudent + '</b></td>'
                	tr +=  '<td class="tdR6"><b>' + item.contracts + '</b></td>'
                	tr +=  '<td class="tdR7"><b>' + avgAmount + '</b></td>';
                	tr +=  '<td class="tdR8"><b>' + item.cusCount + '</b></td>'
                	tr +=  '</tr>';
             	$('#countSeReInner .coTab_2').append(tr);  
          	});
          	$('#cashAmount').text(EduBossApp.commonUtils.formatCurrency(paidTotalAmount));
          	$('#revenueAmount').text(EduBossApp.commonUtils.formatCurrency(realTotalIncome));
//          	$('#revenueAmount').text(EduBossApp.commonUtils.formatCurrency(realTotalIncome) + '/' + EduBossApp.commonUtils.formatCurrency(promotionTotalIncome));
          	$('#studentsAmount').text(newStudent);
          	$('#contractsAmount').text(contracts);
          	$('#resourceAmount').text(cusCount);
        	refreshSroll();
        }
   	});

	function refreshSroll() {
		var scrollHeight = $($('#countSeReInner .coTab_2 tr')[0]).height() * ($('#countSeReInner .coTab_2 tr').length)
	  						+ (document.body.offsetHeight - $('#countSeReInner, .scroll').offset().top) - ($('#countSeRe').height()/2);
		$('#countSeReInner .scroll').height(scrollHeight);
		myscrollIn.refresh();
	}
}

$(document).ready(function() {
	var countSeReInnerHeight =  document.body.offsetHeight - $('#countSeReInner, .scroll').offset().top;
	var countSeReHeight =  document.body.offsetHeight - $('#countSeRe').offset().top;
	$('#countSeReInner').height(countSeReInnerHeight)
	$('#countSeRe').height(countSeReHeight);
	
	initScroll();
		//datepicker:
//	EduBossApp.statisticalAnalysis.datepickerToStartdate();
//	EduBossApp.statisticalAnalysis.datepickerToEnddate();
	//读取本地存储数据
	EduBossApp.statisticalAnalysis.localDataStorage(false);
});
