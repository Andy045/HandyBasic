package com.handy.basic.app

import android.os.Bundle
import android.view.View
import com.handy.basic.base.BaseActivity
import com.handy.basic.mvp.BasePresenter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<BasePresenter>(), View.OnClickListener {

    init {
        this.isCheckPermissions = true
    }

    var number = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tv0.setOnClickListener(this)
        tv1.setOnClickListener(this)
        tv2.setOnClickListener(this)
        tv3.setOnClickListener(this)
        tv4.setOnClickListener(this)
        tv5.setOnClickListener(this)
        tv6.setOnClickListener(this)
        tv7.setOnClickListener(this)
        tv8.setOnClickListener(this)
        tv9.setOnClickListener(this)
        tvAc.setOnClickListener(this)
        tvResidual.setOnClickListener(this)
        tvBack.setOnClickListener(this)
        tvDivide.setOnClickListener(this)
        tvmultiply.setOnClickListener(this)
        tvsub.setOnClickListener(this)
        tvadd.setOnClickListener(this)
        tvpoint.setOnClickListener(this)
        tvequal.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v) {
                tv0 -> {
                    number += "0"
                    tvResult.text = number
                }
                tv1 -> {
                    number += "1"
                    tvResult.text = number
                }
                tv2 -> {
                    number += "2"
                    tvResult.text = number
                }
                tv3 -> {
                    number += "3"
                    tvResult.text = number
                }
                tv4 -> {
                    number += "4"
                    tvResult.text = number
                }
                tv5 -> {
                    number += "5"
                    tvResult.text = number
                }
                tv6 -> {
                    number += "6"
                    tvResult.text = number
                }
                tv7 -> {
                    number += "7"
                    tvResult.text = number
                }
                tv8 -> {
                    number += "8"
                    tvResult.text = number
                }
                tv9 -> {
                    number += "9"
                    tvResult.text = number
                }
            }
        }
    }
}
