package com.xrj.business.product.controller;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rongji.dfish.base.Page;
import com.rongji.dfish.base.Utils;
import com.rongji.dfish.framework.FilterParam;
import com.rongji.dfish.ui.Widget;
import com.rongji.dfish.ui.command.AlertCommand;
import com.rongji.dfish.ui.command.CommandGroup;
import com.rongji.dfish.ui.command.JSCommand;
import com.rongji.dfish.ui.command.ReplaceCommand;
import com.rongji.dfish.ui.command.TipCommand;
import com.rongji.dfish.ui.layout.View;
import com.xrj.api.dto.DevDevice;
import com.xrj.api.dto.DevProduct;
import com.xrj.api.dto.DevProductFunction;
import com.xrj.api.em.DataTypeEnum;
import com.xrj.api.em.FunctionTypeEnum;
import com.xrj.api.model.DeviceRunningState;
import com.xrj.api.support.CallResult;
import com.xrj.business.core.base.PubService;
import com.xrj.business.core.base.RemoteService;
import com.xrj.business.core.view.PubView;
import com.xrj.business.product.factory.BatchFactory;
import com.xrj.business.product.service.DevDeviceInfoService;
import com.xrj.business.product.service.DeviceControlService;
import com.xrj.business.product.service.FactoryService;
import com.xrj.business.product.view.DevDeviceControlView;
import com.xrj.business.product.view.DevDeviceInfoView;
import com.xrj.business.product.view.DevDeviceView;
import com.xrj.business.product.vo.WidgetVo;
import com.xrj.framework.util.HttpUtil;

/**
 * 
 * @author chenjunchao
 * 
 *
 */
@Controller
@RequestMapping("/product")
public class DevDeviceControlController {

	@Resource
	RemoteService remoteService;
	@Resource
	private DevDeviceInfoService devDeviceInfoService;
	@Resource
	private DeviceControlService deviceControlService;

	/**
	 * @查找产品的设备,功能点信息
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("devDeviceControl/control")
	public Object search(HttpServletRequest request) throws Exception {
		DevProduct devProduct = (DevProduct) HttpUtil.getSessionAttr(request, "product");
		String sn = request.getParameter("deviceSn");

		DeviceRunningState deviceRunningState = deviceControlService.findRunningStatusBySn(sn);
		List<DevProductFunction> functionPoint = deviceControlService.findPointListBySn(sn);
		DevDevice devDevice = deviceControlService.findDeviceBySn(sn);

		List<List<Widget<?>>> totalList = new ArrayList<List<Widget<?>>>();
		List<List<WidgetVo>> widgets = FactoryService.pointControl(functionPoint);
		for (int i = 0; i < widgets.size(); i++) {
			List<Widget<?>> list = BatchFactory.create(widgets.get(i));
			totalList.add(list);
		}
		return new ReplaceCommand(DevDeviceControlView
				.deviceControl(devProduct, devDevice, totalList, deviceRunningState).findNodeById("deviceInfoRoot"));
	}

	/**
	 * @模拟设备主界面
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("simulateDevice")
	public Object simulateControlView(HttpServletRequest request) throws Exception {
		FilterParam fp = PubService.getParam(request);
		List<Object> list = deviceControlService.queryPageList(request);
		List<DevDevice> deviceList = (List<DevDevice>) list.get(0);
		Page dfishPage = (Page) list.get(1);
		View view = DevDeviceControlView.simulateDevice(request, deviceList, dfishPage, fp);
		return view;
	}

	/**
	 * @生成模拟设备
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("devDeviceControl/createSimulate")
	public Object createSimulate(HttpServletRequest request) throws Exception {
		DevProduct devProduct = (DevProduct) HttpUtil.getSessionAttr(request, "product");
		CallResult callResult = deviceControlService.createSimnulateDevice(devProduct);
		CommandGroup commandGroup = new CommandGroup();
		if (callResult.isSuccess()) {
			commandGroup.add(new JSCommand("VM(this).parent.find('mainContent').getFocus().reload();"));
			commandGroup.add(new TipCommand("生成成功").setTimeout(3));
			return commandGroup;
		}
		return PubView.getInfoAlertNoTimeout(callResult.getMessage());
	}

	/**
	 * @模拟设备控制界面
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("devDeviceControl/simulateControl")
	public Object simulateControl(HttpServletRequest request) throws Exception {
		String sn = request.getParameter("sn");
		DevProduct devProduct = (DevProduct) HttpUtil.getSessionAttr(request, "product");
		DevDevice devDevice = new DevDevice();
		devDevice.setDeviceSn(sn);
		DeviceRunningState deviceRunningState = deviceControlService.findRunningStatusBySn(sn);
		List<DevProductFunction> functionPoint = deviceControlService.findPointListBySn(sn);

		List<List<Widget<?>>> totalList = new ArrayList<List<Widget<?>>>();
		List<List<WidgetVo>> widgets = FactoryService.pointControl(functionPoint);
		for (int i = 0; i < widgets.size(); i++) {
			List<Widget<?>> list = BatchFactory.create(widgets.get(i));
			totalList.add(list);
		}
		return new ReplaceCommand(DevDeviceControlView
				.deviceDataControl(devProduct, devDevice, totalList, deviceRunningState, functionPoint)
				.findNodeById("simuDeviceInfoRoot"));
	}

	/**
	 * @模拟设备换页
	 * @param request
	 * @return 控件替换命令
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/devSimuDeviceInfo/search")
	public Object deviceSearch(HttpServletRequest request) throws Exception {
		CommandGroup cg = new CommandGroup();
		FilterParam fp = PubService.getParam(request);
		List<Object> list = deviceControlService.queryPageList(request);
		List<DevDevice> deviceList = (List<DevDevice>) list.get(0);
		Page dfishPage = (Page) list.get(1);
		View view = DevDeviceControlView.simulateDevice(request, deviceList, dfishPage, fp);
		cg.add(new ReplaceCommand(view.findNodeById("simuPanel")));
		cg.add(new ReplaceCommand(view.findNodeById("simuPage")));
		return cg;
	}

	/**
	 * @虚拟设备上下线控制
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("devDeviceControl/deviceOnline")
	public Object deviceOnline(HttpServletRequest request) throws Exception {
		CommandGroup commandGroup = new CommandGroup();
		String sn = request.getParameter("sn");
		String status = request.getParameter("status");
		if ("true".equals(status)) {
			deviceControlService.online(sn, true);
			commandGroup.add(
					new JSCommand("VM(this).find('Status1').check(true);VM(this).find('Status1').readonly(true);"));
			commandGroup.add(new TipCommand("设备已上线！"));
		} else {
			deviceControlService.online(sn, false);
			commandGroup.add(
					new JSCommand("VM(this).find('Status1').check(false);VM(this).find('Status1').readonly(true);"));
			commandGroup.add(new TipCommand("设备已下线！"));
		}
		return commandGroup;
	}

	/**
	 * @设备模拟控制刷新数据
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("devDeviceControl/fresh")
	public Object fresh(HttpServletRequest request) throws Exception {
		String sn = request.getParameter("sn");
		List<DevProductFunction> functionPoint = deviceControlService.findPointListBySn(sn);
		List<DevProductFunction> point = DeviceControlService.changeList(functionPoint);
		return new ReplaceCommand(DevDeviceControlView.reFresh(point).findNodeById("gridpan"));
	}

	/**
	 * @用于显示的公式
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("devDeviceControl/formula")
	public Object formula(HttpServletRequest request) throws Exception {
		String id = request.getParameter("id");
		String value = request.getParameter("value");
		DevProductFunction devProductFunction = deviceControlService.findPointById(id);
		String formul = null;
		if (value != "" && value != null && value.matches("-?[0-9]+\\.?[0-9]*")) {
			formul = DevDeviceControlView.formula(devProductFunction.getMinValue(), Double.valueOf(value),
					devProductFunction.getResolution());
		}
		return new ReplaceCommand(
				DevDeviceControlView.formula(formul, id + "end").findNodeById(devProductFunction.getId() + "end"));
	}

	/**
	 * @功能点数据推送
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("devDeviceControl/push")
	public Object data(HttpServletRequest request) throws Exception {
		CommandGroup commandGroup = new CommandGroup();
		String sn = request.getParameter("deviceSn");
		String onlineStatus = request.getParameter("online");
		List<CallResult> callresult = deviceControlService.pushData(sn, onlineStatus, request);
		for (int i = 0; i < callresult.size(); i++) {
			if ("nothingPush".equals(callresult.get(i).getMessage()) && callresult.get(i).isSuccess() == false) {
				commandGroup.add(PubView.getInfoAlertNoTimeout("没有可推送数据"));
				break;
			}
			if ("onlinePush".equals(callresult.get(i).getMessage()) && callresult.get(i).isSuccess() == true) {
				commandGroup.add(new TipCommand("推送成功"));
				break;
			}
			if ("notOnlinePush".equals(callresult.get(i).getMessage()) && callresult.get(i).isSuccess() == true) {
				commandGroup.add(new TipCommand("设备离线,延迟推送"));
				break;
			}
			if (callresult.get(i).isSuccess() == false) {
				if (callresult.get(i).getMessage() == null) {
					callresult.get(i).setMessage("推送失败,设备返回信息为空");
				}
				commandGroup.add(PubView.getInfoAlertNoTimeout(callresult.get(i).getMessage()));
				break;
			}
		}
		return commandGroup;
	}

	/**
	 * @改变checkbox状态的函数
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("changeopen")
	public Object changeopen(HttpServletRequest request) throws Exception {
		String opentype = request.getParameter("openbox");
		String labid = request.getParameter("labid");
		return new ReplaceCommand(DevDeviceControlView.open(opentype, labid).findNodeById(labid));

	}

	/**
	 * @回到模拟设备主界面
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("devDeviceControl/back")
	public Object back(HttpServletRequest request) throws Exception {
		FilterParam fp = PubService.getParam(request);
		List<Object> list = deviceControlService.queryPageList(request);
		List<DevDevice> deviceList = (List<DevDevice>) list.get(0);
		Page dfishPage = (Page) list.get(1);
		return new ReplaceCommand(DevDeviceControlView.simulateDevice(request, deviceList, dfishPage, fp)
				.findNodeById("simuDeviceInfoRoot"));
	}
	
	/**
	 * @回到设备信息主界面
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("devDeviceControl/backtoInfo")
	public Object backtoInfo(HttpServletRequest request) throws Exception {
		FilterParam fp = PubService.getParam(request);
		DevProduct product = (DevProduct) HttpUtil.getSessionAttr(request, "product");
		List<Object> list = devDeviceInfoService.queryPageList(request);
		List<DevDevice> deviceList = (List<DevDevice>) list.get(0);
		Page dfishPage = (Page) list.get(1);
		boolean exist = deviceControlService.hasSimnulateDevice(product.getId());
		return new ReplaceCommand(DevDeviceInfoView.buildDevInfoView(request, deviceList, dfishPage, fp , exist).findNodeById("deviceInfoRoot"));
	}
}
