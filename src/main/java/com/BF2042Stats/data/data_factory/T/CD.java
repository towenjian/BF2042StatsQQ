package com.BF2042Stats.data.data_factory.T;

import com.BF2042Stats.data.GroupMessage;
import com.BF2042Stats.data.data_interface.InterfaceData;

public class CD implements InterfaceData {
    @Override
    public void start(GroupMessage groupMessage) {
        String reply = "   小助手菜单\n" +
                "可用的功能有：\n" +
                "-1.2042战绩查询，前缀为#cx\n" +
                "-2.2042武器数据图输出，前缀为#wp\n" +
                "-3.2042载具数据图输出，前缀为#vh\n" +
                "-4.2042专家数据图输出，前缀为#cl\n" +
                "-5.数据绑定#bd id 平台，都为必填参数\n" +
                "---对于除绑定外的指令结构都为type id 平台（不填默认为pc）" +
                "其中type为每个指令的前缀\n" +
                "---对于绑定后的玩家只需要输入前缀即可，既#cx等等，绑定后机器人会自动填入你之前的值";
        groupMessage.sendGroupMessage(reply);
    }
}
