package com.xrj.business.product.factory;

import com.rongji.dfish.ui.layout.HorizontalLayout;
import com.xrj.business.product.vo.WidgetVo;

public class HorizonFactory implements WidgetFactory{

	@Override
	public HorizontalLayout create(WidgetVo widgetVo) {
		// TODO Auto-generated method stub
		HorizontalLayout hor;
		if(widgetVo.getWeight() != -1) {
			 hor = widgetVo.getHor().setStyle(widgetVo.getStyle()).setWidth(widgetVo.getWeight());
		}
		hor = widgetVo.getHor().setStyle(widgetVo.getStyle());
		return hor;
	}

}
