package com.galive.logic.platform.qq;

/**
 * QQ用户个人信息
 * @author luguangqing
 *
 */
public class QQUserInfoResp extends QQResp {

	/**
	 * 普通用户的标识，对当前开发者帐号唯一
	 */
	private String openid = "";
	
	/**
	 * 普通用户昵称
	 */
	private String nickname = "";
	
	/**
	 * 普通用户性别，男，女
	 */
	private String gender = "男";

	/**
	 * 头像URL。详见：前端页面规范#6. 关于用户头像的获取和尺寸说明。
	 * 空间头像尺寸有：100px，50px，30px 3种规格。
	 * 朋友头像尺寸有：100px，60px，30px 3种规格。
	 * 通过OpenAPI（例如v3/user/get_info ，v3/user/get_multi_info）获取到的头像地址（即返回包中的figureurl）通常是如下格式：
	 * http://头像域名/[campus/qzopenapp]/加密串/尺寸 
	 * 开发者得到上述地址后，修改后面的尺寸数字即可，如下面例子所示： 
	 * 获取到的figureurl为： 
	 * http://qlogo3.store.qq.com/qzopenapp/d8219673598dbd6f00000d307e46c7bde4cfffca38933abc5a4ecac43bc03e44/100 
	 * 如果你想的到30px尺寸的头像，只要把尺寸改为30即可：
	 * http://qlogo3.store.qq.com/qzopenapp/d8219673598dbd6f00000d307e46c7bde4cfffca38933abc5a4ecac43bc03e44/30 
	 */
	private String figureurl = "";

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getFigureurl() {
		return figureurl;
	}

	public void setFigureurl(String figureurl) {
		this.figureurl = figureurl;
	}

	
	
}
