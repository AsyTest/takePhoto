package com.wws.very.takephotodemo.photo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import java.io.File;

/**
 * Created by Maibenben on 2017/7/1.
 */

public class PhotoGraphImpl {

    private File tempFile;
    private File output;
    private String PHOTO_FILE_NAME;

    private static final int PHOTO_REQUEST_CAREMA = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果



    private static final int IMAGE_REQUEST_CODE = 4;
    private static final int SELECT_PIC_KITKAT = 5;

    private Intent intent;

    private Activity mactivity;

    public PhotoGraphImpl(){

    }

    public PhotoGraphImpl(Activity activity){
        intent=new Intent();
        PHOTO_FILE_NAME=System.currentTimeMillis() + ".jpg";
        this.mactivity=activity;

    }

    /**
     * 拍照
     * @return
     */
    public void pictures() {
        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 判断存储卡是否可以用，可用进行存储
        if (hasSdcard()) {
//            new File(Environment.getExternalStorageDirectory(),
//                    PHOTO_FILE_NAME);
            // 从文件中创建uri
            tempFile=tempFile();
            Uri uri=null;
            if (Build.VERSION.SDK_INT >= 24) {
                uri = FileProvider.getUriForFile(mactivity, "com.wws.very.takephotodem.fileprovider", tempFile);
            } else {
                uri = Uri.fromFile(tempFile);
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CAREMA
        mactivity.startActivityForResult(intent, PHOTO_REQUEST_CAREMA);
        if (Build.VERSION.SDK_INT>=24){


        }else {

        }

    }


    /**
     * 相册
     * @return
     */
    public void album() {
        intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        intent.putExtra("return-data", true);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            mactivity.startActivityForResult(intent,SELECT_PIC_KITKAT);
        } else {
            mactivity.startActivityForResult(intent,IMAGE_REQUEST_CODE);
        }

    }

    public String returnUri(int requestCode,Intent data){
        String imgUri="";
        if (requestCode == IMAGE_REQUEST_CODE) {
//            // 从相册返回的数据
            if (data != null) {
//                // 得到图片的全路径
                Uri uri = data.getData();
                //crop(uri);
                imgUri=GetPhotoPathUtils.getPath(mactivity,uri);
            }
        }else if(requestCode == SELECT_PIC_KITKAT){
            if (data != null) {
                Uri uri = data.getData();
                //crop(uri);
                imgUri=GetPhotoPathUtils.getPath(mactivity,uri);
            }
        } else if (requestCode == PHOTO_REQUEST_CAREMA) {
            // 从相机返回的数据
            if (hasSdcard()) {
                //crop(Uri.fromFile(tempFile));
                imgUri=tempFile().toString();
                //ToastUtils.showContent(getActivity(), tempFile.toString());
            } else {
                ToastUtils.showContent(mactivity, "未找到存储卡，无法存储照片！");
            }
        } else {
            if (requestCode == PHOTO_REQUEST_CUT) {
                // 从剪切图片返回的数据
//                if (data != null) {
//                    Bundle bundle=data.getExtras();
//                    if (bundle!=null) {
//                        Bitmap bitmap = data.getParcelableExtra("data");
//                        if (imgUri != null && !"".equals(imgUri)) {
//
//                        }
//                    }
//                }
                try {
                    // 将临时文件删除
                    //tempFile.delete();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
        return imgUri;
    }

    /*
    * 剪切图片
    */
    private void crop(Uri uri) {
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
//        intent.setDataAndType(uri, "image/*");
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            String url= GetPhotoPathUtils.getPath(mactivity,uri);
            intent.setDataAndType(Uri.fromFile(new File(url)), "image/*");
        }else{
            intent.setDataAndType(uri, "image/*");
        }
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);

        intent.putExtra("outputFormat", "JPEG");// 图片格式
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", true);
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CUT
        mactivity.startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }


    /*
     * 判断sdcard是否被挂载
     */
    private boolean hasSdcard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 创建一个文件夹存放相册
     */

    private File tempFile(){
        //获取内存路径并创建一个文件夹
        File file=new File(Environment.getExternalStorageDirectory(),"photoAlums");
        if(!file.exists()){
            file.mkdir();
        }
        output=new File(file,System.currentTimeMillis()+".jpg");
        try {
            if (output.exists()) {
                output.delete();
            }
            output.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }


}
