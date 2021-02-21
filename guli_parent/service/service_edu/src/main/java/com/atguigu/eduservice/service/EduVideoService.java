package com.atguigu.eduservice.service;

import com.atguigu.eduservice.entity.EduVideo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author xiaocai
 * @since 2021-01-22
 */
public interface EduVideoService extends IService<EduVideo> {

    void removeVideo(String courseId);
}
