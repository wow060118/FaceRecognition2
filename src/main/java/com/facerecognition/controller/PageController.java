package com.facerecognition.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 控制页面之间的跳转
 * 
 * @author Mr.Bug
 *
 */
@Controller
public class PageController {
	@RequestMapping("/")
	public String index() {
		return "index";//首页
	}

	@RequestMapping("/page/{view}")
	public String toPage(@PathVariable("view") String view) {
		return view;
	}
}
