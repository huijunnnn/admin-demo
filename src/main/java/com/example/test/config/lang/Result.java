package com.example.test.config.lang;

import lombok.Data;

import java.io.Serializable;

//统一一个结果返回类，这样前后端交互的时候有统一标准
//约定结果返回的数据是正常的或者遇到异常了
//这里我们用到了一个Result的类，这个用于我们的异步统一返回的结果封装。一般来说，结果里面有几个要素必要的
//是否成功，可用code表示（如200表示成功，400表示异常）
//结果消息
//结果数据
@Data
public class Result implements Serializable {
    private int code;//200 是正常，非200 表示异常
    private String msg;
    private Object data;

    public static Result success(int code, String msg, Object data) {
        Result r = new Result();
        r.setCode(code);
        r.setMsg(msg);
        r.setData(data);
        return r;
    }

    public static Result success(Object data) {
        return success(200, " 操作成功", data);
    }

    public static Result fail(String msg) {
        return fail(400, msg, null);
    }

    public static Result fail(int code, String msg, Object data) {
        Result r = new Result();
        r.setCode(code);
        r.setMsg(msg);
        r.setData(data);
        return r;
    }
}
