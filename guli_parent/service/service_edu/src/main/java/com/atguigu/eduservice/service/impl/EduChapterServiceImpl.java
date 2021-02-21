package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.entity.vo.ChapterVo;
import com.atguigu.eduservice.entity.vo.VideoVo;
import com.atguigu.eduservice.mapper.EduChapterMapper;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduVideoService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author xiaocai
 * @since 2021-01-22
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {

    @Autowired
    private EduVideoService eduVideoService;

    @Override
    public List<ChapterVo> getChapterVideoByCourseId(String courseId) {
        //1查询courseId对应的章节信息
        QueryWrapper wrapperChapter = new QueryWrapper();
        wrapperChapter.eq("course_id",courseId);
        List<EduChapter> eudChapterList = baseMapper.selectList(wrapperChapter);

        //2查询courseId对应的小节信息
        QueryWrapper wrapperVideo = new QueryWrapper();
        wrapperVideo.eq("course_id",courseId);
        List<EduVideo> eduVideoList = eduVideoService.list(wrapperVideo);

        //3将章节信息封装进返回的数据中
        List<ChapterVo> finalChapterVoList = new ArrayList<>();

        for (int i = 0; i < eudChapterList.size(); i++) {
            EduChapter eduChapter = eudChapterList.get(i);
            ChapterVo chapterVo = new ChapterVo();
            BeanUtils.copyProperties(eduChapter,chapterVo);

            //4将小节信息封装进chapterVo中
            List<VideoVo> finalVideoVoList = new ArrayList<>();
            for (int m = 0; m < eduVideoList.size(); m++) {

                EduVideo eduVideo = eduVideoList.get(m);
                if (eduVideo.getChapterId().equals(chapterVo.getId())){
                    VideoVo videoVo = new VideoVo();
                    BeanUtils.copyProperties(eduVideo,videoVo);

                    finalVideoVoList.add(videoVo);
                }
            }

            chapterVo.setChildren(finalVideoVoList);
            finalChapterVoList.add(chapterVo);
        }

        return finalChapterVoList;
    }

    @Override
    public boolean deleteChapterById(String chapterId) {
        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("chapter_id",chapterId);
        int count = eduVideoService.count(wrapper);
        if (count > 0 ){
            throw new GuliException(20001,"该章节下有小节，不能删除");
        }else {
            int result = baseMapper.deleteById(chapterId);
            return result>0;
        }
    }

    @Override
    public void removeChapter(String courseId) {
        QueryWrapper<EduChapter> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        baseMapper.delete(wrapper);
    }

}
