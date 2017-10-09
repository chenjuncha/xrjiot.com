package com.xrj.business.product.listener;

import java.util.List;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;


import com.xrj.api.dto.DevDeveloper;
import com.xrj.api.dto.DevDevice;
import com.xrj.api.dto.DevProduct;
import com.xrj.api.service.DevProductService;
import com.xrj.business.core.base.PubConstants;
import com.xrj.business.core.base.RemoteService;
import com.xrj.business.util.SpringUtils;
public class SessionDestoyListener implements HttpSessionListener  {
	
	@Override
	public void sessionCreated(HttpSessionEvent se) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		RemoteService remoteService=SpringUtils.getBean("remoteService");
		DevDeveloper devDeveloper = (DevDeveloper) se.getSession().getAttribute(PubConstants.SESSION_DEV_DEVELOPER);
		if (devDeveloper!=null) {
			DevProductService devProductService = remoteService.getDevProductService();
			List<DevProduct> queryProductList =devProductService.queryProductList(devDeveloper.getId(),null,null);
			for (DevProduct devProduct : queryProductList) {
				DevDevice simnulateDevice = remoteService.getDevDeviceService().getSimnulateDevice(devProduct.getId());
				if (simnulateDevice!=null) {
					remoteService.getSimnulationDeviceService().destroyTask(simnulateDevice.getDeviceSn());
				}
			}
		}
		
	}


}
