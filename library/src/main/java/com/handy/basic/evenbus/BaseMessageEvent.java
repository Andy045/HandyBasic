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
public abstract class BaseMessageEvent<T> {

    private boolean result;
    private String message;
    private T content;

    public BaseMessageEvent(boolean result) {
        this.result = result;
    }

    public BaseMessageEvent(@NonNull String message) {
        this.message = message;
    }

    public BaseMessageEvent(@NonNull String message, @NonNull T content) {
        this.message = message;
        this.content = content;
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
    public T getContent() {
        return content;
    }

    public BaseMessageEvent setContent(@NonNull T content) {
        this.content = content;
        return this;
    }
}