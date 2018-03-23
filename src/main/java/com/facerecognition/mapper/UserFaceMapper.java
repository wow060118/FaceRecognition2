package com.facerecognition.mapper;

import com.facerecognition.bean.UserFace;
import com.facerecognition.bean.UserFaceExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserFaceMapper {
    int countByExample(UserFaceExample example);

    int deleteByExample(UserFaceExample example);

    int deleteByPrimaryKey(Integer userId);

    int insert(UserFace record);

    int insertSelective(UserFace record);

    List<UserFace> selectByExampleWithBLOBs(UserFaceExample example);

    List<UserFace> selectByExample(UserFaceExample example);

    UserFace selectByPrimaryKey(Integer userId);

    int updateByExampleSelective(@Param("record") UserFace record, @Param("example") UserFaceExample example);

    int updateByExampleWithBLOBs(@Param("record") UserFace record, @Param("example") UserFaceExample example);

    int updateByExample(@Param("record") UserFace record, @Param("example") UserFaceExample example);

    int updateByPrimaryKeySelective(UserFace record);

    int updateByPrimaryKeyWithBLOBs(UserFace record);

    int updateByPrimaryKey(UserFace record);
}