package com.xrj.business.product.factory;

import java.util.ArrayList;
import java.util.List;


import com.rongji.dfish.base.Utils;
import com.rongji.dfish.ui.Widget;
import com.rongji.dfish.ui.layout.HorizontalLayout;
import com.xrj.business.product.constans.WidgetTypeConstans;
import com.xrj.business.product.vo.WidgetVo;

public class BatchFactory {
	
	public static List<Widget<?>> create(List<WidgetVo> widgetVos) {

		// TODO Auto-generated method stub
		List<Widget<?>> list = new ArrayList<Widget<?>>();
		for(WidgetVo widgetVo : widgetVos){
			if(widgetVo.getWidgetType().equals(WidgetTypeConstans.TEXT)){
				list.add((Widget<?>) new TextFactory().create(widgetVo));
			}else if(widgetVo.getWidgetType().equals(WidgetTypeConstans.LABEL)){
				list.add(new LabelFactory().create(widgetVo));
			}else if(widgetVo.getWidgetType().equals(WidgetTypeConstans.SELECT)){
				list.add(new SelectFactory().create(widgetVo));
			}else if(widgetVo.getWidgetType().equals(WidgetTypeConstans.SPINNER)){
				list.add(new SpinnerFactory().create(widgetVo));
			}else if(widgetVo.getWidgetType().equals(WidgetTypeConstans.TEXTAREA)){
				list.add(new TextAreaFactory().create(widgetVo));
			}else if(widgetVo.getWidgetType().equals(WidgetTypeConstans.HTML)){
				list.add(new HtmlFactory().create(widgetVo));
			}else if(widgetVo.getWidgetType().equals(WidgetTypeConstans.BUTTON)){
				list.add(new ButtonFactory().create(widgetVo));
			}else if(widgetVo.getWidgetType().equals(WidgetTypeConstans.HOR)){
				list.add(new HorizonFactory().create(widgetVo));
			}	
		}
		return list;
	}
	/**
	 * 创建horizonLayout 包多个控件
	 * @param widgetVos
	 * @return
	 */
	public static Widget<?> createHorizonLayout(List<WidgetVo> widgetVos){
		HorizontalLayout horizontalLayout = new HorizontalLayout("");
		for(WidgetVo widgetVo : widgetVos){
			if(widgetVo.getWidgetType().equals(WidgetTypeConstans.TEXT)){
				horizontalLayout.add((Widget<?>) new TextFactory().create(widgetVo));
			}else if(widgetVo.getWidgetType().equals(WidgetTypeConstans.LABEL)){
				horizontalLayout.add(new LabelFactory().create(widgetVo));
			}else if(widgetVo.getWidgetType().equals(WidgetTypeConstans.SELECT)){
				horizontalLayout.add(new SelectFactory().create(widgetVo));
			}else if(widgetVo.getWidgetType().equals(WidgetTypeConstans.SPINNER)){
				horizontalLayout.add(new SpinnerFactory().create(widgetVo));
			}else if(widgetVo.getWidgetType().equals(WidgetTypeConstans.TEXTAREA)){
				horizontalLayout.add(new TextAreaFactory().create(widgetVo));
			}else if(widgetVo.getWidgetType().equals(WidgetTypeConstans.HTML)){
				horizontalLayout.add(new HtmlFactory().create(widgetVo));
			}else if(widgetVo.getWidgetType().equals(WidgetTypeConstans.BUTTON)){
				horizontalLayout.add(new ButtonFactory().create(widgetVo));
			}else if(widgetVo.getWidgetType().equals(WidgetTypeConstans.Checkbox)){
				horizontalLayout.add(new CheckBoxFactory().create(widgetVo));
			}else if(widgetVo.getWidgetType().equals(WidgetTypeConstans.Radio)){
				horizontalLayout.add(new RadioFactory().create(widgetVo));
			}
		}
		return horizontalLayout;
	}
	
}
