package com.xrj.business.product.view;

import java.util.ArrayList;
import java.util.List;
import com.rongji.dfish.base.Page;
import com.rongji.dfish.framework.FilterParam;
import com.rongji.dfish.ui.form.DatePicker;
import com.rongji.dfish.ui.helper.GridLayoutFormPanel;
import com.rongji.dfish.ui.helper.GridPanel;
import com.rongji.dfish.ui.helper.TabPanel;
import com.rongji.dfish.ui.layout.HorizontalLayout;
import com.rongji.dfish.ui.layout.VerticalLayout;
import com.rongji.dfish.ui.layout.View;
import com.rongji.dfish.ui.layout.grid.GridColumn;
import com.rongji.dfish.ui.layout.grid.Tr;
import com.rongji.dfish.ui.widget.Button;
import com.rongji.dfish.ui.widget.Html;
import com.rongji.dfish.ui.widget.PageBar;
import com.rongji.dfish.ui.widget.SubmitButton;
import com.xrj.api.dto.DevDevice;
import com.xrj.api.dto.DevDeviceLog;
import com.xrj.api.model.DeviceRunningState;

public class DevDeviceLogView {
	public static final String DEVICE_LOG_ROOT = DevDeviceInfoView.DEVICE_INFO_ROOT;
	public static final String DEVICE_LOG_TOP = "deviceLogTop";
	public static final String DEVICE_LOG_FIRST_TAB = "firstTab";
	public static final String DEVICE_LOG_FIRST_SEARCH = "firstSearch";
	public static final String DEVICE_LOG_FIRST_CONTENT = "firstMainContent";
	public static final String DEVICE_LOG_FIRST_PAGE = "firstMainPage";
	public static final String DEVICE_LOG_SECOND_TAB = "secondTab";
	public static final String DEVICE_LOG_SECOND_SEARCH = "secondSearch";
	public static final String DEVICE_LOG_SECOND_CONTENT = "secondMainContent";
	public static final String DEVICE_LOG_SECOND_PAGE = "secondMainPage";

	/**
	 * 设备日志主界面
	 * 
	 * @param logList
	 * @param dfishPage
	 * @param fp
	 * @param onLineOfflineList
	 * @param secondDfishPage
	 * @param secondFp
	 * @param devDevice
	 * @param deviceRunningState
	 * @return
	 */
	public static View buildDevDeviceLogIndex(List<DevDeviceLog> logList, Page dfishPage, FilterParam fp,
			List<DevDeviceLog> onLineOfflineList, Page secondDfishPage, FilterParam secondFp, DevDevice devDevice,
			DeviceRunningState deviceRunningState) {
		View view = new View();
		VerticalLayout root = new VerticalLayout(DEVICE_LOG_ROOT);
		view.add(root);
		// 页面顶部
		HorizontalLayout top = new HorizontalLayout(DEVICE_LOG_TOP).setHeight(40);
		root.add(top);
		top.add(new Html("<h3>设备日志:详细记录</h3>"));
		Button closeButton = new Button().setHeight(30).setText("返回").setWidth(90).setCls("x-btn").setOn(Button.EVENT_CLICK,
				"VM(this).cmd('back')");;
		top.add(closeButton);
		top.add(new Html("").setWidth("30"));
		root.add(new Html(
				"<hr style='filter:progid:DXImageTransform.Microsoft.Shadow(color:#CCC,direction:145,strength:15)' color='#CCC' width='100%' size='1' />")
						.setHeight(1),
				"-1");
		// 两个标签页
		TabPanel tabPanel = new TabPanel("tabPanel").setTabHeight(30);
		root.add(tabPanel);
		tabPanel.add(buildCommunicationLog(logList, dfishPage, fp, devDevice, deviceRunningState), "通信日志");
		tabPanel.add(
				buildOnLineOffLineRecord(onLineOfflineList, secondDfishPage, secondFp, devDevice, deviceRunningState),
				"上下线记录");
		tabPanel.getTabBar().setHeight("40").setCls("x-tabbar ");
		tabPanel.getTabBar().getPub().setCls("x-tab");
		return view;
	}

	/**
	 * 通信日志标签页
	 * 
	 * @param logList
	 * @param dfishPage
	 * @param fp
	 * @param devDevice
	 * @param deviceRunningState
	 * @return
	 */
	public static VerticalLayout buildCommunicationLog(List<DevDeviceLog> logList, Page dfishPage, FilterParam fp,
			DevDevice devDevice, DeviceRunningState deviceRunningState) {
		// 主显示区
		VerticalLayout mainDisplay = new VerticalLayout(DEVICE_LOG_FIRST_TAB).setHmin(10);
		GridLayoutFormPanel maintop = buildTabPanelTop(devDevice, deviceRunningState);
		HorizontalLayout search = new HorizontalLayout(DEVICE_LOG_FIRST_SEARCH);
		SubmitButton btn = new SubmitButton("firstSearch").setOn(SubmitButton.EVENT_CLICK,
				"VM(this).cmd('timeSearching','" + devDevice.getDeviceSn() + "')");
		maintop.add(1, 4, 1, 7, timeSearchLayout(search, "firstFrom", "firstTo", btn));
		mainDisplay.add(maintop);
		mainDisplay.add(buildCommunicationLogGridPanel(logList));
		mainDisplay.add(buildCommunicationLogPageLayout(dfishPage, fp, devDevice), "50");
		return mainDisplay;
	}

	/**
	 * 上下线记录标签页
	 * 
	 * @param onLineOfflineList
	 * @param secondDfishPage
	 * @param secondFp
	 * @param devDevice
	 * @param deviceRunningState
	 * @return
	 */
	public static VerticalLayout buildOnLineOffLineRecord(List<DevDeviceLog> onLineOfflineList, Page secondDfishPage,
			FilterParam secondFp, DevDevice devDevice, DeviceRunningState deviceRunningState) {
		// 主显示区
		VerticalLayout mainDisplay = new VerticalLayout(DEVICE_LOG_SECOND_TAB).setHmin(10);
		GridLayoutFormPanel maintop = buildTabPanelTop(devDevice, deviceRunningState);
		HorizontalLayout search = new HorizontalLayout(DEVICE_LOG_SECOND_SEARCH);
		SubmitButton btn = new SubmitButton("secondSearch").setOn(SubmitButton.EVENT_CLICK,
				"VM(this).cmd('secondTimeSearching','" + devDevice.getDeviceSn() + "')");
		maintop.add(1, 4, 1, 7, timeSearchLayout(search, "secondFrom", "secondTo", btn));
		mainDisplay.add(maintop);
		mainDisplay.add(buildOnLineRecordGridPanel(onLineOfflineList));
		mainDisplay.add(buildOnLineRecordPageLayout(secondDfishPage, secondFp, devDevice), "50");
		return mainDisplay;
	}

	/**
	 * 标签页的顶部显示区
	 * 
	 * @param devDevice
	 * @param deviceRunningState
	 * @return
	 */
	public static GridLayoutFormPanel buildTabPanelTop(DevDevice devDevice, DeviceRunningState deviceRunningState) {
		GridLayoutFormPanel maintop = new GridLayoutFormPanel("");
		maintop.setWmin(20);
		maintop.setHeight("-1").setWidth("100%");
		maintop.add(0, 0, 0, 3, new Html("设备SN：" + devDevice.getDeviceSn()));
		maintop.add(0, 4, 0, 6, new Html("MAC地址： " + devDevice.getMac()));
		maintop.add(0, 7, 0, 9, new Html("在线状态： " + (deviceRunningState.isOnline() == true ? "在线" : "离线")));
		maintop.add(1, 0, 1, 3, new Html("MCU 固件版本号：" + "硬：初始版本 / 软：初始版本"));
		return maintop;
	}

	/**
	 * 标签页的时间段搜索布局
	 * 
	 * @param search
	 * @param fromName 开始时间选择按钮的name
	 * @param toName 结束时间选择按钮的name
	 * @param btn
	 * @return
	 */
	public static HorizontalLayout timeSearchLayout(HorizontalLayout search, String fromName, String toName,
			SubmitButton btn) {
		DatePicker from = new DatePicker("", "", "", DatePicker.DATE_TIME_FULL).setName(fromName);
		DatePicker to = new DatePicker("", "", "", DatePicker.DATE_TIME_FULL).setName(toName);
		search.setHeight(40).setValign(HorizontalLayout.VALIGN_MIDDLE);
		search.add(new Html("从").setWidth(20).setHeight(20));
		search.add(from);
		search.add(new Html("到").setWidth(30).setHeight(20).setAlign(Html.ALIGN_CENTER));
		search.add(to);
		search.add(new Html("").setWidth(10));
		btn.setText("搜索").setHeight(30).setWidth(80).setCls("x-btn");
		search.add(btn);
		search.add(new Html("").setWidth("10%"));
		return search;
	}

	/**
	 * 设置标签页列表区格式
	 * 
	 * @param mainContent
	 * @return
	 */
	public static GridPanel buildGridPanelStyle(GridPanel mainContent) {
		mainContent.setWmin(20).setCls("x-grid-odd").setFace(GridPanel.FACE_LINE).setScroll(true).setNobr(false)
			.setFocusable(true).getPrototype(false).setPub(new Tr().setHeight("40").setCls("w-td-line w-th"))
			.getThead().setCls("x-grid-head");
		return mainContent;
	}

	/**
	 * 标签页分页布局
	 * 
	 * @param mainPageShell
	 * @param mainPage
	 * @param dfishPage
	 * @return
	 */
	public static HorizontalLayout buildPageLayoutStyle(HorizontalLayout mainPageShell, PageBar mainPage,
			Page dfishPage) {
		mainPageShell.setWmin(20).setScroll(true);
		Html mainPageInfo = new Html("");
		mainPageShell.add(mainPageInfo, "250");
		mainPage.setAlign(PageBar.ALIGN_RIGHT).setJump(true).setName("cp");
		mainPageShell.add(mainPage, "*");
		mainPage.setCls("x-pagebar").setBtncount(5);
		mainPage.setCurrentpage(dfishPage.getCurrentPage());
		mainPage.setSumpage(dfishPage.getPageCount());
		return mainPageShell;
	}
	
	/**
	 * 通信日志列表区
	 * 
	 * @param logList
	 * @return 表格
	 */
	public static GridPanel buildCommunicationLogGridPanel(List<DevDeviceLog> logList) {
		GridPanel mainContent = new GridPanel(DEVICE_LOG_FIRST_CONTENT);
		buildGridPanelStyle(mainContent);
		// 查询结果集
		List<Object[]> gridData = new ArrayList<Object[]>();
		for (DevDeviceLog devDeviceLog : logList) {
			gridData.add(new Object[] { devDeviceLog.getId(),
					devDeviceLog.getCommunicateFrom() + " to " + devDeviceLog.getCommunicateTo(),
					devDeviceLog.getCommunicationTime(), devDeviceLog.getContent(), devDeviceLog.getContentDecode(), });
		}
		int columnIndex = 0;
		mainContent.addColumn(GridColumn.hidden(columnIndex++, "id"));
		mainContent.addColumn(GridColumn.text(columnIndex++, "communicationType", "类型", "10%"));
		mainContent.addColumn(
				GridColumn.text(columnIndex++, "communicationTime", "时间", "15%").setDataFormat("yyyy-MM-dd HH:mm:ss"));
		mainContent.addColumn(GridColumn.text(columnIndex++, "content", "指令(HEX)", "30%"));
		mainContent.addColumn(GridColumn.text(columnIndex++, "contentDecode", "指令说明", "35%"));
		mainContent.setGridData(gridData);
		return mainContent;
	}
	
	/**
	 * 上下线记录列表区
	 * 
	 * @param onLineOfflineList
	 * @return 表格
	 */
	public static GridPanel buildOnLineRecordGridPanel(List<DevDeviceLog> onLineOfflineList) {
		GridPanel mainContent = new GridPanel(DEVICE_LOG_SECOND_CONTENT);
		buildGridPanelStyle(mainContent);
		// 查询结果集
		List<Object[]> gridData = new ArrayList<Object[]>();
		for (DevDeviceLog devDeviceLog : onLineOfflineList) {
			gridData.add(new Object[] { devDeviceLog.getId(), devDeviceLog.getAction(),
					devDeviceLog.getCommunicationTime(), devDeviceLog.getContentDecode(), });
		}
		int columnIndex = 0;
		mainContent.addColumn(GridColumn.hidden(columnIndex++, "id"));
		mainContent.addColumn(GridColumn.text(columnIndex++, "action", "状态", "5%"));
		mainContent.addColumn(GridColumn.text(columnIndex++, "time", "时间", "15%").setDataFormat("yyyy-MM-dd HH:mm:ss"));
		mainContent.addColumn(GridColumn.text(columnIndex++, "decode", "指令", "*"));
		mainContent.setGridData(gridData);
		return mainContent;
	}

	/**
	 * 通信日志分页区
	 * 
	 * @param dfishPage
	 * @param fp
	 * @param devDevice
	 * @return 水平布局
	 */
	public static HorizontalLayout buildCommunicationLogPageLayout(Page dfishPage, FilterParam fp,
			DevDevice devDevice) {
		HorizontalLayout mainPageShell = new HorizontalLayout(DEVICE_LOG_FIRST_PAGE);
		PageBar mainPage = new PageBar("", PageBar.TYPE_TEXT)
				.setSrc("product/devDeviceLog/fistPageSearch?cp=$0" + fp + "&deviceSn=" + devDevice.getDeviceSn());
		buildPageLayoutStyle(mainPageShell, mainPage, dfishPage);
		return mainPageShell;
	}

	/**
	 * 通信日志时间段搜索分页
	 * 
	 * @param dfishPage
	 * @param fp
	 * @param from
	 * @param to
	 * @param deviceSn
	 * @return 水平布局
	 */
	public static HorizontalLayout buildSearChPageLayout(Page dfishPage, FilterParam fp, String from, String to,
			String deviceSn) {
		HorizontalLayout mainPageShell = new HorizontalLayout(DEVICE_LOG_FIRST_PAGE);
		PageBar mainPage = new PageBar("", PageBar.TYPE_TEXT).setSrc("product/devDeviceLog/fistPageSearchSearch?cp=$0"
				+ fp + "&firstFrom=" + from + "&firstTo=" + to + "&deviceSn=" + deviceSn);
		buildPageLayoutStyle(mainPageShell, mainPage, dfishPage);
		return mainPageShell;
	}

	/**
	 * 上下线记录分页
	 * 
	 * @param secondDfishPagehPage
	 * @param secondFp
	 * @param devDevice
	 * @return 水平布局
	 */
	public static HorizontalLayout buildOnLineRecordPageLayout(Page secondDfishPage, FilterParam secondFp,
			DevDevice devDevice) {
		HorizontalLayout mainPageShell = new HorizontalLayout(DEVICE_LOG_SECOND_PAGE);
		PageBar mainPage = new PageBar("", PageBar.TYPE_TEXT).setSrc(
				"product/devDeviceLog/secondTimeSearching?cp=$0" + secondFp + "&deviceSn=" + devDevice.getDeviceSn());
		buildPageLayoutStyle(mainPageShell, mainPage, secondDfishPage);
		return mainPageShell;
	}

	/**
	 * 上下线记录时间段搜索分页
	 * 
	 * @param dfishPage
	 * @param fp
	 * @param from
	 * @param to
	 * @param deviceSn
	 * @return 水平布局
	 */
	public static HorizontalLayout buildSearChPageLayout2(Page dfishPage, FilterParam fp, String from, String to,
			String deviceSn) {
		HorizontalLayout mainPageShell = new HorizontalLayout(DEVICE_LOG_SECOND_PAGE);
		PageBar mainPage = new PageBar("", PageBar.TYPE_TEXT).setSrc("product/devDeviceLog/secondPageSearchSearch?cp=$0"
				+ fp + "&secondFrom=" + from + "&secondTo=" + to + "&deviceSn=" + deviceSn);
		buildPageLayoutStyle(mainPageShell, mainPage, dfishPage);
		return mainPageShell;
	}
}
