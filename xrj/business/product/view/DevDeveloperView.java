package com.xrj.business.product.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rongji.dfish.base.Utils;
import com.rongji.dfish.framework.FrameworkHelper;
import com.rongji.dfish.framework.plugin.file.ui.DefaultUploadFile;
import com.rongji.dfish.framework.plugin.file.ui.DefaultUploadImage;
import com.rongji.dfish.ui.Widget;
import com.rongji.dfish.ui.command.JSCommand;
import com.rongji.dfish.ui.form.AbstractInput;
import com.rongji.dfish.ui.form.DatePicker;
import com.rongji.dfish.ui.form.Password;
import com.rongji.dfish.ui.form.Radio;
import com.rongji.dfish.ui.form.Select;
import com.rongji.dfish.ui.form.Text;
import com.rongji.dfish.ui.form.Validate;
import com.rongji.dfish.ui.helper.FormPanel;
import com.rongji.dfish.ui.helper.GridPanel;
import com.rongji.dfish.ui.helper.HorizontalGroup;
import com.rongji.dfish.ui.helper.Label;
import com.rongji.dfish.ui.layout.ButtonBar;
import com.rongji.dfish.ui.layout.FrameLayout;
import com.rongji.dfish.ui.layout.GridLayout;
import com.rongji.dfish.ui.layout.HorizontalLayout;
import com.rongji.dfish.ui.layout.VerticalLayout;
import com.rongji.dfish.ui.layout.View;
import com.rongji.dfish.ui.layout.grid.Tr;
import com.rongji.dfish.ui.widget.Button;
import com.rongji.dfish.ui.widget.Html;
import com.rongji.dfish.ui.widget.SubmitButton;
import com.rongji.dfish.ui.widget.TreePanel;
import com.rongji.dfish.ui.widget.Img;
import com.rongji.dfish.ui.widget.Leaf;
import com.rongji.dfish.ui.widget.Split;
import com.xrj.api.dto.DevDeveloper;
import com.xrj.api.dto.SysDataDict;
import com.xrj.api.dto.SysResource;
import com.xrj.api.em.AuditStatusEnum;
import com.xrj.api.em.DataDictEnum;
import com.xrj.business.core.base.PubService;
import com.xrj.business.core.view.PubView;
import com.xrj.business.product.service.DeveloperBusiness;
import com.xrj.framework.util.Assert;

/**
 * @Date 2017-08-29
 * @author chenjunchao
 *
 */
public class DevDeveloperView {
	
	private static AbstractInput<?,?> getTextSet(AbstractInput<?,?> input){		
		input.setPlaceholder("请输入"+input.getLabel()).setRequired(true);
		input.setOn(Text.EVENT_INPUT, "this.fixhdr()").setWidth(260);
		return input;
	}
	
	/**
	 * @注册对话框
	 * @param inputHeight
	 * @return
	 */
	public static View buildToRegView(int inputHeight) {
		View view =buildDialogView(inputHeight);
		view.addCommand("changeToLogin", new JSCommand("pub.dialogClose(this)"));
		view.addCommand("doReg", PubView.getSubmitCommand("developer/doReg"));
		view.addCommand("showAgree", PubView.getAjaxCommand("developer/showAgree"));
		
		FormPanel form = (FormPanel) view.findNodeById("form");
		form.addHidden("pwd", null);
		form.add(getTextSet(new Text("userEmail", "邮箱", null).setId("userEmail")).setRequired(true)
				.addValidate(new Validate().setPattern("/^[a-zA-Z0-9_-]+@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}/")));
		form.add(getTextSet(new Password("", "密码", null).setId("password").setOn(Text.EVENT_BLUR, "VM(this).f('pwd').val(md5(VM(this).find('password').val()));").setAutocomplete(true)));
		form.add(getTextSet(new Password("", "确认密码", null).setId("cfmpassword").setAutocomplete(true)
				.setOn(Password.EVENT_BLUR, "login.checkPwd(this);")));

		form.add(new Html("点击「注册」按钮，即代表您同意<a onclick=\"dfish.vm(this).cmd('showAgree');\" >《星榕物联网使用协议》</a>").setHeight(20).setStyle("color: #999;text-align:center"));
		form.add(new ButtonBar("btnLogin").add(new Button("注册").setCls("log-btn").setOn(Button.EVENT_CLICK, "login.checkPwd(this,'1')")).setAlign(ButtonBar.ALIGN_CENTER).setHeight(55));
		form.add(new Html("<a onclick=\"dfish.vm(this).cmd('changeToLogin');\" >已有账号？登录</a>").setHeight(20).setCls("log-link-right"));
		return  view;
	}
	
	public static View buildDialogView(int inputHeight){
		View view = new View();
		
		VerticalLayout root = new VerticalLayout("root");
		view.add(root);
		view.setNode(root);
		/*view.setOn(View.EVENT_LOAD, "var $=dfish;if($.br.scroll)return;" +
				"var m=this,o=m.f('loginName'),t=!o.x.value&&setInterval(function(){" +
				"if(o.val()){$.classAdd(o.$('ph'),'f-none');" +
				"$.classAdd(m.f('password').$('ph'),'f-none');clearInterval(t)}},50);" +
				"$.query(document).on('click',function(){clearInterval(t)})");*/
		HorizontalLayout top = new HorizontalLayout("top").setStyle("background-color:#2498ff;");
		root.add(top, "130");
		
		VerticalLayout topShell = new VerticalLayout("topShell").setStyle("background:url('m/index/img/log.png') no-repeat center center;vertical-align:middle");
		top.add(new Html("").setWidth(85));
		top.add(topShell, "*");
		
		VerticalLayout main = new VerticalLayout("main").setAlign(VerticalLayout.ALIGN_CENTER).setValign(VerticalLayout.VALIGN_MIDDLE);
		root.add(main, "*");
		
		FormPanel form = new FormPanel("form");
//		VerticalLayout form = new VerticalLayout("form");
		main.add(form.setHeight(inputHeight));
		form.setScroll(true);
		form.setStyle("padding:35px 0;").setHmin(70).setWidth(450).setHeight(600);
		
		return view;
	}
	
	/**
	 * @param name 
	 * @开发者类型对话框
	 * @return
	 */
	public static View buildTypeView() {
		View view = PubView.buildDialogView(false);
		FormPanel form = (FormPanel) view.findNodeById(PubView.DIALOG_MAIN);
		HorizontalLayout hors = new HorizontalLayout("hors");
		VerticalLayout personal = new VerticalLayout("personal").setOn(VerticalLayout.EVENT_CLICK, "VM(this).find('personType').check(true)").setAlign(VerticalLayout.ALIGN_CENTER);
		VerticalLayout enterprise = new VerticalLayout("enterprise").setOn(VerticalLayout.EVENT_CLICK, "VM(this).find('enterType').check(true)").setAlign(VerticalLayout.ALIGN_CENTER);
		
		personal.add(new Img("m/index/img/personal.png"));
		personal.add(new Radio("devtype", null, true, "1", "个人开发者").setId("personType"));
		enterprise.add(new Img("m/index/img/enterprise.png"));
		enterprise.add(new Radio("devtype", null, false, "2", "企业开发者").setId("enterType"));
		
		hors.add(personal);
		hors.add(enterprise);
		form.add(hors);		
		form.add(new ButtonBar("btnNext").setAlign(ButtonBar.ALIGN_CENTER).add(new Button("下一步").setWidth(100).setCls("x-btn x-btn-main").setOn(Button.EVENT_CLICK, "VM(this).cmd('toCertification')")));
		view.addCommand("toCertification", PubView.getSubmitCommand("developer/toCertification"));	
		return view;
	}
	
	/**
	 * @param devDeveloper 
	 * @个人开发者信息界面
	 * @return
	 */
	public static View bulidPersonInfo(DevDeveloper devDeveloper) {
		View view = new View();
		FormPanel form = new FormPanel("form");
		GridLayout grid=new GridLayout("grid");
		grid.setHeight("76%").setWidth("100%");
		grid.setWmin(20).setCls("x-grid-odd").setFace(GridPanel.FACE_LINE)
			   .setScroll(true).setNobr(false).setFocusable(true);
		grid.getThead().setCls("x-grid-head");
		grid.setPub(new Tr().setHeight("40").setCls("w-td-line w-th"));		
		grid.add(0, 0, new Html("账号:"));  
		grid.add(0, 2, new Text("account","",devDeveloper.getLoginName()).setReadonly(true));
		grid.add(1, 0, new Html("<span style='color:red;'>*</span>联系人名称:"));  
		grid.add(1, 2, new Text("linkman","",devDeveloper.getContactName()).setRequired(true));
		grid.add(1, 3, new Html(""));
		grid.add(2, 0, new Html("<span style='color:red;'>*</span>用户名(昵称):"));  
		grid.add(2, 2, new Text("accountName","",devDeveloper.getUserName()).setRequired(true));
		grid.add(3, 0, new Html("QQ:") );  
		grid.add(3, 2, new Text("QQ","",devDeveloper.getQq()).addValidate(Validate.pattern("/^\\d+$/").setPatterntext("QQ必须是数字")));
		grid.add(4, 0, new Html("<span style='color:red;'>*</span>手机:"));  
		grid.add(4, 2, new Text("tel","",devDeveloper.getPhone()).setRequired(true).addValidate(Validate.pattern("/^1(3|4|5|7|8)\\d{9}$/").setPatterntext("手机号码有误")));
		grid.add(5, 0, new Html("<span style='color:red;'>*</span>联系人邮箱:"));  
		grid.add(5, 2, new Text("linkmanEmail","",devDeveloper.getContactEmail()).setRequired(true).addValidate(new Validate().setPattern("/^[a-zA-Z0-9_-]+@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}/")));
		grid.add(6, 0, new Html("身份证:") );  
		grid.add(6, 2, new Text("IdCard","",devDeveloper.getIdcard()).setReadonly(true));
		HorizontalGroup area = new HorizontalGroup("area");				
		Map<String, List<SysDataDict>> mapClass = DeveloperBusiness.AreaDate(devDeveloper.getAddr()) ;
		String[] StrArray = {null,"110100","110101",""} ;	
		if(!Assert.isEmpty(devDeveloper.getAddr())) {
		    StrArray = devDeveloper.getAddr().split("-");	
		}
		area.add(new Select("provinc", null, StrArray[0], Arrays.asList(AddNewProductView.classList(mapClass.get("list1")))).setOn(Select.EVENT_CHANGE, "VM(this).cmd('changeCity',this.val())").setId("provinc"));
		area.add(new Select("City", null, StrArray[1], Arrays.asList(AddNewProductView.classList(mapClass.get("list2")))).setOn(Select.EVENT_CHANGE, "VM(this).cmd('changeArea',this.val())").setId("City"));
		area.add(new Select("Area", null, StrArray[2], Arrays.asList(AddNewProductView.classList(mapClass.get("list3")))).setId("Area"));	
		grid.add(7, 0, new Html("<span style='color:red;'>*</span>所在地区:"));
		grid.add(7, 2,7,3, area);
		grid.add(8, 0, new Html("地址:") );  
		grid.add(8, 2,8,3, new Text("address","",StrArray[3]));
		HorizontalLayout btm = new HorizontalLayout("btm").setWidth(200).setStyle("display:table;margin:0 auto");
		btm.add(new Button("修改").setCls("x-btn x-btn-main").setWidth(80).setOn(SubmitButton.EVENT_CLICK,"VM(this).cmd('personInfo')"));
		btm.add(new Html("").setWidth(20));
		btm.add(new Button("取消").setCls("x-btn").setWidth(80).setOn(Button.EVENT_CLICK, "VM(this).reload('developer/personInfo')"));
		grid.add(9,1,9,2, btm);	
		form.add(grid);
		view.add(form);
		view.addCommand("personInfo", PubView.getSubmitCommand("developer/personUpdate"));
		view.addCommand("changeCity", PubView.getAjaxCommand("developer/changeCity?code=$0"));
		view.addCommand("changeArea", PubView.getAjaxCommand("developer/changeArea?code=$0"));
		return view;
	}
	
	/**
	 * @param devDeveloper 
	 * @个人开发者认证界面
	 * @return
	 */
	public static View bulidPersonCert(DevDeveloper devDeveloper) {
		View view = new View();
		view.addCommand("Cert", PubView.getSubmitCommand("developer/personCertInfo"));
		FormPanel form = new FormPanel("form");
		GridLayout grid=new GridLayout("grid");
		grid.setHeight("76%").setWidth("100%");
		grid.setWmin(20).setCls("x-grid-odd").setFace(GridPanel.FACE_LINE)
			   .setScroll(true).setNobr(false).setFocusable(true);
		grid.getThead().setCls("x-grid-head");
		grid.setPub(new Tr().setHeight("40").setCls("w-td-line w-th"));
		
		grid.add(0, 0, new Html("<span style='color:red;'>*</span>手机:"));  		
		grid.add(0, 4, new Html(""));
		grid.add(1, 0, new Html("<span style='color:red;'>*</span>身份证:"));  
		
		if(Assert.isEmpty(devDeveloper.getIdcard()) || devDeveloper.getAuditStatus().equals(AuditStatusEnum.NOPASS.getIndex())) {
		    grid.add(0, 3, new Text("tel","",devDeveloper.getPhone()).setRequired(true).addValidate(Validate.pattern("/^1(3|4|5|7|8)\\d{9}$/").setPatterntext("手机号码有误")));
		    grid.add(1, 3, new Text("IdCard","",devDeveloper.getIdcard()).setRequired(true).addValidate(Validate.pattern("/(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)/").setPatterntext("身份证长度为15或18位")));
		    grid.add(2,2,new Button("提交审核").setStyle("display:table;margin:0 auto").setCls("x-btn x-btn-main").setOn(Button.EVENT_CLICK, "VM(this).cmd('Cert')").setWidth(100));
		} 
		if(devDeveloper.getAuditStatus().equals(AuditStatusEnum.HANDLING.getIndex())){
			grid.add(0, 3, new Html(devDeveloper.getPhone()));
			grid.add(1, 3,1,4, new Html(devDeveloper.getIdcard()));
			grid.add(2, 2, new Html("<span style='color:red;display:table;margin:0 auto'>待审核</span>").setWidth(150));
		}
		if(devDeveloper.getAuditStatus().equals(AuditStatusEnum.PASS.getIndex())){
			grid.add(0, 3, new Html(devDeveloper.getPhone()));
			grid.add(1, 3,1,4, new Html(devDeveloper.getIdcard()));
			grid.add(2, 2, new Html("<span style='color:red;display:table;margin:0 auto'>审核通过</span>").setWidth(150));
		}
		form.add(grid);
		view.add(form);
		return view;
	}
	
	/**
	 * @param devDeveloper 
	 * @企业认证界面
	 * @return
	 */
	public static View bulidEnterCert(DevDeveloper devDeveloper) {
		View view = new View();
		FormPanel form = new FormPanel("form");
		GridLayout grid=new GridLayout("grid");
		grid.setHeight("76%").setWidth("100%");
		grid.setWmin(20).setCls("x-grid-odd").setFace(GridPanel.FACE_LINE)
			   .setScroll(true).setNobr(false).setFocusable(true);
		grid.getThead().setCls("x-grid-head");
		grid.setPub(new Tr().setHeight("40").setCls("w-td-line w-th"));		
		grid.add(0, 0, new Html("<span style='color:red;'>*</span>账号:").setAlign(Html.ALIGN_CENTER));  		
		grid.add(0, 2, new Html("<span style='color:red;'>*</span>企业名称:").setAlign(Html.ALIGN_CENTER));  		
		grid.add(1, 0, new Html("<span style='color:red;'>*</span>企业性质:").setAlign(Html.ALIGN_CENTER));  		
		grid.add(1, 2, new Html("<span style='color:red;'>*</span>法人姓名:").setAlign(Html.ALIGN_CENTER));  		
		grid.add(2, 0, new Html("<span style='color:red;'>*</span>成立日期:").setAlign(Html.ALIGN_CENTER));  		
		grid.add(2, 2, new Html("<span style='color:red;'>*</span>营业执照注册号:").setAlign(Html.ALIGN_CENTER));  
		grid.add(3, 0, new Html("<span style='color:red;'>*</span>执照类型:").setAlign(Html.ALIGN_CENTER));  
		grid.add(4, 0, new Html("<span style='color:red;'>*</span>行业:").setAlign(Html.ALIGN_CENTER));
		grid.add(5, 0, new Html("<span style='color:red;'>*</span>企业注册地区:").setAlign(Html.ALIGN_CENTER));
		grid.add(6, 0, new Html("<span style='color:red;'>*</span>执照:").setAlign(Html.ALIGN_CENTER));  
	    //第一次审核或审核失败
		if(Assert.isEmpty(devDeveloper.getLegalPersonName()) || devDeveloper.getAuditStatus().equals(AuditStatusEnum.NOPASS.getIndex())){
			grid.add(0, 1, new Text("account","",devDeveloper.getLoginName()).setReadonly(true));
			grid.add(0, 3, new Text("enterpriseName","",devDeveloper.getUserName()).setRequired(true));
			grid.add(1, 1, new Text("enterpriseType","",devDeveloper.getCompanyNature()).setRequired(true));
			grid.add(1, 3, new Text("legalPersonName","",devDeveloper.getLegalPersonName()).setRequired(true));
			grid.add(2, 1, new DatePicker("yyyymmdd", "", devDeveloper.getCreateDate(), DatePicker.DATE).setRequired(true));  
			grid.add(2, 3, new Text("businessLicence","",devDeveloper.getLicenseNo()).setRequired(true));
			grid.add(3, 1, new Radio("licenceType", null, devDeveloper.getLicenseType().equals("1")?true:false, "1", "三证合一"));
			grid.add(3, 2, new Radio("licenceType", null, devDeveloper.getLicenseType().equals("2")?true:false, "2", "三证"));
			HorizontalGroup trade = new HorizontalGroup("trade");
			Map<String, List<SysDataDict>> mapCla = DeveloperBusiness.IndustryDate(devDeveloper.getIndustry());
			String[] StrArrays = {null,null,null} ;	
			if(!Assert.isEmpty(devDeveloper.getIndustry())) {
			    StrArrays = devDeveloper.getIndustry().split("-");	
			}
			trade.add(new Select("bigClass", null, StrArrays[0], Arrays.asList(AddNewProductView.classList(mapCla.get("list1")))).setOn(Select.EVENT_CHANGE, "VM(this).cmd('changeSmallClass',this.val())").setId("bigClass"));
			trade.add(new Select("smallClass", null, StrArrays[1], Arrays.asList(AddNewProductView.classList(mapCla.get("list2")))).setOn(Select.EVENT_CHANGE, "VM(this).cmd('changeClassType',this.val())").setId("smallClass"));
			trade.add(new Select("classType", null, StrArrays[2], Arrays.asList(AddNewProductView.classList(mapCla.get("list3")))).setId("classType"));	
			grid.add(4, 1,4,3, trade);
			HorizontalGroup regesAddress = new HorizontalGroup("regesAddress");
			Map<String, List<SysDataDict>> mapClass = DeveloperBusiness.AreaDate(devDeveloper.getAddr()) ;
			String[] StrArray = {null,"110100","110101",""} ;	
			if(!Assert.isEmpty(devDeveloper.getRegisterAddr())) {
			    StrArray = devDeveloper.getRegisterAddr().split("-");	
			}
			regesAddress.add(new Select("provinc", null, StrArray[0], Arrays.asList(AddNewProductView.classList(mapClass.get("list1")))).setOn(Select.EVENT_CHANGE, "VM(this).cmd('changeCity',this.val())").setId("provinc"));
			regesAddress.add(new Select("City", null, StrArray[1], Arrays.asList(AddNewProductView.classList(mapClass.get("list2")))).setOn(Select.EVENT_CHANGE, "VM(this).cmd('changeArea',this.val())").setId("City"));
			regesAddress.add(new Select("Area", null, StrArray[2], Arrays.asList(AddNewProductView.classList(mapClass.get("list3")))).setId("Area"));	
			grid.add(5, 1, 5,3,regesAddress); 		
			grid.add(6, 1,6,2, new DefaultUploadFile("uploadFile", "附件").setFile_types("*.jpg,*.png,*.jpeg").setId("logo").setUpload_url("fileService/uploadFile").setRequired(true).setFile_size_limit("20M").setFile_upload_limit(1));
			grid.add(7,0,7,3,new Button("提交审核").setOn(Button.EVENT_CLICK, "VM(this).cmd('enterPriseCert')").setCls("x-btn x-btn-main").setWidth(80).setStyle("display:table;margin:0 auto"));
		} else {
			grid.add(0, 1, new Html(devDeveloper.getLoginName()));
			grid.add(0, 3, new Html(devDeveloper.getUserName()));
			grid.add(1, 1, new Html(devDeveloper.getCompanyNature()));
			grid.add(1, 3, new Html(devDeveloper.getLegalPersonName()));
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");   
			String str2=sdf.format(devDeveloper.getCreateDate());  
			grid.add(2, 1, new Html(str2));  
			grid.add(2, 3, new Html(devDeveloper.getLicenseNo()));
			grid.add(3, 1,new Html(devDeveloper.getLicenseType().equals("1")?"三证合一":"三证"));
			grid.add(4, 1,4,3,new Html(DeveloperBusiness.baseCodefindName(devDeveloper.getIndustry(),"")));
			grid.add(5, 1,5,3,new Html(DeveloperBusiness.baseCodefindName(devDeveloper.getRegisterAddr(),"area")));
		    grid.add(6,1,new Html("<img src='fileService/downloadFile?fileId="+devDeveloper.getLicenseUpload()+"'  width='100' height='100'/>"));
			grid.add(7,1,7,2,new Html(devDeveloper.getAuditStatus().equals(AuditStatusEnum.HANDLING.getIndex())?"<span style='color:red;'>待审核<span>":"<span style='color:red;'>审核通过</span>").setAlign(Html.ALIGN_CENTER));
		}
		view.addCommand("enterPriseCert", PubView.getSubmitCommand("developer/enterPriseCertInfo"));
		form.add(grid);
		view.add(form);
		view.addCommand("changeCity", PubView.getAjaxCommand("developer/changeCity?code=$0"));
		view.addCommand("changeArea", PubView.getAjaxCommand("developer/changeArea?code=$0"));
		view.addCommand("changeSmallClass", PubView.getAjaxCommand("developer/changeSmallClass?code=$0"));
		view.addCommand("changeClassType", PubView.getAjaxCommand("developer/changeClassType?code=$0"));
		return view;
	}
	
	/**
	 * @param devDeveloper 
	 * @企业信息界面
	 * @return
	 */
	public static View bulidEnterInfo(DevDeveloper devDeveloper) {
		View view = new View();
		FormPanel form = new FormPanel("form");
		GridLayout grid=new GridLayout("grid");
		grid.setHeight("76%").setWidth("100%");
		grid.setWmin(20).setCls("x-grid-odd").setFace(GridPanel.FACE_LINE)
			   .setScroll(true).setNobr(false).setFocusable(true);
		grid.getThead().setCls("x-grid-head");
		grid.setPub(new Tr().setHeight("40").setCls("w-td-line w-th"));		
		grid.add(0, 0, new Html("<span style='color:red;'>*</span>账号:").setAlign(Html.ALIGN_CENTER));  
		grid.add(0, 1, new Text("","",devDeveloper.getLoginName()).setReadonly(true));
		grid.add(0, 2, new Html("<span style='color:red;'>*</span>企业名称:").setAlign(Html.ALIGN_CENTER)); 
		grid.add(1, 0, new Html("<span style='color:red;'>*</span>登录名:").setAlign(Html.ALIGN_CENTER));  
		grid.add(1, 1, new Text("","",devDeveloper.getLoginName()).setReadonly(true));
		grid.add(1, 2, new Html("域名:").setAlign(Html.ALIGN_CENTER));  
		grid.add(1, 3, new Text("domainName","",devDeveloper.getDomainName()));
		grid.add(2, 0, new Html("<span style='color:red;'>*</span>联系人名称:").setAlign(Html.ALIGN_CENTER));  
		grid.add(2, 1, new Text("linkman","",devDeveloper.getContactName()).setRequired(true));
		grid.add(2, 2, new Html("QQ:").setAlign(Html.ALIGN_CENTER));  
		grid.add(2, 3, new Text("QQ","",devDeveloper.getQq()).addValidate(Validate.pattern("/^\\d+$/").setPatterntext("QQ必须是数字")));
		grid.add(3, 0, new Html("<span style='color:red;'>*</span>手机:").setAlign(Html.ALIGN_CENTER));  
		grid.add(3, 1, new Text("tel","",devDeveloper.getPhone()).setRequired(true).addValidate(Validate.pattern("/^1(3|4|5|7|8)\\d{9}$/").setPatterntext("手机号码有误")));
		grid.add(3, 2, new Html("联系人邮箱:").setAlign(Html.ALIGN_CENTER));  
		grid.add(3, 3, new Text("linkmanEmail","",devDeveloper.getContactEmail()).addValidate(new Validate().setPattern("/^[a-zA-Z0-9_-]+@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}/")));
		grid.add(4, 0, new Html("所在部门:").setAlign(Html.ALIGN_CENTER));  
		grid.add(4, 1, new Text("dept","",devDeveloper.getDept()));
		grid.add(5, 0, new Html("LOGO:").setAlign(Html.ALIGN_CENTER));  
		if(Assert.isEmpty(devDeveloper.getLogo())){
			grid.add(5, 1,5,2, new DefaultUploadFile("uploadFile", "附件").setFile_types("*.jpg,*.png,*.jpeg").setId("logo").setUpload_url("fileService/uploadFile").setFile_size_limit("20M").setFile_upload_limit(1));
			//grid.add(5, 1,5,2,new DefaultUploadImage("uploadFile", "附件").setThumbnail_url("fileService/downloadFile?fileId=$id").setUpload_url("fileService/uploadImage").setFile_size_limit("50M"));
		} else {
			grid.add(5,1,new Html("<img src='fileService/downloadFile?fileId="+devDeveloper.getLogo()+"'  width='100' height='100'/>"));
			grid.add(5, 2,5,3, new DefaultUploadFile("uploadFile", "附件").setFile_types("*.jpg,*.png,*.jpeg").setId("logo").setUpload_url("fileService/uploadFile").setFile_size_limit("20M").setFile_upload_limit(1));
		}
		HorizontalGroup trade = new HorizontalGroup("trade");
		Map<String, List<SysDataDict>> mapCla = DeveloperBusiness.IndustryDate(devDeveloper.getIndustry());
		String[] StrArrays = {null,null,null} ;	
		if(!Assert.isEmpty(devDeveloper.getIndustry())) {
		    StrArrays = devDeveloper.getIndustry().split("-");	
		}
		HorizontalGroup regesAddress = new HorizontalGroup("regesAddress");
		String[] StrArray = {null,"110100","110101"} ;	
		Map<String, List<SysDataDict>> mapClass = DeveloperBusiness.AreaDate(devDeveloper.getAddr()) ;
		if(!Assert.isEmpty(devDeveloper.getRegisterAddr())) {
		    StrArray = devDeveloper.getRegisterAddr().split("-");	
		}
		if(devDeveloper.getAuditStatus().equals(AuditStatusEnum.PASS.getIndex()) || devDeveloper.getAuditStatus().equals(AuditStatusEnum.HANDLING.getIndex())) 
		{
			grid.add(0, 3, new Text("enterpriseName","",devDeveloper.getUserName()).setReadonly(true).setRequired(true));
			trade.add(new Select("", null, StrArrays[0], Arrays.asList(AddNewProductView.classList(mapCla.get("list1")))).setReadonly(true));
			trade.add(new Select("", null, StrArrays[1], Arrays.asList(AddNewProductView.classList(mapCla.get("list2")))).setReadonly(true));
			trade.add(new Select("", null, StrArrays[2], Arrays.asList(AddNewProductView.classList(mapCla.get("list3")))).setReadonly(true));	
			regesAddress.add(new Select("", null, StrArray[0], Arrays.asList(AddNewProductView.classList(mapClass.get("list1")))).setReadonly(true));
			regesAddress.add(new Select("", null, StrArray[1], Arrays.asList(AddNewProductView.classList(mapClass.get("list2")))).setReadonly(true));
			regesAddress.add(new Select("", null, StrArray[2], Arrays.asList(AddNewProductView.classList(mapClass.get("list3")))).setReadonly(true));	
		} else {
			grid.add(0, 3, new Text("enterpriseName","",devDeveloper.getUserName()));
			trade.add(new Select("bigClass", null, StrArrays[0], Arrays.asList(AddNewProductView.classList(mapCla.get("list1")))).setOn(Select.EVENT_CHANGE, "VM(this).cmd('changeSmallClass',this.val())").setId("bigClass"));
			trade.add(new Select("smallClass", null, StrArrays[1], Arrays.asList(AddNewProductView.classList(mapCla.get("list2")))).setOn(Select.EVENT_CHANGE, "VM(this).cmd('changeClassType',this.val())").setId("smallClass"));
			trade.add(new Select("classType", null, StrArrays[2], Arrays.asList(AddNewProductView.classList(mapCla.get("list3")))).setId("classType"));	
			regesAddress.add(new Select("provinc", null, StrArray[0], Arrays.asList(AddNewProductView.classList(mapClass.get("list1")))).setOn(Select.EVENT_CHANGE, "VM(this).cmd('changeCity',this.val())").setId("provinc"));
			regesAddress.add(new Select("City", null, StrArray[1], Arrays.asList(AddNewProductView.classList(mapClass.get("list2")))).setOn(Select.EVENT_CHANGE, "VM(this).cmd('changeArea',this.val())").setId("City"));
			regesAddress.add(new Select("Area", null, StrArray[2], Arrays.asList(AddNewProductView.classList(mapClass.get("list3")))).setId("Area"));	
		}
		grid.add(6, 0, new Html("<span style='color:red;'>*</span>行业:").setAlign(Html.ALIGN_CENTER));
		grid.add(6, 1,6,3, trade);	
		grid.add(7, 0, new Html("<span style='color:red;'>*</span>企业注册地区:").setAlign(Html.ALIGN_CENTER));
		grid.add(7, 1, 7,3,regesAddress); 	
		grid.add(8, 0, new Html("地址:").setAlign(Html.ALIGN_CENTER)); 
		grid.add(8, 1,8,2, new Text("address","",devDeveloper.getAddr()));
		HorizontalLayout btm = new HorizontalLayout("btm").setWidth(200).setStyle("display:table;margin:0 auto");
		btm.add(new Button("保存").setCls("x-btn x-btn-main").setWidth(80).setOn(SubmitButton.EVENT_CLICK,"VM(this).cmd('enterPriseInfo')"));
		btm.add(new Html("").setWidth(20));
		btm.add(new Button("取消").setCls("x-btn").setWidth(80).setOn(Button.EVENT_CLICK, "VM(this).reload('developer/enterpriseInfo')"));
		grid.add(9,0,9,3, btm);	
		
		form.add(grid);
		view.add(form);
		view.addCommand("enterPriseInfo", PubView.getSubmitCommand("developer/enterPriseUpdate"));
		view.addCommand("changeCity", PubView.getAjaxCommand("developer/changeCity?code=$0"));
		view.addCommand("changeArea", PubView.getAjaxCommand("developer/changeArea?code=$0"));
		view.addCommand("changeSmallClass", PubView.getAjaxCommand("developer/changeSmallClass?code=$0"));
		view.addCommand("changeClassType", PubView.getAjaxCommand("developer/changeClassType?code=$0"));
		return view;
	}
	
	/**
	 * @主布局
	 * @param listMenu
	 * @param anchor
	 * @return
	 */
	public static View buildPersonView(List<SysResource> listMenu, String anchor) {
		View view = new View();
		VerticalLayout verRoot = new VerticalLayout("root");
		HorizontalLayout root = new HorizontalLayout("bd_root");
		view.add(verRoot);

		verRoot.add(new Html(
				"<hr style='filter:progid:DXImageTransform.Microsoft.Shadow(color:#CCC,direction:145,strength:15)' color='#CCC' width='100%' size='1' />")
						.setHeight(1),
				"-1");
		verRoot.add(root);
		root.setCls("bg-body");
		VerticalLayout ver = new VerticalLayout("bd_menu_ver");
		TreePanel menus = new TreePanel("bd_menu").setScroll(true).setCls("bg-menu bd-menu bd-onlyright").setWmin(1);
		ver.add(menus);
		root.add(ver, "200");
		Split split = new Split().setId("bd_split");
		root.add(split, "10");

		Widget<?> main = DevMenuView.buildMainLayout();
		root.add(main, "*");
		
		TreePanel menuTree = (TreePanel) view.findNodeById("bd_menu");
		FrameLayout mainContent = (FrameLayout) view.findNodeById("mainContent");

		Leaf leaf = null;
		SysResource focusMenu = null;
		for (SysResource menu : listMenu) {
			if (Utils.notEmpty(menu.getParentId())) {
				if (menu.getParentId().equals("0100")) {
					leaf = new Leaf(menu.getId(), menu.getResName()).setFocus(false).setOpen(true).setIcon(".x-folder");
					menuTree.add(leaf);
				} else if (menu.getId().substring(0, 2).equals("01")) {
					Leaf childLeaf = new Leaf(menu.getId(), menu.getResName());
					leaf.add(childLeaf);
					View viewContent = new View("v_" + menu.getId());
					viewContent.setSrc(menu.getUri());
					mainContent.add(viewContent);
					// mainContent.setDft("v_"+focusMenu.getMenuId());
					childLeaf.setOn(Leaf.EVENT_CLICK, "this.cmd('clickMenu','" + menu.getId() + "');");
				}
				if (anchor != null && menu.getId().equals(anchor)) {
					focusMenu = menu;
					leaf.setFocus(true);
				}
			}
		}
		mainContent.setDft("v_" + anchor);
		Leaf focusleaf = (Leaf) menuTree.findNodeById(anchor);
		focusleaf.setFocus(true);
		view.addCommand("clickMenu", PubView.getAjaxCommand("developer/clickMenu?menuId=$0"));
		return view;
	}
	
	/**
	 * @激活对话框
	 * @param auth
	 * @return
	 */
	public static View buildActiveView(String auth) {
		View view = PubView.buildDialogView(false);
		FormPanel form = (FormPanel) view.findNodeById(PubView.DIALOG_MAIN);
		form.add(new Html("点击后面的链接即可激活您的账号<a onclick=\"dfish.vm(this).cmd('active','"+auth+"');\" >星榕物联网激活链接</a>").setHeight(20).setStyle("color: #999;text-align:center"));
		view.addCommand("active", PubView.getAjaxCommand("developer/active?userEmail=$0"));
		return view;
	}
	
	/**
	 * @找回对话框
	 * @return
	 */
	public static View buildFindPwdView() {
        View view = PubView.buildDialogView(false);
		
		FormPanel form = (FormPanel) view.findNodeById(PubView.DIALOG_MAIN); 
		form.addHidden("pwd", null);
		HorizontalGroup hors = new HorizontalGroup("hors");
		hors.setLabel("<span style='color:red'>*</span>邮箱");
		hors.add(new Text("userEmail", "邮箱", null).setWidth(160).setPlaceholder("请输入要找回的账号邮箱").setId("userEmail").setRequired(true)
				.addValidate(new Validate().setPattern("/^[a-zA-Z0-9_-]+@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}/")));
		
		hors.add(new ButtonBar("btnCode").add(new Button("发送验证码").setStyle("margin-top:2px;margin-left:2px").setWidth(98).setCls("x-btn x-btn-main").setOn(Button.EVENT_CLICK, "VM(this).cmd('sendCode',''+VM(this).f('userEmail').val()+'');")));
		
		form.add(hors);
		form.add(getTextSet(new Password("", "密码", null).setId("password").setOn(Text.EVENT_BLUR, "VM(this).f('pwd').val(md5(VM(this).find('password').val()));").setAutocomplete(true)));
		form.add(getTextSet(new Password("", "确认密码", null).setId("cfmpassword").setAutocomplete(true)
				.setOn(Password.EVENT_BLUR, "login.checkBackPwd(this);")));
		form.add(new Text("code","验证码",null).setWidth(260).setPlaceholder("请输入收到的验证码").setRequired(true));
		
		ButtonBar btnBar = (ButtonBar) view.findNodeById(PubView.DIALOG_BUTTON);
		btnBar.add(new SubmitButton("确认").setCls("x-btn x-btn-main").setOn(Button.EVENT_CLICK, "login.checkBackPwd(this,'1')"));
		btnBar.add(new Button("关闭").setOn(Button.EVENT_CLICK, "$.dialog(this).close();"));
		
		view.addCommand("sendCode", PubView.getAjaxCommand("developer/sendCode?email=$0"));
		view.addCommand("checkCode", PubView.getSubmitCommand("developer/checkCode"));
		
		return view;
	}
	
	/**
	 * @协议对话框
	 * @return
	 */
	public static Widget<?> buildAgreeView() {
		View view = PubView.buildDialogView();
		
		VerticalLayout main = new VerticalLayout(PubView.DIALOG_MAIN).setScroll(true);
		view.replaceNodeById(main);
		Html agreeList=new Html(FrameworkHelper.getFileText("m/login/html/agree.htm")).setScroll(true);
		main.add(agreeList);
		
		ButtonBar btnBar = (ButtonBar) view.findNodeById(PubView.DIALOG_BTN);
		return view;
	}
	
	/**
	 * 地区二级下拉更改
	 * 
	 * @param list
	 * @return
	 */
	public static View buildSelect2(List<SysDataDict> list) {
		View view = new View();
		Select select = new Select("City", null, null, Arrays.asList(AddNewProductView.classList(list))).setOn(Select.EVENT_CHANGE, "VM(this).cmd('changeArea',this.val())").setId("City");
		view.add(select);
		return view;
	}
	/**
	 * 地区三级下拉列表更改
	 * 
	 * @param list
	 * @return
	 */
	public static View buildSelect3(List<SysDataDict> list) {
		View view = new View();
		Select select = new Select("Area", null, null, Arrays.asList(AddNewProductView.classList(list))).setId("Area");
		view.add(select);
		return view;
	}
	
	/**
	 * 行业二级下拉更改
	 * 
	 * @param list
	 * @return
	 */
	public static View buildSele2(List<SysDataDict> list) {
		View view = new View();
		Select select = new Select("smallClass", null, null, Arrays.asList(AddNewProductView.classList(list))).setOn(Select.EVENT_CHANGE, "VM(this).cmd('changeClassType',this.val())").setId("smallClass");
		view.add(select);
		return view;
	}
	/**
	 * 行业三级下拉列表更改
	 * 
	 * @param list
	 * @return
	 */
	public static View buildSele3(List<SysDataDict> list) {
		View view = new View();
		Select select = new Select("classType", null, null, Arrays.asList(AddNewProductView.classList(list))).setId("classType");
		view.add(select);
		return view;
	}
}
