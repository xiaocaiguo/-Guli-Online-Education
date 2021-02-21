package com.atguigu.servicebase.exceptionhandler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data//get、set方法
@AllArgsConstructor//有参构造
@NoArgsConstructor//无参构造方法
public class GuliException extends RuntimeException{
    private Integer code;//状态码
    private String msg;//异常信息

    @Override
    public String toString() {
        return "GuliException{" +
                "message=" + this.getMessage() +
                ", code=" + code +
                '}';
    }
}
