package com.xrj.business.product.vo;

import java.util.List;

public class WidgetTypeVo {
	private List<String> widgetTypes;
	private Integer widgetNum;
	private String style;
	private Boolean readOnly;
	
	public WidgetTypeVo() {
		// TODO Auto-generated constructor stub
		this.readOnly = false ;
	}
	public WidgetTypeVo(List<String> widgetTypes) {
		super();
		this.widgetTypes = widgetTypes;
		this.widgetNum = widgetTypes.size();
		this.readOnly = false; //默认可写
		
	}
	public List<String> getWidgetTypes() {
		return widgetTypes;
	}
	public void setWidgetTypes(List<String> widgetTypes) {
		this.widgetTypes = widgetTypes;
	}
	public Integer getWidgetNum() {
		return widgetNum;
	}
	
//	public void setWidgetNum(String widgetNum) {
//		this.widgetNum = getWidgetTypes().size();
//	}
//	
	public String getStyle() {
		return style;
	}
	public void setStyle(String style) {
		this.style = style;
	}
	public Boolean getReadOnly() {
		return readOnly;
	}
	public void setReadOnly(Boolean readOnly) {
		this.readOnly = readOnly;
	}
	
}
