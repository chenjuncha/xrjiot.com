package com.xrj.business.core.base;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Reference;
import com.xrj.api.service.DevProductService;
import com.xrj.api.service.DevSubAccountService;
import com.xrj.api.service.DevAuditService;
import com.xrj.api.service.DevCodeService;
import com.xrj.api.service.DevDeveloperService;
import com.xrj.api.service.DevDeviceLogService;
import com.xrj.api.service.DevDeviceRomService;
import com.xrj.api.service.DevDeviceService;
import com.xrj.api.service.DevHardwareVersionService;
import com.xrj.api.service.DevProductFunctionService;
import com.xrj.api.service.HelloWorldService;
import com.xrj.api.service.MessageIssueService;
import com.xrj.api.service.RemoteFileService;
import com.xrj.api.service.SimnulationDeviceService;
import com.xrj.api.service.SysDataDictService;
import com.xrj.api.service.SysManagerService;
import com.xrj.api.service.SysOperLogService;
import com.xrj.api.service.SysResourceService;
import com.xrj.api.service.SysRoleService;

@Component
public class RemoteService {

	/**
	 * 设备ROM服务
	 */
	@Reference
	private DevDeviceRomService devDeviceRomService;
	
	/**
	 * 产品功能点服务
	 */
	@Reference
	private DevProductFunctionService devProductFunctionService;

	/**
	 * 产品设备服务
	 */
	@Reference
	private DevDeviceService devDeviceService;

	/**
	 * 系统资源服务
	 */
	@Reference
	private SysResourceService sysResourceService;

	/**
	 * HelloWord DEMO
	 */
	@Reference
	private HelloWorldService helloWorldService;

	/**
	 * 字典表服务
	 */
	@Reference
	private SysDataDictService sysDataDictService;

	/**
	 * 产品服务
	 */
	@Reference
	private DevProductService devProductService;

	/**
	 * 硬件版本服务
	 */
	@Reference
	private DevHardwareVersionService devHardwareVersionService;

	/**
	 * 子帐号服务
	 */
	@Reference
	private DevSubAccountService devSubAccountService;

	/**
	 * 开发者服务
	 */
	@Reference
	private DevDeveloperService devDeveloperService;

	/**
	 * 产品域审核服务
	 */
	@Reference
	private DevAuditService devAuditService;

	/**
	 * 设备日志服务
	 */
	@Reference
	private DevDeviceLogService devDeviceLogService;
	
	/**
	 * 管理员服务
	 */
	@Reference
	private SysManagerService sysManagerService;

	/**
	 * 角色服役
	 */
	@Reference
	private SysRoleService sysRoleService;

	/**
	 * 系统日志服务
	 */
	@Reference
	private SysOperLogService sysOperLogService;

	/**
	 * 系统文件服务
	 */
	@Reference
	private RemoteFileService remoteFileService;
	
	@Reference
	private DevCodeService codeService;
	/**
	 * 模拟设备
	 */
	@Reference
	private SimnulationDeviceService simnulationDeviceService;
	
	@Reference
	private MessageIssueService messageIssueService;


	public DevDeviceRomService getDevDeviceRomService() {
		return devDeviceRomService;
	}

	public DevProductFunctionService getDevProductFunctionService() {
		return devProductFunctionService;
	}

	public DevDeviceService getDevDeviceService() {
		return devDeviceService;
	}

	public SysResourceService getSysResourceService() {
		return sysResourceService;
	}

	public HelloWorldService getHelloWorldService() {
		return helloWorldService;
	}

	public SysDataDictService getSysDataDictService() {
		return sysDataDictService;
	}

	public DevProductService getDevProductService() {
		return devProductService;
	}

	public DevHardwareVersionService getDevHardwareVersionService() {
		return devHardwareVersionService;
	}

	public DevSubAccountService getDevSubAccountService() {
		return devSubAccountService;
	}

	public DevDeveloperService getDevDeveloperService() {
		return devDeveloperService;
	}

	public DevAuditService getDevAuditService() {
		return devAuditService;
	}

	public DevDeviceLogService getDevDeviceLogService() {
		return devDeviceLogService;
	}

	public SysManagerService getSysManagerService() {
		return sysManagerService;
	}

	public SysRoleService getSysRoleService() {
		return sysRoleService;
	}

	public SysOperLogService getSysOperLogService() {
		return sysOperLogService;
	}

	public RemoteFileService getRemoteFileService() {
		return remoteFileService;
	}

	public DevCodeService getCodeService() {
		return codeService;
	}

	public SimnulationDeviceService getSimnulationDeviceService() {
		return simnulationDeviceService;
	}

	public MessageIssueService getMessageIssueService() {
		return messageIssueService;
	}
	
	
}
