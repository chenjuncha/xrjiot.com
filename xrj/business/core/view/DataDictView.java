package com.xrj.business.core.view;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.CollectionUtils;

import com.rongji.dfish.framework.FrameworkHelper;
import com.rongji.dfish.ui.Widget;
import com.rongji.dfish.ui.command.AjaxCommand;
import com.rongji.dfish.ui.command.ConfirmCommand;
import com.rongji.dfish.ui.form.Select;
import com.rongji.dfish.ui.form.Text;
import com.rongji.dfish.ui.form.Textarea;
import com.rongji.dfish.ui.form.Xbox;
import com.rongji.dfish.ui.helper.FlexGrid;
import com.rongji.dfish.ui.layout.AbstractLayout;
import com.rongji.dfish.ui.layout.ButtonBar;
import com.rongji.dfish.ui.layout.HorizontalLayout;
import com.rongji.dfish.ui.layout.VerticalLayout;
import com.rongji.dfish.ui.layout.View;
import com.rongji.dfish.ui.widget.Button;
import com.rongji.dfish.ui.widget.Html;
import com.rongji.dfish.ui.widget.Leaf;
import com.rongji.dfish.ui.widget.Split;
import com.rongji.dfish.ui.widget.SubmitButton;
import com.rongji.dfish.ui.widget.TreePanel;
import com.xrj.api.dto.SysDataDict;
import com.xrj.business.core.base.RemoteService;
import com.xrj.framework.util.StringUtils;

public class DataDictView {
	/**
	 * 创建数据字典视图
	 * @param dataDictList
	 * @param focus
	 * @return
	 */
	public static View buildDataDictView(List<SysDataDict> dataDictList, SysDataDict focus){
		View view = new View(PubView.MAIN_ROOT);
		VerticalLayout main = new VerticalLayout("mainTitle").setCls("w-vert x-bd-body bg-white").setStyle("margin:8px 12px 8px 12px;");
		view.add(main);
		HorizontalLayout hor = new HorizontalLayout("hor");
		VerticalLayout mainContent = new VerticalLayout("mainContent");
		
		main.add(hor);
		Widget<?> permNode = buildDataDictPermNode(dataDictList, focus);
		hor.add(permNode, "320");
		hor.add(new Split().setRange("100, 300").setCls("bd-mod bd-notop bd-nobottom").setWmin(2), "5");
		hor.add(mainContent);
	
		ButtonBar btnBar = new ButtonBar("btnBar").setAlign(ButtonBar.ALIGN_RIGHT).setStyle("padding:5px;");
		btnBar.setPub(new Button(null)).setSpace(5);
		btnBar.add(new Button("新增").setOn(Button.EVENT_CLICK, "var id=VM(this).fv('id');if(id==''){$.alert('请选择要新增的父类')};VM(this).cmd('addDataDict',id);"));
		btnBar.add(new Button("删除").setOn(Button.EVENT_CLICK, "var id=VM(this).fv('id');if(id==''){$.alert('请选择要删除的字典')};VM(this).cmd('delDataDict',VM(this).fv('id'),VM(this).fv('dictName'));"));
		btnBar.add(new SubmitButton("保存").setCls("x-btn x-btn-main").setOn(Button.EVENT_CLICK, "VM(this).cmd('saveDataDict');"));
		mainContent.add(btnBar, "60");
		mainContent.add(new Split().setCls("bd-mod bd-notop  bd-noleft bd-noright").setWmin(2), "5");
		
		Html mainDisplay = new Html("<div>请点击左边数据字典树查看信息</div>").setStyle("padding:20px;").setHmin(40).setWmin(40);
		FlexGrid form = new FlexGrid("dataDictForm");
		form.add(mainDisplay, 12);
		mainContent.add(form);
		
		
		view.addCommand("dictClick", new AjaxCommand("dataDict/toEdit?id=$0"));
		view.addCommand("addDataDict", new AjaxCommand("dataDict/toAdd?parentId=$0"));
		view.addCommand("saveDataDict", PubView.getSubmitCommand("dataDict/saveDataDict"));
		view.addCommand("delDataDict", new ConfirmCommand("确定要删除：$1？").setYes(PubView.getAjaxCommand("dataDict/delDataDict?id=$0")));
		
		return view;
	}
	/**
	 *  创建数据字典组织树
	 * @param dataDictList
	 * @param focus
	 * @return
	 */
	public static Widget<?> buildDataDictPermNode(List<SysDataDict> dataDictList, SysDataDict focus){
		VerticalLayout root = new VerticalLayout("");
		
		TreePanel tree = buildDataDictTree(dataDictList, focus);
		
		root.add(tree);
		
		return tree;
		
	}
	
	/**
	 * 构建数据字典树
	 * @param loginUserId 
	 * @param focusOrg
	 * @param roleId
	 * @return
	 */
	public static TreePanel buildDataDictTree(List<SysDataDict> dataDictList, SysDataDict focus) {
		Leaf leaf = new Leaf();
		
		leaf = new Leaf().setTip(true).setIcon(".x-folder").setOpenicon(".x-folder-open").setOn(Leaf.EVENT_CLICK, "this.cmd('dictClick',$id);");
			
		TreePanel tree = new TreePanel("dataDictTree").setScroll(true).setStyle("padding:0 10px 10px 10px;").setHmin(10).setWmin(20)
				.setPub(leaf);
		
		fillDataDictLeaf(tree, dataDictList, focus);
		return tree;
	}
	
	/**
	 * 填充组织树
	 * @param node
	 * @param pubService
	 * @param orgList
	 * @param focusOrg
	 */
	private static void fillDataDictLeaf(AbstractLayout<?, Leaf> node,List<SysDataDict> dataDictList, SysDataDict focus) {
		RemoteService remoteService = FrameworkHelper.getBean(RemoteService.class);
		if (node == null || CollectionUtils.isEmpty(dataDictList)) {
			return;
		}
		boolean hasFocus = focus != null;
		Boolean open = null;
		String id = "";
		String type = "";
		if (hasFocus) {
			id = focus.getId();
			type = focus.getDictType();
			if(focus.getParentId()==null){
				type = focus.getDictCode();
			}
		} else if (node instanceof TreePanel) {
			// 第一个节点默认展开
			open = true; // 这么写为了子节点无需输出open属性
		}
		for (SysDataDict sysDataDict : dataDictList) {
			String dictId = sysDataDict.getId();
			String dictType = sysDataDict.getDictType();
			List<SysDataDict> subList = remoteService.getSysDataDictService().findSysDataDictList(dictId, null);
			
			Leaf subLeaf = buildDataDictLeaf(sysDataDict, subList.size()).setOpen(open);
			node.add(subLeaf);
			if (hasFocus) {
				if (id.equals(dictId)) {
					subLeaf.setFocus(true);
					hasFocus = false;
				} else if(type.equals(dictType)){
					if (!CollectionUtils.isEmpty(subList)) {
						subLeaf.setOpen(true);
					}
					fillDataDictLeaf(subLeaf, subList, focus);
				}
			}else{
				open = false;
			}
		}
		
	}
	
	/**
	 * 构建节点
	 * @return
	 */
	public static Leaf buildDataDictLeaf(SysDataDict sysDataDict, int subCount) {
		String id = sysDataDict.getId();
		String parentId = sysDataDict.getParentId();
		String name = sysDataDict.getDictName();
		String type = sysDataDict.getDictType();
		String code = sysDataDict.getDictCode();
		String leafName = name;
		if (subCount > 0) {
			leafName += "&nbsp;&nbsp;<span style='color:green'>(" + subCount + ")</span>";
		}
		Leaf leaf = new Leaf("o_" + id, leafName).setTip(name).setData("id", id).setData("parentId", parentId).setData("name", name).setData("type", type).setData("code", code);
		if (subCount > 0) {
			leaf.setSrc("dataDict/openDataDict?id=$id");
		}
		return leaf;
	}
	
	
	public static FlexGrid buildEditView(SysDataDict sysDataDict){
		
//		View view = new View(PubView.MAIN_DISPLAY);
		FlexGrid form = new FlexGrid("dataDictForm");
		
		form.addHidden("id", (sysDataDict==null)?null:sysDataDict.getId());
		form.addHidden("parentId", (sysDataDict==null)?null:sysDataDict.getParentId());
		form.add("ID：", 4);
		form.add(new Html((sysDataDict==null)?null:sysDataDict.getId()),8);
		form.add("上级父类：", 4);
		form.add(buildDictSelect((sysDataDict==null)?null:sysDataDict.getParentId()).setReadonly(true).setWidth(300).setOn(Xbox.EVENT_CHANGE, "VM(this).f('parentId').val(VM(this).f('select_parentId').val());"),4);
		form.add(new Button("清空").setOn(Button.EVENT_CLICK, "VM(this).f('parentId').val('');VM(this).f('select_parentId').val('');"),4);
		form.add("字典名称：", 4, true);
		form.add(new Text("dictName", "字典名称", (sysDataDict==null)?null:sysDataDict.getDictName()).setRequired(true),8);
		form.add("字典类型：", 4);
		form.add(new Text("dictType", "字典类型", (sysDataDict==null)?null:sysDataDict.getDictType()),8);
		form.add("字典编码：", 4, true);
		form.add(new Text("dictCode", "字典编码", (sysDataDict==null)?null:sysDataDict.getDictCode()).setRequired(true),8);
		form.add("字典说明：", 4);
		form.add(new Textarea("remark", "字典说明", (sysDataDict==null)?null:sysDataDict.getRemark()).setHeight(50), 8);
//		view.add(form);
		
		return form;
	}
	
	public static Select buildDictSelect( String parentId){
		RemoteService remoteService = FrameworkHelper.getBean(RemoteService.class);
		List<Object> objectList = new ArrayList<Object>();
		objectList.add(new Object[]{null,"-请选择-"});
		if(StringUtils.isNotEmpty(parentId)){
			SysDataDict sysDataDict= remoteService.getSysDataDictService().get(parentId);
			objectList.add(new Object[]{sysDataDict.getId(), sysDataDict.getDictName()});
		}
		return new Select("select_parentId", "上级父类", parentId,objectList);
	}
}
