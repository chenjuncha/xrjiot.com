package com.xrj.business.core.view;

import com.rongji.dfish.ui.Widget;
import com.rongji.dfish.ui.helper.FormPanel;
import com.rongji.dfish.ui.helper.GridLayoutFormPanel;
import com.rongji.dfish.ui.helper.GridPanel;
import com.rongji.dfish.ui.layout.ButtonBar;
import com.rongji.dfish.ui.layout.HorizontalLayout;
import com.rongji.dfish.ui.layout.VerticalLayout;
import com.rongji.dfish.ui.layout.View;
import com.rongji.dfish.ui.widget.Button;
import com.rongji.dfish.ui.widget.Html;
import com.rongji.dfish.ui.widget.PageBar;

public class CommonView {
	public static View buildGridView(){
		View view=new View();
		VerticalLayout root=new VerticalLayout("root").setStyle("padding:0 20px 10px 20px;");
		view.setNode(root);
		HorizontalLayout top=new HorizontalLayout("top");
		root.add(top,"50");
		top.add(new Html("title","替换标题 id=title").setStyle("font-size:16px;line-height:50px;"));
		top.add(new ButtonBar("btnbar").setAlign(ButtonBar.ALIGN_RIGHT).setSpace(8).setPub(new Button("").setCls("btn")));
		
		VerticalLayout main=new VerticalLayout("main");
		root.add(main);
		main.setCls("bd-mod").setHmin(2).setWmin(2);
		
		GridPanel grid=new GridPanel("grid");
		main.add(grid);
		grid.setFace(GridPanel.FACE_LINE).setScroll(true).setFocusable(true);
		
		PageBar page=new PageBar("page",PageBar.TYPE_BUTTONGROUP);
		main.add(page,"40");
		page.setAlign(PageBar.ALIGN_RIGHT).setStyle("padding-right:8px;");
		
		return view;
	}
	public static View buildGridAndFormView(){
		View view=new View();
		VerticalLayout root=new VerticalLayout("root").setStyle("padding:0 20px 10px 20px;");
		view.setNode(root);
		root.setCls("bg-white");
		HorizontalLayout top=new HorizontalLayout("top");
		root.add(top,"50");
		top.add(new Html("title","替换标题 id=title").setStyle("font-size:16px;line-height:50px;"));
		top.add(new ButtonBar("btnbar").setAlign(ButtonBar.ALIGN_RIGHT).setSpace(8).setPub(new Button("").setCls("btn")));
		
		HorizontalLayout shell=new HorizontalLayout("shell");
		root.add(shell);
		VerticalLayout left=new VerticalLayout("left");
		shell.add(left,"240");
		left.setCls("bd-mod").setHmin(2).setWmin(2);
		
		GridPanel grid=new GridPanel("grid");
		left.add(grid);
		grid.setFace(GridPanel.FACE_LINE).setScroll(true).setFocusable(true);
		
		PageBar page=new PageBar("page",PageBar.TYPE_BUTTONGROUP);
		left.add(page,"40");
		page.setAlign(PageBar.ALIGN_RIGHT).setStyle("padding-right:8px;");
		
		shell.add(new Html(null),"10");
		
		VerticalLayout right=new VerticalLayout("right");
		shell.add(right);
		right.setCls("bd-mod").setHmin(2).setWmin(2);
		right.add(new FormPanel("form"));
		
		return view;
	}
	public static View buildSelectView() {
		View view = new View();
		
		VerticalLayout root = new VerticalLayout("root");
		view.setNode(root);
		root.setStyle("background-color:#FFFFFF;");
		
		FormPanel form = new FormPanel("form");
		root.add(form, "*");
		form.setScroll(true);
		form.setScrollClass(FormPanel.SCROLL_MINI);
		
		
		return view;
	}
	public static View buildPopView() {
		View view = new View();
		
		VerticalLayout root = new VerticalLayout("root");
		view.setNode(root);
		root.setStyle("background-color:#FFFFFF;");
		
		FormPanel form = new FormPanel("form");
		root.add(form, "*");
		form.setScroll(true);
		form.setScrollClass(FormPanel.SCROLL_MINI);
		
		ButtonBar btnBar = new ButtonBar("btnBar");
		root.add(btnBar, "50");
		btnBar.setPub(new Button(null).setCls("btn"));
		
		btnBar.setAlign(ButtonBar.ALIGN_RIGHT)
				.setCls("dlg-foot").setSpace(8).setStyle("padding-right:20px")
				.setWmin(20).setHmin(1);
		
		return view;
	}
	public final static String DIALOG_ROOT = "dlg_root"; 
	public final static String DIALOG_MAIN_SHELL = "dlg_main_shell";
	public final static String DIALOG_MAIN = "dlg_main";
	public final static String DIALOG_BUTTON_SHELL = "dlg_btn_shell";
	public final static String DIALOG_BUTTON = "dlg_btn";
	public static final String DIALOG_PERM_LEFT = "d_p_left";
	public static final String DIALOG_PERM_RIGHT = "d_p_right";
	public static final String PERM_TREE = "p_tree";
	/**
	 * 构建对话框视图
	 * @param complexForm
	 * @return
	 */
	public static View buildDialogView(boolean complexForm) {
		View view = new View();
		
		VerticalLayout root = new VerticalLayout(DIALOG_ROOT);
		view.add(root);
		
		HorizontalLayout mainShell = new HorizontalLayout(DIALOG_MAIN_SHELL);
		root.add(mainShell);
		mainShell.setStyle("padding:10px 20px;").setHmin(20).setWmin(40);
		
		String mainId = DIALOG_MAIN;
		Widget<?> main = null;
		if (complexForm) {
			main = new GridLayoutFormPanel(mainId).setScroll(true);
		} else {
			main = new FormPanel(mainId).setScroll(true);
		}
		mainShell.add(main);
		
		HorizontalLayout btnShell = new HorizontalLayout(DIALOG_BUTTON_SHELL).setCls("bg-low");
		root.add(btnShell, "50");
		
		ButtonBar btn = new ButtonBar(DIALOG_BUTTON).setAlign(ButtonBar.ALIGN_RIGHT).setStyle("padding-right:20px;").setWmin(20);
		btnShell.add(btn);
		btn.setPub(new Button(null).setCls("x-btn")).setSpace(5);
		
		return view;
	}
}
