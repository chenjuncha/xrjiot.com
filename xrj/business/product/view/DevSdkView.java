package com.xrj.business.product.view;

import java.util.ArrayList;
import java.util.List;

import com.rongji.dfish.base.util.DateUtil;
import com.rongji.dfish.ui.helper.GridPanel;
import com.rongji.dfish.ui.layout.HorizontalLayout;
import com.rongji.dfish.ui.layout.VerticalLayout;
import com.rongji.dfish.ui.layout.View;
import com.rongji.dfish.ui.layout.grid.GridColumn;
import com.rongji.dfish.ui.layout.grid.Tr;
import com.rongji.dfish.ui.widget.Html;

public class DevSdkView {
	private static final String SDK_ROOT = "sdkRoot";
	private static final String SDK_PANL = "sdkPanl";
	
	public static View buildSdkView(){
		View view = new View();
		VerticalLayout root = new VerticalLayout(SDK_ROOT);
		view.add(root);
		
		HorizontalLayout top = new HorizontalLayout("top").setHeight(40);
		top.add(new Html("<h3>SDK管理</h3>"));
		root.add(top);
		root.add(new Html(
				"<hr style='filter:progid:DXImageTransform.Microsoft.Shadow(color:#CCC,direction:145,strength:15)' color='#CCC' width='100%' size='1' />")
						.setHeight(1),
				"-1");
		root.add(buildSdkPanl());
		return view;
	}
	
	public static GridPanel buildSdkPanl(){
		GridPanel gridPanel = new GridPanel(SDK_PANL);
		gridPanel.setWmin(20).setCls("x-grid-odd").setFace(GridPanel.FACE_LINE).setScroll(true).setNobr(false)
		.setFocusable(true);
		gridPanel.getPrototype(false).getThead().setCls("x-grid-head");
		gridPanel.setPub(new Tr().setHeight("40").setCls("w-td-line w-th"));
		List<Object[]> list = new ArrayList<Object[]>();
		int id=1;
		list.add(new Object[]{id++,"开发板 V1.0","2017-08-17 10:35:15","123kb",""});
		list.add(new Object[]{id++,"开发板 V2.0","","",""});
		int column=0;
		gridPanel.addColumn(GridColumn.text(column++, "id","序号", "-1"));
		gridPanel.addColumn(GridColumn.text(column++, "version","版本", "-1"));
		gridPanel.addColumn(GridColumn.text(column++, "createTime","生成时间", "-1"));
		gridPanel.addColumn(GridColumn.text(column++, "createNum","生成大小", "-1"));
		gridPanel.addColumn(GridColumn.text(column++, "oper","操作", "15%").setFormat("javascript:var createTime = $createTime;if(createTime==null){return \"<a href='#'>生成SDK</a>\"}"
				+ "return \"<a href='#'>生成SDK</a>&nbsp&nbsp<a href='#'>下载</a>\""));
		gridPanel.setGridData(list);
		return gridPanel;
		
	}
}
