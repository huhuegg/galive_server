package com.galive.logic.service;

import com.galive.logic.exception.LogicException;
import com.galive.logic.model.Course;
import com.galive.logic.model.Room;

import java.util.Date;
import java.util.Map;


public interface CourseService {

	enum FindCourseBy {
		id
	}

	Course findCourse(FindCourseBy by, String byId) throws LogicException;

	Course createCourse(String accountSid, String name, String profile, long start, long duration) throws LogicException;

	Course joinCourse(String courseSid, String accountSid) throws LogicException;

	Course leaveCourse(String courseSid, String accountSid) throws LogicException;

	Course destroyCourse(String courseSid, String accountSid) throws LogicException;

}
