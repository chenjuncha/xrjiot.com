package com.xrj.business.product.controller;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rongji.dfish.base.Page;
import com.rongji.dfish.base.Utils;
import com.rongji.dfish.framework.FilterParam;
import com.rongji.dfish.ui.command.AlertCommand;
import com.rongji.dfish.ui.command.CommandGroup;
import com.rongji.dfish.ui.command.DialogCommand;
import com.rongji.dfish.ui.command.ReplaceCommand;
import com.rongji.dfish.ui.command.TipCommand;
import com.rongji.dfish.ui.helper.GridPanel;
import com.rongji.dfish.ui.layout.HorizontalLayout;
import com.rongji.dfish.ui.layout.View;
import com.xrj.api.dto.DevDeveloper;
import com.xrj.api.dto.DevSubAccount;
import com.xrj.api.em.StatusEnum;
import com.xrj.business.core.base.PubConstants;
import com.xrj.business.core.base.PubService;
import com.xrj.business.core.view.PubView;
import com.xrj.business.product.service.DevSubAccountIndexService;
import com.xrj.business.product.view.DevSubAccountView;
import com.xrj.framework.util.HttpUtil;

/**
 * 
 * @author yangzeyi
 * @Date 2017-7-25
 *
 */
@Controller
@RequestMapping("product")
public class DevSubAccountController {

	@Resource
	private DevSubAccountIndexService devSubAccountService;

	/**
	 * 子账号视图界面
	 * 
	 * @param request
	 * @return 视图
	 */
	@RequestMapping("childAccountIndex")
	@ResponseBody
	public View buildChildAccountView(HttpServletRequest request) {
		FilterParam fp = PubService.getParam(request);
		List<Object> listObject = devSubAccountService.setPageDataList(request);
		List<DevSubAccount> list = (List<DevSubAccount>) listObject.get(0);
		Page dfishPage = (Page) listObject.get(1);
		View view = DevSubAccountView.buildChildAccountView(request, dfishPage, fp, list);
		return view;
	}

	/**
	 * 子账号分页查询
	 * 
	 * @param request
	 * @return 控件替换命令
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/subAccount/search")
	public Object search(HttpServletRequest request) throws Exception {
		CommandGroup cg = new CommandGroup();
		FilterParam fp = PubService.getParam(request);
		List<Object> listObject = devSubAccountService.setPageDataList(request);
		List<DevSubAccount> list = (List<DevSubAccount>) listObject.get(0);
		Page dfishPage = (Page) listObject.get(1);
		View view = DevSubAccountView.buildChildAccountView(request, dfishPage, fp, list);
		cg.add(new ReplaceCommand(view.findNodeById(DevSubAccountView.CHILDPAGE)));
		cg.add(new ReplaceCommand(view.findNodeById(DevSubAccountView.CHILDPANL)));
		return cg;
	}

	/**
	 * 子账号设备分页查询
	 * 
	 * @param request
	 * @return 控件替换命令
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/subAccountDevice/search")
	public Object deviceSearch(HttpServletRequest request) throws Exception {
		CommandGroup cg = new CommandGroup();
		String subId = request.getParameter("subAccountId");
		DevSubAccount devSubAccount = devSubAccountService.get(subId);
		View view = DevSubAccountView.buildTempChildAccount(request, devSubAccount);

		cg.add(new ReplaceCommand(view.findNodeById("subViewPanl")));
		cg.add(new ReplaceCommand(view.findNodeById("subViewPage")));
		return cg;
	}

	/**
	 * 生成主账号和证书命令 已生成：提示不能重复生成 未生成：提示成功生成
	 * 
	 * @param request
	 * @return 控件替换命令
	 */
	@RequestMapping("buildMainAccountCommand")
	@ResponseBody
	public Object buildSubAccountCommand(HttpServletRequest request) {
		// DevSubAccountService subAccountService =
		// remoteService.getDevSubAccountService();
		DevDeveloper devDeveloper = (DevDeveloper) HttpUtil.getSessionAttr(request, PubConstants.SESSION_DEV_DEVELOPER);
		if (devSubAccountService.isBuildMainAccount(devDeveloper) == true) {
			FilterParam fp = PubService.getParam(request);
			List<Object> listObject = devSubAccountService.setPageDataList(request);
			List<DevSubAccount> list = (List<DevSubAccount>) listObject.get(0);
			Page dfishPage = (Page) listObject.get(1);
			View view = DevSubAccountView.buildChildAccountView(request, dfishPage, fp, list);
			CommandGroup cg = new CommandGroup();
			AlertCommand dialogCommand = PubView.getInfoAlertNoTimeout("成功生成主账号及证书");
			ReplaceCommand replaceCommand = new ReplaceCommand(view.findNodeById(DevSubAccountView.CHILDMAIN));
			cg.add(dialogCommand).add(replaceCommand);
			return cg;
		}
		AlertCommand warnCommand = PubView.getWarnAlert("不能重复生成主账号及证书");
		return warnCommand;
	}

	/**
	 * 提示框显示完整主账号证书信息
	 * 
	 * @param request
	 * @return 提示框命令
	 */
	@RequestMapping("viewSecret")
	@ResponseBody
	public Object viewMainSecret(HttpServletRequest request) {
		String mainSecret = request.getParameter("mainSecret");
		return PubView.getInfoAlertNoTimeout("完整主账号信息：" + mainSecret);
	}

	/**
	 * 提示框显示完整子账号密码信息
	 * 
	 * @param request
	 * @return 提示框命令
	 */
	@RequestMapping("viewAccountSecret")
	@ResponseBody
	public Object subAccountSecret(HttpServletRequest request) {
		String id = request.getParameter("id");
		String newSecret = devSubAccountService.get(id).getAccountSecret();
		return PubView.getInfoAlertNoTimeout("完整子账号证书信息：" + newSecret);
	}

	/**
	 * 查看子账号信息及其下的设备信息
	 * 
	 * @param request
	 * @return 弹出框命令
	 */
	@RequestMapping("subAccount/view")
	@ResponseBody
	public Object childAccountCommand(HttpServletRequest request) {
		String subAccountId = request.getParameter("subAccountId");
		DialogCommand dialogCommand = new DialogCommand().setTemplate("std").setHeight("80%").setWidth("80%")
				.setTitle("查看子账号");
		dialogCommand.setSrc("product/subAccountDetailView?subAccountId=" + subAccountId);
		return dialogCommand;
	}

	/**
	 * 展示子账号所属的设备信息
	 * 
	 * @param request
	 * @return 视图
	 */
	@RequestMapping("subAccountDetailView")
	@ResponseBody
	public View buildChildAccountDetail(HttpServletRequest request) {
		String subAccountId = request.getParameter("subAccountId");
		DevSubAccount devSubAccount = devSubAccountService.get(subAccountId);
		View view = DevSubAccountView.buildTempChildAccount(request, devSubAccount);
		return view;
	}

	/**
	 * 删除子账号
	 * 
	 * @param request
	 * @return 控件替换命令
	 */
	@RequestMapping("subAccount/delete")
	@ResponseBody
	public Object deleteSubAccountById(HttpServletRequest request) {
		String subAccountId = request.getParameter("subAccountId");
		String[] sourceStrArray = subAccountId.split(",");

		boolean isDelete = devSubAccountService.batchDelete(sourceStrArray);
		if (isDelete == false) {
			return PubView.getInfoAlertNoTimeout("请先禁用子账号");
		}
		CommandGroup group = new CommandGroup();
		TipCommand tipCommand = new TipCommand("删除成功");
		ReplaceCommand replaceCommand = new ReplaceCommand();
		FilterParam fp = PubService.getParam(request);
		List<Object> listObject = devSubAccountService.setPageDataList(request);
		List<DevSubAccount> list = (List<DevSubAccount>) listObject.get(0);
		Page dfishPage = (Page) listObject.get(1);
		View view = DevSubAccountView.buildChildAccountView(request, dfishPage, fp, list);
		replaceCommand.setNode(view.findNodeById(DevSubAccountView.CHILDMAIN));
		group.add(tipCommand);
		group.add(replaceCommand);
		return group;
	}

	/**
	 * 禁用子账号
	 * 
	 * @param request
	 * @return 控件替换命令
	 */
	@RequestMapping("subAccount/disable")
	@ResponseBody
	public Object disableSubAccountById(HttpServletRequest request) {
		String subAccountId = request.getParameter("subAccountId");
		String[] sourceStrArray = subAccountId.split(",");
		devSubAccountService.batchUpdateSubAccount(sourceStrArray, StatusEnum.DELETE.getIndex());
		CommandGroup group = new CommandGroup();
		TipCommand tipCommand = new TipCommand("禁用成功");
		ReplaceCommand replaceCommand = new ReplaceCommand();
		FilterParam fp = PubService.getParam(request);
		List<Object> listObject = devSubAccountService.setPageDataList(request);
		List<DevSubAccount> list = (List<DevSubAccount>) listObject.get(0);
		Page dfishPage = (Page) listObject.get(1);
		View view = DevSubAccountView.buildChildAccountView(request, dfishPage, fp, list);
		replaceCommand.setNode(view.findNodeById(DevSubAccountView.CHILDMAIN));
		group.add(tipCommand);
		group.add(replaceCommand);
		return group;
	}

	/**
	 * 启用子账号
	 * 
	 * @param request
	 * @return 控件替换命令
	 */
	@RequestMapping("subAccount/enable")
	@ResponseBody
	public Object enableSubAccountById(HttpServletRequest request) {
		String subAccountId = request.getParameter("subAccountId");
		String[] sourceStrArray = subAccountId.split(",");
		// DevSubAccountService subAccountService =
		// remoteService.getDevSubAccountService();
		devSubAccountService.batchUpdateSubAccount(sourceStrArray, StatusEnum.NORMAL.getIndex());
		CommandGroup group = new CommandGroup();
		TipCommand tipCommand = new TipCommand("启用成功");
		ReplaceCommand replaceCommand = new ReplaceCommand();
		FilterParam fp = PubService.getParam(request);
		List<Object> listObject = devSubAccountService.setPageDataList(request);
		List<DevSubAccount> list = (List<DevSubAccount>) listObject.get(0);
		Page dfishPage = (Page) listObject.get(1);
		View view = DevSubAccountView.buildChildAccountView(request, dfishPage, fp, list);
		replaceCommand.setNode(view.findNodeById(DevSubAccountView.CHILDMAIN));
		group.add(tipCommand);
		group.add(replaceCommand);
		return group;
	}

	/**
	 * 批量生成子账号 未生成主账号提示请先生成主账号及证书
	 * 
	 * @param request
	 * @return 控件替换命令
	 */
	@RequestMapping("batchSubAccountCommand")
	@ResponseBody
	public Object batchSubAccountCommand(HttpServletRequest request) {
		String subAccount = request.getParameter("subAccountNum");
		Pattern pattern = Pattern.compile("^([1-9]\\d{0,2}|1000)$");
		Matcher matcher = pattern.matcher(subAccount);
		if (matcher.matches() == false) {
			return PubView.getInfoAlertNoTimeout("请输入1~1000的数字");
		}
		int subAccountNum = Integer.parseInt(subAccount);
		DevDeveloper devDeveloper = (DevDeveloper) HttpUtil.getSessionAttr(request, PubConstants.SESSION_DEV_DEVELOPER);
		if (Utils.isEmpty(devSubAccountService.findPrimaryAccount(devDeveloper))) {
			return PubView.getInfoAlertNoTimeout("请先生成主账号及证书！！！");
		}
		devSubAccountService.batchBuildSubAccount(devDeveloper, subAccountNum);
		CommandGroup cg = new CommandGroup();
		TipCommand tipCommand = new TipCommand("生成子账号成功");
		ReplaceCommand replaceCommand = new ReplaceCommand();
		FilterParam fp = PubService.getParam(request);
		List<Object> listObject = devSubAccountService.setPageDataList(request);
		List<DevSubAccount> list = (List<DevSubAccount>) listObject.get(0);
		Page dfishPage = (Page) listObject.get(1);
		View view = DevSubAccountView.buildChildAccountView(request, dfishPage, fp, list);
		replaceCommand.setNode(view.findNodeById(DevSubAccountView.CHILDMAIN));
		cg.add(tipCommand).add(replaceCommand);
		return cg;
	}

	/**
	 * 子账号搜索框查询
	 * 
	 * @param request
	 * @return 控件替换命令
	 */
	@RequestMapping("subAccount/searchSubAccount")
	@ResponseBody
	public Object subAccountSearch(HttpServletRequest request) {
		String search = request.getParameter("subAccountSearch");
		FilterParam fp = PubService.getParam(request);
		List<Object> listObject = devSubAccountService.setPageByDevSubAccount(request);
		List<DevSubAccount> list = (List<DevSubAccount>) listObject.get(0);
		Page dfishPage = (Page) listObject.get(1);
		GridPanel gridPanel = DevSubAccountView.buildGridPanel(list);
		HorizontalLayout pageLayout = DevSubAccountView.buildSearChPageLayout(dfishPage, fp, search);
		CommandGroup cg = new CommandGroup();
		cg.add(new ReplaceCommand().setNode(gridPanel));
		cg.add(new ReplaceCommand().setNode(pageLayout));
		return cg;
	}
}
