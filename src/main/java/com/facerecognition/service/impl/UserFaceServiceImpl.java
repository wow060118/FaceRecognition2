package com.facerecognition.service.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.objdetect.CascadeClassifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.facerecognition.bean.UserFace;
import com.facerecognition.bean.UserFaceExample;
import com.facerecognition.bean.UserFaceExample.Criteria;
import com.facerecognition.mapper.UserFaceMapper;
import com.facerecognition.service.UserFaceService;
import com.facerecognition.utils.OpenCVUtils;

/**
 * 
 * @author Mr.Bug
 *
 */
@Service
public class UserFaceServiceImpl implements UserFaceService {
	@Autowired
	private UserFaceMapper userFaceMapper;

	static {
		try {
			// 加载dll文件
			System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		} catch (Exception e) {
			System.out.println("需要加载dll文件！！");
		}
	}

	@Override
	public boolean userlogin(HttpServletRequest request, UserFace userFace) {
		// 加载人脸识别分类器
		CascadeClassifier faceDetector = loadRecognitionXML(request);
		try {
			//根据电话号码获取用户
			UserFace dbuser = getUserFaceByPhone(userFace);
			if (null != dbuser) {
				// 获取数据库用户的base64图片
				String dbFaceBase64 = dbuser.getFaceBase64();
				System.out.println(dbFaceBase64);
				Mat dbMat = OpenCVUtils.base64ToMat(dbFaceBase64);
				// 获取用户传入过来的base64图片
				String userFaceBase64 = userFace.getFaceBase64();
				System.out.println("用户的base64编码"+userFaceBase64);
				// 将用户传入过来的base64图片进行识别截取转换为mat
				Mat userMat = OpenCVUtils.analysisFace(userFaceBase64, faceDetector);
//				Imgcodecs.imwrite("F://pyl.jpg", userMat);
				// 进行人脸识别判断相似度
				System.out.println("这个是dbmat---->"+dbMat);
				System.out.println("这个是userMat---->"+userMat);
				int compareHistogram = OpenCVUtils.compareHistogram(dbMat, userMat);
				if (compareHistogram == 1) {
					return true;// 相似
				}
			}
		} catch (Exception e) {
			System.out.println("识别出错！！！");
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public boolean userrRegister(HttpServletRequest request, UserFace userFace) {
		try {
			if (null != userFace) {// 用户不为空开始注册
				// 获取用户注册图片
				String userfaceBase64 = userFace.getFaceBase64();
				// 加载分类器
				CascadeClassifier faceDetector = loadRecognitionXML(request);
				// 解析人脸
				Mat analysisFace = OpenCVUtils.analysisFace(userfaceBase64, faceDetector);
				// 将mat转换为base64
				String matToBase64 = OpenCVUtils.matToBase64(analysisFace);
				if (null != matToBase64 && !"".equals(matToBase64)) {
					userFace.setFaceBase64(matToBase64);
					// 将数据写入数据库
					userFaceMapper.insert(userFace);
					return true;
				}
			}
		} catch (Exception e) {
			System.out.println("注册失败！！！");
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 根据用户手机号码查询用户
	 * 
	 * @param userFace
	 * @return
	 */
	private UserFace getUserFaceByPhone(UserFace userFace) {
		UserFace user = null;
		if (null != userFace) {
			// 获取用户手机号码
			String phonenum = userFace.getPhonenum();

			// 创建筛选条件
			UserFaceExample example = new UserFaceExample();
			Criteria criteria = example.createCriteria();
			criteria.andPhonenumEqualTo(phonenum);
			List<UserFace> userFaces = userFaceMapper.selectByExampleWithBLOBs(example);
			if (null != userFaces && userFaces.size() > 0) {
				// 获取数据库用户
				user = userFaces.get(0);
			}
		}
		return user;
	}

	/**
	 * 加载人脸识别分类器
	 * 
	 * @param request
	 * @return
	 */
	private CascadeClassifier loadRecognitionXML(HttpServletRequest request) {
		// 加载人脸识别分类器
		CascadeClassifier faceDetector = new CascadeClassifier(
				request.getServletContext().getRealPath("/WEB-INF/xml/haarcascade_frontalface_alt.xml"));
		return faceDetector;
	}

}
