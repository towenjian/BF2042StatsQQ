package com.BF2042Stats.data;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class CheatCheat {
    /*
    * 全自动高于30可疑，高于35锤；如果满足前一条且该武器kpm高于2锤（枪械数据单独比较）
    * 把狙击枪和其他枪的击杀分开，对于狙击枪的击杀占比进行计算，如果狙击枪的击杀小于50%并且爆头率高于35的判断可疑
    * 加入联ban查询结果
    * 真实kpm高于3.5
    * 真实kd高于8的
    * 爆头率超过90直接判定为挂
    * 狙爆头率高于90（武器细查）
    * 按照命中率进行判定--待定
    * */
    private final JSONObject jsonObject;
    /*
    * isHacker 如果为真则实锤为挂
    * isHacker_BFBan 如果为真则为挂
    * isSuspicious 如果为真则为可疑，优先级小于isHacker
    * isPro 如果前面两项都为false则如果是true表示为pro哥，为false则是小薯薯
    * */
    private boolean isHacker = false,isPro = false,isSuspicious = true,isHacker_BFBan = false;

    public CheatCheat(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
        init();
        try {
            BFBan();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void init(){

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
        isHacker_BFBan = JSONObject.parseObject(data_ori).getJSONObject("names").getJSONObject(jsonObject.getString("userName")).getBoolean("hacker");
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
}