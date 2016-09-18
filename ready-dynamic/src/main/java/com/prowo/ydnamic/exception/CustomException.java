package com.prowo.ydnamic.exception;

/**
 * 异常基类Throwable.java的public synchronized native Throwable fillInStackTrace()方法
 * 方法介绍:
 * Fills in the execution stack trace. This method records within this Throwable object information about the current state of the stack frames for the current thread.
 * 性能开销在于:
 * 1. 是一个synchronized方法(主因)
 * 2. 需要填充线程运行堆栈信息
 * 自定义改进的Exception对象 覆写了 fillInStackTrace方法
 * 1. 不填充stack
 * 2. 取消同步
 */
public class CustomException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String message;

    public CustomException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}