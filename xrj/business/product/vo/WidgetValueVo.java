package com.xrj.business.product.vo;

public class WidgetValueVo {
	private Number minValue;
	private Number maxValue;
	private Object value;
	private Double resolution;
	

	public WidgetValueVo(Number minValue,Number maxValue,Object value,Double resolution) {
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.value = value;
		this.resolution = resolution;
	}
	
	
	public WidgetValueVo() {
		// TODO Auto-generated constructor stub
	}


	public Double getResolution() {
		return resolution;
	}
	public void setResolution(Double resolution) {
		this.resolution = resolution;
	}
	public Number getMaxValue() {
		return maxValue;
	}
	public void setMaxValue(Number maxValue) {
		this.maxValue = maxValue;
	}
	public Number getMinValue() {
		return minValue;
	}
	public void setMinValue(Number minValue) {
		this.minValue = minValue;
	}
	public Object getValue() {
		return value;
	}
	public WidgetValueVo setValue(Object value) {
		this.value = value;
		return this;
	}
	
	
}
