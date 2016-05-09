
$(document).ready(function() {
    var udid = getQueryString("udid");
    var invite = getQueryString("invite");
    var platform = getQueryString("platform");
    alert(udid + ";" + invite + ";" + platform);
});

function getQueryString(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
	var r = window.location.search.substr(1).match(reg);
	if (r != null)
		return decodeURIComponent(r[2]);
	return null;
};

function jump() {
	var iOS = (navigator.userAgent.match(/(iPad|iPhone|iPod)/g) ? true : false );
    var weblink = "http://www.baidu.com";
    var appUrlScheme = "wx20ec458b2c5ab93b://";

    if (iOS) {
        // If the app is not installed the script will wait for 2sec and
		// redirect to web.
        var loadedAt = +new Date;
        setTimeout(
                   function(){
                       if (+new Date - loadedAt < 2000){
                    	   window.location = weblink;
                       }
                   }
                   ,25);
        location.href = appUrlScheme;
    } else {
        // Launch the website
    	location.href = weblink;
    }
}