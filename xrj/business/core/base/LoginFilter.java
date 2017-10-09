package com.xrj.business.core.base;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.rongji.dfish.base.Utils;
import com.rongji.dfish.framework.FrameworkHelper;
import com.rongji.dfish.ui.command.JSCommand;
import com.xrj.framework.util.HttpUtil;

public class LoginFilter implements Filter {

	private Set<String> containStr;
	private Set<String> endStr;	
	private Set<String> startStr;	
	
	@Override
    public void destroy() {
	    // TODO Auto-generated method stub
	    
    }

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest  request=(HttpServletRequest)req;
		HttpServletResponse response=(HttpServletResponse)resp;
		String url=request.getRequestURI();

		if (Utils.notEmpty(url)) {
			if (Utils.notEmpty(containStr)) {
				for(String str : containStr) {
					if (url.contains(str)) {
		 				chain.doFilter(req, resp);
						return;
					}
				}
			}
			if (Utils.notEmpty(endStr)) {
				for(String str : endStr) {
					if (url.endsWith(str)) {
						chain.doFilter(req, resp);
						return;
					}
				}
			}
		}
		

//		String loginUserId = FrameworkHelper.getLoginUser(request);
		String role = (String) HttpUtil.getSessionAttr(request, PubConstants.SESSION_USER_ROLE);
		
		if (Utils.notEmpty(role)) {
			chain.doFilter(req, resp);
			return;
		} else {
			String httpProto=request.getHeader("X-Forwarded-Proto");
			httpProto=httpProto==null?"http":httpProto;
			String contextPath= request.getContextPath();
			String redirectUrl=httpProto+"://"+ request.getServerName()+contextPath+"/login.jsp";
			if(FrameworkHelper.isFromDFish(request)) {
				FrameworkHelper.outputJson(response, new JSCommand("alert('登录超时,请重新登录');window.location.replace('"+contextPath+"');"));
				return;
			} else {
				System.out.println(redirectUrl);
				response.sendRedirect(redirectUrl);
				return;
			}
		}
	}
	
	@Override
    public void init(FilterConfig fc) throws ServletException {
//		System.setProperty(Constants.CONFIG_PAM_FILTER_TYPE, Constants.CONFIG_PAM_FILTER_TYPE_DEV);
		String containString = fc.getInitParameter("containString");
		containStr = new HashSet<String>();
		if (Utils.notEmpty(containString)) {
			String[] containArray = containString.split("[\\|]");
			for (String str : containArray) {
				containStr.add(str);
			}
		}
		String endString = fc.getInitParameter("endString");
		endStr = new HashSet<String>();
		if (Utils.notEmpty(endString)) {
			String[] endArray = endString.split("[\\|]");
			for (String str : endArray) {
				endStr.add(str);
			}
		}
		String startString = fc.getInitParameter("startString");
		startStr = new HashSet<String>();
		if (Utils.notEmpty(startString)) {
			String[] endArray = startString.split("[\\|]");
			for (String str : endArray) {
				startStr.add(str);
			}
		}
    }
	
//	private void removeToken(FilterChain chain, HttpServletRequest request,
//			HttpServletResponse response) throws IOException, ServletException {
//		//如果有token
//		String uri = request.getRequestURI();
//		String url = getServAddr(request);
//		// 将第一个/去掉
//		if (uri.startsWith("/")) {
//			uri = uri.substring(1);
//		}
//		url += uri;
//		String queryString=request.getQueryString();
//		if(queryString!=null&&queryString.indexOf("PARTY_TOKEN")>=0){
//			String[] pairs=queryString.split("[&]");
//			StringBuilder sb=new StringBuilder();
//			for(String pair:pairs){
//				if(pair.startsWith("PARTY_TOKEN=")){
//					continue;
//				}
//				sb.append('&');
//				sb.append(pair);
//			}
//			
//			String fullPath=url;
//			if(sb.length()>0){
//				fullPath+="?"+sb.substring(1);
//			}
//			response.sendRedirect(fullPath);
//		}else{
//			//没有token
//			chain.doFilter(request, response);
//		}
//		
//	}
//
//	private String getServAddr(HttpServletRequest request) {
//	    String servAddr = "";
//	    servAddr += request.getScheme();
//	    servAddr += request.getServletPath();
//	    return servAddr;
//    }

}
