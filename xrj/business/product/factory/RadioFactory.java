package com.xrj.business.product.factory;

import com.rongji.dfish.ui.form.Radio;
import com.xrj.business.product.vo.WidgetVo;

public class RadioFactory implements WidgetFactory{

	@Override
	public Radio create(WidgetVo widgetVo) {
		// TODO Auto-generated method stub
		return new Radio(widgetVo.getWidgetId(), "", widgetVo.getSelect(), String.valueOf(widgetVo.getWidgetValueVo().getValue()), String.valueOf(widgetVo.getWidgetValueVo().getValue())).setReadonly(widgetVo.getReadOnly()).setStyle(widgetVo.getStyle());
	}

}
