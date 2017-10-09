package com.xrj.business.product.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.rongji.dfish.ui.Widget;
import com.rongji.dfish.ui.form.Checkbox;
import com.rongji.dfish.ui.form.Radio;
import com.rongji.dfish.ui.form.Select;
import com.rongji.dfish.ui.form.Spinner;
import com.rongji.dfish.ui.form.Text;
import com.rongji.dfish.ui.form.Validate;
import com.rongji.dfish.ui.helper.Label;
import com.rongji.dfish.ui.layout.HorizontalLayout;
import com.rongji.dfish.ui.layout.VerticalLayout;
import com.rongji.dfish.ui.widget.Button;
import com.rongji.dfish.ui.widget.Html;
import com.xrj.api.dto.DevProduct;
import com.xrj.api.dto.DevProductFunction;
import com.xrj.api.em.DataTypeEnum;
import com.xrj.api.em.FunctionTypeEnum;
import com.xrj.api.em.ProductStatusEnum;
import com.xrj.business.product.factory.BatchFactory;
import com.xrj.business.product.vo.EventVo;
import com.xrj.business.product.vo.WidgetValueVo;
import com.xrj.business.product.vo.WidgetVo;
import com.xrj.framework.util.Assert;
import com.xrj.api.em.FunctionStatusEnum;

/**
 * @工厂实例化service
 * @author chenjunchao
 * @Date 2017-08-21
 *
 */
public class FactoryService {

	/**
	 * @新增功能点公共控件
	 * @param action
	 * @param point
	 * @return
	 */
	public static List<WidgetVo> addPoint(String action, DevProductFunction point) {

		List<WidgetVo> widgets = new ArrayList<WidgetVo>();
		// 功能类型
		Object[] s1 = new Object[FunctionTypeEnum.values().length];
		int i = 0;
		for (FunctionTypeEnum e : FunctionTypeEnum.values()) {
			Object[] obj = new Object[2];
			obj[0] = e.getValue() + "";
			obj[1] = e.getFunctionTypeName();
			s1[i] = obj;
			i++;
		}
		List<Object> sList1 = Arrays.asList(s1);
		// 数据类型
		Object[] s2 = new Object[DataTypeEnum.values().length];
		int j = 0;
		for (DataTypeEnum e : DataTypeEnum.values()) {
			Object[] obj = new Object[2];
			obj[0] = e.getValue() + "";
			obj[1] = e.getDataTypeName();
			s2[j] = obj;
			j++;
		}
		List<Object> sList2 = Arrays.asList(s2);

		if ("add".equals(action)) {
			widgets.add(new WidgetVo("functionName", "<span style='color:red;'>*</span> 显示名称:", "text"));
			widgets.add(new WidgetVo("functionCode", "<span style='color:red;'>*</span> 标识名称:", "text"));

			WidgetVo functionType = new WidgetVo("functionType", "功能类型", "select");
			functionType.setOthers(sList1);
			List<EventVo> functionTypeeventVo = new ArrayList<EventVo>();
			functionTypeeventVo.add(
					new EventVo().setClickEvent(Select.EVENT_CHANGE).setJsCommand("VM(this).cmd('selectChange2')"));
			functionType.setEventVo(functionTypeeventVo);
			widgets.add(functionType);

			WidgetVo dataType = new WidgetVo("dataType", "数据类型", "select");
			dataType.setOthers(sList2);
			List<EventVo> dataTypeeventVo = new ArrayList<EventVo>();
			dataTypeeventVo.add(new EventVo().setClickEvent(Select.EVENT_CHANGE)
					.setJsCommand("VM(this).cmd('selectChange',this.val())"));
			dataType.setEventVo(dataTypeeventVo);
			widgets.add(dataType);

			WidgetVo serialNo = new WidgetVo("serialNo", "编号:", "text");
			serialNo.setReadOnly(true);
			widgets.add(serialNo);

			widgets.add(new WidgetVo("remark", "备注:", "textarea"));
		} else {
			widgets.add(new WidgetVo("functionName", "<span style='color:red;'>*</span> 显示名称:", "text")
					.setWidgetValueVo(new WidgetValueVo().setValue(point.getFunctionName())));
			widgets.add(new WidgetVo("functionCode", "<span style='color:red;'>*</span> 标识名称:", "text")
					.setWidgetValueVo(new WidgetValueVo().setValue(point.getFunctionCode())));

			WidgetVo functionType = new WidgetVo("functionType", "功能类型", "select");
			functionType.setWidgetValueVo(new WidgetValueVo().setValue(point.getFunctionType()));
			functionType.setOthers(sList1);
			List<EventVo> functionTypeeventVo = new ArrayList<EventVo>();
			functionTypeeventVo.add(
					new EventVo().setClickEvent(Select.EVENT_CHANGE).setJsCommand("VM(this).cmd('selectChange2')"));
			functionType.setEventVo(functionTypeeventVo);
			widgets.add(functionType);

			WidgetVo dataType = new WidgetVo("dataType", "数据类型", "select");
			dataType.setWidgetValueVo(new WidgetValueVo().setValue(point.getDataType()));
			dataType.setOthers(sList2);
			List<EventVo> dataTypeeventVo = new ArrayList<EventVo>();
			dataTypeeventVo.add(new EventVo().setClickEvent(Select.EVENT_CHANGE)
					.setJsCommand("VM(this).cmd('selectChange',this.val())"));
			dataType.setEventVo(dataTypeeventVo);
			widgets.add(dataType);

			WidgetVo serialNo = new WidgetVo("serialNo", "编号:", "text")
					.setWidgetValueVo(new WidgetValueVo().setValue(point.getSerialNo()));
			serialNo.setReadOnly(true);
			widgets.add(serialNo);

			widgets.add(new WidgetVo("remark", "备注:", "textarea")
					.setWidgetValueVo(new WidgetValueVo().setValue(point.getRemark())));
		}

		return widgets;
	}

	/**
	 * @数值类型控件
	 * @param action
	 * @param point
	 * @return
	 */
	public static HorizontalLayout createNumber(String action, DevProductFunction point) {
		List<WidgetVo> horizonWidgets = new ArrayList<WidgetVo>();
		if ("add".equals(action)) {
			horizonWidgets.add(new WidgetVo("", "<span style='color:red;'>*</span>数据范围:", "html").setWeight(93)
					.setStyle("padding-left:25px"));

			List<EventVo> minValueeventVo = new ArrayList<EventVo>();
			minValueeventVo.add(new EventVo().setClickEvent(Spinner.EVENT_BLUR).setJsCommand(
					"VM(this).cmd('changespin',''+VM(this).f('minValue').val()+'',''+VM(this).f('maxValue').val()+'')"));
			minValueeventVo
					.add(new EventVo().setClickEvent(Spinner.EVENT_CHANGE).setJsCommand("VM(this).cmd('toCaculate')"));
			List<Validate> minValid = new ArrayList<Validate>();
			minValid.add(Validate.pattern("/^-?\\d+\\.?\\d{0,2}$/").setPatterntext("最小值最多保留两位"));
			WidgetVo minValue = new WidgetVo("minValue", "", "spinner").setValid(minValid).setSelect(true).setWeight(90)
					.setEventVo(minValueeventVo);
			minValue.setWidgetValueVo(new WidgetValueVo(-99999, 99999, null, 0.01));
			horizonWidgets.add(minValue);
			WidgetVo htm = new WidgetVo("", "~", "html");
			htm.setWeight(10);
			horizonWidgets.add(htm);

			List<EventVo> maxValueeventVo = new ArrayList<EventVo>();
			maxValueeventVo.add(new EventVo().setClickEvent(Spinner.EVENT_BLUR).setJsCommand(
					"VM(this).cmd('changespin',''+VM(this).f('minValue').val()+'',''+VM(this).f('maxValue').val()+'')"));
			List<Validate> maxValid = new ArrayList<Validate>();
			maxValid.add(Validate.pattern("/^-?\\d+\\.?\\d{0,2}$/").setPatterntext("最大值最多保留两位"));
			maxValid.add(Validate.compare(">", "minValue").setComparetext("最大值必须大于最小值"));
			WidgetVo maxValue = new WidgetVo("maxValue", "", "spinner").setValid(maxValid).setSelect(true).setWeight(90)
					.setEventVo(maxValueeventVo);
			maxValue.setWidgetValueVo(new WidgetValueVo(-99999, 99999, null, 0.01));
			horizonWidgets.add(maxValue);
			horizonWidgets
					.add(new WidgetVo("", "增量:", "html").setWeight(30).setStyle("padding-left:100px;padding-top:5px"));
			WidgetVo increment = new WidgetVo("increment", "", "text");
			increment.setReadOnly(true);
			horizonWidgets.add(increment);
			horizonWidgets.add(new WidgetVo("", "", "html"));
			horizonWidgets.add(new WidgetVo("", "<span style='color:red;'>*</span>分辨率:", "html")
					.setStyle("padding-top:5px").setWeight(50));
			List<Validate> resoValid = new ArrayList<Validate>();
			resoValid.add(Validate.pattern("/^[+]{0,1}(\\d+)$|^[+]{0,1}(\\d+\\.\\d+)$/").setPatterntext("分辨率不能为0"));
			resoValid.add(Validate.pattern("/^-?\\d+\\.?\\d{0,3}$/").setPatterntext("分辨率最多保留三位"));
			WidgetVo resolution = new WidgetVo("resolution", "", "spinner").setValid(resoValid).setSelect(true)
					.setWeight(90);
			resolution.setWidgetValueVo(new WidgetValueVo(0.001, 99999, null, 0.001));
			horizonWidgets.add(resolution);
			horizonWidgets.add(new WidgetVo("", "", "html").setWidth("60%"));
		} else {
			horizonWidgets.add(new WidgetVo("", "<span style='color:red;'>*</span>数据范围:", "html").setWeight(93)
					.setStyle("padding-left:25px"));

			List<EventVo> minValueeventVo = new ArrayList<EventVo>();
			minValueeventVo.add(new EventVo().setClickEvent(Spinner.EVENT_BLUR).setJsCommand(
					"VM(this).cmd('changespin',''+VM(this).f('minValue').val()+'',''+VM(this).f('maxValue').val()+'')"));
			minValueeventVo
					.add(new EventVo().setClickEvent(Spinner.EVENT_CHANGE).setJsCommand("VM(this).cmd('toCaculate')"));
			List<Validate> minValid = new ArrayList<Validate>();
			minValid.add(Validate.pattern("/^-?\\d+\\.?\\d{0,2}$/").setPatterntext("最小值最多保留两位"));
			WidgetVo minValue = new WidgetVo("minValue", "", "spinner").setValid(minValid).setSelect(true).setWeight(90)
					.setEventVo(minValueeventVo);
			minValue.setWidgetValueVo(new WidgetValueVo(-99999, 99999, point.getMinValue(), 0.01));
			horizonWidgets.add(minValue);
			WidgetVo htm = new WidgetVo("", "~", "html");
			htm.setWeight(10);
			horizonWidgets.add(htm);

			List<EventVo> maxValueeventVo = new ArrayList<EventVo>();
			maxValueeventVo.add(new EventVo().setClickEvent(Spinner.EVENT_BLUR).setJsCommand(
					"VM(this).cmd('changespin',''+VM(this).f('minValue').val()+'',''+VM(this).f('maxValue').val()+'')"));
			List<Validate> maxValid = new ArrayList<Validate>();
			maxValid.add(Validate.pattern("/^-?\\d+\\.?\\d{0,2}$/").setPatterntext("最大值最多保留两位"));
			maxValid.add(Validate.compare(">", "minValue").setComparetext("最大值必须大于最小值"));
			WidgetVo maxValue = new WidgetVo("maxValue", "", "spinner").setValid(maxValid).setSelect(true).setWeight(90)
					.setEventVo(maxValueeventVo);
			maxValue.setWidgetValueVo(new WidgetValueVo(-99999, 99999, point.getMaxValue(), 0.01));
			horizonWidgets.add(maxValue);
			horizonWidgets
					.add(new WidgetVo("", "增量:", "html").setWeight(30).setStyle("padding-left:100px;padding-top:5px"));
			WidgetVo increment = new WidgetVo("increment", "", "text")
					.setWidgetValueVo(new WidgetValueVo().setValue((0 - point.getMinValue())));
			increment.setReadOnly(true);
			horizonWidgets.add(increment);
			horizonWidgets.add(new WidgetVo("", "", "html"));
			horizonWidgets.add(new WidgetVo("", "<span style='color:red;'>*</span>分辨率:", "html")
					.setStyle("padding-top:5px").setWeight(50));
			List<Validate> resoValid = new ArrayList<Validate>();
			resoValid.add(Validate.pattern("/^[+]{0,1}(\\d+)$|^[+]{0,1}(\\d+\\.\\d+)$/").setPatterntext("分辨率不能为0"));
			resoValid.add(Validate.pattern("/^-?\\d+\\.?\\d{0,3}$/").setPatterntext("分辨率最多保留三位"));
			WidgetVo resolution = new WidgetVo("resolution", "", "spinner").setValid(resoValid).setSelect(true)
					.setWeight(90);
			resolution.setWidgetValueVo(new WidgetValueVo(0.001, 99999, point.getResolution(), 0.001));
			horizonWidgets.add(resolution);
			horizonWidgets.add(new WidgetVo("", "", "html").setWidth("60%"));
		}
		HorizontalLayout range1 = (HorizontalLayout) BatchFactory.createHorizonLayout(horizonWidgets);
		range1.setId("range1");
		return range1;
	}

	/**
	 * @枚举类型控件
	 * @param action
	 * @param point
	 * @return
	 */
	public static HorizontalLayout createEnum(String action, DevProductFunction point) {
		List<WidgetVo> horizonWidgets = new ArrayList<WidgetVo>();
		horizonWidgets.add(new WidgetVo("", "<span style='color:red;'>*</span>枚举范围:", "html").setWeight(93)
				.setStyle("padding-left:25px"));
		List<Validate> textValid = new ArrayList<Validate>();
		textValid.add(Validate.required().setRequiredtext("枚举不能为空"));
		textValid.add(Validate.pattern("/^[.\\w\\u4e00-\\u9fa5]+(,[.\\w\\u4e00-\\u9fa5]+)*$/")
				.setPatterntext("枚举范围由中文,英文组成且以逗号隔开"));
		horizonWidgets.add(new WidgetVo("enumeration", "<span style='color:red;'>*</span> ", "text").setValid(textValid)
				.setWidth("28.6%").setWidgetValueVo(new WidgetValueVo().setValue(point.getEnumeration())));
		HorizontalLayout range1 = (HorizontalLayout) BatchFactory.createHorizonLayout(horizonWidgets);
		range1.setId("range1");
		return range1;
	}

	/**
	 * @字符类型控件
	 * @param action
	 * @param point
	 * @return
	 */
	public static HorizontalLayout createString(String action, DevProductFunction point) {
		List<WidgetVo> horizonWidgets = new ArrayList<WidgetVo>();
		horizonWidgets.add(new WidgetVo("", "<span style='color:red;'>*</span>数据长度:", "html").setWeight(93)
				.setStyle("padding-left:25px"));
		List<Validate> textValid = new ArrayList<Validate>();
		textValid.add(Validate.required().setRequiredtext("数据长度不能为空"));
		textValid.add(Validate.pattern("/^\\d+$/").setPatterntext("长度必须是自然数"));
		horizonWidgets.add(new WidgetVo("length", "<span style='color:red;'>*</span> ", "text").setValid(textValid)
				.setWidth("28.6%").setWidgetValueVo(new WidgetValueVo().setValue(point.getLength())));
		HorizontalLayout range1 = (HorizontalLayout) BatchFactory.createHorizonLayout(horizonWidgets);
		range1.setId("range1");
		return range1;
	}

	/**
	 * @设备控制控件构造函数
	 * @param point
	 * @return
	 */
	public static List<List<WidgetVo>> pointControl(List<DevProductFunction> point) {
		List<List<WidgetVo>> totalList = new ArrayList<List<WidgetVo>>();
		for (int i = 0; i < point.size(); i++) {
			// 数值类型
			if (DataTypeEnum.NUMBER.getValue().equals(point.get(i).getDataType())) {
				List<WidgetVo> widgets = new ArrayList<WidgetVo>();
				double d = 0;
				if (DeviceControlService.getJsonData(point.get(i).getValue()) instanceof Double) {
					d = (double) DeviceControlService.getJsonData(point.get(i).getValue());
				}
				if (DeviceControlService.getJsonData(point.get(i).getValue()) instanceof Integer) {
					d = Double.parseDouble(DeviceControlService.getJsonData(point.get(i).getValue()).toString());
				}
				List<Validate> spinValid = new ArrayList<Validate>();
				spinValid.add(Validate.pattern("/^-?\\d+\\.?\\d{0,2}$/").setPatterntext("数值最多保留两位"));
				if ((FunctionTypeEnum.SENSOR.getValue().equals(point.get(i).getFunctionType())) != true
						&& (FunctionTypeEnum.TROUBLE.getValue().equals(point.get(i).getFunctionType())) != true) {
					List<WidgetVo> horizonWidgets2 = new ArrayList<WidgetVo>();
					horizonWidgets2.add(new WidgetVo("", "", "label")
							.setWidgetValueVo(new WidgetValueVo().setValue("数值设置——" + point.get(i).getFunctionName())));
					horizonWidgets2.add(new WidgetVo("", "", "html").setWeight(20));
					horizonWidgets2.add(new WidgetVo(point.get(i).getId() + "end", "", "label")
							.setWidgetValueVo(new WidgetValueVo().setValue("")));
					HorizontalLayout bt2 = (HorizontalLayout) BatchFactory.createHorizonLayout(horizonWidgets2);
					widgets.add(new WidgetVo("", "", "hor").setHor(bt2));

					List<WidgetVo> horizonWidgets = new ArrayList<WidgetVo>();
					horizonWidgets.add(new WidgetVo("", "", "label").setWeight(50).setStyle("margin-top:5px")
							.setWidgetValueVo(new WidgetValueVo().setValue(+point.get(i).getMinValue())));
					List<EventVo> spineventVo = new ArrayList<EventVo>();
					spineventVo.add(new EventVo().setClickEvent(Spinner.EVENT_CHANGE)
							.setJsCommand("VM(this).cmd('formula',''+VM(this).f('" + point.get(i).getId()
									+ "').val()+'','" + point.get(i).getId() + "')"));
					WidgetVo resolution = new WidgetVo(point.get(i).getId(), "", "spinner").setValid(spinValid)
							.setEventVo(spineventVo).setWeight(120);
					resolution
							.setWidgetValueVo(new WidgetValueVo(point.get(i).getMinValue(), point.get(i).getMaxValue(),
									point.get(i).getValue() == null ? null : d, point.get(i).getResolution()));
					horizonWidgets.add(resolution);
					horizonWidgets.add(new WidgetVo("", "", "html").setWeight(20));
					horizonWidgets.add(new WidgetVo("", "", "label").setWeight(50).setStyle("margin-top:5px")
							.setWidgetValueVo(new WidgetValueVo().setValue(point.get(i).getMaxValue())));
					HorizontalLayout bt = (HorizontalLayout) BatchFactory.createHorizonLayout(horizonWidgets);
					widgets.add(new WidgetVo("", "", "hor").setHor(bt));
					totalList.add(widgets);
				} else {
					List<WidgetVo> horizonWidgets2 = new ArrayList<WidgetVo>();
					horizonWidgets2.add(new WidgetVo("", "", "label")
							.setWidgetValueVo(new WidgetValueVo().setValue("数值设置——" + point.get(i).getFunctionName())));
					horizonWidgets2.add(new WidgetVo("", "", "html").setWeight(20));
					horizonWidgets2.add(new WidgetVo(point.get(i).getId() + "end", "", "label")
							.setWidgetValueVo(new WidgetValueVo().setValue("")));
					HorizontalLayout bt2 = (HorizontalLayout) BatchFactory.createHorizonLayout(horizonWidgets2);
					widgets.add(new WidgetVo("", "", "hor").setHor(bt2));

					List<WidgetVo> horizonWidgets = new ArrayList<WidgetVo>();
					horizonWidgets.add(new WidgetVo("", "", "label").setWeight(50).setStyle("margin-top:5px")
							.setWidgetValueVo(new WidgetValueVo().setValue(+point.get(i).getMinValue())));
					WidgetVo resolution = new WidgetVo(point.get(i).getId(), "", "spinner").setWeight(120);
					resolution.setReadOnly(true)
							.setWidgetValueVo(new WidgetValueVo(point.get(i).getMinValue(), point.get(i).getMaxValue(),
									point.get(i).getValue() == null ? null : d, point.get(i).getResolution()));
					horizonWidgets.add(resolution);
					horizonWidgets.add(new WidgetVo("", "", "html").setWeight(20));
					horizonWidgets.add(new WidgetVo("", "", "label").setWeight(50).setStyle("margin-top:5px")
							.setWidgetValueVo(new WidgetValueVo().setValue(point.get(i).getMaxValue())));
					HorizontalLayout bt = (HorizontalLayout) BatchFactory.createHorizonLayout(horizonWidgets);
					widgets.add(new WidgetVo("", "", "hor").setHor(bt));
					totalList.add(widgets);
				}
			}
		}
		for (int i = 0; i < point.size(); i++) {
			// 字符类型
			if (DataTypeEnum.STRING.getValue().equals(point.get(i).getDataType())) {
				List<WidgetVo> widgets = new ArrayList<WidgetVo>();
				String d = null;
				if (DeviceControlService.getJsonData(point.get(i).getValue()) instanceof String) {
					d = DeviceControlService.getJsonData(point.get(i).getValue()).toString();
				}
				// 不为传感器或故障

				if ((FunctionTypeEnum.SENSOR.getValue().equals(point.get(i).getFunctionType())) != true
						&& (FunctionTypeEnum.TROUBLE.getValue().equals(point.get(i).getFunctionType())) != true) {
					widgets.add(new WidgetVo("", "", "label")
							.setWidgetValueVo(new WidgetValueVo().setValue("字符设置——" + point.get(i).getFunctionName())));
					List<Validate> textValid = new ArrayList<Validate>();
					textValid.add(Validate.maxlength(point.get(i).getLength()).setMaxlengthtext("字符串长度过大"));
					widgets.add(new WidgetVo(point.get(i).getId(), "", "text").setValid(textValid).setWeight(200)
							.setWidgetValueVo(new WidgetValueVo().setValue(d)));
					totalList.add(widgets);
				} else {
					widgets.add(new WidgetVo("", "", "label")
							.setWidgetValueVo(new WidgetValueVo().setValue("字符设置——" + point.get(i).getFunctionName())));
					widgets.add(new WidgetVo("", "", "text").setWeight(200).setReadOnly(true)
							.setWidgetValueVo(new WidgetValueVo().setValue(d)));
					totalList.add(widgets);
				}
			}
		}
		for (int i = 0; i < point.size(); i++) {
			// 布尔类型
			if (DataTypeEnum.BOOLEAN.getValue().equals(point.get(i).getDataType())) {
				List<WidgetVo> widgets = new ArrayList<WidgetVo>();
				boolean d = false;
				if (DeviceControlService.getJsonData(point.get(i).getValue()) instanceof Boolean) {
					d = (boolean) DeviceControlService.getJsonData(point.get(i).getValue());
				}
				// 不为传感器或故障
				if ((FunctionTypeEnum.SENSOR.getValue().equals(point.get(i).getFunctionType())) != true
						&& (FunctionTypeEnum.TROUBLE.getValue().equals(point.get(i).getFunctionType())) != true) {
					widgets.add(new WidgetVo("", "", "label").setWidgetValueVo(
							new WidgetValueVo().setValue("开启/关闭——" + point.get(i).getFunctionName())));
					List<WidgetVo> horizonWidgets = new ArrayList<WidgetVo>();
					if (d) {
						List<EventVo> boxeventVo = new ArrayList<EventVo>();
						boxeventVo.add(new EventVo().setClickEvent(Checkbox.EVENT_CLICK)
								.setJsCommand("VM(this).cmd('changeopen','" + point.get(i).getSerialNo()
										+ "',''+VM(this).find('" + point.get(i).getId() + "').isChecked()+'')"));
						horizonWidgets.add(new WidgetVo(point.get(i).getId(), "", "checkbox").setSelect(true)
								.setEventVo(boxeventVo));
						horizonWidgets.add(new WidgetVo(point.get(i).getSerialNo().toString(), "", "label")
								.setStyle("margin-top:5px").setWidgetValueVo(new WidgetValueVo().setValue("开启")));
						HorizontalLayout bt = (HorizontalLayout) BatchFactory.createHorizonLayout(horizonWidgets);
						widgets.add(new WidgetVo("", "", "hor").setHor(bt).setWeight(219));
						totalList.add(widgets);
					} else {
						List<EventVo> boxeventVo = new ArrayList<EventVo>();
						boxeventVo.add(new EventVo().setClickEvent(Checkbox.EVENT_CLICK)
								.setJsCommand("VM(this).cmd('changeopen','" + point.get(i).getSerialNo()
										+ "',''+VM(this).find('" + point.get(i).getId() + "').isChecked()+'')"));
						horizonWidgets.add(new WidgetVo(point.get(i).getId(), "", "checkbox").setSelect(false)
								.setEventVo(boxeventVo));
						horizonWidgets.add(new WidgetVo(point.get(i).getSerialNo().toString(), "", "label")
								.setStyle("margin-top:5px").setWidgetValueVo(new WidgetValueVo().setValue("关闭")));
						HorizontalLayout bt = (HorizontalLayout) BatchFactory.createHorizonLayout(horizonWidgets);
						widgets.add(new WidgetVo("", "", "hor").setHor(bt).setWeight(219));
						totalList.add(widgets);
					}
				} else {
					widgets.add(new WidgetVo("", "", "label").setWidgetValueVo(
							new WidgetValueVo().setValue("开启/关闭——" + point.get(i).getFunctionName())));
					List<WidgetVo> horizonWidgets = new ArrayList<WidgetVo>();
					if (d) {
						horizonWidgets.add(
								new WidgetVo(point.get(i).getId(), "", "checkbox").setSelect(true).setReadOnly(true));
						horizonWidgets.add(new WidgetVo(point.get(i).getSerialNo().toString(), "", "label")
								.setStyle("margin-top:5px;").setWidgetValueVo(new WidgetValueVo().setValue("开启")));
						HorizontalLayout bt = (HorizontalLayout) BatchFactory.createHorizonLayout(horizonWidgets);
						widgets.add(new WidgetVo("", "", "hor").setHor(bt).setWeight(219));
						totalList.add(widgets);
					} else {
						horizonWidgets.add(
								new WidgetVo(point.get(i).getId(), "", "checkbox").setSelect(false).setReadOnly(true));
						horizonWidgets.add(new WidgetVo(point.get(i).getSerialNo().toString(), "", "label")
								.setStyle("margin-top:5px;").setWidgetValueVo(new WidgetValueVo().setValue("关闭")));
						HorizontalLayout bt = (HorizontalLayout) BatchFactory.createHorizonLayout(horizonWidgets);
						widgets.add(new WidgetVo("", "", "hor").setHor(bt).setStyle("color:#CCC").setWeight(219));
						totalList.add(widgets);
					}
				}
			}
		}
		for (int i = 0; i < point.size(); i++) {
			// 枚举类型
			if (DataTypeEnum.ENUM.getValue().equals(point.get(i).getDataType())) {
				List<WidgetVo> widgets = new ArrayList<WidgetVo>();
				Object d = null;
				if (DeviceControlService.getJsonData(point.get(i).getValue()) instanceof String) {
					d = DeviceControlService.getJsonData(point.get(i).getValue()).toString();
				}
				if (DeviceControlService.getJsonData(point.get(i).getValue()) instanceof Integer) {
					d = DeviceControlService.getJsonData(point.get(i).getValue());
				}
				// 不为传感器或故障
				if ((FunctionTypeEnum.SENSOR.getValue().equals(point.get(i).getFunctionType())) != true
						&& (FunctionTypeEnum.TROUBLE.getValue().equals(point.get(i).getFunctionType())) != true) {
					String[] StrArray = point.get(i).getEnumeration().split(",");
					widgets.add(new WidgetVo("", "", "label")
							.setWidgetValueVo(new WidgetValueVo().setValue("枚举设置——" + point.get(i).getFunctionName())));
					List<WidgetVo> horizonWidgets = new ArrayList<WidgetVo>();
					if (StrArray.length < 10) {
						for (int j = 0; j < StrArray.length; j++) {
							if (StrArray[j].equals(d)) {
								horizonWidgets.add(new WidgetVo(point.get(i).getId(), "", "radio")
										.setWidgetValueVo(new WidgetValueVo().setValue(StrArray[j])).setSelect(true));
							} else {
								horizonWidgets.add(new WidgetVo(point.get(i).getId(), "", "radio")
										.setWidgetValueVo(new WidgetValueVo().setValue(StrArray[j])));
							}
						}
					} else {
						Object[] s1 = new Object[StrArray.length];
						
						for (int j = 0;j< StrArray.length;j++) {
							Object[] obj = new Object[2];
							obj[0] = StrArray[j];
							obj[1] = StrArray[j];
							s1[j] = obj;			
						}
						List<Object> sList1 = Arrays.asList(s1);
						if (!Assert.isEmpty(d)) {
							horizonWidgets.add(new WidgetVo(point.get(i).getId(), "", "select")
									.setWidth("50%").setWidgetValueVo(new WidgetValueVo().setValue(d)).setOthers(sList1));
						} else {
							horizonWidgets.add(new WidgetVo(point.get(i).getId(), "", "select").setWidth("50%").setOthers(sList1));
						}
					}
					HorizontalLayout bt = (HorizontalLayout) BatchFactory.createHorizonLayout(horizonWidgets);
					widgets.add(new WidgetVo("", "", "hor").setHor(bt));
					totalList.add(widgets);
				} else {
					String[] StrArray = point.get(i).getEnumeration().split(",");
					widgets.add(new WidgetVo("", "", "label")
							.setWidgetValueVo(new WidgetValueVo().setValue("枚举设置——" + point.get(i).getFunctionName())));
					List<WidgetVo> horizonWidgets = new ArrayList<WidgetVo>();
					if (StrArray.length < 10) {
						for (int j = 0; j < StrArray.length; j++) {
							if (StrArray[j].equals(d)) {
								horizonWidgets.add(new WidgetVo(point.get(i).getId(), "", "radio")
										.setWidgetValueVo(new WidgetValueVo().setValue(StrArray[j])).setReadOnly(true)
										.setSelect(true));
							} else {
								horizonWidgets.add(new WidgetVo(point.get(i).getId(), "", "radio")
										.setWidgetValueVo(new WidgetValueVo().setValue(StrArray[j])).setReadOnly(true));
							}
						}
					} else {
						Object[] s1 = new Object[StrArray.length];
						int j = 0;
						for (String e : StrArray) {
							Object[] obj = new Object[2];
							obj[0] = e;
							obj[1] = e;
							s1[j] = obj;
							j++;
						}
						List<Object> sList1 = Arrays.asList(s1);
						if (!Assert.isEmpty(d)) {
							horizonWidgets.add(new WidgetVo(point.get(i).getId(), "", "select")
									.setWidth("50%").setWidgetValueVo(new WidgetValueVo().setValue(d)).setReadOnly(true).setOthers(sList1));
						} else {
							horizonWidgets.add(new WidgetVo(point.get(i).getId(), "", "select").setWidth("50%").setReadOnly(true).setOthers(sList1));
						}
					}
					HorizontalLayout bt = (HorizontalLayout) BatchFactory.createHorizonLayout(horizonWidgets);
					widgets.add(new WidgetVo("", "", "hor").setHor(bt).setStyle("color:#CCC"));
					totalList.add(widgets);
				}
			}
		}
		return totalList;
	}

	/**
	 * @功能点查看控件构造函数
	 * @param action
	 * @param point
	 * @param devProduct
	 * @return
	 */
	public static List<WidgetVo> showPoint(String action, DevProductFunction point, DevProduct devProduct) {
		List<WidgetVo> widgets = new ArrayList<WidgetVo>();
		String dataType = null;
		String functionType = null;
		for (DataTypeEnum e : DataTypeEnum.values()) {
			if (e.getValue().equals(point.getDataType())) {
				dataType = e.getDataTypeName();
			}
		}
		for (FunctionTypeEnum e : FunctionTypeEnum.values()) {
			if (e.getValue().equals(point.getFunctionType())) {
				functionType = e.getFunctionTypeName();
			}
		}
		if ("show".equals(action)) {
			widgets.add(new WidgetVo("", "显示名称:", "label")
					.setWidgetValueVo(new WidgetValueVo().setValue(point.getFunctionName())));
			widgets.add(new WidgetVo("", "标识名称:", "label")
					.setWidgetValueVo(new WidgetValueVo().setValue(point.getFunctionCode())));
			widgets.add(
					new WidgetVo("", "功能类型:", "label").setWidgetValueVo(new WidgetValueVo().setValue(functionType)));
			widgets.add(new WidgetVo("", "数据类型:", "label").setWidgetValueVo(new WidgetValueVo().setValue(dataType)));
			if (point.getDataType().equals(DataTypeEnum.NUMBER.getValue())) {
				widgets.add(new WidgetVo("", "数据范围:", "label").setWidgetValueVo(
						new WidgetValueVo().setValue("" + point.getMinValue() + "~" + point.getMaxValue() + "")));
				widgets.add(new WidgetVo("", "增量:", "label")
						.setWidgetValueVo(new WidgetValueVo().setValue((0 - point.getMinValue()))));
				widgets.add(new WidgetVo("", "分辨率:", "label")
						.setWidgetValueVo(new WidgetValueVo().setValue(point.getResolution())));
			}
			if (point.getDataType().equals(DataTypeEnum.ENUM.getValue())) {
				widgets.add(new WidgetVo("", "枚举范围:", "label")
						.setWidgetValueVo(new WidgetValueVo().setValue(point.getEnumeration())));
			}
			if (point.getDataType().equals(DataTypeEnum.STRING.getValue())) {
				widgets.add(new WidgetVo("", "数据长度:", "label")
						.setWidgetValueVo(new WidgetValueVo().setValue(point.getLength())));
			}
			widgets.add(new WidgetVo("", "编号:", "label")
					.setWidgetValueVo(new WidgetValueVo().setValue(point.getSerialNo())));
			widgets.add(
					new WidgetVo("", "备注:", "label").setWidgetValueVo(new WidgetValueVo().setValue(point.getRemark())));
		} else {
			widgets.add(new WidgetVo("", "显示名称:", "label")
					.setWidgetValueVo(new WidgetValueVo().setValue(point.getFunctionName())));
			widgets.add(new WidgetVo("", "标识名称:", "label")
					.setWidgetValueVo(new WidgetValueVo().setValue(point.getFunctionCode())));
			widgets.add(
					new WidgetVo("", "功能类型:", "label").setWidgetValueVo(new WidgetValueVo().setValue(functionType)));
			widgets.add(new WidgetVo("", "数据类型:", "label").setWidgetValueVo(new WidgetValueVo().setValue(dataType)));
			if (point.getDataType().equals(DataTypeEnum.NUMBER.getValue())) {
				widgets.add(new WidgetVo("", "数据范围:", "label").setWidgetValueVo(
						new WidgetValueVo().setValue("" + point.getMinValue() + "~" + point.getMaxValue() + "")));
				widgets.add(new WidgetVo("", "增量:", "label")
						.setWidgetValueVo(new WidgetValueVo().setValue((0 - point.getMinValue()))));
				widgets.add(new WidgetVo("", "分辨率:", "label")
						.setWidgetValueVo(new WidgetValueVo().setValue(point.getResolution())));
			}
			if (point.getDataType().equals(DataTypeEnum.ENUM.getValue())) {
				widgets.add(new WidgetVo("", "枚举范围:", "label")
						.setWidgetValueVo(new WidgetValueVo().setValue(point.getEnumeration())));
			}
			if (point.getDataType().equals(DataTypeEnum.STRING.getValue())) {
				widgets.add(new WidgetVo("", "数据长度:", "label")
						.setWidgetValueVo(new WidgetValueVo().setValue(point.getLength())));
			}
			widgets.add(new WidgetVo("", "编号:", "label")
					.setWidgetValueVo(new WidgetValueVo().setValue(point.getSerialNo())));
			String x = null;
			if (point.getRemark() != null) {
				if (point.getRemark().length() > 11) {
					x = point.getRemark().substring(0, 11) + "......";
				} else {
					x = point.getRemark();
				}
			}
			widgets.add(new WidgetVo("", "备注:", "label").setWidgetValueVo(new WidgetValueVo().setValue(x)));
			List<WidgetVo> horizonWidgets = new ArrayList<WidgetVo>();
			if (ProductStatusEnum.PUBLISHED.getIndex().equals(devProduct.getStatus())) {
				// 判断功能点创建时间与产品发布时间，功能点状态
				boolean pointStatus = false;
				if (point.getCreateTime() != null && devProduct.getReleaseTime() != null) {
					pointStatus = point.getCreateTime().before(devProduct.getReleaseTime());
					pointStatus = FunctionStatusEnum.PUBLISHED.getIndex().equals(point.getStatus());
				}
				if (pointStatus) {
					List<EventVo> showBtneventVo = new ArrayList<EventVo>();
					showBtneventVo.add(new EventVo().setClickEvent(Button.EVENT_CLICK)
							.setJsCommand("VM(this).cmd('toShow','" + point.getId() + "')"));
					horizonWidgets.add(
							new WidgetVo("", "查看", "button").setCls("x-btn").setWeight(80).setEventVo(showBtneventVo));
				} else {
					List<EventVo> modifyBtneventVo = new ArrayList<EventVo>();
					modifyBtneventVo.add(new EventVo().setClickEvent(Button.EVENT_CLICK)
							.setJsCommand("VM(this).cmd('toUpdate','" + point.getId() + "')"));
					List<EventVo> deleteBtneventVo = new ArrayList<EventVo>();
					deleteBtneventVo.add(new EventVo().setClickEvent(Button.EVENT_CLICK)
							.setJsCommand("VM(this).cmd('toDelete','" + point.getId() + "')"));
					horizonWidgets.add(new WidgetVo("", "修改", "button").setCls("x-btn x-btn-main").setWeight(80)
							.setEventVo(modifyBtneventVo));
					horizonWidgets.add(new WidgetVo("", "", "html").setWeight(15));
					horizonWidgets.add(new WidgetVo("", "删除", "button").setCls("x-btn").setWeight(80)
							.setEventVo(deleteBtneventVo));
				}
			} else {
				List<EventVo> modifyBtneventVo = new ArrayList<EventVo>();
				modifyBtneventVo.add(new EventVo().setClickEvent(Button.EVENT_CLICK)
						.setJsCommand("VM(this).cmd('toUpdate','" + point.getId() + "')"));
				List<EventVo> deleteBtneventVo = new ArrayList<EventVo>();
				deleteBtneventVo.add(new EventVo().setClickEvent(Button.EVENT_CLICK)
						.setJsCommand("VM(this).cmd('toDelete','" + point.getId() + "')"));
				horizonWidgets.add(new WidgetVo("", "修改", "button").setCls("x-btn x-btn-main").setWeight(80)
						.setEventVo(modifyBtneventVo));
				horizonWidgets.add(new WidgetVo("", "", "html").setWeight(15));
				horizonWidgets.add(
						new WidgetVo("", "删除", "button").setCls("x-btn").setWeight(80).setEventVo(deleteBtneventVo));
			}
			HorizontalLayout bt = (HorizontalLayout) BatchFactory.createHorizonLayout(horizonWidgets);
			widgets.add(new WidgetVo("", "", "hor").setHor(bt));
		}
		return widgets;
	}
}
