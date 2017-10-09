package com.xrj.business.product.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import com.rongji.dfish.ui.Widget;
import com.rongji.dfish.ui.command.AlertCommand;
import com.rongji.dfish.ui.command.CommandGroup;
import com.rongji.dfish.ui.command.ConfirmCommand;
import com.rongji.dfish.ui.command.JSCommand;
import com.rongji.dfish.ui.command.ReplaceCommand;
import com.rongji.dfish.ui.command.TipCommand;
import com.rongji.dfish.ui.layout.HorizontalLayout;
import com.rongji.dfish.ui.layout.View;
import com.rongji.dfish.ui.widget.Html;
import com.xrj.api.dto.DevProduct;
import com.xrj.api.dto.DevProductFunction;
import com.xrj.api.em.DataTypeEnum;
import com.xrj.api.service.DevProductFunctionService;

import com.xrj.business.core.base.RemoteService;
import com.xrj.business.core.view.PubView;
import com.xrj.business.product.factory.BatchFactory;
import com.xrj.business.product.service.DeviceControlService;
import com.xrj.business.product.service.FactoryService;
import com.xrj.business.product.service.FunctionPointService;
import com.xrj.business.product.view.DevProductFunctionView;
import com.xrj.business.product.vo.WidgetVo;
import com.xrj.framework.util.HttpUtil;

/**
 * 
 * @author chenjunchao
 *
 */
@Controller
@RequestMapping("/product")
public class DevProductFunctionController {
	@Resource
	RemoteService remoteService;
	@Resource
	private DeviceControlService deviceControlService;
	@Resource
	private FunctionPointService functionPointService;
	
	/**
	 * @新增功能点界面
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/functionPoint/add")
	@ResponseBody
	public ReplaceCommand addFunctionPoint(HttpServletRequest request) throws Exception {
		List<WidgetVo> widgets = FactoryService.addPoint("add", null);
		List<Widget<?>> list = BatchFactory.create(widgets);
		HorizontalLayout range1 = FactoryService.createNumber("add",null);
		return new ReplaceCommand(DevProductFunctionView.modifyPoint(list,range1).findNodeById("ver"));
	}
	
	/**
	 * @构建对话框
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("createSDK")
	@ResponseBody
	public Object createSDK(HttpServletRequest request) throws Exception {	
		CommandGroup commandGroup = new CommandGroup();		
        View view  = DevProductFunctionView.buildSDKView();
		commandGroup.add(PubView.getDialogCommand("edit", "form", "发布前请新建SDK", PubView.DIALOG_SIZE_SMALL, null)
					.setNode(view));	   
		return commandGroup;
	}
	/**
	 * @发布功能点
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("publishPoint")
	@ResponseBody
	public Object publishPoint(HttpServletRequest request) throws Exception {	
		CommandGroup commandGroup = new CommandGroup();	
	    DevProduct devProduct = (DevProduct)HttpUtil.getSessionAttr(request, "product");		
		if(functionPointService.publishPoint(devProduct.getId())) {
			commandGroup.add(PubView.getInfoAlertNoTimeout("发布成功"));   
		    List<DevProductFunction> functionPoint= deviceControlService.findPointListByProductId(devProduct.getId());		
		    List<List<Widget<?>>> totalList = new ArrayList<List<Widget<?>>>();
			for(int i = 0; i < functionPoint.size(); i++) {
				List<WidgetVo> widgets = FactoryService.showPoint("",functionPoint.get(i),devProduct);
				List<Widget<?>> list = BatchFactory.create(widgets);
				totalList.add(list);
			}
		    commandGroup.add(new ReplaceCommand(DevProductFunctionView.showFunctionPoint(totalList,devProduct).findNodeById("ver")));		   
		    commandGroup.add(new JSCommand("var d=$.dialog(this);d.ownerView.cmd('toBack');$.close(this);"));
		} else {
			commandGroup.add(PubView.getInfoAlertNoTimeout("发布失败"));
		}
		return commandGroup;
	}
	/**
	 * @发布功能点前的检查
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("CheckPublish")
	@ResponseBody
	public Object checkPublish(HttpServletRequest request) throws Exception {	
		CommandGroup commandGroup = new CommandGroup();	
	    DevProduct devProduct = (DevProduct)HttpUtil.getSessionAttr(request, "product");		
		if(functionPointService.checkPoint(devProduct.getId())) {
		    commandGroup.add(PubView.getAjaxCommand("product/createSDK"));
		} else {
			commandGroup.add(PubView.getInfoAlertNoTimeout("没有可以发布的功能点"));
		}
		return commandGroup;
	}
	
	/**
	 * @变化spinner控件范围函数
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/functionPoint/changespin")
	@ResponseBody
	public ReplaceCommand changespin(HttpServletRequest request) throws Exception {		
		String min = request.getParameter("min");
		String max = request.getParameter("max");		
		ReplaceCommand re = new ReplaceCommand();
		if ((min != "" && min != null && min.matches("-?[0-9]+\\.?[0-9]*")) && (max != "" && max != null && max.matches("-?[0-9]+\\.?[0-9]*"))) {
			BigDecimal b = new BigDecimal(Double.parseDouble(max) - Double.parseDouble(min));
			double range = b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue(); 
			re.setNode(DevProductFunctionView.SelectChangeResolution(0.001, range).findNodeById("resolution"));
		} else {
		   re.setNode(DevProductFunctionView.SelectChangeResolution(0.001, 99999).findNodeById("resolution"));
		}
		return re;
	}
	
	/**
	 * @返回功能点主界面
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/backToAllPoint")
	@ResponseBody
	public ReplaceCommand backFunctionPoint(HttpServletRequest request) throws Exception {		
		DevProduct devProduct = (DevProduct)HttpUtil.getSessionAttr(request, "product");
		List<DevProductFunction> functionPoint= deviceControlService.findPointListByProductId(devProduct.getId());
		List<List<Widget<?>>> totalList = new ArrayList<List<Widget<?>>>();
		for(int i = 0; i < functionPoint.size(); i++) {
			List<WidgetVo> widgets = FactoryService.showPoint("",functionPoint.get(i),devProduct);
			List<Widget<?>> list = BatchFactory.create(widgets);
			totalList.add(list);
		}
		return new ReplaceCommand(DevProductFunctionView.showFunctionPoint(totalList,devProduct).findNodeById("ver"));
	}
	
	/**
	 * @功能点修改界面
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/functionPoint/modify")
	@ResponseBody
	public ReplaceCommand updateQueryFunctionPoint(HttpServletRequest request) throws Exception {	
		String id = request.getParameter("functionId");
		DevProductFunction functionPoint= deviceControlService.findPointById(id);
		List<WidgetVo> widgets = FactoryService.addPoint("", functionPoint);
		List<Widget<?>> list = BatchFactory.create(widgets);
		HorizontalLayout range1 = null ;
		if(functionPoint.getDataType().equals(DataTypeEnum.NUMBER.getValue())) {
			range1 = FactoryService.createNumber("",functionPoint);
		}
		if(functionPoint.getDataType().equals(DataTypeEnum.BOOLEAN.getValue())) {
			range1 = new HorizontalLayout("range1");
		}
		if(functionPoint.getDataType().equals(DataTypeEnum.ENUM.getValue())) {
			range1 = FactoryService.createEnum("", functionPoint);
		}
		if(functionPoint.getDataType().equals(DataTypeEnum.STRING.getValue())) {
			range1 = FactoryService.createString("", functionPoint);
		}
		return new ReplaceCommand(DevProductFunctionView.updatePoint(list,range1,functionPoint).findNodeById("ver"));
	}
	
	/**
	 * @查看某个功能点
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/showPoint")
	@ResponseBody
	public ReplaceCommand showFunctionPoint(HttpServletRequest request) throws Exception {	
		String id = request.getParameter("functionId");
		DevProductFunction functionPoint= deviceControlService.findPointById(id);
		DevProduct devProduct = (DevProduct)HttpUtil.getSessionAttr(request, "product");
		List<WidgetVo> widgets = FactoryService.showPoint("show",functionPoint,devProduct);
		List<Widget<?>> list = BatchFactory.create(widgets);
		return new ReplaceCommand(DevProductFunctionView.showPoint(list).findNodeById("ver"));
	}
	
	/**
	 * @显示产品所有功能点
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/functionPoint")
	@ResponseBody
	public Object showAllPoint(HttpServletRequest request) throws Exception {
		View view = new View();		
		DevProduct devProduct = (DevProduct)HttpUtil.getSessionAttr(request, "product");
		List<DevProductFunction> functionPoint = deviceControlService.findPointListByProductId(devProduct.getId());
		List<List<Widget<?>>> totalList = new ArrayList<List<Widget<?>>>();
		for(int i = 0; i < functionPoint.size(); i++) {
			List<WidgetVo> widgets = FactoryService.showPoint("",functionPoint.get(i),devProduct);
			List<Widget<?>> list = BatchFactory.create(widgets);
			totalList.add(list);
		}
		view =  DevProductFunctionView.showFunctionPoint(totalList,devProduct);
		return view;
	}
	
	/**
	 * @删除功能点确认函数
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/toDeletePoint")
	@ResponseBody
	public Object deleteconfirmFunctionPoint(HttpServletRequest request) throws Exception {	
		String id = request.getParameter("functionId");
		CommandGroup commandGroup = new CommandGroup();	
		commandGroup.add(new ConfirmCommand("确认删除?",PubView.getAjaxCommand("product/functionPoint/delete?id="+id+"")));    
		return commandGroup;
	}
	
	/**
	 * @删除功能点
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/functionPoint/delete")
	@ResponseBody
	public ReplaceCommand deleteFunctionPoint(HttpServletRequest request) throws Exception {	
		DevProductFunctionService devProductFunctionService = remoteService.getDevProductFunctionService();		
		DevProduct devProduct = (DevProduct)HttpUtil.getSessionAttr(request, "product");
		String id = request.getParameter("id");    
		devProductFunctionService.delete(id);	
		List<DevProductFunction> functionPoint= deviceControlService.findPointListByProductId(devProduct.getId());	
		List<List<Widget<?>>> totalList = new ArrayList<List<Widget<?>>>();
		for(int i = 0; i < functionPoint.size(); i++) {
			List<WidgetVo> widgets = FactoryService.showPoint("",functionPoint.get(i),devProduct);
			List<Widget<?>> list = BatchFactory.create(widgets);
			totalList.add(list);
		}
		return new ReplaceCommand(DevProductFunctionView.showFunctionPoint(totalList,devProduct).findNodeById("ver"));
	}
	
	/**
	 * @修改功能点
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/functionPoint/update")
	@ResponseBody
	public Object updateFunctionPoint(HttpServletRequest request) throws Exception{
		DevProduct devProduct = (DevProduct)HttpUtil.getSessionAttr(request, "product");
		CommandGroup commandGroup = new CommandGroup();		
		if(functionPointService.updatePoint(request)) {	
			commandGroup.add(new TipCommand("修改成功"));	
			List<DevProductFunction> functionPoint= deviceControlService.findPointListByProductId(devProduct.getId());
			List<List<Widget<?>>> totalList = new ArrayList<List<Widget<?>>>();
			for(int i = 0; i < functionPoint.size(); i++) {
				List<WidgetVo> widgets = FactoryService.showPoint("",functionPoint.get(i),devProduct);
				List<Widget<?>> list = BatchFactory.create(widgets);
				totalList.add(list);
			}
			commandGroup.add(new ReplaceCommand(DevProductFunctionView.showFunctionPoint(totalList,devProduct).findNodeById("ver")));
		} else {
			commandGroup.add(PubView.getInfoAlertNoTimeout("标识名称重复,请重新输入"));
		}
		return commandGroup;
	}
	
	/**
	 * 新增功能点
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/functionPoint/save")
	@ResponseBody
	public Object saveFunctionPoint(HttpServletRequest request) throws Exception{
		DevProduct devProduct = (DevProduct)HttpUtil.getSessionAttr(request, "product");
		CommandGroup commandGroup = new CommandGroup();		
		if(functionPointService.savePoint(request)) {	
			commandGroup.add(new TipCommand("新增成功"));	
			List<DevProductFunction> functionPoint = deviceControlService.findPointListByProductId(devProduct.getId());
			List<List<Widget<?>>> totalList = new ArrayList<List<Widget<?>>>();
			for(int i = 0; i < functionPoint.size(); i++) {
				List<WidgetVo> widgets = FactoryService.showPoint("",functionPoint.get(i),devProduct);
				List<Widget<?>> list = BatchFactory.create(widgets);
				totalList.add(list);
			}
			commandGroup.add(new ReplaceCommand(DevProductFunctionView.showFunctionPoint(totalList,devProduct).findNodeById("ver")));
		} else {
			commandGroup.add(PubView.getInfoAlertNoTimeout("标识名称重复,请重新输入"));
		}
		return commandGroup;
	}
	
	/**
	 * @select控件变换函数
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/selectCommand")
	public Object selectCommand(HttpServletRequest request){
		String selectValue=request.getParameter("selectValue");
		ReplaceCommand replaceCommand = null;
		if (selectValue.equals("0")) {
			replaceCommand = new ReplaceCommand(DevProductFunctionView.SelectChangeView0().findNodeById("range1"));
		}
		if (selectValue.equals("1")) {
			replaceCommand = new ReplaceCommand(DevProductFunctionView.SelectChangeView1().findNodeById("range1"));
		}
		if (selectValue.equals("2")) {
			replaceCommand = new ReplaceCommand(DevProductFunctionView.SelectChangeView2().findNodeById("range1"));
		}
		if (selectValue.equals("3")) {
			replaceCommand = new ReplaceCommand(DevProductFunctionView.SelectChangeView3().findNodeById("range1"));
		}
		if (selectValue.equals("4")) {
			replaceCommand = new ReplaceCommand(DevProductFunctionView.SelectChangeView4().findNodeById("range1"));
		}
		return replaceCommand;
	}
	/*@RequestMapping("/selectCommand")
	@ResponseBody
	public Object selectCommand(HttpServletRequest request){
		String selectValue=request.getParameter("selectValue");
		System.out.println(selectValue);
		ReplaceCommand replaceCommand = null;
		
		if(selectValue.equals("0")){
			replaceCommand=new ReplaceCommand(FunctionPointView.modifyPoint().findNodeById("range1"));
		}
		if(selectValue.equals("1")){
			replaceCommand=new ReplaceCommand(FunctionPointView.SelectChangeView1().findNodeById("range1"));
		}
		if(selectValue.equals("2")){
			replaceCommand=new ReplaceCommand(FunctionPointView.SelectChangeView2().findNodeById("range1"));
		}
		if(selectValue.equals("3")){
			replaceCommand=new ReplaceCommand(FunctionPointView.SelectChangeView3().findNodeById("range1"));
		}
		if(selectValue.equals("4")){
			replaceCommand=new ReplaceCommand(FunctionPointView.SelectChangeView4().findNodeById("range1"));
		}
		return replaceCommand;
	}*/
	/*@RequestMapping("/certification")
	@ResponseBody
	public Object certificationTelAndID(HttpServletRequest request) throws Exception {
		View view = new View();		
		view =  FunctionPointView.buildCertificationTelAndIDView();		
		return view;
	}
		
	@RequestMapping("/waiting")
	@ResponseBody
	public Object waitingCertification(HttpServletRequest request) {
		View view = new View();		
		String telePhone = request.getParameter("telePhone");
		String ID = request.getParameter("ID");
		view = FunctionPointView.waitingCertification(telePhone, ID);				
		return view;
	}
	
	/*@RequestMapping("/leftMenu")
	@ResponseBody
	public Object treePage(HttpServletRequest request){
		String anchor=request.getParameter("anchor");
		View view = FunctionPointView.buildAllView(anchor);
		return view;
	}
	
	/*@RequestMapping("/clickMenu")
	@ResponseBody
	public Object clickMenu(HttpServletRequest request) throws Exception {
		String menuId = request.getParameter("menuId");
		CommandGroup cg = new CommandGroup();
		Menu focusMenu = null;
		for (Menu menu : DeveloperCertificationView.getMenuData()) {
			if (menu.getMenuId().equals(menuId)) {
				focusMenu = menu;
			}
		}
		if (focusMenu != null) {
			View view = PubView.buildBodyView();
			//Html mainTitle = (Html) view.findNodeById(PubView.MAIN_TITLE);
			//mainTitle.setText(focusMenu.getMenuName());
			//cg.add(new ReplaceCommand(mainTitle));
		}
		cg.add(new JSCommand("VM(this).find('mainContent').view('v_" + menuId + "');"));
		return cg;
	}
	*/
	
	/*@RequestMapping("/information")
	@ResponseBody
	public Object adminInformation(){
		View view = new View();
		view.addCommand("saveInfo",new SubmitCommand("admin/update"));
		GridLayoutFormPanel formPanel = new GridLayoutFormPanel("info_form");
		formPanel.add(0, 0, new Text("companyNum","账号:", "窝窝我").setDisabled(true));
		formPanel.add(0, 1, new Text("companyName","联系人名称:","222"));
		formPanel.add(1, 0, new Html("").setHeight(10));
		formPanel.add(1, 1, new Html("").setHeight(10));
		formPanel.add(2, 0, new Text("nickname","用户名(昵称):","我企鹅去"));	
		formPanel.add(2, 1, new Text("qq","QQ:",""));
		formPanel.add(3, 0, new Html("").setHeight(10));
		formPanel.add(3, 1, new Html("").setHeight(10));
		formPanel.add(4, 0, new Text("phone","手机:",""));
		formPanel.add(4, 1, new Text("email","联系人邮箱:",""));
		formPanel.add(5, 0, new Html("").setHeight(10));
		formPanel.add(5, 1, new Html("").setHeight(10));
		formPanel.add(6, 0, new Text("ID","身份证:","").setDisabled(true));
		
		HorizontalLayout selectHor= new HorizontalLayout("addr");
		Label labelAddr = new Label("", "所在地区:");
		selectHor.add(labelAddr)
				 .add(new Select("pro", "", "", Arrays.asList(new Object[][]{{"0","省"},{"0","福州"}})))
				 .add(new Select("city", "", "", Arrays.asList(new Object[][]{{"0","市"},{"1","福州"}})))
				 .add(new Select("coun", "", "", Arrays.asList(new Object[][]{{"0","区、乡镇"},{"1","广州"}})));
		selectHor.setAlign("right").setValign(Valignable.VALIGN_MIDDLE);
		System.out.println(selectHor);
		formPanel.add(6, 1, selectHor);
		formPanel.add(7, 0, new Html("").setHeight(10));
		formPanel.add(7, 1, new Html("").setHeight(10));
		/*HorizontalLayout horizontalLayout=new HorizontalLayout("addrHor");
		HorizontalLayout horLabel= new HorizontalLayout("");
		Label label = new Label("","地址:").setWidth(71);
		horLabel.add(new Html(""));
		horLabel.add(label).setWidth(71);
				//CommonView.setPositionLayout(1, 0, new Label("","地址")).setWidth(91);
		horizontalLayout.add(horLabel);
		
		horizontalLayout.add(new Text("address","",""));
		horizontalLayout.setAlign("center");
		horizontalLayout.add(new Html(""));
		formPanel.add(8, 0, 8, 1, horizontalLayout);
		formPanel.add(8, 0, new Text("address","地址:","").setWidth(350));
		HorizontalLayout btnLayout= new HorizontalLayout("btnHor");
		SubmitButton submitButton = new SubmitButton("保存").setCls("btn").setOn(SubmitButton.EVENT_CLICK, "VM(this).cmd('saveInfo')");
		Button button = new Button("取消").setCls("btn").setOn(Button.EVENT_CLOSE, null);
		
		ButtonBar buttonBar = new ButtonBar("buttonBar").add(submitButton).add(button);
		buttonBar.setSpace(30);
		btnLayout.add(new Html(""));
		//btnLayout.add(new Html(""));
		btnLayout.add(buttonBar);
		btnLayout.add(new Html(""));
		btnLayout.add(new Html(""));
		formPanel.add(9, 0, new Html("").setHeight(10));
		formPanel.add(9, 1, new Html("").setHeight(10));
		formPanel.add(10, 0, 10, 2, btnLayout);
		//verticalLayout.add(formPanel);
		//verticalLayout.add(new Html("").setWidth(50));
		//verticalLayout.setAlign("center");
		view.setNode(formPanel);
		
		//LabelRow<LabelRow<Text>> lab =new LabelRow(); 
		return view;
	}*/
}
