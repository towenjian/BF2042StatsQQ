package com.BF2042Stats.data.data_factory.T;

import com.BF2042Stats.data.CapacityPool;
import com.BF2042Stats.data.ConfigData;
import com.BF2042Stats.data.GroupMessage;
import com.BF2042Stats.data.data_interface.InterfaceData;

import java.util.Map;
import java.util.Set;

public class LIST implements InterfaceData {
    @Override
    public void start(GroupMessage groupMessage) {
        if (!(ConfigData.getUser()).equals(String.valueOf(groupMessage.getUser()))){
            groupMessage.sendGroupMessage("非管理员无法使用该指令");
            return;
        }
        int bdC = CapacityPool.getPlayerBd(),NbdC = CapacityPool.getPlayerNbd();
        Map<String,Integer> bd_map_y = CapacityPool.getPlayerName_Time_y(),bd_map_n = CapacityPool.getPlayerName_Time_n();
        StringBuilder bd_s_y = new StringBuilder();
        StringBuilder bd_s_n = new StringBuilder();
        for (String s : bd_map_y.keySet()){
            bd_s_y.append("- ").append(s).append("-remainder:").append(bd_map_y.get(s)).append("\n");
        }
        for (String s : bd_map_n.keySet()){
            bd_s_n.append("- ").append(s).append("-remainder:").append(bd_map_n.get(s)).append("\n");
        }
        String reply = "当前绑定玩家缓存池的玩家数量为"+bdC+",玩家如下：\n" +
                bd_s_y+"\n" +
                "当前普通缓存池的玩家数量为"+NbdC+",最大为10，含有如下玩家\n" +
                bd_s_n;
        groupMessage.sendGroupMessage(reply);
    }
}
