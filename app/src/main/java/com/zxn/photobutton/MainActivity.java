package com.zxn.photobutton;

import android.os.Bundle;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.zxn.camerabutton.CameraButton;


public class MainActivity extends AppCompatActivity {


    private CameraButton buttontake;
    private CameraButton button;
    private CameraButton cbtn_bottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.buttontake = (CameraButton) findViewById(R.id.button_take);
        this.cbtn_bottom = (CameraButton) findViewById(R.id.cbtn_bottom);
        button = (CameraButton) findViewById(R.id.normal_btn);
        buttontake.setOnProgressTouchListener(new CameraButton.OnProgressTouchListener() {
            @Override
            public void onClick(CameraButton photoButton) {
                Toast.makeText(MainActivity.this,"单机",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(CameraButton photoButton) {
                Toast.makeText(MainActivity.this,"长按",Toast.LENGTH_SHORT).show();
                buttontake.start();

            }

            @Override
            public void onLongClickUp(CameraButton photoButton) {
                onFinish();
            }


            @Override
            public void onFinish() {
                Toast.makeText(MainActivity.this,"录制结束",Toast.LENGTH_SHORT).show();
            }
        });

        button.setOnProgressTouchListener(new CameraButton.OnProgressTouchListener() {
            @Override
            public void onClick(CameraButton photoButton) {
                Toast.makeText(MainActivity.this,"单机",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(CameraButton photoButton) {
                Toast.makeText(MainActivity.this,"长按",Toast.LENGTH_SHORT).show();
                button.start();

            }

            @Override
            public void onLongClickUp(CameraButton photoButton) {
                onFinish();
            }


            @Override
            public void onFinish() {
                Toast.makeText(MainActivity.this,"录制结束",Toast.LENGTH_SHORT).show();
            }
        });

        cbtn_bottom.setOnProgressTouchListener(new CameraButton.OnProgressTouchListener() {
            @Override
            public void onClick(CameraButton photoButton) {
                Toast.makeText(MainActivity.this,"单机",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(CameraButton photoButton) {
                Toast.makeText(MainActivity.this,"长按",Toast.LENGTH_SHORT).show();
                cbtn_bottom.start();
            }

            @Override
            public void onLongClickUp(CameraButton photoButton) {
                onFinish();
            }


            @Override
            public void onFinish() {
                Toast.makeText(MainActivity.this,"录制结束",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
