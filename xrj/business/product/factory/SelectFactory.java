package com.xrj.business.product.factory;

import java.util.ArrayList;
import java.util.List;

import com.rongji.dfish.ui.form.AbstractFormElement;
import com.rongji.dfish.ui.form.Select;
import com.xrj.business.product.vo.WidgetVo;
import com.xrj.framework.util.Assert;

public class SelectFactory implements WidgetFactory {

	@Override
	public Select create(WidgetVo widgetVo) {
		// TODO Auto-generated method stub
		if (widgetVo.getEventVo().size() != 0) {
			if (Assert.isEmpty(widgetVo.getWidth())) {
				return new Select(widgetVo.getWidgetId(), widgetVo.getWidgetLabel(),
						widgetVo.getWidgetValueVo().getValue(), (List<?>) widgetVo.getOthers())
								.setReadonly(widgetVo.getReadOnly()).setOn(widgetVo.getEventVo().get(0).getClickEvent(),
										widgetVo.getEventVo().get(0).getJsCommand());
			} else {
				return new Select(widgetVo.getWidgetId(), widgetVo.getWidgetLabel(),
						widgetVo.getWidgetValueVo().getValue(), (List<?>) widgetVo.getOthers())
								.setWidth(widgetVo.getWidth()).setReadonly(widgetVo.getReadOnly())
								.setOn(widgetVo.getEventVo().get(0).getClickEvent(),
										widgetVo.getEventVo().get(0).getJsCommand());
			}
		}
		if (Assert.isEmpty(widgetVo.getWidth())) {
			return new Select(widgetVo.getWidgetId(), widgetVo.getWidgetLabel(), widgetVo.getWidgetValueVo().getValue(),
					(List<?>) widgetVo.getOthers()).setReadonly(widgetVo.getReadOnly());
		} else {
			return new Select(widgetVo.getWidgetId(), widgetVo.getWidgetLabel(), widgetVo.getWidgetValueVo().getValue(),
					(List<?>) widgetVo.getOthers()).setWidth(widgetVo.getWidth()).setReadonly(widgetVo.getReadOnly());
		}
	}

}
