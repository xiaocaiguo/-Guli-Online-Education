package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.client.VodClient;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.service.EduVideoService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author xiaocai
 * @since 2021-01-22
 */
@RestController
@RequestMapping("/eduservice/video")
public class EduVideoController {

    @Autowired
    private EduVideoService eduVideoService;
    @Autowired
    private VodClient vodClient;

    @GetMapping("getVideo/{videoId}")
    public R getVideo(@PathVariable String videoId){
        EduVideo video = eduVideoService.getById(videoId);
        return R.ok().data("video",video);
    }

    @PostMapping("addVideo")
    public R addVideo(@RequestBody EduVideo eduVideo){

        eduVideoService.save(eduVideo);
        return R.ok();
    }

    @PostMapping("updateVideo")
    public R updateVideo(@RequestBody EduVideo eduVideo){

        eduVideoService.updateById(eduVideo);
        return R.ok();
    }

    @DeleteMapping("{videoId}")
    public R deleteVideo(@PathVariable String videoId){

        EduVideo eduVideo = eduVideoService.getById(videoId);
        String videoSourceId = eduVideo.getVideoSourceId();

        if(!StringUtils.isEmpty(videoSourceId));{
            //删除阿里云中的视频
            R result = vodClient.removeAlyVideo(videoSourceId);
            if(result.getCode() ==20000){
                throw new GuliException(20001,"删除视频失败，熔断器");
            }

        }
        eduVideoService.removeById(videoId);

        return R.ok();
    }

}

