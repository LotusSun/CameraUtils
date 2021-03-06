package com.lotus.primarycameralibrary.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Environment;
import android.text.TextPaint;
import android.text.TextUtils;

/**
 * 添加时间水印工具
 * @author lotus
 *
 */
public class WaterMarkUtil {
    private static final int COLOR = Color.parseColor("#f7c215");

    /**
     * 保存已经打好水印的照片工具（处理后的图片会放在原文件夹下的MarkedImage文件夹中）
     * @param activity
     * @param fileName 原图文件名
     * @param fileDir 原图文件夹
     */
    public static String SaveUpBitmapUtil(Activity activity, String fileName, String fileDir) {
        String path = Environment.getExternalStorageDirectory()+ "/" +fileDir;
        Bitmap temp = ImageDimenCompressUtil.decodeBitmapFromFile(path + "/" + fileName, TransPixelUtil.dip2px(activity, 120), TransPixelUtil.dip2px(activity, 160));
        File dir = new File(path + "/MarkedImage");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File f = new File(path + "/MarkedImage/" + fileName);
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            String timeMills = fileName.substring(0, fileName.length() - 4);
            Bitmap newMap = drawTextOnBitmap(activity, temp, TimeMill2DateUtil.TimeMill2Date(timeMills));
            newMap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path + "/MarkedImage/" + fileName;
    }

    /**
     * 在原bitmap上添加文字水印
     * @param acitvity
     * @param srcBitmap 原图片
     * @param text 索要添加的文字
     * @return
     */
    public static Bitmap drawTextOnBitmap(Activity acitvity, Bitmap srcBitmap, String text) {
        if (srcBitmap == null) {
            return srcBitmap;
        }
        if (TextUtils.isEmpty(text) || TextUtils.isEmpty(text.trim())) {
            return srcBitmap;
        }

        Rect textRect = getTextRect(text, acitvity);
        int srcWidth = srcBitmap.getWidth();
        int srcHeight = srcBitmap.getHeight();

        Bitmap newBmp = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBmp);
        canvas.drawBitmap(srcBitmap, 0, 0, null);

        String familyName = "Arial";
        Typeface font = Typeface.create(familyName, Typeface.NORMAL);
        TextPaint textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(COLOR);
        textPaint.setTypeface(font);
        textPaint.setTextSize(TransPixelUtil.sp2px(acitvity, 12, TransPixelUtil.NUMBER_OR_CHARACTER));
        int margin = 5;
        int y = srcHeight - margin;
        int x = srcWidth - textRect.width() - margin;
        canvas.drawText(text, x, y, textPaint);
        canvas.save(Canvas.ALL_SAVE_FLAG);// 保存
        canvas.restore();// 存储
        return newBmp;
    }

    private static Rect getTextRect(String text, Activity activity) {
        if (TextUtils.isEmpty(text) || TextUtils.isEmpty(text.trim())) {
            return null;
        }
        TextPaint textPaint = new TextPaint();
        textPaint.setTextSize(TransPixelUtil.sp2px(activity, 12, TransPixelUtil.NUMBER_OR_CHARACTER));
        Rect rect = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), rect);

        return rect;
    }
}
