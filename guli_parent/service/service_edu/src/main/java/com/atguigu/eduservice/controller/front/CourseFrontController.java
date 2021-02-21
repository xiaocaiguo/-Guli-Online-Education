package com.atguigu.eduservice.controller.front;

import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.commonutils.vo.CourseWebVoOrder;
import com.atguigu.eduservice.client.OrderClient;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.frontvo.CourseFrontVo;
import com.atguigu.eduservice.entity.frontvo.CourseWebVo;
import com.atguigu.eduservice.entity.vo.ChapterVo;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduCourseService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("eduservice/coursefront")
public class CourseFrontController {
    @Autowired
    private EduCourseService eduCourseService;
    @Autowired
    private EduChapterService eduChapterService;
    @Autowired
    private OrderClient orderClient;

    @PostMapping("getFrontCourseList/{page}/{limit}")
    public R getFrontCourseList(@PathVariable Long page,@PathVariable Long limit,
                                @RequestBody(required = false) CourseFrontVo courseFrontVo){
        Page<EduCourse> pageParam = new Page<>(page,limit);
        Map<String,Object> map = eduCourseService.pageListCourse(pageParam,courseFrontVo);

//        //总页数
//        long pages = (long)map.get("pages");
//        if (pages<page || page<0){
//            throw new GuliException(20001,"已经是最后一页了");
//        }

        return R.ok().data(map);
    }

    @GetMapping("getCourseInfo/{courseId}")
    public R getCourseInfo(@PathVariable String courseId, HttpServletRequest request){

        CourseWebVo courseWebVo = eduCourseService.selectInfoWebById(courseId);

        List<ChapterVo> chapterVoList = eduChapterService.getChapterVideoByCourseId(courseId);

        boolean buyCourse = false;
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        if(memberId != null){
            //远程调用，判断课程是否被购买
            buyCourse = orderClient.isBuyCourse(memberId, courseId);
        }

        return R.ok().data("course", courseWebVo).data("chapterVoList", chapterVoList).data("isbuy",buyCourse);
    }

    @GetMapping("getCourseInfoNacos/{courseId}")
    public CourseWebVoOrder getCourseInfoNacos(@PathVariable String courseId){

        CourseWebVo courseWebVo = eduCourseService.selectInfoWebById(courseId);
        CourseWebVoOrder courseWebVoOrder = new CourseWebVoOrder();
        BeanUtils.copyProperties(courseWebVo,courseWebVoOrder);

        return courseWebVoOrder;
    }
}
