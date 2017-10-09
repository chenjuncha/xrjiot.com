package com.xrj.business.core.web;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rongji.dfish.ui.command.CommandGroup;
import com.rongji.dfish.ui.command.JSCommand;
import com.rongji.dfish.ui.command.ReplaceCommand;
import com.rongji.dfish.ui.helper.FlexGrid;
import com.rongji.dfish.ui.widget.Leaf;
import com.xrj.api.dto.SysDataDict;
import com.xrj.api.service.SysDataDictService;
import com.xrj.business.core.base.PubService;
import com.xrj.business.core.base.RemoteService;
import com.xrj.business.core.view.DataDictView;
import com.xrj.business.core.view.PubView;
import com.xrj.framework.util.StringUtils;

@Controller
@RequestMapping("dataDict")
public class DataDictController {
	@Resource
	RemoteService remoteService;
	
	/**
	 * 展开数据字典树
	 */
	@RequestMapping("/openDataDict")
	@ResponseBody
	public Object index(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Leaf leaf = new Leaf();
		SysDataDictService sysDataDictService = remoteService.getSysDataDictService();
		String id = request.getParameter("id");
		List<SysDataDict> list = sysDataDictService.findSysDataDictList(id, null);
		if (!CollectionUtils.isEmpty(list)) {
			for (SysDataDict ssd : list) {
				List<SysDataDict> subList = sysDataDictService.findSysDataDictList(ssd.getId(), null);
				int subCount = 0;
				if (subList != null) {
					subCount = subList.size();
				}
				Leaf subLeaf = DataDictView.buildDataDictLeaf(ssd, subCount);
				leaf.add(subLeaf);
			}
		}

		return leaf;
	}
	
	/**
	 * 转向修改页面
	 */
	@RequestMapping("/toEdit")
	@ResponseBody
	public Object toEdit(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String id = request.getParameter("id");
		SysDataDict sysDataDict = remoteService.getSysDataDictService().get(id);
		FlexGrid form =  DataDictView.buildEditView(sysDataDict);
		return new ReplaceCommand().setNode(form);
	}
	
	/**
	 * 转向新增页面
	 */
	@RequestMapping("/toAdd")
	@ResponseBody
	public Object toAdd(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String parentId = request.getParameter("parentId");
		SysDataDict sysDataDict = new SysDataDict();
		sysDataDict.setParentId(parentId);
		FlexGrid form = DataDictView.buildEditView(sysDataDict);
		return new ReplaceCommand().setNode(form);
	}

	/**
	 * 转向新增页面
	 */
	@RequestMapping("/delDataDict")
	@ResponseBody
	public Object delDataDict(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String id = request.getParameter("id");
		CommandGroup cg = new CommandGroup();
		List<SysDataDict> list = remoteService.getSysDataDictService().findSysDataDictList(id, null);
		if (!CollectionUtils.isEmpty(list)) {
			cg.add(PubView.getInfoAlert("该字典有下级结点，不能删除"));
		} else {
			SysDataDict sysDataDict = remoteService.getSysDataDictService().get(id);
			remoteService.getSysDataDictService().delete(id);
			cg.add(new JSCommand(
					"var parentId = VM(this).fv('parentId'); var leaf = VM(this).find('dataDictTree').find('o_'+parentId);if(leaf){leaf.reload();}else{ VM(this).reload();}"));
			cg.add(PubView.getInfoAlert("删除成功"));
			//重新设置缓存
			if(StringUtils.isNotBlank(sysDataDict.getDictType())){
				PubService.resetDataDictMap(sysDataDict.getDictType());
				PubService.resetDataDictList(sysDataDict.getDictType());
			}
		}
		
		return cg;
	}

	/**
	 * 转向新增页面
	 */
	@RequestMapping("/saveDataDict")
	@ResponseBody
	public Object saveDataDict(HttpServletRequest request, HttpServletResponse response, SysDataDict sysDataDict)
			throws Exception {
		CommandGroup cg = new CommandGroup();
		if (StringUtils.isEmpty(sysDataDict.getParentId())) {
			sysDataDict.setParentId(null);
		}
		if (StringUtils.isEmpty(sysDataDict.getId())) {
			remoteService.getSysDataDictService().saveSysDataDict(sysDataDict);
			cg.add(new JSCommand("var parentId = VM(this).fv('parentId');if(parentId==''){VM(this).reload();}"
					+ "else{var leaf = VM(this).find('dataDictTree').getFocus(); leaf.reload();}"));
			cg.add(PubView.getInfoAlert("新增成功"));
			if(StringUtils.isNotBlank(sysDataDict.getDictType())){
				PubService.resetDataDictMap(sysDataDict.getDictType());
				PubService.resetDataDictList(sysDataDict.getDictType());
			}
		} else {
			remoteService.getSysDataDictService().updateSysDataDict(sysDataDict);
			cg.add(new JSCommand("var leaf = VM(this).find('dataDictTree').find('o_" + sysDataDict.getParentId()
					+ "'); if(leaf){leaf.reload();}else{ VM(this).reload();}"));
			cg.add(PubView.getInfoAlert("修改成功"));
			//重新设置缓存
			if(StringUtils.isNotBlank(sysDataDict.getDictType())){
				PubService.resetDataDictMap(sysDataDict.getDictType());
				PubService.resetDataDictList(sysDataDict.getDictType());
			}
			SysDataDict sysDataDictOld = remoteService.getSysDataDictService().get(sysDataDict.getId());
			if(StringUtils.isNotBlank(sysDataDictOld.getDictType()) && !sysDataDictOld.getDictType().equals(sysDataDict.getDictType())){
				PubService.resetDataDictMap(sysDataDictOld.getDictType());
				PubService.resetDataDictList(sysDataDictOld.getDictType());
			}
		}

		return cg;
	}

}
