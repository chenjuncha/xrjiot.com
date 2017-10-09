package com.xrj.business.product.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.rongji.dfish.base.Utils;
import com.xrj.api.dto.DevDeveloper;
import com.xrj.api.dto.SysDataDict;
import com.xrj.api.em.AuditStatusEnum;
import com.xrj.api.em.DataDictEnum;
import com.xrj.api.em.StatusEnum;
import com.xrj.api.service.DevCodeService;
import com.xrj.api.service.DevDeveloperService;
import com.xrj.api.support.CallResult;
import com.xrj.business.core.base.PubService;
import com.xrj.business.core.base.RemoteService;
import com.xrj.business.product.validate.auth.mail.MailSender;
import com.xrj.framework.util.Assert;
import com.xrj.framework.util.SaltCrypt;

import net.sf.json.JSONObject;

@Service
public class DeveloperBusiness {
	private @Resource RemoteService remoteService;
	private final static long expireTime = 7*24*60 * 60 * 1000;

	/**
	 * 注册
	 * 
	 * @param email
	 * @param password
	 * @return
	 */
	public int register(String email, String password) {
		DevDeveloperService developerService = remoteService.getDevDeveloperService();
		DevDeveloper devDeveloper = developerService.getByLoginName(email);
		if (Assert.notEmpty(devDeveloper)) {
			/*String status = devDeveloper.getStatus();
			if (Assert.notEmpty(status)) {
				if ("-1".equals(status)) {
					devDeveloper.setPassword(SaltCrypt.encrypt(password));
					devDeveloper.setCreateTime(new Date());
					developerService.updateDevDeveloper(devDeveloper);
					return 1;
				}
				if ("1".equals(status))
					return 0;
			}*/
			return 0;
		} else {
			devDeveloper = new DevDeveloper();
			devDeveloper.setLoginName(email);
			devDeveloper.setPassword(SaltCrypt.encrypt(password));
			devDeveloper.setStatus(StatusEnum.INACTIVE.getIndex());
			devDeveloper.setCreateTime(new Date());
			devDeveloper = developerService.saveDevDeveloper(devDeveloper);
			MailSender.getInstance().sendActiveUrl(devDeveloper.getId(), email);
			return 1;
		}
	}

	public CallResult activeDeveloper(String id) {
		CallResult res = new CallResult();
		DevDeveloperService developerService = remoteService.getDevDeveloperService();
		DevDeveloper devDeveloper = developerService.get(id);
		if (devDeveloper != null) {
			if (System.currentTimeMillis() - devDeveloper.getCreateTime().getTime() <= expireTime) {
				if (StatusEnum.INACTIVE.getIndex().equals(devDeveloper.getStatus())) {
					devDeveloper.setStatus(StatusEnum.NORMAL.getIndex());
					developerService.updateDevDeveloper(devDeveloper);
					res.success("激活成功");
				} else {
					res.fail("已经激活，不需要重复激活");
				}
			} else {
				res.fail("链接已经失效");
			}
		} else {
			res.fail("无效链接");

		}
		return res;

	}
	public CallResult reSendEmail(String id) {
		CallResult res = new CallResult();
		DevDeveloperService developerService = remoteService.getDevDeveloperService();
		DevDeveloper devDeveloper = developerService.get(id);
		if (devDeveloper != null) {
			devDeveloper.setCreateDate(new Date());
			developerService.updateDevDeveloper(devDeveloper);
			MailSender.getInstance().sendActiveUrl(devDeveloper.getId(), devDeveloper.getLoginName());
			res.success("发送成功");
		}else{
			res.fail("发送失败");
		}
		return res;
	}
	public CallResult sendCode(String email) {
		CallResult res = new CallResult();
		DevCodeService codeService=remoteService.getCodeService();
		DevDeveloperService developerService = remoteService.getDevDeveloperService();
		DevDeveloper devDeveloper = developerService.getByLoginName(email);
		if (devDeveloper != null) {
			CallResult<String> callResult = codeService.create(email);
			if (callResult.isSuccess()) {
				res.success("发送成功");
				MailSender.getInstance().sendCode(callResult.getData(), email);
			}else{
				res.fail(callResult.getMessage());
			}
			
		}else{
			res.fail("发送失败");
		}
		return res;
	}
	
	public CallResult findPassword(String code, String email, String password) {
		CallResult res=new CallResult();
		if (Assert.notEmpty(email)) {
			DevDeveloperService developerService = remoteService.getDevDeveloperService();
			DevCodeService codeService=remoteService.getCodeService();
			DevDeveloper devDeveloper = developerService.getByLoginName(email);
			if (Assert.notEmpty(devDeveloper)) {
				if (Assert.notEmpty(code)) {
					CallResult<String> callResult = codeService.checkCode(email, code);
					if (callResult.isSuccess()) {
						if (Assert.notEmpty(password)) {
							devDeveloper.setPassword(SaltCrypt.encrypt(password));
							developerService.updateDevDeveloper(devDeveloper);
							res.success("修改成功");
						}else {
							res.fail("修改的密码不能为空");
						}
					}else{
						res.fail(callResult.getMessage());
					}
				}else {
					res.fail("验证码不能为空");
				}
			}else {
				res.fail("此邮箱未注册！");
			}
		}else{
			res.fail("邮箱不能为空");
			
		}
		return res;
	}
	
	/**
	 * @param devDeveloper 
	 * @更新个人开发者数据
	 * @return
	 */
	public CallResult updateDeveloper(HttpServletRequest request, DevDeveloper devDeveloper) {
		CallResult res=new CallResult();
		int temp = 0;
		if(!request.getParameter("linkman").equals(devDeveloper.getContactName())) {
			devDeveloper.setContactName(request.getParameter("linkman"));
			temp++;
		}
		if(!request.getParameter("accountName").equals(devDeveloper.getUserName())) {
			devDeveloper.setUserName(request.getParameter("linkman"));
			temp++;
		}
		if(!request.getParameter("QQ").equals(devDeveloper.getQq())) {
			devDeveloper.setQq(request.getParameter("QQ"));
			temp++;
		}
		if(!request.getParameter("tel").equals(devDeveloper.getPhone())) {
			//第一次填写数据，让其状态为不通过
			if(Assert.isEmpty(devDeveloper.getPhone())) {
			    devDeveloper.setAuditStatus(AuditStatusEnum.NOPASS.getIndex());
			    devDeveloper.setPhone(request.getParameter("tel"));
			//后来修改数据,状态待审核
			} else {
				devDeveloper.setAuditStatus(AuditStatusEnum.HANDLING.getIndex());
				devDeveloper.setPhone(request.getParameter("tel"));
			}
			temp++;
		}
		if(!request.getParameter("linkmanEmail").equals(devDeveloper.getContactEmail())) {
			devDeveloper.setContactEmail(request.getParameter("linkmanEmail"));
			temp++;
		}
		StringBuilder sb = new StringBuilder();
		sb.append(request.getParameter("provinc")+"-");
		sb.append(request.getParameter("City")+"-");
		sb.append(request.getParameter("Area")+"-");
		sb.append(request.getParameter("address"));
		if(!sb.toString().equals(devDeveloper.getAddr())) {
			devDeveloper.setAddr(sb.toString());
			temp++;
		}
		if(temp != 0) {
			remoteService.getDevDeveloperService().updateDevDeveloper(devDeveloper);
			res.setSuccee(true);
		} else {
			res.setSuccee(false);
		}
		return res;
	}
	
	/**
	 *@企业开发者提交审核数据
	 * @param request
	 * @param devDeveloper
	 * @throws ParseException
	 */
	public  void certEnterDeveloper(HttpServletRequest request, DevDeveloper devDeveloper) throws ParseException {
		CallResult res=new CallResult();
		devDeveloper.setUserName(request.getParameter("enterpriseName"));
		devDeveloper.setCompanyNature(request.getParameter("enterpriseType"));
		devDeveloper.setLegalPersonName(request.getParameter("legalPersonName"));
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");	
		java.util.Date date=sdf.parse(request.getParameter("yyyymmdd"));  
		devDeveloper.setCreateDate(date);
		devDeveloper.setLicenseNo(request.getParameter("businessLicence"));
		devDeveloper.setLicenseType(request.getParameter("licenceType"));	
		StringBuilder sb = new StringBuilder();
		sb.append(request.getParameter("bigClass")+"-");
		sb.append(request.getParameter("smallClass")+"-");
		sb.append(request.getParameter("classType"));			
		StringBuilder sb2 = new StringBuilder();
		sb2.append(request.getParameter("provinc")+"-");
		sb2.append(request.getParameter("City")+"-");
		sb2.append(request.getParameter("Area"));		
		devDeveloper.setIndustry(sb.toString());
		devDeveloper.setRegisterAddr(sb2.toString());
		String uploadFile = request.getParameter("uploadFile");
    	String url = null;			
		JSONObject json = JSONObject.fromObject(uploadFile.substring(1,uploadFile.length()-1));
		Object object = json.get("url");
	    if(object instanceof String) {
		    url = json.getString("url");
		    devDeveloper.setLicenseUpload(url);;	   
		}   	
	    devDeveloper.setAuditStatus(AuditStatusEnum.HANDLING.getIndex());
	    remoteService.getDevDeveloperService().updateDevDeveloper(devDeveloper);
	}
	/**
	 * @param devDeveloper 
	 * @更新企业开发者数据
	 * @return
	 */
	public CallResult updateEnterDeveloper(HttpServletRequest request, DevDeveloper devDeveloper) {
		CallResult res=new CallResult();
		int temp = 0;
		//待审核或成功状态
		if(devDeveloper.getAuditStatus().equals(AuditStatusEnum.PASS.getIndex()) || devDeveloper.getAuditStatus().equals(AuditStatusEnum.HANDLING.getIndex()) ) {
		    if(!request.getParameter("domainName").equals(devDeveloper.getDomainName())) {
			    devDeveloper.setDomainName(request.getParameter("domainName"));
			    temp++;
		    }
		    if(!Assert.isEmpty(request.getParameter("uploadFile"))) {
		    	String uploadFile = request.getParameter("uploadFile");
		    	String url = null;			
				JSONObject json = JSONObject.fromObject(uploadFile.substring(1,uploadFile.length()-1));
				Object object = json.get("url");
			    if(object instanceof String) {
				    url = json.getString("url");
				    devDeveloper.setLogo(url);
				    temp++;
				}   		    
		    }
		    if(!request.getParameter("linkman").equals(devDeveloper.getContactName())) {
			    devDeveloper.setContactName(request.getParameter("linkman"));
			    temp++;
		    }
		    if(!request.getParameter("QQ").equals(devDeveloper.getQq())) {
			    devDeveloper.setQq(request.getParameter("QQ"));
			    temp++;
		    }
		    if(!request.getParameter("tel").equals(devDeveloper.getPhone())) {
			    devDeveloper.setPhone(request.getParameter("tel"));
			    temp++;
		    }
		    if(!request.getParameter("linkmanEmail").equals(devDeveloper.getContactEmail())) {
			    devDeveloper.setContactEmail(request.getParameter("linkmanEmail"));
			    temp++;
		    }
		    if(!request.getParameter("dept").equals(devDeveloper.getDept())) {
			    devDeveloper.setDept(request.getParameter("dept"));
			    temp++;
		    }
		    if(!request.getParameter("address").equals(devDeveloper.getAddr())) {
			    devDeveloper.setAddr(request.getParameter("address"));
			    temp++;
		    }	    	    
		} else {
			    if(!Assert.isEmpty(request.getParameter("uploadFile"))) {
			    	String uploadFile = request.getParameter("uploadFile");
			    	String url = null;			
					JSONObject json = JSONObject.fromObject(uploadFile.substring(1,uploadFile.length()-1));
					Object object = json.get("url");
				    if(object instanceof String) {
					    url = json.getString("url");
					    devDeveloper.setLogo(url);
					    temp++;
					}   		    
			    }
			    if(!request.getParameter("enterpriseName").equals(devDeveloper.getUserName())) {
			        devDeveloper.setUserName(request.getParameter("domainName"));
			        temp++;
		        }
			    if(!request.getParameter("domainName").equals(devDeveloper.getDomainName())) {
				    devDeveloper.setDomainName(request.getParameter("domainName"));
				    temp++;
			    }
			    if(!request.getParameter("linkman").equals(devDeveloper.getContactName())) {
				    devDeveloper.setContactName(request.getParameter("linkman"));
				    temp++;
			    }
			    if(!request.getParameter("QQ").equals(devDeveloper.getQq())) {
				    devDeveloper.setQq(request.getParameter("QQ"));
				    temp++;
			    }
			    if(!request.getParameter("tel").equals(devDeveloper.getPhone())) {
				    devDeveloper.setPhone(request.getParameter("tel"));
				    temp++;
			    }
			    if(!request.getParameter("linkmanEmail").equals(devDeveloper.getContactEmail())) {
				    devDeveloper.setContactEmail(request.getParameter("linkmanEmail"));
				    temp++;
			    }
			    if(!request.getParameter("dept").equals(devDeveloper.getDept())) {
				    devDeveloper.setDept(request.getParameter("dept"));
				    temp++;
			    }
			    if(!request.getParameter("address").equals(devDeveloper.getAddr())) {
				    devDeveloper.setAddr(request.getParameter("address"));
				    temp++;
			    }	  
			    StringBuilder sb = new StringBuilder();
				sb.append(request.getParameter("bigClass")+"-");
				sb.append(request.getParameter("smallClass")+"-");
				sb.append(request.getParameter("classType"));
				if(!sb.toString().equals(devDeveloper.getIndustry())) {
					devDeveloper.setIndustry(sb.toString());
					temp++;
				}
				StringBuilder sb2 = new StringBuilder();
				sb2.append(request.getParameter("provinc")+"-");
				sb2.append(request.getParameter("City")+"-");
				sb2.append(request.getParameter("Area"));	
				if(!sb2.toString().equals(devDeveloper.getRegisterAddr())) {
					devDeveloper.setRegisterAddr(sb.toString());
					temp++;
				}
		}
		if(temp != 0) {
			remoteService.getDevDeveloperService().updateDevDeveloper(devDeveloper);
			res.setSuccee(true);
		} else {
			res.setSuccee(false);
		}
		return res;
	}
	/**
	 * @根据省查找市
	 * @param code
	 * @return
	 */
	public static Map<String, List<SysDataDict>> queryCityListMap(String code) {
		Map<String, List<SysDataDict>> map = new HashMap<String, List<SysDataDict>>();
		List<SysDataDict> listAll = PubService.getDataDictList(DataDictEnum.area_code.toString());
		List<SysDataDict> list1 = new ArrayList<SysDataDict>();
		List<SysDataDict> list2 = new ArrayList<SysDataDict>();
		for (SysDataDict sysDataDict : listAll) {
			if (sysDataDict.getDictCode().substring(2, 6).equals("0000")) {
				list1.add(sysDataDict);
			}  else if (sysDataDict.getDictCode().substring(4, 6).equals("00") && sysDataDict.getDictCode().substring(0, 2).equals(code.substring(0, 2)) && !sysDataDict.getDictCode().substring(2, 4).equals("00")) {
				list2.add(sysDataDict);
			}			
		}
		map.put("list1", list1);
		map.put("list2", list2);
		return map;
	}
	
	/**
	 * @根据大类查找小类
	 * @param code
	 * @return
	 */
	public static Map<String, List<SysDataDict>> querySmallListMap(String code) {
		Map<String, List<SysDataDict>> map = new HashMap<String, List<SysDataDict>>();
		List<SysDataDict> listAll = PubService.getDataDictList(DataDictEnum.industry_code.toString());
		List<SysDataDict> list1 = new ArrayList<SysDataDict>();
		List<SysDataDict> list2 = new ArrayList<SysDataDict>();
		for (SysDataDict sysDataDic : listAll) {
			if (sysDataDic.getDictCode().length() == 1) {
				list1.add(sysDataDic);
				 for (SysDataDict sysDataDict : listAll) {
					    if(sysDataDict.getDictCode().length() == 2 && sysDataDict.getDictCode().substring(0, 1).equals(code)) {
					    	list2.add(sysDataDict);				    	
					    } 
					    if(sysDataDict.getDictCode().length() == 3 ) {
					    	for (SysDataDict sysDataDicts : listAll) {
					    		if(!sysDataDict.getDictCode().substring(0, 2).equals(sysDataDicts.getDictCode()) && sysDataDict.getDictCode().substring(0, 1).equals(code)) {
					    			list2.add(sysDataDict);	
					    			break;
					    		}
					    	}		    				    	
					    } 	    
				  }
			}  		
		}
		map.put("list1", list1);
		map.put("list2", list2);
		return map;
	}
	
	/**
	 * @根据小类查找具体行业
	 * @param code
	 * @return
	 */
	public static List<SysDataDict> queryTypeList(String code) {
		List<SysDataDict> listAll = PubService.getDataDictList(DataDictEnum.industry_code.toString());
		List<SysDataDict> list3 = new ArrayList<SysDataDict>();
		if(code.length() == 2) {
	        for (SysDataDict sysDataDict : listAll) {
		        if(sysDataDict.getDictCode().length() > 2 && sysDataDict.getDictCode().substring(0, 2).equals(code)) {
		    	    list3.add(sysDataDict);
		        }
	        }
		}
		if(code.length() == 3) {
			 for (SysDataDict sysDataDict : listAll) {
			     if(sysDataDict.getDictCode().length() > 3 && sysDataDict.getDictCode().substring(0, 3).equals(code)) {
			         list3.add(sysDataDict);
			     }
		     }
		}
		return list3;
	}
	
	/**
	 * @根据数据库地址取得三个行业list数据
	 * @param address
	 * @return
	 */
	public static Map<String, List<SysDataDict>> IndustryDate(String address) {
		String[] StrArray = null;
		Map<String, List<SysDataDict>> mapClass = null ;
		if(!Assert.isEmpty(address)) {
		    StrArray = address.split("-");	
		    mapClass = DeveloperBusiness.querySmallListMap(StrArray[0]);
		    mapClass.put("list3", DeveloperBusiness.queryTypeList(StrArray[1]));
		} else {
			mapClass = DeveloperBusiness.buildTradeClassListMap(null);						
		}
		return mapClass ;
	}
	
	/**
	 * @根据code查询对应名称
	 * @param code
	 * @param type
	 * @return
	 */
	public static String baseCodefindName(String code,String type) {
		String result = null;
		StringBuilder sb = new StringBuilder();
		List<SysDataDict> listAll = null;
		if(type.equals("area")) {
		    listAll = PubService.getDataDictList(DataDictEnum.area_code.toString());
		} else {
			listAll = PubService.getDataDictList(DataDictEnum.industry_code.toString());
		}
		String[] StrArray = code.split("-");
		for (SysDataDict sysDataDict : listAll) {
			if (sysDataDict.getDictCode().equals(StrArray[0])) {
				sb.append(sysDataDict.getDictName()+"——");				
			}  	
			if (sysDataDict.getDictCode().equals(StrArray[1])) {
				sb.append(sysDataDict.getDictName()+"——");				
			}  
			if (sysDataDict.getDictCode().equals(StrArray[2])) {
				sb.append(sysDataDict.getDictName());				
			}  
		}
		result = sb.toString();
		return result;
	}
	
	/**
	 * @根据市查找区
	 * @param code
	 * @return
	 */
	public static List<SysDataDict> queryAreaList(String code) {
		List<SysDataDict> listAll = PubService.getDataDictList(DataDictEnum.area_code.toString());
		List<SysDataDict> list3 = new ArrayList<SysDataDict>();
		for (SysDataDict sysDataDict : listAll) {
			if ( !sysDataDict.getDictCode().substring(4, 6).equals("00") && sysDataDict.getDictCode().substring(0, 4).equals(code.substring(0, 4))) {
				list3.add(sysDataDict);
			}  		
		}
		return list3;
	}
	
	/**
	 * @根据数据库地址取得三个地址list数据
	 * @param address
	 * @return
	 */
	public static Map<String, List<SysDataDict>> AreaDate(String address) {
		String[] StrArray = null;
		Map<String, List<SysDataDict>> mapClass = null ;
		if(!Assert.isEmpty(address)) {
		    StrArray = address.split("-");	
		    mapClass = DeveloperBusiness.queryCityListMap(StrArray[0]);
		    mapClass.put("list3", DeveloperBusiness.queryAreaList(StrArray[1]));
		} else {
			mapClass = DeveloperBusiness.buildAreaClassListMap(null);						
		}
		return mapClass ;
	}
	/**
	 * 从缓存中提取省市区数据
	 * 
	 * @param code
	 * @return
	 */
	public static Map<String, List<SysDataDict>> buildAreaClassListMap(String code) {
		Map<String, List<SysDataDict>> map = new HashMap<String, List<SysDataDict>>();
		List<SysDataDict> listAll = PubService.getDataDictList(DataDictEnum.area_code.toString());
		List<SysDataDict> list1 = new ArrayList<SysDataDict>();
		List<SysDataDict> list2 = new ArrayList<SysDataDict>();
		List<SysDataDict> list3 = new ArrayList<SysDataDict>();
		
		if (Utils.isEmpty(code)) {
			// 初始化加载数据
			for (SysDataDict sysDataDict : listAll) {
				if (sysDataDict.getDictCode().substring(2, 6).equals("0000")) {
					list1.add(sysDataDict);
				} else if (sysDataDict.getDictCode().substring(4, 6).equals("00") && sysDataDict.getDictCode().substring(0, 2).equals("11") && !sysDataDict.getDictCode().substring(2, 4).equals("00")) {
					list2.add(sysDataDict);
				} else if(!sysDataDict.getDictCode().substring(4, 6).equals("00") && sysDataDict.getDictCode().substring(0, 4).equals("1101")){
					list3.add(sysDataDict);
				}
			}
			map.put("list1", list1);
			map.put("list2", list2);
			map.put("list3", list3);
		} else {
			if(code.substring(2, 6).equals("0000")) {
			    for (SysDataDict sysDataDict : listAll) {
				    if(!sysDataDict.getDictCode().substring(2, 4).equals("00") && sysDataDict.getDictCode().substring(4, 6).equals("00") && code.subSequence(0, 2).equals(sysDataDict.getDictCode().substring(0, 2))) {
				    	list2.add(sysDataDict);		    	
				    } 
			    }
			    map.put("list2", list2);
			    for (SysDataDict sysDataDict : listAll) {
				    if(!sysDataDict.getDictCode().substring(4, 6).equals("00") && sysDataDict.getDictCode().substring(0, 4).equals(list2.get(0).getDictCode().substring(0, 4))) {
				    	list3.add(sysDataDict);		    	
				    } 
			    }
				map.put("list3", list3);
			} else {
			    for (SysDataDict sysDataDict : listAll) {
				    if( code.subSequence(0, 4).equals(sysDataDict.getDictCode().substring(0, 4))&& !sysDataDict.getDictCode().substring(4, 6).equals("00")) {
				    	list3.add(sysDataDict);
				    }
			    }
				map.put("list3", list3);
			}
		}
		return map;
	}

	/**
	 * 从缓存中提取行业数据
	 * 
	 * @param code
	 * @return
	 */
	public static Map<String, List<SysDataDict>> buildTradeClassListMap(String code) {
		Map<String, List<SysDataDict>> map = new HashMap<String, List<SysDataDict>>();
		List<SysDataDict> listAll = PubService.getDataDictList(DataDictEnum.industry_code.toString());
		List<SysDataDict> list1 = new ArrayList<SysDataDict>();
		List<SysDataDict> list2 = new ArrayList<SysDataDict>();
		List<SysDataDict> list3 = new ArrayList<SysDataDict>();
		
		if (Utils.isEmpty(code)) {
			// 初始化加载数据
			for (SysDataDict sysDataDict : listAll) {
				if (sysDataDict.getDictCode().length() == 1) {
					list1.add(sysDataDict);
				} else if (sysDataDict.getDictCode().length() == 2 && sysDataDict.getDictCode().substring(0, 1).equals("A") ) {
					list2.add(sysDataDict);
				} else if(sysDataDict.getDictCode().length() > 2 && sysDataDict.getDictCode().substring(0, 2).equals("A1")){
					list3.add(sysDataDict);
				}
			}
			map.put("list1", list1);
			map.put("list2", list2);
			map.put("list3", list3);
		} else {
			if(code.length() == 1) {
			    for (SysDataDict sysDataDict : listAll) {
				    if(sysDataDict.getDictCode().length() == 2 && sysDataDict.getDictCode().substring(0, 1).equals(code)) {
				    	list2.add(sysDataDict);				    	
				    } 
				    if(sysDataDict.getDictCode().length() == 3 ) {
				    	for (SysDataDict sysDataDicts : listAll) {
				    		if(!sysDataDict.getDictCode().substring(0, 2).equals(sysDataDicts.getDictCode()) && sysDataDict.getDictCode().substring(0, 1).equals(code)) {
				    			list2.add(sysDataDict);	
				    			break;
				    		}
				    	}		    				    	
				    } 	    
			    }
			    map.put("list2", list2);
			    if(!list2.isEmpty()) {
			        for (SysDataDict sysDataDict : listAll) {
				        if(sysDataDict.getDictCode().substring(0, sysDataDict.getDictCode().length()-1).equals(list2.get(0).getDictCode())) {				    	    
				    	    list3.add(sysDataDict);		    	
				        } 
			        }				
			    } else {
			    	for (SysDataDict sysDataDict : listAll) {
			    		if(sysDataDict.getDictCode().substring(0, 1).equals(code)) {
			    	        list3.add(sysDataDict);		    	
			    	    }
			    	}
			    }
			    map.put("list3", list3);
			} else {
				if(code.length() == 2) {
			        for (SysDataDict sysDataDict : listAll) {
				        if(sysDataDict.getDictCode().length() > 2 && sysDataDict.getDictCode().substring(0, 2).equals(code)) {
				    	    list3.add(sysDataDict);
				        }
			        }
				}
				if(code.length() == 3) {
					 for (SysDataDict sysDataDict : listAll) {
					     if(sysDataDict.getDictCode().length() > 3 && sysDataDict.getDictCode().substring(0, 3).equals(code)) {
					         list3.add(sysDataDict);
					     }
				     }
				}
				map.put("list3", list3);
			}
		}
		return map;
	}

}