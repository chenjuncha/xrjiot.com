package com.xrj.business.product.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rongji.dfish.ui.command.CommandGroup;
import com.rongji.dfish.ui.command.ReplaceCommand;
import com.rongji.dfish.ui.command.TipCommand;
import com.rongji.dfish.ui.layout.View;
import com.xrj.business.core.base.RemoteService;
import com.xrj.business.core.view.PubView;
import com.xrj.business.product.service.DeviceService;
import com.xrj.business.product.view.DevDeviceView;

/**
 * 
 * @author chenjunchao
 *
 */
@Controller
@RequestMapping("/product")
public class DevDeviceController {

	@Resource
	RemoteService remoteService;
	@Autowired
	private DeviceService devDeviceService;

	/**
	 * @设备换页的函数
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("device/search")
	public Object search(HttpServletRequest request) throws Exception {
		CommandGroup cg = new CommandGroup();
		List<Object> Data = devDeviceService.buildMainData(request);
		View view = DevDeviceView.showAllDevice(request, Data);
		cg.add(new ReplaceCommand(view.findNodeById("bar")));
		cg.add(new ReplaceCommand(view.findNodeById("grid")));
		return cg;
	}

	/**
	 * @设备信息导出Excel表
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("device/export")
	public Object export(HttpServletRequest request, HttpServletResponse response) throws Exception {
		CommandGroup cg = new CommandGroup();
		if (!devDeviceService.exportDevice(request, response)) {
			cg.add(PubView.getInfoAlertNoTimeout("导出失败"));
		}
		return cg;
	}

	/**
	 * @根据产品显示所有设备
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("device/showAllDevice")
	@ResponseBody
	public Object showAll(HttpServletRequest request) throws Exception {
		View view = new View();
		List<Object> Data = devDeviceService.buildMainData(request);
		view = DevDeviceView.showAllDevice(request, Data);
		return view;
	}

	/**
	 * @根据设备id删除设备
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("device/Delete")
	@ResponseBody
	public Object delete(HttpServletRequest request) throws Exception {
		String id = request.getParameter("deviceId");
		CommandGroup cg = new CommandGroup();
		if (devDeviceService.deleteDeviceById(id, request)) {
			cg.add(new TipCommand("删除成功"));
		} else {
			cg.add(PubView.getInfoAlertNoTimeout("选中设备存在子设备,不可删除"));
		}
		List<Object> Data = devDeviceService.buildMainData(request);
		return cg.add(new ReplaceCommand(DevDeviceView.showAllDevice(request, Data).findNodeById("ver")));
	}

	/**
	 * @生成多个设备
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("device/createSome")
	@ResponseBody
	public Object createSome(HttpServletRequest request) throws Exception {
		CommandGroup cg = new CommandGroup();
		String number = request.getParameter("number");
		if (devDeviceService.createDevice(number, request)) {
			cg.add(new TipCommand("生成成功"));
		} else {
			cg.add(PubView.getInfoAlertNoTimeout("后台发现异常"));
		}
		List<Object> Data = devDeviceService.buildMainData(request);
		return cg.add(new ReplaceCommand(DevDeviceView.showAllDevice(request, Data).findNodeById("ver")));
	}

	/**
	 * 生成一个设备
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("device/createOne")
	@ResponseBody
	public Object createOne(HttpServletRequest request) throws Exception {
		CommandGroup cg = new CommandGroup();
		String number = "1";
		if (devDeviceService.createDevice(number, request)) {
			cg.add(new TipCommand("生成成功"));
		} else {
			cg.add(PubView.getInfoAlertNoTimeout("后台发现异常"));
		}
		List<Object> Data = devDeviceService.buildMainData(request);
		return cg.add(new ReplaceCommand(DevDeviceView.showAllDevice(request, Data).findNodeById("ver")));
	}
}
