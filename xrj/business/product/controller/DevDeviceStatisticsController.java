package com.xrj.business.product.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rongji.dfish.ui.command.ReplaceCommand;
import com.rongji.dfish.ui.layout.View;
import com.xrj.business.product.view.DevDeviceStatisticsView;


/**
 * @Date 2017-9-18
 * @author chenjunchao
 *
 */
@Controller
@RequestMapping("/product")
public class DevDeviceStatisticsController {

	@RequestMapping("/deviceStatistics")
	@ResponseBody
	public View deviceStatistics(HttpServletRequest request) {	
		View view = DevDeviceStatisticsView.overView() ;
		return view;

	}
	
	@RequestMapping("/newOnline")
	@ResponseBody
	public Object newOnline(HttpServletRequest request) {	
		View view = DevDeviceStatisticsView.newOnlieView() ;
		return view;

	}
	
	@RequestMapping("/activeDevice")
	@ResponseBody
	public Object activeDevice(HttpServletRequest request) {	
		View view = DevDeviceStatisticsView.activeDeviceView();
		return view;

	}
	
	@RequestMapping("/activePeriod")
	@ResponseBody
	public Object activePeriod(HttpServletRequest request) {	
		View view = DevDeviceStatisticsView.activePeriodView();
		return view;

	}
	
	@RequestMapping("/linkTime")
	@ResponseBody
	public Object linkTime(HttpServletRequest request) {	
		View view = DevDeviceStatisticsView.linkTimeView();
		return view;

	}
	
}
