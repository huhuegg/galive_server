
$(document).ready(function() {
	var appstoreFail = "http://www.baidu.com";

    //Check is device is iOS
    var iOS = (navigator.userAgent.match(/(iPad|iPhone|iPod)/g) ? true : false );
    var weblink = "http://www.baidu.com";
    var appUrlScheme = "wx20ec458b2c5ab93b://";

    if (iOS) {
        //If the app is not installed the script will wait for 2sec and redirect to web.
//        var loadedAt = +new Date;
//        setTimeout(
//                   function(){
//                       if (+new Date - loadedAt < 2000){
//                   window.location = appstoreFail;
//                       }
//                   }
//                   ,25);
        //Try launching the app using URL schemes
        location.href = appUrlScheme;
    } else {
        //Launch the website
    	location.href = weblink;
    }
});

function jump() {
	var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Linux') > -1; //android终端或者uc浏览器
	var isiOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端
	
	if (isiOS) {
		setTimeout(function(){  
            // To avoid failing on return to MobileSafari, ensure freshness!  
            if (+new Date - clickedAt < 2000){  
            window.location = fail;  
            }  
            }, 500); 
		
		
		
		var appstore = "itms://itunes.apple.com/us/app/facebook/id284882215?mt=8&uo=6";  
        function applink(fail){  
            return function(){  
                var clickedAt = +new Date;  
                // During tests on 3g/3gs this timeout fires immediately if less than 500ms.  
                     
            };  
        }
	}
}
