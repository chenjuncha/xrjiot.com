package com.xrj.business.util;


/**
 * Page转换类（dfishPage<-->daoPage）
 * 
 * @author chenyun
 * @date 2017-07-25
 */
public class PageConvertUtil {
	
	
	public static com.rongji.dfish.base.Page   convertDfishPage(com.xrj.framework.hibernate.dao.Page daoPage){
		com.rongji.dfish.base.Page dfishPage = new com.rongji.dfish.base.Page();
		dfishPage.setPageSize(daoPage.getPageSize());
		dfishPage.setCurrentPage(daoPage.getCurrentPage());
//		dfishPage.setCurrentCount(daoPage.get);
		dfishPage.setAutoRowCount(true);
		dfishPage.setRowCount(daoPage.getTotal());
		dfishPage.setResultSet(daoPage.getResultSet());
		//CurrentCount ,	通过以上值计算
		return dfishPage;
	}
	
	public static com.xrj.framework.hibernate.dao.Page  convertDaoPage(com.rongji.dfish.base.Page  dfishPage){
		com.xrj.framework.hibernate.dao.Page daoPage = com.xrj.framework.hibernate.dao.Page.create();
		daoPage.setPageSize(dfishPage.getPageSize());
		daoPage.setCurrentPage(dfishPage.getCurrentPage());
//		daoPage.setTotal(total);
		return daoPage;
	}
}
