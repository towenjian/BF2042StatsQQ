package com.BF2042Stats.listen;

import com.BF2042Stats.callback.TimeCallback;
import com.BF2042Stats.data.CapacityPool;
import com.BF2042Stats.data.ConfigData;
import com.BF2042Stats.data.GroupMessage;
import com.BF2042Stats.data.PlayerData;
import com.BF2042Stats.data.data_enum.FactoryEnum;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.event.events.*;
import net.mamoe.mirai.message.MessageReceipt;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.QuoteReply;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Command implements TimeCallback {
    private static Bot bot;
    private static final List<String> list = new ArrayList<>();
    private static final Map<Integer,MemberJoinRequestEvent> requestEventMap = new HashMap<>();
    //记录最近的消息时间，以便于阻止消息发送
    private final Map<String,LocalDateTime> lastMessageTimeMap = new HashMap<>();
    private static final Command COMMAND = new Command();
    public static  Command getInstance(){
        return COMMAND;
    }

    private Command() {
        for (FactoryEnum factoryEnum:FactoryEnum.values()){
            list.add(factoryEnum.toString());
        }
    }

    /**
     * 监听群聊消息
     */
    public Command GroupMessage(){
        bot.getEventChannel().subscribeAlways(GroupMessageEvent.class, groupMessageEvent -> {
            if (!ConfigData.getGroupList().contains(String.valueOf(groupMessageEvent.getGroup().getId()))) return;
            MessageChain messageChain = groupMessageEvent.getMessage();
            if (!messageChain.contentToString().startsWith("#")) {
                lastMessageTimeMap.put(String.valueOf(groupMessageEvent.getGroup().getId()),LocalDateTime.now());
                return;
            }
            LocalDateTime nowTime = LocalDateTime.now();
            if (lastMessageTimeMap.containsKey(String.valueOf(groupMessageEvent.getGroup().getId())))if (!nowTime.isAfter(lastMessageTimeMap.get(String.valueOf(groupMessageEvent.getGroup().getId())).plusSeconds(ConfigData.getGroupChatInterval()))&&groupMessageEvent.getSender().getPermission().getLevel()==0){
//                groupMessageEvent.getGroup().sendMessage("当前正在聊天，请稍后再发消息，或者私聊查询--剩余时间："+ (ConfigData.getGroupChatInterval()-Duration.between(lastMessageTimeMap.get(String.valueOf(groupMessageEvent.getGroup().getId())),nowTime).getSeconds()));
                groupMessageEvent.getGroup().sendMessage(new MessageChainBuilder()
                        .append(new QuoteReply(groupMessageEvent.getMessage())).append("当前有人正在聊天，请等待-").append(String.valueOf(ConfigData.getGroupChatInterval() - Duration.between(lastMessageTimeMap.get(String.valueOf(groupMessageEvent.getGroup().getId())), nowTime).getSeconds())).append("秒后再使用查询指令，或者私聊机器人进行查询")
                        .build());
                return;
            }
            String[] temp = messageChain.contentToString().replace("#", "").split(" ");
            GroupMessage groupMessage = new GroupMessage(groupMessageEvent, this,bot,temp);
            if (list.contains(temp[0].toLowerCase())) FactoryEnum.valueOf(temp[0].toLowerCase()).getInterfaceData().start(groupMessage);
            else groupMessageEvent.getGroup().sendMessage(new MessageChainBuilder()
                    .append(new QuoteReply(groupMessageEvent.getMessage()))
                    .append("无效命令")
                    .build());
        });
        return this;
    }

    /**
     * 监听私聊消息
     */
    public Command PrivateChat(){
        bot.getEventChannel().subscribeAlways(GroupTempMessageEvent.class, groupTempMessageEvent -> {
            if (!ConfigData.isPrivateChatSearch()) return;
            if (!ConfigData.isGroupList(groupTempMessageEvent.getGroup().getId())) return;
            MessageChain chain = groupTempMessageEvent.getMessage();
            if (!chain.contentToString().startsWith("#")) return;
            String[] temp = chain.contentToString().replace("#", "").split(" ");
            GroupMessage groupMessage = new GroupMessage(groupTempMessageEvent,this,bot,temp);
            if (list.contains(temp[0].toLowerCase())) FactoryEnum.valueOf(temp[0].toLowerCase()).getInterfaceData().start(groupMessage);
            else groupMessage.sendGroupMessage("无效命令");
        });
        return this;
    }

    /**
     * 处理入群请求
     */
    public Command joinGroupMessage(){
        bot.getEventChannel().subscribeAlways(MemberJoinRequestEvent.class, memberJoinRequestEvent -> {
            String s = memberJoinRequestEvent.getMessage().split("答案：")[1];
            if (isContainsChinese(s)){
                Objects.requireNonNull(memberJoinRequestEvent.getGroup()).sendMessage(new MessageChainBuilder()
                        .append("无法验证该ID,因为申请的ID含有中文,请管理员自行验证").append(s)
                        .build());
                return;
            }
            MessageChain chain = new MessageChainBuilder().append("接收到加群消息，正在验证，申请的ID为:").append(s).append("\n回复此条消息加y既同意加入，n则拒绝\n---仅限群管理员")
                    .build();
            MessageReceipt<Group> receipt = Objects.requireNonNull(memberJoinRequestEvent.getGroup()).sendMessage(chain);
            requestEventMap.put(receipt.getSource().getIds()[0],memberJoinRequestEvent);
            GroupMessage groupMessage = new GroupMessage(receipt,this, bot, new String[]{"cx",s},memberJoinRequestEvent.getFromId());
            CapacityPool.addPlayerData(new PlayerData(
                    groupMessage,
                    s,
                    "pc",
                    1), 0).setTime(1);
        });
        bot.getEventChannel().subscribeAlways(GroupMessageEvent.class,groupMessageEvent -> {
            System.out.println(groupMessageEvent.getSource().getFromId()+"tttt");
            System.out.println(groupMessageEvent.getSource().getIds()[0]);
            if (!requestEventMap.containsKey(groupMessageEvent.getSource().getIds()[0])) return;
            if (groupMessageEvent.getPermission().getLevel()==0) {
                groupMessageEvent.getSender().sendMessage("非管理员，请不要回复此条消息");
                return;
            }
            groupMessageEvent.getGroup().sendMessage(new MessageChainBuilder()
                    .append(String.valueOf(groupMessageEvent.getSource().getIds().length))
                    .build());
            List<Integer> list = new ArrayList<>();
            for (int i : groupMessageEvent.getSource().getIds()){
                list.add(i);
            }
            System.out.println(list);
        });
        return this;
    }

    /**
     * 处理加群请求
     */
    public Command joinGroup(){
        FriendMessage();
        bot.getEventChannel().subscribeAlways(BotInvitedJoinGroupRequestEvent.class, botInvitedJoinGroupRequestEvent -> {
            bot.getFriend(Long.parseLong(ConfigData.getUser())).sendMessage(new MessageChainBuilder().append("接收到加群请求,目标群聊为:").append(String.valueOf(botInvitedJoinGroupRequestEvent.getGroupId())).append("邀请人为:").append(String.valueOf(botInvitedJoinGroupRequestEvent.getInvitor().getId()))
                    .build());
        });
        return this;
    }
    private Command FriendMessage(){
        bot.getEventChannel().subscribeAlways(FriendMessageEvent.class, friendMessageEvent -> {
            if (friendMessageEvent.getFriend().getId()!=Long.parseLong(ConfigData.getUser())) {
            }

        });
        return this;
    }
    public Command exitGroup(){
        bot.getEventChannel().subscribeAlways(MemberLeaveEvent.class,memberLeaveEvent -> {
            Member member = memberLeaveEvent.getMember();
            if (ConfigData.isBD(member.getId())){
                ConfigData.removeBD(member.getId());
                memberLeaveEvent.getGroup().sendMessage(new MessageChainBuilder().append(String.valueOf(member.getId())).append("已经离开了我们，已经清除绑定的ID")
                        .build());
            }
        });
        return this;
    }
    public Command MemberJoin(){//玩家加入消息
        bot.getEventChannel().subscribeAlways(MemberJoinEvent.class, new Consumer<MemberJoinEvent>() {
            @Override
            public void accept(MemberJoinEvent memberJoinEvent) {
                memberJoinEvent.getGroup().sendMessage(new MessageChainBuilder()
                        .append(new At(memberJoinEvent.getMember().getId()))
                        .append(ConfigData.getWelcomeMessage())
                        .build());
            }
        });
        return this;
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
    public void start(){

    }
}
