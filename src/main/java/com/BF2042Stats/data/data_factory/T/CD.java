package com.BF2042Stats.data.data_factory.T;

import com.BF2042Stats.data.ConfigData;
import com.BF2042Stats.data.GroupMessage;
import com.BF2042Stats.data.data_interface.InterfaceData;

public class CD implements InterfaceData {
    @Override
    public void start(GroupMessage groupMessage) {
        groupMessage.sendGroupMessage(ConfigData.getMenuMessage());
    }
}
