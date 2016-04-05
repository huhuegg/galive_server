package com.galive.logic.platform.wb;

/**
 * 微博用户个人信息
 * @author luguangqing
 *
 */
public class SinaWeiboUserInfoResp extends SinaWeiboResp {

	private String id = "";
	/**
	 * 字符串型的用户UID
	 */
	private String idstr = "";
	
	/**
	 * 用户昵称
	 */
	private String screen_name = "";
	
	/**
	 * 用户个人描述
	 */
	private String description = "";
	
	/**
	 * 用户头像地址（中图），50×50像素
	 */
	private String profile_image_url = "";
	
	/**
	 * 性别，m：男、f：女、n：未知
	 */
	private String gender = "";
	
	/**
	 * 用户头像地址（大图），180×180像素
	 */
	private String avatar_large = "";
	
	/**
	 * 用户头像地址（高清），高清头像原图
	 */
	private String avatar_hd = "";

	public String getScreen_name() {
		return screen_name;
	}

	public void setScreen_name(String screen_name) {
		this.screen_name = screen_name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getProfile_image_url() {
		return profile_image_url;
	}

	public void setProfile_image_url(String profile_image_url) {
		this.profile_image_url = profile_image_url;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getAvatar_large() {
		return avatar_large;
	}

	public void setAvatar_large(String avatar_large) {
		this.avatar_large = avatar_large;
	}

	public String getAvatar_hd() {
		return avatar_hd;
	}

	public void setAvatar_hd(String avatar_hd) {
		this.avatar_hd = avatar_hd;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIdstr() {
		return idstr;
	}

	public void setIdstr(String idstr) {
		this.idstr = idstr;
	}
}
