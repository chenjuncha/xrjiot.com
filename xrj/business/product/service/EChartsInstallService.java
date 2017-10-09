package com.xrj.business.product.service;

import java.util.ArrayList;
import java.util.List;

import com.github.abel533.echarts.Label;
import com.github.abel533.echarts.LabelLine;
import com.github.abel533.echarts.Legend;
import com.github.abel533.echarts.Title;
import com.github.abel533.echarts.Tooltip;
import com.github.abel533.echarts.code.Orient;
import com.github.abel533.echarts.code.Position;
import com.github.abel533.echarts.code.SeriesType;
import com.github.abel533.echarts.code.Trigger;
import com.github.abel533.echarts.json.GsonOption;
import com.github.abel533.echarts.series.Bar;
import com.github.abel533.echarts.series.Series;
import com.github.abel533.echarts.style.AreaStyle;
import com.github.abel533.echarts.style.ItemStyle;
import com.github.abel533.echarts.style.TextStyle;
import com.github.abel533.echarts.style.itemstyle.Emphasis;
import com.github.abel533.echarts.style.itemstyle.Normal;
import com.rongji.dfish.ui.plugin.echarts.EChartsPanel;

import net.sf.json.JSONObject;

/**
 * @Dare 2017-9-19
 * @author chenjunchao
 *
 */
public class EChartsInstallService {

	public static EChartsPanel overViewChart(){
		 EChartsPanel eChart = new EChartsPanel("eChart");
		 GsonOption gsonOption = new GsonOption();
		  
		  Title title = new Title();
		  title.setText("新增上线数与活跃设备数");
		  title.setSubtext("仅供参考");
		  Tooltip tooltip = new Tooltip();	 
		  tooltip.setTrigger(Trigger.axis);
		  Legend legend = new Legend();
		  List<Object> list = new ArrayList<Object>();
		  list.add("新增上线数");
		  list.add("活跃设备数");
		  legend.setData(list);
		  	  	  
		  List<Series> series = new ArrayList<Series>();			  
		  List<Object> datas = new ArrayList<Object>();
		  datas.add("2");datas.add("9");datas.add("7");datas.add("23");datas.add("27");datas.add("70");datas.add("100");datas.add("150");datas.add("100");datas.add("72");datas.add("50");datas.add("15");		 	  
		  List<Object> datas2 = new ArrayList<Object>();
		  datas2.add("2");datas2.add("5");datas2.add("14");datas2.add("53");datas2.add("27");datas2.add("70");datas2.add("107");datas2.add("180");datas2.add("152");datas2.add("52");datas2.add("40");datas2.add("15");	  
		  series.add(EChartsService.createSeries("新增上线数", SeriesType.line, datas));
		  series.add(EChartsService.createSeries("活跃设备数", SeriesType.line, datas2));
		  
		  gsonOption.setTitle(title);
		  gsonOption.setTooltip(tooltip);
		  gsonOption.setToolbox(EChartsService.createToolbox(""));
		  gsonOption.setLegend(legend);
		  gsonOption.setCalculable(true);
		  gsonOption.setxAxis(EChartsService.createxAxis());
		  gsonOption.setyAxis(EChartsService.createyAxis("tai"));
		  gsonOption.setSeries(series);
		  eChart.setOption(gsonOption);
		 return eChart;
	}
	
	public static EChartsPanel newOnlineViewChart() {
		 EChartsPanel eChart = new EChartsPanel("eChart");
		 GsonOption gsonOption = new GsonOption();
		  
		  Title title = new Title();
		  title.setText("新上线设备数");
		  title.setSubtext("仅供参考");
		  Tooltip tooltip = new Tooltip();	 
		  tooltip.setTrigger(Trigger.axis);
		  Legend legend = new Legend();
		  List<Object> list = new ArrayList<Object>();
		  list.add("新上线设备");
		  legend.setData(list);
		  
		  List<Series> series = new ArrayList<Series>();	
		  List<Object> datas2 = new ArrayList<Object>();
		  datas2.add("2");datas2.add("5");datas2.add("14");datas2.add("53");datas2.add("27");datas2.add("70");datas2.add("107");datas2.add("180");datas2.add("152");datas2.add("52");datas2.add("40");datas2.add("15");	  
		  series.add(EChartsService.createSeries("新上线设备", SeriesType.line, datas2));
		  
		  gsonOption.setTitle(title);
		  gsonOption.setTooltip(tooltip);
		  gsonOption.setLegend(legend);
		  gsonOption.setCalculable(true);
		  gsonOption.setSeries(series);
		  gsonOption.setxAxis(EChartsService.createxAxis());
		  gsonOption.setyAxis(EChartsService.createyAxis("tai"));
		  gsonOption.setToolbox(EChartsService.createToolbox(""));
		  eChart.setOption(gsonOption);
		 return eChart;
	}
	
	public static EChartsPanel activeDeviceViewChart() {
		 EChartsPanel eChart = new EChartsPanel("eChart");
		 GsonOption gsonOption = new GsonOption();
		  
		  Title title = new Title();
		  title.setText("活跃设备数");
		  title.setSubtext("仅供参考");
		  Tooltip tooltip = new Tooltip();	 
		  tooltip.setTrigger(Trigger.axis);
		  Legend legend = new Legend();
		  List<Object> list = new ArrayList<Object>();
		  list.add("活跃设备");
		  list.add("日活跃率");
		  legend.setData(list);
		  
		  List<Series> series = new ArrayList<Series>();	
		  List<Object> datas2 = new ArrayList<Object>();
		  datas2.add("9");datas2.add("5");datas2.add("14");datas2.add("53");datas2.add("27");datas2.add("70");datas2.add("85");datas2.add("10");datas2.add("15");datas2.add("52");datas2.add("40");datas2.add("15");	  
		  List<Object> datas = new ArrayList<Object>();
		  datas.add("2");datas.add("9");datas.add("7");datas.add("23");datas.add("27");datas.add("70");datas.add("100");datas.add("150");datas.add("100");datas.add("72");datas.add("50");datas.add("15");		 	  
		  series.add(EChartsService.createSeries("活跃设备", SeriesType.line, datas));
		  series.add(EChartsService.createSeries("日活跃率", SeriesType.line, datas2));
		  
		  gsonOption.setTitle(title);
		  gsonOption.setTooltip(tooltip);
		  gsonOption.setLegend(legend);
		  gsonOption.setCalculable(true);
		  gsonOption.setSeries(series);
		  gsonOption.setxAxis(EChartsService.createxAxis());
		  gsonOption.setyAxis(EChartsService.createyAxis(""));
		  gsonOption.setToolbox(EChartsService.createToolbox(""));
		  eChart.setOption(gsonOption);
		 return eChart;
	}
	
	public static EChartsPanel activePeriodViewChart(){
		 EChartsPanel eChart = new EChartsPanel("eChart");
		 GsonOption gsonOption = new GsonOption();
		 
		 Title title = new Title();
		  title.setText("活跃周期");
		  title.setSubtext("仅供参考");
		  Tooltip tooltip = new Tooltip();	 
		  tooltip.setTrigger(Trigger.axis);
		  Legend legend = new Legend();
		  List<Object> list = new ArrayList<Object>();
		  list.add("活跃周期");
		  legend.setData(list);
		 
		  List<Series> series = new ArrayList<Series>();	
		  List<Object> datas2 = new ArrayList<Object>();
		  datas2.add("9");datas2.add("5");datas2.add("14");datas2.add("53");datas2.add("27");;	  		 	  
		  Series ser1 = EChartsService.createSeries("活跃周期", SeriesType.line, datas2);
		  ItemStyle itemStyle = new ItemStyle();
		  Normal normal = new Normal();
		  AreaStyle areaStyle = new AreaStyle();
		  areaStyle.setType("default");
		  normal.areaStyle(areaStyle);
		  itemStyle.normal(normal);
		  ser1.setItemStyle(itemStyle);
		  series.add(ser1);
		  
		  gsonOption.setTitle(title);
		  gsonOption.setTooltip(tooltip);
		  gsonOption.setLegend(legend);
		  gsonOption.setCalculable(true);
		  gsonOption.setSeries(series);
		  List<Object> data = new ArrayList<Object>();
		  data.add("一周以上");
		  data.add("一个月以上");
		  data.add("三个月以上");
		  data.add("半年以上");
		  data.add("一年以上");
		  gsonOption.setxAxis(EChartsService.createPeriodxAxis(data));
		  gsonOption.setyAxis(EChartsService.createyAxis("baifenbi"));
		  gsonOption.setToolbox(EChartsService.createToolbox(""));
		 eChart.setOption(gsonOption);
		 return eChart;
	}
	
	public static EChartsPanel linkTimeViewChart(){	
		EChartsPanel eChart = new EChartsPanel("eChart3");	
		 eChart.setOn(EChartsPanel.EVENT_READY, "var option = {title : {text: '在线时长分布',subtext: '纯属虚构'},"
		 		+ "tooltip : {trigger: 'axis'},legend: {data:['在线时长百分比']},"
		 		+ "toolbox: {show : true,feature : {mark : {show: true},dataView : {show: true, readOnly: false},"
		 		+ "magicType : {show: true, type: ['line', 'bar']},restore : {show: true},saveAsImage : {show: true}}},"
		 		+ "calculable : true,xAxis : [{type : 'category',boundaryGap : false,data : ['0:00-1:00','5:00-6:00','10:00-11:00','15:00-16:00','00:00-21:00']}],"
		 		+ "yAxis : [{type : 'value',axisLabel :{formatter: '{value} %'}}],series : [{ name:'所占比例',type:'line', markPoint : {data : [ {type : 'max', name: '最大值'},{type : 'min', name: '最小值'}]},"
		 		+ "markLine : {data : [{type : 'average', name: '平均值'}]},smooth:true,itemStyle: {normal: {areaStyle: {type: 'default'}}},"
		 		+ "data:[9, 5, 14, 53, 27],},]};this.init( option )");
		 return eChart;	
	}
	
	public static EChartsPanel onelinkTimeViewChart(){		
		EChartsPanel eChart = new EChartsPanel("eChart2");	
		 eChart.setOn(EChartsPanel.EVENT_READY, "var option = {tooltip : {trigger: 'item',formatter: '{a} <br/>{b} : {c} ({d}%)'},"
		 		+ "legend: {orient : 'vertical', x : 'left', data:['0-1小时','1-8小时','8-24小时','1-5天','5-30天','30天以上']},"
		 		+ "toolbox: {show : true,feature :{mark : {show: true},"
		 		+ "dataView : {show: true, readOnly: false},"
		 		+ "magicType : {show: true,type: ['pie', 'funnel'],option: {funnel: {x: '25%',width: '50%',funnelAlign: 'center',max: 1548}}},"
		 		+ "restore : {show: true},saveAsImage : {show: true}}}, calculable : true,    "
		 		+ "series : [{name:'在线时长',type:'pie',radius : ['50%', '70%'],itemStyle : {normal : {label : {show : false},"
		 		+ "labelLine : {show : false}},emphasis : {label : { show : true,position : 'center',textStyle : {fontSize : '30',fontWeight : 'bold'}}}},"
		 		+ "data:[{value:335, name:'0-1小时'},{value:310, name:'1-8小时'},{value:234, name:'8-24小时'},{value:135, name:'1-5天'},{value:1548, name:'5-30天'},{value:108, name:'30天以上'}]}]};"
		 		+ "this.init( option )");		 
		 return eChart;
	}
	
	public static EChartsPanel globe (){
		 EChartsPanel eChart = new EChartsPanel("eChart3");	
		 eChart.setOn(EChartsPanel.EVENT_READY, "var option = {   title : {        text: '某楼盘销售情况',       subtext: '纯属虚构'   }, tooltip : {        trigger: 'axis'    },    legend: {        data:['意向','预购','成交']   },   toolbox: {         show : true,         feature : {             mark : {show: true},             dataView : {show: true, readOnly: false},             magicType : {show: true, type: ['line', 'bar', 'stack', 'tiled']},             restore : {show: true},             saveAsImage : {show: true}         }     },     calculable : true,     xAxis : [         {             type : 'category',             boundaryGap : false,             data : ['周一','周二','周三','周四','周五','周六','周日']         }     ],     yAxis : [         {             type : 'value'         }     ],     series : [         {             name:'成交',             type:'line',             smooth:true,             itemStyle: {normal: {areaStyle: {type: 'default'}}},             data:[10, 12, 21, 54, 260, 830, 710]         },         {             name:'预购',             type:'line',             smooth:true,             itemStyle: {normal: {areaStyle: {type: 'default'}}},             data:[30, 182, 434, 791, 390, 30, 10]         },         {             name:'意向',             type:'line',             smooth:true,             itemStyle: {normal: {areaStyle: {type: 'default'}}},             data:[1320, 1132, 601, 234, 120, 90, 20]         }     ] };this.init( option )");
		 return eChart;
		 
	}
}
