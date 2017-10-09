package com.xrj.business.product.factory;

import com.rongji.dfish.ui.form.Checkbox;
import com.xrj.business.product.vo.WidgetVo;

public class CheckBoxFactory implements WidgetFactory{

	@Override
	public Checkbox create(WidgetVo widgetVo) {
		// TODO Auto-generated method stub
		if(widgetVo.getEventVo().size() != 0) {
			return new Checkbox("","",widgetVo.getSelect(),"1","").setReadonly(widgetVo.getReadOnly()).setId(widgetVo.getWidgetId()).setOn(widgetVo.getEventVo().get(0).getClickEvent(),widgetVo.getEventVo().get(0).getJsCommand()).setName(widgetVo.getWidgetId());
		}
		return new Checkbox("","",widgetVo.getSelect(),"1","").setReadonly(widgetVo.getReadOnly()).setId(widgetVo.getWidgetId()).setName(widgetVo.getWidgetId());
	}

}
