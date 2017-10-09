package com.xrj.business.product.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.github.abel533.echarts.Toolbox;
import com.github.abel533.echarts.axis.Axis;
import com.github.abel533.echarts.axis.AxisLabel;
import com.github.abel533.echarts.axis.CategoryAxis;
import com.github.abel533.echarts.code.AxisType;
import com.github.abel533.echarts.code.SeriesType;
import com.github.abel533.echarts.code.X;
import com.github.abel533.echarts.feature.Feature;
import com.github.abel533.echarts.feature.MagicType;
import com.github.abel533.echarts.feature.MagicType.Option;
import com.github.abel533.echarts.series.Bar;
import com.github.abel533.echarts.series.Funnel;
import com.github.abel533.echarts.series.MarkLine;
import com.github.abel533.echarts.series.MarkPoint;
import com.github.abel533.echarts.series.Series;
import com.github.abel533.echarts.style.ItemStyle;
import com.github.abel533.echarts.style.LineStyle;

import net.sf.json.JSONObject;

/**
 * @Date 2017-9-19
 * @author chenjunchao
 *
 */
public class EChartsService {

	public static final JSONObject MAX = new JSONObject();
	public static final JSONObject MIN = new JSONObject();
	public static final JSONObject AVERAGE = new JSONObject();
	static {
		MAX.put("type", "max");
		MAX.put("name", "最大值");
		MIN.put("type", "min");
		MIN.put("name", "最小值");
		AVERAGE.put("type", "average");
		AVERAGE.put("name", "平均值");
	}

	public static Toolbox createToolbox(String types) {
		Toolbox toolbox = new Toolbox();
		toolbox.setShow(true);
		List<Object> color = new ArrayList<Object>();
		color.add("#1e90ff");
		color.add("#22bb22");
		color.add("#4b0082");
		color.add("#d2691e");
		toolbox.setColor(color);
		Map<String, Feature> feature = new HashMap<String, Feature>();
		Feature mark = new Feature();
		mark.setShow(true);
		Feature dataView = new Feature();
		dataView.setShow(true);
		dataView.setReadOnly(false);
		Feature magicType = new Feature();
		magicType.setShow(true);
		List<Object> type = new ArrayList<Object>();
		if("pie".equals(types)) {
			type.add("pie");
			type.add("funnel");
			/*Option option = new Option();
			Funnel funnel = new Funnel();
			funnel.x("25%");
			funnel.width("50%");
			funnel.funnelAlign(X.left);
			funnel.max(1548);
			option.setFunnel(funnel);
			magicType.setOption(option);*/
		} else {
			type.add("line");
			type.add("bar");
		}
		magicType.setType(type);
		Feature restore = new Feature();
		restore.setShow(true);
		Feature saveAsImage = new Feature();
		saveAsImage.setShow(true);
		feature.put("mark", mark);
		feature.put("dataView", dataView);
		feature.put("magicType", magicType);
		feature.put("restore", restore);
		feature.put("saveAsImage", saveAsImage);
		toolbox.setFeature(feature);
		ItemStyle iconStyle = new ItemStyle();
		iconStyle.emphasis();
		toolbox.setIconStyle(iconStyle);
		return toolbox;
	}

	public static List<Axis> createxAxis() {
		List<Axis> xAxis = new ArrayList<Axis>();
		Axis axis = new CategoryAxis();
		axis.setType(AxisType.category);
		List<Object> data = new ArrayList<Object>();
		data.add("2017-09-01");
		data.add("2017-09-02");
		data.add("2017-09-03");
		data.add("2017-09-04");
		data.add("2017-09-05");
		data.add("2017-09-06");
		data.add("2017-09-07");
		data.add("2017-09-08");
		data.add("2017-09-09");
		data.add("2017-09-10");
		data.add("2017-09-11");
		data.add("2017-09-12");
		axis.setData(data);
		xAxis.add(axis);
		return xAxis;
	}
	
	public static List<Axis> createPeriodxAxis(List<Object> data) {
		List<Axis> xAxis = new ArrayList<Axis>();
		Axis axis = new CategoryAxis();
		axis.setType(AxisType.category);	
		axis.setData(data);
		axis.setBoundaryGap(false);
		xAxis.add(axis);
		return xAxis;
	}

	public static Series createSeries(String name, SeriesType type,List<Object> datas) {
		Series ser1 = new Bar();
		ser1.setName(name);
		ser1.setType(type);
		ser1.setData(datas);
		ser1.setMarkPoint(EChartsService.createPoint());
		ser1.setMarkLine(EChartsService.createLine());
		return ser1;
	}

	public static List<Axis> createyAxis(String unit) {
		List<Axis> yAxis = new ArrayList<Axis>();
		Axis yxis = new CategoryAxis();
		yxis.setType(AxisType.value);
		AxisLabel axisLabel = new AxisLabel();
		if("tai".equals(unit)) {
		    axisLabel.setFormatter("{value} 台");
		    yxis.setAxisLabel(axisLabel);
		}
		if("baifenbi".equals(unit)) {
		    axisLabel.setFormatter("{value} %");
		    yxis.setAxisLabel(axisLabel);
		}
		yAxis.add(yxis);
		return yAxis;
	}

	public static MarkPoint createPoint() {
		MarkPoint markPoint = new MarkPoint();
		List<Object> pointdatas = new ArrayList<Object>();
		pointdatas.add(EChartsService.MAX);
		pointdatas.add(EChartsService.MIN);
		markPoint.setData(pointdatas);
		return markPoint;
	}

	public static MarkLine createLine() {
		MarkLine markLine = new MarkLine();
		List<Object> markdatas = new ArrayList<Object>();
		markdatas.add(EChartsService.AVERAGE);
		markLine.setData(markdatas);
		return markLine;
	}
}
