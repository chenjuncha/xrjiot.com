package com.xrj.business.product.view;

import java.util.ArrayList;
import java.util.List;


import com.rongji.dfish.base.Page;
import com.rongji.dfish.framework.FilterParam;
import com.rongji.dfish.ui.Valignable;
import com.rongji.dfish.ui.command.ConfirmCommand;
import com.rongji.dfish.ui.form.Text;
import com.rongji.dfish.ui.form.Validate;
import com.rongji.dfish.ui.helper.FlexGrid;
import com.rongji.dfish.ui.helper.GridPanel;
import com.rongji.dfish.ui.layout.ButtonBar;
import com.rongji.dfish.ui.layout.HorizontalLayout;
import com.rongji.dfish.ui.layout.VerticalLayout;
import com.rongji.dfish.ui.layout.View;
import com.rongji.dfish.ui.layout.grid.GridColumn;
import com.rongji.dfish.ui.layout.grid.Tr;
import com.rongji.dfish.ui.widget.Button;
import com.rongji.dfish.ui.widget.Html;
import com.rongji.dfish.ui.widget.PageBar;
import com.rongji.dfish.ui.widget.SubmitButton;
import com.xrj.api.dto.DevHardwareVersion;
import com.xrj.business.core.base.PubConstants;
import com.xrj.business.core.view.PubView;

public class DevHardwareVersionView {
	/**
	 * 设备版本管理主界面
	 * @param versionList
	 * @param dfishPage
	 * @param fp
	 * @return
	 */
	public static View buildIndexView(List<DevHardwareVersion> versionList,Page dfishPage,FilterParam fp) {

		View view = new View("m_root");
		View pubView = PubView.buildMainView(false);
		VerticalLayout root = new VerticalLayout("");
		view.add(root);
		// 页面顶部
		HorizontalLayout top = new HorizontalLayout("").setHeight(40);
		root.add(top);
		top.add(new Html("<h3>设备版本管理</h3>"));
		top.add(new Button("创建新版本").setCls("x-btn x-btn-main").setOn(Button.EVENT_CLICK, "VM(this).cmd('add')")
				.setWidth(100).setHeight(30));
		top.add(new Html("").setWidth(40));
		top.setValign(Valignable.VALIGN_BOTTOM);
		root.add(new Html(
				"<hr style='filter:progid:DXImageTransform.Microsoft.Shadow(color:#CCC,direction:145,strength:15)' color='#CCC' width='100%' size='1' />")
						.setHeight(1),
				"-1");

		// 列表区
		HorizontalLayout displayShell = (HorizontalLayout) pubView.findNodeById(PubView.MAIN_BODY);
		root.add(displayShell);
		GridPanel mainGrid = (GridPanel) displayShell.findNodeById(PubView.BODY_MAIN_CONTENT).setHeight("95%");

		// 查询结果集
		List<Object[]> gridData = new ArrayList<Object[]>();
		for (DevHardwareVersion version : versionList) {
			gridData.add(new Object[] { version.getId(), version.getVersionName(), version.getHardwareVersion(),
					version.getCreateTime(), version.getCreator() });
		}

		int columnIndex = 0;
		mainGrid.addColumn(GridColumn.hidden(columnIndex++, "id"));
		mainGrid.addColumn(GridColumn.text(columnIndex++, "versionName", "版本名称", "150"));
		mainGrid.addColumn(GridColumn.text(columnIndex++, "hardwareVersion", "设备版本号", "120"));
		mainGrid.addColumn(GridColumn.text(columnIndex++, "createTime", "创建时间", "120").setDataFormat("yyyy-MM-dd HH:mm:ss"));
		mainGrid.addColumn(GridColumn.text(0, "oper", "操作", "120").setAlign(GridColumn.ALIGN_CENTER)
				.setFormat("<i class='x-grid-edit' title='编辑' onclick=\"VM(this).cmd('edit','$id');\"></i> "
						+ "&nbsp;&nbsp;<i class='x-grid-del' title='删除' onclick=\"VM(this).cmd('delete','$id','$versionName');\"></i>"));
		mainGrid.setPub(new Tr().setCls("w-td-line w-th"));
		mainGrid.setGridData(gridData);
		// 分页区
		PageBar mainPage = (PageBar) displayShell.findNodeById(PubView.MAIN_PAGE);
		mainPage.setJump(true);
		mainPage.setCurrentpage(dfishPage.getCurrentPage());
		mainPage.setSumpage(dfishPage.getPageCount());
		mainPage.setSrc("product/devhardwareversion/search?cp=$0" + fp);

		// 绑定命令
		view.addCommand("search", PubView.getSubmitCommand("product/devhardwareversion/search?cp=$0"));
		view.addCommand("delete", new ConfirmCommand("确定要删除：$1？")
				.setYes(PubView.getAjaxCommand("product/devhardwareversion/delete?id=$0")));
		view.addCommand("edit", PubView.getAjaxCommand("product/devhardwareversion/edit?id=$0"));
		view.addCommand("add", PubView.getAjaxCommand("product/devhardwareversion/add"));
		return view;
	}

	/**
	 * 添加新版本或编辑版本 界面
	 * 
	 * @param request
	 * @return 视图
	 */
	public static View buildEditView(DevHardwareVersion devHardwareVersion) {

		View view = PubView.buildDialogView();
		FlexGrid baseForm = new FlexGrid("versionForm");
		fillBaseInfoForm(baseForm, devHardwareVersion);
		baseForm.setId(PubView.DIALOG_MAIN);
		view.replaceNodeById(baseForm);

		ButtonBar btn = (ButtonBar) view.findNodeById(PubView.DIALOG_BUTTON);
		btn.add(new SubmitButton("保存").setOn(Button.EVENT_CLICK, "this.cmd('save');").setCls("x-btn x-btn-main"));
		btn.add(new Button("取消").setOn(Button.EVENT_CLICK, PubConstants.CMD_DLG_CLOSE));

		view.addCommand("save", PubView.getSubmitCommand("product/devhardwareversion/save?closeDlg=$0"));

		return view;
	}
	/**
	 * 固件添加新版本或编辑版本 界面
	 * 
	 * @param request
	 * @return 视图
	 */
	public static View buildHardEditView(DevHardwareVersion devHardwareVersion) {

		View view = PubView.buildDialogView();
		FlexGrid baseForm = new FlexGrid("versionForm");
		fillBaseInfoForm(baseForm, devHardwareVersion);
		baseForm.setId(PubView.DIALOG_MAIN);
		view.replaceNodeById(baseForm);

		ButtonBar btn = (ButtonBar) view.findNodeById(PubView.DIALOG_BUTTON);
		btn.add(new SubmitButton("保存").setOn(Button.EVENT_CLICK,"this.cmd('save');" ).setCls("x-btn x-btn-main"));
		btn.add(new Button("取消").setOn(Button.EVENT_CLICK, PubConstants.CMD_DLG_CLOSE));

		view.addCommand("save", PubView.getSubmitCommand("product/devhardwareversion/saveHard?closeDlg=$0"));

		return view;
	}
	
	/**
	 * 判断是添加新版本或是编辑版本
	 * 
	 * @param form
	 * @param devHardwareVersion
	 */
	public static void fillBaseInfoForm(FlexGrid form, DevHardwareVersion devHardwareVersion) {
		if (form == null) {
			return;
		}
		form.addHidden("id", devHardwareVersion == null ? null : devHardwareVersion.getId());
		form.addHidden("productId", devHardwareVersion == null ? null : devHardwareVersion.getProductId());
		form.add("版本名称：", 3);
		form.add(
				new Text("versionName", "版本名称", devHardwareVersion == null ? null : devHardwareVersion.getVersionName())
						.setRequired(true),
				8);
		
		form.add("设备版本号：", 3);
		form.add(new Text("hardwareVersion", "设备版本号",
				devHardwareVersion == null ? null : devHardwareVersion.getHardwareVersion())
						.addValidate(Validate.maxvalue("99.99").setMaxvaluetext("设备版本号格式为x.y,xy范围为0-99"))
						.addValidate(Validate.minvalue("0.00").setMinvaluetext("设备版本号格式为x.y,xy范围为0-99"))
						.addValidate(Validate.pattern("/^[0-9]{1,2}\\.[0-9]{1,2}$/").setPatterntext("设备版本号格式为x.y,xy范围为0-99"))
						.setRequired(true),
				8);
		
		form.add(new Html(""), 4);
		form.add(new Html("设备版本号格式为x.y,xy范围为0-99"), 8);
	}

}
