package com.facerecognition.test;

import java.io.FileInputStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.facerecognition.bean.UserFace;
import com.facerecognition.mapper.UserFaceMapper;

/**
 * 测试dao层的工作
 * 
 * @author ghy 推荐Spring的项目就可以使用Spring的单元测试，可以自动注入我们需要的组件 1、导入SpringTest模块
 *         2、@ContextConfiguration指定Spring配置文件的位置 3、直接autowired要使用的组件即可
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/applicationContext.xml" })
public class DbTest {

	@Autowired
	private UserFaceMapper userFaceMapper;

	/**
	 * 测试DepartmentMapper
	 */
	@Test
	public void testInsert() {
		try {
			// FileInputStream in = new FileInputStream("F:\\8.jpg");
			// byte[] b = new byte[in.available()];
			// in.read(b);
			// in.close();
			UserFace userFace = new UserFace();
			userFace.setPhonenum("12536524152");
			userFace.setFaceBase64(Resource.RESOURCE_ONE);
			userFaceMapper.insert(userFace);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
