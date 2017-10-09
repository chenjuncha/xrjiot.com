package com.xrj.business.product.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;

import com.rongji.dfish.base.Utils;
import com.rongji.dfish.base.util.DateUtil;
import com.rongji.dfish.ui.Alignable;
import com.rongji.dfish.ui.Command;
import com.rongji.dfish.ui.Valignable;
import com.rongji.dfish.ui.Widget;
import com.rongji.dfish.ui.command.CommandGroup;
import com.rongji.dfish.ui.command.ConfirmCommand;
import com.rongji.dfish.ui.command.JSCommand;
import com.rongji.dfish.ui.command.ReplaceCommand;
import com.rongji.dfish.ui.command.SubmitCommand;
import com.rongji.dfish.ui.form.Text;
import com.rongji.dfish.ui.form.Xbox;
import com.rongji.dfish.ui.helper.FormPanel;
import com.rongji.dfish.ui.helper.GridLayoutFormPanel;
import com.rongji.dfish.ui.helper.Label;
import com.rongji.dfish.ui.helper.TabPanel;
import com.rongji.dfish.ui.layout.ButtonBar;
import com.rongji.dfish.ui.layout.HorizontalLayout;
import com.rongji.dfish.ui.layout.LinearLayout;
import com.rongji.dfish.ui.layout.VerticalLayout;
import com.rongji.dfish.ui.layout.View;
import com.rongji.dfish.ui.widget.Button;
import com.rongji.dfish.ui.widget.Html;
import com.rongji.dfish.ui.widget.Split;
import com.xrj.api.dto.DevProduct;
import com.xrj.api.dto.SysDataDict;
import com.xrj.api.em.AuditStatusEnum;
import com.xrj.api.em.DataDictEnum;
import com.xrj.api.em.DeviceTypeEnum;
import com.xrj.api.em.ProductStatusEnum;
import com.xrj.business.core.base.PubService;
import com.xrj.business.core.view.PubView;
import com.xrj.business.product.constans.TreeConstans;

/**
 * @author yangzeyi
 * @Date 2017年7月19日
 */
public class DevProductView {
	public static final String BASE_MENU_ID = "010102000000000000000000";
	public static View bulidProductIndex(List<DevProduct> productList) {
		View view = new View();
		VerticalLayout root = new VerticalLayout("root");
		view.add(root);
		view.addCommand("baseInfoCommand", PubView.getAjaxCommand("product/baseInfoCommand?anchor=$0&productId=$1"));
		// view.addCommand("", new ReplaceCommand())
		view.addCommand("addNewProductCommand", PubView.getAjaxCommand("product/addNewProductCommand"));
		view.addCommand("clickMenu", PubView.getAjaxCommand("product/clickMenu?menuId=$0"));
		view.addCommand("deleteProduct", new ConfirmCommand("删除产品 [$1] 将删除与其关联的所有配置，是否真的要删除？")
				.setYes(PubView.getAjaxCommand("product/deleteAndtoBack?productId=$0")));
		view.addCommand("addNewProduct", new SubmitCommand("product/addNewProduct").setRange("addNewProductForm"));
		view.addCommand("toBack", PubView.getAjaxCommand("product/toBackCommand"));
		view.addCommand("changeClass2", PubView.getAjaxCommand("selectChange?selectValue=$0"));
		view.addCommand("changeClass3", PubView.getAjaxCommand("selectChange2?selectValue=$0"));
		view.addCommand("publishProduct",
				new ConfirmCommand("是否要发布产品 [$1] ？").setYes(PubView.getAjaxCommand("product/publishProduct?productId=$0")));
		view.addCommand("search", PubView.getSubmitCommand("product/productSearch"));
		HorizontalLayout top = new HorizontalLayout("top").setHeight(50);
		// top.add(new Html("").setWidth(20));
		//--------------------------查询条件--------------------------------------------------
		GridLayoutFormPanel mainSearch =new GridLayoutFormPanel("searchGrid").setScroll(false);
//		mainSearch.setHeight("50");//设置搜索区的高度
		List<String[]> sysCodeTypeArr = new ArrayList<String[]>();
			sysCodeTypeArr.add(new String[] { null, "--全部--" });
			sysCodeTypeArr.add(new String[] { ProductStatusEnum.PUBLISHED.getIndex(), ProductStatusEnum.PUBLISHED.getName() });
			sysCodeTypeArr.add(new String[] { ProductStatusEnum.UNPUBLISHED.getIndex(), ProductStatusEnum.UNPUBLISHED.getName() });
		Xbox xbox = new Xbox("productStatus", "<h3>产品列表</h3>", ProductStatusEnum.PUBLISHED.getIndex(), sysCodeTypeArr).setWidth(200).setOn(Xbox.EVENT_CHANGE, "VM(this).cmd('search');");
		int rowIndex = 0;
		mainSearch.add(rowIndex, 0, xbox);
		HorizontalLayout horizontalLayout = new HorizontalLayout("");
		horizontalLayout.add(new Html(""));
		horizontalLayout.add(new Html("").setWidth(-1));
		horizontalLayout.add(new Text("productName", "", null).setPlaceholder("请输入产品名称"));
		mainSearch.add(rowIndex, 1, horizontalLayout);
		top.add(mainSearch);
//		top.add(new Html("<h2 style=\"color: gray\">产品列表</h2>"));
		ButtonBar topBtn = new ButtonBar("topBtn").setAlign(ButtonBar.ALIGN_RIGHT).setWidth("-1");
		topBtn.setSpace(5).setCls("groupbar").setStyle("margin-right:15px;").setWmin(20).setPub(new Button("").setCls("x-btn"));
		Button searchBtn = new Button("搜索").setOn(Button.EVENT_CLICK, "VM(this).cmd('search');");
		
		topBtn.add(searchBtn);
		topBtn.add(new Button("创建新产品").setCls("x-btn").setOn(Button.EVENT_CLICK, "VM(this).cmd('addNewProductCommand')").setWidth(120));
		//top.add(topBtn);
		top.add(new Html("").setWidth(20)).setValign(Valignable.VALIGN_MIDDLE);
		horizontalLayout.add(new Html("").setWidth(10));
		horizontalLayout.add(topBtn);
		root.add(top);
		root.add(new Html(
				"<hr style='filter:progid:DXImageTransform.Microsoft.Shadow(color:#CCC,direction:145,strength:15)' color='#CCC' width='100%' size='1' />")
						.setHeight(1),
				"-1");
//		Map<String, String> map = PubService.getDataDictMap("product_status");
		// VerticalLayout maintop = new VerticalLayout("maintop");
//		Widget<?> horizontalLayout = (Widget<?>) ProductView.bulidProductList(releaseList);
//		Widget<?> horizontalLayout2 = (Widget<?>) ProductView.bulidProductList(unreleaseList);
//		TabPanel tabPanel = new TabPanel("productTab");
//		tabPanel.add(new VerticalLayout("").add(horizontalLayout), map.get(ProductStatusEnum.PUBLISHED.getIndex()));
//		tabPanel.add(new VerticalLayout("").add(horizontalLayout2), map.get(ProductStatusEnum.UNPUBLISHED.getIndex()));
//		tabPanel.getTabBar().setHeight("40").setCls("x-tabbar");
//		tabPanel.getTabBar().getPub().setCls("x-tab");

//		root.add(mainSearch,"50");
		
		VerticalLayout prover = new VerticalLayout("product_list_ver").setScroll(true);
		root.add(bulidProductList(productList), "*");
//		root.add(new Html(""), "*");
		// maintop.add(new Html("<h2 style=\"color:
		// gray\">已发布产品</h2>").setHeight(50));
		// root.add(maintop);
		//// Widget<?> horizontalLayout = (Widget<?>)
		// ProductView.bulidProductList(releaseList);
		// //root.add(horizontalLayout);
		// maintop.add(horizontalLayout);
		// root.add(new Html("<hr
		// style='filter:progid:DXImageTransform.Microsoft.Shadow(color:#CCC,direction:145,strength:15)'
		// color='#CCC' width='100%' size='1' />").setHeight(1),"-1");
		//
		// VerticalLayout main2top = new VerticalLayout("main2top");
		// main2top.add(new Html("<h2 style=\"color:
		// gray\">开发中的产品</h2>").setHeight(50));
		// root.add(main2top);
		//// Widget<?> horizontalLayout2 = (Widget<?>)
		// ProductView.bulidProductList(unreleaseList);
		// //root.add(horizontalLayout);
		// main2top.add(horizontalLayout2);
		//
		return view;
	}

	/**
	 * 产品列表
	 * 
	 * @return
	 */
	public static Widget<?> bulidProductList(List<DevProduct> productList) {
		// View view = new View("product_list_view");
		VerticalLayout prover = new VerticalLayout("product_list_ver").setScroll(true);
		HorizontalLayout proRoot = null;
		// 每行最多显示3个产品块
		if (productList.size() == 0) {
			proRoot = new HorizontalLayout("").setHeight(-1);
			return prover.add(proRoot);
		}
		if (productList.size() < 3) {
			proRoot = new HorizontalLayout("").setHeight(-1);
			proRoot.add(new Html("").setWidth(100));
			for (DevProduct product : productList) {
				proRoot.add(bulidProductBody(product));
			}
			proRoot.add(new Html(""));
			return prover.add(proRoot);
		} else if (productList.size() == 3) {
			proRoot = new HorizontalLayout("").setHeight(-1);
			proRoot.add(new Html("").setWidth(100));
			for (DevProduct product : productList) {
				proRoot.add(bulidProductBody(product));
			}
			return prover.add(proRoot);
		} else {
			int i = 1;
			int n = 1;
			proRoot = new HorizontalLayout("").setHeight(-1);
			proRoot.add(new Html("").setWidth(100));
			prover.add(proRoot);
			for (DevProduct product : productList) {

				if (i == 3 * n + 1) {
					proRoot = new HorizontalLayout("");
					proRoot.add(new Html("").setWidth(100));
					prover.add(proRoot);
					n++;
				}
				// proRoot.add(new Html("").setWidth(100));
				proRoot.add(bulidProductBody(product));
				// proRoot.add(new Html("").setWidth(100));
				i = i + 1;
			}
			i = i - 1;
			n = n - 1;
			if (i == 3 * n + 2 || i == 3 * n + 1) {
				proRoot.add(new Html(""));
			}
		}
		return prover;
	}

	/**
	 * 产品块
	 * 
	 * @return
	 */
	private static VerticalLayout bulidProductBody(DevProduct product) {
		// View view = new View("product_body");
		VerticalLayout root = new VerticalLayout("product_root");
		// view.setNode(root);
		// VerticalLayout ver = new VerticalLayout("");
		Map<String, String> map = PubService.getDataDictMap(DataDictEnum.product_type.toString());
		String productType=null;
		for(DeviceTypeEnum deviceTypeEnum:DeviceTypeEnum.values()){
			if(deviceTypeEnum.getIndex().equals(product.getProductType())){
				productType=map.get(deviceTypeEnum.getIndex());
			}
		}
		root.add(new Html("").setHeight(10));
		FormPanel formPanel = new FormPanel("form").setScroll(false).setWidth(300).setHeight(250);
		formPanel.add(new Html("").setHeight(20));
		if (product.getAuditStatus().equals(AuditStatusEnum.HANDLING.getIndex())&&
				ProductStatusEnum.PUBLISHED.getIndex().equals(product.getStatus())) {
			formPanel.add(new Label("产品名称:", product.getProductName()+"(待审核)"));
		} else {
			formPanel.add(new Label("产品名称:", product.getProductName()));
		}
		formPanel.add(new Label("产品唯一标识:", product.getProductKey()));
		formPanel.add(new Label("产品类型:", productType));
		formPanel.add(new Label("创建时间:", DateUtil.format(product.getCreateTime(), DateUtil.PATTERN_DATE)));
		// formPanel.add(new Label("更新时间:",product.getProductType()));
		// formPanel.add());
		HorizontalLayout bottonHor = new HorizontalLayout("");
		bottonHor.add(new Html("").setWidth("20%"));
		bottonHor.add(new Button("进入").setCls("x-btn x-btn-main").setOn(Button.EVENT_CLICK,
				"VM(this).cmd('baseInfoCommand','"+BASE_MENU_ID+"','" + product.getId() + "')"));
		if(ProductStatusEnum.UNPUBLISHED.getIndex().equals(product.getStatus())){
			bottonHor.add(new Split().setWidth(5));
			bottonHor.add(new Button("发布").setCls("x-btn x-btn-main").setOn(Button.EVENT_CLICK, "VM(this).cmd('publishProduct','"+product.getId()+"','"+product.getProductName()+"');"));	
			bottonHor.add(new Split().setWidth(5));
			bottonHor.add(new Button("删除").setCls("x-btn x-btn-main").setOn(Button.EVENT_CLICK, "VM(this).cmd('deleteProduct','"+product.getId()+"','"+product.getProductName()+"');"));	
		}
		bottonHor.add(new Html("").setWidth("20%"));
		formPanel.add(bottonHor);
		formPanel.setStyle("border: 1px solid #CCC;");
		root.add(formPanel);
		// root.add(new Html("").setHeight(5));
		return root;
	}

}
