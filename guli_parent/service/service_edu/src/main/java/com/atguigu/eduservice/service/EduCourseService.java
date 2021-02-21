package com.atguigu.eduservice.service;

import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.frontvo.CourseFrontVo;
import com.atguigu.eduservice.entity.frontvo.CourseWebVo;
import com.atguigu.eduservice.entity.vo.CourseInfoVo;
import com.atguigu.eduservice.entity.vo.CoursePublishVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author xiaocai
 * @since 2021-01-22
 */
public interface EduCourseService extends IService<EduCourse> {

    String saveCourseInfoVo(CourseInfoVo courseInfoVo);

    CourseInfoVo getCourseInfo(String courseId);

    void updateCourseInfoVo(CourseInfoVo courseInfoVo);

    CoursePublishVo publishCourseInfo(String courseId);

    void removeCourseById(String courseId);

    List<EduCourse> selectHotCourse();

    List<EduCourse> selectByTeacherId(String teacherId);

    Map<String, Object> pageListCourse(Page<EduCourse> pageParam, CourseFrontVo courseFrontVo);

    /**
     * 获取课程信息
     * @param courseId
     * @return
     */
    CourseWebVo selectInfoWebById(String courseId);

    /**
     * 更新课程浏览数
     * @param courseId
     */
    void updatePageViewCount(String courseId);
}
