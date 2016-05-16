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
	
	private String deviceid = "";
	
	@Embedded
	private UserExtraData extraData = new UserExtraData();
	
	public String desc() {
		return String.format(" %s(%s) ", extraData.getNickname(), sid);
	}

	public UserExtraData getExtraData() {
		return extraData;
	}

	public void setExtraData(UserExtraData extraData) {
		this.extraData = extraData;
	}

	public String getDeviceid() {
		return deviceid;
	}

	public void setDeviceid(String deviceid) {
		this.deviceid = deviceid;
	}

	
	
}
