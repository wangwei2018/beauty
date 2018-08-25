package com.example.ww.beauty.utils;


/**
 * HTTP 请求的结果，包含返回的状态码和返回内容
 * result of HTTP request, includ status code and result content
 *
 * Created by Qi Wang on 2016/12/19.
 */
public class Response {

    private byte[] content;//返回的信息 result content
    private int status;//返回的状态码 status code


    public Response(){

    }

    public Response(byte[] content, int status){
        this.content = content;
        this.status = status;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
