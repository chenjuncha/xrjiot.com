package com.xrj.business.core.config;

public class AliyunConfig {
	private String romDomain;
	private String romBucketName;
	private String bucketEndpint;
	private String accessKeyId;
	private String accesskeySecret;
	public String getRomDomain() {
		return romDomain;
	}
	public void setRomDomain(String romDomain) {
		this.romDomain = romDomain;
	}
	
	public String getRomBucketName() {
		return romBucketName;
	}
	public void setRomBucketName(String romBucketName) {
		this.romBucketName = romBucketName;
	}
	
	public void setBucketEndpint(String bucketEndpint) {
		this.bucketEndpint = bucketEndpint;
	}
	public String getBucketEndpint() {
		return bucketEndpint;
	}
	public String getAccessKeyId() {
		return accessKeyId;
	}
	public void setAccessKeyId(String accessKeyId) {
		this.accessKeyId = accessKeyId;
	}
	public String getAccesskeySecret() {
		return accesskeySecret;
	}
	public void setAccesskeySecret(String accesskeySecret) {
		this.accesskeySecret = accesskeySecret;
	}
	
	
}
