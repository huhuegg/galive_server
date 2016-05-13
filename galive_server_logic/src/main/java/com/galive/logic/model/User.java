package com.galive.logic.model;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.Indexes;


@Entity(value="user", noClassnameStored = true)
@Indexes({@Index("username")})
public class User extends BaseModel {
	
	public static enum UserPlatform {
		App,
		WeChat,
		QQ,
		SinaWeibo;
		
		public static UserPlatform convert(int code) {
			for (UserPlatform platform : UserPlatform.values()) {
				if (platform.ordinal() == code) {
					return platform;
				}
			}
			return UserPlatform.App;
		}
	}

	
	public static enum UserOnlineState {
		Online,
		Offline;
	}
	
	public static enum UserGender {
		Male, 
		Female, 
		Unknown;
		
		public static UserGender convert(int i) {
			for (UserGender g : UserGender.values()) {
				if (g.ordinal() == i) {
					return g;
				}
			}
			return UserGender.Unknown;
		}
	}
	
	private String username = "";

	private String password = "";

	private String nickname = "";

	private String avatar = "";
	
	private String profile = "";
	
	private UserGender gender = UserGender.Unknown;
	
	@Embedded
	private UserExtraData extraData;
	
	public String desc() {
		return String.format(" %s(%s) ", nickname, sid);
	}
	
	/* ======================= Getter Setter ======================= */
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public UserGender getGender() {
		return gender;
	}

	public void setGender(UserGender gender) {
		this.gender = gender;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public UserExtraData getExtraData() {
		return extraData;
	}

	public void setExtraData(UserExtraData extraData) {
		this.extraData = extraData;
	}

	
	
}
