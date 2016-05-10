var kServletUrl = "http://192.168.213.177:8080/galive/share";

$(function() {
	$("#tips").addClass('hide');
	if (!isSafari()) {
		$("#tips").removeClass('hide');
	} else {
		$("#tips").addClass('hide');
		var fp = new Fingerprint2();
		fp.get(function(result) {
			alert(result);
			var udid = result;
			var invite = getQueryString("invite");
			var platform = getQueryString("platform");
			//alert(udid + ";" + invite + ";" + platform);

			$.post(kServletUrl, {
				"udid" : udid,
				"invitee" : invite,
				"platform" : platform
			}, function(data) {
				if (data == "0") {
					jump(invite);
				}
			});
		});
	}
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
