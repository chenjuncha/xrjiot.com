package com.xrj.business.product.view;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.rongji.dfish.base.Page;
import com.rongji.dfish.base.util.DateUtil;
import com.rongji.dfish.framework.FilterParam;
import com.rongji.dfish.framework.FrameworkHelper;
import com.rongji.dfish.ui.Valignable;
import com.rongji.dfish.ui.Widget;
import com.rongji.dfish.ui.command.SubmitCommand;
import com.rongji.dfish.ui.form.Checkbox;
import com.rongji.dfish.ui.form.Radio;
import com.rongji.dfish.ui.form.Radiogroup;
import com.rongji.dfish.ui.form.Spinner;
import com.rongji.dfish.ui.form.Text;
import com.rongji.dfish.ui.form.Validate;
import com.rongji.dfish.ui.helper.FormPanel;
import com.rongji.dfish.ui.helper.GridLayoutFormPanel;
import com.rongji.dfish.ui.helper.GridPanel;
import com.rongji.dfish.ui.helper.HorizontalGroup;
import com.rongji.dfish.ui.helper.Label;
import com.rongji.dfish.ui.layout.GridLayout;
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
import com.xrj.api.dto.DevHardwareVersion;
import com.xrj.api.dto.DevProduct;
import com.xrj.api.dto.DevProductFunction;
import com.xrj.api.em.DataTypeEnum;
import com.xrj.api.em.FunctionTypeEnum;
import com.xrj.api.model.DeviceRunningState;
import com.xrj.business.util.PageConvertUtil;
import com.xrj.business.core.base.PubService;
import com.xrj.business.core.base.RemoteService;
import com.xrj.business.core.view.PubView;
import com.xrj.business.product.service.DeviceControlService;
import com.xrj.framework.util.HttpUtil;

import net.sf.json.JSONObject;

/**
 * 
 * @author chenjunchao
 *
 */
public class DevDeviceControlView {

	/**
	 * @设备推送界面
	 * @param devProduct
	 * @param devDevice
	 * @param totalList
	 * @param deviceRunningState
	 * @return
	 */
	public static View deviceControl(DevProduct devProduct, DevDevice devDevice, List<List<Widget<?>>> totalList,
			DeviceRunningState deviceRunningState) {
		View view = new View();
		view.addCommand("push", new SubmitCommand("product/devDeviceControl/push").setRange("form"));
		view.addCommand("back", PubView.getAjaxCommand("product/devDeviceControl/back"));
		view.addCommand("changeopen", PubView.getAjaxCommand("product/changeopen?open=$0"));
		view.addCommand("changenormal", PubView.getAjaxCommand("product/changenormal?normal=$0"));

		VerticalLayout verticalLayout = new VerticalLayout("deviceInfoRoot");
		HorizontalLayout btm = new HorizontalLayout("btm");
		HorizontalLayout top = new HorizontalLayout("top").setHeight(40);
		GridLayoutFormPanel gridLayoutFormPanel = new GridLayoutFormPanel("gridLayoutFormPanel").setHeight(70);

		GridLayout grid = new GridLayout("grid");
		grid.setHeight("76%").setWidth("100%");
		grid.setWmin(20).setCls("x-grid-odd").setFace(GridPanel.FACE_LINE).setScroll(true).setNobr(false)
				.setFocusable(true);
		grid.getThead().setCls("x-grid-head");
		grid.setPub(new Tr().setHeight("40").setCls("w-td-line w-th"));

		top.add(new Html("<h3>设备控制</h3>"));

		if (totalList.size() > 0) {
			top.add(new Button("推送").setStyle("margin-top:5px").setCls("x-btn x-btn-main").setWidth(80).setOn(
					SubmitButton.EVENT_CLICK,
					"VM(this).cmd('push','" + devDevice.getDeviceSn() + "','" + deviceRunningState.isOnline() + "')"));
			top.add(new Html("").setWidth(20));
			top.add(new Button("返回").setStyle("margin-top:5px").setCls("x-btn").setWidth(80).setOn(Button.EVENT_CLICK,
					"VM(this).cmd('backtoInfo')"));
			top.add(new Html("").setWidth(20));
		} else {
			top.add(new Button("返回").setStyle("margin-top:5px").setCls("x-btn").setWidth(80).setOn(Button.EVENT_CLICK,
					"VM(this).cmd('back')"));
			top.add(new Html("").setWidth(20));
		}

		HorizontalLayout status = new HorizontalLayout("status");
		status.add(new Html("").setWidth(28));
		status.add(new Label("", "状态:在线").setStyle("margin-top:5px").setWidth(55));
		status.add(new Checkbox("", "", deviceRunningState.isOnline() == true ? true : false, "", "").setName("Status1")
				.setReadonly(true));
		String title = "证书有误,不予显示";
		if (devDevice.getDeviceSecret().length() == 32) {
			title = devDevice.getDeviceSecret().substring(0, 4) + "************************"
					+ devDevice.getDeviceSecret().substring(28, 32);
		}
		gridLayoutFormPanel.add(0, 0, status);
		gridLayoutFormPanel.add(0, 1, new Label("产品唯一标识:", devProduct.getProductKey()));
		gridLayoutFormPanel.add(0, 2, new Label("设备名:", devDevice.getDeviceName()));
		gridLayoutFormPanel.add(1, 1, new Label("设备唯一标识:", devDevice.getDeviceSn()));
		gridLayoutFormPanel.add(1, 2, new Label("设备证书:", title));

		FormPanel form = new FormPanel("form");
		int m = 0;
		for (int i = 0; i < totalList.size(); i++) {
			grid.add(m, 0, m, 1, totalList.get(i).get(0));
			grid.add(m, 2, m, 3, totalList.get(i).get(1));
			m++;
		}

		form.add(grid);
		btm.add(form);
		verticalLayout.add(top);
		verticalLayout.add(new Html(
				"<hr style='filter:progid:DXImageTransform.Microsoft.Shadow(color:#CCC,direction:145,strength:15)' color='#CCC' width='100%' size='1' />")
						.setHeight(1),
				"-1");
		verticalLayout.add(gridLayoutFormPanel);
		verticalLayout.add(new Html(
				"<hr style='filter:progid:DXImageTransform.Microsoft.Shadow(color:#CCC,direction:145,strength:15)' color='#CCC' width='100%' size='1' />")
						.setHeight(1),
				"-1");
		verticalLayout.add(btm);
		view.add(verticalLayout);
		return view;
	}

	/**
	 * @模拟设备主界面
	 * @param request
	 * @param deviceList
	 * @param dfishPage
	 * @param fp
	 * @return
	 */
	public static View simulateDevice(HttpServletRequest request, List<DevDevice> deviceList, Page dfishPage,
			FilterParam fp) {
		View view = new View();
		view.addCommand("simulateControl", PubView.getAjaxCommand("product/devDeviceControl/simulateControl?sn=$0"));
		view.addCommand("createSimulate", PubView.getAjaxCommand("product/devDeviceControl/createSimulate"));
		view.addCommand("fresh", PubView.getAjaxCommand("product/devDeviceControl/fresh?sn=$0"));
		view.addCommand("back", PubView.getAjaxCommand("product/devDeviceControl/back"));
		view.addCommand("push",
				new SubmitCommand("product/devDeviceControl/push?deviceSn=$0&online=$1").setRange("form"));
		view.addCommand("deviceOnline",
				PubView.getAjaxCommand("product/devDeviceControl/deviceOnline?sn=$0&status=$1"));
		view.addCommand("changeopen", PubView.getAjaxCommand("product/changeopen?labid=$0&openbox=$1"));
		view.addCommand("changenormal", PubView.getAjaxCommand("product/changenormal?normal=$0"));
		view.addCommand("formula", PubView.getAjaxCommand("product/devDeviceControl/formula?value=$0&id=$1"));
		VerticalLayout deviceInfoRoot = new VerticalLayout("simuDeviceInfoRoot");
		view.add(deviceInfoRoot);
		HorizontalLayout deviceInfoTop = new HorizontalLayout("").setHeight(40);
		deviceInfoRoot.add(deviceInfoTop);
		deviceInfoTop.add(new Html("<h3>模拟设备信息</h3>").setWidth("10%"));
		deviceInfoTop.add(new Html("").setWidth("70%"));
		deviceInfoTop.add(new Button("生成模拟设备").setCls("x-btn")
				.setOn(Button.EVENT_CLICK, "VM(this).cmd('createSimulate')").setWidth("10%"));
		deviceInfoTop.setValign(Valignable.VALIGN_MIDDLE);
		deviceInfoRoot.add(new Html(
				"<hr style='filter:progid:DXImageTransform.Microsoft.Shadow(color:#CCC,direction:145,strength:15)' color='#CCC' width='100%' size='1' />")
						.setHeight(1),
				"-1");
		deviceInfoRoot.add(buildSimuDevGridPanel(deviceList, request));
		deviceInfoRoot.add(buildPageLayout(dfishPage, fp));
		return view;
	}

	public static GridPanel buildSimuDevGridPanel(List<DevDevice> list, HttpServletRequest request) {
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
		GridPanel gridPanel = new GridPanel("simuPanel");
		gridPanel.setWmin(20).setCls("x-grid-odd").setFace(GridPanel.FACE_LINE).setScroll(true).setNobr(false)
				.setFocusable(true);
		gridPanel.getPrototype(false).getThead().setCls("x-grid-head");
		gridPanel.setPub(new Tr().setHeight("40").setCls("w-td-line w-th"));
		gridPanel.setGridData(list);
		gridPanel.addColumn(GridColumn.text("deviceSn", "fdeviceSn", "设备唯一标识", "-1"));
		gridPanel.addColumn(GridColumn.text("Id", "foper", "操作", "30%").setFormat(buildDeviceInfoBtn()));
		gridPanel.addColumn(GridColumn.hidden("id", "Id"));
		gridPanel.addColumn(GridColumn.hidden("devDevice", "fdevDevice"));
		return gridPanel;
	}

	private static String buildDeviceInfoBtn() {
		StringBuffer sb = new StringBuffer();
		sb.append(
				"&nbsp;&nbsp;<i class='x-grid-source' title='控制' onclick=\"VM(this).cmd('simulateControl','$fdeviceSn');\"></i>");
		return sb.toString();
	}

	public static HorizontalLayout buildPageLayout(Page dfishPage, FilterParam fp) {
		HorizontalLayout horizontalLayout = new HorizontalLayout("simuPage").setHeight("10%");
		PageBar pageBar = new PageBar("", PageBar.TYPE_TEXT).setCls("x-pagebar").setBtncount(5)
				.setAlign(PageBar.ALIGN_RIGHT).setJump(true);
		pageBar.setCurrentpage(dfishPage.getCurrentPage());
		pageBar.setSumpage(dfishPage.getPageCount());
		pageBar.setSrc("product/devSimuDeviceInfo/search?cp=$0" + fp);
		horizontalLayout.add(pageBar);
		return horizontalLayout;
	}

	/**
	 * @模拟设备控制推送界面
	 * @param devProduct
	 * @param devDevice
	 * @param totalList
	 * @param deviceRunningState
	 * @return
	 */
	public static View deviceDataControl(DevProduct devProduct, DevDevice devDevice, List<List<Widget<?>>> totalList,
			DeviceRunningState deviceRunningState, List<DevProductFunction> functionPoint) {
		View view = new View();

		VerticalLayout verticalLayout = new VerticalLayout("simuDeviceInfoRoot");
		HorizontalLayout btm = new HorizontalLayout("btm");
		HorizontalLayout top = new HorizontalLayout("top").setHeight(40).setOn(HorizontalLayout.EVENT_READY,
				"login.refresh(this)");
		GridLayoutFormPanel gridLayoutFormPanel = new GridLayoutFormPanel("gridLayoutFormPanel").setHeight(70);

		GridLayout grid = new GridLayout("grid");
		grid.setHeight("76%");
		grid.setWmin(20).setCls("x-grid-odd").setFace(GridPanel.FACE_LINE).setScroll(true).setNobr(false)
				.setFocusable(true);
		grid.getThead().setCls("x-grid-head");
		grid.setPub(new Tr().setHeight("40").setCls("w-td-line w-th"));

		top.add(new Html("<h3>模拟设备控制</h3>"));
		// top.add(new Button("上线").setStyle("margin-top:5px").setCls("x-btn
		// x-btn-main").setWidth(80).setOn(Button.EVENT_CLICK,
		// "VM(this).find('Status1').check(true);VM(this).find('Status1').readonly(true);"));
		top.add(new Button("上线").setStyle("margin-top:5px").setCls("x-btn x-btn-main").setWidth(80)
				.setOn(Button.EVENT_CLICK, "VM(this).cmd('deviceOnline','" + devDevice.getDeviceSn() + "','true')"));
		top.add(new Html("").setWidth(20));
		// top.add(new Button("下线").setStyle("margin-top:5px").setCls("x-btn
		// x-btn-main").setWidth(80).setOn(Button.EVENT_CLICK,
		// "VM(this).find('Status1').check(false);VM(this).find('Status1').readonly(true);"));
		top.add(new Button("下线").setStyle("margin-top:5px").setCls("x-btn x-btn-main").setWidth(80)
				.setOn(Button.EVENT_CLICK, "VM(this).cmd('deviceOnline','" + devDevice.getDeviceSn() + "','false')"));
		top.add(new Html("").setWidth(20));
		if (totalList.size() > 0) {
			top.add(new Button("推送").setStyle("margin-top:5px").setCls("x-btn x-btn-main").setWidth(80).setOn(
					SubmitButton.EVENT_CLICK,
					"VM(this).cmd('push','" + devDevice.getDeviceSn() + "','" + deviceRunningState.isOnline() + "')"));
			top.add(new Html("").setWidth(20));
			top.add(new Button("返回").setStyle("margin-top:5px").setCls("x-btn").setWidth(80).setOn(Button.EVENT_CLICK,
					"VM(this).cmd('back')"));
			top.add(new Html("").setWidth(20));
		} else {
			top.add(new Button("返回").setStyle("margin-top:5px").setCls("x-btn").setWidth(80).setOn(Button.EVENT_CLICK,
					"VM(this).cmd('back')"));
			top.add(new Html("").setWidth(20));
		}

		HorizontalLayout status = new HorizontalLayout("status");
		status.add(new Html("").setWidth(28));
		status.add(new Label("", "状态:在线").setStyle("margin-top:5px").setWidth(55));
		status.add(new Checkbox("", "", deviceRunningState.isOnline() == true ? true : false, "", "").setId("Status1")
				.setName("Status1").setReadonly(true));
		gridLayoutFormPanel.add(0, 0, status);
		gridLayoutFormPanel.add(0, 1, new Label("产品唯一标识:", devProduct.getProductKey()));
		gridLayoutFormPanel.add(0, 2, new Label("设备名:", devDevice.getDeviceName()));
		gridLayoutFormPanel.add(1, 0,
				new Text("deviceSns", "", devDevice.getDeviceSn()).setId("deviceSns").setStyle("display:none"));
		gridLayoutFormPanel.add(1, 1, new Label("设备唯一标识:", devDevice.getDeviceSn()));
		gridLayoutFormPanel.add(1, 2, new Label("设备证书:", ""));

		FormPanel form = new FormPanel("form").setWidth("66%");
		GridPanel gridPanel = new GridPanel("gridpan").setScroll(true).setStyle("border-left:1px solid #CCC");
		List<DevProductFunction> point = DeviceControlService.changeList(functionPoint);
		gridPanel.setGridData(point);
		gridPanel
				.addColumn(GridColumn.text("functionName", "hardver", "功能点名称", "50").setAlign(GridColumn.ALIGN_CENTER));
		gridPanel.addColumn(GridColumn.text("value", "value", "设备当前数据", "50").setAlign(GridColumn.ALIGN_CENTER));

		int m = 0;
		for (int i = 0; i < totalList.size(); i++) {
			grid.add(m, 0, m, 1, totalList.get(i).get(0));
			grid.add(m, 2, m, 3, totalList.get(i).get(1));
			m++;
		}

		form.add(grid);
		btm.add(form);
		btm.add(gridPanel);
		verticalLayout.add(top);
		verticalLayout.add(new Html(
				"<hr style='filter:progid:DXImageTransform.Microsoft.Shadow(color:#CCC,direction:145,strength:15)' color='#CCC' width='100%' size='1' />")
						.setHeight(1),
				"-1");
		verticalLayout.add(gridLayoutFormPanel);
		verticalLayout.add(new Html(
				"<hr style='filter:progid:DXImageTransform.Microsoft.Shadow(color:#CCC,direction:145,strength:15)' color='#CCC' width='100%' size='1' />")
						.setHeight(1),
				"-1");
		verticalLayout.add(btm);
		view.add(verticalLayout);
		return view;
	}

	/**
	 * @刷新表格数据
	 * @param point
	 * @return
	 */
	public static View reFresh(List<DevProductFunction> point) {
		View view = new View();
		GridPanel gridPanel = new GridPanel("gridpan").setScroll(true).setStyle("border-left:1px solid #CCC");
		gridPanel.setGridData(point);
		gridPanel
				.addColumn(GridColumn.text("functionName", "hardver", "功能点名称", "50").setAlign(GridColumn.ALIGN_CENTER));
		gridPanel.addColumn(GridColumn.text("value", "value", "设备当前数据", "50").setAlign(GridColumn.ALIGN_CENTER));
		view.add(gridPanel);
		return view;
	}

	/**
	 * @替换label
	 * @param opentype
	 * @param labid
	 * @return
	 */
	public static View open(String opentype, String labid) {
		View view = new View();
		if ("false".equals(opentype)) {
			view.add(new Label("", "关闭").setStyle("margin-top:5px").setId(labid));
		} else {
			view.add(new Label("", "开启").setStyle("margin-top:5px").setId(labid));
		}
		return view;

	}

	/**
	 * @公式替换
	 * @param x
	 * @param id
	 * @return
	 */
	public static View formula(String x, String id) {
		View view = new View();
		Label lab = new Label("", x).setId(id);
		view.add(lab);
		return view;

	}

	/**
	 * @显示公式
	 * @param min
	 * @param value
	 * @param resolution
	 * @return
	 */
	public static String formula(double min, double value, double resolution) {
		// TODO Auto-generated method stub
		double range = 0;
		double result = 0;
		String formul;
		BigDecimal b = new BigDecimal((Double.valueOf(value) - min) / resolution);
		range = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		BigDecimal a = new BigDecimal(resolution * range + min);
		result = a.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		formul = "" + result + "=" + resolution + "*" + range + "+(" + min + ")";
		return formul;
	}
}
