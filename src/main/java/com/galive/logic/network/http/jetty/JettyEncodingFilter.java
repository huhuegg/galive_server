package com.galive.logic.network.http.jetty;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

@WebFilter(filterName="EncodingFilter", urlPatterns="/*")
public class JettyEncodingFilter implements Filter {

	@Override
	public void init(FilterConfig config) throws ServletException {
		
	}
	
	@Override
	public void destroy() {
		
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		String encoding = StandardCharsets.UTF_8.name();
		req.setCharacterEncoding(encoding);
		resp.setCharacterEncoding(encoding);
		resp.setContentType("text/plain; charset=" + encoding);
		chain.doFilter(req, resp);
	}

	

}
