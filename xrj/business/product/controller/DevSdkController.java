package com.xrj.business.product.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rongji.dfish.ui.layout.View;
import com.xrj.business.product.view.DevSdkView;

@Controller
@RequestMapping("product")
public class DevSdkController {
	@RequestMapping("sdk")
	@ResponseBody
	public View buildSdkIndex() {
		View view = DevSdkView.buildSdkView();
		return view;
	}
}
