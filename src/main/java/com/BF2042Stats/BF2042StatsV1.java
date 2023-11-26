package com.BF2042Stats;

import com.BF2042Stats.data.ConfigData;
import com.BF2042Stats.listen.Command;
import com.BF2042Stats.listen.ListenQqBot;
import net.mamoe.mirai.console.extension.PluginComponentStorage;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import org.jetbrains.annotations.NotNull;

public final class BF2042StatsV1 extends JavaPlugin {
    public static final BF2042StatsV1 INSTANCE = new BF2042StatsV1();
    private static JavaPlugin javaPlugin;

    private BF2042StatsV1() {
        super(new JvmPluginDescriptionBuilder("com.BF2042Stats.v1", "0.1.0")
                .name("BF2042StatsV1")
                .author("hhhh6448")
                .build());
    }

    @Override
    public void onLoad(@NotNull PluginComponentStorage $this$onLoad) {
        super.onLoad($this$onLoad);
        ConfigData configData = new ConfigData(this);
        javaPlugin = this;
    }

    @Override
    public void onEnable() {
        getLogger().info("Plugin loaded!");
        ListenQqBot listenQqBot = new ListenQqBot(this);
        listenQqBot.BotOnline();
        Command command = new Command();
        command.GroupMessage();
        command.MemberJoin();
//        command.joinGroupMessage();
    }
    public static JavaPlugin getJP(){
        return javaPlugin;
    }
}