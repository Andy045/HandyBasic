package com.handy.basic.mvp;

import android.support.annotation.NonNull;

/**
 * FileName
 *
 * @author LiuJie https://github.com/Handy045
 * @description File Description
 * @date Created in 2019-06-13 20:37
 * @modified By liujie
 */
public interface IView<I extends BasePresenter> {

    void setIPresenter(@NonNull I iPresenter);
}
