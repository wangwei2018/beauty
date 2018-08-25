package com.example.ww.beauty.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

//

public class HandleResponse {

    public static int eye_radius(String content){
        JSONObject object = null;
        try {
            object = new JSONObject(content);
            String faces = object.getString("faces");
            JSONArray jsonArray = new JSONArray(faces);
            object = (JSONObject)jsonArray.get(0);
            String left_eye_center = new JSONObject(object.getString("landmark")).getString("left_eye_center");
            int left_eye_x = Integer.valueOf(new JSONObject(left_eye_center).getString("x"));
            int left_eye_y = Integer.valueOf(new JSONObject(left_eye_center).getString("y"));

            String left_eye_left_corner = new JSONObject(object.getString("landmark")).getString("left_eye_left_corner");
            int corner_x = Integer.valueOf(new JSONObject(left_eye_left_corner).getString("x"));
            int corner_y = Integer.valueOf(new JSONObject(left_eye_left_corner).getString("y"));

            return (int)Math.sqrt((left_eye_x-corner_x)*(left_eye_x-corner_x)+(left_eye_y-corner_y)*(left_eye_y-corner_y));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static HashMap left_eye_center(String content){
        JSONObject object = null;
        try {
            object = new JSONObject(content);
            String faces = object.getString("faces");
            JSONArray jsonArray = new JSONArray(faces);
            object = (JSONObject)jsonArray.get(0);
            String left_eye_center = new JSONObject(object.getString("landmark")).getString("left_eye_center");
            int left_eye_x = Integer.valueOf(new JSONObject(left_eye_center).getString("x"));
            int left_eye_y = Integer.valueOf(new JSONObject(left_eye_center).getString("y"));
            HashMap hashMap = new HashMap();
            hashMap.put("x",left_eye_x);
            hashMap.put("y",left_eye_y);
            return hashMap;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static HashMap right_eye_center(String content){
        JSONObject object = null;
        try {
            object = new JSONObject(content);
            String faces = object.getString("faces");
            JSONArray jsonArray = new JSONArray(faces);
            object = (JSONObject)jsonArray.get(0);
            String right_eye_center = new JSONObject(object.getString("landmark")).getString("right_eye_center");
            int left_eye_x = Integer.valueOf(new JSONObject(right_eye_center).getString("x"));
            int left_eye_y = Integer.valueOf(new JSONObject(right_eye_center).getString("y"));
            HashMap hashMap = new HashMap();
            hashMap.put("x",left_eye_x);
            hashMap.put("y",left_eye_y);
            return hashMap;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static HashMap face_center(String content){
        JSONObject object = null;
        try {
            object = new JSONObject(content);
            String faces = object.getString("faces");
            JSONArray jsonArray = new JSONArray(faces);
            object = (JSONObject)jsonArray.get(0);
            String right_eye_center = new JSONObject(object.getString("landmark")).getString("nose_tip");
            int center_x = Integer.valueOf(new JSONObject(right_eye_center).getString("x"));
            int center_y = Integer.valueOf(new JSONObject(right_eye_center).getString("y"));
            HashMap hashMap = new HashMap();
            hashMap.put("x",center_x);
            hashMap.put("y",center_y);
            return hashMap;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int face_radius(String content){
        JSONObject object = null;
        try {
            object = new JSONObject(content);
            String faces = object.getString("faces");
            JSONArray jsonArray = new JSONArray(faces);
            object = (JSONObject)jsonArray.get(0);
            String right_eye_center = new JSONObject(object.getString("landmark")).getString("nose_tip");
            int center_x = Integer.valueOf(new JSONObject(right_eye_center).getString("x"));
            int center_y = Integer.valueOf(new JSONObject(right_eye_center).getString("y"));

            String contour_chin = new JSONObject(object.getString("landmark")).getString("contour_chin");
            int contour_chin_x = Integer.valueOf(new JSONObject(contour_chin).getString("x"));
            int contour_chin_y = Integer.valueOf(new JSONObject(contour_chin).getString("y"));

            return (int)Math.sqrt((center_x-contour_chin_x)*(center_x-contour_chin_x)+(center_y-contour_chin_y)*(center_y-contour_chin_y));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }




}
