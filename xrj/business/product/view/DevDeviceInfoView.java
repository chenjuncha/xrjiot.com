package com.xrj.business.product.view;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.rongji.dfish.base.Page;
import com.rongji.dfish.base.Utils;
import com.rongji.dfish.base.util.DateUtil;
import com.rongji.dfish.framework.FilterParam;
import com.rongji.dfish.framework.FrameworkHelper;
import com.rongji.dfish.ui.Alignable;
import com.rongji.dfish.ui.Valignable;
import com.rongji.dfish.ui.command.SubmitCommand;
import com.rongji.dfish.ui.form.Text;
import com.rongji.dfish.ui.helper.GridLayoutFormPanel;
import com.rongji.dfish.ui.helper.GridPanel;
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
import com.xrj.api.dto.DevProduct;
import com.xrj.api.dto.DevProductFunction;
import com.xrj.api.model.DeviceRunningState;
import com.xrj.business.util.PageConvertUtil;
import com.xrj.business.core.base.PubService;
import com.xrj.business.core.base.RemoteService;
import com.xrj.business.core.view.PubView;
import com.xrj.framework.util.HttpUtil;

/**
 * 
 * @author yangzeyi
 * @date 2017年7月31日
 *
 */
public class DevDeviceInfoView {
	public static final String DEVICE_INFO_ROOT = "deviceInfoRoot";
	public static final String DEVICE_INFO_TOP = "deviceInfoTop";
	public static final String DEVICE_INFO_MAIN = "deviceInfoMain";
	public static final String DEVICE_INFO_MAIN_PANL = "deviceInfoMainPanl";
	public static final String DEVICE_INFO_MAIN_BTN = "deviceInfoMainBtn";
	public static final String DEVICE_INFO_PAGE = "deviceInfoPage";
	public static final String DEVICE_INFO_ALERT_MAIN = "deviceInfoAlertMain";
	public static final String DEVICE_INFO_ALERT_MAIN_TWO = "deviceInfoAlertMainTwo";
	public static final String DEVICE_INFO_ALERT_MAIN_PAGE = "deviceInfoAlertMainPage";

	/**
	 * 设备信息主页面视图
	 * @param exist 
	 */
	public static View buildDevInfoView(HttpServletRequest request,List<DevDevice> list,Page dfishPage,FilterParam fp, boolean exist) {
		View view = new View();
		/**
		 * 绑定命令
		 */
		view.addCommand("buildDeviceCommand",
				PubView.getAjaxCommand("product/devDeviceInfo/toBuildDevice?menuId=010301000000000000000000"));
		view.addCommand("devDeviceView", PubView.getAjaxCommand("product/devDeviceInfo/view?deviceId=$0"));
		view.addCommand("devDeviceLog", PubView.getAjaxCommand("product/devDeviceLogIndex?deviceSn=$0"));
		view.addCommand("devDeviceController", PubView.getAjaxCommand("product/devDeviceControl/control?deviceSn=$0"));
		view.addCommand("searchDeviceCommand",
				new SubmitCommand("product/devDeviceInfo/searchDevice").setRange(DEVICE_INFO_MAIN_BTN));
		view.addCommand("push", new SubmitCommand("product/devDeviceControl/push?deviceSn=$0&online=$1").setRange("form"));
		view.addCommand("back", PubView.getAjaxCommand("product/devDeviceControl/back"));	
		view.addCommand("backtoInfo", PubView.getAjaxCommand("product/devDeviceControl/backtoInfo"));	
		view.addCommand("deviceOnline", PubView.getAjaxCommand("product/devDeviceControl/deviceOnline?sn=$0&status=$1"));
		view.addCommand("formula", PubView.getAjaxCommand("product/devDeviceControl/formula?value=$0&id=$1"));
		view.addCommand("changeopen", PubView.getAjaxCommand("product/changeopen?labid=$0&openbox=$1"));
		view.addCommand("timeSearching",
				PubView.getSubmitCommand("product/devDeviceLog/timeSearching?deviceSn=$0").setRange("firstSearch"));
		view.addCommand("secondTimeSearching", PubView
				.getSubmitCommand("product/devDeviceLog/secondTimeSearching?deviceSn=$0").setRange("secondSearch"));
		DevProduct product = (DevProduct) HttpUtil.getSessionAttr(request, "product");
//		for (DevDevice devDevice : list) {
//			snList.add(devDevice.getDeviceSn());
//			Page dP = PubService.getPage(request);
//			com.xrj.framework.hibernate.dao.Page p = PageConvertUtil.convertDaoPage(dP);
//			p = remoteService.getDevDeviceService().setPageByParentId(p, product.getId(), devDevice.getId());
//			if (p.getResultSet().size() != 0) {
//				DevDevice devSub = new DevDevice();
//				devSub.setParentId(devDevice.getId());
//				devDevice.setDevDevice(devSub);
//			}
//			devDevice.setDeviceSecret(DevSubAccountView.filterSecret(devDevice.getDeviceSecret()));
//		}
//		List<DeviceRunningState> drList = remoteService.getDevDeviceService().getOnlineStatusList(snList);
//		list = filterOnlineData(list, drList);
		VerticalLayout deviceInfoRoot = new VerticalLayout(DEVICE_INFO_ROOT);
		view.add(deviceInfoRoot);
		HorizontalLayout deviceInfoTop = new HorizontalLayout(DEVICE_INFO_TOP).setHeight(40);
		GridLayoutFormPanel gridLayoutFormPanel = new GridLayoutFormPanel(DEVICE_INFO_MAIN_BTN).setScroll(false);
		gridLayoutFormPanel.add(0, 0, 0, 1,
				new Html("产品唯一标识:" + product.getProductKey()).setStyle("color: #666;font-weight: bolder;"));

		gridLayoutFormPanel.add(0, 2, new Text("deviceSearch", "", "").setPlaceholder("Search"));
		gridLayoutFormPanel.add(0, 3, new SubmitButton("搜索").setCls("x-btn x-btn-main").setOn(SubmitButton.EVENT_CLICK,
				"VM(this).cmd('searchDeviceCommand')"));
		deviceInfoRoot.add(deviceInfoTop);
		deviceInfoTop.add(new Html("<h3>设备运行信息</h3>").setWidth("10%"));
		deviceInfoTop.add(gridLayoutFormPanel);
		
		/*if(exist) {
	        deviceInfoTop.add(new Button("模拟设备控制").setCls("x-btn x-btn-main").setWidth(100).setOn(Button.EVENT_CLICK, "VM(this).cmd('simulateControl')"));			   
	        deviceInfoTop.add(new Html("").setWidth(20));
		} else {
			deviceInfoTop.add(new Button("生成模拟设备").setCls("x-btn x-btn-main").setWidth(100).setOn(Button.EVENT_CLICK, "VM(this).cmd('createSimulate')"));		
			deviceInfoTop.add(new Html("").setWidth(20));
		}*/
		deviceInfoTop.add(new Button("生成设备").setCls("x-btn")
				.setOn(Button.EVENT_CLICK, "VM(this).cmd('buildDeviceCommand')").setWidth("10%"));
		deviceInfoTop.add(new Html("").setWidth(40));
		deviceInfoTop.setValign(Valignable.VALIGN_MIDDLE);
		deviceInfoRoot.add(new Html(
				"<hr style='filter:progid:DXImageTransform.Microsoft.Shadow(color:#CCC,direction:145,strength:15)' color='#CCC' width='100%' size='1' />")
						.setHeight(1),
				"-1");
		deviceInfoRoot.add(buildDevInfoGridPanel(list, request));
		deviceInfoRoot.add(buildPageLayout(dfishPage, fp));
		return view;
	}

	public static GridPanel buildDevInfoGridPanel(List<DevDevice> list,HttpServletRequest request) {
		RemoteService remoteService = FrameworkHelper.getBean(RemoteService.class);
		DevProduct product = (DevProduct) HttpUtil.getSessionAttr(request, "product");
		List<String> snList = new ArrayList<String>();
		for (DevDevice devDevice : list) {
			snList.add(devDevice.getDeviceSn());
			Page dP = PubService.getPage(request);
			com.xrj.framework.hibernate.dao.Page p = PageConvertUtil.convertDaoPage(dP);
			p = remoteService.getDevDeviceService().setPageByParentId(p, product.getId(), devDevice.getId());
			if (p.getResultSet().size() != 0) {
				DevDevice devSub = new DevDevice();
				devSub.setParentId(devDevice.getId());
				devDevice.setDevDevice(devSub);
			}
			devDevice.setDeviceSecret(DevSubAccountView.filterSecret(devDevice.getDeviceSecret()));
		}
		List<DeviceRunningState> drList = remoteService.getDevDeviceService().getOnlineStatusList(snList);
		list = filterOnlineData(list, drList);
		GridPanel gridPanel = new GridPanel(DEVICE_INFO_MAIN_PANL);
		gridPanel.setWmin(20).setCls("x-grid-odd").setFace(GridPanel.FACE_LINE).setScroll(true).setNobr(false)
				.setFocusable(true);
		gridPanel.getPrototype(false).getThead().setCls("x-grid-head");
		gridPanel.setPub(new Tr().setHeight("40").setCls("w-td-line w-th"));
		gridPanel.setGridData(list);
		gridPanel.addColumn(GridColumn.hidden("id", "Id"));
		gridPanel.addColumn(GridColumn.hidden("devDevice", "fdevDevice"));
		// gridPanel.addColumn(GridColumn.text("devDevice", "fdevDevice","设备名",
		// "-1"));
		gridPanel.addColumn(GridColumn.text("deviceName", "fdeviceName", "设备名", "-1"));
		gridPanel.addColumn(
				GridColumn.text("fdevDevice", "2%").setFormat("javascript:var devSub = $fdevDevice;if(devSub!=null){"
						+ "return \"<i class='x-grid-copy' title='拥有子设备'></i>\"}return \"<span></span>\""));
		
		gridPanel.addColumn(GridColumn.text("deviceSn", "fdeviceSn", "设备唯一标识", "-1"));
		gridPanel.addColumn(GridColumn.text("deviceSecret", "fdeviceSecret", "设备证书", "-1"));
		// gridPanel.addColumn(GridColumn.text("FlastOnlineTime","-1").setLabel("最后一次在线时间").setFormat("javascript:var
		// data = $flastOnlineTime;if(data==null){"
		// + "return \"<span style='color:gray'>未知</span>\"} return
		// \"<span>$flastOnlineTime</span>\""));
		gridPanel.addColumn(GridColumn.text("lastOnlineTime", "flastOnlineTime", "最后一次在线时间", "15%")
				.setDataFormat(DateUtil.PATTERN_DATE_TIME));
		gridPanel.addColumn(GridColumn.text("status", "fstatus", "设备状态", "10%"));
		gridPanel.addColumn(GridColumn.text("Id", "foper", "操作", "10%").setFormat(buildDeviceInfoBtn()));
		return gridPanel;
	}

	private static String buildDeviceInfoBtn() {
		StringBuffer sb = new StringBuffer();
		sb.append("<i class='x-grid-view' title='查看' onclick=\"VM(this).cmd('devDeviceView','$Id');\"></i> ");
		sb.append(
				"&nbsp;&nbsp;<i class='x-grid-designer' title='日志' onclick=\"VM(this).cmd('devDeviceLog','$fdeviceSn');\"></i>");
		sb.append(
				"&nbsp;&nbsp;<i class='x-grid-source' title='控制' onclick=\"VM(this).cmd('devDeviceController','$fdeviceSn');\"></i>");
		return sb.toString();
	}

	/**
	 * 主分页布局模块
	 * 
	 * @return
	 */
	public static HorizontalLayout buildPageLayout(Page dfishPage, FilterParam fp) {
		HorizontalLayout horizontalLayout = new HorizontalLayout(DEVICE_INFO_PAGE).setHeight("10%");
		PageBar pageBar = new PageBar("", PageBar.TYPE_TEXT).setCls("x-pagebar").setBtncount(5)
				.setAlign(PageBar.ALIGN_RIGHT).setJump(true);
		pageBar.setCurrentpage(dfishPage.getCurrentPage());
		pageBar.setSumpage(dfishPage.getPageCount());
		pageBar.setSrc("product/devDeviceInfo/search?cp=$0" + fp);
		horizontalLayout.add(pageBar);
		return horizontalLayout;
	}

	/**
	 * 搜索分页布局模块
	 * 
	 * @param dfishPage
	 * @param fp
	 * @return
	 */
	public static HorizontalLayout buildSearChPageLayout(Page dfishPage, FilterParam fp, String search) {
		HorizontalLayout horizontalLayout = new HorizontalLayout(DEVICE_INFO_PAGE).setHeight("10%");
		PageBar pageBar = new PageBar("", PageBar.TYPE_TEXT).setCls("x-pagebar").setBtncount(5)
				.setAlign(PageBar.ALIGN_RIGHT).setJump(true);
		pageBar.setCurrentpage(dfishPage.getCurrentPage());
		pageBar.setSumpage(dfishPage.getPageCount());
		pageBar.setSrc("product/devDeviceInfo/searchPage?cp=$0" + fp + "&deviceSearch=" + search);
		horizontalLayout.add(pageBar);
		return horizontalLayout;
	}

	/**
	 * 无子设备的弹出框视图
	 * 
	 * @param devDevice
	 * @param productKey
	 * @param online
	 * @return
	 */
	public static View buildDialogView(DevDevice devDevice, String productKey, List<DeviceRunningState> drList,List<DevProductFunction> list) {
		View view = new View().setStyle("padding:0 10px 10px 10px");

		view.addCommand("viewDeviceSecret",
				PubView.getAjaxCommand("product/devDeviceInfo/viewDeviceSecret?deviceId=$0"));
		VerticalLayout verticalLayout = new VerticalLayout("");
		view.add(verticalLayout);
		DeviceRunningState deviceRunningState = new DeviceRunningState();
		if (Utils.notEmpty(drList)) {
			deviceRunningState = drList.get(0);
		}
		verticalLayout.add(dialogTopFormPanel(productKey, devDevice, deviceRunningState));
		verticalLayout.add(new Html(
				"<hr style='filter:progid:DXImageTransform.Microsoft.Shadow(color:#CCC,direction:145,strength:15)' color='#CCC' width='100%' size='1' />")
						.setHeight(1),
				"-1");
		HorizontalLayout horizontalLayout = new HorizontalLayout(DEVICE_INFO_ALERT_MAIN);
		GridPanel gridPanel1 = new GridPanel(null).setScroll(true);
		gridPanel1.setWmin(20).setCls("x-grid-odd").setFace(GridPanel.FACE_LINE).setScroll(true).setNobr(true)
				.setFocusable(true);
		gridPanel1.getPrototype(false).getThead().setCls("x-grid-head");
		gridPanel1.setPub(new Tr().setHeight("40").setCls("w-td-line w-th"));
		gridPanel1.setGridData(list);
		gridPanel1.addColumn(GridColumn.text("serialNo", "fserialNo", "序号", "10%"));
		gridPanel1.addColumn(GridColumn.text("functionName", "ffunctionName", "功能点名称", "30%"));
		gridPanel1.addColumn(GridColumn.text("value", "fvalue", "数据", "30%"));
		gridPanel1.addColumn(GridColumn.text("functionType", "ffunctionType", "功能类型", "30%"));
		horizontalLayout.add(gridPanel1);
		verticalLayout.add(horizontalLayout);
		return view;
	}

	/**
	 * 存在子设备的查看按钮返回的视图
	 * 
	 * @return
	 */
	public static View buildDialogView2(List<DevDevice> list2,DevDevice devDevice, String productKey, List<DeviceRunningState> drList,
			Page dfishPage, FilterParam fp,List<DevProductFunction> list1) {
		View view = new View().setStyle("padding:0 10px 10px 10px");

		view.addCommand("viewDeviceSecret",
				PubView.getAjaxCommand("product/devDeviceInfo/viewDeviceSecret?deviceId=$0"));
		VerticalLayout verticalLayout = new VerticalLayout("");
		view.add(verticalLayout);
		DeviceRunningState deviceRunningState = new DeviceRunningState();
		if (Utils.notEmpty(drList)) {
			deviceRunningState = drList.get(0);
		}
		verticalLayout.add(dialogTopFormPanel(productKey, devDevice, deviceRunningState));
		verticalLayout.add(new Html(
				"<hr style='filter:progid:DXImageTransform.Microsoft.Shadow(color:#CCC,direction:145,strength:15)' color='#CCC' width='100%' size='1' />")
						.setHeight(1),
				"-1");
		HorizontalLayout horizontalLayout = new HorizontalLayout(DEVICE_INFO_ALERT_MAIN);
		GridPanel gridPanel = new GridPanel("grid1").setScroll(true);
		gridPanel.setWmin(20).setCls("x-grid-odd").setFace(GridPanel.FACE_LINE).setScroll(true).setNobr(false)
				.setFocusable(true);
		gridPanel.getPrototype(false).getThead().setCls("x-grid-head");
		gridPanel.setPub(new Tr().setHeight("40").setCls("w-td-line w-th"));
		gridPanel.setGridData(list1);
		gridPanel.addColumn(GridColumn.text("serialNo", "fserialNo", "序号", "10%"));
		gridPanel.addColumn(GridColumn.text("functionName", "ffunctionName", "功能点名称", "30%"));
		gridPanel.addColumn(GridColumn.text("value", "fvalue", "数据", "30%"));
		gridPanel.addColumn(GridColumn.text("functionType", "ffunctionType", "功能类型", "30%"));
		VerticalLayout verLeft = new VerticalLayout("panlLeft").setWidth("40%");
		verLeft.add(new Html("数据").setAlign(Alignable.ALIGN_CENTER).setHeight("10%"));
		verLeft.add(gridPanel);
		horizontalLayout.add(verLeft);
		horizontalLayout.add(new Html("<hr style=\"width:1px;height:100%;color:#CCC \"></hr>").setWidth(1));
		VerticalLayout verRight = new VerticalLayout("panlRight");
		verRight.add(new Html("终端列表").setAlign(Alignable.ALIGN_CENTER).setHeight("10%"));
		verRight.add(buildDialogView2Panel(list2,drList));
		verRight.add(buildDialogViewPage(dfishPage,fp,devDevice.getId()));
		horizontalLayout.add(verRight);
		verticalLayout.add(horizontalLayout);
		return view;
	}

	public static VerticalLayout buildDialogView2Panel(List<DevDevice> list2,List<DeviceRunningState> drList) {
		list2 = filterOnlineDataAndId(list2, drList);
		VerticalLayout verPanel = new VerticalLayout(DEVICE_INFO_ALERT_MAIN_TWO);
		GridPanel gridPanel2 = new GridPanel("grid2").setScroll(true);
		gridPanel2.setWmin(20).setCls("x-grid-odd").setFace(GridPanel.FACE_LINE).setScroll(true).setNobr(false)
				.setFocusable(true);
		gridPanel2.getPrototype(false).getThead().setCls("x-grid-head");
		gridPanel2.setPub(new Tr().setHeight("40").setCls("w-td-line w-th"));
		gridPanel2.setGridData(list2);
		gridPanel2.addColumn(GridColumn.hidden("lastOnlineTime", "flastOnlineTime"));
		gridPanel2.addColumn(GridColumn.text("id", "Id", "序号", "10%"));
		gridPanel2.addColumn(GridColumn.text("deviceSn", "fdeviceSn", "设备唯一标识", "-1"));
		gridPanel2.addColumn(GridColumn.text("lastOnlineTime", "flastOnlineTime", "最后一次在线时间", "30%")
				.setDataFormat(DateUtil.PATTERN_DATE_TIME));
		verPanel.add(gridPanel2);
		return verPanel;
	}

	/**
	 * 弹出框分页
	 * 
	 * @param dfishPage
	 * @param fp
	 * @return
	 */
	public static HorizontalLayout buildDialogViewPage(Page dfishPage, FilterParam fp, String deviceId) {
		HorizontalLayout horizontalLayout = new HorizontalLayout("page_hor_alert").setHeight("10%");
		PageBar pageBar = new PageBar(DEVICE_INFO_ALERT_MAIN_PAGE, PageBar.TYPE_TEXT).setCls("x-pagebar").setBtncount(5)
				.setAlign(PageBar.ALIGN_RIGHT).setJump(true);
		pageBar.setCurrentpage(dfishPage.getCurrentPage());
		pageBar.setSumpage(dfishPage.getPageCount());
		pageBar.setSrc("product/devDeviceInfo/dialogSearch?cp=$0" + fp + "&deviceId=" + deviceId);
		horizontalLayout.add(pageBar);
		return horizontalLayout;
	}

	/**
	 * 过滤在线状态
	 * 
	 * @param list
	 * @param drList
	 * @return
	 */
	public static List<DevDevice> filterOnlineData(List<DevDevice> list, List<DeviceRunningState> drList) {
		if (!Utils.isEmpty(drList)) {
			for (DevDevice devDevice : list) {
				devDevice.setLastOnlineTime(null);
				devDevice.setStatus("离线");
				for(DeviceRunningState deviceRunningState:drList){
					if(deviceRunningState.getSn().equals(devDevice.getDeviceSn())){
						devDevice.setLastOnlineTime(deviceRunningState.getLastOnlineTime());
						//devDevice.setStatus("在线");
					}
					if(deviceRunningState.isOnline()==true&&deviceRunningState.getSn().equals(devDevice.getDeviceSn())){
						devDevice.setStatus("在线");
					}
				}
			}
		} else {
			for(DevDevice devDevice:list){
				devDevice.setStatus("离线");
			}
		}
		return list;
	}

	public static List<DevDevice> filterOnlineDataAndId(List<DevDevice> list, List<DeviceRunningState> drList) {
		int i = 0;
		if (Utils.isEmpty(drList)) {
			for (DevDevice devDevice : list) {
				devDevice.setLastOnlineTime(null);
				devDevice.setId(i + 1 + "");
				i = i + 1;
			}
		} else {
			for (DevDevice devDevice : list) {
				devDevice.setLastOnlineTime(null);
				for(DeviceRunningState deviceRunningState:drList){
					if(deviceRunningState.getSn().equals(devDevice.getDeviceSn()))
						devDevice.setLastOnlineTime(deviceRunningState.getLastOnlineTime());
				}
				//devDevice.setLastOnlineTime(drList.get(i).getLastOnlineTime());
				devDevice.setId(i + 1 + "");
				i = i + 1;
			}
		}
		return list;
	}

	public static GridLayoutFormPanel dialogTopFormPanel(String productKey, DevDevice devDevice,
			DeviceRunningState deviceRunningState) {
		GridLayoutFormPanel gridLayoutFormPanel = new GridLayoutFormPanel("").setHeight(-1);
		gridLayoutFormPanel.add(0, 0,
				new Html("产品唯一标识(Product Key): " + productKey).setStyle("color: #666;font-weight: bolder;"));
		gridLayoutFormPanel.add(0, 1,
				new Html("设备唯一标识(Device Sn): " + devDevice.getDeviceSn()).setStyle("color: #666;font-weight: bolder;"));
		gridLayoutFormPanel.add(1, 0, new Html("设备名(Device Name): " + devDevice.getDeviceName())
				.setStyle("color: #666;font-weight: bolder;"));
		HorizontalLayout horizontalGroup = new HorizontalLayout("").setValign(Valignable.VALIGN_MIDDLE);
		horizontalGroup.add(new Html("设备证书(Device Secret):" + DevSubAccountView.filterSecret(devDevice.getDeviceSecret()))
				.setStyle("color: #666;font-weight: bolder;"));
		horizontalGroup.add(new Button("查看").setCls("x-btn").setWidth(-1).setOn(Button.EVENT_CLICK,
				"VM(this).cmd('viewDeviceSecret','" + devDevice.getId() + "')"));
		horizontalGroup.add(new Html("").setWidth("10%"));
		gridLayoutFormPanel.add(1, 1, horizontalGroup);
		gridLayoutFormPanel.add(2, 0, new Html("设备状态: " + (deviceRunningState.isOnline() == true ? "在线" : "离线"))
				.setStyle("color: #666;font-weight: bolder;"));
		gridLayoutFormPanel.add(2, 1,
				new Html("设备最后一次在线时间: "
						+ DateUtil.format(deviceRunningState.getLastOnlineTime(), DateUtil.PATTERN_DATE_TIME))
								.setStyle("color: #666;font-weight: bolder;"));
		return gridLayoutFormPanel;
	}
}
