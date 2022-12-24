package com.bytedance.fast_tickets.utils;

public class Result<T> {
    private boolean flag; //是否成功
    private Integer code; //返回码
    private String message; //返回消息
    private T data; //返回数据

    public static final Result<Void> SUCCESS_RESULT = new Result<>(true, StatusCode.OK, "success");
    public static final Result<Void> ERROR_RESULT = new Result<>(true, StatusCode.ERROR, "error");
    public static final Result<Void> BUYTICKET_SUCCESS = new Result<>(true, StatusCode.OK, "BUYTICKET SUCCESS");
    public static final Result<Void> BUYTICKET_PENDING = new Result<>(true, StatusCode.OK, "BUYTICKET PENDING");
    public static final Result<Void> BUYTICKET_FAIL = new Result<>(true, StatusCode.OK, "BUYTICKET FAIL");
    public static final Result<Void> BUYTICKET_NO_LEFT = new Result<>(true, StatusCode.OK, "NO LEFT");

    public static <V> Result<V> okResult(V v) {
        return new Result<>(true, 20000, "success", v);
    }

    public Result(boolean flag, Integer code, String message, Object data) {
        this.flag = flag;
        this.code = code;
        this.message = message;
        this.data = (T) data;
    }

    public Result(boolean flag, Integer code, String message) {
        this.flag = flag;
        this.code = code;
        this.message = message;
    }

    public Result() {
        this.flag = true;
        this.code = StatusCode.OK;
        this.message = "操作成功!";
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static class StatusCode {
        public static final int OK = 200;//成功
        public static final int ERROR = 201;//失败
        public static final int LOGINERROR = 202;//用户名或密码错误
        public static final int ACCESSERROR = 203;//权限不足
        public static final int REMOTEERROR = 204;//远程调用失败
        public static final int REPERROR = 205;//重复操作
        public static final int NOTFOUNDERROR = 206;//没有对应的抢购数据
        public static final int REGISTERERROR = 207;//注册失败
    }
}