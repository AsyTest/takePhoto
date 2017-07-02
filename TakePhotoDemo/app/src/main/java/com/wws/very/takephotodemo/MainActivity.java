package com.wws.very.takephotodemo;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.wws.very.takephotodemo.photo.PhotoGraphImpl;
import com.wws.very.takephotodemo.photo.ToastUtils;

public class MainActivity extends Activity implements View.OnClickListener{

    private PhotoGraphImpl photoGraph;

    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 6;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE2 = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        photoGraph=new PhotoGraphImpl(this);

        Button takePhoto= (Button) findViewById(R.id.take_photo);
        Button takeAlbum= (Button) findViewById(R.id.take_album);

        takePhoto.setOnClickListener(this);
        takeAlbum.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.take_photo:
                //第二个参数是需要申请的权限
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_CALL_PHONE2);
                    //权限还没有授予，需要在这里写申请权限的代码
                }else {
                    //权限已经被授予，在这里直接写要执行的相应方法即可
                    photoGraph.pictures();
                }

                break;
            case R.id.take_album:
                photoGraph.album();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        String imguri=photoGraph.returnUri(requestCode,data);
        ToastUtils.showContent(this,imguri);
    }

}
