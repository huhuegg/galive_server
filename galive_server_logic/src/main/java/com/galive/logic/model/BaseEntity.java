package com.galive.logic.model;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.Indexes;

/**
 * 基础Model
 * @author Luguangqing
 *
 */
@Entity
@Indexes({@Index("sid")})
public class BaseEntity {

	@Id
	protected ObjectId id;
	
	protected long createAt = System.currentTimeMillis();
	
	protected String sid;

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public long getCreateAt() {
		return createAt;
	}

	public void setCreateAt(long createAt) {
		this.createAt = createAt;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	
	
}
