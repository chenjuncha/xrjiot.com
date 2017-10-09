package com.xrj.business.product.validate.auth.mail;

import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.xrj.api.support.CallResult;
import com.xrj.business.product.validate.auth.mail.template.MailActiveTemplate;
import com.xrj.business.product.validate.auth.mail.template.MailCodeTemplate;
import com.xrj.business.util.SpringUtils;

public class MailSender {
	private static MailSender instance;
	private MailSender(){}
	Logger logger=Logger.getLogger(this.getClass());
	JavaMailSender javaMailSender =SpringUtils.getBean("javaMailSender");
	private final static String url="http://127.0.0.1:80/xrj-iot-front//developer/active?id=";
	private final static String postName="星榕物联";
	private final static String postEmail="java@xrjiot.cn";
	public static MailSender getInstance(){
		if (instance==null) {
			synchronized (MailSender.class) {
				if (instance==null) {
					instance=new MailSender();
				}
			}
		}
		return instance;
	}
	public CallResult sendActiveUrl(String id,String email){
		String postUrl=url+id;
		String text = MailActiveTemplate.get(postUrl);
		String subject="星榕基信息科技有限公司物联网开发者激活链接";
		return sender(email,subject, text);
	}
	public CallResult sendCode(String code,String email){
		String text = MailCodeTemplate.get(code);
		String subject="你在星榕基信息科技有限公司物联网发送的验证码（系统邮箱，勿回复）";
		return sender(email,subject, text);
	}
	private CallResult sender(String email,String subject,String text){
		CallResult res=new CallResult();
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		try {
			MimeMessageHelper messageHelper=new MimeMessageHelper(mimeMessage, true);
			messageHelper.setFrom(postEmail, postName);
			messageHelper.setTo(email);  
			messageHelper.setSubject(subject);  
			messageHelper.setText(text);  
	    	javaMailSender.send(mimeMessage); 
			res.success();
		} catch (Exception e) {
			res.fail("系统繁忙，发送邮件失败");
			logger.error("邮件发送失败", e);
		}
		return res;
	}
	
}
