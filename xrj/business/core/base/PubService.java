package com.xrj.business.core.base;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.CollectionUtils;

import com.rongji.dfish.base.Page;
import com.rongji.dfish.base.Utils;
import com.rongji.dfish.framework.FilterParam;
import com.rongji.dfish.framework.FrameworkHelper;
import com.xrj.api.dto.SysDataDict;
import com.xrj.api.dto.SysResource;
import com.xrj.api.service.SysDataDictService;
import com.xrj.business.core.redis.RedisUtil;
import com.xrj.framework.util.MD5;
import com.xrj.framework.util.SaltCrypt;
import com.xrj.framework.util.StringUtils;

/**
 * 公用service方法
 * 
 * @author chenyun
 * @date 2017-07-25
 */
public class PubService {
	
	
	@SuppressWarnings("rawtypes")
	public static Page getPage(HttpServletRequest request) {
		return getPage(request, 10);
	}
	
	@SuppressWarnings("rawtypes")
	public static Page getPage(HttpServletRequest request, int pageSize) {
		return getPage(request.getParameter("cp"), pageSize);
	}
	
	@SuppressWarnings("rawtypes")
	public static Page getPage(String cp, int pageSize) {
		Page page = new Page();
		int cpValue = 1;
		try {
			cpValue = Integer.parseInt(cp);
        } catch (Exception e) {
        	cpValue = 1;
        }
		if (cpValue < 1) {
			cpValue = 1;
		}
		page.setCurrentPage(cpValue);
		if (pageSize < 1) {
			pageSize = 1;
		}
		page.setPageSize(pageSize);
		return page;
	}
	
	/**
	 * 产品审核分页查询的参数
	 * @param request
	 * @return
	 */
	public static FilterParam getParam(HttpServletRequest request) {
		FilterParam fp = new FilterParam();
		fp.registerKey("auditStatus");
		fp.registerKey("likeInfo");
		fp.bindRequest(request);
		return fp;
	}
	
	/**
	 * 查找redis缓存的数据字典MAP
	 * @param dictType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> getDataDictMap(String dictType){
		RedisUtil redisUtil = FrameworkHelper.getBean(RedisUtil.class);
		Object object = redisUtil.get("DATADICTMAP_"+dictType);
		Map<String, String> map = null;
		if(object == null){
			RemoteService remoteService = FrameworkHelper.getBean(RemoteService.class);
			SysDataDictService sysDataDictService = remoteService.getSysDataDictService();
			map = sysDataDictService.findDictTypeMap(dictType);
			redisUtil.set("DATADICTMAP_"+dictType, map);
		}else{
			map = (Map<String, String>) object;
		}
		
		return map;
	}
	
	
	/**
	 * 重新设置数据字典的缓存信息(Map)
	 * @param dictType
	 */
	public static void resetDataDictMap(String dictType){
		RedisUtil redisUtil = FrameworkHelper.getBean(RedisUtil.class);
		RemoteService remoteService = FrameworkHelper.getBean(RemoteService.class);
		SysDataDictService sysDataDictService = remoteService.getSysDataDictService();
		Map<String, String> map = sysDataDictService.findDictTypeMap(dictType);
		redisUtil.set("DATADICTMAP_"+dictType, map);
	}
	
	/**
	 * 查找redis缓存的数据字典LIST
	 * @param dictType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<SysDataDict> getDataDictList(String dictType){
		RedisUtil redisUtil = FrameworkHelper.getBean(RedisUtil.class);
		Object object = redisUtil.get("DATADICTLIST_"+dictType);
		List<SysDataDict> list = null;
		if(object == null){
			RemoteService remoteService = FrameworkHelper.getBean(RemoteService.class);
			SysDataDictService sysDataDictService = remoteService.getSysDataDictService();
			list = sysDataDictService.findListByDictType(dictType);
			redisUtil.set("DATADICTLIST_"+dictType, list);
		}else{
			list = (List<SysDataDict>) object;
		}
		
		return list;
	}
	/**
	 * 重新设置数据字典的缓存信息(LIST)
	 * @param dictType
	 * @return
	 */
	public static void resetDataDictList(String dictType){
		RedisUtil redisUtil = FrameworkHelper.getBean(RedisUtil.class);
		RemoteService remoteService = FrameworkHelper.getBean(RemoteService.class);
		SysDataDictService sysDataDictService = remoteService.getSysDataDictService();
		List<SysDataDict>list = sysDataDictService.findListByDictType(dictType);
		redisUtil.set("DATADICTLIST_"+dictType, list);
	}
	
	/**
	 * 获取 产品分类，行业代码  的多级名称
	 * @param map
	 * @return
	 */
	public static String getDataDictAllName(Map<String, String> map, String code){
		StringBuffer result = new StringBuffer();
		if(StringUtils.isNotEmpty(code)){
			for(int i = 1; i <= code.length(); i++){
			   String temp = code.substring(0, i);
			   if(map.containsKey(temp)){
				   result.append("/"+map.get(temp));
			   }
			}
		}
		return result.length()>1?result.substring(1):result.toString();
		
	}
	
	/**
	 * 获取 产品分类，行业代码  的多级名称(上级的Code，最后替换为00)
	 * @param map
	 * @return
	 */
	public static String getAreaCodeAllName(Map<String, String> map, String code){
		StringBuffer result = new StringBuffer();
		if(StringUtils.isNotEmpty(code)){
			for(int i = 2; i <= code.length(); ){
			   String temp = code.substring(0, i);
			   for(int j = 0 ; j < code.length()-i; j++){
				   temp += "0";
			   }
			   if(map.containsKey(temp)){
				   result.append("/"+map.get(temp));
			   }
			   i += 2;
			}
		}
		return result.length()>1?result.substring(1):result.toString();
		
	}
	
	/**
	 * 递归构建 资源树
	 * @param list
	 * @return
	 */
	public static List<SysResource> buildUserSysResource(List<Map<String, Object>> list){
		List<SysResource> sysResourceseList = new ArrayList<SysResource>();
		List<SysResource> allResource = new ArrayList<SysResource>();
		if(!CollectionUtils.isEmpty(list)){
			for(Map<String, Object> map : list){
				allResource.add(buildSysResource(map));
			}
			for(Map<String, Object> map : list){
				String parentId = (String) map.get("PARENT_ID");
				if(StringUtils.isEmpty(parentId)){
					SysResource sysResource = buildSysResource(map);
					sysResource = bulidParentResource(sysResource, allResource);
					sysResourceseList.add(sysResource);
				}
			}
		}
		return sysResourceseList;
	}
	
	public static SysResource bulidParentResource(SysResource parentResource, List<SysResource> list){
		String parentId = parentResource.getId();
		List<SysResource> childList = new ArrayList<>();
		for(SysResource sysResource : list){
			if(parentId.equals(sysResource.getParentId())){
				sysResource = bulidParentResource(sysResource, list);
				childList.add(sysResource);
			}
		}
		parentResource.setChildList(childList);
		return parentResource;
	}
	
	public static SysResource buildSysResource(Map<String, Object> map){
		SysResource sysResource  = new SysResource();
		sysResource.setId((String) map.get("RES_ID"));
		sysResource.setParentId((String) map.get("PARENT_ID"));
		sysResource.setResName((String) map.get("RES_NAME"));
		sysResource.setResType((String) map.get("RES_TYPE"));
		sysResource.setResImg((String) map.get("RES_IMG"));
		sysResource.setSeq((Integer) map.get("SEQ"));
		sysResource.setUri((String) map.get("URI"));
		sysResource.setMethodName((String) map.get("METHOD_NAME"));
		sysResource.setRemarks((String) map.get("REMARKS"));
		sysResource.setCreateTime((Date) map.get("CREATE_TIME"));
		return sysResource;
	}
	
	/**
	 * 密码加密
	 * @param password
	 * @return
	 */
	public static String encryptPassword(String password) {
		if (Utils.isEmpty(password)) {
			return "";
		}
		return SaltCrypt.encrypt(MD5.encode(password));
	}
	
	/**
	 * 密码验证
	 * @param password MD5加密过一次的值
	 * @param store   数据库保存值
	 * @return
	 */
	public static boolean checkPassword(String passwordMD5, String store){
		return SaltCrypt.checkPass(passwordMD5, store);
	}
	
	public static final String CHECK_CODE_KEY="com.rongji.partysso.CHECK_CODE_KEY";
	
	private static final char[] CHARS={'0','1','2','3','4','5','6','7','8','9',
		'A','B','C','D','E','F','G','H','K','L','M','N','P','Q','R','T','U','V','W','X','Y','Z'};//因为I O S容易混淆
	
	private static final Random RANDOM = new Random(); 
	
	public static String getRandomString(int length){
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<length;i++){
			sb.append(CHARS[RANDOM.nextInt(CHARS.length)]);
		}
		return sb.toString();
	}
	
	public static Color getRandomColor() {
		int x = RANDOM.nextInt(3);
		switch (x) {
		case 0:
			return new Color(37,90,152);
		case 1:
			return new Color(155,62,0);
		case 2:
			return new Color(74,159,18);
		default:
			return Color.BLACK;
		}
	}
	
	public static void drawChars(Graphics g, String code, int width, int height) {
		double rotate = 0.0;
		double lastRotate = 0.0;
		int lastX = 6;
		int lastFontSize = 0;
		char font;
		int size = code.length();
		int minY = height;
		int maxY = 0;
		Graphics2D g2d = (Graphics2D) g;
		// LinLW 2010-11-18 抗锯齿
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
				RenderingHints.VALUE_STROKE_NORMALIZE);
		for (int i = 0; i < size; i++) {
			if (g instanceof Graphics2D) {
				//获取随机旋转角度
				rotate = (RANDOM.nextInt(40) - 20) * Math.PI / 180;
//				rotate = (-30) * Math.PI / 180;
				g2d.rotate(rotate - lastRotate);
				lastRotate = rotate;
			}
			font = code.charAt(i);
			//获取随机字体大小
		
			if(height<20)height=20;//高度必须大于8
			int minFontSize=height/2;
			int fontSize = minFontSize+RANDOM.nextInt(height-minFontSize)*3/4;

			g.setFont(new Font("", Font.BOLD, fontSize*3/4));
			//字体位置，还不完善
			int x = lastX+(int)(lastFontSize*0.70);
			int y = fontSize+RANDOM.nextInt(height-fontSize);
			//y必须比字号大，否则会超出上界
			int x1 = (int) (Math.cos(-rotate)*x-0.75*Math.sin(-rotate)*y);
			int y1 = (int) (0.75*Math.cos(-rotate)*y+Math.sin(-rotate)*x);
						
			if(y<minY)minY = y;
			if(y>maxY)maxY = y;
			g.drawString(font + "", x1, y1);
			lastX = x;
			lastFontSize = fontSize;

		}
		g2d.rotate(-rotate);
	}

}
