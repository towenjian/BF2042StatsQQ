package com.BF2042Stats.data.data_factory.T;

import com.BF2042Stats.data.ConfigData;
import com.BF2042Stats.data.GroupMessage;
import com.BF2042Stats.data.data_interface.InterfaceData;

public class Config implements InterfaceData {
    @Override
    public void start(GroupMessage groupMessage) {
        if (!ConfigData.isAdmin(groupMessage.getUser())){
            groupMessage.sendGroupMessage("您并非Admin用户，请不要使用该指令");
            return;
        }
        if (groupMessage.getS().length<=2){
            groupMessage.sendGroupMessage("请正确填写参数，格式为/config type false");
            return;
        }
        boolean isOpen;
        try {
            isOpen = Boolean.parseBoolean(groupMessage.getS()[2]);
        } catch (Exception e) {
            groupMessage.sendGroupMessage(groupMessage.getS()[2]+"<-该参数不是正确的bool类型");
            throw new RuntimeException(e);
        }
        switch (groupMessage.getS()[1].toLowerCase()){
            case "cx":
                ConfigData.setCx(isOpen);
                groupMessage.sendGroupMessage("cx功能已"+(isOpen?"开启":"关闭"));
                break;
            case "vh":
                ConfigData.setVh(isOpen);
                groupMessage.sendGroupMessage("vh功能已"+(isOpen?"开启":"关闭"));
                break;
            case "wp":
                ConfigData.setWp(isOpen);
                groupMessage.sendGroupMessage("wp功能已"+(isOpen?"开启":"关闭"));
                break;
            case "kd":
                ConfigData.setKd(isOpen);
                groupMessage.sendGroupMessage("kd功能已"+(isOpen?"开启":"关闭"));
                break;
            case "kill":
                ConfigData.setKill(isOpen);
                groupMessage.sendGroupMessage("kill功能已"+(isOpen?"开启":"关闭"));
                break;
            case "cl":
                ConfigData.setCl(isOpen);
                groupMessage.sendGroupMessage("cl功能已"+(isOpen?"开启":"关闭"));
                break;
            case "pr":
                ConfigData.setPrivateChatSearch(isOpen);
                groupMessage.sendGroupMessage("私聊功能已"+(isOpen?"开启":"关闭"));
                break;
            case "pg":
                ConfigData.setPreGet(isOpen);
                groupMessage.sendGroupMessage("前置查询功能已"+(isOpen?"开启":"关闭"));
                break;
            default:
                groupMessage.sendGroupMessage("当前的方法未找到");
        }
    }
}
