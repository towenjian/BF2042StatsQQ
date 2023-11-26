package com.BF2042Stats.data;

import com.BF2042Stats.BF2042StatsV1;
import com.BF2042Stats.listen.Command;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.Event;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MemberJoinRequestEvent;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.*;

public class GroupMessage {
    private final long user;
    private MessageChain messages;
    private final Group group;
    private GroupMessageEvent event;
    private MemberJoinRequestEvent requestEvent;
    private final Command command;
    private final JavaPlugin javaPlugin;
    private final Bot bot;
    private String[] s;
    private int days=7;

    public GroupMessage(GroupMessageEvent event, Command command,Bot bot,String[] s) {
        this.s = s;
        this.bot = bot;
        this.event = event;
        this.group = event.getGroup();
        this.user = event.getSender().getId();
        this.messages = event.getMessage();
        this.command = command;
        this.javaPlugin = BF2042StatsV1.getJP();
    }
    public GroupMessage(MemberJoinRequestEvent event,Command command,Bot bot,String[] s){
        this.s = s;
        this.bot = bot;
        this.javaPlugin = BF2042StatsV1.getJP();
        this.command = command;
        this.requestEvent = event;
        this.group = event.getGroup();
        this.user = event.getFromId();
    }
    public Group getGroup() {
        return group;
    }
    public long getUser() {
        return user;
    }
    public MessageChain getMessages() {
        return messages;
    }
    public GroupMessageEvent getEvent() {
        return event;
    }
    public <Reply extends  String>void sendGroupMessage(Reply reply){
        if (messages!=null) group.sendMessage(new MessageChainBuilder()
                .append(new QuoteReply(messages))
                .append(reply)
                .build());
        else {
            group.sendMessage(new MessageChainBuilder()
                    .append(new At(Long.parseLong(ConfigData.getUser())))
                    .append(reply)
                    .build());
        }
    }
    public Command getCommand() {
        return command;
    }

    public JavaPlugin getJavaPlugin() {
        return javaPlugin;
    }

    public Bot getBot() {
        return bot;
    }

    public String[] getS() {
        return s;
    }
    public void setS(String[] s){
        this.s = s;
    }
    public int getDays() {
        return days;
    }
    public void setDays(int days) {
        if (days>=30) this.days=30;
        else this.days = Math.max(7, days);
    }
}
