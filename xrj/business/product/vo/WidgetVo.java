package com.xrj.business.product.vo;

import java.util.ArrayList;
import java.util.List;

import com.rongji.dfish.ui.form.Validate;
import com.rongji.dfish.ui.layout.HorizontalLayout;

public class WidgetVo {
	private String widgetId;
	private String widgetLabel;
	private String widgetType;
	private WidgetValueVo widgetValueVo;
	private List<EventVo> eventVo;
	private Integer weight;
	private String width;
	private String style;
	private HorizontalLayout hor;
	private String cls;
	private Boolean readOnly;
	private Boolean select;
	private Object others;
	private List<Validate> valid;	
	public String getStyle() {
		return style;
	}

	public WidgetVo setStyle(String style) {
		this.style = style;
		return this;
	}

	public Boolean getReadOnly() {
		return readOnly;
	}

	public WidgetVo setReadOnly(Boolean readOnly) {
		this.readOnly = readOnly;
		return this;
	}

	public WidgetVo() {
		// TODO Auto-generated constructor stub
		this.setWeight(-1);//宽度默认自适应
	}
	
	public WidgetVo(String widgetId, String widgetLabel, String widgetType) {
		super();
		this.widgetId = widgetId;
		this.widgetLabel = widgetLabel;
		this.widgetType = widgetType;
		this.setWeight(-1);
		this.widgetValueVo = new WidgetValueVo();
		this.eventVo = new ArrayList<EventVo>();
		this.readOnly = false ;
		this.select = false;
		this.valid =  new ArrayList<Validate>();
	}

	public String getWidgetType() {
		return widgetType;
	}

	public WidgetVo setWidgetType(String widgetType) {
		this.widgetType = widgetType;
		return this;
	}

	public String getWidgetId() {
		return widgetId;
	}
	public WidgetVo setWidgetId(String widgetId) {
		this.widgetId = widgetId;
		return this;
		
	}
//	public WidgetTypeVo getWidgetTypeVo() {
//		return widgetTypeVo;
//	}
//	public void setWidgetTypeVo(WidgetTypeVo widgetTypeVo) {
//		this.widgetTypeVo = widgetTypeVo;
//	}
	public WidgetValueVo getWidgetValueVo() {
		return widgetValueVo;
	}
	public WidgetVo setWidgetValueVo(WidgetValueVo widgetValueVo) {
		this.widgetValueVo = widgetValueVo;
		return this;
	}
	public List<EventVo> getEventVo() {
		return eventVo;
	}
	public WidgetVo setEventVo(List<EventVo> eventVo) {
		this.eventVo = eventVo;
		return this;
	}
	public Integer getWeight() {
		return weight;
	}
	public WidgetVo setWeight(Integer weight) {
		this.weight = weight;
		return this;
	}
	public String getWidgetLabel() {
		return widgetLabel;
	}
	public WidgetVo setWidgetLabel(String widgetLabel) {
		this.widgetLabel = widgetLabel;
		return this;
	}
	public Object getOthers() {
		return others;
	}

	public WidgetVo setOthers(Object others) {
		this.others = others;
		return this;
	}

	public String getWidth() {
		return width;
	}

	public WidgetVo setWidth(String width) {
		this.width = width;
		return this;
	}

	public String getCls() {
		return cls;
	}

	public WidgetVo setCls(String cls) {
		this.cls = cls;
		return this;
	}

	public HorizontalLayout getHor() {
		return hor;
	}

	public WidgetVo setHor(HorizontalLayout hor) {
		this.hor = hor;
		return this;
	}

	public Boolean getSelect() {
		return select;
	}

	public WidgetVo setSelect(Boolean select) {
		this.select = select;
		return this;
	}

	public List<Validate> getValid() {
		return valid;
	}

	public WidgetVo setValid(List<Validate> valid) {
		this.valid = valid;
		return this;
	}
	
	
}
