package com.wangning.helloworld;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by WangNing on 2017/12/8.
 */
public class DisplayMessageActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);
        Intent intent=getIntent();
        String msgName=intent.getStringExtra("EXTRA_MESSAGE_NAME");
        TextView textViewName=(TextView)findViewById(R.id.tvShowName);
        textViewName.setText(msgName);
        String msgPwd=intent.getStringExtra("EXTRA_MESSAGE_PWD");
        TextView textViewPwd=(TextView)findViewById(R.id.tvShowPwd);
        textViewPwd.setText(msgPwd); // 设置文本为message
    }

    public void rbClick(View view){
        int id = view.getId();
        switch(id)
        {
            case R.id.radioButton:
                Toast.makeText(getApplicationContext(), "选择功能1", Toast.LENGTH_SHORT).show();
                break;
            case R.id.radioButton2:
                Toast.makeText(getApplicationContext(), "选择功能2", Toast.LENGTH_SHORT).show();
                break;
            case R.id.radioButton3:
                Toast.makeText(getApplicationContext(), "选择功能3", Toast.LENGTH_SHORT).show();
                break;

        }
    }

}
