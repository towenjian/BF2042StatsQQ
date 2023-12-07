package com.BF2042Stats.data;

import com.BF2042Stats.BF2042StatsV1;
import com.BF2042Stats.listen.Command;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.MessageReceipt;
import net.mamoe.mirai.message.data.*;

public class GroupMessage {
    private final long user;
    private MessageChain messages;
    private final Group group;
    private GroupMessageEvent event;
    private final Command command;
    private final JavaPlugin javaPlugin;
    private final Bot bot;
    private String[] s;
    private int days=7;
    private MessageReceipt<Group> receipt;
    private QuoteReply quoteReply;
    private Member member;

    /**
     * 指令实例化玩家数据
     * @param event 群聊消息时间
     * @param command commamd实例
     * @param bot 机器人实例
     * @param s 指令串
     */
    public GroupMessage(GroupMessageEvent event, Command command,Bot bot,String[] s) {
        this.s = s;
        this.bot = bot;
        this.event = event;
        this.group = event.getGroup();
        this.user = event.getSender().getId();
        this.messages = event.getMessage();
        this.command = command;
        this.javaPlugin = BF2042StatsV1.getJP();
        this.member = event.getSender();
    }

    /**
     * 群验证机器人自实例化数据
     * @param receipt 回复消息
     * @param command command实例
     * @param bot 机器人实例
     * @param s 指令串，需要自行构建
     * @param user 申请验证的消息发送者
     */
    public GroupMessage(MessageReceipt<Group> receipt, Command command, Bot bot, String[] s,long user){
        this.receipt = receipt;
        this.s = s;
        this.bot = bot;
        this.javaPlugin = BF2042StatsV1.getJP();
        this.command = command;
        this.group = receipt.getTarget();
        this.user = user;
        this.quoteReply = receipt.quote();
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

    /**
     * 发送消息,如果messageChain是null则会启用自回复，因为这种情况是加群验证模式无上下文
     * @param reply 需要回复的消息
     * @param <Reply> 泛型，仅允许String类型
     */
    public <Reply extends  String>void sendGroupMessage(Reply reply){
        if (messages!=null) group.sendMessage(new MessageChainBuilder()
                .append(new QuoteReply(messages))
                .append(reply)
                .build());
        else {
            group.sendMessage(new MessageChainBuilder()
                            .append(receipt.quote())
                            .append(reply)
                            .build());
        }
    }

    /**
     *
     * @return 获取Command实例
     */
    public Command getCommand() {
        return command;
    }

    /**
     *
     * @return 返回插件实例
     */
    public JavaPlugin getJavaPlugin() {
        return javaPlugin;
    }

    /**
     *
     * @return 返回机器人实例
     */
    public Bot getBot() {
        return bot;
    }

    /**
     *
     * @return 返回命令串
     */
    public String[] getS() {
        return s;
    }
    public void setS(String[] s){
        this.s = s;
    }

    /**
     * 改功能已经废除，是曾经获取玩家日常数据的天数变量
     * @return 返回天数
     */
    public int getDays() {
        return days;
    }
    public void setDays(int days) {
        if (days>=30) this.days=30;
        else this.days = Math.max(7, days);
    }
    public QuoteReply getQuoteReply(){
        return quoteReply;
    }

    public Member getMember() {
        return member;
    }
}
