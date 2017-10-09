
/**
 * 
 */
package com.xrj.business.core.web;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.rongji.dfish.base.Utils;
import com.rongji.dfish.framework.FrameworkHelper;
import com.rongji.dfish.framework.plugin.code.CheckCodeGenerator;
import com.rongji.dfish.ui.command.CommandGroup;
import com.rongji.dfish.ui.command.DialogCommand;
import com.rongji.dfish.ui.command.JSCommand;
import com.rongji.dfish.ui.command.TipCommand;
import com.rongji.dfish.ui.layout.View;
import com.rongji.dfish.ui.widget.DialogTemplate;
import com.xrj.api.dto.DevDeveloper;
import com.xrj.api.dto.SysManager;
import com.xrj.api.dto.SysResource;
import com.xrj.api.em.StatusEnum;
import com.xrj.api.service.HelloWorldService;
import com.xrj.business.core.base.PubConstants;
import com.xrj.business.core.base.PubService;
import com.xrj.business.core.base.RemoteService;
import com.xrj.business.core.view.IndexView;
import com.xrj.business.core.view.PubView;
import com.xrj.business.product.service.DeveloperBusiness;
import com.xrj.framework.util.HttpUtil;
import com.xrj.framework.util.MD5;

/**
 * 
 * @author chenyun
 *
 *         2017-7-13
 */
@Controller
@RequestMapping("system")
public class LoginController {

	private final static Log log = LogFactory.getLog(LoginController.class);

	@Resource
	private RemoteService remoteService;

	@Reference
	private HelloWorldService helloWorldService;
	
	@Resource
	private DeveloperBusiness developerBusiness;

	private static Map<String, DialogTemplate> dialogTemplates = new HashMap<String, DialogTemplate>();

	public static String DATA_DEVELOPER_ROLE_ID = "1";
	public static String DATA_MANAGER_ROLE_ID = "2";

	/**
	 * 主页界面
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/index")
	@ResponseBody
	public Object index(HttpServletRequest request, HttpServletResponse response) throws Exception {

		View view = new View();
		view = IndexView.buildIndexView(request);

		return view;
	}

	/**
	 * 一级菜单跳转
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/enterModule")
	@ResponseBody
	public View enterModule(HttpServletRequest request) throws Exception {
		String moduleId = request.getParameter("moduleId");
		View mainView = new View();
		mainView.setId(PubView.BODY_MAIN);
		SysResource sysResource = remoteService.getSysResourceService().get(moduleId);
		mainView.setSrc(sysResource.getUri());

		return new View().add(mainView);
	}
	
	
	
	@RequestMapping("/loginIndex")
	@ResponseBody
	public View loginIndex(HttpServletRequest request) throws Exception {
		View view = IndexView.buildLoginView(DATA_DEVELOPER_ROLE_ID);
		return view;
	}

	@RequestMapping("/loginIndex/manager")
	@ResponseBody
	public View loginIndexManager(HttpServletRequest request) throws Exception {
		View view = IndexView.buildLoginView(DATA_MANAGER_ROLE_ID);
		return view;
	}

	@RequestMapping("/login")
	@ResponseBody
	public Object login(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String auth = request.getParameter("auth");
		String pwd = request.getParameter("pwd");
		String role = request.getParameter("role");
		String error = "";

		String checkCode = request.getParameter("checkCode");
		String code = (String) request.getSession().getAttribute(CheckCodeGenerator.KEY_CHECKCODE);
		String snap = null;
		boolean isActive=true;
		if (Utils.isEmpty(code) || !code.equalsIgnoreCase(checkCode)) {
			 error = "验证码错误";
			 snap = "checkCode";
		 } else {
			auth = Utils.notEmpty(auth) ? auth.trim().toUpperCase() : auth;
			if (DATA_DEVELOPER_ROLE_ID.equals(role)) {
				DevDeveloper devDeveloper = remoteService.getDevDeveloperService().getByLoginName(auth);
				HttpUtil.setSessionAttr(request, "devDeveloper", devDeveloper);
				if (devDeveloper == null || !PubService.checkPassword(pwd, devDeveloper.getPassword())) {
					error = "用户名或密码错误";
				} else {
					HttpUtil.setSessionAttr(request, PubConstants.SESSION_DEV_DEVELOPER,
							devDeveloper);
					if (StatusEnum.INACTIVE.getIndex().equals(devDeveloper.getStatus())) {
						isActive=false;
					}
				}
			} else {
				SysManager sysManager = remoteService.getSysManagerService().findUniqueLoginName(auth);
				if (sysManager == null || !PubService.checkPassword(pwd, sysManager.getPassword())) {
					error = "用户名或密码错误";
				} else {
					HttpUtil.setSessionAttr(request, PubConstants.SESSION_SYS_MANAGER,
						sysManager);
				}
			}
			if (Utils.isEmpty(error)) {
				List<Map<String, Object>> list = remoteService.getSysResourceService().findResMapList(role);
				List<SysResource> sysResourceList = PubService.buildUserSysResource(list);
				HttpUtil.setSessionAttr(request, PubConstants.SESSION_USER_ROLE, role);				
				HttpUtil.setSessionAttr(request, PubConstants.SESSION_USER_SYSRESOURCE, sysResourceList);
			}
		
		 }
		if (Utils.notEmpty(error)) {
			String snaptype = null;
			CommandGroup cg = new CommandGroup();
			if (Utils.notEmpty(snap)) {
				snaptype = "lr"; // 在左边提示
			} else {
				snap = "auth";
			}
			cg.add(new JSCommand("VM(this).fv('checkCode','');Q('#codeImg').click();"));
			cg.add(new TipCommand(error).setSnap(snap).setSnaptype(snaptype));
			return cg;
		} else {
			if (isActive) {
				return new JSCommand("window.location.href='./index.jsp';");
			}else {
				return new JSCommand("VM(this).cmd({type:'ajax',src:'developer/toactive?auth="+auth+"'})");//FIXME
			}
		}
	}

	@RequestMapping("/logout")
	@ResponseBody
	public Object logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String role = (String) HttpUtil.getSessionAttr(request, PubConstants.SESSION_USER_ROLE);
		// 销毁当前系统session
		HttpUtil.destroySession(request);
		String httpProto=request.getHeader("X-Forwarded-Proto");
		httpProto=httpProto==null?"http":httpProto;
		String contextPath= request.getContextPath();
		String redirectUrl=httpProto+"://"+ request.getServerName()+contextPath;
		if ("2".equals(role)) {
			redirectUrl = redirectUrl+"/loginManager.jsp";
		} else {
			redirectUrl = redirectUrl+"/login.jsp";
		}
		if (FrameworkHelper.isFromDFish(request)) {// ajax请求时，js跳转
			return new JSCommand("window.location.replace(\"" + redirectUrl + "\");");
		} else {
			response.sendRedirect(redirectUrl);
			return null;
		}
	}

	@RequestMapping("/template")
	@ResponseBody
	public DialogTemplate template(HttpServletRequest request) throws Exception {
		if (Utils.isEmpty(dialogTemplates)) {
			// 模板获取地址
			DialogTemplate std = IndexView.getStdTemplate();
			dialogTemplates.put("std", std);

			DialogTemplate form = IndexView.getFormTemplate();
			dialogTemplates.put("form", form);

			DialogTemplate callback = IndexView.getCallbackTemplate();
			dialogTemplates.put("callback", callback);

			DialogTemplate alert = IndexView.getAlertTemplate();
			dialogTemplates.put("alert", alert);

			DialogTemplate prong = IndexView.getProngTemplate();
			dialogTemplates.put("prong", prong);
		}

		String id = request.getParameter("id");
		return dialogTemplates.get(id);
	}

	@RequestMapping("/editPwd")
	@ResponseBody
	public Object editPwd(HttpServletRequest request) throws Exception {
		DialogCommand dialog = new DialogCommand("editPwd", "std", "密码修改", "480", "280", DialogCommand.POSITION_MIDDLE, null)
				.setCover(false);
		View view = IndexView.buildEditPwdView();
		dialog.setNode(view);
		return dialog;
	}


	@RequestMapping("/savePwd")
	@ResponseBody
	public Object savePwd(HttpServletRequest request) throws Exception {
		String oldPwd = request.getParameter("oldPwd");
		String newPwd = request.getParameter("newPwd");
		String newPwdCfm = request.getParameter("newPwdCfm");
		if (!newPwd.equals(newPwdCfm)) {
			return new TipCommand("新密码输入不一致").setSnap("newPwdCfm");
		}

		CommandGroup cg = new CommandGroup();
		// 检验

		String password = PubService.encryptPassword(newPwd);
		String role = (String) HttpUtil.getSessionAttr(request, PubConstants.SESSION_USER_ROLE);
		if (DATA_DEVELOPER_ROLE_ID.equals(role)) {
			DevDeveloper devDeveloper = (DevDeveloper) HttpUtil.getSessionAttr(request,
					PubConstants.SESSION_DEV_DEVELOPER);
			// 验证旧密码是正确
			if (PubService.checkPassword(MD5.encode(oldPwd), devDeveloper.getPassword())) {
				devDeveloper.setPassword(password);
				remoteService.getDevDeveloperService().updateDevDeveloper(devDeveloper);
			} else {
				cg.add(PubView.getInfoAlert("原密码错误，修改失败"));
				return cg;
			}
		} else if (DATA_MANAGER_ROLE_ID.equals(role)) {
			SysManager sysManager = (SysManager) HttpUtil.getSessionAttr(request, PubConstants.SESSION_SYS_MANAGER);
			// 验证旧密码是正确
			if (PubService.checkPassword(MD5.encode(oldPwd), sysManager.getPassword())) {
				sysManager.setPassword(password);
				remoteService.getSysManagerService().updateSysManager(sysManager);
			} else {
				cg.add(PubView.getInfoAlert("原密码错误，修改失败"));
				return cg;
			}

		}

		cg.add(PubView.getInfoAlert("密码修改成功"));
		cg.add(new JSCommand("$.dialog(this).close();"));

		return cg;
	}
	
	@RequestMapping("/validImg")
	@ResponseBody
	public Object validImg(HttpServletRequest request, HttpServletResponse response) throws Exception {
		int width = 80, height = 40;
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics g = image.getGraphics();
		Color color = PubService.getRandomColor();//获取随机色
		// new Color(0x72, 0x86, 0xcf)
		g.setColor(Color.lightGray);//设置背景色
		g.fillRect(0, 0, width, height);
		
		g.setColor(color);
		String code=PubService.getRandomString(4);
		request.getSession().setAttribute(PubService.CHECK_CODE_KEY, code);// 把校验码的内容留在session内。
		//填充随机字符串，ys为字符串最高点与最低点，是为了让干扰线穿过这个范围
		PubService.drawChars(g,code,width,height);
		g.dispose();

		response.setHeader("Cache-Control", "no-store");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/png");
		OutputStream output = null;
		try {
			output = response.getOutputStream();
			ImageIO.write(image, "PNG", output);
        } catch (Exception e) {
        	log.error("=====validImg异常=====", e);
        } finally {
        	if (output != null) {
        		output.close();
        	}
        }
		return null;
	}

}
