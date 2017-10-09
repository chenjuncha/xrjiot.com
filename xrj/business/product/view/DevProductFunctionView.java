package com.xrj.business.product.view;
	
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.rongji.dfish.ui.AbstractNode;
import com.rongji.dfish.ui.Widget;
import com.rongji.dfish.ui.command.ConfirmCommand;
import com.rongji.dfish.ui.command.JSCommand;
import com.rongji.dfish.ui.command.ReplaceCommand;
import com.rongji.dfish.ui.command.SubmitCommand;
import com.rongji.dfish.ui.form.AbstractInput;
import com.rongji.dfish.ui.form.Select;
import com.rongji.dfish.ui.form.Spinner;
import com.rongji.dfish.ui.form.Text;
import com.rongji.dfish.ui.form.Textarea;
import com.rongji.dfish.ui.form.Validate;
import com.rongji.dfish.ui.helper.FlexGrid;
import com.rongji.dfish.ui.helper.FormPanel;
import com.rongji.dfish.ui.helper.GridLayoutFormPanel;
import com.rongji.dfish.ui.helper.GridPanel;
import com.rongji.dfish.ui.helper.Label;
import com.rongji.dfish.ui.layout.ButtonBar;
import com.rongji.dfish.ui.layout.FrameLayout;
import com.rongji.dfish.ui.layout.HorizontalLayout;
import com.rongji.dfish.ui.layout.VerticalLayout;
import com.rongji.dfish.ui.layout.View;
import com.rongji.dfish.ui.layout.grid.GridColumn;
import com.rongji.dfish.ui.widget.Button;
import com.rongji.dfish.ui.widget.Html;
import com.rongji.dfish.ui.widget.Img;
import com.rongji.dfish.ui.widget.Leaf;
import com.rongji.dfish.ui.widget.PageBar;
import com.rongji.dfish.ui.widget.SubmitButton;
import com.rongji.dfish.ui.widget.Toggle;
import com.rongji.dfish.ui.widget.TreePanel;
import com.xrj.api.dto.DevHardwareVersion;
import com.xrj.api.dto.DevProduct;
import com.xrj.api.dto.DevProductFunction;
import com.xrj.api.em.DataTypeEnum;
import com.xrj.api.em.FunctionStatusEnum;
import com.xrj.api.em.FunctionTypeEnum;
import com.xrj.api.em.ProductStatusEnum;
import com.xrj.api.service.DevProductFunctionService;
import com.xrj.business.core.base.PubConstants;
import com.xrj.business.core.view.PubView;

/**
 * 
 * @author chenjunchao
 *
 */
public class DevProductFunctionView {
	
	/**
	 * @param devProduct 
	 * @param devProduct 
	 * @param date 
	 * @显示所有功能点
	 * 
	**/
    public static View showFunctionPoint(List<List<Widget<?>>> totalList, DevProduct devProduct) {
    	View view = new View();
    	
		view.addCommand("toAdd",PubView.getAjaxCommand("product/functionPoint/add"));
		view.addCommand("selectChange", PubView.getAjaxCommand("product/selectCommand?selectValue=$0"));
		view.addCommand("selectChange2",new JSCommand("if(VM(this).f('functionType').val()==4){VM(this).f('dataType').val(1);VM(this).f('dataType').readonly(true)}else{VM(this).f('dataType').readonly(false)}"));
		view.addCommand("saveFunctionPoint", new SubmitCommand("product/functionPoint/save").setRange("functionPoint"));
		view.addCommand("updateFunctionPoint", new SubmitCommand("product/functionPoint/update?id=$0").setRange("functionPoint"));
		view.addCommand("toUpdate",PubView.getAjaxCommand("product/functionPoint/modify?functionId=$0"));
		view.addCommand("toShow",PubView.getAjaxCommand("product/showPoint?functionId=$0"));
		view.addCommand("toDelete",PubView.getAjaxCommand("product/toDeletePoint?functionId=$0"));
		view.addCommand("toBack",PubView.getAjaxCommand("product/backToAllPoint"));
		view.addCommand("toCaculate",new JSCommand(" var e = VM(this).f('minValue').val(); VM(this).f('increment').val(0-e)"));
		view.addCommand("changespin",PubView.getAjaxCommand("product/functionPoint/changespin?min=$0&max=$1"));
		view.addCommand("toPublish", PubView.getAjaxCommand("product/CheckPublish"));
		
		VerticalLayout verticalLayout = new VerticalLayout("ver");
		VerticalLayout verticalLayout2 = new VerticalLayout("verti").setScroll(true).setHeight("90%");
		
		HorizontalLayout top = new HorizontalLayout("top").setHeight(40);
		top.add(new Html("<h3>产品功能点</h3>"));
		if(ProductStatusEnum.PUBLISHED.getIndex().equals(devProduct.getStatus())){
		top.add(new Button("发布功能点").setStyle("margin-top:5px").setCls("x-btn x-btn-main").setWidth(100).setOn(Button.EVENT_CLICK, "VM(this).cmd('toPublish')"));		
		top.add(new Html("").setWidth(20));
		}
		top.add(new Button("新增功能点").setStyle("margin-top:5px").setCls("x-btn x-btn-main").setWidth(100).setOn(Button.EVENT_CLICK, "VM(this).cmd('toAdd')"));
	    top.add(new Html("").setWidth(20));
		
		verticalLayout.add(top);
		verticalLayout.add(new Html("<hr style='filter:progid:DXImageTransform.Microsoft.Shadow(color:#CCC,direction:145,strength:15)' color='#CCC' width='100%' size='1' />").setHeight(1),"-1");
		
		for(int n = 0; n < totalList.size(); n++) {
	    	GridLayoutFormPanel gridLayoutFormPanel = new GridLayoutFormPanel("").setRowHeight(10);
		    int m = 0;
		    out:
		    for(int i = 0; i < Math.ceil(totalList.get(n).size()/3.0); i++) {
			    for(int j = 0; j < 3; j++) {
		            gridLayoutFormPanel.add(i, j, totalList.get(n).get(m));
		            m++;
		            if(totalList.get(n).size()-1 == m) {
		        	    break out;
		            }
			    }		
		    }
		    gridLayoutFormPanel.add(1, 3, totalList.get(n).get(totalList.get(n).size()-1));
		    gridLayoutFormPanel.setStyle("border-bottom:1px solid #CCC;height:100px;width:100%");
		    verticalLayout2.add(gridLayoutFormPanel);
		}	
		verticalLayout.add(verticalLayout2);
		view.add(verticalLayout);
	    return view;
    }
    /**
     * @param list 
     * @param range12 
     * @新增功能点界面
     * @param maxSerialNo
     * @return
     */
	public static View modifyPoint(List<Widget<?>> list, HorizontalLayout range1) {
		View view = new View();
		VerticalLayout verticalLayout = new VerticalLayout("ver");		
				
		GridLayoutFormPanel gridLayoutFormPanel = new GridLayoutFormPanel("functionPoint");
		gridLayoutFormPanel.setStyle("height:100%");
		gridLayoutFormPanel.add(0, 0, new Toggle("展开/收起"));
		gridLayoutFormPanel.add(1, 0, 1, 1, ((Text) list.get(0)).addValidate(Validate.required().setRequiredtext("显示名称不能为空")));
		gridLayoutFormPanel.add(1, 2, 1, 3,((Text) list.get(1)).addValidate(Validate.pattern("/^[a-zA-Z0-9_]+$/").setPatterntext("标识名由数字,26个英文字母或者下划线组成")).addValidate(Validate.required().setRequiredtext("标识名称不能为空")));
		gridLayoutFormPanel.add(1, 4, 1, 4, list.get(2));
		gridLayoutFormPanel.add(1, 5, 1, 6, list.get(3));
		gridLayoutFormPanel.add(2, 0, 2, 4, range1.setStyle("margin-left:28px"));
				
		gridLayoutFormPanel.add(3, 0, 3, 1, ((Text)list.get(4)).setPlaceholder("后台自动生成"));
		gridLayoutFormPanel.add(3, 2, 3, 5, list.get(5));
		HorizontalLayout btm = new HorizontalLayout("btm");
		btm.add(new SubmitButton("保存").setCls("x-btn x-btn-main").setOn(SubmitButton.EVENT_CLICK, "VM(this).cmd('saveFunctionPoint')").setWidth(80));
		btm.add(new Html("").setWidth(20));
		btm.add(new Button("取消").setCls("x-btn").setOn(Button.EVENT_CLICK, "VM(this).cmd('toBack')").setWidth(80));
		gridLayoutFormPanel.add(4, 3,4,5, btm);

		HorizontalLayout top = new HorizontalLayout("top").setHeight(40);
		top.add(new Html("<h3>新增产品功能点</h3>"));
		verticalLayout.add(top);
		verticalLayout.add(new Html("<hr style='filter:progid:DXImageTransform.Microsoft.Shadow(color:#CCC,direction:145,strength:15)' color='#CCC' width='100%' size='1' />").setHeight(1),"-1");
		verticalLayout.add(gridLayoutFormPanel);
		view.add(verticalLayout);
		return view;
	}	
	
	
   /**
    * @更新某个功能点
    * @param functionPoint
    * @return
    */
	public static View updatePoint(List<Widget<?>> list,HorizontalLayout range1,DevProductFunction functionPoint) {
		// TODO Auto-generated method stub
		View view = new View();
		VerticalLayout verticalLayout = new VerticalLayout("ver");
		
		GridLayoutFormPanel gridLayoutFormPanel = new GridLayoutFormPanel("functionPoint");
		gridLayoutFormPanel.setStyle("height:100%");
		gridLayoutFormPanel.add(0, 0, new Toggle("展开/收起"));
		gridLayoutFormPanel.add(1, 0, 1, 1, ((Text) list.get(0)).addValidate(Validate.required().setRequiredtext("显示名称不能为空")));
		gridLayoutFormPanel.add(1, 2, 1, 3,((Text) list.get(1)).addValidate(Validate.pattern("/^[a-zA-Z0-9_]+$/").setPatterntext("标识名由数字,26个英文字母或者下划线组成")).addValidate(Validate.required().setRequiredtext("标识名称不能为空")));
		gridLayoutFormPanel.add(1, 4, 1, 4, list.get(2));
		gridLayoutFormPanel.add(1, 5, 1, 6, list.get(3));
		gridLayoutFormPanel.add(2, 0, 2, 4, range1.setStyle("margin-left:28px"));
				
		gridLayoutFormPanel.add(3, 0, 3, 1, list.get(4));
		gridLayoutFormPanel.add(3, 2, 3, 5, list.get(5));
		HorizontalLayout btm = new HorizontalLayout("btm");
		btm.add(new SubmitButton("保存").setCls("x-btn x-btn-main").setOn(SubmitButton.EVENT_CLICK, "VM(this).cmd('updateFunctionPoint','"+functionPoint.getId()+"')").setWidth(80));
		btm.add(new Html("").setWidth(20));
		btm.add(new Button("取消").setCls("x-btn").setOn(SubmitButton.EVENT_CLICK, "VM(this).cmd('toBack')").setWidth(80));
		gridLayoutFormPanel.add(4, 3,4,5, btm);
		
		HorizontalLayout top = new HorizontalLayout("top").setHeight(40);
		top.add(new Html("<h3>修改产品功能点</h3>"));
		verticalLayout.add(top);
		verticalLayout.add(new Html("<hr style='filter:progid:DXImageTransform.Microsoft.Shadow(color:#CCC,direction:145,strength:15)' color='#CCC' width='100%' size='1' />").setHeight(1),"-1");
		verticalLayout.add(gridLayoutFormPanel);
		view.add(verticalLayout);
		return view;
	}
	
	/**
	 * @查看单个功能点
	 * @param list
	 * @return
	 */
	public static View showPoint(List<Widget<?>> list) {
		View view = new View();
		VerticalLayout verticalLayout = new VerticalLayout("ver");
		
		GridLayoutFormPanel gridLayoutFormPanel = new GridLayoutFormPanel("functionPoint");
		int m = 0;
		for(int i = 0; i < Math.ceil(list.size()/3.0); i++) {
			for(int j = 0; j < 3; j++) {
		        gridLayoutFormPanel.add(i, j, list.get(m));
		        m++;
		        if(list.size() == m) {
		        	break;
		        }
			}
		}
		HorizontalLayout top = new HorizontalLayout("top").setHeight(40);
		top.add(new Html("<h3>查看产品功能点</h3>"));
		top.add(new Button("返回").setStyle("margin-top:5px").setCls("x-btn").setWidth(80).setOn(Button.EVENT_CLICK, "VM(this).cmd('toBack')"));
		top.add(new Html("").setWidth(20));
		verticalLayout.add(top);
		verticalLayout.add(new Html("<hr style='filter:progid:DXImageTransform.Microsoft.Shadow(color:#CCC,direction:145,strength:15)' color='#CCC' width='100%' size='1' />").setHeight(1),"-1");
		verticalLayout.add(gridLayoutFormPanel);
		view.add(verticalLayout);
		return view;
	}	  
	    
	     //数值(下面都是用来替换的控件)
		public static View SelectChangeView0()
		{
			View view = new View();
			Html labelData = new Html("<span style='color:red;'>*</span>数据范围:");
			Html labelReso = new Html("<span style='color:red;'>*</span>分辨率:");
			HorizontalLayout range1 = new HorizontalLayout("range1").setStyle("margin-left:28px");
			range1.add(labelData.setStyle("padding-left:25px").setWidth(93));
			range1.add(new Spinner("minValue", "", null, -99999, 99999, 1).setOn(Spinner.EVENT_BLUR, "VM(this).cmd('changespin',''+VM(this).f('minValue').val()+'',''+VM(this).f('maxValue').val()+'')").setOn(Spinner.EVENT_CHANGE, "VM(this).cmd('toCaculate')").setRequired(true).setWidth(90).addValidate(Validate.pattern("/^-?\\d+\\.?\\d{0,2}$/").setPatterntext("最小值最多保留两位")));
			range1.add(new Html("~").setWidth(10));
			range1.add(new Spinner("maxValue", "", null, -99999, 99999, 1).setOn(Spinner.EVENT_BLUR, "VM(this).cmd('changespin',''+VM(this).f('minValue').val()+'',''+VM(this).f('maxValue').val()+'')").setWidth(90).setRequired(true).addValidate(Validate.pattern("/^-?\\d+\\.?\\d{0,2}$/").setPatterntext("最大值最多保留两位")).addValidate(Validate.compare(">", "minValue").setComparetext("最大值必须大于最小值")));
			range1.add(new Label("","增量:").setStyle("padding-left:100px;padding-top:5px").setWidth(30));
			range1.add(new Text("increment","增量","").setReadonly(true).setId("increment"));	
			range1.add(new Html(""));
			range1.add(labelReso.setStyle("padding-top:5px").setWidth(50));
			range1.add(new Spinner("resolution", "", null, 0, 99999, 0.001).setId("resolution").setRequired(true).setWidth(90).addValidate(Validate.required().setRequiredtext("分辨率不能为空")).addValidate(Validate.pattern("/^[+]{0,1}(\\d+)$|^[+]{0,1}(\\d+\\.\\d+)$/").setPatterntext("分辨率不能为0")).addValidate(Validate.pattern("/^-?\\d+\\.?\\d{0,3}$/").setPatterntext("分辨率最多保留三位")));
			range1.add(new Html("").setWidth("60%"));
			view.add(range1);
			return view;
		}
		//布尔
		public static View SelectChangeView1()
		{
			View view = new View();
			HorizontalLayout range1 = new HorizontalLayout("range1");			
			view.add(range1);
			return view;
		}
		//故障
		public static View SelectChangeView2()
		{
			View view = new View();
			HorizontalLayout range1 = new HorizontalLayout("range1");			
			view.add(range1);
			return view;
		}
		//枚举
		public static View SelectChangeView3()
		{
			View view = new View();
			Html labelEnum = new Html("<span style='color:red;'>*</span>枚举范围:");
			HorizontalLayout range1 = new HorizontalLayout("range1").setStyle("margin-left:28px");
			range1.add(labelEnum.setStyle("padding-left:25px").setWidth(93));
			range1.add(new Text("enumeration","<span style='color:red;'>*</span> ","").setWidth("28.6%").addValidate(Validate.pattern("/^[.\\w\\u4e00-\\u9fa5]+(,[.\\w\\u4e00-\\u9fa5]+)*$/").setPatterntext("枚举范围由中文,英文组成且以逗号隔开")).addValidate(Validate.required().setRequiredtext("枚举范围不能为空")));			
			view.add(range1);
			return view;
		}
		//字符
		public static View SelectChangeView4()
		{
			View view = new View();
			Html labelLength = new Html("<span style='color:red;'>*</span>数据长度:");
			HorizontalLayout range1 = new HorizontalLayout("range1").setStyle("margin-left:28px");
			range1.add(labelLength.setStyle("padding-left:25px").setWidth(93));
			range1.add(new Text("length","<span style='color:red;'>*</span> ","").setId("length").setWidth("28.6%").addValidate(Validate.required().setRequiredtext("长度不能为空")).addValidate(Validate.pattern("/^\\d+$/").setPatterntext("长度必须是自然数")));		
			view.add(range1);
			return view;
		}
		//分辨率
		public static View SelectChangeResolution(double min, double max)
		{
			View view = new View();
			Spinner spi = new Spinner("resolution", "", null, min, max, 0.001).setId("resolution").setRequired(true).setWidth(90).addValidate(Validate.required().setRequiredtext("分辨率不能为空")).addValidate(Validate.pattern("/^-?\\d+\\.?\\d{0,3}$/").setPatterntext("分辨率最多保留三位"));
			view.add(spi);
			return view;
		}
		
		/**
		 * 固件添加新版本或编辑版本 界面
		 * 
		 * @param request
		 * @return 视图
		 */
		public static View buildSDKView() {

			View view = PubView.buildDialogView();
			FlexGrid baseForm = new FlexGrid("SDKForm");
			baseForm.setId(PubView.DIALOG_MAIN);
			
			baseForm.add("SDK版本名称",3);
			baseForm.add(new Text("ww","","").addValidate(Validate.required().setRequiredtext("必填")),8);
			baseForm.add("SDK版本号",3);
			baseForm.add(new Text("ee","","").addValidate(Validate.required().setRequiredtext("必填")),8);
			
			view.replaceNodeById(baseForm);
			
			ButtonBar btn = (ButtonBar) view.findNodeById(PubView.DIALOG_BUTTON);
			btn.add(new SubmitButton("保存").setOn(Button.EVENT_CLICK, "this.cmd('publish')").setCls("x-btn x-btn-main"));
			btn.add(new Button("取消").setOn(Button.EVENT_CLICK, PubConstants.CMD_DLG_CLOSE));

			view.addCommand("publish", PubView.getSubmitCommand("product/publishPoint"));

			return view;
		}
	/*public static View buildAllView(String anchor){
		View view=new View();
		VerticalLayout verticalLayout = new VerticalLayout("all_ver").setCls("bg-index");
		//view.add(verticalLayout);
		view.setNode(verticalLayout);
		HorizontalLayout top =new HorizontalLayout("top").setCls("bg-top").setHeight(50);
		top.add(new Img("m/index/images/logo.png"));
		top.add(new Html("").setWidth(200));
		top.add(new Html("").setWidth(200));
//		HorizontalLayout top = setPositionLayout(0, 10, new Img("m/index/img/logo-white.png"))
//				.setCls("bg_top").setHeight(50);
		View treeView = buildCommonView(anchor);
		HorizontalLayout content = new HorizontalLayout("all_main").setStyle("background-color: #f5f5f5");
		content.add(new Html("").setWidth(100));
		content.add(treeView);
		content.add(new Html("").setWidth(100));
		
		verticalLayout.add(top);
		verticalLayout.add(content);
		//verticalLayout.add(new Html("").setHeight(50));
		return view;	
	}
	
	/*public static View buildCommonView(String anchor){
		View view = PubView.buildBodyView().setCls("comm");
		TreePanel menuTree= (TreePanel) view.findNodeById(PubView.BODY_MENU);
		Leaf topLeaf = new Leaf().setText("开发者管理").setFocus(false);
		menuTree.add(topLeaf);
		VerticalLayout mainLayout=(VerticalLayout) view.findNodeById(PubView.MAIN_DISPLAY);
		FrameLayout mainContent = new FrameLayout("mainContent");
		mainLayout.add(mainContent).setCls("bg-white");
//		Split split = new Split();
//		mainLayout.add(split);
//		mainContent.add(split);
		List<Menu> menuList=getMenuData();
		Menu focusMenu=menuList.get(0);
		//menuTree.add("<h2>开发者管理</h2>");
		for(Menu menu:menuList){
			Leaf leaf= new Leaf(menu.getMenuId(),menu.getMenuName());
			//leaf.setOn(Leaf.EVENT_CLICK,"window.open('" + menu.getMenuUrl() + "');");
			menuTree.add(leaf);
			String viewId = "v_" + menu.getMenuId();
			View contentView = new View(viewId).setSrc(menu.getMenuUrl());
			mainContent.add(contentView);
			if(anchor!=null&&menu.getMenuId() .equals(anchor)){
				focusMenu=menu;
				leaf.setFocus(true);
			}
			leaf.setOn(Leaf.EVENT_CLICK, "window.location.href='index.jsp#"+menu.getMenuId()+"';"+
					"this.cmd('clickMenu','" + menu.getMenuId() + "');");
		}
		//window.location.href='index.jsp#info';this.cmd('clickMenu','info');
		mainContent.setDft("v_"+focusMenu.getMenuId());
		view.addCommand("clickMenu", PubView.getAjaxCommand("developercertification/clickMenu?menuId=$0"));
		
		return view;
	}*/
	
	/*public static List<Menu> getMenuData() {
		List<Menu> menuData = new ArrayList<Menu>();
		menuData.add(new Menu( TreeConstans.MOD_ADMIN_INF, "账号信息", "developercertification/information"));
		menuData.add(new Menu( TreeConstans.MOD_ADMIN_SAFE, "账号安全", "accountSecurity/security"));
		menuData.add(new Menu( TreeConstans.MOD_ADMIN_AUTHEN, "实名认证", "enterpriseAuthen/authen"));
		return menuData;
	}
	
	public static class Menu{
		private String menuId;
		private String menuName;
		private String menuUrl;
		public Menu() {
			// TODO Auto-generated constructor stub
		}
		public Menu(String menuId, String menuName, String menuUrl) {
			super();
			this.setMenuId(menuId);
			this.setMenuName(menuName);
			this.setMenuUrl(menuUrl);
		}
		public String getMenuId() {
			return menuId;
		}
		public void setMenuId(String menuId) {
			this.menuId = menuId;
		}
		public String getMenuName() {
			return menuName;
		}
		public void setMenuName(String menuName) {
			this.menuName = menuName;
		}
		public String getMenuUrl() {
			return menuUrl;
		}
		public void setMenuUrl(String menuUrl) {
			this.menuUrl = menuUrl;
		}
		
	}
	
	public static View buildCertificationTelAndIDView() {
		
		View view = new View();

		//view.addCommand("submitTelAndID",new SubmitCommand("developercertification/waiting").setId("submitTelAndID"));

		HorizontalLayout horizontalLayout = new HorizontalLayout("").setAlign(HorizontalLayout.ALIGN_CENTER).setValign(HorizontalLayout.VALIGN_MIDDLE);
		view.add(horizontalLayout);

		horizontalLayout.add(new Html(""));

		VerticalLayout verticalLayout = new VerticalLayout("").setValign(VerticalLayout.VALIGN_BOTTOM);
		horizontalLayout.add(verticalLayout);

		verticalLayout.add(new Html(""));

		FormPanel formPanel = new FormPanel("form");
		verticalLayout.add(formPanel);

		formPanel.add(new Text("telePhone", "手机", "").setWidth(210));

		formPanel.add(new Text("ID", "身份证", "").setWidth(210));

		HorizontalLayout horizontalLayout2 = new HorizontalLayout("");
		formPanel.add(horizontalLayout2);

		horizontalLayout2.add(new Html(""));

		horizontalLayout2.add(new SubmitButton("下一步").setCls("btn").setOn(Button.EVENT_CLICK, "this.cmd({type:'ajax',src:'developercertification/waiting'});"));

		horizontalLayout2.add(new Html(""));

		verticalLayout.add(new Html(""));

		horizontalLayout.add(new Html(""));
		
		return view;

	}
	
	public static View waitingCertification(String telePhone, String ID) {
		View view = new View();

		view.addCommand("modify",new SubmitCommand("developercertification/certification").setId("modify"));

		HorizontalLayout horizontalLayout = new HorizontalLayout("");
		view.add(horizontalLayout);

		horizontalLayout.add(new Html(""));

		VerticalLayout verticalLayout = new VerticalLayout("");
		horizontalLayout.add(verticalLayout);

		verticalLayout.add(new Html(""));

		FormPanel formPanel = new FormPanel("form");
		verticalLayout.add(formPanel);

		formPanel.add(new Label("状态:", "待审核"));

		formPanel.add(new Label("手机:", ""+telePhone+""));

		formPanel.add(new Label("身份证:", ""+ID+""));

		HorizontalLayout horizontalLayout2 = new HorizontalLayout("");
		formPanel.add(horizontalLayout2);

		horizontalLayout2.add(new Html(""));

		horizontalLayout2.add(new Button("修改").setOn(Button.EVENT_CLICK, "VM(this).cmd('modify')"));

		horizontalLayout2.add(new Html(""));

		verticalLayout.add(new Html(""));

		horizontalLayout.add(new Html(""));
		
		return view;

	}
*/
	
}
