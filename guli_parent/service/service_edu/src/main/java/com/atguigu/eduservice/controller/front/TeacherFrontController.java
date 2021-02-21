package com.atguigu.eduservice.controller.front;

import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.service.EduCourseService;
import com.atguigu.eduservice.service.EduTeacherService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("eduservice/teacherfront")
public class TeacherFrontController {

    @Autowired
    private EduTeacherService eduTeacherService;

    @Autowired
    private EduCourseService eduCourseService;

    @GetMapping("pageTeacherList/{page}/{limit}")
    public R pageTeacherList(@PathVariable Long page,@PathVariable Long limit){
        Page<EduTeacher> pageTeacher = new Page<>(page,limit);

        Map<String,Object> map = eduTeacherService.getPageTeacherList(pageTeacher);

        //总页数
        long pages = (long)map.get("pages");
        if (pages<page || page<0){
            throw new GuliException(20001,"已经是最后一页了");
        }
        return R.ok().data(map);
    }

    @GetMapping("getTeacherFrontInfo/{teacherId}")
    public R getTeacherFrontInfo(@PathVariable String teacherId){

        EduTeacher teacher = eduTeacherService.getById(teacherId);
        List<EduCourse> courseList = eduCourseService.selectByTeacherId(teacherId);

        return R.ok().data("teacher",teacher).data("courseList",courseList);
    }
}
