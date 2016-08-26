package com.example.gxl.photofinishing;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;

/**
 * 刚启动进入的Activity界面，用来判断进入哪个页面
 */
public class WelconeActivity extends Activity {
    private final int GO_HOME=1000;
    private final int GO_GUIDE=1001;

    public Handler handler=new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
           switch (msg.what)
           {
               case GO_HOME:
                   Intent HomeIntent=new Intent(WelconeActivity.this,LoadPhotoToViewActivity.class);
                   startActivity(HomeIntent);
                   finish();
                   break;
               case GO_GUIDE:
                   Intent GuideIntent=new Intent(WelconeActivity.this,GuideActivity.class);
                   startActivity(GuideIntent);
                   finish();
                   break;
           }
        }
    };
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.welcome_layout);
        if(is_first_in()){
            handler.sendEmptyMessageDelayed(GO_GUIDE, 100);
        }else
        {
            handler.sendEmptyMessageDelayed(GO_HOME,100);
        }
    }

    public Boolean is_first_in()
    {
        SharedPreferences sharedPreferences=getSharedPreferences("guide",MODE_PRIVATE);
        Boolean result=sharedPreferences.getBoolean("first",true);
        change_first_in();
        return result;
    }

    public void change_first_in()
    {
        SharedPreferences sharedPreferences=getSharedPreferences("guide", MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putBoolean("first",false);
        editor.commit();
    }
}
