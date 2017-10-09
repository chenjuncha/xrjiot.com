package com.xrj.business.product.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.rongji.dfish.base.Page;
import com.rongji.dfish.base.Utils;
import com.rongji.dfish.base.util.DateUtil;
import com.rongji.dfish.framework.FilterParam;
import com.rongji.dfish.framework.FrameworkHelper;
import com.rongji.dfish.ui.Alignable;
import com.rongji.dfish.ui.Valignable;
import com.rongji.dfish.ui.command.JSCommand;
import com.rongji.dfish.ui.command.SubmitCommand;
import com.rongji.dfish.ui.form.AbstractBox;
import com.rongji.dfish.ui.form.Checkbox;
import com.rongji.dfish.ui.form.Text;
import com.rongji.dfish.ui.form.Validatable;
import com.rongji.dfish.ui.form.Validate;
import com.rongji.dfish.ui.helper.FormPanel;
import com.rongji.dfish.ui.helper.GridLayoutFormPanel;
import com.rongji.dfish.ui.helper.GridPanel;
import com.rongji.dfish.ui.helper.HorizontalGroup;
import com.rongji.dfish.ui.helper.Label;
import com.rongji.dfish.ui.layout.HorizontalLayout;
import com.rongji.dfish.ui.layout.VerticalLayout;
import com.rongji.dfish.ui.layout.View;
import com.rongji.dfish.ui.layout.grid.GridColumn;
import com.rongji.dfish.ui.layout.grid.Tr;
import com.rongji.dfish.ui.widget.Button;
import com.rongji.dfish.ui.widget.Html;
import com.rongji.dfish.ui.widget.PageBar;
import com.rongji.dfish.ui.widget.SubmitButton;
import com.sun.org.apache.bcel.internal.generic.NEW;
import com.xrj.api.dto.DevDeveloper;
import com.xrj.api.dto.DevDevice;
import com.xrj.api.dto.DevHardwareVersion;
import com.xrj.api.dto.DevProduct;
import com.xrj.api.dto.DevSubAccount;
import com.xrj.api.em.StatusEnum;
import com.xrj.business.util.PageConvertUtil;
import com.xrj.business.core.base.PubConstants;
import com.xrj.business.core.base.PubService;
import com.xrj.business.core.base.RemoteService;
import com.xrj.business.core.view.PubView;
import com.xrj.framework.util.HttpUtil;

/**
 * 
 * @author yangzeyi
 * @Date 2017-7-25
 *
 */
public class DevSubAccountView {
	public static final String CHILDROOT = "childRoot";
	public static final String CHILDTop = "childTop";
	public static final String CHILDMAIN = "childMain";
	public static final String CHILDPANL = "childPanl";
	public static final String SELECT_BOX_ID = "select_box_id";
	public static final String CHILDPAGE = "childpage";

	/**
	 * 子账号主界面
	 * 
	 * @return
	 */
	public static View buildChildAccountView(HttpServletRequest request,Page dfishPage,FilterParam fp,List<DevSubAccount> list) {
		View view = new View();
		view.addCommand("buildMainAccount", PubView.getAjaxCommand("product/buildMainAccountCommand"));
		view.addCommand("buildSubAccountCommand",
				new SubmitCommand("product/batchSubAccountCommand").setRange("buildSubForm"));
		view.addCommand("viewSecret", PubView.getAjaxCommand("product/viewSecret?mainSecret=$0"));
		view.addCommand("searchSubAccountCommand",
				new SubmitCommand("product/subAccount/searchSubAccount").setRange("searchSubForm"));
		
		RemoteService remoteService = FrameworkHelper.getBean(RemoteService.class);
		//DevProduct product = (DevProduct) HttpUtil.getSessionAttr(request, "product");
		DevDeveloper devDeveloper = (DevDeveloper) HttpUtil.getSessionAttr(request, PubConstants.SESSION_DEV_DEVELOPER);
		String mainAccount = remoteService.getDevSubAccountService().findPrimaryAccount(devDeveloper.getId());
		String mainSecret = remoteService.getDevSubAccountService().findAccountSecret(devDeveloper.getId());
		view.addCommand("checkAll", new JSCommand("VM(this).f('select_box_id').check(true)"));
		VerticalLayout verticalLayout = new VerticalLayout(CHILDROOT);
		view.add(verticalLayout);
		HorizontalLayout top = new HorizontalLayout(CHILDTop).setHeight(40);
		top.add(new Html("<h3>生成子账号</h3>").setWidth("10%"));
		GridLayoutFormPanel gridLayoutFormPanel = new GridLayoutFormPanel("").setHeight("-1").setScroll(false);
		gridLayoutFormPanel.add(0, 0,
				new Button("生成主账号证书").setCls("x-btn").setOn(Button.EVENT_CLICK, "VM(this).cmd('buildMainAccount')"));
		gridLayoutFormPanel.add(0, 1,
				new Text("certificate", "主账号:", "生成主账号使用的Account").setReadonly(true).setValue(mainAccount));
		gridLayoutFormPanel.add(0, 2,
				new Text("secret", "证书:", "生成主账号使用的Secret").setReadonly(true).setValue(filterSecret(mainSecret)));
		gridLayoutFormPanel.add(0, 3, new Button("查看证书").setCls("x-btn x-btn-main").setWidth("100")
				.setOn(Button.EVENT_CLICK, "VM(this).cmd('viewSecret','" + mainSecret + "')"));
		top.add(gridLayoutFormPanel);
		verticalLayout.add(top);
		verticalLayout.add(new Html(
				"<hr style='filter:progid:DXImageTransform.Microsoft.Shadow(color:#CCC,direction:145,strength:15)' color='#CCC' width='100%' size='1' />")
						.setHeight(1),
				"-1");
		VerticalLayout vermain = buildChildAccountMain(list, dfishPage, fp);
		verticalLayout.add(vermain);
		return view;
	}

	/**
	 * 主布局页面
	 * 
	 * @return
	 */
	public static VerticalLayout buildChildAccountMain(List<DevSubAccount> subAccountList, Page dfishPage,
			FilterParam fp) {

		VerticalLayout verticalLayout = new VerticalLayout(CHILDMAIN);

		HorizontalLayout horizontalLayout = new HorizontalLayout("").setHeight("50");
		horizontalLayout
				.add(new Button("全选").setCls("x-btn x-btn-main").setOn(Button.EVENT_CLICK, "VM(this).cmd('checkAll')"))
				.add(new Html("").setWidth("1%"))
				.add(new Button("启用").setCls("x-btn").setOn(Button.EVENT_CLICK,
						" var bidArr = Q('input[name = " + SELECT_BOX_ID + "]:checked'); var bids='';"
								+ "if (bidArr&&bidArr.length) {" + "for (var i = 0;i<bidArr.length;i++) {"
								+ "bids += (bids ? ',':'')+bidArr[i].value;}}"
								+ "else {VM(this).cmd({type:'alert',text:'请先选择子账号！！！'});return null;}"
								+ "VM(this).cmd({type:'confirm',text:'确认启用所选子账号?',"
								+ "yes:{type:'ajax',src:'product/subAccount/enable?subAccountId='+bids}});return ;"))
				.add(new Html("").setWidth("1%"))
				.add(new Button("禁用").setCls("x-btn").setOn(Button.EVENT_CLICK,
						" var bidArr = Q('input[name = " + SELECT_BOX_ID + "]:checked'); var bids='';"
								+ "if (bidArr&&bidArr.length) {" + "for (var i = 0;i<bidArr.length;i++) {"
								+ "bids += (bids ? ',':'')+bidArr[i].value;}}"
								+ "else {VM(this).cmd({type:'alert',text:'请先选择子账号！！！'});return null;}"
								+ "VM(this).cmd({type:'confirm',text:'确认禁用所选子账号?',"
								+ "yes:{type:'ajax',src:'product/subAccount/disable?subAccountId='+bids}});return ;"))
				.add(new Html("").setWidth("1%"))
				.add(new Button("删除").setCls("x-btn").setOn(Button.EVENT_CLICK,
						" var bidArr = Q('input[name = " + SELECT_BOX_ID + "]:checked'); var bids='';"
								+ "if (bidArr&&bidArr.length) {" + "for (var i = 0;i<bidArr.length;i++) {"
								+ "bids += (bids ? ',':'')+bidArr[i].value;}}"
								+ "else {VM(this).cmd({type:'alert',text:'请先选择子账号！！！'});return null;}"
								+ "VM(this).cmd({type:'confirm',text:'确认删除?',"
								+ "yes:{type:'ajax',src:'product/subAccount/delete?subAccountId='+bids}});return ;"))
				.add(new Html("").setWidth("1%"))

				.add(new SubmitButton("查看").setCls("x-btn x-btn-main").setOn(SubmitButton.EVENT_CLICK,
						" var bidArr = Q('input[name = " + SELECT_BOX_ID + "]:checked'); var bids='';"
								+ "if (bidArr&&bidArr.length==1) {" + "for (var i = 0;i<bidArr.length;i++) {"
								+ "bids += (bids ? ',':'')+bidArr[i].value;}}"
								+ "else if(bidArr&&bidArr.length){VM(this).cmd({type:'alert',text:'一次只能查看一个!!!'});return null;}"
								+ "else {VM(this).cmd({type:'alert',text:'请先选择子账号！！！'});return null;}"
								+ "VM(this).cmd({type:'ajax',src:'product/subAccount/view?subAccountId='+bids});return ;"

		));
		GridLayoutFormPanel gridPanel = new GridLayoutFormPanel("buildSubForm").setWidth("25%").setScroll(false);
		gridPanel.add(0, 0,0,1,
				new Text("subAccountNum", "生成数量：", "").setPlaceholder("1~1000")
						.addValidate(Validate.pattern("/^([1-9]\\d{0,2}|1000)$/").setPatterntext("请输入1~1000的数字"))
						.addValidate(Validate.required().setRequiredtext("请输入1~1000的数字")));	
		GridLayoutFormPanel gridPanel2 = new GridLayoutFormPanel("searchSubForm").setWidth("20%").setScroll(false);
		gridPanel2.add(0, 0, new Text("subAccountSearch", "", "").addValidate(Validate.required().setRequiredtext("请输入查询内容")).setPlaceholder("Search"));
		horizontalLayout.add(gridPanel.setStyle("margin-top:5px")).setValign(Valignable.VALIGN_MIDDLE);
		horizontalLayout.add(new SubmitButton("生成子账号").setStyle("margin-left:10px;").setCls("x-btn x-btn-main")
				.setOn(SubmitButton.EVENT_CLICK, "VM(this).cmd('buildSubAccountCommand')").setWidth("100"));
		horizontalLayout.add(gridPanel2.setStyle("margin-top:5px"));
		horizontalLayout.add(new SubmitButton("搜索").setStyle("margin-left:10px;").setWidth(70).setCls("x-btn x-btn-main").setOn(SubmitButton.EVENT_CLICK, "VM(this).cmd('searchSubAccountCommand')"));
		horizontalLayout.add(new Html(""));
		// verticalLayout.add(gridLayoutFormPanel);
		verticalLayout.add(horizontalLayout);
		verticalLayout.add(buildGridPanel(subAccountList));
		verticalLayout.add(buildPageLayout(dfishPage, fp));
		// GridPanel gridPanel = new GridPanel("");
		// gridPanel.addColumn(GridColumn.text(dataColumnIndex, field, label,
		// width));
		return verticalLayout;
	}

	/**
	 * 表格模块
	 * 
	 * @param list
	 * @return
	 */
	public static GridPanel buildGridPanel(List<DevSubAccount> subAccountList) {

		for (DevSubAccount devSubAccount : subAccountList) {
			if (devSubAccount.getStatus().equals(StatusEnum.NORMAL.getIndex())) {
				devSubAccount.setStatus("有效");
			} else {
				devSubAccount.setStatus("无效");
			}
			devSubAccount.setAccountSecret(filterSecret(devSubAccount.getAccountSecret()));
		}
		// FormPanel formPanel = new FormPanel("checkForm");
		// formPanel.add(new Checkbox("Item", "", false, , text))
		GridPanel gridPanel = new GridPanel(CHILDPANL);
		gridPanel.setWmin(20).setCls("x-grid-odd").setFace(GridPanel.FACE_LINE).setScroll(true).setNobr(false)
				.setFocusable(true);
		gridPanel.getPrototype(false).getThead().setCls("x-grid-head");
		gridPanel.setPub(new Tr().setHeight("40").setCls("w-td-line w-th"));
		gridPanel.setGridData(subAccountList);	
		gridPanel.addColumn(GridColumn.checkbox("id", "bid", "50", SELECT_BOX_ID, "", false));
		gridPanel.addColumn(GridColumn.text("accountName", "faccount", "账号(AccessKey)", "-1"));
		gridPanel.addColumn(GridColumn.text("accountSecret", "fsecret", "证书(AccessSecret)", "-1"));

		gridPanel.addColumn(
				GridColumn.text("createTime", "fcreatTime", "生成时间", "-1").setDataFormat(DateUtil.PATTERN_DATE_TIME));
		gridPanel.addColumn(GridColumn.text("status", "fstatus", "状态", "10%"));
//		gridPanel.addColumn(GridColumn.text("id", "ffid", "操作", "5%").setFormat(
//				"<i class='x-grid-view' title='查看密码' onclick=\"VM(this).cmd('subAccountSecret','$fid')\";></i>"));
		return gridPanel;
	}

	/**
	 * 分页布局模块
	 * 
	 * @return
	 */
	public static HorizontalLayout buildPageLayout(Page dfishPage, FilterParam fp) {
		HorizontalLayout horizontalLayout = new HorizontalLayout(CHILDPAGE).setHeight("10%");
		PageBar pageBar = new PageBar("", PageBar.TYPE_TEXT).setCls("x-pagebar").setBtncount(5).setJump(true)
				.setAlign(PageBar.ALIGN_RIGHT);
		pageBar.setCurrentpage(dfishPage.getCurrentPage());
		pageBar.setSumpage(dfishPage.getPageCount());
		pageBar.setSrc("product/subAccount/search?cp=$0" + fp);
		horizontalLayout.add(pageBar);
		return horizontalLayout;
	}

	public static HorizontalLayout buildPageLayout2(Page dfishPage, FilterParam fp, String id) {
		HorizontalLayout horizontalLayout = new HorizontalLayout("").setHeight("10%");
		PageBar pageBar = new PageBar("subViewPage", PageBar.TYPE_TEXT).setCls("x-pagebar").setBtncount(5).setJump(true)
				.setAlign(PageBar.ALIGN_RIGHT);
		pageBar.setCurrentpage(dfishPage.getCurrentPage());
		pageBar.setSumpage(dfishPage.getPageCount());
		pageBar.setSrc("product/subAccountDevice/search?cp=$0" + fp + "&subAccountId=" + id);
		horizontalLayout.add(pageBar);
		return horizontalLayout;
	}

	/**
	 * 查看子账号弹窗视图
	 * 
	 * @return
	 */
	public static View buildTempChildAccount(HttpServletRequest request, DevSubAccount devSubAccount) {
		Page dfishPage = PubService.getPage(request);
		FilterParam fp = PubService.getParam(request);
		RemoteService remoteService = FrameworkHelper.getBean(RemoteService.class);
		DevProduct product = (DevProduct) HttpUtil.getSessionAttr(request, "product");
		com.xrj.framework.hibernate.dao.Page page = PageConvertUtil.convertDaoPage(dfishPage);
		page = remoteService.getDevDeviceService().getBySubAccountId(page, devSubAccount.getId());
		dfishPage = PageConvertUtil.convertDfishPage(page);
		List<DevDevice> list = dfishPage.getResultSet();
		View view = new View().setStyle("padding:0 10px 10px 10px");
		VerticalLayout verticalLayout = new VerticalLayout("");
		view.addCommand("subAccountSecret", PubView.getAjaxCommand("product/viewAccountSecret?id=$0"));
		// HorizontalLayout horizontalLayout = new
		// HorizontalLayout("").setHeight("50");
		GridLayoutFormPanel gridLayoutFormPanel = new GridLayoutFormPanel("").setHeight(-1).setScroll(false);
		gridLayoutFormPanel.add(0, 0,
				new Html("产品唯一标识：" + product.getProductKey()).setStyle("color: #666;font-weight: bolder;"));
		gridLayoutFormPanel.add(0, 1,
				new Html("账号(AccessKey)：" + devSubAccount.getAccountName()).setStyle("color: #666;font-weight: bolder;"));
		HorizontalLayout horizontalGroup = new HorizontalLayout("").setValign(Valignable.VALIGN_MIDDLE);
		horizontalGroup.add(new Html("证书(AccessSecret)：" + filterSecret(devSubAccount.getAccountSecret())).setStyle("color: #666;font-weight: bolder;"));
		horizontalGroup.add(new Button("查看").setCls("x-btn").setWidth(-1).setOn(Button.EVENT_CLICK,
				"VM(this).cmd('subAccountSecret','" + devSubAccount.getId() + "')"));
		horizontalGroup.add(new Html("").setWidth("10"));
		gridLayoutFormPanel.add(0, 2, horizontalGroup);
		gridLayoutFormPanel.add(1, 0,
				new Html("创建时间：" + new SimpleDateFormat("yyyy-MM-dd hh:mm").format(devSubAccount.getCreateTime()))
						.setStyle("color: #666;font-weight: bolder;"));
		gridLayoutFormPanel.add(1, 1,
				new Html("是否有效：" + ((devSubAccount.getStatus().equals(StatusEnum.NORMAL.getIndex())) ? "有效" : "无效"))
						.setStyle("color: #666;font-weight: bolder;"));
		// horizontalLayout.add(gridLayoutFormPanel);
		verticalLayout.add(gridLayoutFormPanel);
		verticalLayout.add(new Html(
				"<hr style='filter:progid:DXImageTransform.Microsoft.Shadow(color:#CCC,direction:145,strength:15)' color='#CCC' width='100%' size='1' />")
						.setHeight(1),
				"-1");
		verticalLayout.add(buildGridPanelDetail(list));
		verticalLayout.add(buildPageLayout2(dfishPage, fp, devSubAccount.getId()));
		view.add(verticalLayout);
		return view;
	}

	public static GridPanel buildGridPanelDetail(List<DevDevice> list) {

		GridPanel gridPanel = new GridPanel("subViewPanl");
		gridPanel.setWmin(20).setCls("x-grid-odd").setFace(GridPanel.FACE_LINE).setScroll(true).setNobr(false)
				.setFocusable(true);
		gridPanel.getPrototype(false).getThead().setCls("x-grid-head");
		gridPanel.setPub(new Tr().setHeight("40").setCls("w-td-line w-th"));
		gridPanel.setGridData(list);
		// gridPanel.addColumn(0,GridColumn.text(0, "deviceName", "设备名", "-1"));
		// gridPanel.addColumn(1,GridColumn.text(1, "deviceMac", "设备MAC",
		// "-1"));
		gridPanel.addColumn(GridColumn.text("deviceSn", "deviceSn", "设备唯一标识", "-1"));
		gridPanel.addColumn(GridColumn.text("deviceSecret", "deviceSecret", "设备证书", "-1"));
		gridPanel.addColumn(GridColumn.text("lastOnlineTime", "lastOnlineTime", "最后一次在线时间", "-1")
				.setDataFormat(DateUtil.PATTERN_DATE_TIME));
		return gridPanel;
	}
	/**
	 * 搜索分页布局模块
	 * 
	 * @param dfishPage
	 * @param fp
	 * @return
	 */
	public static HorizontalLayout buildSearChPageLayout(Page dfishPage, FilterParam fp, String search) {
		HorizontalLayout horizontalLayout = new HorizontalLayout(CHILDPAGE).setHeight("10%");
		PageBar pageBar = new PageBar("", PageBar.TYPE_TEXT).setCls("x-pagebar").setBtncount(5)
				.setAlign(PageBar.ALIGN_RIGHT).setJump(true);
		pageBar.setCurrentpage(dfishPage.getCurrentPage());
		pageBar.setSumpage(dfishPage.getPageCount());
		pageBar.setSrc("product/subAccount/searchSubAccount?cp=$0" + fp + "&subAccountSearch=" + search);
		horizontalLayout.add(pageBar);
		return horizontalLayout;
	}

	/**
	 * 保留前四后四位其余变*
	 */
	public static String filterSecret(String secret) {
		if (Utils.notEmpty(secret) && secret.length() >= 8) {
			String secret1 = secret.substring(0, 4);
			String secret3 = secret.substring(4, secret.length() - 4);
			String secret2 = secret.substring(secret.length() - 4, secret.length());
			String new_secret = secret1 + secret3.replaceAll("[a-zA-Z0-9]", "*") + secret2;
			return new_secret;
		}
		return secret;
	}
}
