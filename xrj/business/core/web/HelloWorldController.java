package com.xrj.business.core.web;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rongji.dfish.ui.layout.View;
import com.rongji.dfish.ui.widget.Html;
import com.xrj.api.service.HelloWorldService;
import com.xrj.business.core.base.RemoteService;

@Controller
@RequestMapping("helloWorld")
public class HelloWorldController {
	@Resource
	RemoteService remoteService;
	@RequestMapping("/index")
	@ResponseBody
	public Object index(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		View view=new View();
		view.setNode(new Html(null,"HELLO WORLD!"));
		HelloWorldService helloWorldService = remoteService.getHelloWorldService();
		helloWorldService.sayHello();
		return view;
	}
	
	@RequestMapping("/indexF")
	@ResponseBody
	public Object indexF(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		View view=new View();
		view.setNode(new Html(null,"HELLO FFFFF!"));
		HelloWorldService helloWorldService = remoteService.getHelloWorldService();
		helloWorldService.sayHello();
		return view;
	}
}
