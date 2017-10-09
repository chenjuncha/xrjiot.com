package com.xrj.business.product.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rongji.dfish.base.Page;
import com.rongji.dfish.framework.FilterParam;
import com.rongji.dfish.ui.command.CommandGroup;
import com.rongji.dfish.ui.command.DialogCommand;
import com.rongji.dfish.ui.command.JSCommand;
import com.rongji.dfish.ui.command.ReplaceCommand;
import com.rongji.dfish.ui.helper.GridPanel;
import com.rongji.dfish.ui.layout.HorizontalLayout;
import com.rongji.dfish.ui.layout.VerticalLayout;
import com.rongji.dfish.ui.layout.View;
import com.xrj.api.dto.DevDevice;
import com.xrj.api.dto.DevProduct;
import com.xrj.api.dto.DevProductFunction;
import com.xrj.api.model.DeviceRunningState;
import com.xrj.business.core.base.PubService;
import com.xrj.business.core.view.PubView;
import com.xrj.business.product.service.DevDeviceInfoService;
import com.xrj.business.product.service.DeviceControlService;
import com.xrj.business.product.view.DevDeviceInfoView;
import com.xrj.framework.util.HttpUtil;

/**
 * 
 * @author yangzeyi
 * @date 2017年7月31日
 *
 */
@Controller
@RequestMapping("product")
public class DevDeviceInfoController {

	@Resource
	private DevDeviceInfoService devDeviceInfoService;
	@Resource
	private DeviceControlService deviceControlService;

	/**
	 * 设备信息界面生成
	 * 
	 * @param request
	 * @return 视图
	 */
	@RequestMapping("devDeviceInfo")
	@ResponseBody
	public View buildDevDeviceView(HttpServletRequest request) {
		FilterParam fp = PubService.getParam(request);
		DevProduct product = (DevProduct) HttpUtil.getSessionAttr(request, "product");
		List<Object> list = devDeviceInfoService.queryPageList(request);
		List<DevDevice> deviceList = (List<DevDevice>) list.get(0);
		Page dfishPage = (Page) list.get(1);
		boolean exist = deviceControlService.hasSimnulateDevice(product.getId());
		View view = DevDeviceInfoView.buildDevInfoView(request, deviceList, dfishPage, fp , exist);
		return view;
	}

	/**
	 * 
	 * 设备信息界面——生成信息界面
	 * 
	 * @param req
	 * @return JS命令
	 */
	@RequestMapping("devDeviceInfo/toBuildDevice")
	@ResponseBody
	public Object toBuildDevicePage(HttpServletRequest req) {
		String menuId = req.getParameter("menuId");
		return new JSCommand("VM(this.parent).find('mainContent').view('v_" + menuId + "');VM(this.parent).find('"
				+ menuId + "').focus(true)");
	}

	/**
	 * 功能点信息弹出框(有子设备弹出框、无子设备弹出框)
	 * 
	 * @param request
	 * @return 弹出框命令
	 */
	@RequestMapping("devDeviceInfo/view")
	@ResponseBody
	public Object viewDialogCommand(HttpServletRequest request) {
		String deviceId = request.getParameter("deviceId");
		DialogCommand dialogCommand = new DialogCommand().setWidth("80%").setHeight("90%").setTemplate("std")
				.setTitle("功能点信息");
		// FilterParam fp = PubService.getParam(request);
		List<DevDevice> list = (List<DevDevice>) devDeviceInfoService.setPageByParentId(request).get(0);
		if (list.size() == 0) {
			dialogCommand.setSrc("product/devDeviceInfo/viewDialog?deviceId=" + deviceId);
		} else {
			dialogCommand.setSrc("product/devDeviceInfo/viewDialogSub?deviceId=" + deviceId);
		}
		return dialogCommand;
	}

	/**
	 * 有子设备弹出框试图
	 * 
	 * @param request
	 * @return 视图
	 */
	@RequestMapping("devDeviceInfo/viewDialogSub")
	@ResponseBody
	public View devDeviceInfoDialogViewSub(HttpServletRequest request) {
		String deviceId = request.getParameter("deviceId");
		DevDevice devDevice = devDeviceInfoService.get(deviceId);
		DevProduct product = (DevProduct) HttpUtil.getSessionAttr(request, "product");
		List<DevDevice> devList = new ArrayList<DevDevice>();
		devList.add(devDevice);
		FilterParam fp = PubService.getParam(request);
		List<Object> list = devDeviceInfoService.setPageByParentId(request);
		List<DevDevice> deviceList = (List<DevDevice>) list.get(0);
		Page dfishPage = (Page) list.get(1);
		List<DeviceRunningState> drList = devDeviceInfoService.getOnlineStatusList(devList);
		List<DevProductFunction> list1 = devDeviceInfoService.filterFunctionData(devDevice);
		View view = DevDeviceInfoView.buildDialogView2(deviceList, devDevice, product.getProductKey(), drList,
				dfishPage, fp, list1);
		return view;
	}

	/**
	 * 无子设备弹出框视图
	 * 
	 * @param request
	 * @return 视图
	 */
	@RequestMapping("devDeviceInfo/viewDialog")
	@ResponseBody
	public View devDeviceInfoDialogView(HttpServletRequest request) {
		// Boolean online = drList.get(0).isOnline();
		String deviceId = request.getParameter("deviceId");
		DevProduct product = (DevProduct) HttpUtil.getSessionAttr(request, "product");
		DevDevice devDevice = devDeviceInfoService.get(deviceId);
		List<DevDevice> devList = new ArrayList<DevDevice>();
		devList.add(devDevice);
		List<DeviceRunningState> drList = devDeviceInfoService.getOnlineStatusList(devList);
		List<DevProductFunction> list = devDeviceInfoService.filterFunctionData(devDevice);
		View view = DevDeviceInfoView.buildDialogView(devDevice, product.getProductKey(), drList, list);
		return view;
	}

	/**
	 * 设备信息主界面分页查询控件替换方法
	 * 
	 * @param request
	 * @return 控件替换命令
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/devDeviceInfo/search")
	public Object deviceSearch(HttpServletRequest request) throws Exception {
		CommandGroup cg = new CommandGroup();
		FilterParam fp = PubService.getParam(request);
		List<Object> list = devDeviceInfoService.queryPageList(request);
		List<DevDevice> deviceList = (List<DevDevice>) list.get(0);
		Page dfishPage = (Page) list.get(1);
		DevProduct product = (DevProduct) HttpUtil.getSessionAttr(request, "product");
		boolean exist = deviceControlService.hasSimnulateDevice(product.getId());
		View view = DevDeviceInfoView.buildDevInfoView(request, deviceList, dfishPage, fp , exist);

		cg.add(new ReplaceCommand(view.findNodeById(DevDeviceInfoView.DEVICE_INFO_MAIN_PANL)));
		cg.add(new ReplaceCommand(view.findNodeById(DevDeviceInfoView.DEVICE_INFO_PAGE)));
		return cg;
	}

	/**
	 * 设备信息主界面搜索框查询
	 * 
	 * @param request
	 * @return 控件替换命令
	 */
	@ResponseBody
	@RequestMapping("devDeviceInfo/searchDevice")
	public Object searchDevice(HttpServletRequest request) {
		String deviceSearch = request.getParameter("deviceSearch");
		FilterParam fp = PubService.getParam(request);
		List<Object> listObject = devDeviceInfoService.setPageByDevice(request);
		List<DevDevice> list = (List<DevDevice>) listObject.get(0);
		Page dfishPage = (Page) listObject.get(1);
		// 过滤最后一次在线時間
		List<DeviceRunningState> drList = devDeviceInfoService.getOnlineStatusList(list);
		list = DevDeviceInfoView.filterOnlineData(list, drList);
		GridPanel gridPanel = DevDeviceInfoView.buildDevInfoGridPanel(list, request);
		CommandGroup cg = new CommandGroup();
		HorizontalLayout horizontalLayout = DevDeviceInfoView.buildSearChPageLayout(dfishPage, fp, deviceSearch);
		cg.add(new ReplaceCommand(gridPanel));
		cg.add(new ReplaceCommand(horizontalLayout));
		return cg;
	}

	/**
	 * 
	 * 设备信息主界面搜索框分页查询页面控件替换方法
	 * 
	 * @param request
	 * @return 控件替换命令
	 */
	@ResponseBody
	@RequestMapping("devDeviceInfo/searchPage")
	public Object searchPage(HttpServletRequest request) {
		String deviceSearch = request.getParameter("deviceSearch");
		List<Object> listObject = devDeviceInfoService.setPageByDevice(request);
		List<DevDevice> list = (List<DevDevice>) listObject.get(0);
		Page dfishPage = (Page) listObject.get(1);
		FilterParam fp = PubService.getParam(request);
		GridPanel gridPanel = DevDeviceInfoView.buildDevInfoGridPanel(list, request);
		CommandGroup cg = new CommandGroup();
		HorizontalLayout horizontalLayout = DevDeviceInfoView.buildSearChPageLayout(dfishPage, fp, deviceSearch);
		cg.add(new ReplaceCommand(gridPanel));
		cg.add(new ReplaceCommand(horizontalLayout));
		return cg;
	}

	/**
	 * 弹出框分页查询
	 * 
	 * @param request
	 * @return 控件替换命令
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("devDeviceInfo/dialogSearch")
	public Object dialogSearch(HttpServletRequest request) throws Exception {
		String deviceId = request.getParameter("deviceId");
		FilterParam fp = PubService.getParam(request);
		List<Object> list = devDeviceInfoService.setPageByParentId(request);
		List<DevDevice> deviceList = (List<DevDevice>) list.get(0);
		Page dfishPage = (Page) list.get(1);
		CommandGroup cg = new CommandGroup();
		List<DeviceRunningState> drList = devDeviceInfoService.getOnlineStatusList(deviceList);
		VerticalLayout verticalLayout = DevDeviceInfoView.buildDialogView2Panel(deviceList, drList);
		HorizontalLayout horizontalLayout = DevDeviceInfoView.buildDialogViewPage(dfishPage, fp, deviceId);
		cg.add(new ReplaceCommand(verticalLayout));
		cg.add(new ReplaceCommand(horizontalLayout));
		return cg;
	}

	/**
	 * 查看设备证书完整信息
	 * 
	 * @param request
	 * @return 提示框命令
	 */
	@ResponseBody
	@RequestMapping("devDeviceInfo/viewDeviceSecret")
	public Object viewDeviceSecret(HttpServletRequest request) {
		String deviceId = request.getParameter("deviceId");
		DevDevice devDevice = devDeviceInfoService.get(deviceId);
		return PubView.getInfoAlertNoTimeout("完整的设备证书信息:" + devDevice.getDeviceSecret());
	}
}
