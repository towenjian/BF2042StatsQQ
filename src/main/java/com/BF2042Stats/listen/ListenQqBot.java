package com.BF2042Stats.listen;

import com.BF2042Stats.BF2042StatsV1;
import com.BF2042Stats.data.ConfigData;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.events.BotOnlineEvent;
import net.mamoe.mirai.event.events.MemberJoinRequestEvent;
import net.mamoe.mirai.utils.MiraiLogger;

import java.util.function.Consumer;

public class ListenQqBot {
    private static final MiraiLogger logger = BF2042StatsV1.getJP().getLogger();
    private static Bot bot;
    private static final ListenQqBot LISTEN_QQ_BOT = new ListenQqBot();
    public static ListenQqBot getInstance(){
        return LISTEN_QQ_BOT;
    }
    private ListenQqBot() {
    }
    public void BotOnline(){
        GlobalEventChannel.INSTANCE.subscribeAlways(BotOnlineEvent.class, botOnlineEvent -> {
            Bot bot = botOnlineEvent.getBot();
            if (ConfigData.getQqBot()==bot.getId()){
                Command.setBot(bot);
                ListenQqBot.bot = bot;
                logger.info("bot已经上线");
                startCommandListen();
            }
        });
    }
    private void startCommandListen(){
        Command.getInstance()
                .GroupMessage()
                .exitGroup()
                .MemberJoin()
                .joinGroupMessage()
                .PrivateChat()
                .start();
    }
}
