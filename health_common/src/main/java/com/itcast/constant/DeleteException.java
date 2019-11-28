package com.itcast.constant;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/11/23 0:22
 * @description: 自定义异常
 */
public class DeleteException extends RuntimeException {


    public DeleteException() {
        super();
    }

    public DeleteException(String message) {
        super(message);
    }

    public DeleteException(String message, Throwable cause) {
        super(message, cause);
    }

    public DeleteException(Throwable cause) {
        super(cause);
    }

    protected DeleteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
