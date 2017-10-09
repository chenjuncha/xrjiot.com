package com.xrj.business.product.entity;


import javax.persistence.Entity;
import javax.persistence.Table;


public class FuntionPoint implements java.io.Serializable{

	private String id;
	private String functionName;
	private String functionCode;
	private String readWriteType;
	private String functionType;
	private Integer minValue;
	private Integer maxValue;
	private Integer resolution;
	private String enumeration;
	private Integer submit;
	private String remark;
	
	public FuntionPoint() {}
	
	public FuntionPoint(String id, String functionName, String functionCode,
			String readWriteType, String functionType, Integer minValue,
			Integer maxValue, Integer resolution, String enumeration,
			Integer submit, String remark) {
		this.id = id;
		this.functionName = functionName;
		this.functionCode = functionCode;
		this.readWriteType = readWriteType;
		this.functionType = functionType;
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.resolution = resolution;
		this.enumeration = enumeration;
		this.submit = submit;
		
		this.remark = remark;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFunctionName() {
		return functionName;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	public String getFunctionCode() {
		return functionCode;
	}

	public void setFunctionCode(String functionCode) {
		this.functionCode = functionCode;
	}

	public String getReadWriteType() {
		return readWriteType;
	}

	public void setReadWriteType(String readWriteType) {
		this.readWriteType = readWriteType;
	}

	public String getFunctionType() {
		return functionType;
	}

	public void setFunctionType(String functionType) {
		this.functionType = functionType;
	}

	public Integer getMinValue() {
		return minValue;
	}

	public void setMinValue(Integer minValue) {
		this.minValue = minValue;
	}

	public Integer getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(Integer maxValue) {
		this.maxValue = maxValue;
	}

	public Integer getResolution() {
		return resolution;
	}

	public void setResolution(Integer resolution) {
		this.resolution = resolution;
	}

	public String getEnumeration() {
		return enumeration;
	}

	public void setEnumeration(String enumeration) {
		this.enumeration = enumeration;
	}

	public Integer getSubmit() {
		return submit;
	}

	public void setLength(Integer submit) {
		this.submit = submit;
	}

	

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
}
