package com.galive.logic.model;

import java.util.Date;

/**
 * 课程
 */
public class Course extends BaseModel {

    private String name = "";

    private String profile = "";

    private long start = 0;

    private long duration = 0;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}
