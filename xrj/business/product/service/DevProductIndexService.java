package com.xrj.business.product.service;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.rongji.dfish.base.Utils;
import com.xrj.api.dto.DevDeveloper;
import com.xrj.api.dto.DevProduct;
import com.xrj.api.service.DevProductService;
import com.xrj.api.support.CallResult;
import com.xrj.business.core.base.PubConstants;
import com.xrj.business.core.base.RemoteService;
import com.xrj.business.core.view.PubView;
import com.xrj.framework.util.HttpUtil;
@Component
public class DevProductIndexService {
	@Resource
	private RemoteService remoteService;
	/**
	 * 根据开发者id、发布状态、产品名称获取产品列表
	 * @param developerId
	 * @param productStatus
	 * @param productName
	 * @return
	 */
	public List<DevProduct> queryProductList(HttpServletRequest request,String productStatus,String productName){
		DevDeveloper devDeveloper = (DevDeveloper) HttpUtil.getSessionAttr(request, PubConstants.SESSION_DEV_DEVELOPER);
		DevProductService productService = remoteService.getDevProductService();
		List<DevProduct> list = productService.queryProductList(devDeveloper.getId(), productStatus, productName);
		return list;
	}
	
	public DevProduct get(String productId){
		DevProductService productService = remoteService.getDevProductService();
		return productService.get(productId);
	}
	
	public CallResult delete(String productId){
		DevProductService productService = remoteService.getDevProductService();
		return productService.delete(productId);
	}
	/**
	 * 新增产品
	 * @param request
	 * @return
	 */
	public CallResult save(HttpServletRequest request){
		DevProductService productService = remoteService.getDevProductService();
		DevProduct product = new DevProduct();
		String productName = request.getParameter("productName");
		product.setProductName(productName);
		String class2 = request.getParameter("class2");
		String class3 = request.getParameter("class3");
		if (Utils.isEmpty(class3)) {
			product.setProductClassification(class2);
		} else {
			product.setProductClassification(class3);
		}
		String productType = request.getParameter("productType");
		product.setProductType(productType);
		//DevProductService devProductService = remoteService.getDevProductService();
		DevDeveloper devDeveloper = (DevDeveloper) HttpUtil.getSessionAttr(request, PubConstants.SESSION_DEV_DEVELOPER);
		product.setDeveloperId(devDeveloper.getId());
		String productUse = request.getParameter("productUse");
		product.setProductUse(productUse);
//		if(Utils.isEmpty(productName)||Utils.isEmpty(product.getProductClassification())||Utils.isEmpty(productType)||Utils.isEmpty(productUse)){
//			return PubView.getInfoAlert("相关信息不能为空");
//		}
		CallResult callResult = productService.save(product);
		return callResult;
	}
	/**
	 * 更新产品信息
	 * @param request
	 * @return
	 */
	public CallResult update(HttpServletRequest request){
		DevProductService productService = remoteService.getDevProductService();
		DevProduct devProduct = (DevProduct) HttpUtil.getSessionAttr(request, "product");
		String productName = request.getParameter("productName");
		devProduct.setProductName(productName);
		String class2 = request.getParameter("class2");
		String class3 = request.getParameter("class3");
		if (Utils.isEmpty(class3)) {
			devProduct.setProductClassification(class2);
		} else {
			devProduct.setProductClassification(class3);
		}
		String productUse = request.getParameter("productUse");
		devProduct.setProductUse(productUse);
		CallResult callResult = productService.update(devProduct);
		return callResult;
	}
	public void publishProduct(String productId){
		DevProductService productService = remoteService.getDevProductService();
		productService.publishDevProduct(productId, PubConstants.AUDIT_TYPE_DEV_PRODUCT);
	}
}
