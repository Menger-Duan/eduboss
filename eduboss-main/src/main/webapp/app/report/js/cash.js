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
        url: "/eduboss/MobileInterface/getFinanceAnalyze.do?token=" + token + "&startDate=" + startDate + "&endDate=" + endDate + "&basicOperationQueryLevelType=" + basicOperationQueryLevelType,
        //complete :function(){$("#load").hide();},
        success: function(res){
        	if (res.rows.length <= 0) {
        			
        	}
        	$('#countSeReInner .coTab_2').empty();
        	var totalPaidTotalAmount = 0;
        	var newPaidTotalAmount = 0;
        	var rePaidCashAmount = 0;
//        	var introducePaidCashAmount = 0;
//        	var contracts = 0;
        	var return_fee = 0;
        	var totalMothTagetValue = 0;
        	
			//更新会话期存储：开始结束日期和月/周/日的选择
			EduBossApp.statisticalAnalysis.localDataStorage(true);

		    $.each(res.rows, function(n, item) {
		    	debugger;
        		var brenchId = item.brenchId.substring(0,item.brenchId.indexOf('_'));
        		var brenchName = item.brenchId.substring(item.brenchId.indexOf('_')+1);
        		totalPaidTotalAmount += parseFloat(item.countPaidTotalAmount);
        		newPaidTotalAmount += parseFloat(item.countPaidCashAmount_new);
        		rePaidCashAmount += parseFloat(item.countPaidCashAmount_re);
//        		introducePaidCashAmount += parseFloat(item.introduce);
//        		contracts += parseFloat(item.contracts);
        		return_fee += parseFloat(item.return_fee);
        		var month_rate = '-';
        		if (item.target_value && item.target_value > 0) {
        			totalMothTagetValue += parseFloat(item.target_value);
        			month_rate = EduBossApp.commonUtils.transToPercentAndRound(item.countPaidTotalAmount/item.target_value);
        		}
//        		var avgAmount = '-';
//        		if (item.contracts && item.contracts > 0) {
//        			avgAmount = EduBossApp.commonUtils.formatCurrency(item.countPaidTotalAmount/item.contracts);
//        		}
        		
            	var tr = '<tr>'
            		if ('BRENCH' == basicOperationQueryLevelType) {
            			var campusId = item.campusId.substring(0,item.campusId.indexOf('_'));
                		var campusName = item.campusId.substring(item.campusId.indexOf('_')+1);
            			tr +=  '<td class="tdR1">' + item.CAMPUS_SORT + '</td>';
            			tr +=  '<td class="tdR2"><div class="tdR2" style="overflow:hidden;">' + campusName + '</div></td>';
            		} else {
            			tr +=  '<td class="tdR1">' + item.BRANCH_SORT + '</td>';
            			tr +=  '<td class="tdR2"><div class="tdR2" style="overflow:hidden;"><a href="cashBranch.html?brenchId=' + brenchId + '&brenchName=' + encodeURI(brenchName) + '">' + brenchName + '</a></div></td>';
            		}
                 	tr +=  '<td class="tdR3"><b>' + EduBossApp.commonUtils.formatCurrency(item.countPaidTotalAmount) + '</b></td>';
                 	tr +=  '<td class="tdR4"><b>' + month_rate + '</b></td>';
                 	tr +=  '<td class="tdR5"><b>' + EduBossApp.commonUtils.formatCurrency(item.countPaidCashAmount_new) + '</b></td>';
                 	tr +=  '<td class="tdR6"><b>' + EduBossApp.commonUtils.formatCurrency(item.countPaidCashAmount_re) + '</b></td>';
//                 	tr +=  '<td class="tdR7"><b>' + EduBossApp.commonUtils.formatCurrency(item.introduce) + '</b></td>';
//                 	tr +=  '<td class="tdR8"><b>' + item.contracts + '</b></td>';
//                 	tr +=  '<td class="tdR9"><b>' + avgAmount + '</b></td>';
                 	tr +=  '<td class="tdR7"><b>' + EduBossApp.commonUtils.formatCurrency(item.return_fee) + '</b></td>';
                 	tr +=  '</tr>';
             	$('#countSeReInner .coTab_2').append(tr);
          	});
		    
		    var total_month_rate = '-';
	  	    if (totalMothTagetValue > 0) {
	  	    	total_month_rate = EduBossApp.commonUtils.transToPercentAndRound(totalPaidTotalAmount/totalMothTagetValue);
	  	    }
          
          	$('#totalPaidTotalAmount').text(EduBossApp.commonUtils.formatCurrency(totalPaidTotalAmount));
          	$('#finishRate').text('(' + total_month_rate + ')');
          	$('#newPaidTotalAmount').text(EduBossApp.commonUtils.formatCurrency(newPaidTotalAmount));
          	$('#rePaidCashAmount').text(EduBossApp.commonUtils.formatCurrency(rePaidCashAmount));
//          	$('#introducePaidCashAmount').text(EduBossApp.commonUtils.formatCurrency(introducePaidCashAmount));
//          	$('#contracts').text(contracts); 
          	$('#returnPremium').text(EduBossApp.commonUtils.formatCurrency(return_fee));
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
	if ('BRENCH' == basicOperationQueryLevelType) {
		$('.coTab_1 .tdR2').text('校区');
	}
	var countSeReInnerHeight =  document.body.offsetHeight - $('#countSeReInner, .scroll').offset().top;
	var countSeReHeight =  document.body.offsetHeight - $('#countSeRe').offset().top;
	$('#countSeReInner').height(countSeReInnerHeight)
	$('#countSeRe').height(countSeReHeight);
	
	initScroll(loadFinanceAnalyze);
	//datepicker:
//	EduBossApp.statisticalAnalysis.datepickerToStartdate();
//	EduBossApp.statisticalAnalysis.datepickerToEnddate();
	//读取本地存储数据
	EduBossApp.statisticalAnalysis.localDataStorage(false);
});