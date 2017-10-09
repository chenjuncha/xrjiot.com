package com.xrj.business.product.factory;


import com.rongji.dfish.ui.helper.Label;
import com.xrj.business.product.vo.WidgetVo;

public class LabelFactory implements WidgetFactory {

	@Override
	public Label create(WidgetVo widgetVo) {
		Label label = new Label(widgetVo.getWidgetLabel(), widgetVo.getWidgetValueVo().getValue()==null?"":String.valueOf(widgetVo.getWidgetValueVo().getValue())).setId(widgetVo.getWidgetId()).setStyle(widgetVo.getStyle()).setWidth(widgetVo.getWeight());
		return label;
	}

}
