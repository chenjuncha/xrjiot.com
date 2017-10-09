package com.xrj.business.product.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;
import com.rongji.dfish.base.Page;
import com.rongji.dfish.framework.FilterParam;
import com.rongji.dfish.framework.FrameworkHelper;
import com.xrj.api.dto.DevDevice;
import com.xrj.api.dto.DevProduct;
import com.xrj.business.util.PageConvertUtil;
import com.xrj.business.core.base.PubService;
import com.xrj.business.core.base.RemoteService;
import com.xrj.framework.util.HttpUtil;

/**
 * 
 * @author chenjunchao
 * @Date 2017-08-15
 *
 */
@Service
public class DeviceService {
	@Resource
	RemoteService remoteService;
	
	public boolean exportDevice(HttpServletRequest request, HttpServletResponse response) {
		boolean result = false;
		DevProduct devProduct = (DevProduct)HttpUtil.getSessionAttr(request, "product");
		List<DevDevice> devDevice = remoteService.getDevDeviceService().getByProductId(devProduct.getId());
		// 创建一个Excel文件
	    HSSFWorkbook workbook = new HSSFWorkbook();
	    // 创建一个工作表
	    HSSFSheet sheet = workbook.createSheet("设备表一");
	    // 添加表头行
	    HSSFRow hssfRow = sheet.createRow(0);
	    // 设置单元格格式居中
	    HSSFCellStyle cellStyle = workbook.createCellStyle();
	    cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); 
	    // 添加表头内容
	    HSSFCell headCell = hssfRow.createCell(0);
	    headCell.setCellValue("设备唯一标识");
	    headCell.setCellStyle(cellStyle);
	    headCell = hssfRow.createCell(1);
	    headCell.setCellValue("设备证书");
	    headCell.setCellStyle(cellStyle);
	    // 添加数据内容
	    for (int i = 0; i < devDevice.size(); i++) {
	        hssfRow = sheet.createRow((int) i + 1);
	        DevDevice devDevice1 = devDevice.get(i); 
	        // 创建单元格，并设置值
	        HSSFCell cell = hssfRow.createCell(0);
	        cell.setCellValue(devDevice1.getDeviceSn());
	        cell.setCellStyle(cellStyle);
	 
	        cell = hssfRow.createCell(1);
	        cell.setCellValue(devDevice1.getDeviceSecret());
	        cell.setCellStyle(cellStyle);
	    }	 
	    // 保存Excel文件
	    try {
	        response.reset();
	        response.setContentType("application/vnd.ms-excel;charset=utf-8");
	        response.setHeader("Content-Disposition", "attachment;filename="+ new String(("device" + ".xls").getBytes(), "iso-8859-1"));
	        ByteArrayOutputStream os = new ByteArrayOutputStream();
	        workbook.write(os);
	        byte[] content = os.toByteArray();
	        InputStream is = new ByteArrayInputStream(content);
	        ServletOutputStream out = response.getOutputStream();
	        BufferedInputStream bis = null;
	        BufferedOutputStream bos = null;
	        try {
	            bis = new BufferedInputStream(is);
	            bos = new BufferedOutputStream(out);
	            byte[] buff = new byte[2048];
	            int bytesRead;
	            // Simple read/write loop.
	            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
	                bos.write(buff, 0, bytesRead);
	            }
	        } catch (final IOException e) {
	            throw e;
	        } finally {
	             if (bis != null)
	                 bis.close();
	             if (bos != null)
	                 bos.close();
	        }	
            result = true;     
	    } catch (Exception e) {
	    	  e.printStackTrace();
	          return result;
	    }
		return result;
	}
	/**
	 * @根据设备id删除设备
	 * @param Id
	 * @param request
	 * @return
	 */
	public boolean deleteDeviceById(String Id, HttpServletRequest request) {
		boolean result = false;
		DevProduct devProduct = (DevProduct)HttpUtil.getSessionAttr(request, "product");
		Page dfishPage = PubService.getPage(request);
		com.xrj.framework.hibernate.dao.Page page = PageConvertUtil.convertDaoPage(dfishPage);
		String[] sourceStrArray = Id.split(",");
		for(int i = 0; i < sourceStrArray.length; i++) {
		page = remoteService.getDevDeviceService().setPageByParentId(page, devProduct.getId(), sourceStrArray[i]);
			if (page.getResultSet().size() == 0) {
				remoteService.getDevDeviceService().delete(""+sourceStrArray[i]+"");
				result = true;
			} else {
				result = false;
				break;
			}
		}	
		return result;
	}
	/**
	 * @根据数量生成设备
	 * @param number
	 * @param request
	 * @return
	 */
	public boolean createDevice(String number, HttpServletRequest request) {
		boolean result = false;
		DevProduct devProduct = (DevProduct)HttpUtil.getSessionAttr(request, "product");
		try {
			List<DevDevice> devices = remoteService.getDevDeviceService().createByProduct(Integer.parseInt(number), devProduct);
			if(devices.get(0).getDeviceSecret() != null && devices.get(0).getDeviceSecret() != "") {
			   result = true;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * @构建设备分页数据
	 * @param request
	 * @return
	 */
	public List<Object> buildMainData(HttpServletRequest request) {	
     	Page dfishPage = PubService.getPage(request);
		FilterParam fp = PubService.getParam(request);
		RemoteService remoteService = FrameworkHelper.getBean(RemoteService.class);
		com.xrj.framework.hibernate.dao.Page page = PageConvertUtil.convertDaoPage(dfishPage);
		DevProduct devProduct = (DevProduct)HttpUtil.getSessionAttr(request, "product");
		page = remoteService.getDevDeviceService().setPageDataList(page, devProduct.getId());
		dfishPage = PageConvertUtil.convertDfishPage(page);
		List<DevDevice> devDevice = dfishPage.getResultSet();
		
		List<Object> result = new ArrayList<Object>();
		result.add(devDevice);
		result.add(dfishPage);
		result.add(fp);
		return result;
	}
}
