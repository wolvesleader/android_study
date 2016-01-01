package com.quincy.mykeepingcar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewStub;

/**
 * Created by guocheng on 2015/6/25.
 */
public abstract class BaseActivity extends FragmentActivity {

    protected View loadingLayout;
    protected ViewStub no_network_layout;
    protected ViewStub no_record_layout;
    protected final int LIMIT_COUNT = 10;
    protected int mStart = 0;
    protected boolean mbJudgeNetState = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

     
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
       
    }

    
}
