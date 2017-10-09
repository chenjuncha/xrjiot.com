package com.xrj.business.core.web;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rongji.dfish.base.Page;
import com.rongji.dfish.ui.command.CommandGroup;
import com.rongji.dfish.ui.command.DialogCommand;
import com.rongji.dfish.ui.command.ReplaceCommand;
import com.rongji.dfish.ui.helper.TabPanel;
import com.rongji.dfish.ui.layout.View;
import com.rongji.dfish.ui.widget.Button;
import com.rongji.dfish.ui.widget.Html;
import com.xrj.api.dto.DevAudit;
import com.xrj.api.dto.DevDeveloper;
import com.xrj.api.dto.DevProduct;
import com.xrj.api.dto.SysDataDict;
import com.xrj.api.dto.SysManager;
import com.xrj.api.dto.SysResource;
import com.xrj.api.em.DataDictEnum;
import com.xrj.business.util.PageConvertUtil;
import com.xrj.business.core.base.PubConstants;
import com.xrj.business.core.base.PubService;
import com.xrj.business.core.base.RemoteService;
import com.xrj.business.core.view.DataDictView;
import com.xrj.business.core.view.ManagerView;
import com.xrj.business.core.view.PubView;
import com.xrj.framework.util.HttpUtil;
import com.xrj.framework.util.StringUtils;

/**
 * 管理者中心
 * 
 * @author chenyun
 * @date 2017-07-20
 */
@Controller
@RequestMapping("manager")
public class ManagerController {

	@Resource
	private RemoteService remoteService;

	/**
	 * 管理者中心界面
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/index")
	@ResponseBody
	public Object index(HttpServletRequest request, HttpServletResponse response) throws Exception {

		View view = new View();
		TabPanel tabPanel = new TabPanel("m_tab");
		tabPanel.getTabBar().setHeight("40").setCls("x-tabbar ");
		tabPanel.getTabBar().getPub().setCls("x-tab");
		String moduleId = request.getParameter("moduleId");
		List<SysResource> sysResourceList = remoteService.getSysResourceService().querySysResourceList(moduleId, null);
		if (!CollectionUtils.isEmpty(sysResourceList)) {
			for (SysResource res : sysResourceList) {
				tabPanel.add(new View("tab_").setHeight(600).setSrc(res.getUri()), new Button(res.getResName())
						.setOn(TabPanel.EVENT_CLICK, "VM(this).find('tab_').reload('" + res.getUri() + "');"));
			}
			tabPanel.setFocusIndex(sysResourceList.size() - 1);
		}

		view.add(tabPanel);
		return view;
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/developerIndex")
	@ResponseBody
	public Object developerIndex(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Page dfishPage = PubService.getPage(request);
		String auditStatus = request.getParameter("auditStatus");
		String likeInfo = request.getParameter("likeInfo");
		com.xrj.framework.hibernate.dao.Page page = PageConvertUtil.convertDaoPage(dfishPage);
		page = remoteService.getDevDeveloperService().setPageDataList(page, auditStatus, likeInfo);
		dfishPage = PageConvertUtil.convertDfishPage(page);
		View view = ManagerView.buildDeveloperListView(request, dfishPage);

		// SysDataDict root =
		// remoteService.getSysDataDictService().get("597313ccc3845ea150b1763e");
		// List<SysDataDict> list =
		// remoteService.getSysDataDictService().findSysDataDictList("597313ccc3845ea150b1763e",
		// null);
		// String temp = "product_classification";
		// setddd(root, list);
		return view;
	}

	// private void setddd(SysDataDict parentDict, List<SysDataDict> list){
	// int i=1;
	// for(SysDataDict dataDict: list){
	// String str = (i<10)?("0"+i):(""+i);
	// dataDict.setDictCode(parentDict.getDictCode().equals("product_classification")?str:(parentDict.getDictCode()+str));
	// dataDict.setRemark(parentDict.getRemark()==null?dataDict.getDictName():(parentDict.getRemark()+"/"+dataDict.getDictName()));
	// remoteService.getSysDataDictService().updateSysDataDict(dataDict);
	// ++i;
	// List<SysDataDict> list2 =
	// remoteService.getSysDataDictService().findSysDataDictList(dataDict.getId(),
	// null);
	// if(!CollectionUtils.isEmpty(list2)){
	// setddd(dataDict, list2);
	// }
	// }
	// }

	@SuppressWarnings("rawtypes")
	@ResponseBody
	@RequestMapping("/developerSearch")
	public Object searchDeveloper(HttpServletRequest request) throws Exception {
		CommandGroup cg = new CommandGroup();
		Page dfishPage = PubService.getPage(request);
		String auditStatus = request.getParameter("auditStatus");
		String likeInfo = request.getParameter("likeInfo");
		com.xrj.framework.hibernate.dao.Page page = PageConvertUtil.convertDaoPage(dfishPage);

		page = remoteService.getDevDeveloperService().setPageDataList(page, auditStatus, likeInfo);
		dfishPage = PageConvertUtil.convertDfishPage(page);
		View view = ManagerView.buildDeveloperListView(request, dfishPage);

		cg.add(new ReplaceCommand(view.findNodeById(PubView.MAIN_CONTENT)));
		cg.add(new ReplaceCommand(view.findNodeById(PubView.MAIN_PAGE)));
		return cg;
	}

	@ResponseBody
	@RequestMapping("/developerAuditDetail")
	public Object developerAuditDetail(HttpServletRequest request) throws Exception {
		String id = request.getParameter("id");
		DevDeveloper devDeveloper = remoteService.getDevDeveloperService().get(id);
		List<DevAudit> devAuditList = remoteService.getDevAuditService().findAll(PubConstants.AUDIT_TYPE_DEV_DEVELOPER,
				devDeveloper.getId());
		View view = ManagerView.buildDeveloperAuditDialog(request, devDeveloper, devAuditList);
		DialogCommand dialog = PubView
				.getDialogCommand("developerAudit", "form", "开发者审核详情", PubView.DIALOG_SIZE_LARGE, null).setNode(view);

		return dialog;
	}

	@ResponseBody
	@RequestMapping("/developerAudit")
	public Object developerAudit(HttpServletRequest request) throws Exception {
		CommandGroup cg = new CommandGroup();

		String developerId = request.getParameter("developerId");
		String auditId = request.getParameter("auditId");
		String auditStatus = request.getParameter("auditStatus");
		String auditContent = request.getParameter("auditContent");
		SysManager sysManager = (SysManager) HttpUtil.getSessionAttr(request, PubConstants.SESSION_SYS_MANAGER);
		remoteService.getDevDeveloperService().auditDevDeveloper(developerId, auditId,
				sysManager == null ? null : sysManager.getId(), auditContent, auditStatus);
		// 查询最新审核历史
		List<DevAudit> devAuditList = remoteService.getDevAuditService().findAll(PubConstants.AUDIT_TYPE_DEV_DEVELOPER,
				developerId);
		DevAudit currentAudit = devAuditList.get(0);
		// 替换状态
		cg.add(new ReplaceCommand(new Html("状态：" + currentAudit.getAuditStatus()).setId("statusHtml")
				.setStyle("margin:10px 6px 8px 6px;").setHeight("40")));
		// 替换按钮
		cg.add(new ReplaceCommand(ManagerView.buildAuditDeveloperButtonBar(devAuditList.get(0))));
		// 替换审核历史
		cg.add(new ReplaceCommand(ManagerView.buildGridAuditList(devAuditList)));
		return cg;
	}

	/**
	 * 重新审核
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/reAuditDeveloper")
	public Object reAuditDeveloper(HttpServletRequest request) throws Exception {
		CommandGroup cg = new CommandGroup();

		// 修改审核结果
		String developerId = request.getParameter("developerId");
		String auditId = request.getParameter("auditId");
		SysManager sysManager = (SysManager) HttpUtil.getSessionAttr(request, PubConstants.SESSION_SYS_MANAGER);
		remoteService.getDevDeveloperService().reAuditDevDeveloper(developerId, auditId,
				sysManager == null ? null : sysManager.getId());

		// 查询最新审核历史
		List<DevAudit> devAuditList = remoteService.getDevAuditService().findAll(PubConstants.AUDIT_TYPE_DEV_DEVELOPER,
				developerId);
		DevAudit currentAudit = devAuditList.get(0);
		// 替换状态
		cg.add(new ReplaceCommand(new Html("状态：" + currentAudit.getAuditStatus()).setId("statusHtml")
				.setStyle("margin:10px 6px 8px 6px;").setHeight("40")));
		// 替换按钮
		cg.add(new ReplaceCommand(ManagerView.buildAuditDeveloperButtonBar(currentAudit)));
		// 替换审核历史
		cg.add(new ReplaceCommand(ManagerView.buildGridAuditList(devAuditList)));
		return cg;
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/productIndex")
	@ResponseBody
	public Object productIndex(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Page dfishPage = PubService.getPage(request);
		String auditStatus = request.getParameter("auditStatus");
		String likeInfo = request.getParameter("likeInfo");
		com.xrj.framework.hibernate.dao.Page page = PageConvertUtil.convertDaoPage(dfishPage);

		page = remoteService.getDevProductService().setPageDataList(page, auditStatus, likeInfo);
		dfishPage = PageConvertUtil.convertDfishPage(page);
		View view = ManagerView.buildProductListView(request, dfishPage);
		return view;
	}

	@SuppressWarnings("rawtypes")
	@ResponseBody
	@RequestMapping("/productSearch")
	public Object searchProduct(HttpServletRequest request) throws Exception {
		CommandGroup cg = new CommandGroup();
		Page dfishPage = PubService.getPage(request);
		String auditStatus = request.getParameter("auditStatus");
		String likeInfo = request.getParameter("likeInfo");
		com.xrj.framework.hibernate.dao.Page page = PageConvertUtil.convertDaoPage(dfishPage);
		page = remoteService.getDevProductService().setPageDataList(page, auditStatus, likeInfo);
		dfishPage = PageConvertUtil.convertDfishPage(page);

		View view = ManagerView.buildProductListView(request, dfishPage);

		cg.add(new ReplaceCommand(view.findNodeById(PubView.MAIN_CONTENT)));
		cg.add(new ReplaceCommand(view.findNodeById(PubView.MAIN_PAGE)));
		return cg;
	}

	@ResponseBody
	@RequestMapping("/productAuditDetail")
	public Object productAuditDetail(HttpServletRequest request) throws Exception {
		String id = request.getParameter("id");
		DevProduct devProduct = remoteService.getDevProductService().get(id);
		if (StringUtils.isNotEmpty(devProduct.getDeveloperId())) {
			DevDeveloper devDeveloper = remoteService.getDevDeveloperService().get(devProduct.getDeveloperId());
			devProduct.setDeveloperName(devDeveloper == null ? null : devDeveloper.getUserName());
		}
		List<DevAudit> devAuditList = remoteService.getDevAuditService().findAll(PubConstants.AUDIT_TYPE_DEV_PRODUCT,
				devProduct.getId());
		View view = ManagerView.buildProductAuditDialog(request, devProduct, devAuditList);
		DialogCommand dialog = PubView
				.getDialogCommand("productAudit", "form", "产品审核详情", PubView.DIALOG_SIZE_LARGE, null).setNode(view);

		return dialog;
	}

	@ResponseBody
	@RequestMapping("/productAudit")
	public Object productAudit(HttpServletRequest request) throws Exception {
		CommandGroup cg = new CommandGroup();

		String productId = request.getParameter("productId");
		String auditId = request.getParameter("auditId");
		String auditStatus = request.getParameter("auditStatus");
		String auditContent = request.getParameter("auditContent");
		SysManager sysManager = (SysManager) HttpUtil.getSessionAttr(request, PubConstants.SESSION_SYS_MANAGER);
		remoteService.getDevProductService().auditDevProduct(productId, auditId,
				sysManager == null ? null : sysManager.getId(), auditContent, auditStatus);
		// 查询最新审核历史
		List<DevAudit> devAuditList = remoteService.getDevAuditService().findAll(PubConstants.AUDIT_TYPE_DEV_PRODUCT,
				productId);
		DevAudit currentAudit = devAuditList.get(0);
		Map<String, String> auditStatusMap = PubService.getDataDictMap(DataDictEnum.audit_status.toString());
		// 替换状态
		cg.add(new ReplaceCommand(new Html("状态：" + auditStatusMap.get(currentAudit.getAuditStatus()))
				.setCls("x-main-title").setId("statusHtml").setHeight("40")));
		// 替换按钮
		cg.add(new ReplaceCommand(ManagerView.buildAuditProductButtonBar(devAuditList.get(0))));
		// 替换审核历史
		cg.add(new ReplaceCommand(ManagerView.buildGridAuditList(devAuditList)));
		return cg;
	}

	/**
	 * 重新审核
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/reAuditProduct")
	public Object reAuditProduct(HttpServletRequest request) throws Exception {
		CommandGroup cg = new CommandGroup();

		// 修改审核结果
		String productId = request.getParameter("productId");
		String auditId = request.getParameter("auditId");
		SysManager sysManager = (SysManager) HttpUtil.getSessionAttr(request, PubConstants.SESSION_SYS_MANAGER);

		remoteService.getDevProductService().reAuditDevProduct(productId, auditId,
				sysManager == null ? null : sysManager.getId());

		// 查询最新审核历史
		List<DevAudit> devAuditList = remoteService.getDevAuditService().findAll(PubConstants.AUDIT_TYPE_DEV_PRODUCT,
				productId);
		DevAudit currentAudit = devAuditList.get(0);
		Map<String, String> auditStatusMap = PubService.getDataDictMap(DataDictEnum.audit_status.toString());
		// 替换状态
		cg.add(new ReplaceCommand(new Html("状态：" + auditStatusMap.get(currentAudit.getAuditStatus()))
				.setCls("x-main-title").setId("statusHtml").setHeight("40")));
		// 替换按钮
		cg.add(new ReplaceCommand(ManagerView.buildAuditProductButtonBar(currentAudit)));
		// 替换审核历史
		cg.add(new ReplaceCommand(ManagerView.buildGridAuditList(devAuditList)));
		return cg;
	}

	@RequestMapping("/datadictIndex")
	@ResponseBody
	public Object datadictIndex(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<SysDataDict> dataDictList = remoteService.getSysDataDictService().findDictTypeList();
		View view = DataDictView.buildDataDictView(dataDictList, null);
		return view;
	}

}
