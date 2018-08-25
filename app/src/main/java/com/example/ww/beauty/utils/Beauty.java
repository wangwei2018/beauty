package com.example.ww.beauty.utils;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import static android.content.ContentValues.TAG;

/**
 * 美颜算法的封装
 */
public class Beauty {
    public static Bitmap bigEyes(Bitmap bitmap, int PointX, int PointY, int radius, int level) {
//        获取图片的宽高以确定边界
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();

//        确定边界
        int left = PointX - radius < 0 ? 0 : PointX - radius;
        int right = PointX + radius > bitmapWidth ? bitmapWidth : PointX + radius;
        int top = PointY - radius < 0 ? 0 : PointY - radius;
        int bottom = PointY + radius > bitmapHeight ? bitmapHeight : PointY + radius;

        Log.e(TAG, "left: " + left);
        Log.e(TAG, "right: " + right);
        Log.e(TAG, "top: " + top);
        Log.e(TAG, "bottom: " + bottom);

        Bitmap newBitmap = bitmap.copy(Bitmap.Config.RGB_565, true);

        int powerRadius = radius * radius;
        int count = 0;

//        遍历圆形区域内的每一点
        for (int y = top; y <= bottom; y++) {
            int offsetY = y - PointY;
            for (int x = left; x <= right; x++) {
                int offsetX = x - PointX;
                int distance = offsetX * offsetX + offsetY * offsetY;
                if (distance < powerRadius) {

                    if (level == 1) {
                        if (x <= PointX && y <= PointY)
                            newBitmap.setPixel(x, y - 1, bitmap.getPixel(x, y));
                        else if (x <= PointX && y > PointY)
                            newBitmap.setPixel(x, y + 1, bitmap.getPixel(x, y));
                        else if (x > PointX && y <= PointY)
                            newBitmap.setPixel(x, y - 1, bitmap.getPixel(x, y));
                        else if (x > PointX && y > PointY)
                            newBitmap.setPixel(x, y + 1, bitmap.getPixel(x, y));
                    } else if (level == 2) {
                        if (x <= PointX && y <= PointY)
                            newBitmap.setPixel(x, y - 2, bitmap.getPixel(x, y));
                        else if (x <= PointX && y > PointY)
                            newBitmap.setPixel(x, y + 2, bitmap.getPixel(x, y));
                        else if (x > PointX && y <= PointY)
                            newBitmap.setPixel(x, y - 2, bitmap.getPixel(x, y));
                        else if (x > PointX && y > PointY)
                            newBitmap.setPixel(x, y + 2, bitmap.getPixel(x, y));
                    }


                }

            }

        }
        return newBitmap;
    }


    public static Bitmap smallFace(Bitmap bitmap, int PointX, int PointY, int radius,int level){
//        获取图片的宽高以确定边界
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();

//        确定边界
        int left = PointX - radius < 0 ? 0 : PointX - radius;
        int right = PointX + radius > bitmapWidth ? bitmapWidth : PointX + radius;
        int top = PointY - radius < 0 ? 0 : PointY - radius;
        int bottom = PointY + radius > bitmapHeight ? bitmapHeight : PointY + radius;

        Log.e(TAG, "left: " + left);
        Log.e(TAG, "right: " + right);
        Log.e(TAG, "top: " + top);
        Log.e(TAG, "bottom: " + bottom);

        Bitmap newBitmap = bitmap.copy(Bitmap.Config.RGB_565, true);

        int powerRadius = radius * radius;


        for(int y=PointY;y<=bottom;y++)
            for(int x=left;x<=right;x++){
                if(level==1){
                    if(x<PointX)
                        newBitmap.setPixel(x,y,bitmap.getPixel(x-1,y));
                    else
                        newBitmap.setPixel(x,y,bitmap.getPixel(x+1,y));
                }else if(level==2){
                    if(x<PointX)
                        newBitmap.setPixel(x,y,bitmap.getPixel(x-2,y));
                    else
                        newBitmap.setPixel(x,y,bitmap.getPixel(x+2,y));
                }

            }

        return newBitmap;
    }

}

