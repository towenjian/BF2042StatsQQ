package com.BF2042Stats.data.data_factory.T;

import com.BF2042Stats.data.CapacityPool;
import com.BF2042Stats.data.ConfigData;
import com.BF2042Stats.data.GroupMessage;
import com.BF2042Stats.data.PlayerData;
import com.BF2042Stats.data.data_interface.InterfaceData;

public class ST implements InterfaceData {
    @Override
    public void start(GroupMessage groupMessage) {
        String name="",pt="pc";
        int isBD=0;
        groupMessage.sendGroupMessage("该功能已被管理员禁用，请等待管理员解除");
        if (true) return;
        int day = 7;
        switch (groupMessage.getS().length){
            case 1:
                if (!ConfigData.isBD(groupMessage.getUser())){
                    groupMessage.sendGroupMessage("未绑定的账号，请绑定后再使用该格式的指令");
                    return;
                }
                isBD=1;
                name = ConfigData.GameID(groupMessage.getUser()).split("#")[0];
                pt = ConfigData.GameID(groupMessage.getUser()).split("#")[1];
                System.out.println(groupMessage.getDays());
                break;
            case 2:
                name = groupMessage.getS()[1];
                if(ConfigData.isBD(name)) isBD=1;
                break;
            case 3:
                name = groupMessage.getS()[1];
                pt = groupMessage.getS()[2];
                if(ConfigData.isBD(name)) isBD=1;
                break;
            default:
                groupMessage.sendGroupMessage("无效参数，请查看菜单页后再继续使用该指令");
        }
        if (!CapacityPool.findPlayerData(name.toLowerCase(), isBD)){
            groupMessage.sendGroupMessage("当前玩家未在"+(isBD==1?"绑定缓存池":"普通缓存池")+"中找到，正在创建");
            System.out.println(groupMessage.getDays());
            CapacityPool.addPlayerData(new PlayerData(groupMessage, name, pt, 6), isBD);
            CapacityPool.getPlayerData(name.toLowerCase(), isBD).setTime(isBD==1?24:2);
            return;
        }
        CapacityPool.getPlayerData(name.toLowerCase(), isBD).STImg(groupMessage);
    }
}
