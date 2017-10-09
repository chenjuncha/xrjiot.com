package com.xrj.business.product.view;
	
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.rongji.dfish.base.Page;
import com.rongji.dfish.framework.FilterParam;
import com.rongji.dfish.framework.FrameworkHelper;
import com.rongji.dfish.ui.JSFunction;
import com.rongji.dfish.ui.command.JSCommand;
import com.rongji.dfish.ui.command.SubmitCommand;
import com.rongji.dfish.ui.form.Checkbox;
import com.rongji.dfish.ui.form.Text;
import com.rongji.dfish.ui.form.Validate;
import com.rongji.dfish.ui.helper.FormPanel;
import com.rongji.dfish.ui.helper.GridPanel;
import com.rongji.dfish.ui.helper.Label;
import com.rongji.dfish.ui.layout.HorizontalLayout;
import com.rongji.dfish.ui.layout.VerticalLayout;
import com.rongji.dfish.ui.layout.View;
import com.rongji.dfish.ui.layout.grid.GridColumn;
import com.rongji.dfish.ui.layout.grid.Tr;
import com.rongji.dfish.ui.widget.Button;
import com.rongji.dfish.ui.widget.Html;
import com.rongji.dfish.ui.widget.PageBar;
import com.rongji.dfish.ui.widget.SubmitButton;
import com.xrj.api.dto.DevDevice;
import com.xrj.api.dto.DevDeviceRom;
import com.xrj.api.dto.DevProduct;
import com.xrj.api.dto.DevProductFunction;
import com.xrj.api.em.FunctionTypeEnum;
import com.xrj.business.util.PageConvertUtil;
import com.xrj.business.core.base.PubService;
import com.xrj.business.core.base.RemoteService;
import com.xrj.business.core.view.PubView;
import com.xrj.framework.util.HttpUtil;

/**
 * 
 * @author chenjunchao
 *
 */
public class DevDeviceView {
	
	 public static final String BOX_ID="boxId";
	/**
	 * @param request 
	 * @param request
	 * @param data 
	 * @param date 
	 * @throws ParseException 
	 * @显示所有设备
	 * 
	**/
	  public static View showAllDevice(HttpServletRequest request, List<Object> data) throws ParseException {
			View view = new View(); 
			view.addCommand("delete",PubView.getAjaxCommand("product/device/Delete?deviceId=$0"));
			view.addCommand("selectAll",new JSCommand("VM(this).f('boxId').check(true)"));
			view.addCommand("createOne",PubView.getAjaxCommand("product/device/createOne"));
			view.addCommand("createSome",new SubmitCommand("product/device/createSome").setRange("form"));
			view.addCommand("change",PubView.getAjaxCommand("product/device/changeData?page=$0"));
			view.addCommand("export",new JSCommand("$.download('product/device/export')"));
			VerticalLayout verticalLayout = new VerticalLayout("ver");
			//
			Page dfishPage = (Page) data.get(1);
			FilterParam fp = (FilterParam) data.get(2);					
			List<DevDevice> devDevice = (List<DevDevice>) data.get(0);
			//
			
			HorizontalLayout top = new HorizontalLayout("top").setHeight(40);
			top.add(new Html("<h3>设备标识管理</h3>"));
			top.add(new Button("全选").setCls("x-btn").setStyle("margin-top:5px").setWidth(80).setOn(Button.EVENT_CLICK, "VM(this).cmd('selectAll')")).setHeight(40);
			top.add(new Html("").setWidth(10));
			top.add(new Button("删除").setStyle("margin-top:5px").setCls("x-btn").setWidth(80).setOn(SubmitButton.EVENT_CLICK,
				" var bidArr = Q('input[name = "+BOX_ID+"]:checked'); var bids='';"
				+"if (bidArr&&bidArr.length) {"
				+"for (var i = 0;i<bidArr.length;i++) {"
			    +"bids += (bids ? ',':'')+bidArr[i].value;}}"
				+"else {VM(this).cmd({type:'alert',text:'未选中'});return null;}"
			    +"VM(this).cmd({type:'confirm',text:'确认删除?',"
				+"yes:{type:'ajax',src:'product/device/Delete?deviceId='+bids}});")).setHeight(40);
			top.add(new Html("").setWidth(50));
			top.add(new Button("导出").setStyle("margin-top:5px").setCls("x-btn x-btn-main").setWidth(80).setOn(Button.EVENT_CLICK, "VM(this).cmd('export')"));
			
			HorizontalLayout fo = new HorizontalLayout("fo").setScroll(false);
			FormPanel form = new FormPanel("form").setHeight(50);
		   // fo.add(new Label("","生成数量:"));
		    form.add(new Text("number","生成数量:","").setPlaceholder("1-10000").addValidate(Validate.maxvalue("10000").setMaxvaluetext("最大值不能超过10000")).addValidate(Validate.pattern("/^\\+?[1-9][0-9]*$/").setPatterntext("生成数量必须是正整数")).addValidate(Validate.required().setRequiredtext("数量不能为空")));
		   // form.add(fo);
			top.add(form);
			top.add(new Button("批量生成").setCls("x-btn").setWidth(100).setStyle("margin-top:5px").setOn(SubmitButton.EVENT_CLICK, "VM(this).cmd('createSome')"));
			top.add(new Html("").setWidth(15));
			top.add(new Button("生成单个").setCls("x-btn").setStyle("margin-top:5px").setWidth(100).setOn(Button.EVENT_CLICK, "VM(this).cmd('createOne')"));
			top.add(new Html("").setWidth(20));
			
			GridPanel grid=new GridPanel("grid");
			grid.setHeight("76%").setWidth("100%");
			grid.setWmin(20).setCls("x-grid-odd").setFace(GridPanel.FACE_LINE)
			   .setScroll(true).setNobr(false).setFocusable(true);
			grid.getPrototype(false).getThead().setCls("x-grid-head");
			grid.setPub(new Tr().setHeight("40").setCls("w-td-line w-th"));
			
			grid.setGridData(devDevice);			
			grid.addColumn(GridColumn.checkbox("Id", "bid", "40", BOX_ID, "", false));
			grid.addColumn(GridColumn.text("deviceSn","dId","设备唯一标识","25%"));
			grid.addColumn(GridColumn.text("deviceSecret","dSecret","设备证书","25%").setFormat("javascript:"+
			                                                                                   "var title=$dSecret.substr(0,4)+'************************'+$dSecret.substr(28,32);"+
					                                                                           "return '<label>'+title+'</label>'"+
			                                                                                   ""));		
			HorizontalLayout pageba = new HorizontalLayout("pageba");
			PageBar pageBar = new PageBar("bar",PageBar.TYPE_TEXT).setCls("x-pagebar").setJump(true).setBtncount(5).setAlign(PageBar.ALIGN_RIGHT);
			pageBar.setCurrentpage(dfishPage.getCurrentPage());
			pageBar.setSumpage(dfishPage.getPageCount());
			pageBar.setSrc("product/device/search?cp=$0" + fp);
			pageba.add(pageBar);
			
			verticalLayout.add(top);
			verticalLayout.add(new Html("<hr style='filter:progid:DXImageTransform.Microsoft.Shadow(color:#CCC,direction:145,strength:15)' color='#CCC' width='100%' size='1' />").setHeight(1),"-1");
			verticalLayout.add(grid);
			verticalLayout.add(pageba);
			view.add(verticalLayout);
			return view;
	  }
	  
}
