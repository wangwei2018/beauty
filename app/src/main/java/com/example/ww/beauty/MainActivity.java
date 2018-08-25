package com.example.ww.beauty;



import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ww.beauty.utils.Beauty;
import com.example.ww.beauty.utils.BitmapByteUtil;
import com.example.ww.beauty.utils.CommonOperate;
import com.example.ww.beauty.utils.HandleResponse;
import com.example.ww.beauty.utils.Response;
import com.example.ww.beauty.utils.Content;
import com.github.dfqin.grantor.PermissionListener;
import com.github.dfqin.grantor.PermissionsUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;

public class MainActivity extends Activity implements View.OnClickListener{

    private static final String TAG ="resizePhoto" ;
    private ImageView mPhoto;
    private Button mDetect, mGetImg;
    private TextView mTip;
    private View mWaiting;

    private Canvas canvas;
    private Paint mPaint;
    private String mCurrentPhotoStr;
    private Bitmap mBitmapPhoto;
    private static final int PICK_CODE = 0x110;
    private static final int MSG_SUCCESS = 0x111;
    private static final int MSG_ERROR = 0x112;
    private int image_width;
    private int image_height;
    private String img_path;
    private Bitmap eye_bitmap;
    private Bitmap face_bitmap;
    private int left_eye_x;
    private int left_eye_y;
    private int eye_radius;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==1){
                mPhoto.setImageBitmap(eye_bitmap);
            }
            if(msg.what==2){
                mDetect.setVisibility(View.VISIBLE);
                bt_big.setVisibility(View.VISIBLE);
                bt_small1.setVisibility(View.VISIBLE);
                bt_small2.setVisibility(View.VISIBLE);
            }
            if(msg.what==3){
                mPhoto.setImageBitmap(face_bitmap);

            }
        }
    };
    private CommonOperate commonOperate;
    private int right_eye_x;
    private int right_eye_y;
    private Button bt_big;
    private Button bt_small1;
    private int face_x;
    private int face_y;
    private int face_radius;
    private Button bt_small2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PermissionsUtil.requestPermission(this, new PermissionListener() {
            @Override
            public void permissionGranted(@NonNull String[] permission) {
                mPhoto = (ImageView) findViewById(R.id.id_photo);
                mDetect = (Button) findViewById(R.id.id_detect);
                mGetImg = (Button) findViewById(R.id.id_getImg);
                bt_small1 = (Button) findViewById(R.id.bt_small1);
                bt_small2 = (Button) findViewById(R.id.bt_small2);
                bt_big = (Button) findViewById(R.id.bt_big);
                mWaiting = findViewById(R.id.id_waiting);
                mDetect.setOnClickListener(MainActivity.this);
                mGetImg.setOnClickListener(MainActivity.this);
                bt_big.setOnClickListener(MainActivity.this);
                bt_small1.setOnClickListener(MainActivity.this);
                bt_small2.setOnClickListener(MainActivity.this);

                bt_small1.setVisibility(View.GONE);
                bt_big.setVisibility(View.GONE);
                mDetect.setVisibility(View.GONE);
                bt_small2.setVisibility(View.GONE);

                mPaint = new Paint();
            }

            @Override
            public void permissionDenied(@NonNull String[] permission) {

            }
        }, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PICK_CODE) {
            if (data != null) {

                Uri uri = data.getData();

                img_path = String.valueOf(uri);


                Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                cursor.moveToFirst();

                int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                mCurrentPhotoStr = cursor.getString(index);
                cursor.close();

                resizePhoto();
                mPhoto.setImageBitmap(mBitmapPhoto);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        commonOperate = new CommonOperate(Content.API_KEY, Content.API_SECRET, false);
                        try {
                            Response response = commonOperate.detectByte(BitmapByteUtil.bitmapToByte(mBitmapPhoto), 1, null);
                            Log.e(TAG, "run: "+response.getStatus());
                            String values = new String(response.getContent());
                            eye_radius = HandleResponse.eye_radius(values);
                            Log.e(TAG, "眼睛半径："+ eye_radius);
                            HashMap hashMap = HandleResponse.left_eye_center(values);
                            left_eye_x = (int) hashMap.get("x");
                            left_eye_y = (int) hashMap.get("y");
                            HashMap map = HandleResponse.right_eye_center(values);
                            right_eye_x = (int)map.get("x");
                            right_eye_y = (int)map.get("y");
                            HashMap map1 = HandleResponse.face_center(values);
                            face_x = (int) map1.get("x");
                            face_y = (int) map1.get("y");
                            face_radius = HandleResponse.face_radius(values);
                            eye_bitmap=null;
                            face_bitmap=null;
                            Message message = new Message();
                            message.what=2;
                            handler.sendMessage(message);


                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e(TAG, "run: "+"有异常");
                        }
                    }
                }).start();

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void resizePhoto() {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(mCurrentPhotoStr, options);

        double ratio = Math.max(options.outWidth * 1.0d / 1024f, options.outHeight * 1.0d / 1024f);
        image_width = options.outWidth;
        image_height = options.outHeight;
        Log.e(TAG, ""+ image_width);
        Log.e(TAG, ""+ image_height);
        options.inSampleSize = (int) Math.ceil(ratio);
        options.inJustDecodeBounds = false;

        mBitmapPhoto = BitmapFactory.decodeFile(mCurrentPhotoStr, options);
    }


    @Override
    public void onClick(View view) {
        Message msg = new Message();
        switch (view.getId()) {
            case R.id.id_getImg:
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_CODE);

                break;
            case R.id.id_detect:
                if(face_bitmap!=null){
                    eye_bitmap = Beauty.bigEyes(this.face_bitmap, right_eye_x, right_eye_y, eye_radius,1);
                    eye_bitmap = Beauty.bigEyes(this.eye_bitmap, left_eye_x, left_eye_y, eye_radius,1);
                }else {
                    eye_bitmap = Beauty.bigEyes(this.mBitmapPhoto, right_eye_x, right_eye_y, eye_radius,1);
                    eye_bitmap = Beauty.bigEyes(this.eye_bitmap, left_eye_x, left_eye_y, eye_radius,1);
                }
                msg.what=1;
                handler.sendMessage(msg);
                break;
            case R.id.bt_big:
                if(face_bitmap!=null){
                    eye_bitmap = Beauty.bigEyes(this.face_bitmap, right_eye_x, right_eye_y, eye_radius,2);
                    eye_bitmap = Beauty.bigEyes(this.eye_bitmap, left_eye_x, left_eye_y, eye_radius,2);
                    msg.what=1;
                    handler.sendMessage(msg);
                }else {
                    eye_bitmap = Beauty.bigEyes(this.mBitmapPhoto, right_eye_x, right_eye_y, eye_radius,2);
                    eye_bitmap = Beauty.bigEyes(this.eye_bitmap, left_eye_x, left_eye_y, eye_radius,2);
                    msg.what=1;
                    handler.sendMessage(msg);
                }
                break;

            case R.id.bt_small1:
                if(eye_bitmap!=null){
                    face_bitmap=Beauty.smallFace(eye_bitmap, face_x, face_y, face_radius,1);
                }else {
                    face_bitmap=Beauty.smallFace(mBitmapPhoto,face_x, face_y, face_radius,1);
                }
                msg.what=3;
                handler.sendMessage(msg);
                break;
            case R.id.bt_small2:
                if(eye_bitmap!=null){
                    face_bitmap=Beauty.smallFace(eye_bitmap, face_x, face_y, face_radius,2);
                }else {
                    face_bitmap=Beauty.smallFace(mBitmapPhoto,face_x, face_y, face_radius,2);
                }
                msg.what=3;
                handler.sendMessage(msg);
                break;


        }

    }
}
