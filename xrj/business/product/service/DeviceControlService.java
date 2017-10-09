package com.xrj.business.product.service;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Service;

import com.rongji.dfish.base.Page;
import com.rongji.dfish.base.Utils;
import com.xrj.api.dto.DevDevice;
import com.xrj.api.dto.DevProduct;
import com.xrj.api.dto.DevProductFunction;
import com.xrj.api.em.DataTypeEnum;
import com.xrj.api.em.FunctionTypeEnum;
import com.xrj.api.model.DeviceRunningState;
import com.xrj.api.support.CallResult;
import com.xrj.business.util.PageConvertUtil;
import com.xrj.business.core.base.PubService;
import com.xrj.business.core.base.RemoteService;
import com.xrj.framework.util.Assert;
import com.xrj.framework.util.HttpUtil;

import net.sf.json.JSONObject;

/**
 * 
 * @author chenjunchao
 * @Date 2017-08-16
 *
 */
@Service
public class DeviceControlService {
	@Resource
	RemoteService remoteService;
	
	/**
	 * @设备数据推送
	 * @param sn
	 * @param onlineStatus
	 * @param request
	 * @return
	 */
	public List<CallResult> pushData(String sn, String onlineStatus, HttpServletRequest request) {
		List<CallResult>  callResult = new ArrayList<CallResult>();
		List<DevProductFunction> functionPoint=remoteService.getDevProductFunctionService().getByDeviceSn(sn);		
		List<String> codelist = new ArrayList<String>();
		List<String> valuelist = new ArrayList<String>();
		
		/*int booleanData = 0;
        int validData = 0;
		for (int i = 0 ; i < functionPoint.size() ; i++) {
			//传感器和故障类型
		    if ((FunctionTypeEnum.SENSOR.getValue().equals(functionPoint.get(i).getFunctionType())) != true && (FunctionTypeEnum.TROUBLE.getValue().equals(functionPoint.get(i).getFunctionType())) != true ){
			    if(DataTypeEnum.BOOLEAN.getValue().equals(functionPoint.get(i).getDataType()) != true) {
			    	if (request.getParameter(functionPoint.get(i).getId()) != null && request.getParameter(functionPoint.get(i).getId()) != ""){
			    		//除了只读数据和boolean型外,有填的数据
			    		validData++;  
				    }
			    } else {
			    	//非只读boolean型数据	
			    	booleanData++; 
			    }			   
		    }
		}*/
	
		    for (int i = 0 ; i < functionPoint.size() ; i++) {
		    	//传感器和故障类型
		        if ((FunctionTypeEnum.SENSOR.getValue().equals(functionPoint.get(i).getFunctionType())) != true && (FunctionTypeEnum.TROUBLE.getValue().equals(functionPoint.get(i).getFunctionType())) != true) {
		    	    //数值类型
		        	if (DataTypeEnum.NUMBER.getValue().equals(functionPoint.get(i).getDataType())) {	
			        	if (request.getParameter(functionPoint.get(i).getId()) != "" && request.getParameter(functionPoint.get(i).getId()) != null ){
			        		if(functionPoint.get(i).getValue()!=null){
			        		    String d = null;
			        		    JSONObject json = JSONObject.fromObject(functionPoint.get(i).getValue());
			        		    d = json.getString("value");
			        		    if(!d.equals(request.getParameter(functionPoint.get(i).getId()))) {
			        		        codelist.add(functionPoint.get(i).getFunctionCode());
				    	            valuelist.add(request.getParameter(functionPoint.get(i).getId()));
			        		    }
			        		} else {
			        			codelist.add(functionPoint.get(i).getFunctionCode());
			    	            valuelist.add(request.getParameter(functionPoint.get(i).getId()));
			        		}
				        }
			        	//布尔型
			        } else if (DataTypeEnum.BOOLEAN.getValue().equals(functionPoint.get(i).getDataType())) {			  		        	
			        	String d = null;
			        	if(functionPoint.get(i).getValue() != null) {
			        	    JSONObject json = JSONObject.fromObject(functionPoint.get(i).getValue());
			        	    d = json.getString("value");
			        	}
			        	 if ("1".equals(request.getParameter(functionPoint.get(i).getId())) && !"true".equals(d)) {
			        		 codelist.add(functionPoint.get(i).getFunctionCode());
					         valuelist.add("true");	
			        	 }
			        	 if (request.getParameter(functionPoint.get(i).getId()) == null && "true".equals(d)){     		 
			        		 codelist.add(functionPoint.get(i).getFunctionCode());
			    		     valuelist.add("true");	       		 
			        	  }  				                 	
			  	        //枚举型
			        } else if (DataTypeEnum.ENUM.getValue().equals(functionPoint.get(i).getDataType())) {
				        if (request.getParameter(functionPoint.get(i).getId()) != "" && request.getParameter(functionPoint.get(i).getId()) != null ) {
				        	if(functionPoint.get(i).getValue()!=null){
			        		    String d = null;
			        		    JSONObject json = JSONObject.fromObject(functionPoint.get(i).getValue());
			        		    d = json.getString("value");
			        		    if(!d.equals(request.getParameter(functionPoint.get(i).getId()))) {
			        		        codelist.add(functionPoint.get(i).getFunctionCode());
				    	            valuelist.add(request.getParameter(functionPoint.get(i).getId()));
			        		    }
			        		} else {
			        			codelist.add(functionPoint.get(i).getFunctionCode());
			    	            valuelist.add(request.getParameter(functionPoint.get(i).getId()));
			        		}
				        }
			        } else {
				        if (request.getParameter(functionPoint.get(i).getId()) != "" && request.getParameter(functionPoint.get(i).getId()) != null ) {
				        	if(functionPoint.get(i).getValue()!=null){
			        		    String d = null;
			        		    JSONObject json = JSONObject.fromObject(functionPoint.get(i).getValue());
			        		    d = json.getString("value");
			        		    if(!d.equals(request.getParameter(functionPoint.get(i).getId()))) {
			        		        codelist.add(functionPoint.get(i).getFunctionCode());
				    	            valuelist.add(request.getParameter(functionPoint.get(i).getId()));
			        		    }
			        		} else {
			        			codelist.add(functionPoint.get(i).getFunctionCode());
			    	            valuelist.add(request.getParameter(functionPoint.get(i).getId()));
			        		}
				        }
			        }
		        }
		    }
		   //推送取到的值
		     if (codelist.size() != 0) {
			     callResult =  remoteService.getDevProductFunctionService().setDeviceFunction(sn, codelist, valuelist, false);
			     String msg = "后台报错信息为空";
				 int temp = 0;
				 if (callResult != null) {
					 for (int j = 0; j < callResult.size(); j++) {
					     if (callResult.get(j).isSuccess() == false) {
							 break;					
						 } else {
							 temp++;
						 }
					 }				 
				 }
				 if ("true".equals(onlineStatus) && temp == callResult.size()) {
					 for (int i = 0; i < callResult.size(); i++) {
						 callResult.get(i).setMessage("onlinePush");
					 }
				 } else if ("false".equals(onlineStatus) && temp == callResult.size()){
					 for (int i = 0; i < callResult.size(); i++) {
						 callResult.get(i).setMessage("notOnlinePush");
					 }
				 }		  
		     } else {
		    	  CallResult result = new CallResult();
		    	  result.setMessage("nothingPush");
		    	  callResult.add(result);
		     }
			
		 //
		/*} else {
			for (int i = 0; i < callResult.size(); i++) {
				 callResult.get(i).setMessage("nothingPush");
			 }
		}*/
		return callResult;
	}
	
	public static  Object getJsonData(String value) {	
		 Object result = null ;
		 if(!Assert.isEmpty(value)) {
		      JSONObject json = JSONObject.fromObject(value);
		      Object object = json.get("value");
		      if(object instanceof Double){
		    	  result = json.getDouble("value");		    	 
		      } else if(object instanceof Integer) {
		    	  result = json.getInt("value");		    	
		      } else if (object instanceof String){
		    	  result = json.getString("value");
		      } else if (object instanceof Boolean) {
		    	  result = json.getBoolean("value");		    	
		      }
	      }
		return result;	
	}
	
	public DeviceRunningState findRunningStatusBySn(String sn) {
		DeviceRunningState deviceRunningState = new DeviceRunningState();
		// 获得设备运行状态
	    List<String> driverSnList = new ArrayList();
		driverSnList.add(sn);
		List<DeviceRunningState> deviceRunningStateList = remoteService.getDevDeviceService().getOnlineStatusList(driverSnList);	
		if (Utils.notEmpty(deviceRunningStateList)) {		
				for(DeviceRunningState List:deviceRunningStateList){
					if(List.getSn().equals(sn)){
						deviceRunningState = List;
						break;
					}
				}			
		}
		return deviceRunningState;
	}
	
    public List<Object> queryPageList(HttpServletRequest request){		
	   //RemoteService remoteService = FrameworkHelper.getBean(RemoteService.class);
		Page dfishPage = PubService.getPage(request);
		DevProduct product = (DevProduct) HttpUtil.getSessionAttr(request, "product");
		com.xrj.framework.hibernate.dao.Page page = PageConvertUtil.convertDaoPage(dfishPage);
		page = remoteService.getDevDeviceService().getSinmucateDevice(page, product.getId());
		dfishPage = PageConvertUtil.convertDfishPage(page);
		List<DevDevice> devDeviceList = dfishPage.getResultSet();
		List<Object> list = new ArrayList<Object>();
		list.add(devDeviceList);
		list.add(dfishPage);
		return list;
	}
	
	public DevProductFunction findPointById(String id) {
		DevProductFunction devProductFunction = new DevProductFunction();
		devProductFunction=remoteService.getDevProductFunctionService().get(id);
		return devProductFunction;
	}
	
	public static List<DevProductFunction> changeList(List<DevProductFunction> functionPoint) {
		List<DevProductFunction> point = new ArrayList<DevProductFunction>() ;
		for(int i = 0; i < functionPoint.size() ; i++) {
			if(functionPoint.get(i).getDataType().equals(DataTypeEnum.NUMBER.getValue())) {
				if(!Assert.isEmpty(functionPoint.get(i).getValue())) {
				    Object obj = DeviceControlService.getJsonData(functionPoint.get(i).getValue());
				    functionPoint.get(i).setValue(obj == null?"":String.valueOf(obj));
				}
				point.add(functionPoint.get(i));
			}
		}
		for(int i = 0; i < functionPoint.size() ; i++) {
			if(functionPoint.get(i).getDataType().equals(DataTypeEnum.STRING.getValue())) {
				if(!Assert.isEmpty(functionPoint.get(i).getValue())) {
				    Object obj = DeviceControlService.getJsonData(functionPoint.get(i).getValue());
				    functionPoint.get(i).setValue(obj == null?"":String.valueOf(obj));
				}
				point.add(functionPoint.get(i));
			}
		}
		for(int i = 0; i < functionPoint.size() ; i++) {
			if(functionPoint.get(i).getDataType().equals(DataTypeEnum.BOOLEAN.getValue())) {
				if(!Assert.isEmpty(functionPoint.get(i).getValue())) {
				    Object obj = DeviceControlService.getJsonData(functionPoint.get(i).getValue());
				    functionPoint.get(i).setValue(obj == null?"":String.valueOf(obj));
				}
				point.add(functionPoint.get(i));
			}
		}
		for(int i = 0; i < functionPoint.size() ; i++) {
			if(functionPoint.get(i).getDataType().equals(DataTypeEnum.ENUM.getValue())) {
				if(!Assert.isEmpty(functionPoint.get(i).getValue())) {
				    Object obj = DeviceControlService.getJsonData(functionPoint.get(i).getValue());
				    functionPoint.get(i).setValue(obj == null?"":String.valueOf(obj));
				}
				point.add(functionPoint.get(i));
			}
		}
		return point;
		
	}
	public List<DevProductFunction> findPointListByProductId(String id) {
		List<DevProductFunction> functionPoint = new ArrayList<DevProductFunction>();
		functionPoint = remoteService.getDevProductFunctionService().getByProductId(id);	
		return functionPoint;
	}
	public List<DevProductFunction> findPointListBySn(String sn) {
		List<DevProductFunction> functionPoint = new ArrayList<DevProductFunction>();
		functionPoint=remoteService.getDevProductFunctionService().getByDeviceSn(sn);
		return functionPoint;
	}
	
	public DevDevice findDeviceBySn(String sn) {
		DevDevice devDevice = new DevDevice();
		devDevice = remoteService.getDevDeviceService().getByDeviceSn(sn);
		return devDevice;
	}
	
	public boolean hasSimnulateDevice(String productId) {
		boolean result = false;
		result = remoteService.getDevDeviceService().hasSimnulateDevice(productId);
		return result;
	}
	
	public DevDevice getSimnulateDevice(String productId) {
		DevDevice device = new DevDevice();
		device = remoteService.getDevDeviceService().getSimnulateDevice(productId);
		remoteService.getSimnulationDeviceService().create(device.getDeviceSn());
		return device;
	}
	
	public CallResult createSimnulateDevice(DevProduct devProduct) {
		CallResult<String> callResult = new CallResult<String>();
		
		callResult = remoteService.getDevDeviceService().createSimnulateDevice(devProduct);
		remoteService.getSimnulationDeviceService().create(callResult.getData());
		return callResult;
	}
	
	public void online(String sn, boolean online) {
		remoteService.getSimnulationDeviceService().online(sn, online);
	}
}
