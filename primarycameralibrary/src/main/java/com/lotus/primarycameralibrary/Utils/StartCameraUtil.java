package com.lotus.primarycameralibrary.Utils;

import java.io.File;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

public class StartCameraUtil {
    /**
     * 启动手机自带相机应用工具
     * @param activity
     * @param code 自定义onActivity处理编号
     * @param dirName 图片存储文件夹名
     */
    public static void cameraLaunch(Activity activity,int code,String dirName){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String status = Environment.getExternalStorageState();
        String localTempImgFileName = System.currentTimeMillis() + ".jpg";
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            try {
                File dir = new File(Environment.getExternalStorageDirectory() + "/" + dirName);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File file = new File(dir, localTempImgFileName);
                Uri uri = Uri.fromFile(file);
                intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                activity.startActivityForResult(intent, code);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(activity, "没有找到储存目录", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(activity, "没有储存卡", Toast.LENGTH_LONG).show();
        }
    }
}
