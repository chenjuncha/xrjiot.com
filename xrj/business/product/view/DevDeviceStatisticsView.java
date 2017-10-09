package com.xrj.business.product.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.abel533.echarts.Legend;
import com.github.abel533.echarts.Title;
import com.github.abel533.echarts.Toolbox;
import com.github.abel533.echarts.Tooltip;
import com.github.abel533.echarts.axis.Axis;
import com.github.abel533.echarts.axis.CategoryAxis;
import com.github.abel533.echarts.code.AxisType;
import com.github.abel533.echarts.code.SeriesType;
import com.github.abel533.echarts.code.Trigger;
import com.github.abel533.echarts.feature.Feature;
import com.github.abel533.echarts.json.GsonOption;
import com.github.abel533.echarts.series.Bar;
import com.github.abel533.echarts.series.MarkLine;
import com.github.abel533.echarts.series.MarkPoint;
import com.github.abel533.echarts.series.Series;
import com.github.abel533.echarts.style.ItemStyle;
import com.github.abel533.echarts.style.LineStyle;
import com.rongji.dfish.ui.command.JSCommand;
import com.rongji.dfish.ui.helper.FormPanel;
import com.rongji.dfish.ui.helper.GridPanel;
import com.rongji.dfish.ui.layout.GridLayout;
import com.rongji.dfish.ui.layout.HorizontalLayout;
import com.rongji.dfish.ui.layout.VerticalLayout;
import com.rongji.dfish.ui.layout.View;
import com.rongji.dfish.ui.plugin.echarts.EChartsPanel;
import com.rongji.dfish.ui.widget.Html;
import com.xrj.api.dto.DevProduct;
import com.xrj.business.product.service.EChartsInstallService;
import com.xrj.business.product.service.EChartsService;

import net.sf.json.JSONObject;

/**
 * @Date 2017-9-18
 * @author chenjunchao
 *
 */
public class DevDeviceStatisticsView {
	
	public static View overView() {
		View view = new View();
		VerticalLayout verticalLayout = new VerticalLayout("statistVer").setScroll(true);
		
		verticalLayout.add(new Html("<h3>概览</h3>").setHeight(40));
		verticalLayout.add(new Html("<hr style='filter:progid:DXImageTransform.Microsoft.Shadow(color:#CCC,direction:145,strength:15)' color='#CCC' width='100%' size='1' />").setHeight(1),"-1");
		HorizontalLayout topHor = new HorizontalLayout("topHor").setWidth(1100).setAlign(HorizontalLayout.ALIGN_CENTER).setStyle("display:table;margin:0 auto;font-family:'微软雅黑'");	
		FormPanel formPanel1 = new FormPanel("form1").setScroll(false).setWidth(240).setHeight(170).setStyle("border:1px solid #CCC");
		FormPanel formPanel2 = new FormPanel("form2").setScroll(false).setWidth(240).setHeight(170).setStyle("border-top:1px solid #CCC;border-bottom:1px solid #CCC");
		FormPanel formPanel3 = new FormPanel("form3").setScroll(false).setWidth(240).setHeight(170).setStyle("border:1px solid #CCC");
		FormPanel formPanel4 = new FormPanel("form4").setScroll(false).setWidth(240).setHeight(170).setStyle("border-top:1px solid #CCC;border-bottom:1px solid #CCC;border-right:1px solid #CCC");
		formPanel1.add(new Html("<div style='font-size:15px;'>在线设备</div><p style='font-size:70px;display:table;margin:0 auto;'>0</p>"));
		formPanel2.add(new Html("<div style='font-size:15px;'>今日新增上线</div><p style='font-size:70px;display:table;margin:0 auto;'>0</p>"));
		formPanel3.add(new Html("<div style='font-size:15px;'>昨日活跃</div><p style='font-size:70px;display:table;margin:0 auto;'>0</p>"));
		formPanel4.add(new Html("<div style='font-size:15px;'>累计上线</div><p style='font-size:70px;display:table;margin:0 auto;'>0</p>"));
		topHor.add(formPanel1);
		topHor.add(formPanel2);
		topHor.add(formPanel3);
		topHor.add(formPanel4);
		
		VerticalLayout verBot = new VerticalLayout("verBot").setHeight("100%").setWidth(960).setStyle("border:1px solid #CCC;display:table;margin:0 auto");
		HorizontalLayout botHor = new HorizontalLayout("botHor").setHeight(35).setStyle(" background-color:#EEE");
		botHor.add(new Html("<div style='font-weight:bold;font-size:15px;margin-top:5px'>趋势分析</div>"));
		verBot.add(botHor);
		EChartsPanel eChart = EChartsInstallService.overViewChart();
		verBot.add(eChart);
		
		verticalLayout.add(topHor);
		verticalLayout.add(new Html("").setHeight(20));
		verticalLayout.add(verBot);
		view.add(verticalLayout);
		return view;
	}	
	
	public static View newOnlieView() {
		View view = new View();
		VerticalLayout verticalLayout = new VerticalLayout("statistVer").setScroll(true);
		verticalLayout.add(new Html("<h3>新增上线</h3>").setHeight(40));
		verticalLayout.add(new Html("<hr style='filter:progid:DXImageTransform.Microsoft.Shadow(color:#CCC,direction:145,strength:15)' color='#CCC' width='100%' size='1' />").setHeight(1),"-1");
			
		HorizontalLayout topHor = new HorizontalLayout("topHor").setWidth(960).setHeight(80).setStyle("display:table;margin:0 auto;font-family:'微软雅黑'");
		GridLayout grid=new GridLayout("grid").setStyle("border:1px solid #CCC");
		grid.add(0, 0, new Html("今日新增").setStyle("font-size:15px;border-bottom:1px solid #CCC").setAlign(Html.ALIGN_CENTER));
		grid.add(0, 1, new Html("前一天新增").setStyle("font-size:15px;border-bottom:1px solid #CCC").setAlign(Html.ALIGN_CENTER));
		grid.add(0, 2, new Html("近7天新增").setStyle("font-size:15px;border-bottom:1px solid #CCC").setAlign(Html.ALIGN_CENTER));
		grid.add(0, 3, new Html("近30天新增").setStyle("font-size:15px;border-bottom:1px solid #CCC").setAlign(Html.ALIGN_CENTER));
		grid.add(1, 0, new Html("0").setStyle("font-size:20px").setAlign(Html.ALIGN_CENTER));
		grid.add(1, 1, new Html("0").setStyle("font-size:20px").setAlign(Html.ALIGN_CENTER));
		grid.add(1, 2, new Html("0").setStyle("font-size:20px").setAlign(Html.ALIGN_CENTER));
		grid.add(1, 3, new Html("0").setStyle("font-size:20px").setAlign(Html.ALIGN_CENTER));
		topHor.add(grid);
		
		VerticalLayout verBot = new VerticalLayout("verBot").setHeight("100%").setWidth(960).setStyle("border:1px solid #CCC;display:table;margin:0 auto");
		HorizontalLayout botHor = new HorizontalLayout("botHor").setHeight(35).setStyle(" background-color:#EEE");
		botHor.add(new Html("<div style='font-weight:bold;font-size:15px;margin-top:5px'>新上线设备趋势分析(近30天)</div>"));
		verBot.add(botHor);
		EChartsPanel eChart = EChartsInstallService.newOnlineViewChart();
		verBot.add(eChart);
		
		verticalLayout.add(topHor);
		verticalLayout.add(new Html("").setHeight(20));
		verticalLayout.add(verBot);
		view.add(verticalLayout);
		return view;
	}
	
	public static View activeDeviceView() {
		View view = new View();
		VerticalLayout verticalLayout = new VerticalLayout("statistVer").setScroll(true);
		verticalLayout.add(new Html("<h3>活跃设备</h3>").setHeight(40));
		verticalLayout.add(new Html("<hr style='filter:progid:DXImageTransform.Microsoft.Shadow(color:#CCC,direction:145,strength:15)' color='#CCC' width='100%' size='1' />").setHeight(1),"-1");
			
		HorizontalLayout topHor = new HorizontalLayout("topHor").setWidth(960).setHeight(80).setStyle("display:table;margin:0 auto;font-family:'微软雅黑'");
		GridLayout grid=new GridLayout("grid").setStyle("border:1px solid #CCC");
		grid.add(0, 0, new Html("累计上线数").setStyle("font-size:15px;border-bottom:1px solid #CCC").setAlign(Html.ALIGN_CENTER));
		grid.add(0, 1, new Html("昨日活跃").setStyle("font-size:15px;border-bottom:1px solid #CCC").setAlign(Html.ALIGN_CENTER));
		grid.add(0, 2, new Html("日活跃率").setStyle("font-size:15px;border-bottom:1px solid #CCC").setAlign(Html.ALIGN_CENTER));
		grid.add(0, 3, new Html("近7天活跃").setStyle("font-size:15px;border-bottom:1px solid #CCC").setAlign(Html.ALIGN_CENTER));
		grid.add(0, 4, new Html("7天活跃率").setStyle("font-size:15px;border-bottom:1px solid #CCC").setAlign(Html.ALIGN_CENTER));
		grid.add(1, 0, new Html("0").setStyle("font-size:20px").setAlign(Html.ALIGN_CENTER));
		grid.add(1, 1, new Html("0").setStyle("font-size:20px").setAlign(Html.ALIGN_CENTER));
		grid.add(1, 2, new Html("0%").setStyle("font-size:20px").setAlign(Html.ALIGN_CENTER));
		grid.add(1, 3, new Html("0").setStyle("font-size:20px").setAlign(Html.ALIGN_CENTER));
		grid.add(1, 4, new Html("0%").setStyle("font-size:20px").setAlign(Html.ALIGN_CENTER));
		topHor.add(grid);
		
		VerticalLayout verBot = new VerticalLayout("verBot").setHeight("100%").setWidth(960).setStyle("border:1px solid #CCC;display:table;margin:0 auto");
		HorizontalLayout botHor = new HorizontalLayout("botHor").setHeight(35).setStyle(" background-color:#EEE");
		botHor.add(new Html("<div style='font-weight:bold;font-size:15px;margin-top:5px'>趋势分析(近30天)</div>"));
		verBot.add(botHor);
		EChartsPanel eChart = EChartsInstallService.activeDeviceViewChart();
		verBot.add(eChart);
		
		verticalLayout.add(topHor);
		verticalLayout.add(new Html("").setHeight(20));
		verticalLayout.add(verBot);
		view.add(verticalLayout);
		return view;
	}
	
	public static View activePeriodView() {
		View view = new View();
		VerticalLayout verticalLayout = new VerticalLayout("statistVer").setScroll(true);
		
		verticalLayout.add(new Html("<h3>活跃周期</h3>").setHeight(40));
		verticalLayout.add(new Html("<hr style='filter:progid:DXImageTransform.Microsoft.Shadow(color:#CCC,direction:145,strength:15)' color='#CCC' width='100%' size='1' />").setHeight(1),"-1");
	    
		VerticalLayout verBot = new VerticalLayout("verBot").setHeight("80%").setWidth(960).setStyle("border:1px solid #CCC;display:table;margin:0 auto");
		HorizontalLayout botHor = new HorizontalLayout("botHor").setHeight(35).setStyle(" background-color:#EEE");
		botHor.add(new Html("<div style='font-weight:bold;font-size:15px;margin-top:5px'>活跃周期分布</div>"));
		verBot.add(botHor);
		EChartsPanel eChart = EChartsInstallService.activePeriodViewChart();
		verBot.add(eChart);
		
		verticalLayout.add(verBot);
		view.add(verticalLayout);
		return view;
	}
	
	public static View linkTimeView() {
		View view = new View();		
		VerticalLayout verticalLayout = new VerticalLayout("statistVer").setScroll(true);
		
		verticalLayout.add(new Html("<h3>连接时长</h3>").setHeight(40));
		verticalLayout.add(new Html("<hr style='filter:progid:DXImageTransform.Microsoft.Shadow(color:#CCC,direction:145,strength:15)' color='#CCC' width='100%' size='1' />").setHeight(1),"-1");
	    
		VerticalLayout verBot = new VerticalLayout("verBot").setHeight(400).setWidth(960).setStyle("border:1px solid #CCC;display:table;margin:0 auto");
		HorizontalLayout botHor = new HorizontalLayout("botHor").setHeight(35).setStyle(" background-color:#EEE");
		botHor.add(new Html("<div style='font-weight:bold;font-size:15px;margin-top:5px'>设备在线时段分布</div>"));
		verBot.add(botHor);
		EChartsPanel eChart = EChartsInstallService.linkTimeViewChart();
		verBot.add(eChart);
		
		VerticalLayout verBot2 = new VerticalLayout("verBot2").setHeight(400).setWidth(960).setStyle("border:1px solid #CCC;display:table;margin:0 auto");
		HorizontalLayout botHor2 = new HorizontalLayout("botHor2").setHeight(35).setStyle(" background-color:#EEE");
		botHor2.add(new Html("<div style='font-weight:bold;font-size:15px;margin-top:5px'>单次连续在线时长</div>"));
		verBot2.add(botHor2);
		EChartsPanel eChart2 = EChartsInstallService.onelinkTimeViewChart();
		verBot2.add(eChart2);
			
		verticalLayout.add(verBot);
		verticalLayout.add(new Html("").setHeight(20));
		verticalLayout.add(verBot2);
		view.add(verticalLayout);
		return view;
	}
}
