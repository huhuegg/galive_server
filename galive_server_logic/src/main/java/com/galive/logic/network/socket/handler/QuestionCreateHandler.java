package com.galive.logic.network.socket.handler;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import com.alibaba.fastjson.JSON;
import com.galive.common.protocol.Command;
import com.galive.common.protocol.CommandOut;
import com.galive.logic.model.Question;
import com.galive.logic.network.model.RespQuestion;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.service.QuestionService;
import com.galive.logic.service.QuestionServiceImpl;

@SocketRequestHandler(desc = "创建问题", command = Command.QUESTION_CREATE)
public class QuestionCreateHandler extends SocketBaseHandler  {

	private QuestionService questionService = new QuestionServiceImpl();
	
	@Override
	public CommandOut handle(String userSid, String reqData) throws Exception {
		appendLog("--QuestionCreateHandler(创建问题)--");
		QuestionCreateIn in = JSON.parseObject(reqData, QuestionCreateIn.class);
		
		String desc = in.desc;
		List<String> imageUrls = in.imageUrls;
		String recordUrl = in.recordUrl;
		List<String> tags = in.tags;
		
		appendLog("问题描述(desc):" + desc);
		if (!CollectionUtils.isEmpty(imageUrls)) {
			for (String url : imageUrls) {
				appendLog("上传图片(imageUrl):" + url);
			}	
		}
		appendLog("录音url(recordUrl):" + recordUrl);
		appendLog("问题描述(desc):" + desc);
		if (!CollectionUtils.isEmpty(tags)) {
			for (String tag : tags) {
				appendLog("标签(tag):" + tag);
			}	
		}
		
		Question q = questionService.createQuestion(userSid, desc, imageUrls, recordUrl, tags);
		
		QuestionCreateOut out = new QuestionCreateOut();
		RespQuestion rq = new RespQuestion();
		rq.convert(q);
		out.question = rq;
		return out;
		
	}
	
	public static class QuestionCreateIn {
		public String desc = "";
		public List<String> imageUrls = new ArrayList<>();
		public String recordUrl = "";
		public List<String> tags = new ArrayList<>();
	}
	
	public static class QuestionCreateOut extends CommandOut {
		
		public RespQuestion question;
		
		public QuestionCreateOut() {
			super(Command.QUESTION_CREATE);
		}
	}
	
}
