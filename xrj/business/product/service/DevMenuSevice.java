package com.xrj.business.product.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.xrj.api.dto.SysResource;
import com.xrj.business.core.base.RemoteService;
@Component
public class DevMenuSevice {
	@Resource
	private RemoteService remoteService;
	/**
	 * 取得菜单列表
	 * @return
	 */
	public List<SysResource> listMenu(){
		return remoteService.getSysResourceService().findAll();
	}
}
