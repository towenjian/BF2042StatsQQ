package com.BF2042Stats.data.data_factory.T;

import com.BF2042Stats.data.CapacityPool;
import com.BF2042Stats.data.GroupMessage;
import com.BF2042Stats.data.data_interface.InterfaceData;

public class Clear implements InterfaceData {
    @Override
    public void start(GroupMessage groupMessage) {
        if (groupMessage.getMember().getPermission().getLevel()<=0){
            groupMessage.sendGroupMessage("该指令仅群聊管理员可用");
            return;
        }
        if (groupMessage.getS().length<2){
            groupMessage.sendGroupMessage("参数不够，请确保第二个为玩家ID");
            return;
        }
        String name = groupMessage.getS()[1];
        CapacityPool.removePlayerData(name);
        groupMessage.sendGroupMessage("玩家："+name+"数据已被移除");
    }
}
