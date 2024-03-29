package com.BF2042Stats.data.data_factory.T;

import com.BF2042Stats.data.ConfigData;
import com.BF2042Stats.data.GroupMessage;
import com.BF2042Stats.data.Permissions;
import com.BF2042Stats.data.data_interface.InterfaceData;
import com.BF2042Stats.gui.ActivityMain;

public class Config implements InterfaceData {
    private static final Permissions p = new Permissions.Builder()
            .allowBotAdmin().build();
    @Override
    public void start(GroupMessage groupMessage) {
        if (p.notAllow(groupMessage.getMember())){
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
        ActivityMain activityMain = ActivityMain.getInstance();
        activityMain.setFun(groupMessage.getS()[1].toLowerCase(),isOpen);
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
            case "cr":
                if (groupMessage.getS().length<4) {
                    groupMessage.sendGroupMessage("缺少必要参数");
                    return;
                }
                ConfigData.setCustomResultsMap(groupMessage.getS()[2],groupMessage.getS()[3]);
                groupMessage.sendGroupMessage("设置完成");
                break;
            case "gci":
                if (groupMessage.getS().length<3) {
                    groupMessage.sendGroupMessage("缺少必要参数");
                    return;
                }
                int time = 0;
                try {
                    time = Integer.parseInt(groupMessage.getS()[2]);
                } catch (NumberFormatException e) {
                    groupMessage.sendGroupMessage("设置错误，非有效数字");
                    throw new RuntimeException(e);
                }
                ConfigData.setGroupChatInterval(time);
                groupMessage.sendGroupMessage("群聊查询间隔时间修改为："+groupMessage.getS()[2]);
                break;
            default:
                groupMessage.sendGroupMessage("当前的方法未找到");
        }
    }
}
