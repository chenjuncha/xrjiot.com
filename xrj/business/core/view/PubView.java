package com.xrj.business.core.view;

import java.util.ArrayList;
import java.util.List;

import com.rongji.dfish.base.Page;
import com.rongji.dfish.base.Utils;
import com.rongji.dfish.framework.FrameworkHelper;
import com.rongji.dfish.ui.Layout;
import com.rongji.dfish.ui.Scrollable;
import com.rongji.dfish.ui.Widget;
import com.rongji.dfish.ui.command.AjaxCommand;
import com.rongji.dfish.ui.command.AlertCommand;
import com.rongji.dfish.ui.command.DialogCommand;
import com.rongji.dfish.ui.command.LoadingCommand;
import com.rongji.dfish.ui.command.SubmitCommand;
import com.rongji.dfish.ui.form.Pickbox;
import com.rongji.dfish.ui.form.Radiogroup;
import com.rongji.dfish.ui.form.Xbox;
import com.rongji.dfish.ui.helper.FlexGrid;
import com.rongji.dfish.ui.helper.FormPanel;
import com.rongji.dfish.ui.helper.GridLayoutFormPanel;
import com.rongji.dfish.ui.helper.GridPanel;
import com.rongji.dfish.ui.layout.ButtonBar;
import com.rongji.dfish.ui.layout.HorizontalLayout;
import com.rongji.dfish.ui.layout.VerticalLayout;
import com.rongji.dfish.ui.layout.View;
import com.rongji.dfish.ui.layout.grid.Tr;
import com.rongji.dfish.ui.widget.Button;
import com.rongji.dfish.ui.widget.Html;
import com.rongji.dfish.ui.widget.Leaf;
import com.rongji.dfish.ui.widget.PageBar;
import com.rongji.dfish.ui.widget.Split;
import com.rongji.dfish.ui.widget.TreePanel;
import com.xrj.api.dto.SysDataDict;
import com.xrj.business.core.base.RemoteService;

/**
 * 系统中的视图构建器,本类中提供系统中有用到的视图模板
 * 
 * @author PAM Team
 *
 */
public class PubView {
	
	/**
	 * 消息提示框(居中)
	 * 
	 * @param alertInfo
	 *            消息内容
	 * @return
	 */
	public static AlertCommand getInfoAlertNoTimeout(String alertInfo) {
		AlertCommand alert = new AlertCommand(alertInfo).setPosition(AlertCommand.POSITION_MIDDLE);
		return alert;
	}
	/**
	 * 消息提示框(右下角自动删除)
	 * 
	 * @param alertInfo
	 *            消息内容
	 * @return
	 */
	public static AlertCommand getInfoAlert(String alertInfo) {
		AlertCommand alert = new AlertCommand(alertInfo).setPosition(AlertCommand.POSITION_BOTTOMRIGHT).setTimeout(5);
		return alert;
	}

	/**
	 * 警告提示框
	 * 
	 * @param alertInfo
	 * @return
	 */
	public static AlertCommand getWarnAlert(String alertInfo) {
		AlertCommand alert = new AlertCommand(alertInfo);
		return alert;
	}

	public static final String MAIN_ROOT = "m_root";
	public static final String MAIN_TOP = "m_top";
	public static final String MAIN_TITLE = "m_title";
	public static final String MAIN_OPER = "m_oper";
	public static final String MAIN_QUERY_BAR = "m_query_bar";
	public static final String MAIN_QUERY = "m_query";
	public static final String MAIN_BODY = "m_body";
	public static final String MAIN_NAV = "m_nav";
	public static final String MAIN_SPLIT = "m_split";
	public static final String MAIN_DISPLAY = "m_disp";
	public static final String MAIN_SEARCH = "m_search";
	public static final String MAIN_SEARCH_TITLE = "m_search_title";
	public static final String MAIN_CONTENT = "m_cont";
	public static final String MAIN_PAGE_SHELL = "m_page_shell";
	public static final String MAIN_PAGE_INFO = "m_page_info";
	public static final String MAIN_PAGE = "m_page";

	/**
	 * 构建主视图
	 * 
	 * @return
	 */
	public static View buildMainView(boolean hasNav) {
		View view = new View();
		Layout<?, ?> mainRoot = buildMainRoot(hasNav);
		view.add(mainRoot);
		return view;
	}

	/**
	 * 构建主视图
	 * 
	 * @return
	 */
	public static View buildMainView() {
		return buildMainView(true);
	}

	public static Layout<?, ?> buildMainRoot(boolean hasNav) {
		return buildMainRoot(hasNav, "");
	}

	public static Layout<?, ?> buildMainRoot(boolean hasNav, String idPre) {
		idPre = (idPre == null) ? "" : idPre;
		// 主体内容
		VerticalLayout main = new VerticalLayout(idPre + MAIN_ROOT);
		main.setCls("x-bd-body bg-white").setWmin(2).setHmin(2);
		// 主体顶部
		HorizontalLayout mainTop = new HorizontalLayout(idPre + MAIN_TOP);
		main.add(mainTop, "59");
		mainTop.setCls("x-bd-mtitle bd-onlybottom");

		HorizontalLayout topLeft = new HorizontalLayout(null);
		mainTop.add(topLeft, "*");

		// 顶部标题
		Html topTitle = new Html(idPre + MAIN_TITLE, "").setHeight(16);
		topLeft.add(topTitle, "-1");
		topTitle.setCls("x-main-title").setStyle("margin:10px 6px 8px 6px;").setHmin(35).setWmin(24);

//		ButtonBar mainQuery = new ButtonBar(idPre + MAIN_QUERY_BAR).setAlign(ButtonBar.ALIGN_LEFT)
//				.setCls("x-btn-selbar").setStyle("margin-top:6px;").setHmin(6);
//		topLeft.add(mainQuery, "100");
//
//		Button btnQuery = new Button(null).setId(idPre + MAIN_QUERY).setHeight("30");
//		mainQuery.add(btnQuery);
		
		// 顶部按钮栏
		ButtonBar topOper = new ButtonBar(idPre + MAIN_OPER).setAlign(ButtonBar.ALIGN_RIGHT);
		mainTop.add(topOper, "*");
		topOper.setSpace(5).setCls("groupbar").setStyle("margin-right:15px;").setWmin(20).setPub(new Button("").setCls("x-btn"));

		

		HorizontalLayout displayShell = new HorizontalLayout(idPre + MAIN_BODY);
		main.add(displayShell, "*");

		if (hasNav) {
			// HorizontalLayout mainNavShell = new
			// HorizontalLayout(MAIN_NAV_SHELL);
			// displayShell.add(mainNavShell, "201");

			TreePanel mainNav = new TreePanel(idPre + MAIN_NAV).setScroll(true).setStyle("padding:0 10px;").setWmin(20)
					.setPub(new Leaf().setTip(true));
			displayShell.add(mainNav, "200");
			// 分割区
			displayShell.add(new Split().setId(idPre + MAIN_SPLIT).setCls("x-bg-split").setRange("200,300"), "1");
		}
		// 显示区
		VerticalLayout mainDisplay = new VerticalLayout(idPre + MAIN_DISPLAY);
		displayShell.add(mainDisplay, "*");
		mainDisplay.setStyle("padding:5px 0;").setHmin(10);
		
		// 搜索区
		GridLayoutFormPanel mainSearch = new GridLayoutFormPanel(idPre + MAIN_SEARCH);
		mainDisplay.add(mainSearch, "0");
		mainSearch.setScroll(true).setStyle("padding:0 10px;").setWmin(20);
		
		// 查询名称栏
		Html mainSearchTitle = new Html(idPre + MAIN_SEARCH_TITLE, "");
		mainDisplay.add(mainSearchTitle, "0");
		mainSearchTitle.setStyle("padding:0 10px;font-size:14px;");

		// 列表区
		GridPanel mainContent = new GridPanel(idPre + MAIN_CONTENT);
		mainDisplay.add(mainContent);
		mainContent.setStyle("padding:0 10px;").setWmin(20).setCls("x-grid-odd").setFace(GridPanel.FACE_NONE)
				.setScroll(true).setNobr(true).setFocusable(true);
		mainContent.getPrototype(false).getThead().setCls("x-grid-head");
		mainContent.setPub(new Tr().setHeight("40"));

		// 分页区
		HorizontalLayout mainPageShell = new HorizontalLayout(idPre + MAIN_PAGE_SHELL).setStyle("padding:0 10px;")
				.setWmin(20).setScroll(true);
		mainDisplay.add(mainPageShell, "50");
		// 分页信息区
		Html mainPageInfo = new Html("").setId(idPre + MAIN_PAGE_INFO).setAlign(Html.ALIGN_LEFT)
				.setValign(Html.VALIGN_MIDDLE);
		mainPageShell.add(mainPageInfo, "250");
		// 分页栏
		PageBar mainPage = new PageBar(idPre + MAIN_PAGE, PageBar.TYPE_TEXT).setAlign(PageBar.ALIGN_RIGHT)
				.setName("cp"); // 设置了默认的表单提交的名称为cp
		mainPageShell.add(mainPage, "*");
		mainPage.setCls("x-pagebar").setBtncount(5);

		return main;
	}

	/**
	 * 
	 * @param view
	 *            主视图
	 * @param page
	 */
	@SuppressWarnings("rawtypes")
	public static void fillPage(PageBar mainPage, Html mainPageInfo, Page page) {
		if (page == null) {
			return;
		}
		if (mainPage != null) {
			// 默认补第1页
			mainPage.setCurrentpage(page.getCurrentPage() < 1 ? 1 : page.getCurrentPage());
			mainPage.setSumpage(page.getPageCount() < 1 ? 1 : page.getPageCount());
		}
		if (mainPageInfo != null) {
			mainPageInfo.setText(getPageInfoText(page));
		}
	}
	
	@SuppressWarnings("rawtypes")
	private static String getPageInfoText(Page page) {
		String pageInfo = "";
		if (page == null) {
			return pageInfo;
		}
		if (page.getCurrentPage() < 1) {
			page.setCurrentPage(1);
		}
		int from = (page.getCurrentPage() - 1) * page.getPageSize();
		// 如果不传入当前页,显示当前最大范围
		int currentCount = page.getCurrentCount() < 1 ? page.getPageSize() : page.getCurrentCount();
		int to = from + currentCount;
		from++;
		pageInfo = "显示第" + from + "至" + to + "项，共" + page.getRowCount() + "项";
		return pageInfo;
	}

	public final static String BODY_ROOT = "b_root";
	public final static String BODY_NAV_SHELL = "b_nav_shell";
	public final static String BODY_NAV = "b_nav";
	public final static String BODY_NAV_TITLE = "b_nav_title";
	public final static String BODY_NAV_CONTENT = "b_nav_cont";
	public final static String BODY_SPLIT = "b_split";

	public final static String BODY_MAIN = MAIN_ROOT;
	public final static String BODY_MAIN_TOP = MAIN_TOP;
	public final static String BODY_MAIN_TITLE = MAIN_TITLE;
	public final static String BODY_MAIN_QUERY_BAR = MAIN_QUERY_BAR;
	public final static String BODY_MAIN_QUERY = MAIN_QUERY;
	public final static String BODY_MAIN_OPER = MAIN_OPER;

	public final static String BODY_MAIN_DISPLAY = MAIN_DISPLAY;
	public final static String BODY_MAIN_SEARCH = MAIN_SEARCH;
	public final static String BODY_MAIN_SEARCH_TITLE = MAIN_SEARCH_TITLE;
	public final static String BODY_MAIN_CONTENT = MAIN_CONTENT;
	public final static String BODY_MAIN_PAGE_SHELL = MAIN_PAGE_SHELL;
	public final static String BODY_MAIN_PAGE_INFO = MAIN_PAGE_INFO;
	public final static String BODY_MAIN_PAGE = MAIN_PAGE;

	/**
	 * 构建框架视图
	 * 
	 * @return
	 */
	public static View buildBodyView() {
		View view = new View();
		// 根布局
		HorizontalLayout root = new HorizontalLayout(BODY_ROOT);
		view.add(root);
		root.setCls("x-bg-body").setStyle("padding:10px 10px 10px 0;").setWmin(12).setHmin(22);

		VerticalLayout navShell = new VerticalLayout(BODY_NAV_SHELL);
		root.add(navShell, "230");

		// 导航
		VerticalLayout nav = new VerticalLayout(BODY_NAV);
		navShell.add(nav);
		nav.setCls("x-bd-body bg-white").setStyle("margin-left:10px;").setWmin(12).setHmin(2);

		// 导航顶部
		Html navTop = new Html("").setId(BODY_NAV_TITLE);
		nav.add(navTop, "50");
		navTop.setCls("x-nav-title").setAlign(Html.ALIGN_CENTER).setValign(Html.VALIGN_MIDDLE);

		// FIXME 可以考虑直接复用MainView的方法
		// 导航内容
		TreePanel navContent = new TreePanel(BODY_NAV_CONTENT).setScroll(true).setCls("nav-tree")
				.setPub(new Leaf().setTip(true));
		nav.add(navContent, "*");

		// 分隔栏
		root.add(new Split().setId(BODY_SPLIT).setRange("0,230").setIcon(".x-split").setOpenicon(".x-split-open"),
				"10");

		Widget<?> mainRoot = buildMainRoot(false);
		root.add(mainRoot, "*");

		return view;
	}

	public final static String DIALOG_ROOT = "dlg_root";
	public final static String DIALOG_MAIN_SHELL = "dlg_main_shell";
	public final static String DIALOG_MAIN = "dlg_main";
	public final static String DIALOG_MAIN_GRID = "dlg_main_grid";
	public static final String DIALOG_PAGE_SHELL = "dlg_page_shell";
	public static final String DIALOG_PAGE_INFO = "dlg_page_info";
	public static final String DIALOG_PAGE = "dlg_page";
	public final static String DIALOG_BUTTON_SHELL = "dlg_btn_shell";
	public final static String DIALOG_BUTTON = "dlg_btn";

	/**
	 * 构建对话框视图
	 * 
	 * @return
	 */
	public static View buildDialogView() {
		// 本系统中的表单比较复杂,所以默认是提供复杂表单的实现
		return buildDialogView(true);
	}

	/**
	 * 构建对话框视图
	 * 
	 * @param complexForm
	 * @return
	 */
	public static View buildDialogView(boolean complexForm) {
		View view = new View();

		VerticalLayout root = new VerticalLayout(DIALOG_ROOT);
		view.add(root);

		HorizontalLayout mainShell = new HorizontalLayout(DIALOG_MAIN_SHELL);
		root.add(mainShell);
		mainShell.setStyle("padding:20px 20px;").setHmin(40).setWmin(40);

		String mainId = DIALOG_MAIN;
		Widget<?> main = null;
		if (complexForm) {
			main = new FlexGrid(mainId).setScroll(true);
		} else {
			main = new FormPanel(mainId).setScroll(true);
		}
		mainShell.add(main);

		HorizontalLayout btnShell = new HorizontalLayout(DIALOG_BUTTON_SHELL).setCls("bg-low");
		root.add(btnShell, "50");

		ButtonBar btnBar = new ButtonBar(DIALOG_BUTTON).setAlign(ButtonBar.ALIGN_RIGHT).setStyle("padding-right:20px;")
				.setWmin(20);
		btnShell.add(btnBar);
		btnBar.setPub(new Button(null).setCls("x-btn")).setSpace(5);

		// btnBar.add(new Button("确定").setOn(Button.EVENT_CLICK,
		// "VM(this).cmd('save');").setCls("x-btn x-btn-main"));
		// btnBar.add(new Button("关闭").setOn(Button.EVENT_CLICK,
		// Constants.CMD_DLG_CLOSE));

		return view;
	}

	public static View buildDialogGridView() {
		View view = new View();

		VerticalLayout root = new VerticalLayout(DIALOG_ROOT);
		view.add(root);

		VerticalLayout mainShell = new VerticalLayout(DIALOG_MAIN_SHELL);
		root.add(mainShell);
		mainShell.setStyle("padding:10px 20px;").setHmin(20).setWmin(40);

		GridPanel mainGrid = new GridPanel(DIALOG_MAIN);
		mainGrid.setStyle("padding:0 10px;").setWmin(20).setCls("x-grid-odd").setFace(GridPanel.FACE_NONE)
				.setScroll(true).setNobr(true).setFocusable(true);
		mainGrid.getPrototype(false).getThead().setCls("x-grid-head");
		mainGrid.setPub(new Tr().setHeight("40"));
		mainShell.add(mainGrid);

		// 分页区
		HorizontalLayout mainPageShell = new HorizontalLayout(DIALOG_PAGE_SHELL).setStyle("padding:0 10px;")
				.setWmin(20);
		mainShell.add(mainPageShell, "50");
		// 分页信息区
		Html mainPageInfo = new Html("").setId(DIALOG_PAGE_INFO).setAlign(Html.ALIGN_LEFT)
				.setValign(Html.VALIGN_MIDDLE);
		mainPageShell.add(mainPageInfo, "200");

		// 分页栏
		PageBar mainPage = new PageBar(DIALOG_PAGE, PageBar.TYPE_TEXT).setAlign(PageBar.ALIGN_RIGHT).setName("cp"); // 设置了默认的表单提交的名称为cp
		mainPageShell.add(mainPage, "*");
		mainPage.setCls("x-pagebar").setBtncount(5);

		HorizontalLayout btnShell = new HorizontalLayout(DIALOG_BUTTON_SHELL).setCls("bg-low");
		root.add(btnShell, "50");

		ButtonBar btnBar = new ButtonBar(DIALOG_BUTTON).setAlign(ButtonBar.ALIGN_RIGHT).setStyle("padding-right:20px;")
				.setWmin(20);
		btnShell.add(btnBar);
		btnBar.setPub(new Button(null).setCls("x-btn")).setSpace(5);

		return view;
	}

	public static final String DIALOG_PERM_LEFT = "d_p_left";
	public static final String DIALOG_PERM_RIGHT = "d_p_right";

//	public static View buildPermDialogView(String userId, String focusOrg, OrgParam orgParam) throws Exception {
//		View view = buildDialogView();
//
//		HorizontalLayout mainShell = (HorizontalLayout) view.findNodeById(DIALOG_MAIN_SHELL);
//		mainShell.setStyle("padding:10px 10px;").setHmin(20).setWmin(20);
//
//		boolean multiple = orgParam.getMultiple() != null && orgParam.getMultiple();
//		mainShell.addHidden("multiple", multiple ? "1" : "0");
//
//		mainShell.removeNodeById(DIALOG_MAIN);
//		VerticalLayout left = new VerticalLayout(DIALOG_PERM_LEFT);
//		mainShell.add(left, "200");
//
//		HorizontalLayout leftTop = new HorizontalLayout("d_p_left_top");
//		left.add(leftTop, "40");
//		leftTop.setCls("x-bd-content").setStyle("margin-bottom:10px;").setHmin(12).setWmin(2);
//
//		LinkableSuggestionBox<?> orgSelector = buildOrgSelector("", "", "", orgParam).setPicker(null)
//				.setTransparent(true);
//		leftTop.add(orgSelector);
//
//		leftTop.add(new Img(".x-btn-search").setCls("x-bd-content bd-onlyleft").setWmin(1), "32");
//
//		TreePanel orgTree = buildOrgTree(userId, focusOrg, orgParam);
//		left.add(orgTree);
//		orgTree.setCls("x-bd-content").setHmin(12).setWmin(22);
//		if (!multiple) {
//			orgTree.getPub().setOn(Leaf.EVENT_DBLCLICK, "VM(this).cmd('pickerBack');");
//		}
//
//		mainShell.add(new Split().setRange("200,200"), "5");
//
//		VerticalLayout right = new VerticalLayout(DIALOG_PERM_RIGHT);
//		mainShell.add(right, "*");
//
//		ButtonBar btnBar = (ButtonBar) view.findNodeById(DIALOG_BUTTON);
//		btnBar.add(
//				new Button("确定").setOn(Button.EVENT_CLICK, "VM(this).cmd('pickerBack');").setCls("x-btn x-btn-main"));
//		btnBar.add(new Button("关闭").setOn(Button.EVENT_CLICK, "$.close(this);"));
//
//		return view;
//	}

	public static Radiogroup buildRadiogroup(String name, String label, String value, String codeType) {
//		CodeService service = FrameworkHelper.getBean(CodeService.class);
//		List<SysCode> sysCodeList = service.getSysCodeList(codeType);
		List<Object[]> optList = new ArrayList<Object[]>();
//		for (SysCode sysCode : sysCodeList) {
//			optList.add(new Object[] { sysCode.getCode0201(), sysCode.getCode0202() });
//		}
		Radiogroup radiogroup = new Radiogroup(name, label, value, optList);
		return radiogroup;
	}

	/**
	 * 构建数据字典下拉选择框
	 * 
	 * @param name
	 * @param label
	 * @param value
	 * @param codeType
	 * @return
	 */
	public static Xbox buildXbox(String name, String label, String value, String codeType) {
//		CodeService service = FrameworkHelper.getBean(CodeService.class);
//		List<SysCode> sysCodeList = service.getSysCodeList(codeType);
		List<String[]> sysCodeArr = new ArrayList<String[]>();
//		sysCodeArr.add(new String[] { "", "----请选择----" });
//		for (SysCode sysCode : sysCodeList) {
//			sysCodeArr.add(new String[] { sysCode.getCode0201(),
//					"<span class='x-df-code'>(" + sysCode.getCode0201() + ")</span>" + sysCode.getCode0202() });
//		}
		Xbox xBox = new Xbox(name, label, value, sysCodeArr).setCls("x-df-codesel").setEscape(false);
		return xBox;
	}

	/**
	 * 构建数据字典类型下拉选择框
	 * 
	 * @param name
	 * @param dictType 数据字典类型
	 * @param label
	 * @param value
	 * @param flag 默认true,是否显示（请选择）
	 * @return
	 */
	public static Xbox buildCodeTypeSelector(String name, String dictType, String label, String defaultValue, Boolean flag) {
		RemoteService remoteService = FrameworkHelper.getBean(RemoteService.class);
		List<SysDataDict> sysDataDictList = remoteService.getSysDataDictService().findListByDictType(dictType);
		List<String[]> sysCodeTypeArr = new ArrayList<String[]>();
		if(flag==null || flag==true){
			sysCodeTypeArr.add(new String[] { null, "----请选择----" });
		}
		for (SysDataDict temp : sysDataDictList) {
			sysCodeTypeArr.add(new String[] { temp.getDictCode(),
					temp.getDictName() });
		}
		Xbox xBox = new Xbox(name, label, defaultValue, sysCodeTypeArr).setCls("x-df-codesel").setEscape(false);
		return xBox;
	}

	/**
	 * 构建数据字典选择框
	 * 
	 * @param name
	 * @param label
	 * @param value
	 * @param codeType
	 * @return
	 */
	public static Pickbox buildXPickbox(String name, String label, String value, String codeType, String codeBranch) {
//		CodeService codeService = FrameworkHelper.getBean(CodeService.class);
//
//		String text = codeService.getCodeName(codeType, value);
//
		String text = null;
		Pickbox pickbox = new Pickbox(name, label, value).setText(text);
		DialogCommand picker = new DialogCommand("picker", "std", label, DialogCommand.WIDTH_MEDIUM,
				DialogCommand.HEIGHT_LARGE, DialogCommand.POSITION_MIDDLE, "pub/codePicker?codeType=" + codeType
						+ (Utils.notEmpty(codeBranch) ? "&codeBranch=" + codeBranch : "") + "&key=$value");
		pickbox.setPicker(picker);
		return pickbox;
	}

	public static Pickbox buildXPickbox(String name, String label, String value, String codeType, String codeBranch,
			Boolean multiple) {
//		CodeService codeService = FrameworkHelper.getBean(CodeService.class);
//		String[] valueArr = Utils.isEmpty(value) ? new String[] {} : value.split(",");
		String text = "";
//		boolean isFirst = true;
//		for (String values : valueArr) {
//			if (isFirst) {
//				isFirst = false;
//			} else {
//				text += ",";
//			}
//			text += codeService.getCodeName(codeType, values);
//		}
		Pickbox pickbox = new Pickbox(name, label, value).setText(text+"");
		if (multiple) {

			DialogCommand picker = new DialogCommand("picker", "std", label, DialogCommand.WIDTH_MEDIUM,
					DialogCommand.HEIGHT_LARGE, DialogCommand.POSITION_MIDDLE,
					"pub/codePicker?codeType=" + codeType
							+ (Utils.notEmpty(codeBranch) ? "&codeBranch=" + codeBranch : "")
							+ "&key=$value&multiple=1");
			pickbox.setPicker(picker);
		} else {
			DialogCommand picker = new DialogCommand("picker", "std", label, DialogCommand.WIDTH_MEDIUM,
					DialogCommand.HEIGHT_LARGE, DialogCommand.POSITION_MIDDLE, "pub/codePicker?codeType=" + codeType
							+ (Utils.notEmpty(codeBranch) ? "&codeBranch=" + codeBranch : "") + "&key=$value");
			pickbox.setPicker(picker);

		}
		return pickbox;
	}

	public static Pickbox buildXPickbox(String name, String label, String value, String codeType) {
		return buildXPickbox(name, label, value, codeType, null);
	}

	public static final String PERM_ROOT = "p_root";
	public static final String PERM_TOP = "p_top";
	public static final String PERM_SEARCH = "p_search";
	public static final String PERM_TREE = "p_tree";

	public static final String ORG_SEARCH = "searchOrg";
	
	
	
	
	
	
	
	public static AjaxCommand getAjaxCommand(String src) {
		return getAjaxCommand(src, null);
	}
	
	public static AjaxCommand getAjaxCommand(String src, Boolean cover) {
		return getAjaxCommand(src, cover, null);
	}
	
	public static AjaxCommand getAjaxCommand(String src, Boolean cover, String loadingText) {
		return new AjaxCommand(src).setLoading(new LoadingCommand(loadingText).setCover(cover));
	}
	
	public static SubmitCommand getSubmitCommand(String src) {
		return getSubmitCommand(src, null);
	}
	
	public static SubmitCommand getSubmitCommand(String src, Boolean cover) {
		return getSubmitCommand(src, cover, null);
	}
	public static SubmitCommand getSubmitCommand(String src, Boolean cover, String loadingText) {
		return new SubmitCommand(src).setLoading(new LoadingCommand(loadingText).setCover(cover));
	}
	public static final int DIALOG_SIZE_MAX = -1;
	public static final int DIALOG_SIZE_LARGE = 0;
	public static final int DIALOG_SIZE_MEDIUM = 1;
	public static final int DIALOG_SIZE_SMALL = 2;
	
	/**
	 * 提供
	 * @param id
	 * @param title
	 * @param size 弹出框标准大小方案
	 * @param src
	 * @return
	 */
	public static DialogCommand getDialogCommand(String id, String template, String title, int size, String src) {
		template = Utils.isEmpty(template) ? "std" : template;
		String width = "*";
		String height = "*";
		if (size == DIALOG_SIZE_MAX) { // 窗口最大
			width = "-1";
			height = "-1";
		} else if (size == DIALOG_SIZE_LARGE) { // 大窗口
			width = DialogCommand.WIDTH_LARGE;
			height = DialogCommand.HEIGHT_LARGE;
		} else if (size == DIALOG_SIZE_MEDIUM) { // 中窗口
			width = DialogCommand.WIDTH_MEDIUM;
			height = DialogCommand.HEIGHT_MEDIUM;
		} else if (size == DIALOG_SIZE_SMALL) { // 小窗口
			width = DialogCommand.WIDTH_SMALL;
			height = DialogCommand.HEIGHT_SMALL;
		}
		int pos = DialogCommand.POSITION_MIDDLE;
		boolean cover =true;
		DialogCommand dialog = new DialogCommand(id, template, title, width, height, pos, src).setCover(cover);
		return dialog;
	}
	
public static final String DIALOG_BTN = "dlg_btn";
	
	public static View buildDialogView(Widget<?> main) {
		View view = new View();
		
		VerticalLayout root = new VerticalLayout(DIALOG_ROOT);
		view.add(root);
		root.setCls("bg-white bd-dlg").setWmin(2).setHmin(2);;
		
		if (main == null) {
			main = new VerticalLayout(DIALOG_MAIN);
		} else {
			main.setId(DIALOG_MAIN);
		}
		root.add(main);
		if (main instanceof Scrollable) { // 默认支持滚动
			((Scrollable<?>) main).setScroll(true);
		}
		main.setCls("bg-white").setStyle("padding:10px 20px;").setHmin(20).setWmin(40);
		
		ButtonBar btnBar = new ButtonBar(DIALOG_BTN);
		root.add(btnBar, "50");
		btnBar.setSpace(8).setAlign(ButtonBar.ALIGN_RIGHT).setCls("bg-dlg-bottom").setStyle("padding-right:20px;").setWmin(20);
		btnBar.getPub().setCls("btn");
		
		btnBar.add(new Button("取消").setOn(Button.EVENT_CLICK, "dfish.close(this);"));
		
		return view;
	}
	
}