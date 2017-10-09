package com.xrj.business.product.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.rongji.dfish.base.Utils;
import com.rongji.dfish.framework.FilterParam;
import com.xrj.api.dto.DevDevice;
import com.xrj.api.dto.DevDeviceLog;
import com.xrj.api.model.DeviceRunningState;
import com.xrj.api.service.DevDeviceLogService;
import com.xrj.api.service.DevDeviceService;
import com.xrj.business.util.PageConvertUtil;
import com.xrj.business.core.base.PubService;
import com.xrj.business.core.base.RemoteService;
import com.xrj.framework.hibernate.dao.Page;

@Component
public class DevDeviceLogIndexService {

	@Resource
	private RemoteService remoteService;

	/**
	 * 日志按时间段分页查询
	 * 
	 * @param page
	 * @param deviceId
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @return
	 */
	public Page<DevDeviceLog> setPageDataList(Page<DevDeviceLog> page, String deviceId, Date startDate, Date endDate) {
		DevDeviceLogService devDeviceLogService = remoteService.getDevDeviceLogService();
		return devDeviceLogService.setPageDataList(page, deviceId, startDate, endDate);
	}

	/**
	 * 查询日志中上下线记录并翻页
	 * 
	 * @param page
	 * @param deviceId
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @return
	 */
	public Page<DevDeviceLog> setPageByOnLineOffline(Page<DevDeviceLog> page, String deviceId, Date startDate,
			Date endDate) {
		DevDeviceLogService devDeviceLogService = remoteService.getDevDeviceLogService();
		return devDeviceLogService.setPageByOnLineOffline(page, deviceId, startDate, endDate);
	}

	/**
	 * 根据设备SN获取设备信息
	 * 
	 * @param deviceSn
	 * @return
	 */
	public DevDevice getByDeviceSn(HttpServletRequest request) {
		DevDeviceService devDeviceService = remoteService.getDevDeviceService();
		String deviceSn = request.getParameter("deviceSn");
		DevDevice devDevice = devDeviceService.getByDeviceSn(deviceSn);
		return devDevice;
	}

	/**
	 * 根据设备获取设备状态
	 * 
	 * @param devDevice
	 * @return
	 */
	public DeviceRunningState getOnlineStatus(DevDevice devDevice) {
		DevDeviceService devDeviceService = remoteService.getDevDeviceService();
		List<String> driverSnList = new ArrayList();
		driverSnList.add(devDevice.getDeviceSn());
		List<DeviceRunningState> deviceRunningStateList = devDeviceService.getOnlineStatusList(driverSnList);
		DeviceRunningState deviceRunningState = new DeviceRunningState();
		if (Utils.notEmpty(deviceRunningStateList)) {
			deviceRunningState = deviceRunningStateList.get(0);
		}
		return deviceRunningState;
	}

	/**
	 * 获取设备日志的列表和分页信息，以list输出
	 * 
	 * @param request
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<Object> queryCommunicationLogPageList(HttpServletRequest request, Date startDate, Date endDate) {

		DevDevice devDevice = this.getByDeviceSn(request);
		com.rongji.dfish.base.Page dfishPage = PubService.getPage(request);
		FilterParam fp = PubService.getParam(request);
		Page page = PageConvertUtil.convertDaoPage(dfishPage);
		page = this.setPageDataList(page, devDevice.getId(), startDate, endDate);
		dfishPage = PageConvertUtil.convertDfishPage(page);
		List<DevDeviceLog> logList = dfishPage.getResultSet();

		List<Object> list = new ArrayList<Object>();
		list.add(logList);
		list.add(dfishPage);
		list.add(fp);
		return list;
	}

	/**
	 * 上下线记录的列表和分页信息，以list输出
	 * 
	 * @param request
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<Object> queryOnLineOffLinePageList(HttpServletRequest request, Date startDate, Date endDate) {

		DevDevice devDevice = this.getByDeviceSn(request);
		com.rongji.dfish.base.Page dfishPage = PubService.getPage(request);
		FilterParam fp = PubService.getParam(request);
		Page page = PageConvertUtil.convertDaoPage(dfishPage);
		page = this.setPageByOnLineOffline(page, devDevice.getId(), startDate, endDate);
		dfishPage = PageConvertUtil.convertDfishPage(page);
		List<DevDeviceLog> logList = dfishPage.getResultSet();

		List<Object> list = new ArrayList<Object>();
		list.add(logList);
		list.add(dfishPage);
		list.add(fp);
		return list;
	}

}
