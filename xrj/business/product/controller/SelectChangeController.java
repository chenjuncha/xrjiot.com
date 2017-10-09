package com.xrj.business.product.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rongji.dfish.ui.command.CommandGroup;
import com.rongji.dfish.ui.command.ReplaceCommand;
import com.rongji.dfish.ui.helper.HorizontalGroup;
import com.xrj.api.dto.SysDataDict;
import com.xrj.business.product.view.AddNewProductView;

@Controller
public class SelectChangeController {

	/**
	 * 二级下拉菜单跳转命令
	 * 
	 * @param request
	 * @return 控件替换命令
	 */
	@RequestMapping("selectChange")
	@ResponseBody
	public Object selectChangeClass2(HttpServletRequest request) {
		String code = request.getParameter("selectValue");
		Map<String, List<SysDataDict>> mapClass = AddNewProductView.buildProductClassListMap(code);
		CommandGroup cGroup = new CommandGroup();
		cGroup.add(new ReplaceCommand(AddNewProductView.buildSelect2(mapClass.get("list2"))));
		if (mapClass.get("list3").isEmpty()) {
			cGroup.add(new ReplaceCommand(new HorizontalGroup("class3Layout")));
		} else {
			cGroup.add(new ReplaceCommand(AddNewProductView.buildSelect3(mapClass.get("list3"))));
		}
		return cGroup;
	}

	/**
	 * 三级下拉菜单跳转命令
	 * 
	 * @param request
	 * @return 控件替换命令
	 */
	@RequestMapping("selectChange2")
	@ResponseBody
	public Object selectChangeClass3(HttpServletRequest request) {
		String code = request.getParameter("selectValue");
		Map<String, List<SysDataDict>> mapClass = AddNewProductView.buildProductClassListMap(code);
		if (mapClass.get("list3").isEmpty()) {
			return new ReplaceCommand(new HorizontalGroup("class3Layout"));
		}
		return new ReplaceCommand(AddNewProductView.buildSelect3(mapClass.get("list3")));
	}
}
