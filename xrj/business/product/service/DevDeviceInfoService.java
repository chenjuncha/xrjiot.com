package com.xrj.business.product.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.rongji.dfish.base.Page;
import com.rongji.dfish.base.Utils;
import com.xrj.api.dto.DevDevice;
import com.xrj.api.dto.DevProduct;
import com.xrj.api.dto.DevProductFunction;
import com.xrj.api.em.FunctionTypeEnum;
import com.xrj.api.model.DeviceRunningState;
import com.xrj.business.core.base.PubService;
import com.xrj.business.core.base.RemoteService;
import com.xrj.business.product.view.DevDeviceControlView;
import com.xrj.business.util.PageConvertUtil;
import com.xrj.framework.util.HttpUtil;

import net.sf.json.JSONObject;
@Component
public class DevDeviceInfoService {
	@Resource
	private RemoteService remoteService;
	
	public DevDevice get(String deviceId){
		DevDevice devDevice = remoteService.getDevDeviceService().get(deviceId);
		return devDevice;
	}
	
	public List<DeviceRunningState> getOnlineStatusList(List<DevDevice> list){
		List<String> listSn = new ArrayList<String>();
		for(DevDevice device:list){
			listSn.add(device.getDeviceSn());
		}
		List<DeviceRunningState> drList = remoteService.getDevDeviceService().getOnlineStatusList(listSn);
		return drList;
	}
	
	public List<Object> queryPageList(HttpServletRequest request){
		
		//RemoteService remoteService = FrameworkHelper.getBean(RemoteService.class);
		Page dfishPage = PubService.getPage(request);
		DevProduct product = (DevProduct) HttpUtil.getSessionAttr(request, "product");
		com.xrj.framework.hibernate.dao.Page page = PageConvertUtil.convertDaoPage(dfishPage);
		page = remoteService.getDevDeviceService().setPageDataList(page, product.getId());
		dfishPage = PageConvertUtil.convertDfishPage(page);
		List<DevDevice> devDeviceList = dfishPage.getResultSet();
		List<Object> list = new ArrayList<Object>();
		list.add(devDeviceList);
		list.add(dfishPage);
		return list;
	}
	/**
	 * 根据设备Id分页查询其下子设备
	 * @param request
	 * @return
	 */
	public List<Object> setPageByParentId(HttpServletRequest request){
		String deviceId = request.getParameter("deviceId");
		DevProduct devProduct = (DevProduct) HttpUtil.getSessionAttr(request, "product");
		Page dfishPage = PubService.getPage(request);
		com.xrj.framework.hibernate.dao.Page page = PageConvertUtil.convertDaoPage(dfishPage);
		page = remoteService.getDevDeviceService().setPageByParentId(page, devProduct.getId(), deviceId);
		dfishPage = PageConvertUtil.convertDfishPage(page);
		List<DevDevice> subDeviceList = dfishPage.getResultSet();
		List<Object> list = new ArrayList<Object>();
		list.add(subDeviceList);
		list.add(dfishPage);
		return list;
	}
	/**
	 * 根据条件分页查询设备
	 * @param request
	 * @return
	 */
	public List<Object> setPageByDevice(HttpServletRequest request){
		String deviceSearch = request.getParameter("deviceSearch");
		DevDevice devDevice = new DevDevice();
		devDevice.setDeviceName(deviceSearch);
		devDevice.setDeviceSn(deviceSearch);
		devDevice.setDeviceSecret(deviceSearch);
		Page dfishPage = PubService.getPage(request);
		DevProduct product = (DevProduct) HttpUtil.getSessionAttr(request, "product");
		com.xrj.framework.hibernate.dao.Page page = PageConvertUtil.convertDaoPage(dfishPage);
		page = remoteService.getDevDeviceService().setPageByDevice(page, product.getId(), devDevice);
		dfishPage = PageConvertUtil.convertDfishPage(page);
		List<DevDevice> subDeviceList = dfishPage.getResultSet();
		List<Object> list = new ArrayList<Object>();
		list.add(subDeviceList);
		list.add(dfishPage);
		return list;
	}
	/**
	 * 修改功能点数据显示
	 * @param devDevice
	 * @return
	 */
	public List<DevProductFunction> filterFunctionData(DevDevice devDevice){
		List<DevProductFunction> list = remoteService.getDevProductFunctionService()
				.getByDeviceSn(devDevice.getDeviceSn());
		int i = 1;
		for (DevProductFunction productFunction : list) {
			for (FunctionTypeEnum functionTypeEnum : FunctionTypeEnum.values()) {
				if (productFunction.getFunctionType().equals(functionTypeEnum.getValue())) {
					productFunction.setFunctionType(functionTypeEnum.getFunctionTypeName());
					break;
				}
			}
			productFunction.setSerialNo(i++);
			if (Utils.notEmpty(productFunction.getValue())) {
				JSONObject jsonObject = JSONObject.fromObject(productFunction.getValue());
				Object object = jsonObject.get("value");
				if(object instanceof Double){
						productFunction.setValue(DevDeviceControlView.formula(productFunction.getMinValue(),
						productFunction.getMaxValue(), (double)object));
				} else {
					productFunction.setValue(object+"");
				}
			}
		}
		return list;
	}
}
