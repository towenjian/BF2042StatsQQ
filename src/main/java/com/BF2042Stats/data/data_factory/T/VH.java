package com.BF2042Stats.data.data_factory.T;

import com.BF2042Stats.data.CapacityPool;
import com.BF2042Stats.data.ConfigData;
import com.BF2042Stats.data.GroupMessage;
import com.BF2042Stats.data.PlayerData;
import com.BF2042Stats.data.data_interface.GetType;
import com.BF2042Stats.data.data_interface.InterfaceData;

import java.util.Objects;

public class VH implements InterfaceData {
    @Override
    public void start(GroupMessage groupMessage) {
        if (!ConfigData.isVh()){
            groupMessage.sendGroupMessage("当前功能已被禁用");
            return;
        }
        String name ;
        String pt = "pc";
        int isBD = 0;
        switch (groupMessage.getS().length){
            case 1:
                if (!ConfigData.isBD(groupMessage.getUser())) {
                    groupMessage.sendGroupMessage("未绑定ID,请绑定ID后在使用单独的vh指令");
                    return;
                }
                String[] s = ConfigData.GameID(groupMessage.getUser()).split("#");
                if (!CapacityPool.findPlayerData(s[0],1)){
                    groupMessage.sendGroupMessage("未找到缓存的数据，正在重新创建数据缓存");
                    CapacityPool.addPlayerData(new PlayerData(groupMessage, s[0],s[1], GetType.VH),1);
                    Objects.requireNonNull(CapacityPool.getPlayerData(s[0], 1)).setTime(24);
                    return;
                }
                CapacityPool.getPlayerData(s[0],1).VEHImg(groupMessage);
                break;
            case 2:
                name = groupMessage.getS()[1];
                pt = "pc";
                if (ConfigData.isBD(name)) isBD=1;
                if ((!CapacityPool.findPlayerData(name, isBD))){
                    groupMessage.sendGroupMessage("未找到缓存的数据，正在重新创建数据缓存");
                    CapacityPool.addPlayerData(new PlayerData(groupMessage, name, pt, GetType.VH), isBD);
                    CapacityPool.getPlayerData(name, isBD).setTime(isBD==1?24:2);
                    return;
                }
                CapacityPool.getPlayerData(name, isBD).VEHImg(groupMessage);
                break;
            case 3:
                name = groupMessage.getS()[1];
                pt = groupMessage.getS()[2];
                if (ConfigData.isBD(name)) isBD=1;
                if ((!CapacityPool.findPlayerData(name, isBD))){
                    groupMessage.sendGroupMessage("未找到缓存的数据，正在重新创建数据缓存");
                    CapacityPool.addPlayerData(new PlayerData(groupMessage, name, pt, GetType.VH), isBD);
                    CapacityPool.getPlayerData(name, isBD).setTime(isBD==1?24:2);
                    return;
                }
                CapacityPool.getPlayerData(name, isBD).VEHImg(groupMessage);
                return;
            default:
                groupMessage.sendGroupMessage("无效指令");
        }
    }
}
