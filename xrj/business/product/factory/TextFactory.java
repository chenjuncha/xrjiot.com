package com.xrj.business.product.factory;

import com.rongji.dfish.ui.form.AbstractFormElement;
import com.rongji.dfish.ui.form.Text;
import com.xrj.business.product.vo.WidgetVo;

public class TextFactory implements WidgetFactory{


	@Override
	public Text create(WidgetVo widgetVo) {
		if(widgetVo.getWeight() != -1) {
			if(widgetVo.getValid().size() == 1) {
				return new Text(widgetVo.getWidgetId(), widgetVo.getWidgetLabel(), widgetVo.getWidgetValueVo().getValue()==null?"":String.valueOf(widgetVo.getWidgetValueVo().getValue()))
					.setWidth(widgetVo.getWeight()).addValidate(widgetVo.getValid().get(0)).setReadonly(widgetVo.getReadOnly());
			} else if(widgetVo.getValid().size() == 2) {
				return new Text(widgetVo.getWidgetId(), widgetVo.getWidgetLabel(), widgetVo.getWidgetValueVo().getValue()==null?"":String.valueOf(widgetVo.getWidgetValueVo().getValue()))
				    .setWidth(widgetVo.getWeight()).addValidate(widgetVo.getValid().get(1)).addValidate(widgetVo.getValid().get(0)).setReadonly(widgetVo.getReadOnly());
			} else {
		        return new Text(widgetVo.getWidgetId(), widgetVo.getWidgetLabel(), widgetVo.getWidgetValueVo().getValue()==null?"":String.valueOf(widgetVo.getWidgetValueVo().getValue()))
				    .setWidth(widgetVo.getWeight()).setReadonly(widgetVo.getReadOnly());
			}
		}
		if(widgetVo.getWidth() != null) {
			if(widgetVo.getValid().size() == 1) {
				return new Text(widgetVo.getWidgetId(), widgetVo.getWidgetLabel(), widgetVo.getWidgetValueVo().getValue()==null?"":String.valueOf(widgetVo.getWidgetValueVo().getValue()))
					.setWidth(widgetVo.getWidth()).addValidate(widgetVo.getValid().get(0)).setReadonly(widgetVo.getReadOnly());
			} else if(widgetVo.getValid().size() == 2) {
				return new Text(widgetVo.getWidgetId(), widgetVo.getWidgetLabel(), widgetVo.getWidgetValueVo().getValue()==null?"":String.valueOf(widgetVo.getWidgetValueVo().getValue()))
					.setWidth(widgetVo.getWidth()).addValidate(widgetVo.getValid().get(1)).addValidate(widgetVo.getValid().get(0)).setReadonly(widgetVo.getReadOnly());
			} else {
				return new Text(widgetVo.getWidgetId(), widgetVo.getWidgetLabel(),widgetVo.getWidgetValueVo().getValue()==null?"":String.valueOf(widgetVo.getWidgetValueVo().getValue()))
					.setWidth(widgetVo.getWidth()).setReadonly(widgetVo.getReadOnly());
			}
		}
		return new Text(widgetVo.getWidgetId(), widgetVo.getWidgetLabel(), widgetVo.getWidgetValueVo().getValue()==null?"":String.valueOf(widgetVo.getWidgetValueVo().getValue()))
			.setReadonly(widgetVo.getReadOnly());
	}
	
}
