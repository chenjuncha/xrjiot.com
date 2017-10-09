package com.xrj.business.product.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rongji.dfish.ui.command.CommandGroup;
import com.rongji.dfish.ui.command.ReplaceCommand;
import com.rongji.dfish.ui.command.TipCommand;
import com.rongji.dfish.ui.layout.View;
import com.xrj.api.dto.DevProduct;
import com.xrj.api.support.CallResult;
import com.xrj.business.core.view.PubView;
import com.xrj.business.product.service.DevProductIndexService;
import com.xrj.business.product.view.DevBaseInfoView;
import com.xrj.framework.util.HttpUtil;

@Controller
@RequestMapping("/product")
public class DevBaseInfoController {
	@Resource
	private DevProductIndexService devProductIndexService;

	/**
	 * 产品基本信息页面
	 * 
	 * @param request
	 * @return 视图
	 */
	@RequestMapping("/baseInfo")
	@ResponseBody
	public View baseInfo(HttpServletRequest request) {
		DevProduct devProduct = (DevProduct) HttpUtil.getSessionAttr(request, "product");
		View view = DevBaseInfoView.baseInfo(devProduct);
		return view;

	}

	/**
	 * 基本信息页面——>修改页面
	 * 
	 * @param request
	 * @return 控件替换命令
	 */
	@RequestMapping("/baseInfo/modify")
	@ResponseBody
	public Object toModifyCommand(HttpServletRequest request) {
		ReplaceCommand replaceCommand = new ReplaceCommand();
		DevProduct product = (DevProduct) HttpUtil.getSessionAttr(request, "product");
		View view = DevBaseInfoView.buildProductModifyView(product);
		replaceCommand.setNode(view.findNodeById("main_content"));
		CommandGroup group = new CommandGroup();
		group.add(replaceCommand);
		return group;
	}

	/**
	 * 修改界面——取消按钮——基本信息页面
	 * 
	 * @param request
	 * @return 控件替换命令
	 */
	@RequestMapping("/baseInfo/toBaseInfo")
	@ResponseBody
	public Object toBaseInfoCommand(HttpServletRequest request) {
		DevProduct product = (DevProduct) HttpUtil.getSessionAttr(request, "product");
		View view = DevBaseInfoView.baseInfo(product);
		return new ReplaceCommand(view.findNodeById("main_content"));
	}

	/**
	 * 修改产品信息
	 * 
	 * @param request
	 * @return 控件替换命令
	 */
	@RequestMapping("modifyProduct")
	@ResponseBody
	public Object modifyBaseInfo(HttpServletRequest request) {
		CallResult callResult = devProductIndexService.update(request);
		DevProduct devProduct = (DevProduct) HttpUtil.getSessionAttr(request, "product");
		View view = DevBaseInfoView.baseInfo(devProduct);
		if (callResult.isSuccess() == false) {

			return PubView.getInfoAlertNoTimeout(callResult.getMessage());
		}
		CommandGroup commandGroup = new CommandGroup();
		commandGroup.add(new TipCommand("修改产品信息成功"));
		commandGroup.add(new ReplaceCommand(view.findNodeById("main_content")));
		return commandGroup;
	}
}
