package com.facerecognition.service;

import javax.servlet.http.HttpServletRequest;

import com.facerecognition.bean.UserFace;

/**
 * 
 * @author Mr.Bug
 *
 */
public interface UserFaceService {
	/**
	 * 
	 * @param userFace
	 *            用户信息
	 * @return 返回用户是否存在
	 */
	boolean userlogin(HttpServletRequest request, UserFace userFace);

	/**
	 * 用户注册
	 * 
	 * @param request
	 * @param userFace
	 * @return
	 */
	boolean userrRegister(HttpServletRequest request, UserFace userFace);

}
