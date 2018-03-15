var smsTamplateArray = [
	"校长您好，诚邀您在教育联盟期间到boss系统T62展位详细了解系统，享受联盟专场优惠，提前安排上门实施【学邦技术】",
	"校长您好，诚邀您今晚到万豪酒店参与BOSS系统专场《培训系统终极解决方案》演讲【学邦技术】",
	"校长您好，很高兴刚才我公司技术人员为您讲解系统，如需详细了解或预约上门演示及业务分析，请致电18620690888【学邦技术】",
	"校长您好，我公司实施人员将于10:30到贵公司进行系统演示，期间如有疑问，请致电18620690888【学邦技术】",
	"校长您好，诚邀您明天到boss系统T62展位了解怎么利用系统实现o2o，享受联盟专场优惠，提前安排上门实施【学邦技术】",
	"校长您好，诚邀您明天到boss系统T62展位了解怎么利用系统实现教学质量提升，并享受联盟专场优惠，提前安排上门实施【学邦技术】",
	"校长您好，诚邀您明天到boss系统T62展位了解怎么利用APP帮忙校区运营，并享受联盟专场优惠，提前安排上门实施【学邦技术】",
	"校长您好，诚邀您明天到boss系统T62展位了解怎么利用运营数据帮忙市场分析，并享受联盟专场优惠，提前安排上门实施【学邦技术】",
	""
];
function initSmsTemplate() {
	for (var x in smsTamplateArray) {
		$("#smsTemplateLayout").append('<div class="main grayBg margin-buttom-15" style="height:200px;"><form role="form"><div class="form-group"><textarea class="form-control" id="smsContent" rows="3" placeholder="请输入发送内容">'
				+ smsTamplateArray[x]
				+'</textarea></div>				<div class="form-group">					<input type="text" class="form-control" id="smsNumbers" placeholder="请输入发送号码，多个号码用逗号隔开">				</div>				<button type="button" id="loginBtn" class="btn btn-primary btn-blue" data-loading-text="提交中..." style="width:100%" onclick="onSendSmsBtnClick(this)">发送</button>			</form>					</div><br>');
	}
}
function onSendSmsBtnClick(element) {
	var smsContent = $(element).parent().find("#smsContent").text();
	var smsNumbers = $(element).parent().find("#smsNumbers").val();
	if (!smsContent || !smsNumbers) {
		alert('内容或号码不能为空！');
		return;
	}
	$.get(EduBossApp.serviceApi.sendSms, {
		'mobileNumber' : smsNumbers,
		'sendType' : "SEND",
		'content' : smsContent
	}, function(response) {
		if (response.resultCode === 0) {
			alert("发送成功！");
		} else {
			alert("发送失败：" + response.resultMessage);
		}
	});
}
$(document).ready(function() {
	
	initSmsTemplate();
	
});
