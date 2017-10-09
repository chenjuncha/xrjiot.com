package com.xrj.business.core.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.CollectionUtils;

import com.rongji.dfish.base.Page;
import com.rongji.dfish.framework.FilterParam;
import com.rongji.dfish.ui.form.Text;
import com.rongji.dfish.ui.helper.FlexGrid;
import com.rongji.dfish.ui.helper.GridLayoutFormPanel;
import com.rongji.dfish.ui.helper.GridPanel;
import com.rongji.dfish.ui.helper.Label;
import com.rongji.dfish.ui.layout.ButtonBar;
import com.rongji.dfish.ui.layout.HorizontalLayout;
import com.rongji.dfish.ui.layout.VerticalLayout;
import com.rongji.dfish.ui.layout.View;
import com.rongji.dfish.ui.layout.grid.GridColumn;
import com.rongji.dfish.ui.layout.grid.Tr;
import com.rongji.dfish.ui.widget.Button;
import com.rongji.dfish.ui.widget.Html;
import com.rongji.dfish.ui.widget.PageBar;
import com.xrj.api.dto.DevAudit;
import com.xrj.api.dto.DevDeveloper;
import com.xrj.api.dto.DevProduct;
import com.xrj.api.em.AuditStatusEnum;
import com.xrj.api.em.DataDictEnum;
import com.xrj.business.core.base.PubService;
import com.xrj.framework.util.DateUtils;

/**
 * 管理域视图
 * 
 * @author chenyun
 * @date 2017-07-25
 */
public class ManagerView {
	
	/**
	 * 管理域产生审核类型查询页面
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static View buildProductListView(HttpServletRequest request, Page dfishPage){
	
		FilterParam fp = PubService.getParam(request);
		View view = PubView.buildMainView(false).setStyle("margin:8px 12px 8px 12px;");
		Html mainTitle = (Html) view.findNodeById(PubView.MAIN_TITLE);
		mainTitle.setText("产品管理");
		ButtonBar topBtn = (ButtonBar) view.findNodeById(PubView.BODY_MAIN_OPER);
		Button searchBtn = new Button("搜索");
		searchBtn.setOn(Button.EVENT_CLICK, "VM(this).cmd('search');");
		topBtn.add(searchBtn);
		
		//--------------------------查询条件--------------------------------------------------
		GridLayoutFormPanel mainSearch = (GridLayoutFormPanel) view.findNodeById(PubView.BODY_MAIN_SEARCH);
		mainSearch.setHeight("50");
		int rowIndex = 0;
		
		mainSearch.add(rowIndex, 0, PubView.buildCodeTypeSelector("auditStatus", DataDictEnum.audit_status.toString(), "审核状态", null, null));
		mainSearch.add(rowIndex, 1, new Text("likeInfo", "", "").setPlaceholder("请输入产品名称或开发者"));
		
		GridPanel mainGrid = (GridPanel) view.findNodeById(PubView.BODY_MAIN_CONTENT).setHeight(350);
		
		Map<String, String> auditStatusMap = PubService.getDataDictMap(DataDictEnum.audit_status.toString());
		
		//查询结果集
		List<Map<String, Object>> productList = dfishPage.getResultSet();
		List<Object[]> gridData = new ArrayList<Object[]>();
		if(!CollectionUtils.isEmpty(productList)){
			for (Map<String, Object> devProduct : productList) {
					gridData.add(new Object[]{
							devProduct.get("ID"), 
							devProduct.get("AUDIT_ID"), 
							auditStatusMap.get(devProduct.get("AUDIT_STATUS")), 
							devProduct.get("PRODUCT_NAME"),  
							devProduct.get("USER_NAME"), 
							devProduct.get("APPLICATION_TIME")
					});
			}
		}
		
		int columnIndex = 0;
		mainGrid.addColumn(GridColumn.hidden(columnIndex++, "id"));
		mainGrid.addColumn(GridColumn.hidden(columnIndex++, "auditId"));
		mainGrid.addColumn(GridColumn.text(columnIndex++, "auditStatus", "审核状态", "150").setFormat("<a onclick=\"VM(this).cmd('auditDetail','$id');\">$auditStatus</a>"));
		mainGrid.addColumn(GridColumn.text(columnIndex++, "productName", "产品名称", "300"));
		mainGrid.addColumn(GridColumn.text(columnIndex++, "userName", "所属开发者", "150"));
		mainGrid.addColumn(GridColumn.text(columnIndex++, "applicationTime", "申请时间", "150").setDataFormat("yyyy年M月d日 HH:mm:ss"));
	
		mainGrid.setGridData(gridData);
		
		PageBar mainPage = (PageBar) view.findNodeById(PubView.MAIN_PAGE);
		mainPage.setCurrentpage(dfishPage.getCurrentPage());
		mainPage.setSumpage(dfishPage.getPageCount());
		mainPage.setSrc("manager/productSearch?cp=$0"+fp);
		
		view.addCommand("search", PubView.getSubmitCommand("manager/productSearch?cp=$0"));
		view.addCommand("auditDetail", PubView.getAjaxCommand("manager/productAuditDetail?id=$0"));
	
		return view;
	}
	
	/**
	 * 管理域产生审核类型查询页面
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static View buildDeveloperListView(HttpServletRequest request, Page dfishPage){
		FilterParam fp = PubService.getParam(request);
		View view = PubView.buildMainView(false).setStyle("margin:8px 12px 8px 12px;");
		Html mainTitle = (Html) view.findNodeById(PubView.MAIN_TITLE);
		mainTitle.setText("开发者管理");
		ButtonBar topBtn = (ButtonBar) view.findNodeById(PubView.BODY_MAIN_OPER);
		Button searchBtn = new Button("搜索");
		searchBtn.setOn(Button.EVENT_CLICK, "VM(this).cmd('search');");
		topBtn.add(searchBtn);
		
		//--------------------------查询条件--------------------------------------------------
		GridLayoutFormPanel mainSearch = (GridLayoutFormPanel) view.findNodeById(PubView.BODY_MAIN_SEARCH);
		mainSearch.setHeight("50");//设置搜索区的高度
		int rowIndex = 0;
		
		mainSearch.add(rowIndex, 0, PubView.buildCodeTypeSelector("auditStatus", DataDictEnum.audit_status.toString(), "审核状态：", null, null));
		mainSearch.add(rowIndex, 1, new Text("likeInfo", "", "").setPlaceholder("请输入名称、联系人、手机号、账号"));
		
		GridPanel mainGrid = (GridPanel) view.findNodeById(PubView.BODY_MAIN_CONTENT).setHeight(350);
		Map<String, String> developerTypeMap = PubService.getDataDictMap(DataDictEnum.developer_type.toString());
		Map<String, String> auditStatusMap = PubService.getDataDictMap(DataDictEnum.audit_status.toString());
		
		//查询结果集
		List<Map<String, Object>> developerList = dfishPage.getResultSet();
		List<Object[]> gridData = new ArrayList<Object[]>();
		if(!CollectionUtils.isEmpty(developerList)){
			for (Map map : developerList) {
					gridData.add(new Object[]{
							map.get("ID"), 
							map.get("AUDIT_ID"), 
							auditStatusMap.get(map.get("AUDIT_STATUS")), 
							map.get("USER_NAME"), 
							map.get("CONTACT_NAME"), 
							map.get("PHONE"),
							map.get("LOGIN_NAME"),
							developerTypeMap.get(map.get("USER_TYPE")),
							map.get("APPLICATION_TIME")
					});
			}
		}
		
		int columnIndex = 0;
		mainGrid.addColumn(GridColumn.hidden(columnIndex++, "id"));
		mainGrid.addColumn(GridColumn.hidden(columnIndex++, "auditId"));
		mainGrid.addColumn(GridColumn.text(columnIndex++, "auditStatus", "审核状态", "120").setFormat("<a onclick=\"VM(this).cmd('auditDetail','$id');\">$auditStatus</a>"));
		mainGrid.addColumn(GridColumn.text(columnIndex++, "userName", "名称", "150"));
		mainGrid.addColumn(GridColumn.text(columnIndex++, "contactName", "联系人", "150"));
		mainGrid.addColumn(GridColumn.text(columnIndex++, "phone", "手机号", "150"));
		mainGrid.addColumn(GridColumn.text(columnIndex++, "loginName", "账号", "150"));
		mainGrid.addColumn(GridColumn.text(columnIndex++, "userType", "角色", "120"));
		mainGrid.addColumn(GridColumn.text(columnIndex++, "applicationTime", "申请时间", "150").setDataFormat("yyyy年M月d日 HH:mm:ss"));
	
		mainGrid.setGridData(gridData);
		
		PageBar mainPage = (PageBar) view.findNodeById(PubView.MAIN_PAGE);
		mainPage.setCurrentpage(dfishPage.getCurrentPage());
		mainPage.setSumpage(dfishPage.getPageCount());
		mainPage.setSrc("manager/developerSearch?cp=$0"+fp);
		
		view.addCommand("search", PubView.getSubmitCommand("manager/developerSearch?cp=$0"));
		view.addCommand("auditDetail", PubView.getAjaxCommand("manager/developerAuditDetail?id=$0"));
		
		return view;
	}
	
	public static View buildDeveloperAuditDialog(HttpServletRequest request, DevDeveloper devDeveloper, List<DevAudit> devAuditList){
		DevAudit currentAudit = devAuditList.get(0);
		View view = new View();

		VerticalLayout root = new VerticalLayout(PubView.DIALOG_ROOT).setStyle("margin:8px 12px 8px 12px;");
		view.add(root);
		
		HorizontalLayout btnShell = new HorizontalLayout(PubView.DIALOG_BUTTON_SHELL).setAlign(HorizontalLayout.ALIGN_RIGHT).setCls("bg-low");
		root.add(btnShell, "50");
		
		Map<String, String> auditStatusMap = PubService.getDataDictMap(DataDictEnum.audit_status.toString());
		Map<String, String> areaCodeMap = PubService.getDataDictMap(DataDictEnum.area_code.toString());
		Html statusHtml = new Html("状态："+auditStatusMap.get(currentAudit.getAuditStatus())).setId("statusHtml").setCls("x-main-title").setHeight("40");
		root.add(statusHtml);
		
		ButtonBar btnBar = buildAuditDeveloperButtonBar(currentAudit);
		btnShell.add(btnBar);

		HorizontalLayout mainShell = new HorizontalLayout(PubView.DIALOG_MAIN_SHELL).setHeight("250");
		root.add(mainShell);
		mainShell.setStyle("padding:10px 10px;").setHmin(40).setWmin(40);

		String mainId = PubView.DIALOG_MAIN;
		FlexGrid form = new FlexGrid(mainId);
		mainShell.add(form);
		form.addHidden("id", devDeveloper.getId());
		form.add("账号：", 2);
		form.add(new Text("loginName", "账号", devDeveloper.getLoginName()).setReadonly(true),4);
		form.add("联系人姓名：", 2);
		form.add(new Text("contactName", "联系人姓名", devDeveloper.getContactName()).setReadonly(true),4);
		form.add("用户名：", 2);
		form.add(new Text("userName", "用户名", devDeveloper.getUserName()).setReadonly(true),4);
		form.add("QQ：", 2);
		form.add(new Text("qq", "QQ", devDeveloper.getQq()).setReadonly(true),4);
		form.add("手机：", 2);
		form.add(new Text("phone", "手机", devDeveloper.getPhone()).setReadonly(true),4);
		form.add("身份证：", 2);
		form.add(new Text("idcard", "身份证", devDeveloper.getIdcard()).setReadonly(true),4);
		form.add("所在地区:", 2);
		form.add(new Text("idcard", "所在地区", PubService.getAreaCodeAllName(areaCodeMap, devDeveloper.getRegisterAddr())).setReadonly(true),10);
		form.add("地区：", 2);
		form.add(new Text("addr", "地区", devDeveloper.getAddr()).setReadonly(true),10);
		
		root.add(new Html("审核历史:").setCls("x-main-title").setHeight("30"));
		
		HorizontalLayout gridHor = new HorizontalLayout(PubView.DIALOG_MAIN_SHELL).setAlign(HorizontalLayout.ALIGN_CENTER);
		root.add(gridHor);
		
		GridPanel mainGrid = buildGridAuditList(devAuditList);
		gridHor.add(mainGrid);
		
		view.addCommand("developerAudit", PubView.getAjaxCommand("manager/developerAudit?developerId=$0&auditId=$1&auditStatus=$2&auditContent=$3"));
		view.addCommand("reAuditDeveloper", PubView.getAjaxCommand("manager/reAuditDeveloper?developerId=$0&auditId=$1"));
		return view;
	}
	
	public static View buildProductAuditDialog(HttpServletRequest request, DevProduct devProduct, List<DevAudit> devAuditList){
		DevAudit currentAudit = devAuditList.get(0);
		View view = new View();

		VerticalLayout root = new VerticalLayout(PubView.DIALOG_ROOT).setStyle("margin:8px 12px 8px 12px;");
		view.add(root);
		
		HorizontalLayout btnShell = new HorizontalLayout(PubView.DIALOG_BUTTON_SHELL).setAlign(HorizontalLayout.ALIGN_RIGHT).setCls("bg-low");
		root.add(btnShell, "50");
		Map<String, String> auditStatusMap = PubService.getDataDictMap(DataDictEnum.audit_status.toString());
		Map<String, String> productTypeMap = PubService.getDataDictMap(DataDictEnum.product_type.toString());
		Map<String, String> productClassificationMap = PubService.getDataDictMap(DataDictEnum.product_classification.toString());
		Html statusHtml = new Html("状态："+auditStatusMap.get(currentAudit.getAuditStatus())).setCls("x-main-title").setId("statusHtml").setStyle("margin:10px 6px 8px 6px;").setHeight("40");
		root.add(statusHtml);
		
		ButtonBar btnBar = buildAuditProductButtonBar(currentAudit);
		btnShell.add(btnBar);

		HorizontalLayout mainShell = new HorizontalLayout(PubView.DIALOG_MAIN_SHELL).setHeight("160");
		root.add(mainShell);
		mainShell.setStyle("padding:10px 10px;").setHmin(40).setWmin(40);

		String mainId = PubView.DIALOG_MAIN;
		FlexGrid form = new FlexGrid(mainId);
		mainShell.add(form);
		form.addHidden("id", devProduct.getId());
		form.add("产品名称：", 2);
		form.add(new Label(null, devProduct.getProductName()),4);
		form.add("产品分类：", 2);
		form.add(new Label(null, PubService.getDataDictAllName(productClassificationMap, devProduct.getProductClassification())),4);
		form.add("产品类型：", 2);
		form.add(new Label(null, productTypeMap.get(devProduct.getProductType())),4);
		form.add("创建时间：", 2);
		form.add(new Label(null, DateUtils.formatDate(devProduct.getCreateTime(), "yyyy年M月d日 HH:mm:ss")),4);
		form.add("产品定价：", 2);
		form.add(new Label(null, ""+devProduct.getPricing()),4);
		form.add("设备数量：", 2);
		form.add(new Label(null, ""+devProduct.getQuantity()),4);
		form.add("所属开发者：", 2);
		form.add(new Label(null, devProduct.getDeveloperName()),4);
		form.add("用途：", 2);
		form.add(new Label(null, devProduct.getProductUse()),4);
		
		
		root.add(new Html("审核历史：").setCls("x-main-title").setHeight("40"));
		
		HorizontalLayout gridHor = new HorizontalLayout(PubView.DIALOG_MAIN_SHELL).setAlign(HorizontalLayout.ALIGN_CENTER);
		root.add(gridHor);
		
		GridPanel mainGrid = buildGridAuditList(devAuditList);
		gridHor.add(mainGrid);
		
		view.addCommand("productAudit", PubView.getAjaxCommand("manager/productAudit?productId=$0&auditId=$1&auditStatus=$2&auditContent=$3"));
		view.addCommand("reAuditProduct", PubView.getAjaxCommand("manager/reAuditProduct?productId=$0&auditId=$1"));
		return view;
	}
	
	public static GridPanel buildGridAuditList(List<DevAudit> devAuditList){
		GridPanel mainGrid = new GridPanel("devAuditGrid").setScroll(true);
		mainGrid.getPrototype(false).getThead().setCls("x-grid-head");
		mainGrid.setPub(new Tr().setHeight("40"));
		Map<String, String> auditStatusMap = PubService.getDataDictMap(DataDictEnum.audit_status.toString());
		if(!CollectionUtils.isEmpty(devAuditList)){
			List<Object[]> gridData = new ArrayList<Object[]>();
			for (DevAudit devAudit : devAuditList) {
				gridData.add(new Object[] { 
						devAudit.getId(),
						devAudit.getAuditorName(),
						auditStatusMap.get(devAudit.getAuditStatus()), 
						devAudit.getAuditTime(),
						devAudit.getAuditContent() });
			}

			int columnIndex = 0;
			mainGrid.addColumn(GridColumn.hidden(columnIndex++, "id").setAlign(GridColumn.ALIGN_CENTER));
			mainGrid.addColumn(GridColumn.text(columnIndex++, "auditor", "审核人", "120").setAlign(GridColumn.ALIGN_CENTER));
			mainGrid.addColumn(GridColumn.text(columnIndex++, "auditStatus", "审核结果", "120").setAlign(GridColumn.ALIGN_CENTER));
			mainGrid.addColumn(GridColumn.text(columnIndex++, "auditTime", "审核时间", "120").setDataFormat("yyyy年M月d日 HH:mm:ss").setAlign(GridColumn.ALIGN_CENTER));
			mainGrid.addColumn(GridColumn.text(columnIndex++, "auditContent", "原因", "150").setAlign(GridColumn.ALIGN_CENTER).setAlign(GridColumn.ALIGN_CENTER));
			mainGrid.setGridData(gridData);
		}
		return mainGrid;
	}
	
	public static ButtonBar buildAuditDeveloperButtonBar(DevAudit currentDevAudit){
		
		ButtonBar btnBar = new ButtonBar(PubView.DIALOG_BUTTON).setAlign(ButtonBar.ALIGN_RIGHT).setStyle("padding-right:20px;")
				.setWmin(20);
		btnBar.setPub(new Button(null).setCls("x-btn")).setSpace(5);
		if(AuditStatusEnum.HANDLING.getIndex().equals(currentDevAudit.getAuditStatus())){
			btnBar.add(new Button("关闭").setOn(Button.EVENT_CLICK, "var d=$.dialog(this);d.ownerView.cmd('search');$.close(this);"));
			btnBar.add(new Button("通过").setOn(Button.EVENT_CLICK, "VM(this).cmd({id:'passConfirm',title:'开发者审核提示',type:'confirm',text:'是否确认审核通过', "
					+ "yes:function(){VM(this).cmd('developerAudit','"+currentDevAudit.getBizId()+"','"+currentDevAudit.getId()+"','1', null)}});"));
			StringBuffer text= new StringBuffer();
			text.append("<input name='auditContent' id='auditContent_A' type='checkbox' value='A'><span class='_tit'>名称不合法</span><br>");
			text.append("<input name='auditContent' id='auditContent_B' type='checkbox' value='B'><span class='_tit'>用途不合法</span><br>");
			text.append("<input name='auditContent' id='auditContent_C' type='checkbox' value='C'><span class='_tit'>联系不上开发者</span><br>");
			text.append("<input name='auditContent' id='auditContent_D' type='checkbox' value='D'><span class='_tit'>其它</span><br>");
			text.append("<textarea name='contentArea' id='contentArea'  rows='5' cols='50'></textarea>");
			btnBar.add(new Button("不通过").setOn(Button.EVENT_CLICK, "VM(this).cmd({id:'noPassConfirm',title:'审核不通过',type:'confirm',text:\""+text.toString()+"\", "
					+ "yes:function(){var auditContent=''; Q(window.parent.document).contents().find(\"input[name='auditContent']:checked\").each(function(){ "
					+ "if('A'==Q(this).val()){auditContent+=',名称不合法'}else if('B'==Q(this).val()){auditContent+=',用途不合法'}else if('C'==Q(this).val()){auditContent+=',联系不上开发者'}"
					+ "else if('D'==Q(this).val()){ var area = Q(window.parent.document).contents().find(\"#contentArea\").val();if(area==''){$.alert('请输入其它原因'); return false;}else{auditContent+=(','+area)} } });"
					+ "if(auditContent==''){$.alert('请输入不通过原因');}else if(auditContent.length>128){$.alert('不通过原因不能超过128位');}else{VM(this).cmd('developerAudit','"+currentDevAudit.getBizId()+"','"+currentDevAudit.getId()+"','0',$.strFrom(auditContent,','));}}});"));
		}else{
			btnBar.add(new Button("关闭").setOn(Button.EVENT_CLICK, "var d=$.dialog(this);d.ownerView.cmd('search');$.close(this);"));
			btnBar.add(new Button("重新审核").setOn(Button.EVENT_CLICK, "VM(this).cmd('reAuditDeveloper','"+currentDevAudit.getBizId()+"','"+currentDevAudit.getId()+"');"));
		}
		
		return btnBar;
		
	}
	
	public static ButtonBar buildAuditProductButtonBar(DevAudit currentDevAudit){
		
		ButtonBar btnBar = new ButtonBar(PubView.DIALOG_BUTTON).setAlign(ButtonBar.ALIGN_RIGHT).setStyle("padding-right:20px;")
				.setWmin(20);
		btnBar.setPub(new Button(null).setCls("x-btn")).setSpace(5);
		if(AuditStatusEnum.HANDLING.getIndex().equals(currentDevAudit.getAuditStatus())){
			btnBar.add(new Button("关闭").setOn(Button.EVENT_CLICK, "var d=$.dialog(this);d.ownerView.cmd('search');$.close(this);"));
			btnBar.add(new Button("通过").setOn(Button.EVENT_CLICK, "VM(this).cmd({id:'passConfirm',title:'开发者审核提示',type:'confirm',text:'是否确认审核通过', "
					+ "yes:function(){VM(this).cmd('productAudit','"+currentDevAudit.getBizId()+"','"+currentDevAudit.getId()+"','1', null)}});"));
			StringBuffer text= new StringBuffer();
			text.append("<input name='auditContent' id='auditContent_A' type='checkbox' value='A'><span class='_tit'>名称不合法</span><br>");
			text.append("<input name='auditContent' id='auditContent_B' type='checkbox' value='B'><span class='_tit'>用途不合法</span><br>");
			text.append("<input name='auditContent' id='auditContent_C' type='checkbox' value='C'><span class='_tit'>联系不上开发者</span><br>");
			text.append("<input name='auditContent' id='auditContent_D' type='checkbox' value='D'><span class='_tit'>其它</span><br>");
			text.append("<textarea name='contentArea' id='contentArea'  rows='5' cols='50'></textarea>");
			btnBar.add(new Button("不通过").setOn(Button.EVENT_CLICK, "VM(this).cmd({id:'noPassConfirm',title:'审核不通过',type:'confirm',text:\""+text.toString()+"\", "
					+ "yes:function(){var auditContent=''; Q(window.parent.document).contents().find(\"input[name='auditContent']:checked\").each(function(){ "
					+ "if('A'==Q(this).val()){auditContent+=',名称不合法'}else if('B'==Q(this).val()){auditContent+=',用途不合法'}else if('C'==Q(this).val()){auditContent+=',联系不上开发者'}"
					+ "else if('D'==Q(this).val()){ var area = Q(window.parent.document).contents().find(\"#contentArea\").val();if(area==''){$.alert('请输入其它原因'); return false;}else{auditContent+=(','+area)} } });"
					+ "if(auditContent==''){$.alert('请输入不通过原因');}else if(auditContent.length>128){$.alert('不通过原因不能超过128位');}else{VM(this).cmd('productAudit','"+currentDevAudit.getBizId()+"','"+currentDevAudit.getId()+"','0',$.strFrom(auditContent,','));}}});"));
		}else{
			btnBar.add(new Button("关闭").setOn(Button.EVENT_CLICK, "var d=$.dialog(this);d.ownerView.cmd('search');$.close(this);"));
			btnBar.add(new Button("重新审核").setOn(Button.EVENT_CLICK, "VM(this).cmd('reAuditProduct','"+currentDevAudit.getBizId()+"','"+currentDevAudit.getId()+"');"));
		}
		
		return btnBar;
		
	}

}
