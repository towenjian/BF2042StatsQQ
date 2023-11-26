package com.BF2042Stats.listen;

import com.BF2042Stats.data.ConfigData;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.events.BotOnlineEvent;
import net.mamoe.mirai.utils.MiraiLogger;

public class ListenQqBot {
    private final JavaPlugin javaPlugin;
    private final MiraiLogger logger;
    public ListenQqBot(JavaPlugin javaPlugin) {
        this.javaPlugin = javaPlugin;
        logger = javaPlugin.getLogger();
    }
    public void BotOnline(){
        GlobalEventChannel.INSTANCE.subscribeAlways(BotOnlineEvent.class, botOnlineEvent -> {
            Bot bot = botOnlineEvent.getBot();
            if (ConfigData.getQqBot()==bot.getId()){
                Command.setBot(bot);
                logger.info("bot已经上线");
            }
        });
    }
}
