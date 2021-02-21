package com.atguigu.orderservice.client;

import com.atguigu.commonutils.vo.CourseWebVoOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(name = "service-edu",fallback = EduClientImpl.class)
public interface EduClient {
    @GetMapping("/eduservice/coursefront/getCourseInfoNacos/{courseId}")
    public CourseWebVoOrder getCourseInfoNacos(@PathVariable("courseId") String courseId);
}
