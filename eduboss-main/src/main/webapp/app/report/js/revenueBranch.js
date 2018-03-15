var token = $.cookie('token');
var basicOperationQueryLevelType = "BRENCH";

debugger;
var brenchId = EduBossApp.commonUtils.getUrlParameter('brenchId');
var brenchName = decodeURI(EduBossApp.commonUtils.getUrlParameter('brenchName'));
var organizationType = EduBossApp.commonUtils.getUrlParameter('organizationType');

function loadFinanceAnalyze(token, startDate, endDate, basicOperationQueryLevelType) {
	$.ajax({
        type: "get",
        dataType: "json",
        url: "/eduboss/MobileInterface/getIncomingAnalyze.do?token=" + token 
        	+ "&startDate=" + startDate + "&endDate=" + endDate + "&basicOperationQueryLevelType=" + basicOperationQueryLevelType + "&blCampusId=" + brenchId,
        //complete :function(){$("#load").hide();},
        success: function(res){
        	$('#countSeReInner .coTab_2').empty();
        	
        	var oneOnoneRealTotalAmount = 0;
        	var oneOnonePromotionTotalAmount = 0;
        	var oneOnOneTotalQuantity = 0;
        	var smallClassRealTotalAmount = 0;
			var smallClassPromotionTotalAmount = 0;
			var smallClassTotalQuantity = 0;
			var othersRealTotalAmount = 0;
			var othersPromotionTotalAmount = 0;
			var escClassRealTotalAmount = 0;
			var escClassPromotionTotalAmount = 0;
			var isNormalRealTotalAmount = 0;
			var isNormalPromotionTotalAmount = 0;
			var totalRealAmount = 0;
			var totalPromotionAmount = 0;
			var totalMothTagetValue = 0;
			//更新会话期存储：开始结束日期和月/周/日的选择
			EduBossApp.statisticalAnalysis.localDataStorage(true);

        	$.each(res.rows, function(n, item) {
        		debugger;
        		var campusName = item.campusName;
        		oneOnoneRealTotalAmount += parseFloat(item.oneOnoneRealAmount);
        		oneOnonePromotionTotalAmount += parseFloat(item.oneOnonePromotionAmount);
        		oneOnOneTotalQuantity += parseFloat(item.oneOnOneQuantity);
        		
        		smallClassRealTotalAmount += parseFloat(item.smallClassRealAmount);
        		smallClassPromotionTotalAmount += parseFloat(item.smallClassPromotionAmount);
        		smallClassTotalQuantity += parseFloat(item.smallClassQuantity);
        		
        		othersRealTotalAmount += parseFloat(item.othersRealAmount);
        		othersPromotionTotalAmount += parseFloat(item.othersPromotionAmount);
        		
        		escClassRealTotalAmount += parseFloat(item.escClassRealAmount);
        		escClassPromotionTotalAmount += parseFloat(item.escClassPromotionAmount);
        		
        		isNormalRealTotalAmount += parseFloat(item.isNormalRealAmount);
        		isNormalPromotionTotalAmount += parseFloat(item.isNormalPromotionAmount);
        		
        		debugger;
        		var realAmount = item.oneOnoneRealAmount + item.smallClassRealAmount + item.othersRealAmount + item.escClassRealAmount + item.isNormalRealAmount;
        		var promotionAmount = item.oneOnonePromotionAmount + item.smallClassPromotionAmount + item.othersPromotionAmount + item.escClassPromotionAmount + item.isNormalPromotionAmount;
        		
        		var month_rate = '-';
        		if (item.target_value && item.target_value > 0) {
        			totalMothTagetValue += parseFloat(item.target_value);
        			month_rate = EduBossApp.commonUtils.transToPercentAndRound(item.oneOnOneQuantity/item.target_value);
        		}
        		
        		var tr = '<tr>'
                 tr +=  '<td class="revenueTdR1">' + item.sort + '</td>';
                 tr +=  '<td class="revenueTdR2"><div class="revenueTdR2" style="overflow:hidden;">' + campusName + '</div></td>';
                 tr +=  '<td class="revenueTdR3"><b>' + EduBossApp.commonUtils.formatCurrency(item.oneOnoneRealAmount) + '</b>'
      						+ '<b><img src="style/images/timeIco.png" width="14" height="14">' + EduBossApp.commonUtils.formatCurrency(item.oneOnOneQuantity) + '</b></td>';
			     tr +=  '<td class="revenueTdR4"><b>' + month_rate + '</b></td>'
			     tr +=  '<td class="revenueTdR5"><b>' + EduBossApp.commonUtils.formatCurrency(item.smallClassRealAmount)+ '</b>' 
			      			+ '<b><img src="style/images/timeIco.png" width="14" height="14">' + EduBossApp.commonUtils.formatCurrency(item.smallClassQuantity) + '</b></td>';
			     tr +=  '<td class="revenueTdR6"><b>' + EduBossApp.commonUtils.formatCurrency(item.escClassRealAmount) + '</b></td>'
			     tr +=  '<td class="revenueTdR7"><b>' + EduBossApp.commonUtils.formatCurrency(item.othersRealAmount) + '</b></td>'
			     tr +=  '<td class="revenueTdR8"><b>' + EduBossApp.commonUtils.formatCurrency(item.isNormalRealAmount) + '</b></td>'
			     tr +=  '<td class="revenueTdR9"><b>' + EduBossApp.commonUtils.formatCurrency(realAmount) + '</b></td>'
			     tr +=  '</tr>';
			     $('#countSeReInner .coTab_2').append(tr);
        	});
	          totalRealAmount += parseFloat(oneOnoneRealTotalAmount + smallClassRealTotalAmount + othersRealTotalAmount + escClassRealTotalAmount + isNormalRealTotalAmount);
	       	  totalPromotionAmount += parseFloat(oneOnonePromotionTotalAmount + smallClassPromotionTotalAmount + othersPromotionTotalAmount + escClassPromotionTotalAmount + isNormalPromotionTotalAmount);
	       	  
	       	  var total_month_rate = '-';
	   	  	  if (totalMothTagetValue > 0) {
	   	  		  total_month_rate = EduBossApp.commonUtils.transToPercentAndRound(oneOnOneTotalQuantity/totalMothTagetValue);
	   	  	  }
	       	  
	   	  	  $('#oneOnOneIncoming').text(EduBossApp.commonUtils.formatCurrency(oneOnoneRealTotalAmount));
//	          $('#oneOnonePromotionIncoming').text('/' + EduBossApp.commonUtils.formatCurrency(oneOnonePromotionTotalAmount));
	          $('#oneOnOneQuantity').html('<img src="style/images/timeIco.png" width="14" height="14">' + EduBossApp.commonUtils.formatCurrency(oneOnOneTotalQuantity));
	          $('#finishRate').text('(' + total_month_rate + ')');
	          $('#miniClassIncoming').text(EduBossApp.commonUtils.formatCurrency(smallClassRealTotalAmount));
//	          $('#miniClassPromotionIncoming').text('/' + EduBossApp.commonUtils.formatCurrency(smallClassPromotionTotalAmount));
	          $('#miniClassQuantity').html('<img src="style/images/timeIco.png" width="14" height="14">' + EduBossApp.commonUtils.formatCurrency(smallClassTotalQuantity));
	          $('#ecsClassIncoming').text(EduBossApp.commonUtils.formatCurrency(escClassRealTotalAmount));
//	          $('#ecsClassPromotionIncoming').text('/' + EduBossApp.commonUtils.formatCurrency(escClassPromotionTotalAmount));
	          $('#othersIncoming').text(EduBossApp.commonUtils.formatCurrency(othersRealTotalAmount));
//	          $('#othersPromotionIncoming').text('/' + EduBossApp.commonUtils.formatCurrency(othersPromotionTotalAmount));
	          $('#isNormalIncoming').text(EduBossApp.commonUtils.formatCurrency(isNormalRealTotalAmount));
//	          $('#isNormalPromotionIncoming').text('/' + EduBossApp.commonUtils.formatCurrency(isNormalPromotionTotalAmount));
	          $('#totalIncoming').text(EduBossApp.commonUtils.formatCurrency(totalRealAmount));
//	          $('#totalPromotionIncoming').text('/' + EduBossApp.commonUtils.formatCurrency(totalPromotionAmount));
	          
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
		$('#overViewUrl').attr('href', 'overviewBranch.html?organizationType=' + organizationType);
		$('#cashUrl').attr('href', 'cashBranch.html?organizationType=' + organizationType);
		$('#revenueUrl').attr('href', 'javascript:void(0)');
		$('#marketUrl').attr('href', 'marketBranch.html?organizationType=' + organizationType);
		$('.selectDiv').show();
		$('.headHd').hide();
	} else {
		$('#overViewUrl').attr('href', 'overview.html');
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
//	EduBossApp.statisticalAnalysis.datepickerToStartdate();
//	EduBossApp.statisticalAnalysis.datepickerToEnddate();
	//读取本地存储数据
	EduBossApp.statisticalAnalysis.localDataStorage(false);
});