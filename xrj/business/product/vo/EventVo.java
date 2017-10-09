package com.xrj.business.product.vo;

public class EventVo {
	//传递的参数
	private Object[] params;
	private String clickEvent;
	private String jsCommand;
	private String AjaxCommad;
	public EventVo() {
		
	}
	public Object[] getParams() {
		return params;
	}
	public EventVo setParams(Object[] params) {
		this.params = params;
		return this;
	}
	public String getClickEvent() {
		return clickEvent;
	}
	public EventVo setClickEvent(String clickEvent) {
		this.clickEvent = clickEvent;
		return this;
	}
	public String getJsCommand() {
		return jsCommand;
	}
	public EventVo setJsCommand(String jsCommand) {
		this.jsCommand = jsCommand;
		return this;
	}
	public String getAjaxCommad() {
		return AjaxCommad;
	}
	public EventVo setAjaxCommad(String ajaxCommad) {
		AjaxCommad = ajaxCommad;
		return this;
	}
	
	
	//dfish 返回AjaxCommand
	public String getCommandString() {
		StringBuffer sb = new StringBuffer();
		if(params.length==0){
			sb.append("VM(this).cmd('");
			sb.append(getAjaxCommad());
			sb.append("');");
			return sb.toString();
		}
		sb.append("VM(this).cmd('");
		sb.append(getAjaxCommad());
		sb.append("'");
		for(Object object:getParams()){
		sb.append(",'");
		sb.append(object);
		sb.append("'");
		}
		sb.append(")");
		return sb.toString();
	}
	
}
