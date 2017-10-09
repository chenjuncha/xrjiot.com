package com.xrj.business.product.factory;

import com.rongji.dfish.ui.form.AbstractFormElement;
import com.xrj.business.product.vo.WidgetVo;

public interface WidgetFactory {
	public  Object create(WidgetVo widgetVo);
}
