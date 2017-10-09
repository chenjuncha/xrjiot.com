package com.xrj.business.product.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.swing.plaf.ButtonUI;

import com.rongji.dfish.base.Utils;
import com.rongji.dfish.base.util.DateUtil;
import com.rongji.dfish.framework.FrameworkHelper;
import com.rongji.dfish.ui.Valignable;
import com.rongji.dfish.ui.command.ReplaceCommand;
import com.rongji.dfish.ui.command.SubmitCommand;
import com.rongji.dfish.ui.form.Radio;
import com.rongji.dfish.ui.form.Radiogroup;
import com.rongji.dfish.ui.form.Select;
import com.rongji.dfish.ui.form.Text;
import com.rongji.dfish.ui.form.Textarea;
import com.rongji.dfish.ui.form.Validate;
import com.rongji.dfish.ui.helper.FlexGrid;
import com.rongji.dfish.ui.helper.FormPanel;
import com.rongji.dfish.ui.helper.GridLayoutFormPanel;
import com.rongji.dfish.ui.helper.GridPanel;
import com.rongji.dfish.ui.helper.HorizontalGroup;
import com.rongji.dfish.ui.helper.Label;
import com.rongji.dfish.ui.layout.ButtonBar;
import com.rongji.dfish.ui.layout.GridLayout;
import com.rongji.dfish.ui.layout.HorizontalLayout;
import com.rongji.dfish.ui.layout.VerticalLayout;
import com.rongji.dfish.ui.layout.View;
import com.rongji.dfish.ui.layout.grid.GridColumn;
import com.rongji.dfish.ui.layout.grid.Tr;
import com.rongji.dfish.ui.widget.Button;
import com.rongji.dfish.ui.widget.Html;
import com.rongji.dfish.ui.widget.SubmitButton;
import com.xrj.api.dto.DevProduct;
import com.xrj.api.dto.SysDataDict;
import com.xrj.api.em.DataDictEnum;
import com.xrj.api.em.DeviceTypeEnum;
import com.xrj.api.em.ProductStatusEnum;
import com.xrj.business.core.base.PubService;
import com.xrj.business.core.base.RemoteService;
import com.xrj.business.core.view.PubView;

public class DevBaseInfoView {

	public static View baseInfo(DevProduct product) {

		View view = new View();
		view.addCommand("toBaseInfo", PubView.getAjaxCommand("product/baseInfo/toBaseInfo"));
		view.addCommand("toProductModify", PubView.getAjaxCommand("product/baseInfo/modify"));
		view.addCommand("modifyInfo", new SubmitCommand("product/modifyProduct").setRange("modifyProductForm"));
		view.addCommand("toBack", PubView.getAjaxCommand("product/toBackCommand2"));
		view.addCommand("changeClass2", PubView.getAjaxCommand("selectChange?selectValue=$0"));
		view.addCommand("changeClass3", PubView.getAjaxCommand("selectChange2?selectValue=$0"));
		VerticalLayout root = new VerticalLayout("main_content");
		view.add(root);

		HorizontalLayout top = new HorizontalLayout("top").setHeight(40);
		top.add(new Html("<h3>产品基本信息</h3>"));
		root.add(top);
		root.add(new Html(
				"<hr style='filter:progid:DXImageTransform.Microsoft.Shadow(color:#CCC,direction:145,strength:15)' color='#CCC' width='100%' size='1' />")
						.setHeight(1),
				"-1");
		HorizontalLayout horizontalLayout = new HorizontalLayout("");
		root.add(horizontalLayout);
		// root.add(new Html(""));

//		FormPanel formPanel = new FormPanel("form").setCls("x-grid-odd");
		RemoteService remoteService = FrameworkHelper.getBean(RemoteService.class);
		SysDataDict sysDataDict = remoteService.getSysDataDictService()
				.findUnique(DataDictEnum.product_classification.toString(), product.getProductClassification());
//		formPanel.add(new Label("产品名称:", product.getProductName()));
//		formPanel.add(new Html("").setHeight(10));
//		formPanel.add(new Label("产品分类:", sysDataDict.getRemark()));
//		formPanel.add(new Html("").setHeight(10));
		Radiogroup radiogroup = new Radiogroup("type", "产品类型:", product.getProductType(),
				Arrays.asList(new Object[][] {})).setReadonly(true);
//		formPanel.add(radiogroup.setStyle("color: #CCC;"));
//		formPanel.add(new Html("").setHeight(10));
		Map<String, String> map = PubService.getDataDictMap(DataDictEnum.product_type.toString());
//		radiogroup.add(new Radio("type", "",
//				(product.getProductType().equals(DeviceTypeEnum.NORMAL.getIndex())) ? true : false,
//				DeviceTypeEnum.NORMAL.getIndex(), map.get(DeviceTypeEnum.NORMAL.getIndex())).setDisabled(true));
//		radiogroup.add(new Radio("type", "",
//				(product.getProductType().equals(DeviceTypeEnum.GATEWAY.getIndex())) ? true : false,
//				DeviceTypeEnum.GATEWAY.getIndex(), map.get(DeviceTypeEnum.GATEWAY.getIndex())).setDisabled(true));
		for(DeviceTypeEnum deviceTypeEnum: DeviceTypeEnum.values()){
			radiogroup.add(
					new Radio("t", "", (product.getProductType().equals(deviceTypeEnum.getIndex())) ? true : false, deviceTypeEnum.getIndex(), map.get(deviceTypeEnum.getIndex())));
		}
		GridLayout gridPanel = new GridLayout("").setCls("x-grid-odd").setFace(GridPanel.FACE_LINE)
				   .setScroll(false).setNobr(false).setFocusable(true)
				   .setPub(new Tr().setHeight("40").setCls("w-td-line w-th"));
		gridPanel.getThead().setCls("x-grid-head").setHeight(-1);
		gridPanel.add(0, 0, new Label("","产品名称:"));
		gridPanel.add(0, 1, new Html(product.getProductName()));
		gridPanel.add(1, 0, new Label("","产品分类:"));
		gridPanel.add(1, 1, new Html(sysDataDict.getRemark()));
		gridPanel.add(2, 0, new Label("","产品类型:"));
		gridPanel.add(2, 1, radiogroup.setStyle("color: #CCC;"));
		gridPanel.add(3, 0, new Label("","产品唯一标识:"));
		gridPanel.add(3, 1, new Html(product.getProductKey()));
		gridPanel.add(4, 0, new Label("","创建时间:"));
		gridPanel.add(4, 1, DateUtil.format(product.getCreateTime(), DateUtil.PATTERN_DATE));
		gridPanel.add(5, 0, new Label("","用途:"));
		gridPanel.add(5, 1, product.getProductUse());
	
		VerticalLayout verticalLayout = new VerticalLayout("");
		verticalLayout.add(gridPanel);
		horizontalLayout.add(verticalLayout);

		Button button1 = new Button("修改").setCls("x-btn x-btn-main").setOn(Button.EVENT_CLICK,
				"VM(this).cmd('toProductModify')");// product/baseInfo/save
		//Button button2 = new Button("返回").setCls("x-btn").setOn(Button.EVENT_CLICK, "VM(this).cmd('toBack')");// product/index

		ButtonBar buttonBar = new ButtonBar("buttonBar");
		if (product.getStatus().equals(ProductStatusEnum.UNPUBLISHED.getIndex())) {
//			buttonBar.add(new Html("").setWidth("70"));
//			buttonBar.add(button2);
//			buttonBar.add(new Html(""));
//		} else {
			buttonBar.add(new Html("").setWidth("50"));
			buttonBar.add(button1);
			buttonBar.setSpace(50);
		}
		HorizontalLayout btnHor = new HorizontalLayout("").setHeight(-1);
		btnHor.add(new Html(""));
		btnHor.add(buttonBar);
		btnHor.add(new Html(""));
		verticalLayout.add(btnHor);
		verticalLayout.add(new Html("").setHeight("10%"));
		return view;
	}

	/**
	 * 产品修改界面
	 * 
	 * @return
	 */
	public static View buildProductModifyView(DevProduct product) {
		View view = new View();
		VerticalLayout verticalLayout = new VerticalLayout("main_content");
		view.add(verticalLayout);

		HorizontalLayout top = new HorizontalLayout("main_top").setHeight(40);
		top.add(new Html("").setWidth(20));
		top.add(new Html("<h3>产品详情</h3>"));
		verticalLayout.add(top);
		verticalLayout.add(new Html(
				"<hr style='filter:progid:DXImageTransform.Microsoft.Shadow(color:#CCC,direction:145,strength:15)' color='#CCC' width='100%' size='1' />")
						.setHeight(1),
				"-1");
		HorizontalLayout mainContentLayout = new HorizontalLayout("main_content_hor");
		mainContentLayout.add(new Html("").setWidth("25%"));
		FormPanel gridLayoutFormPanel = new FormPanel("modifyProductForm").setScroll(false);
		gridLayoutFormPanel
				.add(new Text("productName", "<span style='color:red;'>*</span> 产品名称:", product.getProductName())
						.setValidate(new Validate().setRequiredtext("产品名称不能为空").setRequired(true)).setWidth(300));
		HorizontalGroup selectClass = new HorizontalGroup("class").setLabel("<span style='color:red;'>*</span> 产品分类:")
				.setWidth(1000);
		HorizontalLayout class2Layout = new HorizontalLayout("class2Layout");
		HorizontalLayout class3Layout = new HorizontalLayout("class3Layout");
		String code = product.getProductClassification();
		Map<String, List<SysDataDict>> mapClass = AddNewProductView.buildProductClassListMap(code);
		// 分类code为第二级时显示两个下拉框
		if (code.length() == 4) {
			class2Layout.add(new Select("class2", "", product.getProductClassification(),
					Arrays.asList(AddNewProductView.classList(mapClass.get("list2")))).setOn(Select.EVENT_CHANGE,
							"VM(this).cmd('changeClass3',this.val())"));
			class3Layout.add(new Html(""));
			selectClass.add(new Select("class1", "", product.getProductClassification().substring(0, 2),
					Arrays.asList(AddNewProductView.classList(mapClass.get("list1")))).setOn(Select.EVENT_CHANGE,
							"VM(this).cmd('changeClass2',this.val())"));
		} else {
			class2Layout.add(new Select("class2", "", product.getProductClassification().substring(0, 4),
					Arrays.asList(AddNewProductView.classList(mapClass.get("list2")))).setOn(Select.EVENT_CHANGE,
							"VM(this).cmd('changeClass3',this.val())"));
			class3Layout.add(new Select("class3", "", product.getProductClassification(),
					Arrays.asList(AddNewProductView.classList(mapClass.get("list3")))));
			selectClass.add(new Select("class1", "", product.getProductClassification().substring(0, 2),
					Arrays.asList(AddNewProductView.classList(mapClass.get("list1")))).setOn(Select.EVENT_CHANGE,
							"VM(this).cmd('changeClass2',this.val())"));
		}
		selectClass.setValign(Valignable.VALIGN_MIDDLE);
		selectClass.add(class2Layout);
		selectClass.add(class3Layout);
		selectClass.add(new Html(""));
		selectClass.add(new Html(""));
		selectClass.add(new Html(""));
		gridLayoutFormPanel.add(selectClass);
		Radiogroup radiogroup = new Radiogroup("accessType", "接入方案:", product.getProductType(), null).setReadonly(true);
		radiogroup.setValidate(new Validate().setRequiredtext("接入方案不能为空").setRequired(true));
		Map<String, String> map = PubService.getDataDictMap(DataDictEnum.product_type.toString());
		for(DeviceTypeEnum deviceTypeEnum: DeviceTypeEnum.values()){
			radiogroup.add(
					new Radio("t", "", (product.getProductType().equals(deviceTypeEnum.getIndex())) ? true : false, deviceTypeEnum.getIndex(), map.get(deviceTypeEnum.getIndex())));
		}
		gridLayoutFormPanel.add(radiogroup);
		gridLayoutFormPanel.add(new Label("产品唯一标识:", product.getProductKey()));
		// gridLayoutFormPanel.add(4, 0, 4, 0, new Label("产品证书:",
		// product.getProductKey()));
		// gridLayoutFormPanel.add(4, 1, new Button("显示完整密钥").setCls("btn"));
		// gridLayoutFormPanel.add(4, 2, 4, 3, new Select("year", "密钥有效时间:",
		// "00", Arrays.asList(new Object[][]{{"00","一年"}})));
		gridLayoutFormPanel.add(new Label("创建时间:", DateUtil.format(product.getCreateTime(), DateUtil.PATTERN_DATE)));
		gridLayoutFormPanel
				.add(new Textarea("productUse", "<span style='color:red;'>*</span> 描述、用途:", product.getProductUse())
						.setValidate(new Validate().setRequiredtext("产品用途不能为空").setRequired(true)).setWidth("300")
						.setHeight("100"));
		ButtonBar btm = new ButtonBar("btm").setSpace(50);
		btm.add(new Html("").setWidth("25%"));
		btm.add(new SubmitButton("保存").setCls("x-btn x-btn-main")
				.setOn(SubmitButton.EVENT_CLICK, "VM(this).cmd('modifyInfo')").setWidth(70));
		btm.add(new Button("取消").setCls("x-btn").setOn(Button.EVENT_CLICK, "VM(this).cmd('toBaseInfo')").setWidth(70));
		gridLayoutFormPanel.add(btm);
		// gridLayoutFormPanel.add(7, 0, 7, 4, new Html(""));
		mainContentLayout.add(gridLayoutFormPanel);
		verticalLayout.add(mainContentLayout);
		mainContentLayout.add(new Html("").setWidth(-1));
		return view;
	}
}
