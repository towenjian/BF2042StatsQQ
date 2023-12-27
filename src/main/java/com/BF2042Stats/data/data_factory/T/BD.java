package com.BF2042Stats.data.data_factory.T;

import com.BF2042Stats.data.CapacityPool;
import com.BF2042Stats.data.ConfigData;
import com.BF2042Stats.data.GroupMessage;
import com.BF2042Stats.data.data_interface.InterfaceData;

import java.util.Objects;

public class BD implements InterfaceData {
    @Override
    public void start(GroupMessage groupMessage) {
        if (groupMessage.getS().length!=3){
            groupMessage.sendGroupMessage("格式错误，请按照#bd ID 平台的格式来发送绑定格式,平台类型为“pc”，“xbox”，“ps4”和“ps5”");
            return;
        }
        ConfigData.setGameID(String.valueOf(groupMessage.getUser()), groupMessage.getS()[1]+"#"+groupMessage.getS()[2]);
        groupMessage.sendGroupMessage("绑定成功");
        if (CapacityPool.findPlayerData(groupMessage.getS()[1], 0)){
            groupMessage.sendGroupMessage("已在普通缓存池中找到你的缓存数据，已提升你的数据实例");
            Objects.requireNonNull(CapacityPool.addPlayerData(CapacityPool.getPlayerData(groupMessage.getS()[1], CapacityPool.PLAYER_BD), CapacityPool.PLAYER_BD)).setTime(24);
            CapacityPool.removePlayerData(groupMessage.getS()[1],CapacityPool.PLAYER_NBD);
        }
    }
}
