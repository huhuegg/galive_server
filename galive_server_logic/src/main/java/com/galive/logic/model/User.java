package com.galive.logic.model;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.Indexes;

@Entity(value="user", noClassnameStored = true)
@Indexes({@Index("username")})
public class User extends BaseModel {

	public static enum UserOnlineState {
		Online,
		Offline;
	}
	
	public static enum UserGender {
		Male, 
		Female, 
		Unknown;
	}
	
	private String username = "";

	private String password = "";

	private String nickname = "";

	private String avatar = "";
	
	private UserGender gender = UserGender.Unknown;
	
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
	
	
	
}
