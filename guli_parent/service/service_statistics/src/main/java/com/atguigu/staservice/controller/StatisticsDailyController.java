package com.atguigu.staservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.staservice.service.StatisticsDailyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 前端控制器
 * </p>
 *
 * @author xiaocai
 * @since 2021-02-10
 */
@RestController
@RequestMapping("/staservice/statistics-daily")
public class StatisticsDailyController {

    @Autowired
    private StatisticsDailyService statisticsDailyService;

    //统计注册人数
    @PostMapping("countRegister/{day}")
    public R countRegister(@PathVariable String day){

        statisticsDailyService.createStatisticsByDay(day);
        return R.ok();
    }

    //显示图表数据
    @GetMapping("showData/{type}/{begin}/{end}")
    public R showData(@PathVariable String type,@PathVariable String begin,
                      @PathVariable String end){
        Map<String ,Object> map = statisticsDailyService.getShowData(type,begin,end);
        return R.ok().data(map);
    }


}

