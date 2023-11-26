package com.BF2042Stats.listen;

import com.BF2042Stats.callback.TimeCallback;
import com.BF2042Stats.data.CapacityPool;
import com.BF2042Stats.data.ConfigData;
import com.BF2042Stats.data.GroupMessage;
import com.BF2042Stats.data.PlayerData;
import com.BF2042Stats.data.data_enum.FactoryEnum;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.events.*;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.QuoteReply;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Command implements TimeCallback {
    private static Bot bot;
    private static final List<String> list = new ArrayList<>();
    private static final Map<Long,String> requestEventMap = new HashMap<>();

    public Command() {
        for (FactoryEnum factoryEnum:FactoryEnum.values()){
            list.add(factoryEnum.toString());
        }
    }
    public void GroupMessage(){
        GlobalEventChannel.INSTANCE.subscribeAlways(GroupMessageEvent.class, groupMessageEvent -> {
            QuoteReply reply = new QuoteReply(groupMessageEvent.getSource());
            System.out.println(reply.getSource());
            System.out.println(reply.getSource().getIds()[0]);
            System.out.println(reply.getSource().getIds().length);
            System.out.println(ConfigData.getGroupList().contains(String.valueOf(groupMessageEvent.getGroup().getId())));
            if (!ConfigData.getGroupList().contains(String.valueOf(groupMessageEvent.getGroup().getId()))) return;
            MessageChain messageChain = groupMessageEvent.getMessage();
            System.out.println(messageChain);
            System.out.println(!messageChain.contentToString().startsWith("#"));
            if (!messageChain.contentToString().startsWith("#")) return;
            String[] temp = messageChain.contentToString().replace("#", "").split(" ");
            GroupMessage groupMessage = new GroupMessage(groupMessageEvent, this,bot,temp);
            if (list.contains(temp[0].toLowerCase())) FactoryEnum.valueOf(temp[0].toLowerCase()).getInterfaceData().start(groupMessage);
            else groupMessageEvent.getGroup().sendMessage(new MessageChainBuilder()
                    .append(new QuoteReply(groupMessageEvent.getMessage()))
                    .append("无效命令")
                    .build());
        });
    }
    public void joinGroupMessage(){//申请入群消息
        GlobalEventChannel.INSTANCE.subscribeAlways(MemberJoinRequestEvent.class, memberJoinRequestEvent -> {
            String s = memberJoinRequestEvent.getMessage();
            s = s.replace("问题：你的游戏ID\n" +
                    "答案：","");
            if (isContainsChinese(s)){
                memberJoinRequestEvent.getGroup().sendMessage(new MessageChainBuilder()
                        .append(new At(Long.parseLong(ConfigData.getUser()))).append("无法验证该ID,因为申请的ID含有中文").append(s)
                        .build());
                return;
            }
            memberJoinRequestEvent.getGroup().sendMessage(new MessageChainBuilder()
                    .append(new At(Long.parseLong(ConfigData.getUser())))
                    .append("接收到加群消息，正在验证，申请的ID为:"+s)
                    .build());
            GroupMessage groupMessage = new GroupMessage(memberJoinRequestEvent,this, bot, new String[]{"cx",memberJoinRequestEvent.getMessage()});
            CapacityPool.addPlayerData(new PlayerData(
                    groupMessage,
                    memberJoinRequestEvent.getMessage(),
                    "pc",
                    1), 0);
            requestEventMap.put(memberJoinRequestEvent.getFromId(), memberJoinRequestEvent.getMessage());
            CapacityPool.getPlayerData(memberJoinRequestEvent.getMessage(), 0).setTime(2);
        });
    }
    public void joinGroup(){//被邀请入群
        FriendMessage();
        GlobalEventChannel.INSTANCE.subscribeAlways(BotInvitedJoinGroupRequestEvent.class, botInvitedJoinGroupRequestEvent -> {
            bot.getFriend(Long.parseLong(ConfigData.getUser())).sendMessage(new MessageChainBuilder().append("接收到加群请求,目标群聊为:").append(String.valueOf(botInvitedJoinGroupRequestEvent.getGroupId())).append("邀请人为:").append(String.valueOf(botInvitedJoinGroupRequestEvent.getInvitor().getId()))
                    .build());
        });
    }
    private void  FriendMessage(){
        GlobalEventChannel.INSTANCE.subscribeAlways(FriendMessageEvent.class, friendMessageEvent -> {
            if (friendMessageEvent.getFriend().getId()!=Long.parseLong(ConfigData.getUser())) {
            }

        });
    }
    public void MemberJoin(){//玩家加入消息
        GlobalEventChannel.INSTANCE.subscribeAlways(MemberJoinEvent.class, memberJoinEvent -> {
            memberJoinEvent.getGroup().sendMessage(new MessageChainBuilder()
                    .append(new At(memberJoinEvent.getMember().getId()))
                    .append("欢迎入群，群内机器人可以通过#cd指令查看详细功能，查询软件在bf.mcbtc.top,有bug欢迎向群主提交")
                    .build());
            if (requestEventMap.containsKey(memberJoinEvent.getMember().getId()))memberJoinEvent.getMember().setNameCard(requestEventMap.get(memberJoinEvent.getMember().getId()));
        });
    }

    public static Bot getBot() {
        return bot;
    }

    public static void setBot(Bot bot) {
        Command.bot = bot;
    }

    @Override
    public void Cause(long userID) {
    }
    private void removeMqp_Task(PlayerData playerData){
        if (playerData!=null){
            playerData.removeTimer();
        }
    }
    public boolean isContainsChinese(String str) {//检查是否含有中文
        if (str == null) { return false; }
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        return m.find();
    }
}
