package com.xrj.business.product.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.rongji.dfish.ui.command.CommandGroup;
import com.rongji.dfish.ui.command.DialogCommand;
import com.rongji.dfish.ui.command.JSCommand;
import com.rongji.dfish.ui.command.ReplaceCommand;
import com.rongji.dfish.ui.command.TipCommand;
import com.rongji.dfish.ui.helper.FormPanel;
import com.rongji.dfish.ui.helper.HorizontalGroup;
import com.rongji.dfish.ui.layout.View;
import com.xrj.api.dto.DevDeveloper;
import com.xrj.api.dto.SysDataDict;
import com.xrj.api.dto.SysResource;
import com.xrj.api.em.AuditStatusEnum;
import com.xrj.api.service.DevCodeService;
import com.xrj.api.support.CallResult;
import com.xrj.business.core.base.PubConstants;
import com.xrj.business.core.base.RemoteService;
import com.xrj.business.core.view.IndexView;
import com.xrj.business.core.view.PubView;
import com.xrj.business.product.service.DeveloperBusiness;
import com.xrj.business.product.view.AddNewProductView;
import com.xrj.business.product.view.DevDeveloperView;
import com.xrj.framework.util.Assert;
import com.xrj.framework.util.HttpUtil;

@Controller
@RequestMapping("/developer")
public class DevDeveloperController {
	@Resource
	private DeveloperBusiness developerBusiness;
	@Resource
	private RemoteService remoteService;
	
	/**
	 * @注册对话框
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/toReg")
	@ResponseBody
	public Object toLogin(HttpServletRequest request) throws Exception {
		DialogCommand dialog = PubView.getDialogCommand("toReg", "std", "星榕物联网开发者注册", PubView.DIALOG_SIZE_LARGE, null).setWidth(DialogCommand.WIDTH_SMALL).setCover(true);
		dialog.setNode(DevDeveloperView.buildToRegView(480));
		return dialog;
	}
	
	/**
	 * @协议对话框
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/showAgree")
	@ResponseBody
	public Object showAgree(HttpServletRequest request) throws Exception {
		DialogCommand dialog = new DialogCommand("showAgree", "std", "星榕物联网使用协议",  DialogCommand.WIDTH_LARGE, DialogCommand.HEIGHT_LARGE, null, "");
		dialog.setNode(DevDeveloperView.buildAgreeView());
		return dialog;
	}
	
	/**
	 * @找回密码对话框
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/findPwd")
	@ResponseBody
	public Object findPwd(HttpServletRequest request) throws Exception {
		DialogCommand dialog = new DialogCommand("checkCo", "std", "忘记密码", "480", "300", DialogCommand.POSITION_MIDDLE, null);			
	    dialog.setNode(DevDeveloperView.buildFindPwdView());
		return dialog;
	}
	
	/**
	 * @激活对话框
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/toactive")
	@ResponseBody
	public Object toActive(HttpServletRequest request) throws Exception {
		String auth = request.getParameter("auth");
		DialogCommand dialog = new DialogCommand("active", "std", "请激活账号后登录","400","160", DialogCommand.POSITION_MIDDLE, "");
		dialog.setNode(DevDeveloperView.buildActiveView(auth));
		return dialog;
	}
	
	/**
	 * @开发者类型对话框
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/developertype")
	@ResponseBody
	public Object developertype(HttpServletRequest request) throws Exception {
		DevDeveloper devDeveloper = (DevDeveloper) HttpUtil.getSessionAttr(request, "devDeveloper");
		if(Assert.isEmpty(devDeveloper.getUserType())){
		    DialogCommand dialog = new DialogCommand("devtype", "std", "请先选择您的开发者属性","600","360", DialogCommand.POSITION_MIDDLE, "");
		    dialog.setNode(DevDeveloperView.buildTypeView());
		    return dialog;
		} 
		String type;
		if(devDeveloper.getUserType().equals("1")) {
			type = "person";
		} else {
			type = "enterprise";
		}
		return PubView.getAjaxCommand("developer/personCert?type="+type+"");
	}
	
	/**
	 * @开发者认证对话框
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/personCert")
	@ResponseBody
	public Object personCert(HttpServletRequest request) throws Exception {
		String type = request.getParameter("type");
		DevDeveloper devDeveloper = (DevDeveloper) HttpUtil.getSessionAttr(request, "devDeveloper");
		DialogCommand dialog = null ;
		List<SysResource> listMenu = new ArrayList<SysResource>();
		HttpUtil.setSessionAttr(request, "clicked", false);
		String anchor = "0101";
		HttpUtil.setSessionAttr(request, "clickMenuId", anchor);
		SysResource parent = new SysResource("0100","开发者管理");
	    SysResource info = new SysResource("0101","账号信息");
	    SysResource cert = new SysResource("0102","实名认证");
	    parent.setParentId("0100");
		if("person".equals(type)) {
		    dialog = new DialogCommand("personCert", "std", "开发者管理——个人开发者",DialogCommand.WIDTH_LARGE, DialogCommand.HEIGHT_LARGE, DialogCommand.POSITION_MIDDLE, "");		   
		    devDeveloper.setUserType("1");
		    remoteService.getDevDeveloperService().updateDevDeveloper(devDeveloper);
		    info.setUri("developer/personInfo");	
	        info.setParentId("01");
	        cert.setUri("developer/personRealCert");
	        cert.setParentId("01");
	        listMenu.add(parent);
		    listMenu.add(info);
		    listMenu.add(cert);	
		    dialog.setNode(DevDeveloperView.buildPersonView(listMenu,anchor));
		} else {
			dialog = new DialogCommand("enterpriseCert", "std", "开发者管理——企业开发者",DialogCommand.WIDTH_LARGE, DialogCommand.HEIGHT_LARGE, DialogCommand.POSITION_MIDDLE, "");		   
			devDeveloper.setUserType("2");
			remoteService.getDevDeveloperService().updateDevDeveloper(devDeveloper);
			info.setUri("developer/enterpriseInfo");	
		    info.setParentId("01");
		    cert.setUri("developer/enterpriseRealCert");
		    cert.setParentId("01");
		    listMenu.add(parent);
			listMenu.add(info);
			listMenu.add(cert);	
			dialog.setNode(DevDeveloperView.buildPersonView(listMenu,anchor));
		}
		return dialog;
	}
	
	/**
	 * @个人开发者信息界面
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/personInfo")
	@ResponseBody
	public Object personInfo(HttpServletRequest request) throws Exception {
		View view = new View();
		DevDeveloper devDeveloper = (DevDeveloper) HttpUtil.getSessionAttr(request, "devDeveloper");
		view = DevDeveloperView.bulidPersonInfo(devDeveloper);
		return view;
	}
	
	/**
	 * @个人开发者实名认证界面
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/personRealCert")
	@ResponseBody
	public Object personRealCert(HttpServletRequest request) throws Exception {
		View view = new View();
		DevDeveloper devDeveloper = (DevDeveloper) HttpUtil.getSessionAttr(request, "devDeveloper");
		view = DevDeveloperView.bulidPersonCert(devDeveloper);
		return view;
	}
	
	/**
	 * @企业开发者信息界面
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/enterpriseInfo")
	@ResponseBody
	public Object enterpriseInfo(HttpServletRequest request) throws Exception {
		View view = new View();
		DevDeveloper devDeveloper = (DevDeveloper) HttpUtil.getSessionAttr(request, "devDeveloper");
		view = DevDeveloperView.bulidEnterInfo(devDeveloper);
		return view;
	}
	
	/**
	 * @企业开发者实名认证界面
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/enterpriseRealCert")
	@ResponseBody
	public Object enterpriseRealCert(HttpServletRequest request) throws Exception {
		View view = new View();
		DevDeveloper devDeveloper = (DevDeveloper) HttpUtil.getSessionAttr(request, "devDeveloper");
		view = DevDeveloperView.bulidEnterCert(devDeveloper);
		return view;
	}
	
	/**
	 * @个人开发者数据更新
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/personUpdate")
	@ResponseBody
	public Object personUpdate(HttpServletRequest request) throws Exception {
		CommandGroup commandGroup = new CommandGroup();	
		DevDeveloper devDevelop = (DevDeveloper) HttpUtil.getSessionAttr(request, "devDeveloper");
		String auth = devDevelop.getLoginName();
		CallResult callresult = developerBusiness.updateDeveloper(request,devDevelop);
		if(callresult.isSuccess()) {
			//更新session的开发者信息
			request.getSession().removeAttribute("devDeveloper");
			DevDeveloper devDeveloper = remoteService.getDevDeveloperService().getByLoginName(auth);
			HttpUtil.setSessionAttr(request, "devDeveloper", devDeveloper);		
			commandGroup.add(new JSCommand("VM(this).reload('developer/personInfo')"));	
			commandGroup.add(new TipCommand("修改成功"));
		} else {
			commandGroup.add(PubView.getInfoAlertNoTimeout("没有修改数据"));
		}
		return commandGroup;
	}
	
	/**
	 * @个人开发者第一次认证或失败后重新认证
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/personCertInfo")
	@ResponseBody
	public Object personCertInfo(HttpServletRequest request) throws Exception {
		CommandGroup commandGroup = new CommandGroup();	
		DevDeveloper devDevelop = (DevDeveloper) HttpUtil.getSessionAttr(request, "devDeveloper");
		devDevelop.setPhone(request.getParameter("tel"));
		devDevelop.setIdcard(request.getParameter("IdCard"));
		devDevelop.setAuditStatus(AuditStatusEnum.HANDLING.getIndex());
		remoteService.getDevDeveloperService().updateDevDeveloper(devDevelop);
		//更新session
		request.getSession().removeAttribute("devDeveloper");
		DevDeveloper devDeveloper = remoteService.getDevDeveloperService().getByLoginName(devDevelop.getLoginName());
		HttpUtil.setSessionAttr(request, "devDeveloper", devDeveloper);		
		
		commandGroup.add(new JSCommand("VM(this).reload('developer/personRealCert')"));
		commandGroup.add(new TipCommand("提交审核成功"));
		return commandGroup;
	}
	
	/**
	 * @企业开发者第一次认证或失败后重新认证
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/enterPriseCertInfo")
	@ResponseBody
	public Object enterPriseCertInfo(HttpServletRequest request) throws Exception {
		CommandGroup commandGroup = new CommandGroup();	
		DevDeveloper devDevelop = (DevDeveloper) HttpUtil.getSessionAttr(request, "devDeveloper");	
		developerBusiness.certEnterDeveloper(request, devDevelop);
		//更新session
		request.getSession().removeAttribute("devDeveloper");
		DevDeveloper devDeveloper = remoteService.getDevDeveloperService().getByLoginName(devDevelop.getLoginName());
		HttpUtil.setSessionAttr(request, "devDeveloper", devDeveloper);		
		commandGroup.add(new JSCommand("VM(this).reload('developer/enterpriseRealCert')"));
		commandGroup.add(new TipCommand("提交审核成功"));
		return commandGroup;
	}
	
	/**
	 * @企业开发者数据更新
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/enterPriseUpdate")
	@ResponseBody
	public Object enterPriseUpdate(HttpServletRequest request) throws Exception {
		CommandGroup commandGroup = new CommandGroup();	
		DevDeveloper devDevelop = (DevDeveloper) HttpUtil.getSessionAttr(request, "devDeveloper");
		String auth = devDevelop.getLoginName();
		CallResult callresult = developerBusiness.updateEnterDeveloper(request, devDevelop);
		if(callresult.isSuccess()) {
			//更新session的开发者信息
			request.getSession().removeAttribute("devDeveloper");
			DevDeveloper devDeveloper = remoteService.getDevDeveloperService().getByLoginName(auth);
			HttpUtil.setSessionAttr(request, "devDeveloper", devDeveloper);		
			commandGroup.add(new JSCommand("VM(this).reload('developer/enterpriseInfo')"));	
			commandGroup.add(new TipCommand("修改成功"));
		} else {
			commandGroup.add(PubView.getInfoAlertNoTimeout("没有修改数据"));
		}
		return commandGroup;
	}
	
	/**
	 * 左侧菜单点击改变视图命令
	 * 
	 * @param request
	 * @return 控件替换命令
	 * @throws Exception
	 */
	@RequestMapping("/clickMenu")
	@ResponseBody
	public Object clickMenu(HttpServletRequest request) throws Exception {

		String menuId = request.getParameter("menuId");
		CommandGroup cg = new CommandGroup();		
		boolean clicked = (boolean) HttpUtil.getSessionAttr(request, "clicked");
		String clickMenuId = (String) HttpUtil.getSessionAttr(request, "clickMenuId");
		if (clicked == true) {
			if (clickMenuId.equals(menuId)) {
				cg.add(new JSCommand("VM(this).find('mainContent').view('v_" + menuId + "');"));
				cg.add(new JSCommand("VM(this).find('v_" + menuId + "').reload();"));
			} else {
				HttpUtil.setSessionAttr(request, "clickMenuId", menuId);
				cg.add(new JSCommand("VM(this).find('mainContent').view('v_" + menuId + "');"));
				HttpUtil.setSessionAttr(request, "clicked", true);
			}
		} else {
			cg.add(new JSCommand("VM(this).find('mainContent').view('v_" + menuId + "');"));
			HttpUtil.setSessionAttr(request, "clicked", true);
		}
		return cg;
	}
	
	/**
	 * @判断开发者类型替换对话框
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/toCertification")
	@ResponseBody
	public Object toCertification(HttpServletRequest request) throws Exception {
		String type = request.getParameter("devtype");		
		CommandGroup cg = new CommandGroup();
		if("1".equals(type)) {
			cg.add(new JSCommand("var d=$.dialog(this);d.ownerView.cmd('personCert','person');$.close(this);"));		
		} else {
			cg.add(new JSCommand("var d=$.dialog(this);d.ownerView.cmd('personCert','enterprise');$.close(this);"));	
		}
		return cg;
	}
	
	/**
	 * @发送验证码
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/sendCode")
	@ResponseBody
	public Object sendCode(HttpServletRequest request) throws Exception {
		CommandGroup cg = new CommandGroup();
		String email = request.getParameter("email");
		if(Assert.isEmpty(email)) {
			cg.add(new TipCommand("邮箱不能为空！").setSnap("userEmail").setSnaptype("tb"));
		} else {
			CallResult res = developerBusiness.sendCode(email);
			if(res.isSuccess()) {
			    cg.add(new TipCommand("验证码已发送到该邮箱！").setSnap("userEmail").setSnaptype("tb"));
			} else {
				cg.add(new TipCommand("后台出错，发送失败！").setSnap("userEmail").setSnaptype("tb"));
			}		
		}
		return cg;
	}
	
	/**
	 * @检查验证码
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/checkCode")
	@ResponseBody
	public Object checkCode(HttpServletRequest request) throws Exception {
		CommandGroup cg = new CommandGroup();
		String email = request.getParameter("userEmail");
		String code = request.getParameter("code");
		String password = request.getParameter("pwd");
		CallResult res = developerBusiness.findPassword(code, email, password);
		if(res.isSuccess()) {
			 cg.add(PubView.getInfoAlertNoTimeout("修改成功"));
			 cg.add(new JSCommand("pub.dialogClose(this)"));
		} else {
			 cg.add(new TipCommand("验证码错误或已失效").setSnap("code").setSnaptype("tb"));
		}
			
		return cg;
	}
	
	/**
	 * @激活账号
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/active")
	@ResponseBody
	public Object active(HttpServletRequest request) throws Exception {
		CommandGroup cg = new CommandGroup();
		String email = request.getParameter("userEmail");
		DevDeveloper devDeveloper = remoteService.getDevDeveloperService().getByLoginName(email);
		CallResult res = developerBusiness.activeDeveloper(devDeveloper.getId());
		if(res.isSuccess()) {
			cg.add(PubView.getInfoAlert("激活成功"));
			cg.add(new JSCommand("pub.dialogClose(this)"));
			return cg;		
		} else {
			cg.add(new TipCommand(res.getMessage()));
		}
		return cg;
	}
	
	/**
	 * @注册账号
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/doReg")
	@ResponseBody
	public Object doReg(HttpServletRequest request) throws Exception {
		String email = request.getParameter("userEmail");	
		String pwd = request.getParameter("pwd");		
		int result = developerBusiness.register(email, pwd);
		if(result == 0) {
			return new TipCommand("该邮箱已注册！").setSnap("userEmail").setSnaptype("tb");
		} else {
			CommandGroup cg = new CommandGroup();
			cg.add(PubView.getInfoAlert("注册成功"));
			cg.add(new JSCommand("pub.dialogClose(this)"));
			return cg;		
		}	
	}
	
	/**
	 * @省改变市
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/changeCity")
	@ResponseBody
	public Object dochangeCity(HttpServletRequest request) throws Exception {
		String code = request.getParameter("code");	
		Map<String, List<SysDataDict>> mapClass = DeveloperBusiness.buildAreaClassListMap(code);
		CommandGroup cGroup = new CommandGroup();
		cGroup.add(new ReplaceCommand(DevDeveloperView.buildSelect2(mapClass.get("list2")).findNodeById("City")));
		if (mapClass.get("list3").isEmpty()) {
			cGroup.add(new JSCommand("VM(this).find('Area').display(false)"));
		} else {
			cGroup.add(new ReplaceCommand(DevDeveloperView.buildSelect3(mapClass.get("list3")).findNodeById("Area")));
		}
		return cGroup;
	}
	
	/**
	 * @市改变区
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/changeArea")
	@ResponseBody
	public Object changeArea(HttpServletRequest request) throws Exception {
		String code = request.getParameter("code");	
		Map<String, List<SysDataDict>> mapClass = DeveloperBusiness.buildAreaClassListMap(code);
		CommandGroup cGroup = new CommandGroup();
		if (mapClass.get("list3").isEmpty()) {
			cGroup.add(new JSCommand("VM(this).find('Area').display(false)"));
		} else {
		    cGroup.add(new ReplaceCommand(DevDeveloperView.buildSelect3(mapClass.get("list3")).findNodeById("Area")));
		}
	    return cGroup;
	}
	
	/**
	 * @大类改变小类
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/changeSmallClass")
	@ResponseBody
	public Object changeSmallClass(HttpServletRequest request) throws Exception {
		String code = request.getParameter("code");	
		Map<String, List<SysDataDict>> mapClass = DeveloperBusiness.buildTradeClassListMap(code);
		CommandGroup cGroup = new CommandGroup();
		if (mapClass.get("list2").isEmpty()) {
			cGroup.add(new JSCommand("VM(this).find('smallClass').display(false)"));
			cGroup.add(new ReplaceCommand(DevDeveloperView.buildSele3(mapClass.get("list3")).findNodeById("classType")));
		} else {		
		    cGroup.add(new ReplaceCommand(DevDeveloperView.buildSele2(mapClass.get("list2")).findNodeById("smallClass")));
		    cGroup.add(new ReplaceCommand(DevDeveloperView.buildSele3(mapClass.get("list3")).findNodeById("classType")));
		}
	    return cGroup;
	}
	
	/**
	 * @小类改变具体行业
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/changeClassType")
	@ResponseBody
	public Object changeClassType(HttpServletRequest request) throws Exception {
		String code = request.getParameter("code");	
		Map<String, List<SysDataDict>> mapClass = DeveloperBusiness.buildTradeClassListMap(code);
		CommandGroup cGroup = new CommandGroup();
		if (mapClass.get("list3").isEmpty()) {
			cGroup.add(new JSCommand("VM(this).find('classType').display(false)"));
		} else {
		    cGroup.add(new ReplaceCommand(DevDeveloperView.buildSele3(mapClass.get("list3")).findNodeById("classType")));
		}
	    return cGroup;
	}
}
