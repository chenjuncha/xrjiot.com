package com.xrj.business.core.web;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.PutObjectResult;
import com.rongji.dfish.base.DfishException;
import com.rongji.dfish.base.util.FileUtil;
import com.rongji.dfish.framework.FrameworkHelper;
import com.rongji.dfish.framework.plugin.file.controller.FileController;
import com.xrj.api.dto.DevDeveloper;
import com.xrj.api.dto.UploadItem;
import com.xrj.business.core.base.RemoteService;
import com.xrj.business.core.config.AliyunConfig;
import com.xrj.framework.util.Assert;
import com.xrj.framework.util.FileUtils;
import com.xrj.framework.util.HttpUtil;
import com.xrj.framework.util.ObjectConverter;

@RequestMapping("/fileService")
@Controller
public class FileServiceController extends FileController {

	@Resource
	RemoteService remoteService;
	@Resource
	AliyunConfig config;
	@RequestMapping("/uploadFile")
	@ResponseBody
	public Object uploadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String action = request.getParameter("action");
		if (action != null && !action.equals("")) {
			// 百度ueditor
			if ("config".equals(action)) {
				InputStream is = FileServiceController.class.getClassLoader()
						.getResourceAsStream("com/rongji/dfish/framework/plugin/file/controller/ueditor_config.json");
				int readed = -1;
				byte[] buff = new byte[8192];
				String readedJson = "";
				try {
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					while ((readed = is.read(buff)) > 0) {
						baos.write(buff, 0, readed);
					}
					readedJson = new String(baos.toByteArray(), "UTF-8");
				} catch (IOException e1) {
					e1.printStackTrace();
				} finally {
					try {
						is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				return readedJson;
			}
		}
		UploadItem uploadItem = null;
		long currMs = System.currentTimeMillis();
		try {
			if (request instanceof MultipartHttpServletRequest) {
				MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
				MultipartFile fileData = mRequest.getFile("Filedata");

				// String loginUserId = FrameworkHelper.getLoginUser(mRequest);
				DevDeveloper developer = (DevDeveloper) HttpUtil.getSessionAttr(request, "SESSION_DEV_DEVELOPER");
				if (developer == null) {
					uploadItem = remoteService.getRemoteFileService().save(fileData.getOriginalFilename(),
							fileData.getBytes(), "2");
				} else {
					uploadItem = remoteService.getRemoteFileService().save(fileData.getOriginalFilename(),
							fileData.getBytes(), developer.getId());
				}

			}
		} catch (Exception e) {
			FrameworkHelper.LOG.error("上传失败@" + currMs, e);
		}
		if (action != null && !action.equals("")) {
			// 百度ueditor
			return "{\"state\":\"SUCCESS\"," + "\"url\":\"file/thumbnail?fileId=" + uploadItem.getId() + "\","
					+ "\"title\":\"" + uploadItem.getName() + "\"," + "\"original\":\"" + uploadItem.getName() + "\","
					+ "\"type\":\"" + FileUtil.getFileExtName(uploadItem.getName()) + "\"," + "\"size\":"
					+ uploadItem.getSize() + "" + "}";
		}
		if (uploadItem == null) {
			uploadItem = new UploadItem();
			uploadItem.setError(true);
			uploadItem.setText("上传失败@" + currMs);
		}
		com.rongji.dfish.ui.form.UploadItem dfishUploadItem = new com.rongji.dfish.ui.form.UploadItem();
		dfishUploadItem.setId(uploadItem.getId());
		dfishUploadItem.setUrl(uploadItem.getUrl());
		dfishUploadItem.setName(uploadItem.getName());

		return dfishUploadItem;
	}

	@RequestMapping("/uploadImage")
	@ResponseBody
	public Object uploadImage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		UploadItem uploadItem = null;
		long currMs = System.currentTimeMillis();
		try {
			if (request instanceof MultipartHttpServletRequest) {
				MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
				MultipartFile fileData = mRequest.getFile("Filedata");

				String loginUserId = FrameworkHelper.getLoginUser(mRequest);
				uploadItem = remoteService.getRemoteFileService().save(fileData.getOriginalFilename(),
						fileData.getBytes(), loginUserId);
			}
		} catch (Exception e) {
			FrameworkHelper.LOG.error("上传失败@" + currMs, e);
		}
		if (uploadItem == null) {
			uploadItem = new UploadItem();
			uploadItem.setError(true);
			uploadItem.setText("上传失败@" + currMs);
		}

		return uploadItem;
	}

	@RequestMapping("/downloadFile")
	@ResponseBody
	/**
	 * 下载附件方法
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public void downloadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String enFileId = request.getParameter("fileId");
		StringBuffer ss = new StringBuffer();
		String[] n = enFileId.split("/");
		String str = enFileId.substring(enFileId.lastIndexOf("."));
		ss.append(n[n.length - 2]);
		ss.append(str);
		// String fileId = FileService.decId(enFileId);
		// String contentType = "application/octet-stream";
		response.setContentType("application/octet-stream;charset=utf-8");
		response.setHeader("Content-Disposition",
				"attachment;filename=" + new String((ss.toString()).getBytes(), "iso-8859-1"));
		try {
			byte[] bytes = remoteService.getRemoteFileService().get(enFileId);
			FileUtil.downLoadData(response, new ByteArrayInputStream(bytes));
		} catch (Exception e) {
			FrameworkHelper.LOG.error("=========下载附件异常=========", e);
			String error = "附件不存在[" + enFileId + "]";
			response.sendError(HttpServletResponse.SC_EXPECTATION_FAILED, error);
		}
	}

	@RequestMapping("/removeFile")
	@ResponseBody
	/**
	 * 删除文件方法
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public void removeFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String enFileId = request.getParameter("fileId");
		// String fileId = FileService.decId(enFileId);
		remoteService.getRemoteFileService().delete(enFileId);
		// try {
		// PubFileRecord fileRecord = fileService.get(fileId);
		// File file = fileService.getFile(fileRecord);
		// if (file != null && file.exists()) {
		// file.delete();
		// }
		// if (fileRecord != null) {
		// fileService.delete(fileRecord);
		// }
		// } catch (Exception e) {
		// FrameworkHelper.LOG.error("=========移除附件异常=========", e);
		// String error = "附件不存在[" + enFileId + "]";
		// response.sendError(HttpServletResponse.SC_EXPECTATION_FAILED, error);
		// }
	}

	@RequestMapping("/preview")
	@ResponseBody
	/**
	 * 预览文件方法
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public Object preview(HttpServletRequest request, HttpServletResponse response) throws Exception {

		return null;
	}

	@RequestMapping("/romUpload")
	@ResponseBody
	/**
	 * 预览文件方法
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public Object romUpload(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String url="";
		com.rongji.dfish.ui.form.UploadItem dfishUploadItem = new com.rongji.dfish.ui.form.UploadItem();
		if (request instanceof MultipartHttpServletRequest) {
			MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
			MultipartFile file = mRequest.getFile("Filedata");
			String originName = file.getOriginalFilename();
			String type=originName.substring(originName.lastIndexOf("."));
			String fileName=originName.substring(0, originName.lastIndexOf("."));
			String domain=config.getRomDomain();
			 String endpoint = config.getBucketEndpint();
    		 // 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建
    		 String accessKeyId = config.getAccessKeyId();
    		 String accessKeySecret = config.getAccesskeySecret();
    		 // 创建OSSClient实例
    		 OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
    		 // 上传文件流
    		 byte[] fileByte=null;
			String now= Assert.formatDate2(new Date());
			if (".zip".equals(type)) {
				  CommonsMultipartFile cf= (CommonsMultipartFile)file; 
			        DiskFileItem fi = (DiskFileItem)cf.getFileItem(); 
			        File f = fi.getStoreLocation();
			        ZipFile zipFile=new ZipFile(f);
			        for(  Enumeration<? extends ZipEntry> enumeration  = zipFile.entries();enumeration .hasMoreElements();)  
			        { 
			        	 ZipEntry zipEntry = enumeration.nextElement();//获取元素
			        	 if(!zipEntry.getName().endsWith("/"))  
			             {  
			        		 fileByte=zipEntry.getExtra();
			        		 InputStream inputStream = new ByteArrayInputStream(zipEntry.getExtra());
			        		 ossClient.putObject(config.getRomBucketName(), now+"/"+zipEntry.getName(), inputStream);
			             }
			        }
			        
			}else{
				fileByte=file.getBytes();
				InputStream inputStream = new ByteArrayInputStream(fileByte);
				ossClient.putObject(config.getRomBucketName(),now+"/"+originName, inputStream);
        		fileName=originName;
			}
			ossClient.shutdown(); 
			url=domain+"/"+now+"/"+fileName;
			
			dfishUploadItem.setId("");
			dfishUploadItem.setUrl(url);
			dfishUploadItem.setName(originName);
			return dfishUploadItem;
		}
		return null;
	}

}
