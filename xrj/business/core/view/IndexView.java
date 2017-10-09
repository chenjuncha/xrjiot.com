package com.xrj.business.core.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import org.springframework.util.CollectionUtils;

import com.rongji.dfish.base.Utils;
import com.rongji.dfish.framework.FrameworkHelper;
import com.rongji.dfish.ui.Widget;
import com.rongji.dfish.ui.command.JSCommand;
import com.rongji.dfish.ui.command.MenuCommand;
import com.rongji.dfish.ui.form.AbstractInput;
import com.rongji.dfish.ui.form.Password;
import com.rongji.dfish.ui.form.Text;
import com.rongji.dfish.ui.form.Validate;
import com.rongji.dfish.ui.helper.FormPanel;
import com.rongji.dfish.ui.helper.HorizontalGroup;
import com.rongji.dfish.ui.layout.AbstractLayout;
import com.rongji.dfish.ui.layout.ButtonBar;
import com.rongji.dfish.ui.layout.FrameLayout;
import com.rongji.dfish.ui.layout.HorizontalLayout;
import com.rongji.dfish.ui.layout.VerticalLayout;
import com.rongji.dfish.ui.layout.View;
import com.rongji.dfish.ui.widget.Button;
import com.rongji.dfish.ui.widget.DialogTemplate;
import com.rongji.dfish.ui.widget.Html;
import com.rongji.dfish.ui.widget.Img;
import com.rongji.dfish.ui.widget.Leaf;
import com.rongji.dfish.ui.widget.SubmitButton;
import com.rongji.dfish.ui.widget.TemplateTitle;
import com.rongji.dfish.ui.widget.TemplateView;
import com.rongji.dfish.ui.widget.TreePanel;
import com.xrj.api.dto.DevDeveloper;
import com.xrj.api.dto.DevProduct;
import com.xrj.api.dto.SysManager;
import com.xrj.api.dto.SysResource;
import com.xrj.business.core.base.PubConstants;
import com.xrj.business.core.web.LoginController;
import com.xrj.framework.util.HttpUtil;
import com.xrj.framework.util.StringUtils;


/**
 * 默认进入系统的引导页面
 * @author PAM Team
 *
 */
public class IndexView {

	/**
* 对话框模板-默认对话框,一般都是使用这个对话框
	 * @return
	 */
	public static DialogTemplate getStdTemplate() {
		DialogTemplate std=new DialogTemplate().setCls("dlg-std");
	
		VerticalLayout vp=new VerticalLayout(null);
		std.setNode(vp);
		HorizontalLayout head= new HorizontalLayout(null).setCls("dlg-std-head");
		vp.add(head,"40");
		head.add(new TemplateTitle(null).setCls("dlg-std-title").setWmin(10).setOn(Html.EVENT_DBLCLICK, "$.dialog(this).max(this)"),"*");
		head.add(new Html(null,"<i class=ico-dlg-max></i>").setCls("dlg-max").setAlign("center").setValign("middle").setOn(Html.EVENT_CLICK, "$.dialog(this).max(this)"),"40");
		head.add(new Html(null,"<i class=ico-dlg-close></i>").setCls("dlg-close").setAlign("center").setValign("middle").setOn(Html.EVENT_CLICK, 
				"pub.dialogClose(this);"),"40");
		
		TemplateView cont=new TemplateView(null);
		vp.add(cont,"*");
		return std;
	}
	
	/**
	 * 对话框模板-表单对话框,表单有变动时关闭有进行表单是否保存的提示
	 * @return
	 */
	public static DialogTemplate getFormTemplate() {
		DialogTemplate std=new DialogTemplate().setCls("dlg-std");
	
		VerticalLayout vp=new VerticalLayout(null);
		std.setNode(vp);
		HorizontalLayout head= new HorizontalLayout(null).setCls("dlg-std-head");
		vp.add(head,"40");
		head.add(new TemplateTitle(null).setCls("dlg-std-title").setWmin(10).setOn(Html.EVENT_DBLCLICK, "$.dialog(this).max(this)"),"*");
		head.add(new Html(null,"<i class=ico-dlg-max></i>").setCls("dlg-max").setAlign("center").setValign("middle").setOn(Html.EVENT_CLICK, "$.dialog(this).max(this)"),"40");
		head.add(new Html(null,"<i class=ico-dlg-close></i>").setCls("dlg-close").setAlign("center").setValign("middle").setOn(Html.EVENT_CLICK, 
				"var dlg=$.dialog(this);if(!dlg.contentView.isModified()){pub.dialogClose(dlg);}else{" +
				"$.dialog(this).contentView.cmd({type:'confirm',text:'您有内容尚未保存，确认关闭窗口吗？'," +
				"yes:function(){pub.dialogClose(dlg);}});" +
				"}"),"40");
		
		TemplateView cont=new TemplateView(null);
		vp.add(cont,"*");
		return std;
	}
	
	/**
	 * 对话框模板-子模块信息编辑框,关闭时需将子信息回调到主信息模块
	 * @return
	 */
	public static DialogTemplate getCallbackTemplate() {
		DialogTemplate std=new DialogTemplate().setCls("dlg-std");
	
		VerticalLayout vp=new VerticalLayout(null);
		std.setNode(vp);
		HorizontalLayout head= new HorizontalLayout(null).setCls("dlg-std-head");
		vp.add(head,"40");
		head.add(new TemplateTitle(null).setCls("dlg-std-title").setWmin(10).setOn(Html.EVENT_DBLCLICK, "$.dialog(this).max(this)"),"*");
		head.add(new Html(null,"<i class=ico-dlg-max></i>").setCls("dlg-max").setAlign("center").setValign("middle").setOn(Html.EVENT_CLICK, "$.dialog(this).max(this)"),"40");
		head.add(new Html(null,"<i class=ico-dlg-close></i>").setCls("dlg-close").setAlign("center").setValign("middle").setOn(Html.EVENT_CLICK, 
				"pub.dialogClose(this);"),"40");
		
		TemplateView cont=new TemplateView(null);
		vp.add(cont,"*");
		return std;
	}
	
	/**
	 * 对话框模板-信息提示框(alert)
	 * @return
	 */
	public static DialogTemplate getAlertTemplate() {
		DialogTemplate std=new DialogTemplate().setId("alert").setCls("dlg-std");
	
		VerticalLayout vp=new VerticalLayout(null);
		std.setNode(vp);
		HorizontalLayout head= new HorizontalLayout(null).setCls("dlg-std-head");
		vp.add(head,"40");
		head.add(new TemplateTitle(null).setCls("dlg-std-title").setWmin(10),"*");
		head.add(new Html(null,"<i class=ico-dlg-close></i>").setCls("dlg-close").setAlign("center").setValign("middle").setOn(Html.EVENT_CLICK, "$.dialog(this).close()"),"40");
		
		TemplateView cont=new TemplateView(null);
		vp.add(cont,"*");
		return std;
	}
	
	/**
	 * 对话框模板-信息提示框(带指向的)
	 * @return
	 */
	public static DialogTemplate getProngTemplate() {
		DialogTemplate std=new DialogTemplate().setId("prong").setCls("dlg-prong").setIndent(-10);
		VerticalLayout vp=new VerticalLayout(null);
		std.setNode(vp);
		vp.setAftercontent("<div class=dlg-prong-arrow-out></div><div class=dlg-prong-arrow-in></div>");
		
		HorizontalLayout head= new HorizontalLayout(null).setCls("dlg-prong-head");
		vp.add(head,"36");
		head.add(new TemplateTitle(null).setCls("dlg-prong-title"),"*");
		head.add(new Html(null,"<i onclick=$.close(this) class='dlg-close'>&times;</i><i class=f-vi></i>")
				.setAlign(Html.ALIGN_CENTER)
				.setValign(Html.VALIGN_MIDDLE),
				"40");
		
		TemplateView cont=new TemplateView(null);
		vp.add(cont,"*");
		return std;
	}

	public static final String DB_NODE_ROOT = "000000";
	
	@SuppressWarnings("unchecked")
	public static View buildIndexView(HttpServletRequest request) {
		View view = new View("root_view");
		
		//获取用户的角色权限
		String role = (String) HttpUtil.getSessionAttr(request, PubConstants.SESSION_USER_ROLE);
		List<SysResource> menuList = (List<SysResource>) HttpUtil.getSessionAttr(request, PubConstants.SESSION_USER_SYSRESOURCE);
		
		
		VerticalLayout root=new VerticalLayout("root");
		view.setNode(root);
		
		HorizontalLayout top=new HorizontalLayout("top");
		root.add(top, "60");
		top.setCls("x-bg-title f-nobr");
		
		top.add(new Html(null,"<div class='x-logo'></div>"), "160");
		DevProduct devProduct = (DevProduct)HttpUtil.getSessionAttr(request, "product");
		String productName = "";
		if(devProduct != null){
			productName = "|　"+(devProduct.getProductName()==null?"":devProduct.getProductName());
		}
		top.add(new Html("productNameHtml",productName).setStyle("font-size:16px;color:#FFF;line-height:60px;"), "160");
		
		ButtonBar wstab = new ButtonBar("wstab").setAlign(ButtonBar.ALIGN_RIGHT);
		top.add(wstab,"*");
		wstab.setFocusable(true).setCls("x-mtab-bar").setPub(new Button(null).setCls("x-mtab")).setMinwidth(550);
		
//		wstab.add(new Button(".x-mod-home", "系统首页").setTarget("f_home").setId("home").setFocus(true).setOn(Button.EVENT_CLICK, "index.tabClick(this)"));
		
		FrameLayout main=new FrameLayout("main");
		root.add(main);
		main.setDft("mod_010000000000000000000000");
		String loginUserId ="";
		String userName ="测试";
		String showName ="测试";
		if(LoginController.DATA_DEVELOPER_ROLE_ID.equals(role)){
			DevDeveloper devDeveloper = (DevDeveloper) HttpUtil.getSessionAttr(request, PubConstants.SESSION_DEV_DEVELOPER);
			userName = devDeveloper.getUserName();
			showName= devDeveloper.getUserName();
		}else if(LoginController.DATA_MANAGER_ROLE_ID.equals(role)){
			SysManager sysManager =  (SysManager) HttpUtil.getSessionAttr(request, PubConstants.SESSION_SYS_MANAGER);
			userName = sysManager.getLoginName();
			showName= sysManager.getLoginName();
		}
	
		main.add(buildHome(loginUserId));
		SysResource targerResource = null;
		if(!CollectionUtils.isEmpty(menuList)){
			targerResource = menuList.get(0);
			int i = 0;
			for (SysResource menu : menuList) {
				String mId = menu.getId();
				String wId = "mod_" + mId;
				
				View mView = new View().setId(wId);
				
				String src = "";
				if (Utils.notEmpty(menu.getUri())) {
					src = menu.getUri()+"?moduleId=" + menu.getId();
				} else {
					src = "system/enterModule?moduleId=" + menu.getId();
				}
				wstab.add(new Button(menu.getResImg(), menu.getResName()).setTarget(wId).setId("btn_" + menu.getId())
						.setOn(Button.EVENT_CLICK, "VM(this).find('" + wId +"').reload('" + src + "');").setFocusable(true).setFocus(i==0?true:false));
				mView.setSrc(src);
				main.add(mView);
				i++;
			}
		}
		
		
		String operText = "<a href=javascript:; class='index-loginname' onclick=\"$.widget(this).cmd('clickUser');\" title='" + userName + "'>" + showName + "</a>";
		top.add(new Html(operText).setAlign(Html.ALIGN_CENTER).setValign(Html.VALIGN_MIDDLE).setCls("x-moper"), "100");
		
		
//		ButtonBar oper = new ButtonBar("oper").setValign(ButtonBar.VALIGN_MIDDLE).setCls("x-moper");
//		top.add(oper, "130");
//		oper.setSpace(10);
//		Button help=new Button(".x-moper-help", "帮助").setTip("这是帮助中心").setCls("x-moper-it");
//		oper.add(help);
//		oper.add(new Split());
//		oper.add(new Button(".x-moper-logout", "").setTip("退出").setCls("x-moper-i").setOn(Button.EVENT_CLICK, "this.cmd({type:'ajax',src:'system/logout'});"));
		
		MenuCommand menu = new MenuCommand();
		String loginName = (String) HttpUtil.getSessionAttr(request, "auth");
		menu.add(new Button("个人信息").setOn(Button.EVENT_CLICK, "if(VM(this).find('personCert') == undefined && VM(this).find('enterpriseCert') == undefined && VM(this).find('devtype') == undefined){this.ownerView.cmd('personalInfo');}"));
		menu.add(new Button("修改密码").setOn(Button.EVENT_CLICK, "this.ownerView.cmd('editPwd');"));
		menu.add(new Button("退出").setOn(Button.EVENT_CLICK, "this.ownerView.cmd('logout');"));
		view.addCommand("clickUser", menu);
		if(targerResource != null){
			//默认点击第一个菜单栏
			//view.setOn(View.EVENT_LOAD, "VM(this).find('btn_"+targerResource.getId()+"').focus().click();");
		}
		view.addCommand("logout", PubView.getAjaxCommand("system/logout"));
		view.addCommand("personalInfo", PubView.getAjaxCommand("developer/developertype"));		
		view.addCommand("personCert", PubView.getAjaxCommand("developer/personCert?type=$0"));
		view.addCommand("editPwd", PubView.getAjaxCommand("system/editPwd"));
		
		view.addTemplate("std",getStdTemplate());
		view.addTemplate("form",getFormTemplate());
		view.addTemplate("callback",getCallbackTemplate());
		view.addTemplate("alert",getAlertTemplate());
		view.addTemplate("prong",getProngTemplate());
		
		
		return view;
	}
	
//	private static void fillSelDBSubBtn(Button btn,String parent,String focus,Map<String, List<Object[]>> dbKeyDataMap){
//		List<Object[]> dataList = dbKeyDataMap.get(parent);
//		if(dataList != null){
//			for (Object[] data : dataList) {
//				String dbKey = (String) data[0];
//				String dbKeyName = (String) data[1];
//				Button subBtn = new Button(dbKeyName);
//				if(dbKey.length() == 6){
//					if(!dbKey.equals(focus)){
//						
//						subBtn.setOn(Button.EVENT_CLICK,"VM(this).cmd('selDB','"+dbKey+"');").setCls("hand");
//					}else{
//						subBtn.setCls(".x-btn-sel").setDisabled(true);
//					}
//				}
//				if(dataList.size()==1){
//					fillSelDBSubBtn(btn,dbKey,focus, dbKeyDataMap);
//				}else{
//					btn.add(subBtn);
//					fillSelDBSubBtn(subBtn,dbKey,focus, dbKeyDataMap);
//				}
//			}
//		}
//	}
	

	public static Widget<?> buildHome(String loginUserId) {
		VerticalLayout home = new VerticalLayout("f_home").setStyle("padding:10px;").setWmin(22).setHmin(22).setScroll(true);
		home.setCls(".x-bg-body .x-bd-body");
		
		VerticalLayout shell = new VerticalLayout("").setScroll(true).setCls("bg-white").setStyle("padding:15px;").setWmin(30).setHmin(30);
		home.add(shell);
		return home;
	}
	
//	private static Widget<?> buildCoreNav(SysResource menu) {
//		String memuIcon = menu.getResImg();
//		String horzId="";
//		
//		if ("01000000".equals(menu.getId())) {
//			memuIcon = ".x-menu-orgman";
//			horzId="home_top_org";
//		} else {
//			memuIcon = ".x-menu-userman";
//			horzId="home_top_user";
//		}
//		String menuId = menu.getId();
//		String clk = "index.enterModule(this,'" + menuId + "');";
//		String tip = "点击进入";
//		tip = menu.getRemarks();
//		if (Utils.isEmpty(tip)) {
//			tip = menu.getResName();
//		}
//		
//		HorizontalLayout nav = new HorizontalLayout("");
//		nav.setStyle("padding:20px;margin:15px;").setWmin(72).setHmin(72).setId(horzId)
//			.setCls("x-bd-dashed hand").setOn(HorizontalLayout.EVENT_CLICK, clk);
//		
//		nav.add(new Img(memuIcon).setStyle("margin:0;padding:0;").setTip(tip).setWmin(22).setHmin(22), "90");
//		
//		nav.add(new Html("<div style='margin-left:18px;'><div style='height:30px;line-height:30px;margin:bottom:4px;font-size:16px;color:#333;'>" + menu.getResName() + "</div>" +
//				"<div style='height:30px;line-height:30px;font-size:14px;color:#999;' title='" + Utils.escapeXMLword(menu.getRemarks()) + "'>" + menu.getRemarks() + "</div></div>").setValign(Html.VALIGN_MIDDLE));
//		
//		return nav;
//	}
//	
//	private static Widget<?> buildAffairNav(SysResource menu, Map<String, List<SysResource>> menuMap) {
//		VerticalLayout vert = new VerticalLayout("").setStyle("margin:0 11px;").setWmin(22).setId("home_top_Affair");
//		vert.add(new Toggle(menu.getResName()).setCls("x-home-tog"));
//		
//		List<SysResource> subList = menuMap.get(menu.getId());
//		
//		HorizontalLayout hori = new HorizontalLayout("");
//		vert.add(hori, "*");
//		if (Utils.notEmpty(subList)) {
//			int mCount = subList.size();
//			int index = 0;
//			for (SysResource subMenu : subList) {
//				VerticalLayout mVert = new VerticalLayout("");
//				hori.add(mVert);
//				if (index == 0) {
//					mVert.setStyle("padding:0 50px 0 5px;").setWmin(55);
//				} else if (index == mCount-1) {
//					mVert.setStyle("padding:0 5px 0 50px;").setWmin(55);
//				} else if (index == 1) {
//					mVert.setStyle("padding:0 35px 0 20px;").setWmin(55);
//				} else if (index == 2) {
//					mVert.setStyle("padding:0 20px 0 35px;").setWmin(55);
//				}
//				index++;
//				
//				mVert.add(new Html(subMenu.getResName()).setCls("x-home-affair-title").setValign(Html.VALIGN_MIDDLE).setStyle("padding-bottom:5px;padding-top:20px;").setHmin(25), "20");
//				
//				ButtonBar btnBar = new ButtonBar("").setDir(ButtonBar.DIR_VERTICAL).setWidth("*");
//				mVert.add(btnBar, "*");
////				btnBar.setStyle("").setPub(new Button(null).setHeight("36").setWidth("*").setStyle("background:#f5f5f5;font-size:16px;text-align:left;margin:5px 0"));
//				btnBar.setPub(new Button(null).setWidth("*").setCls("x-home-affair-btn hand").setOn(Img.EVENT_CLICK, "index.clickMenu(this,this.x.id);"));
//				
//				List<SysResource> sonList = menuMap.get(subMenu.getId());
//				if (Utils.notEmpty(sonList)) {
//					for (SysResource sonMenu : sonList) {
//						String tip = sonMenu.getRemarks();
//						if (Utils.isEmpty(tip)) {
//							tip = sonMenu.getResName();
//						}
//						String icon = sonMenu.getResImg();
//						if (Utils.isEmpty(icon)) {
//							icon = ".x-menu-menuman";
//						}
//						icon += "-small";
//						btnBar.add(new Button(icon, sonMenu.getResName()).setId(sonMenu.getId()).setTip(tip));
//					}
//				}
//			}
//		}
//		
//		
//		return vert;
//	}
	
//	private static Widget<?> buildCommonNav(SysResource menu, Map<String, List<SysResource>> menuMap, boolean hasDecription) {
//		VerticalLayout vert = new VerticalLayout("").setStyle("margin:0 11px;").setWmin(22);
//		if("04000000".equals(menu.getId())){
//			vert.setId("home_top_common");
//		}
//		String togCls = hasDecription ? "x-mod-tog" : "x-home-tog";
//		String face = hasDecription ? AlbumLayout.FACE_STRAIGHT : null;
//		Integer textwidth = hasDecription ? 200 : null;
//		
//		vert.add(new Toggle(menu.getResName()).setCls(togCls));
//		
//		AlbumLayout album = new AlbumLayout("").setStyle("padding-top:10px;").setHmin(10).setFace(face);
//		vert.add(album);
//		album.setPub(new Img().setCls("x-home-common-btn hand").setOn(Img.EVENT_CLICK, "index.clickMenu(this,this.x.id);").setTextwidth(textwidth));
//		
//		List<SysResource> subList = menuMap.get(menu.getId());
//		
//		if (Utils.notEmpty(subList)) {
//			for (SysResource subMenu : subList) {
//				String mIcon = subMenu.getResImg();
//				if (Utils.isEmpty(mIcon)) {
//					mIcon = ".x-menu-menuman";
//				}
//				
//				String description = null;
//				if (hasDecription) {
//					description = subMenu.getRemarks();
//					if (Utils.isEmpty(description)) {
//						description = "请点击进入";
//					}
//				}
//				album.add(new Img(mIcon).setId(subMenu.getId()).setText(subMenu.getResName()).setDescription(description));
//			}
//		}
//		
//		return vert;
//	}
//	
//	public static String getNavTitle(SysResource modMenu) {
//		if (modMenu == null) {
//			return "";
//		}
//		String src = "index/enterWelcome?moduleId="+modMenu.getId();
//		return "<div title='点击进入欢迎页' onclick=\"VM(this).cmd({type:'ajax',src:'"+src+"'});\">" + modMenu.getResName() + "</div>";
//	}
//	
//	public static View buildModuleView(String moduleId, String focusMenuId,HttpServletRequest request) {
//		String loginUserId = FrameworkHelper.getLoginUser(request);
//		
//		//需要菜单路径获取下级菜单的方法
//		List<SysResource> menuList =  new ArrayList<>();
////		SysResource modMenu = sysMenuService.find(moduleId);
////		SysResource focusMenu = sysMenuService.find(focusMenuId);
//		SysResource modMenu =  null;
//		SysResource focusMenu = null;
//		View view = PubView.buildBodyView();
//		
//		Html navTop = (Html) view.findNodeById(PubView.BODY_NAV_TITLE);
//		navTop.setText(getNavTitle(modMenu));
//		
//		Map<String, List<SysResource>> menuMap = new HashMap<String, List<SysResource>>();
//		if (Utils.notEmpty(menuList)) {
//			for (SysResource menu : menuList) {
//				List<SysResource> subList = menuMap.get(menu.getId());
//				if (subList == null) {
//					subList = new ArrayList<SysResource>();
//					/*if (powerMenuIdList.contains(menu.getId())) {
//						
//						menuMap.put(menu.getParentId(), subList);
//					}*/
//					menuMap.put(menu.getId(), subList);
//				}
//				/*if (powerMenuIdList.contains(menu.getId())) {
//					
//					subList.add(menu);
//				}*/
//				subList.add(menu);
//			}
//		}
//		if (focusMenu != null && Utils.notEmpty(focusMenu.getUri())) {
//			View mainView = new View();
//			mainView.setId(PubView.BODY_MAIN);
//			mainView.setSrc(focusMenu.getUri());
//			view.replaceNodeById(mainView);
//		} else {
//			Html mainTitle = (Html) view.findNodeById(PubView.BODY_MAIN_TITLE);
//			mainTitle.setText(modMenu.getResName() + "导航区");
//			List<SysResource> subMenus = menuMap.get(moduleId);
//			if (Utils.notEmpty(subMenus)) {
//				if (moduleId.startsWith("03")) {
//					VerticalLayout mainDisplay = new VerticalLayout(PubView.BODY_MAIN_DISPLAY).setScroll(true);
//					view.replaceNodeById(mainDisplay);
//					for (SysResource menu : subMenus) {
//						Widget<?> w = buildCommonNav(menu, menuMap, true);
//						mainDisplay.add(w, "-1");
//					}
//				} else {
//					AlbumLayout mainDisplay = new AlbumLayout(PubView.BODY_MAIN_DISPLAY).setScroll(true).setFace(AlbumLayout.FACE_STRAIGHT).setStyle("padding:5px 10px;").setHmin(10).setWmin(20);
//					view.replaceNodeById(mainDisplay);
//					mainDisplay.setPub(new Img().setCls("x-home-common-btn hand").setTextwidth(200).setOn(Img.EVENT_CLICK, "index.clickMenu(this,this.x.id);"));
//					for (SysResource menu : subMenus) {
//						String mIcon = menu.getResImg();
//						String mName = menu.getResName();
//						if (Utils.isEmpty(mIcon)) {
//							mIcon = ".x-menu-menuman";
//						}
//						String mRemark = menu.getRemarks();
//						if (Utils.isEmpty(mRemark)) {
//							mRemark = "请点击进入";
//						}
//						// 主内容区
//						Img subImg = new Img(mIcon).setText(mName).setDescription(mRemark).setId(menu.getId());
//						mainDisplay.add(subImg);
//					}
//				}
//			}
//			
//		}
//		
////		StringBuilder navMenuHtml = new StringBuilder();
////		fillNavMenus(moduleId, menuMap, navMenuHtml);
////		new Html(navMenuHtml.toString()).setCls("index-l-nav f-clearfix").setScroll(true).setId(PubView.BODY_NAV_CONTENT)
////		view.replaceNodeById(navMenu);
//		TreePanel navContent = (TreePanel) view.findNodeById(PubView.BODY_NAV_CONTENT);
//		fillNavMenu(navContent, moduleId, menuMap, focusMenuId);
//		boolean needGuide = needGuide();
//		if(needGuide){
//			if("03000000".equals(moduleId)){
//				view.setOn(View.EVENT_LOAD, "dfish.use('./m/guide/javascript/guide3.js');" );
//			}else if("04000000".equals(moduleId)){
//				view.setOn(View.EVENT_LOAD, "dfish.use('./m/guide/javascript/guide4.js');" );
//			}
//		}
//		return view;
//	}
//	
//	private static void fillNavMenu(TreePanel navContent, String moduleId, Map<String, List<SysResource>> menuMap, String focusMenuId) {
//		if (navContent == null || Utils.isEmpty(moduleId) || Utils.isEmpty(menuMap)) {
//			return;
//		}
//		navContent.getPub().setHidetoggle(true);
//		
//		List<SysResource> navMenus = menuMap.get(moduleId);
//		if (Utils.isEmpty(navMenus)) {
//			return;
//		}
//		boolean hasFocus = Utils.notEmpty(focusMenuId);
//		String menuPre = "m_";
//		for (SysResource menu : navMenus) {
//			String mId = menu.getId();
//			String mName = menu.getResName();
//			String mRemark = menu.getRemarks();
//			if (Utils.isEmpty(mRemark)) {
//				mRemark = mName;
//			}
////			String mIcon = menu.getResImg();
////			if (Utils.isEmpty(mIcon)) {
////				mIcon = ".x-menu-menuman";
////			}
//			
//			Leaf leaf = new Leaf(menuPre + mId, mName).setTip(mRemark).setCls("nav-menu").setIcon(".nav-menu-icon").setOpenicon(".nav-menu-icon-open").setStyle("padding-left:15px;").setWmin(15);
//			navContent.add(leaf);
//			if (hasFocus && focusMenuId.equals(mId)) {
//				
//				leaf.setFocus(true);
//				hasFocus = false;
//			}
//			// 左边导航树
//			List<SysResource> subList = menuMap.get(mId);
//			String clk = null;
//			if (Utils.notEmpty(subList)) {
//				for (SysResource subMenu : subList) {
//					String subIcon = subMenu.getResImg();
//					if (Utils.notEmpty(subIcon)) {
//						subIcon += "-small";
//					}
//					Leaf subLeaf = new Leaf(menuPre + subMenu.getId(), subMenu.getResName()).setCls("nav-submenu").setIcon(subIcon).setStyle("padding-left:30px;").setWmin(30);
//					subLeaf.setOn(Leaf.EVENT_CLICK, "index.clickMenu(this,'" + subMenu.getId() + "');");
//					leaf.add(subLeaf);
//					if (hasFocus && focusMenuId.equals(subMenu.getId())) {
//						subLeaf.setFocus(true);
//						leaf.setOpen(true);
//						hasFocus = false;
//					}
//				}
//				clk = "this.toggle();";
//			} else {
//				clk = "index.clickMenu(this,'" + mId + "');";
//			}
//			leaf.setOn(Leaf.EVENT_CLICK, clk);
//		}
//	}

	
	public static View buildLoginView(String role) {
		View view = new View();
		
		VerticalLayout root = new VerticalLayout("root").setCls("bg-white").setScroll(true);
		view.add(root);
		view.setOn(View.EVENT_LOAD, "if($.br.scroll)return;var m=this,o=m.f('auth'),t=!o.x.value&&setInterval(function(){if(o.val()){$.classAdd(o.$('ph'),'f-none');$.classAdd(m.f('pwd').$('ph'),'f-none');clearInterval(t)}},50);Q(document).on('click',function(){clearInterval(t)})");
		
		HorizontalLayout top = new HorizontalLayout("top").setCls("log-top-bg");
		root.add(top, "380");
		
		VerticalLayout topShell = new VerticalLayout("topShell").setCls("log-top-log");
		top.add(topShell, "*");
		
		int inputHeight = 308;
		VerticalLayout main = new VerticalLayout("main").setCls("log-main").setMinheight(inputHeight).setAlign("center").setValign("middle");
		root.add(main, "*");
		
		VerticalLayout form = new VerticalLayout("form");
		main.add(form.setHeight(inputHeight));
		form.setStyle("padding:35px 0;").setHmin(70).setWidth("400");
		String enterKey = "login.enterKey(this);";
		if(StringUtils.isEmpty(role)){
			form.addHidden("role", LoginController.DATA_DEVELOPER_ROLE_ID);
		}else{
			form.addHidden("role", role);
		}
		form.addHidden("pwd", null);
		form.add(new Text("auth", "账号", null).setId("auth").setRequired(true).setPlaceholder("请输入账号").setCls("log-input").setBeforecontent("<i class='log-input-auth'></i>")
				.setTransparent(true).setWmin(110), "55");
		form.add(new Password("", "密码").setId("pwd").setRequired(true).setPlaceholder("请输入密码").setCls("log-input").setBeforecontent("<i class='log-input-pwd'></i>")
				.setTransparent(true).setWmin(110), "55");
		HorizontalGroup hg = new HorizontalGroup(null).setValign(HorizontalGroup.VALIGN_MIDDLE);
		form.add(hg);
		hg.add(new Text("checkCode", "验证码", null).setId("checkCode").setRequired(true).setCls("log-input-code")
				.setWmin(60).setTransparent(true).setPlaceholder("请输入验证码").setOn(Text.EVENT_KEYUP, "VM(this).f('pwd').val(md5(Q(\"div[w-id='pwd']\").find('input').val()));").setOn(Text.EVENT_INPUT, ""), "308");
		
		String imgHtml = "<img id='codeImg' src='./checkCode/codeImg.png?t="+System.currentTimeMillis()+"' title='点击更换验证码' onclick=\"this.src='./checkCode/codeImg.png?t='+new Date().getTime()\" class='hand'></img>";
		hg.add(new Html(imgHtml).setStyle("margin-top:8px;"), "92");
		hg.add(new Img("./system/validImg?t="+System.currentTimeMillis()).setValign(Img.VALIGN_MIDDLE).setTip("点击更换验证码").setCls("hand").setOn(Img.EVENT_CLICK, "this.attr('src','./system/validImg?t='+new Date().getTime());"), "92");
		form.add(new ButtonBar("btnLogin").add(new Button("登录").setCls("log-btn").setOn(Button.EVENT_CLICK, "VM(this).cmd('login');")).setAlign(ButtonBar.ALIGN_CENTER), "55");
		
		HorizontalLayout register = new HorizontalLayout("regist").setWidth(450).setStyle("display:table;margin:0 auto");
		Html forgot = new Html("<a onclick=\"dfish.vm(this).cmd('toFindPassword');\" >忘记密码</a>").setStyle("border-top:0px").setCls("log-bottom");
		Html regist = new Html("<a onclick=\"dfish.vm(this).cmd('toReg');\" >免费注册</a>").setStyle("border-top:0px").setCls("log-bottom");
		Html bottom = new Html("版权所有©福建星榕基").setCls("log-bottom").setHmin(1).setAlign(Html.ALIGN_CENTER).setValign(Html.VALIGN_MIDDLE);
		register.add(new Html("").setWidth(300));
		register.add(forgot);
		register.add(regist);
		root.add(register);
		root.add(bottom, "55");
		
		view.addCommand("login", PubView.getSubmitCommand("system/login"));
		view.addCommand("toReg", PubView.getAjaxCommand("developer/toReg"));
		view.addCommand("toFindPassword", PubView.getAjaxCommand("developer/findPwd"));
		
		view.addTemplate("std", getStdTemplate());
		
		return view;
	}
	
	
	public static View buildEditPwdView() {
		View view = PubView.buildDialogView(false);
		FormPanel form = (FormPanel) view.findNodeById(PubView.DIALOG_MAIN);
		String pattern = "/(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[^a-zA-Z0-9]).{8,30}/";
		String placeholder = "8-30位字母、数字和特殊符；如:abc@1234";
		Validate vld = new Validate().setPattern(pattern);
		form.add(new Text("oldPwd", "原密码", null).setPlaceholder("请输入原密码").setId("oldPwd").setRequired(true)
				.addValidate(vld));
		form.add(new Password("newPwd", "新密码").setId("newPwd").setRequired(true).setPlaceholder(placeholder).addValidate(vld));
		form.add(new Password("newPwdCfm", "密码确认").setId("newPwdCfm").setRequired(true).setPlaceholder("与新密码保持一致").addValidate(vld));
		ButtonBar btnBar = (ButtonBar) view.findNodeById(PubView.DIALOG_BUTTON);
		btnBar.add(new SubmitButton("保存").setCls("x-btn x-btn-main").setOn(Button.EVENT_CLICK, "this.cmd('save');"));
		btnBar.add(new Button("关闭").setOn(Button.EVENT_CLICK, "$.dialog(this).close();"));
		view.addCommand("save", PubView.getSubmitCommand("systemvePwd").setRange(PubView.DIALOG_MAIN));
		return view;
	}

	/**
	 * 判断是否需要引导
	 * @param request
	 * @return
	 */
	public static boolean needGuide() {
		String systemGuide = FrameworkHelper.getSystemConfig("system.guide", "1");
		return "0".equals(systemGuide);
	}

	public static View buildSelDBView(List<Object[]> dbKeyData) {
		View view = new View();
		VerticalLayout root = new VerticalLayout("root").setCls("bg-white").setScroll(true);
		view.add(root);
		
		HorizontalLayout top = new HorizontalLayout("top").setCls("log-top-bg");
		root.add(top, "380");
		
		VerticalLayout topShell = new VerticalLayout("topShell").setCls("log-top-log");
		top.add(topShell, "*");
		
		int treeHeight = 308,treeWidth = 400;
		VerticalLayout main = new VerticalLayout("main").setMinheight(treeHeight).setAlign("center").setValign("middle");
		root.add(main, "*");
		
		TreePanel dbContent = buildDBContent(dbKeyData); 
		main.add(dbContent.setHeight(treeHeight).setWidth(treeWidth));
		
		Html bottom = new Html("版权所有©中共福建省委组织部").setCls("log-bottom").setHmin(1).setAlign(Html.ALIGN_CENTER).setValign(Html.VALIGN_MIDDLE);
		root.add(bottom, "55");
		
		view.addCommand("selDB", PubView.getAjaxCommand("index/selDB?dbKey=$0"));
		
		view.addTemplate("std", getStdTemplate());
		
		return view;
	}
	
	public static TreePanel buildDBContent(List<Object[]> dbKeyData) {
		TreePanel dbContent = new TreePanel("form");
		dbContent.setScroll(true);
		dbContent.getPub().setOn(Leaf.EVENT_DBLCLICK, "if($disabled==false){VM(this).cmd('selDB',$id);}");
		fillDBNodes(dbContent, dbKeyData);
		return dbContent;
	}
	
	private static void fillDBNodes(TreePanel dbContent, List<Object[]> dbKeyData) {
		Map<String, List<Object[]>> dbKeyDataMap = getDbKeyDataMap(DB_NODE_ROOT,dbKeyData);
		fillDBSubNodes(dbContent,DB_NODE_ROOT, dbKeyDataMap);
	}
	private static Map<String, List<Object[]>> getDbKeyDataMap(String root,List<Object[]> dbKeyData){
		Map<String, List<Object[]>> dbKeyDataMap = new HashMap<String, List<Object[]>>();
		
//		Map<String, String> dbKeyMap = new HashMap<String, String>();
//		CodeService codeService = FrameworkHelper.getBean(CodeService.class);
//		for (Object[] data : dbKeyData) {
//			String id = (String) data[0];
//			String provCode = id.substring(0, 2);
//			String cityCode = id.substring(0, 4);
//			dbKeyMap.put(provCode, codeService.getCodeName("XZQY", provCode+"0000"));
//			dbKeyMap.put(cityCode, codeService.getCodeName("XZQY", cityCode+"00"));
//			dbKeyMap.put(id, (String)data[1]);
//		}
//		for (Map.Entry<String, String> entry : dbKeyMap.entrySet()) {
//			int keyLength = entry.getKey().length();
//			String parent = "";
//			if( keyLength <= 2){
//				parent = root;
//			}else{
//				parent = entry.getKey().substring(0, keyLength-2);
//			}
//			
//			List<Object[]> subDatas = dbKeyDataMap.get(parent);
//			if(subDatas == null){
//				subDatas = new ArrayList<Object[]>();
//				dbKeyDataMap.put(parent, subDatas);
//			}
//			subDatas.add(new Object[]{entry.getKey(),entry.getValue()});
//		}
		return dbKeyDataMap;
	}
	private static Leaf buildDBNode(Object[] data, List<Object[]> subNodes) {
		Leaf leaf = new Leaf((String)data[0], (String)data[1]);
		boolean disabled = ((String)data[0]).length() ==6?false:true;
		leaf.setData("disabled", disabled);
		
		if (Utils.notEmpty(subNodes)) {
			leaf.setData("subs", subNodes.size());
		}
		return leaf;
	}
	
	private static void fillDBSubNodes(AbstractLayout<?, Leaf> layout,String parent, Map<String, List<Object[]>> dataMap) {
		List<Object[]> dataList = dataMap.get(parent);
		if (dataList != null) {
			for (Object[] data : dataList) {
				String dbKey = (String)data[0];
				List<Object[]> subNodes = dataMap.get(dbKey);
				Leaf leaf = buildDBNode(data, subNodes);
				if(dataList.size()==1){
					fillDBSubNodes(layout,dbKey, dataMap);
				}else{
					layout.add(leaf);
					fillDBSubNodes(leaf,dbKey, dataMap);
				}
			}
		}
	}
}
