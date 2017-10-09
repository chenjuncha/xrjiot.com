package com.xrj.business.product.service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.rongji.dfish.framework.FilterParam;
import com.xrj.api.dto.DevDeviceRom;
import com.xrj.api.dto.DevHardwareVersion;
import com.xrj.api.dto.DevProduct;
import com.xrj.api.service.DevHardwareVersionService;
import com.xrj.business.util.PageConvertUtil;
import com.xrj.business.core.base.PubService;
import com.xrj.business.core.base.RemoteService;
import com.xrj.framework.hibernate.dao.Page;
import com.xrj.framework.util.HttpUtil;
@Component
public class DevHardwareVersionIndexService {
	@Resource
	private RemoteService remoteService;
	
	/**
	 * 根据硬件版本ID得到硬件版本对象
	 * @param id
	 * @return
	 */
	public DevHardwareVersion get(String id){
		DevHardwareVersionService devHardwareVersionService = remoteService.getDevHardwareVersionService();
		return devHardwareVersionService.get(id);
	}
	
	/**
	 * 保存硬件版本
	 * @param DevHardwareVersion
	 */
	public void save(DevHardwareVersion devHardwareVersion){
		DevHardwareVersionService devHardwareVersionService = remoteService.getDevHardwareVersionService();
		devHardwareVersionService.save(devHardwareVersion);
		return;
	}

	/**
	 * 删除
	 * @param id
	 */
	public void delete(String id){
		DevHardwareVersionService devHardwareVersionService = remoteService.getDevHardwareVersionService();
		devHardwareVersionService.delete(id);		
		return;
	}

	/**
	 * 更新硬件版本
	 * @param resource
	 */
	public void update(DevHardwareVersion devHardwareVersion){
		DevHardwareVersionService devHardwareVersionService = remoteService.getDevHardwareVersionService();
		devHardwareVersionService.update(devHardwareVersion);
		return;
	}
	
	/**
	 * 产品硬件版本分页查询 
	 * @param dfishPage
	 * @return
	 */
	public Page<DevHardwareVersion> setPageDataList(Page<DevHardwareVersion> page, String productId){
		DevHardwareVersionService devHardwareVersionService = remoteService.getDevHardwareVersionService();
		return devHardwareVersionService.setPageDataList(page, productId);
	}
	
	/**
	 * 校验同一个产品的 硬件版本名称及版本号的唯一性
	 * @param devHardwareVersion
	 * @return
	 */
	public boolean checkUnqie(DevHardwareVersion devHardwareVersion){
		DevHardwareVersionService devHardwareVersionService = remoteService.getDevHardwareVersionService();
		return devHardwareVersionService.checkUnqie(devHardwareVersion);
	}
	
	/**
	 * 根据硬件版本Id查询是否有关联设备rom
	 * @param id
	 * @return
	 */
	public List<DevDeviceRom> getDeviceRomById(String id){
		DevHardwareVersionService devHardwareVersionService = remoteService.getDevHardwareVersionService();
		return devHardwareVersionService.getDeviceRomById(id);
	}
	
	/**
	 * 硬件版本号校验
	 * @param hardWareVersion
	 * @return
	 */
	public Boolean verificationHardwareVersion(String hardWareVersion,String patternStr) {
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(hardWareVersion);
		return matcher.matches();
	}
	
	public List<DevHardwareVersion> findHardListByProductId(HttpServletRequest request) {
		List<DevHardwareVersion> list = new ArrayList<>();
		DevProduct product = (DevProduct) HttpUtil.getSessionAttr(request, "product");
		list = remoteService.getDevHardwareVersionService().getByProductId(product.getId());
		return list;
	}
	
	public List<Object> queryPageList(HttpServletRequest request) {
		
		DevProduct product = (DevProduct) HttpUtil.getSessionAttr(request, "product");
		com.rongji.dfish.base.Page dfishPage = PubService.getPage(request);
		FilterParam fp = PubService.getParam(request);
		Page page = PageConvertUtil.convertDaoPage(dfishPage);
		page = this.setPageDataList(page, product.getId());
		dfishPage = PageConvertUtil.convertDfishPage(page);
		List<DevHardwareVersion> versionList = dfishPage.getResultSet();

		List<Object> list = new ArrayList<Object>();
		list.add(versionList);
		list.add(dfishPage);
		list.add(fp);
		return list;
	}


}
