package com.xrj.business.product.factory;

import com.rongji.dfish.ui.form.Spinner;
import com.xrj.business.product.vo.WidgetVo;

public class SpinnerFactory implements WidgetFactory{

	@Override
	public Spinner create(WidgetVo widgetVo) {
		if(widgetVo.getEventVo().size() == 1 ){
			if(widgetVo.getValid().size() == 1){
			return new Spinner(widgetVo.getWidgetId(), widgetVo.getWidgetLabel(),
					(Number) widgetVo.getWidgetValueVo().getValue(), widgetVo.getWidgetValueVo().getMinValue(), widgetVo.getWidgetValueVo().getMaxValue(),widgetVo.getWidgetValueVo().getResolution())
					.setId(widgetVo.getWidgetId()).addValidate(widgetVo.getValid().get(0)).setRequired(widgetVo.getSelect()).setReadonly(widgetVo.getReadOnly()).setWidth(widgetVo.getWeight()).setOn(widgetVo.getEventVo().get(0).getClickEvent(), widgetVo.getEventVo().get(0).getJsCommand());
			}else if(widgetVo.getValid().size() == 2){
				return new Spinner(widgetVo.getWidgetId(), widgetVo.getWidgetLabel(),
						(Number) widgetVo.getWidgetValueVo().getValue(), widgetVo.getWidgetValueVo().getMinValue(), widgetVo.getWidgetValueVo().getMaxValue(),widgetVo.getWidgetValueVo().getResolution())
						.setId(widgetVo.getWidgetId()).addValidate(widgetVo.getValid().get(1)).addValidate(widgetVo.getValid().get(0)).setRequired(widgetVo.getSelect()).setReadonly(widgetVo.getReadOnly()).setWidth(widgetVo.getWeight()).setOn(widgetVo.getEventVo().get(0).getClickEvent(), widgetVo.getEventVo().get(0).getJsCommand());
			}else {
				return new Spinner(widgetVo.getWidgetId(), widgetVo.getWidgetLabel(),
						(Number) widgetVo.getWidgetValueVo().getValue(), widgetVo.getWidgetValueVo().getMinValue(), widgetVo.getWidgetValueVo().getMaxValue(),widgetVo.getWidgetValueVo().getResolution())
						.setId(widgetVo.getWidgetId()).setRequired(widgetVo.getSelect()).setReadonly(widgetVo.getReadOnly()).setWidth(widgetVo.getWeight()).setOn(widgetVo.getEventVo().get(0).getClickEvent(), widgetVo.getEventVo().get(0).getJsCommand());
			}
			
		}
		if(widgetVo.getEventVo().size() == 2) {
			if(widgetVo.getValid().size() == 1){
			return new Spinner(widgetVo.getWidgetId(), widgetVo.getWidgetLabel(),
					(Number) widgetVo.getWidgetValueVo().getValue(), widgetVo.getWidgetValueVo().getMinValue(), widgetVo.getWidgetValueVo().getMaxValue(),widgetVo.getWidgetValueVo().getResolution())
					.setId(widgetVo.getWidgetId()).addValidate(widgetVo.getValid().get(0)).setRequired(widgetVo.getSelect()).setReadonly(widgetVo.getReadOnly()).setWidth(widgetVo.getWeight()).setOn(widgetVo.getEventVo().get(0).getClickEvent(), widgetVo.getEventVo().get(0).getJsCommand()).setOn(widgetVo.getEventVo().get(1).getClickEvent(), widgetVo.getEventVo().get(1).getJsCommand());
			}else if(widgetVo.getValid().size() == 2){
				return new Spinner(widgetVo.getWidgetId(), widgetVo.getWidgetLabel(),
						(Number) widgetVo.getWidgetValueVo().getValue(), widgetVo.getWidgetValueVo().getMinValue(), widgetVo.getWidgetValueVo().getMaxValue(),widgetVo.getWidgetValueVo().getResolution())
						.setId(widgetVo.getWidgetId()).addValidate(widgetVo.getValid().get(1)).addValidate(widgetVo.getValid().get(0)).setRequired(widgetVo.getSelect()).setReadonly(widgetVo.getReadOnly()).setWidth(widgetVo.getWeight()).setOn(widgetVo.getEventVo().get(0).getClickEvent(), widgetVo.getEventVo().get(0).getJsCommand()).setOn(widgetVo.getEventVo().get(1).getClickEvent(), widgetVo.getEventVo().get(1).getJsCommand());
			}else {
				return new Spinner(widgetVo.getWidgetId(), widgetVo.getWidgetLabel(),
						(Number) widgetVo.getWidgetValueVo().getValue(), widgetVo.getWidgetValueVo().getMinValue(), widgetVo.getWidgetValueVo().getMaxValue(),widgetVo.getWidgetValueVo().getResolution())
						.setId(widgetVo.getWidgetId()).setRequired(widgetVo.getSelect()).setReadonly(widgetVo.getReadOnly()).setWidth(widgetVo.getWeight()).setOn(widgetVo.getEventVo().get(0).getClickEvent(), widgetVo.getEventVo().get(0).getJsCommand()).setOn(widgetVo.getEventVo().get(1).getClickEvent(), widgetVo.getEventVo().get(1).getJsCommand());
			}
		}
		return new Spinner(widgetVo.getWidgetId(), widgetVo.getWidgetLabel(),
				(Number) widgetVo.getWidgetValueVo().getValue(), widgetVo.getWidgetValueVo().getMinValue(), widgetVo.getWidgetValueVo().getMaxValue(),widgetVo.getWidgetValueVo().getResolution())
				.setId(widgetVo.getWidgetId()).setRequired(widgetVo.getSelect()).setReadonly(widgetVo.getReadOnly()).setWidth(widgetVo.getWeight());
	}
	
}
