package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.subject.OneSubject;
import com.atguigu.eduservice.service.EduSubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author xiaocai
 * @since 2021-01-21
 */
@Api(description = "课程分类管理")
@RestController
@RequestMapping("/eduservice/subject")
public class EduSubjectController {

    @Autowired
    private EduSubjectService SubjectService;

    //添加课程分类
    //获取上传的文件，把文件内容读取出去
    @PostMapping("addSubject")
    @ApiOperation(value = "Excel批量导入")
    public R addSubject(MultipartFile file){
        SubjectService.saveSubject(file,SubjectService);
        return R.ok();
    }

    //课程分类列表（树形）
    @GetMapping("getAllSubject")
    public R getAllSubject(){

        List<OneSubject> list = SubjectService.getAllOneTwoSubject();


        return R.ok().data("list",list);
    }
}

