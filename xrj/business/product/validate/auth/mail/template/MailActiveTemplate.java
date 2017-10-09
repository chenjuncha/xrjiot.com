package com.xrj.business.product.validate.auth.mail.template;

public class MailActiveTemplate {
	public static String get(String code){
		StringBuilder contentBuilder=new StringBuilder();
		contentBuilder.append("亲爱的用户:");
		contentBuilder.append("你的验证码:,").append(code).append(" ,非本人操作请忽略。");
		contentBuilder.append("星榕基物联网平台");
		return contentBuilder.toString();
	}
}
