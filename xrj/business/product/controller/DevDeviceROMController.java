package com.xrj.business.product.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.rongji.dfish.framework.plugin.file.service.FileService;
import com.rongji.dfish.ui.command.CommandGroup;
import com.rongji.dfish.ui.command.ConfirmCommand;
import com.rongji.dfish.ui.command.ReplaceCommand;
import com.rongji.dfish.ui.command.TipCommand;
import com.rongji.dfish.ui.layout.View;
import com.xrj.api.dto.DevDeviceRom;
import com.xrj.api.dto.DevHardwareVersion;
import com.xrj.api.dto.DevProduct;
import com.xrj.api.support.CallResult;
import com.xrj.business.core.base.RemoteService;
import com.xrj.business.core.view.PubView;
import com.xrj.business.product.service.DeviceROMService;
import com.xrj.business.product.view.DevDeviceROMView;
import com.xrj.framework.util.HttpUtil;
import java.util.zip.*;;
/**
 * 
 * @author chenjunchao
 *
 */
@Controller
@RequestMapping("/product")
public class DevDeviceROMController {
	@Resource
	RemoteService remoteService;
	@Autowired
	private FileService fileService;
	@Autowired
	private DeviceROMService devDeviceROMService;
	
	/**
	 * @显示所有ROM信息
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/showROM")
	@ResponseBody
	public Object showAll(HttpServletRequest request) throws Exception {
		View view = new View();
		List<Object> Data = devDeviceROMService.buildMainData(request);
		view = DevDeviceROMView.showROM(request, Data);
		return view;
	}

	/**
	 * @替换硬件设备grid
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/replaceHard")
	@ResponseBody
	public Object replaceHard(HttpServletRequest request) throws Exception {
		DevProduct devProduct = (DevProduct) HttpUtil.getSessionAttr(request, "product");
		List<DevHardwareVersion> versionList = devDeviceROMService.findDevHardwareVersionByProduct(devProduct.getId());
		return new ReplaceCommand(DevDeviceROMView.changeHard(versionList).findNodeById("gridpan"));
	}

	/**
	 * @返回ROM显示界面
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/backtoROM")
	@ResponseBody
	public Object back(HttpServletRequest request) throws Exception {
		List<Object> Data = devDeviceROMService.buildMainData(request);
		return new ReplaceCommand(DevDeviceROMView.showROM(request, Data).findNodeById("ver"));
	}

	/**
	 * @删除某个ROM
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/devDeleteROM")
	@ResponseBody
	public Object delete(HttpServletRequest request) throws Exception {
		String id = request.getParameter("id");
		CommandGroup commandGroup = new CommandGroup();
		if (devDeviceROMService.deleteROM(id)) {
			commandGroup.add(new TipCommand("删除成功"));
		} else {
			commandGroup.add(PubView.getInfoAlertNoTimeout("查询出错,请检查数据"));
		}
		List<Object> Data = devDeviceROMService.buildMainData(request);
		commandGroup.add(new ReplaceCommand(DevDeviceROMView.showROM(request, Data).findNodeById("ver")));
		return commandGroup;
	}

	/**
	 * @版本统计
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/versionStatistics")
	@ResponseBody
	public Object versionStatistics(HttpServletRequest request) throws Exception {	
		return new ReplaceCommand(DevDeviceROMView.versionStatistics().findNodeById("ver"));
	}
	
	/**
	 * @创建ROM界面
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/createROM")
	@ResponseBody
	public Object create(HttpServletRequest request) throws Exception {
		DevProduct devProduct = (DevProduct) HttpUtil.getSessionAttr(request, "product");
		CommandGroup commandGroup = new CommandGroup();
		List<DevHardwareVersion> devHardwareVersion = devDeviceROMService
				.findDevHardwareVersionByProduct(devProduct.getId());
		if (devHardwareVersion.size() != 0) {
			commandGroup.add(
					new ReplaceCommand(DevDeviceROMView.createROM(request, devHardwareVersion).findNodeById("ver")));
		} else {
			commandGroup.add(PubView.getInfoAlertNoTimeout("查询出错,请检查数据"));
		}
		return commandGroup;
	}

	/**
	 * @保存ROM信息
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/saveROM")
	@ResponseBody
	public Object save(HttpServletRequest request) throws Exception {
		CommandGroup commandGroup = new CommandGroup();
		String name = request.getParameter("versionName");
		String soft = request.getParameter("softVersion");
		String hard = request.getParameter("hardId");
		String url = request.getParameter("uploadFile");
		//String compress  = request.getParameter("deCompress");
		DevProduct devProduct = (DevProduct)HttpUtil.getSessionAttr(request, "product");
		CallResult<String> callResult = devDeviceROMService.saveROM(devProduct, name, soft, hard, url);
		if (callResult.isSuccess()) {
			devDeviceROMService.noticeDeviceUpdate(callResult.getData());
			List<Object> Data = devDeviceROMService.buildMainData(request);
			commandGroup.add(new ReplaceCommand(DevDeviceROMView.showROM(request, Data).findNodeById("ver")));
			commandGroup.add(new TipCommand("添加成功"));
		} else {
			commandGroup.add(PubView.getInfoAlertNoTimeout("添加失败，请检查输入数据"));
		}
		return commandGroup;
		
	}

	/**
	 * @ROM换页函数
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/ROMsearch")
	public Object search(HttpServletRequest request) throws Exception {
		CommandGroup cg = new CommandGroup();
		List<Object> Data = devDeviceROMService.buildMainData(request);
		View view = DevDeviceROMView.showROM(request, Data);
		cg.add(new ReplaceCommand(view.findNodeById("bar")));
		cg.add(new ReplaceCommand(view.findNodeById("gridPanel")));
		return cg;
	}

	/**
	 * @删除ROM的确认函数
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/toDeleteROM")
	@ResponseBody
	public Object deleteconfirmFunctionPoint(HttpServletRequest request) throws Exception {
		String id = request.getParameter("ROMId");
		CommandGroup commandGroup = new CommandGroup();
		commandGroup.add(new ConfirmCommand("确认删除?", PubView.getAjaxCommand("product/devDeleteROM?id=" + id + "")));
		return commandGroup;
	}

	/**
	 * @查看某个ROM信息
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/devShowROM")
	public Object shouROM(HttpServletRequest request) throws Exception {
		CommandGroup commandGroup = new CommandGroup();
		String id = request.getParameter("ROMId");
		DevDeviceRom rom = devDeviceROMService.findROM(id);
		List<DevHardwareVersion> devHardwareVersion = devDeviceROMService.findDevHardwareVersionByROM(id);
		if (rom != null || devHardwareVersion.size() != 0) {
			commandGroup.add(new ReplaceCommand(
					DevDeviceROMView.showOneROM(request, devHardwareVersion, rom).findNodeById("ver")));
		} else {
			commandGroup.add(PubView.getInfoAlertNoTimeout("查询出错,请检查数据"));
		}
		return commandGroup;
	}
	/**
	 * @查看某个ROM信息
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/updateRom")
	public Object updateRom(HttpServletRequest request) throws Exception {
		CommandGroup commandGroup = new CommandGroup();
		String id = request.getParameter("ROMId");
		devDeviceROMService.noticeDeviceUpdate(id);
		return commandGroup;
	}
}
