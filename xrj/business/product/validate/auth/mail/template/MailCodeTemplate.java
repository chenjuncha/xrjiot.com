package com.xrj.business.product.validate.auth.mail.template;

public class MailCodeTemplate {
	public static String get(String url){
		StringBuilder contentBuilder=new StringBuilder();
		contentBuilder.append("亲爱的用户:<br>");
		contentBuilder.append("欢迎注册星榕基物联网平台,").append(url).append(" ,(点击该链接你可以激活注册的账号，此链接7天失效，非本人操作请忽略。)");
		contentBuilder.append("星榕基物联网平台");
		return contentBuilder.toString();
	}
}
