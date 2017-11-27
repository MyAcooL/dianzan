package com.acool.zhibodianzan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private LoveLayout mLoveLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoveLayout = (LoveLayout) findViewById(R.id.love_layout);
        Toast.makeText(this, "测试", Toast.LENGTH_SHORT).show();
    }


    public void add(View view) {
        mLoveLayout.addLove();
    }
}
