package com.BF2042Stats.data;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class CheatCheat {
    /*
    * 全自动高于30可疑，高于35锤；如果满足前一条且该武器kpm高于2锤（枪械数据单独比较）
    * 把狙击枪和其他枪的击杀分开，对于狙击枪的击杀占比进行计算，如果狙击枪的击杀小于50%并且爆头率高于35的判断可疑
    * 加入联ban查询结果
    * 真实kpm高于3.5
    * 真实kd高于8的可疑
    * 爆头率超过90直接判定为挂
    * 狙爆头率高于90（武器细查）
    * 按照命中率进行判定--待定
    * */
    private final JSONObject jsonObject;
    private JSONArray wp_array;
    private String reason;
    /*
    * isHacker 如果为真则实锤为挂
    * isHacker_BFBan 如果为真则为挂
    * isSuspicious 如果为真则为可疑，优先级小于isHacker
    * isPro 如果前面两项都为false则如果是true表示为pro哥，为false则是小薯薯
    * */
    private boolean isHacker = false,isPro = false,isSuspicious = false,isHacker_BFBan = false;
    private double kills,kills_human,kills_ju=0,kills_wp = 0,btl_all;
    private int suspiciousMember = 0,hackerMember = 0;

    public CheatCheat(JSONObject jsonObject,JSONArray wp_array) {
        this.jsonObject = jsonObject;
        this.wp_array = wp_array;
        init();
        try {
            BFBan();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void init(){
        //数据获取
        btl_all = Double.parseDouble(jsonObject.getString("headshots").replace("%", ""));
        kills = jsonObject.getDouble("kills");
        JSONObject dividedKills = jsonObject.getJSONObject("dividedKills");
        kills_human = dividedKills.getDouble("human");
        for (int i = 0; i < wp_array.size(); i++) {
            JSONObject j = wp_array.getJSONObject(i);
            kills_wp += j.getDouble("kills");
            if (j.getString("type").equals("Bolt Action")) kills_ju += j.getDouble("kills");
            else if (!j.getString("type").equals("DMR")&&j.getDouble("kills")>100){
                if (Double.parseDouble(j.getString("headshots").replace("%", ""))>30&&!j.getString("weaponName").equals("Rorsch Mk-4")) {
                    suspiciousMember++;
                    if (suspiciousMember>=3)
                    {
                        isSuspicious = true;
                        reason = "大部分自动武器爆头率高于30%";
                    }
                }
                if (Double.parseDouble(j.getString("headshots").replace("%", ""))>35&&j.getDouble("killsPerMinute")>2&&!j.getString("weaponName").equals("Rorsch Mk-4")) {
                    hackerMember++;
                    if (hackerMember>=3){
                        isHacker = true;
                        reason = "大部分自动武器爆头率高于35%，并且kpm过高";
                    }
                }
            }
            if (j.getString("weaponName").equals("Rorsch Mk-4")) {
                if (Double.parseDouble(j.getString("headshots").replace("%", ""))>35) kills_ju += j.getDouble("kills");
            }
        }
        kills_wp = kills_wp - jsonObject.getDouble("melee");
        //判定
        if (btl_all>=90) {
            isHacker = true;//爆头率高于90%
            reason = "总爆头率过高，超过90%";
        }
        if (kills_ju/kills_wp<0.5&&btl_all>35) {
            isSuspicious = true;
            if (!isHacker)  reason = "狙击步枪击杀占比小于50%且爆头率过高";
        }
        if (jsonObject.getDouble("inkpm")>3.5) {
            isSuspicious = true;
            if (!isHacker) reason = "真实kpm超过3.5";
        }
        if (jsonObject.getDouble("infantryKillDeath")>8) {
            isSuspicious = true;
            if (!isHacker) reason = "真实kd过高";
        }
        if (jsonObject.getDouble("killDeath")>2&&jsonObject.getDouble("killsPerMinute")>1) isPro = true;
    }
    private void BFBan() throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();
        String url = "https://api.gametools.network/bfban/checkban/?names="+jsonObject.getString("userName");
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        Call call = okHttpClient.newCall(request);
        Response response = call.execute();
        int code = response.code();
        if (code!=200) {
            isHacker_BFBan = false;
            return;
        }
        String data_ori = response.body().string();
        response.close();
        isHacker_BFBan = JSONObject.parseObject(data_ori).getJSONObject("names").getJSONObject(jsonObject.getString("userName").toLowerCase()).getBoolean("hacker");
    }

    public boolean isHacker() {
        return isHacker;
    }

    public boolean isPro() {
        return isPro;
    }

    public boolean isSuspicious() {
        return isSuspicious;
    }

    public boolean isHacker_BFBan() {
        return isHacker_BFBan;
    }
    public String getResult(){
        if (isHacker){
            return "鉴定为挂钩";
        }
        if (isSuspicious){
            return "该玩家数据可疑机器人无法判断";
        }
        if (ConfigData.getCusTomResult(jsonObject.getString("userName"))!=null){
            return ConfigData.getCusTomResult(jsonObject.getString("userName"));
        }
        if (isPro){
            return "普肉哥罢了，轻点捞";
        }
//        if (jsonObject.getString("userName").equals("hhhh6448")){
//            return "而你，我的英雄，你才是真正的Pro";
//        }
        else return "我是薯薯不要捞我了！";
    }
    public String getBFBanResult(){
        if (isHacker_BFBan){
            return "实锤";
        }else return "无结果";
    }
    public String getReason(){
        if (reason == null) return "当前玩家数据正常";
        return reason;
    }
    public Color getColor(){
        if (isHacker){
            return new Color(209, 22, 8);
        }
        if (isSuspicious){
            return new Color(232, 155, 0);
        }
        return new Color(36, 168, 91);
    }
    public Color getBFBanColor(){
        if (isHacker_BFBan) return new Color(209, 22, 8);
        return new Color(36, 168, 91);
    }
}