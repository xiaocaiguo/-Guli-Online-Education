package com.atguigu.eduservice.mapper;

import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.frontvo.CourseWebVo;
import com.atguigu.eduservice.entity.vo.CoursePublishVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author xiaocai
 * @since 2021-01-22
 */
public interface EduCourseMapper extends BaseMapper<EduCourse> {
    CoursePublishVo getPublishCourseInfo(String courseId);
    CourseWebVo selectInfoWebById(String courseId);
}
