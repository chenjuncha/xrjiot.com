package com.xrj.business.product.view;

import java.util.ArrayList;
import java.util.List;

import com.rongji.dfish.base.Utils;
import com.rongji.dfish.ui.Alignable;
import com.rongji.dfish.ui.Valignable;
import com.rongji.dfish.ui.Widget;
import com.rongji.dfish.ui.helper.Label;
import com.rongji.dfish.ui.layout.EmbedWindow;
import com.rongji.dfish.ui.layout.FrameLayout;
import com.rongji.dfish.ui.layout.HorizontalLayout;
import com.rongji.dfish.ui.layout.VerticalLayout;
import com.rongji.dfish.ui.layout.View;
import com.rongji.dfish.ui.widget.Button;
import com.rongji.dfish.ui.widget.Html;
import com.rongji.dfish.ui.widget.Leaf;
import com.rongji.dfish.ui.widget.Split;
import com.rongji.dfish.ui.widget.TreePanel;
import com.xrj.api.dto.DevProduct;
import com.xrj.api.dto.SysResource;
import com.xrj.api.em.AuditStatusEnum;
import com.xrj.api.em.ProductStatusEnum;
import com.xrj.business.product.constans.TreeConstans;

/**
 * 
 * @author yangzeyi
 * @Date 2017年7月19日
 *
 */
public class DevMenuView {
	public static final String DEVELOPER_MENU_ID="010000000000000000000000";
	public static View buildBodyView(DevProduct product) {
		View view = new View();
		VerticalLayout verRoot = new VerticalLayout("root");
		HorizontalLayout root = new HorizontalLayout("bd_root");
		view.add(verRoot);
//		verRoot.add(buildTopLayout(product).setHeight(40));

		verRoot.add(new Html(
				"<hr style='filter:progid:DXImageTransform.Microsoft.Shadow(color:#CCC,direction:145,strength:15)' color='#CCC' width='100%' size='1' />")
						.setHeight(1),
				"-1");
		verRoot.add(root);
		root.setCls("bg-body");
		VerticalLayout ver = new VerticalLayout("bd_menu_ver");
		TreePanel menu = new TreePanel("bd_menu").setScroll(true).setCls("bg-menu bd-menu bd-onlyright").setWmin(1);
//		ver.add(new Html("<h3>产品:"+product.getProductName()+"</h3>").setCls("bd-onlyright .bg-menu"), "50");
		ver.add(menu);
		root.add(ver, "200");

		Split split = new Split().setId("bd_split");
		// split.setRange("0,230").setIcon(".ico-split").setOpenicon(".ico-split-open");
		root.add(split, "10");

		Widget<?> main = buildMainLayout();
		root.add(main, "*");

		return view;
	}

	public static HorizontalLayout buildTopLayout(DevProduct product) {
		HorizontalLayout top = new HorizontalLayout("top");
		if (product.getStatus().equals(ProductStatusEnum.PUBLISHED.getIndex())) {
			top.add(new Html("<h3>产品:</h3>").setWidth(40));
			top.add(new Html("<h3>" + product.getProductName() + "</h3>"));
			top.add(new Html(""));
			top.add(new Html("")).add(new Html("")).add(new Html(""));
			top.add(new Html(""));
			top.add(new Html(""));
			top.add(new Html(""));
			top.add(new Html(""));
			if (!product.getAuditStatus().equals(AuditStatusEnum.PASS.getIndex())) {
				top.add(new Button("发布产品").setCls("x-btn").setOn(Button.EVENT_CLICK, "VM(this).cmd('publishProduct')")
						.setWidth(120));
				top.add(new Html("").setWidth(40));
			}
			top.setValign(Valignable.VALIGN_BOTTOM);
			return top;
		}
		top.add(new Html("<h3>产品:</h3>").setWidth(40));
		top.add(new Html("<h3>" + product.getProductName() + "</h3>"));
		top.add(new Html(""));
		top.add(new Html("")).add(new Html("")).add(new Html(""));
		top.add(new Html(""));
		top.add(new Html(""));
		top.add(new Html(""));
		top.add(new Html(""));
		top.add(new Button("删除产品").setCls("x-btn")
				.setOn(Button.EVENT_CLICK, " VM(this).cmd('deleteProduct','" + product.getId() + "')").setWidth(120));
		top.add(new Html("").setWidth(40));
		top.add(new Button("发布产品").setCls("x-btn").setOn(Button.EVENT_CLICK, "VM(this).cmd('publishProduct')")
				.setWidth(120));
		top.add(new Html("").setWidth(40));
		top.setValign(Valignable.VALIGN_BOTTOM);
		return top;
	}

	public static FrameLayout buildMainLayout() {
		// EmbedWindow embedWindow = new EmbedWindow("register.jsp");
		FrameLayout mainContent = new FrameLayout("mainContent");
		return mainContent;
	}

	public static View buildMenuView(String anchor, DevProduct product, List<SysResource> listMenu) {
		View view = buildBodyView(product);
		TreePanel menuTree = (TreePanel) view.findNodeById("bd_menu");
		FrameLayout mainContent = (FrameLayout) view.findNodeById("mainContent");
		// List<Menu> menuList = getMenuData();
		Leaf leaf = null;
		SysResource focusMenu = null;
		for (SysResource menu : listMenu) {
			if (Utils.notEmpty(menu.getParentId())) {
				if (menu.getParentId().equals(DEVELOPER_MENU_ID)) {
					leaf = new Leaf(menu.getId(), menu.getResName()).setFocus(false).setOpen(true).setIcon(".x-folder");
					menuTree.add(leaf);
				} else if (menu.getId().substring(0, 2).equals("01")) {
					Leaf childLeaf = new Leaf(menu.getId(), menu.getResName());
					leaf.add(childLeaf);
					View viewContent = new View("v_" + menu.getId());
					viewContent.setSrc(menu.getUri());
					mainContent.add(viewContent);
					// mainContent.setDft("v_"+focusMenu.getMenuId());
					childLeaf.setOn(Leaf.EVENT_CLICK, "this.cmd('clickMenu','" + menu.getId() + "');");
				}
				if (anchor != null && menu.getId().equals(anchor)) {
					focusMenu = menu;
					leaf.setFocus(true);
				}
			}
		}
		mainContent.setDft("v_" + anchor);
		Leaf focusleaf = (Leaf) menuTree.findNodeById(anchor);
		focusleaf.setFocus(true);

		// System.out.println(mainContent.getNodes());
		return view;
	}

}
