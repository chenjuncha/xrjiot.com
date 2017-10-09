package com.xrj.business.product.service;



import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;


import com.xrj.api.dto.DevProduct;
import com.xrj.api.dto.DevProductFunction;
import com.xrj.api.em.DataTypeEnum;
import com.xrj.api.em.FunctionStatusEnum;
import com.xrj.api.em.ProductStatusEnum;
import com.xrj.business.core.base.RemoteService;
import com.xrj.framework.util.HttpUtil;
/**
 * 
 * @author chenjunchao
 * @Date 2017-08-16
 *
 */
@Service
public class FunctionPointService {
	@Resource
	RemoteService remoteService;	
	@Resource
	private DeviceControlService deviceControlService;
	
	/**
	 * @功能点发布
	 * @param productId
	 * @return
	 * @throws Exception
	 */
	public boolean publishPoint(String productId) throws Exception {
		boolean result = false;
		try {
		    remoteService.getDevProductFunctionService().updateToPublishedStatus(productId);
		    result = true;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * @功能点发布前检查
	 * @param productId
	 * @return
	 * @throws Exception
	 */
	public boolean checkPoint(String productId) throws Exception {
		boolean result = false;
		List<DevProductFunction> functionPoint= deviceControlService.findPointListByProductId(productId);
		int temp = 0;
		for(int i = 0; i < functionPoint.size(); i++) {
			if(FunctionStatusEnum.UNPUBLISHED.getIndex().equals(functionPoint.get(i).getStatus()) == true) {						
				temp++;
			}
		}
		if(temp != 0) {
		   result = true;
		} 
		return result;
	}
	/**
	 * @更新功能点
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public boolean updatePoint(HttpServletRequest request) throws Exception {
		boolean result = false;
		DevProductFunction productFunction = new DevProductFunction();	
		DevProduct devProduct = (DevProduct)HttpUtil.getSessionAttr(request, "product");	
	
		String functionPointId = request.getParameter("id");
		String functionCode = request.getParameter("functionCode");
		if (!remoteService.getDevProductFunctionService().checkFunctionCode(devProduct.getId(), functionCode, functionPointId)) {		
			productFunction.setFunctionCode(functionCode);
		
			String functionType = request.getParameter("functionType");
			productFunction.setFunctionType(functionType);
			
			String functionName = request.getParameter("functionName");
			productFunction.setFunctionName(functionName);
		
			String dataType = request.getParameter("dataType");
			productFunction.setDataType(dataType);
			    //数值类型
			if (DataTypeEnum.NUMBER.getValue().equals(dataType)) {
				String minValue = request.getParameter("minValue");
				String maxValue = request.getParameter("maxValue");
				String resolution = request.getParameter("resolution");
				productFunction.setResolution(Double.parseDouble((resolution)));
				productFunction.setMinValue(Double.parseDouble(minValue));
				productFunction.setMaxValue(Double.parseDouble(maxValue));
				//枚举类型
			} else if (DataTypeEnum.ENUM.getValue().equals(dataType)) {
				String enumeration = request.getParameter("enumeration");
				productFunction.setEnumeration(enumeration);
				//字符类型
			} else if (DataTypeEnum.STRING.getValue().equals(dataType)) {
				String length = request.getParameter("length");
				productFunction.setLength(Integer.parseInt(length));
			}
			String remark = request.getParameter("remark");
			productFunction.setRemark(remark);
			productFunction.setProductId(""+devProduct.getId()+"");
		
			productFunction.setId(functionPointId);
			productFunction.setStatus(FunctionStatusEnum.UNPUBLISHED.getIndex());
							
			remoteService.getDevProductFunctionService().update(productFunction);		
			result = true;
		} 	
		return result;
	}
	/**
	 * @新增功能点
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public boolean savePoint(HttpServletRequest request) throws Exception {
		boolean result = false;
		DevProductFunction productFunction = new DevProductFunction();
		DevProduct devProduct = (DevProduct)HttpUtil.getSessionAttr(request, "product");		
		
		String functionCode = request.getParameter("functionCode");	
		if (!remoteService.getDevProductFunctionService().checkFunctionCode(devProduct.getId(), functionCode)) {
			String functionName = request.getParameter("functionName");
		
			productFunction.setFunctionName(functionName);
			productFunction.setFunctionCode(functionCode);
			
			String functionType = request.getParameter("functionType");
			productFunction.setFunctionType(functionType);
		
			String dataType = request.getParameter("dataType");
			productFunction.setDataType(dataType);
		        //数值类型
			if (DataTypeEnum.NUMBER.getValue().equals(dataType)) {
				String minValue = request.getParameter("minValue");
				String maxValue = request.getParameter("maxValue");
				String resolution = request.getParameter("resolution");
				productFunction.setResolution(Double.parseDouble(resolution));
				productFunction.setMinValue(Double.parseDouble(minValue));
				productFunction.setMaxValue(Double.parseDouble(maxValue));
				//枚举类型
			} else if (DataTypeEnum.ENUM.getValue().equals(dataType)) {
				String enumeration = request.getParameter("enumeration");
				productFunction.setEnumeration(enumeration);
				//字符类型
			} else if (DataTypeEnum.STRING.getValue().equals(dataType)) {
				String length = request.getParameter("length");
				productFunction.setLength(Integer.parseInt(length));
			}
		
			String remark = request.getParameter("remark");
			productFunction.setRemark(remark);
			
			productFunction.setProductId(""+devProduct.getId()+"");
			
			productFunction.setCreateTime(new Date());
			productFunction.setStatus(FunctionStatusEnum.UNPUBLISHED.getIndex());
			
			remoteService.getDevProductFunctionService().save(productFunction);
			//修改审核状态
			devProduct.setAuditStatus(ProductStatusEnum.UNPUBLISHED.getIndex());  
			result = true;
		} 
		return result;
	}
}
