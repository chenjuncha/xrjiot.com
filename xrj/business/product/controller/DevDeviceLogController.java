package com.xrj.business.product.controller;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rongji.dfish.base.Page;
import com.rongji.dfish.framework.FilterParam;
import com.rongji.dfish.ui.command.CommandGroup;
import com.rongji.dfish.ui.command.ReplaceCommand;
import com.rongji.dfish.ui.command.TipCommand;
import com.rongji.dfish.ui.helper.GridPanel;
import com.rongji.dfish.ui.layout.HorizontalLayout;
import com.rongji.dfish.ui.layout.View;
import com.xrj.api.dto.DevDevice;
import com.xrj.api.dto.DevDeviceLog;
import com.xrj.api.dto.DevProduct;
import com.xrj.api.model.DeviceRunningState;
import com.xrj.business.core.base.PubService;
import com.xrj.business.product.service.DevDeviceInfoService;
import com.xrj.business.product.service.DevDeviceLogIndexService;
import com.xrj.business.product.service.DeviceControlService;
import com.xrj.business.product.view.DevDeviceInfoView;
import com.xrj.business.product.view.DevDeviceLogView;
import com.xrj.framework.util.DateUtil;
import com.xrj.framework.util.HttpUtil;

/**
 * 通信日志功能控制器
 * 
 * @author chenliqian
 * @date 2017-07-31
 */
@Controller
@RequestMapping("/product")
public class DevDeviceLogController {
	@Resource
	private DevDeviceLogIndexService devDeviceLogIndexService;
	@Resource
	private DevDeviceInfoService devDeviceInfoService;
	@Resource
	private DeviceControlService deviceControlService;

	/**
	 * 设备日志主页面
	 * 
	 * @param request
	 * @return 控件替换命令
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("devDeviceLogIndex")
	public Object index(HttpServletRequest request) throws Exception {
		// 根据设备SN获取设备信息和设备状态
		DevDevice devDevice = devDeviceLogIndexService.getByDeviceSn(request);
		DeviceRunningState deviceRunningState = devDeviceLogIndexService.getOnlineStatus(devDevice);
		// 第一个标签页（通信日志）信息
		List<Object> list = devDeviceLogIndexService.queryCommunicationLogPageList(request, null, null);
		List<DevDeviceLog> logList = (List<DevDeviceLog>) list.get(0);
		Page dfishPage = (Page) list.get(1);
		FilterParam fp = (FilterParam) list.get(2);
		// 第二个标签页（上下线记录）信息
		List<Object> list2 = devDeviceLogIndexService.queryOnLineOffLinePageList(request, null, null);
		List<DevDeviceLog> onLineOfflineList = (List<DevDeviceLog>) list2.get(0);
		Page secondDfishPage = (Page) list2.get(1);
		FilterParam secondFp = (FilterParam) list2.get(2);
		// 主页面
		View view = DevDeviceLogView.buildDevDeviceLogIndex(logList, dfishPage, fp, onLineOfflineList, secondDfishPage,
				secondFp, devDevice, deviceRunningState);
		return new ReplaceCommand(view.findNodeById(DevDeviceInfoView.DEVICE_INFO_ROOT));
	}

	/**
	 * 标签页（通信日志）的分页搜索
	 * 
	 * @param request
	 * @return 控件替换命令
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("devDeviceLog/fistPageSearch")
	public Object fistPageSearch(HttpServletRequest request) throws Exception {
		// 根据设备SN获取设备信息
		DevDevice devDevice = devDeviceLogIndexService.getByDeviceSn(request);
		// 第一个标签页（通信日志）信息
		List<Object> list = devDeviceLogIndexService.queryCommunicationLogPageList(request, null, null);
		List<DevDeviceLog> logList = (List<DevDeviceLog>) list.get(0);
		Page dfishPage = (Page) list.get(1);
		FilterParam fp = (FilterParam) list.get(2);
		// 分页搜索后列表区、分页区变换
		GridPanel gridPanel = DevDeviceLogView.buildCommunicationLogGridPanel(logList);
		HorizontalLayout horizontalLayout = DevDeviceLogView.buildCommunicationLogPageLayout(dfishPage, fp, devDevice);
		CommandGroup cg = new CommandGroup();
		cg.add(new ReplaceCommand(gridPanel));
		cg.add(new ReplaceCommand(horizontalLayout));
		return cg;
	}

	/**
	 * 标签页（上下线记录）的分页搜索
	 * 
	 * @param request
	 * @return 控件替换命令
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("devDeviceLog/secondPageSearch")
	public Object secondPageSearch(HttpServletRequest request) throws Exception {
		// 根据设备SN获取设备信息
		DevDevice devDevice = devDeviceLogIndexService.getByDeviceSn(request);
		// 第二个标签页（上下线记录）信息
		List<Object> list2 = devDeviceLogIndexService.queryOnLineOffLinePageList(request, null, null);
		List<DevDeviceLog> onLineOfflineList = (List<DevDeviceLog>) list2.get(0);
		Page secondDfishPage = (Page) list2.get(1);
		FilterParam secondFp = (FilterParam) list2.get(2);
		// 分页搜索后列表区、分页区变换
		GridPanel gridPanel = DevDeviceLogView.buildOnLineRecordGridPanel(onLineOfflineList);
		HorizontalLayout horizontalLayout = DevDeviceLogView.buildOnLineRecordPageLayout(secondDfishPage, secondFp,
				devDevice);
		CommandGroup cg = new CommandGroup();
		cg.add(new ReplaceCommand(gridPanel));
		cg.add(new ReplaceCommand(horizontalLayout));
		return cg;
	}

	/**
	 * 返回命令 设备日志->返回->设备信息
	 * 
	 * @param request
	 * @return 控件替换命令
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("devDeviceLog/back")
	public Object back(HttpServletRequest request) throws Exception {
		FilterParam fp = PubService.getParam(request);
		List<Object> list = devDeviceInfoService.queryPageList(request);
		List<DevDevice> deviceList = (List<DevDevice>) list.get(0);
		DevProduct product = (DevProduct) HttpUtil.getSessionAttr(request, "product");
		boolean exist = deviceControlService.hasSimnulateDevice(product.getId());
		Page dfishPage = (Page) list.get(1);
		return new ReplaceCommand(
				
				DevDeviceInfoView.buildDevInfoView(request, deviceList, dfishPage, fp ,exist).findNodeById("deviceInfoRoot"));
	}

	/**
	 * 标签页（通信日志）按时间段搜索功能
	 * 
	 * @param request
	 * @return 控件替换命令
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("devDeviceLog/timeSearching")
	public Object timeSearch(HttpServletRequest request) throws Exception {

		String from = request.getParameter("firstFrom");
		String to = request.getParameter("firstTo");
		Date startDate = DateUtil.toDate(from, "yyyy-MM-dd HH:mm:ss");
		Date endDate = DateUtil.toDate(to, "yyyy-MM-dd HH:mm:ss");
		// 按时间段搜索后，第一个标签页（通信日志）信息
		DevDevice devDevice = devDeviceLogIndexService.getByDeviceSn(request);
		List<Object> list = devDeviceLogIndexService.queryCommunicationLogPageList(request, startDate, endDate);
		List<DevDeviceLog> logList = (List<DevDeviceLog>) list.get(0);
		Page dfishPage = (Page) list.get(1);
		FilterParam fp = (FilterParam) list.get(2);
		// 按时间段搜索后，提示成功，列表区、分页区变换
		CommandGroup cg = new CommandGroup();
		TipCommand command = new TipCommand("查询成功");
		cg.add(command);
		GridPanel gridPanel = DevDeviceLogView.buildCommunicationLogGridPanel(logList);
		HorizontalLayout horizontalLayout = DevDeviceLogView.buildSearChPageLayout(dfishPage, fp, from, to,
				devDevice.getDeviceSn());
		cg.add(new ReplaceCommand(gridPanel));
		cg.add(new ReplaceCommand(horizontalLayout));
		return cg;
	}

	/**
	 * 标签页（上下线记录）按时间段搜索功能
	 * 
	 * @param request
	 * @return 控件替换命令
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("devDeviceLog/secondTimeSearching")
	public Object secondTimeSearching(HttpServletRequest request) throws Exception {

		String from = request.getParameter("secondFrom");
		String to = request.getParameter("secondTo");
		Date startDate = DateUtil.toDate(from, "yyyy-MM-dd HH:mm:ss");
		Date endDate = DateUtil.toDate(to, "yyyy-MM-dd HH:mm:ss");
		// 按时间段搜索后，第二个标签页（上下线记录）信息
		DevDevice devDevice = devDeviceLogIndexService.getByDeviceSn(request);
		List<Object> list2 = devDeviceLogIndexService.queryOnLineOffLinePageList(request, startDate, endDate);
		List<DevDeviceLog> onLineOfflineList = (List<DevDeviceLog>) list2.get(0);
		Page secondDfishPage = (Page) list2.get(1);
		FilterParam secondFp = (FilterParam) list2.get(2);
		// 按时间段搜索后，提示成功，列表区、分页区变换
		CommandGroup cg = new CommandGroup();
		TipCommand command = new TipCommand("查询成功");
		cg.add(command);
		GridPanel gridPanel = DevDeviceLogView.buildOnLineRecordGridPanel(onLineOfflineList);
		HorizontalLayout horizontalLayout = DevDeviceLogView.buildSearChPageLayout2(secondDfishPage, secondFp, from, to,
				devDevice.getDeviceSn());
		cg.add(new ReplaceCommand(gridPanel));
		cg.add(new ReplaceCommand(horizontalLayout));
		return cg;
	}

	/**
	 * 标签页（通信日志）按时间段搜索后的分页搜索
	 * 
	 * @param request
	 * @return 控件替换命令
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("devDeviceLog/fistPageSearchSearch")
	public Object firstPageSearch(HttpServletRequest request) {
		String from = request.getParameter("firstFrom");
		String to = request.getParameter("firstTo");
		Date startDate = DateUtil.toDate(from, "yyyy-MM-dd HH:mm:ss");
		Date endDate = DateUtil.toDate(to, "yyyy-MM-dd HH:mm:ss");
		// 按时间段搜索后，第一个标签页（通信日志）信息
		DevDevice devDevice = devDeviceLogIndexService.getByDeviceSn(request);
		List<Object> list = devDeviceLogIndexService.queryCommunicationLogPageList(request, startDate, endDate);
		List<DevDeviceLog> logList = (List<DevDeviceLog>) list.get(0);
		Page dfishPage = (Page) list.get(1);
		FilterParam fp = (FilterParam) list.get(2);
		// 分页搜索后列表区、分页区变换
		CommandGroup cg = new CommandGroup();
		GridPanel gridPanel = DevDeviceLogView.buildCommunicationLogGridPanel(logList);
		HorizontalLayout horizontalLayout = DevDeviceLogView.buildSearChPageLayout(dfishPage, fp, from, to,
				devDevice.getDeviceSn());
		cg.add(new ReplaceCommand(gridPanel));
		cg.add(new ReplaceCommand(horizontalLayout));

		return cg;
	}

	/**
	 * 标签页（上下线记录）按时间段搜索后的分页搜索
	 * 
	 * @param request
	 * @return 控件替换命令
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("devDeviceLog/secondPageSearchSearch")
	public Object secondPageSearchSearch(HttpServletRequest request) throws Exception {

		String from = request.getParameter("secondFrom");
		String to = request.getParameter("secondTo");
		Date startDate = DateUtil.toDate(from, "yyyy-MM-dd HH:mm:ss");
		Date endDate = DateUtil.toDate(to, "yyyy-MM-dd HH:mm:ss");
		// 按时间段搜索后，第二个标签页（上下线记录）信息
		DevDevice devDevice = devDeviceLogIndexService.getByDeviceSn(request);
		List<Object> list2 = devDeviceLogIndexService.queryOnLineOffLinePageList(request, startDate, endDate);
		List<DevDeviceLog> onLineOfflineList = (List<DevDeviceLog>) list2.get(0);
		Page secondDfishPage = (Page) list2.get(1);
		FilterParam secondFp = (FilterParam) list2.get(2);
		// 分页搜索后列表区、分页区变换
		CommandGroup cg = new CommandGroup();
		GridPanel gridPanel = DevDeviceLogView.buildOnLineRecordGridPanel(onLineOfflineList);
		HorizontalLayout horizontalLayout = DevDeviceLogView.buildSearChPageLayout2(secondDfishPage, secondFp, from, to,
				devDevice.getDeviceSn());
		cg.add(new ReplaceCommand(gridPanel));
		cg.add(new ReplaceCommand(horizontalLayout));
		return cg;
	}
}