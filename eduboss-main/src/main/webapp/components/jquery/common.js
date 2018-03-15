var _rights={'find':1,'add':2,'update':3,'save':4,'copy':5,'del':6,'audit':7,'imp':8,'exp':9,'upload':10,'download':11};
function checkAccess(role, access) {
	if (role) {
		var value = Math.pow(2, access);
		return parseInt(role & value) == value;
	}
	return false;
}
function grantRights(ary) {
	var rs = 0;
	for(var i=0; i<ary.length; i++)
		rs = rs | parseInt(Math.pow(2, ary[i]));
	return rs;
}
function openWindow(id,title) {
	$('#'+id).window({
		title:title,
	    iconCls:'icon-save',
	    resizable:false,
	    modal:true  
	}); 
	$('#'+id).clearForm();
	$('#'+id).window('open');
}
function openDialog(id,title) {
	$('#'+id).dialog({
		title:title,
	    iconCls:'icon-save',
	    modal:true
	}); 
	$('#'+id).clearForm();
	$('#'+id).dialog('open');
}
function closeWindow(id) {
	$('#'+id).window('close');
}
function resetForm(id) {
	$('#'+id).resetForm();
}
function queryData(gridId, formId) {
	var param = $.extend($('#'+gridId).datagrid('options').queryParams, $('#'+formId).getValues());
	$('#'+gridId).datagrid('load', param);
	$('#'+gridId).datagrid('clearSelections');
}
function handRowStyle(index, row) {
	return 'cursor:pointer';
}
function currentDateString() {
	var date = new Date();
	var y = date.getFullYear();
	var m = date.getMonth()+1;
	var d = date.getDate();
	return y+'-'+(m<10?('0'+m):m)+'-'+(d<10?('0'+d):d);
}