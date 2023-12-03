package com.BF2042Stats.data.data_factory.T;

import com.BF2042Stats.data.CapacityPool;
import com.BF2042Stats.data.ConfigData;
import com.BF2042Stats.data.GroupMessage;
import com.BF2042Stats.data.PlayerData;
import com.BF2042Stats.data.data_interface.GetType;
import com.BF2042Stats.data.data_interface.InterfaceData;

public class CX implements InterfaceData {
    @Override
    public void start(GroupMessage groupMessage) {
        if (!ConfigData.isCx()){
            groupMessage.sendGroupMessage("当前功能已被禁用");
            return;
        }
        String name,platform;
        int isBD=0;
        System.out.println(isBD+"|f");
        switch (groupMessage.getS().length){
            case 1:
                if (!ConfigData.isBD(groupMessage.getUser())) {
                    groupMessage.sendGroupMessage("无法查找到你的相关数据，请进行绑定");
                    return;
                }
                String[] temp = ConfigData.GameID(groupMessage.getUser()).split("#");
                name = temp[0];
                platform = temp[1];
                isBD=1;
                System.out.println(isBD+"|case1");
                break;
            case 2:
                name = groupMessage.getS()[1];
                if (ConfigData.isBD(name)) isBD=1;
                platform = "pc";
                System.out.println(isBD+"|case2");
                break;
            case 3:
                name = groupMessage.getS()[1];
                if (ConfigData.isBD(name)) isBD=1;
                platform = groupMessage.getS()[2];
                System.out.println(isBD+"|case3");
                break;
            default:
                groupMessage.sendGroupMessage("无效指令");
                return;
        }
        System.out.println(isBD);
        if (!CapacityPool.findPlayerData(name, isBD)){
            groupMessage.sendGroupMessage("当前玩家数据并未在缓存中，正在重新创建");
            CapacityPool.addPlayerData(new PlayerData(groupMessage, name, platform, GetType.CX), isBD);
            CapacityPool.getPlayerData(name,isBD).setTime((isBD==1?24:2));
            return;
        }
        CapacityPool.getPlayerData(name, isBD).CX(groupMessage);
    }
}
