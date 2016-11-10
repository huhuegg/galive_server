package com.galive.logic.model;

import java.util.ArrayList;
import java.util.List;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Transient;

import com.galive.logic.model.account.Account;


@Entity
public class Meeting extends BaseModel {
	
	/**
	 * 语音服务器对应房间号
	 */
	@Transient
	private String room = "";
	
	/**
	 * 会议对应用户
	 */
	private String accountSid = "";
	
	/**
	 * 针对用户唯一切无法更改,用于直接进入用户房间
	 */
	private String searchName = "";
	
	/**
	 * 会议名称 用于显示
	 */
	private String displayName = "";
	
	/**
	 * 会议成员
	 */
	private List<String> memberSids = new ArrayList<>();
	
	/**
	 * 成员对象 用于返回数据
	 */
	@Transient
	private List<Account> members = new ArrayList<>();
	
	/**
	 *  简介
	 */
	private String profile = "";
	
	/**
	 * 进入会议需要的密码
	 */
	private String password = "";
	
	/**
	 * 标签
	 */
	private List<String> tags = new ArrayList<>();
	
	/**
	 * 封面
	 */
	private String coverImage = "";

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public String getAccountSid() {
		return accountSid;
	}

	public void setAccountSid(String accountSid) {
		this.accountSid = accountSid;
	}

	public String getSearchName() {
		return searchName;
	}

	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public List<String> getMemberSids() {
		return memberSids;
	}

	public void setMemberSids(List<String> memberSids) {
		this.memberSids = memberSids;
	}

	public List<Account> getMembers() {
		return members;
	}

	public void setMembers(List<Account> members) {
		this.members = members;
	}

	public String getCoverImage() {
		return coverImage;
	}

	public void setCoverImage(String coverImage) {
		this.coverImage = coverImage;
	}

}
