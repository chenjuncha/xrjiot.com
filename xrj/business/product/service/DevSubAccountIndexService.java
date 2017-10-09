package com.xrj.business.product.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.rongji.dfish.base.Page;
import com.rongji.dfish.base.Utils;
import com.rongji.dfish.framework.FilterParam;
import com.rongji.dfish.ui.command.AlertCommand;
import com.rongji.dfish.ui.command.CommandGroup;
import com.rongji.dfish.ui.command.ReplaceCommand;
import com.rongji.dfish.ui.layout.View;
import com.xrj.api.dto.DevDeveloper;
import com.xrj.api.dto.DevDevice;
import com.xrj.api.dto.DevProduct;
import com.xrj.api.dto.DevSubAccount;
import com.xrj.api.em.StatusEnum;
import com.xrj.api.service.DevSubAccountService;
import com.xrj.business.util.PageConvertUtil;
import com.xrj.business.core.base.PubConstants;
import com.xrj.business.core.base.PubService;
import com.xrj.business.core.base.RemoteService;
import com.xrj.business.core.view.PubView;
import com.xrj.business.product.view.DevSubAccountView;
import com.xrj.framework.util.HttpUtil;
@Component
public class DevSubAccountIndexService {
	@Resource
	private RemoteService remoteService;
	
	public DevSubAccount get(String subId){
		return remoteService.getDevSubAccountService().get(subId);
	}
	/**
	 * 分页查询子账号
	 * @param request
	 * @return
	 */
	public List<Object> setPageDataList(HttpServletRequest request){
		DevDeveloper devDeveloper = (DevDeveloper) HttpUtil.getSessionAttr(request, PubConstants.SESSION_DEV_DEVELOPER);
		Page dfishPage = PubService.getPage(request);
		com.xrj.framework.hibernate.dao.Page page = PageConvertUtil.convertDaoPage(dfishPage);
		page = remoteService.getDevSubAccountService().setPageDataList(page, devDeveloper.getId());
		dfishPage = PageConvertUtil.convertDfishPage(page);
		List<DevSubAccount> devSubAccountList = dfishPage.getResultSet();
		List<Object> list = new ArrayList<Object>();
		list.add(devSubAccountList);
		list.add(dfishPage);
		return list;
	}
	/**
	 * 根据条件分页查询子设备账号
	 * @param request
	 * @return
	 */
	public List<Object> setPageByDevSubAccount(HttpServletRequest request){
		String search = request.getParameter("subAccountSearch");
		DevSubAccount devSubAccount = new DevSubAccount();
		devSubAccount.setAccountSecret(search);
		devSubAccount.setAccountName(search);
		Page dfishPage = PubService.getPage(request);
		DevDeveloper devDeveloper = (DevDeveloper) HttpUtil.getSessionAttr(request, PubConstants.SESSION_DEV_DEVELOPER);
		com.xrj.framework.hibernate.dao.Page page = PageConvertUtil.convertDaoPage(dfishPage);
		page = remoteService.getDevSubAccountService().setPageByDevSubAccount(page, devDeveloper.getId(), devSubAccount);
		dfishPage = PageConvertUtil.convertDfishPage(page);
		List<DevSubAccount> devSubAccountList = dfishPage.getResultSet();
		List<Object> list = new ArrayList<Object>();
		list.add(devSubAccountList);
		list.add(dfishPage);
		return list;
	}
	
	public boolean isBuildMainAccount(DevDeveloper devDeveloper){
		DevSubAccountService subAccountService =remoteService.getDevSubAccountService();
		if (Utils.isEmpty(subAccountService.findPrimaryAccount(devDeveloper.getId()))
				&& Utils.isEmpty(subAccountService.findAccountSecret(devDeveloper.getId()))) {
			subAccountService.buildPrimaryAccount(devDeveloper.getId());
			return true;
		}
		return false;
	}
	/**
	 * 批量删除子账号
	 * @param sourceStrArray
	 * @return
	 */
	public boolean batchDelete(String[] sourceStrArray){
		for(String subId : sourceStrArray){
			DevSubAccount devSubAccount = remoteService.getDevSubAccountService().get(subId);
			if(devSubAccount.getStatus().equals(StatusEnum.NORMAL.getIndex())){
				return false;
			}
		}
		remoteService.getDevSubAccountService().batchDelete(sourceStrArray);
		return true;
	}
	
	public void batchUpdateSubAccount(String[] sourceStrArray,String status){
		remoteService.getDevSubAccountService().batchUpdateSubAccount(sourceStrArray, status);
	}
	
	public void batchBuildSubAccount(DevDeveloper devDeveloper,int subCount){
		remoteService.getDevSubAccountService().batchBuildSubAccount(devDeveloper.getId(), subCount);
	}
	
	public String findPrimaryAccount(DevDeveloper devDeveloper){
		String account=remoteService.getDevSubAccountService().findPrimaryAccount(devDeveloper.getId());
		return account;
	}
	
}
