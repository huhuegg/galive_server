var kServletUrl = "http://192.168.213.177:8080/galive/share";

$(function() {
	var ownerSid = getQueryString("ownerSid");
	var platform = getQueryString("platform");
	var timestamp = getQueryString("timestamp");
	
	
	$("#jump").click(function() {
		location.href = "https://b.doodduck.com/invite/" + ownerSid + "/" + platform + "/" + timestamp;
	});
});

function getQueryString(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
	var r = window.location.search.substr(1).match(reg);
	if (r != null)
		return decodeURIComponent(r[2]);
	return null;
};

function jump(invite) {
	var iOS = (navigator.userAgent.match(/(iPad|iPhone|iPod)/g) ? true : false);
	var downloadUrl = "http://222.73.60.153/galive.html";
	var schemeUrl = "galive://invite=" + invite;

	if (iOS) {
		location.href = schemeUrl;
		setTimeout(function() {
			location.href = downloadUrl;
		}, 5000);
	} else {
		location.href = downloadUrl;
	}
}

function isSafari() {
	var userAgent = navigator.userAgent; //取得浏览器的userAgent字符串
	if (userAgent.indexOf("Safari") > -1) {
		return true;
	}
	return false;
}
