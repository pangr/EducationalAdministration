package com.hcjcch.educationaladministration.utils;

import android.content.Context;

import com.hcjcch.educationaladministration.config.StaticVariable;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by limbo on 2014/11/6.
 */
public class MarkUtils {
    private Context context;
    private String[] years = null;
    private List<Map<String,Object>> list_score = null;
    private String xuehao = null;
    private String year = null;
    private String semester = null;
    private String type = null;
    public MarkUtils(String id, Context context){
        this.xuehao = id;
        this.context = context;
        this.years = this.pull_years("year.php");
    }

    public MarkUtils(String id,Context context, String year, String semester, String type){
        this.xuehao = id;
        this.year = year;
        this.semester = semester;
        this.type = type;
        list_score = get_score("score.php");
    }

    //获取学年列表
    private String[] pull_years(String url){
        final String[] str = new String[4];
        RequestParams params = new RequestParams();
        params.add("xh",xuehao);
        //api_path;
        //异步获取json并解析json
        EduHttpClient.get(url,params,new AsyncHttpResponseHandler() {
            @Override
            public void onStart(){
                super.onStart();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                //execution;
                String json = new String(responseBody);
                try {
                    //!!!decode JSON must try block!!!!
                    JSONArray array = new JSONArray(json);
                    for(int i=0;i<array.length();i++){
                        //解析json
                        JSONObject object = array.getJSONObject(i);
                        str[i]=object.getString("title");
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                //show the error
                error.printStackTrace();
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
        return str;
    }

    public CharSequence[] getyears(){
        CharSequence[] a = new CharSequence[4];
        a[0]=years[0];
        a[1]=years[1];
        a[2]=years[2];
        a[3]=years[3];
        return a;
    }

    public boolean years_is_empty(){
        for(int i=0;i<4;i++){
            if((years[i]==null)||(years[i].equals("")))
                return true;
        }
        return false;
    }

    //获取成绩
    private List<Map<String,Object>> get_score(String url){
        final List<Map<String,Object>> list = new ArrayList<Map<String, Object>>();
        RequestParams params = new RequestParams();
        params.add("xh",xuehao);
        params.add("xn",year);
        params.add("xq",semester);
        if(!type.equals(StaticVariable.qbkc))
            params.add("kcxz",type);
        EduHttpClient.get(url,params,new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String json = new String(responseBody);
                try {
                        Map<String, Object> map;
                        JSONArray array = new JSONArray(json);
                        for (int i = 0; i < array.length(); i++) {
                             //解析json
                            JSONObject object = array.getJSONObject(i);
                            map = new HashMap<String, Object>();
                            //map 操作
                            map.put("kcmc", object.getString("kcmc"));
                            map.put("pscj", object.getString("pscj"));
                            map.put("qmcj", object.getString("qmcj"));
                            map.put("sycj", object.getString("sycj"));
                            map.put("qzcj", object.getString("qzcj"));
                            map.put("cj", object.getString("cj"));
                            map.put("xf", object.getString("xf"));
                            map.put("gd", object.getString("gd"));
                            list.add(map);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                error.printStackTrace();
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
        return list;
    }

    public List<Map<String,Object>> get_list(){return list_score;}
}