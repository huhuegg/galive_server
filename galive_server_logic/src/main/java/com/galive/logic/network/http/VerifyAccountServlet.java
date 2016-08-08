package com.galive.logic.network.http;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.galive.logic.service.AccountService;
import com.galive.logic.service.AccountServiceImpl;

public class VerifyAccountServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		verifyAccount(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		verifyAccount(req, resp);
	}
	
	private void verifyAccount(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String account = req.getParameter("account");
		AccountService accountService = new AccountServiceImpl();
		boolean result = accountService.verifyAccount(account);
		resp.getWriter().write(result + "");
	}
	
}
