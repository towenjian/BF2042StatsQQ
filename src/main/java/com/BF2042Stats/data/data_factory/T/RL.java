package com.BF2042Stats.data.data_factory.T;

import com.BF2042Stats.data.CapacityPool;
import com.BF2042Stats.data.ConfigData;
import com.BF2042Stats.data.GroupMessage;
import com.BF2042Stats.data.PlayerData;
import com.BF2042Stats.data.data_interface.InterfaceData;

public class RL implements InterfaceData {
    @Override
    public void start(GroupMessage groupMessage) {
        String name,pt = "pc";
        int isBD = 0;
        switch (groupMessage.getS().length){
            case 1:
                if (!ConfigData.isBD(groupMessage.getUser())){
                    groupMessage.sendGroupMessage("你未绑定，请绑定后使用单独指令rl，详情请输入#cd");
                    return;
                }
                name = ConfigData.GameID(groupMessage.getUser()).split("#")[0];
                pt = ConfigData.GameID(groupMessage.getUser()).split("#")[1];
                isBD = 1;
                break;
            case 2:
                if (ConfigData.isBD(groupMessage.getS()[1])) isBD = 1;
                name = groupMessage.getS()[1];
                break;
            case 3:
                if (ConfigData.isBD(groupMessage.getS()[1])) isBD = 1;
                name = groupMessage.getS()[1];
                pt = groupMessage.getS()[2];
                break;
            default:
                groupMessage.sendGroupMessage("无效的参数数量");
                return;
        }
        if (!CapacityPool.findPlayerData(name, isBD)){
            groupMessage.sendGroupMessage("当前玩家未在"+(isBD==1?"绑定缓存池":"普通缓存池")+"中找到，正在创建");
            CapacityPool.addPlayerData(new PlayerData(groupMessage, name, pt, 7), isBD);
            CapacityPool.getPlayerData(name.toLowerCase(), isBD).setTime(isBD==1?24:2);
            return;
        }
        CapacityPool.getPlayerData(name, isBD).RL(groupMessage);
    }
}
