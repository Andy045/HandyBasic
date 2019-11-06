package com.handy.basic.mvp

/**
 * @title: IView
 * @projectName HandyBasicKT
 * @description: view层通用接口
 * @author LiuJie https://github.com/Handy045
 * @date Created in 2019-10-11 16:30
 */
interface IView<P : BasePresenter> {
    fun initPresenter(): P?
}