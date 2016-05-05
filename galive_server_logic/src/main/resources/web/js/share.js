
$(document).ready(function() {
	getWebUdid();
});

function getWebUdid() {
	var fp = new Fingerprint2();
	fp.get(function(result) {
		alert(result);
	});
}
