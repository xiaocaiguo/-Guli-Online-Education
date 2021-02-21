package com.atguigu.eduservice.service.impl;

import com.alibaba.excel.EasyExcel;
import com.atguigu.eduservice.Listener.SubjectExcelListener;
import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.entity.excel.SubjectData;
import com.atguigu.eduservice.entity.subject.OneSubject;
import com.atguigu.eduservice.entity.subject.TwoSubject;
import com.atguigu.eduservice.mapper.EduSubjectMapper;
import com.atguigu.eduservice.service.EduSubjectService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author xiaocai
 * @since 2021-01-21
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {

    //添加课程分类
    @Override
    public void saveSubject(MultipartFile file, EduSubjectService subjectService) {

        try {
            InputStream in = file.getInputStream();
            EasyExcel.read(in, SubjectData.class,new SubjectExcelListener(subjectService)).sheet().doRead();

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public List<OneSubject> getAllOneTwoSubject() {

        List<OneSubject> finalSubjectList = new ArrayList<>();
        //先把一级分类和二级分类查询出来
        QueryWrapper oneWrapper = new QueryWrapper();
        oneWrapper.eq("parent_id","0");
        List<EduSubject> OneSubjectList = baseMapper.selectList(oneWrapper);

        QueryWrapper twoWrapper = new QueryWrapper();
        oneWrapper.ne("parent_id",'0');
        List<EduSubject> twoSubjectList = baseMapper.selectList(twoWrapper);
        //再将一级分类封装近数据
        for (int i = 0 ;i < OneSubjectList.size(); i++){
            //获取一级分类
            EduSubject oneEduSubject = OneSubjectList.get(i);
            //将一级分类数据封装到oneSubject中
            OneSubject oneSubject = new OneSubject();
            BeanUtils.copyProperties(oneEduSubject,oneSubject);

            //再将二级分类封装进数据
            List<TwoSubject> finalTwoSubjectList = new ArrayList<>();
            for (int j = 0;j < twoSubjectList.size();j++){
                EduSubject twoEduSubject = twoSubjectList.get(j);

                if (oneEduSubject.getId().equals(twoEduSubject.getParentId())){
                    TwoSubject twoSubject = new TwoSubject();
                    BeanUtils.copyProperties(twoEduSubject,twoSubject);

                    finalTwoSubjectList.add(twoSubject);
                }

                oneSubject.setChildren(finalTwoSubjectList);
            }

            finalSubjectList.add(oneSubject);
        }




        return finalSubjectList;
    }
}
