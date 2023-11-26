package com.BF2042Stats.data.data_factory.T;

import com.BF2042Stats.data.ConfigData;
import com.BF2042Stats.data.GroupMessage;
import com.BF2042Stats.data.data_interface.InterfaceData;

public class BD implements InterfaceData {
    @Override
    public void start(GroupMessage groupMessage) {
        if (groupMessage.getS().length!=3){
            groupMessage.sendGroupMessage("格式错误，请按照#bd ID 平台的格式来发送绑定格式,平台类型为“pc”，“xbox”，“ps4”和“ps5”");
            return;
        }
        ConfigData.setGameID(String.valueOf(groupMessage.getUser()), groupMessage.getS()[1]+"#"+groupMessage.getS()[2]);
        groupMessage.sendGroupMessage("绑定成功");
    }
}
