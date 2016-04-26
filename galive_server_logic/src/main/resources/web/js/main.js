var kServletUrl = "http://192.168.212.188:8080/galive/tag"

var tags = new Array()

$(document).ready(function() {
	$("#tag_form").hide();

	$("#tag_add").click(function(){
		var tag = $("#tag_input").val();
		addTag(tag);
	});

	tagList();
});

function tagList() {
	$.post(kServletUrl, {
		"req" : "list"
	}, function(data) {
		var json = $.parseJSON(data);
		var table = $("#tag_list");
		var html = "";
		$.each(json, function(i, tag) {
			var cell = appendTag(tag, i);
			html += cell;
			tags[i] = tag;
        });
		table.html(html);
		$("#tag_form").show();
	});
}

function addTag(tag) {
	$.post(kServletUrl, {
		"req" : "add",
		"tag" : tag
	}, function(data) {
		location.reload();
	});
}

function delTag(index) {
	var tag = tags[index];
	$.post(kServletUrl, {
		"req" : "del",
		"tag" : tag
	}, function(data) {
		location.reload();
	});
}

function appendTag(tag, index) {
	var html = "<tr>";
	html += "<td>" + tag + "</td>";
	html += "<td>" + "<button onclick='delTag(" + index + ")'>删除</button>" + "</td>";
	html += "</tr>";
	return html;
}
