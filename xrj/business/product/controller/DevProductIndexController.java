package com.xrj.business.product.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rongji.dfish.ui.command.CommandGroup;
import com.rongji.dfish.ui.command.JSCommand;
import com.rongji.dfish.ui.command.ReplaceCommand;
import com.rongji.dfish.ui.command.TipCommand;
import com.rongji.dfish.ui.layout.VerticalLayout;
import com.rongji.dfish.ui.layout.View;
import com.xrj.api.dto.DevProduct;
import com.xrj.api.em.ProductStatusEnum;
import com.xrj.api.support.CallResult;
import com.xrj.business.core.view.PubView;
import com.xrj.business.product.service.DevMenuSevice;
import com.xrj.business.product.service.DevProductIndexService;
//import com.xrj.api.service.DevProductService;
import com.xrj.business.product.view.AddNewProductView;
import com.xrj.business.product.view.DevMenuView;
import com.xrj.business.product.view.DevProductView;
import com.xrj.framework.util.HttpUtil;

/**
 * 
 * @author yangzeyi
 * @Date 2017年7月19日
 *
 */
@Controller
@RequestMapping("/product")
public class DevProductIndexController {

	@Resource
	private DevProductIndexService devProductIndexService;
	@Resource
	private DevMenuSevice devMenuSevice;

	/**
	 * 产品管理入口界面
	 * 
	 * @param request
	 * @return 视图
	 */
	@RequestMapping("/index")
	@ResponseBody
	public View productIndex(HttpServletRequest request) {
		List<DevProduct> publishedList = devProductIndexService.queryProductList(request,
				ProductStatusEnum.PUBLISHED.getIndex(), null);
		View view = DevProductView.bulidProductIndex(publishedList);
		return view;
	}

	@RequestMapping("/productSearch")
	@ResponseBody
	public Object productSearch(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String productStatus = request.getParameter("productStatus");
		String productName = request.getParameter("productName");
		List<DevProduct> list = devProductIndexService.queryProductList(request, productStatus, productName);
		ReplaceCommand rc = new ReplaceCommand().setNode(DevProductView.bulidProductList(list));
		return rc;
	}

	/**
	 * 产品列表界面———产品基本信息页面
	 * 
	 * @param request
	 * @return 控件替换命令
	 */
	@RequestMapping("/baseInfoCommand")
	@ResponseBody
	public CommandGroup baseInfoCommand(HttpServletRequest request) {
		String anchor = request.getParameter("anchor");
		String productId = request.getParameter("productId");
		CommandGroup commandGroup = new CommandGroup();
		DevProduct product = devProductIndexService.get(productId);

		HttpUtil.setSessionAttr(request, "product", product);
		HttpUtil.setSessionAttr(request, "clicked", false);
		HttpUtil.setSessionAttr(request, "clickMenuId", "010101000000000000000000");
		View view = DevMenuView.buildMenuView(anchor, product, devMenuSevice.listMenu());
		commandGroup.add(new ReplaceCommand(view.findNodeById("root")));
		// 更新产品名称显示
		commandGroup
				.add(new JSCommand("Q(window.parent.document).contents().find(\"div[w-id='productNameHtml']\").html('|　"
						+ product.getProductName() + "');"));
		return commandGroup;
	}

	/**
	 * 产品列表界面———增加新产品界面
	 * 
	 * @param request
	 * @return 控件替换命令
	 */
	@RequestMapping("/addNewProductCommand")
	@ResponseBody
	public ReplaceCommand addNewProductCommand(HttpServletRequest request) {
		View view = AddNewProductView.addNewProductView();
		VerticalLayout verticalLayout = (VerticalLayout) view.findNodeById("root");
		return new ReplaceCommand(verticalLayout);
	}

	/**
	 * 从新增页面跳转至产品列表页面
	 * 
	 * @param request
	 * @return 控件替换命令
	 */
	@RequestMapping("/toBackCommand")
	@ResponseBody
	public Object toBackProductList(HttpServletRequest request) {
		List<DevProduct> releaseList = devProductIndexService.queryProductList(request,
				ProductStatusEnum.PUBLISHED.getIndex(), null);
		View view = DevProductView.bulidProductIndex(releaseList);
		VerticalLayout verticalLayout = (VerticalLayout) view.findNodeById("root");
		return new ReplaceCommand(verticalLayout);
	}

	/**
	 * 刷新产品列表页面
	 * 
	 * @param request
	 * @return JS命令
	 */
	@RequestMapping("/toBackCommand2")
	@ResponseBody
	public Object toBackProductList2(HttpServletRequest request) {
		return new JSCommand("VM(this.parent).find('root').ownerView.reload();");
	}

	/**
	 * 删除产品、删除成功后跳转至产品列表界面
	 * 
	 * @param request
	 * @return 控件替换命令
	 */
	@RequestMapping("/deleteAndtoBack")
	@ResponseBody
	public Object deleteAndtoBack(HttpServletRequest request) {
		String productId = request.getParameter("productId");
		// DevProductService devProductService =
		// remoteService.getDevProductService();
		CallResult callResult = devProductIndexService.delete(productId);
		if (callResult.isSuccess() == false) {

			return PubView.getInfoAlertNoTimeout(callResult.getMessage());
		}
		List<DevProduct> releaseList = devProductIndexService.queryProductList(request,
				ProductStatusEnum.PUBLISHED.getIndex(), null);
		View view = DevProductView.bulidProductIndex(releaseList);
		VerticalLayout verticalLayout = (VerticalLayout) view.findNodeById("root");
		CommandGroup commandGroup = new CommandGroup();
		commandGroup.add(new TipCommand("删除成功"));
		return commandGroup.add(new ReplaceCommand(verticalLayout));
	}

	/**
	 * 右侧菜单点击改变试图命令
	 * 
	 * @param request
	 * @return 控件替换命令
	 * @throws Exception
	 */
	@RequestMapping("/clickMenu")
	@ResponseBody
	public Object clickMenu(HttpServletRequest request) throws Exception {

		String menuId = request.getParameter("menuId");
		CommandGroup cg = new CommandGroup();
		// DevProductService devProductService =
		// remoteService.getDevProductService();
		// 点击产品列表-->产品列表界面
		// if (menuId.equals("010101000000000000000000")) {
		// DevDeveloper devDeveloper = (DevDeveloper)
		// HttpUtil.getSessionAttr(request,
		// PubConstants.SESSION_DEV_DEVELOPER);
		// List<DevProduct> publishedList =
		// devProductIndexService.queryProductList(devDeveloper.getId(),
		// ProductStatusEnum.PUBLISHED.getIndex(), null);
		// List<DevProduct> unPublishedList =
		// devProductService.queryUnPublishedProductList(devDeveloper.getId());
		// View view = DevProductView.bulidProductIndex(publishedList);
		// cg.add(new ReplaceCommand(view.findNodeById("root")));
		// } else {
		boolean clicked = (boolean) HttpUtil.getSessionAttr(request, "clicked");
		String clickMenuId = (String) HttpUtil.getSessionAttr(request, "clickMenuId");
		if (clicked == true) {
			if (clickMenuId.equals(menuId)) {
				cg.add(new JSCommand("VM(this).find('mainContent').view('v_" + menuId + "');"));
				cg.add(new JSCommand("VM(this).find('v_" + menuId + "').reload();"));
			} else {
				HttpUtil.setSessionAttr(request, "clickMenuId", menuId);
				cg.add(new JSCommand("VM(this).find('mainContent').view('v_" + menuId + "');"));
				HttpUtil.setSessionAttr(request, "clicked", true);
			}
		} else {
			cg.add(new JSCommand("VM(this).find('mainContent').view('v_" + menuId + "');"));
			HttpUtil.setSessionAttr(request, "clicked", true);
		}
		// cg.add(new JSCommand("VM(this).find('mainContent').view('v_" +
		// menuId + "');"));
		// cg.add(new JSCommand("VM(this).find('v_"+menuId+"').reload();"));
		// }

		return cg;
	}

	/**
	 * 新增产品、新增成功后跳转至产品列表界面 新增失败后弹出框提示
	 * 
	 * @param request
	 * @return 控件替换命令
	 */
	@RequestMapping("/addNewProduct")
	@ResponseBody
	public Object addNewProduct(HttpServletRequest request) {
		CallResult callResult = devProductIndexService.save(request);

		CommandGroup commandGroup = new CommandGroup();
		if (callResult.isSuccess() == false) {
			commandGroup.add(PubView.getInfoAlertNoTimeout(callResult.getMessage()));
			return commandGroup;
		}
		TipCommand command = new TipCommand("新增产品成功");
		List<DevProduct> releaseList = devProductIndexService.queryProductList(request,
				ProductStatusEnum.PUBLISHED.getIndex(), null);
		// List<DevProduct> unreleaseList =
		// devProductService.queryUnPublishedProductList(devDeveloper.getId());
		View view = DevProductView.bulidProductIndex(releaseList);
		VerticalLayout verticalLayout = (VerticalLayout) view.findNodeById("root");
		commandGroup.add(command);
		commandGroup.add(new ReplaceCommand(verticalLayout));
		return commandGroup;
	}

	/**
	 * 发布产品、跳转到基本信息页面
	 * 
	 * @param request
	 * @return 控件替换命令
	 */
	@RequestMapping("publishProduct")
	@ResponseBody
	public Object publishProduct(HttpServletRequest request) {
		// DevProduct product = (DevProduct) HttpUtil.getSessionAttr(request,
		// "product");
		String productId = request.getParameter("productId");
		CommandGroup cg = new CommandGroup();
		devProductIndexService.publishProduct(productId);
		TipCommand tip = new TipCommand("发布成功");
		cg.add(tip);
		List<DevProduct> releaseList = devProductIndexService.queryProductList(request,
				ProductStatusEnum.PUBLISHED.getIndex(), null);
		View view = DevProductView.bulidProductIndex(releaseList);
		VerticalLayout verticalLayout = (VerticalLayout) view.findNodeById("root");
		cg.add(new ReplaceCommand(verticalLayout));
		return cg;
	}
}
