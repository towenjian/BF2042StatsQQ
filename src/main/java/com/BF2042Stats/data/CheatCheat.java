package com.BF2042Stats.data;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

public class CheatCheat {
    /*
    * 全自动高于30可疑，高于35锤；如果满足前一条且该武器kpm高于2锤（枪械数据单独比较）
    * 把狙击枪和其他枪的击杀分开，对于狙击枪的击杀占比进行计算，如果狙击枪的击杀小于50%并且爆头率高于35的判断可疑
    * 真实kpm高于3.5
    * 真实kd高于8的
    * 爆头率超过90直接判定为挂
    * 狙爆头率高于90（武器细查）
    * 按照命中率进行判定--待定
    * */
    private final JSONObject jsonObject;
    /*
    * isHacker 如果为真则实锤为挂
    * isSuspicious 如果为真则为可疑，优先级小于isHacker
    * isPro 如果前面两项都为false则如果是true表示为pro哥，为false则是小薯薯
    * */
    private boolean isHacker = false,isPro = false,isSuspicious = true;

    public CheatCheat(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
        init();
    }
    private void init(){

    }
}
