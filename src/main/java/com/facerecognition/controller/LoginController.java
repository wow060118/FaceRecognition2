package com.facerecognition.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.facerecognition.bean.UserFace;
import com.facerecognition.common.Msg;
import com.facerecognition.service.UserFaceService;

@Controller
@RequestMapping("/user")
public class LoginController {
	@Autowired
	private UserFaceService userFaceService;

	@RequestMapping("/userLogin")
	@ResponseBody
	public Msg userLogin(HttpServletRequest request, UserFace userFace) {
//		System.out.println(userFace.getPhonenum());
//		System.out.println(userFace.getFaceBase64());
		boolean exist = userFaceService.userlogin(request, userFace);
		if (exist) {
			return Msg.success(); // 欢迎页
		}
		return Msg.fail().add("message", "识别错误，请再次扫描！！");// 登录页
	}

	@RequestMapping("/userRegister")
	@ResponseBody
	public Msg userRegister(HttpServletRequest request, UserFace userFace) {
//		System.out.println(userFace.getPhonenum());
//		System.out.println(userFace.getFaceBase64());
		boolean state = userFaceService.userrRegister(request, userFace);
		if (state) { // 注册成功
			 return Msg.success().add("message", "注册成功，请登录。");
		}
		 return Msg.fail().add("message", "注册失败，请再次扫描！！");// 再次注册
	}

}
