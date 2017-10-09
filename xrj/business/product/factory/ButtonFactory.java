package com.xrj.business.product.factory;

import com.rongji.dfish.ui.widget.Button;
import com.xrj.business.product.vo.WidgetVo;

public class ButtonFactory implements WidgetFactory{

	@Override
	public Button create(WidgetVo widgetVo) {
		// TODO Auto-generated method stub
		return new Button(widgetVo.getWidgetLabel()).setOn(widgetVo.getEventVo().get(0).getClickEvent(), widgetVo.getEventVo().get(0).getJsCommand()).setWidth(widgetVo.getWeight()).setStyle(widgetVo.getStyle()).setCls(widgetVo.getCls());
	}

}
