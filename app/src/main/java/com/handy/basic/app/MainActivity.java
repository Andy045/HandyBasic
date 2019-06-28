package com.handy.basic.app;

import android.os.Bundle;

import com.blankj.utilcode.util.ToastUtils;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.tv).setOnClickListener(v -> ToastUtils.showShort("AAAAAAA"));
    }
}
