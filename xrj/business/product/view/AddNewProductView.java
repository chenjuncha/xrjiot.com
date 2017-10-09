package com.xrj.business.product.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rongji.dfish.base.Utils;
import com.rongji.dfish.ui.Valignable;
import com.rongji.dfish.ui.form.Radio;
import com.rongji.dfish.ui.form.Radiogroup;
import com.rongji.dfish.ui.form.Select;
import com.rongji.dfish.ui.form.Text;
import com.rongji.dfish.ui.form.Textarea;
import com.rongji.dfish.ui.form.Validate;
import com.rongji.dfish.ui.helper.FormPanel;
import com.rongji.dfish.ui.helper.HorizontalGroup;
import com.rongji.dfish.ui.helper.Label;
import com.rongji.dfish.ui.layout.ButtonBar;
import com.rongji.dfish.ui.layout.HorizontalLayout;
import com.rongji.dfish.ui.layout.VerticalLayout;
import com.rongji.dfish.ui.layout.View;
import com.rongji.dfish.ui.widget.Button;
import com.rongji.dfish.ui.widget.Html;
import com.rongji.dfish.ui.widget.SubmitButton;
import com.xrj.api.dto.SysDataDict;
import com.xrj.api.em.DataDictEnum;
import com.xrj.api.em.DeviceTypeEnum;
import com.xrj.business.core.base.PubService;
import com.xrj.business.core.view.PubView;

import antlr.collections.impl.Vector;

/**
 * 
 * @author yangzeyi
 *
 */
public class AddNewProductView {

	public static View addNewProductView() {

		View view = new View();

		VerticalLayout root = new VerticalLayout("root");
		view.add(root);

		HorizontalLayout top = new HorizontalLayout("top").setHeight(50);
		top.add(new Html("").setWidth(20));
		top.add(new Html("<h2>产品列表：创建新产品</h2>"));
		root.add(top);
		root.add(new Html(
				"<hr style='filter:progid:DXImageTransform.Microsoft.Shadow(color:#CCC,direction:145,strength:15)' color='#CCC' width='100%' size='1' />")
						.setHeight(1),
				"-1");

		HorizontalLayout horizontalLayout = new HorizontalLayout("");
		root.add(horizontalLayout);
		horizontalLayout.add(new Html("").setWidth("30%"));

		FormPanel formPanel = new FormPanel("addNewProductForm").setScroll(false);
		horizontalLayout.add(formPanel);

		formPanel.add(new Text("productName", "<span style='color:red;'>*</span> 产品名称:", "").setWidth(300)
				.setValidate(new Validate().setRequiredtext("产品名称不能为空").setRequired(true)));
		HorizontalGroup selectClass = new HorizontalGroup("class").setLabel("<span style='color:red;'>*</span> 产品分类:")
				.setWidth(1000);
		Map<String, List<SysDataDict>> mapClass = buildProductClassListMap(null);
		HorizontalLayout class2Layout = new HorizontalLayout("class2Layout");
		class2Layout.add(new Select("class2", "", "", Arrays.asList(classList(mapClass.get("list2"))))
				.setOn(Select.EVENT_CHANGE, "VM(this).cmd('changeClass3',this.val())"));
		// formPanel.add(class2Layout);
		HorizontalLayout class3Layout = new HorizontalLayout("class3Layout");
		class3Layout.add(new Select("class3", "", "", Arrays.asList(classList(mapClass.get("list3")))));
		// formPanel.add(class3Layout);
		selectClass.setValign(Valignable.VALIGN_MIDDLE);
		selectClass.add(new Select("class1", "", "", Arrays.asList(classList(mapClass.get("list1"))))
				.setOn(Select.EVENT_CHANGE, "VM(this).cmd('changeClass2',this.val())"));
		selectClass.add(class2Layout);
		selectClass.add(class3Layout);
		selectClass.add(new Html(""));
		selectClass.add(new Html(""));
		selectClass.add(new Html(""));
		selectClass.add(new Html(""));
		formPanel.add(selectClass);

		Radiogroup radiogroup = new Radiogroup("productType", "", "", Arrays.asList(new Object[][] {}))
				.setValidate(new Validate().setRequiredtext("产品类型不能为空").setRequired(true));

		Map<String, String> map = PubService.getDataDictMap(DataDictEnum.product_type.toString());
		for(DeviceTypeEnum deviceTypeEnum: DeviceTypeEnum.values()){
			radiogroup.add(
					new Radio("t", "", false, deviceTypeEnum.getIndex(), map.get(deviceTypeEnum.getIndex())));
		}
//		radiogroup.add(
//				new Radio("t", "", false, DeviceTypeEnum.NORMAL.getIndex(), map.get(DeviceTypeEnum.NORMAL.getIndex())));
//		radiogroup.add(new Radio("t", "", false, DeviceTypeEnum.GATEWAY.getIndex(),
//				map.get(DeviceTypeEnum.GATEWAY.getIndex())));
		HorizontalGroup horizontalGroup = new HorizontalGroup("").setLabel("<span style='color:red;'>*</span> 产品类型:");
		horizontalGroup.add(radiogroup);
		formPanel.add(horizontalGroup);
		formPanel.add(new Textarea("productUse", "<span style='color:red;'>*</span> 用途:", "").setWidth("300")
				.setHeight("100").setValidate(new Validate().setRequiredtext("产品用途不能为空").setRequired(true)));

		HorizontalLayout btnLayout = new HorizontalLayout("btnHor");

		SubmitButton submitButton1 = new SubmitButton("创建").setCls("x-btn x-btn-main").setOn(SubmitButton.EVENT_CLICK,
				"VM(this).cmd('addNewProduct')");// product/baseInfo/save
		Button submitButton2 = new Button("取消").setCls("x-btn").setOn(Button.EVENT_CLICK, "VM(this).cmd('toBack')");// product/index
		ButtonBar buttonBar = new ButtonBar("buttonBar").add(submitButton1).add(submitButton2);
		buttonBar.setSpace(50);
		btnLayout.add(new Html("").setWidth("10%"));
		btnLayout.add(buttonBar);
		btnLayout.add(new Html(""));
		formPanel.add(new Label("", ""));
		formPanel.add(btnLayout);

		return view;
	}

	/**
	 * 初始化下拉
	 * 
	 * @param list
	 * @return
	 */
	public static Object[][] classList(List<SysDataDict> list) {
		// System.out.println("长度"+sb.length());
		String class1[][] = new String[list.size()][2];
		int j = 0;
		for (SysDataDict sysDataDict : list) {
			class1[j][0] = sysDataDict.getDictCode();
			class1[j][1] = sysDataDict.getDictName();
			j = j + 1;
		}

		return class1;
	}

	/**
	 * 二级下拉更改
	 * 
	 * @param list
	 * @return
	 */
	public static HorizontalLayout buildSelect2(List<SysDataDict> list) {
		HorizontalLayout class2Layout = new HorizontalLayout("class2Layout");
		Select select = new Select("class2", "", "", Arrays.asList(classList(list))).setOn(Select.EVENT_CHANGE,
				"VM(this).cmd('changeClass3',this.val())");
		// view.add(select)
		class2Layout.add(select);
		return class2Layout;
	}

	/**
	 * 三级下拉列表更改
	 * 
	 * @param list
	 * @return
	 */
	public static HorizontalLayout buildSelect3(List<SysDataDict> list) {

		HorizontalLayout class3Layout = new HorizontalLayout("class3Layout");
		Select select = new Select("class3", "", "", Arrays.asList(classList(list)));
		class3Layout.add(select);
		return class3Layout;
	}

	/**
	 * 从缓存中提取产品分类数据
	 * 
	 * @param code
	 *            产品分类代码
	 * @return
	 */
	public static Map<String, List<SysDataDict>> buildProductClassListMap(String code) {
		Map<String, List<SysDataDict>> map = new HashMap<String, List<SysDataDict>>();
		List<SysDataDict> listAll = PubService.getDataDictList(DataDictEnum.product_classification.toString());
		List<SysDataDict> list1 = new ArrayList<SysDataDict>();
		List<SysDataDict> list2 = new ArrayList<SysDataDict>();
		List<SysDataDict> list3 = new ArrayList<SysDataDict>();
		if (Utils.isEmpty(code)) {
			// 初始化加载数据
			for (SysDataDict sysDataDict : listAll) {
				if (sysDataDict.getDictCode().length() == 2) {
					list1.add(sysDataDict);
				} else if (sysDataDict.getDictCode().substring(0, 2).equals("01")
						&& sysDataDict.getDictCode().length() == 4) {
					list2.add(sysDataDict);
				} else if (sysDataDict.getDictCode().substring(0, 4).equals("0101")
						&& sysDataDict.getDictCode().length() > 4) {
					list3.add(sysDataDict);
				}
			}
		} else {
			if (code.length() == 4) {

				for (SysDataDict sysDataDict : listAll) {
					if (sysDataDict.getDictCode().length() == 4
							&& sysDataDict.getDictCode().substring(0, 2).equals(code.substring(0, 2))) {
						list2.add(sysDataDict);
					} else if (sysDataDict.getDictCode().length() == 2) {
						list1.add(sysDataDict);
					} else if (sysDataDict.getDictCode().length() == 6
							&& sysDataDict.getDictCode().substring(0, 4).equals(code)) {
						list3.add(sysDataDict);
					}
				}
			} else if (code.length() == 6) {
				for (SysDataDict sysDataDict : listAll) {
					if (sysDataDict.getDictCode().length() > 4
							&& sysDataDict.getDictCode().substring(0, 4).equals(code.substring(0, 4))) {
						list3.add(sysDataDict);
					} else if (sysDataDict.getDictCode().length() == 4
							&& sysDataDict.getDictCode().substring(0, 2).equals(code.substring(0, 2))) {
						list2.add(sysDataDict);
					} else if (sysDataDict.getDictCode().length() == 2) {
						list1.add(sysDataDict);
					}
				}
			} else if (code.length() == 2) {
				for (SysDataDict sysDataDict : listAll) {

					if (sysDataDict.getDictCode().length() == 4
							&& sysDataDict.getDictCode().substring(0, 2).equals(code)) {
						list2.add(sysDataDict);
					}
				}
				String list2Code = list2.get(0).getDictCode().substring(0, 4);
				for (SysDataDict sysDataDict : listAll) {
					if (sysDataDict.getDictCode().length() == 6
							&& (sysDataDict.getDictCode().substring(0, 4)).equals(list2Code)) {
						list3.add(sysDataDict);
					}
				}
			}

		}
		map.put("list1", list1);
		map.put("list2", list2);
		map.put("list3", list3);
		return map;

	}
}
