package com.galive.logic.service;

import com.galive.logic.dao.CourseDao;
import com.galive.logic.dao.CourseDaoImpl;
import com.galive.logic.exception.LogicException;
import com.galive.logic.model.Course;


public class CourseServiceImpl extends BaseService implements CourseService {

    private CourseDao courseDao = new CourseDaoImpl();

    @Override
    public Course findCourse(FindCourseBy by, String byId) throws LogicException {
        switch (by) {
            case id:

                break;
        }
        return null;
    }

    @Override
    public Course createCourse(String accountSid, String name, String profile, long start, long duration) throws LogicException {
        return null;
    }

    @Override
    public Course joinCourse(String courseSid, String accountSid) throws LogicException {
        return null;
    }

    @Override
    public Course leaveCourse(String courseSid, String accountSid) throws LogicException {
        return null;
    }

    @Override
    public Course destroyCourse(String courseSid, String accountSid) throws LogicException {
        return null;
    }
}
