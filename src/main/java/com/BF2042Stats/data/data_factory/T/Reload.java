package com.BF2042Stats.data.data_factory.T;

import com.BF2042Stats.data.ConfigData;
import com.BF2042Stats.data.GroupMessage;
import com.BF2042Stats.data.Permissions;
import com.BF2042Stats.data.data_interface.InterfaceData;
import net.mamoe.mirai.console.events.ConsoleEvent;
import net.mamoe.mirai.event.GlobalEventChannel;

import java.io.IOException;
import java.util.function.Consumer;

public class Reload implements InterfaceData {
    private static final Permissions p = new Permissions.Builder()
            .allowBotAdmin().build();
    @Override
    public void start(GroupMessage groupMessage) {
        if (!p.isAllow(groupMessage.getMember())){
            groupMessage.sendGroupMessage("非管理员无法使用该指令");
            return;
        }
        try {
            ConfigData.reload();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("reload");
        groupMessage.sendGroupMessage("重载成功");
    }
}
