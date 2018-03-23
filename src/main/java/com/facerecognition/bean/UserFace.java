package com.facerecognition.bean;

public class UserFace {
    private Integer userId;

    private String phonenum;

    private String faceBase64;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getPhonenum() {
        return phonenum;
    }

    public void setPhonenum(String phonenum) {
        this.phonenum = phonenum == null ? null : phonenum.trim();
    }

    public String getFaceBase64() {
        return faceBase64;
    }

    public void setFaceBase64(String faceBase64) {
        this.faceBase64 = faceBase64 == null ? null : faceBase64.trim();
    }

	@Override
	public String toString() {
		return "UserFace [userId=" + userId + ", phonenum=" + phonenum + ", faceBase64=" + faceBase64 + "]";
	}
    
}