package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.vo.ChapterVo;
import com.atguigu.eduservice.service.EduChapterService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/eduservice/chapter")
public class EduChapterController {

    @Autowired
    private EduChapterService eduChapterService;

    @GetMapping("getChapterVideo/{courseId}")
    public R getChapterVideo(@PathVariable String courseId){
        List<ChapterVo> list = eduChapterService.getChapterVideoByCourseId(courseId);

        return R.ok().data("list",list);
    }

    @ApiOperation(value = "根据ID查询章节")
    @GetMapping("getChapterInfo/{chapterId}")
    public R getChapterInfo(
            @ApiParam(name = "chapterId", value = "章节ID", required = true)
            @PathVariable String chapterId){

        EduChapter educhapter = eduChapterService.getById(chapterId);
        return R.ok().data("chapter", educhapter);
    }

    @PostMapping("addChapter")
    public R addChapter(@RequestBody EduChapter eduChapter){

        eduChapterService.save(eduChapter);

        return R.ok();
    }

    @PostMapping("updateChapter")
    public R updateChapter(@RequestBody EduChapter eduChapter){
        eduChapterService.updateById(eduChapter);

        return R.ok();
    }

    @DeleteMapping("{chapterId}")
    public R deleteChapter(@PathVariable String chapterId){

        boolean flag = eduChapterService.deleteChapterById(chapterId);

        if (flag){
            return R.ok();
        }else {
            return R.error();
        }
    }
}

