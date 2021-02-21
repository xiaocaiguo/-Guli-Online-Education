package com.atguigu.demo.execl;

import com.alibaba.excel.EasyExcel;

import java.util.ArrayList;
import java.util.List;

public class TestEasyExcel {

    public static void main(String[] args){

        String filename = "D:/test.xls";

        //EasyExcel.write(filename,DemoData.class).sheet("学生表").doWrite(getList());
        EasyExcel.read(filename,DemoData.class,new ExcelListener()).sheet().doRead();
    }

    private static List<DemoData> getList(){

        List<DemoData> list = new ArrayList<>();
        for(int i = 0; i<10;i++){
            DemoData demoData = new DemoData();
            demoData.setSno(i);
            demoData.setSname("lucy"+i);
            list.add(demoData);
        }

        return list;
    }
}
