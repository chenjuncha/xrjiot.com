package com.xrj.business.product.service;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rongji.dfish.base.Page;
import com.rongji.dfish.framework.FilterParam;
import com.rongji.dfish.framework.FrameworkHelper;
import com.rongji.dfish.framework.plugin.file.service.FileService;
import com.xrj.api.dto.DevDevice;
import com.xrj.api.dto.DevDeviceRom;
import com.xrj.api.dto.DevHardwareVersion;
import com.xrj.api.dto.DevProduct;
import com.xrj.api.dto.DevRomVersionRel;
import com.xrj.api.model.RomUpdateRequestUrl;
import com.xrj.api.support.CallResult;
import com.xrj.business.util.PageConvertUtil;
import com.xrj.business.core.base.PubService;
import com.xrj.business.core.base.RemoteService;
import com.xrj.framework.util.HttpUtil;

import net.sf.json.JSONObject;

/**
 * 
 * @author chenjunchao
 * @Date 2017-08-15
 *
 */
@Service
public class DeviceROMService {

	@Resource
	RemoteService remoteService;
	@Autowired
	private FileService fileService;
	
	/**
	 * @保存ROM
	 * @param request
	 * @param name
	 * @param softVersion
	 * @param hardVersion
	 * @param uploadFile
	 * @return
	 */
	public CallResult<String> saveROM(DevProduct devProduct,  String name, String softVersion, String hardVersion, String uploadFile) {
		CallResult<String>result=new CallResult<String>();
       // DevProduct devProduct = (DevProduct)HttpUtil.getSessionAttr(request, "product");
		String url = null;
		if(uploadFile != null) {
		    JSONObject json = JSONObject.fromObject(uploadFile.substring(1,uploadFile.length()-1));
		    Object object = json.get("url");
		    if(object instanceof String){
		       url = json.getString("url");
		    }   
	    }
		//组装ROM
		DevDeviceRom devDeviceRom = new DevDeviceRom();
		devDeviceRom.setSoftVersion(softVersion);
		devDeviceRom.setVersionName(name);
		devDeviceRom.setAttachmentPath(url);
		devDeviceRom.setProductId(devProduct.getId());
		//分割硬件版本
		String[] sourceStrArray = hardVersion.split(",");
		List<DevRomVersionRel> devRomVersionRel = new ArrayList<DevRomVersionRel>();
		//组装DevRomVersionRel
		for (int i = 0; i< sourceStrArray.length; i++) {
			DevRomVersionRel RomVersionRel = new DevRomVersionRel();
			RomVersionRel.getId().setVersionId(sourceStrArray[i]);	
			RomVersionRel.setDevDeviceRom(devDeviceRom);
			devRomVersionRel.add(RomVersionRel);
		}
		try {
			devDeviceRom=remoteService.getDevDeviceRomService().save(devDeviceRom, devRomVersionRel);	
			result.success();
			result.setData(devDeviceRom.getId());
		} catch(Exception e) {
			result.fail("保存失败");
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * @删除ROM
	 * @param ROMId
	 * @return
	 */
	public boolean deleteROM(String ROMId) {
		boolean result = false;
		try {
			remoteService.getDevDeviceRomService().delete(ROMId);
			result = true;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;	
	}
	/**
	 * @构建ROM分页数据
	 * @param request
	 * @return
	 */
	public List<Object> buildMainData(HttpServletRequest request) {	
     	Page dfishPage = PubService.getPage(request);
		FilterParam fp = PubService.getParam(request);
		RemoteService remoteService = FrameworkHelper.getBean(RemoteService.class);
		com.xrj.framework.hibernate.dao.Page page = PageConvertUtil.convertDaoPage(dfishPage);
		DevProduct devProduct = (DevProduct)HttpUtil.getSessionAttr(request, "product");
		page = remoteService.getDevDeviceRomService().setPageDataList(page, devProduct.getId());
		dfishPage = PageConvertUtil.convertDfishPage(page);
		List<DevDeviceRom> devDeviceRom = dfishPage.getResultSet();
		
		List<Object> result = new ArrayList<Object>();
		result.add(devDeviceRom);
		result.add(dfishPage);
		result.add(fp);
		return result;
	}
	/**
	 * @根据ROMId获取硬件版本
	 * @param ROMId
	 * @return
	 */
	public List<DevHardwareVersion> findDevHardwareVersionByROM(String ROMId) {
		List<DevHardwareVersion> devHardwareVersion = new ArrayList<DevHardwareVersion>();
		try {
			 devHardwareVersion=remoteService.getDevDeviceRomService().getHardwareVersion(ROMId);	
		} catch(Exception e) {
			e.printStackTrace();
		}
		return devHardwareVersion;
	}
	
	/**
	 * @根据产品Id获取硬件版本
	 * @param ROMId
	 * @return
	 */
	public List<DevHardwareVersion> findDevHardwareVersionByProduct(String productId) {
		List<DevHardwareVersion> devHardwareVersion = new ArrayList<DevHardwareVersion>();
		try {
			 devHardwareVersion=remoteService.getDevHardwareVersionService().getByProductId(productId);	
		} catch(Exception e) {
			e.printStackTrace();
		}
		return devHardwareVersion;
	}
	
	/**
	 * @根据id获取ROM
	 * @param ROMId
	 * @return
	 */
	public DevDeviceRom findROM(String ROMId) {
		DevDeviceRom rom = null;
		try {
			rom = remoteService.getDevDeviceRomService().get(ROMId);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return rom;
	}

	public void noticeDeviceUpdate(String romId) {
		DevDeviceRom rom = findROM(romId);
		List<DevHardwareVersion> devHardwareVersionList =  remoteService.getDevDeviceRomService().getHardwareVersion(rom.getId());
		List<String> hardwareVersionNameList = new ArrayList<String>();
		if (devHardwareVersionList != null) {
			for (DevHardwareVersion devHardwareVersion : devHardwareVersionList) {
				hardwareVersionNameList.add(devHardwareVersion.getHardwareVersion());
			}
		}
		List<DevDevice> devices = remoteService.getDevDeviceService().getUpdateDevice(rom.getProductId(),hardwareVersionNameList,rom.getSoftVersion(),-1);
		for (DevDevice device : devices) {
			RomUpdateRequestUrl request=new RomUpdateRequestUrl(device.getDeviceSn(), rom.getAttachmentPath());
			remoteService.getMessageIssueService().issueCommand(device.getDeviceSn(), request);
		}
		
	}
	
}
