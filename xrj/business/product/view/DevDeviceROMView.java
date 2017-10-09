package com.xrj.business.product.view;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.github.abel533.echarts.Legend;
import com.github.abel533.echarts.Title;
import com.github.abel533.echarts.Tooltip;
import com.github.abel533.echarts.axis.Axis;
import com.github.abel533.echarts.axis.CategoryAxis;
import com.github.abel533.echarts.code.AxisType;
import com.github.abel533.echarts.code.SeriesType;
import com.github.abel533.echarts.code.Trigger;
import com.github.abel533.echarts.json.GsonOption;
import com.github.abel533.echarts.series.Bar;
import com.github.abel533.echarts.series.MarkLine;
import com.github.abel533.echarts.series.MarkPoint;
import com.github.abel533.echarts.series.Series;
import com.rongji.dfish.base.Page;
import com.rongji.dfish.framework.FilterParam;
import com.rongji.dfish.framework.plugin.file.ui.DefaultUploadFile;
import com.rongji.dfish.ui.Valignable;
import com.rongji.dfish.ui.command.JSCommand;
import com.rongji.dfish.ui.command.SubmitCommand;
import com.rongji.dfish.ui.form.Checkbox;
import com.rongji.dfish.ui.form.Radio;
import com.rongji.dfish.ui.form.Text;
import com.rongji.dfish.ui.form.Validate;
import com.rongji.dfish.ui.helper.FormPanel;
import com.rongji.dfish.ui.helper.GridPanel;
import com.rongji.dfish.ui.helper.Label;
import com.rongji.dfish.ui.layout.GridLayout;
import com.rongji.dfish.ui.layout.HorizontalLayout;
import com.rongji.dfish.ui.layout.VerticalLayout;
import com.rongji.dfish.ui.layout.View;
import com.rongji.dfish.ui.layout.grid.GridColumn;
import com.rongji.dfish.ui.layout.grid.Tr;
import com.rongji.dfish.ui.plugin.echarts.EChartsPanel;
import com.rongji.dfish.ui.widget.Button;
import com.rongji.dfish.ui.widget.Html;
import com.rongji.dfish.ui.widget.PageBar;
import com.rongji.dfish.ui.widget.SubmitButton;
import com.xrj.api.dto.DevDeviceRom;
import com.xrj.api.dto.DevHardwareVersion;
import com.xrj.business.core.view.PubView;

/**
 * 
 * @author chenjunchao
 *
 */
public class DevDeviceROMView {
      
	/**
	 * @ROM主界面
	 * @param request
	 * @param data 
	 * @param devDeviceRom2 
	 * @return
	 */
	  public static View showROM(HttpServletRequest request, List<Object> data) {
		  View view = new View();
		  view.addCommand("devShowROM",PubView.getAjaxCommand("product/devShowROM?ROMId=$0&name=$1"));
		  view.addCommand("devDeleteROM", PubView.getAjaxCommand("product/toDeleteROM?ROMId=$0"));
		  view.addCommand("download", new JSCommand("$.download('fileService/downloadFile?fileId='+VM(this).find('hiddenText').val())"));		 
		  view.addCommand("back", PubView.getAjaxCommand("product/backtoROM"));
		  view.addCommand("createROM", PubView.getAjaxCommand("product/createROM"));
		  view.addCommand("import",new JSCommand("var bidArr = Q('input[name = boxid]:checked'); var bids='';"
							+"if (bidArr&&bidArr.length) {"
							+"for (var i = 0;i<bidArr.length;i++) {"
						    +"bids += (bids ? ',':'')+bidArr[i].value;}}"
							+"else {VM(this).cmd({type:'alert',text:'硬件版本未选中'});return null;}"
						   // +"VM(this).cmd({type:'confirm',text:'确认创建?',"
							+"VM(this).cmd{type:'ajax',src:'product/saveROM?uploadFile='+VM(this).f('uploadFile').val()+'&softversion='+VM(this).f('softVersion').val()+'&name='+VM(this).f('versionName').val()+'&hardId='+bids}});"));		
		  view.addCommand("selectfile",PubView.getAjaxCommand("product/selectfile"));
		  view.addCommand("saveROM", new SubmitCommand("product/saveROM").setRange("form"));
		  view.addCommand("addHard", PubView.getAjaxCommand("product/devhardwareversion/addHard"));
		  view.addCommand("replaceHard", PubView.getAjaxCommand("product/replaceHard"));
		  view.addCommand("versionStatistics", PubView.getAjaxCommand("product/versionStatistics"));
		  
		  VerticalLayout verticalLayout = new VerticalLayout("ver");
		  //
		  Page dfishPage = (Page) data.get(1);
		  FilterParam fp = (FilterParam) data.get(2);			
		  List<DevDeviceRom> devDeviceRom = (List<DevDeviceRom>) data.get(0);
		  //
			List<Object[]> gridData = new ArrayList<Object[]>();
			for (DevDeviceRom version : devDeviceRom) {
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < version.getHardVersion().size(); i++) {				
					sb.append(version.getHardVersion().get(i)+",");
				}
				int x = sb.length();
				gridData.add(new Object[]{version.getId(),version.getVersionName(),version.getSoftVersion(),sb.substring(0, x-1)});
			}
			//
		  HorizontalLayout mid = new HorizontalLayout("mid").setHeight(40);
		  mid.add(new Html("<h3>固件管理(OTA)</h3>"));
		  mid.add(new Button("创建新固件").setCls("x-btn x-btn-main").setWidth(100).setOn(Button.EVENT_CLICK, "VM(this).cmd('createROM')"));
		  mid.add(new Html("").setWidth(10));
		  mid.add(new Button("版本统计").setOn(Button.EVENT_CLICK, "VM(this).cmd('versionStatistics')").setCls("x-btn").setWidth(100));
		  mid.add(new Html("").setWidth(20));
		  mid.setValign(Valignable.VALIGN_MIDDLE);
		  
		  GridPanel gridPanel = new GridPanel("gridPanel");
		  gridPanel.setHeight("76%").setWidth("100%");
		  gridPanel.setWmin(20).setCls("x-grid-odd").setFace(GridPanel.FACE_LINE)
		   .setScroll(true).setNobr(false).setFocusable(true);
		  gridPanel.getPrototype(false).getThead().setCls("x-grid-head");
		  gridPanel.setPub(new Tr().setHeight("40").setCls("w-td-line w-th"));
		  
		  gridPanel.setGridData(gridData);
		  int columnIndex = 0;
		  gridPanel.addColumn(GridColumn.hidden(columnIndex++, "Id"));
		  gridPanel.addColumn(GridColumn.text(columnIndex++, "verName","固件名称", "-1"));
		  gridPanel.addColumn(GridColumn.text(columnIndex++, "softVersion","固件版本号", "-1"));
		  gridPanel.addColumn(GridColumn.text(columnIndex++, "hardVersion","兼容设备版本", "-1"));	  
		  gridPanel.addColumn(GridColumn.text(0, "foper" ,"操作", "-1").setFormat(buildROMBtn()));
		  
		  HorizontalLayout pageba = new HorizontalLayout("pageba");
		  PageBar pageBar = new PageBar("bar",PageBar.TYPE_TEXT).setCls("x-pagebar").setJump(true).setBtncount(5).setAlign(PageBar.ALIGN_RIGHT);
		  pageBar.setCurrentpage(dfishPage.getCurrentPage());
		  pageBar.setSumpage(dfishPage.getPageCount());
		  pageBar.setSrc("product/ROMsearch?cp=$0" + fp);
		  pageba.add(pageBar);
			  
		  verticalLayout.add(mid);
		  verticalLayout.add(new Html("<hr style='filter:progid:DXImageTransform.Microsoft.Shadow(color:#CCC,direction:145,strength:15)' color='#CCC' width='100%' size='1' />").setHeight(1),"-1");
		  verticalLayout.add(gridPanel);
		  verticalLayout.add(pageba);
		  view.add(verticalLayout);  	
		  return view;
	  }
	  /**
	   * @查看某一ROM
	   * @param request
	   * @param devHardwareVersion
	   * @param rom 
	   * @return
	   */
	  public static View showOneROM(HttpServletRequest request, List<DevHardwareVersion> devHardwareVersion, DevDeviceRom rom) {
		  View view = new View();
		  VerticalLayout verticalLayout = new VerticalLayout("ver");
		  
		  HorizontalLayout top = new HorizontalLayout("top").setHeight(40);
		  top.add(new Html("<h3>固件管理(OTA):查看</h3>"));
		  top.add(new Button("下载固件文件").setStyle("margin-top:5px").setCls("x-btn x-btn-main").setWidth(120).setOn(Button.EVENT_CLICK, "VM(this).cmd('download','"+rom.getAttachmentPath()+"')"));
		  top.add(new Html("").setWidth(20));
		  top.add(new Button("返回").setStyle("margin-top:5px").setCls("x-btn").setWidth(80).setOn(Button.EVENT_CLICK, "VM(this).cmd('back')"));
		  top.add(new Html("").setWidth(20));
		  
		  StringBuffer ss = new StringBuffer();
		  String [] n = rom.getAttachmentPath().split("/");
		  String str= rom.getAttachmentPath().substring( rom.getAttachmentPath().lastIndexOf("."));
		  ss.append(n[n.length-2]);
		  ss.append(str);
		  StringBuffer sb = new StringBuffer();
		  for (int i = 0; i < devHardwareVersion.size(); i++) {
			  sb.append(devHardwareVersion.get(i).getVersionName() + ",");
		  }
		  int x = sb.length();
		  
		  GridLayout grid=new GridLayout("grid");
		  grid.setHeight("76%").setWidth("100%");
		  grid.setWmin(20).setCls("x-grid-odd").setFace(GridPanel.FACE_LINE)
			   .setScroll(true).setNobr(false).setFocusable(true);
		  grid.getThead().setCls("x-grid-head");
		  grid.setPub(new Tr().setHeight("40").setCls("w-td-line w-th"));
		
		  grid.add(0, 0, new Label("","固件名称"));  
		  grid.add(0, 2, new Label("",rom.getVersionName()));
		  grid.add(1, 0, new Label("","固件文件"));  	 
		  grid.add(1, 3, new Text("","",rom.getAttachmentPath()).setId("hiddenText").setStyle("display:none"));
		  grid.add(1, 2, new Label("",ss.toString()));
		  grid.add(2, 0, new Label("","固件版本号"));  
		  grid.add(2, 2, new Label("",rom.getSoftVersion()));
		  grid.add(3, 0, new Label("","兼容设备版本"));  
		  grid.add(3, 2, new Label("",sb.substring(0,x-1)));
		  		  
		  verticalLayout.add(top);
		  verticalLayout.add(new Html("<hr style='filter:progid:DXImageTransform.Microsoft.Shadow(color:#CCC,direction:145,strength:15)' color='#CCC' width='100%' size='1' />").setHeight(1),"-1");
		  //verticalLayout.add(mid);
		  verticalLayout.add(grid);
		  view.add(verticalLayout);
		  return view;
	  }
	  /**
	   * 新建ROM
	   * @param request
	   * @param devHardwareVersion 
	   * @param devHardwareVersion
	   * @return
	   */
	  public static View createROM(HttpServletRequest request, List<DevHardwareVersion> devHardwareVersion) {
		  View view = new View("ctr");
		  VerticalLayout verticalLayout = new VerticalLayout("ver");
		  
		  HorizontalLayout top = new HorizontalLayout("top").setHeight(40);
		  top.add(new Html("<h3>固件管理(OTA):添加新固件</h3>"));
		  //top.add(new Button("新增设备版本").setStyle("margin-top:5px").setCls("x-btn x-btn-main").setOn(Button.EVENT_CLICK, "VM(this).cmd('addHard')").setWidth(100));
		 // top.add(new Html("").setWidth(20));
		 
		  Html labelData = new Html("<span style='color:red;'>*</span>固件名称:");
		  Html labelData4 = new Html("<span style='color:red;'>*</span>固件文件:");
		  Html labelData3 = new Html("<span style='color:red;'>*</span>兼容设备版本:");	
		  Html labelData2 = new Html("<span style='color:red;'>*</span>固件版本号:");
		  Html labelData5 = new Html("<span style='color:red;'>*</span>是否解压:");
		  		 
		  GridPanel gridPanel = new GridPanel("gridpan").setHeight(180).setScroll(true);	 
		  gridPanel.setGridData(devHardwareVersion);	 
		  gridPanel.addColumn(GridColumn.checkbox("id", "bid", "30", "boxid", "", false));
		  gridPanel.addColumn(GridColumn.text("versionName","hardver","版本名称","50").setAlign(GridColumn.ALIGN_CENTER));		 
		  
		  HorizontalLayout btm = new HorizontalLayout("btm").setWidth(200).setStyle("display:table;margin:0 auto");
		  btm.add(new Button("保存").setCls("x-btn x-btn-main").setWidth(80).setOn(SubmitButton.EVENT_CLICK,
					" var bidArr = Q('input[name = boxid]:checked'); var bids='';"
							+"if (bidArr&&bidArr.length) {"
							+"for (var i = 0;i<bidArr.length;i++) {"
						    +"bids += (bids ? ',':'')+bidArr[i].value;}}"
							+"else {VM(this).cmd({type:'alert',text:'设备版本未选中'});return null;}"
						   // +"VM(this).cmd({type:'confirm',text:'确认创建?',"
							+"VM(this).cmd({type:'submit',range:'form',src:'product/saveROM?&hardId='+bids});"));
		  btm.add(new Html("").setWidth(20));
		  btm.add(new Button("取消").setCls("x-btn").setWidth(80).setOn(Button.EVENT_CLICK, "VM(this).cmd('back')"));
		  HorizontalLayout compress = new HorizontalLayout("compress");
		  compress.add(new Radio("deCompress",null, false, "1", "是"));
		  compress.add(new Radio("deCompress",null, true, "0", "否"));
		  
		  FormPanel form = new FormPanel("form");
		  GridLayout grid=new GridLayout("grid");
		  grid.setHeight("76%").setWidth("100%");
		  grid.setWmin(20).setCls("x-grid-odd").setFace(GridPanel.FACE_LINE)
			   .setScroll(true).setNobr(false).setFocusable(true);
		  grid.getThead().setCls("x-grid-head");
		  grid.setPub(new Tr().setHeight("40").setCls("w-td-line w-th"));
		
		  grid.add(0, 0, labelData);  
		  grid.add(0, 2, new Text("versionName","","").addValidate(Validate.required().setRequiredtext("名称不能为空")));
		  grid.add(1, 3, new Text("","","").setId("hidText").setStyle("display:none"));
		  grid.add(1, 0, labelData2);  	 
		  grid.add(1, 2, new Text("softVersion","","").addValidate(Validate.pattern("/^\\d{1,2}(\\.\\d{1,2}){0,2}$/").setPatterntext("格式有误")).addValidate(Validate.required().setRequiredtext("固件版本号不能为空")));
		  grid.add(2, 0, labelData4);  
		  grid.add(2, 2, new DefaultUploadFile("uploadFile", "附件").setId("uploadFile").setFile_types("*.bin,*.zip,*.rar").setUpload_url("fileService/romUpload").setRequired(true).setFile_size_limit("50M").setFile_upload_limit(1));
		  grid.add(3, 0, labelData3);  
		  grid.add(3, 2, gridPanel);
		  grid.add(3,3,new Button("新增设备版本").setStyle("margin-top:5px").setCls("x-btn x-btn-main").setOn(Button.EVENT_CLICK, "VM(this).cmd('addHard')").setWidth(100));
		  grid.add(4, 1, 4, 2, btm);
		  form.add(grid);
		  
		  verticalLayout.add(top);
		  verticalLayout.add(new Html("<hr style='filter:progid:DXImageTransform.Microsoft.Shadow(color:#CCC,direction:145,strength:15)' color='#CCC' width='100%' size='1' />").setHeight(1),"-1");
		  //verticalLayout.add(new Html("").setHeight(5));
		  verticalLayout.add(form);
		  view.add(verticalLayout);
		  return view;
	  }
	  
	  public static View versionStatistics() {
		  View view = new View();
		  VerticalLayout verticalLayout = new VerticalLayout("ver");
		  EChartsPanel eChart = new EChartsPanel("eChart");
		  GsonOption gsonOption = new GsonOption();
		  
		  Title title = new Title();
		  title.setText("某地区蒸发量和降水量");
		  title.setSubtext("纯属虚构");
		  Tooltip tooltip = new Tooltip();	 
		  tooltip.setTrigger(Trigger.axis);
		  Legend legend = new Legend();
		  List<Object> list = new ArrayList<Object>();
		  list.add("蒸发量");
		  list.add("降水量");
		  legend.setData(list);
		  List<Axis> xAxis = new ArrayList<Axis>();	 	 
		  Axis axis = new CategoryAxis()  ;
		  axis.setType(AxisType.category);
		  List<Object> data = new ArrayList<Object>();
		  data.add("1月");data.add("2月");data.add("3月");data.add("4月");data.add("5月");data.add("6月");data.add("7月");data.add("8月");data.add("9月");data.add("10月");data.add("11月");data.add("12月");
		  axis.setData(data);
		  xAxis.add(axis);
		  List<Axis> yAxis = new ArrayList<Axis>();	 
		  Axis yxis = new CategoryAxis()  ;
		  yxis.setType(AxisType.value);
		  yAxis.add(yxis);
		  List<Series> series = new ArrayList<Series>();	
		  Series ser1 = new Bar();
		  Series ser2 = new Bar();
		  ser1.setName("蒸发量");
		  ser1.setType(SeriesType.bar);
		  List<Object> datas = new ArrayList<Object>();
		  datas.add("2.0");datas.add("4.9");datas.add("7.0");datas.add("23.2");datas.add("27.6");datas.add("70.5");datas.add("100.5");datas.add("150.5");datas.add("100.5");datas.add("72.5");datas.add("50.5");datas.add("15.5");
		  ser1.setData(datas);
		  MarkPoint markPoint = new MarkPoint();
		  List<Object> pointdatas = new ArrayList<Object>();	
		  String s1 =  "{type:'max',name:'最大值'}";
		  String s2 =  "{type:'min',name:'最小值'}";
		  String s3 =  "{type:'average',name:'平均值'}";
		  pointdatas.add(s1);
		  pointdatas.add(s2);
		  markPoint.setData(pointdatas);
		  ser1.setMarkPoint(markPoint);
		  MarkLine markLine = new MarkLine();
		  List<Object> markdatas = new ArrayList<Object>();
		  markdatas.add(s3);
		  markLine.setData(markdatas);
		  ser1.setMarkLine(markLine);
		  
		  ser2.setName("降水量");
		  ser2.setType(SeriesType.bar);
		  List<Object> datas2 = new ArrayList<Object>();
		  datas2.add("2.0");datas2.add("5.9");datas2.add("14.0");datas2.add("53.2");datas2.add("27.6");datas2.add("70.5");datas2.add("107.5");datas2.add("180.5");datas2.add("152.5");datas2.add("52.5");datas2.add("40.5");datas2.add("15.5");
		  ser2.setData(datas2);
		  MarkPoint markPoint2 = new MarkPoint();
		  List<Object> pointdatas2 = new ArrayList<Object>();	
		  String s4 =  "{name:'年最高',value:182.2,xAxis:7,yAxis:183,symbolSize:18}";
		  String s5 =  "{name:'年最低',value:2.3,xAxis:11,yAxis:3}";
		  String s6 =  "{type:'average',name:'平均值'}";
		  pointdatas2.add(s4);
		  pointdatas2.add(s5);
		  markPoint2.setData(pointdatas2);
		  ser2.setMarkPoint(markPoint2);
		  MarkLine markLine2 = new MarkLine();
		  List<Object> markdatas2 = new ArrayList<Object>();
		  markdatas2.add(s6);
		  markLine2.setData(markdatas2);
		  ser2.setMarkLine(markLine2);
		  series.add(ser1);
		  series.add(ser2);
		  
		  gsonOption.setTitle(title);
		  gsonOption.setTooltip(tooltip);
		  gsonOption.setLegend(legend);
		  gsonOption.setCalculable(true);
		  gsonOption.setxAxis(xAxis);
		  gsonOption.setyAxis(yAxis);
		  gsonOption.setSeries(series);
		  eChart.setOption(gsonOption);
		  verticalLayout.add(eChart);
		  view.add(verticalLayout);
		  return view;
	  }
	  
	  public static View changeHard(List<DevHardwareVersion> devHardwareVersion) {
		  View view = new View();		
		  GridPanel gridPanel = new GridPanel("gridpan").setHeight(180);	 
		  gridPanel.setGridData(devHardwareVersion);		
		  gridPanel.addColumn(GridColumn.checkbox("id", "bid", "30", "boxid", "", false));	
		  gridPanel.addColumn(GridColumn.text("versionName","hardver","版本名称","50").setAlign(GridColumn.ALIGN_CENTER));	
		  view.add(gridPanel);
		  return view;
	  }
	  
	  private static String buildROMBtn() {
			StringBuffer sb = new StringBuffer();
			sb.append("<i class='x-grid-view' title='查看' onclick=\"VM(this).cmd('devShowROM','$Id','$verName');\"></i> ");
			sb.append("&nbsp;&nbsp;<i class='x-grid-del' title='删除' onclick=\"VM(this).cmd('devDeleteROM','$Id');\"></i>");
			return sb.toString();
		}
}
