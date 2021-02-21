package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.vo.CourseInfoVo;
import com.atguigu.eduservice.entity.vo.CoursePublishVo;
import com.atguigu.eduservice.entity.vo.CourseQuery;
import com.atguigu.eduservice.service.EduCourseService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author xiaocai
 * @since 2021-01-22
 */
@RestController
@RequestMapping("/eduservice/course")
public class EduCourseController {

    @Autowired
    private EduCourseService courseService;

    @ApiOperation(value = "分页查询课程列表")
    @GetMapping("pageCourse/{current}/{limit}")
    public R pageListCourse(@ApiParam(name = "current", value = "当前页码", required = true)
                             @PathVariable Long current,
                             @ApiParam(name = "limit", value = "每页记录数", required = true)
                             @PathVariable Long limit){

        //创建page对象
        Page<EduCourse> pageCourse = new Page<>(current,limit);
        //调用方法实现分页
        //调用方法时候，底层封装，把分页所有数据封装到pageTeacher对象里面
        courseService.page(pageCourse, null);

        long total = pageCourse.getTotal();
        List<EduCourse> records = pageCourse.getRecords();

        return R.ok().data("total",total).data("rows",records);
    }

    //4 分页查询
    @ApiOperation(value = "带条件分页讲师列表")
    @PostMapping("pageCourseCondition/{current}/{limit}")
    public R pageCourseCondition(@ApiParam(name = "current", value = "当前页码", required = true)
                                  @PathVariable Long current,
                                  @ApiParam(name = "limit", value = "每页记录数", required = true)
                                  @PathVariable Long limit,
                                  @RequestBody(required = false) CourseQuery courseQuery){

        //创建page对象
        Page<EduCourse> pageCourse = new Page<>(current,limit);
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        //多条件组合查询,类似于动态语句
        String title = courseQuery.getTitle();
        String subjectId = courseQuery.getSubjectId();
        String subjectParentId = courseQuery.getSubjectParentId();
        String teacherId = courseQuery.getTeacherId();
        if(!StringUtils.isEmpty(title)){
            wrapper.like("title",title);
        }
        if(!StringUtils.isEmpty(subjectId)){
            wrapper.ge("subject_id",subjectId);
        }
        if(!StringUtils.isEmpty(subjectParentId)){
            wrapper.ge("subject_parent_id",subjectParentId);
        }
        if(!StringUtils.isEmpty(teacherId)){
            wrapper.eq("teacher_id",teacherId);
        }

        //排序
        wrapper.orderByDesc("gmt_create");

        courseService.page(pageCourse,wrapper);
        long total = pageCourse.getTotal();
        List<EduCourse> records = pageCourse.getRecords();

        return R.ok().data("total",total).data("rows",records);
    }

    @PostMapping("addCourseInfo")
    public R addCourseInfo(@RequestBody CourseInfoVo courseInfoVo){

        String id = courseService.saveCourseInfoVo(courseInfoVo);
        return R.ok().data("courseId",id);
    }

    @GetMapping("getCourseInfo/{courseId}")
    public R getCourseInfo(@PathVariable String courseId){
        CourseInfoVo courseInfoVo = courseService.getCourseInfo(courseId);

        return R.ok().data("courseInfoVo",courseInfoVo);
    }

    @PostMapping("updateCourseInfo")
    public R updateCourseInfo(@RequestBody CourseInfoVo courseInfoVo){

        courseService.updateCourseInfoVo(courseInfoVo);
        return R.ok();
    }

    @GetMapping("getPublishCourseInfo/{courseId}")
    public R getPublishCourseInfo(@PathVariable String courseId){
        CoursePublishVo coursePublishVo = courseService.publishCourseInfo(courseId);

        return R.ok().data("coursePublishVo",coursePublishVo);
    }

    @GetMapping("publishCourse/{courseId}")
    public R publishCourse(@PathVariable String courseId){
        EduCourse eduCourse = new EduCourse();
        eduCourse.setId(courseId);
        eduCourse.setStatus("Normal");
        courseService.updateById(eduCourse);

        return R.ok();
    }

    @DeleteMapping("{courseId}")
    public R removeCourse(@PathVariable String courseId){
        courseService.removeCourseById(courseId);

        return R.ok();
    }

}

