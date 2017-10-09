package com.xrj.business.product.factory;

import com.rongji.dfish.ui.form.Textarea;
import com.xrj.business.product.vo.WidgetVo;

public class TextAreaFactory implements WidgetFactory{

	@Override
	public Textarea create(WidgetVo widgetVo) {
		// TODO Auto-generated method stub
		Textarea textarea = new Textarea(widgetVo.getWidgetId(), widgetVo.getWidgetLabel(), (String)widgetVo.getWidgetValueVo().getValue());
		
		return textarea;
	}
	
}
