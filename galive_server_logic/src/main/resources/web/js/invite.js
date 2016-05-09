var kServletUrl = "http://192.168.213.177:8080/galive/share";

$(document).ready(function() {
	if (!isSafari()) {
		alert("111");
	} else {
		alert("3333");
		var fp = new Fingerprint2();
		fp.get(function(result) {
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
					jump();
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

function jump() {
	var iOS = (navigator.userAgent.match(/(iPad|iPhone|iPod)/g) ? true : false);
	var weblink = "http://www.baidu.com";
	var appUrlScheme = "wx20ec458b2c5ab93b://";

	if (iOS) {
		var loadedAt = +new Date;
		setTimeout(function() {
			if (+new Date - loadedAt < 2000) {
				window.location = weblink;
			}
		}, 25);
		location.href = appUrlScheme;
	} else {
		location.href = weblink;
	}
}

function isSafari() {
	var userAgent = navigator.userAgent; //取得浏览器的userAgent字符串
	alert(userAgent);
	if (userAgent.indexOf("Safari") > -1) {
		return true;
	}
	return false;
}
