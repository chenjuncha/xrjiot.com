package com.xrj.business.product.factory;

import com.rongji.dfish.ui.widget.Html;
import com.xrj.business.product.vo.WidgetVo;

public class HtmlFactory implements WidgetFactory{

	@Override
	public Html create(WidgetVo widgetVo) {
		if(widgetVo.getWeight() != -1) {
		    return new Html(widgetVo.getWidgetLabel()).setStyle(widgetVo.getStyle()).setWidth(widgetVo.getWeight());
	    }
		if(widgetVo.getWidth() != null) {
			 return new Html(widgetVo.getWidgetLabel()).setStyle(widgetVo.getStyle()).setWidth(widgetVo.getWidth());
		}	
		return new Html(widgetVo.getWidgetLabel());
	}
}
