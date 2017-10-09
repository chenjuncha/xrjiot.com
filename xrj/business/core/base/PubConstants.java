package com.xrj.business.core.base;

public class PubConstants {
	
	/**********↓系统相关↓**********/
	public static final String ENCODING = "UTF-8";
	
	public static final String TIP_FORM_IS_MODIFIED = "您有内容尚未保存，确认关闭窗口吗？"; 
	
	public static final String CMD_DLG_CLOSE = "if(VM(this).isModified()){VM(this).cmd({type:'confirm',text:'" + TIP_FORM_IS_MODIFIED +
			"',yes:function(){$.dialog(this).close();}});}else{$.dialog(this).close();}";
	
	/**
	 * 审核类型
	 */
	public static final String AUDIT_TYPE_DEV_DEVELOPER = "DEV_DEVELOPER";
	public static final String AUDIT_TYPE_DEV_PRODUCT = "DEV_PRODUCT";
	
	/**
	 * session信息
	 */
	public static final String SESSION_DEV_DEVELOPER = "SESSION_DEV_DEVELOPER";
	public static final String SESSION_SYS_MANAGER = "SESSION_SYS_MANAGER";
	public static final String SESSION_USER_ROLE = "SESSION_USER_ROLE";
	public static final String SESSION_USER_SYSRESOURCE= "SESSION_USER_SYSRESOURCE";

}
