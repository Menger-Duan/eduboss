var token = $.cookie('token');
var basicOperationQueryLevelType = "BRENCH";
var brenchId = EduBossApp.commonUtils.getUrlParameter('brenchId');
var brenchName = decodeURI(EduBossApp.commonUtils.getUrlParameter('brenchName'));
var organizationType = EduBossApp.commonUtils.getUrlParameter('organizationType');

function loadFinanceAnalyze(token, startDate, endDate, basicOperationQueryLevelType) {
	$.ajax({
        type: "get",
        dataType: "json",
        //@param url
        url: "/eduboss/MobileInterface/getAllTotalForMobile.do?token=" + token + "&startDate=" + startDate + "&endDate=" + endDate + "&basicOperationQueryLevelType=" + basicOperationQueryLevelType + "&blCampusId=" + brenchId,
        success: function(res){
        	//debugger;
        	if (res.rows.length <= 0) {
        			
        	}
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
		    		var brenchId = item.CAMPUS.substring(0,item.CAMPUS.indexOf('_'));
		    		var brenchName = item.CAMPUS.substring(item.CAMPUS.indexOf('_')+1);
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
		        		tr +=  '<td class="tdR1">'+item.CAMPUS_SORT+'</td>'
		            	tr +=  '<td class="overViewTdR2"><a href="overviewBranch.html?brenchId=' + brenchId + '&brenchName=' + encodeURI(brenchName) + '">' + brenchName + '</a></td>'
		            	tr +=  '<td class="overViewTdR3"><b>' + EduBossApp.commonUtils.formatCurrency(item.paidTotalAmount) + '</b></td>'
		            	tr +=  '<td class="overViewTdR4"><b>' + EduBossApp.commonUtils.formatCurrency(item.realIncome) + '</b></td>'
//		            	tr +=  '<td class="overViewTdR4"><b>' + EduBossApp.commonUtils.formatCurrency(item.realIncome) + '/' + EduBossApp.commonUtils.formatCurrency(item.promotionIncome) + '</b></td>'
		            	tr +=  '<td class="overViewTdR5"><b>' + item.newStudent + '</b></td>'
		            	tr +=  '<td class="tdR6"><b>' + item.contracts + '</b></td>'
		            	tr +=  '<td class="tdR7"><b>' + avgAmount + '</b></td>';
	                	tr +=  '<td class="tdR8"><b>' + item.cusCount + '</b></td>'
		            	tr +=  '</tr>';
		         	$('#countSeReInner .coTab_2').append(tr);  
		      	});
		      	$('#cashAmount').text(EduBossApp.commonUtils.formatCurrency(paidTotalAmount));
		      	$('#revenueAmount').text(EduBossApp.commonUtils.formatCurrency(realTotalIncome));
//		      	$('#revenueAmount').text(EduBossApp.commonUtils.formatCurrency(realTotalIncome) + '/' + EduBossApp.commonUtils.formatCurrency(promotionTotalIncome));
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
	
	debugger;
	if (organizationType == 'CAMPUS') {
		$('#overViewUrl').attr('href', 'javascript:void(0)');
		$('#cashUrl').attr('href', 'cashBranch.html?organizationType=' + organizationType);
		$('#revenueUrl').attr('href', 'revenueBranch.html?organizationType=' + organizationType);
		$('#marketUrl').attr('href', 'marketBranch.html?organizationType=' + organizationType);
		$('.selectDiv').show();
		$('.headHd').hide();
	} else {
		$('#overViewUrl').attr('href', 'javascript:void(0)');
		$('#cashUrl').attr('href', 'cash.html');
		$('#revenueUrl').attr('href', 'revenue.html');
		$('#marketUrl').attr('href', 'market.html');
		$('.selectDiv').hide();
		$('.headHd').show();
	}
	
	if (brenchName) {
		$('#headName').text(brenchName);
	}	
	//datepicker:
	//EduBossApp.statisticalAnalysis.datepickerToStartdate();
	//EduBossApp.statisticalAnalysis.datepickerToEnddate();
	//读取本地存储数据
	EduBossApp.statisticalAnalysis.localDataStorage(false);
});
