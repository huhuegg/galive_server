package com.galive.logic.network.http;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import com.alibaba.fastjson.JSON;
import com.galive.logic.service.QuestionService;
import com.galive.logic.service.QuestionServiceImpl;

public class QuestionTagServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	public static final String REQ_LIST = "list";
	public static final String REQ_ADD = "add";
	public static final String REQ_DEL = "del";

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST");
		response.setHeader("Access-Control-Allow-Headers", "x-requested-with,content-type");
		String req = request.getParameter("req");
		String tag = request.getParameter("tag");
		if (StringUtils.isBlank(req)) {
			response.getWriter().write("error");
			return;
		}
		String result = "error";
		try {
			QuestionService questionService = new QuestionServiceImpl();
			if (req.equals(REQ_LIST)) {
				List<String> tags = questionService.listQuestionTags();
				result = JSON.toJSONString(tags);
			} else if (req.equals(REQ_ADD)) {
				if (!StringUtils.isBlank(tag)) {
					questionService.addQuestionTag(tag);
					result = "ok";
				}
			} else if (req.equals(REQ_DEL)) {
				if (!StringUtils.isBlank(tag)) {
					questionService.removeQuestionTag(tag);
					result = "ok";
				}
			} 
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			response.getWriter().write(result);
		}
	}

}
