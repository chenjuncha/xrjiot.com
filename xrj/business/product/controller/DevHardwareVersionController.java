package com.xrj.business.product.controller;

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
import com.rongji.dfish.ui.layout.View;
import com.xrj.api.dto.DevDeviceRom;
import com.xrj.api.dto.DevHardwareVersion;
import com.xrj.api.dto.DevProduct;
import com.xrj.business.core.view.PubView;
import com.xrj.business.product.service.DevHardwareVersionIndexService;
import com.xrj.business.product.view.DevHardwareVersionView;
import com.xrj.framework.util.HttpUtil;
import com.xrj.framework.util.StringUtils;

/**
 * 硬件版本功能控制器
 * 
 * @author chenyun
 * @date 2017-07-25
 */
@Controller
@RequestMapping("/product")
public class DevHardwareVersionController {

	@Resource
	private DevHardwareVersionIndexService devHardwareVersionIndexService;

	/**
	 * 硬件版本管理主页面
	 * 
	 * @param request
	 * @return 视图
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/devhardwareversion/index")
	public Object index(HttpServletRequest request) throws Exception {
		List<Object> list = devHardwareVersionIndexService.queryPageList(request);
		List<DevHardwareVersion> versionList = (List<DevHardwareVersion>) list.get(0);
		Page dfishPage = (Page) list.get(1);
		FilterParam fp = (FilterParam) list.get(2);
		View view = DevHardwareVersionView.buildIndexView(versionList, dfishPage, fp);
		return view;
	}

	/**
	 * 硬件版本管理分页搜索
	 * 
	 * @param request
	 * @return 控件替换命令
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/devhardwareversion/search")
	public Object search(HttpServletRequest request) throws Exception {
		List<Object> list = devHardwareVersionIndexService.queryPageList(request);
		List<DevHardwareVersion> versionList = (List<DevHardwareVersion>) list.get(0);
		Page dfishPage = (Page) list.get(1);
		FilterParam fp = (FilterParam) list.get(2);
		View view = DevHardwareVersionView.buildIndexView(versionList, dfishPage, fp);

		CommandGroup cg = new CommandGroup();
		cg.add(new ReplaceCommand(view.findNodeById(PubView.MAIN_CONTENT)));
		cg.add(new ReplaceCommand(view.findNodeById(PubView.MAIN_PAGE)));
		return cg;
	}

	/**
	 * 新建新版本界面
	 * 
	 * @param request
	 * @return 弹出框
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/devhardwareversion/add")
	public Object add(HttpServletRequest request) throws Exception {
		DevProduct product = (DevProduct) HttpUtil.getSessionAttr(request, "product");
		DevHardwareVersion devHardwareVersioin = new DevHardwareVersion();
		devHardwareVersioin.setProductId(product.getId());
		View view = DevHardwareVersionView.buildEditView(devHardwareVersioin);
		DialogCommand dialog = PubView.getDialogCommand("edit", "form", "新建设备版本号", PubView.DIALOG_SIZE_SMALL, null)
				.setNode(view);
		return dialog;
	}

	/**
	 * 编辑硬件版本界面
	 * 
	 * @param request
	 * @return 弹出框
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/devhardwareversion/edit")
	public Object edit(HttpServletRequest request) throws Exception {
		String id = request.getParameter("id");
		DevHardwareVersion devHardwareVersioin = devHardwareVersionIndexService.get(id);
		View view = DevHardwareVersionView.buildEditView(devHardwareVersioin);
		DialogCommand dialog = PubView.getDialogCommand("edit", "form", "设备版本号编辑", PubView.DIALOG_SIZE_SMALL, null)
				.setNode(view);

		return dialog;
	}

	/**
	 * 新建/编辑硬件版本->保存
	 * 
	 * @param request
	 * @param devHardwareVersion
	 * @return 消息提示框和JS命令
	 * @throws Exception
	 */
	@RequestMapping("/devhardwareversion/save")
	@ResponseBody
	public Object save(HttpServletRequest request, DevHardwareVersion devHardwareVersion) throws Exception {
		CommandGroup cg = new CommandGroup();
		// 硬件版本号校验
		String hardWareVersion = request.getParameter("hardwareVersion");
		Boolean right = devHardwareVersionIndexService.verificationHardwareVersion(hardWareVersion,
				"^[0-9]{1,2}\\.[0-9]{1,2}$");
		if (right == false) {
			return cg.add(PubView.getInfoAlert("设备版本号格式为x.y,xy范围为0-99"));
		}
		// 硬件版本号、版本名称排重
		if (devHardwareVersionIndexService.checkUnqie(devHardwareVersion)) {
			// 不存在该硬件版本则保存，反之则更新
			if (StringUtils.isEmpty(devHardwareVersion.getId())) {
				devHardwareVersionIndexService.save(devHardwareVersion);
			} else {
				devHardwareVersionIndexService.update(devHardwareVersion);
			}
			cg.add(PubView.getInfoAlertNoTimeout("保存成功"));
		} else {
			cg.add(PubView.getInfoAlertNoTimeout("已存在该设备版本名称及版本号"));
		}
		cg.add(new JSCommand("var d=$.dialog(this);d.ownerView.cmd('search');$.close(this);"));
		return cg;
	}

	/**
	 * 删除该版本
	 * 
	 * @param request
	 * @return JS命令和消息提示框
	 * @throws Exception
	 */
	@RequestMapping("/devhardwareversion/delete")
	@ResponseBody
	public Object delete(HttpServletRequest request) throws Exception {
		String id = request.getParameter("id");
		CommandGroup cg = new CommandGroup();

		// 根据硬件版本Id查询是否有关联设备rom，未关联则删除成功，反之失败
		List<DevDeviceRom> devDeviceRomlist = devHardwareVersionIndexService.getDeviceRomById(id);
		if (devDeviceRomlist.size() == 0 || devDeviceRomlist == null) {
			devHardwareVersionIndexService.delete(id);
			cg.add(new JSCommand("VM(this).cmd('search');"));
			cg.add(PubView.getInfoAlertNoTimeout("删除成功"));
		} else {
			cg.add(new JSCommand("VM(this).cmd('search');"));
			cg.add(PubView.getInfoAlertNoTimeout("已有ROM使用了该设备版本，删除失败"));
		}
		return cg;
	}

	/**
	 * 固件新建新版本界面
	 * 
	 * @param request
	 * @return 弹出框
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/devhardwareversion/addHard")
	public Object addHard(HttpServletRequest request) throws Exception {
		DevProduct product = (DevProduct) HttpUtil.getSessionAttr(request, "product");
		DevHardwareVersion devHardwareVersioin = new DevHardwareVersion();
		devHardwareVersioin.setProductId(product.getId());
		View view = DevHardwareVersionView.buildHardEditView(devHardwareVersioin);
		DialogCommand dialog = PubView.getDialogCommand("edit", "form", "新建设备版本", PubView.DIALOG_SIZE_SMALL, null)
				.setNode(view);
		return dialog;
	}

	/**
	 * 固件新增硬件版本->保存
	 * 
	 * @param request
	 * @param devHardwareVersion
	 * @return 消息提示框和JS命令
	 * @throws Exception
	 */
	@RequestMapping("/devhardwareversion/saveHard")
	@ResponseBody
	public Object saveHard(HttpServletRequest request, DevHardwareVersion devHardwareVersion) throws Exception {
		CommandGroup cg = new CommandGroup();
		// 硬件版本号校验
		String hardWareVersion = request.getParameter("hardwareVersion");
		Boolean right = devHardwareVersionIndexService.verificationHardwareVersion(hardWareVersion,
				"^[0-9]{1,2}\\.[0-9]{1,2}$");
		if (right == false) {
			return cg.add(PubView.getInfoAlert("设备版本号格式为x.y,xy范围为0-99"));
		}
		// 硬件版本号、版本名称排重
		if (devHardwareVersionIndexService.checkUnqie(devHardwareVersion)) {
			// 不存在该硬件版本则保存，反之则更新
			if (StringUtils.isEmpty(devHardwareVersion.getId())) {
				devHardwareVersionIndexService.save(devHardwareVersion);
			} else {
				devHardwareVersionIndexService.update(devHardwareVersion);
			}
			cg.add(PubView.getInfoAlertNoTimeout("保存成功"));
		} else {
			cg.add(PubView.getInfoAlertNoTimeout("已存在该设备版本名称及版本号"));
		}
		cg.add(new JSCommand("var d=$.dialog(this);d.ownerView.cmd('replaceHard');$.close(this);"));
		return cg;
	}

}
