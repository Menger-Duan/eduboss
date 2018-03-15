$(document).ready(function() {	
	var organizationType=GetQueryString("organizationType");//取地址栏的参数
	 if ('GROUNP' == organizationType || 'BRENCH' == organizationType) {
		 window.location.href = "cash.html";
	 } else {
		 window.location.href = "cashCampus.html?organizationType=" + organizationType;
	 }
});
