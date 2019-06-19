package com.handy.basic.evenbus;

import android.support.annotation.NonNull;

/**
 * EvenBus 消息体对象
 *
 * @author LiuJie https://github.com/Handy045
 * @description EvenBus消息体对象父类
 * @date Created in 2018/12/20 16:49
 * @modified By liujie
 */
public abstract class BaseMessageEvent {

    private boolean result;
    private String message;
    private Object object;

    public BaseMessageEvent(boolean result) {
        this.result = result;
    }

    public BaseMessageEvent(@NonNull String message) {
        this.message = message;
    }

    public BaseMessageEvent(@NonNull String message, @NonNull Object object) {
        this.message = message;
        this.object = object;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    @NonNull
    public String getMessage() {
        return message;
    }

    public BaseMessageEvent setMessage(@NonNull String message) {
        this.message = message;
        return this;
    }

    @NonNull
    public Object getObject() {
        return object;
    }

    public BaseMessageEvent setObject(@NonNull Object object) {
        this.object = object;
        return this;
    }
}